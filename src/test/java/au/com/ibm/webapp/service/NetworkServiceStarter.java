package au.com.ibm.webapp.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.openejb.util.ServiceManagerProxy;
import org.apache.openejb.util.ServiceManagerProxy.AlreadyStartedException;

public class NetworkServiceStarter {

	private static final Log LOG = LogFactory.getLog(NetworkServiceStarter.class);
	
	private ServiceManagerProxy serviceManagerProxy;
	
	@PostConstruct
	public void postConstruct() {
		LOG.info("Starting network services");
		if(serviceManagerProxy == null) {
			try {
				serviceManagerProxy = new ServiceManagerProxy();
			} catch (AlreadyStartedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		serviceManagerProxy.start();
	}
	
	@PreDestroy
	public void preDestroy() {
		LOG.info("Stoping network services");
		if(serviceManagerProxy != null) {
			serviceManagerProxy.stop();
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
