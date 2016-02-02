package au.com.ibm.webapp.model.relational;

import java.io.IOException;
import java.io.InputStream;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

import org.apache.commons.io.IOUtils;

import au.com.ibm.webapp.scaffold.AbstractMasterEntity;


@Entity
public class FileData extends AbstractMasterEntity<String> {		

	/**
	 * 
	 */
	private static final long serialVersionUID = -7217453192983635095L;

	@Id
	private String name;
	
	private byte[] file;
	
	@Version
	private int version;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String getSearchResultInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return name;
	}


	
	
}
