package au.com.ibm.webapp.model.relational;

import java.io.Serializable;

public class Day implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5214088419325968954L;
	String id;



	/**
	 * @param id
	 */
	public Day(String id) {
		this.id = id;
	}
	
	

	/**
	 * 
	 */
	public Day() {
	}



	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}


	
	

}
