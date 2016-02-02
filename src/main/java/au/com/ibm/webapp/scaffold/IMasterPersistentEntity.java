package au.com.ibm.webapp.scaffold;

import java.io.Serializable;

public interface IMasterPersistentEntity<K> extends Serializable {
	String getSearchResultInfo();
	K getId();
}
