package org.apache.james.jamesui.backend.constant;


/**
 * JMX objects names of the ActiveMQ queue
 * 
 * @author fulvio
 *
 */
public enum ActiveMQJmxObjectNameEnum {
	
	BROKER_NAME("org.apache.activemq:BrokerName=james,Type=Broker");
	
    private String objectName;
	
	private ActiveMQJmxObjectNameEnum(String obj){
		this.objectName = obj;
	}

	public String getObjectName() {
		return objectName;
	}

}
