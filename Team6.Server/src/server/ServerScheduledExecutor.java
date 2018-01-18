package server;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import common.IStartable;
import common.NotRunningException;
import common.Startable;
import configurations.ServerConfiguration;
import configurations.TimeConfiguration;
import db.DbController;
import db.EntitiesResolver;
import db.QueryGenerator;
import general.ScheduledExecutor;
import general.ScheduledExecutor.IScheduledExecutionHandler;
import logger.LogManager;
import newEntities.IEntity;
import newEntities.ShopManager;

/**
 *
 * ServerScheduledExecutor: A class that responsible to execute all scheduled
 * operation in the server which related to the database.
 * 
 * @see Startable
 * @see IStartable
 */
public class ServerScheduledExecutor extends Startable {

	// region Constants

	private final static DateFormat s_dateForamt = new SimpleDateFormat("dd-MM-yyyy");

	// end region -> Constants

	// region Fields

	private ScheduledExecutor m_scheduledExecutor;

	private DbController m_dbController;

	private TimeConfiguration m_timeConfiguration;

	// end region -> Fields

	// region Constructors

	/**
	 * Create an instance of {@link ServerScheduledExecutor} which using
	 * {@link ScheduledExecutor} for execute all scheduled operation in the server
	 * which related to the database.
	 * 
	 * @param logger
	 *            A logger to write to it.
	 * 
	 * @param dbController
	 *            An instance of connection with DB.
	 * @param timeConfiguration
	 *            An instance of server configuration that related to time.
	 *
	 */
	public ServerScheduledExecutor(Logger logger, DbController dbController, TimeConfiguration timeConfiguration) {
		super(true, logger);
		m_dbController = dbController;
		m_timeConfiguration = timeConfiguration;

		m_scheduledExecutor = new ScheduledExecutor(true, logger, 1, 1, TimeUnit.HOURS) {
			@Override
			protected boolean toExecute() {
				return m_dbController.isRunning();
			}
		};
	}

	/**
	 * Create an instance of {@link ServerScheduledExecutor} which using
	 * {@link ScheduledExecutor} for execute all scheduled operation in the server
	 * which related to the database. Get logger from {@link LogManager#getLogger()}
	 * and server configuration from {@link ServerConfiguration#getInstance()}.
	 * 
	 * @param dbController
	 *            An instance of connection with DB.
	 */
	public ServerScheduledExecutor(DbController dbController) {
		this(LogManager.getLogger(), dbController, ServerConfiguration.getInstance().getTimeConfiguration());
	}

	// end region -> Constructors

