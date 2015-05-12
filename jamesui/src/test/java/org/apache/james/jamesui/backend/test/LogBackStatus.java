package org.apache.james.jamesui.backend.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;


public class LogBackStatus {
	
	final static Logger logger = LoggerFactory.getLogger(LogBackStatus.class);

	public static void main(String[] args) {
	    // assume SLF4J is bound to logback in the current environment
	    LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
	    
//	    try {
//	      JoranConfigurator configurator = new JoranConfigurator();
//	      configurator.setContext(context);
//	      // Call context.reset() to clear any previous configuration, e.g. default 
//	      // configuration. For multi-step configuration, omit calling context.reset().
//	      context.reset(); 
//	      configurator.doConfigure(args[0]);
//	    } catch (JoranException je) {
//	      // StatusPrinter will handle this
//	    }
	    StatusPrinter.print(context);

	    logger.info("Exiting application.");
	  }

}
