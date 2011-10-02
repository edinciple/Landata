package lan.struct;

import lan.exceptions.IdNotExistException;

public abstract class LanObject implements ILanWorldSubstance {
	
	public abstract String getId();
	
	public LanWorld getOwner() {
		return this.getProvider().getOwner();
	}
	
	public abstract LanObjectProvider getProvider();
	
	public LanObjectProcessor getProcessor() throws IdNotExistException {
		return this.getOwner().getProcessor(this.getProcessorName());
	}

	public abstract String getProcessorName() throws IdNotExistException;
	
	public abstract void setProcessorName(String name);
	
	public Boolean Equal(Object obj) {
		if(obj instanceof LanObject && 
				((LanObject)obj).getId() == this.getId() &&
				((LanObject)obj).getProvider() == this.getProvider())
			return true;
		
		return false;
	}
}
