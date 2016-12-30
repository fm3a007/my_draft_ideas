/**
 * BarChartDataSet.java
 *  
 * These source files are released under the GPLv3 license.
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
 * 柱状图数据集flex
 */
public class FlexBarDataSet {
	
	private	String	title;
	private	Map map;
	private	String	CatecategoryAxisLabel;
	private	String	valueAxisLabel;
	private Integer group;
	
	public Integer getGroup() {
		return group;
	}

	public void setGroup(Integer group) {
		this.group = group;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public String getCatecategoryAxisLabel() {
		return CatecategoryAxisLabel;
	}

	public void setCatecategoryAxisLabel(String catecategoryAxisLabel) {
		CatecategoryAxisLabel = catecategoryAxisLabel;
	}

	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getValueAxisLabel() {
		return valueAxisLabel;
	}

	public void setValueAxisLabel(String valueAxisLabel) {
		this.valueAxisLabel = valueAxisLabel;
	}

	public	FlexBarDataSet( String title, Map map,
			String CatecategoryAxisLabel, String valueAxisLabel){
		this.title = title;
		this.map = map;
		this.CatecategoryAxisLabel=CatecategoryAxisLabel; 
		this.valueAxisLabel = valueAxisLabel;
		
	}

}
