/**
 * \file
 * 
 * 该类定义各模块里用到的错误码及其含义.
 * These modules are released under the GPLv3 license.
 * 
 * =============该类定义各模块里用到的错误码及其含义===========。
 * 
 * 
 * 
 * 
 *  
 * These source files are released under the GPLv3 license.
 *
 * @author 
 *
 */

package my.frmwk.util;


import java.util.HashMap;
import java.util.Map;



/**
 * 错误码 和 错误说明的转换类.
 * 
 * 注意： 错误码的号码段规划: 
 * -#, 这里定义公用的错误码, 各个业务模块的错误码不要定义在这里;
 * -#, 公用错误码使用号码段在 -1 ~ -32767 之间的值;
 * -#, 各个业务模块的错误码以 -100000 * 模块编号 + 模块内部错误码的方式; 
 * 
 * @author Liang,David
 * 
 */
public class ErrorCode {

	private static Map<Integer, ErrMsg> msgs = new HashMap<Integer, ErrMsg>();

	// common error code
	public final static int OK = add(0, "OK","操作成功");
	public final static int ER_UNKNOWN = add(-32768, "Unkonw Error.","未知状态");

	public final static int ER_SYSTEM_EXCEPTION = add(-1, "System exception!","系统异常");
	public final static int ER_MAIN_DB_CONN_FAILED = add(-2, "Main database connection failure!","数据库连接失败");
	public final static int ER_FILE_SYSTEM_EXCEPTION = add(-3, "File system exception!","文件系统异常");
	public final static int ER_ILLEGAL_PARAMETER = add(-4, "Illegal parameter!","无效参数");
	public final static int ER_READ_CONFIG_FILE_FAILED = add(-5, "Failed to read configuration file!","读取配置文件失败");
	public final static int ER_SAVE_CONFIG_FAILED = add(-6, "Failed to saving configuration!","保存配置失败");
	public final static int ER_DBMS_OP_FAILED = add(-7, "DBMS operation failed!","数据库操作失败");
	public	final static int ER_NETWORK_FAILED = add(-8, "Network communication error!", "网络通讯出错");
	public	final static int ER_NO_PRIVILEGE = add(-9, "Insufficient privilege", "权限不足");
	public final static int ER_UNAUTHORIZED = add(-10, "User is NOT authorized!","用户无法通过身份验证!");
	public final static int ER_OPERATE_THROW_EXCEPIOTN = add(-11, "Operation throw exception","操作异常");
	public final static int ER_LOGIN_CONFIG_FAILED = add(-12, "Login failure!","登录失败");
	


	// *** CAUTION *** 其他程序片段的错误码请勿定义在自己的代码文件,以方便维护.


	/**
	 * 错误码转换成消息.
	 * 
	 * 该方法用于将错误码转换成消息。
	 * 
	 * @param code
	 *            错误码
	 * @return 错误消息
	 */
	public static String getMsgByCode(int code) {
		return	getMsgByCode( code, false);
	}
	
	public static Map<Integer,ErrMsg> getErrMsg( ){
		Map<Integer,ErrMsg> map = new HashMap<Integer, ErrMsg>();
		map.putAll( msgs);
		return	map;
	}

	public static String getMsgByCode(int code, boolean local) {
		ErrMsg val = msgs.get( code);
		if(null==val){
			return	"";
		}
		return	local ? val.msg2 : val.msg;
	}

	/** 这个类内部用, 兼容原来的程序. */
	protected static int add( int errCode, String errMsg,
			String errMsgLocal) 
	{
		return	add( 0, errCode, errMsg, errMsgLocal);
	}
	
	/**
	 * 注册错误码与错误信息
	 * 
	 * 各人请将自己负责的业务程序的错误码和描述通过该方法进行注册
	 */
	public synchronized static int add( int modCod, int errCode, String errMsg,
			String errMsgLocal) 
	{
		int errCode2 = EX_ER( modCod, errCode);
		ErrMsg val = msgs.get( errCode2);
		if( null!=val){
			try {
				throw new Exception("错误码已存在！");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		msgs.put( errCode2,  new ErrMsg(errCode2, errMsg, errMsgLocal));
		return errCode2;
	}
	
	/** 转换成给外部使用的错误码形式 */
	protected	static		int EX_ER( int modCod, int interalErr){
		int err = interalErr>0 ? interalErr : -interalErr;
		err &= 0x0FFFF;			// 除止重复调用
		err += (modCod*100000);
		return -err;
	}
	
	// 1,要写构造方法,避免编译器自动生成构造方法
	// 2,必需是私有方法,避免各处代码自己拥有该类对象
	private ErrorCode() {
	};

	/**
	 * 用于单元测试
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		// System.out.println( ErrorCode.getMsgByCode(ERR_XXMOD_XXXERROR));

	}

}

