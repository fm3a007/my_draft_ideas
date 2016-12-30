/**
 *
 * These source files are released under the GPLv3 license.
 *
 *
 */

package my.frmwk.util;

/**
 * 
 * 错误代码-信息项对照.
 * 
 * 该类包括:错误码 和 错误说明 两者的对照.
 *
 * @author David
 * 
 */
public class ErrMsg{
	/**
	 * 错误代码
	 */
	public int code;
	/**
	 * 错误信息
	 */
	//英文
	public String msg;
	//当地语言
	public String msg2;

	/**
	 * ErrMsg: 错误信息项 类的构造函数
	 * @param code:	错误码
	 * @param msg:	错误信息
	 */
	public ErrMsg(int code, String msg)
	{
		this.code = code;
		this.msg = msg;
	}
	
	public ErrMsg(int code, String msg,String msg2)
	{
		this.code = code;
		this.msg = msg;
		this.msg2 = msg2;
	}
	
	@Override
	protected void finalize() throws Throwable {
		msg = null;
		msg2 = null;

		super.finalize();
	}
}