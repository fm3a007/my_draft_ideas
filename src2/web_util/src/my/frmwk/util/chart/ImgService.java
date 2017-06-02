/**
 * \file
 * 工具类报表图片.
 * 
 * 
 * 
 * 
 * 
 * These source files are released under the GPLv3 license.
 *
 * @author David
 *  
 * @version 1.0
 * 
 * @see 
 * Modify log:
 * 	 - 2013-11-22 Liang,David created
 * 
 */
package my.frmwk.util.chart;

import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.ui.TextAnchor;
import org.jfree.util.Rotation;

import com.sun.org.apache.xml.internal.security.utils.Base64;

/**
 * 工具类: 生成报表图片.
 * 
 * 将内部的一些数据对象生成图片. 将纯粹的数据处理 与 WEB 环境分离, 
 * 以方便相关功能的重用.
 * \n
 * 用法: 构造对象时设置数据集, 然后调用所需的方法.
 * 
 * @author David
 * @version 1.0
 */
public class ImgService implements ImgJChartProviderIntf
{
	
	public static String encode2Base64( JFreeChart chart, int width, int height){
		ByteArrayOutputStream bas = new ByteArrayOutputStream();
		try {
			ChartUtilities.writeChartAsJPEG( bas, chart, width, height);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte [] buf = bas.toByteArray();
		return Base64.encode(buf);
	}

	/** 数据对象, 用于生成图表的数据集. */
	protected Object ds;
	
	/** 图形的类形 */
	protected int chartType = 0;


	/**
	 * 构造函数: 真接设置数据对象, 以便调用后面的函数获取JFreeChart对象.
	 * @param ds
	 * @param type 图形的类型,构造时不设置该值则只能调用 getChart( type);
	 */
	public ImgService( Object ds, int type) {
		this.ds = ds;
		chartType = type;
		setTheme();
	}


	public JFreeChart getChart( int type) {
		switch (type) {
		case 1:
			return getBarChart( );
			// break;
		case 2:
			return getPieChart( );
			// break;
		case 3:
			return getTrendsChart( );
		case 4:
			return getIndexBarChart( );
		case 5:
			return getBarChart3D( );
		case 6:
			return getStackedBarChart3D( );
			// break;
		case 7:
			return getMonthTrendsChart( );
			// break;
		case 8 :
			return getYearTrendsChart();
		default:
			return null;
		}
	}

	/** 获取图形对象, 本函数要配合特定的构造函数(指定类型，否则是默认类形) */
	public JFreeChart getChart(){
		return	getChart( chartType);
	}

	private void setTheme() {
		// 创建主题样式
		StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
		// 设置标题字体
		standardChartTheme.setExtraLargeFont(new Font("隶书", Font.BOLD, 12));
		// 设置图例的字体
		standardChartTheme.setRegularFont(new Font("宋书", Font.PLAIN, 12));
		// 设置轴向的字体
		standardChartTheme.setLargeFont(new Font("宋书", Font.PLAIN, 12));
		// 应用主题样式
		ChartFactory.setChartTheme(standardChartTheme);
	}

	@SuppressWarnings("deprecation")
	protected JFreeChart getBarChart( ) {
		BarChartDataSet ds = (BarChartDataSet) this.ds;

		if (null == ds) {
			// System.out.println("Failed to get data from session!!!!!!!!!!");
			return null;
		}

		JFreeChart chart = ChartFactory.createBarChart(ds.title,
				ds.CatecategoryAxisLabel, ds.valueAxisLabel, ds.ds,
				PlotOrientation.VERTICAL, true, false, false);
		chart.setBackgroundPaint(null);
		CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.red);
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		//
		rangeAxis.setUpperMargin(0.1);
		rangeAxis.setLowerBound(0);
		// rangeAxis.setUpperBound(2);
		rangeAxis.setTickUnit(new NumberTickUnit(1));
		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setAxisLineVisible(true);
		//
		domainAxis.setCategoryMargin(0.2);
		//
		domainAxis.setUpperMargin(0.1);//
		// domainAxis.setVerticalCategoryLabels( false );
		plot.setDomainAxis(domainAxis);
		BarRenderer renderer = new BarRenderer();
		// 设置柱形图的颜色
		renderer.setSeriesPaint(0, Color.decode("#5ab115"));
		renderer.setSeriesPaint(1, Color.decode("#0271af"));
		renderer.setMaximumBarWidth(0.03);
		renderer.setBaseOutlinePaint(Color.BLACK);
		// 设置每个地区所包含的平行柱的之间距离
		renderer.setItemMargin(0.03);
		renderer.setBaseSeriesVisible(true);
		// 显示每个柱的数值，并修改该数值的字体属性
		renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer.setItemLabelsVisible(true);
		renderer.setAutoPopulateSeriesOutlinePaint(true);
		plot.setRenderer(renderer);
		// 设置柱的透明度
		plot.setForegroundAlpha(0.8f);
		plot.setOutlinePaint( new   Color(62, 62, 62));

		return chart;
	}

