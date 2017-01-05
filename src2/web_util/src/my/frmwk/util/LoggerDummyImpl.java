/**
 * \file
 * 
 * 对 Logger 接口的哑元实现.
 * 这个类不输出任何日志, 简化各功能模块的内部逻辑处理.
 *
 * These source files are released under the GPLv3 license.
 *
 */
package my.frmwk.util;

/**
 * @author David
 *
 */
public class LoggerDummyImpl extends Logger {
	
	/* (non-Javadoc)
	 * @see my.webframework.util.Logger#log(int, int, java.lang.String, int, int)
	 */
	@Override
	public int log(int level, int MOD_COD, int uid, String msg, int status) {
		return ++m_nId;
	}

	/* (non-Javadoc)
	 * @see my.webframework.util.Logger#update_log(int, int)
	 */
	@Override
	public int update_log(int log_id, int status) {
		return status;
	}
	
	public static Logger	getTheDummyLogger(){
		return		theDummyLoger;
	}
	
	protected	static	Logger	theDummyLoger = new LoggerDummyImpl();
	
	protected	int		m_nId = 0;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
