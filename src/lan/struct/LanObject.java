package lan.struct;

import lan.exceptions.IdNotExistException;
import lan.helper.MongodbHelper;
import lan.helper.MongodbStrings;

public abstract class LanObject extends LanWorldSubstance {
	String id;
	
	public LanObject(String id, LanWorld owner) {
		super(owner);
		this.id = id;
	}

	public String getId() {
		return this.id;
	}
	
	public void setProcessor(LanObjectProcessor processor) {
		setProcessorName(this.getOwner().getProcessorName(processor));
	}
	
	public LanObjectProcessor getProcessor() throws IdNotExistException {
		return this.getOwner().getProcessor(this.getProcessorName());
	}

	public String getProcessorName() throws IdNotExistException {
		return MongodbHelper.getAttribute(this.getOwner().getObjectDbCollection(), this.getId(), MongodbStrings.ProcessorKey).toString();
	}

	public void setProcessorName(String name) {
		MongodbHelper.setAttribute(this.getOwner().getObjectDbCollection(), this.getId(), MongodbStrings.ProcessorKey, name);
		
	}
	
	public Object getRealObject() throws Exception{
		return this.getProcessor().getRealObject(this);
	}
	
	public Boolean Equal(Object obj) {
		if(obj instanceof LanObject && 
				((LanObject)obj).getId() == this.getId() &&
				((LanObject)obj).getOwner() == this.getOwner())
			return true;
		
		return false;
	}
}
