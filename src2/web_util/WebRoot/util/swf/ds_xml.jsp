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
	//Map map = (Map)request.getAttribute("reportmap");
	//Map map = (Map)ssPrx.GetData(iC, true);
%>
<?xml version="1.0" encoding="UTF-8"?>
<chart>
	
<% 
  if(1==iT) {
	FlexBarDataSet bar = (FlexBarDataSet)ssPrx.getData(iC, true);
%> 
	<title><%=bar.getTitle()%></title>
	<series><%=bar.getGroup()%></series>
	<% 
		
		Map map = bar.getMap();
		Iterator typeit = map.keySet().iterator();
		Iterator countsit = map.values().iterator();
		while(typeit.hasNext()){
			String eventtype = typeit.next().toString();
			Number counts = (Number) countsit.next();
	%>
	<el>
		<n><%=eventtype%></n>
		<d1><%=counts%></d1>
	</el>
	<% } %>
<% 
  } else if(2==iT) {
	FlexPieDataSet pei = (FlexPieDataSet)ssPrx.getData(iC, true);
%>
	<title><%=pei.getTitle()%></title>
	<% 
		
		Map map = pei.getMap();
		Iterator typeit = map.keySet().iterator();
		Iterator countsit = map.values().iterator();
		while(typeit.hasNext()){
			String eventtype = typeit.next().toString();
			Long counts = (Long) countsit.next();
			
	%>
	<el>
		<n><%=eventtype%></n>
		<d1><%=counts%></d1>
	</el><% } %>
<% } else if(3==iT) {
	FlexTrendsDataSet trends = (FlexTrendsDataSet)ssPrx.getData(iC, true);
%>
	<title><%=trends.getTitle()%></title>
	<series><%=trends.getGroup()%></series>
	<%
		Map<Integer,List<Long>> map = trends.getMap();
		Iterator times = map.keySet().iterator();
		Iterator values = map.values().iterator();
		while(times.hasNext()){
			//System.out.println("<el>");
			Integer time = (Integer) times.next();
			List<Long> value = (List) values.next();
			//System.out.println("\t<n>"+time+"</n>");
			
	%>
	<el>
		<n><%=time%></n>
		<%
			int i = 1;
			for(Long num:value){
			
			//System.out.println("\t<d"+i+">"+num+"</d"+i+">");
		%>
		<d<%=i%>><%=num%></d<%=i++%>>
		<% } %>
		
	</el>
	<% 
		//System.out.println("</el>");
	} %>

<% } else { %>

<% } %>
</chart>
