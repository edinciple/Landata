package lan.test;

import java.util.Iterator;

import com.mongodb.BasicDBObject;
import lan.helper.MongodbStrings;
import lan.struct.LanObject;
import lan.struct.LanObjectProcessor;
import lan.struct.LanOuterObject;
import lan.struct.LanWorld;

public class StringObjectProcessor extends LanObjectProcessor {

	public StringObjectProcessor(LanWorld owner) {
		super(owner);
	}

	@Override
	public Object getRealObject(LanObject object) throws Exception {
		checkForOuterObject(object);
		//TODO Change the Exception type;
		
		return ((LanOuterObject)object).getMetadata();
	}

	@Override
	public LanObject fromRealObject(Object object) throws Exception {
		if(!(object instanceof String))
			throw new Exception();
		//TODO Change the Exception type;
		
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put(MongodbStrings.TypeKey, MongodbStrings.OuterTypeValue);
		searchQuery.put(MongodbStrings.MetadataKey, (String)object);
		Iterator<LanObject> iterator = this.getOwner().find(searchQuery).iterator();
		while(iterator.hasNext()) {
			LanObject entry = iterator.next();
			if(entry.getProcessor() == this) {
				return entry;
			}
		}
		
		LanOuterObject obj = this.getOwner().createOuterObject();
		obj.setMetadata((String)object);
		obj.setProcessor(this);
		return obj;
	}

}
