/*	file
 *	OTL SPECIFICATION MACRO declearation file
 */



#if defined (WIN32)
#define OTL_ODBC
#else // NON WIN32
#define OTL_ODBC_UNIX
#endif	// WIN32
//#define OTL_ORA9I // Compile OTL 4.0/OCI9i
#define OTL_STL // Turn on STL features
#define OTL_ANSI_CPP // Turn on ANSI C++ typecasts
//#define OTL_ORA_TIMESTAMP
//#define OTL_ORA_UTF8 // Enable UTF8 OTL for OCI9i

#include "otlv4.h"
