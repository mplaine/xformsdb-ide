package fi.tkk.media.xide.server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fi.tkk.media.xide.client.Data.APElementApplication;
import fi.tkk.media.xide.client.Server.RPC.PublishException;
import fi.tkk.media.xide.server.transformation.filehandlers.PublishHandler;
import fi.tkk.media.xide.server.transformation.filehandlers.WidgetMaker;

/**
 * 
 * The servlet produces the data, which should be sent to the user. Is used to
 * perform an export of the application and send a resulting zip file to the
 * user
 * 
 * @author evgeniasamochadina
 * 
 */
public class DownloadFileServlet extends HttpServlet {

	/**
	 * Constructs the output
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("context" + req.getQueryString());
		String appID = req.getQueryString();

		APElementApplication app = SearchServiceImpl.getInstance().getApplicationByID(appID);

		// Process export
		File zipFile = null;

		WidgetMaker widgetMaker = new WidgetMaker(app);
		zipFile = widgetMaker.getZipFile();

		if (widgetMaker.success()) {
			
			// Set the response type
			String fileName = zipFile.getName();
			resp.setContentType("application/zip");
			resp.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			
			//Write to response
			String filePath = zipFile.getAbsolutePath();
			writeFileIntoResponse(resp, filePath);
			
		} else {

			// Something went wrong, what to do ??
		}


	}

	private void writeFileIntoResponse(HttpServletResponse resp, String fileName) throws IOException {
		//		
		// PrintWriter out = resp.getWriter();
		// out.println("This is the output content");
		// out.println("Probably something dynamic should go in here");

		File file = new File(fileName);

		// Write the file content to the response body
		OutputStream out = resp.getOutputStream();

		FileInputStream fileStream = new FileInputStream(file);
		InputStream input = new BufferedInputStream(fileStream);
		byte[] buffer = new byte[1024 * 8];
		int bytesRead;

		bytesRead = input.read(buffer);
		while (bytesRead > 0) {
			out.write(buffer, 0, bytesRead);
			bytesRead = input.read(buffer);
		}

	}

}
