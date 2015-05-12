package org.apache.james.jamesui.frontend.statistic;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.james.jamesui.backend.configuration.manager.EnvironmentConfigurationReader;
import org.apache.james.jamesui.backend.constant.QueueNameEnum;
import org.apache.james.jamesui.backend.constant.QueueParamEnum;
import org.apache.james.jamesui.backend.constant.RoutesNameEnum;
import org.apache.james.jamesui.backend.constant.RoutesParamEnum;
import org.apache.james.jamesui.backend.database.manager.QueuesDatabaseManager;
import org.apache.james.jamesui.backend.database.manager.RoutesDatabaseManager;
import org.apache.james.jamesui.frontend.statistic.util.QueueChartDataManager;
import org.apache.james.jamesui.frontend.statistic.util.RouteChartDataManager;
import org.mapdb.DB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.dialogs.ConfirmDialog;

import com.jensjansson.pagedtable.PagedTable;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.MouseEvents;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Panel that shows with a Flot chart a previously saved statistic about Routes and/or Queues.
 * Also allow to search and delete the saved statistics Database.
 *   
 * @author fulvio
 *
 */
public class HistoryStatisticPanel extends VerticalLayout {	
		
	private final static Logger LOG = LoggerFactory.getLogger(HistoryStatisticPanel.class);

	private static final long serialVersionUID = 1L;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	private Chart chart;
	
	private FormLayout searchStatisticFormLayout;
	private FormLayout statisticsFilterFormLayout;	
	
	private ComboBox statisticTypeCombo;
	private static final Object[] statisticsType = {"ALL Type","Queue","Route"};
	private DateField statsDatePicker;
	private PagedTable statsDatabaseTable;
	private Button searchStatisticButton;
	
	//to filter the statistic value/parameter to shown in the chart
	private ComboBox routeNameCombo;
	private ComboBox routeParamStatistics;
	
	private ComboBox queueNameCombo;
	private ComboBox queueParamStatistics;
	private Button showChartButton;
	
	private HorizontalSplitPanel horizontalSplitPanel;
	private	VerticalLayout rightPanelLayout;
	private VerticalLayout leftPanelLayout;
	
	/* the database name to load/display chosen by user in the table */	
	private String databaseNameToDisplay;
	 

	/**
	 * Constructor
	 */
	public HistoryStatisticPanel() {		
		 
		this.setSizeFull();
        this.setMargin(true);  
        this.setSpacing(true);		
         
        //--- RIGHT PANEL --- form to load/filter statistics type (queue/route)
        this.searchStatisticFormLayout = new FormLayout();
        
        this.searchStatisticButton = new Button("Search");
        this.searchStatisticButton.addClickListener(new SearchStatisticsButtonListener());
        
        this.statisticTypeCombo = new ComboBox("Statistics type:");
        this.statisticTypeCombo.addItems(statisticsType);	
        this.statisticTypeCombo.addValidator(new StringLengthValidator("Must not be empty", 1, 100, false));
        this.statisticTypeCombo.addValueChangeListener(new ValueChangeListener() {
		
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				
				if(statisticTypeCombo.getValue().equals("ALL Type"))
				   statsDatePicker.setEnabled(false);					  	
				else									
				   statsDatePicker.setEnabled(true);
				
			    /* disable left panel widget until user press "load" button in the column */
				manageLeftPanelVisibility(false,false,false);				
				statisticsFilterFormLayout.setCaption("");
				chart.clearSerie(); 
				chart.addSeries(new ArrayList<String>(),new ArrayList<Long>()); //empty values
			}
		});
        
        this.statsDatePicker = new DateField("Statistics date (yyyy-MM-dd):");  
        this.statsDatePicker.setResolution(Resolution.DAY);
        this.statsDatePicker.setValue(new Date());
        this.statsDatePicker.setDateFormat("yyyy-MM-dd"); 
               
        this.searchStatisticFormLayout.setCaption("Search Statistic:");
        this.searchStatisticFormLayout.addComponent(statisticTypeCombo);
        this.searchStatisticFormLayout.addComponent(statsDatePicker);
        this.searchStatisticFormLayout.addComponent(searchStatisticButton);                
        
        
        //--- LEFT PANEL --- form to choose the statistics to show in the chart
        this.statisticsFilterFormLayout = new FormLayout(); 
         
        this.queueNameCombo = new ComboBox("Queue name:");
        this.queueNameCombo.addItems(QueueNameEnum.values());        
        this.queueParamStatistics = new ComboBox("Queue param:");
        this.queueParamStatistics.addItems(QueueParamEnum.values());              
       
