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


#include "logger_impl.h"


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
#define	CXX11_STD std
//#include <mutex>
#include <condition_variable>
//#include <memory>
#else
#include <boost/thread/mutex.hpp>
#include <boost/thread/locks.hpp>
#define	CXX11_STD boost
#endif	// C++11
#endif		//WITH_THREAD_SAFETY

using namespace std;


Logger::~Logger(){
}


class MyException : public std::exception {

public:
	MyException( const char * what){
		msg = what;
	}
	~MyException( ) throw(){
	}
	const char * what() const throw()
	{
		return	msg.c_str();
	}
private:
	std::string msg;
};



int PlainLoggerImpl::log_stat(unsigned int log_id, int status)
{
	if(0==log_id){
		return status;
	}
#if defined ( WITH_THREAD_SAFETY )
	CXX11_STD::lock_guard<CXX11_STD::mutex> lock( m_mutex);
#endif	//  WITH_THREAD_SAFETY 
	ostream * pOs = m_pStdout;
	const char * pAppand = "\n\n";
	if(0==log_id){
		pOs = m_pStderr;
		pAppand = ". *** log_id is invalid! ***\n\n";
	}
	if(NULL==pOs){
		return status;
	}

	ostream & OS = *pOs;
	time_t t = time(&t);
	tm ttm;
	memcpy( &ttm, localtime( &t), sizeof(tm));
	char cBuf[64];
	sprintf( cBuf, "%d-%02d-%02d %02d:%02d:%02d\n", 
		ttm.tm_year+1900, ttm.tm_mon+1, ttm.tm_mday, ttm.tm_hour, ttm.tm_min,ttm.tm_sec );
	OS << cBuf <<
		log_id << "\tlog_stat(): "<< status;
	if( m_pTranProc){
		const char* pCodMsg = (*m_pTranProc)( status, m_pUsrData);
		OS << "(" << (pCodMsg?pCodMsg:"(null)") << ")";
	}
	OS << pAppand;
	OS.flush();
	return	status;
}

unsigned int PlainLoggerImpl::log(int log_level, int fun_code, int u_id, const char * msg, int status/*=STAT_UNKNOWN*/, const char * fun_name/*=NULL*/, int line_no/*=0*/)
{
	if (!( m_nLogLvlMsk & log_level)){
		return	0;
	}
#if defined ( WITH_THREAD_SAFETY )
	CXX11_STD::lock_guard<CXX11_STD::mutex> lock( m_mutex);
#endif	// WITH_THREAD_SAFETY
	std::ostream * pOs = m_pStderr;
	if ( m_nLogLvlMsk & log_level ){
		pOs = m_pStdout;
	}
	if(NULL==pOs){
		return	0;
	}
	std::ostream & OS = *pOs;

	int id = 0;
	do{
		id = ++m_iId;
	}while(0==id);

	time_t t = time(&t);
	tm ttm;
	memcpy( &ttm, localtime( &t), sizeof(tm));
	char cBuf[64];
	sprintf( cBuf, "%d-%02d-%02d %02d:%02d:%02d\n", 
		ttm.tm_year+1900, ttm.tm_mon+1, ttm.tm_mday, ttm.tm_hour, ttm.tm_min,ttm.tm_sec );

	OS << cBuf /*ctime( &t)*/ << id << "\tlevel: "<<log_level<<",\tFunCode: " << fun_code << 
		",\tuser_id: " << u_id << ",\tstatus: ";
	if(STAT_UNKNOWN!=status){ 
		OS << status; 
		if( m_pTranProc){
			const char* pCodMsg = (*m_pTranProc)( status, m_pUsrData);
			OS << "(" << (pCodMsg?pCodMsg:"(null)") << ")";
		}
	}
	OS << "\n";
	if( fun_name){
		OS << "\tIn " << fun_name << ", line:" << line_no << "\n";
	}
	OS << "\tMsg: " << msg <<"\n\n";
	OS.flush();

	if(STAT_UNKNOWN!=status ){
		id = 0;
	}

	return id;
}

PlainLoggerImpl::~PlainLoggerImpl()
{
	//if(NULL!=m_pStdout){
	//	time_t t = time(&t);
	//	*m_pStdout << ctime(&t) <<
	//		"\tLogger object deconstruction.\n\n";
	//}
}

