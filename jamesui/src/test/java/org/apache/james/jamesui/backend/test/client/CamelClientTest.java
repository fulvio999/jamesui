package org.apache.james.jamesui.backend.test.client;

import java.util.Hashtable;

import junit.framework.TestCase;

import org.apache.james.jamesui.backend.bean.statistics.CamelRouteStatisticBean;
import org.apache.james.jamesui.backend.client.jmx.CamelClient;
import org.apache.james.jamesui.backend.constant.RoutesNameEnum;

/**
 * Test cases for class CamelClient
 * @author fulvio
 *
 */
public class CamelClientTest extends TestCase {
	
	
//    this.addItem( new Object[] { "routeState", String.valueOf(cb.getRouteName()) },new Integer( 1 ) );
//    this.addItem( new Object[] { "exchangesCompleted", String.valueOf(cb.getExchangesCompleted()) },new Integer( 2 ) );
//    this.addItem( new Object[] { "exchangesFailed", String.valueOf(cb.getExchangesFailed()) },new Integer( 3 ) );
//    this.addItem( new Object[] { "exchangesTotal", String.valueOf(cb.getExchangesTotal()) },new Integer( 4 ) );
//    this.addItem( new Object[] { "minProcessingTime", String.valueOf(cb.getMinProcessingTime()) },new Integer( 5 ) );
//    this.addItem( new Object[] { "maxProcessingTime", String.valueOf(cb.getMaxProcessingTime()) },new Integer( 6 ) );
//    this.addItem( new Object[] { "meanProcessingTime", String.valueOf(cb.getMeanProcessingTime()) },new Integer( 7 ) );   

	
	
	private CamelClient camelClient;

	protected void setUp() throws Exception {		
		super.setUp();
		camelClient = new CamelClient();
	}

	public void testQueryAllQueues() {
		Hashtable<RoutesNameEnum, CamelRouteStatisticBean> s = camelClient.queryAllRoutes();
		assertNotNull(s);
		
		assertEquals(7,s.size());
		
//		CamelRouteStatisticBean sb = s.get("routeState");
//		assertNotNull(sb.getRouteState());
	}

	public void testQuerySpamRoute() {
		CamelRouteStatisticBean cs = camelClient.querySpamRoute();
		assertNotNull(cs);		
	}

	public void testQueryBouncesRoute() {
		CamelRouteStatisticBean cs = camelClient.queryBouncesRoute();
		assertNotNull(cs);
	}

	public void testQueryErrorRoute() {
		CamelRouteStatisticBean cs = camelClient.queryErrorRoute();
		assertNotNull(cs);
	}

	public void testQueryTransportRoute() {
		CamelRouteStatisticBean cs = camelClient.queryTransportRoute();
		assertNotNull(cs);	
	}

	public void testQueryLocalAddressErrorRoute() {
		CamelRouteStatisticBean cs = camelClient.queryLocalAddressErrorRoute();
		assertNotNull(cs);
	}

	public void testQueryRelayDeniedRoute() {
		CamelRouteStatisticBean cs = camelClient.queryRelayDeniedRoute();
		assertNotNull(cs);
	}

	public void testQueryRootRoute() {
		CamelRouteStatisticBean cs = camelClient.queryRootRoute();
		assertNotNull(cs);
	}

}
