package com.fbudassi.logger;

import org.junit.Test;

/**
 * Tests for NullJobLogger.
 * 
 * As this is a "Null Object" pattern object, nothing should happen when executing the methods in any way. For the same reason, there is nothing to assert,
 * since the only thing that could go wrong is an exception to be thrown.
 * 
 * @author fbudassi
 */
public class NullJobLoggerTest {

	private static final String LOG_MESSAGE = "Test log message";
	private static final String LOG_MESSAGE_PARAM = "Test log message: {0}";
	private static final String PARAM = "test_param";

	private static NullJobLogger logger = NullJobLogger.NULL_JOB_LOGGER;

	/**
	 * init tests.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testInit() throws Exception {
		logger.init(null, null);
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
}