PlainLoggerImpl::PlainLoggerImpl(int log_level/*=0*/, ostream * pStdout/*=NULL*/, ostream * pStderr/*=NULL*/)
:Logger(log_level)
{
	m_iId = 0;

	m_pStdout = NULL==pStdout ? &cout : pStdout;
	m_pStderr = NULL==pStderr ? &cerr : pStderr;

	if(0==log_level){
		m_nLogLvlMsk = ~Logger::L_ERROR;
	}else{
		m_nLogLvlMsk = log_level;
	}
	m_pTranProc = NULL;
	m_pTranProc = NULL;
}

void PlainLoggerImpl::setErrCodeTranslator(TRANSLATE_PROC tran_proc, void * usr_data)
{
	m_pTranProc = tran_proc;
	m_pUsrData = usr_data;
}

#ifdef WIN32
static const char SOLIDUS = '\\';
#else
static const char SOLIDUS = '/';
#endif // WIN32


std::string FileLoggerImpl::getNewFileName()
{
	string file( m_strPath+m_strFile);
	if( m_bNameWithDate){
		time_t t = time(NULL);
		tm tm;
		memcpy( &tm, (void*)localtime( &t), sizeof(tm));
		char buf[64];
		sprintf( buf, ".%04d-%02d-%02d", tm.tm_year+1900, tm.tm_mon+1, tm.tm_mday);
		file += buf;
	}
	if(m_strExt.length()>0){
		file += ".";
		file += m_strExt;
	}
	return	file;
}

bool FileLoggerImpl::isNewDay()
{
	time_t t = time(NULL);
	int day = localtime( &t)->tm_mday;
	if(day!=m_iDay){
		m_iDay = day;
		return true;
	}
	return false;
}

void FileLoggerImpl::closeFile()
{
	if(NULL!=m_pStdout){
		delete m_pStdout;
		if(m_pStderr==m_pStdout){
			m_pStderr = NULL;
		}
		m_pStdout = NULL;
	}
	if(NULL!=m_pStderr){
		delete m_pStderr;
		m_pStderr = NULL;
	}
}

unsigned int FileLoggerImpl::log(int log_level, int fun_code, int u_id, const char * msg, int status/*=STAT_UNKNOWN*/, const char * fun_name/*=NULL*/, int line_no/*=0*/)
{
	if (!( m_nLogLvlMsk & log_level)){
		return	0;
	}
	{
#if defined ( WITH_THREAD_SAFETY )
		CXX11_STD::lock_guard<CXX11_STD::mutex> lock( m_mutex);
#endif	// WITH_THREAD_SAFETY 
		if( NULL==m_pStdout || ( m_bNameWithDate && isNewDay())){
			closeFile();
			string file;
			if( m_bNameWithDate){
				file = getNewFileName();
			}else{
				file = m_strPath+m_strFile;
				if(m_strExt.length()>0){
					file += ".";
					file += m_strExt;
				}
			}
			m_pStdout = new ofstream( file.c_str(), std::ios::app | std::ios::binary);
		}
	}
	return PlainLoggerImpl::log( log_level, fun_code, u_id, msg, status, fun_name, line_no);
}

FileLoggerImpl::~FileLoggerImpl()
{
	closeFile();
}

FileLoggerImpl::FileLoggerImpl(const char * file_name, const char * ext, int log_level/*=0*/, const char *path/*=NULL*/, bool name_with_date/*=true */)
{
	m_strFile = NULL==file_name ? "" : file_name;
	m_strExt = NULL==ext ? "" : ext;
	m_strPath = NULL==path ? "" : path;

	m_nLogLvlMsk = 0==log_level ? ~0 : log_level;
	m_bNameWithDate = name_with_date;
	m_pStdout = NULL;
	m_pStderr = NULL;

	m_iDay = -1;

	int n = m_strPath.length();
	if( n>0 && SOLIDUS!=m_strPath[n-1]){
		m_strPath+=SOLIDUS;
	}
}


