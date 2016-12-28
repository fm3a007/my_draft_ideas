/**
 * \file
 * @see AppSys
 *
 * copyright (C) 2000, 2013, xxxx, co.,ltd
 *
 * @author David.Liang
 *
 * @version 1.0
 *
 */

package my.frmwk.sys;

import org.hibernate.Session;

import my.frmwk.util.Logger;

/**
 * 应用系统实例的前后文环境对象.
 * 
 * 该对象中管理的是应用系统实例在运行时的前后文环境,如全局数据, 全局资料等.
 * 
 * 
 * @author Liang,David
 *
 */
public interface AppSys {
	

	/**
	 * 启动系统(后台任务).
	 * @return
	 */
	public	int	start();
	
	/**
	 * 停止系统(后台任务).
	 * @return
	 */
	public	int	stop();

		
	/**
	 * @see Logger
	 * @return 返回日志记录器对象
	 */
	public	Logger	getLogger();
	

	/**
	 * 获得 **主数据库** 的一个新连接, 注意:使用者请保管好该连接!
	 * 
	 * 由于数据库连接是全局资源, 而且关系到系统整体性能, 
	 * 应避免占用不必要的连接, 并且尽快释放.
	 * 
	 * @return 
	 */
	public	Session	getDbSession();


	/**
	 * 获取配置参数，以 String类型返回.
	 *
	 * @param key 配置项名, 格式: path1/path2/path3/.../conf_key
	 * @return	配置项的值,若无对应的配置项则返回 null.
	 */
	public	String	getConfString( String key);
	
	/**
	 * 获取配置参数，作用同 getConfString(),以 float类型返回.
	 */
	public	float	getConfFloat( String key);
	
}
