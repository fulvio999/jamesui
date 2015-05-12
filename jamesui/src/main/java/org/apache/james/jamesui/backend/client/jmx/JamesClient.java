
package org.apache.james.jamesui.backend.client.jmx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import org.apache.james.domainlist.api.DomainListManagementMBean;
import org.apache.james.imapserver.netty.IMAPServerMBean;
import org.apache.james.jamesui.backend.configuration.bean.RecipientRewriteMapping;
import org.apache.james.jamesui.backend.constant.JamesJmxObjectNameEnum;
import org.apache.james.pop3server.netty.POP3ServerMBean;
import org.apache.james.rrt.api.RecipientRewriteTableManagementMBean;
import org.apache.james.smtpserver.netty.SMTPServerMBean;
import org.apache.james.user.api.UsersRepositoryManagementMBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class that offer the API to invoke the JMX Operations exposed by Apache James
 *
 * Note: Was also possible query James server with the class org.apache.james.cli.probe.impl.JmxServerProbe
 * contained in the James sources, but is used this JamesClient class to have have a decoupled solution. 
 */
public class JamesClient {	

	 private final static Logger LOG = LoggerFactory.getLogger(JamesClient.class);
	
	 /**
	  * Constructor
	  */
 	  public JamesClient(){
 		
	  }
 	  
	  /**
	   * Test/probe connection with James Server. Method called in phase of UI constructions
	   * @return true if JamesUI can connect with James server
	   */
	  public boolean isConnectionValid() {
				
		  try{ 
		      MBeanServerConnection mBeanServerConnection =  JmxServerConnectionManager.getJmxServerConnection(); 
		      return true;
		  }catch(Exception e){ 
			  LOG.error("Error Testing connection with James Server, cause:",e);
			  return false;			 
      	 }
	  }
	  
	  /**
	   * List all the DNS server user by James
	   * @return An array containing all the configured DNS server
	   */
 /*	  
	  public String[] getDnsServer() { 
		  
		 try{ 
	         MBeanServerConnection mBeanServerConnection =  JmxServerConnectionManager.getJmxServerConnection();
		    
			 ObjectName jamesDnsMbeanName = new ObjectName( JamesJmxObjectNameEnum.DNS_SERVICE.getObjectName() ); 
			 DNSJavaService dnsListMbeanProxy = (DNSJavaService) JMX.newMBeanProxy(mBeanServerConnection, jamesDnsMbeanName, org.apache.james.dnsservice.dnsjava.DNSJavaService.class);
	
			 String[] allDns = dnsListMbeanProxy.getDNSServers();
			 
			 for(int i=0;i<allDns.length;i++)
				 LOG.debug("\nJames DNS = " +allDns[i]);  
				   
			 return allDns;	 
		    
		  }catch(Exception e){ 
			  LOG.error("Error reading DNS server, cause: ",e);
      		  return null;
      		 
      	 }finally{
      		JmxServerConnectionManager.closeJmxServerConnection(); 
      	 }
	  }
*/	  
	  
	  /**
	   * Return all the domain configured in Apache James
	   * @return An array with all the configured Doamins. Return null in case of error
	   */
      public String[] getDomains() { 
    	  
    	 try{  
	          MBeanServerConnection mBeanServerConnection = JmxServerConnectionManager.getJmxServerConnection();
	    	  
	          ObjectName jamesDomainsMbeanName = new ObjectName( JamesJmxObjectNameEnum.DOMAIN_LIST.getObjectName() ); 
	          DomainListManagementMBean domainListMbeanProxy = (DomainListManagementMBean) JMX.newMBeanProxy(mBeanServerConnection, jamesDomainsMbeanName, org.apache.james.domainlist.api.DomainListManagementMBean.class);
	        
	  	      String[] allDomains = domainListMbeanProxy.getDomains();
	  	      
	  	      return allDomains;
  	      
    	   }catch(Exception e){  
    		  LOG.error("Error reading james domains, cause: ",e);
      		  return null;
      		 
      	 }finally{
      		 JmxServerConnectionManager.closeJmxServerConnection(); 
      	 }
      }
      
