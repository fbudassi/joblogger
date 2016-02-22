package com.fbudassi.logger.util;

/**
 * Utilities to manipulate String objects.
 * 
 * @author fbudassi
 */
public class StringUtils {

	/**
	 * Private to prevent instantiation.
	 */
	private StringUtils() {
	}

	/**
	 * Checks if a String is not empty (""), not null and not whitespace only.
	 * 
	 * @param str
	 *            The string to check.
	 * @return True if the string is not blank.
	 */
	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	/**
	 * Checks if a String is whitespace, empty ("") or null.
	 * 
	 * @param str
	 *            The string to check.
	 * @return True if the string is blank.
	 */
	public static boolean isBlank(String str) {
		return str == null || str.trim().length() == 0;
	}

	/**
	 * Truncate a string to a specified length.
	 * 
	 * @param str
	 *            The string to be truncated.
	 * @param length
	 *            The maximum length of the string.
	 * @return The string truncated.
	 */
	public static String truncate(String str, int length) {
		if (length < 0) {
			throw new IllegalArgumentException("Parameter length should be greater than zero");
		}
		
		return str == null || str.length() <= length ? str : str.substring(0, length);
	}
}
