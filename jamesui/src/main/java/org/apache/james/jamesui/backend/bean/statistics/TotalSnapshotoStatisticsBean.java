
package org.apache.james.jamesui.backend.bean.statistics;

import java.util.List;

/**
 * Bean that aggregate in one bean ALL the statistics of James server, retrieved at a specific time (Snapshot statistics).
 * It contains all the statistic retrieved from  Camel and ActiveMQ.
 * 
 * The contents of this bean will be shown in the "Statistics" tab panel of the application.
 */
public class TotalSnapshotoStatisticsBean {
		
	private ActiveMQstatisticBean activeMQstatisticBean;
	
	private List<CamelRouteStatisticBean> camelRouteStatisticBean;

	/**
	 * Construstor
	 */
	public TotalSnapshotoStatisticsBean() {
		
	}

	public ActiveMQstatisticBean getActiveMQstatisticBean() {
		return activeMQstatisticBean;
	}

	public void setActiveMQstatisticBean(ActiveMQstatisticBean activeMQstatisticBean) {
		this.activeMQstatisticBean = activeMQstatisticBean;
	}

	public List<CamelRouteStatisticBean> getCamelRouteStatisticBean() {
		return camelRouteStatisticBean;
	}

	public void setCamelRouteStatisticBean(
			List<CamelRouteStatisticBean> camelRouteStatisticBean) {
		this.camelRouteStatisticBean = camelRouteStatisticBean;
	}

}
