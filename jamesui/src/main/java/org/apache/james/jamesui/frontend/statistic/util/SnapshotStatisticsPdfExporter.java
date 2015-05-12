
package org.apache.james.jamesui.frontend.statistic.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.james.jamesui.backend.bean.statistics.ActiveMQstatisticBean;
import org.apache.james.jamesui.backend.bean.statistics.CamelRouteStatisticBean;
import org.apache.james.jamesui.backend.bean.statistics.StatisticQueueBean;
import org.apache.james.jamesui.backend.constant.RoutesNameEnum;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Export to PDF file the currently shown Snapshot statistics in the "Statistics" panel
 *
 */
public class SnapshotStatisticsPdfExporter {
	
	public static SimpleDateFormat DATEFORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * Constructor
	 * 
	 */
	public SnapshotStatisticsPdfExporter() {
	
	}	
   
    /**
     * Creates a PDF file using the dat shown in the snapshot statistics p
     * @param camelRouteStatisticTable The beans with Camel Route statistics
     * @param activeMQstatisticBean The bean with ActiveMQ Route statistics
     * @param statsTime the snapshot statistics date
     */
	public ByteArrayOutputStream createPdfDocument(Hashtable<RoutesNameEnum, CamelRouteStatisticBean> camelRouteStatisticTable, ActiveMQstatisticBean activeMQstatisticBean, String statsTime) throws DocumentException, IOException{
			
		 Document document = new Document(PageSize.A4, 10, 10, 10, 10);  		
		 ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		 
		 Font headerFont = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD);
		 Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 7);
		 
         PdfWriter.getInstance(document, bos);
         
		 document.open();  
		 
		 document.add(new Paragraph("\n\n"));
		 document.add(new Chunk("Snapshot Date: "+statsTime,headerFont));		
		 document.add(new Paragraph("\n\n"));
		 
		 // Add metadata
		 document.addTitle("James snapshot statisitcs");
		 document.addSubject("statistics");		
		 document.addAuthor("JamesUI");
		 document.addCreator("JamesUI");
 		 
		 Iterator<Entry<RoutesNameEnum, CamelRouteStatisticBean>> camelStatsIterator = camelRouteStatisticTable.entrySet().iterator();
          
		 while (camelStatsIterator.hasNext())
		 {
			 Entry<RoutesNameEnum, CamelRouteStatisticBean> camelRouteStatisticEntry = camelStatsIterator.next();
			 RoutesNameEnum routName = camelRouteStatisticEntry.getKey();
			 CamelRouteStatisticBean b = camelRouteStatisticEntry.getValue();
		 
			 /* ---------------- CAMEL ROUTE STATS ----------------- */			 
			 //-- spam route
	         PdfPTable camelRouteStatsTable = new PdfPTable(2); //two column
	
	         //column header
	         PdfPCell header_col_1 = new PdfPCell(new Paragraph("ROUTE: "+routName,headerFont));
	         PdfPCell header_col_2 = new PdfPCell(new Paragraph(""));
	         
	         //row 1
	         PdfPCell row_1_key = new PdfPCell(new Paragraph("routeState",normalFont));
	         PdfPCell row_1_value = new PdfPCell(new Paragraph(b.getRouteState(),normalFont));
	        
	         //row2
	         PdfPCell row_2_key = new PdfPCell(new Paragraph("exchangesCompleted",normalFont));
	         PdfPCell row_2_value = new PdfPCell(new Paragraph(String.valueOf(b.getExchangesCompleted()),normalFont));
	         
	         //row3
	         PdfPCell row_3_key = new PdfPCell(new Paragraph("exchangesFailed",normalFont));
	         PdfPCell row_3_value = new PdfPCell(new Paragraph(String.valueOf(b.getExchangesFailed()),normalFont));
	         
	         //row4
	         PdfPCell row_4_key = new PdfPCell(new Paragraph("exchangesTotal",normalFont));
	         PdfPCell row_4_value = new PdfPCell(new Paragraph(String.valueOf(b.getExchangesTotal()),normalFont));
	         
	         //row5
	         PdfPCell row_5_key = new PdfPCell(new Paragraph("minProcessingTime",normalFont));
	         PdfPCell row_5_value = new PdfPCell(new Paragraph( String.valueOf(b.getMinProcessingTime()),normalFont));
	         
	         //row6
	         PdfPCell row_6_key = new PdfPCell(new Paragraph("maxProcessingTime",normalFont));
	         PdfPCell row_6_value = new PdfPCell(new Paragraph(String.valueOf(b.getMaxProcessingTime()),normalFont));
	         
	         //row7
	         PdfPCell row_7_key = new PdfPCell(new Paragraph("meanProcessingTime",normalFont));
	         PdfPCell row_7_value = new PdfPCell(new Paragraph(String.valueOf(b.getMeanProcessingTime()),normalFont));
	       
	         camelRouteStatsTable.addCell(header_col_1);
	         camelRouteStatsTable.addCell(header_col_2);
	         
	         camelRouteStatsTable.addCell(row_1_key);
	         camelRouteStatsTable.addCell(row_1_value);
	         
	         camelRouteStatsTable.addCell(row_2_key);
	         camelRouteStatsTable.addCell(row_2_value);
	         
	         camelRouteStatsTable.addCell(row_3_key);
	         camelRouteStatsTable.addCell(row_3_value);
	         
	         camelRouteStatsTable.addCell(row_4_key);
	         camelRouteStatsTable.addCell(row_4_value);
	         
	         camelRouteStatsTable.addCell(row_5_key);
	         camelRouteStatsTable.addCell(row_5_value);
	         
	         camelRouteStatsTable.addCell(row_6_key);
	         camelRouteStatsTable.addCell(row_6_value);
	         
	         camelRouteStatsTable.addCell(row_7_key);
	         camelRouteStatsTable.addCell(row_7_value);
	        
	         camelRouteStatsTable.setSpacingAfter(10); 
	         
	         document.add(camelRouteStatsTable);	    
		  }   
	         
		  document.newPage();
		 
	      /* ---------------- ACTIVEMQ STATS ----------------- */
		 
		  PdfPTable activeMQstatsTable = new PdfPTable(2); //two column
				
	      //column header
	      PdfPCell header_col_1 = new PdfPCell(new Paragraph("Broker info",headerFont));
	      PdfPCell header_col_2 = new PdfPCell(new Paragraph(""));	         
	      
	      PdfPCell row_1_key = new PdfPCell(new Paragraph("Broker id",normalFont));
	      PdfPCell row_1_value = new PdfPCell(new Paragraph(activeMQstatisticBean.getBrokerId(),normalFont));
	         
	      PdfPCell row_2_key = new PdfPCell(new Paragraph("Broker name",normalFont));
	      PdfPCell row_2_value = new PdfPCell(new Paragraph(activeMQstatisticBean.getBrokerName(),normalFont));
	         
	      PdfPCell row_3_key = new PdfPCell(new Paragraph("Total Message Count",normalFont));
	      PdfPCell row_3_value = new PdfPCell(new Paragraph(String.valueOf(activeMQstatisticBean.getTotalMessageCount()),normalFont));
	         
	      PdfPCell row_4_key = new PdfPCell(new Paragraph("Total Consumer Count",normalFont));
	      PdfPCell row_4_value = new PdfPCell(new Paragraph(String.valueOf(activeMQstatisticBean.getTotalConsumerCount()),normalFont));
	         
	      PdfPCell row_5_key = new PdfPCell(new Paragraph("Queue length",normalFont));
	      PdfPCell row_5_value = new PdfPCell(new Paragraph(String.valueOf(activeMQstatisticBean.getQueueLength()),normalFont));
	         
	      activeMQstatsTable.addCell(header_col_1);
	      activeMQstatsTable.addCell(header_col_2);
	         
	      activeMQstatsTable.addCell(row_1_key);
	      activeMQstatsTable.addCell(row_1_value);
	         
	      activeMQstatsTable.addCell(row_2_key);
	      activeMQstatsTable.addCell(row_2_value);
	         
	      activeMQstatsTable.addCell(row_3_key);
	      activeMQstatsTable.addCell(row_3_value);
	         
	      activeMQstatsTable.addCell(row_4_key);
	      activeMQstatsTable.addCell(row_4_value);
	         
	      activeMQstatsTable.addCell(row_5_key);
	      activeMQstatsTable.addCell(row_5_value);	
	      
	      activeMQstatsTable.setSpacingAfter(10); 
	         
	      document.add(activeMQstatsTable);
	         
	      //queues stats tables
	      Iterator<Entry<String, StatisticQueueBean>> queueStatsIterator = activeMQstatisticBean.getStatisticQueueTable().entrySet().iterator();
          
		  while (queueStatsIterator.hasNext())
		  {
			 Entry<String, StatisticQueueBean> queueStatisticEntry = queueStatsIterator.next();
			 String queueName = queueStatisticEntry.getKey();
			 StatisticQueueBean b = queueStatisticEntry.getValue();
			 
			 PdfPTable queueStatsTable = new PdfPTable(2); //two column
				
	         //column header
	         PdfPCell header_1 = new PdfPCell(new Paragraph(queueName+ " queue",headerFont));
	         PdfPCell header_2 = new PdfPCell(new Paragraph(""));	         
	        
	         PdfPCell row_1_k = new PdfPCell(new Paragraph("Queue name",normalFont));
	         PdfPCell row_1_v = new PdfPCell(new Paragraph(b.getQueueName(),normalFont));	         
	        
	         PdfPCell row_2_k = new PdfPCell(new Paragraph("Size",normalFont));
	         PdfPCell row_2_v = new PdfPCell(new Paragraph(String.valueOf(b.getSize()),normalFont));         
	        
	         PdfPCell row_3_k = new PdfPCell(new Paragraph("Consumer count",normalFont));
	         PdfPCell row_3_v = new PdfPCell(new Paragraph(String.valueOf(b.getConsumerCount()),normalFont));
	        
	         PdfPCell row_4_k = new PdfPCell(new Paragraph("Enqueue count",normalFont));
	         PdfPCell row_4_v = new PdfPCell(new Paragraph(String.valueOf(b.getEnqueueCount()),normalFont));
	         
	         queueStatsTable.addCell(header_1);
	         queueStatsTable.addCell(header_2);
		         
	         queueStatsTable.addCell(row_1_k);
	         queueStatsTable.addCell(row_1_v);
		         
	         queueStatsTable.addCell(row_2_k);
	         queueStatsTable.addCell(row_2_v);
		         
	         queueStatsTable.addCell(row_3_k);
	         queueStatsTable.addCell(row_3_v);
		         
	         queueStatsTable.addCell(row_4_k);
	         queueStatsTable.addCell(row_4_v);	
	         
	         queueStatsTable.setSpacingAfter(10); 
	         
	         document.add(queueStatsTable);
		 }	 	
         
         document.close();
         
         return bos;
    }  

}
