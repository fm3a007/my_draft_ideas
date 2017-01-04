/**
 * \file
 * 
 * 
 * @author  Liang, David
 *  
 * These source files are released under the GPLv3 license.
 * 
 */ 

package my.frmwk.util;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *	微信用户信息 相关的工具类.
 *
 *	各成员变量都是按照微信公众平台文档中的变量来命名， 含义见微信的文档.
 *
 *	@author David
 *
 */
public class WeixinUserInfoHelper {
	
	public	String	openid;
	
	public	String	nickname;
	
	public	int		sex;
	
	public	String	province;
	
	public	String	city;
	
	public	String	country;
	
	public	String	headimgurl;
	
	public	ArrayList<String>	privilege;
	
	public	String	unionid;
	
	public	String	language;
	

	public	String	access_token;
	
	public	int		expires_in;
	
	public	Date	expires_dtime;
	
	public	String	refresh_token;
	
	public	String	scope;
	
	public	int		errcode;
	
	public	String	errmsg;

	

	/**
	 * 要注意微信官网是否变更了接口URL.
	 * 
	 * @param AppId
	 * @param AppSecret
	 */
	public	WeixinUserInfoHelper( String AppId, String AppSecret){
		m_strAppid = AppId;
		m_strAppSecret = AppSecret;
	}
	
	/**
	 * 获取用户信息.
	 *
	 * 调用方式: []多线程并发调用, [Y]后台定时触发/[Y]外部触发 \n
	 * 调用频率: 峰值[1]( t/s), 平均值[1](t/s). \n
	 * 处理时长: max< [ 30,000] (millisecond), avg<[1000]( millisecond). \n
	 * 单次调用数据处理量: max=[ 1 ] records. \n
	 * \n
	 * @param code 参数含义见微信开发文档要求
	 * @param lang 语言,参数含义见微信开发文档要求
	 * 
	 * @return 非0表示出错, 0表示正常.
	 */
	public	int	fetchUserInfo( String code, String lang){

		int	ret = -999;
		try {
			Date now = new Date();
			WxAccessToken token = getAccessToken(m_strAppid, m_strAppSecret, code);

			errcode = token.errcode;
			errmsg = token.errmsg;
			
			if( errcode!=0){
				return	errcode;
			}

			access_token = token.access_token;
			expires_in = token.expires_in;
			if(expires_in>0){				
				this.expires_dtime = new Date( now.getTime()+1000*this.expires_in);
			}
			refresh_token = token.refresh_token;
			openid = token.openid;
			if(null!=token.unionid){
				unionid = token.unionid;				
			}
			scope = token.scope;			
		
			WxUserInfo usr = getUserInfo(access_token, openid, lang);
			errcode = usr.errcode;
			errmsg = usr.errmsg;
			
			nickname = usr.nickname;
			sex = usr.sex;
			province = usr.province;
			city = usr.city;
			country = usr.country;
			headimgurl = usr.headimgurl;
			privilege = usr.privilege;
			unionid = usr.unionid;
			language = usr.language;

			if( errcode!=0){
				return	errcode;
			}

			ret = 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return	ret;
	}
	
	
	/**
	 * 更新用户信息.
	 *
	 * 调用方式: []多线程并发调用, [Y]后台定时触发/[Y]外部触发 \n
	 * 调用频率: 峰值[1]( t/s), 平均值[1](t/s). \n
	 * 处理时长: max< [ 30,000] (millisecond), avg<[1000]( millisecond). \n
	 * 单次调用数据处理量: max=[ 1 ] records. \n
	 * 
	 * @return 负数表示出错, 其他表示正常.
	 */
	public	int	refresh(){
		
		// TODO:  尚未用到，暂未实现
		return	0;
	}
	

	protected	static	WxAccessToken	getAccessToken( String appId, String secret, String code)
		throws Exception
	{
//		https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
		StringBuffer str = new StringBuffer();
		str.append("https://api.weixin.qq.com/sns/oauth2/access_token?appid=").append(appId)
			.append("&secret=").append(secret).append("&code=").append(code).append("&grant_type=authorization_code");
		URL url = new URL(str.toString());
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
//		con.setDoOutput( false);
		InputStream is = con.getInputStream();
		String jsonStr = IOUtils.toString(is, "UTF-8"); 
		is.close();
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure( DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
		WxAccessToken json = mapper.readValue( jsonStr,  WxAccessToken.class);
		
		return	json;
	}
	
	protected	static	WxUserInfo	getUserInfo( String accessToken, String openid, String lang)
		throws Exception
	{
//		https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN
		StringBuffer str = new StringBuffer();
		str.append("https://api.weixin.qq.com/sns/userinfo?access_token=").append(accessToken)
			.append("&openid=").append(openid).append("&lang=").append(lang);

		URL url = new URL(str.toString());
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
//		con.setDoOutput( false);
		InputStream is = con.getInputStream();
		String jsonStr = IOUtils.toString(is, "UTF-8"); 
		is.close();
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure( DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
		
		WxUserInfo json = mapper.readValue( jsonStr,  WxUserInfo.class);
		
		return	json;
	}
	
	protected	String m_strAppid;
	
	protected	String	m_strAppSecret;
	
	
}

class	WxAccessToken{
	
	public	String	access_token;
	public	int		expires_in;
	public	String	refresh_token;
	public	String	openid;
	public	String	scope;

	public	String	unionid;

	public		int	errcode = 0;
	public		String	errmsg;
}

class	WxUserInfo{
	
	public	String	openid;
	public	String	nickname;
	public	int		sex;
	public	String	province;
	public	String	city;
	public	String	country;
	public	String	language;
	public	String	headimgurl;
	public	ArrayList<String>	privilege;
	public	String	unionid;
	
	
	public		int	errcode = 0;
	public		String	errmsg;
}




