package fi.tkk.media.xide.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import fi.tkk.media.xide.client.Server.RPC.XIDEException;
import fi.tkk.media.xide.client.UI.Widget.BasicPageElement;
import fi.tkk.media.xide.client.UI.Widget.Slot;

/**
 * Creates file structure for the new object.
 * 
 * @author evgeniasamochadina
 * 
 */
public class ServerFileStructureCreator {
	
	public static String INIT_FILE_NAME = "init_page.xml";
	public static String INIT_FILE_WITH_CONT_NAME = "init_page_with_cont.xml";
	public static String INIT_REPLACE_STRING = "123fhugfd2D";
	
	public static String INIT_MAIN_PAGE_DECLARATION = "main_page_name_and_extention_09_07_09";

	public static void createPageXMLFile(String path, String pageName,  boolean addDefaultContainer)  throws XIDEException{
		try {
			String init_file = null;
			if (!addDefaultContainer) {	
				init_file = Config.getSystemFilesPath() + INIT_FILE_NAME;
			}
			else {
				init_file = Config.getSystemFilesPath() + INIT_FILE_WITH_CONT_NAME;
			}
			String new_file = path + pageName + ".xml";

			String text = SearchServiceImpl.readFileAsString(init_file);

			SearchServiceImpl.writeStringToFile(new_file, text.replace(
					INIT_REPLACE_STRING, pageName));

		}

		catch (FileNotFoundException ex) {
//			System.out.println("Server File Structure: ");
			System.out
					.println(ex.getMessage() + " in the specified directory.");
			throw new XIDEException(ex.getMessage());
		} catch (IOException e) {
//			System.out.println("Server File Structure: " + e.getMessage());
			throw new XIDEException(e.getMessage());
		}
	}

	public ServerFileStructureCreator() {

	}

