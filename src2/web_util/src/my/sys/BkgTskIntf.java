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

package my.sys;

import java.util.Date;

/**
 *	后台任务接口.
 *
 *	需要后台定时执行的任务,按这个接口实现, 方便 AppSys定时调用.
 *	需要实时处理的后台任务不能用这种机制; 用这种机制的任务不能
 *	运行太长时间.
 *
 *	@author David
 *
 */
public interface BkgTskIntf {
	
	/** 下次执行的时间 */
	public	Date	getNextExecTime();
	
	/**
	 * 被定时调用的任务方法.
	 *
	 * 调用方式: [N]多线程并发调用, [Y]后台定时触发/[N]外部触发 \n
	 * 调用频率: 峰值[ 1 ]( t/s), 平均值[ 按需 ](t/s). \n
	 * 处理时长: max< [1000] (millisecond), avg<[  ]( millisecond). \n
	 * \n
	 * @return 暂时未定义.
	 * \n
	 * 设计: Liang,David \n
	 * 审核: Liang,David \n
	 */
	public	int		execTask();
	
	/**
	 * 任务完成后(下次不需要再被调用 execTask()时),这个函数要返回 true.
	 *
	 * \n
	 * 调用方式: [N]多线程并发调用, [Y]后台定时触发/[N]外部触发 \n
	 * 调用频率: 峰值[ 10 ]( t/s), 平均值[ 1 ](t/s). \n
	 * 处理时长: max< [ 1 ] (millisecond). \n
	 * \n
	 * @return 返回真表示这个任务不再被执行.
	 * \n
	 * 设计: Liang,David \n
	 * 审核: Liang,David \n
	 */
	public	boolean	isFinished();
	
	/** 初始化函数, 会在调用 execTask() 之前调用一次. */
	public	int		initialise();
	
	/** 初始化函数, 会在调用 销毁对象 之前调用一次. */
	public	int		destroy();

}
