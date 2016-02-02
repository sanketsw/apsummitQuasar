package au.com.ibm.webapp.service.impl;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import au.com.ibm.webapp.dao.Dao;
import au.com.ibm.webapp.dao.IMessageDao;
import au.com.ibm.webapp.model.relational.Message;
import au.com.ibm.webapp.service.IMessageSvc;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Path("/message")
public class MessageSvc implements IMessageSvc {

	@Inject
	@Dao
	private IMessageDao dao;

	public IMessageDao getDao() {
		return dao;
	}

	public void setDao(IMessageDao dao) {
		this.dao = dao;
	}

	@Override
	public Message getById(String pId) {
		return getDao().findOne(pId);
	}
	
	@Override
	public Message create(Message pEntity) {
		return getDao().save(pEntity);
	}
	
	@Override
	public Message update(Message pEntity) {
		return getDao().save(pEntity);
	}

	@Override
	public void delete(Message pEntity) {
		getDao().delete(pEntity);
	}
	
	@Override
	@GET
	@Path("/list")
	@Produces({ "application/json" })
	public List<Message> getAll() {
		return getDao().findAll();
	}
	
	@Override
	public List<Message> getByEntityId(String entityId) {
		return getDao().findByEntityIdOrderByDateDesc(entityId);
	}

	@Override
	public List<Message> getRecentActivity() {
		return getDao().findTop10ByEntityIdNotOrderByDateDesc(Message.EntityIdGlobal);
	}
	

}
