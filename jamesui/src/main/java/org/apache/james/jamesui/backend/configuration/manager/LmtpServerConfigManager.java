
package org.apache.james.jamesui.backend.configuration.manager;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.james.jamesui.backend.configuration.bean.LmtpServer;

/**
 * Allow to configure SMTP servers used by James server writing the dedicated xml configuration file: lmtpserver.xml
 * ( Local Mail Transfer Protocol (LMTP) )
 * 
 * NOTE: the default James3 configurations files are placed in James jar file. 
 * To overwrite them can be modified the templates files: copy in the "conf" folder *-template.conf to *.xml
 * (eg lmtpserver-template.conf  --> lmtpserver.xml)
 * 
 * The new provided values are placed in the custom files location under the james_home/conf folder
 * 
 * @author fulvio
 *
 */
public class LmtpServerConfigManager {
	
	private static final String TARGET_FILE_NAME = "lmtpserver.xml";
	private static final String TEMPLATE_FILE_NAME = "lmtpserver-template.conf";

	/**
	 * Constructor
	 */
	public LmtpServerConfigManager() {
		
	}
	
	
	/**
	 * Read the configuration from  lmtpserver.xml file
	 * @param jamesConfFolder The James server "conf" folder  
	 * @return
	 * @throws ConfigurationException
	 */
	public LmtpServer readConfiguration(String jamesConfFolder) throws ConfigurationException {
		
		XMLConfiguration xmlConfiguration = null;
		
		File imapServerTemplateFile = new File(jamesConfFolder+File.separator+TEMPLATE_FILE_NAME);
		File imapServerFile = new File(jamesConfFolder+File.separator+TARGET_FILE_NAME);

		//decide where load the existing configuration: lmtpserver.xml (if exist) or his template 	
		if (imapServerFile.exists()) {			
			xmlConfiguration = new XMLConfiguration(imapServerFile);		   
		}else
			xmlConfiguration = new XMLConfiguration(imapServerTemplateFile);
	
		LmtpServer lmtpServer = new LmtpServer();
		/* NOTE: assign a default value in case the parameters is missing or empty */
		lmtpServer.setLmtpserverEnabled(xmlConfiguration.getBoolean("lmtpserver(0)[@enabled]",true));
		lmtpServer.setBindAddress(xmlConfiguration.getString("lmtpserver.bind",""));
		lmtpServer.setConnectionBacklog(xmlConfiguration.getString("lmtpserver.connectionBacklog",""));
		lmtpServer.setConnectionTimeout(xmlConfiguration.getString("lmtpserver.connectiontimeout",""));
		
		/* note: use default value because that element <helloName autodetect="true"> in the xml is commented by default
		   so that key doesn't map to an existing object */ 
		lmtpServer.setHelloNameAutoDetect(xmlConfiguration.getBoolean("lmtpserver.helloName(0)[@autodetect]",true));
		lmtpServer.setHelloName(xmlConfiguration.getString("lmtpserver.helloName",""));
		lmtpServer.setConnectionLimit(xmlConfiguration.getString("lmtpserver.connectionLimit",""));
		lmtpServer.setConnectionLimitPerIP(xmlConfiguration.getString("lmtpserver.connectionLimitPerIP",""));
		lmtpServer.setMaxmessagesize(xmlConfiguration.getString("lmtpserver.maxmessagesize",""));
		lmtpServer.setLmtpGreeting(xmlConfiguration.getString("lmtpserver.smtpGreeting",""));

	    return lmtpServer;		
	}
	
	/**
	 * Update the target file (ie dsnservice.xml) with the provided input parameters. 
	 * If this file is missing, create it merging template the old one one with the new one.
	 * The input parameters are the ones shown in the front-end page.
	 * @param jamesConfFolder The James server "conf" folder 
	 * @param dns The bean containing the configuration to add
	 * @throws ConfigurationException
	 */
 	public void updateConfiguration(LmtpServer lmtpServer, String jamesConfFolder) throws ConfigurationException{
		
		XMLConfiguration xmlConfiguration = null;		
			
		File dnsTemplateFile = new File(jamesConfFolder+File.separator+TEMPLATE_FILE_NAME);
		File dnsFile = new File(jamesConfFolder+File.separator+TARGET_FILE_NAME);

		//decide where load the existing configuration: dnsservice.xml (if exist) or his template 	
		if (dnsFile.exists()) {			
			xmlConfiguration = new XMLConfiguration(dnsFile);		   
		}else
			xmlConfiguration = new XMLConfiguration(dnsTemplateFile);			
		
		//set the target file where save the configuration
		xmlConfiguration.setFileName(jamesConfFolder+File.separator+TARGET_FILE_NAME);
		
		xmlConfiguration.setProperty("lmtpserver(0)[@enabled]",lmtpServer.isLmtpserverEnabled());
		xmlConfiguration.setProperty("lmtpserver.bind",lmtpServer.getBindAddress());
		xmlConfiguration.setProperty("lmtpserver.connectionBacklog",lmtpServer.getConnectionBacklog());
		xmlConfiguration.setProperty("lmtpserver.connectiontimeout",lmtpServer.getConnectionTimeout());
		xmlConfiguration.setProperty("lmtpserver.helloName(0)[@autodetect]",lmtpServer.isHelloNameAutoDetect());
		xmlConfiguration.setProperty("lmtpserver.helloName",lmtpServer.getHelloName());
		xmlConfiguration.setProperty("lmtpserver.connectionLimit",lmtpServer.getConnectionLimit());
		xmlConfiguration.setProperty("lmtpserver.connectionLimitPerIP",lmtpServer.getConnectionLimitPerIP());
		xmlConfiguration.setProperty("lmtpserver.maxmessagesize",lmtpServer.getMaxmessagesize());
		xmlConfiguration.setProperty("lmtpserver.smtpGreeting",lmtpServer.getLmtpGreeting());
	    
	    xmlConfiguration.save();	
	}

}
