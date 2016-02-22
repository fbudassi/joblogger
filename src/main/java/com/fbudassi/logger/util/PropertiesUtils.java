package com.fbudassi.logger.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utilities to manipulate Properties objects.
 * 
 * @author fbudassi
 */
public class PropertiesUtils {

	/**
	 * Private to prevent instantiation.
	 */
	private PropertiesUtils() {
	}

	/**
	 * Load a properties file from a classpath resource.
	 * 
	 * @param resource
	 * @return
	 * @throws IOException
	 */
	public static Properties loadFromClasspath(String resource) throws IOException {
		if (!resource.startsWith("/")) {
			resource = "/" + resource;
		}

		Properties props;
		try (InputStream is = PropertiesUtils.class.getResourceAsStream(resource)) {
			props = new Properties();
			if (is != null) {
				props.load(is);
			}
		}
		return props;
	}

	/**
	 * Set the value of a certain property key if it is not defined or is blank.
	 * 
	 * @param props
	 * @param key
	 * @param value
	 */
	public static void setIfBlank(Properties props, String key, String value) {
		if (StringUtils.isBlank(props.getProperty(key))) {
			props.setProperty(key, value);
		}
	}
}
