package org.apache.james.jamesui.frontend.statistic;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.james.jamesui.backend.configuration.manager.EnvironmentConfigurationReader;
import org.apache.james.jamesui.backend.database.manager.QueuesDatabaseManager;
import org.apache.james.jamesui.backend.database.manager.RoutesDatabaseManager;
import org.mapdb.DB;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.matchers.KeyMatcher;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.icu.util.Calendar;
import com.vaadin.data.util.converter.Converter.ConversionException;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;

/**
 * Create the monitoring panel where the user can start a quartz monitoring job to query James and save them
 * in a MapDB database
 * 
 * @author fulvio
 *
 */
public class MonitorPanel extends HorizontalLayout{	
	
	private static final long serialVersionUID = 1L;

	private final static Logger LOG = LoggerFactory.getLogger(MonitorPanel.class);
	
	/* to manage/restore start/stop button after logout use a tmp file stored in tomcat tmp folder to notify that a monitor job is already running  */
	private static final String JAMESUI_TMP_FILE_NAME = "jamesui.tmp";	
	private static final File JAMESUI_TMP_FILE = new File(EnvironmentConfigurationReader.getOsTmpFolder(),JAMESUI_TMP_FILE_NAME); 
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
	private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
	
	private Button startJobButton;
	private Button stopJobButton;	
	private final TextField pollingFrequencyTextField;	
	private Label operationResultLabel;	
    private DateField startDatePicker;
    private DateField endDatePicker;
    
    private JobKey monitorJobKey;
    
    //private final StdSchedulerFactory jamesUIQuartzSchedulerFactory = (StdSchedulerFactory) VaadinServlet.getCurrent().getServletContext().getAttribute(QuartzInitializerListener.QUARTZ_FACTORY_KEY);
    private Scheduler scheduler;
    
    private DB routeDb;
    private DB queueDb;
    
	/**
	 * Constructor
	 *
	 */
	public MonitorPanel(final Scheduler sch) {
		
		setSizeFull();	
				
		this.setMargin(true); 
	    this.setSpacing(true);
		
	    this.scheduler = sch;	    
		this.monitorJobKey = new JobKey("monitorJob", "monitorJobGroup");
		
		this.pollingFrequencyTextField = new TextField("Polling frequency (sec.):");
		this.pollingFrequencyTextField.setConverter(Integer.class);
		this.pollingFrequencyTextField.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
		this.pollingFrequencyTextField.setValue("20");
		
		this.operationResultLabel = new Label();
              
		this.startDatePicker = new DateField("Start date (yyyy-MM-dd HH:mm:ss):");      	
		this.startDatePicker.setValue(new Date()); 
		this.startDatePicker.setDateFormat("yyyy-MM-dd HH:mm:ss"); 
		this.startDatePicker.setResolution(Resolution.SECOND);
        
		this.endDatePicker = new DateField("End date (yyyy-MM-dd HH:mm:ss):"); 
		this.endDatePicker.setValue(new Date());
		this.endDatePicker.setDateFormat("yyyy-MM-dd HH:mm:ss"); 
		this.endDatePicker.setResolution(Resolution.SECOND);
 		
        this.startJobButton = new Button("Start JOB");	
        this.stopJobButton = new Button("Stop JOB");	
        
        this.chekcJobStatus();
        
        this.startJobButton.addClickListener(new StartJobButtonListener());       	
        this.stopJobButton.addClickListener(new StoptJobButtonListener());
       
        addComponent(stopJobButton);	
        addComponent(startJobButton);	
        addComponent(operationResultLabel);
        addComponent(startDatePicker);
        addComponent(endDatePicker);
        addComponent(pollingFrequencyTextField);      
	}	
	
	/**
	 * Button Listener invoked when the user press the "Stop Job" button
	 * @author fulvio
	 *
	 */
	private class StoptJobButtonListener implements com.vaadin.ui.Button.ClickListener
	{
		private static final long serialVersionUID = 1138238278903887545L;
		
		public void buttonClick(ClickEvent event) {
				
			try {				
				 Notification.show("Stopping job");						
				 //Debug: listAlljobs();				
							
				 scheduler.standby();
				 
				 if(routeDb != null && queueDb != null) {
					routeDb.close();
					queueDb.close();  //queueDb.isClosed();
				 }
				
			    /* communicate at the job to stop (solution used due to the fact the InterruptableJob doesn't work, i don't know why:(( )
				  monitorJobDetail.getJobDataMap().put("stop", true);				
				  scheduler.standby();
				*/				
	
				//delete the tmp file because job is stopped by the user				
				JAMESUI_TMP_FILE.delete();
								
				startJobButton.setEnabled(true);
				stopJobButton.setEnabled(false);					
				
				operationResultLabel.setValue("JOB Stopped");
				 
			} catch (Exception e) {
				LOG.error("Error stopping the job, cause: ",e);
			}		
		 }
	}
	
	
	/**
	 * Button Listener invoked when the user press the "Start Job" button
	 * @author fulvio
	 *
	 */
	private class StartJobButtonListener implements com.vaadin.ui.Button.ClickListener
	{					
			private static final long serialVersionUID = -2309200399776470721L;

