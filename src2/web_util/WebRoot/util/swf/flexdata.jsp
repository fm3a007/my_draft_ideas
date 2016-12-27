<%@ page language="java" pageEncoding="utf-8"%>
<%@ page contentType="text/xml; charset=utf-8"%>
<%@ page import="org.jfree.chart.*"%>
<%@ page import="org.jfree.chart.ChartUtilities"%>
<%@ page import="my.frmwk.util.chart.*" %>
<%@ page import="java.util.*" %>
<% 
	String	c = pageContext.getRequest().getParameter("c");
	String	t = pageContext.getRequest().getParameter("t");
	SessionDatasetProxy ssPrx = new SessionDatasetProxy( pageContext.getSession());
	if(null==c||c.length()==0||null==t||t.length()==0){
		//return	null;
		System.exit(0);
	}
	int iC = Integer.parseInt(c);
	int iT = Integer.parseInt(t);
	System.out.println("iT==="+iT);
	//Map map = (Map)request.getAttribute("reportmap");
	//Map map = (Map)ssPrx.getData(iC, true);
%>
<?xml version="1.0" encoding="UTF-8"?>
<chart>
	
<% 
  if(1==iT||2==iT) {
	FlexTrends pei = (FlexTrends)ssPrx.getData(iC, true);
%>
	<title><%=pei.getTitle()%></title>
	<name>
	<% 
		//System.out.println("<name>");
		List<ReportVo> list = pei.getList();
		if(list!=null&&list.size()>0){
			for(int i = 1;i<=list.size();i++){
				//System.out.println("<n"+i+">"+list.get(i-1).getKey()+"</n"+i+">");
			%>
			<n<%=i%>><%=list.get(i-1).getKey()%></n<%=i%>>
			<%	
			}
		//System.out.println("<name>");
	%>
	</name>
	<%
			for(ReportVo vo:list){
			//System.out.println(vo.getKey()+":"+vo.getCounts());
		%>
		<el>
			<n><%=vo.getKey()%></n>
			<d1><%=vo.getCounts()%></d1>
		</el><% 
			} 
		}%>
<% } else if(3==iT||4==iT) {
	FlexTrends trends = (FlexTrends)ssPrx.getData(iC, true);
%>
	<title><%=trends.getTitle()%></title>
	<series><%=trends.getGroup()%></series>
	<name>
	<%
		List<ReportVo> list = trends.getList();
		List<String> datelist = trends.getDatelist();
		if(list!=null&&list.size()>0){
			for(int i = 1;i<=list.size();i++){
			%>
			<n<%=i%>><%=list.get(i-1).getKey()%></n<%=i%>>
			<%	
			}
	%>
	</name>
	<%
	
		
		
			for(int i =1; i<=list.get(0).getCountlist().size();i++){
	%>
		<el>	
			<n><%=datelist.get(i-1)%></n>
			<%
				//System.out.println("<el>");
				//System.out.println("  <n>时间"+datelist.get(i-1)+"</n>");
				for(int j=1;j<=list.size();j++){
				
				//System.out.println("  <d"+j+">"+list.get(j-1).getCountlist().get(i-1)+"</d"+j+">");
			%>
			<d<%=j%>><%=list.get(j-1).getCountlist().get(i-1)%></d<%=j%>>
			<% } %>
			
		</el>
	<% 
		//System.out.println("</el>");
			} 
		}
	%>

<% } else { %>

<% }
ssPrx.clearData(iC);
//System.out.println("cleardate:ic="+iC);
 %>

</chart>