        this.routeNameCombo = new ComboBox("Route name:");
        this.routeNameCombo.addItems(RoutesNameEnum.values());        
        this.routeParamStatistics = new ComboBox("Route param:");
        this.routeParamStatistics.addItems(RoutesParamEnum.values());
        
        this.showChartButton = new Button("Show");   
        this.showChartButton.addClickListener(new ShowChartButtonListener());        
        
        /* By default is everything disabled: enabled when a statistic DB is selected */
        this.manageLeftPanelVisibility(false,false,false);
        
        this.statisticsFilterFormLayout.addComponent(queueNameCombo);
        this.statisticsFilterFormLayout.addComponent(queueParamStatistics);        
        this.statisticsFilterFormLayout.addComponent(routeNameCombo);   
        this.statisticsFilterFormLayout.addComponent(routeParamStatistics);         
        this.statisticsFilterFormLayout.addComponent(showChartButton);
        
        
        // Compose the full layout
        this.horizontalSplitPanel = new HorizontalSplitPanel();
		this.horizontalSplitPanel.setStyleName(Reindeer.SPLITPANEL_SMALL);
		this.horizontalSplitPanel.setLocked(false);
		
		this.rightPanelLayout = new VerticalLayout();
		this.rightPanelLayout.setMargin(true);       
        this.rightPanelLayout.addComponent(searchStatisticFormLayout);
        
		this.chart = new Chart();
		this.chart.addSeries(new ArrayList<String>(),new ArrayList<Long>()); //empty data		
		this.chart.setWidth("520px");		
		this.chart.setHeight("300px");
		
		this.leftPanelLayout = new VerticalLayout();
		this.leftPanelLayout.setMargin(true);
		this.leftPanelLayout.addComponent(statisticsFilterFormLayout);
		this.leftPanelLayout.addComponent(chart);
		
		this.horizontalSplitPanel.setFirstComponent(leftPanelLayout);	  
		this.horizontalSplitPanel.setSecondComponent(rightPanelLayout);		
	
