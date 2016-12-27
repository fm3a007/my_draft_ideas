/**
 * demo_report_process.java
 * 
 * Description:
 * 	A demo program for report_generating_process
 * 
 * @author	David
 * Date:	2011-3-6
 * Copyright:China xxxx 2011
 */
package my.frmwk.util.chart;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
/**
 * @author David
 *
 */
public class demo_report_process {

	
	/**
	 * 
	 */
	public demo_report_process( ) {
	}
	
	
	
	/**
	 * @return	a JfreeChart for output, null if error occur!
	 */
	@SuppressWarnings("deprecation")
	public BarChartDataSet  getBarChartReport() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(150, "北京", "苹果");
		dataset.addValue(530, "上海", "苹果");
		dataset.addValue(160, "广州", "苹果");
		dataset.addValue(120, "北京", "梨子");
		dataset.addValue(230, "上海", "梨子");
		dataset.addValue(360, "广州", "梨子");
		dataset.addValue(600, "北京", "葡萄");
		dataset.addValue(430, "上海", "葡萄");
		dataset.addValue(560, "广州", "葡萄");
		dataset.addValue(400, "北京", "香蕉");
		dataset.addValue(530, "上海", "香蕉");
		dataset.addValue(660, "广州", "香蕉");
		dataset.addValue(500, "北京", "荔枝");
		dataset.addValue(630, "上海", "荔枝");
		dataset.addValue(430, "广州", "荔枝");
		
		BarChartDataSet  bcds = 
			new BarChartDataSet( "水果销量图统计-By somebody",dataset,
					"水果种类-http://www.blogjava.net/Alpha/", "销量");
		return	bcds;

	}

	/**
	 * @return	a JfreeChart for output, null if error occur!
	 */
	@SuppressWarnings("deprecation")
	public PieChartDataSet  getPieChartReport() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("图书", new Double(33.2));
        dataset.setValue("电器", new Double(10D));
        dataset.setValue("玩具", new Double(27.5D));
        dataset.setValue("水果", new Double(9.5D));
        dataset.setValue("饮料", new Double(10D));
        dataset.setValue("酒类", new Double(9.8));
		
		PieChartDataSet  bcds = 
			new PieChartDataSet( "饼图标例",dataset);
		return	bcds;
		
	}
	
	public TrendsChartDataSet getTrendsChartReport() {   
        XYSeriesCollection seriesCollection = new XYSeriesCollection();   
        XYSeries series1 = new XYSeries("平均价格");   
        XYSeries series2 = new XYSeries("最高价格");   
        XYSeries series3 = new XYSeries("最低价格");   
  
        series1.add(1, 7.25);   
//        series1.add(2, 4.81);   
//        series1.add(3, 3.69);   
//        series1.add(4, 3.53);   
        series1.add(5, 2.95);   
        series1.add(6, 3.96);   
  
        series2.add(1, 10.57);   
        series2.add(2, 5.37);   
        series2.add(3, 4.87);   
        series2.add(4, 4.87);   
//        series2.add(5, 3.63);   
//        series2.add(6, 5.27);   
//        series3.add(1, 4.76);   
//        series3.add(2, 3.63);   
        series3.add(3, 2.82);   
        series3.add(4, 2.82);   
        series3.add(5, 2.37);   
        series3.add(6, 3.33);   
        seriesCollection.addSeries(series1);   
        seriesCollection.addSeries(series2);   
        seriesCollection.addSeries(series3);   
        TrendsChartDataSet trend = 
        	new TrendsChartDataSet("title", seriesCollection,"Cate...", "Axis...");
        return trend;   
    }  
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
