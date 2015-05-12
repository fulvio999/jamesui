
package org.apache.james.jamesui.backend.client.jmx;

import java.util.Hashtable;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import org.apache.camel.api.management.mbean.ManagedRouteMBean;
import org.apache.james.jamesui.backend.bean.statistics.CamelRouteStatisticBean;
import org.apache.james.jamesui.backend.constant.CamelJmxObjectNameEnum;
import org.apache.james.jamesui.backend.constant.RoutesNameEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client used to invoke JMX operations of Apache Camel used by James
 *
 */
public class CamelClient {		

	private final static Logger LOG = LoggerFactory.getLogger(CamelClient.class);	

	/**
	 * Constructor
	 */
	public CamelClient() {
		
	}

	
	/**
	 * Query all the Camel routes 
	 * @return A list of CamelRouteStatisticBean (one for each queue) 
	 */
	public Hashtable<RoutesNameEnum, CamelRouteStatisticBean> queryAllRoutes(){
		
		//key is the route name
		Hashtable<RoutesNameEnum, CamelRouteStatisticBean> fullRoutStatHashTable = new Hashtable<RoutesNameEnum, CamelRouteStatisticBean>();	
		
		fullRoutStatHashTable.put(RoutesNameEnum.SPAM, this.querySpamRoute() );
		fullRoutStatHashTable.put(RoutesNameEnum.BOUNCES, this.queryBouncesRoute());
		fullRoutStatHashTable.put(RoutesNameEnum.ERROR, this.queryErrorRoute());
		fullRoutStatHashTable.put(RoutesNameEnum.TRANSPORT, this.queryTransportRoute());
		fullRoutStatHashTable.put(RoutesNameEnum.LOCAL_ADDRESS_ERROR, this.queryLocalAddressErrorRoute());
		fullRoutStatHashTable.put(RoutesNameEnum.RELAY_DENIED, this.queryRelayDeniedRoute());
		fullRoutStatHashTable.put(RoutesNameEnum.ROOT, this.queryRootRoute());
		
		return fullRoutStatHashTable;		
	}
	
	/**
	 * Query the Camel route named "spam" (the one used when an email is considered spam)
	 * @return CamelRouteStatisticBean A bean with the retrieved data from the spam route
	 */
	public CamelRouteStatisticBean querySpamRoute(){
		
		 CamelRouteStatisticBean camelRouteStatisticBean = new CamelRouteStatisticBean();		 
		
		 try{ 
	         MBeanServerConnection mBeanServerConnection = JmxServerConnectionManager.getJmxServerConnection();
		    
	         ObjectName objectName = new ObjectName( CamelJmxObjectNameEnum.SPAM_ROUTE.getObjectName() ); 
	         ManagedRouteMBean spamRouteProxy;  spamRouteProxy = (ManagedRouteMBean) JMX.newMBeanProxy(mBeanServerConnection, objectName, org.apache.camel.api.management.mbean.ManagedRouteMBean.class);
	         
	         String routeState = spamRouteProxy.getState();        
	         long exchangesCompleted = spamRouteProxy.getExchangesCompleted();
	         long exchangesFailed = spamRouteProxy.getExchangesFailed();
	         long exchangesTotal = spamRouteProxy.getExchangesTotal();
	         
	         long minProcessingTime = spamRouteProxy.getMinProcessingTime();
	         long maxProcessingTime = spamRouteProxy.getMaxProcessingTime();
	         long meanProcessingTime = spamRouteProxy.getMeanProcessingTime();
	         
	         LOG.trace("----- SPAM Route informations: --------");         
	         LOG.trace("Route state: "+routeState); 
	         LOG.trace("Exchanges completed: "+exchangesCompleted);
	         LOG.trace("Exchanges failed: "+exchangesFailed);
	         LOG.trace("Exchanges total: "+exchangesTotal);
	         
	         
	         LOG.trace("Min processing time: "+minProcessingTime);
	         LOG.trace("Max processing time: "+maxProcessingTime);
	         LOG.trace("Mean processing time : "+meanProcessingTime);
	         LOG.trace("----------------------------------------");  
	         
	         camelRouteStatisticBean.setRouteName("spam");
		     camelRouteStatisticBean.setRouteState(routeState);
		     camelRouteStatisticBean.setExchangesCompleted(exchangesCompleted);
		     camelRouteStatisticBean.setExchangesFailed(exchangesFailed);
		     camelRouteStatisticBean.setExchangesTotal(exchangesTotal);
		     camelRouteStatisticBean.setMinProcessingTime(minProcessingTime);
		     camelRouteStatisticBean.setMaxProcessingTime(maxProcessingTime);
		     camelRouteStatisticBean.setMeanProcessingTime(meanProcessingTime);		
			    
		  }catch(Exception e){    		 
			  LOG.error("Error querying Camel Spam Route, cause: ",e);
      		 
      	 }finally{
      		JmxServerConnectionManager.closeJmxServerConnection(); 
      	 }
		 
		return camelRouteStatisticBean;
	}
	
