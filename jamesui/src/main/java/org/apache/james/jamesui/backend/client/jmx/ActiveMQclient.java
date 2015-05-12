package org.apache.james.jamesui.backend.client.jmx;

import java.io.Serializable;
import java.util.Hashtable;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;

import org.apache.activemq.broker.jmx.BrokerViewMBean;
import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.apache.james.jamesui.backend.bean.statistics.ActiveMQstatisticBean;
import org.apache.james.jamesui.backend.bean.statistics.StatisticQueueBean;
import org.apache.james.jamesui.backend.constant.ActiveMQJmxObjectNameEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**  
 * Client used to invoke JMX operations of ActiveMQ broker used by Apache James
 *
 */
public class ActiveMQclient implements Serializable {	

	private static final long serialVersionUID = 8444017917407812757L;	

	private final static Logger LOG = LoggerFactory.getLogger(ActiveMQclient.class);

	/**
	 * Constructor
	 */
	public ActiveMQclient() {
		
	}	
	
	/**
	 * Get some statistics informations about the the ActiveMQ broker used by Apache James
	 */
	public ActiveMQstatisticBean getJamesMQbrokerStats(){
		
		ActiveMQstatisticBean activeMQstatisticBean = new ActiveMQstatisticBean();
		Hashtable<String,StatisticQueueBean> statisticQueueTable = new Hashtable<String,StatisticQueueBean>();
		
		 try{ 
	         MBeanServerConnection mBeanServerConnection = JmxServerConnectionManager.getJmxServerConnection();
	     	 ObjectName name = new ObjectName( ActiveMQJmxObjectNameEnum.BROKER_NAME.getObjectName() ); 	    	
	    	 BrokerViewMBean mbean = (BrokerViewMBean) MBeanServerInvocationHandler.newProxyInstance(mBeanServerConnection, name, BrokerViewMBean.class, true);
	    	
	    	 LOG.trace("Statistics for broker id: " + mbean.getBrokerId()+ " - Name: " + mbean.getBrokerName());
	    	 LOG.trace("Total message count: " + mbean.getTotalMessageCount() + "\n");
	    	 LOG.trace("Total number of consumers: " + mbean.getTotalConsumerCount());
	    	 LOG.trace("Total Queues length: " + mbean.getQueues().length);
	    	 LOG.trace("\n-----------------\n");
	    	
	    	 //Broker general data
	    	 activeMQstatisticBean.setBrokerId(mbean.getBrokerId());
	    	 activeMQstatisticBean.setBrokerName(mbean.getBrokerName());
	    	 activeMQstatisticBean.setTotalMessageCount(mbean.getTotalMessageCount());
	    	 activeMQstatisticBean.setQueueLength(mbean.getQueues().length);
	    	 activeMQstatisticBean.setTotalConsumerCount(mbean.getTotalConsumerCount());	    	 
	    	
	    	 for (ObjectName queueName : mbean.getQueues()) 
			 {		
	    		StatisticQueueBean statisticQueueBean = new StatisticQueueBean();
	    		 
	    		LOG.trace("Queue canonical name:"+queueName.getCanonicalName());
	    		    		
				// Stats/info for each single queue: "spool" e "outgoing"				
				QueueViewMBean queueMbean = (QueueViewMBean) MBeanServerInvocationHandler.newProxyInstance(mBeanServerConnection, queueName, QueueViewMBean.class, true);
						
				LOG.trace("\n-----------------\n");
				LOG.trace("Statistics for queue: " + queueMbean.getName());
				LOG.trace("Size: " + queueMbean.getQueueSize());
				LOG.trace("Number of consumers: " + queueMbean.getConsumerCount());
				LOG.trace("Number of messages sent to the destination: "+queueMbean.getEnqueueCount());
									
				//Queue specific data
				statisticQueueBean.setQueueName(queueMbean.getName());
				statisticQueueBean.setSize(queueMbean.getQueueSize());
				statisticQueueBean.setConsumerCount(queueMbean.getConsumerCount());
				statisticQueueBean.setEnqueueCount(queueMbean.getEnqueueCount());
				
				statisticQueueTable.put(queueMbean.getName(), statisticQueueBean);				
			}
	         
		  }catch(Exception e){    		 
			  LOG.error("Error retrieving ActiveMQ broker stats, cause: ",e); 
	      		 
	      }finally{
	      	 JmxServerConnectionManager.closeJmxServerConnection(); 
	      }
		 
		 activeMQstatisticBean.setStatisticQueueTable(statisticQueueTable);
		 
		 return activeMQstatisticBean;		
	}	

}
