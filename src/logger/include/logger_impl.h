/**
 * \file
 * 对Logger的简单实现.
 *
 * 为保证线程安全,使用了BOOST的线程锁,如果不需要这么复杂也不想依赖太多库,
 * 不定义WITH_THREAD_SAFETY宏即可.
 * 本程序对logger的实现包括输出的屏幕,文件 和 ODBC 数据库 三种形式. 对于
 * 输出到ODBC数据库的实现可能对大多数的产品都不需要, 因此默认没有编译
 *	TODO: 
 * 若需要输出到ODBC数据库的功能, 请在工程配置文件或Makefile中定义 WITH_ODBC_OUTPUT 宏.
 * @see Log class
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

#if ! defined	__MY_LOGGER_IMPL_HEADER__
#define			__MY_LOGGER_IMPL_HEADER__

#include "logger.h"


#include <time.h> 
#include <memory.h> 
#include <string.h> 
#include <stdio.h> 
#include <string>
#include <sstream>
#include <iostream>
#include <fstream>
#include <iomanip>
#include <exception>

#if defined ( WITH_THREAD_SAFETY )
#if defined ( _WITH_CXX11_ )
#include <mutex>
#define	CXX11_STD std
#else
#include <boost/thread/mutex.hpp>
#include <boost/thread/locks.hpp>
#define	CXX11_STD boost
#endif
#endif		//WITH_THREAD_SAFETY



//! 啥也不做的哑元对象
class TheDummyLogger : public Logger
{
public:

	//! 方便给Logger*指针赋初值, 不用判断空值, 简化处理逻辑和减少缩进层数. 
	static CLASS_DLL_API TheDummyLogger&	getTheDummyLogger();

	virtual unsigned int log(  int log_level, int fun_code, int u_id, const char * msg, int status=STAT_UNKNOWN, 
		const char * fun_name=NULL, int line_no=0);
	virtual int log_stat( unsigned int log_id, int status );

protected:
	TheDummyLogger();
private:
};


//! 用于将错误码转换为文本信息的函数(便于日志类扩展)
typedef	const char*	(*TRANSLATE_PROC)( int code, void *usr_data);

//! 输出到 stdout and stderr
class PlainLoggerImpl : public Logger{

public:
	// log_level以外的级别输出到m_pStderr(默认stderr)中.
	PlainLoggerImpl( int log_level=0, std::ostream * pStdout=NULL, std::ostream * pStderr=NULL);

	~PlainLoggerImpl();


	unsigned int log( int log_level, int fun_code, int u_id, const char * msg, int status=STAT_UNKNOWN, 
		const char * fun_name=NULL, int line_no=0);

	int log_stat( unsigned int log_id, int status);

	//! 设置错误码的转换函数(usr_data原样回传给 tran_proc).
	void	setErrCodeTranslator( TRANSLATE_PROC tran_proc, void * usr_data );

protected:

	int m_iId;
	std::ostream * m_pStdout;
	std::ostream * m_pStderr;
	TRANSLATE_PROC	m_pTranProc;
	void*			m_pUsrData;

#if defined ( WITH_THREAD_SAFETY )
	CXX11_STD::mutex	m_mutex;
#endif	//  ( WITH_THREAD_SAFETY )

private:

};


//! 输出到文件
class FileLoggerImpl : public PlainLoggerImpl{

public:
	FileLoggerImpl(const char * file_name, const char * ext, 
			int log_level=0, const char *path=NULL, bool name_with_date=true );

	~FileLoggerImpl();

	unsigned int log( int log_level, int fun_code, int u_id, const char * msg, int status=STAT_UNKNOWN, 
		const char * fun_name=NULL, int line_no=0);

protected:
	std::string m_strPath;
	std::string m_strFile;
	std::string m_strExt;
	bool	m_bNameWithDate;
	int m_iDay;

	void closeFile();

	bool isNewDay();

	std::string getNewFileName( );

private:

};


#if defined ( WITH_ODBC_OUTPUT )
#include "otl_customizing.h"


//! 输出到数据库(ODBC)
class OdbcLoggerImpl : public Logger{

public:

	OdbcLoggerImpl(
		const char* odbc_src, const char * db_user, const char * db_psw,
		int log_level=0, const char * log_file_path=NULL, int repeats=5 );

	~OdbcLoggerImpl();

	unsigned int log( int log_level, int fun_code, int u_id, const char * msg, int status=STAT_UNKNOWN, 
		const char * fun_name=NULL, int line_no=0);

	int log_stat( unsigned int log_id, int status);

protected:

	int	m_iReps;

	std::string	m_strOdbc_src;
	std::string	m_strDbUser;
	std::string	m_strDbPsw;
	std::string	m_strLogFile;
	otl_connect m_con;
	FileLoggerImpl *	m_pFLoger;


private:

	CXX11_STD::mutex	m_mutex;

};

#endif	//	WITH_ODBC_OUTPUT


/**
 * 一个日志记录器(Logger)的简单实现, 方便调试使用.
 * 
 * 用法: 通过不同的造函数来构造一个管理类的实例,然后调用getLogger可以获得不同功能的Logger对象.
 */