	/**
	 * Query the Camel route named "bounces" 
	 * @return CamelRouteStatisticBean A bean with the retrieved data from the Bounce route 
	 */
	public CamelRouteStatisticBean queryBouncesRoute(){
		
		CamelRouteStatisticBean camelRouteStatisticBean = new CamelRouteStatisticBean();
		
		try{
	            MBeanServerConnection mBeanServerConnection =  JmxServerConnectionManager.getJmxServerConnection();
	            ObjectName objectName = new ObjectName( CamelJmxObjectNameEnum.BOUCES_ROUTE.getObjectName() ); 
	            
	            ManagedRouteMBean bouncesRouteProxy = (ManagedRouteMBean) JMX.newMBeanProxy(mBeanServerConnection, objectName, org.apache.camel.api.management.mbean.ManagedRouteMBean.class);
	            
		        String routeState = bouncesRouteProxy.getState();        
		        long exchangesCompleted = bouncesRouteProxy.getExchangesCompleted();
		        long exchangesFailed = bouncesRouteProxy.getExchangesFailed();
		        long exchangesTotal = bouncesRouteProxy.getExchangesTotal();
		        
		        long minProcessingTime = bouncesRouteProxy.getMinProcessingTime();
		        long maxProcessingTime = bouncesRouteProxy.getMaxProcessingTime();
		        long meanProcessingTime = bouncesRouteProxy.getMeanProcessingTime();
		        
		        LOG.trace("----- Bounces Route informations: --------");         
		        LOG.trace("Route state: "+routeState); 
		        LOG.trace("Exchanges completed: "+exchangesCompleted);
		        LOG.trace("Exchanges failed: "+exchangesFailed);
		        LOG.trace("Exchanges total: "+exchangesTotal);        
		        
		        LOG.trace("Min processing time: "+minProcessingTime);
		        LOG.trace("Max processing time: "+maxProcessingTime);
		        LOG.trace("Mean processing time : "+meanProcessingTime);
		        LOG.trace("----------------------------------------");  
		        
		        camelRouteStatisticBean.setRouteName("Bounces");
		        camelRouteStatisticBean.setRouteState(routeState);
		        camelRouteStatisticBean.setExchangesCompleted(exchangesCompleted);
		        camelRouteStatisticBean.setExchangesFailed(exchangesFailed);
		        camelRouteStatisticBean.setExchangesTotal(exchangesTotal);
		        camelRouteStatisticBean.setMinProcessingTime(minProcessingTime);
		        camelRouteStatisticBean.setMaxProcessingTime(maxProcessingTime);
		        camelRouteStatisticBean.setMeanProcessingTime(meanProcessingTime);		        
	        
		  }catch(Exception e){    		 
			  LOG.error("Error querying Camel Bounces Route, cause: ",e);
	      		 
	      }finally{
	      	 JmxServerConnectionManager.closeJmxServerConnection(); 
	      }
		
		return camelRouteStatisticBean;	
	 }
	
	
	/**
	 * Query the Camel route named "Error" 
	 * @return CamelRouteStatisticBean A bean with the retrieved data from the Error route 
	 */
	public CamelRouteStatisticBean queryErrorRoute(){
		
		  CamelRouteStatisticBean camelRouteStatisticBean = new CamelRouteStatisticBean();
		
		  try{
	            MBeanServerConnection mBeanServerConnection =  JmxServerConnectionManager.getJmxServerConnection();
	            ObjectName objectName = new ObjectName( CamelJmxObjectNameEnum.ERROR_ROUTE.getObjectName() ); 
		
	            ManagedRouteMBean errorRouteProxy = (ManagedRouteMBean) JMX.newMBeanProxy(mBeanServerConnection, objectName, org.apache.camel.api.management.mbean.ManagedRouteMBean.class);
	            
		        String routeState = errorRouteProxy.getState();        
		        long exchangesCompleted = errorRouteProxy.getExchangesCompleted();
		        long exchangesFailed = errorRouteProxy.getExchangesFailed();
		        long exchangesTotal = errorRouteProxy.getExchangesTotal();
		        
		        long minProcessingTime = errorRouteProxy.getMinProcessingTime();
		        long maxProcessingTime = errorRouteProxy.getMaxProcessingTime();
		        long meanProcessingTime = errorRouteProxy.getMeanProcessingTime();
		        
		        LOG.trace("----- Error Route informations: --------");         
		        LOG.trace("Route state: "+routeState); 
		        LOG.trace("Exchanges completed: "+exchangesCompleted);
		        LOG.trace("Exchanges failed: "+exchangesFailed);
		        LOG.trace("Exchanges total: "+exchangesTotal);        
		        
		        LOG.trace("Min processing time: "+minProcessingTime);
		        LOG.trace("Max processing time: "+maxProcessingTime);
		        LOG.trace("Mean processing time : "+meanProcessingTime);
		        LOG.trace("----------------------------------------");  
		        
		        camelRouteStatisticBean.setRouteName("Error");
		        camelRouteStatisticBean.setRouteState(routeState);
		        camelRouteStatisticBean.setExchangesCompleted(exchangesCompleted);
		        camelRouteStatisticBean.setExchangesFailed(exchangesFailed);
		        camelRouteStatisticBean.setExchangesTotal(exchangesTotal);
		        camelRouteStatisticBean.setMinProcessingTime(minProcessingTime);
		        camelRouteStatisticBean.setMaxProcessingTime(maxProcessingTime);
		        camelRouteStatisticBean.setMeanProcessingTime(meanProcessingTime);
	        
   		  }catch(Exception e){    		 
	    	  LOG.error("Error querying Camel Error Route, cause: ",e);
	    	      		 
	      }finally{
	      	  JmxServerConnectionManager.closeJmxServerConnection(); 
	      }	
		  
		  return camelRouteStatisticBean;
	}
	
