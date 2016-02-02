package au.com.ibm.webapp.dao;

import org.springframework.data.repository.NoRepositoryBean;

import au.com.ibm.webapp.dao.relational.repository.FileDataRepository;
import au.com.ibm.webapp.model.relational.FileData;
import au.com.ibm.webapp.scaffold.IDao;

@NoRepositoryBean
public interface IFileDataDao extends IDao<FileData, String, FileDataRepository> , FileDataRepository {
	
	
}
