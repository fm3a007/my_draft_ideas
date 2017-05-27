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
package my.modules.common.service.impl;

import my.frmwk.sys.AppSys;
import my.frmwk.util.ErrorCode;
import my.frmwk.util.Logger;
import my.modules.common.service.LoginService;


/**
 * 登录检验服务程序,其他需要权限控制的服务应依赖于该类的对象进行用户身份检验.
 * 
 * @author David
 *
 */
@SuppressWarnings("unused")
public class LoginServiceImpl implements LoginService
{
	public	final	static	int	MOD_ID = 1;
	
	public	final	static	int	ER_UNAUTHORIZED		= ErrorCode.add( MOD_ID, -1, "User is NOT authorized.","用户无法通过身份验证." );
	public	final	static	int	ER_PRIVILEGE			= ErrorCode.add( MOD_ID, -2, "insufficient privilege.","权限不足." );

	private String user;
	private String psw;
	private boolean bLogin;
	private AppSys app;
	private Logger loger;
	private int	m_iErCode;

	private int m_privilege;
	
	
	/**
	 * 登录到app系统中.
	 */
	public LoginServiceImpl( AppSys app) {
		m_iErCode = 0;
		user = null;
		bLogin = false;
		this.app = app;

		this.loger = app.getLogger();
	}
	
	@Override
	public	int	getErCode(){
		return	(m_iErCode);
	}

	@Override
	public boolean isLogin(){
		return bLogin;
	}

	@Override
	public int login(String user, String psw, String desc) {
		this.user = user;
		this.psw = psw;
		int id = loger.log( Logger.L_BS_OPERATE, 0, 0, user, " attempt to login, "+desc);
		int ret = -1; //super.login(user, psw,desc);
		bLogin = ret==0;
		ret = ret== 0? ErrorCode.OK :ER_UNAUTHORIZED;
		loger.update_log(id, ret);
		return	(ret);
	}

	@Override
	public int logout() {
		bLogin = false;
		user = null;
		psw = null;
		// TODO: super.logout( "User request to logout.");
		return 0;
	}

	@Override
	public String getUserName() {
		return user;
	}

	@Override
	public int getUserId() {
		return -1; // TODO:
	}
	
	
	@Override
	public AppSys getAppContext() {
		return app;
	}


	@Override
	public String getClientSrc() {
		return m_strClientSrc;
	}
	
	public	void	setClientSrc( String src){
		m_strClientSrc = src;
	}
	
	protected	String	m_strClientSrc = "";


	@Override
	public	boolean	isAuth( int moduleId, int funcId, int power){
		// TODO Auto-generated method stub
		return true;
	}

	
}
