package au.com.ibm.webapp.model.relational;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;

/**
 * @author sanketsw
 *
 */

public class Session implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7630564358751736254L;

	private String id;
	private List<Schedule> times;
	private String title;
	private String description;
	private String presenter;
	private String exec;
	private boolean channel;
	private String parent;
	private String URL;
	
	

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return id + ":" + title + ":" + channel + ":" + parent;
	}

	/*-- Getters and Setters --*/

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Schedule> getTimes() {
		return times;
	}

	public void setTimes(List<Schedule> times) {
		this.times = times;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPresenter() {
		return presenter;
	}

	public void setPresenter(String presenter) {
		this.presenter = presenter;
	}

	public String getExec() {
		return exec;
	}

	public void setExec(String exec) {
		this.exec = exec;
	}

	public boolean isChannel() {
		return channel;
	}

	public void setChannel(boolean channel) {
		this.channel = channel;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public static String getSessionId(String title, String channelNumber) {
		String id = title + (channelNumber != null ? "_ch" + channelNumber : "");
		return id;
	}

	public void add(Schedule t) {
		if (times == null) {
			times = new ArrayList<>();
		}
		times.add(t);
	}

	public Schedule getSchedule(String day, String target) {
		for (Schedule t : times) {
			if (t.getDay().equalsIgnoreCase(day) && (target == null || target.isEmpty() || t.getTarget() == null
					|| t.getTarget().equalsIgnoreCase(target))) {
				return t;
			}
		}
		return null;
	}
	
	public Schedule getSchedule(String day) {
		for (Schedule t : times) {
			if (t.getDay().equalsIgnoreCase(day)) {
				return t;
			}
		}
		return null;
	}

}
