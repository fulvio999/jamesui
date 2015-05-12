package org.apache.james.jamesui.backend.constant;

/**
 * Enum with all the Camel Routes names used in James 
 * 
 * @author fulvio
 *
 */
public enum RoutesNameEnum {
	
	//in the route MapDB there is Collection for each Camel Route
	SPAM,
	BOUNCES,
	ERROR,
	TRANSPORT,
	LOCAL_ADDRESS_ERROR,
	RELAY_DENIED,
	ROOT;

}
