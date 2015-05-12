package org.apache.james.jamesui.backend.test.servers.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.james.jamesui.backend.configuration.bean.Dns;
import org.apache.james.jamesui.backend.configuration.manager.DnsConfigManager;
import org.apache.james.jamesui.backend.configuration.manager.EnvironmentConfigurationReader;

import junit.framework.TestCase;

/**
 * Test case for read/write operations on the configuration file dnsservice.xml
 * @author fulvio
 *
 */
public class DnsConfigManagerTest extends TestCase {	
	
	private static final String JAMES_CONF_FOLDER = EnvironmentConfigurationReader.getCurrentDir()+File.separator+"src/test/resources";
	private static final String TARGET_FILE_NAME = "dnsservice.xml";
	
	private DnsConfigManager dnsConfigManager;

	protected void setUp() throws Exception {
		super.setUp();
		
		//remove old test result
		File dnsFile = new File(JAMES_CONF_FOLDER+File.separator+TARGET_FILE_NAME);
		if(dnsFile.exists())
			dnsFile.delete();
		
		dnsConfigManager = new DnsConfigManager();
	}

	public void testReadConfiguration() throws ConfigurationException {
		
		Dns dns = dnsConfigManager.readConfiguration(JAMES_CONF_FOLDER);
				
		assertNotNull(dns);		
		System.out.println("Read DNS: "+dns.toString());	
	}

	public void testUpdateConfiguration() throws ConfigurationException {		
		
		//part 1: store a new configuration
		List<Object> newServer = new ArrayList<Object>();
		newServer.add("333444555ii");
		newServer.add("234ii");
		newServer.add("795654ii"); 
		
		Dns dns = new Dns();
		dns.setDnsServerList(newServer);
		dns.setAuthoritative(true);
		dns.setAutodiscover(true);
		dns.setMaxcachesize(333);
		dns.setSingleIPperMX(true);
		
		System.out.println("DNS to Update: "+dns.toString());
		
		dnsConfigManager.updateConfiguration(dns,JAMES_CONF_FOLDER);
		
		//part 2: check previously stored configuration
        Dns loadedConfig = dnsConfigManager.readConfiguration(JAMES_CONF_FOLDER);
        
        System.out.println("DNS loaded: "+loadedConfig.toString());
     		
        assertNotNull(loadedConfig);
		assertNotNull(loadedConfig.getDnsServerList());
		assertNotNull(loadedConfig.isAutodiscover());
		assertNotNull(loadedConfig.isAuthoritative());
		assertNotNull(loadedConfig.getMaxcachesize());
		assertNotNull(loadedConfig.isSingleIPperMX());	
		
		assertEquals(3, loadedConfig.getDnsServerList().size());
		assertTrue(loadedConfig.isAutodiscover());
		assertTrue(loadedConfig.isAuthoritative());
		assertEquals(333,loadedConfig.getMaxcachesize());
		assertTrue(loadedConfig.isSingleIPperMX());		
	}

}