	public static void createPageStructure(String name,
			String applicationAbsolutPath, boolean addDefaultXML, boolean addDefaultContainer) throws XIDEException{
		File mainFolder = new File(applicationAbsolutPath);
		try {
			boolean success = mainFolder.mkdir();

			for (int i = 0; i < ServerFileStructureParser.DIRECTORIES.length; i++) {
				File firstLevelFolder = new File(mainFolder,
						ServerFileStructureParser.DIRECTORIES[i] + "/");
				firstLevelFolder.mkdir();
				if (success) {
					// File did not exist and was created
				} else {
					// File already exists
				}
				File secondLevelFolder = new File(firstLevelFolder, name + "/");
				secondLevelFolder.mkdir();
			}

			if (addDefaultXML) {
				createPageXMLFile(applicationAbsolutPath, name, addDefaultContainer);
			} else {
				File mainXMLFile = new File(mainFolder, name + ".xml");
				mainXMLFile.createNewFile();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new XIDEException(e.getMessage());
		}

	}

	public static void configureWebXML(String applicationPath, String mainPageName) {


		try {
			String text;
			String webXMLPath = applicationPath + "WEB-INF/" + "web.xml";
			
			text = SearchServiceImpl.readFileAsString(webXMLPath);

			String newString = "	<welcome-file-list>" + 
				"<welcome-file>" + mainPageName + ".xformsdb" + "</welcome-file> \n" + 
				"</welcome-file-list>";
			int start = text.indexOf("<welcome-file-list");
			int end = text.indexOf("</welcome-file-list>");
			end += 20;
			String newCode = text.substring(0, start) + newString + text.substring(end, text.length());
				SearchServiceImpl.writeStringToFile(webXMLPath, newCode);
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * Configures the WEB-INF/conf.xml file, so that an application can use exist-db.
	 * 
	 * @param applicationPath the application path
	 * @param appName the app name
	 */
	public static void configureConfXML(String applicationPath, String appName) {


		try {
			String text;
			String webXMLPath = applicationPath + "WEB-INF/" + "conf.xml";
			
			text = SearchServiceImpl.readFileAsString(webXMLPath);

			String newString = "\n	<data-source id=\"exist-data-data-source\">\n" + 
				"<type>2</type>\n" +
				"<uri>xmldb:exist://testbed.tml.hut.fi/exist/xmlrpc</uri> \n" + 
				"<username>flexi</username>\n" +
				"<password>4uuYmWKm</password>\n" +
				"<collection>/db/xformsdb/multinews/data</collection>\n" +
				"</data-source>\n" +
				"<data-source id=\"exist-realm-data-source\">\n" +
				"<type>2</type>\n" +
				"<uri>xmldb:exist://testbed.tml.hut.fi/exist/xmlrpc</uri>\n" +
				"<username>flexi</username>\n" + 
				"<password>4uuYmWKm</password>\n" +
				"<collection>/db/xformsdb/multinews/realm</collection>\n" +
				"</data-source>\n";
			
			int start = text.indexOf("<!-- Database configuration START -->");
			int end = text.indexOf("<!-- Database configuration END -->");
			String newCode = text.substring(0, start) + "<!-- Database configuration START -->\n" +  newString + 
							text.substring(end, text.length());
				SearchServiceImpl.writeStringToFile(webXMLPath, newCode);
			} catch (IOException e) {

			e.printStackTrace();
		}

	}
	
	/**
	 * Creates application based on its name and name of the user who creates it
	 * 
	 * @param name
	 * @param user
	 *            user name. Now its fake parameter - user is always markku
	 */
	public static void createApplicationStructure(String name, String user) throws XIDEException{
//		user = "markku";

		// XIDE_file_system + username/
		File usersFolder = new File(Config.getAppsSourcePath() 
				+ user + "/");
		usersFolder.mkdir();

		// XIDE_file_system + username/ + appname/
		File mainFolder = new File(Config.getAppsSourcePath()
				+ user + "/" + name
				+ "/");
		mainFolder.mkdir();

		for (int i = 0; i < ServerFileStructureParser.DIRECTORIES.length; i++) {
			File firstLevelFolder = new File(mainFolder,
					ServerFileStructureParser.DIRECTORIES[i] + "/");
			firstLevelFolder.mkdir();
			// File secondLevelFolder = new File(mainFolder,
			// ServerFileStructureParser.DIRECTORIES[i] + "/");
		}
		
		// Copy necessary folders from system folders
		// TODO: remove 55MB !
		File webInfPath = new File(Config.getSystemFilesPath() + "WEB-INF/");
		File metaInfPath = new File(Config.getSystemFilesPath() + "META-INF/");

		File webInfPathDest = new File(mainFolder, "WEB-INF/");
		File metaInfPathDest = new File(mainFolder, "META-INF/");

		try {
			copyFiles(webInfPath, webInfPathDest);
			copyFiles(metaInfPath, metaInfPathDest);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new XIDEException(e.getMessage());
		}
	}

	public static void deleteApplicationStructure(String name, String user)  throws XIDEException{
		if (user != null && name != null) {
			File usersFolder = new File(Config.getAppsSourcePath()
					+ user + "/"
					+ name + "/");
			deleteFile(usersFolder);
		}
	}

	/**
	 * This function will recursively delete directories and files.
	 * 
	 * @param path
	 *            File or Directory to be deleted
	 * @return true indicates success.
	 */
	public static void deleteFile(File path) throws XIDEException{
		try {
			if (path.exists()) {
				if (path.isDirectory()) {
					File[] files = path.listFiles();
					for (int i = 0; i < files.length; i++) {
						if (files[i].isDirectory()) {
							deleteFile(files[i]);
						} else {
							files[i].delete();
						}
					}
				}
				path.delete();
			}
			else {
				throw new XIDEException("File "+ path.getName() +" cannot be deleted, because it doesn't exist");
				}
			}
		catch (Exception e){
			throw new XIDEException(e.getMessage());
		}
	}

	/**
	 * This function will copy files or directories from one location to
	 * another. note that the source and the destination must be mutually
	 * exclusive. This function can not be used to copy a directory to a sub
	 * directory of itself. The function will also have problems if the
	 * destination files already exist.
	 * 
	 * @param src A File object that represents the source for the copy
	 * @param dest A File object that represnts the destination for the copy.
	 * @throws IOException
	 *             if unable to copy.
	 */
	public static void copyFiles(File src, File dest) throws IOException {
		// Check to ensure that the source is valid...
		if (!src.exists()) {
			throw new IOException("copyFiles: Can not find source: "
					+ src.getAbsolutePath() + ".");
		} else if (!src.canRead()) { // check to ensure we have rights to the
										// source...
			throw new IOException("copyFiles: No right to source: "
					+ src.getAbsolutePath() + ".");
		}
		// is this a directory copy?
		if (src.isDirectory()) {
			if (!dest.exists()) { // does the destination already exist?
				// if not we need to make it exist if possible (note this is
				// mkdirs not mkdir)
				if (!dest.mkdirs()) {
					throw new IOException(
							"copyFiles: Could not create direcotry: "
									+ dest.getAbsolutePath() + ".");
				}
			}
			// get a listing of files...
			String list[] = src.list();
			// copy all the files in the list.
			for (int i = 0; i < list.length; i++) {
				File dest1 = new File(dest, list[i]);
				File src1 = new File(src, list[i]);
				copyFiles(src1, dest1);
			}
		} else {
			// This was not a directory, so lets just copy the file
			FileInputStream fin = null;
			FileOutputStream fout = null;
			byte[] buffer = new byte[4096]; // Buffer 4K at a time (you can
											// change this).
			int bytesRead;
			try {
				// open the files for input and output
				fin = new FileInputStream(src);
				fout = new FileOutputStream(dest);
				// while bytesRead indicates a successful read, lets write...
				while ((bytesRead = fin.read(buffer)) >= 0) {
					fout.write(buffer, 0, bytesRead);
				}
			} catch (IOException e) { // Error copying file...
				IOException wrapper = new IOException(
						"copyFiles: Unable to copy file: "
								+ src.getAbsolutePath() + "to"
								+ dest.getAbsolutePath() + ".");
				wrapper.initCause(e);
				wrapper.setStackTrace(e.getStackTrace());
				throw wrapper;
			} finally { // Ensure that the files are closed (if they were open).
				if (fin != null) {
					fin.close();
				}
				if (fout != null) {
					fout.close();
				}
			}
		}
	}
}
