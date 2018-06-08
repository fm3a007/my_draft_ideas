/**	
 *	\file
 *
 *	for module test
 *
 */

#if defined ( _DEBUG)
#define CRTDBG_MAP_ALLOC   
#include <stdlib.h>   
#include <crtdbg.h> 
#endif //   _DEBUG

#include "otl_customizing.h"
#include "dbpool.h"
#include <assert.h>
#include <time.h>
#include <iostream>

#if	defined( _WITH_CXX11_ )
#include <thread>
#else
#include <boost/shared_ptr.hpp>
#include <boost/thread.hpp>
#endif	// _WITH_CXX11_


#if defined (WIN32) || defined(WIN64)
#else
#include <unistd.h>
inline int	Sleep( int milliseconds){
	return	usleep( 1000*milliseconds);
}
#endif


using	namespace std;

DbPool	db;

int	MAX_DB_CONNECTION = 30;
bool	bPoolFluctuation = true;

void RecursiveFun(){
	static	int	iRecursiveCount = MAX_DB_CONNECTION/2;

	cout << "from RecursiveFun "<< iRecursiveCount << " ... " << endl;

	DbPool::shared_ptr p1 = db.getConn();
	DbPool::shared_ptr p2 = p1;
	DbPool::shared_ptr p3 = db.getConn();

	if(p1==NULL){
		cerr << "NULL pointer return from getConn()!\n";
	}
	if(p1!=NULL){
		cerr << "p1 NOT NULL. \n";
	}

	if(p1==p3){
		cerr << "p1 == p3. \n";
	}
	if(p1!=p2){
		cerr << "p1 != p2. \n";
	}

	while(--iRecursiveCount>0){
		RecursiveFun();
	}
}


void f2(){

	static	int	iRecursiveCount = MAX_DB_CONNECTION;
	char	cBuf[512];
	sprintf( cBuf,"\nFunction 2, recursive count: %d\n",iRecursiveCount);
	cout << cBuf;

	DbPool::shared_ptr p1 = db.getConn();
	DbPool::shared_ptr p2 = p1;

	if(--iRecursiveCount>0){
		f2();
	}
	++iRecursiveCount;
}

void f3( int counts=100){
	for(int i=0; i<counts && bPoolFluctuation; ++i){
		char	cBuf[512];
		sprintf( cBuf,"\n\n================From f3 ... : %d ...\n",i);
		cout << cBuf;
		f2();
		Sleep(5);
	}
}

void f1( int id){
	Sleep(800);
	for( int i=0; i<100; ++i){
		char	cBuf[512];
		sprintf( cBuf,"\nThread ID : %d, times: %d. ... \n",id,i);
		cout << cBuf;
		try{
			DbPool::shared_ptr p1 = db.getConn();
			DbPool::shared_ptr p2 = p1;
            // 表结构
            //+-------+--------------+------+-----+---------+----------------+
            //| Field | Type         | Null | Key | Default | Extra          |
            //+-------+--------------+------+-----+---------+----------------+
            //| f1    | int(11)      |      | PRI | NULL    | auto_increment |
            //| f2    | varchar(255) | YES  |     | NULL    |                |
            //+-------+--------------+------+-----+---------+----------------+
            // create table test_str ( f1 int primary key auto_increment, f2 varchar(255));
            string sql;
			sql += "select f1,f2 from test_str";
			otl_stream is( 100, sql.c_str(), *p2);
			while( !is.eof())
			{
				int		iId = 0;
				string	strContent;

				is >> iId >> strContent >> endr;

				cout << "\t" << iId << strContent << endl;
			}

		}catch(otl_exception &e){
			cerr << "Error occur in DAO, code: " << e.code << endl;
			cerr<<e.msg<<endl; // print out error message
			cerr<<e.stm_text<<endl; // print out SQL that caused the error
			cerr<<e.sqlstate<<endl; // print out SQLSTATE message
			cerr<<e.var_info<<endl; // print out the variable that caused the error
		}

	}
}


void	my_free_string( std::string* p){
	if( p){
		delete	p;
	}
}

int	fff2(){

	CXX11_STD::shared_ptr<std::string>  pp( new std::string, my_free_string);

	return	0;
}


int	main( int argc, char * argv[]){


#if defined ( _DEBUG)
    _CrtSetDbgFlag ( _CRTDBG_ALLOC_MEM_DF | _CRTDBG_LEAK_CHECK_DF );  
    // 勿删,作为参照,以便确认内存泄漏检测功能是正确的.
    int * p = new int(3);
    //    _CrtDumpMemoryLeaks();  // 这句放最后?
#endif //   _DEBUG

	cout << "Hello, ..." << endl;
	cout << "Usage:" << argv[0] << "[how_many_threads_to_test=100] [max_db_connections=30]" << endl;

	int rettt = fff2();

	if(argc>=3){
		MAX_DB_CONNECTION = atoi(argv[2]);
	}

	db.init( "test", "", "mysql_test", MAX_DB_CONNECTION);

	RecursiveFun();

	//f1();
	//f2();
	//f3();

	//DbPool::shared_ptr p1 = db.getConn();
	//DbPool::shared_ptr p2 = p1;
	//DbPool::shared_ptr p3 = db.getConn();


/************************************************************************/
/*	并发测试                                                            */
/************************************************************************/


	int	iMaxThread = 100;

	if(argc >=2){
		iMaxThread = atoi( argv[1]);
	}

	CXX11_STD::thread worker_f3( &f3, iMaxThread);
	vector<CXX11_STD::thread *> vThreadBuf;
	vThreadBuf.reserve(iMaxThread);
	for( int i=0; i<iMaxThread; ++i){
		CXX11_STD::thread * p = new CXX11_STD::thread( &f1, i);
		vThreadBuf.push_back(p);
	}
	for( int i=0,n=vThreadBuf.size(); i<n; ++i){
		vThreadBuf[i]->join();
		delete vThreadBuf[i];
		vThreadBuf[i] = NULL;
	}
	bPoolFluctuation = false;
	worker_f3.join();
	/************************************************************************/
	/* end of 并发测试                                                      */
	/************************************************************************/



	return	0;
}