      /**
       * Add a new domain in James server
       * @param newDomain
       * @return true if the operation was executed successfully
       */
      public boolean addDomain(String newDomain){
    	  
    	  try{
  	          MBeanServerConnection mBeanServerConnection = JmxServerConnectionManager.getJmxServerConnection();
  	          ObjectName jamesUsersMbeanName = new ObjectName( JamesJmxObjectNameEnum.DOMAIN_LIST.getObjectName() );

  	          DomainListManagementMBean domainListManagementMbeanProxy = (DomainListManagementMBean) JMX.newMBeanProxy(mBeanServerConnection, jamesUsersMbeanName, org.apache.james.domainlist.api.DomainListManagementMBean.class);
  		      domainListManagementMbeanProxy.addDomain(newDomain);
  	     
  		      return true;
  	    
      	 }catch(Exception e){  
      		 LOG.error("Error adding new james domain, cause: ",e);
      		 return false;
      		 
      	 }finally{
      		 JmxServerConnectionManager.closeJmxServerConnection(); 
      	 }	
    	  
      }
      
      
      /**
       * remove an existing domain in James server
       * @param domainToRemove
       * @return true if the operation was executed successfully
       */
      public boolean removeDomain(String domainToRemove){
    	  
    	  try{
  	          MBeanServerConnection mBeanServerConnection = JmxServerConnectionManager.getJmxServerConnection();
  	          ObjectName jamesUsersMbeanName = new ObjectName( JamesJmxObjectNameEnum.DOMAIN_LIST.getObjectName() );

  	          DomainListManagementMBean domainListManagementMbeanProxy = (DomainListManagementMBean) JMX.newMBeanProxy(mBeanServerConnection, jamesUsersMbeanName, org.apache.james.domainlist.api.DomainListManagementMBean.class);
  	          domainListManagementMbeanProxy.removeDomain(domainToRemove);
  	     
  		     return true;
  	    
      	 }catch(Exception e){  
      		 LOG.error("Error removing james domain, cause: ",e);
      		 return false;
      		 
      	 }finally{
      		 JmxServerConnectionManager.closeJmxServerConnection(); 
      	 }	
    	  
      }
      
      /**
       * Check if James contains the in argument domain
       * @param domain
       * @return true if James server contains the provided domain
       */
      public boolean containsDomain(String domain){
    	  
    	  try{
  	          MBeanServerConnection mBeanServerConnection = JmxServerConnectionManager.getJmxServerConnection();
  	          ObjectName jamesUsersMbeanName = new ObjectName( JamesJmxObjectNameEnum.DOMAIN_LIST.getObjectName() );

  	          DomainListManagementMBean domainListManagementMbeanProxy = (DomainListManagementMBean) JMX.newMBeanProxy(mBeanServerConnection, jamesUsersMbeanName, org.apache.james.domainlist.api.DomainListManagementMBean.class);
  	          return domainListManagementMbeanProxy.containsDomain(domain);
   	    
      	 }catch(Exception e){  
      		 LOG.error("Error removing james domain, cause: ",e);
      		 return false;
      		 
      	 }finally{
      		 JmxServerConnectionManager.closeJmxServerConnection(); 
      	 }	
    	  
      }
		    
	 /**
	  * Get all the users configured in Apache James	 
	  * @return An array with all the user configured in Apache James. Return null in case of error
	  */
     public String[] getAllusers() {    	
      
       try{
	        MBeanServerConnection mBeanServerConnection = JmxServerConnectionManager.getJmxServerConnection();
	        ObjectName jamesUsersMbeanName = new ObjectName( JamesJmxObjectNameEnum.USER_REPOSITORY.getObjectName() ); 

	        UsersRepositoryManagementMBean usersRepositoryManagementMbeanProxy = (UsersRepositoryManagementMBean) JMX.newMBeanProxy(mBeanServerConnection, jamesUsersMbeanName, org.apache.james.user.api.UsersRepositoryManagementMBean.class);
	         
		    String[] allUser = usersRepositoryManagementMbeanProxy.listAllUsers();
		    //LOG.debug("\nJames total users = "+allUser.length);
		    
		    //for(int i=0;i<allUser.length;i++)
		       //LOG.trace("\nJames User = " +allUser[i]);
	     
		    return allUser;
	    
    	 }catch(Exception e){  
    		 LOG.error("Error reading james users, cause: ",e);
    		 return null;
    		 
    	 }finally{
    		 JmxServerConnectionManager.closeJmxServerConnection(); 
    	 }	
    }
     
     /**
      * Add a new user in the Apache James server
      * @param newUser
      * @param userPassword
      * @return true if the operation was executed successfully
      */
     public boolean addUser(String newUser, String userPassword)  {   
    	 
    	 try{
	         MBeanServerConnection mBeanServerConnection = JmxServerConnectionManager.getJmxServerConnection();
	         ObjectName jamesUsersMbeanName = new ObjectName( JamesJmxObjectNameEnum.USER_REPOSITORY.getObjectName() );
	
	         UsersRepositoryManagementMBean usersRepositoryManagementMbeanProxy = (UsersRepositoryManagementMBean) JMX.newMBeanProxy(mBeanServerConnection, jamesUsersMbeanName, org.apache.james.user.api.UsersRepositoryManagementMBean.class);
	         usersRepositoryManagementMbeanProxy.addUser(newUser, userPassword);
          
             return true;
    	 }catch(Exception e){    
    		 LOG.error("Error adding new james user: "+ newUser+" with password: "+userPassword+ "cause: ",e);
    		 return false;
    		 
    	 }finally{
    		 JmxServerConnectionManager.closeJmxServerConnection(); 
    	 }
     }
     
