/**
 * \file
 * 信号(错误码)声明.
 *
 *	只在本文件修改 模块, 错误码 的常量变量, 取值, 说明 等信息即可, 勿改其他程序.
 * 
 *     文件使用UTF-8字符集(无BOM), 以便兼容各OS,各版本的底层库的同时, 亦能做到多国语言(不限于中文)的支持. 
 * 文本的内容, 对于写入文件并无影响; 对于终端的输出,取决于OS能否支持. 当前的主流LINUX发行版都支持UTF-8,
 * 而WINDOWS平台支持ANSI和UNICDOE, 对于非UNICODE编码的非英语字符, 取决于具体的版本. 若本程序文件应用于
 * WINDOWS平台时, 若文本内容不涉及终端的输出时无任何影响,可正常使用. \n
 *
 *	   若 VC编译时因中文原因报错, 请修改WINDOW默认语言(非UNICODE字符时的默认字符集,中文版一般为"中文"即GBK),
 * 设为"英语", 即ANSI, 便能正常编译. 该选项需重启计算机, 该选项影响的是编译器的预处理机制.VC的编译器会在编译
 * 前将非UNICODE的源码文件以当前计算机的当前默认语言字符集来看待. 由于无BOM的UTF-8文件(BOM是MS自家标准,
 * 为了兼容LINUX所以文件统一为无BOM), 译器会认为是GBK, 所以有些字符会在GBK码表中找不到, 所以报错. 
 * 把默认语言设为英文, UTF-8文件中的任一个字节都会是合法的ANSI字符, 所以编译能通过, 而且还能保证文本的内
 * 容不被隐性转码. \n
 *
 *     若本程序应用在WINDOWS下且所涉及内容需要在终端下输出(可能会乱码), 而又不想在输出的程序中加入转码的功能,
 * 可将本文件另存为GBK, 或其他合适的字符集, 但请自行考虑对程序功能逻辑的影响.\n
 * 
 * copyright Liang,David
 * 
 * @author Liang,David
 * 
 * \b Modify_log:
 * 	- 2012-1-25 David: file create
 * 
 */ 

#ifdef    __MY_APP_SIGNALS_CPP_MACRO
#undef _MY_APP_SIGNALS_HEADER_FILE_
#endif	//__MY_APP_SIGNALS_CPP_MACRO

#if ! defined ( _MY_APP_SIGNALS_HEADER_FILE_ )
#define _MY_APP_SIGNALS_HEADER_FILE_ 

#include <map>
#include <iostream>

//namespace myns{

#ifdef    __MY_APP_SIGNALS_CPP_MACRO
	__MY_APP_SIGNALS_CPP_MACRO
#else	// ! __MY_APP_SIGNALS_CPP_MACRO

#define ERR_CODE_DEFINE(SIG, val, e_desc, local_desc) static const int SIG ;
#define MODULE_DEFINE(SIG, val, e_desc, local_desc) static const int SIG ;

/** 
 *	信号,错误码, 功能模块号等 的声明类.
 *
 *	请使用 MODULE_DEFINE 和 ERR_CODE_DEFINE 来声明常量变量, 不需在CPP文件中重复定义.
 */
class AppSig
{
public:
	
#endif	// __MY_APP_SIGNALS_CPP_MACRO

////////////////////////////////////////////////////////////////////
//	// TODO: please declare you constants here:

	ERR_CODE_DEFINE( SIG_OK ,0, "OK!", NULL);
	
	// module declearation
	MODULE_DEFINE( MD_SYSTEM_NAME ,9999, "Lean Production Management System", NULL);
	MODULE_DEFINE( MD_BKG_PRG ,0, "BKG_PROGRAM", NULL);
	MODULE_DEFINE( MD_LOGIN ,1, "LOGIN_CHECK", NULL);
	MODULE_DEFINE( MD_USER_ADMIN ,2, "User Administration", NULL);

	// Error code declearation 
	ERR_CODE_DEFINE( ER_SYS_EXCEPTION, -9999,	"System exception.",NULL);
	ERR_CODE_DEFINE( ER_INVALID_INPUT, -9998,	"Invalid input value.",NULL);
	ERR_CODE_DEFINE( ER_USER_NOT_EXISTS, -1,	"User not exists.",NULL);
	ERR_CODE_DEFINE( ER_AUTH_FAILURE, -2,	"Authenticate failure.",NULL);
	ERR_CODE_DEFINE( ER_USER_EXISTS, -3,	"User exists.",NULL);
	ERR_CODE_DEFINE( ER_INVALID_USER, -4,	"Invalid user account.",NULL);
	ERR_CODE_DEFINE( ER_USER_LOCKED, -5,	"User account is locked.",NULL);
	ERR_CODE_DEFINE( ER_NO_PRIVILEGE, -6,	"Insufficient privilege.",NULL);
	ERR_CODE_DEFINE( ER_ILLEGAL_DATA, -7,	"Illegal data.",NULL);
	ERR_CODE_DEFINE( ER_ILLEGAL_OPERATION, -8,	"Illegal operation.",NULL);	


//	end of constants declaration.
////////////////////////////////////////////////////////////////////


#ifndef    __MY_APP_SIGNALS_CPP_MACRO

public:

		class AppMsg
		{
		public:
			int code;				//! 错误码
			std::string enMsg;	//! 英文的说明
			std::string loMsg;	//! 本地语言的说明

			// 构造函数
			AppMsg( int code, const char * en_msg, const char * local_msg);

		};


	/**
     *	将错误码转换为信息
     */
    static const char *getMsg( int ErrCode, bool local_language = false);
	static std::map< int, AppMsg>& getErrorList();

	/**
     *	将模块代号转换为模块名称
     */
	static const char *getModName( int ModCode, bool local_language = false);
	static std::map< int, AppMsg>& getModList();

	//! 从流中读入消息, 默认是加载当地语言的消息; 内容格式与 saveMsg的一致.
	static int loadMsg( std::istream & is, bool local_language = true );

	//! 将消息输出到流中; 内容格式与 loadMsg的一致.
	static int saveMsg( std::ostream & os, bool local_language );

	//! 从流中读入当地语言的模块名; 内容格式与 saveModName的一致.
	static int loadModName( std::istream & is);

	//! 将英文模块名输出到流中; 内容格式与 loadModName的一致.
	static int saveModName( std::ostream & os);

protected:

	//!用于保存错误码-信息对照记录的数据集
	static std::map< int, AppMsg> vMsg;
	//!用于保存模块号-模块名称对照记录的数据集
	static std::map< int, AppMsg> vModule;

    //! 便于初始化错误码变量的值
    //friend int setAppMsg( int code, const char * en_msg, const char * local_msg);
	static int setAppMsg( int code, const char * en_msg, const char * local_msg);
	static int setModName( int code, const char * en_name, const char * local_name);

private:


};

#endif	// ! __MY_APP_SIGNALS_CPP_MACRO

//}	// namespace myns

#endif // _MY_APP_SIGNALS_HEADER_FILE_


