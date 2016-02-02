package au.com.ibm.webapp.scaffold;

import java.beans.Introspector;
import java.io.Serializable;
import java.util.List;

import au.com.ibm.webapp.config.ConfigUtil;
import au.com.ibm.webapp.utils.PageDetails;
import au.com.ibm.webapp.utils.ValidationUtils;
import au.com.ibm.webapp.web.forms.Mode;
import au.com.ibm.webapp.web.forms.SessionModel;

public abstract class AbstractMaintenanceForm<K, T extends IMasterPersistentEntity<K>> extends AbstractForm<T> {

	private static final long serialVersionUID = 5628812973875829541L;

	/**
	 * Get the form bean name such as serviceLineForm
	 * 
	 * @return
	 */
	public String getFormClassName() {
		// String formClassName =
		// getEntityBusinessName().toLowerCase().replace(" ", "") + "Form";
		String formClassName = Introspector.decapitalize(this.getClass().getSimpleName());
		return formClassName;
	}

	public void viewList() {
		String formClassName = getFormClassName();
		List<T> results = getDefaultSearchResults();
		getSessionModel().addPage(new PageDetails(getViewForList(), results, formClassName), this);
		getSessionModel().setResults(results);
		getSessionModel().setContent(getViewForList());
	}

	public void viewOne(T entity) {
		String formClassName = getFormClassName();
		getSessionModel().addPage(new PageDetails(entity, getViewForOne(), null, formClassName), this);
		getSessionModel().setModel(entity);
		getSessionModel().setMode(Mode.EDIT);
		getSessionModel().setContent(getViewForOne());
	}

	public void viewNewOne() {
		getSessionModel().setModel(getNewOne());
		getSessionModel().setMode(Mode.NEW);
		getSessionModel().setContent(getViewForOne());
	}
	
	public boolean validateAdmin() {
		return ValidationUtils.isAdmin();
	}
	
	
	public boolean validateLoggedIn() {
		return ValidationUtils.isLoggedIn();
	}
	
	public boolean validateMyself(String userName) {
		return ValidationUtils.validateMyself(userName);
	}

	@SuppressWarnings("unchecked")
	public String save() {
		try {
			executeBeforeSave();
			T entity = (T) getService().update(getSessionModel().getModel());
			getSessionModel().setModel(entity);
			getSessionModel().setMode(Mode.EDIT);
			ConfigUtil.growl("Info", "Changes saved");
			executeAfterSave();
		} catch (Exception e) {
			e.printStackTrace();
			ConfigUtil.growl("Error", "Could not save the changes");
		}
		return null;
	}
	
	/*
	public String save() {

		executeBeforeSave();

		if (getSessionModel().getMode().equals(Mode.NEW)) {
			return saveNew();
		}

		try {
			T en = (T) getService().update(getSessionModel().getModel());
			getSessionModel().setModel(en);
			getSessionModel().setMode(Mode.EDIT);
			ConfigUtil.growl("Info", "Changes saved");
			//executeAfterSave();
		} catch (Exception e) {
			e.printStackTrace();
			ConfigUtil.growl("Error", "Could not save the changes");
		}
		return null;
	}
	*/

	@SuppressWarnings("unchecked")
	/*
	public String saveNew() {
		//By default New entity cannot overwrite existing blindly but it will start editing the existing entity
		if (!canNewEntityOverwriteExisting()) {
			T en = (T) getService().getById((Serializable) getSessionModel().getModel().getId());
			if (en != null && canNewEntityEditExisting()) {
				ConfigUtil.growl("Warning", "Already exists. Modifying existing entity...");
				getSessionModel().setModel(en);
				getSessionModel().setMode(Mode.EDIT);
				return null;
			} else if(en != null && ! canNewEntityEditExisting()) {
				ConfigUtil.growl("Info", "Id already exists. Choose a new one.");
				return null;
			}
		}
		try {
			T newEn = (T) getService().update(getSessionModel().getModel());
			getSessionModel().setModel(newEn);
			getSessionModel().setMode(Mode.EDIT);
			ConfigUtil.growl("Info", "Details saved.");
			//executeAfterSave();
		} catch (Exception e) {
			e.printStackTrace();
			ConfigUtil.growl("Error", "Could not save details");
		}
		return null;
	}
	*/


	/**
	 * Method for implementation classes to do some operations before saving to database
	 */
	public void executeBeforeSave() {
		
	}

	public void executeAfterSave() {
		//goBack();
	}

	/**
	 * True if you want to overwrite the existing entity with new one
	 * False if you want to select behavior if entity already exists
	 * @see canNewEntityEditExisting
	 * @return true if you want to overwrite blindly
	 */
	public boolean canNewEntityOverwriteExisting() {
		return false;
	}
	
	/**
	 * True if you want to start modifying existing entity. Will populate with existing entity
	 * False if you want to show an error that id already exists.
	 * @return false for cases like register a new user
	 */
	public boolean canNewEntityEditExisting() {
		return true;
	}


	@SuppressWarnings("unchecked")
	public void remove(T entity) {
		try {
			getService().delete(entity);
			getSessionModel().getResults().remove(entity);
		} catch (Exception e) {
			e.printStackTrace();
			ConfigUtil.growl("Error", "Could not remove the entity");
		}

	}

	/**
	 * Go to the previous screen from back button
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void goBack() {
		SessionModel s = getSessionModel();
		PageDetails previousPage = s.removePreviousPage();
		if (previousPage != null) {
			if (previousPage.getFormClassName() != null) {
				// This means that it is called from AbstractMaintenanceForm
				AbstractMaintenanceForm form = (AbstractMaintenanceForm) ConfigUtil.getManagedBean(previousPage.getFormClassName());
				if (form != null) {
					if (previousPage.getModel() == null) {
						form.viewList();
					} else {
						// Get the latest version of the entity. The one we
						// saved may be old.
						IMasterPersistentEntity entity = form.getService().getById((Serializable) previousPage.getModel().getId());
						form.viewOne(entity);
					}
				} else {
					System.out.println("Could not move to previous page. Use the menu to navigate");
					ConfigUtil.growl("INFO", "Could not move to previous page. Use the menu to navigate");
				}
			} else {
				// These are pages called from NavigationBean
				s.setContent(previousPage.getContent());
				// Add this page back to the stack
				s.addPage(previousPage);
			}
		}
		// System.out.println("Do nothing");
	}

	public String getViewForOne() {

		String compactBusinessName = getEntityBusinessName().toLowerCase().replace(" ", "");

		return "/ui/" + compactBusinessName + "/" + compactBusinessName + ".xhtml";
	}

	public String getViewForList() {
		String compactBusinessName = getEntityBusinessName().toLowerCase().replace(" ", "");

		return "/ui/" + compactBusinessName + "/" + compactBusinessName + "List.xhtml";
	}

	public String getViewListTitle() {
		return getEntityBusinessName() + "s";
	}

	public String getViewOneTitle() {
		return getEntityBusinessName();
	}

	public abstract T getNewOne();

	public abstract List<T> getDefaultSearchResults();

	public abstract String getEntityBusinessName();

	@SuppressWarnings("rawtypes")
	public abstract IService getService();

}