	@SuppressWarnings("deprecation")
	protected JFreeChart getIndexBarChart( ) {

		BarChartDataSet ds = (BarChartDataSet) this.ds;

		if (null == ds) {
			// System.out.println("Failed to get data from session!!!!!!!!!!");
			return null;
		}
		JFreeChart chart = ChartFactory.createBarChart(ds.title,
				ds.CatecategoryAxisLabel, ds.valueAxisLabel, ds.ds,
				PlotOrientation.VERTICAL, true, false, false);
		chart.setBackgroundPaint(null);
		CategoryPlot plot = chart.getCategoryPlot();
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		//
		rangeAxis.setUpperMargin(0.1);
		rangeAxis.setLowerBound(0);
		// rangeAxis.setUpperBound(2);
		rangeAxis.setNumberFormatOverride(new DecimalFormat("#0.0"));
		// rangeAxis.setTickUnit(new NumberTickUnit(1));
		rangeAxis.setAutoTickUnitSelection(true);
		// rangeAxis.setTickUnit(new NumberTickUnit(100));
		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setAxisLineVisible(true);
		//
		domainAxis.setCategoryMargin(0.2);
		//
		domainAxis.setUpperMargin(0.05);//
		// domainAxis.setVerticalCategoryLabels( false );
		plot.setDomainAxis(domainAxis);
		BarRenderer renderer = new BarRenderer();
		// 设置柱形图的颜色
		renderer.setSeriesPaint(0, Color.decode("#acd9a6"));
		renderer.setSeriesPaint(1, Color.decode("#a5d9da"));
		renderer.setMaximumBarWidth(0.03);
		renderer.setBaseOutlinePaint(Color.BLACK);
		// 设置每个地区所包含的平行柱的之间距离
		renderer.setItemMargin(0.01);
		renderer.setBaseSeriesVisible(true);
		// 显示每个柱的数值，并修改该数值的字体属性
		renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer.setItemLabelsVisible(true);
		renderer.setAutoPopulateSeriesOutlinePaint(true);
		plot.setRenderer(renderer);
		// 设置柱的透明度
		plot.setForegroundAlpha(0.8f);
		plot.setOutlinePaint( new   Color(62, 62, 62));    
		plot.setRangeGridlinePaint( new Color(255,255,255));
		return chart;
	}

	/**
	 * 主要展示三维效果
	 * 
	 * @param chartId
	 * @param type
	 * @return
	 */
	protected JFreeChart getBarChart3D( ) {

		BarChartDataSet ds = (BarChartDataSet) this.ds;

		if (null == ds) {
			// System.out.println("Failed to get data from session!!!!!!!!!!");
			return null;
		}
		JFreeChart chart = ChartFactory.createBarChart3D(ds.title,
				ds.CatecategoryAxisLabel, ds.valueAxisLabel, ds.ds,
				PlotOrientation.VERTICAL, true, false, false);

		// 设置图表背景颜色
		chart.setBackgroundPaint(null);

		// 取得CategoryPlot对象的引用，通过这个对象可以对图标进行具体的设置
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.setBackgroundPaint(new Color(255, 254, 223));
		plot.setRangeGridlinePaint(new Color(255,255,255));
		plot.setOutlinePaint( new   Color(62, 62, 62));    

		// 显示柱体代表的值
		CategoryItemRenderer renderer = plot.getRenderer();
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer.setSeriesItemLabelsVisible(0, Boolean.TRUE);

		// 设置柱体的外观
		BarRenderer barrenderer = (BarRenderer) plot.getRenderer();
		barrenderer.setDrawBarOutline(true);
		barrenderer.setMaximumBarWidth(0.05);
		barrenderer.setSeriesPaint(0, Color.decode("#acd9a6"));
		barrenderer.setSeriesPaint(1, Color.decode("#a5d9da"));
		// 设置坐标
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis.setUpperMargin(0.15);

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

		return chart;
	}

