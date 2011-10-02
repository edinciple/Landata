package lan.struct;

public abstract class LanWorldSubstance implements ILanWorldSubstance{
	public LanWorldSubstance(LanWorld owner) {
		this.owner = owner;
	}
	
	LanWorld owner;
	
	public LanWorld getOwner() {
		return this.owner;
	}
}
