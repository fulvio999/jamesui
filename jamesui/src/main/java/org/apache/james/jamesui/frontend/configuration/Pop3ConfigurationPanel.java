package org.apache.james.jamesui.frontend.configuration;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.james.jamesui.backend.client.jmx.JamesClient;
import org.apache.james.jamesui.backend.configuration.bean.JamesuiConfiguration;
import org.apache.james.jamesui.backend.configuration.bean.Pop3server;
import org.apache.james.jamesui.backend.configuration.manager.EnvironmentConfigurationReader;
import org.apache.james.jamesui.backend.configuration.manager.Pop3serverConfigManager;
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
 * Panel that allow to configure some parameters about POP3 server configuration writing the dedicated xml file
 * 
 * @author fulvio
 *
 */
public class Pop3ConfigurationPanel extends Panel {

	private final static Logger LOG = LoggerFactory.getLogger(Pop3ConfigurationPanel.class);

	private static final long serialVersionUID = 1L;
	
	private JamesClient jamesClient;
	private JamesuiConfiguration jamesuiConfiguration;
	
	private Label pop3TitleLabel;
	
	private Label serverStatusLabel; //running/stopped
	private Button startServerButton;
	private Button stopServerButton;
	
	private CheckBox pop3ServerEnabledCheckBox;
	private TextField pop3BindAddress;
	private TextField pop3ConnectionBacklog;
	
	//TLS config
	private CheckBox pop3SocketTls;
	private CheckBox pop3StartTls;
	private TextField pop3KeyStore;
	private TextField pop3Secret;
	private TextField pop3Provider;
	
	//values used by the server to identify itself in the POP3 protocol		
	private CheckBox pop3HelloNameAutoDetect;
	private TextField pop3helloName;	
	
	private TextField pop3ConnectionTimeout;	
	private TextField pop3ConnectionLimit;
	private TextField pop3ConnectionLimitPerIP;
		
	private Button savePop3ConfigurationButton;
	
	private Pop3serverConfigManager pop3serverConfigManager;

