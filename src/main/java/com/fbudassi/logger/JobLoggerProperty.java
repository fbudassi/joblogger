package com.fbudassi.logger;

/**
 * JobLogger configuration properties' keys.
 * 
 * @author fbudassi
 */
public enum JobLoggerProperty {
	// Global properties
	IMPLEMENTATION("joblogger.implementation", JULJobLogger.class.getCanonicalName()),
	MIN_LEVEL("joblogger.min.level", "ALL"),

	// Console handler properties
	CONSOLE_ENABLED("joblogger.handler.console.enabled", "true"),

	// File Handler properties
	FILE_ENABLED("joblogger.handler.file.enabled", "false"),
	FILE_APPEND("joblogger.handler.file.append", "true"),
	FILE_DESTINATION("joblogger.handler.file.destination", "log"),

	// Database Handler properties
	DB_ENABLED("joblogger.handler.db.enabled", "false"),
	DB_DRIVER("joblogger.handler.db.driver", null),
	DB_URL("joblogger.handler.db.url", null),
	DB_USER("joblogger.handler.db.user", null),
	DB_PASSWORD("joblogger.handler.db.password", null),
	DB_TABLE("joblogger.handler.db.table", "log");

	private final String key;
	private final String defaultValue;

	private JobLoggerProperty(String key, String defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
	}

	/**
	 * Return the property key.
	 * 
	 * @return
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Return the property default value.
	 * 
	 * @return
	 */
	public String getDefault() {
		return defaultValue;
	}
}
