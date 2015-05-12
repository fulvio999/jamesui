
package org.apache.james.jamesui.backend.configuration.bean;

/**
 * bean that represents the configuration stored in TOMCAT_HOME/conf/jamesui.config file
 * (a user defined file with some configuration option)
 * 
 * @author fulvio
 *
 */
public class JamesuiConfiguration {
	
	/* the folder where save MapDB files */
	private String statisticDbLocation;
	
	/* the root folder where is installed Apache James server */
	private String jamesBaseFolder;

	/**
	 * Constructor
	 */
	public JamesuiConfiguration() {
		
	}
	

	public String getStatisticDbLocation() {
		return statisticDbLocation;
	}

	public void setStatisticDbLocation(String statisticDbLocation) {
		this.statisticDbLocation = statisticDbLocation;
	}

	public String getJamesBaseFolder() {
		return jamesBaseFolder;
	}

	public void setJamesBaseFolder(String jamesBaseFolder) {
		this.jamesBaseFolder = jamesBaseFolder;
	}


	@Override
	public String toString() {
		return "JamesuiConfiguration [statisticDbLocation="
				+ statisticDbLocation + ", jamesBaseFolder=" + jamesBaseFolder
				+ "]";
	}

}
