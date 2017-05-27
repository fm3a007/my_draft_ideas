/**
 * \file
 * 
 * These source files are released under the GPLv3 license.
 *
 * @author Liang,David
 * 
 */


package my.frmwk.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * @author David
 * 
 */
public class DocJsp2PdfAction {

	private HttpServletRequest request;
	private HttpServletResponse response;
	private static String strFile = "example_report.pdf";
	// default value for debug only
	private String cmd = "java -jar E:/Documents/David/DeskTop/docx2pdf.jar - ";

	public DocJsp2PdfAction(ServletRequest req, ServletResponse resp,
			ServletContext servCon, HttpSession session,
			String converterCmd) {
		request = (HttpServletRequest) req;
		response = (HttpServletResponse) resp;
		if( null != converterCmd ){
			cmd = converterCmd;
		}
	}

	public int process() throws Exception {
		// 取得真正想请求的页面,后面动态调用.
		String reqJsp = request.getParameter("req_page");
		if( null==reqJsp){
			return 0;
		}
		
		BufResponse resp = new BufResponse( response);
		//切换Response对象, 以便拦截下面的页面输出
		request.getRequestDispatcher( reqJsp).forward(request, resp); 
		byte [] buf = resp.getBuf();
		resp.clear(); // 清缓存
		resp = null;
		
		Process converter = Runtime.getRuntime().exec( cmd );
		(new Thread(new StreamDrainer( converter.getErrorStream()))).start();
		(new Thread(new BkgStreamPrinter( buf, converter.getOutputStream()))).start();
//		OutputStream pdfOS = converter.getOutputStream();
//		pdfOS.write( buf);
//		pdfOS.close();


		// http 输出
		response.setHeader("Content-Disposition", ("attachment; filename=\"" + strFile + "\""));
		InputStream pdfIS = converter.getInputStream();
		OutputStream httpOs;
//		buf = new byte[4096]; // 可利用先前的大缓冲区
		try {
			httpOs = response.getOutputStream();
			int len = -1;
			while ((len = pdfIS.read( buf)) != -1) {
				httpOs.write( buf, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			buf = null;
			pdfIS.close();
		}
		@SuppressWarnings("unused")
		int status = converter.waitFor();// TODO: 要处理吗?
		return 0; // or status ?
	}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args){
		
	}
}

// 后台写入(输出给)子进程, 以免输入和输出相互等待导致被阻塞住.
class BkgStreamPrinter implements Runnable  {

	private OutputStream os;
	private InputStream	is;

	public BkgStreamPrinter( InputStream is, OutputStream os) {
		this.is = is;
		this.os = os;
	}

	public BkgStreamPrinter( byte [] buf, OutputStream os) {
		this.is = new ByteArrayInputStream( buf);
		this.os = os;
	}

	public void run() {
		byte [] buf = new byte[4096];
		try {
			int len = 0;
			while( -1 != (len=is.read(buf))){
				os.write( buf, 0, len );
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			buf = null;
			if(null!=os){
				try {
					os.flush();
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				os = null;
			}
			if(null!=is){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				is = null;
			}
		}
	}

}
