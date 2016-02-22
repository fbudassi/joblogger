package com.fbudassi.logger.util;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * Tests for StringUtils.
 * 
 * @author fbudassi
 */
public class StringUtilsTest {

	private static final String TEST_STRING = "test-string";

	/**
	 * isBlank tests.
	 */
	@Test
	public void testIsBlankNull() {
		assertThat(StringUtils.isBlank(null), is(true));
	}

	@Test
	public void testIsBlankEmpty() {
		assertThat(StringUtils.isBlank(""), is(true));
	}

	@Test
	public void testIsBlankSpaces() {
		assertThat(StringUtils.isBlank("   "), is(true));
	}

	@Test
	public void testIsBlankFalse() {
		assertThat(StringUtils.isBlank(TEST_STRING), is(false));
	}

	/**
	 * isNotBlank tests.
	 */
	@Test
	public void testIsNotBlankNull() {
		assertThat(StringUtils.isNotBlank(null), is(false));
	}

	@Test
	public void testIsNotBlankEmpty() {
		assertThat(StringUtils.isNotBlank(""), is(false));
	}

	@Test
	public void testIsNotBlankSpaces() {
		assertThat(StringUtils.isNotBlank("   "), is(false));
	}

	@Test
	public void testIsNotBlankFalse() {
		assertThat(StringUtils.isNotBlank(TEST_STRING), is(true));
	}

	/**
	 * truncate tests.
	 */
	@Test
	public void testTruncateNull() {
		assertThat(StringUtils.truncate(null, 10), is(nullValue()));
	}

	@Test
	public void testTruncateGreaterThan() {
		assertThat(StringUtils.truncate(TEST_STRING, TEST_STRING.length() + 1), is(TEST_STRING));
	}

	@Test
	public void testTruncateEquals() {
		assertThat(StringUtils.truncate(TEST_STRING, TEST_STRING.length()), is(TEST_STRING));
	}

	@Test
	public void testTruncateLessThan() {
		assertThat(StringUtils.truncate(TEST_STRING, TEST_STRING.length() - 1), is(TEST_STRING.substring(0, TEST_STRING.length() - 1)));
	}
}
