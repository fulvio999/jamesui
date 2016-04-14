package org.apache.james.jamesui.backend.configuration.manager;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.james.jamesui.backend.configuration.bean.JamesuiConfiguration;
import org.apache.james.jamesui.backend.configuration.bean.SmtpServer;

/**
 * Allow to configure SMTP servers used by James server writing the dedicated xml configuration file: smtpserver.xml
 * 
 * NOTE: the default James3 configurations files are placed in James jar file. 
 * To overwrite them can be modified the templates files: copy in the "conf" folder *-template.conf to *.xml
 * (eg smtpserver-template.conf  --> smtpserver.xml)
 * 
 * The new provided values are placed in the custom files location under the james_home/conf folder
 * 
 * @author fulvio
 *
 */
public class SmtpServerConfigManager {
	
	private JamesuiConfiguration jamesuiConfiguration;

	/**
	 * Constructor
	 */
	public SmtpServerConfigManager(JamesuiConfiguration jamesuiConfiguration) {
		this.jamesuiConfiguration = jamesuiConfiguration;		
	}
	
	
	/**
	 * Read the configuration from  smtpserver.xml file
	 * @param jamesConfFolder The James server "conf" folder 
	 * @return
	 * @throws ConfigurationException
	 */
	public SmtpServer readConfiguration() throws ConfigurationException {
		
		XMLConfiguration xmlConfiguration = null;
		
		String JAMES_CONF_FOLDER = this.jamesuiConfiguration.getJamesBaseFolder()+File.separator+"conf";
		
		File smtpServerTemplateFile = new File(JAMES_CONF_FOLDER+File.separator+jamesuiConfiguration.getJamesSmtpConfigTemplateFileName());
		File smtpServerFile = new File(JAMES_CONF_FOLDER+File.separator+jamesuiConfiguration.getJamesSmtpConfigFileName());


		//decide where load the existing configuration: lmtpserver.xml (if exist) or his template 	
		if (smtpServerFile.exists()) {			
			xmlConfiguration = new XMLConfiguration(smtpServerFile);		   
		}else
			xmlConfiguration = new XMLConfiguration(smtpServerTemplateFile);
	
		SmtpServer smtpServer = new SmtpServer();
		/* NOTE: assign a default value in case the parameters is missing or empty */
		smtpServer.setSmtpServerEnabled(xmlConfiguration.getBoolean("smtpserver(0)[@enabled]",true));
		smtpServer.setBindAddress(xmlConfiguration.getString("smtpserver.bind",""));
		smtpServer.setConnectionBacklog(xmlConfiguration.getString("smtpserver.connectionBacklog",""));
		smtpServer.setConnectiontimeout(xmlConfiguration.getString("smtpserver.connectiontimeout",""));
		smtpServer.setMaxmessagesize(xmlConfiguration.getString("smtpserver.maxmessagesize",""));
		
		/* note: use default value because that element <helloName autodetect="true"> in the xml is commented by default
		 so that key doesn't map to an existing object */
		smtpServer.setHelloNameAutoDetect(xmlConfiguration.getBoolean("smtpserver.helloName(0)[@autodetect]",true));
		smtpServer.setHelloName(xmlConfiguration.getString("smtpserver.helloName",""));
		
		smtpServer.setSocketTls(xmlConfiguration.getBoolean("smtpserver.tls(0)[@socketTLS]",false));
		smtpServer.setStartTls(xmlConfiguration.getBoolean("smtpserver.tls(0)[@startTLS]",false));
		
		
		smtpServer.setSecret(xmlConfiguration.getString("smtpserver.tls.secret",""));
		smtpServer.setProvider(xmlConfiguration.getString("smtpserver.tls.provider",""));
		smtpServer.setAlgorithm(xmlConfiguration.getString("smtpserver.tls.algorithm",""));
		smtpServer.setKeystore(xmlConfiguration.getString("smtpserver.tls.keystore",""));		
		
		smtpServer.setAuthorizedAddresses(xmlConfiguration.getString("smtpserver.authorizedAddresses",""));
		smtpServer.setConnectionLimit(xmlConfiguration.getString("smtpserver.connectionLimit",""));
		smtpServer.setConnectionLimitPerIP(xmlConfiguration.getString("smtpserver.connectionLimitPerIP",""));
		smtpServer.setMaxmessagesize(xmlConfiguration.getString("smtpserver.maxmessagesize",""));		
		smtpServer.setAddressBracketsEnforcement(xmlConfiguration.getBoolean("smtpserver.addressBracketsEnforcement",false));
		
		smtpServer.setGreeting(xmlConfiguration.getString("smtpserver.smtpGreeting",""));

	    return smtpServer;		
	}
	
