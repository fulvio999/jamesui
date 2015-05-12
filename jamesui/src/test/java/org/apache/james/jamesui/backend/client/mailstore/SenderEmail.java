package org.apache.james.jamesui.backend.client.mailstore;

import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;


/**
 * Ok work
 * 
 * Scrive mail da un account ad altro account, entrambi configurati in James. NOTA: il messaggio non appare nella folder 
 * posta inviata di Thunderbird per l'utente che invia. Vedo però il messaggio nella inbox del destinatario
 * 
 * A livello di log di james vedo:
 * INFO  18:13:08,185 | james.smtpserver | Id='17299512' User='' Successfully spooled mail Mail1387905188083-ad508e37-9efd-4f65-9230-96f3eed81e6e from pippo@localhost on localhost/127.0.0.1 for [myuser@mydomain.tld]
 * INFO  18:13:08,195 | james.smtpserver | Id='17299512' User='' Connection closed for 127.0.0.1
 * INFO  18:13:08,572 | james.mailetcontext | Local delivered mail Mail1387905188083-ad508e37-9efd-4f65-9230-96f3eed81e6e sucessfully from pippo@localhost to myuser@mydomain.tld in folder INBOX
 *
 */
public class SenderEmail extends Authenticator
{	
	
  public static final int SHOW_MESSAGES = 1;
  public static final int CLEAR_MESSAGES = 2;
  public static final int SHOW_AND_CLEAR = SHOW_MESSAGES + CLEAR_MESSAGES;
  
  protected String from;
  protected Session session;
  protected PasswordAuthentication authentication;
  
  // Constructor
  public SenderEmail(String user, String host)
  {
    this(user, host, false);
  }
  
  //entry point
  public static void main(String args[]) throws MessagingException, IOException
  {
	  SenderEmail senderEmail = new SenderEmail("pippo","localhost");
	  senderEmail.sendMessage("myuser@mydomain.tld", "subjcetJava4773", "contentJava4773");
   }
  
  // Constructor
  public SenderEmail(String user, String host, boolean debug)
  {
    from = user + '@' + host;
    authentication = new PasswordAuthentication(user, user);
    Properties props = new Properties();
    props.put("mail.user", user);     
    props.put("mail.host", host);
    props.put("mail.debug", debug ? "true" : "false");
    props.put("mail.store.protocol", "pop3");
    props.put("mail.transport.protocol", "smtp");
    session = Session.getInstance(props, this);
  }
  
  
  public void sendMessage(String to, String subject, String content) throws MessagingException
  {
    System.out.println("SENDING message from " + from + " to " + to);
    System.out.println();        
    
    MimeMessage msg = new MimeMessage(session); 
    msg.addRecipients(Message.RecipientType.TO, to); 
    msg.setFrom(); //if omitted the mail client don't show and From address
    msg.setSubject(subject);
    msg.setText(content);
    
    Transport.send(msg);
    System.out.println("Message sent.");
  }
  

}