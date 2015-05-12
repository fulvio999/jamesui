package org.apache.james.jamesui.backend.database.manager;

import java.io.File;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentNavigableMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.james.jamesui.backend.bean.statistics.CamelRouteStatisticBean;
import org.apache.james.jamesui.backend.constant.RoutesNameEnum;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Store;

/**
 * MapDB database manager containing Camel routes statistic data
 * 
 * @author fulvio
 *
 */
public class RoutesDatabaseManager {
	
	private static final Logger LOG = LoggerFactory.getLogger(RoutesDatabaseManager.class);	
	
	private static String ROUTE_DB_METADATA_NAME = "ROUTE_DB_METADATA";	
 	
	/**
	 * Create a new MapDb with the provided path/name if not exist. Otherwise return a reference to it.
	 * @param dbPath
	 * @param dbName
	 * @return a reference at the new created DB
	 */
	public static DB getRouteDataBase(String dbPath, String dbName){
		
		File databaseFile = new File(dbPath+File.separator+dbName);		
		DB routesDB = DBMaker.newFileDB(databaseFile).make();
	
		return routesDB;
	}
	
	/**
	 * ConcurrentNavigableMap: A Map that provides a total ordering on its keys
	 * @param collectionName
	 * @param db
	 * @return The values contained in the collection in argument
	 */
	public static ConcurrentNavigableMap<Date,CamelRouteStatisticBean> getCollectionValues(DB db, RoutesNameEnum collectionName){			
		return db.getTreeMap(collectionName.name());
	}
	
	/** 
	 * Create a structure that contains some meta data informations 
	 * (ie: the time start/stop when the stastistic was taken, the polling frequency used to monitor James server..)
	 *  
	 * @param db
	 * @param startTime
	 * @param endTime
	 * @param pollingFreq
	 */
	public static void createStatsMetaData(DB db, String startTime, String endTime, String pollingFreq){
		
		ConcurrentNavigableMap<Integer, String> map = db.getTreeMap(ROUTE_DB_METADATA_NAME); 
	    map.put(1, startTime);
	    map.put(2, endTime);
	    map.put(3, pollingFreq);
	    db.commit();  //persist changes into disk			
	}
	
	
	/**
	 * Print the values in the mata data collection
	 * @param db
	 */
    public static void printMetadataCollectionValues(DB db){
		
    	 ConcurrentNavigableMap<Integer, String> map = db.getTreeMap(ROUTE_DB_METADATA_NAME);		 
		 Iterator<Entry<Integer, String>> it = map.entrySet().iterator();
		    
		 while (it.hasNext()) {
		    Entry<Integer, String> v = it.next();
		    System.out.println("- metadatavalue:"+ v.getValue().toString());		  
		 }		
	}
	
	/**
	 * Return the Metadata information for the route DB
	 * @param db
	 * @return
	 */
	public static ConcurrentNavigableMap<Integer, String> getStatsMetaData(DB db){
		return db.getTreeMap(ROUTE_DB_METADATA_NAME); 
	}
	
	
	/**
	 * Save in the provided collection database a new value
	 * @param value The new value to put
	 * @param collectionName The collection name where save the value
	 * @param db the database where add the value
	 * @throws ParseException 
	 * @throws InterruptedException 
	 */
	public static void putAndCommit(DB db, RoutesNameEnum collectionName, CamelRouteStatisticBean value) {

		ConcurrentNavigableMap<Date, CamelRouteStatisticBean> map = db.getTreeMap(collectionName.name()); 
	    map.put(getCurrentDate(), value);
	    db.commit();  //persist changes into disk	
	}
	
	/**
	 * Print on System.out all the values contained in the collection whose name is in argument
	 * @param collectionName
	 * @param db
	 */
	public static void printCollectionValues(DB db, RoutesNameEnum collectionName){
		
		 ConcurrentNavigableMap<Date,CamelRouteStatisticBean> map = db.getTreeMap(collectionName.name());		 
		 Iterator<Entry<Date, CamelRouteStatisticBean>> it = map.entrySet().iterator();
		 System.out.println("Route name: "+collectionName+", size: "+map.size());
		 
		 while (it.hasNext()) {
		   Entry<Date, CamelRouteStatisticBean> v = it.next();	
		   System.out.println("key: "+v.getKey()+" value:"+ v.getValue().toString());
		 }	
	}
	
	/** 
	 * Print on System.out all the values contained ALL the collection of Route	
	 * @param db
	 */
	public static void printAllCollectionValues(DB db){
		
		RoutesNameEnum[] routes = RoutesNameEnum.values();
		
		for(int i=0; i<routes.length; i++)
		{
		   System.out.println("---- Route:"+routes[i]);
		   printCollectionValues(db, routes[i]);
		}
	}

	
	/**
	 * Print to System.out some informations/statistics (eg. size, free size...) for the DB in argument 
	 * @param db
	 */
	public static void printDBstatistic(DB db){
		
		long cursixe = Store.forDB(db).getCurrSize();
	    System.out.println("Cur size: "+cursixe);
	    
	    long freeSize = Store.forDB(db).getFreeSize();
	    System.out.println("freeSize: "+freeSize);
	    
	    long sizeLimit = Store.forDB(db).getSizeLimit();
	    System.out.println("SizeLimit: "+sizeLimit); // -1 mean no limit	  
	}
	
	/** 
	 * Utility method
	 * @return the current Date
	 */
	private static Date getCurrentDate()
	{  	   	   
	    return Calendar.getInstance().getTime();
	}

}
