package org.apache.james.jamesui.backend.client.mailstore;

import java.io.IOException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


//esempio da teach j2ee in 21 days pag. 484
public class SenderEmailWithAttachment extends Authenticator{

	  protected String from;
	  protected Session session;
	  protected PasswordAuthentication authentication;
	  
	  // Constructor
	  public SenderEmailWithAttachment(String user, String host)
	  {
	    this(user, host, false);
	  }
	  
	  //entry point
	  public static void main(String args[]) throws MessagingException, IOException
	  {
		  SenderEmailWithAttachment senderEmailWithAttachment = new SenderEmailWithAttachment("pippo","localhost");
		  senderEmailWithAttachment.sendMessage("myuser@mydomain.tld", "subjcetJava44Allegato", "contentJava44");
	   }
	  
	  // Constructor
	  public SenderEmailWithAttachment(String user, String host, boolean debug)
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
	   // msg.setText(content);
	    
	    //create message body part
	    BodyPart messageBodyPart = new MimeBodyPart();
	    messageBodyPart.setText("here is an attachment");
	    Multipart multipart = new MimeMultipart();
	    multipart.addBodyPart(messageBodyPart);
	    
	    //create the attachment body part
	    messageBodyPart = new MimeBodyPart();
	    DataSource source = new FileDataSource("/home/fulvio/derby.log");
	    messageBodyPart.setDataHandler(new DataHandler(source));
	    messageBodyPart.setFileName("/home/fulvio/derby.log");
	    
	    multipart.addBodyPart(messageBodyPart);
	    
	    //put part in message
	    msg.setContent(multipart);
	    
	    
	    Transport.send(msg);
	    System.out.println("Message sent.");
	  }

	

}
