package au.com.ibm.webapp.service.impl;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import au.com.ibm.webapp.dao.Dao;
import au.com.ibm.webapp.dao.IUserDao;
import au.com.ibm.webapp.model.relational.AppUser;
import au.com.ibm.webapp.service.IUserSvc;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UserSvc implements IUserSvc {

	@Inject
	@Dao
	private IUserDao dao;

	public IUserDao getDao() {
		return dao;
	}

	public void setDao(IUserDao dao) {
		this.dao = dao;
	}

	@Override
	public AppUser getById(String pId) {
		return getDao().findOne(pId);
	}
	
	@Override
	public AppUser create(AppUser pEntity) {
		return getDao().save(pEntity);
	}
	
	@Override
	public AppUser update(AppUser pEntity) {
		return getDao().save(pEntity);
	}

	@Override
	public void delete(AppUser pEntity) {
		getDao().delete(pEntity);
	}
	
	@Override
	public List<AppUser> getAll() {
		return getDao().findAll();
	}

	@GET
	@Path("/get")	
	@Produces({"application/json"})
	public AppUser getByNumber() {
		AppUser user = new AppUser();
		user.setLogin("REST AppUser");
		
		return user;
		
	}

	@Override
	public List<AppUser> findbyLoginLike(String userName) {
		return getDao().findByLoginLike(userName);
	}
}
