package lan.struct;

import java.util.HashMap;

public class LanWorld {
	public LanWorld(LanObjectProvider provider) {
		this.provider = provider;
	}
	
	LanObjectProvider provider;
	HashMap<String, LanObjectProcessor> processors;

	public LanObjectProvider getProvider() {
		return provider;
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
	
	public LanObject getObject(String id) throws Exception {
		if(this.getProvider().isInnerObject(id)) {
			return new LanInnerObject(id, this);
		} else {
			return new LanOuterObject(id, this);
		}
	}
	
	public LanObject[] getObjectsByProcessor(String processor) throws Exception {
		String[] ids = this.getProvider().getIdsByProcessor(processor);
		LanObject[] objects = new LanObject[ids.length];
		for(int i = 0; i < ids.length ; i++) {
			objects[i] = this.getObject(ids[i]);
		}
		return objects;
	}
	
	public LanInnerObject createInnerObject() throws Exception {
		return new LanInnerObject(this.getProvider().createInnerObject(), this);
	}
	
	public LanOuterObject createOuterObject() throws Exception {
		return new LanOuterObject(this.getProvider().createOuterObject(), this);
	}
}
