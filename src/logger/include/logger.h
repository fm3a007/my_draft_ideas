/**
 * \file
 *	日志记录接口的声明.
 * 
 * copyright Liang,David
 * 
 * @author Liang,David
 * 
 * These source files are released under the GPLv3 license.
 * 
 * \b Modify_log:
 * 	- 2011-12-18 David: file create
 * 
 */ 

#ifndef		_COMMON_UTILITY_LOGGER_HEADER_FILE_
#define		_COMMON_UTILITY_LOGGER_HEADER_FILE_

#include <stdlib.h>
#include <stdio.h>
#include <stdarg.h>

#if !defined ( NULL )
#define NULL 0 
#endif

#if defined( _MSC_VER )

#ifdef LOGGER_EXPORTS
#define CLASS_DLL_API __declspec(dllexport)
#endif

#ifdef LOGGER_IMPORTS
#define CLASS_DLL_API __declspec(dllimport)
#endif

#endif

#if ! defined( CLASS_DLL_API )
#define CLASS_DLL_API 
#endif


#define STAT_UNKNOWN -32768
#define	LOG_PRINTF_BUF_LEN 1024*1
/**
 *	日志记录类,纯虚类,以便动态切换底层的实现.
 *
 *	为了方便整合功能模块到各产品, 日志的记录方式必需能方便地与各产品原来的日志体系结合.
 *	同时, 日志记录体系的变化, 也不应该影响到功能模块. 该接口的设计做法, 就是为达到这两方面
 *	的目的, 让功能模块的开发实现 与 最终的运行环境分离. \n
 *		各个功能模块在使用日志接口时, 应引用 当前的这个虚类, 而不应引用具体的实现类, 以便做到
 *	日志记录的实现机制变化时, 功能模块的程序不须改任何一行代码. \n
 *	\n
 *	\n
 *	该类各函数的用法: \n
 *	执行任何业务操作前, 应先调用int	log( ... ), 记录用户准备进行的操作, 当操作执行完毕之后,再调用
 *	update_log( int log_id, int status) 更新日志的状况(结果).
 *	\n
 *	此举(分两步记录)的目的在于可以预防系统崩溃或掉电时无法追朔发生了什么事.\n
 *	如果某些特殊情况下, 在记日志时已知道了状态(结果),也可以在调用log(...) 时直接给出状态值(默认是
 *	STAT_UNKNOWN)或调用另外几个函数( info(...),debug(...),eror(...) ), 一步到位地记录日志.
 *	\n
 *	其他的几个参数较少的方法是对log方法再次封装的便捷方法.

 */
class CLASS_DLL_API Logger
{	
public:

	inline	Logger(int nLogLvlMask) :m_nLogLvlMsk(nLogLvlMask){};

	virtual	~Logger();

	/** 日志级别定义：低8位留给基本的级别，8位以上用于定义各种业务类型 */
	enum	LOG_LEVEL{
		L_DEBUG				= 1,		//!< 日志级别,调试
		L_INFO				= 2,		//!< 日志级别,信息
		L_WARN				= 4,		//!< 日志级别,告警
		L_ERROR				= 8,		//!< 日志级别,错误

		L_BS_QUERY			= 1<<8,		//!< 日志级别,业务查询
		L_BS_OPPERATE		= 1<<9, 	//!< 日志级别,业务操作

		L_UNDEF		// 让上一个枚举ID后面可以始终加上","
	};

	/**
	 * 记日志的方法.
	 *
	 * 该函数根据日志级别用不同方式来记录日志(依据配置).\n
	 * \n
	 * 调用方式: [Y]多线程并发调用, [Y]后台定时触发/[Y]外部触发\n
	 * 调用频率: 峰值[300](t/s), 平均值[1](t/s).\n
	 * 处理时长: max< [80] (millisecond) storage to local DBMS/FILE SYSTEM, avg<[  ](millisecond). \n
	 * 单次调用数据处理量: max>=[   ] records, avg>=[  ]records.\n
	 *
	 * @param log_level 日志级别
	 * @param fun_code 程序功能代号
	 * @param u_id 用户ID,若非用户直接操作(如系统后台定时程序)填0
	 * @param msg 日志内容
	 * @param status 日志(操作)结果
	 * @param fun_name 函数名为了方便使用 __FUNCTION__ 来获得调试信息
	 * @param line_no 代码所在行号,为了方便使用 __LINE__ 来获得调试信息
	 *
	 * @return 当前日志需要记录时返回日志ID,不需要记录或不需要更新状态时(在后面调用 log_stat())返回0.
	 */
	virtual unsigned int log( int log_level, int fun_code, int u_id, const char * msg, int status=STAT_UNKNOWN, 
		const char * fun_name=NULL, int line_no=0) = 0;

