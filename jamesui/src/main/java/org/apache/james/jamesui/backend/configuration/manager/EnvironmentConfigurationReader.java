package org.apache.james.jamesui.backend.configuration.manager;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.configuration.EnvironmentConfiguration;

/**
 * Utility class to read environment variables (eg: JAVA_HOME) using Apache Commons configuration library
 * @author fulvio
 *
 */
public class EnvironmentConfigurationReader {		
	
	/* read only the ENVIRONMENT VARIABLE (not OS tmp folder) */
	public static EnvironmentConfiguration config = new EnvironmentConfiguration();
	
	
	/**
	 * Return JAVA_HOME env variable
	 */
	public static String getJavaHome() {	
		
		String javaHome = config.getString("JAVA_HOME");		
		return javaHome;
	}
	
	/**
	 * Return $CATALINA_HOME folder (value set by Tomcat during startup) or null if is not set (eg: Jamesui is running inside Eclipse)
	 */
	public static String getCatalinaHomeFolder() {	
		
		//if running in Eclise eg: C:\Users\.....\Desktop\MY_DOC\JamesUI\WKSPC\.metadata\.plugins\org.eclipse.wst.server.core\tmp0
		String catalinaBaseFolder = System.getProperty("catalina.base");	
		return catalinaBaseFolder;
	}
	
	/**
	 * Return OS tmp folder
	 */
	public static String getOsTmpFolder() {
		
		String tempDir = System.getProperty("java.io.tmpdir");
		return tempDir;
	}
	
	/**
	 * Return Current working directory
	 */
	public static String getCurrentDir() {	
		
		String curDir = System.getProperty("user.dir");		
		return curDir;
	}
	
	
	/** 
	 * @return The host name of the host running JamesUI
	 * @throws UnknownHostException 
	 */
	public static String getHostName() {
		
		InetAddress iAddress = null;
		String hostName = null;
		
		try{
			iAddress = InetAddress.getLocalHost();
			hostName = iAddress.getHostName();
		}catch (UnknownHostException e) {
		   hostName = "unknown";
		}
		 
		return hostName;		
	}	

}
