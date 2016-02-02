package au.com.ibm.webapp.model.config;

import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.SynchronizationType;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import au.com.ibm.webapp.dao.Dao;
import au.com.ibm.webapp.dao.IFileDataDao;
import au.com.ibm.webapp.dao.IUserDao;
import au.com.ibm.webapp.dao.relational.FileDataDao;
import au.com.ibm.webapp.dao.relational.UserDao;

@Configuration
@EnableJpaRepositories(basePackages = "au.com.ibm.webapp.dao.relational.repository")
@EnableTransactionManagement
public class ModelJPAConfig {

	//private static final Logger logger = LoggerFactory.getLogger(ModelJPAConfig.class);
	
	@Bean public DataSource dataSource() throws SQLException {

		DataSource dataSource = null;
		
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		dataSource = builder.setName("test-webapp-db").setType(EmbeddedDatabaseType.H2).build();
        
        return dataSource;
	}
	

	@Bean @Scope(value="singleton") public EntityManagerFactory entityManagerFactory() throws SQLException {

		
		EclipseLinkJpaVendorAdapter vendorAdapter = new EclipseLinkJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);
		

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		
		// Note because the prod pesistence.xml references a JTA transaction type and uses a JNDI reference to a datasource
		// it won't work in the case where we are simulating through Spring so we have to have a test specific version of 
		// persistence.xml file
		factory.setPersistenceXmlLocation("classpath*:/META-INF/persistence-test.xml");		
		factory.setDataSource(dataSource());
		factory.afterPropertiesSet();

		return factory.getObject();
	}

	@Bean @Scope(value="singleton") public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
		return entityManagerFactory.createEntityManager(SynchronizationType.SYNCHRONIZED);
	}

	@Bean @Scope(value="singleton") public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) throws SQLException {

		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory);
		
		return txManager;
	}
	
	@Bean @Scope(value="singleton") public JpaRepositoryFactory jpaRepositoryFactory(EntityManager entityManager) throws SQLException {
		
		return new JpaRepositoryFactory(entityManager);
	}
	
	
	@Bean @Dao public IUserDao userDao() {
		return new UserDao();
	}
	
	@Bean @Dao public IFileDataDao fileDataDao() {
		return new FileDataDao();
	}

}
