package lan.struct;

public class LanInnerObject extends LanObject {
	
	public LanInnerObject(String id, LanWorld owner) throws Exception {
		super(id, owner);
	}
	
	public String[] getAttributeKeys() throws Exception {
		return this.getProvider().getInnerObjectAttributeKeys(this.id);
	}
	
	public LanObject getAttribute(String key) throws Exception {
		String id = this.getProvider().getInnerObjectAttribute(this.getId(), key);
		return this.getOwner().getObject(id);
	}
	
	public Boolean isSameObject(LanObject object) {
		if(object.getOwner() == this.getOwner())
			return true;
		
		return false;
	}
	
	private void checkSameObject(LanObject object) throws Exception {
		if(!isSameObject(object))
			throw new Exception();
		
		//TODO change the exception type
	}
	
	public void setAttribute(String key, LanObject value) throws Exception {
		checkSameObject(value);
		this.getProvider().setInnerObjectAttribute(this.getId(), key, value.getId());
	}
	
	public Object getRealObject() throws Exception {
		return this.getProcessor().getRealObject(this);
	}
}
