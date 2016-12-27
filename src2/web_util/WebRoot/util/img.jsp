<%@ page language="java" pageEncoding="utf-8"
%><%@ page contentType="image/jpeg"
%><%@ page import="org.jfree.chart.*"
%><%@ page import="org.jfree.chart.ChartUtilities"
%><%@ page import="my.frmwk.util.chart.ImgProxy" 
%><% 
	ImgProxy	img = new ImgProxy( pageContext);
	int width =600;
	int height = 400; 
	if(request.getParameter("width")!=null){
		width = Integer.parseInt(request.getParameter("width"));
	}
	// 这段是由于先前的人变量命名时张冠李戴, 还留在这里是为了兼容其他已经做好了的程序.
	if(request.getParameter("size")!=null){
		height = Integer.parseInt(request.getParameter("size"));
	}
	if(request.getParameter("height")!=null){
		height = Integer.parseInt(request.getParameter("height"));
	}
	if(request.getParameter("w")!=null){
		width = Integer.parseInt(request.getParameter("w"));
	}
	if(request.getParameter("h")!=null){
		height = Integer.parseInt(request.getParameter("h"));
	}
	JFreeChart chart  =  img.getChart( );
	if(null!=chart){
		ChartUtilities.writeChartAsJPEG(response.getOutputStream(),chart, width , height);
		out.clear();
		out = pageContext.pushBody();
	}
%>