/**
 * \file
 * @see app_signals.h
 * 
 * copyright Liang,David
 * 
 * @author Liang,David
 * 
 * These source files are released under the GPLv3 license.
 * 
 * \b Modify_log:
 * 	- 2012-1-25 David: 文件创建
 * 
 */ 

#include "app_signals.h"

#define __MY_APP_SIGNALS_CPP_MACRO \
	std::map<int, AppSig::AppMsg> AppSig::vMsg; \
	std::map<int, AppSig::AppMsg> AppSig::vModule;

#undef	ERR_CODE_DEFINE
#define ERR_CODE_DEFINE(SIG, val, e_desc, local_desc) const int AppSig::SIG = setAppMsg( val, e_desc, local_desc );

#undef	MODULE_DEFINE
#define MODULE_DEFINE(SIG, val, e_desc, local_desc) const int AppSig::SIG = setModName( val, e_desc, local_desc );
#include "app_signals.h"
#include "stdlib.h"
#include "string.h"

//using namespace bd_gs_cmd_sh_ns;


// 在错误信息记录中查找出ErrCode所代表的信息文本.
const char * AppSig::getMsg( int ErrCode, bool local_language)
{
    std::map< int, AppSig::AppMsg>::iterator it = vMsg.find( ErrCode);
    if( it==vMsg.end() )
    {
        return "Undefined error!";
    }
	const char * p = NULL;
	if( !local_language || it->second.loMsg.empty()){
		p = it->second.enMsg.c_str();
	}else{
		p = it->second.loMsg.c_str();
	}
	return p;
}

std::map< int, AppSig::AppMsg>& AppSig::getErrorList(){

	return vMsg;
}

const char * AppSig::getModName(int ModCode, bool local_language)
{
	std::map< int, AppSig::AppMsg>::iterator it = vModule.find( ModCode);
	if( it==vModule.end() )
	{
		return "Undefined Module!";
	}
	const char * p = NULL;
	if( !local_language || it->second.loMsg.empty()){
		p = it->second.enMsg.c_str();
	}else{
		p = it->second.loMsg.c_str();
	}
	return p;
}

std::map< int, AppSig::AppMsg>& AppSig::getModList(){

	return vModule;
}

// 构造AppMsg对象
AppSig::AppMsg::AppMsg( int code, const char * en_msg, const char * local_msg)
{
    this->code = code;
	if( NULL!=en_msg){
		enMsg = en_msg;	
	}
	if( NULL!=local_msg){
		loMsg = local_msg;
	}
}


// 用入口参数构造AppMsg对象, 返回错误码,以便于初始化错误码变量
int AppSig::setAppMsg( int code, const char * en_msg, const char * local_msg)
{
	AppSig::AppMsg msg ( code, en_msg, local_msg);
	AppSig::vMsg.insert( std::pair<int,AppSig::AppMsg>(code, msg));
	return code;
}


int AppSig::setModName( int code, const char * en_name, const char * local_name)
{
	AppSig::AppMsg msg ( code, en_name, local_name);
	AppSig::vModule.insert( std::pair<int,AppSig::AppMsg>(code, msg));
	return code;
}


int AppSig::loadMsg( std::istream & is, bool local_language /* = true */ ){
	int ret = 0;
	char buf[4096] = {0};
	while( is.getline( buf, sizeof(buf)-1)){
		char *p = strchr( buf, ',');
		if(NULL==p){
			continue;
		}
		*p = 0;
		++p;
		if( 0==*p){
			continue;
		}
		int id = atoi(buf);
		std::map< int, AppSig::AppMsg>::iterator it = vMsg.find( id);
		if( it!=vMsg.end() ){
			std::string & oldMsg = local_language ? it->second.loMsg : it->second.enMsg;
			oldMsg = p;
		}else{
			setAppMsg( id, p, NULL);
		}
		++ret;
	}
	return	ret;
}

int AppSig::saveMsg( std::ostream & os, bool local_language ){
	int ret = 0;
	for( std::map< int, AppSig::AppMsg>::iterator it = vMsg.begin();
		 it!=vMsg.end();
		 ++it, ++ret){
		AppSig::AppMsg & appMsg = it->second;
		std::string & msg = (local_language&& (!appMsg.loMsg.empty())) ? appMsg.loMsg : appMsg.enMsg;
		os << appMsg.code << "," << msg.c_str() << std::endl;
	}
	return	ret;
}


int AppSig::loadModName( std::istream & is){
	int ret = 0;
	char buf[4096] = {0};
	while( is.getline( buf, sizeof(buf)-1)){
		char *p = strchr( buf, ',');
		if(NULL==p){
			continue;
		}
		*p = 0;
		++p;
		if( 0==*p){
			continue;
		}
		int id = atoi(buf);
		std::map< int, AppSig::AppMsg>::iterator it = vModule.find( id);
		if( it!=vModule.end() ){
			std::string & oldMsg = it->second.loMsg;
			oldMsg = p;
			++ret;
		}
	}
	return	ret;
}

//! 将英文模块名输出到流中; 内容格式与 loadModName的一致.
int AppSig::saveModName( std::ostream & os){
	int ret = 0;
	for( std::map< int, AppSig::AppMsg>::iterator it = vModule.begin();
		it!=vModule.end();
		++it, ++ret){
			AppSig::AppMsg & appMsg = it->second;
			std::string & msg = appMsg.enMsg;
			os << appMsg.code << "," << msg.c_str() << std::endl;
	}
	return	ret;
}



