/**
 * \file
 * @see Logger .
 *  
 * 
 * copyright (C) 2000, 2013, xxxx, co.,ltd
 * 
 * 
 * @author David
 *  
 */
package my.frmwk.util;


/**
 * 日志记录对象的接口定义.
 * 
 * 统一定义好日志的记录接口, 以便日志的最终存储形式,方法即使
 * 变化了, 各功能模块自身的逻辑并不需要修改. 
 * 
 * 用法: \n
 * 执行任何业务操作前, 应先调用int	log( int MOD_COD, int uid, String msg),
 * 记录用户准备进行的操作, 当操作执行完毕之后,再调用
 * update_log( int log_id, int status) 更新日志的状况(结果).
 * \n
 * 此举(分两步记录)的目的在于可以预防系统崩溃或掉电时无法追朔发生了什么事.\n
 * 如果某些特殊情况下, 在记日志时已知道了状态(结果),可调用另一个log方法.
 * \n
 * 其他的几个参数较少的方法是对log方法再次封装的便捷方法.
 * 
 * @author David
 *
 */
public abstract class Logger {

	/** 日志级别常量定义, 调试日志 */
	public final static int	L_DEBUG = 1;
	
	/** 日志级别常量定义,程序信息 */
	public final static int	L_INFO = 2;
	
	/**日志级别常量定义, 系统错误(异常)日志 */
	public final static int	L_ERROR = 4;
	
	/** 日志级别常量定义, 普通(查询类)业务日志 */
	public final static int	L_BS_QUERY =  1<<8;

	/** 日志级别常量定义, 业务操作类日志  */
	public final static int	L_BS_OPERATE = 1 << 9;
	
	
	/** 日志状态常量定义, 代表未知状态（操作没执行完成） */
	//public final static int STAT_UNKNOWN = ErrorCode.ER_UNKNOWN;
	// 上面语句是为了让STAT_UNKNOWN的取值更易维护,
	// 对于要把Logger单独使用而没有ErrorCode类时,可用下面这句
	public final static int STAT_UNKNOWN = -32768;

	

	/**
	 * 最终的日志记录方法入口, 其他便捷入口最后都是调用该方法.
	 * 
	 * 除update_log(int log_id, int status)之外, 其他便捷方法都
	 * 只是这个方法的简化形式, 以方便调用者使用.\n
	 * 而集中在该方法上才做日志记录的实现, 是为了能方便地控制最日志的
	 * 最终输出形式,方法.
	 * 
	 * @param MOD_COD 模块代号
	 * @param uid	用户ID, 如果是系统后台程序的操作,用户ID记为 0.
	 * @param msg	附加消息
	 * @param status 日志的状态(操作执行的结果)
	 * @param level	日志的级别, 见本类的常量定义
	 * @return 当status为"未知状态"时,该方法的返回是日志记录ID(大于0); 0表示成功, 负数为出错.
	 */
	public abstract	int	log( int MOD_COD, int uid, String msg, int status, int level);

	
	/**
	 * 记录业务日志的方法, 简化参数方便使用.
	 */
	public	int	log( int MOD_COD, int uid, String msg){
		return log(MOD_COD, uid,  msg, STAT_UNKNOWN);
	}

	/**
	 * 记录业务日志的方法, 简化参数方便使用.
	 */
	public	int log( int MOD_COD, int uid, String msg, int status ){
		return log(MOD_COD, uid, msg, status,L_DEBUG);
	}

	/**
	 * 用于更新日志状态的方法.
	 * 见 int log( int MOD_COD, int uid, String msg) 的说明.
	 * 
	 * @param log_id 日志记录ID, int log( int MOD_COD, int uid, String msg)的迫返回值
	 * @param status 日志的状态(操作执行的结果)
	 * @return 返回 status 的值, 方便应用程序使用连等式赋值.
	 */
	public abstract	int update_log( int log_id, int status);


	/**
	 * 记录业务日志的方法, 简化参数方便使用.
	 */
	public int error(int MOD_COD, String msg) {
		return log(MOD_COD, 0, msg, 0, L_ERROR);
	}
	
	
	/**
	 * 记录业务日志的方法, 简化参数方便使用.
	 */
	public int debug(int MOD_COD, String msg) {
		return log(MOD_COD, 0, msg, 0, L_DEBUG);
	}

}
