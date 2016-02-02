package au.com.ibm.webapp.web.forms;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import au.com.ibm.webapp.config.ConfigUtil;
import au.com.ibm.webapp.model.relational.AppUser;
import au.com.ibm.webapp.scaffold.AbstractMaintenanceForm;
import au.com.ibm.webapp.scaffold.IService;
import au.com.ibm.webapp.service.IUserSvc;
import au.com.ibm.webapp.utils.Role;

@ManagedBean
public class LoginForm extends AbstractMaintenanceForm<String, AppUser> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5096788528222496999L;
	private String userName;
	private String email;
	private String password;

	private String loggedUser;
	private String loggedRole;
	private String loggedEmail;

	@EJB
	private IUserSvc userSvc;

	public IUserSvc getUserSvc() {
		return userSvc;
	}

	public void setUserSvc(IUserSvc userSvc) {
		this.userSvc = userSvc;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLoggedEmail() {
		return loggedEmail;
	}

	public void setLoggedEmail(String loggedEmail) {
		this.loggedEmail = loggedEmail;
	}

	@PostConstruct
	public void init() {
		AppUser user = getUserSvc().getById(AppUser.DEFAULT_USER);
		if (user == null) {
			user = new AppUser();
			user.setLogin(AppUser.DEFAULT_USER);
		}
		user.setEmail(AppUser.DEFAULT_USER);
		user.setPassword(AppUser.PWD);
		user.addRole(Role.ADMIN.value());
		getUserSvc().update(user);

		setUserName("");
		setPassword("");
		setEmail("");
		Authentication authToken = SecurityContextHolder.getContext().getAuthentication();
		printUser(authToken);
		if (authToken != null) {
			setLoggedUser(authToken.getName());
			setLoggedRole(validateAdmin() ? Role.ADMIN.display() : Role.USER.display());
		}
	}

	private void printUser(Authentication authToken) {
		System.out.print("Login Name=" + authToken.getName() + ", Login Role=" + authToken.getAuthorities()
				+ ", isAdmin=" + validateAdmin() + ", isauthenticated=" + validateLoggedIn() + "\n");
		// System.out.println((UserRepositoryUserDetails)
		// authToken.getPrincipal());
		// System.out.println(authToken.getAuthorities());
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String login() throws ServletException, IOException {
		try {
			System.out.println(email);
			AppUser user = getUserSvc().getById(userName);
			if ((!email.contains("ibm.com") || !email.contains("@"))
					&& (!email.equalsIgnoreCase(AppUser.DEFAULT_USER))) {
				FacesContext.getCurrentInstance().addMessage("growl",
						new FacesMessage("Error", "Please enter a valid IBM email."));
				return null;
			}
			if (user == null) {
				List<AppUser> users = getUserSvc().findbyLoginLike(userName);
				if (users != null && !users.isEmpty()) {
					for (AppUser u : users) {
						if (u.getLogin().contains(email.split("@")[0])) {
							user = u;
						}
					}
				}
			}
			if (user == null) {
				user = new AppUser();
				user.setEmail(getEmail());
				user.setLogin(getUserName());
				user.setPassword(AppUser.PIN);
				user.addRole(Role.USER.value());
				getUserSvc().update(user);
				System.out.println(getUserSvc().getById(userName));
			} else if (!email.equalsIgnoreCase(user.getEmail())) {
				// This is another user with same name but different email
				// address
				user = new AppUser();
				user.setEmail(getEmail());
				user.setLogin(getUserName() + "(" + email.split("@")[0] + ")");
				user.setPassword(AppUser.PIN);
				user.addRole(Role.USER.value());
				getUserSvc().update(user);
				System.out.println(getUserSvc().getById(user.getLogin()));
			}

			WebApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(
					(ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext());
			AuthenticationManager authenticationManager = ac.getBean(AuthenticationManager.class);
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(), getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);

			setLoggedUser(authentication.getName());
			setLoggedRole(validateAdmin() ? Role.ADMIN.display() : Role.USER.display());

		} catch (Exception ex) {
			// log.equals(ex.getMessage());
			// ConfigUtil.addMessage("Login Failed: " + ex.getMessage());
			ex.printStackTrace();
			String message = ex.getLocalizedMessage();
			if (message == null) {
				message = "Incorrect user name or password";
			}
			FacesContext.getCurrentInstance().addMessage("growl",
					new FacesMessage("Error", "Login Failed: " + message));
			return null;
		}

		return NavigationBean.dashboard; // + "?faces-redirect=true";
	}

	public String logout() {
		System.out.println("Logging out...");
		getSessionModel().reset();
		SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
		return NavigationBean.Login;

	}

	public void checkEmail() {
		List<AppUser> user = getUserSvc().findbyLoginLike(userName);
		if (user != null && user.size() == 1 && user.get(0).getEmail() != null) {
			email = user.get(0).getEmail();
		}
	}

	public String loginScreen() {
		return NavigationBean.Login;
	}

	public void forgotPwd() {
		ConfigUtil.growl("Action required.", "Send an email to admin to reset the password",
				FacesMessage.SEVERITY_WARN);
	}

	@Override
	public AppUser getNewOne() {
		return new AppUser();
	}

	@Override
	public List<AppUser> getDefaultSearchResults() {
		return getUserSvc().getAll();
	}

	@Override
	public String getEntityBusinessName() {
		return "Login";
	}

	@Override
	public IService getService() {
		return getUserSvc();
	}

	public String getLoggedUser() {
		return loggedUser;
	}

	public void setLoggedUser(String loggedUser) {
		this.loggedUser = loggedUser;
	}

	public String getLoggedRole() {
		return loggedRole;
	}

	public void setLoggedRole(String loggedRole) {
		this.loggedRole = loggedRole;
	}

}
