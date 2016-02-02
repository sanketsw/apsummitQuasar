package au.com.ibm.webapp.dao.relational;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import au.com.ibm.webapp.dao.Dao;
import au.com.ibm.webapp.dao.IMessageDao;
import au.com.ibm.webapp.dao.relational.repository.MessageRepository;
import au.com.ibm.webapp.model.relational.Message;

@Dao
public class MessageDao extends AbstractDao<Message, String, MessageRepository> implements IMessageDao {

	@Override
	@PostConstruct
	public void init() {
		JpaRepositoryFactory factory = new JpaRepositoryFactory(getEntityManager());
		setRepository(factory.getRepository(MessageRepository.class));
	}

	@Override
	public List<Message> findByEntityIdOrderByDateDesc(String entityId) {
		return getRepository().findByEntityIdOrderByDateDesc(entityId);
	}

	@Override
	public List<Message> findTop10ByEntityIdNotOrderByDateDesc(String excludeEntitytId) {
		return getRepository().findTop10ByEntityIdNotOrderByDateDesc(excludeEntitytId);
	}

	


}
