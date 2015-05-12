
package org.apache.james.jamesui.frontend.configuration;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.james.jamesui.backend.configuration.bean.JmxConfiguration;
import org.apache.james.jamesui.backend.configuration.manager.EnvironmentConfigurationReader;
import org.apache.james.jamesui.backend.configuration.manager.JmxConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.Reindeer;

/**
 * @author fulvio
 *
 */
public class JmxConfigurationPanel extends Panel {
	
	private static final long serialVersionUID = 1L;

	private final static Logger LOG = LoggerFactory.getLogger(JmxConfigurationPanel.class);
	
	private Label jmxTitleLabel;	
	private TextField jmxAddressText; 
	private TextField jmxPortText;	
	
	private GridLayout panelLayout;	
	private Button saveJmxConfigurationButton;	
	
	/* the manager used to manage stored DNS configuration */
	private JmxConfigManager jmxConfigManager;

	/**
	 * Constructor
	 */
	public JmxConfigurationPanel() {
		
		this.setSizeFull();  
		this.setStyleName(Reindeer.PANEL_LIGHT);
		
		this.jmxConfigManager = new JmxConfigManager();
		FormLayout formLayout = new FormLayout();
		
		this.panelLayout = new GridLayout(1,2);
		this.panelLayout.setMargin(true);
		this.panelLayout.setSpacing(true); 
		this.panelLayout.setSizeFull();
		
		this.jmxTitleLabel = new Label("<b>JMX Configuration</b>",ContentMode.HTML);
		this.jmxAddressText = new TextField("JMX Address:");
		this.jmxAddressText.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
		
		this.jmxPortText = new TextField("JMX Port:");
		this.jmxPortText.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
		
		this.saveJmxConfigurationButton = new Button("Save");
		this.saveJmxConfigurationButton.addClickListener(new SaveConfigurationButtonListener());
		
		loadCurrentConfig();
		
		formLayout.addComponent(jmxAddressText);
		formLayout.addComponent(jmxPortText);
		formLayout.addComponent(saveJmxConfigurationButton);
		
		this.panelLayout.addComponent(jmxTitleLabel);	
		this.panelLayout.newLine();		
		this.panelLayout.addComponent(formLayout);	
		
		this.setContent(panelLayout);
	}
	
	/**
	 *  Load the currently stored JMX configuration in the dedicated file and fill the form to display it 
	 */
	private void loadCurrentConfig() {
		
		JmxConfiguration jmxConfiguration;
		
		try {
			jmxConfiguration = jmxConfigManager.readConfiguration(EnvironmentConfigurationReader.getJamesBaseDir()+File.separator+"conf");		
			jmxAddressText.setValue(jmxConfiguration.getJmxHost());
			jmxPortText.setValue(jmxConfiguration.getJmxPort());
		
		} catch (ConfigurationException e) {
			LOG.error("Error loading currently saved JMX configuration, cause: ",e);			
		}
	}
	
	/**
	 * Listener for the "Save" button
	 * @author fulvio
	 *
	 */
	private class SaveConfigurationButtonListener implements com.vaadin.ui.Button.ClickListener {

		private static final long serialVersionUID = 1L;

		public void buttonClick(ClickEvent event) {	        	
        	
			JmxConfiguration jmxConfiguration = new JmxConfiguration();
			jmxConfiguration.setJmxHost(jmxAddressText.getValue());
			jmxConfiguration.setJmxPort(jmxPortText.getValue());	    	       	
    		
    		String JAMES_CONF_FOLDER = EnvironmentConfigurationReader.getJamesBaseDir()+File.separator+"conf";
    		LOG.debug("JAMES_CONF_FOLDER: "+JAMES_CONF_FOLDER);
    		
    		try {
    			jmxConfigManager.updateConfiguration(jmxConfiguration,JAMES_CONF_FOLDER);
    			Notification.show("Operation Executed Successfully !", Type.HUMANIZED_MESSAGE);
    			
			} catch (ConfigurationException e) {
				LOG.error("Error saving JMX configuration, cause: ",e);
				Notification.show("Error saving JMX configuration:"+e.getMessage(),  Type.ERROR_MESSAGE);
			}
        }		
	}

}


