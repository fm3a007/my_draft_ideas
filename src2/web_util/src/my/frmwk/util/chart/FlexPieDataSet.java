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

import java.util.Map;


/**
 * @author David
 * 饼状图数据集flex
 */
public class FlexPieDataSet {
	
	private	String	title;
	private	Map<String,Long> map;
	
	public	FlexPieDataSet( String title, Map map){
		this.title = title;
		this.map = map;
	}

	public Map<String, Long> getMap() {
		return map;
	}

	public void setMap(Map<String, Long> map) {
		this.map = map;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
