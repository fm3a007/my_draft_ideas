/**
 * \file
 * 最终产品的整合层程序文件, @see theApp .
 *
 *
 *
 * @author David.Liang
 *
 * These source files are released under the GPLv3 license.
 *
 * @version 1.0
 *
 */

package my.theApp;

import my.sys.impl.DemoAppSysImpl;

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
	
	public DemoAppSysImpl sys;
	
	/** 系统初始化函数, 有同步锁防止多人并发调用该函数. */
	public static synchronized	int	init( ){
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
		sys.stop();
		return	0;
	}

	/** 单例 */
	protected static  theApp app;

	/** 除止外部构造 */
	protected theApp( ) {
		String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		path += cfgPath;
		sys = new DemoAppSysImpl( path,  hibernateCfg);
		sys.start();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// for functionality test	
		theApp.init();
		theApp app = theApp.getInst();
		System.out.println( "theApp app:" +app.toString());
	}

}
