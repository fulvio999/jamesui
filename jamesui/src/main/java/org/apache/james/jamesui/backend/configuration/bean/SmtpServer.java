
package org.apache.james.jamesui.backend.configuration.bean;


/**
 * Bean that represents the information stored in the file smtpserver.xml
 * @author fulvio
 *
 */
public class SmtpServer {	
	
	private boolean smtpServerEnabled;
	private String bindAddress;
	private String connectionBacklog;
	private boolean socketTls; //new
	private boolean startTls;  //new
	private String keystore;
	private String secret;
	private String provider;
	private String algorithm;
	private boolean helloNameAutoDetect; //true/false
	private String helloName;	
	private String connectiontimeout;
	private String connectionLimit;
	private String connectionLimitPerIP;
	private String authorizedAddresses;
	private String maxmessagesize;
	private boolean addressBracketsEnforcement;
	private String greeting; //new	


	/**
	 * constructor
	 */
	public SmtpServer() {
		
	}


	public boolean isSmtpServerEnabled() {
		return smtpServerEnabled;
	}


	public void setSmtpServerEnabled(boolean smtpServerEnabled) {
		this.smtpServerEnabled = smtpServerEnabled;
	}


	public String getBindAddress() {
		return bindAddress;
	}


	public void setBindAddress(String bindAddress) {
		this.bindAddress = bindAddress;
	}


	public String getConnectionBacklog() {
		return connectionBacklog;
	}


	public void setConnectionBacklog(String connectionBacklog) {
		this.connectionBacklog = connectionBacklog;
	}


	public String getKeystore() {
		return keystore;
	}


	public void setKeystore(String keystore) {
		this.keystore = keystore;
	}


	public String getSecret() {
		return secret;
	}


	public void setSecret(String secret) {
		this.secret = secret;
	}


	public String getProvider() {
		return provider;
	}


	public void setProvider(String provider) {
		this.provider = provider;
	}


	public String getAlgorithm() {
		return algorithm;
	}


	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}


	public boolean isHelloNameAutoDetect() {
		return helloNameAutoDetect;
	}


	public void setHelloNameAutoDetect(boolean helloNameAutoDetect) {
		this.helloNameAutoDetect = helloNameAutoDetect;
	}


	public String getHelloName() {
		return helloName;
	}


	public void setHelloName(String helloName) {
		this.helloName = helloName;
	}


	public String getConnectiontimeout() {
		return connectiontimeout;
	}


	public void setConnectiontimeout(String connectiontimeout) {
		this.connectiontimeout = connectiontimeout;
	}


	public String getConnectionLimit() {
		return connectionLimit;
	}


	public void setConnectionLimit(String connectionLimit) {
		this.connectionLimit = connectionLimit;
	}


	public String getConnectionLimitPerIP() {
		return connectionLimitPerIP;
	}


	public void setConnectionLimitPerIP(String connectionLimitPerIP) {
		this.connectionLimitPerIP = connectionLimitPerIP;
	}


	public String getAuthorizedAddresses() {
		return authorizedAddresses;
	}


	public void setAuthorizedAddresses(String authorizedAddresses) {
		this.authorizedAddresses = authorizedAddresses;
	}


	public String getMaxmessagesize() {
		return maxmessagesize;
	}


	public void setMaxmessagesize(String maxmessagesize) {
		this.maxmessagesize = maxmessagesize;
	}


	public boolean isSocketTls() {
		return socketTls;
	}


	public void setSocketTls(boolean socketTls) {
		this.socketTls = socketTls;
	}


	public boolean isStartTls() {
		return startTls;
	}


	public void setStartTls(boolean startTls) {
		this.startTls = startTls;
	}


	public String getGreeting() {
		return greeting;
	}


	public void setGreeting(String greeting) {
		this.greeting = greeting;
	}


	public boolean isAddressBracketsEnforcement() {
		return addressBracketsEnforcement;
	}


	public void setAddressBracketsEnforcement(boolean addressBracketsEnforcement) {
		this.addressBracketsEnforcement = addressBracketsEnforcement;
	}


	@Override
	public String toString() {
		return "SmtpServer [smtpServerEnabled=" + smtpServerEnabled
				+ ", bindAddress=" + bindAddress + ", connectionBacklog="
				+ connectionBacklog + ", socketTls=" + socketTls
				+ ", startTls=" + startTls + ", keystore=" + keystore
				+ ", secret=" + secret + ", provider=" + provider
				+ ", algorithm=" + algorithm + ", helloNameAutoDetect="
				+ helloNameAutoDetect + ", helloName=" + helloName
				+ ", connectiontimeout=" + connectiontimeout
				+ ", connectionLimit=" + connectionLimit
				+ ", connectionLimitPerIP=" + connectionLimitPerIP
				+ ", authorizedAddresses=" + authorizedAddresses
				+ ", maxmessagesize=" + maxmessagesize
				+ ", addressBracketsEnforcement=" + addressBracketsEnforcement
				+ ", greeting=" + greeting + "]";
	}




}