	// region Startable Implementation

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initialStart() throws Exception {
		final SubscriptionCheckerTask checkerTask = new SubscriptionCheckerTask();
		m_scheduledExecutor.addTask(checkerTask, checkerTask);
		final ReportsGeneratorTask reportsTask = new ReportsGeneratorTask();
		m_scheduledExecutor.addTask(reportsTask, reportsTask);

		m_scheduledExecutor.Start();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initialStop() throws Exception {
		m_scheduledExecutor.Stop();
	}

	// end region -> Startable Implementation

	// region Nested Classes

	/**
	 * SubscriptionCheckerTask: This task is responsible for checking customer
	 * subscriptions, and resetting them in case they expire.
	 */
	private class SubscriptionCheckerTask implements Callable<Boolean>, IScheduledExecutionHandler<Boolean> {

		@Override
		public Boolean call() throws Exception {
			if (!m_dbController.isRunning()) {
				return false;
			}

			String lastSubsctiptionCheckDate = m_timeConfiguration.getLastSubsctiptionCheckDate();

			Date today = new Date();
			String todayString = s_dateForamt.format(today);

			if (lastSubsctiptionCheckDate != null && lastSubsctiptionCheckDate.equals(todayString)) {
				return null;
			}

			String query = QueryGenerator.checkCostumersSubscriptionQuery();

			try {
				CallableStatement callableStatement = m_dbController.getCallableStatement(query);
				if (callableStatement == null) {
					m_Logger.warning(
							"Received null CallableStatement from the database controller on the query: " + query);
					return false;
				}
				callableStatement.executeUpdate();
				m_timeConfiguration.setLastSubsctiptionCheckDate(todayString);

			} catch (NotRunningException ex) {
				m_Logger.warning(ex.getMessage());
				return false;
			} catch (Exception ex) {
				m_Logger.severe("Failed on try to exectute query: " + query + ", exception: " + ex.getMessage());
				return false;
			}

			return true;
		}

		@Override
		public void onScheduledExecution(Boolean executionResult) {
			if (executionResult == null) {
				return;
			}
			if (executionResult) {
				m_Logger.info("Daily subscriptions checking succeed.");
			} else {
				m_Logger.info(
						"Daily subscriptions checking failed, the next attempt will be in 1 hour. For more information look up the log.");
			}
		}
	}

	/**
	 * ReportsGeneratorTask: This task is responsible for generating reports in each
	 * quarter of a year.
	 */
	private class ReportsGeneratorTask implements Callable<Boolean>, IScheduledExecutionHandler<Boolean> {

		@Override
		public Boolean call() throws Exception {
			if (!m_dbController.isRunning()) {
				return false;
			}

			String lastSubsctiptionCheckDate = m_timeConfiguration.getLastGenerationDate();
			Date date = s_dateForamt.parse(lastSubsctiptionCheckDate);

			Calendar lastGenerationDate = Calendar.getInstance();
			lastGenerationDate.setTime(date);

			Calendar today = Calendar.getInstance();
			int currentQuarter = today.get(Calendar.MONTH) / 3 + 1;

			if (lastGenerationDate.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
				int generationQuarter = lastGenerationDate.get(Calendar.MONTH) / 3 + 1;
				if (generationQuarter == currentQuarter) {
					return null;
				}
			}

			String selectQuery = QueryGenerator.selectAllShopManagersQuery();

			ResultSet queryResult = null;
			List<IEntity> entities;

			try {
				queryResult = m_dbController.executeSelectQuery(selectQuery);
				if (queryResult == null) {
					m_Logger.warning("Failed on try to execute select query : " + selectQuery);
					return false;
				}
				entities = EntitiesResolver.ResultSetToEntity(queryResult, ShopManager.class);
				if (entities == null) {
					m_Logger.warning("Failed on try to reslove ResultSet to ShopManager.");
					return false;
				}

			} catch (Exception e) {
				m_Logger.warning("Failed on try to execute select query! query: " + selectQuery + ", exception: "
						+ e.getMessage());
				return false;
			} finally {
				if (queryResult != null) {
					try {
						queryResult.close();
					} catch (SQLException e) {
						m_Logger.warning("Failed on try to close the ResultSet of the executed query:" + selectQuery
								+ ", exception: " + e.getMessage());
					}
				}
			}

			String year = Integer.toString(today.get(Calendar.YEAR));

			for (IEntity entity : entities) {
				if (!(entity instanceof ShopManager)) {
					continue;
				}
				ShopManager shopManager = (ShopManager) entity;

				// Generation of incomes report:
				String query = QueryGenerator.generateIncomesReportQuery(shopManager.getId(), year, currentQuarter);
				try {
					CallableStatement callableStatement = m_dbController.getCallableStatement(query);
					callableStatement.executeQuery();
				} catch (NotRunningException ex) {
					m_Logger.warning(ex.getMessage());
					return false;
				} catch (Exception ex) {
					m_Logger.severe("Failed on try to exectute query: " + query + ", exception: " + ex.getMessage());
				}

				// Generation of complaints report:
				query = QueryGenerator.generateComplaintsReportQuery(shopManager.getId(), year, currentQuarter);
				try {
					CallableStatement callableStatement = m_dbController.getCallableStatement(query);
					callableStatement.executeQuery();
				} catch (NotRunningException ex) {
					m_Logger.warning(ex.getMessage());
					return false;
				} catch (Exception ex) {
					m_Logger.severe("Failed on try to exectute query: " + query + ", exception: " + ex.getMessage());
				}

				// Generation of reservations report:
				query = QueryGenerator.generateReservationsReportQuery(shopManager.getId(), year, currentQuarter);
				try {
					CallableStatement callableStatement = m_dbController.getCallableStatement(query);
					callableStatement.executeQuery();
				} catch (NotRunningException ex) {
					m_Logger.warning(ex.getMessage());
					return false;
				} catch (Exception ex) {
					m_Logger.severe("Failed on try to exectute query: " + query + ", exception: " + ex.getMessage());
				}

				// Generation of surveys report:
				query = QueryGenerator.generateSurveysReportQuery(shopManager.getId(), year, currentQuarter);
				try {
					CallableStatement callableStatement = m_dbController.getCallableStatement(query);
					callableStatement.executeQuery();
				} catch (NotRunningException ex) {
					m_Logger.warning(ex.getMessage());
					return false;
				} catch (Exception ex) {
					m_Logger.severe("Failed on try to exectute query: " + query + ", exception: " + ex.getMessage());
				}
			}
			m_timeConfiguration.setLastGenerationDate(s_dateForamt.format(new Date()));

			return true;
		}

		@Override
		public void onScheduledExecution(Boolean executionResult) {
			if (executionResult == null) {
				return;
			}

			int currentQuarter = Calendar.getInstance().get(Calendar.MONTH) / 3 + 1;

			if (executionResult) {

				m_Logger.info("Reports generation of " + currentQuarter + " quarter succeed.");
			} else {
				m_Logger.info("Reports generation of " + currentQuarter
						+ " quarter failed, the next attempt will be in 1 hour. For more information look up the log.");
			}
		}
	}

	// end region -> Nested Classes

}
