
package org.apache.james.jamesui.frontend.configuration;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.james.jamesui.backend.configuration.bean.LmtpServer;
import org.apache.james.jamesui.backend.configuration.manager.EnvironmentConfigurationReader;
import org.apache.james.jamesui.backend.configuration.manager.LmtpServerConfigManager;
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
 * Panel that allow to configure some parameters about LMTP server configuration writing the dedicated xml file
 *  
 * @author fulvio
 *
 */
public class LmtpConfigurationPanel extends Panel {	
	
	private static final long serialVersionUID = 1L;

	private final static Logger LOG = LoggerFactory.getLogger(LmtpConfigurationPanel.class);
	
	private Label lmtpTitleLabel;
	
	private CheckBox lmtpServerEnabled;
	private TextField lmtpBindAddress;
	private TextField lmtpConnectionBacklog;
	private TextField lmtpConnectionTimeout;
	private TextField lmtpConnectionLimit;
	private TextField lmtpConnectionLimitPerIP;
	private CheckBox lmtpHelloNameAutoDetect; //true/false
	private TextField lmtpHelloName;
	
	private TextField lmtpMaxMessageSize;
	private TextField lmtpSmtpGreeting;
	
	private Button saveLmtpConfigurationButton;
	
	/* the manager used to manage stored LMTP Server configuration */
	private LmtpServerConfigManager lmtpServerConfigManager;


