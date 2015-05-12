package org.apache.james.jamesui.backend.test.servers.configuration;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllConfigurationTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllConfigurationTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(DnsConfigManagerTest.class);
		suite.addTestSuite(ImapServerConfigManagerTest.class);
		suite.addTestSuite(LmtpServerConfigManagerTest.class);
		suite.addTestSuite(Pop3serverConfigManagerTest.class);
		suite.addTestSuite(SmtpServerConfigManagerTest.class);
		//$JUnit-END$
		return suite;
	}

}
