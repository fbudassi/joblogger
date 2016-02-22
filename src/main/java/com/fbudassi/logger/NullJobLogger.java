package com.fbudassi.logger;

import java.util.Properties;

/**
 * JobLogger that applies the Null Object pattern. Useful when something goes wrong getting a logger and we need to fail gracefully.
 * 
 * @author fbudassi
 */
public class NullJobLogger extends AbstractJobLogger {

	public static final NullJobLogger NULL_JOB_LOGGER = new NullJobLogger();

	/**
	 * Just one instance of this JobLogger should exist.
	 */
	private NullJobLogger() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void init(String name, Properties props) throws Exception {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(String msg) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(String msg, Object... params) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(String msg, Throwable thrown) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warning(String msg) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warning(String msg, Object... params) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warning(String msg, Throwable thrown) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void message(String msg) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void message(String msg, Object... params) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void message(String msg, Throwable thrown) {
	}
}