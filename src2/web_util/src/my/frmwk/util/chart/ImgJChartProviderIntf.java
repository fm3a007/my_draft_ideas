/**
 * \file
 * 工具类报表图片 -- 接口定义.
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

import org.jfree.chart.JFreeChart;

/**
 *	上层应用逻辑按这个接口来提供图片数据.
 *
 *
 *	@author David
 *
 */
public interface ImgJChartProviderIntf {

	/**
	 * 若需要通过 img.jsp来输出更多自定义的图片，上层的开发人员需按这个接口来提供绘图对象.
	 *
	 * 在空多一行之后详细的说明（若有必要），可多行。 \n
	 * \n
	 *
	 * @return 返回一个 JFreeChart对象.
	 */
	public JFreeChart getChart();
	
}
