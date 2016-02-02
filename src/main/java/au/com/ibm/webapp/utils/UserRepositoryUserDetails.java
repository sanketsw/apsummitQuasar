/**
 * 
 */
package au.com.ibm.webapp.utils;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import au.com.ibm.webapp.model.relational.AppUser;

/**
 * @author sanketsw
 *
 */
public class UserRepositoryUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;
	AppUser user;

	public UserRepositoryUserDetails(AppUser user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRoles());
	}

	@Override
	public String getUsername() {
		return user.getLogin();// inherited from user
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;// not for production just to show concept
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;// not for production just to show concept
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;// not for production just to show concept
	}

	@Override
	public boolean isEnabled() {
		return true;// not for production just to show concept
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	public AppUser getUser() {
		return user;
	}

	public void setUser(AppUser user) {
		this.user = user;
	}
	
	

}

