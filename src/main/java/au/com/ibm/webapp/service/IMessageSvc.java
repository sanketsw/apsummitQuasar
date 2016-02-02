package au.com.ibm.webapp.service;

import java.util.List;

import javax.ejb.Local;

import au.com.ibm.webapp.dao.IMessageDao;
import au.com.ibm.webapp.dao.relational.repository.MessageRepository;
import au.com.ibm.webapp.model.relational.Message;
import au.com.ibm.webapp.scaffold.IService;

@Local
public interface IMessageSvc extends IService<Message, String, MessageRepository, IMessageDao> {

	List<Message> getByEntityId(String entityId);

	List<Message> getRecentActivity();



}
