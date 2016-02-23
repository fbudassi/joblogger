package com.fbudassi.logger.util;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Properties;

import org.junit.Test;

/**
 * Tests for PropertiesUtils.
 * 
 * @author fbudassi
 */
public class PropertiesUtilsTest {

	private static final String TEST_KEY = "test";

	/**
	 * loadFromClasspath tests.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testLoadFromClasspathNull() throws IOException {
		PropertiesUtils.loadFromClasspath(null);
	}

	@Test
	public void testLoadFromClassPathExistent() {
		try {
			Properties props = PropertiesUtils.loadFromClasspath("joblogger.properties");
			assertThat(props.size(), is(greaterThan(0)));
		} catch (IOException e) {
			fail();
		}
	}

	@Test
	public void testLoadFromClassPathExistentSlash() {
		try {
			Properties props = PropertiesUtils.loadFromClasspath("/joblogger.properties");
			assertThat(props.size(), is(greaterThan(0)));
		} catch (IOException e) {
			fail();
		}
	}

	@Test
	public void testLoadFromClassPathNonExistent() throws IOException {
		try {
			Properties props = PropertiesUtils.loadFromClasspath("nonexistent.properties");
			assertThat(props.size(), is(0));
		} catch (IOException e) {
			fail();
		}
	}

	/**
	 * setIfBlank tests.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetIfBlankInvalidProps() {
		PropertiesUtils.setIfBlank(null, TEST_KEY, "");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetIfBlankInvalidKey() {
		Properties props = new Properties();
		PropertiesUtils.setIfBlank(props, null, "");
	}

	@Test
	public void testSetIfBlankTrue() {
		Properties props = new Properties();
		PropertiesUtils.setIfBlank(props, TEST_KEY, "value");

		assertThat(props.getProperty(TEST_KEY), is("value"));
	}

	@Test
	public void testSetIfBlankFalse() {
		Properties props = new Properties();
		props.setProperty(TEST_KEY, "old");
		PropertiesUtils.setIfBlank(props, TEST_KEY, "new");

		assertThat(props.getProperty(TEST_KEY), is("old"));
	}
}
