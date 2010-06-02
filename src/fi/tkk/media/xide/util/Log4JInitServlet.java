package fi.tkk.media.xide.util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Log4JInitServlet
 * 
 * This class should get loaded first (based on the web.xml), so it can init the
 * logger.
 * 
 * @author Nick
 */
public class Log4JInitServlet extends HttpServlet {
	static final Logger logger = Logger.getLogger(Log4JInitServlet.class);
	

	public void init() throws ServletException {
		System.out.println("Log4JInitServlet init() starting.");
		String log4jfile = getInitParameter("log4j-properties");
		System.out.println("log4jfile: " + log4jfile);
		if (log4jfile != null) {
			String propertiesFilename = getServletContext().getRealPath(
					log4jfile);
			PropertyConfigurator.configure(propertiesFilename);
			logger.info("logger configured.");
		} else {
			System.out.println("Error setting up logger.");
		}
	}
}