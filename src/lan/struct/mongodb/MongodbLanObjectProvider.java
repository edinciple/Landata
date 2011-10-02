package lan.struct.mongodb;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
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

public class MongodbLanObjectProvider extends LanObjectProvider {
	public class InnerObject extends LanInnerObject {
		MongodbLanObjectProvider provider;
		String id;
		
		public InnerObject(String id, MongodbLanObjectProvider provider) {
			this.id = id;
			this.provider = provider;
		}

		@Override
		public Set<String> getAttributeKeys() throws IdNotExistException {
			DBObject dbobj = checkAndGetObject(this.getId());
			
			Set<String> keys = dbobj.keySet();
			keys.remove(IdKey);
			keys.remove(TypeKey);
			keys.remove(ProcessorKey);
			
			return keys;
		}

		@Override
		public LanObject getAttribute(String key) throws IdNotExistException {
			return this.provider.get(this.provider.getAttribute(this.getId(), key).toString());
		}

		@Override
		public void setAttribute(String key, LanObject value) {
			this.provider.setAttribute(this.getId(), key, new ObjectId(value.getId()));
		}

		@Override
		public String getId() {
			return this.id;
		}

		@Override
		public LanObjectProvider getProvider() {
			return this.provider;
		}

		@Override
		public String getProcessorName() throws IdNotExistException {
			return this.provider.getAttribute(this.getId(), ProcessorKey).toString();
		}

		@Override
		public void setProcessorName(String name) {
			this.provider.setAttribute(this.getId(), ProcessorKey, name);
			
		}

	}
	
	public class OuterObject extends LanOuterObject {
		MongodbLanObjectProvider provider;
		String id;
		
		public OuterObject(String id, MongodbLanObjectProvider provider) {
			this.id = id;
			this.provider = provider;
		}

		@Override
		public String getId() {
			return this.id;
		}

		@Override
		public LanObjectProvider getProvider() {
			return this.provider;
		}

		@Override
		public String getMetadata() throws IdNotExistException {
			return this.provider.getAttribute(this.getId(), MetadataKey).toString();
		}

		@Override
		public void setMetadata(String metadata) {
			this.provider.setAttribute(this.getId(), MetadataKey, metadata);
			
		}

		@Override
		public String getProcessorName() throws IdNotExistException {
			return this.provider.getAttribute(this.getId(), ProcessorKey).toString();
		}

		@Override
		public void setProcessorName(String name) {
			this.provider.setAttribute(this.getId(), ProcessorKey, name);
			
		}
		
	}

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

	public MongodbLanObjectProvider(LanWorld owner) throws UnknownHostException, MongoException {
		super(owner);
		
		this.storedObjects = new HashMap<String, LanObject>();
		
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
	public LanInnerObject createInnerObject() throws IdNotExistException {
		DBObject innerObject = new BasicDBObject();
		innerObject.put(TypeKey, InnerTypeValue);
		innerObject.put(ProcessorKey, "");
		dbCollection.insert(innerObject);
		return (LanInnerObject)this.get(innerObject.get(IdKey).toString());
	}

	@Override
	public LanOuterObject createOuterObject() throws IdNotExistException {
		DBObject outerObject = new BasicDBObject();
		outerObject.put(TypeKey, OuterTypeValue);
		outerObject.put(ProcessorKey, "");
		outerObject.put(MetadataKey, "");
		dbCollection.insert(outerObject);
		return (LanOuterObject)this.get(outerObject.get(IdKey).toString());
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
	
	private void setAttribute(String id, String key, Object value) {
		this.dbCollection.update(new BasicDBObject(IdKey, new ObjectId(id)), new BasicDBObject("$set", new BasicDBObject(key, value)));
	}
	
	private Object getAttribute(String id, String key) throws IdNotExistException {
		DBObject dbobj = checkAndGetObject(id);
		return dbobj.get(key);
	}
	
	private Boolean isInnerObject(String id) throws IdNotExistException {
		DBObject dbobj = checkAndGetObject(id);
		String type = (String) dbobj.get(TypeKey);
		if(type.equals(InnerTypeValue))
			return true;
		return false;
	}
	
	HashMap<String, LanObject> storedObjects;
	private LanObject get(String id) throws IdNotExistException {
		if(!storedObjects.containsKey(id)) {
			LanObject obj;
			if(isInnerObject(id)) {
				obj = new InnerObject(id, this);
			} else {
				obj = new OuterObject(id, this);
			}
			storedObjects.put(id, obj);
		}
		return storedObjects.get(id);
	}

	@Override
	public Set<LanObject> all() throws IdNotExistException {
		HashSet<LanObject> objects = new HashSet<LanObject>();
		BasicDBObject searchQuery = new BasicDBObject();
        DBCursor cursor = this.dbCollection.find(searchQuery);
        while(cursor.hasNext())
        	objects.add(this.get(cursor.next().get(IdKey).toString()));
        return objects;
	}

	@Override
	public LanObject findById(String id) throws IdNotExistException {
		return this.get(id);
	}

	@Override
	public Set<LanObject> findByProcessor(String processorName) throws IdNotExistException {
		HashSet<LanObject> objects = new HashSet<LanObject>();
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put(ProcessorKey, processorName);
        DBCursor cursor = this.dbCollection.find(searchQuery);
        while(cursor.hasNext())
        	objects.add(this.get(cursor.next().get(IdKey).toString()));
        return objects;
	}
	
}
