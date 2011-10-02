package lan.struct;

import lan.exceptions.IdNotExistException;

public interface LanObjectProvider {
	//TODO find a more apt way
	
	public String[] getIdsByProcessor(String processor);
	public Boolean isExists(String id);
	public String createInnerObject();
	public String createOuterObject();
	public String[] getInnerObjectAttributeKeys(String id) throws IdNotExistException;
	public String getInnerObjectAttribute(String id, String key) throws IdNotExistException;
	public String getOuterObjectMetadata(String id) throws IdNotExistException;
	public String getObjectProcessor(String id) throws IdNotExistException;
	public void setObjectProcessor(String id, String processor) throws IdNotExistException;
	public Boolean isInnerObject(String id) throws IdNotExistException;
	public void setOuterObjectMetadata(String id, String metadata) throws IdNotExistException;
	public void setInnerObjectAttribute(String id, String key, String object_id) throws IdNotExistException;
}
