package au.com.ibm.webapp.web.forms;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import au.com.ibm.webapp.config.ConfigUtil;
import au.com.ibm.webapp.scaffold.AbstractMaintenanceForm;
import au.com.ibm.webapp.scaffold.IMasterPersistentEntity;
import au.com.ibm.webapp.utils.PageDetails;

@SuppressWarnings("rawtypes")
@ManagedBean
@SessionScoped
public class SessionModel<T extends IMasterPersistentEntity> implements Serializable {

	private static final long serialVersionUID = -8287845504096436779L;

	private String content = "/dashboard.xhtml";

	private Mode mode;

	private T model;

	private List<T> results;

	/**
	 * Stack of the history of the pages accessed
	 */
	private Stack<PageDetails> operations;

	@PostConstruct
	public void init() {
		operations = new Stack<PageDetails>();
	}

	/**
	 * Add the current page to the stack
	 * Called from the NavigationBean
	 * @param page
	 */
	public void addPage(PageDetails page) {
		page.setFormClassName(null);
		operations.push(page);
	}

	/**
	 * Add the current page to the stack
	 * Called from the AbstractMaintenanceForm
	 * @param page
	 * @param form Not being used currently
	 */
	public void addPage(PageDetails page, AbstractMaintenanceForm form) {
//		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//		Map<String, Object> sessionMap = externalContext.getSessionMap();
//		sessionMap.put(page.getFormClassName(), form);
		operations.push(page);
	}
	
	/**
	 * Get a peek at the current page. Does not pop from the stack
	 * @return previous page details
	 */
	public PageDetails viewCurrentPage() {
		if (operations.size() > 0) {
			// This is just the current page
			return operations.peek();
		} else {
			return null;
		}
	}

	/**
	 * Pop the previous page from the stack
	 * @return previous page details
	 */
	public PageDetails removePreviousPage() {
		if (operations.size() > 1) {
			// This is just the current page. We are interested in the previous to this.
			operations.pop();
			return operations.pop();
		} else {
			return null;
		}
	}

	
	
	/**
	 * Not being used
	 * @deprecated
	 * @param entity
	 */
	public void updateModelInSessionStack(T entity) {
		PageDetails peek = viewCurrentPage();
		if (peek!=null && peek.getModel()!= null  && peek.getModel().getId().equals(entity.getId())) {
			//System.out.println("Saving to" + peek.getModel().getId());
			peek.setModel(entity);
		} else {
			//System.out.println("Got wrong form instead" + peek);
		}
	}

	public Stack<PageDetails> getOperations() {
		return operations;
	}

	public void setOperations(Stack<PageDetails> operations) {
		this.operations = operations;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public boolean isNewMode() {
		return mode == Mode.NEW ? true : false;
	}

	public boolean isEditMode() {
		return mode == Mode.EDIT ? true : false;
	}

	public T getModel() {
		return model;
	}

	public void setModel(T pModel) {
		this.model = pModel;
		//updateModelInSessionStack(pModel);
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

	public void reset() {
		content = "/dashboard.xhtml";
		init();
	}
}
