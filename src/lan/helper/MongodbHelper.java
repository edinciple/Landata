package lan.helper;

import lan.exceptions.IdNotExistException;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class MongodbHelper {
	public static void setAttribute(DBCollection collection, String id, String key, Object value) {
		collection.update(new BasicDBObject(MongodbStrings.IdKey, new ObjectId(id)), new BasicDBObject("$set", new BasicDBObject(key, value)));
	}
	
	public static Object getAttribute(DBCollection collection, String id, String key) throws IdNotExistException {
		DBObject dbobj = checkAndGetObjectById(collection, id);
		return dbobj.get(key);
	}
	
	public static DBObject getObjectById(DBCollection collection, String id) {
		BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put(MongodbStrings.IdKey, new ObjectId(id));
        DBCursor cursor = collection.find(searchQuery);
        if(cursor.hasNext())
        	return cursor.next();
        else
        	return null;
	}
	
	public static DBObject checkAndGetObjectById(DBCollection collection, String id) throws IdNotExistException {
		DBObject dbobj = getObjectById(collection, id);
		
		if(dbobj == null)
			throw new IdNotExistException(id);
		
		return dbobj;
	}

	public static Boolean isInnerObject(DBCollection collection, String id) throws IdNotExistException {
		DBObject dbobj = checkAndGetObjectById(collection, id);
		String type = (String) dbobj.get(MongodbStrings.TypeKey);
		if(type.equals(MongodbStrings.InnerTypeValue))
			return true;
		return false;
	}
	
	public static Boolean isOuterObject(DBCollection collection, String id) throws IdNotExistException {
		DBObject dbobj = checkAndGetObjectById(collection, id);
		String type = (String) dbobj.get(MongodbStrings.TypeKey);
		if(type.equals(MongodbStrings.OuterTypeValue))
			return true;
		return false;
	}
}
