package fi.tkk.media.xide.server.transformation.filehandlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import fi.tkk.media.xide.client.Server.RPC.PublishException;
import fi.tkk.media.xide.server.Config;
import fi.tkk.media.xide.server.ExistManager;
import fi.tkk.media.xide.server.SearchServiceImpl;
import fi.tkk.media.xide.server.transformation.transformers.ComponentTransformer;
import fi.tkk.media.xide.server.transformation.transformers.XideToXFormsDBTransformer;
import fi.tkk.tml.xformsdb.xml.sax.XFormsDBHandler;

/**
 * This class copies all the necessary folders from the XIDE file system to the
 * production file system and deploy the application to Tomcat. It will also
 * generate the necessary component files and directories to the user's
 * production directory.
 * 
 * This class also calls the transformations.
 * 
 * @author Sebastian Monte
 * @version 1.0
 */

public class PublishHandler {

	public static final int PUBLISH_TO_PRODUCTION = 1;
	public static final int PUBLISH_TO_TEMP = 2;
	public static final int PUBLISH_TO_PREVIEW = 3;

	/** The application name to be published. */
	private final String applicationName;

	/** The user name that published the application. */
	private final String userName;

	/** Tag that calls the components from the template pages. */
	private final String componentTag = "template:call-component";

	/** The pages that are included in the application. */
	private final ArrayList<String> pages;

	/** The components root. */
	private final String componentsRoot = Config.getComponentsPath();

	/** The application source path. */
	private String appSourcePath = null;

	/** The application production path. */
	private String appPublishedStructurePath = null;

	private static Logger loggerInstance = Logger.getLogger(PublishHandler.class);

	/**
	 * Instantiates a new PublishHandler for a whole application
	 * 
	 * @param userName
	 *            the user name
	 * @param applicationName
	 *            the application name
	 * @param pages
	 *            the pages
	 * @param publish
	 *            the publish (true = application is published, false =
	 *            application is unpublished)
	 * 
	 * @throws PublishException
	 *             the publish exception
	 */
	public PublishHandler(String userName, String applicationName, ArrayList<String> pages, int type, boolean doReloadDB)
			throws PublishException {

		loggerInstance.log(Level.DEBUG, "Constuctor called.");

		this.applicationName = applicationName;
		this.userName = userName;
		this.pages = pages;

		try {
			if (type == PUBLISH_TO_PRODUCTION) {
				publishToProduction(doReloadDB);
				setLog4jProperties();
			}
			if (type == PUBLISH_TO_TEMP) {
				publishToTemporaryLocation();
			}
			if (type == PUBLISH_TO_PREVIEW) {
				publishToPreview();
			}
		} catch (Exception e) {
			throw new PublishException(e.getMessage());
		}
	}

	/**
	 * Sets all the necessary paths for publishing. It also creates a new folder
	 * for the user if it doesn't already exist in the XIDE production system.
	 * 
	 * Starts the publishing process.
	 * 
	 * @throws PublishException
	 *             the publish exception
	 */
	private void publishToProduction(boolean doReloadDB) throws PublishException {

		loggerInstance.log(Level.DEBUG, "Publishing \"" + this.applicationName + "\"");

		// Set the paths for publishing
		this.appSourcePath = Config.getAppsSourcePath() + this.userName + "/" + this.applicationName;
		this.appPublishedStructurePath = Config.getAppsPublishedPath() + this.userName + "/" + this.applicationName;

		copyDirectoriesToProduction();
		copyComponentData();

		if (Config.useExistDb() && doReloadDB) {
			initDatabaseForProduction();
		}

		deployToTomcatForProduction(); // Deploy to Tomcat

		loggerInstance.log(Level.DEBUG, "Transformations finished for \"" + this.applicationName + "\"");
	}

