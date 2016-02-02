package au.com.ibm.webapp.dao.relational;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

import au.com.ibm.webapp.dao.Dao;
import au.com.ibm.webapp.dao.IFileDataDao;
import au.com.ibm.webapp.dao.RelationalTests;
import au.com.ibm.webapp.model.relational.AppUser;
import au.com.ibm.webapp.model.relational.FileData;
import au.com.ibm.webapp.utils.Role;
import au.com.ibm.webapp.web.forms.ScheduleManager;

public class FileDataDaoTest extends RelationalTests {

	@Inject
	@Dao
	IFileDataDao dao;
	
	@Test
	public void test() {
		FileData f =  new FileData();
		f.setName("me");
		try {
			InputStream is = this.getClass().getResourceAsStream("schedule.xlsx");
			f.setFile(org.apache.commons.io.IOUtils.toByteArray(is));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dao.save(f);
		
		FileData u2 = dao.findOne("me");
		
		assertNotNull(u2);
		assertNotNull(u2.getFile());
	}
	
	
}
