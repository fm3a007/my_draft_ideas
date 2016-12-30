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

package my.modules.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import my.frmwk.util.ErrMsg;


/**
 * 错误码 和 错误说明的转换类.
 * 
 * 注意： 错误码必须小于-10000
 * 
 * @author David
 * 
 */
public class ErrorCode {

	private static ArrayList<ErrMsg> msgs = new ArrayList<ErrMsg>();

	// common error code
	public final static int OK = add(0, "OK","操作成功");
	public final static int ER_UNKNOWN = add(-32768, "Unkonw Error.","未知状态");
	public final static int ER_SYSTEM_EXCEPTION = add(-19998, "System exception!","系统异常");
	public final static int ER_UNAUTHORIZED = add(-19997, "User is NOT authorized!","用户无法通过身份验证!");
	public final static int ER_MAIN_DB_CONN_FAILED = add(-19996, "Main database connection failure!","数据库连接失败");
	public final static int ER_FILE_SYSTEM_EXCEPTION = add(-19995, "File system exception!","文件系统异常");
	public final static int ER_NOT_LOGIN = add(-19994, "Haven't login!","未登录");
	public final static int ER_INSUFFICIENT_PERMISSIONS = add(-19993, "Insufficient permissions!","权限不足");
	public final static int ER_OPERATE_THROW_EXCEPIOTN = add(-19992, "Operation throw exception","操作异常");
	public final static int ER_ILLEGAL_PARAMETER = add(-19991, "Illegal parameter!","无效参数");
	public final static int ER_LOGIN_CONFIG_FAILED = add(-19990, "Login failure!","登录失败");
	public final static int ER_READ_CONFIG_FILE_FAILED = add(-19989, "Failed to read configuration file!","读取配置文件失败");
	public final static int ER_SAVE_CONFIG_FAILED = add(-19988, "Failed to saving configuration!","保存配置失败");
	// service error code
	public final static int ER_MONITORED_HOST_ALREADY_EXIST = add(-19980, "monitored host ip address already exist.","监控主机ip已存在");

	// action error code
	public final static int ER_ADD_CONFIG_SET = add(-19800, "create config set failure.","修改配置设置失败");
	public final static int ER_BEGIN_TIME_SET = add(-19801, "Start time should be earlier than end time.","开始时间大于结束时间");
	public final static int ER_SET_LOG_CONFIG = add(-19802, "set log config error.","设置日志配置错误");
	public final static int ER_PARSE_XML_STREAM = add(-19803, "parser xml stream error.","解析XML文件错误");
	public final static int ER_SET_LOG_MONITORNING = add(-19804, "set log monitoring error.","设置日志监控错误");
	public final static int ER_ADD_PREDEFINE_OBJ = add(-19805, "add predefine object error.","增加预定义对象失败");
	public final static int ER_DEL_PREDEFINE_OBJ = add(-19806, "delete predefine object error.","删除预定义对象失败");
	public final static int ER_DEL_CONFIG_SET = add(-19807, "del config set failure.","删除配置项失败");
	public final static int ER_EMIAL_NUM_ERR = add(-19808, "Email format error.","邮件格式错误");
	public final static int ER_PHONE_NUM_ERR = add(-19809, "Phone format error.","电话格式错误");
	public final static int ER_SET_DEPLOY_MODE = add(-19810, "set deploy mode error.","设置部署模式错误");
	public final static int ER_NO_SELECT_LAN = add(-19811, "Please select LAN.","请选择网口");
	public final static int ER_URL_PARM_ERROR = add(-19812, "URL parameters error.","URL参数错误");
	public final static int ER_ADD_RULE_TO_SERVER_ERROR = add(-19813, "Rules have been applied to the server, can not add again.","规则已应用到主机，不能重复添加");
	public final static int ER_NO_SELECT_RULES = add(-19814, "Please select detection rules.","请选择检测规则");
	public final static int ER_NO_SELECT_HOST = add(-19815, "Please select hosts.","请选择主机");
	public final static int ER_GET_CONFIG = add(-19816, "Can't get config.","无法获取配置");
	public final static int ER_ENEBLED_RULE_EXISTED = add(-19817, "Rules exist,can't add again.","规则已存在，无法重复添加");
	public final static int ER_INVALID_IP = add(-19818, "invalid ip address.","错误的ip格式");
	public final static int ER_SET_MONITOR_HOST = add(-19819, "set monitor host error.","设置监控主机错误");
	public final static int ER_DEL_MONITOR_HOST = add(-19820, "del monitor host error.","删除监控主机错误");
	public final static int ER_POINT_TYPE = add(-19821, "point value must be grant than 0","坐标值必须是正整数.");
	public final static int ER_MONITOR_HOST_EXIST = add(-19822, "the monitored host is already exist.","监控主机已存在.");
	public final static int ER_CONNECT_CONFIG_CENTER = add(-19823, "connect config center error.","连接配置中心失败.");
	public final static int ER_PORT = add(-19824, "Invalid port, please input a value in 0-65535.","端口必须位于0-65535之间.");
	public final static int ER_REFERENCE_OBJ = add(-19825, "You can't delete the object referenced by other config.","该对象在其他地方被引用.");
	public final static int ER_SYS_MODULE_RESTART = add(-19826, "Restart the system module failure.","系统模块重启失败.");
	public final static int ER_WRONG_FORMAT = add(-19827, "invalid param format.","错误的参数格式");
	public final static int ER_ALARM_COUNTS = add(-19828, "Retry count must be greater than 0.","重试次数必须是正整数");
	public final static int ER_ADD_APPLICATION_RANGE = add(-19829, "Rules is not enabled, cannot increase application range.","规则没有被启用,不能添加应用范围.");
	//service error code
	public final static int ER_GETSTATISTICLIST = add(-19900, "Get statistic list error.","获取统计数据失败.");

	

