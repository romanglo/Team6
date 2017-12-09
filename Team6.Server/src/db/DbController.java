package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.istack.internal.NotNull;

import common.IStartable;
import common.NotRunningException;
import common.Startable;
import common.StartableState;
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
				String errormessage = "Failed to intialize MySQL driver!";
				m_Logger.log(Level.SEVERE, errormessage, e);
				throw e;
			}
		}

		String connectionString = "jdbc:mysql://" + m_dbConfiguration.getIp() + '/' + m_dbConfiguration.getSchema();

		try {
			m_connection = DriverManager.getConnection(connectionString, m_dbConfiguration.getUsername(),
					m_dbConfiguration.getPassword());
		} catch (SQLException e) {
			m_connection = null;
			String errormessage = "Failed to open connection with MySql server!";
			m_Logger.log(Level.SEVERE, errormessage, e);
			throw e;
		}
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

			String errormessage = "Failed to close the connection to MySql!";
			m_Logger.log(Level.SEVERE, errormessage, e);

			throw e;
		}

	}

	// end region -> Startable Implementation

	// region Public Methods

	/**
	 * 
	 * The method prints all the rows in product table.
	 *
	 * @throws NotRunningException
	 *             if the method called when the class not in
	 *             {@link StartableState#Running}
	 */
	public void printAllProducts() throws NotRunningException {
		if (m_State != StartableState.Running) {
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
	 * The method execute update query in the DB.
	 *
	 * @param query the query to execute.
	 */
	public void executeUpdate(String query) {
		if (m_State != StartableState.Running) {
			throw new NotRunningException(this);
		}

		Statement stmt;
		try {
			stmt = m_connection.createStatement();
			int result = stmt.executeUpdate(query);
			if (result == 1) {
				m_Logger.info("A query executed seccessfully! The query: " + query);
			} else {
				m_Logger.warning("Failed on try to execute the query: " + query);
			}
		} catch (SQLException ex) {
			m_Logger.log(Level.SEVERE, "Failed on try to execute the query: " + query, ex);
		}
	}
	// end region -> Public Methods

}
