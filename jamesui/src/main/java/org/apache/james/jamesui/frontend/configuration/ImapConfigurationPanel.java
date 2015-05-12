
package org.apache.james.jamesui.frontend.configuration;


import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.james.jamesui.backend.client.jmx.JamesClient;
import org.apache.james.jamesui.backend.configuration.bean.ImapServer;
import org.apache.james.jamesui.backend.configuration.manager.EnvironmentConfigurationReader;
import org.apache.james.jamesui.backend.configuration.manager.ImapServerConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.Reindeer;

/**
 * Panel that allow to configure some parameters about IMAP server configuration writing the dedicated xml file
 * 
 * @author fulvio
 *
 */
public class ImapConfigurationPanel extends Panel {
	
	private static final long serialVersionUID = 1L;
	
	private final static Logger LOG = LoggerFactory.getLogger(ImapConfigurationPanel.class);
	
	private JamesClient jamesClient;
	private Label imapTitleLabel;	
	
	private Label serverStatusLabel; //running/stopped
	private Button startServerButton;
	private Button stopServerButton;
	
	private CheckBox imapServerEnabled;
	private TextField imapBindAddress;
	private TextField imapConnectionBacklog;
	private TextField imapConnectionLimit;
	private TextField imapConnectionLimitPerIP;
	private CheckBox imapHelloNameAutoDetect; 
	private TextField imapHelloName;
	
	//TLS
	private CheckBox imapSocketTls;
	private CheckBox imapStartTls;
	private TextField imapKeystore;
	private TextField imapSecret;
	private TextField imapProvider;	
	
	private Button saveImapConfigurationButton;
	
	/* the manager used to manage stored LMTP Server configuration */
	private ImapServerConfigManager imapServerConfigManager;

