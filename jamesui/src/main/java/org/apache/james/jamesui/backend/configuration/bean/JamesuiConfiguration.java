
package org.apache.james.jamesui.backend.configuration.bean;

/**
 * bean that represents the configuration stored in TOMCAT_HOME/conf/jamesui.config file (or jamesui-devel.config if Jamesui is running inside Eclipse)
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
	
	/* the configuration files names used as template and the used configuration file created using the associated template
	  Note that james beta4 version uses template files with .conf extension, insted beta5 uses .xml extension */
	
	
	private String jamesDnsConfigTemplateFileName;
	private String jamesDnsConfigFileName;

	private String jamesImapConfigTemplateFileName;
	private String jamesImapConfigFileName;

	private String jamesJmxConfigTemplateFileName;
	private String jamesJmxConfigFileName;

	private String jamesLmtpConfigTemplateFileName;
	private String jamesLmtpConfigFileName;

	private String jamesPop3ConfigTemplateFileName;
	private String jamesPop3ConfigFileName;
	
    private String jamesSmtpConfigTemplateFileName;
	private String jamesSmtpConfigFileName;
	
	private String jamesDatabaseConfigTemplateFileName;
	private String jamesDatabaseConfigFileName;
	

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


	public String getJamesDnsConfigTemplateFileName() {
		return jamesDnsConfigTemplateFileName;
	}


	public void setJamesDnsConfigTemplateFileName(String jamesDnsConfigTemplateFileName) {
		this.jamesDnsConfigTemplateFileName = jamesDnsConfigTemplateFileName;
	}


	public String getJamesDnsConfigFileName() {
		return jamesDnsConfigFileName;
	}


	public void setJamesDnsConfigFileName(String jamesDnsConfigFileName) {
		this.jamesDnsConfigFileName = jamesDnsConfigFileName;
	}


	public String getJamesImapConfigTemplateFileName() {
		return jamesImapConfigTemplateFileName;
	}


	public void setJamesImapConfigTemplateFileName(String jamesImapConfigTemplateFileName) {
		this.jamesImapConfigTemplateFileName = jamesImapConfigTemplateFileName;
	}


	public String getJamesImapConfigFileName() {
		return jamesImapConfigFileName;
	}


	public void setJamesImapConfigFileName(String jamesImapConfigFileName) {
		this.jamesImapConfigFileName = jamesImapConfigFileName;
	}


	public String getJamesJmxConfigTemplateFileName() {
		return jamesJmxConfigTemplateFileName;
	}


	public void setJamesJmxConfigTemplateFileName(String jamesJmxConfigTemplateFileName) {
		this.jamesJmxConfigTemplateFileName = jamesJmxConfigTemplateFileName;
	}


	public String getJamesJmxConfigFileName() {
		return jamesJmxConfigFileName;
	}


	public void setJamesJmxConfigFileName(String jamesJmxConfigFileName) {
		this.jamesJmxConfigFileName = jamesJmxConfigFileName;
	}


	public String getJamesLmtpConfigTemplateFileName() {
		return jamesLmtpConfigTemplateFileName;
	}


	public void setJamesLmtpConfigTemplateFileName(String jamesLmtpConfigTemplateFileName) {
		this.jamesLmtpConfigTemplateFileName = jamesLmtpConfigTemplateFileName;
	}


	public String getJamesLmtpConfigFileName() {
		return jamesLmtpConfigFileName;
	}


	public void setJamesLmtpConfigFileName(String jamesLmtpConfigFileName) {
		this.jamesLmtpConfigFileName = jamesLmtpConfigFileName;
	}


	public String getJamesPop3ConfigTemplateFileName() {
		return jamesPop3ConfigTemplateFileName;
	}


	public void setJamesPop3ConfigTemplateFileName(String jamesPop3ConfigTemplateFileName) {
		this.jamesPop3ConfigTemplateFileName = jamesPop3ConfigTemplateFileName;
	}


	public String getJamesPop3ConfigFileName() {
		return jamesPop3ConfigFileName;
	}


	public void setJamesPop3ConfigFileName(String jamesPop3ConfigFileName) {
		this.jamesPop3ConfigFileName = jamesPop3ConfigFileName;
	}


	public String getJamesSmtpConfigTemplateFileName() {
		return jamesSmtpConfigTemplateFileName;
	}


	public void setJamesSmtpConfigTemplateFileName(String jamesSmtpConfigTemplateFileName) {
		this.jamesSmtpConfigTemplateFileName = jamesSmtpConfigTemplateFileName;
	}


	public String getJamesSmtpConfigFileName() {
		return jamesSmtpConfigFileName;
	}


	public void setJamesSmtpConfigFileName(String jamesSmtpConfigFileName) {
		this.jamesSmtpConfigFileName = jamesSmtpConfigFileName;
	}


	public String getJamesDatabaseConfigTemplateFileName() {
		return jamesDatabaseConfigTemplateFileName;
	}


	public void setJamesDatabaseConfigTemplateFileName(String jamesDatabaseConfigTemplateFileName) {
		this.jamesDatabaseConfigTemplateFileName = jamesDatabaseConfigTemplateFileName;
	}


	public String getJamesDatabaseConfigFileName() {
		return jamesDatabaseConfigFileName;
	}


	public void setJamesDatabaseConfigFileName(String jamesDatabaseConfigFileName) {
		this.jamesDatabaseConfigFileName = jamesDatabaseConfigFileName;
	}


	@Override
	public String toString() {
		return "JamesuiConfiguration [statisticDbLocation=" + statisticDbLocation + ", jamesBaseFolder="
				+ jamesBaseFolder + ", jamesDnsConfigTemplateFileName=" + jamesDnsConfigTemplateFileName
				+ ", jamesDnsConfigFileName=" + jamesDnsConfigFileName + ", jamesImapConfigTemplateFileName="
				+ jamesImapConfigTemplateFileName + ", jamesImapConfigFileName=" + jamesImapConfigFileName
				+ ", jamesJmxConfigTemplateFileName=" + jamesJmxConfigTemplateFileName + ", jamesJmxConfigFileName="
				+ jamesJmxConfigFileName + ", jamesLmtpConfigTemplateFileName=" + jamesLmtpConfigTemplateFileName
				+ ", jamesLmtpConfigFileName=" + jamesLmtpConfigFileName + ", jamesPop3ConfigTemplateFileName="
				+ jamesPop3ConfigTemplateFileName + ", jamesPop3ConfigFileName=" + jamesPop3ConfigFileName
				+ ", jamesSmtpConfigTemplateFileName=" + jamesSmtpConfigTemplateFileName + ", jamesSmtpConfigFileName="
				+ jamesSmtpConfigFileName + ", jamesDatabaseConfigTemplateFileName="
				+ jamesDatabaseConfigTemplateFileName + ", jamesDatabaseConfigFileName=" + jamesDatabaseConfigFileName
				+ "]";
	}








}
