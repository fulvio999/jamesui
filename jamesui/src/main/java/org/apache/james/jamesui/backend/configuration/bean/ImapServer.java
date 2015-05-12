
package org.apache.james.jamesui.backend.configuration.bean;


/**
 * Bean that represents the information stored in the file imapserver.xml
 * 
 * @author fulvio
 *
 */
public class ImapServer {
	
	private boolean imapServerEnabled;
	private String bindAddress;
	private String connectionBacklog;
	private boolean socketTls; //new
	private boolean startTls;  //new
	private String keystore;
	private String secret;
	private String provider;
	private boolean helloNameAutoDetect; //true/false
	private String helloName;	
	private String connectionLimit;
	private String connectionLimitPerIP;

	
	/**
	 * Constructor
	 */
	public ImapServer() {
		
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

	public boolean isImapServerEnabled() {
		return imapServerEnabled;
	}

	public void setImapServerEnabled(boolean imapServerEnabled) {
		this.imapServerEnabled = imapServerEnabled;
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

	@Override
	public String toString() {
		return "ImapServer [imapServerEnabled=" + imapServerEnabled
				+ ", bindAddress=" + bindAddress + ", connectionBacklog="
				+ connectionBacklog + ", socketTls=" + socketTls
				+ ", startTls=" + startTls + ", keystore=" + keystore
				+ ", secret=" + secret + ", provider=" + provider
				+ ", helloNameAutoDetect=" + helloNameAutoDetect
				+ ", helloName=" + helloName + ", connectionLimit="
				+ connectionLimit + ", connectionLimitPerIP="
				+ connectionLimitPerIP + "]";
	}

}