class CLASS_DLL_API SimpleLoggerMngr{

public:

	/**
	 * 构造SimpleLoggerMngr,以获得Logger将日志输出到stdout和stderr.
	 * 
	 * \n
	 * 调用方式: [Y]多线程并发调用, [Y]后台定时触发/[Y]外部触发\n
	 * 调用频率: 峰值[300](t/s), 平均值[1](t/s).\n
	 * 处理时长: max< [80] (millisecond) storage to local DBMS/FILE SYSTEM, avg<[  ](millisecond). \n
	 * 单次调用数据处理量: max>=[   ] records, avg>=[  ]records.\n
	 *
	 * @see <参见暂无>
	 */
	SimpleLoggerMngr( );

#if defined ( WITH_ODBC_OUTPUT )
	/** 
	 * 构造SimpleLoggerMngr,以获得Logger将日志存到ODBC数据库.
	 * 
	 * \n
	 * 调用方式: [Y]多线程并发调用, [Y]后台定时触发/[Y]外部触发\n
	 * 调用频率: 峰值[300](t/s), 平均值[1](t/s).\n
	 * 处理时长: max< [80] (millisecond) storage to local DBMS/FILE SYSTEM, avg<[  ](millisecond). \n
	 * 单次调用数据处理量: max>=[   ] records, avg>=[  ]records.\n
	 *
	 * @param odbc_src		ODBCO数据源
	 * @param db_user		数据库用户名
	 * @param db_psw		数据库密码
	 * @param log_level		要记录的日志级别(多个级别可取或运算的值),为0时记录所有级别
	 * @param file_path		日志文件的全路径,当数据库连接不上时写到该文件
	 * @param repeats		(数据库操作出错时)重试的次数,小于等于1时算为1
	 *
	 * @see <参见暂无>
	 */
	SimpleLoggerMngr( 
		const char* odbc_src, const char * db_user, const char * db_psw, 
			int log_level=0 , const char * log_file_path=NULL, int db_conn_repeats=5);
#endif	// ( WITH_ODBC_OUTPUT )
	
	/**
	 * 构造SimpleLoggerMngr,以获得Logger将日志存到文件.
	 * 
	 * \n
	 * 调用方式: [Y]多线程并发调用, [Y]后台定时触发/[Y]外部触发\n
	 * 调用频率: 峰值[300](t/s), 平均值[1](t/s).\n
	 * 处理时长: max< [80] (millisecond) storage to local DBMS/FILE SYSTEM, avg<[  ](millisecond). \n
	 * 单次调用数据处理量: max>=[   ] records, avg>=[  ]records.\n
	 *
	 * @param file_name			主文件名
	 * @param ext				后缀名
	 * @param log_level			要记录的日志级别(多个级别可取或运算的值),为0时记录所有级别.
	 * @param path				路径,为空时输出到当前文件夹
	 * @param name_with_date	以日期命名文件(path_and_name.yyyy-mm-dd.ext)
	 *
	 * @see <参见暂无>
	 */
	SimpleLoggerMngr( const char * file_name, const char * ext, 
		int log_level=0, const char *path=NULL, bool name_with_date=true );

	~SimpleLoggerMngr();

	/**
	 *	返回Logger对象
	 */
	Logger & getLogger();

protected:

	Logger * m_pLogger;

private:

};


#endif	//	include once


