package com.fbudassi.logger;

import java.util.logging.Level;

/**
 * Custom JobLogger Levels for java.util.logging.
 * 
 * @author fbudassi
 */
public class JULJobLoggerLevel extends Level {

	private static final long serialVersionUID = 7556854034214825464L;

	public static final Level ERROR = new JULJobLoggerLevel("ERROR", 3);
	public static final Level WARN = new JULJobLoggerLevel("WARN", 2);
	public static final Level MESSAGE = new JULJobLoggerLevel("MESSAGE", 1);

	protected JULJobLoggerLevel(String name, int value) {
		super(name, value);
	}
}