	/** 网卡设置 */
	public final static int MOD_NETWORK = 21001;
	/** 自动升级 */
	public final static int MOD_AUTO_UPDATE = 21002;
	/** 配置中心 */
	public final static int MOD_CONF_COMM = 21003;
	/** 日志中心 */
	public final static int MOD_LOG_COMM = 21004;
	/** 网络通讯方式 */
	public final static int MOD_NETWORK_COMM = 21005;
	/** 用户自定义对象 */
	public final static int MOD_USER_DEFINED_OBJ = 21006;
	/** 部署模式 */
	public final static int MOD_DEPLOY = 21008;
	/** IDS设置 */
	public final static int MOD_IDS_CONF = 21012;
	/** 时间设置 */
	public final static int MOD_TIME = 21015;
	/** 查看配置 */
	public final static int MOD_VIEW_CONF = 21016;
	/**功能模块号常量定义,源IP攻击列表 */
	public final static int MOD_SRC_ATT = 21017;
	/**功能模块号常量定义,目的IP攻击列表 */
	public final static int MOD_DES_ATT_HOST = 21018;
	/**功能模块号常量定义,TOP10攻击源*/
	public final static int MOD_TOP10_SRC_ATT = 21019;
	/**功能模块号常量定义,TOP10被攻击IP列表 */
	public  final static int MOD_TOP10_DES_ATT = 21020;
	/**功能模块号常量定义,网络风险服务情况 */
	public final static int MOD_DES_RIST_DIS = 21021;
	/**功能模块号常量定义,攻击源威胁分布*/
	public final static int MOD_SRC_RIST_DIS = 21022;
	/**功能模块号常量定义,被攻击时间分布 */
	public final static int MOD_DES_ATT_TIME_DIS = 21023;
	/**功能模块号常量定义,攻击时间分布 */	
	public final static int MOD_SRC_ATT_TIME_DIS = 21024;
	/**功能模块号常量定义,明细日志记录 */
	public final static int MOD_DET_LOG_REC = 21025;
	/**功能模块号常量定义,主机监控*/
	public final static int MOD_HOST_MONITO = 21026;
	/** 实时日志 */
	public final static int MOD_ONLINE_LOG = 21027;
	/**功能模块号常量定义,攻击源定位 */
	public final static int MOD_SRC_ATT_LOC = 21028;
	/** 系统状态 */
	public final static int MOD_SYS_STAT = 21029;
	/** 手动升级 */
	public final static int MOD_MANUAL_UPDATE = 21030;
	/** 关机或重启 */
	public final static int MOD_POWER_MNGR = 21031;
	/** 查看日志 */
	public final static int MOD_SHOW_LOG = 21032;
	/**功能模块号常量定义,TOP10受攻击服务*/
	public final static int MOD_DET_ATT_APP = 21033;

	
		
	


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
//		if (0 == code) {
//			return "0表示正常";
//		}
		int s = msgs.size();
		for (int i = 0; i < s; i++) {
			if (msgs.get(i).code == code) {
				return msgs.get(i).msg2;
			}
		}
		return "";
	}
	
	public static Map<Integer,String> getErrMsgStr( boolean local_language){
		Map<Integer,String> map = new HashMap<Integer, String>();
		int n = null == msgs ? 0 : msgs.size();
		for(int i = 0; i < n ; i++){
			ErrMsg errMsg = msgs.get(i);
			map.put(errMsg.code, local_language ? errMsg.msg2 : errMsg.msg);
		}
		return map;
	}

	public static Map<Integer,ErrMsg> getErrMsg( ){
		Map<Integer,ErrMsg> map = new HashMap<Integer, ErrMsg>();
		int n = null == msgs ? 0 : msgs.size();
		for(int i = 0; i < n ; i++){
			ErrMsg errMsg = msgs.get(i);
			map.put(errMsg.code, errMsg);
		}
		return map;
	}

	public static String getMsgByCode(int code, boolean local) {
//		if (0 == code) {
//			return local ? "0表示正常" : "0 means OK";
//		}
		int s = msgs.size();
		for (int i = 0; i < s; i++) {
			if (msgs.get(i).code == code) {
				if (local) {
				return msgs.get(i).msg2;
				}
				return msgs.get(i).msg;

			}
		}

		return "";
	}

	/**
	 * 注册错误码与错误信息
	 * 
	 * 各人请将自己负责的业务程序的错误码和描述通过该方法进行注册
	 */
	public synchronized static int add(int errorCode, String errorMsg,
			String errMsgLocal) {
		boolean flag = true;
		for (ErrMsg msg : msgs) {
			if (msg.code == errorCode) {
				flag = false;
				break;
			}
		}
		if (flag) {
			msgs.add(new ErrMsg(errorCode, errorMsg, errMsgLocal));
		} else {
			try {
				throw new Exception("错误码已存在！");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return errorCode;
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

