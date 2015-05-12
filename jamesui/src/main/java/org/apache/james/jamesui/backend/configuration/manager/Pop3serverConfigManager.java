
package org.apache.james.jamesui.backend.configuration.manager;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.james.jamesui.backend.configuration.bean.Pop3server;

/**
 * Allow to configure SMTP servers used by James server writing the dedicated xml configuration file: pop3server.xml
 * 
 * NOTE: the default James3 configurations files are placed in James jar file. 
 * To overwrite them can be modified the templates files: copy in the "conf" folder *-template.conf to *.xml
 * (eg pop3server-template.conf  --> pop3server.xml)
 * 
 * The new provided values are placed in the custom files location under the james_home/conf folder
 * 
 * @author fulvio
 *
 */
public class Pop3serverConfigManager {
	
	private static final String TARGET_FILE_NAME = "pop3server.xml";
	private static final String TEMPLATE_FILE_NAME = "pop3server-template.conf";

	/**
	 * Constructor
	 */
	public Pop3serverConfigManager() {
		
	}
	
	/**
	 * Read the configuration from  pop3server.xml file
	 * @param jamesConfFolder The James server "conf" folder  
	 * @return
	 * @throws ConfigurationException
	 */
	public Pop3server readConfiguration(String jamesConfFolder) throws ConfigurationException {
		
		XMLConfiguration xmlConfiguration = null;
		
		File pop3ServerTemplateFile = new File(jamesConfFolder+File.separator+TEMPLATE_FILE_NAME);
		File pop3ServerFile = new File(jamesConfFolder+File.separator+TARGET_FILE_NAME);

		//decide where load the existing configuration: lmtpserver.xml (if exist) or his template 	
		if (pop3ServerFile.exists()) {			
			xmlConfiguration = new XMLConfiguration(pop3ServerFile);
			//System.out.println("reading Pop3File: "+pop3ServerFile.getName());
		}else{
			xmlConfiguration = new XMLConfiguration(pop3ServerTemplateFile);
			//System.out.println("reading Pop3File: "+pop3ServerTemplateFile.getName());
		}
		
		Pop3server pop3server = new Pop3server();		
		
		/* NOTE: assign a default value in case the parameters is missing or empty */
		pop3server.setPop3serverEnabled(xmlConfiguration.getBoolean("pop3server(0)[@enabled]"));
		pop3server.setBindAddress(xmlConfiguration.getString("pop3server.bind"));
		pop3server.setConnectionBacklog(xmlConfiguration.getString("pop3server.connectionBacklog"));
		pop3server.setConnectiontimeout(xmlConfiguration.getString("pop3server.connectiontimeout"));		
		pop3server.setSocketTls(xmlConfiguration.getBoolean("pop3server.tls(0)[@socketTLS]",false));
		pop3server.setStartTls(xmlConfiguration.getBoolean("pop3server.tls(0)[@startTLS]",false));
		pop3server.setKeystore(xmlConfiguration.getString("pop3server.tls.keystore",""));
		pop3server.setSecret(xmlConfiguration.getString("pop3server.tls.secret",""));
		pop3server.setProvider(xmlConfiguration.getString("pop3server.tls.provider",""));
		
		/* note: use default value because that element <helloName autodetect="true"> in the xml is commented by default
		   so that key doesn't map to an existing object */
		pop3server.setHelloNameAutoDetect(xmlConfiguration.getBoolean("pop3server.helloName(0)[@autodetect]",true));
		pop3server.setHelloName(xmlConfiguration.getString("pop3server.helloName","")); //empty string because is commented in the template file
		pop3server.setConnectionLimit(xmlConfiguration.getString("pop3server.connectionLimit",""));
		pop3server.setConnectionLimitPerIP(xmlConfiguration.getString("pop3server.connectionLimitPerIP",""));		
		//pop3server.setHandlerchainHandler(xmlConfiguration.getString("pop3server.handlerchain.handler(0)[@class]"));

	    return pop3server;		
	}
	
	/**
	 * Update the target file (ie dsnservice.xml) with the provided input parameters. 
	 * If this file is missing, create it merging template the old one one with the new one.
	 * The input parameters are the ones shown in the front-end page.
	 * @param dns The bean containing the configuration to add
	 * @param jamesConfFolder The James server "conf" folder  
	 * @throws ConfigurationException
	 */
 	public void updateConfiguration(Pop3server pop3server, String jamesConfFolder) throws ConfigurationException{
		
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
		
		xmlConfiguration.setProperty("pop3server(0)[@enabled]",pop3server.isPop3serverEnabled());
		xmlConfiguration.setProperty("pop3server.bind",pop3server.getBindAddress());
		xmlConfiguration.setProperty("pop3server.connectionBacklog",pop3server.getConnectionBacklog());
		xmlConfiguration.setProperty("pop3server.connectiontimeout",pop3server.getConnectiontimeout());
		xmlConfiguration.setProperty("pop3server.helloName(0)[@autodetect]",pop3server.isHelloNameAutoDetect());
		xmlConfiguration.setProperty("pop3server.helloName",pop3server.getHelloName());		
		xmlConfiguration.setProperty("pop3server.tls(0)[@socketTLS]",pop3server.isSocketTls());
		xmlConfiguration.setProperty("pop3server.tls(0)[@startTLS]",pop3server.isStartTls());		
		
		xmlConfiguration.setProperty("pop3server.keystore", pop3server.getKeystore());
		xmlConfiguration.setProperty("pop3server.secret", pop3server.getSecret());		
		xmlConfiguration.setProperty("pop3server.provider",pop3server.getProvider());
		
		xmlConfiguration.setProperty("pop3server.connectionLimit",pop3server.getConnectionLimit());
		xmlConfiguration.setProperty("pop3server.connectionLimitPerIP",pop3server.getConnectionLimitPerIP());				
		//xmlConfiguration.setProperty("pop3server.handlerchain.handler(0)[@class]",pop3server.getHandlerchainHandler());
	    
	    xmlConfiguration.save();	
	}

}
