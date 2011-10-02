package lan.struct;

import java.util.HashMap;
import java.util.Set;

import lan.exceptions.IdNotExistException;

public class LanWorld {
	
	LanObjectProvider provider;
	HashMap<String, LanObjectProcessor> processors;

	public LanObjectProvider getProvider() {
		return provider;
	}
	
	public void setProvider(LanObjectProvider provider) {
		this.provider = provider;
	}
	
	public LanObjectProcessor getProcessor(String name) {
		return processors.get(name);
	}
	
	public void removeProcessor(String name) {
		processors.remove(name);
	}
	
	public void putProcessor(String name, LanObjectProcessor processor) {
		processors.put(name, processor);
	}
	
	public LanObject findById(String id) throws IdNotExistException {
		return this.getProvider().findById(id);
	}
	
	public Set<LanObject> findByProcessor(String processor) throws IdNotExistException {
		return this.getProvider().findByProcessor(processor);
	}
	
	public LanInnerObject createInnerObject() throws Exception {
		return this.getProvider().createInnerObject();
	}
	
	public LanOuterObject createOuterObject() throws Exception {
		return this.getProvider().createOuterObject();
	}
}