        	public void buttonClick(ClickEvent event) {
							
        	   Integer pollingFrequency = null;
        		
		       try {
		    	     pollingFrequency = (Integer) pollingFrequencyTextField.getConvertedValue() * 1000; //convert in ms
		        	 Date startDate = startDatePicker.getValue();
					 Date endDate = endDatePicker.getValue();					 
		        	
		             if(pollingFrequency <=0)
		                throw new ConversionException();	
		             
		             if(getCurrentDate().after(endDate) || endDate.compareTo(startDate) == 0)
		            	throw new Exception("End Date is before of current Time or start/end date are equals");
		            
					 LOG.info("Preparing to schedule a job with startDate: "+startDate+" and endDate: "+endDate+ " and polling frequency: "+pollingFrequency);
				
				     Notification.show("Starting job...");	
				     
				     //prepare DB, operation non necessary if the user has previously stopped a running job
				     if(routeDb == null && queueDb == null) {
				    	 LOG.trace("Creating the two empty databases...");
				    	 routeDb = RoutesDatabaseManager.getRouteDataBase(EnvironmentConfigurationReader.getStatsDatabaseFolder(), "route-db_"+sdf2.format(new Date()));   
				    	 queueDb = QueuesDatabaseManager.getQueueDataBase(EnvironmentConfigurationReader.getStatsDatabaseFolder(), "queue-db_"+sdf2.format(new Date()));
				     }
				     
			    	 RoutesDatabaseManager.createStatsMetaData(routeDb,sdf.format(startDate),sdf.format(endDate),pollingFrequency.toString());
			    	 QueuesDatabaseManager.createStatsMetaData(queueDb,sdf.format(startDate),sdf.format(endDate),pollingFrequency.toString());

				     
				     //get form the Scheduler (created and configured by Spring) a Job also configured with Spring (See: spring-quartz.xml)
					 JobDetail jobDetail = scheduler.getJobDetail(new JobKey("monitorJob", "monitorJobGroup"));
					 SimpleTriggerImpl trigger = (SimpleTriggerImpl) scheduler.getTrigger(new TriggerKey("monitorJobTriggerName","monitorJobTriggerGroup"));
					
					 //customize the loaded Trigger with the values provided by the user
					 trigger.setStartTime(startDate); 
					 trigger.setRepeatInterval(pollingFrequency);
					 trigger.setEndTime(endDate);
				     
			         //In case the Scheduler contains a job with the same name/group delete it			    
					 scheduler.deleteJob(monitorJobKey);
					 
					 //pass at the job the two database where save the picked-up data
					 jobDetail.getJobDataMap().put("routeDb",routeDb);
					 jobDetail.getJobDataMap().put("queueDb", queueDb);					 
						
				     /* To create a custom job without Spring
					     monitorJobDetail = newJob(MonitorJob.class).withIdentity(monitorJobKey).build(); monitorJobDetail.getJobDataMap().put("stop", true);
					     monitorJobDetail.getJobDataMap().put("stop", false);				     
						 trigger = newTrigger().withIdentity("trigger1", "monitorJobGroup").startAt(startDate)
				    	           .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever().withIntervalInSeconds(convertedValue)).endAt(endDate).build();
				     */
					
				     scheduler.scheduleJob(jobDetail,trigger);
				     scheduler.getListenerManager().addJobListener(new JamesMonitorJobListener(endDate,pollingFrequency), KeyMatcher.keyEquals(jobDetail.getKey()));				     
				     scheduler.start(); 
				   
				     //create jamesui.tmp file to restore status of start/stop buttons in case of the user execute a logout/login when the job is running		
					 JAMESUI_TMP_FILE.createNewFile();
					 LOG.debug("Created tmp file: "+JAMESUI_TMP_FILE.getAbsolutePath());
					 
					 PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration(JAMESUI_TMP_FILE);
					 propertiesConfiguration.setProperty("job.start.date",sdf.format(startDatePicker.getValue()));
					 propertiesConfiguration.setProperty("job.stop.date",sdf.format(endDatePicker.getValue()));
					 propertiesConfiguration.save();	
					  
				     startJobButton.setEnabled(false);
					 stopJobButton.setEnabled(true);
							 
				     operationResultLabel.setValue("JOB Running");
				
				} catch (ConversionException e) {					
			        Notification.show("Polling frequency must be a positive integer", Type.ERROR_MESSAGE);			             
				     
				} catch (Exception ex) {
					LOG.error("Error starting/scheduling job, cause: ",ex);
					Notification.show("Error scheduling job. cause:"+ ex.getMessage(), Type.ERROR_MESSAGE);	
				}	
		  }		
	}
	
