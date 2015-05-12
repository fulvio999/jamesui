package org.apache.james.jamesui.frontend.statistic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import org.apache.james.jamesui.backend.bean.statistics.ActiveMQstatisticBean;
import org.apache.james.jamesui.backend.bean.statistics.CamelRouteStatisticBean;
import org.apache.james.jamesui.backend.bean.statistics.StatisticQueueBean;
import org.apache.james.jamesui.backend.client.jmx.ActiveMQclient;
import org.apache.james.jamesui.backend.client.jmx.CamelClient;
import org.apache.james.jamesui.backend.constant.RoutesNameEnum;
import org.apache.james.jamesui.frontend.statistic.util.SnapshotStatisticsPdfExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.DocumentException;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Sizeable;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Create some tables with snapshot statistics about Camel routes and ActiveMQ queues taken at a specific datatime.
 * Also allow to export them to a PDF file.
 *   
 * @author fulvio
 */
public class SnapshotStatisticsPanel extends GridLayout {

	private static final long serialVersionUID = -8081470157286313524L;
		
	private final static Logger LOG = LoggerFactory.getLogger(SnapshotStatisticsPanel.class);
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	/* the layout of the page header */
	private VerticalLayout headerLayout;
	
	/* the layout of the page footer */
	private HorizontalLayout footerLayout;
	
	/* the main layout of the page */
	private GridLayout fullPageLayout;
	
	private Label headerTitle;
	
    private Button exportPdfButton;	
	private Button refreshButton;
	
	private CamelClient camelClient;
	private ActiveMQclient activeMQclient;
	
	//Camel stats
	private Table camelSpamRouteTable;
	private Table camelBouncesRouteTable;
	private Table camelErrorRouteTable;
	private Table camelTransportRouteTable;
	private Table camelLocalAddressErrortTable;
	private Table camelRelayDeniedTable;
	private Table camelRootRouteTable;	
	
	//ActiveMQ stats
	private Table activeMQbrokerInfoTable;
	private Table activeMQspoolQueueTable;
	private Table activeMQoutgoingQueueTable;
	
	//Data structure to hold stats data
	private Hashtable<RoutesNameEnum, CamelRouteStatisticBean> camelRouteStatisticHashTable;
	private ActiveMQstatisticBean activeMQstatisticBean;

	private SnapshotStatisticsPdfExporter snapshotStatisticsPdfExporter;
	
