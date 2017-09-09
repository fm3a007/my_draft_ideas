/*
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


/** 用法: 

// 在头文件中先包含下面一行, 															 
		#define ERR_CODE_DEFINE( SIG, e_desc, local_desc ) SIG, 
// 然后再: 
		enum {
		#include "当前这个头文件"
	UNDEF //enum结束前自行定义多一个
		}

// 若有需要(若做成类声明中)则再加上下面的声明:
		/ ** 
		 * 通过错误码返回文件信息, lanIdx 表示是否返回哪种语言.
		 * 
		 * code 的取值无论是正负都会转换成正值处理. 因为有些函数用负值表示出错, 正值表示其它含义.
		 * 这种情况下, 负值取反就是 ErrCode中定义的错误码; 但正值情况下, 函数的调用者应自行处理, 
		 * 而不应传给当前这个转换函数处理(这时调用本函数无意义).\n
		 * \n
		 *
		 * @param	int code
		 * @param	unsigned int lanIdx
		 * @return	const char * 
		 *
		 * @see <参见暂无>
		 * /
		static	const char * getErrMsg( int code, unsigned int lanIdx=0);

// 在 .cpp 文件中包含下面几行:
		//  ----- for error code - message translation 
		#undef	ERR_CODE_DEFINE
		#define ERR_CODE_DEFINE(SIG, e_desc, local_desc)  YourClassName__setErrMsg( YourClassName::SIG, e_desc, local_desc); 
		static	
		const	unsigned	int	MESSAGE_LANGUAGES = 2;
		static	
		std::map< int, char* >	gUpdaterErrMsg[MESSAGE_LANGUAGES];
		static
		int		YourClassName__setErrMsg( int code, char* msg1, char* msg2){
			gUpdaterErrMsg[0][code] = msg1;
			gUpdaterErrMsg[1][code] = msg2;
			return	code;
		}
		static
		int		YourClassName__setErrMsg( ){
			#include "当前这个头文件"
			return	1;
		}
		static	
		int		VARIABLE_HELP_TO_INITIALIZE_YOUR_ERROR_MESSAGES = YourClassName__setErrMsg();
		//  ----- for error code - message translation 

// 若有需要实现错误码到消息的转换, 在 .cpp 文件中包含下面几行:
		//		错误码到 文本信息 的转换函数
		const char*	YourClassName::getErrMsg( int code, unsigned int lanIdx){
			unsigned	int cod2 = code<0 ? -1*code : code;

			if( lanIdx>MESSAGE_LANGUAGES){
				lanIdx = MESSAGE_LANGUAGES-1;
			}

			std::map< int, char* >::iterator it = gUpdaterErrMsg[lanIdx].find( cod2);
			if( it==gUpdaterErrMsg[lanIdx].end()){
				return	"Undefined error code!";
	}
			return	it->second;
	}


*/


////////////////////////////////////////////////////////////////////
//	// TODO: please declare you constants here:

	ERR_CODE_DEFINE( SIG_OK,				"OK!", NULL)
	ERR_CODE_DEFINE( ER_SYS_EXCEPTION,		"System exception.",NULL)
	ERR_CODE_DEFINE( ER_INVALID_INPUT,		"Invalid input value.",NULL)
	ERR_CODE_DEFINE( ER_USER_NOT_EXISTS,	"User not exists.",NULL)
	ERR_CODE_DEFINE( ER_AUTH_FAILURE,		"Authenticate failure.",NULL)
	ERR_CODE_DEFINE( ER_USER_EXISTS,		"User exists.",NULL)
	ERR_CODE_DEFINE( ER_INVALID_USER,		"Invalid user account.",NULL)
	ERR_CODE_DEFINE( ER_USER_LOCKED,		"User account is locked.",NULL)
	ERR_CODE_DEFINE( ER_NO_PRIVILEGE,		"Insufficient privilege.",NULL)
	ERR_CODE_DEFINE( ER_ILLEGAL_DATA,		"Illegal data.",NULL)
	ERR_CODE_DEFINE( ER_ILLEGAL_OPERATION,	"Illegal operation.",NULL)


