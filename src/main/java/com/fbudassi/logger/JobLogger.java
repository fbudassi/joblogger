package com.fbudassi.logger;

public interface JobLogger {

	/**
	 * Log an ERROR Level message.
	 * 
	 * @param msg
	 *            The message to log.
	 */
	public void error(String msg);

	/**
	 * Log an ERROR Level message. Allow parameters to be replaced in the message.
	 * 
	 * @param msg
	 *            The message to log.
	 * @param params
	 *            Parameters to be replaced in the message.
	 */
	public void error(String msg, Object... params);

	/**
	 * Log an ERROR Level Message. Allow a throwable to be logged.
	 * 
	 * @param msg
	 *            The message to log.
	 * @param thrown
	 *            A throwable to log its stack trace.
	 */
	public void error(String msg, Throwable thrown);

	/**
	 * Log a WARNING Level message.
	 * 
	 * @param msg
	 *            The message to log.
	 */
	public void warning(String msg);

	/**
	 * Log a WARNING Level message. Allow parameters to be replaced in the message.
	 * 
	 * @param msg
	 *            The message to log.
	 * @param params
	 *            Parameters to be replaced in the message.
	 */
	public void warning(String msg, Object... params);

	/**
	 * Log a WARNING Level Message. Allow a throwable to be logged.
	 * 
	 * @param msg
	 *            The message to log.
	 * @param thrown
	 *            A throwable to log its stack trace.
	 */
	public void warning(String msg, Throwable thrown);

	/**
	 * Log a MESSAGE Level message.
	 * 
	 * @param msg
	 *            The message to log.
	 */
	public void message(String msg);

	/**
	 * Log a MESSAGE Level message. Allow parameters to be replaced in the message.
	 * 
	 * @param msg
	 *            The message to log.
	 * @param params
	 *            Parameters to be replaced in the message.
	 */
	public void message(String msg, Object... params);

	/**
	 * Log a MESSAGE Level Message. Allow a throwable to be logged.
	 * 
	 * @param msg
	 *            The message to log.
	 * @param thrown
	 *            A throwable to log its stack trace.
	 */
	public void message(String msg, Throwable thrown);
}
