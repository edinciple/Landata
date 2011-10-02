package lan.struct;

import java.util.Set;

import lan.exceptions.IdNotExistException;

public abstract class LanInnerObject extends LanObject {
	
	public abstract Set<String> getAttributeKeys() throws IdNotExistException;
	
	public abstract LanObject getAttribute(String key) throws IdNotExistException;
	
	public abstract void setAttribute(String key, LanObject value);
	
	public Object getRealObject() throws IdNotExistException{
		return this.getProcessor().getRealObject(this);
	}
}
