package lan.struct;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import lan.exceptions.IdNotExistException;
import lan.helper.MongodbHelper;
import lan.helper.MongodbStrings;
import lan.helper.StringHelper;

public class LanWorld {
	String _host;
	int _port;
	String _dbName;
	boolean _isTempWorld;
	
	DB _db;
	Mongo _mongo;
	DBCollection _objectDbCollection;
	
	public String getHost() {
		return _host;
	}

	public int getPort() {
		return _port;
	}

	public String getDbName() {
		return _dbName;
	}
	
	public boolean isTempWorld() {
		return _isTempWorld;
	}
	
	protected DB getDb() {
		return _db;
	}
	
	protected DBCollection getObjectDbCollection() {
		return _objectDbCollection;
	}
	
	public LanWorld() throws UnknownHostException, MongoException {
		this(StringHelper.randomString(32));
		this._isTempWorld = true;
	}
	
	public LanWorld(String dbName) throws UnknownHostException, MongoException {
		this("127.0.0.1", 27017, dbName);
	}
	
	public LanWorld(String host, int port) throws UnknownHostException, MongoException {
		this(host, port, StringHelper.randomString(32));
		this._isTempWorld = true;
	}
	
	public LanWorld(String host, int port, String dbName) throws UnknownHostException, MongoException {
		this.storedObjects = new HashMap<String, LanObject>();
		this._isTempWorld = false;
		
		this._host = host;
		this._port = port;
		this._dbName = dbName;
		
		this._mongo = new Mongo(this.getHost(), this.getPort());
		this._db = _mongo.getDB(this.getDbName());
		this._objectDbCollection = _db.getCollection(MongodbStrings.ObjectCollectionName);
	}
	
	public void close() {
		if(this.isTempWorld()) {
			this.getDb().dropDatabase();
		}
		this._mongo.close();
	}
	
	HashMap<String, LanObject> storedObjects;
	private LanObject get(String id) throws IdNotExistException {
		if(!storedObjects.containsKey(id)) {
			LanObject obj;
			if(MongodbHelper.isInnerObject(this.getObjectDbCollection(), id)) {
				obj = new LanInnerObject(id, this);
			} else {
				obj = new LanOuterObject(id, this);
			}
			storedObjects.put(id, obj);
		}
		return storedObjects.get(id);
	}
	
	HashMap<String, LanObjectProcessor> processors;	
	public LanObjectProcessor getProcessor(String name) {
		return processors.get(name);
	}
	
	public void removeProcessor(String name) {
		processors.remove(name);
	}
	
	public void putProcessor(String name, LanObjectProcessor processor) {
		if(processor == null)
			removeProcessor(name);
		else
			processors.put(name, processor);
	}

	public Set<LanObject> all() throws IdNotExistException {
		HashSet<LanObject> objects = new HashSet<LanObject>();
		BasicDBObject searchQuery = new BasicDBObject();
        DBCursor cursor = this.getObjectDbCollection().find(searchQuery);
        while(cursor.hasNext())
        	objects.add(this.get(cursor.next().get(MongodbStrings.IdKey).toString()));
        return objects;
	}

	public LanObject findById(String id) throws IdNotExistException {
		return this.get(id);
	}
	
	private LanObject createObject(String type) throws IdNotExistException {
		DBObject object = new BasicDBObject();
		object.put(MongodbStrings.TypeKey, type);
		this.getObjectDbCollection().insert(object);
		return (LanObject)this.get(object.get(MongodbStrings.IdKey).toString());
	}
	
	public LanInnerObject createInnerObject() throws IdNotExistException {
		return (LanInnerObject)createObject(MongodbStrings.InnerTypeValue);
	}

	public LanOuterObject createOuterObject() throws IdNotExistException {
		return (LanOuterObject)createObject(MongodbStrings.OuterTypeValue);
	}

	public Set<LanObject> findByProcessor(String processorName) throws IdNotExistException {
		HashSet<LanObject> objects = new HashSet<LanObject>();
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put(MongodbStrings.ProcessorKey, processorName);
        DBCursor cursor = this.getObjectDbCollection().find(searchQuery);
        while(cursor.hasNext())
        	objects.add(this.get(cursor.next().get(MongodbStrings.IdKey).toString()));
        return objects;
	}
}
