package com.fbudassi.logger.handler;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.LogRecord;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fbudassi.logger.JULJobLoggerLevel;

/**
 * Tests for DatabaseHandler. It uses an in-memory RDBMS to avoid mocking JDBC.
 * 
 * @author fbudassi
 */
public class DatabaseHandlerTest {

	private static final String LOG_MESSAGE = "Test log message";

	/**
	 * Database connection properties.
	 */
	private static final String DRIVER = "org.hsqldb.jdbcDriver";
	private static final String URL = "jdbc:hsqldb:mem:logdb";
	private static final String USER = "sa";
	private static final String PASSWORD = "";
	private static final String TABLE = "joblogger";

	/**
	 * Database queries.
	 */
	private static final String SELECT_COUNT_FROM_TABLE = "SELECT COUNT(*) FROM " + TABLE;
	private static final String SELECT_FROM_TABLE = "SELECT * FROM " + TABLE;

	/**
	 * Database Connection. It's not worth mocking the connection since an in-memory SQL db can do a good job here.
	 */
	private static Connection connection;

	/**
	 * Open database Connection to avoid reopening it over and over again, which is computationally expensive.
	 * 
	 * @throws SQLException
	 */
	@BeforeClass
	public static void openConnection() throws SQLException {
		// Load JDBC driver.
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Could not load JDBC driver class [" + DRIVER + "]", e);
		}

		Properties connProps = new Properties();
		connProps.setProperty("user", USER);
		connProps.setProperty("password", PASSWORD);

		// Open connection.
		connection = DriverManager.getConnection(URL, connProps);

		// Disable auto-commit to let us rollback changes after every test. This gives us isolation between tests.
		connection.setAutoCommit(false);
	}

	@After
	public void rollbackTestChanges() throws SQLException {
		connection.rollback();
	}

	/**
	 * Constructor tests.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateDatabaseHandlerNullConnection() {
		try {
			new DatabaseHandler(null, TABLE);
		} catch (SQLException e) {
			fail();
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateDatabaseHandlerNullTable() {
		try {
			new DatabaseHandler(connection, null);
		} catch (SQLException e) {
			fail();
		}
	}

	/**
	 * publish tests.
	 * 
	 * @throws SQLException
	 */
	@Test
	public void testPublishSingleLogRecord() throws SQLException {
		// Build logrecord.
		LogRecord lr = new LogRecord(JULJobLoggerLevel.ERROR, LOG_MESSAGE);
		lr.setLoggerName(this.getClass().getCanonicalName());

		// Publish logrecord.
		DatabaseHandler dbh = new DatabaseHandler(connection, TABLE);
		dbh.publish(lr);

		// Assert that a single LogRecord was correctly published.
		ResultSet countRs = connection.createStatement().executeQuery(SELECT_COUNT_FROM_TABLE);
		assertThat(countRs.next(), is(true));
		assertThat(countRs.getInt(1), is(1));

		// Assert that the correct LogRecord was published.
		ResultSet selectRs = connection.createStatement().executeQuery(SELECT_FROM_TABLE);
		assertThat(selectRs.next(), is(true));
		assertThat(selectRs.getString(1), is(notNullValue()));
		assertThat(selectRs.getString(2), is(JULJobLoggerLevel.ERROR.toString()));
		assertThat(selectRs.getString(3), is(this.getClass().getCanonicalName()));
		assertThat(selectRs.getString(4), is(LOG_MESSAGE));
		assertThat(selectRs.getString(5), is(notNullValue()));
		assertThat(selectRs.getString(6), is(notNullValue()));
		assertThat(selectRs.getString(7), is(nullValue()));
		assertThat(selectRs.next(), is(false));
	}

	@Test
	public void testPublishTwoLogRecords() throws SQLException {
		// Build logrecords.
		LogRecord firstLr = new LogRecord(JULJobLoggerLevel.ERROR, LOG_MESSAGE);
		firstLr.setLoggerName(this.getClass().getCanonicalName());

		LogRecord secondLr = new LogRecord(JULJobLoggerLevel.WARN, LOG_MESSAGE);
		secondLr.setLoggerName(this.getClass().getCanonicalName());

		// Publish logrecords.
		DatabaseHandler dbh = new DatabaseHandler(connection, TABLE);
		dbh.publish(firstLr);
		dbh.publish(secondLr);

		// Assert that a single LogRecord was correctly published.
		ResultSet countRs = connection.createStatement().executeQuery(SELECT_COUNT_FROM_TABLE);
		assertThat(countRs.next(), is(true));
		assertThat(countRs.getInt(1), is(2));

		// Assert that the correct LogRecord was published.
		ResultSet selectRs = connection.createStatement().executeQuery(SELECT_FROM_TABLE);
		assertThat(selectRs.next(), is(true));
		assertThat(selectRs.getString(2), is(JULJobLoggerLevel.ERROR.toString()));
		assertThat(selectRs.next(), is(true));
		assertThat(selectRs.getString(2), is(JULJobLoggerLevel.WARN.toString()));
		assertThat(selectRs.next(), is(false));
	}

	@Test
	public void testPublishStackTraceLogRecord() throws SQLException {
		// Build logrecord.
		LogRecord lr = new LogRecord(JULJobLoggerLevel.ERROR, LOG_MESSAGE);
		lr.setLoggerName(this.getClass().getCanonicalName());
		lr.setThrown(new Exception()); // add a throwable object

		// Publish logrecord.
		DatabaseHandler dbh = new DatabaseHandler(connection, TABLE);
		dbh.publish(lr);

		// Assert that a single LogRecord was correctly published.
		ResultSet countRs = connection.createStatement().executeQuery(SELECT_COUNT_FROM_TABLE);
		assertThat(countRs.next(), is(true));
		assertThat(countRs.getInt(1), is(1));

		// Assert that the correct LogRecord was published.
		ResultSet selectRs = connection.createStatement().executeQuery(SELECT_FROM_TABLE);
		assertThat(selectRs.next(), is(true));
		assertThat(selectRs.getString(1), is(notNullValue()));
		assertThat(selectRs.getString(2), is(JULJobLoggerLevel.ERROR.toString()));
		assertThat(selectRs.getString(3), is(this.getClass().getCanonicalName()));
		assertThat(selectRs.getString(4), is(LOG_MESSAGE));
		assertThat(selectRs.getString(5), is(notNullValue()));
		assertThat(selectRs.getString(6), is(notNullValue()));
		assertThat(selectRs.getString(7), is(notNullValue()));
		assertThat(selectRs.next(), is(false));
	}
}