    /**
     * Delete a user from Apache James	
     * @param userToDelete Must in the form user@domain
     * @return true if the operation was executed successfully
     */
     public boolean deleteUser(String userToDelete) {  
    	 
    	 try{ 
	         MBeanServerConnection mBeanServerConnection = JmxServerConnectionManager.getJmxServerConnection();
	         ObjectName jamesUsersMbeanName = new ObjectName( JamesJmxObjectNameEnum.USER_REPOSITORY.getObjectName() );
	
	         UsersRepositoryManagementMBean usersRepositoryManagementMbeanProxy = (UsersRepositoryManagementMBean) JMX.newMBeanProxy(mBeanServerConnection, jamesUsersMbeanName, org.apache.james.user.api.UsersRepositoryManagementMBean.class);
	         usersRepositoryManagementMbeanProxy.deleteUser(userToDelete);
	          
	         return true;
       	 }catch(Exception e){
       		 LOG.error("Error deleting james user: "+ userToDelete+" cause: ",e);
       		 return false;
       		 
       	 }finally{
       		 JmxServerConnectionManager.closeJmxServerConnection(); 
       	 }
     }
     
     /**
      * Check if a user exist in Apache James
      * @param userToCheck Must in the form user@domain
      * @return true if the user exist
      */
     public boolean verifyExistUser(String userToCheck) {  
    	 
    	 try{ 
	         MBeanServerConnection mBeanServerConnection = JmxServerConnectionManager.getJmxServerConnection();
	         ObjectName jamesUsersMbeanName = new ObjectName( JamesJmxObjectNameEnum.USER_REPOSITORY.getObjectName() );
	
	         UsersRepositoryManagementMBean usersRepositoryManagementMbeanProxy = (UsersRepositoryManagementMBean) JMX.newMBeanProxy(mBeanServerConnection, jamesUsersMbeanName, org.apache.james.user.api.UsersRepositoryManagementMBean.class);
	         usersRepositoryManagementMbeanProxy.verifyExists(userToCheck);
         
	         return true;
	   	 }catch(Exception e){  
	   		 LOG.error("Error checking existence of james user: "+ userToCheck+" cause: ",e);
	   		 return false;
	   		 
	   	 }finally{
	   		JmxServerConnectionManager.closeJmxServerConnection(); 
	   	 }  	 
     } 
     
     
     /**
      * Change the password for the given user
      * @param user
      * @param newPassword
      * @return true if the operation execute successfully
      */
     public boolean changeUserPassword(String user, String newPassword) {  
    	 
    	 try{ 
	         MBeanServerConnection mBeanServerConnection = JmxServerConnectionManager.getJmxServerConnection();
	         ObjectName jamesUsersMbeanName = new ObjectName( JamesJmxObjectNameEnum.USER_REPOSITORY.getObjectName() );
	
	         UsersRepositoryManagementMBean usersRepositoryManagementMbeanProxy = (UsersRepositoryManagementMBean) JMX.newMBeanProxy(mBeanServerConnection, jamesUsersMbeanName, org.apache.james.user.api.UsersRepositoryManagementMBean.class);
	         usersRepositoryManagementMbeanProxy.setPassword(user, newPassword);
         
	         return true;
	   	 }catch(Exception e){  
	   		 LOG.error("Error changing user password for user: "+ user+" cause: ",e);
	   		 return false;
	   		 
	   	 }finally{
	   		JmxServerConnectionManager.closeJmxServerConnection(); 
	   	 }  	 
     } 
     
     
     //----------------------------------- IMAP server start/stop ------------------------------------
     
     /**
      * Get the Imap server status (start/stop) 
      * @return true if the server is started  
      */
     public boolean isImapServerStarted() throws Exception{
    	 
    	 try{ 
	         MBeanServerConnection mBeanServerConnection = JmxServerConnectionManager.getJmxServerConnection();
	         ObjectName jamesUsersMbeanName = new ObjectName( JamesJmxObjectNameEnum.IMAP_SERVER.getObjectName() );
	
	         IMAPServerMBean imapServerMBeanProxy = (IMAPServerMBean) JMX.newMBeanProxy(mBeanServerConnection, jamesUsersMbeanName, org.apache.james.imapserver.netty.IMAPServerMBean.class);
	         return imapServerMBeanProxy.isStarted();
	         	         
	   	 }catch(Exception e){  
	   		LOG.error("Error checking Imap server status, cause: ",e);
	   		throw e;	   		 
	   	 }finally{
	   		JmxServerConnectionManager.closeJmxServerConnection(); 
	   	 }
     }     
     
