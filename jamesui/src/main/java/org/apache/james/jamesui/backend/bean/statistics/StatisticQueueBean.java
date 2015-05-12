
package org.apache.james.jamesui.backend.bean.statistics;

import java.io.Serializable;

/**
 * Contains the statistics data about a single JMS Queue of the ActiveMQ broker used by James 
 * @author fulvio
 *
 */
public class StatisticQueueBean implements Serializable {	
	
	 private static final long serialVersionUID = 1L;
 
	 private String queueName;
	 private long size;
	 private long consumerCount;
	 private long enqueueCount;

	/**
	 * Constructor
	 */
	public StatisticQueueBean() {
		
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getConsumerCount() {
		return consumerCount;
	}

	public void setConsumerCount(long consumerCount) {
		this.consumerCount = consumerCount;
	}

	public long getEnqueueCount() {
		return enqueueCount;
	}

	public void setEnqueueCount(long enqueueCount) {
		this.enqueueCount = enqueueCount;
	}


	@Override
	public String toString() {
		return "StatisticQueueBean [queueName=" + queueName + ", size=" + size
				+ ", consumerCount=" + consumerCount + ", enqueueCount="
				+ enqueueCount + "]";
	}

	

}
