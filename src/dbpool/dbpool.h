/**
 *	\file
 *
 *	Description:
 *		DB connection pool declaration file
 *  \n
 *  CAUTION:
 *      Please define your customized OTL_SPECIFICATION macro in a otl_customizing.h.
 *	
 *	by:	Liang,David @ 2011-3-24
 * 
 * These source files are released under the GPLv3 license.
 *
 */


#ifndef _MY_DB_POOL_HEADER_
#define _MY_DB_POOL_HEADER_


extern otl_connect*	IF_YOU_SEE_COMPILATION_ERROR_HERE_MEANS_SOMETHING_IS_WRONG_AND_PLEASE_FOLLOW_THE_INSTRUCTION_BELOW;
// OTL SPECIFICATION MACROs should be always identically defined within the entirely program source,
// so pls define them and include the otlv4.h in one file for global use; and make it to be included before including this header file.
// if OTL SPECIFICATION MACROs are defined inconsistantly, strange problems will occur!!!!

#include "resource_pool.h"

#include <string>
#include <time.h>
#include <iostream>




/**
 *  connection pool.
 *
 *  This class realizes connection pool management. Provides method to achieve always the same connection belong to current thread,
 *  preventing abuse so important resource.
 *  \n
 *  besides, it provides method to achieve new connection different to the thread specific connection mentioned above.
 *  \n
 *  Suggest to define a singleton class inheriting from current class,to avoid pass the pool object to everywhere which uses it.
 */
class DbPool : public ResPool<otl_connect>
{

public:

    /**
     *  initialization.
     *	@Param:
     *		db_user:	user name to login db;
     *		db_psw:		Password to login db;
     *		db_name:	database name to connect;
     *	@return:
     *		0 - success, -1 - already initialized; -2 failed to connect db
     */
	inline int init(const char * db_user, const char * db_psw, const char* db_name, unsigned int iMaxConn = 10) {
		if (iMax > 0) { return	-1; }
		_User = db_user;
		_Psw = db_psw;
		_dbName = db_name;
		otl_connect::otl_initialize(1);
		for (unsigned int i = 0; i < iMaxConn; ++i) {
			add(new otl_connect);
		}
		return	0;
	}




    /**
     *  return a connection.
     *
     *  always return a connection in the connection pool( blocked if no connection available), .
     *  database connection is very important resource, abuse can lead to very low efficiency.
     *  We provide a smart_pointer (shared_ptr) for convenience, DB connection will be released automatically.
	 *	hope you enjoy it.\n
	 *
	 *	If set reconnectIfNotAlive will lead to calling isAlive() and reconnect(), otherwise only test connected flag.\n
	 *
	 *	set reconnectIfNotAlive to be true could provide convenience for user but waste performance. For gaining full
	 *	capacity of the overall system, user should call getConn(false) to avoid testing isAlive() every time.
     */
	inline
    shared_ptr getConn( bool reconnectIfNotAlive=false, bool throwIfFailToConnect=false) {
		DbPool::shared_ptr pp = getResource();
		otl_connect * p = pp.get();

		// try to connect, to make convenience for caller
		if (!p->connected || p->get_throw_count() ||
			(reconnectIfNotAlive && !isAlive(*p)))
		{
			reconnect(*p, throwIfFailToConnect);
			p->reset_throw_count();
		}

		return	pp;
	}

	/** 
	 * test if connection still alive.
	 * 
	 * test by sending a sql to server side.
	 * throw exception for making convenience to catch at outside caller.
	 */
	inline static	bool	isAlive(otl_connect& conn, bool throwException = false) {
		try {
			return	isAliveImpl2(conn);
		}
		catch (otl_exception& e) {
			if (throwException) {
				throw	e;
			}
			std::cerr << "In " << __FUNCTION__ << ", Error code: " << e.code << ", Error msg : " << e.msg << std::endl;
		}
		return	false;
	}

	//! see its name.
	inline
	int		reconnect(otl_connect& conn, bool throwException = false)
	{
		otl_connect * p = &conn;
		if (p->connected) {
			p->logoff();
		}
		std::string	str = _User;
		str += "/";
		str += _Psw;
		str += "@";
		str += _dbName;
		try {
			p->rlogon(str.c_str());
			return	0;
		}
		catch (otl_exception & e) {
			if (throwException) {
				throw	e;
			}
			std::cerr << "Error occur while initializing db connection:" << std::endl;
			std::cerr << "USR:" << _User.c_str() << std::endl;
			std::cerr << "DSN:" << _dbName.c_str() << std::endl;
			//std::cerr << "PSW:" << _Psw.c_str() << std::endl;
			std::cerr << "Error code: " << e.code << std::endl;
			std::cerr << "Error msg : " << e.msg << std::endl;
		}
		return	-1;
	}



protected:

		// this sql may be compatible with different DBMS,according to sql-92 page 139, 6.8<datetime value function>
		// CURRENT_DATE, CURRENT_TIME, CURRENT_TIMESTAMP are defined in "General Rules".
	static	inline
	bool	isAliveImpl1(otl_connect& conn) {
		const char* sql = "select CURRENT_TIMESTAMP from dual";
		otl_stream is(1, sql, conn);
		is << endr;
		otl_datetime dt;
		is >> dt >> endr;
		if ( dt.year > 0) {
			return	true;
		}
		return	false;
	}


		// this sql may be more compatible with different DBMS as they are designed for ODBC.
	static	inline
	bool	isAliveImpl2(otl_connect& conn) {
		//const char* sql = "SELECT COUNT(TABLE_NAME) FROM INFORMATION_SCHEMA.tables";
		const char* sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.tables";
		otl_stream is(1, sql, conn);
		is << endr;
		int	count = -1;
		is >> count >> endr;
		if (count >= 0) {
			return	true;
		}
		return	false;
	}

		// this sql may be more efficient and compatible with different DBMS as they support DUAL.
	static	inline
	bool	isAliveImpl3(otl_connect& conn) {
		const char* sql = "SELECT 1 FROM DUAL WHERE 1>0";
		otl_stream is(1, sql, conn);
		is << endr;
		int	count = -1;
		is >> count >> endr;
		if (count >= 0) {
			return	true;
		}
		return	false;
	}


private:

    std::string	_User;
    std::string _Psw;
    std::string _dbName;

};




#endif // _MY_DB_POOL_HEADER_

