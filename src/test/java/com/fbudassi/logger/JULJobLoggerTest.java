package com.fbudassi.logger;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.fbudassi.logger.handler.DatabaseHandler;

/**
 * Tests for JULJobLogger.
 * 
 * Regarding the lack of isolation with third party objects in the folling tests (JULJobLogger dependencies are not mocked), the tests will use a sociable
 * approach to the unit testing regarding external dependencies (like java.util.logging, java.sql, etc.). Those third party dependencies won't be mocked since
 * they don't affect the unit test speed enough and are thoroughly tested to trust them. (Ref: http://martinfowler.com/bliki/UnitTest.html).
 * 
 * @author fbudassi
 */
@RunWith(MockitoJUnitRunner.class)
public class JULJobLoggerTest {

	private static final String LOG_MESSAGE = "Test log message";
	private static final String LOG_MESSAGE_PARAM = "Test log message: {0}";
	private static final String PARAM = "test_param";

	/**
	 * Database connection properties.
	 */
	private static final String DRIVER = "org.hsqldb.jdbcDriver";
	private static final String URL = "jdbc:hsqldb:mem:logdb";
	private static final String USER = "sa";
	private static final String PASSWORD = "";
	private static final String TABLE = "joblogger";

	/**
	 * JULJobLogger instance to be used in the logger.message()/warning()/error() methods.
	 */
	private static JULJobLogger logger = new JULJobLogger();

	/**
	 * Initialize logger.
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public static void initLogger() throws Exception {
		logger.init("initLogger()", getProperties(false, false, false));
	}

	/**
	 * init tests.
	 * 
	 * @throws Exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInitNameNull() throws Exception {
		JULJobLogger logger = new JULJobLogger();
		logger.init(null, getProperties(false, false, false));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInitPropsNull() throws Exception {
		JULJobLogger logger = new JULJobLogger();
		logger.init("testInitPropsNull", null);
	}

	@Test
	public void testInitNoHandlers() throws Exception {
		JULJobLogger jjl = new JULJobLogger();
		jjl.init("testInitNoHandlers", getProperties(false, false, false));

		Logger jul = jjl.getInnerLogger();
		assertThat(jul.getName(), is("testInitNoHandlers"));
		assertThat(jul.getLevel(), is(JobLoggerLevel.ALL));
	}

	@Test
	public void testInitConsole() throws Exception {
		JULJobLogger jjl = new JULJobLogger();
		jjl.init("testInitConsole", getProperties(true, false, false));

		Logger jul = jjl.getInnerLogger();
		assertThat(jul.getHandlers().length, is(1));
		assertThat(jul.getHandlers()[0], is(instanceOf(ConsoleHandler.class)));
	}

	@Test
	public void testInitFile() throws Exception {
		JULJobLogger jjl = new JULJobLogger();
		jjl.init("testInitFile", getProperties(false, true, false));

		Logger jul = jjl.getInnerLogger();
		assertThat(jul.getHandlers().length, is(1));
		assertThat(jul.getHandlers()[0], is(instanceOf(FileHandler.class)));
	}

	@Test
	public void testInitDb() throws Exception {
		JULJobLogger jjl = new JULJobLogger();
		jjl.init("testInitDb", getProperties(false, false, true));

		Logger jul = jjl.getInnerLogger();
		assertThat(jul.getHandlers().length, is(1));
		assertThat(jul.getHandlers()[0], is(instanceOf(DatabaseHandler.class)));

		Connection conn = jjl.getOpenConnection();
		assertThat(conn, is(notNullValue()));
	}

	/**
	 * error tests.
	 */
	@Test
	public void testErrorBasic() {
		logger.error(LOG_MESSAGE);
	}

	@Test
	public void testErrorParams() {
		logger.error(LOG_MESSAGE_PARAM, PARAM);
	}

	@Test
	public void testErrorThrown() {
		logger.error(LOG_MESSAGE, new Exception());
	}

	/**
	 * warning tests.
	 */
	@Test
	public void testWarningBasic() {
		logger.warning(LOG_MESSAGE);
	}

	@Test
	public void testWarningParams() {
		logger.warning(LOG_MESSAGE_PARAM, PARAM);
	}

	@Test
	public void testWarningThrown() {
		logger.warning(LOG_MESSAGE, new Exception());
	}

	/**
	 * message tests.
	 */
	@Test
	public void testMessageBasic() {
		logger.message(LOG_MESSAGE);
	}

	@Test
	public void testMessageParams() {
		logger.message(LOG_MESSAGE_PARAM, PARAM);
	}

	@Test
	public void testMessageThrown() {
		logger.message(LOG_MESSAGE, new Exception());
	}

	/**
	 * Return a Properties instance basically setted up depending on the handlers chosen.
	 * 
	 * @param console
	 * @param file
	 * @param db
	 * @return
	 */
	private static Properties getProperties(boolean console, boolean file, boolean db) {
		Properties props = new Properties();

		// Set default properties' values.
		for (JobLoggerProperty jlp : JobLoggerProperty.values()) {
			if (jlp.getDefault() != null) {
				props.setProperty(jlp.getKey(), jlp.getDefault());
			}
		}

		props.setProperty(JobLoggerProperty.CONSOLE_ENABLED.getKey(), String.valueOf(console));
		props.setProperty(JobLoggerProperty.FILE_ENABLED.getKey(), String.valueOf(file));
		props.setProperty(JobLoggerProperty.DB_ENABLED.getKey(), String.valueOf(db));

		// Set testing environment db properties' values.
		if (db) {
			props.setProperty(JobLoggerProperty.DB_DRIVER.getKey(), DRIVER);
			props.setProperty(JobLoggerProperty.DB_URL.getKey(), URL);
			props.setProperty(JobLoggerProperty.DB_USER.getKey(), USER);
			props.setProperty(JobLoggerProperty.DB_PASSWORD.getKey(), PASSWORD);
			props.setProperty(JobLoggerProperty.DB_TABLE.getKey(), TABLE);
		}

		return props;
	}
}
