
package org.apache.james.jamesui.backend.configuration.manager;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import org.apache.james.jamesui.backend.configuration.bean.DataBaseConfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Allow to configure the DBMS type used by James as Mail store (by default use Apache DERBY), 
 * The configuration is written in the dedicated configuration files named
 * "james-database-configuration.properties"
 *  
 * NOTE: the default James3 configurations files are placed in James jar file. 
 * To overwrite them can be modified the templates files james-database-configuration.properties in the "conf" folder 
 * and save it as james-database.properties
 * 
 * NOTE: the jdbc Driver must be placed in the "lib" folder (See James official web site for how to change default DBMS).
 * 
 * @author fulvio
 *
 */
public class DatabaseConfigurationManager {
		
	private final static Logger LOG = LoggerFactory.getLogger(DatabaseConfigurationManager.class);
	
	private static final String TARGET_FILE_NAME = "james-database.properties";
	private static final String TEMPLATE_FILE_NAME = "james-database-template.properties";

	/**
	 * Constructor
	 */
	public DatabaseConfigurationManager() {
		
	}
	
	/**
	 * Read the configuration from james-database.properties file
	 * @param jamesConfFolder The James server "conf" folder 
	 * @return
	 * @throws ConfigurationException
	 */
	public DataBaseConfiguration readConfiguration(String jamesConfFolder) throws ConfigurationException {
				
		PropertiesConfiguration propertiesConfiguration = null;
		
		File databaseTemplateFile = new File(jamesConfFolder+File.separator+TEMPLATE_FILE_NAME);
		File databaseFile = new File(jamesConfFolder+File.separator+TARGET_FILE_NAME);
		
		//decide where load the existing configuration: james-database.properties (if exist) or his template 	
		if (databaseFile.exists()) {			
			propertiesConfiguration = new PropertiesConfiguration(databaseFile);
			//LOG.trace("reading databaseFile: "+databaseFile.getName());
		}else{
			propertiesConfiguration = new PropertiesConfiguration(databaseTemplateFile);
			//LOG.trace("reading databaseTemplateFile: "+databaseTemplateFile.getName());
		}
		
		DataBaseConfiguration dataBaseConfiguration = new DataBaseConfiguration();		
		dataBaseConfiguration.setDatabaseType(propertiesConfiguration.getString("vendorAdapter.database")); //eg: MySQL
		dataBaseConfiguration.setDriverClassName(propertiesConfiguration.getString("database.driverClassName"));
		dataBaseConfiguration.setDatabaseUrl(propertiesConfiguration.getString("database.url"));
		dataBaseConfiguration.setDatabaseUsername(propertiesConfiguration.getString("database.username"));
		dataBaseConfiguration.setDatabasePassword(propertiesConfiguration.getString("database.password"));
		dataBaseConfiguration.setOpenjpaStreaming(propertiesConfiguration.getString("openjpa.streaming"));
		
		return dataBaseConfiguration;		
	}
	
	/**
	 * Update configuration file 
	 * @param dataBaseConfiguration The configuration to save
	 * @param jamesConfFolder
	 * @throws ConfigurationException
	 */
	public void updateConfiguration(DataBaseConfiguration dataBaseConfiguration, String jamesConfFolder) throws ConfigurationException{
		
		PropertiesConfiguration propertiesConfiguration = null;
		
		File databaseTemplateFile = new File(jamesConfFolder+File.separator+TEMPLATE_FILE_NAME);
		File databaseFile = new File(jamesConfFolder+File.separator+TARGET_FILE_NAME);
		
		//decide where load the existing configuration: james-database.properties (if exist) or his template 	
		if (databaseFile.exists()) {			
			propertiesConfiguration = new PropertiesConfiguration(databaseFile);
			System.out.println("reading databaseFile: "+databaseFile.getName());
		}else{
			propertiesConfiguration = new PropertiesConfiguration(databaseTemplateFile);
			System.out.println("reading databaseTemplateFile: "+databaseTemplateFile.getName());
		}		
		
		//set the target file where save the configuration
		propertiesConfiguration.setFileName(jamesConfFolder+File.separator+TARGET_FILE_NAME);
		
		propertiesConfiguration.setProperty("vendorAdapter.database",dataBaseConfiguration.getDatabaseType());
		propertiesConfiguration.setProperty("database.driverClassName",dataBaseConfiguration.getDriverClassName());
		propertiesConfiguration.setProperty("database.url",dataBaseConfiguration.getDatabaseUrl());
		propertiesConfiguration.setProperty("database.username",dataBaseConfiguration.getDatabaseUsername());
		propertiesConfiguration.setProperty("database.password",dataBaseConfiguration.getDatabasePassword());
		propertiesConfiguration.setProperty("openjpa.streaming",dataBaseConfiguration.getOpenjpaStreaming());
	    
		propertiesConfiguration.save();			
	}

}
