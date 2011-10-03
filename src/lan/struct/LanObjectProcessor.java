package lan.struct;

public abstract class LanObjectProcessor extends LanWorldSubstance {
	public LanObjectProcessor(LanWorld owner) {
		super(owner);
	}
	
	public void onObjectCreated(LanObject object) { }
	public void onObjectRemoved(LanObject object) { }
	
	public abstract Object getRealObject(LanObject object) throws Exception;
	public abstract LanObject fromRealObject(Object object) throws Exception;
	
	protected void checkForOuterObject(LanObject object) throws Exception {
		if(!(object instanceof LanOuterObject) || this != object.getProcessor())
			throw new Exception();
	}
	
	protected void checkForInnerObject(LanObject object) throws Exception {
		if(!(object instanceof LanInnerObject) || this != object.getProcessor())
			throw new Exception();
	}
}
