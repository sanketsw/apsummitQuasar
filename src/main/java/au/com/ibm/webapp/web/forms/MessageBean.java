package au.com.ibm.webapp.web.forms;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import au.com.ibm.webapp.model.relational.AppUser;
import au.com.ibm.webapp.model.relational.Message;
import au.com.ibm.webapp.model.relational.Presenter;
import au.com.ibm.webapp.model.relational.Session;
import au.com.ibm.webapp.service.IMessageSvc;
import au.com.ibm.webapp.service.IUserSvc;
import au.com.ibm.webapp.service.impl.UserSvc;

@ManagedBean
@ViewScoped
public class MessageBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2612128045044508166L;

	private String currentMessage;

	private String messageType;

	@EJB
	private IMessageSvc messageSvc;
	
	@EJB
	private IUserSvc userSvc;

	private List<Message> messages;
	private List<Message> questions;
	private List<Message> comments;

	private boolean formEnabled;

	private String activeIndexQuestions;

	private String entityId;

	private String prevSession;

	private String nextSession;

	private Session currentSession;

	/**
	 * @return the prevSession
	 */
	public String getPrevSession() {
		return prevSession;
	}

	/**
	 * @param prevSession
	 *            the prevSession to set
	 */
	public void setPrevSession(String prevSession) {
		this.prevSession = prevSession;
	}

	/**
	 * @return the nextSession
	 */
	public String getNextSession() {
		return nextSession;
	}

	/**
	 * @param nextSession
	 *            the nextSession to set
	 */
	public void setNextSession(String nextSession) {
		this.nextSession = nextSession;
	}

	/**
	 * @return the currentSession
	 */
	public Session getCurrentSession() {
		return currentSession;
	}

	/**
	 * @param currentSession
	 *            the currentSession to set
	 */
	public void setCurrentSession(Session currentSession) {
		this.currentSession = currentSession;
	}

	/**
	 * @return the entityId
	 */
	public String getEntityId() {
		return entityId;
	}

	/**
	 * @param entityId
	 *            the entityId to set
	 */
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	/**
	 * @return the messageType
	 */
	public String getMessageType() {
		return messageType;
	}

	/**
	 * @param messageType
	 *            the messageType to set
	 */
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	/**
	 * @return the activeIndexQuestions
	 */
	public String getActiveIndexQuestions() {
		return activeIndexQuestions;
	}

	/**
	 * @param activeIndexQuestions
	 *            the activeIndexQuestions to set
	 */
	public void setActiveIndexQuestions(String activeIndexQuestions) {
		this.activeIndexQuestions = activeIndexQuestions;
	}

	/**
	 * @return the formEnabled
	 */
	public boolean isFormEnabled() {
		return formEnabled;
	}

	/**
	 * @param formEnabled
	 *            the formEnabled to set
	 */
	public void setFormEnabled(boolean formEnabled) {
		this.formEnabled = formEnabled;
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

	/**
	 * @return the questions
	 */
	public List<Message> getQuestions() {
		return questions;
	}

	/**
	 * @param questions
	 *            the questions to set
	 */
	public void setQuestions(List<Message> questions) {
		this.questions = questions;
	}

	/**
	 * @return the comments
	 */
	public List<Message> getComments() {
		return comments;
	}

	/**
	 * @param comments
	 *            the comments to set
	 */
	public void setComments(List<Message> comments) {
		this.comments = comments;
	}

	/**
	 * @return the currentMessage
	 */
	public String getCurrentMessage() {
		return currentMessage;
	}

	/**
	 * @param currentMessage
	 *            the currentMessage to set
	 */
	public void setCurrentMessage(String currentMessage) {
		this.currentMessage = currentMessage;
	}

	public void saveMessage() {
		if (entityId == null) {
			// this is global announcement
			entityId = Message.EntityIdGlobal;
			messageType = Message.ANNOUNCEMENT;
		}
		Message m = new Message();
		m.setMessage(currentMessage);
		m.setDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
		m.setEntityId(entityId);
		System.out.println("saveMessage:" + entityId);
		m.setType(messageType);
		Authentication authToken = SecurityContextHolder.getContext().getAuthentication();
		// printUser(authToken);
		if (authToken != null) {
			m.setUserId(authToken.getName());
		}
		messageSvc.update(m);
		refreshMessages(entityId);

		currentMessage = new String();
		if (messageType.equals(Message.QUESTION))
			setActiveIndexQuestions("0");
		else
			setActiveIndexQuestions(null);

		setFormEnabled(false);
	}

	public void removeMessage(Message m) {
		System.out.println("removeMessage");
		System.out.println(m);
		String type = m.getType();
		String entityId = m.getEntityId();
		messageSvc.delete(m);
		refreshMessages(entityId);
		if (type.equals(Message.QUESTION))
			setActiveIndexQuestions("0");
		else
			setActiveIndexQuestions(null);
	}

	public void askQuestion() {
		setFormEnabled(true);
		setMessageType(Message.QUESTION);
	}

	public void postComment() {
		setFormEnabled(true);
		setMessageType(Message.COMMENT);
	}

	@PostConstruct
	public void init() {
		loadSessionDetails();
	}

	private void loadSessionDetails() {
		Authentication authToken = SecurityContextHolder.getContext().getAuthentication();
		setFormEnabled(false);
		setActiveIndexQuestions(null);
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		entityId = params.get("entityId");
		updateSessionDetails(entityId);
		refreshMessages(entityId);
		System.out.println("loadSessionDetails:" + entityId);
	}

	public String getDisplayStyle() {
		if (isFormEnabled())
			return "display:block;";
		else
			return "display:none;";
	}

	private void refreshMessages(String entityId) {
		if (entityId == null) {
			// this is global announcement page
			messages = messageSvc.getByEntityId(Message.EntityIdGlobal);
		} else {
			messages = messageSvc.getByEntityId(entityId);
		}

		if (messages != null) {
			questions = new ArrayList<Message>();
			comments = new ArrayList<Message>();
			for (Message m : messages) {
				if (m.getType() == null)
					continue;
				if (m.getType().equals(Message.QUESTION)) {
					questions.add(m);
				} else {
					comments.add(m);
				}
			}
		}
	}

	public void updateSessionDetails(String entityId) {
		Session cur = null;
		try {
			cur = ScheduleManager.getInstance().getG_sessionMap().get(entityId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		currentSession = cur;
		// if(cur != null) {
		// List<Session> l = null;
		// if(! cur.isChannel() && cur.getParent() != null ) {
		// this is a session under a channel
		// l =
		// ScheduleManager.getInstance().getChannelSesionsMap().get(cur.getParent());
		// } else {
		// this is a top level session
		// List<Session> l =
		// ScheduleManager.getInstance().getG_dayMap().get(cur.getDay());
		// //}
		// System.out.println(l);
		// Session current=null, next=null, prev=null;
		// for (Session s : l) {
		// if (current == null && s.getId().equals(entityId)) {
		// current = s;
		// } else if (current == null) {
		// prev = s;
		// } else {
		// next = s;
		// break;
		// }
		// }
		// if (current != null) {
		// prevSession = prev != null ? prev.getId() : null;
		// nextSession = next != null ? next.getId() : null;
		// }
		// }
	}
	
	public List<Presenter> getPresneters(String commaString) {
		List<Presenter> r = new ArrayList<>();
		String[] presneterNames = commaString.replace('+', ',').split(",");
		for(String name: presneterNames) {
			Presenter p = null;
			try {
				p = ScheduleManager.getInstance().getG_presenterMap().get(name.trim());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(p!= null) {
				r.add(p);
			} else {
				p = new Presenter();
				p.setName(name.trim());
				r.add(p);
			}
		}
		return r;
	}
	
	public String getEmail(String userId) {
		AppUser user = userSvc.getById(userId);
		System.out.println(user);
		if(user != null) {
			return user.getEmail();
		}
		return null;
	}

}
