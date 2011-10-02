package lan.struct;

public abstract class LanObject {
	String id;
	LanWorld owner;

	public String getId() throws Exception {
		checkUsable();
		return this.id;
	}
	
	public LanObject(String id, LanWorld owner) throws Exception {
		this.id = id;
		this.owner = owner;
		checkUsable();
	}
	
	public LanObjectProvider getProvider() {
		return this.getOwner().getProvider();
	}
	
	public LanObjectProcessor getProcessor() throws Exception {
		return this.getOwner().getProcessor(this.getProcessorString());
	}

	public Boolean isUsable() {
		if(!owner.getProvider().isExists(this.id))
			return false;
		
		return true;
	}
	
	private void checkUsable() throws Exception {
		if(!isUsable()) {
			throw new Exception();
			//TODO Change the type of the exception
		}
	}
	
	public LanWorld getOwner() {
		return owner;
	}

	public String getProcessorString() throws Exception {
		checkUsable();
		return owner.getProvider().getObjectProcessor(id);
	}
	
	public void setProcessorString(String provider) throws Exception {
		checkUsable();
		owner.getProvider().setObjectProcessor(id, provider);
	}
	
	public Boolean Equal(Object obj) {
		if(obj instanceof LanObject && 
				((LanObject)obj).id == this.id &&
				((LanObject)obj).owner == this.owner)
			return true;
		
		return false;
	}
}
