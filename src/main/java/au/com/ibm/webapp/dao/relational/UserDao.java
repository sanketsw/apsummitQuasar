package au.com.ibm.webapp.dao.relational;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import au.com.ibm.webapp.dao.Dao;
import au.com.ibm.webapp.dao.IUserDao;
import au.com.ibm.webapp.dao.relational.repository.UserRepository;
import au.com.ibm.webapp.model.relational.AppUser;


@Dao
public class UserDao extends AbstractDao<AppUser, String, UserRepository> implements IUserDao {

	@Override
	@PostConstruct
	public void init() {
		JpaRepositoryFactory factory = new JpaRepositoryFactory(getEntityManager());
		setRepository(factory.getRepository(UserRepository.class));
	}

	@Override
	public List<AppUser> findByLoginLike(String genericSearchString) {
		CriteriaBuilder c = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<AppUser> criteria = c.createQuery(AppUser.class);
		Root<AppUser> usr = criteria.from(AppUser.class);
		List<Predicate> predicates = new ArrayList<Predicate>();
		criteria.select(usr);
		if (genericSearchString != null && !genericSearchString.isEmpty()) {
			Predicate predicate = c.like(c.upper(usr.get("login")), "%" + genericSearchString.toUpperCase() + "%");
			predicates.add(predicate);
		}
		criteria.where(c.or(predicates.toArray(new Predicate[0])));
		List<AppUser> resultList = getEntityManager().createQuery(criteria).getResultList();
		return resultList;
	}


}
