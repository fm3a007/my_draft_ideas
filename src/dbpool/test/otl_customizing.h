/**
 *  \file
 *  
 *  OTL_SPECIFICATION_MACRO should be always the same in entirely program source code,so pls define them in one file for global use.
 *  The file is an example to illustrate how to use.
 */


#if defined( _WIN32 ) || defined( _WIN64 )
#define OTL_ODBC
#else	// WINDOWS or LINUX
#define OTL_ODBC_UNIX
#endif	// WINDOWS
//#define OTL_ORA9I // Compile OTL 4.0/OCI9i
#define OTL_STL // Turn on STL features
#define OTL_ANSI_CPP // Turn on ANSI C++ typecasts
//#define OTL_ORA_TIMESTAMP
//#define OTL_ORA_UTF8 // Enable UTF8 OTL for OCI9i

#include "otlv4.h"