	/**
	 * Publishes a page page to a temporary location.
	 * 
	 * @throws PublishException
	 *             the publish exception
	 */
	private void publishToTemporaryLocation() throws PublishException {

		loggerInstance.log(Level.DEBUG, "Publishing \"" + this.applicationName + "\" to a temporary location");

		// Set the paths for publishing
		this.appSourcePath = Config.getAppsSourcePath() + this.userName + "/" + this.applicationName;
		this.appPublishedStructurePath = Config.getTempFilesPath() + this.userName + "/" + this.applicationName;

		copyDirectoriesToProduction();
		copyComponentData();

		loggerInstance.log(Level.DEBUG, "Transformations finished for \"" + this.applicationName + "\"");
	}

	private void publishToPreview() throws PublishException {

		loggerInstance.log(Level.DEBUG, "Publishing \"" + this.applicationName + "\" for preview");

		// Set the paths for publishing
		String previewSourcePath = Config.getPreviewSourcePath() + this.userName + "/" + this.applicationName;
		File previewSourceFolder = new File(previewSourcePath);

		if (previewSourceFolder.exists()) {
			this.appSourcePath = previewSourcePath;

		} else {
			this.appSourcePath = Config.getAppsSourcePath() + this.userName + "/" + this.applicationName;
		}

		this.appPublishedStructurePath = Config.getPreviewPublishedPath() + this.userName + "/" + this.applicationName;

		copyDirectoriesToProduction();
		copyComponentData();

		if (Config.useExistDb()) {
			initDatabaseForPreview();
		}

		deployToTomcatForPreview();

		loggerInstance.log(Level.DEBUG, "Transformations finished for \"" + this.applicationName + "\"");
	}

	/**
	 * Adds a context descriptor for the application to Tomcat (production
	 * environment).
	 * 
	 * @throws PublishException
	 *             the publish exception
	 */
	private void deployToTomcatForProduction() throws PublishException {

		String contextString = "<Context override=\"true\" crossContext=\"true\" docBase=\""
				+ this.appPublishedStructurePath + "\" />";
		String fileName = this.userName + "#" + this.applicationName + ".xml";
		String contextFilePath = Config.getTomcatContextFilesFolder() + fileName;
		try {

			// ONLY WRITE TO THE FILE IF IT DOESN'T EXIST
			if ((new File(contextFilePath)).exists() == false) {
				SearchServiceImpl.writeStringToFile(contextFilePath, contextString);
			}
		} catch (IOException e) {
			throw new PublishException("Unable to write context information in Tomcat.\n" + e.getMessage());
		}
	}

	/**
	 * Adds a context descriptor for the application to Tomcat (preview
	 * environment).
	 * 
	 * @throws PublishException
	 *             the publish exception
	 */
	private void deployToTomcatForPreview() throws PublishException {

		String fileName = "preview#" + this.userName + "#" + this.applicationName + ".xml";

		// Only write the context file if it doesn't exist
		if ((new File(fileName)).exists() == false) {

			String contextString = "<Context override=\"true\" crossContext=\"true\" docBase=\""
					+ this.appPublishedStructurePath + "\" />";

			try {
				SearchServiceImpl.writeStringToFile(Config.getTomcatContextFilesFolder() + fileName, contextString);

			} catch (IOException e) {
				throw new PublishException("Unable to write context information in Tomcat.\n" + e.getMessage());
			}
		}
	}

	/**
	 * Deletes all files and folders recursively.
	 * 
	 * @param dir
	 *            the directory or file to be deleted
	 */
	public static void deleteDir(File dir) {

		if (dir.isDirectory()) {

			File fileList[] = dir.listFiles();

			for (int index = 0; index < fileList.length; index++) {

				File file = fileList[index];

				deleteDir(file);
			}
		}

		try {
			dir.delete();
		} catch (SecurityException e) {
			loggerInstance.log(Level.DEBUG, "Could not delete file: " + dir.getAbsolutePath());
		}

	}

