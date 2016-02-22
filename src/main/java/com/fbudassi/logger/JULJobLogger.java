package com.fbudassi.logger;

import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JobLogger implementation that uses java.util.logging (JUL).
 * 
 * @author fbudassi
 */
public class JULJobLogger implements JobLogger {

	private final Logger logger;

	protected JULJobLogger(String name, Properties props) {
		logger = Logger.getLogger(name);
		
		// Configure logger and handlers.
		logger.setLevel(Level.parse(props.getProperty(JobLoggerProperty.MIN_LEVEL.getKey())));
		
		if (Boolean.parseBoolean(props.getProperty(JobLoggerProperty.CONSOLE_ENABLED.getKey()))) {
			logger.addHandler(new ConsoleHandler());
		}
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