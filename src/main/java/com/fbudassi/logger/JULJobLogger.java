package com.fbudassi.logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fbudassi.logger.handler.DatabaseHandler;
import com.fbudassi.logger.util.StringUtils;

/**
 * JobLogger implementation that uses java.util.logging (JUL).
 * 
 * @author fbudassi
 */
public class JULJobLogger extends AbstractJobLogger {

	private Logger logger;

	private Connection connection;

	/**
	 * Create a java.util.logging based logger.
	 */
	protected JULJobLogger() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void init(String name, Properties props) throws Exception {
		logger = Logger.getLogger(name);
		logger.setLevel(Level.parse(props.getProperty(JobLoggerProperty.MIN_LEVEL.getKey())));

		// Console Handler.
		if (Boolean.parseBoolean(props.getProperty(JobLoggerProperty.CONSOLE_ENABLED.getKey()))) {
			logger.addHandler(new ConsoleHandler());
		}

		// File Handler.
		if (Boolean.parseBoolean(props.getProperty(JobLoggerProperty.FILE_ENABLED.getKey()))) {
			String dest = props.getProperty(JobLoggerProperty.FILE_DESTINATION.getKey());
			boolean append = Boolean.parseBoolean(props.getProperty(JobLoggerProperty.FILE_APPEND.getKey()));

			logger.addHandler(new FileHandler(dest, append));
		}

		// Database Handler.
		if (Boolean.parseBoolean(props.getProperty(JobLoggerProperty.DB_ENABLED.getKey()))) {
			String driver = props.getProperty(JobLoggerProperty.DB_DRIVER.getKey());
			String url = props.getProperty(JobLoggerProperty.DB_URL.getKey());
			String table = props.getProperty(JobLoggerProperty.DB_TABLE.getKey());

			Properties connProps = new Properties();
			connProps.setProperty("user", props.getProperty(JobLoggerProperty.DB_USER.getKey()));
			connProps.setProperty("password", props.getProperty(JobLoggerProperty.DB_PASSWORD.getKey()));

			logger.addHandler(new DatabaseHandler(getConnection(driver, url, connProps), table));
		}
	}

	/**
	 * Get a connection to the database. If a connection was already open, return that one instead.
	 * 
	 * @param driver
	 *            JDBC driver to use.
	 * @param url
	 *            JDBC url to the database.
	 * 
	 * @param connProps
	 *            Connection properties (user, password).
	 * @return The connection.
	 * @throws SQLException
	 */
	private synchronized Connection getConnection(String driver, String url, Properties connProps) throws SQLException {
		// Return connection if already open.
		if (connection != null) {
			return connection;
		}

		// Check parameters.
		if (StringUtils.isBlank(driver)) {
			throw new IllegalArgumentException("Parameter driver can't be empty");
		}

		if (StringUtils.isBlank(url)) {
			throw new IllegalArgumentException("Parameter connectionString can't be empty");
		}

		// Load JDBC driver.
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Could not load JDBC driver class [" + driver + "]", e);
		}

		// Open connection.
		return connection = DriverManager.getConnection(url, connProps);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(String msg) {
		logger.log(JobLoggerLevel.ERROR, msg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(String msg, Object... params) {
		logger.log(JobLoggerLevel.ERROR, msg, params);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(String msg, Throwable thrown) {
		logger.log(JobLoggerLevel.ERROR, msg, thrown);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warning(String msg) {
		logger.log(JobLoggerLevel.WARNING, msg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warning(String msg, Object... params) {
		logger.log(JobLoggerLevel.WARNING, msg, params);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warning(String msg, Throwable thrown) {
		logger.log(JobLoggerLevel.WARNING, msg, thrown);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void message(String msg) {
		logger.log(JobLoggerLevel.MESSAGE, msg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void message(String msg, Object... params) {
		logger.log(JobLoggerLevel.MESSAGE, msg, params);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void message(String msg, Throwable thrown) {
		logger.log(JobLoggerLevel.MESSAGE, msg, thrown);
	}
}