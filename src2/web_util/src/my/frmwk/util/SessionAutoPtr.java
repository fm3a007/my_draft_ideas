/**
 * \file
 * 
 * 
 * @author  David

 * 
 * 
 * These source files are released under the GPLv3 license.
 *
 */ 

package my.frmwk.util;

import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *	这个类的作用在于自动释放连接对象,避免开发人员忘记释放.
 *
 *	用法要点：
 *		-#,	公用模块提给尽可能统一的接口获取 Hibernate Session,如叫做 getSession();
 *		-#,	上层应用逻辑通过获取到 Hibernate Session后交给 SessionAutoPtr 来保管;
 *		-#,	以上面述的 getSession(或你自己的真实方法名)为关键字进行代码检查, 保证用 try-with-resources的方式使用.
 *
 *	@author David
 *
 */
public class SessionAutoPtr implements AutoCloseable
{
	public	SessionAutoPtr( Session s){
		ss = s;
	}
	
	/** 连接对象 */
	public	Session	ss = null;

	/** 事务对象 */
	public	Transaction tx = null;
	
	public	Transaction beginTransaction(){
		return	tx = ss.beginTransaction();
	}
	
	/** 提交,并释放 Transaction对象 */
	public	void	commit(){
		tx.commit();
		tx = null;
	}
	
	/** 回滚,并释放 Transaction对象 */
	public	void	rollback(){
		tx.rollback();
		tx = null;
	}

	// 自动释放资源
	@Override
	public void close() 
	{
		if(null!=tx){
			tx.rollback();
			tx = null;
		}
		if(null!=ss){
			ss.close();
			ss = null;
		}
	}

}
