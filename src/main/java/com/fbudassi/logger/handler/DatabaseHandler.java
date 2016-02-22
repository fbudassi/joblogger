package com.fbudassi.logger.handler;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
	private static final String INSERT_SQL = "insert into {table} (level,logger,message,sequence,sourceClass,sourceMethod,threadID,timeEntered,stackTrace) values (?,?,?,?,?,?,?,?,?)";
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

		psInsert = connection.prepareStatement(INSERT_SQL.replace(TABLE_KEY, table));
		psTruncate = connection.prepareStatement(TRUNCATE_SQL.replace(TABLE_KEY, table));

		setFormatter(new SimpleFormatter());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void publish(LogRecord record) {
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