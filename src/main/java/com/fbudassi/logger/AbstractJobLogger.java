package com.fbudassi.logger;

import java.util.Properties;

/**
 * Abstract JobLogger. Used to define the protected init() method that is needed by JobLoggerFactory but not by any other JobLogger client.
 * 
 * @author fbudassi
 */
public abstract class AbstractJobLogger implements JobLogger {

	/**
	 * Initialize this concrete implementation of JobLogger.
	 * 
	 * @param name
	 *            The name of the logger.
	 * @param props
	 *            A list of properties to initialize the JobLogger.
	 * @throws Exception
	 *             Any type of exception that might get thrown during initialization.
	 */
	protected abstract void init(String name, Properties props) throws Exception;
}