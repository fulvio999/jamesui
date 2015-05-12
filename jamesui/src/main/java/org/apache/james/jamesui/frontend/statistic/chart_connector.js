// init function named after the components complete path

window.org_apache_james_jamesui_frontend_statistic_Chart = function() {
    var element = $(this.getElement());
    
    this.onStateChange = function() {
        $.plot(element, this.getState().series, {grid: {clickable: true}});
    };
    
      // Communicate local events back to server-side component
     element.bind('plotclick', function(event, point, item) {
        if (item) {
        	window.notify(item.seriesIndex, item.dataIndex);
        }
  });
};