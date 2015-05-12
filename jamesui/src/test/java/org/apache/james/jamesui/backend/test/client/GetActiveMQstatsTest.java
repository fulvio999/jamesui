package org.apache.james.jamesui.backend.test.client;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.activemq.broker.jmx.BrokerViewMBean;
import org.apache.activemq.broker.jmx.QueueViewMBean;

public class GetActiveMQstatsTest {
	
	public static void main(String[] args) throws Exception {
	
		JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi");
		JMXConnector connector = JMXConnectorFactory.connect(url, null);
		connector.connect();
		MBeanServerConnection connection = connector.getMBeanServerConnection(); 
		
		//ObjectName name = new ObjectName("my-broker:BrokerName=localhost,Type=Broker");
		ObjectName name = new ObjectName("org.apache.activemq:BrokerName=james,Type=Broker");
		
		BrokerViewMBean mbean = (BrokerViewMBean) MBeanServerInvocationHandler.newProxyInstance(connection, name, BrokerViewMBean.class, true); 
		
		System.out.println("Statistics for broker " + mbean.getBrokerId()+ " - " + mbean.getBrokerName());
		System.out.println("\n-----------------\n");
		System.out.println("Total message count: " + mbean.getTotalMessageCount() + "\n");
		System.out.println("Total number of consumers: " + mbean.getTotalConsumerCount());
		System.out.println("Total number of Queues: " + mbean.getQueues().length);
		
		for (ObjectName queueName : mbean.getQueues())
		{
			QueueViewMBean queueMbean = (QueueViewMBean) MBeanServerInvocationHandler.newProxyInstance(connection, queueName, QueueViewMBean.class, true);
			System.out.println("\n-----------------\n");
			System.out.println("Statistics for queue " + queueMbean.getName());
			System.out.println("Size: " + queueMbean.getQueueSize());
			System.out.println("Number of consumers: " + queueMbean.getConsumerCount());
		} 
		
   }
}