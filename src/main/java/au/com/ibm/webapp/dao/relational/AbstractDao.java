package au.com.ibm.webapp.dao.relational;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import au.com.ibm.webapp.scaffold.IDao;

public abstract class AbstractDao<A, B extends Serializable, C extends JpaRepository<A, B> > implements IDao<A, B, C> {

	// Default logger
	private static Logger logger = LoggerFactory.getLogger(AbstractDao.class); 
	
	private C repository;
	
	@Inject
	private EntityManager entityManager;					
	
	abstract public void init();

	public C getRepository() {
		return repository;
	}
	
	public void setRepository(C repository) {
		this.repository = repository;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}
			
	@Override
	public List<A> findAll() {
		
		List<A> results = new ArrayList<A>();
		
		for(A entity : getRepository().findAll()) {
			results.add(entity);
		}
		return results;
	}

	@Override
	public List<A> findAll(Iterable<B> arg0) {
		return getRepository().findAll(arg0);
	}
	
	@Override
	public <S extends A> S save(S entity) {
		logger.info("event=save, entity_type=" + entity.getClass().getSimpleName() + ", entity=" + entity.toString());
		return getRepository().save(entity);
	}

	@Override
	public A findOne(B id) {
		return getRepository().findOne(id);
	}

	@Override
	public boolean exists(B id) {
		return getRepository().exists(id);
	}

	@Override
	public long count() {
		return getRepository().count();
	}

	@Override
	public void delete(B id) {
		getRepository().delete(id);
	}

	@Override
	public void delete(A entity) {
		getRepository().delete(entity);
	}

	@Override
	public void delete(Iterable<? extends A> entities) {
		getRepository().delete(entities);
	}

	@Override
	public void deleteAll() {
		getRepository().deleteAll();
	}
	
	@Override
	public <S extends A> List<S> save(Iterable<S> arg0) {
		return getRepository().save(arg0);
	}

	@Override
	public void deleteAllInBatch() {
		getRepository().deleteAllInBatch();
		
	}

	@Override
	public void deleteInBatch(Iterable<A> arg0) {
		getRepository().deleteInBatch(arg0);
		
	}

	@Override
	public List<A> findAll(Sort arg0) {
		return getRepository().findAll(arg0);
	}

	@Override
	public void flush() {
		getRepository().flush();		
	}

	@Override
	public A getOne(B arg0) {
		return getRepository().getOne(arg0);
	}

	@Override
	public <S extends A> S saveAndFlush(S arg0) {
		return getRepository().saveAndFlush(arg0);
	}

	@Override
	public Page<A> findAll(Pageable arg0) {
		return getRepository().findAll(arg0);
	}
}
