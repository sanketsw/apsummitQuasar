/**
 * 
 */
package au.com.ibm.webapp.utils;

import java.io.Serializable;
import java.util.List;

import au.com.ibm.webapp.scaffold.IMasterPersistentEntity;

/**
 * @author sanketsw
 *
 */

public class PageDetails implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5733599192903150611L;
	
	@SuppressWarnings("rawtypes")
	private IMasterPersistentEntity model;
	private String content;
	private List results;
	private String formClassName;
	
	public PageDetails() {
		super();
	}
	
	public PageDetails(String content, List results, String formClassName) {
		super();
		this.content = content;
		this.results = results;
		this.formClassName = formClassName;
	}

	@SuppressWarnings("rawtypes")
	public PageDetails(IMasterPersistentEntity model, String content, List results, String formClassName) {
		super();
		this.model = model;
		this.content = content;
		this.results = results;
		this.formClassName = formClassName;
	}

	@SuppressWarnings("rawtypes")
	public IMasterPersistentEntity getModel() {
		return model;
	}

	@SuppressWarnings("rawtypes")
	public void setModel(IMasterPersistentEntity model) {
		this.model = model;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List getResults() {
		return results;
	}

	public void setResults(List results) {
		this.results = results;
	}

	public String getFormClassName() {
		return formClassName;
	}

	public void setFormClassName(String formClassName) {
		this.formClassName = formClassName;
	}
	
	

}
