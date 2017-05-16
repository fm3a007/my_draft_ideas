<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="my.frmwk.util.chart.*"%>
<%
SessionDatasetProxy sPrx = new SessionDatasetProxy(request.getSession());
demo_report_process prs = new demo_report_process();
BarChartDataSet bar = prs.getBarChartReport();
PieChartDataSet pie = prs.getPieChartReport();
TrendsChartDataSet trend = prs.getTrendsChartReport();
ImgService imgSvc = new ImgService(pie,2);
%>
<html>
<head>
<title>系统报表</title>
<script type="text/javascript" src="js/myTime.js"></script> 
<style type="text/css">
</style>
<script type="text/javascript">
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../css/report.css" rel="stylesheet" type="text/css">
</head>

<body>
<div class="bodydiv">
<dl class="tbox light">
  <dt>&nbsp;&nbsp;系统报表<span>&gt;&gt;</span>XXX报表</dt>
</dl>
</div>
 

<div class="frame"> <span class="frame_label">报表</span> 
  <table width="708" border="0" cellspacing="0" cellpadding="0" summary="dddddddddddddddd">
    <caption>
    <strong>XXXXX报表</strong> 
    </caption>
		<tr> 
		  <td width="78">标题：</td>
		  <td width="584">事件趋势图</td>
		  <td>&nbsp;</td>
		</tr>
		<tr> 
		  <td>说明:</td>
		  <td>指定时间内不同事件类型的柱状图</td>
		  <td>&nbsp;</td>
		</tr>
		<tr> 
		  <td>开始时间：</td>
		  <td>${dtStartDateTime}</td>
		  <td>&nbsp;</td>
		</tr>
		<tr> 
		  <td>结束时间</td>
		  <td>2010-10-11 11:11:11</td>
		  <td>&nbsp;</td>
		</tr>
		<tr> 
		  <td>排序规则</td>
		  <td>按事件数量降序</td>
		  <td>&nbsp;</td>
		</tr>
		<tr> 
		  
      <td colspan="2"><img src="img.jsp?c=<%=sPrx.saveData( bar)%>&t=1&h=640&w=999" width="600" height="400"></td>
		  <td>1</td>
		</tr>
		<tr>
		  <td colspan="2">&nbsp;</td>
		  <td>&nbsp;</td>
    </tr>
		<tr>
		  <td colspan="2"><img src="img.jsp?c=<%=sPrx.saveData( imgSvc)%>&t=0&h=640&w=999" width="600" height="400"></td>
		  <td>2</td>
    </tr>
		<tr>
		  <td colspan="2"><img src="img.jsp?c=<%=sPrx.saveData( pie)%>&t=2&h=640&w=999" width="600" height="400"></td>
		  <td>2</td>
    </tr>
		<tr>
		  <td colspan="2">&nbsp;</td>
		  <td>&nbsp;</td>
    </tr>
		<tr>
		  <td colspan="2"><img src="img.jsp?c=<%=sPrx.saveData( trend)%>&t=3&h=640&w=999" width="600" height="400"></td>
		  <td>3</td>
    </tr>
		<tr>
		  <td colspan="2">&nbsp;</td>
		  <td>&nbsp;</td>
    </tr>
		<tr>
		  <td colspan="2"><img src="img.jsp?c=<%=sPrx.saveData( bar)%>&t=4&h=640&w=999" width="600" height="400"></td>
		  <td>4</td>
    </tr>
		<tr>
		  <td colspan="2">&nbsp;</td>
		  <td>&nbsp;</td>
    </tr>
		<tr>
		  <td colspan="2"><img src="img.jsp?c=<%=sPrx.saveData( bar)%>&t=5&h=640&w=999" width="600" height="400"></td>
		  <td>5</td>
    </tr>
		<tr>
		  <td colspan="2"><img src="img.jsp?c=<%=sPrx.saveData( bar)%>&t=6&h=640&w=999" width="600" height="400"></td>
		  <td>6</td>
    </tr>
    </tr>
    </table>
</div>
</body>
</html>
<%
	System.out.println(request.getRequestURI()+" is called! end -------------");
%>