	/**
	 * 主要展示三维效果
	 * 
	 * @author David
	 * @param chartId
	 * @param type
	 * @return
	 */
	@SuppressWarnings("deprecation")
	protected JFreeChart getStackedBarChart3D( ) {

		BarChartDataSet ds = (BarChartDataSet) this.ds;

		if (null == ds) {
			// System.out.println("Failed to get data from session!!!!!!!!!!");
			return null;
		}
		JFreeChart chart = ChartFactory.createStackedBarChart3D(ds.title,
				ds.CatecategoryAxisLabel, ds.valueAxisLabel, ds.ds,
				PlotOrientation.VERTICAL, true, false, false);

		CategoryPlot plot = chart.getCategoryPlot();
		CategoryAxis domainAxis = plot.getDomainAxis();
		Font xfont = new Font("黑体", Font.BOLD, 10);// X轴  
		domainAxis.setTickLabelFont(xfont);  
		
		
		/*
		 * BarRenderer barrenderer = (BarRenderer) plot.getRenderer();
		 * barrenderer.setItemMargin(2.0D);
		 * barrenderer.setMaximumBarWidth(10.0);
		 * plot.setRenderer(barrenderer);
		 * barrenderer.setDrawBarOutline(true);
		 */

		CategoryItemRenderer render = plot.getRenderer();
		//BarRenderer renderer = (BarRenderer) catPlot.getRenderer();
		render.setBaseItemLabelGenerator(new  StandardCategoryItemLabelGenerator());
		render.setBaseItemLabelsVisible(true);
		render.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
		render.setItemLabelsVisible(false);
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		domainAxis.setLowerMargin(0.01);
		domainAxis.setUpperMargin(0.01);
		plot.setOutlinePaint(new   Color(0, 0, 0));
		plot.setBackgroundAlpha(0.0f);
		plot.setBackgroundPaint(null);
		render.setSeriesPaint(0, new   Color(100, 218, 238));
		render.setSeriesPaint(1, new   Color(205, 229, 236));
		render.setSeriesPaint(2, new   Color(47, 176, 212));
		render.setSeriesPaint(3, new   Color(60, 118, 250));
		render.setSeriesPaint(4, new   Color(125, 196, 252));
		render.setSeriesPaint(5, new   Color(52, 163, 193));
		render.setSeriesPaint(6, new   Color(41, 86, 143));
		render.setSeriesPaint(7, new   Color(127, 93, 170));
		render.setSeriesPaint(8, new   Color(160, 205, 78));
		render.setSeriesPaint(9, new   Color(65, 129, 203));
		render.setSeriesPaint(10, new   Color(235, 201, 166));
		render.setSeriesPaint(11, new   Color(196, 61, 57));
		return chart;
	}

