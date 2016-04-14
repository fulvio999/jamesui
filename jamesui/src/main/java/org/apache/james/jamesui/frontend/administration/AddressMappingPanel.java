
package org.apache.james.jamesui.frontend.administration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.james.jamesui.backend.client.jmx.JamesClient;
import org.apache.james.jamesui.backend.configuration.bean.RecipientRewriteMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.dialogs.ConfirmDialog;

import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.Reindeer;

/**
 * Create the panel where the user can manage the Address mapping
 * See: {@link http://james.apache.org/server/3/manage-recipientrewrite.html}
 * 
 * @author fulvio
 *
 */
public class AddressMappingPanel extends VerticalLayout {
	
	private static final long serialVersionUID = 1L;

	private final static Logger LOG = LoggerFactory.getLogger(AddressMappingPanel.class);
	
	private HorizontalSplitPanel horizontalSplitPanel;
	private GridLayout rightPanelLayout;
	
	//--- Left Panel
	private Tree addressMappingTree;	
	private Button removeMappingButton;
	private Button compactMappingButton;
	private Button expandMappingButton;
	
	
	//--- Right Panel	
	//Address Mapping
	private ComboBox addressMappingUserCombo;
	private ComboBox addressMappingDomainCombo;
	private TextField addressMappingAddressText;
	private Label addressMappingTitle;
	private FormLayout addressMappingLayout;
	private Button addAddressMappingButton;
	
	//regex Mapping
	private ComboBox regexMappingUserCombo;
	private ComboBox regexMappingDomainCombo;
	private TextField regexMappingRegex;
	private Label regexMappingTitle;
	private FormLayout regexMappingLayout;
	private Button addRegexpMappingButton;
	
	//error Mapping
	private ComboBox errorMappingUserCombo;
	private ComboBox errorMappingDomainCombo;
	private TextField errorMappingError;
	private Label errorMappingTitle;
	private FormLayout errorMappingLayout;
	private Button addErrorMappingButton;
	
	//domain mapping	
	private ComboBox domainMappingDomainCombo;
	private ComboBox domainMappingTargetDomainCombo;
	private Label domainMappingTitle;
	private FormLayout domainMappingLayout;
	private Button addDomainMappingButton;
	
	// A list containing the mapping mapped to be removed
	private HashSet<String> mappingToRemoveSet = new HashSet<String>();
	
	private JamesClient jamesClient; 

	/**
	 * Constructor
	 */	
	public AddressMappingPanel(final JamesClient jamesClient, int heigth) {		

		setHeight(String.valueOf(heigth)); 

		this.setMargin(true); 
		this.setSpacing(true); 
		
		this.jamesClient = jamesClient;
		
	    //--------- Address Mapping ---------
	    this.addressMappingUserCombo = new ComboBox("User:"); 
	    this.addressMappingDomainCombo = new ComboBox("Domain:");	   
	    this.addressMappingUserCombo.addFocusListener(new ComboRefreshListener());	   
	    this.addressMappingDomainCombo.addFocusListener(new ComboRefreshListener());
	    
	    this.addressMappingAddressText = new TextField("Address:");	    
	 	    
	    this.addressMappingLayout = new FormLayout();	    
	    this.addressMappingLayout.setMargin(new MarginInfo(false, false, false, true));
	    this.addressMappingTitle = new Label("<b>Address Mapping</b>",ContentMode.HTML);
	    this.addAddressMappingButton = new Button("Add");
	    this.addAddressMappingButton.addClickListener(new Button.ClickListener() {
		     
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
	        	
				LOG.debug("Adding Address Mapping between user: "+addressMappingUserCombo.getValue()+ ", Domain: "+addressMappingDomainCombo.getValue()+", Address: "+addressMappingAddressText.getValue());				
				
	    		try {
	    			jamesClient.addAddressMapping((String)addressMappingUserCombo.getValue(), (String)addressMappingDomainCombo.getValue(), (String)addressMappingAddressText.getValue());
	    		    updateMappingTreeData(jamesClient.listMappings());        		
	    			Notification.show("Operation Executed Successfully !", Type.HUMANIZED_MESSAGE);
	    			
				} catch (Exception e) {
					LOG.error("Error Adding Address Mapping, cause: ",e);
					Notification.show("Error Adding Address Mapping: "+e.getMessage(), Type.ERROR_MESSAGE);
				}
	        }
		});	 	    
	   
