package lan.struct;

import lan.exceptions.IdNotExistException;

public class LanOuterObject extends LanObject {
	public LanOuterObject(String id, LanWorld world) throws Exception {
		super(id, world);
	}
	
	public String getMetadata() throws IdNotExistException {
		return this.getProvider().getOuterObjectMetadata(this.id);
	}

	public void setMetadata(String metadata) throws IdNotExistException {
		this.getProvider().setOuterObjectMetadata(id, metadata);
	}
	
	public Object getRealObject() throws Exception {
		return this.getProcessor().getRealObject(this);
	}
}
