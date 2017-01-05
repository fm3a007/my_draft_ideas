/**
 * \file
 * @see Logger .
 *  
 * 
 * 
 * 
 * 
 * @author Liang,David
 *  
 * These source files are released under the GPLv3 license.
 *
 */


package my.frmwk.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 对 @see Logger的实现之一,方便大家调试.
 * 
 * @author David
 * 
 */
public class LoggerOutputForDebug extends Logger {

	protected int logId;

	public LoggerOutputForDebug() {
		logId = 0;
	}



	@Override
	public int update_log(int log_id, int status) {
		Date now = new Date();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		String msg = log_id +":\t" + fmt.format(now)+" result: "+ status + "\n";
		try {
				System.out.print(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}


	@Override
	public int log( int level, int MOD_COD, int uid, String msg, int status) {
		int log_id = 0;
		synchronized (this) {
			log_id = ++logId;
		}

		Date now = new Date();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		String lmsg = log_id + ":\t" + fmt.format(now) + " log level " + level + ", user <id:" + uid
				+ ">, module " + MOD_COD + ": " + msg
				+ "\n";
		try {
			System.out.print(lmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (STAT_UNKNOWN != status) {
			update_log(log_id, status);
			log_id = 0;
		}
		return log_id;
	}

}
