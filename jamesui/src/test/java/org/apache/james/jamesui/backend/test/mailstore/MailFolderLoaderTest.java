package org.apache.james.jamesui.backend.test.mailstore;

import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.james.jamesui.backend.client.mailstore.MailFolderLoader;


public class MailFolderLoaderTest extends TestCase {
	
	private String user;
	private String password;
	private String host;
	private String protocol;
	
	protected void setUp() throws Exception {
		super.setUp();		
	
		user = "myuser@mydomain.tld";
		password = "myuser";	
		protocol = "imap";
		host= "localhost";
	}
	
  /**
   * Load the mail folders for the given user
   */
   public void testFetchUserMailFolder() throws ConfigurationException {
     
      List<String> allFolderList = MailFolderLoader.loadFolders(user,password,host,protocol);
				
	  assertNotNull(allFolderList);
	  assertFalse(allFolderList.isEmpty());
      
      for(String f: allFolderList){
          System.out.println("Folder: "+f);
       }
		
	}

}
