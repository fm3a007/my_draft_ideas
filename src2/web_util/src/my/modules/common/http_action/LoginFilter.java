/**
 * \file
 * @see LoginFilter .
 *
 * copyright (C) 2000, 2013, xxxx, co.,ltd
 *
 * @author David.Liang
 *
 * @version 1.0
 *
 */
package my.modules.common.http_action;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import my.modules.common.service.LoginService;


/**
 * Servlet Filter implementation class loginFilter
 */
public class LoginFilter implements Filter {
	public final static String LOGIN_SERVICE_OBJ = "loginCheckerObj";

	/**
	 * Default constructor.
	 */
	public LoginFilter() {
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		if (request instanceof HttpServletRequest) {
			HttpServletRequest hreq = (HttpServletRequest) request;
			String ctPath = hreq.getContextPath();
			ctPath = null==ctPath ? "" : ctPath.trim();
			ctPath += "/";
			String path = hreq.getRequestURI();
			if ( path.contains("login.jsp") ) {
				;
			}else{
				HttpSession ses = hreq.getSession();
				LoginService o = (LoginService) ses.getAttribute(LOGIN_SERVICE_OBJ);
				if (null == o || !(o instanceof LoginService) || !o.isLogin()) {
					String qStr = hreq.getQueryString();
					if( null!=qStr){
						qStr = "?"+qStr;
						path+= URLEncoder.encode(qStr);
					}
					String redirect = ctPath+"login.jsp";
					if( !path.equals(ctPath)){
						redirect += "?method=reject";
						// 登录超时跳转到login.jsp即可,没必要登再次返回当前页面,因为是框架式的页面.
						//redirect += "&req_url="+path;
					}
					((HttpServletResponse) response).sendRedirect( redirect);
					return;
				}
			}
		}
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
