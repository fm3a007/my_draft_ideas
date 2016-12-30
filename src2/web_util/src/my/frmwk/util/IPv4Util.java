/**
 * \file
 * @see IPv4Util
 *
 * 
 * These source files are released under the GPLv3 license.
 *
 *
 * @author David
 *
 * @version 1.0
 *
 */
package my.frmwk.util;

import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ipv4转换类，提供ip转换其他数值类型的函数。
 * 
 */
public class IPv4Util {

	private final static int INADDRSZ = 4;

	/**
	 * 把IP地址转化为字节数组
	 * 
	 * @param ipAddr
	 * @return byte[]
	 */
	public static byte[] ipToBytesByInet(String ipAddr) {
		try {
			return InetAddress.getByName(ipAddr).getAddress();
		} catch (Exception e) {
			throw new IllegalArgumentException(ipAddr + " is invalid IP");
		}
	}

	public static long ipToLong(String ipaddress) {  
        long[] ip = new long[4];  
        //先找到IP地址字符串中.的位置  
        int position1 = ipaddress.indexOf(".");  
        int position2 = ipaddress.indexOf(".", position1 + 1);  
        int position3 = ipaddress.indexOf(".", position2 + 1);  
        //将每个.之间的字符串转换成整型  
         ip[0] = Long.parseLong(ipaddress.substring(0, position1));  
         ip[1] = Long.parseLong(ipaddress.substring(position1+1, position2));  
         ip[2] = Long.parseLong(ipaddress.substring(position2+1, position3));  
         ip[3] = Long.parseLong(ipaddress.substring(position3+1));  
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];  
     }  

	
	public static boolean ipValid(String strIp)
	{
		if(null == strIp || strIp.isEmpty() || "null".equalsIgnoreCase(strIp))
		{
			return false;
		}
	    String regex="\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b";
	    Pattern p=Pattern.compile(regex);
	    Matcher m=p.matcher(strIp);

		return m.matches();
	 }
	
	/**
	 * 把IP地址转化为int
	 * 
	 * @param ipAddr
	 * @return int
	 */
	public static byte[] ipToBytesByReg(String ipAddr) {
		byte[] ret = new byte[4];
		try {
			String[] ipArr = ipAddr.split("\\.");
			ret[0] = (byte) (Integer.parseInt(ipArr[0]) & 0xFF);
			ret[1] = (byte) (Integer.parseInt(ipArr[1]) & 0xFF);
			ret[2] = (byte) (Integer.parseInt(ipArr[2]) & 0xFF);
			ret[3] = (byte) (Integer.parseInt(ipArr[3]) & 0xFF);
			return ret;
		} catch (Exception e) {
			throw new IllegalArgumentException(ipAddr + " is invalid IP");
		}

	}

	/**
	 * 字节数组转化为IP
	 * 
	 * @param bytes
	 * @return int
	 */
	public static String bytesToIp(byte[] bytes) {
		return new StringBuffer().append(bytes[0] & 0xFF).append('.')
				.append(bytes[1] & 0xFF).append('.').append(bytes[2] & 0xFF)
				.append('.').append(bytes[3] & 0xFF).toString();
	}

	/**
	 * 根据位运算把 byte[] -> int
	 * 
	 * @param bytes
	 * @return int
	 */
	public static int bytesToInt(byte[] bytes) {
		int addr = bytes[3] & 0xFF;
		addr |= ((bytes[2] << 8) & 0xFF00);
		addr |= ((bytes[1] << 16) & 0xFF0000);
		addr |= ((bytes[0] << 24) & 0xFF000000);
		return addr;
	}

	public static long bytesToLong(byte[] bytes)
	{
		long addr = bytes[3] & 0xFF;
		addr |= ((bytes[2] << 8) & 0xFF00);
		addr |= ((bytes[1] << 16) & 0xFF0000);
		addr |= ((bytes[0] << 24) & 0xFF000000);
		return addr;
	}
	
	/**
	 * 把IP地址转化为int
	 * 
	 * @param ipAddr
	 * @return int
	 */
	public static int ipToInt(String ipAddr) {
		try {
			return bytesToInt(ipToBytesByInet(ipAddr));
		} catch (Exception e) {
			throw new IllegalArgumentException(ipAddr + " is invalid IP");
		}
	}

	public static String longToIp(long ipaddress) {  
        StringBuffer sb = new StringBuffer("");  
        //直接右移24位  
        sb.append(String.valueOf((ipaddress >>> 24)));  
        sb.append(".");  
        //将高8位置0，然后右移16位  
        sb.append(String.valueOf((ipaddress & 0x00FFFFFF) >>> 16));  
        sb.append(".");  
        //将高16位置0，然后右移8位  
        sb.append(String.valueOf((ipaddress & 0x0000FFFF) >>> 8));  
        sb.append(".");  
        //将高24位置0  
        sb.append(String.valueOf((ipaddress & 0x000000FF)));  
        return sb.toString();  
    }

	
	/**
	 * ipInt -> byte[]
	 * 
	 * @param ipInt
	 * @return byte[]
	 */
	public static byte[] intToBytes(int ipInt) {
		byte[] ipAddr = new byte[INADDRSZ];
		ipAddr[0] = (byte) ((ipInt >>> 24) & 0xFF);
		ipAddr[1] = (byte) ((ipInt >>> 16) & 0xFF);
		ipAddr[2] = (byte) ((ipInt >>> 8) & 0xFF);
		ipAddr[3] = (byte) (ipInt & 0xFF);
		return ipAddr;
	}

	/**
	 * 把int->ip地址
	 * 
	 * @param ipInt
	 * @return String
	 */
	public static String intToIp(int ipInt) {
		return new StringBuilder().append(((ipInt >> 24) & 0xff)).append('.')
				.append((ipInt >> 16) & 0xff).append('.')
				.append((ipInt >> 8) & 0xff).append('.').append((ipInt & 0xff))
				.toString();
	}
	
	/**
	 * 把192.168.1.1/24 转化为int数组范围
	 * 
	 * @param ipAndMask
	 * @return int[]
	 */
	public static int[] getIPIntScope(String ipAndMask) {

		String[] ipArr = ipAndMask.split("/");
		if (ipArr.length != 2) {
			throw new IllegalArgumentException("invalid ipAndMask with: "
					+ ipAndMask);
		}
		int netMask = Integer.valueOf(ipArr[1].trim());
		if (netMask < 0 || netMask > 31) {
			throw new IllegalArgumentException("invalid ipAndMask with: "
					+ ipAndMask);
		}
		int ipInt = IPv4Util.ipToInt(ipArr[0]);
		int netIP = ipInt & (0xFFFFFFFF << (32 - netMask));
		int hostScope = (0xFFFFFFFF >>> netMask);
		return new int[] { netIP, netIP + hostScope };

	}
	
	/**
	 * 把192.168.1.1/24 转化为int数组范围
	 * 
	 * @param ipAndMask
	 * @return int[]
	 */
	public static long[] getIPLongScope(String ipAndMask) {

		String[] ipArr = ipAndMask.split("/");
		if (ipArr.length > 2) {
			return null;
		}
		else if(ipArr.length == 1 && ipValid(ipArr[0]))
		{
			return new long[] {IPv4Util.ipToLong(ipArr[0]), IPv4Util.ipToLong(ipArr[0])};
		}
		else if(ipArr.length == 2 && ipValid(ipArr[0]) && ipArr[1].matches("[0-9]+"))
		{		
			long netMask = Long.valueOf(ipArr[1].trim());
			if (netMask < 0 || netMask > 31) {
				return null;
			}
			long ipLong = IPv4Util.ipToLong(ipArr[0]);
			long netIP = ipLong & (0xFFFFFFFF << (64 - netMask));
			long hostScope = (0xFFFFFFFF >>> netMask);
			return new long[] { netIP, netIP + hostScope };
		}
		else 
			return null;

	}
	

	/**
	 * 把192.168.1.1/24 转化为IP数组范围
	 * 
	 * @param ipAndMask
	 * @return String[]
	 */
	public static String[] getIPAddrScope(String ipAndMask) {
		int[] ipIntArr = IPv4Util.getIPIntScope(ipAndMask);
		return new String[] { IPv4Util.intToIp(ipIntArr[0]),
				IPv4Util.intToIp(ipIntArr[0]) };
	}

	/**
	 * 根据IP 子网掩码（192.168.1.1 255.255.255.0）转化为IP段
	 * 
	 * @param ipAddr
	 *            ipAddr
	 * @param mask
	 *            mask
	 * @return int[]
	 */
	public static int[] getIPIntScope(String ipAddr, String mask) {

		int ipInt;
		int netMaskInt = 0, ipcount = 0;
		try {
			ipInt = IPv4Util.ipToInt(ipAddr);
			if (null == mask || "".equals(mask)) {
				return new int[] { ipInt, ipInt };
			}
			netMaskInt = IPv4Util.ipToInt(mask);
			ipcount = IPv4Util.ipToInt("255.255.255.255") - netMaskInt;
			int netIP = ipInt & netMaskInt;
			int hostScope = netIP + ipcount;
			return new int[] { netIP, hostScope };
		} catch (Exception e) {
			throw new IllegalArgumentException("invalid ip scope express  ip:"
					+ ipAddr + "  mask:" + mask);
		}

	}

	/**
	 * 根据IP 子网掩码（192.168.1.1 255.255.255.0）转化为IP段
	 * 
	 * @param ipAddr
	 *            ipAddr
	 * @param mask
	 *            mask
	 * @return String[]
	 */
	public static String[] getIPStrScope(String ipAddr, String mask) {
		int[] ipIntArr = IPv4Util.getIPIntScope(ipAddr, mask);
		return new String[] { IPv4Util.intToIp(ipIntArr[0]),
				IPv4Util.intToIp(ipIntArr[0]) };
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String ipAddr = "192.168.8.1";

		/*byte[] bytearr = IPv4Util.ipToBytesByInet(ipAddr);

		StringBuffer byteStr = new StringBuffer();

		for (byte b : bytearr) {
			if (byteStr.length() == 0) {
				byteStr.append(b);
			} else {
				byteStr.append("," + b);
			}
		}
		System.out.println("IP: " + ipAddr + " ByInet --> byte[]: [ " + byteStr
				+ " ]");

		bytearr = IPv4Util.ipToBytesByReg(ipAddr);
		byteStr = new StringBuffer();

		for (byte b : bytearr) {
			if (byteStr.length() == 0) {
				byteStr.append(b);
			} else {
				byteStr.append("," + b);
			}
		}
		System.out.println("IP: " + ipAddr + " ByReg  --> byte[]: [ " + byteStr
				+ " ]");

		System.out.println("byte[]: " + byteStr + " --> IP: "
				+ IPv4Util.bytesToIp(bytearr));

		int ipInt = IPv4Util.ipToInt(ipAddr);

		System.out.println("IP: " + ipAddr + "  --> int: " + ipInt);

		System.out.println("int: " + ipInt + " --> IP: "
				+ IPv4Util.intToIp(ipInt));

		String ipAndMask = "192.168.1.1/24";

		int[] ipscope = IPv4Util.getIPIntScope(ipAndMask);
		System.out.println(ipAndMask + " --> int地址段：[ " + ipscope[0] + ","
				+ ipscope[1] + " ]");

		System.out.println(ipAndMask + " --> IP 地址段：[ "
				+ IPv4Util.intToIp(ipscope[0]) + ","
				+ IPv4Util.intToIp(ipscope[1]) + " ]");

		String ipAddr1 = "192.168.1.1", ipMask1 = "255.255.255.0";

		int[] ipscope1 = IPv4Util.getIPIntScope(ipAddr1, ipMask1);
		System.out.println(ipAddr1 + " , " + ipMask1 + "  --> int地址段 ：[ "
				+ ipscope1[0] + "," + ipscope1[1] + " ]");

		System.out.println(ipAddr1 + " , " + ipMask1 + "  --> IP地址段 ：[ "
				+ IPv4Util.intToIp(ipscope1[0]) + ","
				+ IPv4Util.intToIp(ipscope1[1]) + " ]");*/
		
		
		System.out.println(IPv4Util.ipValid("192.0.555.0"));
		System.out.println(IPv4Util.longToIp(16843009));
		System.out.println(IPv4Util.ipToLong("4.196.180.4"));
		if(null == getIPLongScope("172.16.3.21/33"))
		{
			System.out.println("invalid ip addresss.");
		}
		else
		{
			System.out.println("valid ip addresss.");
		}

	}

}
