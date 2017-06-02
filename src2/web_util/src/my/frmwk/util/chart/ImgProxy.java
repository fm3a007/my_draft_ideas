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
 *   -# 2013-11-22 Liang,David 将纯粹的功能与WEB环境分离, 以方便重用.
 * 
 */
package my.frmwk.util.chart;

import javax.servlet.jsp.PageContext;

import org.jfree.chart.JFreeChart;

/**
 * @description 工具类报表图片.
 * @author David
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

		int chartTypeId = Integer.parseInt(t);
		if(0==chartTypeId){
			ImgJChartProviderIntf prvd = (ImgJChartProviderIntf)ssPrx.getData(chartId);
			return	prvd.getChart();
		}

		ds = ssPrx.getData(chartId);
		// release session_storage_data
		ssPrx.clearData(chartId);
		return getChart( chartTypeId);
	}


	@Override
	protected void finalize() throws Throwable {
		pgContext = null;
		ssPrx = null;
		super.finalize();
	}

}
