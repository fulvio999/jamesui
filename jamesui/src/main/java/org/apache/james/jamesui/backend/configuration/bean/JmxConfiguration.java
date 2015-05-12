
package org.apache.james.jamesui.backend.configuration.bean;

/**
 * Bean that represents the configuration stored in the file james_conf_folder/jmx.properties (or his template)
 * That file contains the connection parameter to connect to James using jmx 
 * 
 * @author fulvio
 *
 */
public class JmxConfiguration {
	
	private String jmxHost;
	private String jmxPort;

	/**
	 * constructor
	 */
	public JmxConfiguration() {
		
	}

	public String getJmxHost() {
		return jmxHost;
	}

	public void setJmxHost(String jmxHost) {
		this.jmxHost = jmxHost;
	}

	public String getJmxPort() {
		return jmxPort;
	}

	public void setJmxPort(String jmxPort) {
		this.jmxPort = jmxPort;
	}

	@Override
	public String toString() {
		return "JmxConfiguration [jmxHost=" + jmxHost + ", jmxPort=" + jmxPort
				+ "]";
	}

}
