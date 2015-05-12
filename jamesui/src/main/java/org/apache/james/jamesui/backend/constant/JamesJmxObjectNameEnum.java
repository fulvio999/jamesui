package org.apache.james.jamesui.backend.constant;

/**
 * JMX objects names of the James objects
 * 
 * @author fulvio
 *
 */
public enum JamesJmxObjectNameEnum {
	
	DNS_SERVICE("org.apache.james:type=component,name=dnsservice"),
	
	DOMAIN_LIST("org.apache.james:type=component,name=domainlist"),
	
	USER_REPOSITORY("org.apache.james:type=component,name=usersrepository"),
	
	//className: org.apache.james.rrt.lib.RecipientRewriteTableManagement
	RECIPIENT_REWRITE_TABLE("org.apache.james:type=component,name=recipientrewritetable"),
	
	//className: org.apache.james.imapserver.netty.IMAPServer
	IMAP_SERVER("org.apache.james:type=server,name=imapserver"),
	
	//className: org.apache.james.pop3server.netty.POP3Server
	POP3_SERVER("org.apache.james:type=server,name=pop3server"),
	
	//className: org.apache.james.smtpserver.netty.SMTPServer
	SMTP_SERVER("org.apache.james:type=server,name=smtpserver");
	
    private String objectName;
	
	private JamesJmxObjectNameEnum(String obj){
		this.objectName = obj;
	}

	public String getObjectName() {
		return objectName;
	}

}
