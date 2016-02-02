package au.com.ibm.webapp.service;

import java.util.List;

import javax.ejb.Local;

import au.com.ibm.webapp.dao.IUserDao;
import au.com.ibm.webapp.dao.relational.repository.UserRepository;
import au.com.ibm.webapp.model.relational.AppUser;
import au.com.ibm.webapp.scaffold.IService;

@Local
public interface IUserSvc extends IService<AppUser, String, UserRepository, IUserDao> {

	List<AppUser> findbyLoginLike(String userName);

}
