package fi.tkk.media.xide.server.transformation.filehandlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.media.xide.client.Data.APElementApplication;
import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.Server.RPC.PublishException;
import fi.tkk.media.xide.server.SearchServiceImpl;

// TODO: Auto-generated Javadoc
/**
 * The Class ApplicationToWidgetHandler.
 */
public class WidgetMaker {

	/** The user name. */
	private final String userName;

	/** The application name. */
	private final String applicationName;

	/** The pages. */
	private final ArrayList<String> pages;

	private File applicationFolder;

	private File zipFile;

	private StringBuilder xmlString = new StringBuilder();

	private boolean success = true;

	private HashMap<String, Property> properties;

	private static  Logger loggerInstance = Logger.getLogger(WidgetMaker.class);

	/**
	 * Instantiates a new widget maker which packages an application to a zip
	 * file.
	 * 
	 * @param application
	 *            to be packaged
	 * 
	 * @throws IOException
	 * 
	 */
	public WidgetMaker(APElementApplication application) {

		this.properties = application.properties;
		this.userName = application.properties.get(Property.AUTHOR).getStringValue();
		this.applicationName = application.properties.get(Property.RELATED_URL).getStringValue();
		this.pages = SearchServiceImpl.getPageRelatedNames(application);

		startPackaging();
	}

	private void startPackaging() {

		try {

			// Publish the application to a temporary location
			this.applicationFolder = publishToTempLocation();

			// Build the xml string
			addXMLDeclarations();
			addName();
			addDescription();
			addAuthor();
			addLicense();
			addIcon();
			addContent();
			addRoles();
			addXFormsDBData();
			addEndElement();
			writeContentToFile();

			updateConfFile();

			makeZip();

		} catch (PublishException e) {

			e.printStackTrace();
			loggerInstance.log(Level.ERROR, "Could not package the application. " + e.getMessage());
			this.success = false;

		} catch (FileNotFoundException e) {

			e.printStackTrace();
			loggerInstance.log(Level.ERROR, "Could not package the application. " + e.getMessage());
			this.success = false;

		} catch (IOException e) {

			e.printStackTrace();
			loggerInstance.log(Level.ERROR, "Could not package the application. " + e.getMessage());
			this.success = false;

		} finally {
			PublishHandler.deleteDir(this.applicationFolder);
		}
	}

	/**
	 * Publishes the application to a temporary location.
	 * 
	 * @return the application folder that was transformed
	 * 
	 * @throws PublishException
	 *             the publish exception
	 */
	private File publishToTempLocation() throws PublishException {

		PublishHandler ph = new PublishHandler(this.userName, this.applicationName, this.pages,
				PublishHandler.PUBLISH_TO_TEMP, false);
		String applicationPath = ph.getPublishedAppPath();
		return new File(applicationPath);
	}

	private void addXMLDeclarations() {

		loggerInstance.log(Level.DEBUG, "Adding XML declarations");

		xmlString.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		xmlString.append("<widget xmlns=\"http://www.w3.org/ns/widgets\" "
				+ "xmlns:xformsdb=\"http://www.tml.tkk.fi/2007/xformsdb\" "
				+ "id=\"http://media.tkk.fi/widgets/xweather\" " + "version=\"2.0\" " + "height=\"250\" "
				+ "width=\"320\" " + "viewmodes=\"all\">\n");
	}

	private void addName() {

		loggerInstance.log(Level.DEBUG, "Adding <name>.");

		xmlString.append("\t<name short=\"" + this.applicationName + "\">" + this.applicationName + "</name>\n");
	}

	private void addDescription() {

		String description = this.properties.get(Property.DESCR).getStringValue();

		xmlString.append("\t<description>" + description + "</description>\n");
	}

	private void addAuthor() {

		loggerInstance.log(Level.DEBUG, "Adding <author>.");

		xmlString.append("\t<author href=\"\" email=\"\" xformsdb:username=\"\">" + this.userName + "</author>\n");
	}

	private void addLicense() {

		loggerInstance.log(Level.DEBUG, "Adding application <license>.");

		xmlString.append("\t<license>\n " + "\t\tThe MIT License\n " + "\t\tCopyright (c) 2009 XIDE\n "
				+ "\t\tPermission is hereby granted, free of charge, to any person obtaining a copy\n "
				+ "\t\tof this software and associated documentation files (the \"Software\"), to deal\n "
				+ "\t\tin the Software without restriction, including without limitation the rights\n "
				+ "\t\tto use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n "
				+ "\t\tcopies of the Software, and to permit persons to whom the Software is\n "
				+ "\t\tfurnished to do so, subject to the following conditions: \n\n"
				+ "\t\tThe above copyright notice and this permission notice shall be included in\n "
				+ "\t\tall copies or substantial portions of the Software. \n\n"
				+ "\t\tTHE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n "
				+ "\t\tIMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\n "
				+ "\t\tFITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\n "
				+ "\t\tAUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\n "
				+ "\t\tLIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\n "
				+ "\t\tOUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN\n "
				+ "\t\tTHE SOFTWARE.\n" + "\t</license>\n");
	}

