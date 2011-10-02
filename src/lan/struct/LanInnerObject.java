package lan.struct;

import java.util.Set;

import org.bson.types.ObjectId;

import com.mongodb.DBObject;

import lan.exceptions.IdNotExistException;
import lan.helper.MongodbHelper;
import lan.helper.MongodbStrings;

public class LanInnerObject extends LanObject {
	
	public LanInnerObject(String id, LanWorld owner) {
		super(id, owner);
	}

	public Set<String> getAttributeKeys() throws IdNotExistException {
		DBObject dbobj = MongodbHelper.checkAndGetObjectById(this.getOwner().getObjectDbCollection(), this.getId());
		
		Set<String> keys = dbobj.keySet();
		keys.remove(MongodbStrings.IdKey);
		keys.remove(MongodbStrings.TypeKey);
		keys.remove(MongodbStrings.ProcessorKey);
		
		return keys;
	}
	
	public LanObject getAttribute(String key) throws IdNotExistException {
		return this.getOwner().findById(
				MongodbHelper.getAttribute(this.getOwner().getObjectDbCollection(), this.getId(), key).toString());
	}
	
	public void setAttribute(String key, LanObject value) {
		MongodbHelper.setAttribute(this.getOwner().getObjectDbCollection(), this.getId(), key, new ObjectId(value.getId()));
	}
	
	public Object getRealObject() throws IdNotExistException{
		return this.getProcessor().getRealObject(this);
	}
}
