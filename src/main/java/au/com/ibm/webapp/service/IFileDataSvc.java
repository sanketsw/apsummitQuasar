package au.com.ibm.webapp.service;

import javax.ejb.Local;

import au.com.ibm.webapp.dao.IFileDataDao;
import au.com.ibm.webapp.dao.relational.repository.FileDataRepository;
import au.com.ibm.webapp.model.relational.FileData;
import au.com.ibm.webapp.scaffold.IService;

@Local
public interface IFileDataSvc extends IService<FileData, String, FileDataRepository, IFileDataDao> {


}