#if defined ( WITH_ODBC_OUTPUT )
#include "otl_customizing.h"
/**
	bs_log日志表对应的DAO类.
	数据库建库脚本
	CREATE TABLE bs_log
	(
	log_id INTEGER NOT NULL AUTO_INCREMENT,
	level INTEGER,
	fun_code INTEGER,
	u_id INTEGER,
	msg VARCHAR(255),
	status INTEGER,
	PRIMARY KEY (log_id)
	);
*/
class BsLogDao{

public:
	static const char * TABLE;
	static const char * COLUMNS;
	static const char * COLUMNS_TYPE;
	static const std::string	INSERT_SQL;
	static const std::string	GET_ID_SQL;
	static const std::string	UPDATE_SQL;

	BsLogDao( otl_connect & conn ) : m_con(conn)
	{
	}

	~BsLogDao()
	{
	}

	int	add(int log_level, int fun_code, int u_id, const char * msg, int status)
	{
		otl_nocommit_stream os( 1, INSERT_SQL.c_str(), m_con);;
		//otl_stream os( 1, INSERT_SQL.c_str(), m_con);
		//os.set_commit(0);
		os << otl_null() << log_level << fun_code << u_id << msg << status << endr;
		os.flush();
		otl_nocommit_stream os2(1, GET_ID_SQL.c_str(), m_con);
		int id = 0;
		if( !os2.eof()){
			os2 >> id >> endr;
		}else{
			MyException e( "Failed to insert record into database! Err:-1");
			throw e;
		}
		return	id;
	}

	void update_status( unsigned int id, int stat)
	{
		otl_nocommit_stream os( 1, UPDATE_SQL.c_str(), m_con);
		os << stat << (int)id << endr;
		os.flush();
	}


protected:
	otl_connect	& m_con;

private:
	
};

//// 表的字段名字符串, 统一定义,多处引用, 方便维护.
const char * BsLogDao::TABLE = "bs_log";
const char * BsLogDao::COLUMNS = "log_id,level,fun_code,u_id,msg,status ";
const char * BsLogDao::COLUMNS_TYPE = 
	"( :id<int>,:lvl<int>,:fun_code<int>,:u_id<int>,:msg<char[255]>,:status<int>)";
const std::string	BsLogDao::INSERT_SQL = 
	string( "insert into ")+BsLogDao::TABLE+
	" ("+BsLogDao::COLUMNS+") values "+BsLogDao::COLUMNS_TYPE;
// 重新取回新的记录ID
// 据MYSQL手册以下的SQL是ODBC取回上次插入的ID的实际做法,至少dephic和access支持
// TODO *** 注意: ODBC的配置必需打开 AUTO_INCREMENT null search.
//std::string	BsLogDao::GET_ID_SQL = 
//	string("select log_id from ")+BsLogDao::TABLE+" where log_id is null";
//std::string	BsLogDao::GET_ID_SQL = 
//	string("select max(log_id) from ")+BsLogDao::TABLE;
const std::string	BsLogDao::GET_ID_SQL = string("SELECT @@IDENTITY FROM ")+BsLogDao::TABLE;
// MYSQL get last id
//std::string	BsLogDao::GET_ID_SQL = 
//	string("select LAST_INSERT_ID() as log_id");
const std::string	BsLogDao::UPDATE_SQL = 
	string("update ")+BsLogDao::TABLE+" set status=:st<int> where log_id=:id<int> ";



int OdbcLoggerImpl::log_stat(unsigned int log_id, int status)
{
	if(log_id<=0){	
		return status; 
	}

	CXX11_STD::lock_guard<CXX11_STD::mutex> lock( m_mutex);
	for( int i=0; i<m_iReps; ++i){
		try
		{
			if( !m_con.connected){
				m_con.rlogon( (m_strDbUser+"/"+m_strDbPsw+"@"+m_strOdbc_src).c_str());
			}
			BsLogDao dao( m_con);
			dao.update_status( log_id, status);
			m_con.commit();
			return status;
		}
		catch(otl_exception& p){
			// intercept OTL exceptions  
			cerr<<p.msg<<endl; // print out error message  
			cerr<<p.stm_text<<endl; // print out SQL that caused the error  
			cerr<<p.var_info<<endl; // print out the variable that caused the error 
			m_con.logoff(); // log off to try again
		}
	}
	if( m_strLogFile.length()>0){
		if(NULL==m_pFLoger){
			m_pFLoger = new FileLoggerImpl(m_strLogFile.c_str(), NULL, 0, NULL, false);
		}
		m_pFLoger->log_stat( log_id, status);
		return status;
	}
	MyException e( "Failed to insert record into database, retry too many times! Err:-2");
	throw e;
}

