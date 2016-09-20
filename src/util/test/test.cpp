/**
 * \file
 *
 *	unit test.
 */

#include "util.h"

#include <iostream>
#include <string.h>

using namespace std;

time_t	getLocal3rdDay(){
	tm tm0;
	memset( &tm0, 0, sizeof(tm));
	tm0.tm_year = 1970 - 1900;
	tm0.tm_mon = 0;
	tm0.tm_mday = 4;
	tm0.tm_yday = 3;
	tm0.tm_hour = 0;
	tm0.tm_min = 0;
	tm0.tm_sec = 0;
	tm0.tm_isdst = 0;
	return	mktime( &tm0);
}

#include <boost/filesystem.hpp>

int		testFS(){

	namespace	fs = boost::filesystem ;

	fs::path pth("../../hello.txt");

	cout << "\nPath:" << pth.string().c_str()
		<< "\nabsolute path: " << fs::absolute( pth).string().c_str()
		<< "\ngeneric path: " << pth.generic_string().c_str()
		<< "\nnormalized path: " << pth.normalize().string().c_str() 
		//<< "\ndirectory_string: " << pth.directory_string().c_str()
		<< endl;

	return	0;

}


int main( int argc, char* argv[])
{
	cout << "Hello ... " << endl;

	time_t t = time(NULL);

	time_t tLocal3rdDay = getLocal3rdDay();
	
	int nTimeStamp2 = timestamp( tLocal3rdDay);

	int nTimeStamp = timestamp( t);

	//cout << "Password:";
	//string psw = GetPasswordFromStdIn();
	//cout << "\nPassword you inputted:" << psw.c_str() << endl;

/*
	char buf[512] = {0};
	str_md5( argv[1], buf);
	cout << "MD5: " << buf << endl;
*/

	std::string	url = url_encode( "a b c _ ~!@!#@#$#@$" );
	//std::string	url;

	std::string url2( url);
	std::string url3( "hello");

	testFS();

	return 0;
}

