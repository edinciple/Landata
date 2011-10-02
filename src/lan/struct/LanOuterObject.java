package lan.struct;

import lan.exceptions.IdNotExistException;
import lan.helper.MongodbHelper;
import lan.helper.MongodbStrings;

public class LanOuterObject extends LanObject {
	public LanOuterObject(String id, LanWorld owner) {
		super(id, owner);
	}

	public String getMetadata() throws IdNotExistException {
		return (String)MongodbHelper.getAttribute(this.getOwner().getObjectDbCollection(), this.getId(), MongodbStrings.MetadataKey);
	}

	public void setMetadata(String metadata) {
		MongodbHelper.setAttribute(this.getOwner().getObjectDbCollection(), this.getId(), MongodbStrings.MetadataKey, metadata);
		
	}
	
	public Object getRealObject() throws IdNotExistException{
		return this.getProcessor().getRealObject(this);
	}
}
