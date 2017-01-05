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

import java.io.File;

import my.frmwk.sys.AppSys;
import my.frmwk.util.Logger;
import my.frmwk.util.LoggerFileImpl;
import my.frmwk.util.LoggerOutputForDebug;
import my.modules.common.ErrorCode;


/**
 * 登录检验服务程序,其他需要权限控制的服务应依赖于该类的对象进行用户身份检验.
 * 
 * @author David
 *
 */
public class LoginService
{
	private String user;
	private String psw;
	private boolean bLogin;
	private AppSys app;
	private Logger loger;
	private int	iErCode;
	private int m_privilege;
	
	
	/**
	 * 登录到app系统中.
	 */
	public LoginService( AppSys app) {
		iErCode = 0;
		user = null;
		bLogin = false;
		this.app = app;
		//TODO:防止配置中心日志重复记录，设置临时路径存放重复日志记录
		this.loger = app.getLogger();
	}
	
	public	int	getErCode(){
		return	iErCode;
	}

	public boolean isLogin(){
		return bLogin;
	}

	/**
	 * 用user和psw登录系统.
	 * 
	 * @param user
	 * @param psw
	 * @return 0 - 成功,非0为错误码. 除返回值外,还设置该类的ErCode.
	 */
	public int login(String user, String psw, String desc) {
		this.user = user;
		this.psw = psw;
		int id = loger.log( Logger.L_BS_OPERATE, 0, 0, user+" attempt to login, "+desc);
		int ret = -1; //super.login(user, psw,desc);
		bLogin = ret==0;
		ret = ret==0?0:ErrorCode.ER_UNAUTHORIZED;
		loger.update_log(id, ret);
		return	ret;
	}

	public int logout() {
		bLogin = false;
		user = null;
		psw = null;
		// TODO: super.logout( "User request to logout.");
		return 0;
	}

	public String getUserName() {
		return user;
	}
	public int getUserId() {
		return -1; // TODO:
	}
	
	
	public AppSys getAppContext() {
		return app;
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		my.theApp.theApp.init();
		my.theApp.theApp app = my.theApp.theApp.getInst();
		app.sys.start();
		//app.commSys.start();
		LoginService login = new LoginService( app.sys);
		login.login( "aaa", "bbb", "cccc");
		
		app.sys.stop();
		//app.commSys.stop();

	}

	
	
}