	private void addIcon() {

		loggerInstance.log(Level.DEBUG, "Adding <icon>.");

		xmlString
				.append("\t<icon src=\"resource/index/icon_weather_48x48_updated.png\" width=\"48\" height=\"48\" />\n");
	}

	private void addContent() {

		loggerInstance.log(Level.DEBUG, "Adding <content>");

		xmlString.append("\t<content src=\"index.xformsdb\" type=\"text/html\" encoding=\"UTF-8\" />\n");
	}

	private void addRoles() {

		loggerInstance.log(Level.DEBUG, "Adding <role>");

		xmlString
				.append("\t<xformsdb:role description=\"Administrator of the widget.\" default=\"true\">admin</xformsdb:role>\n");
	}

	private void addXFormsDBData() {

		loggerInstance.log(Level.DEBUG, "Adding <xformsdb:widgetdata>");

		String relativeDBPath = "WEB-INF/db/xide/" + this.applicationName;
		xmlString.append("\t<xformsdb:widgetdata datasrc=\"user\">" + relativeDBPath + "</xformsdb:widgetdata>\n");
		// xmlString.append("\t<xformsdb:data datasrc=\"default-data-source\">"
		// + relativeDBPath + "</xformsdb:data>\n");

	}

	private void addEndElement() {
		xmlString.append("</widget>");
	}

	private void updateConfFile() throws IOException {

		File confFile = new File(this.applicationFolder, "WEB-INF/conf.xml");

		String confContent = SearchServiceImpl.readFileAsString(confFile.getAbsolutePath());
		int start = confContent.indexOf("<data-source");
		int end = confContent.lastIndexOf("</data-source>");

		// UPDATE ONLY IF WE ARE USING EXIST-DB
		if (start != -1 && end != -1) {

			// // Build the content
			// String dataSourceContent =
			// "<data-source id=\"default-data-source\">\n";
			// dataSourceContent += "<type>2</type>\n";
			// dataSourceContent +=
			// "<uri>xmldb:exist://localhost:8080/exist/xmlrpc</uri>\n";
			// dataSourceContent += "<username>uspace</username>\n";
			// dataSourceContent += "<password>uspace1234</password>\n";
			// dataSourceContent += "<collection>/db/uspace/widgets/" +
			// this.applicationName + "</collection>\n";
			// dataSourceContent += "</data-source>";
			//
			// // Insert the content
			// end += 14;
			// String newContent = confContent.substring(0, start) +
			// dataSourceContent
			// + confContent.substring(end, confContent.length());
			// SearchServiceImpl.writeStringToFile(confFile.getAbsolutePath(),
			// newContent);
		}
	}

	private void writeContentToFile() throws PublishException {

		loggerInstance.log(Level.DEBUG, "Writing the XML string to a file.");

		String filePath = this.applicationFolder.getAbsolutePath() + "/config.xml";
		try {
			SearchServiceImpl.writeStringToFile(filePath, this.xmlString.toString());

		} catch (IOException e) {

			throw new PublishException("Could not write data to config.xml." + e.getMessage());
		}
	}

	private void makeZip() throws IOException {

		loggerInstance.log(Level.DEBUG, "Making a ZIP file.");

		String zipPath = this.applicationFolder.getParent() + "/" + this.applicationName + ".zip";
		this.zipFile = new File(zipPath);
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipPath));
		addDir(this.applicationFolder, out);
		out.close();
	}

	private void addDir(File sourceFolder, ZipOutputStream out) throws IOException {
		String[] files = sourceFolder.list();
		byte[] tmpBuf = new byte[1024];

		for (int i = 0; i < files.length; i++) {

			File f = new File(sourceFolder, files[i]);

			if (f.getAbsolutePath().contains(".svn")) {
				continue;
			}

			String absolutePathToFile = f.getAbsolutePath();
			String absolutePathToApplication = this.applicationFolder.getAbsolutePath();
			String entryName = absolutePathToFile.replace(absolutePathToApplication, "");

			if (entryName.startsWith("\\") || entryName.startsWith("/")) {
				entryName = entryName.substring(1, entryName.length());
			}

			// NEEDED FOR WINDOWS
			entryName = entryName.replace("\\", "/");

			if (f.isDirectory()) {

				ZipEntry zipEntry = new ZipEntry(entryName + "/");
				out.putNextEntry(zipEntry);
				out.closeEntry();
				addDir(f, out);
				continue;

			}

			FileInputStream in = new FileInputStream(absolutePathToFile);
			ZipEntry zipEntry = new ZipEntry(entryName);
			out.putNextEntry(zipEntry);

			int len;
			while ((len = in.read(tmpBuf)) > 0) {
				out.write(tmpBuf, 0, len);
			}
			out.closeEntry();
			in.close();

		}
	}

	public File getZipFile() {
		return this.zipFile;
	}

	public boolean success() {
		return this.success;
	}
}
