
package org.apache.james.jamesui.frontend.administration;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.james.jamesui.backend.configuration.bean.JamesuiConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Create the "About" panel with some information about JamesUI 
 * @author fulvio
 *
 */
public class ProductInfoPanel extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private final static Logger LOG = LoggerFactory.getLogger(ProductInfoPanel.class);
	
	private Label titleLabel;
	private Label aboutLabel;
	
	private HorizontalSplitPanel horizontalSplitPanel;

	/**
	 * Constructor
	 */
	public ProductInfoPanel(JamesuiConfiguration jamesuiConfiguration) {
		
		setSizeFull();	
		
		setCaption("Product Info");
		
		this.setMargin(true); 
	    this.setSpacing(true);
		
	    this.titleLabel = new Label("<b>JamesUI Product Info</b>", ContentMode.HTML);

	    try {	   
	    	
	    	//---- Part 1: load product license and author info
	    	VerticalLayout leftPanelLayout = new VerticalLayout();
	    	
	 	    InputStream is = this.getClass().getResourceAsStream("/product-info.html");
		    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
	    	StringBuffer stringBuffer = new StringBuffer();
		    
			String currentLine;
			
			while ((currentLine = bufferedReader.readLine()) != null) {				
			   stringBuffer.append(currentLine);
			}
			
			aboutLabel = new Label(stringBuffer.toString(), ContentMode.HTML);
	    
			leftPanelLayout.addComponent(titleLabel);
			leftPanelLayout.addComponent(aboutLabel);		
			
			//----- Part 2: load the product details (ie: library version used)	
			Properties productDetailsConfig = new Properties();			
			productDetailsConfig.load(this.getClass().getResourceAsStream("/product-details.properties"));   
			
			FormLayout rightPanelLayout = new FormLayout(); 
			
			rightPanelLayout.addComponent(new Label("<b>Product details</b>",ContentMode.HTML));
			rightPanelLayout.addComponent(new Label("<b>JamesUI version:</b> "+ productDetailsConfig.getProperty("jamesui.version"),ContentMode.HTML));
			rightPanelLayout.addComponent(new Label("<br/>", ContentMode.HTML));
			rightPanelLayout.addComponent(new Label("<b>Library version in use</b>:",ContentMode.HTML));
			rightPanelLayout.addComponent(new Label("<b>Vaadin version:</b> "+ productDetailsConfig.getProperty("vaadin.version"),ContentMode.HTML));
			rightPanelLayout.addComponent(new Label("<b>James lib version:</b> "+ productDetailsConfig.getProperty("james.lib.version"),ContentMode.HTML));
			rightPanelLayout.addComponent(new Label("<b>Camel lib version:</b> "+ productDetailsConfig.getProperty("camel.lib.version"),ContentMode.HTML));
			rightPanelLayout.addComponent(new Label("<b>ActiveMQ lib version:</b> "+ productDetailsConfig.getProperty("activemq.lib.version"),ContentMode.HTML));
			rightPanelLayout.addComponent(new Label("<b>MapDB version:</b> "+ productDetailsConfig.getProperty("mapdb.lib.version"),ContentMode.HTML));
			rightPanelLayout.addComponent(new Label("<b>Spring version:</b> "+ productDetailsConfig.getProperty("spring.version"),ContentMode.HTML));
			rightPanelLayout.addComponent(new Label("<b>Spring security version:</b> "+ productDetailsConfig.getProperty("spring.security.version"),ContentMode.HTML));
			
			horizontalSplitPanel = new HorizontalSplitPanel();
			horizontalSplitPanel.setWidth("100"); 
		    horizontalSplitPanel.setStyleName(Reindeer.SPLITPANEL_SMALL);
		    horizontalSplitPanel.setFirstComponent(leftPanelLayout);
		    horizontalSplitPanel.setSecondComponent(rightPanelLayout);		 	 
		    horizontalSplitPanel.setLocked(true);
		    horizontalSplitPanel.setSizeFull();
			   	    
			addComponent(horizontalSplitPanel);	
			
		    } catch (Exception e) {
				LOG.error("Error reading from file product-info.html or product-details.properties, cause: ",e);
				aboutLabel = new Label("<b>Error loading product-info file ! See log file</b>", ContentMode.HTML);
			}
		
	  }

}
