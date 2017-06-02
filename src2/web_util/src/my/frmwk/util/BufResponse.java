/**
 * \file
 * 
 * These source files are released under the GPLv3 license.
 *
 * @author Liang,David
 * 
 */


package my.frmwk.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * 用于拦载JSP页面输出(以便用作其他用途)的一个缓冲类.
 * 
 * 用法: 用这个类的实例替换JSP 中的response对象即可.
 * 暂时只实现了拦载HTML内容的功能.
 * 
 * 
 * @author David
 *
 */
public class BufResponse implements HttpServletResponse {

	/** 保存旧的Response对象, 备用 */
	protected HttpServletResponse oldResponse = null;
	
	/** 缓冲区 */
	protected ByteArrayOutputStream buf = new ByteArrayOutputStream();

	/** 为符合Servlet标准的输出流对象 */
	protected ServletOutputStream svrOs = new MyServletOutputStream( buf);
	
	/** 为符合Servlet标准的输出流对象 */
	protected PrintWriter prtr = null;
	
	public BufResponse( HttpServletResponse oldObj){
		oldResponse = oldObj;
		OutputStreamWriter osWrt = null;
		try {
			osWrt = new OutputStreamWriter(buf,"UTF-8");
			prtr = new PrintWriter( osWrt);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}finally{
			if(null==osWrt){
				prtr = new PrintWriter( buf);
			}
		}
	}

	/**
	 * @see javax.servlet.ServletResponse#flushBuffer()
	 */
	@Override
	public void flushBuffer() throws IOException {
		prtr.flush();
		svrOs.flush();
	}

	/**
	 * @see javax.servlet.ServletResponse#getBufferSize()
	 */
	@Override
	public int getBufferSize() {
		return buf.size();
	}

	/**
	 * @see javax.servlet.ServletResponse#getCharacterEncoding()
	 */
	@Override
	public String getCharacterEncoding() {

		return null;
	}

	/**
	 * @see javax.servlet.ServletResponse#getContentType()
	 */
	@Override
	public String getContentType() {

		return null;
	}

	/**
	 * @see javax.servlet.ServletResponse#getLocale()
	 */
	@Override
	public Locale getLocale() {

		return null;
	}

	/**
	 * @see javax.servlet.ServletResponse#getOutputStream()
	 */
	@Override
	public ServletOutputStream getOutputStream() throws IOException {

		return svrOs;
	}

	/**
	 * @see javax.servlet.ServletResponse#getWriter()
	 */
	@Override
	public PrintWriter getWriter() throws IOException {
		return prtr;
	}

	/**
	 * @see javax.servlet.ServletResponse#isCommitted()
	 */
	@Override
	public boolean isCommitted() {

		return false;
	}

	/**
	 * @see javax.servlet.ServletResponse#reset()
	 */
	@Override
	public void reset() {


	}

	/**
	 * @see javax.servlet.ServletResponse#resetBuffer()
	 */
	@Override
	public void resetBuffer() {


	}

	/**
	 * @see javax.servlet.ServletResponse#setBufferSize(int)
	 */
	@Override
	public void setBufferSize(int arg0) {


	}

	/**
	 * @see javax.servlet.ServletResponse#setCharacterEncoding(java.lang.String)
	 */
	@Override
	public void setCharacterEncoding(String arg0) {


	}

	/**
	 * @see javax.servlet.ServletResponse#setContentLength(int)
	 */
	@Override
	public void setContentLength(int arg0) {


	}

	/**
	 * @see javax.servlet.ServletResponse#setContentType(java.lang.String)
	 */
	@Override
	public void setContentType(String arg0) {


	}

	/**
	 * @see javax.servlet.ServletResponse#setLocale(java.util.Locale)
	 */
	@Override
	public void setLocale(Locale arg0) {


	}

	/**
	 * @see javax.servlet.http.HttpServletResponse#addCookie(javax.servlet.http.Cookie)
	 */
	@Override
	public void addCookie(Cookie arg0) {


	}

	/**
	 * @see javax.servlet.http.HttpServletResponse#addDateHeader(java.lang.String, long)
	 */
	@Override
	public void addDateHeader(String arg0, long arg1) {


	}