		/*
		 * NOT USED CURRENTLY ONLY AN EXPERIMENT
		 * Create on client side, a Javascript function that execute an Ajax request passing some value at the server
		 */
	  /*	
		JavaScript.getCurrent().addFunction("com.example.foo.myfunc", new JavaScriptFunction() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void call(JSONArray arguments) throws JSONException {
				
				try {
					String message = arguments.getString(0);
					int value = arguments.getInt(1);
					Notification.show("Message: " + message +", value: " + value);
										
					chart.clearSerie();						
					chart.addSeries(1.0, 2.0, 3.0, 3.0, 3, 3, 3);
					
					//Simple Vaadin Container implementation that reads data in JSON format to populate the Container.
					//V. https://vaadin.com/directory#addon/jsoncontainer
				
				} catch (JSONException e) {
				   Notification.show("Error: " + e.getMessage());
				}
			}
			});
			
		    //Front-end element that call the javascript function define above (prompt('Message'). That function open a popup with input text
			Link link = new Link("Send Message", new ExternalResource("javascript:com.example.foo.myfunc(prompt('Message'), 42)"));
			//addComponent(link);
			
		 */
		addComponent(horizontalSplitPanel);			  
	}	

	
	/**
	 * Enable the right widgets in the left panel according with DB type chosen (queue/route)
	 * @param queueEnabled Enable the widget about the queue
	 * @param routeEnabled
	 */
	 private void manageLeftPanelVisibility(boolean queueEnabled, boolean routeEnabled, boolean buttonEnabled){
		
		queueNameCombo.setEnabled(queueEnabled);
    	queueParamStatistics.setEnabled(queueEnabled);
    	
    	routeParamStatistics.setEnabled(routeEnabled);
    	routeNameCombo.setEnabled(routeEnabled);	
    	
    	showChartButton.setEnabled(buttonEnabled);
	 }
	
	 /**
	  * Invoked when the user press the "show" button (on the left panel) to show the statistics about of the queue/route chosen
	  *
	  **/
	 private class ShowChartButtonListener implements com.vaadin.ui.Button.ClickListener
	 {
		private static final long serialVersionUID = 1L;

		public void buttonClick(ClickEvent event)
		{		
			DB targetDatabaseFile = null;
			
			LOG.trace("TARGET DB: "+databaseNameToDisplay);
			
			if(databaseNameToDisplay.startsWith("route")) 
			{			   
			   targetDatabaseFile = RoutesDatabaseManager.getRouteDataBase(EnvironmentConfigurationReader.getStatsDatabaseFolder(), databaseNameToDisplay);
			   
			   RoutesParamEnum routeParam = (RoutesParamEnum) routeParamStatistics.getValue();
			   RoutesNameEnum routeName = (RoutesNameEnum) routeNameCombo.getValue();
			   
			   List<String> x_values = RouteChartDataManager.extractRouteXvalues(targetDatabaseFile, routeName);
			   List<Long> y_values = RouteChartDataManager.extractRouteYvalues(targetDatabaseFile, routeName, routeParam);
			   
			   //refresh the chart
			   chart.clearSerie();
			   chart.addSeries(x_values,y_values);
			   			   
			   targetDatabaseFile.close(); //necessary to unlock DB files			   
			   Notification.show("Operation Executed", Type.HUMANIZED_MESSAGE);
			   
			}else{ //queue
				targetDatabaseFile = QueuesDatabaseManager.getQueueDataBase(EnvironmentConfigurationReader.getStatsDatabaseFolder(), databaseNameToDisplay);
				
				QueueParamEnum queueParam = (QueueParamEnum) queueParamStatistics.getValue();
				QueueNameEnum queueName = (QueueNameEnum) queueNameCombo.getValue();
				
				List<String> x_values = QueueChartDataManager.extractQueueXvalues(targetDatabaseFile, queueName);
				List<Long> y_values = QueueChartDataManager.extractQueueYvalues(targetDatabaseFile, queueName, queueParam);
				
				//refresh the chart
				chart.clearSerie();
				chart.addSeries(x_values,y_values);
				   
				targetDatabaseFile.close(); //necessary to unlock DB files
				Notification.show("Operation Executed", Type.HUMANIZED_MESSAGE);
			}			
		}		
	 }
	
	 /**
	  * Invoked when the user press the "Search" button to search the Database files with the chosen statistics (Queue or Route)
	  *
	  **/
	 private class SearchStatisticsButtonListener implements com.vaadin.ui.Button.ClickListener
	 {
		private static final long serialVersionUID = 1L;

		public void buttonClick(ClickEvent event)
		{				
			statisticTypeCombo.validate();
			String chosenStatsType = (String) statisticTypeCombo.getValue();		
			fillOrUpdateStatisticsTable(chosenStatsType);	
			
			if(statsDatabaseTable.getContainerDataSource().size() == 0) 
			{
			   manageLeftPanelVisibility(false, false,false);	
			   Notification.show("No files found", Type.HUMANIZED_MESSAGE);
			  
			   statisticsFilterFormLayout.setCaption("");
			   chart.clearSerie(); 
			   chart.addSeries(new ArrayList<String>(),new ArrayList<Long>()); //empty values
			}  
		}		
	 }
	 
	 /**
	  * Method that fill the table with the Database files found on the file system according with the search filters
	  * @param chosenStatsType The value set in the combo Box: Queue or Route
	  */
	 private void fillOrUpdateStatisticsTable(String chosenStatsType)
	 {	
		  String statsDatabaseFolder = EnvironmentConfigurationReader.getStatsDatabaseFolder();		
		  		  
		  Collection<File> filesFound = new ArrayList<File>();
		 			
		  if(chosenStatsType.equalsIgnoreCase("Queue"))				
			 filesFound.addAll( FileUtils.listFiles(new File(statsDatabaseFolder), new QueueFileFilter(), null) );	
		  
		  else if(chosenStatsType.equalsIgnoreCase("Route"))
			 filesFound.addAll( FileUtils.listFiles(new File(statsDatabaseFolder), new RouteFileFilter(), null) );	
		  
		  else
			 filesFound.addAll( FileUtils.listFiles(new File(statsDatabaseFolder), new NoFileFilter(), null) );
		 
		  IndexedContainer container = new IndexedContainer();
	      container.addContainerProperty("File", String.class, null);
	      container.addContainerProperty("Date (yyyy-MM-dd)", String.class, null);
	   
	      String[] fileNameTokens = null;
	      for(File f: filesFound) 
	      {
	         Item item = container.addItem(f.getName());
	         fileNameTokens = f.getName().split("_");
	         
	         item.getItemProperty("File").setValue(f.getName());	
	         item.getItemProperty("Date (yyyy-MM-dd)").setValue(fileNameTokens[1]);	
	      } 
	      
	      if(this.statsDatabaseTable == null) //no table shown on first page load
	      {	      
	    	  this.statsDatabaseTable = new PagedTable(""); 	      
		      this.statsDatabaseTable.addGeneratedColumn("Delete", new DeleteColoumnGenerator()); 
		      this.statsDatabaseTable.setColumnWidth("Delete", 50);
		      this.statsDatabaseTable.addGeneratedColumn("Load", new LoadDatabaseColoumnGenerator());
		      this.statsDatabaseTable.setColumnWidth("Load", 50);
		      this.statsDatabaseTable.setColumnWidth("Date (yyyy-MM-dd)", 100);
		      this.statsDatabaseTable.setPageLength(15); 
		      this.statsDatabaseTable.setSelectable(true);
		      this.statsDatabaseTable.setSizeFull();    
		      this.statsDatabaseTable.setContainerDataSource(container); 
		      
		      this.rightPanelLayout.addComponent(statsDatabaseTable); 
		      this.rightPanelLayout.addComponent(statsDatabaseTable.createControls());
		      
	      }else{ //refresh only the table content
	    	  this.statsDatabaseTable.removeAllItems(); 
	    	  this.statsDatabaseTable.setContainerDataSource(container);
	      }	      
	 }
	 
	/**
	 * Column Generator for the "Load" table column.
	 * When clicked this table cell, is set the target database parameter (ie: the Database to load)
	 * @author fulvio
	 *
	 **/	
	private class LoadDatabaseColoumnGenerator implements ColumnGenerator {

			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId, Object columnId) {			 
			     
			     String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
			     FileResource resource = new FileResource(new File(basepath +"/WEB-INF/images/display.png")); 
			        
			     Image showStatsImage = new Image("", resource); 			        
			     showStatsImage.setDescription("Load");
			     showStatsImage.setAlternateText("Load");
			     showStatsImage.addClickListener(new MouseEvents.ClickListener() 
			     {				
			     	private static final long serialVersionUID = 1L;

					@Override
					public void click(com.vaadin.event.MouseEvents.ClickEvent event) 
					{						
					   Item selectedItem = source.getContainerDataSource().getItem(itemId);				  
					   databaseNameToDisplay = (String) selectedItem.getItemProperty("File").getValue(); //eg: route-db_2015-04-17_16_06_13
					   LOG.debug("Target DB file name: "+databaseNameToDisplay); 	
					   
					   if(databaseNameToDisplay.startsWith("queue"))						  
						   manageLeftPanelVisibility(true,false,true);
					   else
						   manageLeftPanelVisibility(false,true,true);	
					   					   
				        statisticsFilterFormLayout.setCaption("Target database: "+databaseNameToDisplay);
					}
				 });	

		     return showStatsImage;
	     }
	}	 
	 
	/**
	 * Column Generator for the "delete" table column. When selected is deleted the associated Database file
	 * @author fulvio
	 *
	 **/	
	private class DeleteColoumnGenerator implements ColumnGenerator {

			private static final long serialVersionUID = 1L;

			@Override
		    public Object generateCell(final Table source, final Object itemId, Object columnId) {			 
		      
		        String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		        FileResource resource = new FileResource(new File(basepath +"/WEB-INF/images/delete.png")); 
		        
		        Image deleteFileImage = new Image("", resource); 			        
		        deleteFileImage.setDescription("Delete File");
		        deleteFileImage.setAlternateText("Delete");
		        deleteFileImage.addClickListener(new MouseEvents.ClickListener() 
		        {				
					private static final long serialVersionUID = 1L;

					@Override
					public void click(com.vaadin.event.MouseEvents.ClickEvent event) 
					{						
					   Item selectedItem = source.getContainerDataSource().getItem(itemId);						
					   showConfirm("Delete ? "+ selectedItem.getItemProperty("File").getValue(), selectedItem.getItemProperty("File").getValue()+"");							 
					}
				});	

		        return deleteFileImage;
	       }
	 }
	
	 /**
	 * Show a window confirmation using Vaadin "confirm window" component
	 * @param message The message to show in the confirm window
	 * @param itemToRemove the Database file to delete
	 */
	 private void showConfirm(String message, final String itemToRemove) {	
	
		ConfirmDialog.show(this.getUI(), "Please Confirm:", message,"Yes","No", new ConfirmDialog.Listener() {
			
			private static final long serialVersionUID = 1L;

			public void onClose(ConfirmDialog dialog) 
			{						
	           if(dialog.isConfirmed()) 
	           {
					if(removeStatisticsFiles(itemToRemove)) 
					{					 
					   fillOrUpdateStatisticsTable((String) statisticTypeCombo.getValue());						  
					   Notification.show("File Removed Successfully !", Type.HUMANIZED_MESSAGE);					   
					}else{
					   LOG.error("Error removing DB statistics file: "+itemToRemove);
					   fillOrUpdateStatisticsTable((String) statisticTypeCombo.getValue());	
					   Notification.show("Error removing file: "+itemToRemove+", file locked ?" , Type.ERROR_MESSAGE);	
					 }
		        }else{
		           // User did not confirm		                	
		        }
		     }
		});
	 }
	
	 /**
	 * Utility method to remove from the file system ALL the three MapDB files about a statistic (.t ; .p; file with no extension)
	 * @return true if all file are remove successfully
	 */
	 private boolean removeStatisticsFiles(String fileName) {
		
		String fileToRemove = EnvironmentConfigurationReader.getStatsDatabaseFolder()+File.separator+fileName;
		LOG.debug("File to remove: "+fileToRemove);	
		
		boolean result1 = FileUtils.deleteQuietly( new File(fileToRemove));
		boolean result2 = FileUtils.deleteQuietly( new File(fileToRemove+".p"));
		boolean result3 = FileUtils.deleteQuietly( new File(fileToRemove+".t"));
		
		if(result1 && result2 && result3)		
		   return true;
		else
		   return false;		
	 }	 
	 
	 /**
	  * Custom file-filter to pick-up only database files with Queue statistics and with the user defined date
	  * Each database with MapDB is composed of three files with same name but with different extension:
	  * - a file with no extension <--- only this one is shown in the Table
	  * - a file with .t extension
	  * - a file with -p
	  * 
	  * Note: when the user press "delete" button, ALL the three database files are removed
	  * 
	  * @author fulvio
	  *
	  */
	 private class QueueFileFilter implements IOFileFilter {

		@Override
		public boolean accept(File file) {
			
		   if(file.isDirectory())
			  return false;
			
		   if(file.getName().startsWith("queue-db")) {
			   
			   //eg File: queue-db_2015-04-17_16_06_13
			   String fileExtension = FilenameUtils.getExtension(file.getName());
			   boolean fileDateMatch = file.getName().split("_")[1].equals(sdf.format(statsDatePicker.getValue()));
			   
			  if(fileDateMatch && fileExtension.equalsIgnoreCase(""))			   
				  return true;
			  else
				  return false;
		   }else
			  return false;
		}
	
		@Override
		public boolean accept(File f, String name) {			
			return accept(f);
		}		 
	 }
	 
	 /**
	  * Custom file-filter to pick-up only database files with Route statistics and with the user defined date
	  * Each database with MapDB is composed of three files with same name but with different extension:
	  * - a file with no extension <--- only this one is shown in the Table
	  * - a file with .t extension
	  * - a file with -p
	  * 
	  * Note: when the user press "delete" button, ALL the three database files are removed
	  * 
	  * @author fulvio
	  *
	  */
	 private class RouteFileFilter implements IOFileFilter {

		@Override
		public boolean accept(File file) {
			
		   if(file.isDirectory())
			  return false;	
			
		   if (file.getName().startsWith("route-db")) {
		   
			   //eg File: route-db_2015-04-17_16_06_13	
			   String fileExtension = FilenameUtils.getExtension(file.getName());
			   boolean fileDateMatch = file.getName().split("_")[1].equals(sdf.format(statsDatePicker.getValue()));
				
			   if(fileDateMatch && fileExtension.equalsIgnoreCase(""))
				   return true;
			   else
				   return false;
		   }		
			 return false;
		}

		@Override
		public boolean accept(File f, String name) {			
			return accept(f);
		}			 
	 }
	 
	 /**
	  * No filter is applied, all Database files found (ie the ones with no file extension) are loaded
	  * because a MapDB database is split in three files.
	  * @author fulvio
	  *
	  */
	 private class NoFileFilter implements IOFileFilter {
		 
			@Override
			public boolean accept(File file) {	
				
				if(file.getName().startsWith("route") || file.getName().startsWith("queue"))
				{
				String fileExtension = FilenameUtils.getExtension(file.getName());
				
				if(fileExtension.equalsIgnoreCase(""))
				   return true;
				else
				   return false;
				}else
					return false;				
			}

			@Override
			public boolean accept(File f, String name) {				
				return accept(f);			
			}			 
	  }	 
  }
	
	
