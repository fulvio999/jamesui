
package org.apache.james.jamesui.frontend.administration;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.james.jamesui.backend.configuration.bean.DataBaseConfiguration;
import org.apache.james.jamesui.backend.configuration.bean.JamesuiConfiguration;
import org.apache.james.jamesui.backend.configuration.manager.DatabaseConfigurationManager;
import org.apache.james.jamesui.backend.configuration.manager.EnvironmentConfigurationReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Panel that allow to configure the Mail store to use (ie: the database type and his connection parameters)
 * It read parameters from james-databases-template.properties and write 2james-databases.properties"
 * NOTE; the JDBC driver must be placed under the conf/lib folder
 * James must be restarted 
 * 
 * @author fulvio
 *
 */
public class MailStorePanel extends VerticalLayout {
	  
    private final static Logger LOG = LoggerFactory.getLogger(MailStorePanel.class);
	
	private static final long serialVersionUID = 1L;
	
	private Label titleLabel;
	
	private ComboBox supportedDatabaseTypeCombo;
	private TextField driverClassNameText;
	private TextField databaseUrl;
	private TextField databaseUsername;
	private TextField databasePassword;
	private ComboBox openjpaStreamingEnabled;
	
	private Button confirmButton;
	
	private DatabaseConfigurationManager databaseConfigurationManager;
	
	private JamesuiConfiguration jamesuiConfiguration;
	
	/**
	 * constructor
	 */
	public MailStorePanel(JamesuiConfiguration jamesuiConfiguration) {
		
		setMargin(true);
		setSpacing(true); 
        setSizeFull();
        
        this.jamesuiConfiguration = jamesuiConfiguration;
        this.databaseConfigurationManager = new DatabaseConfigurationManager(jamesuiConfiguration);
        
        FormLayout formLayout = new FormLayout();
        
        this.titleLabel = new Label("James has the capacity to use a JDBC-compatible database for storage of both message and user data. "
        							+ "<br/> An appropriate JDBC driver must be available for installation in conf/lib folder. <b>A James restart is necessary </b> ", ContentMode.HTML);
        
        this.supportedDatabaseTypeCombo = new ComboBox("Database Type:");      
        this.supportedDatabaseTypeCombo.addItems("DB2", "DERBY", "H2", "HSQL", "INFORMIX", "MYSQL", "ORACLE", "POSTGRESQL", "SQL_SERVER", "SYBASE");
        this.supportedDatabaseTypeCombo.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
        
        this.driverClassNameText = new TextField("Driver ClassName:");
        this.driverClassNameText.setWidth("350");
        this.driverClassNameText.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
        
        this.databaseUrl = new TextField("Database Url:");
        this.databaseUrl.setWidth("350");
        this.databaseUrl.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
        
        this.databaseUsername = new TextField("Username:");
        this.databaseUsername.setWidth("200");
        this.databaseUsername.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
        
        this.databasePassword = new TextField("Password:");
        this.databasePassword.setWidth("200");
        this.databasePassword.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
        
        this.openjpaStreamingEnabled = new ComboBox("** Streaming enabled:");   
        this.openjpaStreamingEnabled.addItems("true", "false");        
        
        this.confirmButton = new Button("Save");
        this.confirmButton.addClickListener(new SaveDatabaseConfigurationListener());
        
        loadCurrentConfig();
        
        formLayout.addComponent(supportedDatabaseTypeCombo);
        formLayout.addComponent(driverClassNameText);
        formLayout.addComponent(databaseUrl);
        formLayout.addComponent(databaseUsername);
        formLayout.addComponent(databasePassword);
        formLayout.addComponent(openjpaStreamingEnabled);
        formLayout.addComponent(new Label("** Supported on a limited set of databases. Check before enable"));
        formLayout.addComponent(confirmButton);
        
        addComponent(titleLabel);
        addComponent(formLayout);
	}
	
	/**
	 * Load the currently stored Database configuration in the dedicated file and fill the form to display it 
	 */
	private void loadCurrentConfig() {
		
		DataBaseConfiguration dataBaseConfiguration;
		
		try {
			dataBaseConfiguration = databaseConfigurationManager.readConfiguration();
			
			supportedDatabaseTypeCombo.setValue(dataBaseConfiguration.getDatabaseType());
			driverClassNameText.setValue(dataBaseConfiguration.getDriverClassName());
			databaseUrl.setValue(dataBaseConfiguration.getDatabaseUrl());
			databaseUsername.setValue(dataBaseConfiguration.getDatabaseUsername());
			databasePassword.setValue(dataBaseConfiguration.getDatabasePassword());
			openjpaStreamingEnabled.setValue(dataBaseConfiguration.getOpenjpaStreaming());
	
		} catch (ConfigurationException e) {
			LOG.error("Error loading currently saved Database mail-store configuration, cause: ",e);			
		}
	}	
	
	
   /**
	 * Button Listener invoked when the user press the "Save" button to update the database configuration
	 *
    */
	private class SaveDatabaseConfigurationListener implements com.vaadin.ui.Button.ClickListener
	{
		private static final long serialVersionUID = 1L;

		public void buttonClick(ClickEvent event)
		{			 		
			DataBaseConfiguration dataBaseConfiguration = new DataBaseConfiguration();
			
			dataBaseConfiguration.setDatabaseType((String) supportedDatabaseTypeCombo.getValue()); //eg: MySQL
			dataBaseConfiguration.setDriverClassName(driverClassNameText.getValue());
			dataBaseConfiguration.setDatabaseUrl(databaseUrl.getValue());
			dataBaseConfiguration.setDatabaseUsername(databaseUsername.getValue());
			dataBaseConfiguration.setDatabasePassword(databasePassword.getValue());
			dataBaseConfiguration.setOpenjpaStreaming((String) openjpaStreamingEnabled.getValue()); 
		    		
    		LOG.debug("Database configuration to Update: "+dataBaseConfiguration.toString());

    		try {
    			databaseConfigurationManager.updateConfiguration(dataBaseConfiguration);
    			Notification.show("Operation Executed Successfully !", Type.HUMANIZED_MESSAGE);
    			
			} catch (ConfigurationException e) {
				LOG.error("Error saving SMTP configuration, cause: ",e);
				Notification.show("Error saving POP3 configuration:"+e.getMessage(),  Type.ERROR_MESSAGE);					
			}			
		}		
	}

}