	@SuppressWarnings("deprecation")
	protected JFreeChart getPieChart( ) {
		PieChartDataSet ds = (PieChartDataSet) this.ds;

		if (null == ds) {
			// System.out.println("Failed to get data from session!!!!!!!!!!");
			return null;
		}

		JFreeChart chart = ChartFactory.createPieChart3D(ds.title, ds.ds, true,
				false, false);
		chart.setBackgroundPaint(Color.WHITE);
		PiePlot3D plot3D = (PiePlot3D) chart.getPlot();

		// 设置百分比
		plot3D.setLabelGenerator(new StandardPieSectionLabelGenerator(
				"{0}:{2}", NumberFormat.getNumberInstance(), new DecimalFormat(
						"0%")));

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setBackgroundPaint(Color.decode("#FFFFFF"));
		plot.setIgnoreNullValues(true);
		plot.setIgnoreZeroValues(true);
		// 设置旋转角度
		plot.setStartAngle(290);
		// 设置旋转方向
		plot.setDirection(Rotation.CLOCKWISE);
		// 设置透明度
		plot.setForegroundAlpha(1f);
		plot.setLabelFont((new Font("黑体", Font.PLAIN, 10)));
		// 设置柱的透明度
		plot.setForegroundAlpha(0.8f);
		plot.setOutlinePaint( new   Color(62, 62, 62));    
		plot.setLabelOutlinePaint(null);
        plot.setSectionPaint(0, new   Color(101, 205, 233));
       
        plot.setSectionPaint(1, new   Color(160, 210, 244));
		plot.setSectionPaint(2, new   Color(124, 161, 191));
		plot.setSectionPaint(3, new   Color(60, 118, 192));
		plot.setSectionPaint(4, new   Color(125, 196, 252));
		plot.setSectionPaint(5, new   Color(52, 163, 193));
		plot.setSectionPaint(6, new   Color(41, 86, 143));
		plot.setSectionPaint(7, new   Color(127, 93, 170));
		plot.setSectionPaint(8, new   Color(160, 205, 78));
		plot.setSectionPaint(9, new   Color(65, 129, 203));
		plot.setSectionPaint(10, new   Color(235, 201, 166));
		plot.setSectionPaint(11, new   Color(196, 61, 57));
		//plot.setSectionPaint(1, Color.decode("#a5d9da"));
		return chart;
	}

	@SuppressWarnings("deprecation")
	protected JFreeChart getTrendsChart( ) {
		TrendsChartDataSet ds = (TrendsChartDataSet) this.ds;

		if (null == ds) {
			// System.out.println("Failed to get data from session!!!!!!!!!!");
			return null;
		}

		JFreeChart chart = ChartFactory.createTimeSeriesChart(ds.title,
				ds.CatecategoryAxisLabel, ds.valueAxisLabel, ds.ds, true, true,
				true);
		Font xfont = new Font("宋体", Font.PLAIN, 12); // X轴

		XYPlot xyPlot = (XYPlot) chart.getPlot();
		
		//xyPlot.setBackgroundPaint(Color.GREEN);
		xyPlot.setDomainCrosshairVisible(true);
		xyPlot.setRangeCrosshairVisible(true);
		xyPlot.setOutlinePaint(new   Color(62, 62, 62));
		xyPlot.setBackgroundPaint(null);
		xyPlot. setRangeGridlinePaint(new Color(62, 62, 62));
		DateAxis domainAxis = (DateAxis) xyPlot.getDomainAxis();
		// 设置日期格式
		domainAxis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd"));
		domainAxis.setTickUnit(new DateTickUnit(DateTickUnitType.DAY, 1));
		domainAxis.setTickLabelFont(xfont);// 轴数值
		XYItemRenderer xyItemRenderer = xyPlot.getRenderer();
		if (xyItemRenderer instanceof XYLineAndShapeRenderer) {
			XYLineAndShapeRenderer xyLineAndShapeRenderer = (XYLineAndShapeRenderer) xyItemRenderer;
			xyLineAndShapeRenderer.setShapesVisible(true); // 数据点可见
			xyLineAndShapeRenderer.setShapesFilled(true); // 数据点是实心点
			xyLineAndShapeRenderer.setSeriesFillPaint(0, Color.BLUE); // 数据点填充为蓝色
			xyLineAndShapeRenderer.setUseFillPaint(true);// 将设置好的属性应用到render上
		}
		return chart;
	}