	/**
	 * Starts the copying process.
	 * 
	 * @throws PublishException
	 *             the publish exception
	 */
	public void copyDirectoriesToProduction() throws PublishException {

		long start = System.currentTimeMillis(); // start timing

		File src1 = new File(this.appSourcePath);

		String srcPath = this.appSourcePath;
		String dstPath = this.appPublishedStructurePath;

		try {

			String files1[] = src1.list();

			// Loop through the files

			for (int i = 0; i < files1.length; i++) {

				String fileName1 = files1[i];

				if (fileName1.equals("db")) {

					File src2 = new File(srcPath + "/db");
					String files2[] = src2.list();

					for (int j = 0; j < files2.length; j++) {

						String fileName2 = files2[j];

						String dstDb = this.appPublishedStructurePath + "/WEB-INF/db/xide/" + this.applicationName;

						copyDirectory(new File(src2, fileName2), new File(dstDb, fileName2));
					}

				} else {
					copyDirectory(new File(srcPath, fileName1), new File(dstPath, fileName1));
				}
			}

		} catch (IOException ex) {

			loggerInstance.log(Level.ERROR, "Unable to copy files when publishing.\n" + ex.getMessage());

			throw new PublishException("Unable to copy files.");
		}

		long stop = System.currentTimeMillis(); // stop timing
		loggerInstance.log(Level.INFO, "File copy time: " + (stop - start) + " ms");
	}

