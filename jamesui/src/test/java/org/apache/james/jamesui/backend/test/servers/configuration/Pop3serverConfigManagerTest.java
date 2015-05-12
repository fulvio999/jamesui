package org.apache.james.jamesui.backend.test.servers.configuration;

import java.io.File;

import junit.framework.TestCase;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.james.jamesui.backend.configuration.bean.Pop3server;
import org.apache.james.jamesui.backend.configuration.manager.EnvironmentConfigurationReader;
import org.apache.james.jamesui.backend.configuration.manager.Pop3serverConfigManager;

/**
 * Test case for read/write operations on the configuration file pop3server.xml
 * @author fulvio
 *
 */
public class Pop3serverConfigManagerTest extends TestCase {
	
	private static final String JAMES_CONF_FOLDER = EnvironmentConfigurationReader.getCurrentDir()+File.separator+"src/test/resources";
	private static final String TARGET_FILE_NAME = "pop3server.xml";
	
	private Pop3serverConfigManager pop3serverConfigManager;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		//remove old test result
		File dnsFile = new File(JAMES_CONF_FOLDER+File.separator+TARGET_FILE_NAME);
		if(dnsFile.exists())
		   dnsFile.delete();
		
		pop3serverConfigManager = new Pop3serverConfigManager();
	}

    public void testReadConfiguration() throws ConfigurationException {
		
		Pop3server pop3server =	pop3serverConfigManager.readConfiguration(JAMES_CONF_FOLDER);	
		assertNotNull(pop3server);	
		System.out.println(pop3server.toString());		
	}

	
	public void testUpdateConfiguration() throws ConfigurationException {		
		
		Pop3server currentConfig = pop3serverConfigManager.readConfiguration(JAMES_CONF_FOLDER);
		System.out.println("Pop3Server current config: "+currentConfig.toString());
		
		//part 1: store a new configuration				
		Pop3server pop3server = new Pop3server();
		
		pop3server.setPop3serverEnabled(false);
		pop3server.setBindAddress("3.4.5.6:877");
		pop3server.setConnectionBacklog("cntbklg");
		pop3server.setConnectiontimeout("678");
		pop3server.setKeystore("file://mykey");
		pop3server.setSecret("mysecret");
		pop3server.setProvider("org.myprovider.com");		
		pop3server.setHelloNameAutoDetect(false);
		pop3server.setHelloName("hellonam");
		pop3server.setConnectionLimit("444");
		pop3server.setConnectionLimitPerIP("555");			
		pop3server.setSocketTls(true);  //new
		pop3server.setStartTls(false);  //new
							
		System.out.println("Pop3Server to store: "+pop3server.toString());
		
		pop3serverConfigManager.updateConfiguration(pop3server,JAMES_CONF_FOLDER);
				
		//part 2: check previously stored configuration
		Pop3server loadedConfig = pop3serverConfigManager.readConfiguration(JAMES_CONF_FOLDER);
				
		System.out.println("Pop3Server loaded config: "+loadedConfig.toString());
		
		assertNotNull(loadedConfig);	
		assertNotNull(loadedConfig.isPop3serverEnabled());
		assertNotNull(loadedConfig.getBindAddress());
		assertNotNull(loadedConfig.getConnectionBacklog());
		assertNotNull(loadedConfig.getConnectiontimeout());
		assertNotNull(loadedConfig.isHelloNameAutoDetect());
		assertNotNull(loadedConfig.getHelloName());
		assertNotNull(loadedConfig.getConnectionLimit());
		assertNotNull(loadedConfig.getConnectionLimitPerIP());	
		assertNotNull(loadedConfig.getKeystore());
		assertNotNull(loadedConfig.getSecret());
		assertNotNull(loadedConfig.getProvider());		
		
		assertEquals(false, loadedConfig.isPop3serverEnabled());
		assertEquals("3.4.5.6:877",loadedConfig.getBindAddress());
		assertEquals("cntbklg",loadedConfig.getConnectionBacklog());
		assertEquals("678",loadedConfig.getConnectiontimeout());
		assertEquals(false,loadedConfig.isHelloNameAutoDetect());
		assertEquals("hellonam",loadedConfig.getHelloName());
		assertEquals("444",loadedConfig.getConnectionLimit());
		assertEquals("555",loadedConfig.getConnectionLimitPerIP());
		assertEquals("file://mykey",loadedConfig.getKeystore());
		assertEquals("mysecret",loadedConfig.getSecret());
		assertEquals("org.myprovider.com",loadedConfig.getProvider());
		assertEquals(true, loadedConfig.isSocketTls());
		assertEquals(false, loadedConfig.isStartTls());
		
   }

}
