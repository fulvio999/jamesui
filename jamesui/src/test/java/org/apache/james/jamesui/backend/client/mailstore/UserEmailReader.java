package org.apache.james.jamesui.backend.client.mailstore;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/*
 * Read the email for the given user and folder
 * 
 */
public class UserEmailReader {

	private final static Logger LOG = LoggerFactory.getLogger(UserEmailReader.class);
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * Load the email messages in the provide folder
	 * @param user
	 * @param password
	 * @param host
	 * @param protocol
	 * @param folderName
	 * 
	 * @return A list of Email object, null in case of errors happens
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static List<Email> readEmail(String user, String password, String host, String protocol, String folderName) {
		
		LOG.debug("Reading Messages for user: "+user+" in the folder: "+folderName);
		
		List<Email> messageList = new ArrayList<Email>();
		
		Session sessioned = Session.getDefaultInstance(System.getProperties(),null);
		
		try {
			/* The input protocol (eg: imap) is mandatory */
			Store store = sessioned.getStore(protocol);		
			store.connect(host, user,password);
			
			/* Get a handle on the default folder */
			Folder folder = store.getDefaultFolder();		
			folder = folder.getFolder(folderName);
	
			/* Reading the Email Index in Read / Write Mode */
			folder.open(Folder.READ_WRITE); 
	
			/* Retrieve the messages */
			Message[] messages = folder.getMessages();
	
			if (messages.length == 0) {
				LOG.debug("No Message to Read for user: "+user+" in the folder: "+folderName);
			} else {
				LOG.debug("Total Messages Found:" + messages.length + "for user: "+user+" in the folder: "+folderName);
			}
					
			for (int messageNumber = 0; messageNumber < messages.length; messageNumber++) 
			{
				Email email = new Email();
				
				/* Retrieve the next message to be read */
				Message message = messages[messageNumber];
						
				/* "content" can be "String" if the body is only text or javax.mail.internet.MimeMultipart if there is an attachment */							
				String content = (String)message.getContent();
				
				LOG.trace("Subject: "+ message.getSubject());
				LOG.trace("Content: "+content);  //the text of the mail
				LOG.trace("From: "+message.getFrom()[0]);
				LOG.trace("Date: "+message.getSentDate());
				LOG.trace("Size: "+message.getSize() +" bytes");
				LOG.trace("----------------------------------");
				
				email.setSubject( message.getSubject());
				email.setContent(content);
				email.setFrom(message.getFrom()[0].toString());
				email.setSentDate(sdf.format( message.getSentDate() ));
				email.setSize(message.getSize()); //bytes
				
				/*
				Flags flags = message.getFlags();
				if (!message.isSet(Flags.Flag.SEEN) && !message.isSet(Flags.Flag.ANSWERED)) {
						//Here you will read all the email content which are unread
				}
				*/
				
				messageList.add(email);
			}
				
			folder.close(false); //the false param indicates that we don't want delete the message marked to deletion using Flag
			store.close();
		
		} catch (Exception e) {
			LOG.error("Error reading user mail, cause: ",e);			
			return null;
		}
		
		return messageList;
	}

}