	/**
	 * This method copies files and folders recursively
	 * 
	 * @param srcPath
	 *            The source file or directory to be copied
	 * @param dstPath
	 *            The destination where copies are generated
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void copyDirectory(File srcPath, File dstPath) throws IOException {

		// Avoid hidden files and folders
		if (srcPath.isHidden()) {
			return;
		}

		// Check if the source is a directory

		if (srcPath.isDirectory()) {

			// Create a new directory in the destination if it doesn't exist

			if (!dstPath.exists()) {

				dstPath.mkdirs();
			}

			// Get all the files in the source directory

			String files[] = srcPath.list();

			// Loop through the files

			for (int i = 0; i < files.length; i++) {

				// Copy the directory or file (recursive call)

				copyDirectory(new File(srcPath, files[i]), new File(dstPath, files[i]));
			}
		} else {

			if (!srcPath.exists()) {

				loggerInstance.log(Level.ERROR, "Source not found: " + srcPath.getAbsolutePath());
			}

			else

			// Copy a file

			{
				InputStream in = new FileInputStream(srcPath);
				OutputStream out = new FileOutputStream(dstPath);

				byte[] buf = new byte[1024];
				int len;

				while ((len = in.read(buf)) > 0) {

					out.write(buf, 0, len);
				}

				in.close();
				out.close();
			}
		}
	}

	/**
	 * Copies the necessary component data into the user's folder.
	 * 
	 * This method also creates the necessary transformers for each component
	 * and page.
	 * 
	 * @throws PublishException
	 *             the publish exception
	 */
	public void copyComponentData() throws PublishException {

		long start = System.currentTimeMillis(); // stop timing

		// All component file copies which are removed when transformation are
		// finished
		ArrayList<File> componentFileCopies = new ArrayList<File>();

		try {

			// Loop through the pages in the webapplication

			for (int i = 0; i < this.pages.size(); i++) {

				// Get a page name

				String pageName = this.pages.get(i);

				// Get the .xml file from the production system

				File xmlPageFile = new File(this.appPublishedStructurePath + "/" + pageName + ".xml");

				// Make sure we are not dealing with an empty file

				if (xmlPageFile.exists()) {

					loggerInstance.log(Level.DEBUG, "Starting to handle \"" + xmlPageFile.getName() + "\"");

					// Create a new DOM model from the index.xml

					Document doc = createXMLDocumentFromFile(xmlPageFile);

					if (doc != null) {

						// Add unique ids for the components
						addComponentIDs(doc, xmlPageFile);

						// Get the components found in the document
						Hashtable<String, String> components = getComponentListWithID(doc);

						// Loop through the found components

						Enumeration<String> e = components.keys();

						int idCounter = 0;

						while (e.hasMoreElements()) {

							// Get the component name

							String componentId = e.nextElement();
							String componentName = components.get(componentId);

							// Don't copy the component files if they already
							// exist

							File testFile = new File(this.appPublishedStructurePath + "/css/" + componentName);
							if (!testFile.exists()) {

								// Create the files (source and target) for the
								// component

								String cssSourcePath = this.componentsRoot + componentName + "/css";
								File cssSourceFile = new File(cssSourcePath);

								String cssTargetPath = this.appPublishedStructurePath + "/css";
								File cssTargetFile = new File(cssTargetPath);

								String dataSourcePath = this.componentsRoot + componentName + "/data";
								File dataSourceFile = new File(dataSourcePath);

								String dataTargetPath = this.appPublishedStructurePath + "/data";
								File dataTargetFile = new File(dataTargetPath);

								String dbSourcePath = this.componentsRoot + componentName + "/db";
								File dbSourceFile = new File(dbSourcePath);

								String dbTargetPath = this.appPublishedStructurePath + "/WEB-INF/db/xide/"
										+ this.applicationName;
								File dbTargetFile = new File(dbTargetPath);

								String querySourcePath = this.componentsRoot + componentName + "/query";
								File querySourceFile = new File(querySourcePath);

								String queryTargetPath = this.appPublishedStructurePath + "/query";
								File queryTargetFile = new File(queryTargetPath);

								String resourceSourcePath = this.componentsRoot + componentName + "/resource";
								File resourceSourceFile = new File(resourceSourcePath);

								String resourceTargetPath = this.appPublishedStructurePath + "/resource";
								File resourceTargetFile = new File(resourceTargetPath);

								// Copy the component files

								copyDirectory(cssSourceFile, cssTargetFile);
								copyDirectory(dataSourceFile, dataTargetFile);
								copyDirectory(dbSourceFile, dbTargetFile);
								copyDirectory(querySourceFile, queryTargetFile);
								copyDirectory(resourceSourceFile, resourceTargetFile);
							}

							loggerInstance.log(Level.DEBUG, "Copied component \"" + componentName + "\" to production");

							// Get the component parameters
							Hashtable<String, String> componentParams = getComponentParameterHashtable(doc, componentId);

							// Create a new components parameter transformer
							// which
							// sets component specific parameters

							ComponentTransformer componentTransformer = new ComponentTransformer(componentName,
									componentId, componentParams, this.userName, idCounter);

							componentFileCopies.add(componentTransformer.getComponentFileCopy());

							idCounter = componentTransformer.getIdCounter() + 1;
						}

						File targetFile = new File(this.appPublishedStructurePath + "/" + pageName + ".xformsdb");
						new XideToXFormsDBTransformer(xmlPageFile, targetFile, this.userName);

						// Remove the original .xml file
						xmlPageFile.delete();
					}
				}

				loggerInstance.log(Level.DEBUG, "Page \"" + pageName + "\" is now transformed.");
			}
		} catch (IOException ex) {

			loggerInstance.log(Level.ERROR, "IOException when copying and transforming pages / components.\n"
					+ ex.getMessage());

			throw new PublishException("Unable to copy component data: " + ex.getMessage());

		} finally {

			for (File file : componentFileCopies) {
				file.delete();
			}
		}

		long stop = System.currentTimeMillis(); // stop timing
		loggerInstance.log(Level.INFO, "Handling time for components: " + (stop - start) + " ms");
	}