	/**
     * Constructor
     */
    public SnapshotStatisticsPanel(ActiveMQclient client1, CamelClient client2) {    	
    	
        super();
        
        this.snapshotStatisticsPdfExporter = new SnapshotStatisticsPdfExporter();
        
        this.setMargin(true); 
        this.setSpacing(true);
        
        this.setWidth(100, Sizeable.Unit.PERCENTAGE );
        this.setHeight(100, Sizeable.Unit.PERCENTAGE );
       
        this.setColumns(1);
        this.setRows(3);
        
        this.camelClient = client2;
        this.activeMQclient = client1;

        this.fullPageLayout = new GridLayout(4,1);	
        this.fullPageLayout.setMargin(true); 
        this.fullPageLayout.setSpacing(true); 
        this.fullPageLayout.setHeight(100, Sizeable.Unit.PERCENTAGE );
        this.fullPageLayout.setWidth(100, Sizeable.Unit.PERCENTAGE );
      
        //-- ONLY FOR TEST: insert fake data
        //this.camelRouteStatisticHashTable = insertFakeCamelStats();        
        //this.activeMQstatisticBean =  insertFakeActiveMQStats();    
                    
        this.getStatisticData();
          
        //------- HEADER
        Panel headerPanel = new Panel();
        headerPanel.setSizeFull();  
        headerPanel.setStyleName(Reindeer.PANEL_LIGHT);
       
        this.headerTitle = new Label("<b> SNAPSHOT DATE (dd-MM-yyyy HH:mm:ss): </b> "+sdf.format(getCurrentDate()), ContentMode.HTML);
        
        this.headerLayout = new VerticalLayout();
        this.headerLayout.setMargin(true);
        this.headerLayout.setSpacing(true);
        this.headerLayout.setSizeFull();         
        this.headerLayout.addComponent(headerTitle);   
        
        headerPanel.setContent(headerLayout);	         
        
        this.createStatsTables();   
                 
        this.fillCamelStatsTable(camelSpamRouteTable, "Route: Spam", camelRouteStatisticHashTable.get(RoutesNameEnum.SPAM));
        this.fillCamelStatsTable(camelBouncesRouteTable,"Route: Bounces", camelRouteStatisticHashTable.get(RoutesNameEnum.BOUNCES) ); 
        this.fillCamelStatsTable(camelErrorRouteTable,"Route: Error", camelRouteStatisticHashTable.get(RoutesNameEnum.ERROR) ); 
        this.fillCamelStatsTable(camelTransportRouteTable,"Route: transport", camelRouteStatisticHashTable.get(RoutesNameEnum.TRANSPORT) );  
        this.fillCamelStatsTable(camelLocalAddressErrortTable,"Route: Local-Address-Error", camelRouteStatisticHashTable.get(RoutesNameEnum.LOCAL_ADDRESS_ERROR) );
        this.fillCamelStatsTable(camelRelayDeniedTable,"Route: Relay-Denied", camelRouteStatisticHashTable.get(RoutesNameEnum.RELAY_DENIED) );
        this.fillCamelStatsTable(camelRootRouteTable,"Route: Root", camelRouteStatisticHashTable.get(RoutesNameEnum.ROOT) );
           
        this.fillActiveMQbrokerTable(activeMQbrokerInfoTable, activeMQstatisticBean, "Broker Info");
        this.fillQueueStatTable(activeMQoutgoingQueueTable, activeMQstatisticBean.getStatisticQueueTable().get("spool") , "Outgoing queue");
        this.fillQueueStatTable(activeMQspoolQueueTable, activeMQstatisticBean.getStatisticQueueTable().get("outgoing"), "Spool queue");
        
        //Camel Tables
        this.fullPageLayout.addComponent(camelSpamRouteTable);
        this.fullPageLayout.addComponent(camelBouncesRouteTable);
        this.fullPageLayout.addComponent(camelErrorRouteTable);
        this.fullPageLayout.addComponent(camelTransportRouteTable);        
        this.fullPageLayout.addComponent(camelLocalAddressErrortTable);
        this.fullPageLayout.addComponent(camelRelayDeniedTable);
        this.fullPageLayout.addComponent(camelRootRouteTable);
        this.fullPageLayout.addComponent(new Label()); //placeholder          
        
        //ActiveMQ tables
        this.fullPageLayout.addComponent(activeMQbrokerInfoTable);
        this.fullPageLayout.addComponent(activeMQspoolQueueTable);
        this.fullPageLayout.addComponent(activeMQoutgoingQueueTable);        
        this.fullPageLayout.addComponent(new Label()); //placeholder        
        
        
        //---- FOOTER        
        Panel footerPanel = new Panel();
        footerPanel.setSizeFull();  
        footerPanel.setStyleName(Reindeer.PANEL_LIGHT);
       
        this.footerLayout = new HorizontalLayout();      
        this.footerLayout.setMargin(true);
        this.footerLayout.setSpacing(true);             
        footerPanel.setContent(footerLayout);	
        
        this.exportPdfButton = new Button("Export PDF");

        //StreamResource myResource = this.downloadPDFresource(camelRouteStatisticHashTable,activeMQstatisticBean,sdf.format(getCurrentDate()));
        StreamResource myResource = this.downloadPDFresource();
        FileDownloader fileDownloader = new FileDownloader(myResource);
        fileDownloader.extend(exportPdfButton);

        this.refreshButton = new Button("Refresh");   
        this.refreshButton.addClickListener(new RefreshSnapshotStatsButtonListener());
        
        footerLayout.addComponent(exportPdfButton);
        footerLayout.addComponent(refreshButton);        
        
        this.addComponent(headerLayout);
        this.addComponent(fullPageLayout);   
        this.addComponent(footerLayout);
    }  
    
    
    private Date getCurrentDate()
    {    	
    	Calendar calendar = Calendar.getInstance();
        return calendar.getTime(); 
    }
    
