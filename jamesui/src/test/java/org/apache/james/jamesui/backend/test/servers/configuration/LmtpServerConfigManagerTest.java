package org.apache.james.jamesui.backend.test.servers.configuration;

import java.io.File;

import junit.framework.TestCase;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.james.jamesui.backend.configuration.bean.LmtpServer;
import org.apache.james.jamesui.backend.configuration.manager.EnvironmentConfigurationReader;
import org.apache.james.jamesui.backend.configuration.manager.LmtpServerConfigManager;

/**
 * Test case for read/write operations on the configuration file lmtpserver.xml
 * @author fulvio
 *
 */
public class LmtpServerConfigManagerTest extends TestCase {
	
	private static final String JAMES_CONF_FOLDER = EnvironmentConfigurationReader.getCurrentDir()+File.separator+"src/test/resources";
	private static final String TARGET_FILE_NAME = "lmtpserver.xml";
	
	private LmtpServerConfigManager lmtpServerConfigManager;

	protected void setUp() throws Exception {
		super.setUp();
		
		//remove old test result
		File dnsFile = new File(JAMES_CONF_FOLDER+File.separator+TARGET_FILE_NAME);
		if(dnsFile.exists())
		   dnsFile.delete();
		
		lmtpServerConfigManager = new LmtpServerConfigManager();
	}

	public void testReadConfiguration() throws ConfigurationException {
		
		LmtpServer lmtpServer =	lmtpServerConfigManager.readConfiguration(JAMES_CONF_FOLDER);	
		assertNotNull(lmtpServer);	
		System.out.println(lmtpServer.toString());		
	}

	
	public void testUpdateConfiguration() throws ConfigurationException {
		
		//part 1: store a new configuration				
		LmtpServer lmtpServer = new LmtpServer();

		lmtpServer.setLmtpserverEnabled(true);
		lmtpServer.setBindAddress("3.4.5.6:877");
		lmtpServer.setConnectionBacklog("cntbklg");
		lmtpServer.setConnectionTimeout("678");
		lmtpServer.setHelloNameAutoDetect(false);
		lmtpServer.setHelloName("hellonam");
		lmtpServer.setConnectionLimit("444");
		lmtpServer.setConnectionLimitPerIP("555");
		lmtpServer.setMaxmessagesize("999");
		lmtpServer.setLmtpGreeting("greeeeet");
						
		System.out.println("LmapServer to save: "+lmtpServer.toString());
		
		lmtpServerConfigManager.updateConfiguration(lmtpServer,JAMES_CONF_FOLDER);
				
		//part 2: check previously stored configuration
		LmtpServer loadedConfig = lmtpServerConfigManager.readConfiguration(JAMES_CONF_FOLDER);
		
		System.out.println("LmapServer loaded config: "+loadedConfig.toString());
			
		assertNotNull(loadedConfig);	
		assertNotNull(loadedConfig.isLmtpserverEnabled());
		assertNotNull(loadedConfig.getBindAddress());
		assertNotNull(loadedConfig.getConnectionBacklog());
		assertNotNull(loadedConfig.getConnectionTimeout());
		assertNotNull(loadedConfig.isHelloNameAutoDetect());
		assertNotNull(loadedConfig.getHelloName());
		assertNotNull(loadedConfig.getConnectionLimit());
		assertNotNull(loadedConfig.getConnectionLimitPerIP());
		assertNotNull(loadedConfig.getMaxmessagesize());
		assertNotNull(loadedConfig.getLmtpGreeting());
	
		
		assertEquals(true, loadedConfig.isLmtpserverEnabled());
		assertEquals("3.4.5.6:877",loadedConfig.getBindAddress());
		assertEquals("cntbklg",loadedConfig.getConnectionBacklog());
		assertEquals("678",loadedConfig.getConnectionTimeout());
		assertEquals(false,loadedConfig.isHelloNameAutoDetect());
		assertEquals("hellonam",loadedConfig.getHelloName());
		assertEquals("444",loadedConfig.getConnectionLimit());
		assertEquals("555",loadedConfig.getConnectionLimitPerIP());
		assertEquals("999",loadedConfig.getMaxmessagesize());
		assertEquals("greeeeet",loadedConfig.getLmtpGreeting());
   }

}
