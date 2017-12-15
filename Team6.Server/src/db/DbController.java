package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import com.sun.istack.internal.NotNull;

import common.IStartable;
import common.NotRunningException;
import common.Startable;
import configurations.DbConfiguration;

/**
 *
 * DbController: A class which communicates with MySQL database.
 * 
 * @see IStartable
 */
public class DbController extends Startable {

	// region Constants

	/**
	 * My SQL driver name.
	 */
	public static final String MY_SQL_DRIVER_NAME = "com.mysql.jdbc.Driver";

	// end region -> Constants

	// region Fields

	private static boolean s_driverInitialized = false;

	private Connection m_connection;

	private DbConfiguration m_dbConfiguration;

	// end region -> Fields

	// region Constructors

	/**
	 * 
	 * Create a instance which communicates with the database, you must
	 * {@link IStartable#Start()} the class before using it.
	 * 
	 * @param logger
	 *            A logger to log to it.
	 *
	 * @param dbConfiguration
	 *            The configuration of the database.
	 */
	public DbController(@NotNull Logger logger, @NotNull DbConfiguration dbConfiguration) {
		super(true, logger);
		m_dbConfiguration = dbConfiguration;
		m_connection = null;
	}

	// end region -> Constructors

	// region Startable Implementation

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initialStart() throws Exception {

		if (!s_driverInitialized) {
			try {
				Class.forName(MY_SQL_DRIVER_NAME).newInstance();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				m_Logger.severe("Failed to intialize MySQL driver! exception: " + e.getMessage());
				throw e;
			}
		}

		String connectionString = "jdbc:mysql://" + m_dbConfiguration.getIp() + '/' + m_dbConfiguration.getSchema();

		try {
			m_connection = DriverManager.getConnection(connectionString, m_dbConfiguration.getUsername(),
					m_dbConfiguration.getPassword());
		} catch (SQLException e) {
			m_connection = null;
			m_Logger.severe("Failed to open connection with MySql server! exception: " + e.getMessage());
			throw e;
		}
		m_Logger.info("Connection with MySQL database created! connection string: " + connectionString);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initialStop() throws SQLException {
		if (m_connection == null) {
			return;
		}

		try {
			m_connection.close();
		} catch (SQLException e) {
			m_connection = null;
			m_Logger.severe("Failed to close the connection to MySql! exception: " + e.getMessage());
			throw e;
		}
		m_Logger.info("Connection with MySQL database closed!");

	}

	// end region -> Startable Implementation

	// region Public Methods

	/**
	 * 
	 * The method prints all the rows in product table.
	 *
	 * @throws NotRunningException
	 *             if the method called when the state is not Running.
	 * 
	 */
	public void printAllProducts() throws NotRunningException {
		if (!isRunning()) {
			throw new NotRunningException(this);
		}

		Statement stmt;
		try {
			stmt = m_connection.createStatement();
			ResultSet resultSet = stmt.executeQuery("SELECT * FROM product;");
			while (resultSet.next()) {
				// Print out the values
				System.out.println("ID: " + resultSet.getString(1) + ", Name: " + resultSet.getString(2) + ", Type: "
						+ resultSet.getString(3));
			}
			resultSet.close();
		} catch (SQLException ex) {
			System.out.println("Failed to print all the products! Exception: " + ex.getMessage());
		}
	}

	/**
	 * 
	 * The method execute select query.
	 *
	 * @param query
	 *            The query to execute.
	 * @return A {@link ResultSet} with the query result.
	 * @throws NotRunningException
	 *             if the method called when the state is not Running.
	 */
	public ResultSet executeSelectQuery(String query) throws NotRunningException {
		if (!isRunning()) {
			throw new NotRunningException(this);
		}
		if (query == null || query.isEmpty()) {
			return null;
		}

		Statement stmt;
		try {
			stmt = m_connection.createStatement();
			ResultSet queryReuslt = stmt.executeQuery(query);
			m_Logger.info("A select query executed seccessfully! The query: " + query);
			return queryReuslt;
		} catch (SQLException ex) {
			m_Logger.warning("Failed on try to execute the query: " + query + " , exception: " + ex);
			return null;
		}
	}

	/**
	 * 
	 * The method execute update query.
	 *
	 * @param query
	 *            The query to execute.
	 * @return true if the update succeed and false if does not.
	 * @throws NotRunningException
	 *             if the method called when the state is not Running.
	 */
	public boolean executeUpdateQuery(String query) throws NotRunningException {
		if (!isRunning()) {
			throw new NotRunningException(this);
		}
		if (query == null || query.isEmpty()) {
			return false;
		}

		Statement stmt;
		try {
			stmt = m_connection.createStatement();
			boolean result = stmt.executeUpdate(query) == 1;
			if (result) {
				m_Logger.info("A update query executed seccessfully! The query: " + query);
			} else {
				m_Logger.warning("Failed on try to execute the update query: " + query);
			}
			return result;
		} catch (SQLException ex) {
			m_Logger.warning("Failed on try to execute the query: " + query + " , exception: " + ex);
			return false;
		}
	}

	// end region -> Public Methods

}
