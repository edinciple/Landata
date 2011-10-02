package lan.exceptions;

public class IdNotExistException extends LanException {
	String id;

	public String getId() {
		return id;
	}
	
	public IdNotExistException(String id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return id;
	}
}
