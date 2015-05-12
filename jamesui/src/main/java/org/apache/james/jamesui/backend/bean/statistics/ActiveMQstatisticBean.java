
package org.apache.james.jamesui.backend.bean.statistics;

import java.io.Serializable;
import java.util.Hashtable;


/**
 * Contains the statistics data about ActiveMQ queues and JMS Broker used by James.
 * The data are retrieved using ActiveMQClient class 
 * 
 * @author fulvio
 *
 */
public class ActiveMQstatisticBean implements Serializable {	

	 private static final long serialVersionUID = -6840600766695380528L;
	 
	 private String brokerId;
	 private String brokerName;
	 
	 private long totalMessageCount;
	 private long totalConsumerCount;
	 private int queueLength;
	 
     /* the statistics info about the ActiveMQ queues, (Table KEY is the Queue name) */
	 private Hashtable<String,StatisticQueueBean> statisticQueueTable;

	/**
	 * Constructor
	 */
	public ActiveMQstatisticBean() {
		
	}

	public String getBrokerId() {
		return brokerId;
	}

	public void setBrokerId(String brokerId) {
		this.brokerId = brokerId;
	}

	public String getBrokerName() {
		return brokerName;
	}

	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}

	public long getTotalMessageCount() {
		return totalMessageCount;
	}

	public void setTotalMessageCount(long totalMessageCount) {
		this.totalMessageCount = totalMessageCount;
	}

	public int getQueueLength() {
		return queueLength;
	}

	public void setQueueLength(int queueLength) {
		this.queueLength = queueLength;
	}

	public long getTotalConsumerCount() {
		return totalConsumerCount;
	}

	public void setTotalConsumerCount(long totalConsumerCount) {
		this.totalConsumerCount = totalConsumerCount;
	}

	public Hashtable<String, StatisticQueueBean> getStatisticQueueTable() {
		return statisticQueueTable;
	}

	public void setStatisticQueueTable(
			Hashtable<String, StatisticQueueBean> statisticQueueTable) {
		this.statisticQueueTable = statisticQueueTable;
	}

}
