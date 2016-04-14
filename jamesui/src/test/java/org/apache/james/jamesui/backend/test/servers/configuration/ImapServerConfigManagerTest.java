package org.apache.james.jamesui.backend.test.servers.configuration;

import java.io.File;

import junit.framework.TestCase;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.james.jamesui.backend.configuration.bean.ImapServer;
import org.apache.james.jamesui.backend.configuration.bean.JamesuiConfiguration;
import org.apache.james.jamesui.backend.configuration.manager.EnvironmentConfigurationReader;
import org.apache.james.jamesui.backend.configuration.manager.ImapServerConfigManager;
import org.apache.james.jamesui.backend.configuration.manager.JamesuiConfigurationManager;

/**
 * Test case for read/write operations on the configuration file imapserver.xml
 * @author fulvio
 *
 */
public class ImapServerConfigManagerTest extends TestCase {	
	
    private static final String FAKE_JAMES_BASE_FOLDER = EnvironmentConfigurationReader.getCurrentDir()+File.separator+"src/test/resources";
    private static final String TARGET_FILE_NAME = "imapserver.xml";
    
	private ImapServerConfigManager imapServerConfigManager;

	protected void setUp() throws Exception {
		super.setUp();
		
		JamesuiConfigurationManager jamesuiConfigurationManager = new JamesuiConfigurationManager();
		JamesuiConfiguration jamesuiConfiguration = jamesuiConfigurationManager.loadConfiguration();
		
		//remove old test result
		File dnsFile = new File(FAKE_JAMES_BASE_FOLDER+File.separator+TARGET_FILE_NAME);
		if(dnsFile.exists())
		   dnsFile.delete();
		
		jamesuiConfiguration.setJamesBaseFolder(FAKE_JAMES_BASE_FOLDER);
		imapServerConfigManager = new ImapServerConfigManager(jamesuiConfiguration);
	}

	public void testReadConfiguration() throws ConfigurationException {
		
		ImapServer imapServer =	imapServerConfigManager.readConfiguration();
		assertNotNull(imapServer);
		System.out.println(imapServer.toString());		
	}
	
    public void testUpdateConfiguration() throws ConfigurationException {		
		
		//part 1: store a new configuration		
		ImapServer imapServer = new ImapServer();
		imapServer.setImapServerEnabled(false);
		imapServer.setBindAddress("1.2.3.4:777");
		imapServer.setConnectionBacklog("111");
		imapServer.setKeystore("file://pippo//");
		imapServer.setSecret("mysecret123");
		imapServer.setProvider("com.provired.fake");
		
		imapServer.setHelloNameAutoDetect(false);		
		imapServer.setHelloName("helloName");
		
		imapServer.setConnectionLimit("222");
		imapServer.setConnectionLimitPerIP("333");
		
		imapServer.setSocketTls(true);  //new
		imapServer.setStartTls(false);  //new
			
		System.out.println("ImapServer to save: "+imapServer.toString());
		
		imapServerConfigManager.updateConfiguration(imapServer);
		
		//part 2: check previously stored configuration
		ImapServer loadedConfig = imapServerConfigManager.readConfiguration();
		
		System.out.println("ImapServer loaded: "+loadedConfig.toString());
		
		assertNotNull(loadedConfig);
		assertNotNull(loadedConfig.isImapServerEnabled());
		assertNotNull(loadedConfig.getBindAddress());
		assertNotNull(loadedConfig.getConnectionBacklog());
		assertNotNull(loadedConfig.getKeystore());
		assertNotNull(loadedConfig.getSecret());	
		assertNotNull(loadedConfig.getProvider());
		assertNotNull(loadedConfig.isHelloNameAutoDetect());
		assertNotNull(loadedConfig.getConnectionLimit());
		assertNotNull(loadedConfig.getConnectionLimitPerIP());
		
		
		assertEquals(false, loadedConfig.isImapServerEnabled());
		assertEquals("1.2.3.4:777",loadedConfig.getBindAddress());
		assertEquals("111",loadedConfig.getConnectionBacklog());
		assertEquals("file://pippo//",loadedConfig.getKeystore());
		assertEquals("mysecret123",loadedConfig.getSecret());	
		assertEquals("com.provired.fake", loadedConfig.getProvider());
		assertEquals(false,loadedConfig.isHelloNameAutoDetect());
		assertEquals("222",loadedConfig.getConnectionLimit());
		assertEquals("333",loadedConfig.getConnectionLimitPerIP());	
		
		assertEquals(true, loadedConfig.isSocketTls());
		assertEquals(false, loadedConfig.isStartTls());
	}

}
