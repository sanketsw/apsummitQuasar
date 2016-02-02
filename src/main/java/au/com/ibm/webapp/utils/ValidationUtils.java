package au.com.ibm.webapp.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class ValidationUtils {
	
	public static boolean isAdmin() {
		Authentication authToken = SecurityContextHolder.getContext().getAuthentication();
		return isAdmin(authToken);
	}
	
	@SuppressWarnings("unchecked")
	public static boolean isAdmin(Authentication authToken) {
		if (authToken != null) {
			Collection<GrantedAuthority> auth = (Collection<GrantedAuthority>) authToken.getAuthorities();
			for (GrantedAuthority role : auth) {
				if (role.getAuthority().equals(Role.ADMIN.value())) {
					return true && authToken.isAuthenticated();
				}
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public static boolean checkRoles(Authentication authToken, Set<Role> targetRoles) {
		if (authToken != null) {
			Collection<GrantedAuthority> auth = (Collection<GrantedAuthority>) authToken.getAuthorities();
			for (GrantedAuthority role : auth) {
				if (targetRoles.contains(Role.getEnum(role.getAuthority()))) {
					return true && authToken.isAuthenticated();
				}
			}
		}
		return false;
	}
	
	public static boolean isLoggedIn() {
		Authentication authToken = SecurityContextHolder.getContext().getAuthentication();
		Set<Role> validRoles = new HashSet<Role>();
		validRoles.add(Role.ADMIN);
		validRoles.add(Role.USER);
		return checkRoles(authToken, validRoles);
	}
	
	public static boolean validateMyself(String userName) {
		Authentication authToken = SecurityContextHolder.getContext().getAuthentication();
		if(authToken.getName().equalsIgnoreCase(userName)) {
			return true;
		}
		return false;
	}

}