	/**
	 * Constructor
	 */
	public ImapConfigurationPanel(JamesClient client) {
		
		this.setSizeFull();  
		this.setStyleName(Reindeer.PANEL_LIGHT);
	       
		this.jamesClient = client;
		
		GridLayout panelLayout = new GridLayout(2,4);
	    panelLayout.setMargin(true);
	    panelLayout.setSpacing(true); 
	    panelLayout.setSizeFull();	    
	    
	    HorizontalLayout startStopServerLayout = new HorizontalLayout();
	    startStopServerLayout.setSpacing(true);
	    	    
	    this.imapServerConfigManager = new ImapServerConfigManager();
	    
	    FormLayout formLayout0 = new FormLayout(); 
	    FormLayout formLayout1 = new FormLayout(); 
	    FormLayout formLayout2 = new FormLayout(); 	   
	    FormLayout formLayout3 = new FormLayout(); 
		
	    this.stopServerButton = new Button("Stop Server");
	    this.startServerButton = new Button("Start Server");
	    this.serverStatusLabel = new Label("Server status:"); 
	    
	    /* Get current server status */
	    try{
			if(this.jamesClient.isImapServerStarted())
			{
			   serverStatusLabel.setValue("Server status: Running");
			   startServerButton.setEnabled(false);
			   stopServerButton.setEnabled(true);
			}else{
			   serverStatusLabel.setValue("Server status: Stopped");
			   startServerButton.setEnabled(true);
			   stopServerButton.setEnabled(false);
			}   
		}catch (Exception e1) {
		   serverStatusLabel.setValue("Server status: ??");
		}	    
	   
	    this.startServerButton.addClickListener(new StartServerButtonListener());	    
	    this.stopServerButton.addClickListener(new StopServerButtonListener());
	    
		this.imapTitleLabel = new Label("<b>IMAP Configuration</b>",ContentMode.HTML);	
		this.imapServerEnabled = new CheckBox("ServerEnabled");
		
		this.imapBindAddress = new TextField("Bind Address:");
		this.imapBindAddress.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
		
		this.imapConnectionBacklog = new TextField("Connection Backlog:");
		this.imapConnectionBacklog.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
		
		this.imapConnectionLimit = new TextField("Connection Limit:");
		this.imapConnectionLimit.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
		
		this.imapConnectionLimitPerIP = new TextField("Connection limit per IP:");
		this.imapConnectionLimitPerIP.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
		
		this.imapHelloNameAutoDetect = new CheckBox("Auto Detect Enabled:"); //true/false
		
		this.imapHelloName = new TextField("Hello Name:");
		this.imapHelloName.setWidth("300");
		this.imapHelloName.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
		
		//TLS
		this.imapSocketTls = new CheckBox("Socket TLS:");
		this.imapStartTls = new CheckBox("Start TLS:");
		this.imapKeystore = new TextField("Key Store:"); 
		this.imapKeystore.setWidth("300");
		
		this.imapSecret = new TextField("Secret:");
		this.imapSecret.setWidth("300");
		
		this.imapProvider = new TextField("Provider:");
		this.imapProvider.setWidth("300");
		
		this.saveImapConfigurationButton = new Button("Save");
		this.saveImapConfigurationButton.addClickListener(new SaveConfigurationButtonListener());
			
		loadCurrentConfig();
		
		startStopServerLayout.addComponent(serverStatusLabel);
		startStopServerLayout.addComponent(startServerButton);
		startStopServerLayout.addComponent(stopServerButton);
		
		panelLayout.addComponent(imapTitleLabel);	
		panelLayout.newLine();
		
		panelLayout.addComponent(startStopServerLayout);		
			
		formLayout1.addComponent(imapServerEnabled);		
		formLayout1.addComponent(imapBindAddress);	
		formLayout1.addComponent(imapConnectionBacklog);	
		formLayout1.addComponent(imapConnectionLimit);	
		formLayout1.addComponent(imapConnectionLimitPerIP);	
		
		formLayout2.addComponent(imapHelloNameAutoDetect);	
		formLayout2.addComponent(imapHelloName);
		
		formLayout1.addComponent(new Label("TLS Configuration"));
		formLayout1.addComponent(imapSocketTls);
		formLayout1.addComponent(imapStartTls);			
		formLayout1.addComponent(imapKeystore);	
		formLayout1.addComponent(imapSecret);	
		formLayout1.addComponent(imapProvider);	
			
		formLayout3.addComponent(saveImapConfigurationButton);	
		
		panelLayout.addComponent(formLayout0);
		panelLayout.newLine();
		
		panelLayout.addComponent(formLayout1);
		panelLayout.addComponent(formLayout2);	
		panelLayout.newLine();
		panelLayout.addComponent(formLayout3);		
		
		this.setContent(panelLayout);
	}
	
	/**
	 * Listener for the "Save" button
	 * @author fulvio
	 *
	 */
	private class SaveConfigurationButtonListener implements com.vaadin.ui.Button.ClickListener {

		private static final long serialVersionUID = 1L;

		public void buttonClick(ClickEvent event) {	
        	
			ImapServer imapServer = new ImapServer();
			
			imapServer.setImapServerEnabled(imapServerEnabled.getValue());
			imapServer.setBindAddress(imapBindAddress.getValue());
			imapServer.setConnectionBacklog(imapConnectionBacklog.getValue());
			imapServer.setKeystore(imapKeystore.getValue());
			imapServer.setSecret(imapSecret.getValue());
			imapServer.setProvider(imapProvider.getValue());
			
			imapServer.setHelloNameAutoDetect(imapHelloNameAutoDetect.getValue());		
			imapServer.setHelloName(imapHelloName.getValue());
			
			imapServer.setConnectionLimit(imapConnectionLimit.getValue());
			imapServer.setConnectionLimitPerIP(imapConnectionLimitPerIP.getValue());
			
			imapServer.setSocketTls(imapSocketTls.getValue());
			imapServer.setStartTls(imapStartTls.getValue());        	
    	
    		String JAMES_CONF_FOLDER = EnvironmentConfigurationReader.getJamesBaseDir()+File.separator+"conf";
    		LOG.debug("JAMES_CONF_FOLDER: "+JAMES_CONF_FOLDER);
    		
    		try {
    			imapServerConfigManager.updateConfiguration(imapServer,JAMES_CONF_FOLDER);	
    			Notification.show("Operation Executed Successfully !", Type.HUMANIZED_MESSAGE);
			} catch (ConfigurationException e) {
				LOG.error("Error saving DNS configuration, cause: ",e);
				Notification.show("Error saving IMAP configuration:"+e.getMessage(),  Type.ERROR_MESSAGE);					
			}
        }		
	}

	
	/**
	 * Listener for the "Start Server" button
	 * @author fulvio
	 *
	 */
	private class StartServerButtonListener implements com.vaadin.ui.Button.ClickListener {

