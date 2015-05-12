package org.apache.james.jamesui.backend.client.mailstore;

import java.io.IOException;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;;

/**
 * Checks for new incoming mail for a given user and show their content or delete them, depends on the input command
 *
 */
public class EmailChecker {

	public static final int SHOW_MESSAGES = 1;
	public static final int CLEAR_MESSAGES = 2; //delete user inbox messages
	public static final int SHOW_AND_CLEAR = SHOW_MESSAGES + CLEAR_MESSAGES;
	
	private Session session = null;
	
	// Constructor
	public EmailChecker() {
		
	}

	/**
	 * @param args
	 * @throws IOException 
	 * @throws MessagingException 
	 */
	 public static void main(String[] args) throws MessagingException, IOException {
		
		EmailChecker emailChecker = new EmailChecker();
		//emailChecker.checkInbox(CLEAR_MESSAGES);
		emailChecker.checkInbox(SHOW_MESSAGES);
	 }
	
	
	 /* For checking inbox folder for some user is necessary provide user and password of the user owner of the account */
	 public void checkInbox(int mode) throws MessagingException, IOException
	 {
	    if (mode == 0) return;
	    boolean show = (mode & SHOW_MESSAGES) > 0; // & esegue and bit a bit
	    boolean clear = (mode & CLEAR_MESSAGES) > 0;
	    String action = (show ? "Show" : "") + (show && clear ? " and " : "") + (clear ? "Clear" : "");
	    
	    System.out.println("Action to perform: "+action + " INBOX for myuser@mydomain.tld");
	    
	    session = Session.getDefaultInstance(System.getProperties(),null);
	    
	    /*
	      The Store class defines a database that holds a folder hierarchy together with its messages,
	      also specifies the access protocol that accesses folders and retrieves messages stored in folders.
	      The input protocol (imap) is mandatory
	      
	      NOTE: a Store object is necessary when you want read/manage the received messages in a mailbox,folder of a user.
	      Is not necessary if you want only connect to send a mail. 
	    */	     
	    Store store = session.getStore("imap");
	   
	    store.connect("localhost", "myuser@mydomain.tld", "test");
	    //store.connect("localhost", "myuser@mydomain.tld", "mypassword"); //<---- IMPORTANT: the password for the user, necessary to read,delete... his msg
	    Folder root = store.getDefaultFolder();
	    
	    Folder inbox = root.getFolder("inbox");
	    inbox.open(Folder.READ_WRITE);
	    Message[] msgs = inbox.getMessages();
	    
	    if (msgs.length == 0 && show)
	    {
	      System.out.println("No messages in inbox");
	    }
	    
	    System.out.println("Total msg found in the inbox: "+msgs.length);
	    //for each msg in the inbox print his informations
	    for (int i = 0; i < msgs.length; i++)
	    {
	      MimeMessage msg = (MimeMessage)msgs[i];
	      
	      if (show) //if action is show
	      { 
	    	//System.out.println(" size From: " +  msg.getFrom().length);
	        System.out.println(" From: " + msg.getFrom()[0]); //can be null if the java sender omit instruction msg.setFrom(); 
	        System.out.println(" Subject: " + msg.getSubject());
	        System.out.println(" Mail Boby: " + msg.getContent());
	      }
	      
	      if (clear)
	      {
	    	//mark as read any message and delete them (don't trash them)
	    	//note: the flag is marked on the mail server, sometimes can be necessary restart Thunderbird to update his inbox) 
	        msg.setFlag(Flags.Flag.DELETED, true);
	      }
	    }
	    inbox.close(true);  //Treu say that we want delete msg marked for deletion using Flag: setFlag(Flags.Flag.DELETED, true);
	    store.close();
	    
	    System.out.println("---- Done ----");
	  }

}
