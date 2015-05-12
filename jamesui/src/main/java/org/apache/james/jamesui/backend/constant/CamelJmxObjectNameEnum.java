package org.apache.james.jamesui.backend.constant;

import org.apache.james.jamesui.backend.configuration.manager.EnvironmentConfigurationReader;

/**
 * JMX objects names of the Camel routes
 * 
 * @author fulvio
 *
 */
public enum CamelJmxObjectNameEnum {
	
	SPAM_ROUTE ("org.apache.camel:type=routes,context="+EnvironmentConfigurationReader.getHostName()+"/jamesCamelContext,name=\"spam\""),
	
	BOUCES_ROUTE("org.apache.camel:type=routes,context="+EnvironmentConfigurationReader.getHostName()+"/jamesCamelContext,name=\"bounces\""),
	
	ERROR_ROUTE("org.apache.camel:type=routes,context="+EnvironmentConfigurationReader.getHostName()+"/jamesCamelContext,name=\"error\""),
	
	TRANSPORT_ROUTE("org.apache.camel:type=routes,context="+EnvironmentConfigurationReader.getHostName()+"/jamesCamelContext,name=\"transport\""),
	
	LOCAL_ADDRESS_ERROR("org.apache.camel:type=routes,context="+EnvironmentConfigurationReader.getHostName()+"/jamesCamelContext,name=\"local-address-error\""),
	
	RELAY_DENIED_ERROR("org.apache.camel:type=routes,context="+EnvironmentConfigurationReader.getHostName()+"/jamesCamelContext,name=\"relay-denied\""),
	
	ROOT_ROUTE("org.apache.camel:type=routes,context="+EnvironmentConfigurationReader.getHostName()+"/jamesCamelContext,name=\"root\"");	
	
    private String objectName;
	
	private CamelJmxObjectNameEnum(String obj){
		this.objectName = obj;
	}

	public String getObjectName() {
		return objectName;
	}		 

}
