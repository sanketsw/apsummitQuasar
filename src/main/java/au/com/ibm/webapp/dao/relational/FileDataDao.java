package au.com.ibm.webapp.dao.relational;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import au.com.ibm.webapp.dao.Dao;
import au.com.ibm.webapp.dao.IFileDataDao;
import au.com.ibm.webapp.dao.relational.repository.FileDataRepository;
import au.com.ibm.webapp.model.relational.FileData;

@Dao
public class FileDataDao extends AbstractDao<FileData, String, FileDataRepository> implements IFileDataDao {

	@Override
	@PostConstruct
	public void init() {
		JpaRepositoryFactory factory = new JpaRepositoryFactory(getEntityManager());
		setRepository(factory.getRepository(FileDataRepository.class));
	}


}
