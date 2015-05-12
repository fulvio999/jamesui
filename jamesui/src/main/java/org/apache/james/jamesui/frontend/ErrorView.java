
package org.apache.james.jamesui.frontend;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;

/**
 * View shown when the some error happen (eg: can't connect with James server because is down)
 * @author fulvio
 *
 */
public class ErrorView extends GridLayout implements View {
		
	private static final long serialVersionUID = 1L;
	
	private String errorMessage;

	/**
	 * Constructor
	 * @param errorMsg 
	 */
	public ErrorView(String errorMsg) {
		this.errorMessage = errorMsg;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		
		setSizeFull();	
		setMargin(true);
		setSpacing(true);
		
		this.setColumns(3);
		this.setRows(3);
				
		GridLayout gd = new GridLayout(1,3);
		
		Label errorLabel = new Label("<b>Fatal Error: "+errorMessage+"  </b>", ContentMode.HTML);	
		gd.addComponent(new Label("<h2>JamesUI - Error PAGE</h2>", ContentMode.HTML));		
		gd.addComponent(errorLabel);		
		gd.addComponent(new Label("See Tomcat Log file for more Details. (Clear Browser cache before retry)"));
		this.newLine();
		addComponent(new Label(""));
		addComponent(gd);
		this.newLine();
	}

}