	/**
	 * constructor
	 */
	public Pop3ConfigurationPanel(JamesClient client, JamesuiConfiguration jamesuiConfiguration) {
		
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
        
        pop3serverConfigManager = new Pop3serverConfigManager(jamesuiConfiguration);
                   
        FormLayout formLayout1 = new FormLayout();         
        FormLayout formLayout2 = new FormLayout();         
        FormLayout formLayout3 = new FormLayout();         
        FormLayout formLayout4 = new FormLayout();
        
        this.stopServerButton = new Button("Stop Server");
        this.startServerButton = new Button("Start Server");
        
        /* Get current server status */
        this.serverStatusLabel = new Label(); 
        try {
			if(this.jamesClient.isPop3ServerStarted())
			{
			   serverStatusLabel.setValue("Server status: Running");
			   startServerButton.setEnabled(false);
	    	   stopServerButton.setEnabled(true);
			}else{
			   serverStatusLabel.setValue("Server status: Stopped");
			   startServerButton.setEnabled(true);
 			   stopServerButton.setEnabled(false);
			}	
		}catch(Exception e1) {
			serverStatusLabel.setValue("Server status: ??");
		}        
	   
	    this.startServerButton.addClickListener(new StartServerButtonListener());	   
	    this.stopServerButton.addClickListener(new StopServerButtonListener());
				
		this.pop3TitleLabel = new Label("<b>POP3 Configuration</b>",ContentMode.HTML);	
		this.pop3ServerEnabledCheckBox = new CheckBox("Server enabled");
		this.pop3BindAddress = new TextField("Bind Address:");
		this.pop3BindAddress.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
		  
		this.pop3ConnectionBacklog = new TextField("Connection Backlog:");	
		this.pop3ConnectionBacklog.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
		
		//TLS config
		this.pop3SocketTls = new CheckBox("Socket TLS:");
		this.pop3StartTls = new CheckBox("Start TLS:");
		this.pop3KeyStore = new TextField("KeyStore File:");
		this.pop3KeyStore.setWidth("300");
		this.pop3KeyStore.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
		
		this.pop3Secret = new TextField("Secret:");
		this.pop3Secret.setWidth("300");
		this.pop3Secret.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
		
		this.pop3Provider = new TextField("Provider:");	
		this.pop3Provider.setWidth("300");
		this.pop3Provider.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
		
		//values used by the server to identify itself in the POP3 protocol		
		this.pop3HelloNameAutoDetect = new CheckBox("Auto Detect Enabled:");
		this.pop3helloName = new TextField("Hello Name:");	
		this.pop3helloName.setWidth("300");
		this.pop3helloName.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
		
		this.pop3ConnectionTimeout = new TextField("Connection Timeout:");	
		this.pop3ConnectionTimeout.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
		
		this.pop3ConnectionLimit = new TextField("Connection limit:");
		this.pop3ConnectionLimit.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
		
		this.pop3ConnectionLimitPerIP = new TextField("Connection limit per IP:");
		this.pop3ConnectionLimitPerIP.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
				
		this.savePop3ConfigurationButton = new Button("Save");
		this.savePop3ConfigurationButton.addClickListener(new SaveConfigurationButtonListener());
				
		loadCurrentPop3Config();
		
		startStopServerLayout.addComponent(serverStatusLabel);
		startStopServerLayout.addComponent(startServerButton);
		startStopServerLayout.addComponent(stopServerButton);
		
		panelLayout.addComponent(pop3TitleLabel);			
		panelLayout.newLine();		
		panelLayout.addComponent(startStopServerLayout);	
		
		//TLS		
		formLayout2.addComponent(new Label("TLS Configuration"));
		formLayout2.addComponent(pop3SocketTls);
		formLayout2.addComponent(pop3StartTls);
		formLayout2.addComponent(pop3KeyStore);
		formLayout2.addComponent(pop3Secret);
		formLayout2.addComponent(pop3Provider);
		
		formLayout1.addComponent(pop3ServerEnabledCheckBox);
		formLayout1.addComponent(pop3HelloNameAutoDetect);
		formLayout1.addComponent(pop3helloName);
		
		formLayout1.addComponent(pop3BindAddress);		
		formLayout1.addComponent(pop3ConnectionBacklog);		
		formLayout1.addComponent(pop3ConnectionTimeout);
		formLayout1.addComponent(pop3ConnectionLimit);
		formLayout1.addComponent(pop3ConnectionLimitPerIP);
		
		formLayout4.addComponent(savePop3ConfigurationButton);
		
		panelLayout.newLine();
		
		panelLayout.addComponent(formLayout1);		
		panelLayout.addComponent(formLayout2);		
		panelLayout.addComponent(formLayout3);
		
		panelLayout.newLine();
		panelLayout.addComponent(formLayout4);
		
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
        	
        	Pop3server pop3server = new Pop3server();
    		
    		pop3server.setPop3serverEnabled(pop3ServerEnabledCheckBox.getValue());
    		pop3server.setBindAddress(pop3BindAddress.getValue());
    		pop3server.setConnectionBacklog(pop3ConnectionBacklog.getValue());
    		pop3server.setConnectiontimeout(pop3ConnectionTimeout.getValue());
    		pop3server.setKeystore(pop3KeyStore.getValue());
    		pop3server.setSecret(pop3Secret.getValue());
    		pop3server.setProvider(pop3Provider.getValue());		
    		pop3server.setHelloNameAutoDetect(pop3HelloNameAutoDetect.getValue());
    		pop3server.setHelloName(pop3helloName.getValue());
    		pop3server.setConnectionLimit(pop3ConnectionLimit.getValue());
    		pop3server.setConnectionLimitPerIP(pop3ConnectionLimitPerIP.getValue());			
    		pop3server.setSocketTls(pop3SocketTls.getValue()); 
    		pop3server.setStartTls(pop3StartTls.getValue()); 
    		
    		LOG.debug("POP3 Server configuration to Update: "+pop3server.toString());
	    		
    		String JAMES_CONF_FOLDER = jamesuiConfiguration.getJamesBaseFolder()+File.separator+"conf";
    		LOG.debug("JAMES_CONF_FOLDER: "+JAMES_CONF_FOLDER);
    		
    		try {
    			pop3serverConfigManager.updateConfiguration(pop3server);	
    			Notification.show("Operation Executed Successfully !", Type.HUMANIZED_MESSAGE);
    			
			} catch (ConfigurationException e) {
				LOG.error("Error saving POP3 configuration, cause: ",e);
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
        	
			LOG.debug("Starting Pop3 Server...");				
			
    		try {
    			boolean result = jamesClient.startPop3Server();
    			if(result)
    			{
    			  serverStatusLabel.setValue("Server status: Running");
    			  startServerButton.setEnabled(false);
	    		  stopServerButton.setEnabled(true);
	    		  Notification.show("Operation Executed Successfully !", Type.HUMANIZED_MESSAGE);
    			}
			} catch (Exception e) {
				LOG.error("Error starting POP3 server, cause: ",e);
				Notification.show("Error starting POP3 server: "+e.getMessage(), Type.ERROR_MESSAGE);
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
        	
			LOG.debug("Stopping Pop3 Server...");				
			
    		try {
    			boolean result = jamesClient.stopPop3Server();
    			if(result)
    			{
    			   serverStatusLabel.setValue("Server status: Stopped");	    			 
    			   startServerButton.setEnabled(true);
    			   stopServerButton.setEnabled(false);
    			   Notification.show("Operation Executed Successfully !", Type.HUMANIZED_MESSAGE);
    			}  
			} catch (Exception e) {
				LOG.error("Error stopping POP3 server, cause: ",e);
				Notification.show("Error stopping POP3 server:"+e.getMessage(), Type.ERROR_MESSAGE);
			}
        }		
	}
	
	/**
	 *  Load the currently stored IMAP configuration in the dedicated file and fill the form to display it 
	 */
	private void loadCurrentPop3Config() {
		
		Pop3server pop3server;
		
		try {
			pop3server = pop3serverConfigManager.readConfiguration();
		
			pop3ServerEnabledCheckBox.setValue(pop3server.isPop3serverEnabled());
			pop3BindAddress.setValue(pop3server.getBindAddress());
			pop3ConnectionBacklog.setValue(pop3server.getConnectionBacklog());
			pop3ConnectionLimit.setValue(pop3server.getConnectionLimit());
			pop3ConnectionLimitPerIP.setValue(pop3server.getConnectionLimitPerIP());
			pop3ConnectionTimeout.setValue(pop3server.getConnectiontimeout());
			pop3HelloNameAutoDetect.setValue(pop3server.isHelloNameAutoDetect());
			pop3helloName.setValue(pop3server.getHelloName());
			
			pop3SocketTls.setValue(pop3server.isSocketTls());
			pop3StartTls.setValue(pop3server.isStartTls());
			pop3KeyStore.setValue(pop3server.getKeystore());
			pop3Secret.setValue(pop3server.getSecret());
			pop3Provider.setValue(pop3server.getProvider());		
		
		} catch (ConfigurationException e) {
			LOG.error("Error loading currently saved POP3 configuration, cause: ",e);			
		}
	}

}
