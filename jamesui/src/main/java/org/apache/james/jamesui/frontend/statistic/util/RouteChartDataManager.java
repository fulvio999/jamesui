package org.apache.james.jamesui.frontend.statistic.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentNavigableMap;

import org.apache.james.jamesui.backend.bean.statistics.CamelRouteStatisticBean;
import org.apache.james.jamesui.backend.constant.RoutesNameEnum;
import org.apache.james.jamesui.backend.constant.RoutesParamEnum;
import org.apache.james.jamesui.backend.database.manager.RoutesDatabaseManager;
import org.apache.james.jamesui.frontend.JamesUI;
import org.mapdb.DB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class with the methods necessary to manage/manipulate the data-set of the chart in
 * "History statistic panel" about Camel route statistics.
 * 
 * @author fulvio
 *
 */
public class RouteChartDataManager {
		
	private final static Logger LOG = LoggerFactory.getLogger(RouteChartDataManager.class);
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * Constructor
	 */
	public RouteChartDataManager() {
	
	}
	
	/**
	 * Extract the X values from the database (ie: the key of the Map DB collection) for the given route
	 * @param routeDB
	 * @param routeName
	 * @param routeParam
	 * @return A list of X values
	 */
	public static List<String> extractRouteXvalues(DB routeDB, RoutesNameEnum routeName){
		
		ConcurrentNavigableMap<Date, CamelRouteStatisticBean> transportRouteValues = RoutesDatabaseManager.getCollectionValues(routeDB, routeName);
		Iterator<Entry<Date, CamelRouteStatisticBean>> iterator = transportRouteValues.entrySet().iterator();
		
		Entry<Date, CamelRouteStatisticBean> entry = null;
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
	 * Return the Y chart values (from the database) for the in argument Route name an Route parameter 
	 * 
	 * @param routeDB The MapDB where extract the data for the Route name and Route parameter in argument 
	 * @param routeName
	 * @param routeParam
	 * @return
	 */
	public static List<Long> extractRouteYvalues(DB routeDB, RoutesNameEnum routeName, RoutesParamEnum routeParam) {
		
		 /* Note: a ConcurrentNavigableMap is a Map that further provides a total ordering on its keys.
		    The map is ordered according to the natural ordering of its keys: our key is a java Date, so that the lowest key is the the first inserted value
		 */
		 ConcurrentNavigableMap<Date, CamelRouteStatisticBean> transportRouteValues = RoutesDatabaseManager.getCollectionValues(routeDB, routeName);
		 Iterator<Entry<Date, CamelRouteStatisticBean>> iterator = transportRouteValues.entrySet().iterator();
			  
		 Entry<Date, CamelRouteStatisticBean> entry = null;
		 List<Long> values = new ArrayList<Long>();
			   
		 while(iterator.hasNext()) 
		 {
		    entry = iterator.next();
		    //extract only a specific Parameter (ie: a user defined Routes Parameter) from all the statistic bean
 			values.add(getRouteParam(routeParam,entry.getValue()));			
		}
			   
		return values;		
	}
	
	/**
	 * Utility method that return the right Route parameter value according with the RoutesParamEnum in argument
	 * @param routeParam The value to decode
	 * @param cb The bean where extract the right parameter value to show on Y axis
	 * @return
	 */
	private static Long getRouteParam(RoutesParamEnum routeParam, CamelRouteStatisticBean cb){
		
		 if(routeParam.name().equalsIgnoreCase("EXACHANGES_COMPLETED"))
			 return cb.getExchangesCompleted();
		 
		 else if(routeParam.name().equalsIgnoreCase("EXCHANGES_FAILED"))
			 return cb.getExchangesFailed();
		 
		 else if(routeParam.name().equalsIgnoreCase("EXCHANGES_TOTAL"))
			 return cb.getExchangesTotal();
		 
		 else if(routeParam.name().equalsIgnoreCase("MIN_PROCESSING_TIME"))
			 return cb.getMinProcessingTime();
		 
		 else if(routeParam.name().equalsIgnoreCase("MAX_PROCESSING_TIME"))
			 return cb.getMaxProcessingTime();
		 
		 else if(routeParam.name().equalsIgnoreCase("MEAN_PROCESSING_TIME"))
			 return cb.getMeanProcessingTime();
		 
		 else{
			 LOG.error("Unknown Routes Param name: "+routeParam.name());
			 return 0L;
		 } 		
	}

}
