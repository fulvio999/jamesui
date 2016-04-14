
package org.apache.james.jamesui.frontend.configuration;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.james.jamesui.backend.client.jmx.JamesClient;
import org.apache.james.jamesui.backend.configuration.bean.JamesuiConfiguration;
import org.apache.james.jamesui.backend.configuration.bean.SmtpServer;
import org.apache.james.jamesui.backend.configuration.manager.EnvironmentConfigurationReader;
import org.apache.james.jamesui.backend.configuration.manager.SmtpServerConfigManager;
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
 * Panel that allow to configure some parameters about SMTP server configuration writing the dedicated xml file
 *  
 * @author fulvio
 *
 */
public class SmtpConfigurationPanel extends Panel {	
	
	private static final long serialVersionUID = 1L;

	private final static Logger LOG = LoggerFactory.getLogger(SmtpConfigurationPanel.class);
	
	private JamesClient jamesClient;	
	private JamesuiConfiguration jamesuiConfiguration;
	
	private Label smtpTitleLabel;
	
	private Label serverStatusLabel; //running/stopped
	private Button startServerButton;
	private Button stopServerButton;
	
	private CheckBox smtpServerEnabledCheckBox;
	private TextField smtpBindAddress;
	private TextField smtpConnectionBacklog;
	
	//TLS config
	private CheckBox smtpsocketTls;
	private CheckBox smtpStartTls;
	private TextField smtpKeyStore;
	private TextField smtpSecret;
	private TextField smtpProvider;
	private TextField smtpAlgorithm;
	
	private CheckBox smtpHelloNameAutoDetect;
	private TextField smtpHelloName;
	
	private TextField smtpConnectiontimeout;
	private TextField smtpConnectionLimit;
	private TextField smtpConnectionLimitPerIP;
	private TextField smtpAuthorizedAddresses;
	private TextField smtpMaxMessagesize;
	private CheckBox smtpAddressBracketsEnforcement;
	private TextField smtpGreeting;
	
	private Button saveSmtpConfigurationButton;
	
	private SmtpServerConfigManager smtpServerConfigManager;

