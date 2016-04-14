
package org.apache.james.jamesui.backend.configuration.manager;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.james.jamesui.backend.configuration.bean.JamesuiConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manager for the configuration of JamesUi product.
 * It offer methods to manage the configuration stored in jamesui.config file that
 * the user must place under TOMCAT/conf folder with his custom values about James server installation folder
 * and  Database storage folder.
 * 
 * @author fulvio
 *
 */
public class JamesuiConfigurationManager {
	
	private final static Logger LOG = LoggerFactory.getLogger(JamesuiConfigurationManager.class);
	
	/* config filename that must be placed by the user in TOMCAT/conf folder */
	private static final String CONFIG_FILE_NAME = "jamesui.config";
	
	/* config filename placed inside the war file used for Eclipse development */
	private static final String DEVEL_CONFIG_FILE_NAME = "jamesui-devel.config";
	
	/*
	 * Constructor
	 */
	public JamesuiConfigurationManager(){
		
	}
	
	/**
	 * Check if the loaded JamesUi configuration (the one from jamesui-devel.properties or jamesui.properties) is present and valid
	 *  
	 * @return true If the configuration file is present and valid
	 */
	public boolean isConfigurationValid(JamesuiConfiguration jamesuiConfiguration) {		
			
		if(jamesuiConfiguration == null) {
		   LOG.error("JameUI configuration file not found in TOMCAT/conf folder, nor inside war file !!!");
		   return false;
		}	
			
		if( StringUtils.isNotBlank(jamesuiConfiguration.getJamesBaseFolder()) && StringUtils.isNotBlank(jamesuiConfiguration.getStatisticDbLocation()) )
		{
		   File dbFolder = new File(jamesuiConfiguration.getStatisticDbLocation());
		   File jamesFolder = new File(jamesuiConfiguration.getJamesBaseFolder());
					 
		   if(dbFolder.isDirectory() && dbFolder.canRead() && jamesFolder.isDirectory() && jamesFolder.canRead()) 
		   {				 
			 return true;
				 
		   }else{
			  LOG.error("A property value in the jamesui configuration file is not a directory or is not a readable folder");
			  return false;
			}	 
		}
		
	    return false; 		
	}
	
