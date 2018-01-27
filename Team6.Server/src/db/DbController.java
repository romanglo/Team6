package db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import com.sun.istack.internal.NotNull;

import common.IStartable;
import common.NotRunningException;
import common.Startable;
import configurations.DbConfiguration;
import configurations.ServerConfiguration;
import logger.LogManager;

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

	/**
	 * 
	 * Create a instance which communicates with the database, you must
	 * {@link IStartable#Start()} the class before using it. The instance get logger
	 * from {@link LogManager#getLogger()} and get the configuration from
	 * {@link ServerConfiguration#getInstance()}.
	 * 
	 */
	public DbController() {
		this(LogManager.getLogger(), ServerConfiguration.getInstance().getDbConfiguration());
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
		m_Logger.info("Connection with MySQL database created successfully! connection string: " + connectionString);
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
		m_Logger.info("Connection with MySQL database closed successfully!");

	}

	// end region -> Startable Implementation

	// region Public Methods

	/**
	 * 
	 * The method execute select query.
	 *
	 * @param query
	 *            The query to execute.
	 * @return A {@link ResultSet} with the query result.
	 * @throws NotRunningException
	 *             thrown if the method called when the {@link IStartable} is not in
	 *             running state.
	 * 
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
			return stmt.executeQuery(query);
		} catch (SQLException ex) {
			m_Logger.warning("Failed on try to execute the query: " + query + " , exception: " + ex);
			return null;
		}
	}

	/**
	 * Executes the given SQL query, which may be an INSERT, UPDATE, or DELETE
	 * statement or an SQL statement that returns nothing.
	 *
	 * @param query
	 *            The query to execute.
	 * @return true if the update succeed and false if does not.
	 * @throws NotRunningException
	 *             thrown if the method called when the {@link IStartable} is not in
	 *             running state.
	 * 
	 */
	public boolean executeQuery(String query) throws NotRunningException {
		if (!isRunning()) {
			throw new NotRunningException(this);
		}
		if (query == null || query.isEmpty()) {
			return false;
		}

		try {
			Statement stmt = m_connection.createStatement();
			stmt.executeUpdate(query);
			return true;
		} catch (SQLException ex) {
			m_Logger.warning("Failed on try to execute the query: " + query + " , exception: " + ex);
			return false;
		}
	}

	/**
	 * The method create a {@link PreparedStatement} that related to the connection.
	 *
	 * @param sql
	 *            An query that matching to {@link PreparedStatement} rules.
	 * @return A {@link PreparedStatement} that related to the connected schema.
	 * @throws NotRunningException
	 *             thrown if the method called when the {@link IStartable} is not in
	 *             running state.
	 * 
	 */
	public PreparedStatement getPreparedStatement(String sql) throws NotRunningException {
		if (!isRunning()) {
			throw new NotRunningException(this);
		}

		if (sql == null) {
			return null;
		}

		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = m_connection.prepareStatement(sql);
		} catch (SQLException e) {
			m_Logger.warning("Failed on try to create prepere statement with the query: " + sql + ", exception:"
					+ e.getMessage());
		}
		return preparedStatement;
	}

	/**
	 * The method create a {@link Statement} that related to the connection.
	 *
	 * @return A {@link Statement} that related to the connected schema.
	 */
	public Statement getStatement() {
		if (!isRunning()) {
			throw new NotRunningException(this);
		}

		Statement stmt = null;
		try {
			stmt = m_connection.createStatement();
		} catch (SQLException ex) {
			m_Logger.warning("Failed on try to create statement, exception: " + ex);
		}
		return stmt;
	}

	/**
	 * The method create a {@link CallableStatement} that related to the connection.
	 *
	 * @param sql
	 *            An query that matching to {@link CallableStatement} rules.
	 * @return A {@link CallableStatement} that related to the connected schema.
	 * @throws NotRunningException
	 *             thrown if the method called when the {@link IStartable} is not in
	 *             running state.
	 */
	public CallableStatement getCallableStatement(String sql) throws NotRunningException {
		if (!isRunning()) {
			throw new NotRunningException(this);
		}

		if (sql == null) {
			return null;
		}

		CallableStatement callableStatement = null;
		try {
			callableStatement = m_connection.prepareCall(sql);
		} catch (SQLException e) {
			m_Logger.warning("Failed on try to create callable statement with the query: " + sql + ", exception:"
					+ e.getMessage());
		}
		return callableStatement;
	}

	// end region -> Public Methods
}
