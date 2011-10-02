package lan.struct.mongodb;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lan.struct.*;
import lan.exceptions.*;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class MongodbLanObjectProvider implements LanObjectProvider {

	String host;
	int port;
	String dbName;
	String collectionName;
	
	DB db;
	Mongo mongo;
	DBCollection dbCollection;
	
	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getDbName() {
		return dbName;
	}
	
	public String getCollectionName() {
		return this.collectionName;
	}

	public MongodbLanObjectProvider() throws UnknownHostException, MongoException {
		this.host = "127.0.0.1";
		this.port = 27017;
		this.dbName = "landata";
		this.collectionName = "objects";
		
		this.mongo = new Mongo(this.getHost(), this.getPort());
		this.db = mongo.getDB(this.getDbName());
		this.dbCollection = db.getCollection(this.getCollectionName());
		
		try {
			BasicDBObject searchQuery = new BasicDBObject();
			DBCursor cursor = this.dbCollection.find(searchQuery);
			while(cursor.hasNext()) {
				this.dbCollection.remove(cursor.next());
			}
		} catch(Exception e) {
			System.out.print(e.toString());
		}
	}
	
	public void close() {
		this.mongo.close();
	}
	
	final String IdKey = "_id";
	final String ProcessorKey = "_processor";
	final String TypeKey = "_type";
	final String InnerTypeValue = "inner";
	final String OuterTypeValue = "outer";
	final String MetadataKey = "_metadata";
	
	@Override
	public String[] getIdsByProcessor(String processor) {
		BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put(ProcessorKey, processor);
        DBCursor cursor = this.dbCollection.find(searchQuery);
        List<String> ids = new ArrayList<String>();
        while(cursor.hasNext()) {
        	ObjectId id = (ObjectId)cursor.next().get(IdKey);
        	ids.add(id.toString());
        }
        String[] ids_str = new String[ids.size()];
        return ids.toArray(ids_str);
	}

	@Override
	public Boolean isExists(String id) {
        if(getObjectById(id) != null)
        	return true;
        else
        	return false;
	}

	@Override
	public String createInnerObject() {
		DBObject innerObject = new BasicDBObject();
		innerObject.put(TypeKey, InnerTypeValue);
		innerObject.put(ProcessorKey, "");
		dbCollection.insert(innerObject);
		return innerObject.get(IdKey).toString();
	}

	@Override
	public String createOuterObject() {
		DBObject outerObject = new BasicDBObject();
		outerObject.put(TypeKey, OuterTypeValue);
		outerObject.put(ProcessorKey, "");
		outerObject.put(MetadataKey, "");
		dbCollection.insert(outerObject);
		return outerObject.get(IdKey).toString();
	}
	
	private DBObject getObjectById(String id) {
		BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put(IdKey, new ObjectId(id));
        DBCursor cursor = this.dbCollection.find(searchQuery);
        if(cursor.hasNext())
        	return cursor.next();
        else
        	return null;
	}
	
	private DBObject checkAndGetObject(String id) throws IdNotExistException {
		DBObject dbobj = getObjectById(id);
		
		if(dbobj == null)
			throw new IdNotExistException(id);
		
		return dbobj;
	}

	@Override
	public String[] getInnerObjectAttributeKeys(String id) throws IdNotExistException {
		DBObject dbobj = checkAndGetObject(id);
		
		Set<String> keys = dbobj.keySet();
		keys.remove(IdKey);
		keys.remove(TypeKey);
		keys.remove(ProcessorKey);
		
		String[] key_strs = new String[keys.size()];
		return keys.toArray(key_strs);
	}

	@Override
	public String getInnerObjectAttribute(String id, String key) throws IdNotExistException {
		DBObject dbobj = checkAndGetObject(id);
		return (String)dbobj.get(key);
	}

	@Override
	public String getOuterObjectMetadata(String id) throws IdNotExistException {
		DBObject dbobj = checkAndGetObject(id);
		return (String)dbobj.get(MetadataKey);
	}

	@Override
	public String getObjectProcessor(String id) throws IdNotExistException {
		DBObject dbobj = checkAndGetObject(id);
		return (String)dbobj.get(ProcessorKey);
	}
	
	private void setAttribute(String id, String key, String value) {
		this.dbCollection.update(new BasicDBObject(IdKey, new ObjectId(id)), new BasicDBObject("$set", new BasicDBObject(key, value)));
	}

	@Override
	public void setObjectProcessor(String id, String processor) throws IdNotExistException {
		// TODO Auto-generated method stub
		setAttribute(id, ProcessorKey, processor);
	}

	@Override
	public Boolean isInnerObject(String id) throws IdNotExistException {
		// TODO Auto-generated method stub
		DBObject dbobj = checkAndGetObject(id);
		
		String type = (String) dbobj.get(TypeKey);
		
		if(type.equals(InnerTypeValue))
			return true;
		
		return false;
	}

	@Override
	public void setOuterObjectMetadata(String id, String metadata) throws IdNotExistException {
		setAttribute(id, MetadataKey, metadata);
	}

	@Override
	public void setInnerObjectAttribute(String id, String key, String object_id) throws IdNotExistException {
		setAttribute(id, key, object_id);
	}
	
}