unsigned int OdbcLoggerImpl::log(int log_level, int fun_code, int u_id, const char * msg, int status/*=STAT_UNKNOWN*/, const char * fun_name/*=NULL*/, int line_no/*=0*/)
{
	if( !(log_level&m_nLogLvlMsk) ){
		return	0;
	}

	CXX11_STD::lock_guard<CXX11_STD::mutex> lock( m_mutex);
	for( int i=0; i<m_iReps; ++i){
		try
		{
			if( !m_con.connected){
				m_con.rlogon( (m_strDbUser+"/"+m_strDbPsw+"@"+m_strOdbc_src).c_str());
			}
			BsLogDao dao(m_con);
			int id = dao.add( log_level, fun_code, u_id, msg, status);
			m_con.commit();
			return	id;
		}
		catch(otl_exception& p){
			// intercept OTL exceptions  
			cerr<<p.msg<<endl; // print out error message  
			cerr<<p.stm_text<<endl; // print out SQL that caused the error  
			cerr<<p.var_info<<endl; // print out the variable that caused the error 

			m_con.logoff(); // log off to try again
		}
	}
	if( m_strLogFile.length()>0){
		if(NULL==m_pFLoger){
			m_pFLoger = new FileLoggerImpl(m_strLogFile.c_str(), NULL, 0, NULL, false);
		}
		return m_pFLoger->log( log_level|L_DEBUG,fun_code,u_id,msg,status);
	}
	MyException e( "Failed to insert record into database, retry too many times! Err:-2");
	throw e;
}

OdbcLoggerImpl::~OdbcLoggerImpl()
{
	if(NULL!=m_pFLoger){
		delete m_pFLoger;
		m_pFLoger = NULL;
	}
}

OdbcLoggerImpl::OdbcLoggerImpl(const char* odbc_src, const char * db_user, const char * db_psw, int log_level/*=0*/, const char * log_file_path/*=NULL*/, int repeats/*=5 */)
	:Logger(log_level)
{
	m_strOdbc_src = odbc_src;
	m_strDbUser = db_user;
	m_strDbPsw = db_psw;
	m_nLogLvlMsk = 0==log_level ? ~0 : log_level;
	m_strLogFile = NULL==log_file_path ? "" : log_file_path;
	m_iReps = repeats>1 ? repeats : 1;			

	m_pFLoger = NULL;
}


SimpleLoggerMngr::SimpleLoggerMngr(
	   const char* odbc_src, const char * db_user, const char * db_psw, 
	   int log_level, const char * log_file_path, int repeats)
{
	m_pLogger = new OdbcLoggerImpl( odbc_src, db_user, db_psw, log_level, log_file_path, repeats);
}

#endif	//	WITH_ODBC_OUTPUT

SimpleLoggerMngr::SimpleLoggerMngr()
{
	m_pLogger = new PlainLoggerImpl();
}

SimpleLoggerMngr::SimpleLoggerMngr( const char * file_name, const char * ext, 
					   int log_level, const char *path, bool name_with_date)
{
	m_pLogger = new FileLoggerImpl( file_name, ext, log_level, path, name_with_date);
}

SimpleLoggerMngr::~SimpleLoggerMngr(){
	if(NULL!=m_pLogger){
		delete m_pLogger;
		m_pLogger = NULL;
	}
}

Logger & SimpleLoggerMngr::getLogger(){
	return	*m_pLogger;
}


unsigned int TheDummyLogger::log(int log_level, int fun_code, int u_id, const char * msg, int status/*=STAT_UNKNOWN*/, const char * fun_name/*=NULL*/, int line_no/*=0*/)
{
	static int i = 0;
	return	STAT_UNKNOWN==status ? 0 : ++i;
}

int TheDummyLogger::log_stat( unsigned int log_id, int status )
{
	return status;
}

TheDummyLogger& TheDummyLogger::getTheDummyLogger()
{
	static	char theLoger[sizeof(TheDummyLogger)+4];
	static	TheDummyLogger* pTheLoger = new(&theLoger) TheDummyLogger;
	return	*pTheLoger;
}

TheDummyLogger::TheDummyLogger()
:Logger(0)
{
}

