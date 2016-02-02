/**
 * 
 */
package au.com.ibm.webapp.config;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.persistence.NoResultException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import au.com.ibm.webapp.model.relational.AppUser;
import au.com.ibm.webapp.service.IUserSvc;
import au.com.ibm.webapp.utils.UserRepositoryUserDetails;

/**
 * @author sanketsw
 *
 */
@RequestScoped
@Service
public class CustomUserDetailsService implements UserDetailsService {

	@EJB(mappedName="java:module/UserSvc!au.com.ibm.webapp.service.impl.UserSvc")
	private IUserSvc userSvc;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails userDetails = null;
		try {
			AppUser user = userSvc.getById(username);
			userDetails = new UserRepositoryUserDetails(user);
		} catch (NoResultException e) {
			throw new UsernameNotFoundException(username);
		}
		return userDetails;

	}

	public IUserSvc getUserSvc() {
		return userSvc;
	}
	
	public void setUserSvc(IUserSvc userSvc) {
		this.userSvc = userSvc;
	}
	
}