     /**
      * Stop the Imap server. 
      * @return true if the server stop successfully or is already stopped
      * @throws Exception If some problems happen invoking the operation
      */
     public boolean stopImapServer() throws Exception{
		
    	 try{ 
	         MBeanServerConnection mBeanServerConnection = JmxServerConnectionManager.getJmxServerConnection();
	         ObjectName jamesUsersMbeanName = new ObjectName( JamesJmxObjectNameEnum.IMAP_SERVER.getObjectName() );
	
	         IMAPServerMBean imapServerMBeanProxy = (IMAPServerMBean) JMX.newMBeanProxy(mBeanServerConnection, jamesUsersMbeanName,  org.apache.james.imapserver.netty.IMAPServerMBean.class);
	         return imapServerMBeanProxy.stop();
	         	         
	   	 }catch(Exception e){  
	   		LOG.error("Error Stopping Imap server, cause: ",e);
	   		throw e;	   		 
	   	 }finally{
	   		JmxServerConnectionManager.closeJmxServerConnection(); 
	   	 } 
     }
     
     
     /**
      * Start the Imap server. 
      * @return false if the server is already started, else return true if the server start successfully
      * @throws Exception If some problems happen invoking the operation
      */
     public boolean startImapServer() throws Exception{
    	
    	 try{ 
	         MBeanServerConnection mBeanServerConnection = JmxServerConnectionManager.getJmxServerConnection();
	         ObjectName jamesUsersMbeanName = new ObjectName( JamesJmxObjectNameEnum.IMAP_SERVER.getObjectName() );
	
	         IMAPServerMBean imapServerMBeanProxy = (IMAPServerMBean) JMX.newMBeanProxy(mBeanServerConnection, jamesUsersMbeanName, org.apache.james.imapserver.netty.IMAPServerMBean.class);
	         return imapServerMBeanProxy.start();  //imapServerMBeanProxy.isStarted()
	         	         
	   	 }catch(Exception e){  
	   		LOG.error("Error Starting Imap server, cause: ",e);
	   		throw e;	   		 
	   	 }finally{
	   		JmxServerConnectionManager.closeJmxServerConnection(); 
	   	 } 
     }
     
     //----------------------------------- POP3 server start/stop ------------------------------------

     /**
      * Get the POP3 server status (start/stop) 
      * @return true if the server is started  
      */
     public boolean isPop3ServerStarted() throws Exception{
    	 
    	 try{ 
	         MBeanServerConnection mBeanServerConnection = JmxServerConnectionManager.getJmxServerConnection();
	         ObjectName jamesUsersMbeanName = new ObjectName( JamesJmxObjectNameEnum.POP3_SERVER.getObjectName() );
	
	         POP3ServerMBean pop3ServerMBeanProxy = (POP3ServerMBean) JMX.newMBeanProxy(mBeanServerConnection, jamesUsersMbeanName, org.apache.james.pop3server.netty.POP3ServerMBean.class);
	         return pop3ServerMBeanProxy.isStarted();
	         	         
	   	 }catch(Exception e){  
	   		LOG.error("Error checking POP3 server status, cause: ",e);
	   		throw e;	   		 
	   	 }finally{
	   		JmxServerConnectionManager.closeJmxServerConnection(); 
	   	 }
     }
     
     /**
      * Start the POP3 server. 
      * @return false if the server is already started, else return true if the server start successfully
      * @throws Exception If some problems happen invoking the operation
      */
     public boolean startPop3Server() throws Exception{
    	 
    	 try{ 
	         MBeanServerConnection mBeanServerConnection = JmxServerConnectionManager.getJmxServerConnection();
	         ObjectName jamesUsersMbeanName = new ObjectName( JamesJmxObjectNameEnum.POP3_SERVER.getObjectName() );
	
	         POP3ServerMBean pop3ServerMBeanProxy = (POP3ServerMBean) JMX.newMBeanProxy(mBeanServerConnection, jamesUsersMbeanName, org.apache.james.pop3server.netty.POP3ServerMBean.class);
	         return pop3ServerMBeanProxy.start();
	         	         
	   	 }catch(Exception e){  
	   		LOG.error("Error Starting POP3 server, cause: ",e);
	   		throw e;	   		 
	   	 }finally{
	   		JmxServerConnectionManager.closeJmxServerConnection(); 
	   	 }
     }
     
