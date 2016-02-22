package com.fbudassi.logger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
		return getLogger(clazz.getName());
	}

	/**
	 * Find or create a logger for the given name. Depending on the JobLogger implementation, it could return an already existing logger based on name.
	 * 
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static JobLogger getLogger(String name) {
		try {
			// Get JobLogger concrete implementation to use.
			Class<JobLogger> jobLogger = (Class<JobLogger>) Class.forName(props.getProperty(JobLoggerProperty.IMPLEMENTATION.getKey()));

			// Get implementation's constructor and build the logger.
			return jobLogger.getDeclaredConstructor(new Class[] {String.class, Properties.class}).newInstance(name, props);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			System.err.println("Unable to get JobLogger: " + e.getMessage());
			e.printStackTrace();

			// Return the only instance of NullJobLogger to fail gracefully and avoid stopping the execution of the class' clients.
			return NullJobLogger.NULL_JOB_LOGGER;
		}
	}
}