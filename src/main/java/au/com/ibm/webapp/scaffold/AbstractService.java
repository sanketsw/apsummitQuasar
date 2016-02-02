package au.com.ibm.webapp.scaffold;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.springframework.data.jpa.repository.JpaRepository;

import au.com.ibm.webapp.dao.Dao;

/*
 * This class cannot be used to create Service classes as it will not work in WebSphere Liberty.
 */
public abstract class AbstractService<A extends IMasterPersistentEntity<B>, B extends Serializable, C extends JpaRepository<A, B>, D extends IDao<A, B, C>>
		implements IService<A, B, C, D> {

	@Inject
	@Dao
	private D dao;
	
	@Override
	public D getDao() {
		return dao;
	}
	
	public void setDao(D dao) {
		this.dao = dao;
	}

	@Override
	public A getById(B pId) {
		return getDao().findOne(pId);
	}
	
	@Override
	public A create(A pEntity) {
		return getDao().save(pEntity);
	}
	
	@Override
	public A update(A pEntity) {
		return getDao().save(pEntity);
	}

	@Override
	public void delete(A pEntity) {
		getDao().delete(pEntity);
	}

	@Override
	public List<A> getAll() {
		return getDao().findAll();
	}
}