     /**
      * Stop the POP3 server. 
      * @return true if the server is already stopped, or if the server start successfully
      * @throws Exception If some problems happen invoking the operation
      */
     public boolean stopPop3Server() throws Exception{
    	
    	 try{ 
	         MBeanServerConnection mBeanServerConnection = JmxServerConnectionManager.getJmxServerConnection();
	         ObjectName jamesUsersMbeanName = new ObjectName( JamesJmxObjectNameEnum.POP3_SERVER.getObjectName() );
	         
	         POP3ServerMBean pop3ServerMBeanProxy = (POP3ServerMBean) JMX.newMBeanProxy(mBeanServerConnection, jamesUsersMbeanName, org.apache.james.pop3server.netty.POP3ServerMBean.class);
	         return pop3ServerMBeanProxy.stop();
	               
	   	 }catch(Exception e){  
	   		LOG.error("Error Stopping POP3 server, cause: ",e);
	   		throw e;	   		 
	   	 }finally{
	   		JmxServerConnectionManager.closeJmxServerConnection(); 
	   	 }
     }
     
     //---------------------------- SMTP server start/stop --------------------------------------
     
     /**
      * Get the SMTP server status (start/stop) 
      * @return true if the server is started  
      */
     public boolean isSmtpServerStarted() throws Exception{
    	 
    	 try{ 
	         MBeanServerConnection mBeanServerConnection = JmxServerConnectionManager.getJmxServerConnection();
	         ObjectName jamesUsersMbeanName = new ObjectName( JamesJmxObjectNameEnum.SMTP_SERVER.getObjectName() );
	
	         SMTPServerMBean smtpServerMBeanProxy = (SMTPServerMBean) JMX.newMBeanProxy(mBeanServerConnection, jamesUsersMbeanName, org.apache.james.smtpserver.netty.SMTPServerMBean.class);
	         return smtpServerMBeanProxy.isStarted();
	         	         
	   	 }catch(Exception e){  
	   		LOG.error("Error checking SMTP server status, cause: ",e);
	   		throw e;	   		 
	   	 }finally{
	   		JmxServerConnectionManager.closeJmxServerConnection(); 
	   	 } 
     }
     
     /**
      * Start the SMTP server. 
      * @return false if the server is already started, else return true if the server start successfully
      * @throws Exception If some problems happen invoking the operation
      */
     public boolean startSmtpServer() throws Exception{
    	 
    	 try{ 
	         MBeanServerConnection mBeanServerConnection = JmxServerConnectionManager.getJmxServerConnection();
	         ObjectName jamesUsersMbeanName = new ObjectName( JamesJmxObjectNameEnum.SMTP_SERVER.getObjectName() );
	
	         SMTPServerMBean smtpServerMBeanProxy = (SMTPServerMBean) JMX.newMBeanProxy(mBeanServerConnection, jamesUsersMbeanName, org.apache.james.smtpserver.netty.SMTPServerMBean.class);
	         return smtpServerMBeanProxy.start();
	         	         
	   	 }catch(Exception e){  
	   		LOG.error("Error Starting Imap server, cause: ",e);
	   		throw e;	   		 
	   	 }finally{
	   		JmxServerConnectionManager.closeJmxServerConnection(); 
	   	 } 
     }
     
     
     /**
      * Start the SMTP server. 
      * @return true if the server is already stopped, or if the server start successfully
      * @throws Exception If some problems happen invoking the operation
      */
     public boolean stopSmtpServer() throws Exception{
    	 
    	 try{ 
	         MBeanServerConnection mBeanServerConnection = JmxServerConnectionManager.getJmxServerConnection();
	         ObjectName jamesUsersMbeanName = new ObjectName( JamesJmxObjectNameEnum.SMTP_SERVER.getObjectName() );
	
	         SMTPServerMBean smtpServerMBeanProxy = (SMTPServerMBean) JMX.newMBeanProxy(mBeanServerConnection, jamesUsersMbeanName, org.apache.james.smtpserver.netty.SMTPServerMBean.class);
	         return smtpServerMBeanProxy.stop();
	         	         
	   	 }catch(Exception e){  
	   		LOG.error("Error Stopping Imap server, cause: ",e);
	   		throw e;	   		 
	   	 }finally{
	   		JmxServerConnectionManager.closeJmxServerConnection(); 
	   	 } 
     }     
     
     
    //------------------------ Address Mapping Management: RECIPIENT_REWRITE_TABLE ---------------------------
     
