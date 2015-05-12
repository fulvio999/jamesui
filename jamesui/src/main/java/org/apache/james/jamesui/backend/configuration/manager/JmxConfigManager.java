
package org.apache.james.jamesui.backend.configuration.manager;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.james.jamesui.backend.configuration.bean.JmxConfiguration;

/**
 * Allow to configure JMX connection parameters to connect with James servers
 * writing/reading the dedicated configuration files (ie: jmx-template.properties)
 * 
 * NOTE: the default James3 configurations files is placed in James jar file. 
 * To overwrite it, can be edited and renamed the templates files in the james_home "conf" folder *-template.properties to *.properties
 * (ie jmx-template.properties  --> jmx.properties)
 * 
 * See: James web site for more details about configuration.
 * 
 * @author fulvio
 *
 */
public class JmxConfigManager {
	
	private static final String TARGET_FILE_NAME = "jmx.properties";
	private static final String TEMPLATE_FILE_NAME = "jmx-template.properties";

	/**
	 * Constructor
	 */
	public JmxConfigManager() {
		
	}	
	
	/**
	 * If exist file jmx.properties under JAMES/config folder mean that user has overwritten the default settings
	 * @return true if exist file jmx.properties under JAMES/config folder
	 */
	public boolean existCustomConfiguration(String jamesConfFolder){
		
		File jmxConfFile = new File(jamesConfFolder+File.separator+TARGET_FILE_NAME);
		if (jmxConfFile.exists())
			return true;
		else
			return false;
		
	}
	
	/**
	 * Read the configuration from  jmx.properties (if exist) or jmx-template.properties  file
	 * @param jamesConfFolder The James server "conf" folder 
	 * @return
	 * @throws ConfigurationException
	 */
	public JmxConfiguration readConfiguration(String jamesConfFolder) throws ConfigurationException {
		
		PropertiesConfiguration propertiesConfiguration = null;
		
		File jmxTemplateFile = new File(jamesConfFolder+File.separator+TEMPLATE_FILE_NAME);
		File jmxConfFile = new File(jamesConfFolder+File.separator+TARGET_FILE_NAME);
		
		//decide where load the existing configuration: james-database.properties (if exist) or his template 	
		if (jmxConfFile.exists()) {			
			propertiesConfiguration = new PropertiesConfiguration(jmxConfFile);
			//System.out.println("reading databaseFile: "+databaseFile.getName());
		}else{
			propertiesConfiguration = new PropertiesConfiguration(jmxTemplateFile);
			//System.out.println("reading databaseTemplateFile: "+databaseTemplateFile.getName());
		}
		
		JmxConfiguration jmxConfiguration = new JmxConfiguration();	
		jmxConfiguration.setJmxHost(propertiesConfiguration.getString("jmx.address"));
		jmxConfiguration.setJmxPort(propertiesConfiguration.getString("jmx.port"));
			
		return jmxConfiguration;	
	}
	
	
	/**
	 * Update JMX configuration file 
	 * @param JmxConfiguration The configuration to save
	 * @param jamesConfFolder
	 * @throws ConfigurationException
	 */
	public void updateConfiguration(JmxConfiguration jmxConfiguration, String jamesConfFolder) throws ConfigurationException{
		
		PropertiesConfiguration propertiesConfiguration = null;
		
		File jmxTemplateFile = new File(jamesConfFolder+File.separator+TEMPLATE_FILE_NAME);
		File databaseFile = new File(jamesConfFolder+File.separator+TARGET_FILE_NAME);
		
		//decide where load the existing configuration: james-database.properties (if exist) or his template 	
		if (databaseFile.exists()) {			
			propertiesConfiguration = new PropertiesConfiguration(databaseFile);
			//System.out.println("reading databaseFile: "+databaseFile.getName());
		}else{
			propertiesConfiguration = new PropertiesConfiguration(jmxTemplateFile);
			//System.out.println("reading databaseTemplateFile: "+jmxTemplateFile.getName());
		}		
		
		//set the target file where save the configuration
		propertiesConfiguration.setFileName(jamesConfFolder+File.separator+TARGET_FILE_NAME);		
		propertiesConfiguration.setProperty("jmx.address",jmxConfiguration.getJmxHost());
		propertiesConfiguration.setProperty("jmx.port",jmxConfiguration.getJmxPort());
			    
		propertiesConfiguration.save();			
	}

}