    /**
     * Create and fill the stats tables
     */
    private void createStatsTables() {    	
        
        this.camelSpamRouteTable = new Table();
        this.camelSpamRouteTable.setWidth( 100, Sizeable.Unit.PERCENTAGE );
        this.camelSpamRouteTable.setHeight(200, Unit.PIXELS);     
        
        //Camel bounce route
        this.camelBouncesRouteTable = new Table();
        this.camelBouncesRouteTable.setWidth( 100, Sizeable.Unit.PERCENTAGE );
        this.camelBouncesRouteTable.setHeight(200, Unit.PIXELS);       

        //Camel error route
        this.camelErrorRouteTable = new Table();
        this.camelErrorRouteTable.setWidth( 100, Sizeable.Unit.PERCENTAGE );
        this.camelErrorRouteTable.setHeight(200, Unit.PIXELS);       
        
        //Camel transport route
        this.camelTransportRouteTable = new Table();
        this.camelTransportRouteTable.setWidth( 100, Sizeable.Unit.PERCENTAGE );
        this.camelTransportRouteTable.setHeight(200, Unit.PIXELS);       
         
        //Camel local-address-error route
        this.camelLocalAddressErrortTable = new Table();
        this.camelLocalAddressErrortTable.setWidth( 100, Sizeable.Unit.PERCENTAGE );
        this.camelLocalAddressErrortTable.setHeight(200, Unit.PIXELS);       
        
        //Camel relay-denied route
        this.camelRelayDeniedTable = new Table();
        this.camelRelayDeniedTable.setWidth( 100, Sizeable.Unit.PERCENTAGE );
        this.camelRelayDeniedTable.setHeight(200, Unit.PIXELS);       
        
        //Camel root route
        this.camelRootRouteTable = new Table();
        this.camelRootRouteTable.setWidth( 100, Sizeable.Unit.PERCENTAGE );
        this.camelRootRouteTable.setHeight(200, Unit.PIXELS);  
       
        //---------- ActiveMQ (Note: the table height is fix because it have a fix total amount of rows to show )        
        this.activeMQbrokerInfoTable = new Table();
        this.activeMQbrokerInfoTable.setSizeFull();
        this.activeMQbrokerInfoTable.setWidth( 100, Sizeable.Unit.PERCENTAGE );
        this.activeMQbrokerInfoTable.setHeight(200, Unit.PIXELS);          
       
        this.activeMQspoolQueueTable = new Table();
        this.activeMQspoolQueueTable.setSizeFull();
        this.activeMQspoolQueueTable.setWidth( 100, Sizeable.Unit.PERCENTAGE );
        this.activeMQspoolQueueTable.setHeight(200, Unit.PIXELS);
        
        this.activeMQoutgoingQueueTable = new Table();
        this.activeMQoutgoingQueueTable.setSizeFull();
        this.activeMQoutgoingQueueTable.setWidth( 100, Sizeable.Unit.PERCENTAGE );
        this.activeMQoutgoingQueueTable.setHeight(200, Unit.PIXELS);   
    }
    
    
   /* Utility method that fill the table
    * @param camelRouteStatisticBean The data model used to fill the table
    */
	public void fillCamelStatsTable(Table t, String caption, CamelRouteStatisticBean cb) { 
	 
	      t.setSelectable(true);     
	      t.addContainerProperty( caption, String.class, null );
	      t.addContainerProperty( "", String.class, null );      
	       
	      t.addItem( new Object[] { "routeState", String.valueOf(cb.getRouteName()) },new Integer( 1 ) );
	      t.addItem( new Object[] { "exchangesCompleted", String.valueOf(cb.getExchangesCompleted()) },new Integer( 2 ) );
	      t.addItem( new Object[] { "exchangesFailed", String.valueOf(cb.getExchangesFailed()) },new Integer( 3 ) );
	      t.addItem( new Object[] { "exchangesTotal", String.valueOf(cb.getExchangesTotal()) },new Integer( 4 ) );
	      t.addItem( new Object[] { "minProcessingTime", String.valueOf(cb.getMinProcessingTime()) },new Integer( 5 ) );
	      t.addItem( new Object[] { "maxProcessingTime", String.valueOf(cb.getMaxProcessingTime()) },new Integer( 6 ) );
	      t.addItem( new Object[] { "meanProcessingTime", String.valueOf(cb.getMeanProcessingTime()) },new Integer( 7 ) );   
	}
  
  
  	 /**
	   * Utility method that fill the table about ActiveMQ broker stats
	   * @param activeMQstatisticBean The data model used to fill the table
	   */
	 private void fillActiveMQbrokerTable(Table t, ActiveMQstatisticBean activeMQstatisticBean, String caption) { 
	     	
	     t.setSelectable(true);   	   
	     t.addContainerProperty(caption, String.class, null);
	     t.addContainerProperty("", String.class, null);
	    
	     t.addItem( new Object[] { "Broker Id", activeMQstatisticBean.getBrokerId() }, new Integer( 1 ) );
	     t.addItem( new Object[] { "Broker Name", activeMQstatisticBean.getBrokerName() }, new Integer( 2 ) );
	     t.addItem( new Object[] { "Total Message Count", String.valueOf(activeMQstatisticBean.getTotalMessageCount()) }, new Integer( 3 ) );
	     t.addItem( new Object[] { "Total Consumer Count", String.valueOf(activeMQstatisticBean.getTotalConsumerCount()) }, new Integer( 4 ) );
	     t.addItem( new Object[] { "Queue Length", String.valueOf(activeMQstatisticBean.getQueueLength()) }, new Integer( 5 ) ); 	    
	 }
	 
	 
	/**
	 * Utility method that fill the table about Queue stats
	 * @param activeMQstatisticBean The data model used to fill the table
	 */
    private void fillQueueStatTable(Table t, StatisticQueueBean qb, String caption) { 
	    
    	t.setSelectable(true);   	
	    t.addContainerProperty(caption, String.class, null);
	    t.addContainerProperty("", String.class, null);
	        
	    t.addItem( new Object[] { "Queue name", qb.getQueueName() }, new Integer( 1 ) );
	    t.addItem( new Object[] { "size", String.valueOf(qb.getSize()) }, new Integer( 2 ) );
	    t.addItem( new Object[] { "Consumer count",  String.valueOf(qb.getConsumerCount()) }, new Integer( 3 ) );
	    t.addItem( new Object[] { "Enque count",  String.valueOf(qb.getEnqueueCount()) } , new Integer( 4 ));
	} 
    
	  
    /*
     * Utility method that clean all 
     */
    private void cleanAllTables(){
    	
    	this.camelSpamRouteTable.removeAllItems();
    	this.camelBouncesRouteTable.removeAllItems();
    	this.camelErrorRouteTable.removeAllItems();
    	this.camelTransportRouteTable.removeAllItems();
    	this.camelLocalAddressErrortTable.removeAllItems();
    	this.camelRelayDeniedTable.removeAllItems();
    	this.camelRootRouteTable.removeAllItems();    	
        
    	this.activeMQbrokerInfoTable.removeAllItems();
    	this.activeMQspoolQueueTable.removeAllItems();
    	this.activeMQoutgoingQueueTable.removeAllItems();
    }
    
