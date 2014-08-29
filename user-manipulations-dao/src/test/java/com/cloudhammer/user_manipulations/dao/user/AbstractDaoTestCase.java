package com.cloudhammer.user_manipulations.dao.user;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * Setup for DBUnit
 */
public abstract class AbstractDaoTestCase {
	private static String url;
	private static EntityManager entityManager;
	protected static EntityManagerFactory entityManagerFactory;

	/**
	 * Initializes the database setup with DbUnit
	 *
	 * @throws Exception
	 * 		when something goes wrong.
	 */
	@BeforeClass
	public static void initializeDatabaseSetup() throws Exception {
		url = "jdbc:hsqldb:mem:TEST_DB";
		Class.forName("org.hsqldb.jdbcDriver");

		/* Create Test Schema */
		Connection conn = null;
		Statement st = null;
		try {
			conn = DriverManager.getConnection(url, "sa", "");
			st = conn.createStatement();
			try {
				st.executeUpdate("DROP SCHEMA TEST_SCHEMA CASCADE");
			} catch (SQLException e) {
				// Ignore Exception: Caused by Schema not yet Existing!
			}
			st.executeUpdate("CREATE SCHEMA TEST_SCHEMA AUTHORIZATION DBA");

		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					st = null;
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					conn = null;
				}
			}
		}
		entityManagerFactory = HsqlDbUtils.getEntityManagerFactory("MANIPULATION_PERSISTENCE");
		entityManager = entityManagerFactory.createEntityManager();
	}

	@Before
	public void setUp() throws Exception {
		// Initialize Entity manager
		getEntityManager();

		IDatabaseConnection conn = getConnection();
		DatabaseConfig config = conn.getConfig();
		config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new HsqldbDataTypeFactory());
		config.setProperty(DatabaseConfig.PROPERTY_TABLE_TYPE, new String[]{"VIEW", "TABLE", "MATERIALIZED VIEW"});
		IDataSet dataSet = getDataSet();
		DatabaseOperation.CLEAN_INSERT.execute(conn, dataSet);
		conn.close();
	}

	/**
	 * Retrieve the dataset.
	 *
	 * @return the {@link IDataSet}
	 * @throws DataSetException
	 * @throws MalformedURLException
	 */
	protected IDataSet getDataSet() throws DataSetException, MalformedURLException {
		String errMsg = "Dataset not found";
		FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
		builder.setColumnSensing(true);

		URL path = AbstractDaoTestCase.class.getClassLoader().getResource("dataset.xml");
		return builder.build(new File(path.getPath()));
	}

	protected static IDatabaseConnection getConnection() throws SQLException, DatabaseUnitException {
		return new DatabaseConnection(DriverManager.getConnection(url, "sa", ""), "TEST_SCHEMA");
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@AfterClass
	public static void tearDownSystem() {
		entityManager.close();
		entityManagerFactory.close();
	}

	/**
	 * @return EntityManager
	 */
	public synchronized EntityManager getEntityManager() {
		return entityManager;
	}
}
