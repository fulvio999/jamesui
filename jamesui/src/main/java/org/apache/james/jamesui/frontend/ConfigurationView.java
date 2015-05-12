package org.apache.james.jamesui.frontend;


import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * View representing the page where the user can configure JamesUI application (eg set the url where is placed James server)
 *
 */
public class ConfigurationView extends VerticalLayout implements View {

	
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public ConfigurationView() {
		
		setSizeFull();	
		setMargin(true);
		
		final TextField jamesServerUrlTextField = new TextField("James Server URL");			
		jamesServerUrlTextField.setRequired(true);        
		jamesServerUrlTextField.setRequiredError("Required field");
		jamesServerUrlTextField.setImmediate(true);		
		jamesServerUrlTextField.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
		
		
		final TextField jamesServerPortTextField = new TextField("James Server Port");
		jamesServerPortTextField.setRequired(true);        
		jamesServerPortTextField.setRequiredError("Required field");
		jamesServerPortTextField.setImmediate(true);		
		
		//the editing form to add/delete domains
		FormLayout formLayout = new FormLayout();
					   
		formLayout.addComponent(jamesServerUrlTextField);
		formLayout.addComponent(jamesServerPortTextField);
		
		addComponent(formLayout);		
	}

	@Override
	public void enter(ViewChangeEvent event) {		
		
	}

}
