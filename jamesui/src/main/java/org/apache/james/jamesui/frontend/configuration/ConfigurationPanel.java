package org.apache.james.jamesui.frontend.configuration;

import java.io.File;

import org.apache.james.jamesui.backend.client.jmx.JamesClient;

import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;


/**
 * Panel that allow to configure some parameters about James server writing the dedicated xml configuration files
 * (eg dnsservice-template.xml for DNS configuration)
 * 
 * NOTE: the default James3 configurations files are placed in James jar file. To overwrite them can be modified the templates files:
 * copy in the "conf" folder *-template.xml to *.xml
 * 
 * The new provided values are placed in the custom files location under the james_home/conf folder
 * 
 * @author fulvio
 *
 */
public class ConfigurationPanel extends VerticalLayout{
	
    private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor
	 */
	public ConfigurationPanel(JamesClient jamesClient) {
		
		setSizeFull();		
			
		this.setMargin(true); 
	    this.setSpacing(true);
	    
		Accordion accordion = new Accordion(); 
		accordion.setSizeFull();
		
		String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
	    FileResource resource = new FileResource(new File(basepath +"/WEB-INF/images/server-mail.png")); 
	        
	    Image deleteDomainImage = new Image("", resource);
		
	    accordion.addTab(new JmxConfigurationPanel(), "JMX", null);
		accordion.addTab(new DnsConfigurationPanel(), "DNS", null); 
		accordion.addTab(new Pop3ConfigurationPanel(jamesClient), "POP3", null);
		accordion.addTab(new SmtpConfigurationPanel(jamesClient), "SMTP", null);
		accordion.addTab(new LmtpConfigurationPanel(), "LMTP", null);
		accordion.addTab(new ImapConfigurationPanel(jamesClient), "IMAP", null);

		addComponent(new Label("<b> James Server Configuration: to modify the configuration, JamesUI must be installad on the same host of Apache James. After modification a James restart is required </b>",ContentMode.HTML));
		addComponent(deleteDomainImage);
		
		addComponent(accordion);		
	}

}