	@SuppressWarnings("deprecation")
	protected JFreeChart getMonthTrendsChart( ) {
		TrendsChartDataSet ds = (TrendsChartDataSet) this.ds;

		if (null == ds) {
			// System.out.println("Failed to get data from session!!!!!!!!!!");
			return null;
		}

		JFreeChart chart = ChartFactory.createTimeSeriesChart(ds.title,
				ds.CatecategoryAxisLabel, ds.valueAxisLabel, ds.ds, true, true,
				true);
		Font xfont = new Font("宋体", Font.PLAIN, 12); // X轴

		XYPlot xyPlot = (XYPlot) chart.getPlot();
		
		//xyPlot.setBackgroundPaint(Color.GREEN);
		xyPlot.setDomainCrosshairVisible(true);
		xyPlot.setRangeCrosshairVisible(true);
		xyPlot.setOutlinePaint(new   Color(62, 62, 62));
		xyPlot.setBackgroundPaint(null);
		xyPlot. setRangeGridlinePaint(new Color(62, 62, 62));
		DateAxis domainAxis = (DateAxis) xyPlot.getDomainAxis();
		// 设置日期格式
		domainAxis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd"));
		domainAxis.setTickUnit(new DateTickUnit(DateTickUnitType.DAY, 3));
		domainAxis.setTickLabelFont(xfont);// 轴数值
		XYItemRenderer xyItemRenderer = xyPlot.getRenderer();
		if (xyItemRenderer instanceof XYLineAndShapeRenderer) {
			XYLineAndShapeRenderer xyLineAndShapeRenderer = (XYLineAndShapeRenderer) xyItemRenderer;
			xyLineAndShapeRenderer.setShapesVisible(true); // 数据点可见
			xyLineAndShapeRenderer.setShapesFilled(true); // 数据点是实心点
			xyLineAndShapeRenderer.setSeriesFillPaint(0, Color.BLUE); // 数据点填充为蓝色
			xyLineAndShapeRenderer.setUseFillPaint(true);// 将设置好的属性应用到render上
		}
		return chart;
	}
	
	@SuppressWarnings("deprecation")
	protected JFreeChart getYearTrendsChart( ) {
		TrendsChartDataSet ds = (TrendsChartDataSet) this.ds;

		if (null == ds) {
			// System.out.println("Failed to get data from session!!!!!!!!!!");
			return null;
		}

		JFreeChart chart = ChartFactory.createTimeSeriesChart(ds.title,
				ds.CatecategoryAxisLabel, ds.valueAxisLabel, ds.ds, true, true,
				true);
		Font xfont = new Font("宋体", Font.PLAIN, 12); // X轴

		XYPlot xyPlot = (XYPlot) chart.getPlot();
		
		//xyPlot.setBackgroundPaint(Color.GREEN);
		xyPlot.setDomainCrosshairVisible(true);
		xyPlot.setRangeCrosshairVisible(true);
		xyPlot.setOutlinePaint(new   Color(62, 62, 62));
		xyPlot.setBackgroundPaint(null);
		xyPlot. setRangeGridlinePaint(new Color(62, 62, 62));
		DateAxis domainAxis = (DateAxis) xyPlot.getDomainAxis();
		domainAxis.setVerticalTickLabels(false);
		// 设置日期格式
		domainAxis.setTickUnit(new DateTickUnit(DateTickUnit.MONTH, 1, new SimpleDateFormat("yyyy-MM"))); 
		domainAxis.setTickLabelFont(xfont);// 轴数值
		XYItemRenderer xyItemRenderer = xyPlot.getRenderer();
		if (xyItemRenderer instanceof XYLineAndShapeRenderer) {
			XYLineAndShapeRenderer xyLineAndShapeRenderer = (XYLineAndShapeRenderer) xyItemRenderer;
			xyLineAndShapeRenderer.setShapesVisible(true); // 数据点可见
			xyLineAndShapeRenderer.setShapesFilled(true); // 数据点是实心点
			xyLineAndShapeRenderer.setSeriesFillPaint(0, Color.BLUE); // 数据点填充为蓝色
			xyLineAndShapeRenderer.setUseFillPaint(true);// 将设置好的属性应用到render上
		}
		return chart;
	}
	@Override
	protected void finalize() throws Throwable {
		ds = null;
	}
	
}
