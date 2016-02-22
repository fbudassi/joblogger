package com.fbudassi.logger;

import java.util.logging.Level;

/**
 * Custom JobLogger Levels for java.util.logging.
 * 
 * @author fbudassi
 */
public class JobLoggerLevel extends Level {

	private static final long serialVersionUID = 7556854034214825464L;

	public static final Level ERROR = new JobLoggerLevel("ERROR", 3);
	public static final Level WARNING = new JobLoggerLevel("WARNING", 2);
	public static final Level MESSAGE = new JobLoggerLevel("MESSAGE", 1);

	protected JobLoggerLevel(String name, int value) {
		super(name, value);
	}
}