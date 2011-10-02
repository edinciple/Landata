package lan.struct;

import java.util.Set;

import lan.exceptions.IdNotExistException;

public abstract class LanObjectProvider extends LanWorldSubstance {
	public LanObjectProvider(LanWorld owner) {
		super(owner);
	}
	
	public abstract Set<LanObject> all() throws IdNotExistException;
	public abstract LanObject findById(String id) throws IdNotExistException;
	public abstract Set<LanObject> findByProcessor(String processorName) throws IdNotExistException;
	
	public abstract LanInnerObject createInnerObject() throws IdNotExistException;
	public abstract LanOuterObject createOuterObject() throws IdNotExistException;
}