	/**
	 * Query the Camel route named "Transport" 
	 * @return CamelRouteStatisticBean A bean with the retrieved data from the Transport route 
	 */
	public CamelRouteStatisticBean queryTransportRoute(){
		
		  CamelRouteStatisticBean camelRouteStatisticBean = new CamelRouteStatisticBean();
		
		  try{
	            MBeanServerConnection mBeanServerConnection =  JmxServerConnectionManager.getJmxServerConnection();
	            ObjectName objectName = new ObjectName( CamelJmxObjectNameEnum.TRANSPORT_ROUTE.getObjectName() ); 
	            
	            ManagedRouteMBean transportRouteProxy = (ManagedRouteMBean) JMX.newMBeanProxy(mBeanServerConnection, objectName, org.apache.camel.api.management.mbean.ManagedRouteMBean.class);
	            
		  		String routeState = transportRouteProxy.getState();        
		        long exchangesCompleted = transportRouteProxy.getExchangesCompleted();
		        long exchangesFailed = transportRouteProxy.getExchangesFailed();
		        long exchangesTotal = transportRouteProxy.getExchangesTotal();
		        
		        long minProcessingTime = transportRouteProxy.getMinProcessingTime();
		        long maxProcessingTime = transportRouteProxy.getMaxProcessingTime();
		        long meanProcessingTime = transportRouteProxy.getMeanProcessingTime();
	        
		        LOG.trace("----- TRANSPORT Route informations: --------");         
		        LOG.trace("Route state: "+routeState); 
		        LOG.trace("Exchanges completed: "+exchangesCompleted);
		        LOG.trace("Exchanges failed: "+exchangesFailed);
		        LOG.trace("Exchanges total: "+exchangesTotal);          
		        
		        LOG.trace("Min processing time: "+minProcessingTime);
		        LOG.trace("Max processing time: "+maxProcessingTime);
		        LOG.trace("Mean processing time : "+meanProcessingTime);
		        LOG.trace("----------------------------------------");  	
		        
		        camelRouteStatisticBean.setRouteName("Transport");
		        camelRouteStatisticBean.setRouteState(routeState);
		        camelRouteStatisticBean.setExchangesCompleted(exchangesCompleted);
		        camelRouteStatisticBean.setExchangesFailed(exchangesFailed);
		        camelRouteStatisticBean.setExchangesTotal(exchangesTotal);
		        camelRouteStatisticBean.setMinProcessingTime(minProcessingTime);
		        camelRouteStatisticBean.setMaxProcessingTime(maxProcessingTime);
		        camelRouteStatisticBean.setMeanProcessingTime(meanProcessingTime);
			  
		  }catch(Exception e){    		 
	    	  LOG.error("Error querying Camel Transport route, cause: ",e);
	    	      		 
	      }finally{
	      	  JmxServerConnectionManager.closeJmxServerConnection(); 
	      }	
		  
		  return camelRouteStatisticBean;
	 }
	