    /**
     * Using the client get the statistics data
     */
    private void getStatisticData() {
    	
    	this.camelRouteStatisticHashTable = camelClient.queryAllRoutes();      
        this.activeMQstatisticBean = activeMQclient.getJamesMQbrokerStats();        
    }
    
    
    /**
	 * Button Listener invoked when the user press the "Refresh" button to update the Snapshot statistics
	 *
	 */
	private class RefreshSnapshotStatsButtonListener implements com.vaadin.ui.Button.ClickListener
	{
		private static final long serialVersionUID = 1L;

		public void buttonClick(ClickEvent event)
		{			
			headerTitle.setValue("SNAPSHOT DATE (dd-MM-yyyy HH:mm:ss): "+sdf.format(getCurrentDate()));
			getStatisticData();		
			cleanAllTables();
			
			fillCamelStatsTable(camelSpamRouteTable, "Route: Spam", camelRouteStatisticHashTable.get(RoutesNameEnum.SPAM));
			fillCamelStatsTable(camelBouncesRouteTable,"Route: Bounces", camelRouteStatisticHashTable.get(RoutesNameEnum.BOUNCES) ); 
			fillCamelStatsTable(camelErrorRouteTable,"Route: Error", camelRouteStatisticHashTable.get(RoutesNameEnum.ERROR) ); 
			fillCamelStatsTable(camelTransportRouteTable,"Route: transport", camelRouteStatisticHashTable.get(RoutesNameEnum.TRANSPORT) );  
			fillCamelStatsTable(camelLocalAddressErrortTable,"Route: Local-Address-Error", camelRouteStatisticHashTable.get(RoutesNameEnum.LOCAL_ADDRESS_ERROR) );
			fillCamelStatsTable(camelRelayDeniedTable,"Route: Relay-Denied", camelRouteStatisticHashTable.get(RoutesNameEnum.RELAY_DENIED) );
			fillCamelStatsTable(camelRootRouteTable,"Route: Root", camelRouteStatisticHashTable.get(RoutesNameEnum.ROOT) );
			
		    fillActiveMQbrokerTable(activeMQbrokerInfoTable, activeMQstatisticBean, "Broker Info");
		    fillQueueStatTable(activeMQoutgoingQueueTable, activeMQstatisticBean.getStatisticQueueTable().get("spool") , "Outgoing queue");
		    fillQueueStatTable(activeMQspoolQueueTable, activeMQstatisticBean.getStatisticQueueTable().get("outgoing"), "Spool queue");
		 			
			Notification.show("Operation Executed Successfully", Type.HUMANIZED_MESSAGE);
		}		
	}
	
    
    /**
     * Utility method to download the PDF export
     * @return the PDF file as PDF
     */
	private StreamResource downloadPDFresource() {
		
        return new StreamResource(new StreamSource() {         
           
			private static final long serialVersionUID = 1L;

			public InputStream getStream() 
			{
				try {             
					return util(sdf.format(getCurrentDate()));
                }catch (Exception e) {
                    LOG.error("Error exporting the Snaphsot stats to PDF, cause: ",e);
                    return null;
                }
            }
        }, "snapshot-"+sdf.format(getCurrentDate())+"-export.pdf");
    }
	