	/**
	 * Utility method that check if a Monitor job is already running or not, used on page construction.
	 * Necessary to set/restore the right start/stop button status after a login or logout/login
	 */
	private void chekcJobStatus(){	
		
		if(JAMESUI_TMP_FILE.exists())
		{			
			LOG.debug("Monitor job already running, "+JAMESUI_TMP_FILE_NAME+ " found, restoring field values...");
			
			this.startJobButton.setEnabled(false);
		    this.stopJobButton.setEnabled(true);
			this.operationResultLabel.setValue("JOB Running");
			 		    
		    try {
		    	 //restore job date start/stop values
		    	 PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration(JAMESUI_TMP_FILE);
		    	 startDatePicker.setValue( sdf.parse(propertiesConfiguration.getString("job.start.date") ) ); 
		    	 endDatePicker.setValue( sdf.parse(propertiesConfiguration.getString("job.stop.date") ) );			
		    	 
			} catch (Exception e) {
			    Notification.show("Error restoring job start/stop date, cause:"+ e.getMessage(), Type.ERROR_MESSAGE);		
			    LOG.error("Error restoring job start/stop date, cause: ",e);
			}
		    
		} else {
		    LOG.debug("Monitor job NOT running, "+JAMESUI_TMP_FILE_NAME+ " not found");
		    this.startJobButton.setEnabled(true);
		    this.stopJobButton.setEnabled(false);
		    this.operationResultLabel.setValue("JOB Stopped");
		}
	}	

	
	/**
	 * Job Listener for the James Monitoring job. 
	 * It is necessary know when the monitor job has finished (not stopped by user) to remove the Jamesui temporary file and update the UI
	 * 
	 * NOTE: Should be better place it in a separated class file, but placing in here is possible access at the UI element
	 * to update the UI elements when the job has finished.
	 * 
	 * @author fulvio
	 *
	 */
	private class JamesMonitorJobListener implements org.quartz.JobListener{
		
		public static final String LISTENER_NAME = "JamesMonitorJobListener";
		
		/* the date when the job must finish (if the user don't stop it before with stop the button) */
		private Date jobStopDate;		
		private int pollingFrequency;

		/**
		 * Constructor
		 * @param stopDate The job stop date set by user in the gui
		 * @param freq The polling frequency
		 */
		public JamesMonitorJobListener(Date stopDate, int freq) {
			
		    this.jobStopDate = stopDate; 
		    this.pollingFrequency = freq;
		}
		
		public String getName() {
			return LISTENER_NAME; 
		}
		
		/**
		 * Executed when the job has finished. 
		 * James is monitored with a polling strategy, so that we can considerate finished the job when
		 * the next execution time is after the end date set by user in the JamesUI, if this is true the scheduler will not execute the job another time. 
		 */
		public void jobWasExecuted(JobExecutionContext context,JobExecutionException jobException) {
					
			Date nowDate = Calendar.getInstance().getTime();  
			long nextExecutionTime = (nowDate.getTime())  + pollingFrequency;
			
			Date nextExecutionDate = new Date(nextExecutionTime);
		
			if(nextExecutionDate.after(jobStopDate)) 
			{			  
			   JAMESUI_TMP_FILE.delete();
			   LOG.info("Monitor job Was Executed ");
			   			   
			   //updates of the UI to Push using Vaadin Push features
			   startJobButton.setEnabled(true);
			   stopJobButton.setEnabled(false);
			   operationResultLabel.setValue("JOB Finished");
			   
			   /* Enable for Debug
			   System.out.println("--> Database Queue stats:");
			   QueuesDatabaseManager.printAllCollectionValues(queueDb);
			   
			   System.out.println("--> Database Route stats:");
			   RoutesDatabaseManager.printAllCollectionValues(routeDb);
			   
			   System.out.println("--> Database Metatadata:");
			   QueuesDatabaseManager.printMetadataCollectionValues(queueDb);
			   RoutesDatabaseManager.printMetadataCollectionValues(routeDb);			   
			   */
			   
			   routeDb.close();
			   queueDb.close();
			   
			   //"fake" Thread, necessary to Push the above updates to the UI using Vaadin @Push features  
			   getUI().access(new Runnable()
               {
                   @Override
                   public void run ()
                   {
                                         
                   }
               } );
			   
			 }   
		}

		@Override
		public void jobExecutionVetoed(JobExecutionContext arg0) {		
			
		}

		@Override
		public void jobToBeExecuted(JobExecutionContext arg0) {		
			
		}
	}
	
	/** 
	 * Utility method
	 * @return the current Date
	 */
	private static Date getCurrentDate()
	{  	   	   
	    return Calendar.getInstance().getTime();
	}
	
	/**
	 * Utility method that list all the job in the Quartz Scheduler
	 * @throws SchedulerException 
	 */
	private void listAlljobs() throws SchedulerException {		
		
		LOG.info("----- Scheduler:"+scheduler.getSchedulerName() +"-----");		
		  for (String groupName : scheduler.getJobGroupNames()) 
		  {	 		
			 for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) 
			   LOG.info("In Group name: "+groupName+", Found job name: "+jobKey.getName()+" job group: "+jobKey.getGroup());
		  }	
		LOG.info("-----------------------------------------------------");
	}


}
