/**
 * \file
 * 异常处理类的实现.
 *
 * 把常见的 Exception到放到一个公用的类中检查处理.
 * 
 * @author Liang,David
 * 
 * These source files are released under the GPLv3 license.
 * 
 * \b Modify_log:
 * 	- 2014-9-9 David: file create
 * 
 */ 


#if defined( WINDOWS ) || defined( _WIN32 ) || defined( _WIN64 ) 
#define	_CATCH_MFC_EXCEPTION_HERE_
#endif	//

#if defined( _CATCH_MFC_EXCEPTION_HERE_ ) 
#include "afx.h"
#else
#endif	// #if defined( _CATCH_MFC_EXCEPTION_HERE_ ) 

#include "exception_handler.h"
#include <stdio.h>
#include <typeinfo>


#if defined( _CATCH_MFC_EXCEPTION_HERE_ ) 
static void handleCException( std::ostream & os, CException & e){
	char buf[512] = {0};
	if(e.GetErrorMessage( buf, sizeof(buf)-1)){
		os << buf;
	}
	e.Delete();
}
#endif	// #if defined( _CATCH_MFC_EXCEPTION_HERE_ ) 

void ExceptionLoggingHandler::print( std::ostream & os, const char * catcherFunction )
{
	try{
		throw;
	}

#if defined( _CATCH_MFC_EXCEPTION_HERE_ ) 
	catch (CMemoryException* e){
		const std::type_info & tinf = typeid( *e);
		os << "Exception(" << tinf.name() <<") caught in " << catcherFunction << ", message: ";
		handleCException( os, *e);
	}catch (CMemoryException& e){
		const std::type_info & tinf = typeid( e);
		os << "Exception(" << tinf.name() <<") caught in " << catcherFunction << ", message: ";
		handleCException( os, e);
	}catch (CFileException* e){
		const std::type_info & tinf = typeid( *e);
		os << "Exception(" << tinf.name() <<") caught in " << catcherFunction << ", message: ";
		handleCException( os, *e);
	}catch (CFileException& e){
		const std::type_info & tinf = typeid( e);
		os << "Exception(" << tinf.name() <<") caught in " << catcherFunction << ", message: ";
		handleCException( os, e);
	}catch (CException* e){
		const std::type_info & tinf = typeid( *e);
		os << "Exception(" << tinf.name() <<") caught in " << catcherFunction << ", message: ";
		handleCException( os, *e);
	}catch (CException& e){
		const std::type_info & tinf = typeid( e);
		os << "Exception(" << tinf.name() <<") caught in " << catcherFunction << ", message: ";
		handleCException( os, e);
	}
#endif	// #if defined( _CATCH_MFC_EXCEPTION_HERE_ ) 
	catch (std::exception* e){
		const std::type_info & tinf = typeid( *e);
		os << "Exception(" << tinf.name() <<") caught in " << catcherFunction << ", message: ";
		os << e->what();
	}catch (std::exception& e){
		const std::type_info & tinf = typeid( e);
		os << "Exception(" << tinf.name() <<") caught in " << catcherFunction << ", message: ";
		os << e.what();
	}catch( char e){
		const std::type_info & tinf = typeid( e);
		os << "Exception(" << tinf.name() <<") caught in " << catcherFunction << ", message: ";
		os << e;
	}catch( const char * e){
		const std::type_info & tinf = typeid( e);
		os << "Exception(" << tinf.name() <<") caught in " << catcherFunction << ", message: ";
		os << e;
	}catch( float * e){
		const std::type_info & tinf = typeid( e);
		os << "Exception(" << tinf.name() <<") caught in " << catcherFunction << ", message: ";
		os << *e;
	}catch( float & e){
		const std::type_info & tinf = typeid( e);
		os << "Exception(" << tinf.name() <<") caught in " << catcherFunction << ", message: ";
		os << e;
	}catch( double * e){
		const std::type_info & tinf = typeid( e);
		os << "Exception(" << tinf.name() <<") caught in " << catcherFunction << ", message: ";
		os << *e;
	}catch( double & e){
		const std::type_info & tinf = typeid( e);
		os << "Exception(" << tinf.name() <<") caught in " << catcherFunction << ", message: ";
		os << e;
	}catch( int *e){
		const std::type_info & tinf = typeid( e);
		os << "Exception(" << tinf.name() <<") caught in " << catcherFunction << ", message: ";
		os << *e;
	}catch( void * e){
		const std::type_info & tinf = typeid( e);
		os << "Exception(" << tinf.name() <<") caught in " << catcherFunction << ", message: ";
		char buf[64] = {0};
		sprintf( buf, "void * e=0x%llx .", (long long)e);
		os << buf;
	}catch( int e){
		const std::type_info & tinf = typeid( e);
		os << "Exception(" << tinf.name() <<") caught in " << catcherFunction << ", message: ";
		os << e;
	}catch( ... ){
		os << "Unknown Exception caught in " << catcherFunction << ", No further information!";
	}

}


