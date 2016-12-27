/**
 * \file
 * 工具类报表图片.
 * 
 * 
 * 
 * copyright (C) 2001, 2011, xxxx, co.,ltd
 * 
 * @author zyj
 *  
 * @version 1.0
 * 
 * @see 
 * Modify log:
 * 	 - 2011-7-29 zyj
 *   - 2013-11-22 Liang,David 将纯粹的功能与WEB环境分离, 以方便重用.
 * 
 */
package my.frmwk.util.chart;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.PageContext;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.ui.TextAnchor;
import org.jfree.util.Rotation;

/**
 * @description 工具类报表图片.
 * @author zyj
 * @version 1.0
 */
public class ImgProxy extends ImgService {

	// //////////////////////
	// attributes
	private PageContext pgContext;
	private SessionDatasetProxy ssPrx;

	/**
	 * constructor without input, for debug
	 * 
	 */
	public ImgProxy() {
		super(null, 0);
		// constructor without input, for debug
		ssPrx = null;
		pgContext = null;
	}

	/**
	 * 
	 * @param pg
	 *            : page contexts from jsp container
	 */
	public ImgProxy(PageContext pg) {
		super(null, 0);
		pgContext = pg;
		ssPrx = new SessionDatasetProxy(pg.getSession());
	}

	/**
	 * 
	 * @return null if error occur, otherwise a jfree chart
	 * 
	 */
	public JFreeChart getChart() {

		if (null == pgContext)
			return null;
		String c = pgContext.getRequest().getParameter("c");
		String t = pgContext.getRequest().getParameter("t");

		if (null == c || c.length() == 0 || null == t || t.length() == 0) {
			return null;
		}

		int chartId = Integer.parseInt(c);
		ds = ssPrx.getData(chartId, true);
		// release session_storage_data
		ssPrx.clearData(chartId);
		return getChart( Integer.parseInt(t));
	}


	@Override
	protected void finalize() throws Throwable {
		pgContext = null;
		ssPrx = null;
		super.finalize();
	}

}
