package org.apache.james.jamesui.frontend.statistic;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.shared.ui.JavaScriptComponentState;

/**
 * The Chart state (ie: the chart data model)
 * @author fulvio
 *
 */
public class ChartState extends JavaScriptComponentState {
	
	private static final long serialVersionUID = 1L;
		
	public List<List<List<Long>>> series = new ArrayList<List<List<Long>>>();		
}