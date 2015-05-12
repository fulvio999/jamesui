package org.apache.james.jamesui.backend.configuration.bean;


/**
 * Bean that represents the information stored in the file lmtpserver.xml
 * 
 * @author fulvio
 *
 */
public class LmtpServer {
	
	private boolean lmtpserverEnabled;
	private String bindAddress;
	private String connectionBacklog;
	private String connectionTimeout;
	private boolean helloNameAutoDetect; //true/false
	private String helloName;
	private String connectionLimit;
	private String connectionLimitPerIP;
	private String maxmessagesize;
	private String lmtpGreeting;


	/**
	 * Constructor
	 */
	public LmtpServer() {
		
	}

	public boolean isLmtpserverEnabled() {
		return lmtpserverEnabled;
	}

	public void setLmtpserverEnabled(boolean lmtpserverEnabled) {
		this.lmtpserverEnabled = lmtpserverEnabled;
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

	public String getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(String connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
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

	public String getMaxmessagesize() {
		return maxmessagesize;
	}

	public void setMaxmessagesize(String maxmessagesize) {
		this.maxmessagesize = maxmessagesize;
	}

	public String getLmtpGreeting() {
		return lmtpGreeting;
	}

	public void setLmtpGreeting(String lmtpGreeting) {
		this.lmtpGreeting = lmtpGreeting;
	}

	@Override
	public String toString() {
		return "LmtpServer [lmtpserverEnabled=" + lmtpserverEnabled
				+ ", bindAddress=" + bindAddress + ", connectionBacklog="
				+ connectionBacklog + ", connectionTimeout="
				+ connectionTimeout + ", helloNameAutoDetect="
				+ helloNameAutoDetect + ", helloName=" + helloName
				+ ", connectionLimit=" + connectionLimit
				+ ", connectionLimitPerIP=" + connectionLimitPerIP
				+ ", maxmessagesize=" + maxmessagesize + ", lmtpGreeting="
				+ lmtpGreeting + "]";
	}



	
}
