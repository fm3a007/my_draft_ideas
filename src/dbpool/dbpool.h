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

//! OTL_SPECIFICATION_MACRO should be always the same in entirely program source code,so pls define them in one file for global use.
#include "otl_customizing.h"

#include "resource_pool.h"


#include <vector>
#include <string>
#include <time.h>


//! make convenience to change OTL date/time data type into time_t
time_t otl2time( otl_datetime & odt);
//! change time_t  to OTL date/time data type.
otl_datetime & time2otl(const time_t &t, otl_datetime & odt);



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

    DbPool();
    ~DbPool();


    /**
     *  initialization.
     *	@Param:
     *		db_user:	user name to login db;
     *		db_psw:		Password to login db;
     *		db_name:	database name to connect;
     *	@return:
     *		0 - success, -1 - already initialized; -2 failed to connect db
     */
    int init( const char * db_user, const char * db_psw, const char* db_name , unsigned int iMaxConn = 10 );


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
    shared_ptr getConn( bool reconnectIfNotAlive=false, bool throwIfFailToConnect=false);

	/** 
	 * test if connection still alive.
	 * 
	 * test by sending a sql to server side.
	 * throw exception for making convenience to catch at outside caller.
	 */
	static	bool	isAlive(otl_connect& conn, bool throwException = false);

	//! see its name.
	int		reconnect(otl_connect& conn, bool throwException = false);

protected:


private:

    std::string	_User;
    std::string _Psw;
    std::string _dbName;

};




#endif // _MY_DB_POOL_HEADER_

