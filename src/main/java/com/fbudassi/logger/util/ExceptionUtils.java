package com.fbudassi.logger.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Utilities to manipulate Throwable objects.
 * 
 * @author fbudassi
 */
public class ExceptionUtils {

	/**
	 * Private to prevent instantiation.
	 */
	private ExceptionUtils() {
	}

	/**
	 * Gets the stack trace from a Throwable as a String.
	 *
	 * @param throwable
	 *            the Throwable to be examined
	 * @return the stack trace as a String
	 */
	public static String getStackTrace(final Throwable thrown) {
		if (thrown == null) {
			return null;
		}

		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw, true);
		thrown.printStackTrace(pw);
		return sw.getBuffer().toString();
	}
}
