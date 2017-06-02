/**
 * \file
 *
 * 
 *
 * @author David
 *
 * @version 1.0
 *
 * These source files are released under the GPLv3 license.
 */


package my.frmwk.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 将日志记录到文件中,对 @see Logger的实现.
 * 
 * @author David
 * 
 */
public class LoggerFileImpl extends Logger {
	
	protected	int	logLevel = 2147483647;

	protected	String path;
	
	protected	boolean	externalOutput = false;
	
	protected	String name;
	
	protected	String ext;

	protected	OutputStream fs;

	protected	int logId;
	
	protected	int	dateNum;
	
	protected	SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S"); 

	/**
	 * @param logLevel 日志级别, 记日志时与这个值按位与,结果非0时才记.
	 * @param name 日志文件名
	 * @param path 日志输出路径
	 * @param ext 后缀名(缺省:log)
	 */
	public LoggerFileImpl(int logLevel, String name, String path, String ext) {
		this.logLevel = logLevel;
		this.path = null!=path? path : "";
		this.name = null!=name? name: this.getClass().getSimpleName();
		this.ext = null!=ext ? ext : "log";
		
		dateNum = -1;
		
		logId = 0;
	}
	
	public LoggerFileImpl(int logLevel, OutputStream os) {
		externalOutput = true;
		this.logLevel = logLevel;
		fs = os;
	}

	@SuppressWarnings("deprecation")
	protected boolean open( Date now) {
		if(externalOutput){
			return	true;
		}
		int	curDate = null==now ? dateNum : now.getDate();
		if (null == fs || curDate!=dateNum ) {
			OutputStream fs0 = null;
			try {
				synchronized (path) {
					SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd"); 
					Date	d = new Date();
					String fullPath = path + name + "." + fmt.format(d)+ "." + ext;
					dateNum = d.getDate();

					fs0 = fs;
					fs = new FileOutputStream(fullPath, true);
				}
			} catch (FileNotFoundException e) {
				fs0 = null;
				e.printStackTrace();
			}
			if(null!=fs0){
				synchronized (fs0) {
					try {
						fs0.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return (null == fs ? false : true);
	}

	public void close() {
		if ( externalOutput || null == fs) {
			return;
		}
		try {
			fs.close();
			fs = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int update_log(int log_id, int status) {
		if(log_id<=0){
			return	status;
		}
		if (open(null)) {
			Date now = new Date();
			String msg = log_id +":\t" + fmt.format(now)+" result: "+ status + "\n";
			try {
				synchronized (fs) {
					fs.write(msg.getBytes());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return status;
	}

	@Override
	protected void finalize() throws Throwable {
		this.path = null;
		if (null != fs) {
			this.close();
		}
		super.finalize();
	}


	@Override
	public int log( int level, int MOD_COD, int uid, String usr, String msg, int status) {
		int log_id = 0;
		if( 0==(logLevel & level)){
			return	0;
		}
		Date now = new Date();
		if (open(now)) {
			synchronized (fs) {
				log_id = ++logId;
				if(logId>2000000000){
					logId = 0;
				}
			}

			StringBuffer lmsg = new StringBuffer();
			lmsg.append(log_id).append(":\t").append(fmt.format(now)).append(
					" log level ").append(level).append(", user <id:").append(uid).append(
					">").append(usr).append(", module ").append(MOD_COD).append(
					": ").append(msg).append("\n");
			try {
				synchronized (fs) {
					fs.write(lmsg.toString().getBytes());
				}
			}catch (IOException e) {
				e.printStackTrace();
			}
			if (STAT_UNKNOWN != status) {
				update_log(log_id, status);
				log_id = 0;
			}
		}
		return log_id;
	}
}
