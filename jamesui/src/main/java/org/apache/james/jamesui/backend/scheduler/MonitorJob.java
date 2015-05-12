package org.apache.james.jamesui.backend.scheduler;

import java.util.Date;
import java.util.Hashtable;

import org.apache.james.jamesui.backend.client.jmx.ActiveMQclient;
import org.apache.james.jamesui.backend.client.jmx.CamelClient;
import org.apache.james.jamesui.backend.constant.QueueNameEnum;
import org.apache.james.jamesui.backend.constant.RoutesNameEnum;
import org.apache.james.jamesui.backend.database.manager.QueuesDatabaseManager;
import org.apache.james.jamesui.backend.database.manager.RoutesDatabaseManager;
import org.mapdb.DB;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Quartz job that query James using the dedicated JMX client
 * 
 * @author fulvio
 *
 */
public class MonitorJob extends QuartzJobBean {

	private final static Logger LOG = LoggerFactory.getLogger(MonitorJob.class);
	
	private JobDataMap dataMap;
	
	/* injected by Spring */	
	private ActiveMQclient activeMQclient;
	private CamelClient camelClient; 
 
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		
		dataMap = context.getJobDetail().getJobDataMap();			
		
		//get from the Job context the two DB where save the data
		DB routeDb = (DB) dataMap.get("routeDb");
	    DB queueDb = (DB) dataMap.get("queueDb");  
	    
	    //get stats data for James server
	  	Hashtable<RoutesNameEnum, org.apache.james.jamesui.backend.bean.statistics.CamelRouteStatisticBean> camelRouteStatisticTable = camelClient.queryAllRoutes();      
	  	org.apache.james.jamesui.backend.bean.statistics.ActiveMQstatisticBean activeMQstatisticBean = activeMQclient.getJamesMQbrokerStats();	
	  
	  	LOG.debug("---> RUNNING MonitorJob..."+new Date());
	  	
	    //-------------------- queues ----------------------	    
	  	Hashtable<String, org.apache.james.jamesui.backend.bean.statistics.StatisticQueueBean> t = activeMQstatisticBean.getStatisticQueueTable();
	  	
	    //insert data in the DB inside the right collection	    
	    QueuesDatabaseManager.putAndCommit(queueDb,QueueNameEnum.OUTGOING_QUEUE, t.get("outgoing") );
	    QueuesDatabaseManager.putAndCommit(queueDb,QueueNameEnum.SPOOL_QUEUE, t.get("spool") );	    
	    
	    //------------------- routes ------------------------         
	    RoutesDatabaseManager.putAndCommit(routeDb, RoutesNameEnum.SPAM, camelRouteStatisticTable.get(RoutesNameEnum.SPAM));
	    RoutesDatabaseManager.putAndCommit(routeDb, RoutesNameEnum.BOUNCES, camelRouteStatisticTable.get(RoutesNameEnum.BOUNCES));
	    RoutesDatabaseManager.putAndCommit(routeDb, RoutesNameEnum.ERROR, camelRouteStatisticTable.get(RoutesNameEnum.ERROR));
	    RoutesDatabaseManager.putAndCommit(routeDb, RoutesNameEnum.TRANSPORT, camelRouteStatisticTable.get(RoutesNameEnum.TRANSPORT));
	    RoutesDatabaseManager.putAndCommit(routeDb, RoutesNameEnum.LOCAL_ADDRESS_ERROR, camelRouteStatisticTable.get(RoutesNameEnum.LOCAL_ADDRESS_ERROR));
	    RoutesDatabaseManager.putAndCommit(routeDb, RoutesNameEnum.RELAY_DENIED, camelRouteStatisticTable.get(RoutesNameEnum.RELAY_DENIED));
	    RoutesDatabaseManager.putAndCommit(routeDb, RoutesNameEnum.ROOT, camelRouteStatisticTable.get(RoutesNameEnum.ROOT));	    
	}

	
	public void setActiveMQclient(ActiveMQclient activeMQclient) {
		this.activeMQclient = activeMQclient;
	}

	public void setCamelClient(CamelClient camelClient) {
		this.camelClient = camelClient;
	}
}