	/**
	 * Constructor
	 */
	public SmtpConfigurationPanel(JamesClient client, JamesuiConfiguration jamesuiConfiguration) {
		
		this.setSizeFull();  
		this.setStyleName(Reindeer.PANEL_LIGHT);
	    
		this.jamesClient = client;
		this.jamesuiConfiguration = jamesuiConfiguration;
		
	    GridLayout panelLayout = new GridLayout(2,4);
        panelLayout.setMargin(true);
        panelLayout.setSpacing(true); 
        panelLayout.setSizeFull();
        
        HorizontalLayout startStopServerLayout = new HorizontalLayout();
	    startStopServerLayout.setSpacing(true);
        
        this.smtpServerConfigManager = new SmtpServerConfigManager(jamesuiConfiguration);
        
        FormLayout formLayout0 = new FormLayout();        
        FormLayout formLayout = new FormLayout();       
        FormLayout formLayout3 = new FormLayout();        
        FormLayout formLayout5 = new FormLayout();
        
        this.stopServerButton = new Button("Stop Server");
        this.startServerButton = new Button("Start Server");
        this.serverStatusLabel = new Label("Server status: Running"); 
        
        /* Get current server status */
        try {
			if(this.jamesClient.isSmtpServerStarted()){
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
		
		this.smtpTitleLabel = new Label("<b>SMTP Configuration</b>",ContentMode.HTML);	
		this.smtpServerEnabledCheckBox = new CheckBox("Server Enabled");		
		this.smtpsocketTls = new CheckBox("Socket TLS:");	
		this.smtpStartTls = new CheckBox("Start TLS:");		
		this.smtpBindAddress = new TextField("Bind Address:");
		this.smtpBindAddress.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
		
		this.smtpConnectionBacklog = new TextField("Connection Backlog:");	
		this.smtpConnectionBacklog.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
		
		this.smtpKeyStore = new TextField("Key Store:"); 
		this.smtpKeyStore.setWidth("300");
		this.smtpKeyStore.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
		
		this.smtpSecret = new TextField("Secret:");
		this.smtpSecret.setWidth("300");
		this.smtpSecret.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
		
		this.smtpProvider = new TextField("Provider:");
		this.smtpProvider.setWidth("300");
		this.smtpProvider.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
		
		this.smtpAlgorithm = new TextField("Algorithm:");
		this.smtpAlgorithm.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
		
		this.smtpHelloNameAutoDetect = new CheckBox("Auto Detect Enabled");
		this.smtpHelloName = new TextField("Hello Name:");
		this.smtpHelloName.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
			
		this.smtpConnectiontimeout = new TextField("Connection Timeout:");
		this.smtpConnectiontimeout.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
		
		this.smtpConnectionLimit = new TextField("Connection Limit:");
		this.smtpConnectionLimit.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
		
		this.smtpConnectionLimitPerIP = new TextField("Connection Limit Per IP:");
		this.smtpConnectionLimitPerIP.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
		
		this.smtpAuthorizedAddresses = new TextField("Authorized Address:");
		this.smtpAuthorizedAddresses.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
		
		this.smtpMaxMessagesize = new TextField("Max Message size:");	
		this.smtpMaxMessagesize.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
		
		this.smtpGreeting = new TextField("Greeting:");
		this.smtpGreeting.setWidth("300");
		this.smtpGreeting.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
		
		this.smtpAddressBracketsEnforcement = new CheckBox("Address Brackets Enforcement");
		
		this.saveSmtpConfigurationButton = new Button("Save");
		this.saveSmtpConfigurationButton.addClickListener(new SaveConfigurationButtonListener());				
			
		loadCurrentSmtpConfig();
		
		startStopServerLayout.addComponent(serverStatusLabel);
		startStopServerLayout.addComponent(startServerButton);
		startStopServerLayout.addComponent(stopServerButton);
		
		panelLayout.addComponent(smtpTitleLabel);	
		panelLayout.newLine();
		
		panelLayout.addComponent(startStopServerLayout);	
		
		//formLayout0.addComponent(smtpTitleLabel);		
		formLayout.addComponent(smtpServerEnabledCheckBox);
		
		formLayout.addComponent(smtpBindAddress);
		formLayout.addComponent(smtpConnectionBacklog);
		formLayout.addComponent(smtpConnectiontimeout);
		formLayout.addComponent(smtpConnectionLimit);
		formLayout.addComponent(smtpConnectionLimitPerIP);
		
		//column2
		formLayout.addComponent(new Label("TLS Configuration"));
		formLayout.addComponent(smtpsocketTls);		
		formLayout.addComponent(smtpStartTls);		
		formLayout.addComponent(smtpKeyStore);
		formLayout.addComponent(smtpSecret);
		formLayout.addComponent(smtpProvider);		
		formLayout.addComponent(smtpAlgorithm);
		panelLayout.newLine();
		
		//column3
		formLayout3.addComponent(new Label("Hello Name"));
		formLayout3.addComponent(smtpHelloNameAutoDetect);
		formLayout3.addComponent(smtpHelloName);
		
		formLayout3.addComponent(smtpAuthorizedAddresses);
		formLayout3.addComponent(smtpMaxMessagesize);
		
		formLayout3.addComponent(smtpAddressBracketsEnforcement);
		formLayout3.addComponent(smtpGreeting);
		
		formLayout5.addComponent(saveSmtpConfigurationButton);

		panelLayout.addComponent(formLayout0);
		panelLayout.newLine();
		
		panelLayout.addComponent(formLayout);
		panelLayout.addComponent(formLayout3);
		panelLayout.addComponent(formLayout5);

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
        	
			SmtpServer smtpServer = new SmtpServer();
			
			smtpServer.setSmtpServerEnabled(smtpServerEnabledCheckBox.getValue());
			smtpServer.setBindAddress(smtpBindAddress.getValue());
			smtpServer.setConnectionBacklog(smtpConnectionBacklog.getValue());
			smtpServer.setAuthorizedAddresses(smtpAuthorizedAddresses.getValue());
			smtpServer.setMaxmessagesize(smtpMaxMessagesize.getValue());
			smtpServer.setConnectiontimeout(smtpConnectiontimeout.getValue());
			smtpServer.setKeystore(smtpKeyStore.getValue());
			smtpServer.setSecret(smtpSecret.getValue());
			smtpServer.setAlgorithm(smtpAlgorithm.getValue());
			smtpServer.setProvider(smtpProvider.getValue());		
			smtpServer.setHelloNameAutoDetect(smtpHelloNameAutoDetect.getValue());
			smtpServer.setHelloName(smtpHelloName.getValue());
			smtpServer.setConnectionLimit(smtpConnectionLimit.getValue());			
			smtpServer.setConnectionLimitPerIP(smtpConnectionLimitPerIP.getValue());		
			smtpServer.setAddressBracketsEnforcement(smtpAddressBracketsEnforcement.getValue());				
			smtpServer.setSocketTls(smtpsocketTls.getValue()); 
			smtpServer.setStartTls(smtpStartTls.getValue());  
			smtpServer.setGreeting(smtpGreeting.getValue()); 
    		
    		LOG.debug("SMTP Server configuration to Update: "+smtpServer.toString());
    		
    		String JAMES_CONF_FOLDER = jamesuiConfiguration.getJamesBaseFolder()+File.separator+"conf";
    		LOG.debug("JAMES_CONF_FOLDER: "+JAMES_CONF_FOLDER);

    		try {
    			smtpServerConfigManager.updateConfiguration(smtpServer);
    			Notification.show("Operation Executed Successfully !", Type.HUMANIZED_MESSAGE);
    			
			} catch (ConfigurationException e) {
				LOG.error("Error saving SMTP configuration, cause: ",e);
				Notification.show("Error saving POP3 configuration:"+e.getMessage(),  Type.ERROR_MESSAGE);					
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
        	
			LOG.debug("Starting SMTP Server...");				
			
    		try {
    			boolean result = jamesClient.startSmtpServer();
    			if(result)
    			{
    			   serverStatusLabel.setValue("Server status: Running");
    			   startServerButton.setEnabled(false);
	    		   stopServerButton.setEnabled(true);
    			   Notification.show("Operation Executed Successfully !", Type.HUMANIZED_MESSAGE);
    		    }	
			}catch (Exception e) {
			   LOG.error("Error starting SMTP server, cause: ",e);
			   Notification.show("Error starting SMTP server:"+e.getMessage(), Type.ERROR_MESSAGE);
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
        	
			LOG.debug("Stopping SMTP Server...");				
			
    		try {
    			boolean result = jamesClient.stopSmtpServer();
    			if(result)
    			{
    			   serverStatusLabel.setValue("Server status: Stopped");	    				
    			   Notification.show("Operation Executed Successfully !", Type.HUMANIZED_MESSAGE);
    			   startServerButton.setEnabled(true);
    			   stopServerButton.setEnabled(false);
    			}	   
			}catch (Exception e) {
				LOG.error("Error stopping SMTP server, cause: ",e);
				Notification.show("Error stopping SMTP server:"+e.getMessage(), Type.ERROR_MESSAGE);
			}
        }		
	}
	
	/**
	 *  Load the currently stored IMAP configuration in the dedicated file and fill the form to display it 
	 */
	private void loadCurrentSmtpConfig() {
		
		SmtpServer smtpServer;
		
		try {
			smtpServer = smtpServerConfigManager.readConfiguration();
		
			smtpServerEnabledCheckBox.setValue(smtpServer.isSmtpServerEnabled());
			smtpBindAddress.setValue(smtpServer.getBindAddress());
			smtpConnectionBacklog.setValue(smtpServer.getConnectionBacklog());
			smtpConnectionLimit.setValue(smtpServer.getConnectionLimit());
			smtpConnectionLimitPerIP.setValue(smtpServer.getConnectionLimitPerIP());
			smtpConnectiontimeout.setValue(smtpServer.getConnectiontimeout());
			smtpHelloNameAutoDetect.setValue(smtpServer.isHelloNameAutoDetect());
			smtpHelloName.setValue(smtpServer.getHelloName());
			
			smtpsocketTls.setValue(smtpServer.isSocketTls());
			smtpStartTls.setValue(smtpServer.isStartTls());
			smtpKeyStore.setValue(smtpServer.getKeystore());
			smtpSecret.setValue(smtpServer.getSecret());
			smtpProvider.setValue(smtpServer.getProvider());	
			smtpAlgorithm.setValue(smtpServer.getAlgorithm());
		
		
		} catch (ConfigurationException e) {
			LOG.error("Error loading currently saved IMAP configuration, cause: ",e);			
		}
	}	

}