	/**
	 * @see javax.servlet.http.HttpServletResponse#addHeader(java.lang.String, java.lang.String)
	 */
	@Override
	public void addHeader(String arg0, String arg1) {


	}

	/**
	 * @see javax.servlet.http.HttpServletResponse#addIntHeader(java.lang.String, int)
	 */
	@Override
	public void addIntHeader(String arg0, int arg1) {


	}

	/**
	 * @see javax.servlet.http.HttpServletResponse#containsHeader(java.lang.String)
	 */
	@Override
	public boolean containsHeader(String arg0) {

		return false;
	}

	/**
	 * @see javax.servlet.http.HttpServletResponse#encodeRedirectURL(java.lang.String)
	 */
	@Override
	public String encodeRedirectURL(String arg0) {

		return null;
	}

	/**
	 * @see javax.servlet.http.HttpServletResponse#encodeRedirectUrl(java.lang.String)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public String encodeRedirectUrl(String arg0) {
		if( null!=oldResponse){
			return	oldResponse.encodeRedirectUrl( arg0);
		}
		return null;
	}

	/**
	 * @see javax.servlet.http.HttpServletResponse#encodeURL(java.lang.String)
	 */
	@Override
	public String encodeURL(String arg0) {
		if( null!=oldResponse){
			return	oldResponse.encodeRedirectURL( arg0);
		}
		return null;
	}

	/**
	 * @see javax.servlet.http.HttpServletResponse#encodeUrl(java.lang.String)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public String encodeUrl(String arg0) {
		if( null!=oldResponse){
			return	oldResponse.encodeUrl( arg0);
		}
		return null;
	}

	/**
	 * @see javax.servlet.http.HttpServletResponse#sendError(int)
	 */
	@Override
	public void sendError(int arg0) throws IOException {


	}

	/**
	 * @see javax.servlet.http.HttpServletResponse#sendError(int, java.lang.String)
	 */
	@Override
	public void sendError(int arg0, String arg1) throws IOException {


	}

	/**
	 * @see javax.servlet.http.HttpServletResponse#sendRedirect(java.lang.String)
	 */
	@Override
	public void sendRedirect(String arg0) throws IOException {


	}

	/**
	 * @see javax.servlet.http.HttpServletResponse#setDateHeader(java.lang.String, long)
	 */
	@Override
	public void setDateHeader(String arg0, long arg1) {


	}

	/**
	 * @see javax.servlet.http.HttpServletResponse#setHeader(java.lang.String, java.lang.String)
	 */
	@Override
	public void setHeader(String arg0, String arg1) {


	}

	/**
	 * @see javax.servlet.http.HttpServletResponse#setIntHeader(java.lang.String, int)
	 */
	@Override
	public void setIntHeader(String arg0, int arg1) {


	}

	/**
	 * @see javax.servlet.http.HttpServletResponse#setStatus(int)
	 */
	@Override
	public void setStatus(int arg0) {


	}

	/**
	 * @see javax.servlet.http.HttpServletResponse#setStatus(int, java.lang.String)
	 */
	@Override
	public void setStatus(int arg0, String arg1) {


	}
	
	/** 返回旧的 Response对象 */
	public HttpServletResponse getOldResponse(){
		return oldResponse;
	}

	/** 返回缓冲区的内容 */
	public byte[] getBuf(){
		try {
			flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buf.toByteArray();
	}
	
	public void clear(){
		buf.reset();
	}
	
	protected void finalize() throws Throwable{
		buf.reset();
		buf.close();
		buf = null;
		super.finalize();
	}

	@Override
	public String getHeader(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<String> getHeaderNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<String> getHeaders(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getStatus() {
		// TODO Auto-generated method stub
		return 0;
	}
	

}

class MyServletOutputStream extends ServletOutputStream{

	protected OutputStream os = null;
	
	public MyServletOutputStream( OutputStream os){
		this.os = os;
	}

	@Override
	public void write(int b) throws IOException {
		os.write(b);
	}
	
	
	
}