	/**
	 * Return the JamesUI configuration to use, chosen between:
	 * 
	 * 1) TOMCAT_HOME/conf/jamesui.config --> if jamesui running inside Tomcat
	 * 2) src/main/resources/jamesui-devel.config --> if jamesui is running inside Eclipse
	 *  
	 * @return A JamesuiConfiguration object containing the configuration to use. return null if no configuration file was found anywhere.
	 */
	public JamesuiConfiguration loadConfiguration() 
	{		
		JamesuiConfiguration jamesuiConfiguration = new JamesuiConfiguration();
		
		try {   	
	    	  LOG.trace("Looking for file "+CONFIG_FILE_NAME+" : under Tomcat/conf folder...");
	    				 	  
			  File jamesuiConfigFile = new File(EnvironmentConfigurationReader.getCatalinaHomeFolder()+File.separator+"conf"+File.separator+CONFIG_FILE_NAME);	
			  
			  if(!jamesuiConfigFile.exists())
				 throw new ConfigurationException();
		
			  PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration(jamesuiConfigFile);	
			  
			  jamesuiConfiguration.setStatisticDbLocation(propertiesConfiguration.getString("statistic_db_location"));
			  jamesuiConfiguration.setJamesBaseFolder(propertiesConfiguration.getString("james_base_folder"));	
			  
			  jamesuiConfiguration.setJamesDnsConfigTemplateFileName(propertiesConfiguration.getString("james_dns_config_template_file_name"));			  
			  jamesuiConfiguration.setJamesDnsConfigFileName(propertiesConfiguration.getString("james_dns_config_file_name"));	
			  
			  jamesuiConfiguration.setJamesImapConfigTemplateFileName(propertiesConfiguration.getString("james_imap_config_template_file_name"));	
			  jamesuiConfiguration.setJamesImapConfigFileName(propertiesConfiguration.getString("james_imap_config_file_name"));	
			  
			  jamesuiConfiguration.setJamesJmxConfigTemplateFileName(propertiesConfiguration.getString("james_jmx_config_template_file_name"));	
			  jamesuiConfiguration.setJamesJmxConfigFileName(propertiesConfiguration.getString("james_jmx_config_file_name"));	
			  
			  jamesuiConfiguration.setJamesLmtpConfigTemplateFileName(propertiesConfiguration.getString("james_lmtp_config_template_file_name"));	
			  jamesuiConfiguration.setJamesLmtpConfigFileName(propertiesConfiguration.getString("james_lmtp_config_file_name"));	
			  
			  jamesuiConfiguration.setJamesPop3ConfigTemplateFileName(propertiesConfiguration.getString("james_pop3_config_template_file_name"));	
			  jamesuiConfiguration.setJamesPop3ConfigFileName(propertiesConfiguration.getString("james_pop3_config_file_name"));	
			  
			  jamesuiConfiguration.setJamesSmtpConfigTemplateFileName(propertiesConfiguration.getString("james_smtp_config_template_file_name"));	
			  jamesuiConfiguration.setJamesSmtpConfigFileName(propertiesConfiguration.getString("james_smtp_config_file_name"));
			  
			  jamesuiConfiguration.setJamesDatabaseConfigTemplateFileName(propertiesConfiguration.getString("james_database_config_template_file_name"));	
			  jamesuiConfiguration.setJamesDatabaseConfigFileName(propertiesConfiguration.getString("james_database_config_file_name"));
					
			}catch (ConfigurationException e) {
				
			    LOG.trace("File: "+CONFIG_FILE_NAME+" not found in: "+EnvironmentConfigurationReader.getCatalinaHomeFolder()+File.separator+"conf" +" folder, use the one inside the war");
			    
				try {
					  LOG.trace("Using file "+DEVEL_CONFIG_FILE_NAME+" : Jamesui is running inside Eclipse...");
						
					  Properties config = new Properties();
					  config.load(this.getClass().getResourceAsStream("/"+DEVEL_CONFIG_FILE_NAME));	
					  
					  jamesuiConfiguration.setStatisticDbLocation (config.getProperty("statistic_db_location"));
					  jamesuiConfiguration.setJamesBaseFolder(config.getProperty("james_base_folder"));
					  
					  jamesuiConfiguration.setJamesDnsConfigTemplateFileName(config.getProperty("james_dns_config_template_file_name"));			  
					  jamesuiConfiguration.setJamesDnsConfigFileName(config.getProperty("james_dns_config_file_name"));	
					  
					  jamesuiConfiguration.setJamesImapConfigTemplateFileName(config.getProperty("james_imap_config_template_file_name"));	
					  jamesuiConfiguration.setJamesImapConfigFileName(config.getProperty("james_imap_config_file_name"));	
					  
					  jamesuiConfiguration.setJamesJmxConfigTemplateFileName(config.getProperty("james_jmx_config_template_file_name"));	
					  jamesuiConfiguration.setJamesJmxConfigFileName(config.getProperty("james_jmx_config_file_name"));	
					  
					  jamesuiConfiguration.setJamesLmtpConfigTemplateFileName(config.getProperty("james_lmtp_config_template_file_name"));	
					  jamesuiConfiguration.setJamesLmtpConfigFileName(config.getProperty("james_lmtp_config_file_name"));	
					  
					  jamesuiConfiguration.setJamesPop3ConfigTemplateFileName(config.getProperty("james_pop3_config_template_file_name"));	
					  jamesuiConfiguration.setJamesPop3ConfigFileName(config.getProperty("james_pop3_config_file_name"));	
					  
					  jamesuiConfiguration.setJamesSmtpConfigTemplateFileName(config.getProperty("james_smtp_config_template_file_name"));	
					  jamesuiConfiguration.setJamesSmtpConfigFileName(config.getProperty("james_smtp_config_file_name"));
					 
						
					} catch (IOException ex) {
						LOG.error("Error loading file: "+DEVEL_CONFIG_FILE_NAME+" inside the war file, Can't find jamesui config file ANYWHERE !!");
						return null;
					}			   
			}	   
		
		return jamesuiConfiguration;		
	}

}