    /**
     * Return all Recipient Rewrite mappings 
     * @return  A List of RecipientRewriteMapping beans where the key is the auser@domain and the value is a Collection which holds all mappings for that key
     * @throws Exception
     */
    public List<RecipientRewriteMapping> listMappings() {   
    	
    	List<RecipientRewriteMapping> mappingList = new ArrayList<RecipientRewriteMapping>();
    	
    	try{ 
	         MBeanServerConnection mBeanServerConnection = JmxServerConnectionManager.getJmxServerConnection();
	         ObjectName jamesUsersMbeanName = new ObjectName( JamesJmxObjectNameEnum.RECIPIENT_REWRITE_TABLE.getObjectName() );
	
	         RecipientRewriteTableManagementMBean recipientRewriteTableMBeanProxy = (RecipientRewriteTableManagementMBean) JMX.newMBeanProxy(mBeanServerConnection, jamesUsersMbeanName, org.apache.james.rrt.api.RecipientRewriteTableManagementMBean.class);
	         Map<String, Collection<String>> m = recipientRewriteTableMBeanProxy.getAllMappings();
	         
	         Collection<String> mappingsCollection;
	         Set<String> mappingsSet;
	         
	         if(m !=null) //if m=null no mapping found
	         {	 
		         //transform the returned collection in a useful format for the front-end
		         for (String userAndDomain : m.keySet()) 
		         {
		        	 mappingsCollection = m.get(userAndDomain);
	
		        	 if (mappingsCollection instanceof Set) {
		        		 mappingsSet = (Set<String>) mappingsCollection;
		        	 }else{
		        		 mappingsSet = new HashSet<String>(mappingsCollection);
		        	 }
		        	 mappingList.add(new RecipientRewriteMapping(userAndDomain, mappingsSet));
		         }
	         }

	         return mappingList;	         
	         
	   	 }catch(Exception e){  
	   		LOG.error("Error loading all RecipientRewrite Mappings, cause: ",e);
	   		return null;	   		 
	   	 }finally{
	   		JmxServerConnectionManager.closeJmxServerConnection(); 
	   	 }     	   
    }
    
    /**
     * return the userDomain mapping for a given user and domain pair
     * @param user the user without domain part
     * @param domain the domain without @
     * @return
     * @throws Exception
     */
    public Collection<String> getUserDomainMappings(String user, String domain) throws Exception {
    	
    	 try{ 
	         MBeanServerConnection mBeanServerConnection = JmxServerConnectionManager.getJmxServerConnection();
	         ObjectName jamesUsersMbeanName = new ObjectName( JamesJmxObjectNameEnum.RECIPIENT_REWRITE_TABLE.getObjectName() );
	
	         RecipientRewriteTableManagementMBean recipientRewriteTableMBeanProxy = (RecipientRewriteTableManagementMBean) JMX.newMBeanProxy(mBeanServerConnection, jamesUsersMbeanName, org.apache.james.rrt.api.RecipientRewriteTableManagementMBean.class);
	         return recipientRewriteTableMBeanProxy.getUserDomainMappings(user, domain);
	         
	   	 }catch(Exception e){  
	   		LOG.error("Error loading userDomain Mapping for user:"+user+", domain:"+domain+" cause:",e);
	   		throw e;	   		 
	   	 }finally{
	   		JmxServerConnectionManager.closeJmxServerConnection(); 
	   	 } 
    }
     
     /**
      * Add a new address mapping for a user and domain
      *
      * @param user the user without domain part (eg: john)
      * @param domain the domain part that contains the user (eg: thedomain)
      * @param address a mail address with the domain part (es: test@mydom.com) that address can exist or not
      * @throws Exception
      * 
      * The inserted mapping appear in the mapping list as:
      * john@thedomain=[test@mydom.com]
      */
     public void addAddressMapping(String user, String domain, String address) throws Exception {
      
    	 try{ 
	         MBeanServerConnection mBeanServerConnection = JmxServerConnectionManager.getJmxServerConnection();
	         ObjectName jamesUsersMbeanName = new ObjectName( JamesJmxObjectNameEnum.RECIPIENT_REWRITE_TABLE.getObjectName() );
	
	         RecipientRewriteTableManagementMBean recipientRewriteTableMBeanProxy = (RecipientRewriteTableManagementMBean) JMX.newMBeanProxy(mBeanServerConnection, jamesUsersMbeanName, org.apache.james.rrt.api.RecipientRewriteTableManagementMBean.class);
	         recipientRewriteTableMBeanProxy.addAddressMapping(user, domain, address);
	         
	   	 }catch(Exception e){  
	   		LOG.error("Error adding a new addressMapping for user:"+user+", domain:"+domain+", address:"+address+" cause:",e);
	   		throw e;	   		 
	   	 }finally{
	   		JmxServerConnectionManager.closeJmxServerConnection(); 
	   	 }    	 
     }

