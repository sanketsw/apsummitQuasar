package au.com.ibm.webapp.service.impl.utils;

public class EntityCount {

	private long count;
	
	private String entity;
	
	public EntityCount(String entity,long count) {
		super();
		this.count = count;
		this.entity = entity;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}
}
