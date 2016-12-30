/**
 * SessionDatasetProxy.java
 * 
 * These source files are released under the GPLv3 license.
 *
 */
package my.frmwk.util.chart;

import javax.servlet.http.HttpSession;

/**
 * Description:
 * 	To save dataset in session, returning an Integer,
 *  by which to retrieve this dataset in other requesting
 *  within same session
 *  
 *  
 * @author	Liang,David
 * Date:	2011-3-6
 * 
 *
 */
public class SessionDatasetProxy {

	//	Session to store dataset
	private	HttpSession session;
	//	prefixion for Proxy
	private	String	strPref;

	/**
	 * Constructor method
	 *
	 */
	public SessionDatasetProxy() {
		session = null;
	}

	/**
	 * @param s	HttpSession where to store dataset latter
	 * @param pref prifixion for Proxy,
	 *  so that Proxy class can be reuse by giving a different prefixion
	 */
	public SessionDatasetProxy( HttpSession s, String pref) {
		init(s,pref);
	}
	
	
	public int init( HttpSession s, String pref){
//		System.out.println(this.getClass().getName()+" is called!");
        StackTraceElement[] ste = new Throwable().getStackTrace();
//		System.out.println( "Location:" + ste[ste.length-1].getLineNumber());
		if(null != pref)
			strPref = pref;
		else
			strPref = "soc_img";

		session = s;
		if(null==session)
			return	-1;
		return	0;
	}

	/**
	 * @param s HttpSession where to store dataset for latter useage
	 */
	public SessionDatasetProxy( HttpSession s){
//		System.out.println(this.getClass().getName()+" is called!");
        StackTraceElement[] ste = new Throwable().getStackTrace();
//		System.out.println( "Location:" + ste[ste.length-1].getLineNumber());
		init( s, null);
	}
	
	/**
	 * @param id
	 * @return generate session_attribute_name
	 * 	Note:	because session_attribute_name is multi-referenced,
	 * 			so a common method will be convenience 
	 */
	protected String id_str(int id){
		return	strPref+"_ss_ds_proxy_"+ Integer.toString(id);
	}

	/**
	 * @param cid: chart_dataset_session_ID
	 * @return	retrived dataset, null means failed to retrived
	 * 		Since dataset is retrived, dataset will be cleared to free resource
	 */
	public	Object getData( int id){
		return	getData(id, true);
	}

	/**
	 * @param cid: chart_dataset_session_ID
	 * @param keep: 'true' tells Proxy to leave dataset in session 
	 * @return	retrived dataset, null means failed to retrived
	 * 		Since dataset is retrived, dataset will be cleared to free resource
	 */
	public	Object getData( int id, boolean keep ){

		if(null==session)
			return	null;
		
		String	str = id_str(id);
		Object o = session.getAttribute( str );

		if(!keep)
			session.removeAttribute(str);

		return	o;
	}
	
	/**
	 * @return : allocated id, negative means error,
	 * 
	 */
	synchronized  protected	int	getNextDatasetId(){		
		if(null==session)
			return	-1;
		
		String	strAtr = strPref+"_last_id";
		Object o = session.getAttribute(strAtr);
		int iCur = ( null==o ? 0 : (Integer)o );
		
		int iNext = iCur + 1;
		//	maximum 1000 thought to be enough
		//	can be set to larger, but cause server heavier loading
		iNext %= 1000;	
		session.setAttribute(strAtr, iNext);
		
		return	iCur;
	}
	
	/**
	 * @param dataset : which to be saved in to session
	 * @return : id for retriving dataset, negative means error
	 */
	public int	saveData( Object dataset){
		
		if(null==session)
			return	-1;
		
		int	id = getNextDatasetId();
		if(id>=0)
			session.setAttribute( id_str(id), dataset);
		return	id;
	}
	public int	clearData( int id){
		if(null==session)
			return	-1;

		session.removeAttribute(id_str(id));
		return	0;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
