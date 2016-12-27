<%@page 
	import="my.frmwk.util.FilePrinterAction"
	language="java" pageEncoding="utf-8"
	contentType="application/octet-stream;"

%><%

// 这只是个示例说明如何使用 FilePrinterAction
// 请只行在使实现指定用户文件夹的逻辑
	String	filePath = "D:/temp"; 

	FilePrinterAction action= new FilePrinterAction( 
			request, response, application, filePath,session);
	action.process();%>