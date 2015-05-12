package org.apache.james.jamesui.backend.test;


import java.lang.management.ManagementFactory;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;

public class ListAllJVMMBeans {
	
	public static void main(String[] args) throws Exception {
		
		//to access the MBeanServer in the same JVM,
		MBeanServer server = ManagementFactory.getPlatformMBeanServer();	
		
	    
	    Set<ObjectInstance> instances = server.queryMBeans(null, null);
	    
	    Iterator<ObjectInstance> iterator = instances.iterator();
	    
		while (iterator.hasNext()) {
			ObjectInstance instance = iterator.next();
			System.out.println("MBean Found:");
			System.out.println("Class Name:t" + instance.getClassName());
			System.out.println("Object Name:t" + instance.getObjectName());
			System.out.println("****************************************");
		}
		
		ListAllJVMMBeans listAllJVMMBeans = new ListAllJVMMBeans();
		listAllJVMMBeans.listAllMBeanServer();		
	}
	
	
	private void listAllMBeanServer() throws IntrospectionException, InstanceNotFoundException, ReflectionException{
		
	
        final List<MBeanServer> servers = new LinkedList<MBeanServer>();
        
        servers.add(ManagementFactory.getPlatformMBeanServer()); //NB: getPlatformMBeanServer()  crea anche un MbeanServer
        
        System.out.println("----- Found total: "+servers.size()+ " MBeanServer");
        
        for (final MBeanServer server : servers) {        	
        	System.out.println("Default Domain: "+ server.getDefaultDomain());
        
        	System.out.println("-------------------------------------------------");
        	
        	System.out.println("Server class:"+ server.getClass().getName());
        	System.out.println("Server defsult domain: "+server.getDefaultDomain());
        	
            final Set<ObjectName> mbeans = new HashSet<ObjectName>();
            mbeans.addAll(server.queryNames(null, null));
            
            for (final ObjectName mbean : mbeans) {
                
            	System.out.println("MBean: "+ mbean);
            }
        }
		
	}

}
