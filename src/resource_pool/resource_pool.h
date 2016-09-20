/**
 *	\file
 *
 *	Description:
 *		Resource pool declaration file
 *  \n
 *	
 *	by:	Liang,David @ 2013-6-28
 *
 */


#ifndef _MY_RES_POOL_HEADER_
#define _MY_RES_POOL_HEADER_

#if	defined( _WITH_CXX11_ )
#include <mutex>
#include <condition_variable>
#include <memory>

#define	CXX11_STD std

#else

#include <boost/shared_ptr.hpp>
#include <boost/thread/locks.hpp>
#include <boost/thread/condition_variable.hpp>

#define	CXX11_STD boost

#endif	// _WITH_CXX11_

#include <vector>


/**
 *  Resource pool.
 *
 *  This class realizes resource pool management. 
 *	Provides method to achieve a resource.
 *  preventing abuse so important resource.
 *  \n
 *  \n
 */
template <typename T>
class ResPool{

public:

	ResPool(){
		iRest = iMax = 0;
	}

	virtual ~ResPool(){
		destroy();
	}

	class SpDeleter
	{
	public:
		SpDeleter( ResPool &pool){
			_pl = &pool;
		}

		SpDeleter( const SpDeleter& dlt){
			operator=( dlt);
		}

		SpDeleter & operator=( const SpDeleter& dlt){
			_pl = dlt._pl;
			return	*this;
		}

		void	operator()( T* p){
			_pl->release( p);
		}

	protected:
		ResPool	* _pl;
	private:
	};

	typedef	CXX11_STD::shared_ptr< T>	shared_ptr;


    /**
     *  Add resource to resource pool.
	 *
	 *	Resource will be deleted when resource pool dis-constructs.
     *	@Param:
	 *		res: pointer to a resource
     *	@return:
     *		>0 : succeed and return resources count; <0 means failed.
     */
	int add(  T * res){
		CXX11_STD::lock_guard<CXX11_STD::mutex> lock(_mutex);
		vRes.push_back( res);
		++iRest;
		return	++iMax;
	}

    /**
     *  release all resource before deconstruction.
     */
	void destroy(){
		{	// critical area
			CXX11_STD::lock_guard<CXX11_STD::mutex> lock(_mutex);

			for( int i=0; i<(int)iMax; ++i){
				T * p = vRes[i];
				if(NULL!=p){
					delete p;
					vRes[i] = NULL;
				}
			}
			iRest = iMax = 0;
		}
	}

    /**
     *  return a smart pointer of resource.
     *
     *  always return a different resource( blocked if no resource available), .
     *  We provide a smart_pointer (shared_ptr) for convenience, resource will be released automatically,
	 *	hope you enjoy it.
     */
	shared_ptr getResource(){
		T * p = NULL;
		{   // critical area
			CXX11_STD::unique_lock<CXX11_STD::mutex> lock(_mutex);
			while( iRest<=0){
				_cond.wait(lock);
			}
			p = vRes[iMax-iRest--];
		}
		shared_ptr pp( p, SpDeleter(*this));
		return	pp;
	}

protected:

	std::vector<T*> vRes;
	unsigned int	iRest;
	unsigned int	iMax;

	CXX11_STD::mutex _mutex;
	CXX11_STD::condition_variable   _cond;


    /**
     *  To release a resource, so that others can use the resource again.
     */
	virtual int release( T * pRes){
		if(NULL==pRes){
			return -1; 
		}

		// critical area
		{
			CXX11_STD::lock_guard<CXX11_STD::mutex> lock(_mutex);
			int iFound = 0;
			for( iFound=iMax-iRest-1; iFound>=0; --iFound ){
				if(pRes==vRes[iFound]){
					break;
				}
			}
			// not found
			if(iFound<0){
				return -2;
			}

			++iRest;
			int iCur = iMax-iRest;
			// need to swap ?
			if(iFound!=iCur){
				vRes[iFound] = vRes[iCur];
				vRes[iCur] = pRes;
			}
		}
		_cond.notify_one();
		return	0;
	}

private:



};




#endif // _MY_RES_POOL_HEADER_
