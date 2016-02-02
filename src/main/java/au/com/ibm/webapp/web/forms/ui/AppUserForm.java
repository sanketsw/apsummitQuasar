package au.com.ibm.webapp.web.forms.ui;

import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.ibm.webapp.config.ConfigUtil;
import au.com.ibm.webapp.model.relational.AppUser;
import au.com.ibm.webapp.scaffold.AbstractMaintenanceForm;
import au.com.ibm.webapp.scaffold.IService;
import au.com.ibm.webapp.service.IUserSvc;
import au.com.ibm.webapp.utils.Role;
import au.com.ibm.webapp.utils.ValidationUtils;

@ManagedBean
public class AppUserForm extends AbstractMaintenanceForm<String, AppUser> {

	private static final long serialVersionUID = 8158434638931310723L;

	private static final Logger logger = LoggerFactory.getLogger(AppUserForm.class);

	@EJB
	private IUserSvc userSvc;

	private String password;

	@PostConstruct
	public void init() {
	}


	@Override
	public AppUser getNewOne() {
		return new AppUser();
	}

	@Override
	public List<AppUser> getDefaultSearchResults() {
		return userSvc.getAll();
	}

	@Override
	public String getEntityBusinessName() {
		return "App User";
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public IService getService() {
		return userSvc;
	}

	public Set<String> getAllRolesList() {
		return Role.getAllRolesList();
	}

	public boolean isDisbaledToModify(String data) {
		return data.equals(Role.ANONYMOUS.display()) ? true : data.equals("admin")? true : false;
	}

	@Override
	public void executeBeforeSave() {
		// Set the default password unless specified
		AppUser user = getSessionModel().getModel();
		if (password != null && !password.isEmpty()) {
			// At the time of registration of USER
			user.setPassword(password);
		} else if (user.getPassword() == null || user.getPassword().isEmpty()) {
			// When admin adds a new user manually
			user.setPassword(AppUser.PIN);
		}
	}
	

	@Override
	public void executeAfterSave() {
		if (getSessionModel().isNewMode() && !ValidationUtils.isAdmin()) {
			ConfigUtil.growl("Info", "User details pending for administrator approval");
			getSessionModel().reset();
		} else {
			viewList();
		}

	}

	@Override
	public boolean canNewEntityEditExisting() {
		// We dont want to touch existing users
		return false;
	}

	public void resetPwd() {
		getSessionModel().getModel().setPassword(AppUser.PIN);
	}

}