	/**
	 * 记录业务操作的状态.
	 *
	 * 调用方式: [Y]多线程并发调用, [Y]后台定时触发/[Y]外部触发\n
	 * 调用频率: 峰值[300](t/s), 平均值[1](t/s).\n
	 * 处理时长: max< [80] (millisecond) storage to local DBMS/FILE SYSTEM, avg<[  ](millisecond). \n
	 * 单次调用数据处理量: max>=[   ] records, avg>=[  ]records.\n
	 *
	 * @param log_id 如果log_id不是有效的日志ID,则不进行任何操作.
	 * @param status 状态,表示业务操作的执行结果
	 *
	 * @return	int	返回 status, 方便用户在代码中 return logger.log_stat(..., status) 这样使用.
	 */
	virtual int log_stat( unsigned int log_id, int status) = 0;

	/**
	 * @see int log( int log_level, int fun_code, int u_id, char * msg, int status=STAT_UNKNOWN)
	 */
	inline	void info( int fun_code, const char * msg, int status = 0){
		log( L_INFO, fun_code, 0, msg, status);
	}

	/**
	 * @see int log( int log_level, int fun_code, int u_id, char * msg, int status=STAT_UNKNOWN)
	 */
	inline	void debug( int fun_code, const char * msg, int status = 0){
		log( L_DEBUG, fun_code, 0, msg, status);
	}

	/**
	 * make conveniency for: loger.debug( __FUNCTION__, __LINE__, ...);
	 */
	inline	void debug( const char * fun_name, int line_no, const char * msg, int status=0){
		log( L_DEBUG, 0, 0, msg, status, fun_name, line_no);
	}

	/**
	 * @see int log( int log_level, int fun_code, int u_id, char * msg, int status=STAT_UNKNOWN)
	 *	返回 status 以便: return loger.error( ...., errCode, ...); 
	 */
	inline	int error( int fun_code, const char * msg, int status, int u_id = 0 ){
		log( L_ERROR, fun_code, u_id, msg, status);
		return	status;
	}

	/**
	 * @see int log( int log_level, int fun_code, int u_id, char * msg, int status=STAT_UNKNOWN)
	 *	返回 status 以便: return loger.error( ...., errCode, ...); 
	 */
	inline	int error( const char * fun_name, int line_no, int fun_code, const char * msg, int status, int u_id=0){
		log( L_ERROR, fun_code, u_id, msg, status, fun_name, line_no);
		return	status;
	}

	/**
	 * @see int log( int log_level, int fun_code, int u_id, char * msg, int status=STAT_UNKNOWN), 注意LOG_PRINTF_BUF_LEN的限制.
	 */
	inline	unsigned int printf( int log_level, int fun_code, int u_id, const char *msg, ...)
	{
		if (!isNeeded2log(log_level)){
			return	0;
		}
		char buf[LOG_PRINTF_BUF_LEN];
		va_list va;
		va_start( va, msg);
		vsnprintf( buf, sizeof(buf), msg, va);
		va_end(va);
		return	log(log_level, fun_code, u_id, buf, STAT_UNKNOWN);
	}

	/**
	 * @see int log( int log_level, int fun_code, int u_id, char * msg, int status=STAT_UNKNOWN), 注意LOG_PRINTF_BUF_LEN的限制.
	 */
	inline	unsigned int printf( int log_level, int fun_code, int u_id, int status, const char *msg, ...)
	{
		if (!isNeeded2log(log_level)){
			return	0;
		}
		char buf[LOG_PRINTF_BUF_LEN];
		va_list va;
		va_start( va, msg);
		vsnprintf( buf, sizeof(buf), msg, va);
		va_end(va);
		return	log(log_level, fun_code, u_id, buf, status);
	}

	/**
	 * @see int log( int log_level, int fun_code, int u_id, char * msg, int status=STAT_UNKNOWN), 注意LOG_PRINTF_BUF_LEN的限制.
	 */
	inline	unsigned int printf( int log_level, const char *fun_name, int line_no,
		const char *msg, ...)
	{
		if (!isNeeded2log(log_level)){
			return	0;
		}
		char buf[LOG_PRINTF_BUF_LEN];
		va_list va;
		va_start( va, msg);
		vsnprintf( buf, sizeof(buf), msg, va);
		va_end(va);
		return	log(log_level, 0, 0, buf, STAT_UNKNOWN, fun_name, line_no);
	}

	/**
	 * @see int log( int log_level, int fun_code, int u_id, char * msg, int status=STAT_UNKNOWN), 注意LOG_PRINTF_BUF_LEN的限制.
	 */
	inline	unsigned int printf( int log_level, const char *fun_name, int line_no, int status, 
		const char *msg, ...)
	{
		if (!isNeeded2log(log_level)){
			return	0;
		}
		char buf[LOG_PRINTF_BUF_LEN];
		va_list va;
		va_start( va, msg);
		vsnprintf( buf, sizeof(buf), msg, va);
		va_end(va);
		return	log(log_level, 0, 0, buf, status, fun_name, line_no);
	}

