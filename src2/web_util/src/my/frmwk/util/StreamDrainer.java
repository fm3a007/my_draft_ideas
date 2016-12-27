/**
 * \file
 * @see StreamDrainer.
 * 
 * copyright (C) 2000, 2011, xxxx, co.,ltd
 * 
 * @author zjw
 *  
 * @version 1.0 
 * 
 */



package my.frmwk.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 读取标准输出和标准错误的内容.
 * 
 * @author jiaweiz
 *
 */
public class StreamDrainer implements Runnable  {
	private InputStream ins;

	public StreamDrainer(InputStream ins) {
		this.ins = ins;
	}

	public void run() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					ins));
			String line = null;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
