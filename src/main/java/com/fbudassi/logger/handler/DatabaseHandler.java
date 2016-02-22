package com.fbudassi.logger.handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.ErrorManager;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

import com.fbudassi.logger.util.ExceptionUtils;
import com.fbudassi.logger.util.StringUtils;

/**
 * Custom JDBC Database Handler for java.util.logging.
 * 
 * @author fbudassi
 */
public class DatabaseHandler extends Handler {

	private Connection connection;
	private PreparedStatement psInsert;
	private PreparedStatement psTruncate;

	private static final String TABLE_KEY = "{table}";
	private static final String INSERT_SQL = "insert into {table} (level,logger,message,sequence,sourceClass,sourceMethod,threadID,timeEntered,stackTrace) values (?,?,?,?,?,?,?,?,?)";
	private static final String TRUNCATE_SQL = "truncate table {table}";

	/**
	 * DatabaseHandler constructor.
	 * 
	 * @param driver
	 *            The database driver.
	 * @param connectionString
	 *            The connection string to the database.
	 * @param props
	 *            A set of properties to open the connection to the database.
	 * @param tableName
	 *            The table name inside the database to store log messages.
	 * @throws SQLException
	 */
	public DatabaseHandler(String driver, String connectionString, Properties props, String tableName) throws SQLException {
		if (StringUtils.isBlank(driver)) {
			throw new IllegalArgumentException("Parameter driver can't be empty");
		}

		if (StringUtils.isBlank(connectionString)) {
			throw new IllegalArgumentException("Parameter connectionString can't be empty");
		}

		if (StringUtils.isBlank(tableName)) {
			throw new IllegalArgumentException("Parameter tableName can't be empty");
		}

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Could not load JDBC driver class [" + driver + "]", e);
		}

		connection = DriverManager.getConnection(connectionString, props);
		psInsert = connection.prepareStatement(INSERT_SQL.replace(TABLE_KEY, tableName));
		psTruncate = connection.prepareStatement(TRUNCATE_SQL.replace(TABLE_KEY, tableName));

		setFormatter(new SimpleFormatter());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void publish(LogRecord record) {
		// Check if this record should be omitted.
		if (getFilter() != null && !getFilter().isLoggable(record)) {
			return;
		}

		// Store the log entry.
		try {
			psInsert.setInt(1, record.getLevel().intValue());
			psInsert.setString(2, StringUtils.truncate(record.getLoggerName(), 64));
			psInsert.setString(3, StringUtils.truncate(getFormatter().formatMessage(record), 255));
			psInsert.setLong(4, record.getSequenceNumber());
			psInsert.setString(5, StringUtils.truncate(record.getSourceClassName(), 64));
			psInsert.setString(6, StringUtils.truncate(record.getSourceMethodName(), 32));
			psInsert.setInt(7, record.getThreadID());
			psInsert.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
			psInsert.setString(9, StringUtils.truncate(ExceptionUtils.getStackTrace(record.getThrown()), 8192));
			psInsert.executeUpdate();
		} catch (SQLException e) {
			reportError(e.getMessage(), e, ErrorManager.WRITE_FAILURE);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			reportError(e.getMessage(), e, ErrorManager.CLOSE_FAILURE);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void flush() {
	}

	/**
	 * Clear log entries from the database table.
	 */
	public void clear() {
		try {
			psTruncate.executeUpdate();
		} catch (SQLException e) {
			reportError(e.getMessage(), e, ErrorManager.GENERIC_FAILURE);
		}
	}
}