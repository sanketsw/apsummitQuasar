package au.com.ibm.webapp.web.forms;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class SessionBean {
	private String daysActiveIndex;
	
	private String sessionsActiveIndex;
	
	private String sessionsInChannelActiveIndex;

	public String getDaysActiveIndex() {
		return daysActiveIndex;
	}

	public void setDaysActiveIndex(String daysActiveIndex) {
		this.daysActiveIndex = daysActiveIndex;
	}

	public String getSessionsActiveIndex() {
		return sessionsActiveIndex;
	}

	public void setSessionsActiveIndex(String sessionsActiveIndex) {
		this.sessionsActiveIndex = sessionsActiveIndex;
	}

	public String getSessionsInChannelActiveIndex() {
		return sessionsInChannelActiveIndex;
	}

	public void setSessionsInChannelActiveIndex(String sessionsInChannelActiveIndex) {
		this.sessionsInChannelActiveIndex = sessionsInChannelActiveIndex;
	}
	
	
	
	public void onDaysTabChange() {
		System.out.println("onDaysTabChange");
		setSessionsActiveIndex(null);		
	}
	
	public void onSessionsTabChange() {
		System.out.println("onSessionsTabChange");
		setSessionsInChannelActiveIndex(null);
	}

	
}
