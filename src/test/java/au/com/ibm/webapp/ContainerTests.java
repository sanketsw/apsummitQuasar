package au.com.ibm.webapp;

import java.io.File;
import java.io.FilenameFilter;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//@RunWith(Arquillian.class)
public abstract class ContainerTests {

	private static final Logger logger = LoggerFactory.getLogger(ContainerTests.class);
	
	private static final String WEB_INF = "src/main/webapp/WEB-INF";
	private static final String META_INF = "src/main/resources/META-INF";
	private static final String APP_ROOT = "au/com/ibm/webapp";
	private static final String APP_ROOT_PACKAGE = "au.com.ibm.webapp";
	private static final String WEBAPP_SRC = "src/main/webapp";
	private static final String WEBAPP_LIB = "src/main/webapp/WEB-INF/lib";
	
	@Deployment
    public static WebArchive createDeployment() {
    	
    	File[] files = Maven.resolver().loadPomFromFile("pom.xml")
                .importRuntimeDependencies().resolve().withTransitivity().asFile();
    	
    	WebArchive war = ShrinkWrap.create(WebArchive.class, "test-webapp.war")
    			.addAsLibraries(files)
    			
    			// need to filter out non container tests
    			.addPackages(true, Filters.include(".*ContainerTest.*"), "au.com.ibm.webapp")
    			.addPackages(true, Filters.exclude(".*Test.*|.*ModelJPAConfig.*|.*ApplicationConfig.*"), "au.com.ibm.webapp")

    			// need to added arquillian test to war
    			.addPackages(true, "au.com.ibm.arquillian")
    			
    			// add as a resource forces persistence.xml into the WEB-INF/classess folder which is visible to classloader when
    			// searching
		        .addAsResource(new File(META_INF,"persistence.xml"),"META-INF/persistence.xml")


		        //WEB-INF
		        // datasource definitions
		        .addAsWebInfResource("wildfly-ds.xml")
		        .addAsWebInfResource(new File(WEB_INF,"beans.xml"), "beans.xml")
		        
		        // this version of the web.xml remove resource refs as I haven't worked out how to define them dynamically during tests
		        .addAsWebInfResource("test-web.xml", "web.xml")
				
				 .addAsLibraries(new File(WEBAPP_LIB, "rio-theme-1.1.jar"))
		        
		        .addAsWebInfResource(new File(WEB_INF,"faces-config.xml"), "faces-config.xml")
		        ;    													  

		for (File f : new File(WEBAPP_SRC).listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".xhtml");
			}
		})) {
			war.addAsWebResource(f);
		}
    	
		//war.addAsWebResource(new File(WEBAPP_SRC+"/ui/opportunity/opportunityList.xhtml"),"/ui/opportunity/opportunityList.xhtml");
		//war.addAsWebResource(new File(WEBAPP_SRC+"/ui/opportunity/opportunity.xhtml"),"/ui/opportunity/opportunity.xhtml");
		
    	logger.info(war.toString(true));
    	
        return war;
    }
    


}
