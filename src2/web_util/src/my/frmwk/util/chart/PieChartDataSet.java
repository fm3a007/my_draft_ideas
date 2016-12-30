/**
 * BarChartDataSet.java
 * These source files are released under the GPLv3 license.
 *
 */

/**
 * DataSet for storage of BarChart
 * 
 * @author David
 * Date:	2011-3-6
 *
 */

package my.frmwk.util.chart;

import org.jfree.data.general.DefaultPieDataset;

/**
 * @author David
 * 饼状图数据集jfreechar
 */
public class PieChartDataSet {
	
	public	String	title;
	public	DefaultPieDataset ds;
	
	public	PieChartDataSet( String title, DefaultPieDataset ds){
		this.title = title;
		this.ds = ds;
	}

}
