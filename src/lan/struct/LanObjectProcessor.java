package lan.struct;

public interface LanObjectProcessor {
	Object getRealObject(LanObject object);
	void setOwner(LanWorld world);
}
