package au.com.ibm.webapp.web.forms;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import au.com.ibm.webapp.model.relational.Message;
import au.com.ibm.webapp.model.relational.Session;
import au.com.ibm.webapp.service.IMessageSvc;

@ManagedBean
@ViewScoped
public class AnnouncementBean {

	private List<Message> messages;
	private List<Message> activity;

	@EJB
	private IMessageSvc messageSvc;

	/**
	 * @return the activity
	 */
	public List<Message> getActivity() {
		return activity;
	}

	/**
	 * @param activity
	 *            the activity to set
	 */
	public void setActivity(List<Message> activity) {
		this.activity = activity;
	}

	/**
	 * @return the messages
	 */
	public List<Message> getMessages() {
		return messages;
	}

	/**
	 * @param messages
	 *            the messages to set
	 */
	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	@PostConstruct
	public void Init() {
		messages = messageSvc.getByEntityId(Message.EntityIdGlobal);
		activity = messageSvc.getRecentActivity();
	}

	public String getEntityName(String entityId) {
		Session s = null;
		try {
			s = ScheduleManager.getInstance().getG_sessionMap().get(entityId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (s == null) {
			return null;
		}
		return " " + (s.getTitle() == null ? s.getId() : s.getTitle()) + " ";
	}
	
	public void removeMessage(Message m) {
		messageSvc.delete(m);
		activity = messageSvc.getRecentActivity();
	}

}
