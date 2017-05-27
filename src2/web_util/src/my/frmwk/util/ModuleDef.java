/**
 * \file
 * 
 * 保存模块代号与及其说明.
 * 
 * 
 * copyright (C) 2000, 2011
 * 
 * 
 * 
 * @author Liang,David
 *  
 */

package my.frmwk.util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * 方便定义各个模块将"功能模块"和"功能号"等等信息.
 * 
 * @author Liang,David
 * 
 */
public class ModuleDef {
	
	
	public static	class	FuncDef{
		public	FuncDef( int modId, int funcId, String funcDesc,String funcDesc2){
			this.modId = modId;
			this.funcId = funcId;
			this.funcDesc = funcDesc;
			this.funcDesc2 = funcDesc2;
		}

		public	int		modId = 0;
		public	int		funcId = 0;
		public	String	funcDesc = "";	// 功能描述
		public	String	funcDesc2 = ""; // 功能描述(方便用两种语言)
	}
	

	private static Map<Integer, FuncDef> m_FuncDefMapLst = new HashMap<Integer, FuncDef>();


	/**
	 * 获取功能号的描述.
	 * 
	 * 该方法用于将功能ID转换成文本描述。
	 */
	public static FuncDef getFuncDescById( int modId, int funcId) {
		int	id = modId;
		id *= 100000;
		id += funcId;
		return m_FuncDefMapLst.get(id);
	}
	
	
	/**
	 * 注册错误码与错误信息
	 * 
	 * 各人请将自己负责的业务程序的错误码和描述通过该方法进行注册
	 */
	public synchronized static int add( int modId, int funcId, String desc, String desc2) 
	{
		int	id = modId;
		id *= 100000;
		id += funcId;
		
		FuncDef val = m_FuncDefMapLst.get( id);
		if( null!=val){
			try {
				throw new Exception("功能ID已存在！");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		m_FuncDefMapLst.put( id,  new FuncDef( modId, funcId, desc, desc2));
		return funcId;
	}
	
	public	static	List<FuncDef>	getFuncList(){
		return	new ArrayList<FuncDef>(m_FuncDefMapLst.values());
	}
	
	
	// 1,要写构造方法,避免编译器自动生成构造方法
	// 2,必需是私有方法,避免各处代码自己拥有该类对象
	private ModuleDef() {
	};

	/**
	 * 用于单元测试
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		// System.out.println( ErrorCode.getMsgByCode(ERR_XXMOD_XXXERROR));

	}

}