	/**
	 * Adds ID attributes to each component.
	 * 
	 * @param doc
	 *            XML document where the component IDs are added
	 * @param xmlPageFile
	 *            XML file where the component IDs are added
	 * 
	 * @throws PublishException
	 *             the publish exception
	 */
	private void addComponentIDs(Document doc, File xmlPageFile) throws PublishException {

		NodeList componentNodeList = doc.getDocumentElement().getElementsByTagName(this.componentTag);

		for (int i = 0; i < componentNodeList.getLength(); i++) {

			Element componentNode = (Element) componentNodeList.item(i);
			String componentID = getRandomID();
			componentNode.setAttribute("id", componentID);
		}

		try {

			DOMSource source = new DOMSource(doc);
			Result result = new StreamResult(xmlPageFile);
			Transformer trans = TransformerFactory.newInstance().newTransformer();
			trans.transform(source, result);

		} catch (TransformerConfigurationException e) {

			loggerInstance.log(Level.ERROR, "TransformerConfigurationException occured when adding component ID's. \n"
					+ e.getMessage());

			throw new PublishException("TransformerConfigurationException occured when adding component ID's. \n"
					+ e.getMessage());

		} catch (TransformerFactoryConfigurationError e) {

			loggerInstance.log(Level.ERROR,
					"TransformerFactoryConfigurationError occured when adding component ID's. \n" + e.getMessage());

			throw new PublishException("TransformerFactoryConfigurationError occured when adding component ID's. \n"
					+ e.getMessage());

		} catch (TransformerException e) {

			loggerInstance.log(Level.ERROR, "TransformerException occured when adding component ID's. \n"
					+ e.getMessage());

			throw new PublishException("TransformerException occured when adding component ID's. \n" + e.getMessage());
		}

	}

	/**
	 * Gets the components and their IDs.
	 * 
	 * @param doc
	 *            XML document
	 * 
	 * @return Hashtable that's keys are component ID's and values are component
	 *         names
	 */
	public Hashtable<String, String> getComponentListWithID(Document doc) {

		NodeList componentNodeList = doc.getDocumentElement().getElementsByTagName(this.componentTag);

		Hashtable<String, String> components = new Hashtable<String, String>();

		for (int i = 0; i < componentNodeList.getLength(); i++) {

			Element componentNode = (Element) componentNodeList.item(i);
			NamedNodeMap componentAttributes = componentNode.getAttributes();
			Node componentNameNode = componentAttributes.getNamedItem("name");
			Node componentIdNode = componentAttributes.getNamedItem("id");

			components.put(componentIdNode.getNodeValue().trim(), componentNameNode.getNodeValue().trim());

		}
		return components;
	}

	/**
	 * Gets component's parameters that are given in the XML document with
	 * "<template with-param>" tags.
	 * 
	 * @param doc
	 *            XML document
	 * @param componentId
	 *            The component's ID
	 * 
	 * @return Hashtable that's keys are parameter names and values parameter
	 *         values
	 * 
	 */
	public Hashtable<String, String> getComponentParameterHashtable(Document doc, String componentId) {

		// Get component parameters

		NodeList componentNodeList = doc.getDocumentElement().getElementsByTagName(this.componentTag);
		Node componentNode = null;
		NamedNodeMap componentAttributes = null;

		Hashtable<String, String> componentParameters = new Hashtable<String, String>();

		for (int i = 0; i < componentNodeList.getLength(); i++) {

			componentNode = componentNodeList.item(i);
			componentAttributes = componentNode.getAttributes();

			if (componentId.equals(componentAttributes.getNamedItem("id").getNodeValue().trim())) {

				// Component's "<template with-param>" nodes
				NodeList withParamNodeList = componentNode.getChildNodes();

				for (int j = 0; j < withParamNodeList.getLength(); j++) {

					Node parameterNode = withParamNodeList.item(j);

					if (parameterNode.hasAttributes()) {

						NamedNodeMap parameterAttributes = parameterNode.getAttributes();
						String parameterName = parameterAttributes.getNamedItem("name").getNodeValue();
						String parameterValue = parameterNode.getLastChild().getNodeValue();

						componentParameters.put(parameterName, parameterValue);
					}
				}
			}
		}

		return componentParameters;
	}

