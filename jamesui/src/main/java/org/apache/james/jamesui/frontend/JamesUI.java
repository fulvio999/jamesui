package org.apache.james.jamesui.frontend;

import java.net.ConnectException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.james.jamesui.backend.client.jmx.ActiveMQclient;
import org.apache.james.jamesui.backend.client.jmx.CamelClient;
import org.apache.james.jamesui.backend.client.jmx.JamesClient;
import org.apache.james.jamesui.backend.configuration.bean.JamesuiConfiguration;
import org.apache.james.jamesui.backend.configuration.manager.JamesuiConfigurationManager;
import org.quartz.impl.StdScheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Scope;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.ErrorHandler;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;



/**
 * Create the main page of jamesUI front-end (ie the home page)
 * The "prototype" scope of the bean: will create new instance of UI for each request (the opposite of singleton)
 * With "PreserveOnRefresh" the method init() is called only once instead of each refresh
 * 
 * This UI use the Vaadin Push features to update the UI when the Monitoring Job has finished
 * 
 * @author fulvio
 *
 */
@Push
@Component
@Scope("prototype")
@PreserveOnRefresh
@Theme("jamesuitheme")
public class JamesUI extends UI implements ErrorHandler {
	
	private static final long serialVersionUID = 1L;

	private final static Logger LOG = LoggerFactory.getLogger(JamesUI.class);
	
    protected static final String HOME_PAGE = "homePage";	
	protected static final String CONFIGURATION_PAGE = "configurationPage";
	protected static final String CONNECTION_ERROR_PAGE = "connectionErrorPage";
	
	/* injected by Spring */
	private JamesClient jamesClient;
	private ActiveMQclient activeMQclient;
	private CamelClient camelClient;
	private StdScheduler scheduler;
	private JamesuiConfigurationManager jamesuiConfigurationManager;
	
	private VerticalLayout mainLayout;	
	private Navigator navigator;		
	private MainView mainView;
	private ConfigurationView configurationView;
	
	@Override
	protected void init(VaadinRequest request)  { 
		
		getPage().setTitle("JamesUI");
		
		VaadinSession.getCurrent().setErrorHandler(this);
			
		this.mainLayout = new VerticalLayout();
		this.mainLayout.setMargin(true);		
		this.mainLayout.setHeight(String.valueOf(getPage().getBrowserWindowHeight()));	
		
		this.navigator = new Navigator(this, this);	
		
		//To access at QuartzFactroy using web.xml configuration with QuartzListener	
		//jamesUIQuartzSchedulerFactory = (StdSchedulerFactory) VaadinServlet.getCurrent().getServletContext().getAttribute(QuartzInitializerListener.QUARTZ_FACTORY_KEY);
        //jamesUIscheduler = jamesUIQuartzSchedulerFactory.getScheduler();
		
		/* Error msg to display in the error page */
		String errorMessage = null;
		
		try{	
			/* check jmx connection */
			if(!this.jamesClient.isConnectionValid()) 
			{
			   errorMessage = "Error connecting with James, the server in up and running ? and jamesui.config file is available and correct ?";
			   LOG.error("Error connecting with James server, server is up adn running ?");
			   throw new ConnectException();
			}   
			
			/* load and check jamesui.config or jamesui-devel.conf file */
			this.jamesuiConfigurationManager = new JamesuiConfigurationManager();
			JamesuiConfiguration jamesuiConfiguration = jamesuiConfigurationManager.loadConfiguration();	
			
			if(!this.jamesuiConfigurationManager.isConfigurationValid(jamesuiConfiguration)) 
			{ 
			   errorMessage = "Error connecting with James, the server in up and running ? and jamesui.config file is available and correct ?";
			   LOG.error("Error checking jamesui.config file, missing or invalid file");
			   throw new ConfigurationException();
			} 
			
			LOG.info("Using this Jamesui configuration: \n"+jamesuiConfiguration.toString());
			
			this.mainView = new MainView(scheduler, jamesClient, activeMQclient, camelClient, getPage().getBrowserWindowHeight(), jamesuiConfiguration);			
			this.navigator.addView("", mainView);
			this.configurationView = new ConfigurationView();	
			this.navigator.addView(CONFIGURATION_PAGE, configurationView);		
			this.mainLayout.addComponent(mainView);			
			setContent(mainLayout);		
		
		}catch (Exception e) {	
		   this.navigator.addView("", new ErrorView(errorMessage));
		}		
	}
	
	
	/**
	 * Invoked when an error occurs.
	 * Thanks https://github.com/xpoft for the sample code 
	 */
	@Override
	public void error(com.vaadin.server.ErrorEvent event) {	
		
		if (event.getThrowable().getCause() instanceof Exception)
		{
			Exception accessDeniedException = (Exception) event.getThrowable().getCause();			
			Notification.show(accessDeniedException.getMessage(), Notification.Type.ERROR_MESSAGE);
			/* Cleanup view */
			setContent(null);
			return;
		}		
			
		if (event.getThrowable().getCause() instanceof AccessDeniedException)
		{
			AccessDeniedException accessDeniedException = (AccessDeniedException) event.getThrowable().getCause();
			
			Notification.show(accessDeniedException.getMessage(), Notification.Type.ERROR_MESSAGE);
			/* Cleanup view */
			setContent(null);
			return;
		}
		
		/* Error on page load */
		if (event.getThrowable() instanceof AccessDeniedException)
		{
			AccessDeniedException exception = (AccessDeniedException) event.getThrowable();
			Label label = new Label(exception.getMessage());
			label.setWidth(-1, Unit.PERCENTAGE);
			
			Link goToMain = new Link("Go to main", new ExternalResource("/"));
			
			VerticalLayout layout = new VerticalLayout();
			layout.addComponent(label);
			layout.addComponent(goToMain);
			layout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
			layout.setComponentAlignment(goToMain, Alignment.MIDDLE_CENTER);
			
			VerticalLayout mainLayout = new VerticalLayout();
			mainLayout.setSizeFull();
			mainLayout.addComponent(layout);
			mainLayout.setComponentAlignment(layout, Alignment.MIDDLE_CENTER);
			
			setContent(mainLayout);
			
			Notification.show(exception.getMessage(), Notification.Type.ERROR_MESSAGE);
			return;
		}
		
		DefaultErrorHandler.doDefault(event);		
	}

	public void setJamesClient(JamesClient jamesClient) {
		this.jamesClient = jamesClient;
	}

	public void setActiveMQclient(ActiveMQclient activeMQclient) {
		this.activeMQclient = activeMQclient;
	}

	public void setCamelClient(CamelClient camelClient) {
		this.camelClient = camelClient;
	}

	public void setScheduler(StdScheduler scheduler) {
		this.scheduler = scheduler;
	}


	public void setJamesuiConfigurationManager(
			JamesuiConfigurationManager jamesuiConfigurationManager) {
		this.jamesuiConfigurationManager = jamesuiConfigurationManager;
	}



}