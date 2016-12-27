/**
 * 
 * 
 * 
 */
package my.frmwk.util;



import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * @author administrator
 * 
 */
public class FilePrinterAction {

	private HttpServletRequest request;
	private HttpServletResponse response;
	private String basePath;
	private Logger logger;

	public FilePrinterAction(ServletRequest req, ServletResponse resp,
			ServletContext servCon, String baseDir, HttpSession session) {
		request = (HttpServletRequest) req;
		response = (HttpServletResponse) resp;
		basePath = baseDir;
		if ('/' != basePath.charAt(basePath.length() - 1)) {
			basePath += '/';
		}
	}

	public int process() {
		int iRet = 0;
		int log_id = 0;
		String file = request.getParameter("f");

		if (null == file || file.isEmpty()) {
			return 0;
		}

		file = basePath + file;
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			OutputStream os = response.getOutputStream();
			byte[] buf = new byte[4096];
			int len = -1;
			while ((len = in.read(buf)) != -1) {
				os.write(buf, 0, len);
			}
			logger.update_log(log_id, 0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return iRet;
	}


}
