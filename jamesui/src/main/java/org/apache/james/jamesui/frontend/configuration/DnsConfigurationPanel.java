
package org.apache.james.jamesui.frontend.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang.StringUtils;
import org.apache.james.jamesui.backend.configuration.bean.Dns;
import org.apache.james.jamesui.backend.configuration.bean.JamesuiConfiguration;
import org.apache.james.jamesui.backend.configuration.manager.DnsConfigManager;
import org.apache.james.jamesui.backend.configuration.manager.EnvironmentConfigurationReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.data.Property;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.MouseEvents;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Panel that allow to configure some parameters about DNS configuration writing the dedicated xml file
 * 
 * @author fulvio
 *
 */
public class DnsConfigurationPanel extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private final static Logger LOG = LoggerFactory.getLogger(DnsConfigurationPanel.class);
	
	private Label dnsTitleLabel;
	private CheckBox dnsAutodiscoveryEnabledCheckbox;	
	private CheckBox dnsAuthoritativeCheckbox;
	private TextField dnsMaxCacheSizeText; 
	private CheckBox dnsSingleIPperMXCheckbox;	
	private Table dnsIpAddressTable;	// a List
	private TextField addDnsText; 
	private Button addDnsButton;
	
	private  GridLayout panelLayout;
	
	private Button saveDnsConfigurationButton;	
	
	/* the manager used to manage stored DNS configuration */
	private DnsConfigManager dnsConfigManager;
	
	private JamesuiConfiguration jamesuiConfiguration;

	/**
	 * Constructor
	 */
	public DnsConfigurationPanel(JamesuiConfiguration jamesuiConfiguration) {
		
		    this.jamesuiConfiguration = jamesuiConfiguration;
		
		    this.setSizeFull();  
		    this.setStyleName(Reindeer.PANEL_LIGHT);
		    
		    this.panelLayout = new GridLayout(3,4);
	        panelLayout.setMargin(true);
	        panelLayout.setSpacing(true); 
	        panelLayout.setSizeFull();   
	        
	        FormLayout formLayout = new FormLayout();
	        FormLayout formLayout1 = new FormLayout();
	      
	        this.dnsConfigManager = new DnsConfigManager(jamesuiConfiguration);
	        
		    this.dnsTitleLabel = new Label("<b>DNS Configuration</b>",ContentMode.HTML);		   
		    this.dnsAutodiscoveryEnabledCheckbox = new CheckBox("Enable Autodiscovery");
		    
		    this.dnsAuthoritativeCheckbox = new CheckBox("Authoritative");
		    this.dnsMaxCacheSizeText = new TextField("Max Cache Size:");
		    this.dnsMaxCacheSizeText.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
		 
		    formLayout.addComponent(dnsMaxCacheSizeText); 
		    
		    this.dnsSingleIPperMXCheckbox = new CheckBox("Single IP for MX");		   
		    
		    this.dnsIpAddressTable = new Table();		   
		    this.dnsIpAddressTable.setSortEnabled(false);
		    this.dnsIpAddressTable.addContainerProperty("DNS", String.class, null);		    
		    this.dnsIpAddressTable.addGeneratedColumn("Delete",new DeleteDnsColumnGenerator()); 
				    
		    dnsIpAddressTable.setPageLength(7);
		    dnsIpAddressTable.setWidth(90, Unit.PERCENTAGE);
		    dnsIpAddressTable.setEditable(true);
		    
		    //Dummy Data
		    /*
		     dnsIpAddressTable.addItem(new Object[]{"192.168.2.1"}, null);		    
		     dnsIpAddressTable.addItem(new Object[]{"192.168.2.2"}, null);
		     dnsIpAddressTable.addItem(new Object[]{"192.168.2.3"}, null);
		    */
		    
		    this.addDnsButton = new Button("Add DNS");		    
		    this.addDnsText = new TextField("New DNS:");	
		    this.addDnsText.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
		    this.addDnsButton.addClickListener(new AddDnsButtonButtonListener());
		    
			this.saveDnsConfigurationButton = new Button("Save");
			this.saveDnsConfigurationButton.addClickListener(new SaveDnsButtonButtonListener());
				
			loadCurrentDnsConfig(jamesuiConfiguration);
			
			panelLayout.addComponent(dnsTitleLabel); 		
			panelLayout.newLine();
			panelLayout.newLine();
			
			panelLayout.addComponent(dnsAutodiscoveryEnabledCheckbox);
			panelLayout.newLine();
			panelLayout.addComponent(dnsAuthoritativeCheckbox);
			panelLayout.newLine();
			panelLayout.addComponent(dnsSingleIPperMXCheckbox);
			panelLayout.newLine();
			panelLayout.addComponent(formLayout);
			
			panelLayout.newLine();
			
			panelLayout.addComponent(dnsIpAddressTable);			
			
			formLayout1.addComponent(addDnsText); 
			formLayout1.addComponent(addDnsButton);			
			panelLayout.addComponent(formLayout1);	
			panelLayout.newLine();			
			
			panelLayout.addComponent(saveDnsConfigurationButton);
	        
			addComponent(panelLayout);	      		
	}
	
	/**
	 * Listener for the "Save DNS" button
	 * @author fulvio
	 *
	 */
	private class SaveDnsButtonButtonListener implements com.vaadin.ui.Button.ClickListener {
		
		private static final long serialVersionUID = 1L;

		public void buttonClick(ClickEvent event) {
        	
        	List<Object> dnsServer = new ArrayList<Object>();
          	   			          	
	        Collection<?> itemIDS= dnsIpAddressTable.getContainerDataSource().getItemIds();
	        for (Object itemID : itemIDS)
	        {
	            Property property = dnsIpAddressTable.getContainerDataSource().getContainerProperty(itemID, "DNS");				          	     
	            dnsServer.add(property.getValue());
	        }
        	
        	Dns dns = new Dns(); 
    		dns.setDnsServerList(dnsServer);
    		dns.setAuthoritative(dnsAuthoritativeCheckbox.getValue()); 
    		dns.setAutodiscover(dnsAutodiscoveryEnabledCheckbox.getValue());
    					    		
    		if (StringUtils.isNotBlank(dnsMaxCacheSizeText.getValue()))
    			dns.setMaxcachesize(Long.parseLong(dnsMaxCacheSizeText.getValue()));
    		
    		dns.setSingleIPperMX(dnsSingleIPperMXCheckbox.getValue());
    		
    		LOG.debug("DNS Configuration to Update: "+dns.toString());
    		
    		String JAMES_CONF_FOLDER = jamesuiConfiguration.getJamesBaseFolder()+File.separator+"conf";
    		LOG.debug("JAMES_CONF_FOLDER: "+JAMES_CONF_FOLDER); //eg:  C:\myprogram\eclipse-kepler\conf
    		
    		try {
				dnsConfigManager.updateConfiguration(dns);								
				Notification.show("Operation Executed Successfully !", Type.HUMANIZED_MESSAGE);
				
			} catch (ConfigurationException e) {
				LOG.error("Error saving DNS configuration, cause: ",e);
				Notification.show("Error saving DNS configuration: "+e.getMessage(),  Type.ERROR_MESSAGE);						
			}
        }
		
	}
	
	/**
	 * Listener for the "Add DNS" button
	 * @author fulvio
	 *
	 */
	private class AddDnsButtonButtonListener implements com.vaadin.ui.Button.ClickListener {

		private static final long serialVersionUID = 1L;

		public void buttonClick(ClickEvent event) {
			addDnsText.validate();
        	dnsIpAddressTable.addItem(new Object[]{ addDnsText.getValue()}, null);
        }		
	}
	
	/**
	 * Generator for the table column Delete "DNS"
	 * @author fulvio
	 *
	 */
	private class DeleteDnsColumnGenerator implements com.vaadin.ui.Table.ColumnGenerator {

		private static final long serialVersionUID = 1L;

		@Override
	    public Object generateCell(final Table source, final Object itemId, Object columnId) {			 
	      
	        String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
	        FileResource resource = new FileResource(new File(basepath +"/WEB-INF/images/delete.png")); 
	        
	        Image deleteDomainImage = new Image("", resource); 			        
	        deleteDomainImage.setDescription("Delete");
	        deleteDomainImage.setAlternateText("Delete");
	        deleteDomainImage.addClickListener(new MouseEvents.ClickListener() {
			
				private static final long serialVersionUID = 1L;

				@Override
				public void click(com.vaadin.event.MouseEvents.ClickEvent event) {							
					
					ConfirmDialog.show(getUI(), "Please Confirm:", "Delete Item: "+itemId,
					        "Yes","No", new ConfirmDialog.Listener() {
						
								private static final long serialVersionUID = 1L;

								public void onClose(ConfirmDialog dialog) {
									
					                if (dialog.isConfirmed()) {							                	
					                	source.removeItem(itemId);							                	
					                	Notification.show("DNS Removed");
					                } else {
					                    // User did not confirm							                	
					                }
					            }
					        });							
				   }
			});	

	        return deleteDomainImage;
	     }		
	}

	
	/*
	 * Load the currently stored data iDNS configuration file
	 */
	private void loadCurrentDnsConfig(JamesuiConfiguration jamesuiConfiguration){
			
		try {
			Dns dns = dnsConfigManager.readConfiguration();
			
			dnsAutodiscoveryEnabledCheckbox.setValue(dns.isAutodiscover());
			dnsAuthoritativeCheckbox.setValue(dns.isAuthoritative());
			dnsMaxCacheSizeText.setValue(Long.toString(dns.getMaxcachesize()));
			dnsSingleIPperMXCheckbox.setValue(dns.isSingleIPperMX()); 
			dnsIpAddressTable.addItems(dns.getDnsServerList());
			
		} catch (ConfigurationException e) {
			LOG.error("Error loading currently saved DNS configuration, cause: ",e);
		}
	}
	
}
