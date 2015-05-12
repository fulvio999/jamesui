package org.apache.james.jamesui.frontend;


import org.apache.james.jamesui.backend.client.jmx.ActiveMQclient;
import org.apache.james.jamesui.backend.client.jmx.CamelClient;
import org.apache.james.jamesui.backend.client.jmx.JamesClient;
import org.apache.james.jamesui.frontend.administration.AddressMappingPanel;
import org.apache.james.jamesui.frontend.administration.DomainsPanel;
import org.apache.james.jamesui.frontend.administration.MailStorePanel;
import org.apache.james.jamesui.frontend.administration.ProductConfigurationPanel;
import org.apache.james.jamesui.frontend.administration.ProductInfoPanel;
import org.apache.james.jamesui.frontend.administration.UsersPanel;
import org.apache.james.jamesui.frontend.configuration.ConfigurationPanel;
import org.apache.james.jamesui.frontend.statistic.HistoryStatisticPanel;
import org.apache.james.jamesui.frontend.statistic.MonitorPanel;
import org.apache.james.jamesui.frontend.statistic.SnapshotStatisticsPanel;
import org.quartz.Scheduler;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;


/**
 * Build the main page (ie: the home page) 
 * @author fulvio
 *
 */
@Component
@Scope("prototype")
@Secured("ROLE_USER")
public class MainView extends VerticalLayout implements View {
		
	private static final long serialVersionUID = 1L;
	
	private HeaderPanel headerPanel;		
	private TabSheet bodyTabsheet;		

	/**
	 * Constructor
	 * @throws Exception 
	 */
	public MainView(Scheduler scheduler,JamesClient jamesClient, ActiveMQclient activeMQclient, CamelClient camelClient, int heigth) {		
		
		setSizeFull();
		setSpacing(true);
		setMargin(new MarginInfo(true, true, true, true)); 
				
		this.headerPanel = new HeaderPanel(scheduler);
		
		this.bodyTabsheet = new TabSheet();		
		this.bodyTabsheet.addTab(new DomainsPanel(jamesClient),"Domains");
		this.bodyTabsheet.addTab(new UsersPanel(jamesClient),"Users"); 
		this.bodyTabsheet.addTab(new AddressMappingPanel(jamesClient,heigth),"Mapping");
		this.bodyTabsheet.addTab(new MailStorePanel(),"Mail Store");		
		this.bodyTabsheet.addTab(new SnapshotStatisticsPanel(activeMQclient, camelClient),"Statistics");		
		this.bodyTabsheet.addTab(new HistoryStatisticPanel(),"History Statistic"); 	
		this.bodyTabsheet.addTab(new MonitorPanel(scheduler),"Monitoring");
		this.bodyTabsheet.addTab(new ConfigurationPanel(jamesClient),"Server Configuration");
		this.bodyTabsheet.addTab(new ProductInfoPanel(),"Produtc Info");	
		this.bodyTabsheet.addTab(new ProductConfigurationPanel(), "JamseUI configuration");
				
		addComponent(headerPanel);
		addComponent(bodyTabsheet);
		
		setExpandRatio(headerPanel, 1);
		setExpandRatio(bodyTabsheet, 3);	
	}

	@Override
	public void enter(ViewChangeEvent event) {	

	}

}
