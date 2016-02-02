package au.com.ibm.webapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.xa.XAException;

import org.apache.openejb.spring.ClassPathApplication;
import org.apache.openejb.spring.OpenEJB;
import org.apache.openejb.spring.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class ApplicationConfig {

	@Bean public org.apache.openejb.spring.Resource webappDb() {
		Resource webappDb = new Resource();
		
		/*
		<property name="type" value="javax.sql.DataSource" />
		<property name="id" value="jdbc/somedb" />
		<property name="properties">
			<props>
				<prop key="JdbcDriver">org.apache.derby.jdbc.EmbeddedDriver</prop>
				<prop key="JdbcUrl">jdbc:derby:somedb;create=true</prop>
			</props>
		</property>
		 */
		webappDb.setType("javax.sql.DataSource");
		webappDb.setId("jdbc/webapp-db");
		
		Properties properties = new Properties();
		properties.put("JdbcDriver", "org.h2.Driver");
		properties.put("JdbcUrl", "jdbc:h2:mem:webapp-db");
		webappDb.setProperties(properties);
		
		return webappDb;
	}

	
	@Bean
	@DependsOn("openEJB")
	public NetworkServiceStarter networkServiceStarter() {
		return new NetworkServiceStarter();
	}
	
	
	@Bean public org.apache.openejb.spring.OpenEJB openEJB() throws XAException {
		org.apache.openejb.spring.OpenEJB openEJB = new OpenEJB();
		
		/*
		<property name="properties">
			<props>
				<prop key="openejb.embedded.remotable">true</prop>
				
			</props>	
		</property>

		<!-- <property name="transactionManager" ref="txnManager"/> -->
		<property name="resources">
			<list value-type="org.apache.openejb.spring.Resource">
				<ref bean="somedb"/>
			</list>
		</property>
		*/
		
		Properties properties = new Properties();
		properties.put("openejb.embedded.remotable", true);
		openEJB.setProperties(properties);
		
		List<org.apache.openejb.spring.Resource> resources = new ArrayList<org.apache.openejb.spring.Resource>();
		resources.add(webappDb());
		openEJB.setResources(resources);
		//openEJB.setTransactionManager(txnManager());
		
		return openEJB;
	}
	
	@Bean org.apache.openejb.spring.ClassPathApplication classPathApplication() throws XAException {
		
		/*
		<bean name="classPathApplication" class="org.apache.openejb.spring.ClassPathApplication" >
		<property name="classpathAsEar" value="true"/>
		<property name="export" value="true"/>
		<property name="openEJB" ref="openEJB"/>
		</bean>
		*/
		org.apache.openejb.spring.ClassPathApplication classPathApplication = new ClassPathApplication();
		
		classPathApplication.setClasspathAsEar(true);
		classPathApplication.setExport(true);
		classPathApplication.setOpenEJB(openEJB());
		
		return classPathApplication;
	}
	
	@Bean org.apache.openejb.ri.sp.PseudoSecurityService securityService() {
		return new org.apache.openejb.ri.sp.PseudoSecurityService();
	}
	
	@Bean org.apache.geronimo.transaction.manager.GeronimoTransactionManager txnManager() throws XAException {
		return new org.apache.geronimo.transaction.manager.GeronimoTransactionManager();
	}
	
	@Bean
	@Lazy(true)
	org.springframework.transaction.jta.JtaTransactionManager transactionManager() {
		org.springframework.transaction.jta.JtaTransactionManager jtaTransactionManager = new org.springframework.transaction.jta.JtaTransactionManager();
		
		Properties properties = new Properties();
		properties.put("java.naming.factory.initial", "org.apache.openejb.client.LocalInitialContextFactory");
		
		jtaTransactionManager.setJndiEnvironment(properties);
		
		return jtaTransactionManager;
	}

	/*
	@Bean
	@Lazy(true)
	public javax.sql.DataSource dataSource() {
		DataSource dataSource = null;
		
		final JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();

		Properties properties = new Properties();
		properties.put("java.naming.factory.initial", "org.apache.openejb.client.LocalInitialContextFactory");
		
		dsLookup.setJndiEnvironment(properties);
        dsLookup.setResourceRef(true);
        dataSource = dsLookup.getDataSource("jdbc/multiverse-db");
		dataSource = null;
		
		try {
	        dsLookup.setResourceRef(true);
	        dataSource = dsLookup.getDataSource("jdbc/multiverse-db");
		} catch(Exception nice) {
			//noop
		}

		if(dataSource == null) {
    		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
    		dataSource = builder.setName("multiverse-db").setType(EmbeddedDatabaseType.H2).build();
        }
        
        return dataSource;
	}
	
	
	@Bean
	@Lazy(true)
	public javax.persistence.EntityManagerFactory entityManagerFactory() throws NamingException {
		final JndiObjectFactoryBean dsLookup = new JndiObjectFactoryBean();

		//dsLookup.setExpectedType(javax.persistence.EntityManagerFactory.class);
		dsLookup.setJndiName("java:openejb/PersistenceUnit/multiverse-db");
		dsLookup.setLookupOnStartup(false);
		Properties properties = new Properties();
		properties.put("java.naming.factory.initial", "org.apache.openejb.client.LocalInitialContextFactory");
		
		dsLookup.setJndiEnvironment(properties);
		Context context = dsLookup.getJndiTemplate().getContext();
		System.out.println("environment -> " + context.getEnvironment().isEmpty());
				
		return (EntityManagerFactory)dsLookup.getObject();

 	}
	*/
	
	/*

	<jee:jndi-lookup id="cdcentral" jndi-name="java:openejb/PersistenceUnit/cdcentral 1554215845" lookup-on-startup="false" expected-type="javax.persistence.EntityManagerFactory">
		<jee:environment>
			java.naming.factory.initial=org.apache.openejb.client.LocalInitialContextFactory
		</jee:environment>
	</jee:jndi-lookup>
	
	*/
}
