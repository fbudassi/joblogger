package com.fbudassi.logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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
		if (StringUtils.isBlank(name)) {
			throw new IllegalArgumentException("Parameter name can't be blank");
		}

		if (props == null) {
			throw new IllegalArgumentException("Parameter props can't be null");
		}

		// Force the VM to load custom log levels before we use JobLoggerLevel.parse().
		Class.forName(JobLoggerLevel.class.getCanonicalName());

		// Get logger and set minimum log level.
		logger = Logger.getLogger(name);
		logger.setLevel(JobLoggerLevel.parse(props.getProperty(JobLoggerProperty.MIN_LEVEL.getKey())));

		// Console Handler.
		if (Boolean.parseBoolean(props.getProperty(JobLoggerProperty.CONSOLE_ENABLED.getKey()))) {
			ConsoleHandler ch = new ConsoleHandler();
			ch.setLevel(JobLoggerLevel.ALL);

			logger.addHandler(ch);
		}

		// File Handler.
		if (Boolean.parseBoolean(props.getProperty(JobLoggerProperty.FILE_ENABLED.getKey()))) {
			String dest = props.getProperty(JobLoggerProperty.FILE_DESTINATION.getKey());
			boolean append = Boolean.parseBoolean(props.getProperty(JobLoggerProperty.FILE_APPEND.getKey()));

			Handler fh = new FileHandler(dest, append);
			fh.setFormatter(new SimpleFormatter());

			logger.addHandler(fh);
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
			throw new IllegalArgumentException("Parameter url can't be empty");
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
	 * Return the inner java.util.logging.Logger.
	 * 
	 * @return
	 */
	protected Logger getInnerLogger() {
		return logger;
	}

	/**
	 * Return the open Connectio to the database.
	 * 
	 * @return
	 */
	protected Connection getOpenConnection() {
		return connection;
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
		logger.log(JobLoggerLevel.WARN, msg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warning(String msg, Object... params) {
		logger.log(JobLoggerLevel.WARN, msg, params);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warning(String msg, Throwable thrown) {
		logger.log(JobLoggerLevel.WARN, msg, thrown);
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