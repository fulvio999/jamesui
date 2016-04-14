package org.apache.james.jamesui.backend.client.jmx;

import java.io.IOException;

import javax.management.MBeanServerConnection;
import javax.management.NotificationListener;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.james.jamesui.backend.configuration.bean.JmxConfiguration;
import org.apache.james.jamesui.backend.configuration.manager.JmxConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility used to get/close a connection with a JMX server 
 *
 */
public class JmxServerConnectionManager {

	private final static Logger LOG = LoggerFactory.getLogger(JmxServerConnectionManager.class);
	
	//private static final JmxConfigManager jmxConfigManager = new JmxConfigManager();
	
	private volatile static MBeanServerConnection mbeanServerConnection;
	private volatile static JMXConnector jmxConnector;

	/**
	 * Constructor
	 */
	private JmxServerConnectionManager() {
		
	}

	/**
	 * Return an instance of MBeanServerConnection to execute operation on a jmx server
	 * @return null in case of can't connect with the provided jmx server
	 * @throws IOException If can't obtain a JMx connection with the server
	 * @throws ConfigurationException 
	 */
	public static MBeanServerConnection getJmxServerConnection() throws IOException, ConfigurationException{
		
		JmxConfigManager jmxConfigManager = new JmxConfigManager();
		
		if (mbeanServerConnection == null) {	
			
			JMXServiceURL url;
			
			
			try {			
				if (jmxConfigManager.existCustomConfiguration()) //user has provided his custom values
				{
				    JmxConfiguration jmxConfiguration = jmxConfigManager.readConfiguration();
				    url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://"+jmxConfiguration.getJmxHost()+":"+jmxConfiguration.getJmxPort()+"/jmxrmi");
				}else //use default ones 
				    url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://127.0.0.1:9999/jmxrmi");
			
				jmxConnector = JMXConnectorFactory.connect(url);
				mbeanServerConnection = jmxConnector.getMBeanServerConnection();
				
			} catch (IOException e) {
				LOG.error("Error obtaining a jmx server connection, cause: ",e);
				throw new IOException("Error Connecting wiht James Server (the server is running ?)");
				//return null;
			}
		}		
		return mbeanServerConnection;		
	}
	
	/**
	 *  Close the connection with the JMX server
	 */
	public static void closeJmxServerConnection(){
		
		if(jmxConnector != null){
			try {
				jmxConnector.close(); 
				mbeanServerConnection = null;
			} catch (IOException e) {
				LOG.error("Error closing a jmx server connection, cause: ",e);
			}	
		}	
	}
	
	/**
	 * Register a new notification listener with the JMX server
	 */
	public void registerNotificationListener(NotificationListener listener){
		
	}

}
