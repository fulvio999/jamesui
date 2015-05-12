package org.apache.james.jamesui.frontend.statistic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;

/**
 * Create a Flot chart that show saved statistic data inside a MapDB database (ATTENTION: at the naming convention to use also for the .js included files)
 * NOTE: Edit jquery.chart.js to customize the chart look&feel and to configure the X-Y axis to show
 * different format/type (eg: "time" instead of numeric values)
 * 
 * @author fulvio
 * 
 * See: https://github.com/aanno/More-Vaadin  for example
 * http://www.jqueryflottutorial.com/jquery-flot-data-format.html  for JS date time format to pass at Flot
 *
 */

 @JavaScript({
		"jquery.min.js",
		"jquery.chart.js", "chart_connector.js", "jquery.flot.time.js" })
 public class Chart extends AbstractJavaScriptComponent {
	 
	private static final long serialVersionUID = 1L;
	
	private final static Logger LOG = LoggerFactory.getLogger(Chart.class);
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * Assign a new data-set to be shown
	 * @param points
	 * @throws ParseException 
	 */
	public void addSeries(List<String> x_values, List<Long> y_values) {
		
		List<List<Long>> pointList = new ArrayList<List<Long>>();
		
		for (int i = 0; i < x_values.size(); i++) 
		{	
			LOG.trace("Adding Point["+x_values.get(i)+","+y_values.get(i)+"]");			
			try {
				pointList.add( Arrays.asList( Long.valueOf(sdf.parse(x_values.get(i)).getTime()), Long.valueOf(y_values.get(i)) ) );
			} catch (ParseException e) {
				LOG.error("Error formatting Date during building chart dataset, cause: ",e);
			}
		}

		getState().series.add(pointList);		
	}
	
	public List<List<List<Long>>> getSerie(){
		return getState().series;
	}
	
	/**
	 * call it before add a new series at the chart to clean the previously assigned series
	 */
	public void clearSerie(){
		getState().series.clear();
	}
	

	@Override
	public ChartState getState() {
		return (ChartState) super.getState();
	}
}