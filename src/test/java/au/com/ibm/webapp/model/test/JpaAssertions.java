package au.com.ibm.webapp.model.test;

import static junit.framework.TestCase.fail;

import javax.persistence.EntityManager;

import org.eclipse.persistence.internal.sessions.DatabaseSessionImpl;
import org.eclipse.persistence.sessions.Session;

public class JpaAssertions {

	public static void assertTableHasColumn(EntityManager manager,
			final String tableName, final String columnName) {
		DatabaseSessionImpl session = (DatabaseSessionImpl) manager.unwrap(Session.class);

		final ResultCollector rc = new ResultCollector();
				
		/*
		session.doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				ResultSet columns = connection.getMetaData().getColumns(null,
						null, tableName.toUpperCase(), null);
				while (columns.next()) {
					if (columns.getString(4).toUpperCase()
							.equals(columnName.toUpperCase())) {
						rc.found = true;
					}
				}
			}
		});
		*/

		if (!rc.found) {
			fail("Column [" + columnName + "] not found on table : "
					+ tableName);
		}
	}

	public static void assertTableExists(EntityManager manager,
			final String name) {
		DatabaseSessionImpl session = (DatabaseSessionImpl) manager.unwrap(Session.class);

		final ResultCollector rc = new ResultCollector();

		/*
		session.doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				ResultSet tables = connection.getMetaData().getTables(null,
						null, "%", null);
				while (tables.next()) {
					if (tables.getString(3).toUpperCase()
							.equals(name.toUpperCase())) {
						rc.found = true;
					}
				}
			}
		});
		*/
		
		if (!rc.found) {
			fail("Table not found in schema : " + name);
		}
	}

	static class ResultCollector {
		public boolean found = false;
	}

}
