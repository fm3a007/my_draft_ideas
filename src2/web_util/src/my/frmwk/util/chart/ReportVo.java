/**
 *	\file
 * These source files are released under the GPLv3 license.
 */


package my.frmwk.util.chart;

import java.util.List;
import java.util.Map;

/**
 * @author David
 * 报表显示vo，专门用来显示报表所需的内容
 */
public class ReportVo {
	
	public String getKeyurl() {
		return keyurl;
	}
	public void setKeyurl(String keyurl) {
		this.keyurl = keyurl;
	}
	public ReportVo() {
		super();
//		this.counts = (long)0;
	}
	/**
	 * 事件级别
	 */
	private String eventlevel;
	
	/**
	 * 要显示的东西主键id,key是该id对应的name
	 */
	private String keyid;
	
	/**
	 * 要显示的东西，比如主机，状态，类型等等
	 */
	private String key;
	
	/**
	 * 	要显示的报表链接
	 */
	private String keyurl;
	
	/**
	 * 数据集
	 */
	private List<String> countlist;
	
	/**
	 * 时间对应countlist数据集
	 */
	private List<Integer> datelist; 
	
	/**
	 * 数量
	 */
	private Long counts;
	
	/**
	 * 百分比
	 */
	private String per;
	
	/**
	 * 主机
	 */
	private String hostid;
	
	/**
	 * 时间
	 */
	private String time;
	
	/**
	 * 分析
	 */
	private String analysis;
	
	/**
	 * 建议
	 */
	private String suggest;
	
	private String starttime;
	
	private String endtime;
	
	/**
	 * 显示集合，比如string可以为时间，long为该时间点的数量
	 */
	private Map<Integer,Long> map;
	
	public List<Integer> getDatelist() {
		return datelist;
	}
	public void setDatelist(List<Integer> datelist) {
		this.datelist = datelist;
	}
	public List<String> getCountlist() {
		return countlist;
	}
	public void setCountlist(List<String> countlist) {
		this.countlist = countlist;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
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
	public String getHostid() {
		return hostid;
	}
	public void setHostid(String hostid) {
		this.hostid = hostid;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getPer() {
		return per;
	}
	public void setPer(String per) {
		this.per = per;
	}
	public Long getCounts() {
		return counts;
	}
	public void setCounts(Long counts) {
		this.counts = counts;
	}
	public String getEventlevel() {
		return eventlevel;
	}
	public void setEventlevel(String eventlevel) {
		this.eventlevel = eventlevel;
	}
	public Map<Integer, Long> getMap() {
		return map;
	}
	public void setMap(Map<Integer, Long> map) {
		this.map = map;
	}
	public String getKeyid() {
		return keyid;
	}
	public void setKeyid(String keyid) {
		this.keyid = keyid;
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
