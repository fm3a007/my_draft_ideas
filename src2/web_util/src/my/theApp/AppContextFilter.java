/**
 * \file
 * 最终产品的整合层程序文件, @see AppContextSwitchFilter .
 *
 * copyright (C) 2000, 2013, xxxx, co.,ltd
 *
 * @author David.Liang
 *
 * @version 1.0
 *
 */
package my.theApp;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import my.frmwk.sys.AppSys;


/**
 * 为界面模块进行产品实例运行环境切换适配的程序模块.
 * 
 * 界面的程序(JSP及http_action)是按照可重用于多个系统实例的思想设计的,
 * 所以需要最终产品层为其切换到合适的系统环境中.
 * @author David
 */
public class AppContextFilter implements Filter {

	/**
	 * Default constructor.
	 */
	public AppContextFilter() {
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		theApp.getInst().destroy();
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		theApp app = theApp.getInst();
		if( null==app){
			System.err.print(new Date());
			System.err.println("App system is not yet start, try to start it .....");
			theApp.init();
			app = theApp.getInst();
		}
		if(null==app){
			String msg  = "Trying to start app system failed!";
			System.err.println(msg);
			// TODO 如果需要界面有较友好的提示, 可转到其他的页面.
			response.getOutputStream().println( msg);
			return;
		}
		ServletRequest ct = request;
		ct.setAttribute( 
				AppSys.class.toGenericString(), 
				app);
		// pass the request along the filter chain
		try{
			chain.doFilter(request, response);
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			// TODO 检测数据库连接的占用, 程序稳定后可不再检查.
//			IdsDao dao = DebugAssessor.getIdsDao( app.idsSys);
//			if(null!=dao && dao instanceof IdsDaoImpl){
//				((IdsDaoImpl)dao).ResourceCheckEnd();
//			}
			
		}
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		if(0!=theApp.init()){
			throw new ServletException("Failed to Start theApp system!");
		}
	}

}
