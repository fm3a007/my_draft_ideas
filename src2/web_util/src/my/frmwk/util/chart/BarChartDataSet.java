/**
 * BarChartDataSet.java
 */

/**
 * DataSet for storage of BarChart
 * 
 * @author David
 * Date:	2011-3-6
 *
 */

package my.frmwk.util.chart;

import org.jfree.data.category.DefaultCategoryDataset;


/**
 * @author lianghh
 * 柱状图数据集jfreechar
 */
public class BarChartDataSet {
	
	public	String	title;
	public	DefaultCategoryDataset ds;
	public	String	CatecategoryAxisLabel;
	public	String	valueAxisLabel;
	
	public	BarChartDataSet( String title, DefaultCategoryDataset ds,
			String CatecategoryAxisLabel, String valueAxisLabel){
		this.title = title;
		this.ds = ds;
		this.CatecategoryAxisLabel=CatecategoryAxisLabel; 
		this.valueAxisLabel = valueAxisLabel;
		
	}

}
