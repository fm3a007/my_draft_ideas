/**
 * \file
 * 
 * @copyright xxxx co., ltd
 * @author Liang,David
 * 
 */

package my.frmwk.util;

import java.io.Serializable;

import org.hibernate.Session;
import org.hibernate.Transaction;


/**
 * (基于Hibernate的)数据库操作相关的常用/通用DAO功能的实现. 
 * 
 * 
 * @author David
 * 
 */
public abstract class BaseDbDao {

	protected Session sess = null;
	protected Transaction trans = null;
	protected int errCod = 0;

	public BaseDbDao(Session s) {
		sess = s;
		ResourceCheckBegin();
	}

	/** 开始事务 */
	public int begin() {
		if (null == trans) {
			trans = sess.beginTransaction();
		}
		return 0;
	}

	/** 提交事务 */
	public int commit() {
		if (null != trans) {
			trans.commit();
			trans = null;
		}
		return 0;
	}

	/** 回滚事务 */
	public int rollback() {
		if (null != trans) {
			trans.rollback();
			trans = null;
		}
		return 0;
	}

	/** 关闭资源 */
	public int close() {
		try {
			// 实际使用中发现最好勿自动提交, 因为修改了数据会触发hibernate自动update
			//commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != sess) {
				sess.close();
				sess = null;
			}
			trans = null;
		}
		return 0;
	}

	public boolean isClosed() {
		return (null == sess);
	}

	public int getLastErr(){
		return errCod;
	}

	/**
	 * 保存信息, 适用于各个pojo/bean.
	 * 
	 * 若ID为空或为0则新增(生成的ID填到对象对应的变量中),否则覆盖旧记录. 
	 * 
	 * @param obj 待保存对象
	 * @return 0 - 成功, 其他为错误信息.
	 */
	public int save( Object obj){
		begin();
		obj = sess.merge( obj);
		sess.update( obj);
		return	0;
	}

	public Object get(Class c, Serializable id) {
		return sess.get( c, id);
	}

	public int delete(Object obj) {
		begin();
		sess.delete(obj);
		return 0;
	}

		
	
	// 用于记录是谁获取了当前对象, 便于定位未释放资源的情况
	// 和下面的两个函数是配套的
	private String strStack;

	/* 要在释构函数上释放资源, 更要揪出是谁未释放资源 */
	protected void ResourceCheckBegin() {
		StackTraceElement stack[] = Thread.currentThread().getStackTrace();
		StringBuffer strBuf = new StringBuffer();
		int n = stack.length > 8 ? 8 : stack.length;
		for (int i = 2; i < n; ++i) {
			strBuf.append(stack[i].getClassName());
			strBuf.append("->");
			strBuf.append(stack[i].getMethodName());
			strBuf.append("\n");
		}
		strStack = strBuf.toString();
	}

	// 用于记录谁申请了连接, 便于发便漏掉释放资源的情况
	// public可访问性是为了方便外部检查
	protected void ResourceCheckEnd() {
		if (null != sess) {
			System.err
					.println("==========================================================");
			System.err
					.println("Session was forgot to be closed, which was open in:");
			System.err.println(strStack);
			System.err
					.println("==========================================================");
		}
	}

	protected void finalize() throws Throwable {
		ResourceCheckEnd();
		close();
		super.finalize();
	}

}
