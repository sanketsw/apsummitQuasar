package au.com.ibm.webapp.dao.relational.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import au.com.ibm.webapp.model.relational.FileData;


@NoRepositoryBean
@Repo
public interface FileDataRepository extends JpaRepository<FileData, String> {

}
