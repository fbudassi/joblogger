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

	@Test(expected = IllegalArgumentException.class)
	public void testLoadFromClassPathNull() throws IOException {
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
	public void testLoadFromClassPathInexistent() throws IOException {
		try {
			Properties props = PropertiesUtils.loadFromClasspath("inexistent.properties");
			assertThat(props.size(), is(0));
		} catch (IOException e) {
			fail();
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetIfBlankInvalidProps() {
		PropertiesUtils.setIfBlank(null, "test", "value");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetIfBlankInvalidKey() {
		Properties props = new Properties();
		PropertiesUtils.setIfBlank(props, null, "value");
	}

	@Test
	public void testSetIfBlankTrue() {
		Properties props = new Properties();
		PropertiesUtils.setIfBlank(props, "test", "value");

		assertThat(props.getProperty("test"), is("value"));
	}

	@Test
	public void testSetIfBlankFalse() {
		Properties props = new Properties();
		props.setProperty("test", "old");
		PropertiesUtils.setIfBlank(props, "test", "new");

		assertThat(props.getProperty("test"), is("old"));
	}
}
