package com.greathammer.eqm.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LogTest {

	private static Log log = LogFactory.getLog(LogTest.class);

	public static void main(String[] args) {
		log.trace("This is trace log.");
		log.info("This is debug log.");
		log.info("This is info log.");
		log.error("This is error log.");
		log.fatal("This is fatal log.");
	}
}
