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
import au.com.ibm.webapp.dao.IFileDataDao;
import au.com.ibm.webapp.model.relational.FileData;
import au.com.ibm.webapp.service.IFileDataSvc;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Path("/fileData")
public class FileDataSvc implements IFileDataSvc {

	@Inject
	@Dao
	private IFileDataDao dao;

	public IFileDataDao getDao() {
		return dao;
	}

	public void setDao(IFileDataDao dao) {
		this.dao = dao;
	}

	@Override
	public FileData getById(String pId) {
		return getDao().findOne(pId);
	}
	
	@Override
	public FileData create(FileData pEntity) {
		return getDao().save(pEntity);
	}
	
	@Override
	public FileData update(FileData pEntity) {
		return getDao().saveAndFlush(pEntity);
	}

	@Override
	public void delete(FileData pEntity) {
		getDao().delete(pEntity);
	}
	
	@Override
	@GET
	@Path("/list")
	@Produces({ "application/json" })
	public List<FileData> getAll() {
		return getDao().findAll();
	}
	

	

}
