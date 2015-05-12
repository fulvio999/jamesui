
package org.apache.james.jamesui.frontend.statistic.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentNavigableMap;

import org.apache.james.jamesui.backend.bean.statistics.StatisticQueueBean;
import org.apache.james.jamesui.backend.constant.QueueNameEnum;
import org.apache.james.jamesui.backend.constant.QueueParamEnum;
import org.apache.james.jamesui.backend.database.manager.QueuesDatabaseManager;
import org.apache.james.jamesui.frontend.JamesUI;
import org.mapdb.DB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class with the methods necessary to manage/manipulate the data-set of the chart in
 * "History statistic panel" about ActiveMQ queue statistics.
 * 
 * @author fulvio
 *
 */
public class QueueChartDataManager {
		
	private final static Logger LOG = LoggerFactory.getLogger(QueueChartDataManager.class);
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * Constructor
	 */
	public QueueChartDataManager() {
		
	}	
	
	/**
	 * Extract the X values (ie: the key of the Map DB collection) for the given queue
	 * @param routeDB
	 * @param routeName
	 * @param routeParam
	 * @return A list of X values
	 */
	public static List<String> extractQueueXvalues(DB routeDB, QueueNameEnum queueName){
		
		ConcurrentNavigableMap<Date, StatisticQueueBean> transportRouteValues = QueuesDatabaseManager.getCollectionValues(routeDB, queueName);
		Iterator<Entry<Date, StatisticQueueBean>> iterator = transportRouteValues.entrySet().iterator();
		
		Entry<Date, StatisticQueueBean> entry = null;
		List<String> x_values = new ArrayList<String>();
		   
		while(iterator.hasNext()) 
		{
		   //eg key entry: Wed Apr 22 11:10:24 CEST 2015
		   entry = iterator.next();		   
		   x_values.add(sdf.format(entry.getKey()));
		}
		   
		return x_values;		 
	}
	
	/**
	 * Return the Y chart values for the in argument Route name an Route parameter 
	 * 
	 * @param routeDB The MapDB where extract the data for the Route name and Route parameter in argument 
	 * @param routeName
	 * @param routeParam
	 * @return
	 */
	public static List<Long> extractQueueYvalues(DB queueDB, QueueNameEnum queueName, QueueParamEnum queueParam) {
		
		 /* Note: a ConcurrentNavigableMap is a Map that further provides a total ordering on its keys.
		    The map is ordered according to the natural ordering of its keys: our key is a java Date, so that the lowest key is the the first inserted value
		 */
		 ConcurrentNavigableMap<Date, StatisticQueueBean> transportRouteValues = QueuesDatabaseManager.getCollectionValues(queueDB, queueName);
		 Iterator<Entry<Date, StatisticQueueBean>> iterator = transportRouteValues.entrySet().iterator();
			  
		 Entry<Date, StatisticQueueBean> entry = null;
		 List<Long> values = new ArrayList<Long>();
			   
		 while(iterator.hasNext()) 
		 {
		    entry = iterator.next();
		    //extract only a specific Parameter (ie: a user defined Queue Parameter) from all the statistic bean
 			values.add(getQueueParam(queueParam,entry.getValue()));			
		}
			   
		return values;		
	}
	
	/**
	 * Utility method that return the right Queue parameter value according with the QueueParamEnum in argument
	 * @param queueParam The value to decode
	 * @param cb The bean where extract the right parameter value to show on Y axis
	 * @return A parameter value of the StatisticQueueBean
	 */
	private static Long getQueueParam(QueueParamEnum queueParam, StatisticQueueBean sb){
		
		 if(queueParam.name().equalsIgnoreCase("SIZE"))
			 return sb.getSize();
		 
		 else if(queueParam.name().equalsIgnoreCase("CONSUMER_COUNT"))
			 return sb.getConsumerCount();
		 
		 else if(queueParam.name().equalsIgnoreCase("ENQUEUE_COUNT"))
			 return sb.getEnqueueCount();
		 
		 else{
			 LOG.error("Unknown Queue Param name: "+queueParam.name());
			 return 0L;
		 } 		
	}

}
