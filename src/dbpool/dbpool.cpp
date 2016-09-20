/**	dbpool.cpp
 *
 *	Description:
 *		db pool implementation
 *
 *	Author:	David
 *	Date:	2011-3-24
 *
 */



#include "dbpool.h"
#include <assert.h>
#include <time.h>


#include <iostream>


time_t otl2time( otl_datetime & odt)
{
	struct tm tm;
	tm.tm_year = (odt.year-1900);
	tm.tm_mon = odt.month-1;
	tm.tm_mday = odt.day;
	tm.tm_hour = odt.hour;
	tm.tm_min = odt.minute;
	tm.tm_sec = odt.second;
	return mktime( &tm);

}

otl_datetime & time2otl(const time_t &t, otl_datetime & odt)
{
	tm * ptm = localtime( &t);
	odt.year = 1900+ ptm->tm_year; 
	odt.month = 1 + ptm->tm_mon;
	odt.day = ptm->tm_mday;
	odt.hour = ptm->tm_hour;
	odt.minute = ptm->tm_min;
	odt.second = ptm->tm_sec;
	odt.fraction = 0;
	odt.frac_precision = 0;
	return	odt;
}



DbPool::DbPool(){
}

DbPool::~DbPool(){
}


int DbPool::init( const char * db_user, const char * db_psw, const char* db_name , unsigned int iMaxConn )
{
	if(iMax > 0){    return	-1; }

	_User = db_user;
	_Psw = db_psw;
	_dbName = db_name;
	
	otl_connect::otl_initialize(1);

	for( unsigned int i=0; i<iMaxConn; ++i){
		add( new otl_connect);
	}

    return	0;
}


DbPool::shared_ptr DbPool::getConn(bool reconnectIfNotAlive/*=false*/, bool throwIfFailToConnect/*=false*/)
{
	DbPool::shared_ptr pp = getResource();
	otl_connect * p = pp.get();

    // try to connect, to make convenience for caller
    if( !p->connected || p->get_throw_count() || 
		( reconnectIfNotAlive && !isAlive( *p)))
	{
		reconnect(*p, throwIfFailToConnect);
		p->reset_throw_count();
	}

    return	pp;
}

static	inline
// this sql may be compatible with different DBMS,according to sql-92 page 139, 6.8<datetime value function>
// CURRENT_DATE, CURRENT_TIME, CURRENT_TIMESTAMP are defined in "General Rules".
bool	isAliveImpl1(otl_connect& conn){
	const char* sql = "select CURRENT_TIMESTAMP from dual";
	otl_stream is(1, sql, conn);
	is << endr;
	otl_datetime dt;
	is >> dt >> endr;
	if ( otl2time(dt) > 0){
		return	true;
	}
	return	false;
}


static	inline
// this sql may be more compatible with different DBMS as they are designed for ODBC.
bool	isAliveImpl2(otl_connect& conn){
	//const char* sql = "SELECT COUNT(TABLE_NAME) FROM INFORMATION_SCHEMA.tables";
	const char* sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.tables";
	otl_stream is(1, sql, conn);
	is << endr;
	int	count = -1;
	is >> count >> endr;
	if (count >= 0){
		return	true;
	}
	return	false;
}

static	inline
// this sql may be more efficient and compatible with different DBMS as they support DUAL.
bool	isAliveImpl3(otl_connect& conn){
	const char* sql = "SELECT 1 FROM DUAL WHERE 1>0";
	otl_stream is(1, sql, conn);
	is << endr;
	int	count = -1;
	is >> count >> endr;
	if (count >= 0){
		return	true;
	}
	return	false;
}

bool DbPool::isAlive(otl_connect& conn, bool throwException /*= false*/)
{
	try{
		return	isAliveImpl2(conn);
	}catch (otl_exception& e){
		if ( throwException){
			throw	e;
		}
		std::cerr << "In " << __FUNCTION__ << ", Error code: " << e.code << ", Error msg : " << e.msg << std::endl;
	}
	return	false;
}


int DbPool::reconnect(otl_connect& conn, bool throwException /*= false*/)
{
	otl_connect * p = &conn;
	if (p->connected){ 
		p->logoff(); 
	}
	std::string	str = _User;
	str += "/";
	str += _Psw;
	str += "@";
	str += _dbName;
	try{
		p->rlogon(str.c_str());
		return	0;
	}catch (otl_exception & e){
		if (throwException){
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