	    HorizontalLayout addressButtonLayout = new HorizontalLayout();
	    addressButtonLayout.setSpacing(true);
	    addressButtonLayout.addComponent(addAddressMappingButton);
	    
	    this.addressMappingLayout.addComponent(addressMappingTitle);
	    this.addressMappingLayout.addComponent(addressMappingUserCombo);
	    this.addressMappingLayout.addComponent(addressMappingDomainCombo);
	    this.addressMappingLayout.addComponent(addressMappingAddressText);
	    this.addressMappingLayout.addComponent(addressButtonLayout);		
	    
	    
		//------------ Regex Mapping -------------
	    this.regexMappingUserCombo = new ComboBox("User:");
	    this.regexMappingDomainCombo = new ComboBox("Domain:");
	    this.regexMappingUserCombo.addFocusListener(new ComboRefreshListener());	   
	    this.regexMappingDomainCombo.addFocusListener(new ComboRefreshListener());
	    
	    this.regexMappingRegex = new TextField("Regex:");
	    this.regexMappingRegex.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));	
	   	    
	    this.regexMappingLayout = new FormLayout();
	    this.regexMappingLayout.setMargin(new MarginInfo(false, false, false, true)); 
	    this.regexMappingTitle = new Label("<b>Regex Mapping</b>",ContentMode.HTML);
	    this.addRegexpMappingButton = new Button("Add");
	    this.addRegexpMappingButton.addClickListener(new Button.ClickListener() {
		     
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
	        	
				LOG.debug("Adding Regex Mapping between user: "+regexMappingUserCombo.getValue()+ ", Domain: "+regexMappingDomainCombo.getValue()+", Regex: "+regexMappingRegex.getValue());				
				
	    		try {
	    			regexMappingRegex.validate();
	    			jamesClient.addRegexMapping((String)regexMappingUserCombo.getValue(), (String)regexMappingDomainCombo.getValue(), (String)regexMappingRegex.getValue());
	    			updateMappingTreeData(jamesClient.listMappings()); 
	    			Notification.show("Operation Executed Successfully !", Type.HUMANIZED_MESSAGE);
	    			
				} catch (Exception e) {
					LOG.error("Error starting IMAP server, cause: ",e);
					Notification.show("Error Adding Regex Mapping: "+e.getMessage(), Type.ERROR_MESSAGE);
				}
	        }
		});	
	  
	    HorizontalLayout regexButtonLayout = new HorizontalLayout();
	    regexButtonLayout.setSpacing(true);
	    regexButtonLayout.addComponent(addRegexpMappingButton);
	    
	    this.regexMappingLayout.addComponent(regexMappingTitle);
	    this.regexMappingLayout.addComponent(regexMappingUserCombo);
	    this.regexMappingLayout.addComponent(regexMappingDomainCombo);
	    this.regexMappingLayout.addComponent(regexMappingRegex);	    
	    this.regexMappingLayout.addComponent(regexButtonLayout);	   	    
	    
	    
		//-------------- Error Mapping ---------------
	    this.errorMappingUserCombo = new ComboBox("User:"); 	    
	    this.errorMappingDomainCombo = new ComboBox("Domain:");
	    this.errorMappingUserCombo.addFocusListener(new ComboRefreshListener());	   
	    this.errorMappingDomainCombo.addFocusListener(new ComboRefreshListener());
	    
	    this.errorMappingError = new TextField("Error:");	
	    this.errorMappingError.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));	
	    
	    this.errorMappingLayout = new FormLayout();
	    this.errorMappingLayout.setMargin(new MarginInfo(true, false, false, true));
	    this.errorMappingTitle = new Label("<b>Error Mapping</b>",ContentMode.HTML);
	    this.addErrorMappingButton = new Button("Add");
	    this.addErrorMappingButton.setStyleName("checkboxed"); 	   
	    this.addErrorMappingButton.addClickListener(new Button.ClickListener() {
		     
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
	        	
				LOG.debug("Adding Error Mapping between user: "+errorMappingUserCombo.getValue()+ ", Domain: "+errorMappingDomainCombo.getValue()+", Error: "+errorMappingError.getValue());				
				
	    		try {
	    			errorMappingError.validate();
	    			jamesClient.addErrorMapping((String)errorMappingUserCombo.getValue(), (String)errorMappingDomainCombo.getValue(), (String)errorMappingError.getValue());
	    			updateMappingTreeData(jamesClient.listMappings()); 
	    			Notification.show("Operation Executed Successfully !", Type.HUMANIZED_MESSAGE);
	    			
				} catch (Exception e) {
					LOG.error("Error starting IMAP server, cause: ",e);
					Notification.show("Error Adding Error mapping: "+e.getMessage(), Type.ERROR_MESSAGE);
				}
	        }
		});		    
	    
	    HorizontalLayout errorButtonLayout = new HorizontalLayout();
	    errorButtonLayout.setSpacing(true);
	    errorButtonLayout.addComponent(addErrorMappingButton);
	    
	    this.errorMappingLayout.addComponent(errorMappingTitle);
	    this.errorMappingLayout.addComponent(errorMappingUserCombo);
	    this.errorMappingLayout.addComponent(errorMappingDomainCombo);
	    this.errorMappingLayout.addComponent(errorMappingError);
	    this.errorMappingLayout.addComponent(errorButtonLayout);	  	    
	    
	    
		//---------------- Domain mapping -----------------------	   
	    this.domainMappingDomainCombo = new ComboBox("Domain:");
	    this.domainMappingTargetDomainCombo = new ComboBox("Target Domain:");
	    this.domainMappingDomainCombo.addFocusListener(new ComboRefreshListener());	   
	    this.domainMappingTargetDomainCombo.addFocusListener(new ComboRefreshListener());
	    
	    this.domainMappingLayout = new FormLayout();
	    this.domainMappingLayout.setMargin(new MarginInfo(true, false, false, true));
	    this.domainMappingTitle = new Label("<b>Domain Mapping</b>",ContentMode.HTML);
	    this.addDomainMappingButton = new Button("Add");
	    this.addDomainMappingButton.addClickListener(new Button.ClickListener() {
		     
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				
				LOG.debug("Adding Domain Mapping between Domain: "+domainMappingDomainCombo.getValue()+", Target Domain: "+domainMappingTargetDomainCombo.getValue());				
				
	    		try {
	    			jamesClient.addDomainMapping((String)domainMappingDomainCombo.getValue(), (String)domainMappingTargetDomainCombo.getValue());
	    			updateMappingTreeData(jamesClient.listMappings()); 
	    			Notification.show("Operation Executed Successfully !", Type.HUMANIZED_MESSAGE);
	    			
				} catch (Exception e) {
					LOG.error("Error starting IMAP server, cause: ",e);
					Notification.show("Error Adding Domain Mapping: "+e.getMessage(), Type.ERROR_MESSAGE);
				}
	        }
		});
	  
	    HorizontalLayout domainButtonLayout = new HorizontalLayout();
	    domainButtonLayout.setSpacing(true);
	    domainButtonLayout.addComponent(addDomainMappingButton);
	    
	    this.domainMappingLayout.addComponent(domainMappingTitle);	   
	    this.domainMappingLayout.addComponent(domainMappingDomainCombo);
	    this.domainMappingLayout.addComponent(domainMappingTargetDomainCombo);
	    this.domainMappingLayout.addComponent(domainButtonLayout);
	    
	    insertComboData(jamesClient);
	    
	    //for development/debug
	    //insertComboFakeData(); 
	  	    
		//--- Left panel
		VerticalSplitPanel leftPanelLayout = new VerticalSplitPanel();
		leftPanelLayout.setStyleName(Reindeer.SPLITPANEL_SMALL);	
		leftPanelLayout.setSplitPosition(80, Sizeable.Unit.PERCENTAGE);
		this.removeMappingButton = new Button("Remove Mapping(s)");		
		this.removeMappingButton.addClickListener(new Button.ClickListener() {
		     
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {	
				if(mappingToRemoveSet.isEmpty())
				   Notification.show("Please, select an Item !", Type.HUMANIZED_MESSAGE);
				else
				   showConfirmAndDelete(jamesClient);
			}
		});	
		
		this.compactMappingButton = new Button("Compact Tree");
		this.compactMappingButton.addClickListener(new Button.ClickListener() {
		     
			private static final long serialVersionUID = 1L;
			public void buttonClick(ClickEvent event) {	
				for(Object itemId: addressMappingTree.getItemIds())
				   addressMappingTree.collapseItem(itemId);
			}
		});	
		
		this.expandMappingButton = new Button("Expand Tree");
		this.expandMappingButton.addClickListener(new Button.ClickListener() {
		     
			private static final long serialVersionUID = 1L;
			public void buttonClick(ClickEvent event) {	
				for(Object itemId: addressMappingTree.getItemIds())
				   addressMappingTree.expandItem(itemId);
			}
		});	
		
		addressMappingTree = new Tree();
		addressMappingTree.setHeight(100, Unit.PERCENTAGE);
		addressMappingTree.addStyleName("checkboxed"); //a custom class in the custom theme 			
		addressMappingTree.setSelectable(false);
        		
        try {              	  
           updateMappingTreeData(jamesClient.listMappings());        		
		} catch (Exception e) {
			LOG.error("Error retrieving mapping, cause:",e);
		} 
        
        HorizontalLayout bottomPageLayout = new HorizontalLayout();
        bottomPageLayout.setMargin(true);
        bottomPageLayout.setSpacing(true);
        bottomPageLayout.addComponent(removeMappingButton);
        bottomPageLayout.addComponent(compactMappingButton);
        bottomPageLayout.addComponent(expandMappingButton);        
        
        VerticalLayout vl = new VerticalLayout();
        vl.setSpacing(true);
        vl.addComponent(new Label("<b>Mappings found:<b>",ContentMode.HTML));
        vl.addComponent(addressMappingTree);
        
        leftPanelLayout.setFirstComponent(vl);       
        leftPanelLayout.setSecondComponent(bottomPageLayout);
		   
        
		//--- Right panel
	    rightPanelLayout = new GridLayout(2, 2);
	    rightPanelLayout.addComponent(addressMappingLayout);
	    rightPanelLayout.addComponent(regexMappingLayout);
	    rightPanelLayout.addComponent(errorMappingLayout);
	    rightPanelLayout.addComponent(domainMappingLayout);	     
	     
	    
	    //--- Compose the full page	  
		horizontalSplitPanel = new HorizontalSplitPanel();
		horizontalSplitPanel.setStyleName(Reindeer.SPLITPANEL_SMALL);		 
		horizontalSplitPanel.setFirstComponent(leftPanelLayout);
		horizontalSplitPanel.setSecondComponent(rightPanelLayout);	
		horizontalSplitPanel.setLocked(true);
			
		addComponent(horizontalSplitPanel);		
	}
	
	
	/**
	 * Show a window confirmation using Vaadin "confirm window" component and proceed with Mapping deletion if user confirm	
	 * 
	 */
	private void showConfirmAndDelete(final JamesClient jamesClient) {	
		
		ConfirmDialog.show(this.getUI(), "Please Confirm:", "Remove "+mappingToRemoveSet.size()+ " Mapping(s) ?","Yes","No", new ConfirmDialog.Listener() {
			
				private static final long serialVersionUID = 1L;

				public void onClose(ConfirmDialog dialog) {
						
		              if (dialog.isConfirmed()) 
		              {	
		            	    boolean errorFlag = false;
		            	  
		                	//the tree node that represents the mapping
		                	String mappingParent;
		                	String mappingToRemove;		                	
		                	Iterator<String>  mappingToRemoveIterator = mappingToRemoveSet.iterator();
		                	
			    			while(mappingToRemoveIterator.hasNext())
			    			{
			    				mappingToRemove = mappingToRemoveIterator.next();
			    				
			    				//System.out.println("Item to Remove: "+mappingToRemove);			    				
			    				mappingParent = (String) addressMappingTree.getParent(mappingToRemove);
			    				//System.out.println("Mapping Parent: "+mappingParent);
			    				
			    				//analyze node parent (ie: the address that own the mapping)
			    				String[] token = mappingParent.split("@");
			    				String ownerUser = token[0];
			    				String ownerDomain = token[1];
			    				
			    				try{			    				
				    				//decide the mapping type
				    				if(mappingToRemove.startsWith("regex")){
				    					//regex mapping eg:  regex:theregex
				    					LOG.debug("Calling removeRegexMapping("+ownerUser+","+ ownerDomain+","+ mappingToRemove.split(":")[1]+")");
				    					jamesClient.removeRegexMapping(ownerUser, ownerDomain, mappingToRemove.split(":")[1]);
				    									    					
				    				}else if(mappingToRemove.startsWith("error")){
				    					//error mapping eg:  error:errmap
				    					LOG.debug("Calling removeErrorMapping("+ownerUser+","+ ownerDomain+","+ mappingToRemove.split(":")[1]+")");
				    					jamesClient.removeErrorMapping(ownerUser, ownerDomain, mappingToRemove.split(":")[1]);
				    					
				    				}else if(mappingToRemove.startsWith("domain")){
				    					//domain mapping eg: domain:thedomain
				    					LOG.debug("Calling removeDomainMapping("+ownerDomain+","+mappingToRemove.split(":")[1]+")");
				    					jamesClient.removeDomainMapping(ownerDomain, mappingToRemove.split(":")[1]);
				    					
				    				}else{
				    					//address mapping eg: theuser@thedomain
				    					LOG.debug("Calling removeAddressMapping("+ownerUser+","+ ownerDomain+","+mappingToRemove+")");
				    					jamesClient.removeAddressMapping(ownerUser, ownerDomain, mappingToRemove);
				    			    }	
			    				
			    				}catch(Exception e){
			    					LOG.error("Error removing Address Mapping, cause: ",e);			    					
			    					errorFlag = true;
			    					break;
			    				}			    				
			       	       }
			    			
			    		  if(!errorFlag){	
			    			  Notification.show("Mapping(s) Removed Successfully !", Type.HUMANIZED_MESSAGE);
			    			  mappingToRemoveSet.clear();
			    			  updateMappingTreeData(jamesClient.listMappings());
			    			  
			    		  }else{
			    			  Notification.show("Error Removing Mapping(s) See log file !", Type.ERROR_MESSAGE);
			    			  mappingToRemoveSet.clear();
			    			  updateMappingTreeData(jamesClient.listMappings());
			    		  }
		                   
		             }else{
		                    // User did not confirm		                	
		             }
		        }
		 });
	}
	
	/**
	 * Insert in the Mapping Tree the dataset provided in argument
	 * @param dataSet
	 */
	private void updateMappingTreeData(final List<RecipientRewriteMapping> dataSet){
		
		addressMappingTree.removeAllItems(); 

		// The tree root nodes (ie: user@doamin)
     	final HashSet<String> usersAndDomainSet = new HashSet<String>();
     	
	    for (int i=0; i<dataSet.size(); i++)
  	    {       
	    	// The value show in the "user" column
	    	String userAndDomain = dataSet.get(i).getUserAndDomain();
     	    if(!userAndDomain.equalsIgnoreCase("*@*")) //don't show the "universal" mapping
    	    { 
	     	   	LOG.debug("Mapping panel, UserAndDomain: "+userAndDomain);	     	     
	     	    addressMappingTree.addItem(userAndDomain); 
	     	   
	     	    usersAndDomainSet.add(userAndDomain);
	     	    
	     	    Object[] it = dataSet.get(i).getMappings().toArray();
	     	    // mappingTable.setPageLength(it.length);
	     	    
	         	for (int j=0; j<it.length; j++)
	         	{	         		        			
		         	LOG.debug("Found Address Mapping: "+it[j]);       		
		         	addressMappingTree.addItem(it[j]);
		         	addressMappingTree.setParent(it[j], userAndDomain);
	         	}
	         	
	       	    // Remember which nodes are checked (use a Set implementation to prevent duplicates)
	         	final HashSet<String> checked = new HashSet<String>();
	         	
	         	// Decide which css style apply returning a css suffix (se jamesuitheme.css) 
	    		Tree.ItemStyleGenerator itemStyleGenerator = new Tree.ItemStyleGenerator() {	    		  
	    		  
					private static final long serialVersionUID = 1L;
	
					/*
	    		     * @param itemId Is the name shows in the Tree node (eg auser@adomanin.com )
	    		     */
	    			@Override	
	    		    public String getStyle(Tree source, Object itemId) {
	    				
	    				if(!usersAndDomainSet.contains(itemId)){
		    		       if (checked.contains(itemId))	    		        
		    		          return "checked";
		    		       else
		    		          return "unchecked";
		    		    }
	    				return "normal";  // Use default style for root node (ie: user@domain)
	    			}
	    		}; 
	    		
	    		// Allow the user to "check" and "uncheck" tree nodes  by clicking them
	    		addressMappingTree.addItemClickListener(new ItemClickListener() {
				
					private static final long serialVersionUID = 1L;

					@Override
					public void itemClick(ItemClickEvent event) {
						
						if (checked.contains(event.getItemId())){
					        checked.remove(event.getItemId());
					        mappingToRemoveSet.remove(event.getItemId());
						}else{
					        checked.add((String) event.getItemId());
					        mappingToRemoveSet.add((String) event.getItemId());
						}
					    
					    // Trigger running the item style generator which the return the class name to return
						addressMappingTree.markAsDirty();						
					}
				});	 	    		
	    		addressMappingTree.setItemStyleGenerator(itemStyleGenerator);	         	
	  	    }	
        }
	}
	
	/**
	 * Utility method that fill the Users, addresses, domains Combo boxes
	 */
	private void insertComboData(JamesClient jamesClient){
		
		String[] users = jamesClient.getAllusers(); //without domain part
		String[] domains = jamesClient.getDomains(); //without user part
		
		List<String> usersWithoutDomain = new ArrayList<String>(); 
		for(int i=0; i<users.length;i++)
			usersWithoutDomain.add(users[i].split("@")[0]);
		
		this.addressMappingUserCombo.addItems(usersWithoutDomain);
	    this.addressMappingDomainCombo.addItems(Arrays.asList(domains));	    
	    
	    this.regexMappingUserCombo.addItems(usersWithoutDomain);	
	    this.regexMappingDomainCombo.addItems(Arrays.asList(domains)); 
	    
	    this.errorMappingUserCombo.addItems(usersWithoutDomain);	
	    this.errorMappingDomainCombo.addItems(Arrays.asList(domains));
	    
	    this.domainMappingDomainCombo.addItems(usersWithoutDomain); 
	    this.domainMappingTargetDomainCombo.addItems(Arrays.asList(domains)); 		
	}
	
	/**
	 * Utility that remove alla items in all the combo
	 */
	private void emptyAllCombo(){
		
		addressMappingUserCombo.removeAllItems();
		addressMappingDomainCombo.removeAllItems();
		regexMappingUserCombo.removeAllItems();
		regexMappingDomainCombo.removeAllItems();
		errorMappingUserCombo.removeAllItems();
		errorMappingDomainCombo.removeAllItems();
		domainMappingDomainCombo.removeAllItems();
		domainMappingTargetDomainCombo.removeAllItems();
	}
	
	/**
	 * Utility method that insert fake data in the Combo box 
	 * Methods used used ONLY during the development when real data are not available
	 */
	private void insertComboFakeData(){
		
		//----- Dummy Test Data to insert in combo box -----
	    this.addressMappingUserCombo.addItems("User0", "User1", "User2", "User3");
	    this.addressMappingDomainCombo.addItems("Domain0", "Domain1", "Domain2", "Domain3");
	    this.addressMappingAddressText.setValue("atestName@fakedomain.com");	    
	    
	    this.regexMappingUserCombo.addItems("User0", "User1", "User2", "User3");	
	    this.regexMappingDomainCombo.addItems("Domain0", "Domain1", "Domain2", "Domain3"); 
	    
	    this.errorMappingUserCombo.addItems("User0", "User1", "User2", "User3");	
	    this.errorMappingDomainCombo.addItems("Domain0", "Domain1", "Domain2", "Domain3");
	    
	    this.domainMappingDomainCombo.addItems("Domain0", "Domain1", "Domain2", "Domain3"); 
	    this.domainMappingTargetDomainCombo.addItems("Target-Domain0", "Target-Domain1", "Target-Domain2", "Target-Domain3"); 
	    
		//------------- fake Existing Mapping TEST data
		Object users[] = {"User1", "User2", "User3", "User4", "pluto", "paperino", "paperoga"};
		Object mapping[] = {"Mapping1", "Mapping2", "Mapping3", "Mapping4", "Mapping5", "Mapping6", "Mapping7"};
	    //-----------------------------------------------------	    
		
	}
	
	/*
	 * Listener attached at the combo that reload the combo valueswhen the combo is opened, to have the last values
	 * of users and domains
	 */
	private class ComboRefreshListener implements com.vaadin.event.FieldEvents.FocusListener{

		private static final long serialVersionUID = 1L;
		
		@Override
		public void focus(FocusEvent event) {
				emptyAllCombo();
				insertComboData(jamesClient);
		}
		
	}
}
