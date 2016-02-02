package au.com.ibm.webapp.scaffold;

import java.io.Serializable;

import javax.faces.bean.ManagedProperty;

import au.com.ibm.webapp.web.forms.SessionModel;

public class AbstractForm<T extends IMasterPersistentEntity> implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 9189493095792576953L;
	/**
     * Holds the current entity that is being edited.
     */
    @ManagedProperty("#{sessionModel}")
    private SessionModel<T> sessionModel;
 
    public SessionModel<T> getSessionModel() {
		return sessionModel;
	}
    
    public void setSessionModel(SessionModel<T> sessionModel) {
		this.sessionModel = sessionModel;
	}
    
}