	/**
	 * Creates a XML document.
	 * 
	 * @param file
	 *            The file from which we want to generate the XML document.
	 * 
	 * @return The generated XML document
	 * 
	 * @throws PublishException
	 *             the publish exception
	 */
	public Document createXMLDocumentFromFile(File file) throws PublishException {

		Document doc = null;

		try {
			// Set the DTD to point to right folder

			XFormsDBHandler xformsdbHandler = new XFormsDBHandler();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			dbf.setValidating(false);

			DocumentBuilder db;

			db = dbf.newDocumentBuilder();

			db.setEntityResolver(xformsdbHandler);
			doc = db.parse(file);

		} catch (ParserConfigurationException e) {

			loggerInstance.log(Level.ERROR,
					"ParserConfigurationException occured when creating a document from xml file. \n" + e.getMessage());

			throw new PublishException(
					"ParserConfigurationException occured when creating a document from xml file. \n" + e.getMessage());

		} catch (SAXException e) {

			loggerInstance.log(Level.ERROR, "SAXException occured when creating a document from xml file. \n"
					+ e.getMessage());

			throw new PublishException("SAXException occured when creating a document from xml file. \n"
					+ e.getMessage());

		} catch (IOException e) {

			loggerInstance.log(Level.ERROR, "IOException occured when creating a document from xml file. \n"
					+ e.getMessage());

			throw new PublishException("IOException occured when creating a document from xml file. \n"
					+ e.getMessage());

		}

		return doc;
	}

	public void initDatabaseForProduction() throws PublishException {

		new ExistManager(this.applicationName, this.userName, this.appPublishedStructurePath + "/",
				PublishHandler.PUBLISH_TO_PRODUCTION);

	}

	public void initDatabaseForPreview() throws PublishException {

		new ExistManager(this.applicationName, this.userName, this.appPublishedStructurePath + "/",
				PublishHandler.PUBLISH_TO_PREVIEW);
	}

	/**
	 * Create a random ID
	 * 
	 * @return The generated random ID.
	 */
	private String getRandomID() {

		Random r = new Random();
		String randomID = Long.toString(Math.abs(r.nextLong()), 16);

		return randomID;
	}

	/**
	 * Sets the correct paths for log4j
	 * 
	 * @throws PublishException
	 */
	private void setLog4jProperties() throws PublishException {

		File propertiesFile = new File(this.appPublishedStructurePath + "/WEB-INF/classes/log4j.properties");
		Properties prop = loadProperties(propertiesFile);
		prop = updateProperties(prop);
		saveProperties(prop, propertiesFile);
	}

	/**
	 * Loads properties from a file.
	 * 
	 * @param fileName
	 * @return
	 */
	private Properties loadProperties(File file) throws PublishException {

		InputStream inPropFile;
		Properties tempProp = new Properties();

		try {
			inPropFile = new FileInputStream(file);
			tempProp.load(inPropFile);
			inPropFile.close();
		} catch (IOException ioe) {
			throw new PublishException("Could not load log4j properties" + ioe.getMessage());
		}

		return tempProp;

	}

	/**
	 * Updates properties
	 */
	private Properties updateProperties(Properties properties) {

		Enumeration<?> enumProps = properties.propertyNames();
		String key = "";

		while (enumProps.hasMoreElements()) {

			key = (String) enumProps.nextElement();

			if (key.equals("log4j.appender.SINGLE_FILE_APPENDER.File")
					|| key.equals("log4j.appender.ROLLING_FILE_APPENDER.File")) {

				properties.setProperty(key, Config.getTomcatPath() + "logs/" + this.userName + "/"
						+ this.applicationName + ".log");
			}
		}
		return properties;
	}

	private void saveProperties(Properties p, File file) throws PublishException {

		OutputStream outPropFile;

		try {
			outPropFile = new FileOutputStream(file);
			p.store(outPropFile, "Log4j for " + this.applicationName);
			outPropFile.close();
		} catch (IOException ioe) {
			throw new PublishException("Could not save log4j properties" + ioe.getMessage());
		}

	}

	public String getPublishedAppPath() {
		return this.appPublishedStructurePath;
	}

}