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

import java.util.List;
import java.util.Map;

import org.jfree.chart.JFreeChart;



/**
 * @author David
 * 曲线图数据集flex
 */
public class FlexTrends {
	
	private	String	title;						//标题
	private	List<ReportVo> list;				//数据集
	private	String	CatecategoryAxisLabel;		//种类
	private	String	valueAxisLabel;				//值
	private Integer group;						//多少组数据
	private JFreeChart chart;					//画图
	private String starttime;					//开始时间
	private String endtime;						//结束时间
	private String analysis;					//分析
	private String suggest;						//建议
	private List<String> datelist;					//时间
	


	public List<String> getDatelist() {
		return datelist;
	}

	public void setDatelist(List<String> datelist) {
		this.datelist = datelist;
	}

	public String getAnalysis() {
		return analysis;
	}

	public void setAnalysis(String analysis) {
		this.analysis = analysis;
	}

	public String getSuggest() {
		return suggest;
	}

	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}

	public JFreeChart getChart() {
		return chart;
	}

	public void setChart(JFreeChart chart) {
		this.chart = chart;
	}

	public Integer getGroup() {
		return group;
	}

	public void setGroup(Integer group) {
		this.group = group;
	}

	

	public List<ReportVo> getList() {
		return list;
	}

	public void setList(List<ReportVo> list) {
		this.list = list;
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

	public	FlexTrends( String title, List<ReportVo> list,
			String CatecategoryAxisLabel, String valueAxisLabel){
		this.title = title;
		this.list = list;
		this.CatecategoryAxisLabel=CatecategoryAxisLabel; 
		this.valueAxisLabel = valueAxisLabel;
		
	}

	public FlexTrends() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	

}
