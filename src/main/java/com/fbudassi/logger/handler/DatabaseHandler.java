package com.fbudassi.logger.handler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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

	/**
	 * SQL queries.
	 */
	private static final String CREATE_TABLE_SQL = "create table {table} (logTime timestamp not null, level varchar(32) not null,"
			+ "logger varchar(255) not null, message varchar(255) not null, sequence integer not null, threadID integer not null,"
			+ "stackTrace varchar(8192))";
	private static final String INSERT_SQL = "insert into {table} (logTime,level,logger,message,sequence,threadID,stackTrace) values (?,?,?,?,?,?,?)";
	private static final String TRUNCATE_SQL = "truncate table {table}";

	/**
	 * DatabaseHandler constructor.
	 * 
	 * @param connection
	 *            The connection to the database.
	 * @param table
	 *            The table name inside the database to store log messages.
	 * @throws SQLException
	 */
	public DatabaseHandler(Connection connection, String table) throws SQLException {
		if (connection == null) {
			throw new IllegalArgumentException("Parameter connection can't be null");
		}

		if (StringUtils.isBlank(table)) {
			throw new IllegalArgumentException("Parameter tableName can't be empty");
		}

		// If log table does not exist, create it.
		if (!isTablePresent(connection, table)) {
			connection.createStatement().executeQuery(CREATE_TABLE_SQL.replace(TABLE_KEY, table));
		}

		// Create prepared statements.
		psInsert = connection.prepareStatement(INSERT_SQL.replace(TABLE_KEY, table));
		psTruncate = connection.prepareStatement(TRUNCATE_SQL.replace(TABLE_KEY, table));

		// Set simple formatter instead of the default XMLFormatter().
		setFormatter(new SimpleFormatter());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void publish(LogRecord record) {
		try {
			psInsert.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
			psInsert.setString(2, StringUtils.truncate(record.getLevel().getName(), 32));
			psInsert.setString(3, StringUtils.truncate(record.getLoggerName(), 255));
			psInsert.setString(4, StringUtils.truncate(getFormatter().formatMessage(record), 255));
			psInsert.setLong(5, record.getSequenceNumber());
			psInsert.setInt(6, record.getThreadID());
			psInsert.setString(7, StringUtils.truncate(ExceptionUtils.getStackTrace(record.getThrown()), 8192));
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

	/**
	 * Check if a certain table exists in the database.
	 * 
	 * @param connection
	 * @param table
	 * @return
	 * @throws SQLException
	 */
	private boolean isTablePresent(Connection connection, String table) throws SQLException {
		ResultSet rs = connection.getMetaData().getTables(null, null, table.toUpperCase(), null);
		return rs.next();
	}
}