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

import org.jfree.data.xy.XYDataset;


/**
 * @author David
 * 曲线图数据集jfreechar
 */
public class TrendsChartDataSet {
	
	public	String	title;
	public	XYDataset ds;
	public	String	CatecategoryAxisLabel;
	public	String	valueAxisLabel;
	
	public	TrendsChartDataSet( String title, XYDataset ds,
			String CatecategoryAxisLabel, String valueAxisLabel){
		this.title = title;
		this.ds = ds;
		this.CatecategoryAxisLabel=CatecategoryAxisLabel; 
		this.valueAxisLabel = valueAxisLabel;
		
	}

}
