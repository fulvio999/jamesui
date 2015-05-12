package org.apache.james.jamesui.backend.test.client;

import junit.framework.TestCase;

import org.apache.james.jamesui.backend.bean.statistics.ActiveMQstatisticBean;
import org.apache.james.jamesui.backend.client.jmx.ActiveMQclient;

/**
 * Test cases for class ActiveMQclient
 * @author fulvio
 *
 */
public class ActiveMQclientTest extends TestCase {

	public ActiveMQclient activeMQclient;
	
	protected void setUp() throws Exception {
		super.setUp();
		activeMQclient = new ActiveMQclient();
	}

	public void testGetJamesMQbrokerStats() {
		ActiveMQstatisticBean ab = activeMQclient.getJamesMQbrokerStats();
		assertNotNull(ab);
	}

}
