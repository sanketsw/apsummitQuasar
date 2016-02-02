package au.com.ibm.webapp.web.forms;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;


@ManagedBean
public class NavigationBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1900301179864465448L;
	public static final String dashboard = "/ui/dashboard.xhtml?faces-redirect=true";
	public static final String Login = "/login.xhtml?faces-redirect=true";
	

	public String showSession(String sessionId) {
		System.out.println("Opening the session details");
		return "/ui/session.xhtml?faces-redirect=true&entityId=" + sessionId;
	}
	
	public String showDashboard() {
		System.out.println("Opening the schedule details");
		return dashboard;
	}
}
