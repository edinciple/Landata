package lan.struct;

public abstract class LanObjectProcessor extends LanWorldSubstance {
	public LanObjectProcessor(LanWorld owner) {
		super(owner);
	}
	
	public void onObjectCreated(LanObject object) { }
	public void onObjectRemoved(LanObject object) { }
	
	public abstract Object getRealObject(LanObject object);
}
