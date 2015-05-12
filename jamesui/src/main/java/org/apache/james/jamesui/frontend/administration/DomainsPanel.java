
package org.apache.james.jamesui.frontend.administration;

import java.io.File;

import org.apache.james.jamesui.backend.client.jmx.JamesClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.dialogs.ConfirmDialog;

import com.jensjansson.pagedtable.PagedTable;
import com.vaadin.data.Item;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.MouseEvents;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Create the panel with the operations to manage a James domains
 * 
 * @author fulvio
 *
 */
public class DomainsPanel extends VerticalLayout {
	
	private static final long serialVersionUID = 1L;	

	private final static Logger LOG = LoggerFactory.getLogger(DomainsPanel.class);
	
	private JamesClient jamesClient;
	
	private Label totalDomain;	
	private PagedTable domainsTable;
	private TextField newDomainTextField;	
	private VerticalLayout leftPanelLayout;
	private HorizontalSplitPanel horizontalSplitPanel;
	private FormLayout addDomainFormLayout;
	private Button addDomainButton;	
	
	/**
	 * Constructor
	 */	
	public DomainsPanel(final JamesClient client) {		
			
		setMargin(true);
		setSizeFull();
		 
		this.jamesClient = client;
		
		String[] domains = jamesClient.getDomains();         
        LOG.debug("Found "+ domains.length +" domains");
        
        this.leftPanelLayout = new VerticalLayout();	
        this.leftPanelLayout.setSizeFull();        
             
        this.totalDomain = new Label();
            
        this.domainsTable = new PagedTable(""); 
        this.domainsTable.setSelectable( true );
        this.domainsTable.addContainerProperty("Domain", String.class, null);
        this.domainsTable.addStyleName(Reindeer.TABLE_STRONG);		
        this.domainsTable.setWidth(90, Unit.PERCENTAGE);
        this.domainsTable.setPageLength(15);
         
	    //TEST DATA
        //Object domains[][] = {{domains[0]}, {"Valels"}, {"Valtaoja"}, {"pippo"}, {"pluto"}, {"paperino"}, {"paperoga"}};
        
        this.updateTableDataSet(domains);		    
		
        this.domainsTable.addGeneratedColumn("Delete", new DeleteColoumnGenerator());
        this.domainsTable.setColumnWidth("Delete", 40);
		
        this.leftPanelLayout.addComponent(totalDomain);	
        this.leftPanelLayout.addComponent(domainsTable);	
        this.leftPanelLayout.addComponent(domainsTable.createControls());
	
        this.newDomainTextField = new TextField("Domain Name");
        this.newDomainTextField.setWidth("400px");
        this.newDomainTextField.setRequired(true);        
        this.newDomainTextField.setRequiredError("Required field");
        this.newDomainTextField.setImmediate(true);	
        this.newDomainTextField.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));		
		
        this.addDomainButton = new Button("Add Domain");		
        this.addDomainButton.addClickListener(new AddDomainButtonListener());
				
		/* right panel: the editing form to add new domains */
        this.addDomainFormLayout = new FormLayout();	
        this.addDomainFormLayout.setSpacing(true);
        this.addDomainFormLayout.setMargin(new MarginInfo(false, false, false, true));
        this.addDomainFormLayout.addComponent(newDomainTextField);
		this.addDomainFormLayout.addComponent(addDomainButton);	 		
		
		this.horizontalSplitPanel = new HorizontalSplitPanel();
		this.horizontalSplitPanel.setStyleName(Reindeer.SPLITPANEL_SMALL);
		this.horizontalSplitPanel.setFirstComponent(leftPanelLayout);
		this.horizontalSplitPanel.setSecondComponent(addDomainFormLayout);	 
		this.horizontalSplitPanel.setLocked(true);
	   	    
		this.addComponent(horizontalSplitPanel);			
	}
	
	/**
	 * Insert in the Domains table the data-set provided in argument
	 * @param data
	 */
	private void updateTableDataSet(String[] dataSet){
		
		Object newItemId = null;
	    Item row = null;
		
	    domainsTable.removeAllItems();
	    
		for (int i=0; i<dataSet.length; i++) {
        	
	    	newItemId = domainsTable.addItem();
	    	row = domainsTable.getItem(newItemId);
	        row.getItemProperty("Domain").setValue(dataSet[i]);
	    }
		
		domainsTable.refreshRowCache();
		totalDomain.setValue("Total Domain: "+dataSet.length);
	}
	
	/**
	 * Show a window confirmation using Vaadin "confirm window" component
	 * @param message The message to show in the confirm window
	 * @param itemToRemove the domain to delete
	 */
	private void showConfirm(String message, final String itemToRemove, final JamesClient jamesClient) {	
	
		ConfirmDialog.show(this.getUI(), "Please Confirm:", message,"Yes","No", new ConfirmDialog.Listener() {
			
			private static final long serialVersionUID = 1L;

			public void onClose(ConfirmDialog dialog) {
						
	           if (dialog.isConfirmed()) 
	           {	                	
	        	   jamesClient.removeDomain(itemToRemove);	           
		           updateTableDataSet(jamesClient.getDomains());
		           Notification.show("Domain Removed Successfully !", Type.HUMANIZED_MESSAGE);
		                	
		        }else{
		            // User did not confirm		                	
		        }
		     }
		 });
	}
	
	/**
	 * Listener for the "Add domain button"
	 * @author fulvio
	 *
	 */
	private class AddDomainButtonListener implements com.vaadin.ui.Button.ClickListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			
			String newDomain = newDomainTextField.getValue();
				
			//call the validator assigned at the input text: if fail, execution is stopped
			newDomainTextField.validate(); 
				
			jamesClient.addDomain(newDomain);				
			Notification.show("New Domain Added successfully !", Type.HUMANIZED_MESSAGE);				
			updateTableDataSet(jamesClient.getDomains());
		}		
	}
	
	
	/**
	 * Column Generator for the "delete domain" table column
	 * @author fulvio
	 *
	 */
	private class DeleteColoumnGenerator implements ColumnGenerator {

		private static final long serialVersionUID = 1L;

		@Override
	    public Object generateCell(final Table source, final Object itemId, Object columnId) {			 
	      
	        String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
	        FileResource resource = new FileResource(new File(basepath +"/WEB-INF/images/delete.png")); 
	        
	        Image deleteDomainImage = new Image("", resource); 			        
	        deleteDomainImage.setDescription("Delete Domain");
	        deleteDomainImage.setAlternateText("Delete");
	        deleteDomainImage.addClickListener(new MouseEvents.ClickListener() {
			
				private static final long serialVersionUID = 1L;

				@Override
				public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
					
					Item selectedItem = source.getContainerDataSource().getItem(itemId);
					showConfirm("Delete ? "+ selectedItem.getItemProperty("Domain").getValue(), selectedItem.getItemProperty("Domain").getValue()+"", jamesClient);							 
				}
			});	

	        return deleteDomainImage;
	    }		
	}

}
