package org.apache.james.jamesui.backend.test.client;

import junit.framework.TestCase;

import org.apache.james.jamesui.backend.client.jmx.JamesClient;

/**
 * Test cases for class JamesClient
 * @author fulvio
 *
 */
public class JamesClientTest extends TestCase {

	private JamesClient jamesClient;
	
	protected void setUp() throws Exception {
		super.setUp();
		jamesClient = new JamesClient();
	}

	/*
	public void testGetDnsServer() {
		String[] dns = jamesClient.getDnsServer();
		assertNotNull(dns);		
	}
	*/

	public void testGetDomains() {
		String[] domains = jamesClient.getDomains();
		assertNotNull(domains);		
	}

	public void testAddDomain() {
		boolean result = jamesClient.addDomain("newDomains");
		assertTrue(result);
	}

	public void testRemoveDomain() {
		jamesClient.addDomain("newDomains");
		boolean result = jamesClient.removeDomain("newDomains");
		assertTrue(result);
	}

	public void testGetAllsers() {
		String[] users = jamesClient.getAllusers();
		assertNotNull(users);		
	}

	public void testAddUser() {
		boolean result = jamesClient.addUser("newUser", "userPassword");
		assertTrue(result);
	}

	public void testDeleteUser() {
		jamesClient.addUser("newUser", "userPassword");
		boolean result = jamesClient.deleteUser("newUser");
		assertTrue(result);
	}

	public void testVerifyExistUser() {
		jamesClient.addUser("newUser", "userPassword");
		boolean result = jamesClient.verifyExistUser("newUser");
		assertTrue(result);
	}

}
