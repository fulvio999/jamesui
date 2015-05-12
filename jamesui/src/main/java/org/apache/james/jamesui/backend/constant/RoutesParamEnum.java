package org.apache.james.jamesui.backend.constant;

/**
 * Contains the names of the Camel Routes parameters monitored and shown in the statistics panel
 * @author fulvio
 *
 */
public enum RoutesParamEnum {	
	
	//parameters for each route
	EXACHANGES_COMPLETED,
	EXCHANGES_FAILED,
	EXCHANGES_TOTAL,
	MIN_PROCESSING_TIME,
	MAX_PROCESSING_TIME,
	MEAN_PROCESSING_TIME;
}