	/**
	 * @see int log( int log_level, int fun_code, int u_id, char * msg, int status=STAT_UNKNOWN), 注意LOG_PRINTF_BUF_LEN的限制.
	 */
	inline	unsigned int printf( int log_level, const char *fun_name, int line_no, int fun_code, int u_id,
		int status, const char *msg, ...)
	{
		if (!isNeeded2log(log_level)){
			return	0;
		}
		char buf[LOG_PRINTF_BUF_LEN];
		va_list va;
		va_start( va, msg);
		vsnprintf( buf, sizeof(buf), msg, va);
		va_end(va);
		return	log(log_level, fun_code, u_id, buf, status, fun_name, line_no);
	}

	//! 各个实现类/函数, 配套的辅助类/函数, 可利用这个函数算出当前日志是否需要记录从而有望优化性能.
	inline	int		isNeeded2log(int log_level){
		return	m_nLogLvlMsk & log_level;
	}

protected:

	int		m_nLogLvlMsk;		//!< 日志级别掩码, 把这个变量放到接口类, 以便各个实现类/函数能直接算出日志是否该过滤掉从而提升性能.

public:
   

}; 


//! 这个类用于自动给函数的进入和离开加上调试日志
class AutoEnterLeaveLogger
{
public:

	//! 参数的含义见 Logger::printf(), 这个类的作用是打动记录日志.
	inline AutoEnterLeaveLogger( Logger& loger, 
		const char* fun_name, int line_no, 
		int *pRet=NULL, int log_level=Logger::L_DEBUG, const char *msg=NULL, ...)
		:m_loger(loger), m_pRet(pRet), m_nLogId(0)
	{
		if (!loger.isNeeded2log(log_level)){
			return;
		}
		const char* pMsg2 = "Entering method ... ";
		char buf[LOG_PRINTF_BUF_LEN];
		if( msg){
			va_list va;
			va_start( va, msg);
			vsnprintf( buf, sizeof(buf), msg, va);	
			va_end(va);
			pMsg2 = buf;
		}
		m_nLogId = m_loger.log( log_level, 0, 0, pMsg2, STAT_UNKNOWN, fun_name, line_no);
	}

	//! 参数的含义见 Logger::printf(), 这个类的作用是打动记录日志.
	inline AutoEnterLeaveLogger( Logger& loger, 
		int fun_code, int u_id, 
		int *pRet=NULL, int log_level=Logger::L_DEBUG, const char *msg=NULL, ...)
		:m_loger(loger), m_pRet(pRet), m_nLogId(0)
	{
		if (!loger.isNeeded2log(log_level)){
			return;
		}
		const char* pMsg2 = "Entering method ... ";
		char buf[LOG_PRINTF_BUF_LEN];
		if( msg){
			va_list va;
			va_start( va, msg);
			vsnprintf( buf, sizeof(buf), msg, va);	
			va_end(va);
			pMsg2 = buf;
		}
		m_nLogId = m_loger.log( log_level, fun_code, u_id, buf, STAT_UNKNOWN, NULL, 0);
	}

	//! 参数的含义见 Logger::printf(), 这个类的作用是打动记录日志.
	inline AutoEnterLeaveLogger( Logger& loger, 
		int fun_code, int u_id, const char* fun_name, int line_no, 
		int *pRet=NULL, int log_level=Logger::L_DEBUG, const char *msg=NULL, ...)
		:m_loger(loger), m_pRet(pRet), m_nLogId(0)
	{
		if (!loger.isNeeded2log(log_level)){
			return;
		}
		const char* pMsg2 = "Entering method ... ";
		char buf[LOG_PRINTF_BUF_LEN];
		if( msg){
			va_list va;
			va_start( va, msg);
			vsnprintf( buf, sizeof(buf), msg, va);	
			va_end(va);
			pMsg2 = buf;
		}
		m_nLogId = m_loger.log(log_level, fun_code, u_id, buf, STAT_UNKNOWN, fun_name, line_no);
	}

	inline ~AutoEnterLeaveLogger(){
		if (0!=m_nLogId){
			m_loger.log_stat(m_nLogId, NULL == m_pRet ? STAT_UNKNOWN : *m_pRet);
		}
	}


protected:
	Logger&			m_loger;
	int*			m_pRet;
	unsigned int	m_nLogId;
private:

	// 拷贝没意义
	AutoEnterLeaveLogger(const AutoEnterLeaveLogger& obj);
	AutoEnterLeaveLogger& operator=(const AutoEnterLeaveLogger& obj);
};




#endif      //_COMMON_UTILITY_LOGGER_HEADER_FILE_

