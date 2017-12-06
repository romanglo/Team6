import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import com.sun.istack.internal.NotNull;

import java.sql.Connection;

import common.IStartable;
import common.NotRunningException;
import common.Startable;
import common.StartableState;
import configurations.DbConfiguration;
import logs.LogManager;

/**
 *
 * DbController:
 * A class which communicates with the database
 * 
 * @see IStartable
 */
public class DbController extends Startable {
	
	// region Fields

	private Connection m_connection = null;
	
	private DbConfiguration m_dbConfiguration;

	// end region -> Fields

	// region Constructors

	/**
	 * 
	 *  Create a instance which communicates with the database, you must {@link IStartable#Start()} the class before using it.
	 *
	 * @param dbConfiguration The configuration of the database.
	 */
	public DbController(@NotNull DbConfiguration dbConfiguration) {
		super(false, LogManager.getLogger());
		m_dbConfiguration = dbConfiguration;
		
	}
	
	// end region -> Constructors


	// region Startable Implementation

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initialStart() throws SQLException 
	{
		String connectionString = "jdbc:mysql://"+m_dbConfiguration.getIp()+'/'+m_dbConfiguration.getSchema();
		try {
			//			m_connection = DriverManager.getConnection("jdbc:mysql://localhost/zer-li","root","123456");

			m_connection = DriverManager.getConnection(connectionString,m_dbConfiguration.getUsername(),m_dbConfiguration.getPassword());
		} catch (SQLException e) {
			m_connection=null;			
			String errormessage = "Failed to open connection with MySql server!";
			m_Logger.log(Level.SEVERE,errormessage, e);
			throw e;
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initialStop() throws SQLException
	{
		if(m_connection==null) {
			return;
		}

		try {
			m_connection.close();
		} catch (SQLException e) {
			m_connection=null;
			
			String errormessage = "Failed to close the connection to MySql!";
			m_Logger.log(Level.SEVERE,errormessage, e);
			
			throw e;
		}
		
	}

	// end region -> Startable Implementation

	//region Public Methods
	
	/**
	 * 
	 * The method prints all the rows in product table.
	 *
	 * @throws NotRunningException if the method called when the class not in {@link StartableState#Running}
	 */
	public void printAllProducts() throws NotRunningException
	{
		if(m_State != StartableState.Running) {
			throw new NotRunningException(this);
		}
		
		Statement stmt;
		try 
		{
			stmt = m_connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM product;");
	 		while(rs.next())
	 		{
				 // Print out the values
				 System.out.println("ID: " + rs.getString(1) +", Name: "+rs.getString(2) +", Type: "+rs.getString(3));
			} 
			rs.close();
		} catch (SQLException ex) {			
			System.out.println("Failed to print all the products! Exception: " + ex.getMessage());
		}
	}
	
	//end region -> Public Methods
	
}

