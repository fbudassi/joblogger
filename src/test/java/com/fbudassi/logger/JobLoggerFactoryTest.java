package com.fbudassi.logger;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.Test;

/**
 * Tests for JobLoggerFactory.
 * 
 * @author fbudassi
 */
public class JobLoggerFactoryTest {

	/**
	 * getProps test.
	 */
	@Test
	public void testGetProperties() {
		Properties props = JobLoggerFactory.getProperties();

		assertThat(props, is(notNullValue()));
		assertThat(props.size(), is(JobLoggerProperty.values().length));
		assertThat(props.getProperty(JobLoggerProperty.FILE_ENABLED.getKey()), is("true"));
		assertThat(props.getProperty(JobLoggerProperty.MIN_LEVEL.getKey()), is(JobLoggerProperty.MIN_LEVEL.getDefault()));
	}

	/**
	 * getLogger tests.
	 */
	@Test
	public void testGetLoggerNullName() {
		JobLogger logger = JobLoggerFactory.getLogger(null, JobLoggerFactory.getProperties());

		assertThat(logger, is(instanceOf(NullJobLogger.class)));
	}

	@Test
	public void testGetLoggerUnknownImplementation() {
		Properties props = (Properties) JobLoggerFactory.getProperties();
		props.setProperty(JobLoggerProperty.IMPLEMENTATION.getKey(), "com.fbudassi.logger.UnknownJobLogger");
		JobLogger logger = JobLoggerFactory.getLogger(this.getClass().getCanonicalName(), props);

		assertThat(logger, is(instanceOf(NullJobLogger.class)));
	}

	@Test
	public void testGetLoggerFromProperties() {
		JobLogger logger = JobLoggerFactory.getLogger(this.getClass().getCanonicalName(), JobLoggerFactory.getProperties());

		assertThat(logger, is(instanceOf(JULJobLogger.class)));
		JULJobLogger jjl = (JULJobLogger) logger;
		assertThat(jjl.getLogger(), is(notNullValue()));
		assertThat(jjl.getLogger().getHandlers().length, is(3));
		assertThat(jjl.getConnection(), is(notNullValue()));
	}
}
