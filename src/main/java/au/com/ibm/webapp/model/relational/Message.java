package au.com.ibm.webapp.model.relational;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.ocpsoft.prettytime.PrettyTime;

import au.com.ibm.webapp.scaffold.AbstractMasterEntity;


@Entity
public class Message extends AbstractMasterEntity<String> {
	
	public static final String QUESTION = "Question";
	public static final String COMMENT = "Comment/Feedback";
	public static final String ANNOUNCEMENT = "Announcement";
	
	public static final String EntityIdGlobal = "Global";
	

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	private String message;
	
	private String type;
		
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	private String userId;
	
	private String entityId;
	
	@Version
	private int version;
	
	public static Comparator<Message> comparator = new Comparator<Message>() {
        @Override
        public int compare(final Message object1, final Message object2) {
            return object2.getDate().compareTo(object1.getDate());
        }
       };
	

	/* (non-Javadoc)
	 * @see au.com.ibm.cdis.scaffold.IMasterPersistentEntity#getId()
	 */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}



	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String getSearchResultInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return userId + " on " + entityId + " on  " + date + " wrote: " + message;
	}
	
	//Return formatted start date
	public String getFormattedDate()	{
		PrettyTime p = new PrettyTime();
		return p.format(getDate());
		//String result = new SimpleDateFormat("dd-MMM-yy HH:mm").format(getDate());
		//return result;
	}
	
	
	
}
