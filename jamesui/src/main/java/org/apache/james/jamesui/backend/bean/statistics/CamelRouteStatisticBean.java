
package org.apache.james.jamesui.backend.bean.statistics;

import java.io.Serializable;

/**
 * Contains the data about Camel routes statistics. That data are retrieved using CamelClient class
 * The informations are about the Camel Routes:
 * 
 * - spam 
 * - bounce 	        
 * - error 	        
 * - transport 	
 * - local-address-error    	
 * - relay-denied 
 * - root 
 *   
 * @author fulvio
 *
 */
public class CamelRouteStatisticBean implements Serializable{
	   
	private static final long serialVersionUID = 6546162577658256662L;
	
	/* the name of the Camel route that own the statistics */
	private String routeName;	
	
	private String routeState;
	private long exchangesCompleted;
	private long exchangesFailed;
	private long exchangesTotal;
	
     
	private long minProcessingTime;
	private long maxProcessingTime;
	private long meanProcessingTime;
    

	/**
	 * Constructor
	 */
	public CamelRouteStatisticBean() {
		
	}


	public String getRouteName() {
		return routeName;
	}


	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}


	public long getExchangesFailed() {
		return exchangesFailed;
	}


	public void setExchangesFailed(long exchangesFailed) {
		this.exchangesFailed = exchangesFailed;
	}


	public long getExchangesTotal() {
		return exchangesTotal;
	}


	public void setExchangesTotal(long exchangesTotal) {
		this.exchangesTotal = exchangesTotal;
	}


	public long getMinProcessingTime() {
		return minProcessingTime;
	}


	public void setMinProcessingTime(long minProcessingTime) {
		this.minProcessingTime = minProcessingTime;
	}


	public long getMaxProcessingTime() {
		return maxProcessingTime;
	}


	public void setMaxProcessingTime(long maxProcessingTime) {
		this.maxProcessingTime = maxProcessingTime;
	}


	public long getMeanProcessingTime() {
		return meanProcessingTime;
	}


	public void setMeanProcessingTime(long meanProcessingTime) {
		this.meanProcessingTime = meanProcessingTime;
	}


	public String getRouteState() {
		return routeState;
	}


	public void setRouteState(String routeState) {
		this.routeState = routeState;
	}


	public long getExchangesCompleted() {
		return exchangesCompleted;
	}


	public void setExchangesCompleted(long exchangesCompleted) {
		this.exchangesCompleted = exchangesCompleted;
	}


	@Override
	public String toString() {
		return "CamelRouteStatisticBean [routeName=" + routeName
				+ ", routeState=" + routeState + ", exchangesCompleted="
				+ exchangesCompleted + ", exchangesFailed=" + exchangesFailed
				+ ", exchangesTotal=" + exchangesTotal + ", minProcessingTime="
				+ minProcessingTime + ", maxProcessingTime="
				+ maxProcessingTime + ", meanProcessingTime="
				+ meanProcessingTime + "]";
	}


}
