
package org.apache.james.jamesui.frontend.administration;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.apache.james.jamesui.backend.client.jmx.JamesClient;
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
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * The management panel for James server users
 *
 */
public class UsersPanel extends VerticalLayout {
	
	private JamesClient jamesClient;
	
	private Label totalUsers;
	private PagedTable usersTable;	
	private TextField newUserTextField;
	private PasswordField newUserPasswordTextField;
	private VerticalLayout leftPanelLayout;	
	private HorizontalSplitPanel horizontalSplitPanel;
	private FormLayout addUserFormLayout;
	private FormLayout editUserFormLayout;	
	private	VerticalLayout rightPanelLayout;
	private Button addUserButton;
	
	private TextField selectedUserText;	
	private PasswordField newUserPasswordText;	
	private PasswordField retypeNewUserPasswordText; 		
	private Button changePasswordButton;
	
	private static final long serialVersionUID = 1L;	

	/**
	 * Constructor
	 */
	public UsersPanel(final JamesClient client) {	

	   setMargin(true);
	   setSizeFull();
		 
       this.jamesClient = client;
       String[] dataSet = this.jamesClient.getAllusers();
        
	   this.leftPanelLayout = new VerticalLayout();			    
             
	   this.totalUsers = new Label();
	   
	   this.usersTable = new PagedTable("");	   
	   this.usersTable.setSelectable( true );
	   this.usersTable.addContainerProperty("Users", String.class, null);	   
	   this.usersTable.addStyleName(Reindeer.TABLE_STRONG);		
	   this.usersTable.setWidth(90, Unit.PERCENTAGE);
	   this.usersTable.setPageLength(15);
		
	   /* Delete user column */	
	   this.usersTable.addGeneratedColumn("Delete", new DeleteColoumnGenerator());					 
	   /* Change user password column */	 
       this.usersTable.addGeneratedColumn("Edit", new EditColoumnGenerator());
	   this.usersTable.setColumnWidth("Delete", 40);
	   this.usersTable.setColumnWidth("Edit", 30);
	  	  
	   this.updateUsersTable(dataSet);
		
	   this.leftPanelLayout.addComponent(totalUsers);
	   this.leftPanelLayout.addComponent(usersTable);
	   this.leftPanelLayout.addComponent(usersTable.createControls());
	  
	   this.newUserTextField = new TextField("User:");
	   this.newUserTextField.setWidth("300px");
	   this.newUserTextField.setRequired(true);        
	   this.newUserTextField.setRequiredError("Required field");
	   this.newUserTextField.setImmediate(true);	
	   this.newUserTextField.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));	
				
	   this.newUserPasswordTextField = new PasswordField("Password:");
	   this.newUserPasswordTextField.setWidth("300px");
	   this.newUserPasswordTextField.setRequired(true);        
	   this.newUserPasswordTextField.setRequiredError("Required field");
	   this.newUserPasswordTextField.setImmediate(true);	
	   this.newUserPasswordTextField.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));	
		
	   this.addUserButton = new Button("Add User");		
	   this.addUserButton.addClickListener(new AddUserButtonListener());
		
	   
	   //---- user password edit form
	   this.selectedUserText = new TextField("User:");
	   this.selectedUserText.setWidth("300px");
	   
	   this.newUserPasswordText = new PasswordField("New password:");
	   this.newUserPasswordText.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
	   this.newUserPasswordText.setRequired(true);        
	   this.newUserPasswordText.setRequiredError("Required field");	   
	   this.newUserPasswordText.setWidth("300px");
	   
	   this.retypeNewUserPasswordText = new PasswordField("Confirm password:");
	   this.retypeNewUserPasswordText.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
	   this.retypeNewUserPasswordText.setRequired(true);        
	   this.retypeNewUserPasswordText.setRequiredError("Required field");	 
	   this.retypeNewUserPasswordText.setWidth("300px");
	   
	   this.changePasswordButton = new Button("Change Password");
	   this.changePasswordButton.addClickListener(new ChangeUserPasswordButtonListener());
	   
	   //------- Right panel: the editing form to add a new user and modify password
	   this.rightPanelLayout = new VerticalLayout(); 
		
	   /* add user form */
	   this.addUserFormLayout = new FormLayout();	
	   this.addUserFormLayout.setSpacing(true);
	   this.addUserFormLayout.setMargin(new MarginInfo(false, false, false, true));		
	   this.addUserFormLayout.addComponent(new Label("Note: User must contain an existing @domain part"));
	   this.addUserFormLayout.addComponent(newUserTextField);
	   this.addUserFormLayout.addComponent(newUserPasswordTextField);
	   this.addUserFormLayout.addComponent(addUserButton);	
		
	   /* "edit user password form": enabled and filled when the user select an item */		
	   this.editUserFormLayout = new FormLayout();
	   this.editUserFormLayout.setMargin(new MarginInfo(true,false,false,true)); 
	   this.editUserFormLayout.setSpacing(true);
	   this.editUserFormLayout.setEnabled(false);
	   this.editUserFormLayout.addComponent(new Label("Editing Form:")); //placeholder
	   this.editUserFormLayout.addComponent(selectedUserText);	
	   this.editUserFormLayout.addComponent(newUserPasswordText);	
	   this.editUserFormLayout.addComponent(retypeNewUserPasswordText);		
	   this.editUserFormLayout.addComponent(changePasswordButton);	
		
	   this.rightPanelLayout.addComponent(addUserFormLayout);
	   this.rightPanelLayout.addComponent(editUserFormLayout);		
		
	   this.horizontalSplitPanel = new HorizontalSplitPanel();
	   this.horizontalSplitPanel.setStyleName(Reindeer.SPLITPANEL_SMALL);
	   this.horizontalSplitPanel.setFirstComponent(leftPanelLayout);	  
	   this.horizontalSplitPanel.setSecondComponent(rightPanelLayout);	
	   this.horizontalSplitPanel.setLocked(true);
	   	    
	   this.addComponent(horizontalSplitPanel);			
	}
	
	
	/**
	 * Column generator for Delete column of the user Table
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
	        deleteDomainImage.setDescription("Delete user");
	        deleteDomainImage.setAlternateText("Delete");
	        deleteDomainImage.addClickListener(new MouseEvents.ClickListener() {
			
				private static final long serialVersionUID = 1L;

				@Override
				public void click(com.vaadin.event.MouseEvents.ClickEvent event) 
				{							
					Item selectedItem = source.getContainerDataSource().getItem(itemId);
					showConfirmDeletion("Delete ? "+ selectedItem.getItemProperty("Users").getValue(), selectedItem.getItemProperty("Users").getValue()+"", jamesClient);							 
				}
			});	

	        return deleteDomainImage;
	    }		
	}	
	
	
	/**
	 * Column generator for Edit column of the user Table
	 * @author fulvio
	 *
	 */
	private class EditColoumnGenerator implements ColumnGenerator {

		private static final long serialVersionUID = -9039124416372118990L;

		@Override
		public Object generateCell(final Table source, final Object itemId, Object columnId) 
		{				      
		      String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		      FileResource resource = new FileResource(new File(basepath +"/WEB-INF/images/edit.png")); 
			        
		      Image editUserImage = new Image("", resource);
		      editUserImage.setDescription("Change Password");
		      editUserImage.setAlternateText("Change Password");
		      editUserImage.addClickListener(new MouseEvents.ClickListener() {
					
			      private static final long serialVersionUID = 1L;

				  @Override
				  public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
							
						String userToEdit = (String) source.getContainerDataSource().getItem(itemId).getItemProperty("Users").getValue();								
						editUserFormLayout.setEnabled(true);	
						newUserPasswordText.setValue("");
						retypeNewUserPasswordText.setValue("");
						selectedUserText.setValue(userToEdit);	
						selectedUserText.setEnabled(false);
				  }
			  });	

		      return editUserImage;
		 }		
	}	
	
	
	/**
	 * Button Listener invoked when the user press the "Change password" button
	 * @author fulvio
	 *
	 */
	private class ChangeUserPasswordButtonListener implements com.vaadin.ui.Button.ClickListener
	{
		private static final long serialVersionUID = 1L;
		
		public void buttonClick(ClickEvent event) 
		{			
			if( newUserPasswordText.getValue().equals(retypeNewUserPasswordText.getValue()) && StringUtils.isNotEmpty(newUserPasswordText.getValue()) && StringUtils.isNotEmpty(retypeNewUserPasswordText.getValue()))				
				showConfirmEdit("Confirm new password ?", selectedUserText.getValue(),newUserPasswordText.getValue(), jamesClient);
			else{
				Notification.show("The two passwords must be equals and not empty, retry", Type.ERROR_MESSAGE);
				newUserPasswordText.setValue("");
				retypeNewUserPasswordText.setValue("");
			}	
		}
	}
	
	
	/**
	 * Button Listener invoked when the user press the "Add User" button
	 * @author fulvio
	 *
	 */
	private class AddUserButtonListener implements com.vaadin.ui.Button.ClickListener
	{
		private static final long serialVersionUID = 1L;

		public void buttonClick(ClickEvent event) {
			
			/* call the validators assigned at the input field: if fail, execution is stopped */
			newUserTextField.validate(); 
			newUserPasswordTextField.validate();
			
			if(!newUserTextField.getValue().contains("@"))
				Notification.show("Error, Username must have a @domainpart with existing domain", Type.ERROR_MESSAGE);
			else{					
				String domainToCheck = newUserTextField.getValue().split("@")[1]; 
				
				if(jamesClient.containsDomain(domainToCheck)){
					jamesClient.addUser(newUserTextField.getValue(), newUserPasswordTextField.getValue());
					Notification.show("New User Added successfully", Type.HUMANIZED_MESSAGE);
					updateUsersTable(jamesClient.getAllusers());					
				}else{
					Notification.show("Provided domain: "+domainToCheck+" does not exist in James", Type.ERROR_MESSAGE);
				}
			}
		}		
	}
	
	
	/**
	 * Utility method to Insert/Update the Users table with the dataset provided in argument
	 */
	private void updateUsersTable(String[] dataSet){
		
		//String[] dataSet = this.jamesClient.getAllusers();		
		Object newItemId = null;
        Item row = null;
        
        usersTable.removeAllItems();
        
        for (int i=0; i<dataSet.length; i++)
        {        	
    	   newItemId = usersTable.addItem();
    	   row = usersTable.getItem(newItemId);
           row.getItemProperty("Users").setValue(dataSet[i]);
        }
        usersTable.refreshRowCache();        
        totalUsers.setValue("Total User: "+dataSet.length);
     }
	
	/**
	 * Utility method  that show a window confirmation using Vaadin "confirm window" component
	 * @param message The message to show in the confirm window
	 * @param itemToRemove the User to delete
	 */
	private void showConfirmDeletion(String message, final String itemToRemove, final JamesClient jamesClient) {	
		
		  ConfirmDialog.show(this.getUI(), "Please Confirm:", message, "Yes","No", new ConfirmDialog.Listener() {
		         
				private static final long serialVersionUID = 1L;

				public void onClose(ConfirmDialog dialog) {
						
		            if (dialog.isConfirmed()) 
		            {		                	
		               jamesClient.deleteUser(itemToRemove);
		               Notification.show("Operation Executed Successfully !", Type.HUMANIZED_MESSAGE);
		               updateUsersTable(jamesClient.getAllusers());		                	
		            }else{
		                // User did not confirm		                    
		            }
		        }
		   });
	 }
	
	/**
	 * Show a window confirmation using Vaadin "confirm window" component to ask confirmation for change password
	 * @param message The message to show in the confirm window
	 * @param userToEdit the User to change password
	 * @param newPassword The new user password
	 * @param jamesClient The client to connect with James
	 */
	private void showConfirmEdit(String message, final String userToEdit,final String newPassword, final JamesClient jamesClient) {	
		
		  ConfirmDialog.show(this.getUI(), "Please Confirm:", message, "Yes","No", new ConfirmDialog.Listener() {
		         
				private static final long serialVersionUID = 1L;

				public void onClose(ConfirmDialog dialog) {
						
		            if (dialog.isConfirmed()) 
		            {		                	
		               jamesClient.changeUserPassword(userToEdit, newPassword);
		               Notification.show("Operation Executed Successfully !", Type.HUMANIZED_MESSAGE);    	
		            }else{
		                // User did not confirm		                    
		            }
		        }
		  });
	 }


}
