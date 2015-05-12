package org.apache.james.jamesui.backend.configuration.bean;


/**
 * Bean that represents the data to store in the file "james-database.propertie"s about the DBMS used by James 
 * to store mails and users.
 * 
 * @author fulvio
 *
 */
public class DataBaseConfiguration {
	
	private String databaseType;
	private String driverClassName;
	private String databaseUrl;
	private String databaseUsername;
	private String databasePassword;
	private String openjpaStreaming; //only supported on a limited set of databases

	/**
	 * Constructor
	 */
	public DataBaseConfiguration() {
		
	}

	public String getDatabaseType() {
		return databaseType;
	}

	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getDatabaseUrl() {
		return databaseUrl;
	}

	public void setDatabaseUrl(String databaseUrl) {
		this.databaseUrl = databaseUrl;
	}

	public String getDatabaseUsername() {
		return databaseUsername;
	}

	public void setDatabaseUsername(String databaseUsername) {
		this.databaseUsername = databaseUsername;
	}

	public String getDatabasePassword() {
		return databasePassword;
	}

	public void setDatabasePassword(String databasePassword) {
		this.databasePassword = databasePassword;
	}


	public String getOpenjpaStreaming() {
		return openjpaStreaming;
	}

	public void setOpenjpaStreaming(String openjpaStreaming) {
		this.openjpaStreaming = openjpaStreaming;
	}

	@Override
	public String toString() {
		return "DataBaseConfiguration [databaseType=" + databaseType
				+ ", driverClassName=" + driverClassName + ", databaseUrl="
				+ databaseUrl + ", databaseUsername=" + databaseUsername
				+ ", databasePassword=" + databasePassword
				+ ", openjpaStreaming=" + openjpaStreaming + "]";
	}
}
