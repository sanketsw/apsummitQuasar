package au.com.ibm.webapp.scaffold;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import org.springframework.data.jpa.repository.JpaRepository;

@Local
public interface IService<A extends IMasterPersistentEntity<B>,B extends Serializable, C extends JpaRepository<A, B>, D extends IDao<A,B,C>> {
	
	default A getById(B pId) {
		return getDao().findOne(pId);
	}
	
	default A create(A pEntity) {
		return getDao().save(pEntity);
	}
	
	default A update(A pEntity) {
		return getDao().saveAndFlush(pEntity);
	}
	
	default void delete(A pEntity) {
		getDao().delete(pEntity);
	}
	
	default List<A> getAll() {
		return getDao().findAll();
	}
	
	// cannot be defaulted as it requires access to a instance variable 
	D getDao();
}
