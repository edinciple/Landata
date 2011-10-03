package lan.test;

import java.util.Iterator;

import com.mongodb.BasicDBObject;

import lan.helper.MongodbStrings;
import lan.struct.LanObject;
import lan.struct.LanObjectProcessor;
import lan.struct.LanOuterObject;
import lan.struct.LanWorld;

public class IntegerObjectProcessor extends LanObjectProcessor {

	public IntegerObjectProcessor(LanWorld owner) {
		super(owner);
	}

	@Override
	public Object getRealObject(LanObject object) throws Exception {
		checkForOuterObject(object);
		//TODO Change the Exception type;
		if(((LanOuterObject)object).getMetadata() == null)
			((LanOuterObject)object).setMetadata("0");
		return Integer.parseInt(((LanOuterObject)object).getMetadata());
	}

	@Override
	public LanObject fromRealObject(Object object) throws Exception {
		if(!(object instanceof Integer))
			throw new Exception();
		//TODO Change the Exception type;
		
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put(MongodbStrings.TypeKey, MongodbStrings.OuterTypeValue);
		searchQuery.put(MongodbStrings.MetadataKey, object.toString());
		Iterator<LanObject> iterator = this.getOwner().find(searchQuery).iterator();
		while(iterator.hasNext()) {
			LanObject entry = iterator.next();
			if(entry.getProcessor() == this) {
				return entry;
			}
		}
		
		LanOuterObject obj = this.getOwner().createOuterObject();
		obj.setMetadata(object.toString());
		obj.setProcessor(this);
		return obj;
	}

}
