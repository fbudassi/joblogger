package com.fbudassi.logger;

import java.io.IOException;
import java.util.Properties;

import com.fbudassi.logger.util.PropertiesUtils;

/**
 * Utility class for producing JobLogger instances.
 * 
 * @author fbudassi
 */
public final class JobLoggerFactory {

	private static Properties props;

	private static final String DEFAULT_PROPERTIES_FILE = "joblogger.properties";

	/**
	 * Read properties file to build loggers.
	 */
	static {
		try {
			// Load properties.
			props = PropertiesUtils.loadFromClasspath(DEFAULT_PROPERTIES_FILE);
		} catch (IOException e) {
			System.err.println("Error reading JobLoggerFactory properties file: " + e.getMessage());
			e.printStackTrace();

			props = new Properties();
		}

		// Set defaults if no values were set.
		for (JobLoggerProperty jlp : JobLoggerProperty.values()) {
			if (jlp.getDefault() != null) {
				PropertiesUtils.setIfBlank(props, jlp.getKey(), jlp.getDefault());
			}
		}
	}

	/**
	 * Private to prevent instantiation.
	 */
	private JobLoggerFactory() {
	}

	/**
	 * Find or create a logger for the given clazz. Depending on the JobLogger implementation, it could return an already existing logger based on clazz.
	 * 
	 * @param clazz
	 * @return
	 * @throws IOException
	 */
	public static JobLogger getLogger(Class<?> clazz) {
		return getLogger(clazz.getCanonicalName());
	}

	/**
	 * Find or create a logger for the given name. Depending on the JobLogger implementation, it could return an already existing logger based on name.
	 * 
	 * @param name
	 *            The name of the JobLogger.
	 * @return The concrete implementation of JobLogger.
	 */
	public static JobLogger getLogger(String name) {
		return getLogger(name, props);
	}

	/**
	 * Find or create a logger for the given name. Depending on the JobLogger implementation, it could return an already existing logger based on name.
	 * 
	 * @param name
	 *            The name of the JobLogger.
	 * @param props
	 *            A series of config properties to initialize the JobLogger.
	 * @return The concrete implementation of JobLogger.
	 */
	@SuppressWarnings("unchecked")
	static JobLogger getLogger(String name, Properties props) {
		try {
			// Get JobLogger concrete implementation to use.
			Class<AbstractJobLogger> jlClass = (Class<AbstractJobLogger>) Class.forName(props.getProperty(JobLoggerProperty.IMPLEMENTATION.getKey()));

			// Get implementation's constructor and construct the logger.
			AbstractJobLogger jl = jlClass.getDeclaredConstructor(new Class[] {}).newInstance();

			// Initialize concrete JobLogger.
			jl.init(name, props);

			return jl;
		} catch (Exception e) {
			System.err.println("Unable to get JobLogger: " + e.getMessage());
			e.printStackTrace();

			// Return the only instance of NullJobLogger to fail gracefully and avoid stopping the execution of the class' clients.
			return NullJobLogger.NULL_JOB_LOGGER;
		}
	}
}