	/**
	 * Update the target file (ie dsnservice.xml) with the provided input parameters. 
	 * If this file is missing, create it merging template the old one one with the new one.
	 * The input parameters are the ones shown in the front-end page.
	 * @param dns The bean containing the configuration to add
	 * @param jamesConfFolder The James server "conf" folder 
	 * @throws ConfigurationException
	 */
 	public void updateConfiguration(SmtpServer smtpServer) throws ConfigurationException{
		
		XMLConfiguration xmlConfiguration = null;		
			
		String JAMES_CONF_FOLDER = this.jamesuiConfiguration.getJamesBaseFolder()+File.separator+"conf";
		
		File smtpServerTemplateFile = new File(JAMES_CONF_FOLDER+File.separator+jamesuiConfiguration.getJamesSmtpConfigTemplateFileName());
		File smtpServerFile = new File(JAMES_CONF_FOLDER+File.separator+jamesuiConfiguration.getJamesSmtpConfigFileName());

		//decide where load the existing configuration: dnsservice.xml (if exist) or his template 	
		if (smtpServerFile.exists()) {			
			xmlConfiguration = new XMLConfiguration(smtpServerFile);		   
		}else
			xmlConfiguration = new XMLConfiguration(smtpServerTemplateFile);			
		
		//set the target file where save the configuration
		xmlConfiguration.setFileName(smtpServerFile.getAbsolutePath());
		
		xmlConfiguration.setProperty("smtpserver(0)[@enabled]",smtpServer.isSmtpServerEnabled());
		xmlConfiguration.setProperty("smtpserver.bind",smtpServer.getBindAddress());
		xmlConfiguration.setProperty("smtpserver.connectionBacklog",smtpServer.getConnectionBacklog());
		xmlConfiguration.setProperty("smtpserver.connectiontimeout",smtpServer.getConnectiontimeout());
		xmlConfiguration.setProperty("smtpserver.maxmessagesize",smtpServer.getMaxmessagesize());
		xmlConfiguration.setProperty("smtpserver.helloName(0)[@autodetect]",smtpServer.isHelloNameAutoDetect());
		xmlConfiguration.setProperty("smtpserver.helloName",smtpServer.getHelloName());	
		xmlConfiguration.setProperty("smtpserver.tls(0)[@socketTLS]",smtpServer.isSocketTls());
		xmlConfiguration.setProperty("smtpserver.tls(0)[@startTLS]",smtpServer.isStartTls());	
		xmlConfiguration.setProperty("smtpserver.tls.keystore",smtpServer.getKeystore());
		xmlConfiguration.setProperty("smtpserver.tls.provider",smtpServer.getProvider());
		xmlConfiguration.setProperty("smtpserver.tls.algorithm",smtpServer.getAlgorithm());
		xmlConfiguration.setProperty("smtpserver.tls.secret",smtpServer.getSecret());
		xmlConfiguration.setProperty("smtpserver.authorizedAddresses",smtpServer.getAuthorizedAddresses());
		xmlConfiguration.setProperty("smtpserver.connectionLimit",smtpServer.getConnectionLimit());
		xmlConfiguration.setProperty("smtpserver.connectionLimitPerIP",smtpServer.getConnectionLimitPerIP());
		xmlConfiguration.setProperty("smtpserver.maxmessagesize",smtpServer.getMaxmessagesize());
		
		xmlConfiguration.setProperty("smtpserver.smtpGreeting",smtpServer.getGreeting());
		xmlConfiguration.setProperty("smtpserver.addressBracketsEnforcement",smtpServer.isAddressBracketsEnforcement());		
	    
	    xmlConfiguration.save();	
	}

}