     /**
      * Add a new regular expression mapping for the user and domain
      *
      * @param user an existing user without doamin part
      * @param domain an existing domain
      * @param regex a valid regular expression (eg: ^aname)
      * @throws Exception
      * 
      * The inserted mapping appear in the mapping list as:
      * user@domain=[regex:^aname]
      */
     public void addRegexMapping(String user, String domain, String aregexp) throws Exception {
    	 
    	 try{ 
	         MBeanServerConnection mBeanServerConnection = JmxServerConnectionManager.getJmxServerConnection();
	         ObjectName jamesUsersMbeanName = new ObjectName( JamesJmxObjectNameEnum.RECIPIENT_REWRITE_TABLE.getObjectName() );
	
	         RecipientRewriteTableManagementMBean recipientRewriteTableMBeanProxy = (RecipientRewriteTableManagementMBean) JMX.newMBeanProxy(mBeanServerConnection, jamesUsersMbeanName, org.apache.james.rrt.api.RecipientRewriteTableManagementMBean.class);
	         recipientRewriteTableMBeanProxy.addRegexMapping(user, domain, aregexp);
	         
	   	 }catch(Exception e){  
	   		LOG.error("Error adding a new regexp addressMapping for user:"+user+", domain:"+domain+", regexp:"+aregexp+", cause:",e);
	   		throw e;	   		 
	   	 }finally{
	   		JmxServerConnectionManager.closeJmxServerConnection(); 
	   	 } 
     }
     
     /**
      * Removes the address mapping for a user and domain
      *
      * @param user
      * @param domain
      * @param address
      * @throws Exception
      */  
     public void removeAddressMapping(String user, String domain, String address) throws Exception {
      
    	 try{ 
	         MBeanServerConnection mBeanServerConnection = JmxServerConnectionManager.getJmxServerConnection();
	         ObjectName jamesUsersMbeanName = new ObjectName( JamesJmxObjectNameEnum.RECIPIENT_REWRITE_TABLE.getObjectName() );
	
	         RecipientRewriteTableManagementMBean recipientRewriteTableMBeanProxy = (RecipientRewriteTableManagementMBean) JMX.newMBeanProxy(mBeanServerConnection, jamesUsersMbeanName, org.apache.james.rrt.api.RecipientRewriteTableManagementMBean.class);
	         recipientRewriteTableMBeanProxy.removeAddressMapping(user, domain, address);
	         
	   	 }catch(Exception e){  
	   		LOG.error("Error removing addressMapping for user: "+user+", domain: "+domain+", address: "+address+", cause: ",e);
	   		throw e;	   		 
	   	 }finally{
	   		JmxServerConnectionManager.closeJmxServerConnection(); 
	   	 }    	 
     }
     
     
     /**
      * Removes the regular expression mapping for a user and domain
      *
      * @param user
      * @param domain
      * @param regex
      * @throws Exception
      */
     public void removeRegexMapping(String user, String domain, String regex) throws Exception {
      
    	 try{ 
	         MBeanServerConnection mBeanServerConnection = JmxServerConnectionManager.getJmxServerConnection();
	         ObjectName jamesUsersMbeanName = new ObjectName( JamesJmxObjectNameEnum.RECIPIENT_REWRITE_TABLE.getObjectName() );
	
	         RecipientRewriteTableManagementMBean recipientRewriteTableMBeanProxy = (RecipientRewriteTableManagementMBean) JMX.newMBeanProxy(mBeanServerConnection, jamesUsersMbeanName, org.apache.james.rrt.api.RecipientRewriteTableManagementMBean.class);
	         recipientRewriteTableMBeanProxy.removeRegexMapping(user, domain, regex);
	         
	   	 }catch(Exception e){  
	   		LOG.error("Error removing regexp addressMapping for user:"+user+", domain:"+domain+", regex:"+regex+", cause:",e);
	   		throw e;	   		 
	   	 }finally{
	   		JmxServerConnectionManager.closeJmxServerConnection(); 
	   	 }  
     }
     
     
     /**
      * Add an error mapping for a user and domain
      *
      * @param user an existing james user
      * @param domain an existing domain (the one than own the user or not)
      * @param error the error mapping
      * @throws Exception
      * 
      * The inserted mapping appear in List domains with:
      * user@domain=[error:errorMapping]
      */
     public void addErrorMapping(String user, String domain, String errorMapping) throws Exception {
    	 
    	 try{ 
	         MBeanServerConnection mBeanServerConnection = JmxServerConnectionManager.getJmxServerConnection();
	         ObjectName jamesUsersMbeanName = new ObjectName( JamesJmxObjectNameEnum.RECIPIENT_REWRITE_TABLE.getObjectName() );
	
	         RecipientRewriteTableManagementMBean recipientRewriteTableMBeanProxy = (RecipientRewriteTableManagementMBean) JMX.newMBeanProxy(mBeanServerConnection, jamesUsersMbeanName, org.apache.james.rrt.api.RecipientRewriteTableManagementMBean.class);
	         recipientRewriteTableMBeanProxy.addErrorMapping(user, domain, errorMapping);
	         
	   	 }catch(Exception e){  
	   		LOG.error("Error adding error Mapping for user:"+user+", domain:"+domain+", error:"+errorMapping+", cause:",e);
	   		throw e;	   		 
	   	 }finally{
	   		JmxServerConnectionManager.closeJmxServerConnection(); 
	   	 }
     }
     
