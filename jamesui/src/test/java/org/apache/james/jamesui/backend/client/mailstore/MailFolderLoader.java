package org.apache.james.jamesui.backend.client.mailstore;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Load all the IMAPFolder for a given user
 *
 */
public class MailFolderLoader {

	private final static Logger LOG = LoggerFactory.getLogger(MailFolderLoader.class);
	
    /**
     * Load the mail folder for the given user
     * 
     * @param targetUser
     * @param userPassword
     * @param host
     * @param protocol
     * @return the IMAPFolder for the given user
     */
    public static List<String> loadFolders(String targetUser, String userPassword, String host, String protocol){  
    	
    	List<String> allFolderList = new ArrayList<String>();
    	
        try {        	
        	Session session = Session.getDefaultInstance(System.getProperties(),null);
        	Store store = session.getStore(protocol);
    		store.connect(host, targetUser,userPassword);
            
    		/* GET the default folders: INBOX,SENT;TRASH */ 
            com.sun.mail.imap.IMAPFolder folder = (com.sun.mail.imap.IMAPFolder) store.getDefaultFolder();
           
            for (Folder f : folder.list()) {
            	/* ie: INBOX,SENT;TRASH */
            	LOG.debug("Root Folder: " + f.getName()); 
               
            	allFolderList.add(f.getName());
            	walkFolders(f,allFolderList);
            }
          
        } catch (Exception e) {            
            LOG.error("Unable to get folders for User:" + targetUser+", with password: "+userPassword +", on host:"+host+ ", and protocol:"+protocol );
            LOG.error("Cause:",e);
        }
        
        return allFolderList;
    }

    /**
     * Utility method to walk through the folders and subfolders
     * @param rootFolder
     * @param allFolderList
     * @return a List with all the folders names found
     * @throws MessagingException
     */
    private static List<String> walkFolders(Folder rootFolder,List<String> allFolderList) throws MessagingException
    { 
    	Folder[] folder = rootFolder.list();
    	
        for (int i=0; i< folder.length; i++)
        {   	
           allFolderList.add(folder[i].getName());
           walkFolders(folder[i],allFolderList);           
       }        
        return allFolderList;
    }

}
