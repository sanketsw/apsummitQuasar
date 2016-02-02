package au.com.ibm.webapp.dao.relational;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

import au.com.ibm.webapp.dao.Dao;
import au.com.ibm.webapp.dao.IUserDao;
import au.com.ibm.webapp.dao.RelationalTests;
import au.com.ibm.webapp.model.relational.AppUser;
import au.com.ibm.webapp.utils.Role;

public class UserDaoTest extends RelationalTests {

	@Inject
	@Dao
	IUserDao dao;
	
	@Test
	public void test() {
		AppUser user = new AppUser();
		user.setLogin("sanket");
		user.setPassword("passw0rd");
		user.addRole(Role.USER.value());
		user.addRole(Role.ADMIN.value());
		user.addRole(Role.USER.value());
		dao.save(user);
		
		AppUser u2 = dao.findOne("sanket");
		
		assertNotNull(u2);
		assertEquals("sanket", u2.getLogin());
		assertEquals("passw0rd", u2.getPassword());
		assertEquals(true, u2.getRoles().contains(Role.USER.value()));
		assertEquals(true, u2.getRoles().contains(Role.ADMIN.value()));
	}
	
	@Test
	public void testfindByLoginLike() {
		AppUser user = new AppUser();
		user.setLogin("sanket");
		user.setPassword("passw0rd");
		user.addRole(Role.USER.value());
		user.addRole(Role.ADMIN.value());
		dao.save(user);
		
		user = new AppUser();
		user.setLogin("sanket12");
		user.setPassword("data");
		user.addRole(Role.USER.value());
		dao.save(user);
		
		user = new AppUser();
		user.setLogin("edwin_an");
		user.setPassword("data");
		user.addRole(Role.USER.value());
		dao.save(user);
		
		List<AppUser> users = dao.findByLoginLike("an");
		assertEquals(3, users.size());
		
		users = dao.findByLoginLike("sanket");
		assertEquals(2, users.size());
		
		users = dao.findByLoginLike("edwin");
		assertEquals(1, users.size());
		
		users = dao.findByLoginLike("bancha");
		assertEquals(0, users.size());
	}
	
	
}