     /**
      * Remove an error mapping for a user and domain
      *
      * @param user
      * @param domain
      * @param error
      * @throws Exception
      */
     public void removeErrorMapping(String user, String domain, String error) throws Exception {
         
    	 try{ 
	         MBeanServerConnection mBeanServerConnection = JmxServerConnectionManager.getJmxServerConnection();
	         ObjectName jamesUsersMbeanName = new ObjectName( JamesJmxObjectNameEnum.RECIPIENT_REWRITE_TABLE.getObjectName() );
	
	         RecipientRewriteTableManagementMBean recipientRewriteTableMBeanProxy = (RecipientRewriteTableManagementMBean) JMX.newMBeanProxy(mBeanServerConnection, jamesUsersMbeanName, org.apache.james.rrt.api.RecipientRewriteTableManagementMBean.class);
	         recipientRewriteTableMBeanProxy.removeErrorMapping(user, domain, error);
	         
	   	 }catch(Exception e){  
	   		LOG.error("Error removing error Mapping for user:"+user+", domain:"+domain+", error:"+error+", cause:",e);
	   		throw e;	   		 
	   	 }finally{
	   		JmxServerConnectionManager.closeJmxServerConnection(); 
	   	 }    	 
     }
     
     /**
      * Add a new domain mapping
      *
      * @param sourcedomain a valid James doamin
      * @param targetDomain another valid James Domain
      * @throws Exception
      * 
      * The new inserted domain appear in the list domain with:
      *  *@sourcedomain=[doamin:targetDomain]
      *  
      *  The two provided domains could not exist on James server (?!)
      */
     public void addDomainMapping(String sourcedomain, String targetDomain) throws Exception {
    	 
    	 try{ 
	         MBeanServerConnection mBeanServerConnection = JmxServerConnectionManager.getJmxServerConnection();
	         ObjectName jamesUsersMbeanName = new ObjectName( JamesJmxObjectNameEnum.RECIPIENT_REWRITE_TABLE.getObjectName() );
	
	         RecipientRewriteTableManagementMBean recipientRewriteTableMBeanProxy = (RecipientRewriteTableManagementMBean) JMX.newMBeanProxy(mBeanServerConnection, jamesUsersMbeanName, org.apache.james.rrt.api.RecipientRewriteTableManagementMBean.class);
	         recipientRewriteTableMBeanProxy.addDomainMapping(sourcedomain, targetDomain);
	         
	   	 }catch(Exception e){  
	   		LOG.error("Error adding Domain Mapping for sourcedomain:"+sourcedomain+", targetDomain:"+targetDomain+", cause:",e);
	   		throw e;	   		 
	   	 }finally{
	   		JmxServerConnectionManager.closeJmxServerConnection(); 
	   	 }   
     }
     
     /**
      * Remove a domain mapping
      * 
      * @param domain
      * @param targetDomain
      * @throws Exception
      */
     public void removeDomainMapping(String domain, String targetDomain) throws Exception {
    	 
    	 try{ 
	         MBeanServerConnection mBeanServerConnection = JmxServerConnectionManager.getJmxServerConnection();
	         ObjectName jamesUsersMbeanName = new ObjectName( JamesJmxObjectNameEnum.RECIPIENT_REWRITE_TABLE.getObjectName() );
	
	         RecipientRewriteTableManagementMBean recipientRewriteTableMBeanProxy = (RecipientRewriteTableManagementMBean) JMX.newMBeanProxy(mBeanServerConnection, jamesUsersMbeanName, org.apache.james.rrt.api.RecipientRewriteTableManagementMBean.class);
	         recipientRewriteTableMBeanProxy.removeDomainMapping(domain, targetDomain);
	         
	   	 }catch(Exception e){  
	   		LOG.error("Error removing a Domain Mapping for domain:"+domain+", targetDomain:"+targetDomain+", cause:",e);
	   		throw e;	   		 
	   	 }finally{
	   		JmxServerConnectionManager.closeJmxServerConnection(); 
         }
   }
     
}
