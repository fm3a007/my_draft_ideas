/**
 * \file
 * @see LoginService
 *
 *
 *
 * @author David.Liang
 *
 * @version 1.0
 *
 *  
 * These source files are released under the GPLv3 license.
 */
package my.modules.common.service;

import my.frmwk.sys.AppSys;

/**
 * 登录检验服务程序,其他需要权限控制的服务应依赖于该类的对象进行用户身份检验.
 * 
 * @author David
 *
 */
public interface LoginService {

	int getErCode();

	boolean isLogin();

	/**
	 * 用user和psw登录系统.
	 * 
	 * @param user
	 * @param psw
	 * @return 0 - 成功,非0为错误码. 除返回值外,还设置该类的ErCode.
	 */
	int login(String user, String psw, String desc);

	int logout();

	String getUserName();

	int getUserId();
	
	/**
	 * 检查当前用户是否有 moduleId模块的 funcId 功能是授权.
	 */
	boolean	isAuth( int moduleId, int funcId, int power);
	
	/**
	 * 获得访问的来源.
	 */
	String	getClientSrc();

	AppSys getAppContext();

}