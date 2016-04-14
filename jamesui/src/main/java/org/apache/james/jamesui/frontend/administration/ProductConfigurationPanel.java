
package org.apache.james.jamesui.frontend.administration;

import org.apache.james.jamesui.backend.configuration.bean.JamesuiConfiguration;
import org.apache.james.jamesui.backend.configuration.manager.JamesuiConfigurationManager;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Create a panel that display the currently saved configuration (eg: Database store folder...)
 * picked-up from the configuration files with user defined values or with default ones:
 * @author fulvio
 *
 */
public class ProductConfigurationPanel extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	private Label titleLabel;
	
	/* the folder where user want save the the MapDB files */
	private TextField databaseStorageFolder;
	
	/* the path to JAMES/conf folder (depends on where the user has installed James server) */
	private TextField jamesConfigFolder;

	
	/**
	 * Constructor
	 */
	public ProductConfigurationPanel(JamesuiConfiguration jamesuiConfiguration) {
		
		setSizeFull();			
		setCaption("Product Info");
		
		this.setMargin(true); 
	    this.setSpacing(true);		
	    
		FormLayout formLayout = new FormLayout();	   
	    
	    this.databaseStorageFolder = new TextField("DataBase store folder");
	    this.databaseStorageFolder.setSizeFull();
	    this.databaseStorageFolder.setValue(jamesuiConfiguration.getStatisticDbLocation());	   
	    this.databaseStorageFolder.setReadOnly(true);
	    
	    this.jamesConfigFolder = new TextField("James base folder");
	    this.jamesConfigFolder.setSizeFull();
	    this.jamesConfigFolder.setValue(jamesuiConfiguration.getJamesBaseFolder());	  
	    this.jamesConfigFolder.setReadOnly(true);
	    
	    this.titleLabel = new Label("<b>JamesUI User defined configuration</b>", ContentMode.HTML);
	    
	    formLayout.addComponent(titleLabel);
	    formLayout.addComponent(databaseStorageFolder);
	    formLayout.addComponent(jamesConfigFolder);
	    
	    this.addComponent(formLayout);	
	}

}
