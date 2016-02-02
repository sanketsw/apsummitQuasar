package au.com.ibm.webapp.dao;

import org.springframework.data.repository.NoRepositoryBean;

import au.com.ibm.webapp.dao.relational.repository.MessageRepository;
import au.com.ibm.webapp.model.relational.Message;
import au.com.ibm.webapp.scaffold.IDao;

@NoRepositoryBean
public interface IMessageDao extends IDao<Message, String, MessageRepository> , MessageRepository {
	
	
}
