/**
 * \file
 * 
 * unit test.
 * 
 */



#include <iostream>
#include "logger.h"

#include <time.h>

using namespace std;


int	testAutoEnterLeaveLoger( Logger&loger){
	int		ret = 0;
	AutoEnterLeaveLogger autoLog( loger, __FUNCTION__, __LINE__, &ret, Logger::L_BS_OPPERATE,
		"Hello test AutoEnterLeaverLoger:%d, %s", (int)123, "abcdefg<<");

	for( int i=0; i<5; ++i){
		printf( "%d,\thello, in %s, line %d\n", i, __FUNCTION__, __LINE__);
	}

	return	ret = 99;

}

void test( Logger & logger){
	for (int i = 0; i < 100; i++){
		int id = logger.log( Logger::L_BS_QUERY, i, 01, "hello");
		int stat = i%30;
		cout << "Logid: " << id << ", status: " << stat << endl;
		logger.log_stat( id, stat);
		logger.debug( __FUNCTION__, __LINE__, "Hello ...." );			  
		logger.error( __FUNCTION__, __LINE__, 123, "AAAAAAAAAAAA", 99, 2);

		logger.printf( Logger::L_DEBUG, __FUNCTION__, __LINE__, 0, 0, STAT_UNKNOWN, "abc%d,abc:%s,abc:%x,abc:%f", 123, "123",123,123.45);
		logger.printf( Logger::L_INFO, __FUNCTION__, __LINE__, "Hello, %s, %d, %d, %x", "abcde", 123, 456, 123456);
		logger.printf( Logger::L_INFO, __FUNCTION__, __LINE__, 1, "Hello, %s, %d, %d, %x", "abcde", 123, 456, 123456);
		logger.printf( Logger::L_ERROR, 0, 0, "Hello, %s, %d, %d, %x", "abcde", 123, 456, 123456);
		logger.printf( Logger::L_ERROR, 0, 0, 2, "Hello, %s, %d, %d, %x", "abcde", 123, 456, 123456);
	}
}

#include "logger_impl.h"

class TestCls{
public:
	TestCls()
	{

	}

	~TestCls(){
		Logger& loger = TheDummyLogger::getTheDummyLogger();
		char* p = (char*)&loger;
		loger.printf( Logger::L_DEBUG, __FUNCTION__, __LINE__, "object initailizing ...");
	}

};

TestCls	a;

TestCls b;

/**
 *
 * log_file的使用例子
 *
 */
int main( int argc, char * argv[])
{
	time_t t = time(NULL);
	std::cerr << ctime( &t) << std::endl;;
	SimpleLoggerMngr logMng1;

	testAutoEnterLeaveLoger( logMng1.getLogger());

	test( logMng1.getLogger());

	SimpleLoggerMngr logMng2("abc","log");
	testAutoEnterLeaveLoger( logMng2.getLogger());
	test( logMng2.getLogger());


	Logger & logger = logMng1.getLogger();
	logger.debug( 3, "debug ....");
	logger.error( 2, "error ....", -100);
	logger.info( 1, "info ....");
	int id = logger.log( 1, 2, 3, "log msg",0);
	id = logger.log( 3,2,1, "log 2 msg");
	logger.log_stat( id, 0);

	logger.debug( __FUNCTION__, __LINE__, "Hello ...." );

	logger.error( __FUNCTION__, __LINE__, 123, "AAAAAAAAAAAA", 99, 2);


	test( TheDummyLogger::getTheDummyLogger());

	t = time(NULL);
	std::cerr << ctime( &t) << std::endl;;

	return 0;
}

