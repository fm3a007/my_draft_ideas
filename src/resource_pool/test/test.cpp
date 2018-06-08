/**	
 *	\file
 *
 *	for module test
 *
 */

#if ( defined (_WIN32) || defined (_WIN64) )

#if defined ( _DEBUG)
#define CRTDBG_MAP_ALLOC   
#include <stdlib.h>   
#include <Windows.h>   
#include <crtdbg.h> 
#endif //   _DEBUG

#else

#endif	// NOT WINDOWS

#include "resource_pool.h"

#if	defined( _WITH_CXX11_ )
#include <thread>
#include <chrono>
void	my_sleep( int millsec){
	std::this_thread::sleep_for( std::chrono::milliseconds( millsec) );
}
#else
#include <boost/shared_ptr.hpp>
#include <boost/thread.hpp>
void	my_sleep( int millsec){
	boost::this_thread::sleep( boost::posix_time::seconds( millsec));
}
#endif	// _WITH_CXX11_


#include <assert.h>
#include <time.h>
#include <stdio.h>
#include <iostream>

using	namespace std;



ResPool<string>	db;

int	MAX_DB_CONNECTION = 30;
bool	bPoolFluctuation = true;

void RecursiveFun(){
	static	int	iRecursiveCount = MAX_DB_CONNECTION/2;

	cout << "from RecursiveFun "<< iRecursiveCount << " ... " << endl;

	ResPool<string>::shared_ptr p1 = db.getResource();
	if(p1==NULL){
		cerr << "NULL returned from ResPool::getResource()\n";
	}
	ResPool<string>::shared_ptr p2 = p1;
	ResPool<string>::shared_ptr p3 = db.getResource();

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

	ResPool<string>::shared_ptr p1 = db.getResource();
	ResPool<string>::shared_ptr p2 = p1;

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
		my_sleep(5);
	}
}

void f1( int id){
	my_sleep( 750);
	for( int i=0; i<100; ++i){
		char	cBuf[512];
		sprintf( cBuf,"\nThread ID : %d, times: %d. ... \n",id,i);
		cout << cBuf;
		try{
			ResPool<string>::shared_ptr p1 = db.getResource();
			ResPool<string>::shared_ptr p2 = p1;
			sprintf( cBuf,"Str1: %s\n",p1->c_str());
			cout << cBuf;
			sprintf( cBuf,"Str2: %s\n",p2->c_str());
			cout << cBuf;

		}catch( ... ){
			cerr << "Error occur in f1().\n";
		}

	}
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

	cout << "Sleep 5 seconds, please mark down memory usage for latter comparison.\n";
	my_sleep(5);

	if(argc>=3){
		MAX_DB_CONNECTION = atoi(argv[2]);
	}

	char buf[] = "a";
	for( int i=0; i<MAX_DB_CONNECTION; ++i){
		++(*buf);
		db.add( new string(buf));
	}

	RecursiveFun();

	//f1();
	//f2();
	//f3();

	//ResPool<string>::shared_ptr p1 = db.getResource();
	//ResPool<string>::shared_ptr p2 = p1;
	//ResPool<string>::shared_ptr p3 = db.getResource();


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


	cout << "Test done, sleep 30 seconds, please mark down and check if memory leak.\n";
	my_sleep( 30*1000);


	return	0;
}


