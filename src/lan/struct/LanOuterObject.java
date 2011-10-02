package lan.struct;

import lan.exceptions.IdNotExistException;

public abstract class LanOuterObject extends LanObject {
	public abstract String getMetadata() throws IdNotExistException;

	public abstract void setMetadata(String metadata);
	
	public Object getRealObject() throws IdNotExistException{
		return this.getProcessor().getRealObject(this);
	}
}
