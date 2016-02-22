package com.fbudassi.logger.util;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for ExceptionUtils.
 * 
 * @author fbudassi
 */
public class ExceptionUtilsTest {

	/**
	 * getStackTrace tests.
	 */
	@Test
	public void testGetStackTraceNull() {
		assertThat(ExceptionUtils.getStackTrace(null), is(nullValue()));
	}

	@Test
	public void testGetStackTraceException() {
		Exception e = new Exception();
		assertThat(ExceptionUtils.getStackTrace(e), allOf(is(notNullValue()), startsWith("java.lang.Exception")));
	}
}