	/**
	 * Constructor
	 */
	public LmtpConfigurationPanel() {
		
		this.setSizeFull();  
		this.setStyleName(Reindeer.PANEL_LIGHT);
		
		GridLayout panelLayout = new GridLayout(2,2);
	    panelLayout.setMargin(true);
	    panelLayout.setSpacing(true); 
	    panelLayout.setSizeFull();
	    
	    HorizontalLayout startStopServerLayout = new HorizontalLayout();
	    startStopServerLayout.setSpacing(true);
	    
	    this.lmtpServerConfigManager = new LmtpServerConfigManager();
	    
	    FormLayout formLayout0 = new FormLayout(); 
	    FormLayout formLayout1 = new FormLayout(); 
	    FormLayout formLayout2 = new FormLayout();	  
	    FormLayout formLayout4 = new FormLayout();
	    
		this.lmtpTitleLabel = new Label("<b>LMTP Configuration</b>",ContentMode.HTML);
		this.lmtpServerEnabled = new CheckBox("Server Enabled");
		this.lmtpBindAddress = new TextField("Address:");
		this.lmtpBindAddress.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
		
		this.lmtpConnectionBacklog = new TextField("Connection Backlog:");
		this.lmtpConnectionBacklog.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
		
		this.lmtpConnectionTimeout = new TextField("Connection Timeout:");
		this.lmtpConnectionTimeout.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
		
		this.lmtpConnectionLimitPerIP = new TextField("Connection limit per IP:");
		this.lmtpConnectionLimitPerIP.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
		
		this.lmtpHelloNameAutoDetect = new CheckBox("Autodetect Enabled"); //true/false
		this.lmtpHelloName = new TextField("Hello Name:");
		this.lmtpHelloName.setWidth("300");
		this.lmtpHelloName.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
		
		this.lmtpConnectionLimit = new TextField("Connection Limit:");
		this.lmtpConnectionLimit.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
	
		this.lmtpMaxMessageSize = new TextField("MaxMessageSize:");
		this.lmtpMaxMessageSize.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
		
		this.lmtpSmtpGreeting = new TextField("Greeting:");
		this.lmtpSmtpGreeting.setWidth("300");
		this.lmtpSmtpGreeting.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
			
		this.saveLmtpConfigurationButton = new Button("Save");
		this.saveLmtpConfigurationButton.addClickListener(new SaveConfigurationButtonListener());
						
		loadCurrentConfig();
		
		panelLayout.addComponent(lmtpTitleLabel);	
		panelLayout.newLine();
		
		panelLayout.addComponent(startStopServerLayout);		
			
		formLayout1.addComponent(lmtpServerEnabled);		
		formLayout1.addComponent(lmtpBindAddress);
		formLayout1.addComponent(lmtpConnectionBacklog);
		formLayout1.addComponent(lmtpConnectionTimeout);
		formLayout1.addComponent(lmtpConnectionLimitPerIP);
		formLayout1.addComponent(lmtpConnectionLimit);
		
		formLayout2.addComponent(new Label("Hello Name"));
		formLayout2.addComponent(lmtpHelloNameAutoDetect);
		formLayout2.addComponent(lmtpHelloName);		
		
		formLayout1.addComponent(lmtpMaxMessageSize);
		formLayout1.addComponent(lmtpSmtpGreeting);
		
		formLayout4.addComponent(saveLmtpConfigurationButton);	
		
		panelLayout.addComponent(formLayout0);
		panelLayout.newLine();
		
		panelLayout.addComponent(formLayout1);
		panelLayout.addComponent(formLayout2);
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
        	
        	LmtpServer lmtpServer = new LmtpServer();

    		lmtpServer.setLmtpserverEnabled(lmtpServerEnabled.getValue());
    		lmtpServer.setBindAddress(lmtpBindAddress.getValue());
    		lmtpServer.setConnectionBacklog(lmtpConnectionBacklog.getValue());
    		lmtpServer.setConnectionTimeout(lmtpConnectionTimeout.getValue());
    		lmtpServer.setHelloNameAutoDetect(lmtpHelloNameAutoDetect.getValue());
    		lmtpServer.setHelloName(lmtpHelloName.getValue());
    		lmtpServer.setConnectionLimit(lmtpConnectionLimit.getValue());
    		lmtpServer.setConnectionLimitPerIP(lmtpConnectionLimitPerIP.getValue());
    		lmtpServer.setMaxmessagesize(lmtpMaxMessageSize.getValue());
    		lmtpServer.setLmtpGreeting(lmtpSmtpGreeting.getValue());	        	
    		
    		String JAMES_CONF_FOLDER = EnvironmentConfigurationReader.getJamesBaseDir()+File.separator+"conf";
    		LOG.debug("JAMES_CONF_FOLDER: "+JAMES_CONF_FOLDER);
    		
    		try {
    			lmtpServerConfigManager.updateConfiguration(lmtpServer,JAMES_CONF_FOLDER);
    			Notification.show("Operation Executed Successfully !", Type.HUMANIZED_MESSAGE);
    			
			} catch (ConfigurationException e) {
				LOG.error("Error saving DNS configuration, cause: ",e);
				Notification.show("Error saving LMTP configuration:"+e.getMessage(),  Type.ERROR_MESSAGE);
			}
        }		
	}

	
	/**
	 *  Load the currently stored IMAP configuration in the dedicated file and fill the form to display it 
	 */
	private void loadCurrentConfig() {
		
		LmtpServer lmtpServer;
		
		try {
			lmtpServer = lmtpServerConfigManager.readConfiguration(EnvironmentConfigurationReader.getJamesBaseDir()+File.separator+"conf");
		
			lmtpServerEnabled.setValue(lmtpServer.isLmtpserverEnabled());
			lmtpBindAddress.setValue(lmtpServer.getBindAddress());
			lmtpConnectionBacklog.setValue(lmtpServer.getConnectionBacklog());
			lmtpConnectionLimit.setValue(lmtpServer.getConnectionLimit());
			lmtpConnectionLimitPerIP.setValue(lmtpServer.getConnectionLimitPerIP());
			lmtpHelloNameAutoDetect.setValue(lmtpServer.isHelloNameAutoDetect());
			lmtpHelloName.setValue(lmtpServer.getHelloName());
			lmtpConnectionTimeout.setValue(lmtpServer.getConnectionTimeout());			
			lmtpMaxMessageSize.setValue(lmtpServer.getMaxmessagesize());
			lmtpSmtpGreeting.setValue(lmtpServer.getLmtpGreeting());
		
		} catch (ConfigurationException e) {
			LOG.error("Error loading currently saved LMTP configuration, cause: ",e);			
		}
	}	

}
