package org.apache.james.jamesui.backend.test.servers.configuration;

import java.io.File;

import junit.framework.TestCase;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.james.jamesui.backend.configuration.bean.JamesuiConfiguration;
import org.apache.james.jamesui.backend.configuration.bean.SmtpServer;
import org.apache.james.jamesui.backend.configuration.manager.EnvironmentConfigurationReader;
import org.apache.james.jamesui.backend.configuration.manager.JamesuiConfigurationManager;
import org.apache.james.jamesui.backend.configuration.manager.Pop3serverConfigManager;
import org.apache.james.jamesui.backend.configuration.manager.SmtpServerConfigManager;


/**
 * Test case for read/write operations on the configuration file smtpserver.xml
 * @author fulvio
 *
 */
public class SmtpServerConfigManagerTest extends TestCase {
	
	private static final String FAKE_JAMES_BASE_FOLDER = EnvironmentConfigurationReader.getCurrentDir()+File.separator+"src/test/resources";
	
	private SmtpServerConfigManager smtpServerConfigManager;
	private static final String TARGET_FILE_NAME = "smtpserver.xml";

	protected void setUp() throws Exception {
		
		super.setUp();
		
		JamesuiConfigurationManager jamesuiConfigurationManager = new JamesuiConfigurationManager();
		JamesuiConfiguration jamesuiConfiguration = jamesuiConfigurationManager.loadConfiguration();
		
		//remove old test result
		File dnsFile = new File(FAKE_JAMES_BASE_FOLDER+File.separator+TARGET_FILE_NAME);
		if(dnsFile.exists())
		   dnsFile.delete();	
		
		jamesuiConfiguration.setJamesBaseFolder(FAKE_JAMES_BASE_FOLDER);
		smtpServerConfigManager = new SmtpServerConfigManager(jamesuiConfiguration);	
	}

	public void testReadConfiguration() throws ConfigurationException {
		
		SmtpServer smtpServer = smtpServerConfigManager.readConfiguration();
		assertNotNull(smtpServer);
		System.out.println(smtpServer.toString());		
	}

	public void testUpdateConfiguration() throws ConfigurationException {
		
		//part 1: store a new configuration				
		SmtpServer smtpServer = new SmtpServer();
				
		smtpServer.setSmtpServerEnabled(false);
		smtpServer.setBindAddress("3.4.5.6:877");
		smtpServer.setConnectionBacklog("cntbacklog");
		smtpServer.setAuthorizedAddresses("a.b.c.d");
		smtpServer.setMaxmessagesize("333");
		smtpServer.setConnectiontimeout("678");
		smtpServer.setKeystore("file://mykey");
		smtpServer.setSecret("mysecret");
		smtpServer.setAlgorithm("myalgo");
		smtpServer.setProvider("org.myprovider.com");		
		smtpServer.setHelloNameAutoDetect(false);
		smtpServer.setHelloName("hellonam");
		smtpServer.setConnectionLimit("444");		
		smtpServer.setConnectionLimitPerIP("555");		
		smtpServer.setAddressBracketsEnforcement(false);
		
		smtpServer.setSocketTls(true);  //new
		smtpServer.setStartTls(false);  //new
		smtpServer.setGreeting("GREAT"); //new
		
		System.out.println("SmtpServer to store: "+smtpServer.toString());
							
		smtpServerConfigManager.updateConfiguration(smtpServer);
						
		//part 2: check previously stored configuration
		SmtpServer loadedConfig = smtpServerConfigManager.readConfiguration();
		
		System.out.println("SmtpServer loaded: "+loadedConfig.toString());
						
		assertNotNull(loadedConfig);	
		assertNotNull(loadedConfig.isSmtpServerEnabled());
		assertNotNull(loadedConfig.getBindAddress());
		assertNotNull(loadedConfig.getConnectionBacklog());
		assertNotNull(loadedConfig.getConnectiontimeout());
		assertNotNull(loadedConfig.isHelloNameAutoDetect());
		assertNotNull(loadedConfig.getHelloName());
		assertNotNull(loadedConfig.getConnectionLimit());
		assertNotNull(loadedConfig.getConnectionLimitPerIP());	
		assertNotNull(loadedConfig.getAlgorithm());
		assertNotNull(loadedConfig.getKeystore());
		assertNotNull(loadedConfig.getSecret());
		assertNotNull(loadedConfig.getProvider());
		assertNotNull(loadedConfig.getAuthorizedAddresses());
		assertNotNull(loadedConfig.getMaxmessagesize());
		assertNotNull(loadedConfig.isAddressBracketsEnforcement());
				
				
		assertEquals(false,loadedConfig.isSmtpServerEnabled());
		assertEquals("3.4.5.6:877",loadedConfig.getBindAddress());
		assertEquals("a.b.c.d",loadedConfig.getAuthorizedAddresses());
		assertEquals("cntbacklog",loadedConfig.getConnectionBacklog());
		assertEquals("678",loadedConfig.getConnectiontimeout());
		assertEquals(false,loadedConfig.isHelloNameAutoDetect());
		assertEquals("hellonam",loadedConfig.getHelloName());
		assertEquals("333",loadedConfig.getMaxmessagesize());
		assertEquals("444",loadedConfig.getConnectionLimit());
		assertEquals("555",loadedConfig.getConnectionLimitPerIP());
		assertEquals("myalgo",loadedConfig.getAlgorithm());
		assertEquals("file://mykey",loadedConfig.getKeystore());
		assertEquals("mysecret",loadedConfig.getSecret());
		assertEquals("org.myprovider.com",loadedConfig.getProvider());
		assertEquals(false,loadedConfig.isAddressBracketsEnforcement());
		
		assertEquals(true,loadedConfig.isSocketTls());
		assertEquals(false,loadedConfig.isStartTls());
		assertEquals("GREAT",loadedConfig.getGreeting());		
	}

}
