/**
 * \file
 * 异常处理类的声明.
 *
 * 把常见的 Exception到放到一个公用的类中检查处理.
 * 封装这部分程序作为公用模块的目的是减少try-catch时要catch一大堆的不同异常类型.
 * 
 * @author Liang,David
 * 
 * These source files are released under the GPLv3 license.
 * 
 * \b Modify_log:
 * 	- 2014-9-9 David: file create
 * 
 */ 

#if ! defined(	__COMMON_EXCEPTION_HANDLER_HEADER_FILE_ ) 
#define			__COMMON_EXCEPTION_HANDLER_HEADER_FILE_

#include <iostream>
#include <exception>

//! 用于简化异常处理的类, 便于把异常处理封装成可用重的模块
class ExceptionLoggingHandler{

public:
	//ExceptionLoggingHandler(){};
	//~ExceptionLoggingHandler(){};

	//! 捕捉所有异常然后输出到 os中, 同时也输出 catcherFunction, 以便追溯
	static void print( std::ostream & os, const char * catcherFunction);

};




#define MY_CATCH_ALL( std_ostream ) \
	catch( ... )   \
{   \
	ExceptionLoggingHandler::print( std_ostream, __FUNCTION__ ); \
}  


#endif	//programma once