	/**
	 * Query the Camel route named "local-address-error" 
	 * @return CamelRouteStatisticBean A bean with the retrieved data from the local-address-error route 
	 */
	public CamelRouteStatisticBean queryLocalAddressErrorRoute() {
		
		  CamelRouteStatisticBean camelRouteStatisticBean = new CamelRouteStatisticBean();
		
		  try{			  
	            MBeanServerConnection mBeanServerConnection =  JmxServerConnectionManager.getJmxServerConnection(); 
	            ObjectName objectName = new ObjectName( CamelJmxObjectNameEnum.LOCAL_ADDRESS_ERROR.getObjectName() ); 
	            
	            ManagedRouteMBean localAddressErrorRouteProxy = (ManagedRouteMBean) JMX.newMBeanProxy(mBeanServerConnection, objectName, org.apache.camel.api.management.mbean.ManagedRouteMBean.class);

	            String routeState = localAddressErrorRouteProxy.getState();        
	            long exchangesCompleted = localAddressErrorRouteProxy.getExchangesCompleted();
	            long exchangesFailed = localAddressErrorRouteProxy.getExchangesFailed();
	            long exchangesTotal = localAddressErrorRouteProxy.getExchangesTotal();
	            
	            long minProcessingTime = localAddressErrorRouteProxy.getMinProcessingTime();
	            long maxProcessingTime = localAddressErrorRouteProxy.getMaxProcessingTime();
	            long meanProcessingTime = localAddressErrorRouteProxy.getMeanProcessingTime();
	            
	            LOG.trace("----- Local-address-error Route informations: --------");         
	            LOG.trace("Route state: "+routeState); 
	            LOG.trace("Exchanges completed: "+exchangesCompleted);
	            LOG.trace("Exchanges failed: "+exchangesFailed);
	            LOG.trace("Exchanges total: "+exchangesTotal);        
	            
	            LOG.trace("Min processing time: "+minProcessingTime);
	            LOG.trace("Max processing time: "+maxProcessingTime);
	            LOG.trace("Mean processing time : "+meanProcessingTime);
	            LOG.trace("----------------------------------------");  
	            
	            camelRouteStatisticBean.setRouteName("local-address-error");
		        camelRouteStatisticBean.setRouteState(routeState);
		        camelRouteStatisticBean.setExchangesCompleted(exchangesCompleted);
		        camelRouteStatisticBean.setExchangesFailed(exchangesFailed);
		        camelRouteStatisticBean.setExchangesTotal(exchangesTotal);
		        camelRouteStatisticBean.setMinProcessingTime(minProcessingTime);
		        camelRouteStatisticBean.setMaxProcessingTime(maxProcessingTime);
		        camelRouteStatisticBean.setMeanProcessingTime(meanProcessingTime);
			  
		  }catch(Exception e){    		 
	    	  LOG.error("Error querying Camel Local-address-error route, cause: ",e);
	    	      		 
	      }finally{
	      	  JmxServerConnectionManager.closeJmxServerConnection(); 
	      }	
		  
		  return camelRouteStatisticBean;
	  }
	
