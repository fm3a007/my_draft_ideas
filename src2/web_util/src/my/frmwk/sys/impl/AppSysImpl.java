/**
 * \file
 * @see AppSysImpl
 *
 * copyright (C) 2000, 2013, xxxx, co.,ltd
 *
 * @author David.Liang
 *
 * @version 1.0
 *
 */

package my.frmwk.sys.impl;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import my.frmwk.sys.AppSys;
import my.frmwk.util.Logger;
import my.frmwk.util.LoggerFileImpl;
import my.frmwk.util.LoggerOutputForDebug;

/**
 * @see AppSys.
 * 
 * 对AppContext的实现.
 * 
 * @author administrator
 *
 */
public class AppSysImpl implements AppSys {
	

	/**
	 * 构造函数,需要外部提供AppConf和Hibernate的Configuration对象.
	 * 
	 * 由外部提供直接可用的对象是为了可方便地将该类作为模块整合到更高阶的系统中.
	 */
	public AppSysImpl( Configuration hibernateCfg ) {
		logger = new LoggerOutputForDebug();
		hibernateConfig = hibernateCfg;
	}

	/**
	 * 构造函数,需要外部提供系统参数配置文件和Hibernate配置文件.
	 * 
	 * 通过配置文件来加载配置,能方便地把该类直接作为一个独立的完整系统使用.
	 */
	public AppSysImpl( String logerFilePath, String hibernateCfgPath ) {
		hibernateConfig = new Configuration();
		hibernateConfig.configure( hibernateCfgPath);
		logger = new LoggerFileImpl(logerFilePath);
	}

	/**
	 * 启动系统(后台任务).
	 * @return
	 */
	public	int	start(){
		return	0;
	}
	
	/**
	 * 停止系统(后台任务).
	 * @return
	 */
	public	int	stop(){
		return	0;
	}

	/**
	 * @see AppSys
	 */
	public	Logger	getLogger(){
		return logger;
	}
	
	/**
	 * 获得 **主数据库** 的一个新连接, 注意:使用者请保管好该连接!
	 * 
	 * 由于数据库连接是全局资源, 而且关系到系统整体性能, 
	 * 应避免占用不必要的连接, 并且尽快释放.
	 * 
	 * @return 
	 */
	public	Session	getDbSession(){
		return	getSessionFactory().openSession();
	}

	protected Logger logger;
	protected SessionFactory sessionFactory;
	protected Configuration hibernateConfig;
	protected Session logSession;
	
	
	/**
	 * 返回hibernate的session工厂, 已做了断线重连处理.
	 * @return
	 */
	protected	SessionFactory getSessionFactory(){
		if (sessionFactory == null||sessionFactory.isClosed()){
			rebuildSessionFactory();
		}
		return	sessionFactory;
	}
	
	private void rebuildSessionFactory(){
		try{
			sessionFactory = hibernateConfig.buildSessionFactory();
		}catch (Exception e){
			System.err.println("%%%% Error Creating SessionFactory %%%%");
			e.printStackTrace();
		}
	}
	

	@Override
	protected void finalize() throws Throwable {
		stop();
		this.hibernateConfig = null;
		this.logger = null;
		if( null != this.logSession){
			this.logSession.close();			
			this.logSession = null;
		}
		if( null!=sessionFactory){
			this.sessionFactory.close();
			this.sessionFactory = null;
		}
		
		super.finalize();
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

}