	private InputStream util(String statsTime) throws DocumentException, IOException{
		
		 ByteArrayOutputStream bos = snapshotStatisticsPdfExporter.createPdfDocument(camelRouteStatisticHashTable, activeMQstatisticBean, sdf.format(getCurrentDate()));
         return new ByteArrayInputStream(bos.toByteArray());
	}
	
	
	/**
	 * Utility method that insert some FAKE data in the Camel Route statistics beans
	 * Used ONLY for local development 
	 */
	private  Hashtable<RoutesNameEnum, CamelRouteStatisticBean> insertFakeCamelStats(){
		
		//key is the route name
        Hashtable<RoutesNameEnum, CamelRouteStatisticBean> camelRouteStatisticHashTable = new Hashtable<RoutesNameEnum,CamelRouteStatisticBean>();
       
        CamelRouteStatisticBean dummy = new CamelRouteStatisticBean();   
        dummy.setRouteName("route-test");
        dummy.setExchangesCompleted(23);
        dummy.setExchangesFailed(455);
        dummy.setExchangesTotal(534);
        dummy.setMinProcessingTime(3454);
        dummy.setMaxProcessingTime(43);
        dummy.setMeanProcessingTime(33);
 		
		camelRouteStatisticHashTable.put(RoutesNameEnum.SPAM, dummy );
        camelRouteStatisticHashTable.put(RoutesNameEnum.BOUNCES, dummy);
        camelRouteStatisticHashTable.put(RoutesNameEnum.ERROR, dummy);
        camelRouteStatisticHashTable.put(RoutesNameEnum.TRANSPORT, dummy);
		camelRouteStatisticHashTable.put(RoutesNameEnum.LOCAL_ADDRESS_ERROR, dummy);
		camelRouteStatisticHashTable.put(RoutesNameEnum.RELAY_DENIED, dummy);
		camelRouteStatisticHashTable.put(RoutesNameEnum.ROOT, dummy);
		
		return camelRouteStatisticHashTable;
	}
	
	/**
	 * Utility method that insert some fake data in the ActiveMQ stats beans 
	 */
	private ActiveMQstatisticBean insertFakeActiveMQStats(){
		
		//key is the route name
		ActiveMQstatisticBean activeMQstatisticBean = new ActiveMQstatisticBean();
       
		activeMQstatisticBean.setBrokerId("fakeID-broker");
		activeMQstatisticBean.setBrokerName("fakeBroker name");		
		activeMQstatisticBean.setTotalMessageCount(257);
		activeMQstatisticBean.setTotalConsumerCount(4534);
		activeMQstatisticBean.setQueueLength(434);
		
		//---- Queue stats
		Hashtable<String,StatisticQueueBean> statisticQueueTable = new Hashtable<String,StatisticQueueBean>();
		
		StatisticQueueBean statisticQueueBean1 = new StatisticQueueBean();
		statisticQueueBean1.setQueueName("spool");
		statisticQueueBean1.setSize(23);
		statisticQueueBean1.setConsumerCount(21);
		statisticQueueBean1.setEnqueueCount(87);
		
		StatisticQueueBean statisticQueueBean2 = new StatisticQueueBean();
		statisticQueueBean2.setQueueName("outgoing");
		statisticQueueBean2.setSize(83);
		statisticQueueBean2.setConsumerCount(26);
		statisticQueueBean2.setEnqueueCount(77);

		statisticQueueTable.put("spool", statisticQueueBean1);
		statisticQueueTable.put("outgoing", statisticQueueBean2);
		
		activeMQstatisticBean.setStatisticQueueTable(statisticQueueTable);
		
		return activeMQstatisticBean;
	}

}