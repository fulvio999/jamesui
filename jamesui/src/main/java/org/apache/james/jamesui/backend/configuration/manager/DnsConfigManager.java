
package org.apache.james.jamesui.backend.configuration.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.ConfigurationNode;
import org.apache.commons.configuration.tree.DefaultConfigurationNode;
import org.apache.james.jamesui.backend.configuration.bean.Dns;
import org.apache.james.jamesui.backend.configuration.bean.JamesuiConfiguration;

/**
 * Allow to configure DNS servers used by James server writing the dedicated xml configuration files
 * (ie: dnsservice.xml)
 * 
 * NOTE: the default James3 configurations files are placed in James jar file. 
 * To overwrite them can be modified the templates files: copy in the "conf" folder *-template.conf to *.xml
 * (eg dnsservice-template.conf  --> -dnsservice.xml)
 * 
 * The new provided values are placed in the custom files location under the james_home/conf folder
 * 
 * @author fulvio
 *
 */
public class DnsConfigManager {
	
	private JamesuiConfiguration jamesuiConfiguration;

	/**
	 * Constructor
	 */
	public DnsConfigManager(JamesuiConfiguration jamesuiConfiguration) {
		this.jamesuiConfiguration = jamesuiConfiguration;		
	}
	
	/**
	 * Read the configuration from  dnsservice.xml file
	 * @param jamesConfFolder The James server "conf" folder 
	 * @return
	 * @throws ConfigurationException
	 */
	public Dns readConfiguration() throws ConfigurationException {
		
		XMLConfiguration xmlConfiguration = null;
		
		String JAMES_CONF_FOLDER = this.jamesuiConfiguration.getJamesBaseFolder()+File.separator+"conf";
		
		File dnsTemplateFile = new File(JAMES_CONF_FOLDER+File.separator+jamesuiConfiguration.getJamesDnsConfigTemplateFileName());
		File dnsFile = new File(JAMES_CONF_FOLDER+File.separator+jamesuiConfiguration.getJamesDnsConfigFileName());
		
		//decide where load the existing configuration: dnsservice.xml (if exist) or his template 	
		if (dnsFile.exists()) {			
			xmlConfiguration = new XMLConfiguration(dnsFile);	  
			//System.out.println("reading dnsFile: "+dnsFile.getName());
		}else{
			xmlConfiguration = new XMLConfiguration(dnsTemplateFile);
			//System.out.println("reading dnsTemplateFile: "+dnsTemplateFile.getName());
		}
		Dns dns = new Dns();
	   
	    List<Object> servers = xmlConfiguration.getList("servers.server");
	    dns.setDnsServerList(servers);
	    dns.setAutodiscover(xmlConfiguration.getBoolean("autodiscover", true));
	    dns.setAuthoritative(xmlConfiguration.getBoolean("authoritative", false));
	    dns.setSingleIPperMX(xmlConfiguration.getBoolean("singleIPperMX", true));
	    dns.setMaxcachesize(xmlConfiguration.getLong("maxcachesize", 50000));

	    return dns;		
	}

		
	
	/**
	 * Update the target file (ie dsnservice.xml) with the provided input parameters. 
	 * If this file is missing, create it merging template the old one one with the new one.
	 * The input parameters are the ones shown in the front-end page.
	 * @param dns The bean containing the configuration to add
	 * @param jamesConfFolder The James server "conf" folder 
	 * @throws ConfigurationException
	 */
 	public void updateConfiguration(Dns dns) throws ConfigurationException{
		
 		XMLConfiguration xmlConfiguration = null;		

 		String JAMES_CONF_FOLDER = this.jamesuiConfiguration.getJamesBaseFolder()+File.separator+"conf";
		
		File dnsTemplateFile = new File(JAMES_CONF_FOLDER+File.separator+jamesuiConfiguration.getJamesDnsConfigTemplateFileName());
		File dnsFile = new File(JAMES_CONF_FOLDER+File.separator+jamesuiConfiguration.getJamesDnsConfigFileName());
		

		//decide where load the existing configuration: dnsservice.xml (if exist) or his template 	
		if (dnsFile.exists()) {			
			xmlConfiguration = new XMLConfiguration(dnsFile);		
		}else{
			xmlConfiguration = new XMLConfiguration(dnsTemplateFile);			
		}
		//set the target file where save the configuration
		xmlConfiguration.setFileName(dnsFile.getAbsolutePath());
	    
	    List<ConfigurationNode> serverList = new ArrayList<ConfigurationNode>(); 
	    serverList.clear(); 
	    
	    Iterator<Object> it = dns.getDnsServerList().iterator();	    
	    while(it.hasNext()){
	    	//add new <server> node
	    	serverList.add( new DefaultConfigurationNode("server",it.next()) );	  
	    }
	    //removes already existing <server> nodes
	    xmlConfiguration.clearTree("servers.server");  
	    //add new <server> nodes
	    xmlConfiguration.addNodes("servers", serverList);	    
	    
	    //overwrite the the old values with the new ones
	    xmlConfiguration.setProperty("autodiscover", dns.isAutodiscover());
	    xmlConfiguration.setProperty("authoritative", dns.isAuthoritative());
	    xmlConfiguration.setProperty("maxcachesize", dns.getMaxcachesize()); 
	    xmlConfiguration.setProperty("singleIPperMX", dns.isSingleIPperMX());
	    
	    xmlConfiguration.save();	
	}

}
