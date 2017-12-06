import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;

import common.NotRunningException;
import common.Startable;
import common.StartableState;

/**
 *
 * DbController:
 * TODO Roman: Auto-generated type stub - Change with type description
 * 
 */
public class DbController extends Startable {
	
	// region Fields

	
	private Connection m_connection = null;

	// end region -> Fields

	// region Constructors

	protected DbController(boolean throwable, Logger logger) {
		super(throwable, logger);
		// TODO Auto-generated constructor stub
	}
	
	// end region -> Constructors

	// region Public Methods

	

	// end region -> Public Methods

	// region Startable Implementation

	@Override
	protected void initialStart() throws SQLException 
	{
		try {
			m_connection = DriverManager.getConnection("jdbc:mysql://localhost/zer-li","root","123456");
		} catch (SQLException e) {
			m_connection=null;			
			String errormessage = "Failed to open connection with MySql server!";
			m_Logger.log(Level.SEVERE,errormessage, e);
			throw e;
		}
		
	}

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
	 * TODO Roman: Auto-generated comment stub - Change it!
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
	
	
	// region Nested Classes


	// end region -> Nested Classes
}

