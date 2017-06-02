/**
 * \file
 * @see AppSysImpl
 *
 *
 *
 * These source files are released under the GPLv3 license.
 *
 * @author David.Liang
 *
 * @version 1.0
 *
 */

package my.frmwk.sys.impl;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

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
 * @author David
 *
 */
public class AppSysImpl implements AppSys {
	

	/**
	 * 构造函数,需要外部提供系统参数配置文件和Hibernate配置文件.
	 * 
	 * 通过配置文件来加载配置,能方便地把该类直接作为一个独立的完整系统使用.
	 * 
	 * @param confPath
	 * @param hibernateCfgPath
	 * @param logFile
	 * @param logPath
	 * @param logExt
	 */
	public AppSysImpl( String confPath, String hibernateCfgPath, String logFile, String logPath, String logExt, int logLevel ) {
		if(null!=confPath){
			loadConf(confPath);
		}
		hibernateConfig = new Configuration();
		hibernateConfig.configure( hibernateCfgPath);
		logger = null==logFile ? new LoggerOutputForDebug() :new LoggerFileImpl( logLevel, logFile,  logPath,  logExt);
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
		if(null!=sessionFactory){
			sessionFactory.close();
		}
		return	0;
	}

	/**
	 * @see AppSys
	 */
	public	Logger	getLogger(){
		return logger;
	}
	
	// 见接口的说明
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
	
	protected	Map<String, String>	m_mapConfList = new HashMap<String, String>();
	
	
	@Override
	public String getConfString(String key) {
		return m_mapConfList.get( key);
	}

	@Override
	public float getConfFloat(String key) {
		float res = 0.0f;
		String str = getConfString( key);
		if(null!=str){
			try{
				res = Float.parseFloat( str);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	
		return	res;
	}
	
	/**
	 * 提供给初始化相关的程序调用，业务逻辑程序不应该用到这个方法.
	 *
	 * @param key
	 * @param val
	 * 
	 * @deprecated <业务逻辑程序不应该用到这个方法>
	 */
	public	void	setConfString( String key, String val){
		m_mapConfList.put(key, val);
	}

	protected	int	loadConf( String confPath ){
		
		Properties pro = new Properties();
		try {
			String path = confPath;
			if(-1 != System.getProperty("os.name").indexOf("Windows")){
				path = path.substring(1);
			}
			FileInputStream file = new FileInputStream(path);
			try {
				Reader reader = new InputStreamReader(file, "UTF-8");
				pro.load(reader);
				Set<Object> keyValue = pro.keySet();
				for (Iterator<Object> it = keyValue.iterator(); it.hasNext();){
					String key = (String) it.next();
					setConfString(key, pro.getProperty(key));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		pro.clear();
		return	0;
	}

}
