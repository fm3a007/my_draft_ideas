/**
 * 
 * These source files are released under the GPLv3 license.
 *
 */
package web_test;

/**
 * @author David
 *
 */
public class TestCls1 {
	
	public	TestCls1(){
		;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public	String	getMsg(int i){
		String	msg = "hello ...";
		msg += m_iCount++;
		msg += " ... ";
		msg += i;
		
		return	msg;
	}
	
	protected	static	int	m_iCount = 0;	

}
