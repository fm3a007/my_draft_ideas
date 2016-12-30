/**
 * \file
 * 最终产品的整合层程序文件, @see theApp .
 *
 * copyright (C) 2000, 2013, xxxx, co.,ltd
 *
 * @author David.Liang
 *
 * @version 1.0
 *
 */

package my.theApp;


import org.hibernate.cfg.Configuration;

import my.frmwk.sys.impl.AppSysImpl;




/**
 * 最终的产品整合实例类.
 * 
 * @author David
 *
 */
public class theApp {

	/** 主配置文件 */
	protected static String cfgPath = "config/sys_conf.conf";

	/** hibernate的配置文件 */
	protected static String hibernateCfg = "config/hibernate.cfg.xml";
	
	public AppSysImpl sys;
	
	/** 系统初始化函数, 有同步锁防止多人并发调用该函数. */
	public static synchronized	int	init( ){
		
		System.out.println("the App initialization ... ");
		
		int	iRet = -1;
		try{
			if( null==app){
				app = new theApp( );
				iRet = 0;
			}
		}catch(Exception e){
			System.err.println(
					"\n\n\n"+
					"*******************************************************\n" +
					"Failed to start System: ");
			e.printStackTrace(System.err);
			System.err.println(
					"\n\n\n"+
					"*******************************************************");
		}finally{
		}
		return	iRet;
	}
	
	public static	theApp getInst(){
		return	app;
	}
	
	public	int	destroy(){
		System.out.println("the App destroy() ...");
		sys.stop();
		return	0;
	}

	/** 单例 */
	protected static  theApp app;

	/** 除止外部构造 */
	protected theApp( ) {
		String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		path += cfgPath;
		sys = new AppSysImpl( path,  hibernateCfg, null);
		sys.start();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// for functionality test	
		theApp.init();
		theApp app = theApp.getInst();
	}

}
