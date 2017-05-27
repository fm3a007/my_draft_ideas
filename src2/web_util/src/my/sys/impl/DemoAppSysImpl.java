/**
 * \file
 * 
 * 
 * @author  David
 * 
 * @version 1.0 
 * 
 *
 * \b Modify_log:
 * 	- 2016-12-29 file creation.
 * 
 */ 

package my.sys.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import my.frmwk.sys.AppSys;
import my.frmwk.sys.impl.AppSysImpl;
import my.frmwk.util.Logger;
import my.frmwk.util.LoggerFileImpl;
import my.modules.common.service.LoginService;
import my.modules.common.service.impl.LoginServiceImpl;
import my.sys.BkgTskIntf;
import my.sys.DemoAppSys;

/**
 *	对接口的实现.
 *
 *	这个类要做的事情:
 *	-#,	;
 *
 *	@author David
 *
 */
public class DemoAppSysImpl extends AppSysImpl implements DemoAppSys, Runnable {

	public DemoAppSysImpl(String cfgPath, String hibernateCfgPath) {
		super(cfgPath, hibernateCfgPath,null, null, null, 0 );

		setDefaultConf();
		
		String logPath = getConfString("LogPath");
		String logFile = getConfString("LogFile");
		String logExt = getConfString("LogExt");
		String strLogLevel = getConfString("LogLevel");
		int		logLevel = 2147483647;
		if(null!=strLogLevel){
			logLevel = Integer.parseInt(strLogLevel);
		}
		if( null!=logFile){
			logger = new LoggerFileImpl( logLevel, logFile, logPath, logExt);
		}
		
		m_loginSvcOfAppSys = new LoginServiceOfAppSysItself(this);
		m_loginSvcOfAppSys.login("", "", "System background task");
	}
	
	LoginService m_loginSvcOfAppSys = null;
	
	public	int	start(){

		int logId = logger.log( Logger.L_BS_OPERATE, 0, 0, this.getClass().getSimpleName(), "DemoAppSysImpl.start()");
		
		if( m_bRun || null!= m_bkgThread){
			return	logger.update_log(logId, -1);			
		}
		
		int ret = super.start();
		
		//m_bkgTskList.add(new BkgTskRecord( new YourBkgTsk(this)));

		if(null==m_bkgThread){
			m_bRun = true;
			m_bkgThread = new Thread( this);
			m_bkgThread.start();
		}

		logger.update_log(logId, ret);
		return	ret;
	}
	
	public	int	stop(){
		int	ret = 0;
		int logId = logger.log( Logger.L_BS_OPERATE, 0, 0, this.getClass().getSimpleName(), "DemoAppSysImpl.stop()");
		
		m_bRun = false;
		
		if(null!=m_bkgThread){
			try {
				m_bkgThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			m_bkgThread = null;
		}
		m_bkgTskList.clear();
		
		ret = super.stop();

		logger.update_log(logId, ret);
		return	ret;
	}

	protected	int	setDefaultConf( ){
		String	val = null;

		val = getConfString("XXXX");
		if(null!=val){
			// m_strWeixinAppId = val;
		}
		
		return	0;
	}
	
	protected	Map< String, Object>	m_mapBuf = new HashMap<String, Object>();

	@Override
	public void setBufObj(String key, Object o) {
		if(null==o){
			m_mapBuf.remove(key);
		}
		m_mapBuf.put( key, o);
		
	}

	@Override
	public Object getBufObj(String key) {
		return	m_mapBuf.get(key);
	}

	@Override
	public LoginService getOpKpiAppSysLoginSvcObj() {
		return m_loginSvcOfAppSys;
	}
	
	protected	Thread	m_bkgThread = null;

	@Override
	public void run() {
		try {
			Thread.sleep( 3000); // 延后运行
		} catch (InterruptedException e) {
			//e.printStackTrace();
		}
		while( m_bRun){
			runImpl();
			try {
				Thread.sleep( 300);
			} catch (InterruptedException e) {
				//e.printStackTrace();
			}
		}
	}
	
	protected	List<BkgTskRecord>	m_bkgTskList = new ArrayList<BkgTskRecord>();
	
	protected	void	runImpl(){
		Date now = new Date();
		
		List<BkgTskRecord> finishedTaskList = new ArrayList<>();
		
		int n = m_bkgTskList.size();
		for( int i=0; i<n && m_bRun; ++i){ // quit at once
			BkgTskRecord tsk = m_bkgTskList.get(i);
			Date nextExecTime = tsk.m_tsk.getNextExecTime(); 
			if( null!=nextExecTime && now.after( nextExecTime) 
				&& ( null==tsk.m_lastExec || nextExecTime.after( tsk.m_lastExec) ) )
			{
				tsk.m_lastExec = now;
				tsk.m_lastResult = tsk.m_tsk.execTask();
				if(tsk.m_tsk.isFinished()){
					finishedTaskList.add( tsk);
				}
			}
		}
		
		n = finishedTaskList.size();
		for( int i=0; i<n; ++i){
			BkgTskRecord tsk = finishedTaskList.get(i);
			m_bkgTskList.remove( tsk);
		}		
	}
	
	protected	boolean	m_bRun = false;		//!< 后台线程的运行控制标志

	
	
	protected	static	int	ErrMsgLoadedFlag = loadErrMsg();

	protected	static	int	loadErrMsg(){
		int	id = 0;
		id += LoginServiceImpl.ER_PRIVILEGE;
		if(id==0){
			return	0;
		}
		return	1;
	}
	
}

class	BkgTskRecord{
	
	public	BkgTskRecord( BkgTskIntf tsk){
		m_tsk = tsk;
	}
	
	public	BkgTskIntf	m_tsk = null;
	
	public	Date		m_lastExec = null;
	
	public	int			m_lastResult = 0;
	
}

class LoginServiceOfAppSysItself extends LoginServiceImpl{

	public LoginServiceOfAppSysItself(AppSys app) {
		super(app);
	}
	
	// 以便拥有所有权限
	public	boolean	isAuth( int moduleId, int funcId, int power){
		return	true;
	}
}

