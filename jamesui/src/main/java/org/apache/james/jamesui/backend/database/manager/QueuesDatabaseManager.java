package org.apache.james.jamesui.backend.database.manager;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentNavigableMap;

import org.apache.james.jamesui.backend.bean.statistics.StatisticQueueBean;
import org.apache.james.jamesui.backend.constant.QueueNameEnum;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MapDB database manager containing ActiveMQ queue statistic data
 * 
 * @author fulvio
 *
 */
public class QueuesDatabaseManager {	

	private final static Logger LOG = LoggerFactory.getLogger(QueuesDatabaseManager.class);
	
	private static String QUEUE_DB_METADATA_NAME = "QUEUE_DB_METADATA";	 
	
	/**
	 * Create a new MapDb with the provided path/name if not exist. Otherwise return a reference to it.
	 * @param dbPath
	 * @param dbName	
	 * @return the new created DB
	 */
	public static DB getQueueDataBase(String dbPath, String dbName){
		
		File databaseFile = new File(dbPath+File.separator+dbName);		
		DB routesDB = DBMaker.newFileDB(databaseFile).make();
		
		return routesDB;
	}
		
	
	/** 
	 * Create a structure that contains some meta data informations 
	 * (ie: the time start/stop when the the stastistic was taken, the polling frequency used to monitor James server..)
	 * 
	 * @param db
	 * @param startTime
	 * @param endTime
	 * @param pollingFreq
	 */
	public static void createStatsMetaData(DB db, String startTime, String endTime, String pollingFreq){
		
		ConcurrentNavigableMap<Integer, String> map = db.getTreeMap(QUEUE_DB_METADATA_NAME); 
	    map.put(1, startTime);
	    map.put(2, endTime);
	    map.put(3, pollingFreq);
	    db.commit();  //persist changes into disk			
	}
	
	/**
	 * Print the values in the matadata collection
	 * @param db
	 */
    public static void printMetadataCollectionValues(DB db){
		
    	 ConcurrentNavigableMap<Integer, String> map = db.getTreeMap(QUEUE_DB_METADATA_NAME);		 
		 Iterator<Entry<Integer, String>> it = map.entrySet().iterator();
		    
		 while (it.hasNext())
		 {
		   Entry<Integer, String> v = it.next();
		   System.out.println("- metadata_value: "+ v.getValue().toString());		  
		}		
	}
	
	/**
	 * Return the Metadata information for the route DB
	 * @param db
	 * @return
	 */
	public static ConcurrentNavigableMap<Integer, String> getStatsMetaData(DB db){
		return db.getTreeMap(QUEUE_DB_METADATA_NAME); 
	}
	
	
	/**
	 * Return the values contained in the collection 
	 * @param collectionName the collection name to return
	 * @param db the database
	 * @return
	 */
	public static ConcurrentNavigableMap<Date, StatisticQueueBean> getCollectionValues(DB db, QueueNameEnum collectionName){
		return db.getTreeMap(collectionName.name());		
	}
	
	
	/**
	 * Save in the database a new value in the provided collection name
	 * @param value The new value to put
	 * @param db the database
	 * @param collectionName The collection name where save the value
	 */
	public static void putAndCommit(DB db, QueueNameEnum collectionName, StatisticQueueBean qb){
		
		ConcurrentNavigableMap<Date, StatisticQueueBean> map = db.getTreeMap(collectionName.name()); 
	    map.put(getCurrentDate(), qb);
	    db.commit();  //persist changes into disk	
	}
	
	
	/** 
	 * Print on System.out all the values contained ALL the collection of Route	
	 * @param db
	 */
	public static void printAllCollectionValues(DB db){
		
		QueueNameEnum[] queue = QueueNameEnum.values();
		
		for(int i=0; i<queue.length; i++)
		{
		   //System.out.println("Route:"+routes[i]);
		   printCollectionValues(db, queue[i]);
		}
	}
	
	/**
	 * Print on System.out all the values contained in the collection whose name is in argument
	 * @param collectionName
	 */
	public static void printCollectionValues(DB db, QueueNameEnum collectionName){
		
	     ConcurrentNavigableMap<Date,StatisticQueueBean> map = db.getTreeMap(collectionName.name());		 
		 Iterator<Entry<Date, StatisticQueueBean>> it = map.entrySet().iterator();
		 System.out.println("Queue name: "+collectionName+", size: "+map.size());   
		 
		 while (it.hasNext())
		 {
		   Entry<Date, StatisticQueueBean> v = it.next();
		   System.out.println("key: "+v.getKey()+" value:"+ v.getValue().toString());		  
		}		
	}
	
	/**
	 * Print to stdout some informations/statistics (eg. size, free size...) for the DB in argument 
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
