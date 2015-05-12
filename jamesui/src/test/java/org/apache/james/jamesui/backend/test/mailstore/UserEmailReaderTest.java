
package org.apache.james.jamesui.backend.test.mailstore;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

import junit.framework.TestCase;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.james.jamesui.backend.client.mailstore.Email;
import org.apache.james.jamesui.backend.client.mailstore.UserEmailReader;

/**
 * @author fulvio
 *
 */
public class UserEmailReaderTest extends TestCase {
	
	private String user;
	private String password;
	private String host;
	private String protocol;
	private String folderName;
	
	
	protected void setUp() throws Exception {
		super.setUp();		
	
		user = "myuser@mydomain.tld";
		password = "myuser";	
		//password = "5d5a582e5adf896ed6e1474c700b481a";	
		protocol = "imap";
		host= "localhost";
		folderName = "inbox"; //the folder chosen in the folder list table
	}
	
	
  /**
   * Load the mail folders for the given user
 * @throws IOException 
 * @throws MessagingException 
   */
   public void testReadEmail() throws ConfigurationException, MessagingException, IOException {
	  	   
	 List<Email> messages = UserEmailReader.readEmail(user,password,host,protocol,folderName);
	   
	 assertNotNull(messages);
	 
	 for(Email message: messages){
		 String content = (String)message.getContent();
		 
         System.out.println("Subject: "+ message.getSubject());
         System.out.println("Content: "+content);  //the text of the mail
         System.out.println("From: "+message.getFrom());
         System.out.println("Date: "+message.getSentDate());
         System.out.println("Size: "+message.getSize() +" bytes");
         System.out.println("----------------------------------");
      }	   

	}

}