		private static final long serialVersionUID = 1L;

		public void buttonClick(ClickEvent event) {
        	
			LOG.debug("Starting Imap Server...");				
			
    		try {
    			boolean result = jamesClient.startImapServer();
    			if(result)
    			{
    			   serverStatusLabel.setValue("Server status: Running");	    				
    			   Notification.show("Operation Executed Successfully !", Type.HUMANIZED_MESSAGE);
    			   startServerButton.setEnabled(false);
	    		   stopServerButton.setEnabled(true);
    			}   
			} catch (Exception e) {
				LOG.error("Error starting IMAP server, cause: ",e);
				Notification.show("Error starting IMAP server:"+e.getMessage(), Type.ERROR_MESSAGE);
			}
        }		
	}
	
	/**
	 * Listener for the "Stop Server" button
	 * @author fulvio
	 *
	 */
	private class StopServerButtonListener implements com.vaadin.ui.Button.ClickListener {

		private static final long serialVersionUID = 1L;

		public void buttonClick(ClickEvent event) {
        	
			LOG.debug("Stopping Imap Server...");				
			
    		try {
    			boolean result = jamesClient.stopImapServer();
    			if(result)
    			{
    				serverStatusLabel.setValue("Server status: Stopped");	    				
    				Notification.show("Operation Executed Successfully !", Type.HUMANIZED_MESSAGE);
    				startServerButton.setEnabled(true);
    				stopServerButton.setEnabled(false);
    			}
			}catch (Exception e) {
				LOG.error("Error stopping IMAP server, cause: ",e);
				Notification.show("Error stopping IMAP server:"+e.getMessage(), Type.ERROR_MESSAGE);
			}
         }		
	}
	
	/**
	 *  Load the currently stored IMAP configuration in the dedicated file and fill the form to display it 
	 */
	private void loadCurrentConfig() {
		
		ImapServer imapServer;
		
		try {
			imapServer = imapServerConfigManager.readConfiguration(EnvironmentConfigurationReader.getJamesBaseDir()+File.separator+"conf");
		
			imapServerEnabled.setValue(imapServer.isImapServerEnabled());
			imapBindAddress.setValue(imapServer.getBindAddress());
			imapConnectionBacklog.setValue(imapServer.getConnectionBacklog());
			imapConnectionLimit.setValue(imapServer.getConnectionLimit());
			imapConnectionLimitPerIP.setValue(imapServer.getConnectionLimitPerIP());
			imapHelloNameAutoDetect.setValue(imapServer.isHelloNameAutoDetect());
			imapHelloName.setValue(imapServer.getHelloName());
			
			imapSocketTls.setValue(imapServer.isSocketTls());
			imapStartTls.setValue(imapServer.isStartTls());
			imapKeystore.setValue(imapServer.getKeystore());
			imapSecret.setValue(imapServer.getSecret());
			imapProvider.setValue(imapServer.getProvider());		
		
		} catch (ConfigurationException e) {
			LOG.error("Error loading currently saved IMAP configuration, cause: ",e);			
		}
	}	

}
