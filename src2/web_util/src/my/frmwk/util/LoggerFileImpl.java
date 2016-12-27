/**
 * \file
 *
 * copyright (C) 2000, 2013, xxxx, co.,ltd
 *
 * @author HCW,CCB
 *
 * @version 1.0
 *
 */


package my.frmwk.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * 将日志记录到文件中,对 @see Logger的实现.
 * 
 * @author administrator
 * 
 */
public class LoggerFileImpl extends Logger {

	protected String file;

	protected FileOutputStream fs;

	protected int logId;

	public LoggerFileImpl(String path) {
		file = path;
		logId = 0;
	}

	protected boolean open() {
		if (null == fs) {
			try {
				synchronized (file) {
					if (null == fs) {
						fs = new FileOutputStream(file, true);
					}
				}
			} catch (FileNotFoundException e) {
				System.out.println("Can't find log file " + file);
			}
		}
		return (null == fs ? false : true);
	}

	public void close() {
		if (null != fs) {
			try {
				fs.close();
				fs = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public int update_log(int log_id, int status) {
		if (open()) {
			String msg = "\t" + log_id + " result:\t" + status + "\n";
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
		this.file = null;
		if (null != fs) {
			this.close();
		}
		super.finalize();
	}

	@Override
	public int log(int MOD_COD, int uid, String msg, int status, int type) {
		int log_id = 0;
		if (open()) {
			synchronized (file) {
				log_id = ++logId;
			}
			String lmsg = log_id + ":\t" + new Date() + " user <id:" + uid
					+ ">" + " access system module <" + MOD_COD + ">, " + msg
					+ "\n\tLog Type:" + type + "\n";
			try {
				synchronized (fs) {
					fs.write(lmsg.getBytes());
				}
			} catch (IOException e) {
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
