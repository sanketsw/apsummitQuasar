package au.com.ibm.webapp.dao.relational.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import au.com.ibm.webapp.model.relational.Message;


@NoRepositoryBean
@Repo
public interface MessageRepository extends JpaRepository<Message, String> {
	
	List<Message> findByEntityIdOrderByDateDesc(String entityId);
	
	List<Message> findTop10ByEntityIdNotOrderByDateDesc(String excludeEntitytId);

}