	/**
	 * Query the Camel route named "relay-denied" 
	 * @return CamelRouteStatisticBean A bean with the retrieved data from the relay-denied route
	 */
	public CamelRouteStatisticBean queryRelayDeniedRoute(){
		
		 CamelRouteStatisticBean camelRouteStatisticBean = new CamelRouteStatisticBean();
		 
		 try{
			 
	        MBeanServerConnection mBeanServerConnection =  JmxServerConnectionManager.getJmxServerConnection(); 
	        ObjectName objectName = new ObjectName( CamelJmxObjectNameEnum.RELAY_DENIED_ERROR.getObjectName() ); 
	            
	        ManagedRouteMBean relayDeniedRouteProxy = (ManagedRouteMBean) JMX.newMBeanProxy(mBeanServerConnection, objectName, org.apache.camel.api.management.mbean.ManagedRouteMBean.class);
	        
	        String routeState = relayDeniedRouteProxy.getState();        
	        long exchangesCompleted = relayDeniedRouteProxy.getExchangesCompleted();
	        long exchangesFailed = relayDeniedRouteProxy.getExchangesFailed();
	        long exchangesTotal = relayDeniedRouteProxy.getExchangesTotal();
	        
	        long minProcessingTime = relayDeniedRouteProxy.getMinProcessingTime();
	        long maxProcessingTime = relayDeniedRouteProxy.getMaxProcessingTime();
	        long meanProcessingTime = relayDeniedRouteProxy.getMeanProcessingTime();
	        
	        LOG.trace("----- relay-denied Route informations: --------");         
	        LOG.trace("Route state: "+routeState); 
	        LOG.trace("Exchanges completed: "+exchangesCompleted);
	        LOG.trace("Exchanges failed: "+exchangesFailed);
	        LOG.trace("Exchanges total: "+exchangesTotal);        
	        
	        LOG.trace("Min processing time: "+minProcessingTime);
	        LOG.trace("Max processing time: "+maxProcessingTime);
	        LOG.trace("Mean processing time : "+meanProcessingTime);
	        LOG.trace("----------------------------------------");    
	        
	        camelRouteStatisticBean.setRouteName("relay-denied");
	        camelRouteStatisticBean.setRouteState(routeState);
	        camelRouteStatisticBean.setExchangesCompleted(exchangesCompleted);
	        camelRouteStatisticBean.setExchangesFailed(exchangesFailed);
	        camelRouteStatisticBean.setExchangesTotal(exchangesTotal);
	        camelRouteStatisticBean.setMinProcessingTime(minProcessingTime);
	        camelRouteStatisticBean.setMaxProcessingTime(maxProcessingTime);
	        camelRouteStatisticBean.setMeanProcessingTime(meanProcessingTime);
			  
		  }catch(Exception e){    		 
	    	  LOG.error("Error querying Camel relay-denied route, cause: ",e);
	    	      		 
	      }finally{
	      	  JmxServerConnectionManager.closeJmxServerConnection(); 
	      }
		return camelRouteStatisticBean;		
	}
	
	
	/**
	 * Query the Camel route named "root" 
	 * @return CamelRouteStatisticBean A bean with the retrieved data from the root route
	 */
	public CamelRouteStatisticBean queryRootRoute(){
		
		  CamelRouteStatisticBean camelRouteStatisticBean = new CamelRouteStatisticBean();
		  
		  try{			 
		        MBeanServerConnection mBeanServerConnection =  JmxServerConnectionManager.getJmxServerConnection(); 
		        ObjectName objectName = new ObjectName( CamelJmxObjectNameEnum.ROOT_ROUTE.getObjectName() ); 
		            
		        ManagedRouteMBean rootRouteProxy = (ManagedRouteMBean) JMX.newMBeanProxy(mBeanServerConnection, objectName, org.apache.camel.api.management.mbean.ManagedRouteMBean.class);
	
		        String routeState = rootRouteProxy.getState();        
		        long exchangesCompleted = rootRouteProxy.getExchangesCompleted();
		        long exchangesFailed = rootRouteProxy.getExchangesFailed();
		        long exchangesTotal = rootRouteProxy.getExchangesTotal();
		        
		        long minProcessingTime = rootRouteProxy.getMinProcessingTime();
		        long maxProcessingTime = rootRouteProxy.getMaxProcessingTime();
		        long meanProcessingTime = rootRouteProxy.getMeanProcessingTime();
		        
		        LOG.trace("----- Root Route informations: --------");         
		        LOG.trace("Route state: "+routeState); 
		        LOG.trace("Exchanges completed: "+exchangesCompleted);
		        LOG.trace("Exchanges failed: "+exchangesFailed);
		        LOG.trace("Exchanges total: "+exchangesTotal);        
		        
		        LOG.trace("Min processing time: "+minProcessingTime);
		        LOG.trace("Max processing time: "+maxProcessingTime);
		        LOG.trace("Mean processing time : "+meanProcessingTime);
		        LOG.trace("----------------------------------------");   
		        
		        camelRouteStatisticBean.setRouteName("root");
		        camelRouteStatisticBean.setRouteState(routeState);
		        camelRouteStatisticBean.setExchangesCompleted(exchangesCompleted);
		        camelRouteStatisticBean.setExchangesFailed(exchangesFailed);
		        camelRouteStatisticBean.setExchangesTotal(exchangesTotal);
		        camelRouteStatisticBean.setMinProcessingTime(minProcessingTime);
		        camelRouteStatisticBean.setMaxProcessingTime(maxProcessingTime);
		        camelRouteStatisticBean.setMeanProcessingTime(meanProcessingTime);
		     
		  }catch(Exception e){    		 
	    	  LOG.error("Error querying Camel Root route, cause: ",e);
	    	      		 
	      }finally{
	      	  JmxServerConnectionManager.closeJmxServerConnection(); 
	      }
		  
		return camelRouteStatisticBean;		
	}

}
