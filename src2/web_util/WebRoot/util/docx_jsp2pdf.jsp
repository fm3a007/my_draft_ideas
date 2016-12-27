<%@page pageEncoding="utf-8"
%><%@page import="java.io.FileOutputStream"
%><%@ page import="my.frmwk.util.DocJsp2PdfAction" 
%><%

// TODO: 把这句改规范一点, 通过配置来获取命令
//AppContext sys = (AppContext)request.getAttribute(
//		my.modules.common.http_action.AttrDef.AppSys_CONTEXT_NAME);
//String cmd = sys.getAppConf().doc2PdfCmd;
// 下面这句放个你最终需要的转换命令
String cmd = "E:/....";

//String usrPath = sys.getAppConf().userDataPath;
// TODO: 换成最终的路径
String usrPath = "E:/";

DocJsp2PdfAction action= new DocJsp2PdfAction( 
		request, response, application,
		session, cmd);
action.process();%>