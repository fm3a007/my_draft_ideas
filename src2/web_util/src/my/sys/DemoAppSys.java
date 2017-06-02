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

package my.sys;

import my.frmwk.sys.AppSys;
import my.modules.common.service.LoginService;

/**
 *	微信公众号 系统对象.
 *
 *	系统运行时所需的前后文环境对象在这个类的对象中维护.
 *
 *	@author David
 *
 */
public interface DemoAppSys extends AppSys {

	/**
	 * 设置缓存对象.
	 *
	 * 方便上层业务程序把需要缓存的数据存在在系统对象中.\n
	 * \n
	 *
	 * @param key
	 * @param o: 若 o为 null则表示将对象从缓存中清掉.
	 */
	public	void	setBufObj( String key, Object o);

	/**
	 * 取回缓存的对象.
	 *
	 * @param key
	 * @return 若 key 没有对应的对象则返回 null. 
	 */
	public	Object	getBufObj( String key);
	
	/**
	 * 获取代表系统自己的登录对象.
	 *
	 * 有了这个对象就可以方便后台任务直接使用 Wrap层的程序. \n
	 * \n
	 * 调用方式: [Y]多线程并发调用, [Y]后台定时触发/[N]外部触发 \n
	 */
	public	LoginService	getLoginSvcObjOfAppSys();
	
}
