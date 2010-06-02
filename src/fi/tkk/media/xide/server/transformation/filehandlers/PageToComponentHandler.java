package fi.tkk.media.xide.server.transformation.filehandlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import fi.tkk.media.xide.client.Server.RPC.PublishException;
import fi.tkk.media.xide.server.Config;
import fi.tkk.media.xide.server.SearchServiceImpl;
import fi.tkk.media.xide.server.transformation.transformers.XSLTURIResolver;
import fi.tkk.tml.xformsdb.xml.sax.XFormsDBHandler;

/**
 * This class transforms a XIDE page into a XIDE component.
 */
public class PageToComponentHandler {

	/** The application name which contains the page to be converted. */
	private final String applicationName;

	/** The user name that owns the page. */
	private final String userName;

	/** The page to be converted into a component. */
	private final String pageName;

	/** The application production path. */
	private final String appProductionPath;

	/** The application source path. */
	private final String appSourcePath;

	/** The to be created component's name. */
	private String componentName;

	/** The Constant logger. */

	private static Logger loggerInstance = Logger.getLogger(PageToComponentHandler.class);

	/**
	 * Instantiates a new page to component handler.
	 * 
	 * @param userName
	 *            the user name
	 * @param applicationName
	 *            the application name
	 * @param pageName
	 *            the page name
	 * @throws PublishException
	 */
	public PageToComponentHandler(String userName, String applicationName, String pageName, String componentName)
			throws PublishException {

		this.userName = userName;
		this.applicationName = applicationName;
		this.pageName = pageName;
		this.componentName = componentName;
		this.appProductionPath = Config.getTempFilesPath() + this.userName + "/" + this.applicationName + "/";
		this.appSourcePath = Config.getAppsSourcePath() + this.userName + "/" + this.applicationName + "/";

		start();
	}

	/**
	 * Start the transformation process. The method goes through each component
	 * folder and handles their content individually.
	 * 
	 * @throws PublishException
	 */
	private void start() throws PublishException {

		try {

			createFileStructure();
			publishPageToTemporaryLocation();

			File xFormsFile = new File(this.appProductionPath + this.pageName + ".xformsdb");
			File componentFile = new File(Config.getComponentsPath() + this.componentName + "/" + this.componentName
					+ ".xml");
			copyFile(xFormsFile, componentFile);

			Document doc = createDocumentFromFile(componentFile);

			ArrayList<String> usedComponents = new ArrayList<String>();
			usedComponents = getUsedComponents(usedComponents);

			/** Handle the page content */
			handleCss(doc, this.pageName);
			handleCssTheme(doc);
			handleData(doc, this.pageName);
			handleDbData(doc, this.pageName);
			handleDbRealm(doc, this.pageName);
			handleQuery(doc, this.pageName);
			handleResource(doc, this.pageName);

			/** Handle the used components' content */
			for (int i = 0; i < usedComponents.size(); i++) {

				String usedComponentName = usedComponents.get(i);
				handleCss(doc, usedComponentName);
				handleData(doc, usedComponentName);
				handleDbData(doc, usedComponentName);
				handleDbRealm(doc, usedComponentName);
				handleQuery(doc, usedComponentName);
				handleResource(doc, usedComponentName);
			}

			writeDocumentToFile(componentFile, doc, false);
			transform(componentFile);
			postProcessDocument(componentFile);
			removePageFromTemporaryLocation();

			loggerInstance.log(Level.DEBUG, "Page succesfully converted into a component!");

		} catch (IOException e) {

			loggerInstance.log(
					Level.ERROR,
					"IOException occured when transforming \"" + this.pageName + "\" into a component.\n"
							+ e.getMessage());
			throw new PublishException("IOException occured when transforming \"" + this.pageName
					+ "\" into a compoennt.\n" + e.getMessage());

		} catch (TransformerFactoryConfigurationError e) {

			loggerInstance.log(
					Level.ERROR,
					"TransformerFactoryConfigurationError occured when transforming \"" + this.pageName
							+ "\" into a component.\n" + e.getMessage());
			throw new PublishException("TransformerFactoryConfigurationError occured when transforming \""
					+ this.pageName + "\" into a component.\n" + e.getMessage());

		} catch (XPathExpressionException e) {
			loggerInstance.log(
					Level.ERROR,
					"XPathExpressionException occured when transforming \"" + this.pageName + "\" into a component.\n"
							+ e.getMessage());
			throw new PublishException("XPathExpressionException occured when transforming \"" + this.pageName
					+ "\" into a component.\n" + e.getMessage());
		} catch (TransformerException e) {
			loggerInstance.log(
					Level.ERROR,
					"TransformerException occured when transforming \"" + this.pageName + "\" into a component.\n"
							+ e.getMessage());
			throw new PublishException("TransformerException occured when transforming \"" + this.pageName
					+ "\" into a component.\n" + e.getMessage());

		} catch (Exception e) {

			loggerInstance.log(
					Level.ERROR,
					"Exception occured when transforming \"" + this.pageName + "\" into a component.\n"
							+ e.getMessage());

			throw new PublishException("Exception occured when transforming \"" + this.pageName
					+ "\" into a component.\n" + e.getMessage());
		}
	}

	/**
	 * Publish the page to a temporary location.
	 * 
	 * @throws PublishException
	 *             the publish exception
	 */
	private void publishPageToTemporaryLocation() throws PublishException {

		ArrayList<String> pages = new ArrayList<String>();
		pages.add(this.pageName);
		new PublishHandler(this.userName, this.applicationName, pages, PublishHandler.PUBLISH_TO_TEMP, false);
	}

	/**
	 * Remove the page from the temporary location
	 */
	private void removePageFromTemporaryLocation() {

		PublishHandler.deleteDir(new File(Config.getTempFilesPath() + this.userName + "/" + this.applicationName));
	}

	/**
	 * This method uses XSLT to transform a file to component structure.
	 * 
	 * @throws TransformerException
	 *             the transformer exception
	 * @throws IOException
	 */
	private void transform(File file) throws TransformerException, IOException {

		loggerInstance.log(Level.DEBUG, "Starting the transformation process for \"" + file.getName() + "\"");

		File clone = new File(file.getParent() + "/" + file.getName() + "_clone.xml");
		copyFile(file, clone);

		StreamSource xmlSource = new StreamSource(clone);

		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(
				Config.getPageToComponentPath());
		StreamSource xsltSource = new StreamSource(inputStream);

		StreamResult result = new StreamResult(file);

		TransformerFactory transFact = TransformerFactory.newInstance();
		transFact.setURIResolver(new XSLTURIResolver());

		Transformer trans = transFact.newTransformer(xsltSource);
		trans.setParameter("identityPath", Config.getPageToComponentPath());
		trans.transform(xmlSource, result);

		clone.delete();
	}

	/**
	 * Post process the generated component file.
	 * 
	 * @throws PublishException
	 *             the publish exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws TransformerFactoryConfigurationError
	 *             the transformer factory configuration error
	 * @throws TransformerException
	 *             the transformer exception
	 */
	private void postProcessDocument(File file) throws PublishException, IOException,
			TransformerFactoryConfigurationError, TransformerException {

		loggerInstance.log(Level.DEBUG, "Postprocessing \"" + file.getName() + "\"");

		Document transformedDocument = createDocumentFromFile(file);
		NodeList nodes = transformedDocument.getDocumentElement().getElementsByTagName("template:meta");

		for (int i = 0; i < nodes.getLength(); i++) {

			Node metaNode = nodes.item(i);
			NamedNodeMap nodeMap = metaNode.getAttributes();
			if (nodeMap != null) {

				Node equivNode = nodeMap.getNamedItem("http-equiv");
				if (equivNode != null) {

					if (equivNode.getNodeValue().equals("Content-Type")) {
						metaNode.getParentNode().removeChild(metaNode);
					}
				}
			}
		}

		writeDocumentToFile(file, transformedDocument, true);
	}

	/**
	 * Creates the file structure for a component.
	 * 
	 * @throws PublishException
	 *             the publish exception
	 */
	private void createFileStructure() {

		loggerInstance.log(Level.DEBUG, "Creating folder structure for the new component \"" + this.componentName + "\"");

		File componentFolder = new File(Config.getComponentsPath() + this.componentName);
		componentFolder.mkdir();
		new File(componentFolder, "css/" + this.componentName).mkdirs();
		new File(componentFolder, "data/" + this.componentName).mkdirs();
		new File(componentFolder, "db/" + this.componentName).mkdirs();
		new File(componentFolder, "db/" + this.componentName + "/data/").mkdirs();
		new File(componentFolder, "db/" + this.componentName + "/realm/").mkdirs();
		new File(componentFolder, "query/" + this.componentName).mkdirs();
		new File(componentFolder, "resource/" + this.componentName).mkdirs();
	}

	/**
	 ********************************** CSS handling (start) **************************************
	 * 
	 * Handles the CSS content.
	 * 
	 * @param folderName
	 *            the folder name
	 * 
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws PublishException
	 *             the publish exception
	 */

	private void handleCss(Document doc, String folderName) throws IOException, PublishException,
			XPathExpressionException {

		loggerInstance.log(Level.DEBUG, "Handling css/ for \"" + folderName + "\"");

		ArrayList<String> cssFilepathList = new ArrayList<String>();

		/** Copy components' CSS files */
		File start = new File(this.appProductionPath + "css/" + folderName + "/");
		ArrayList<String> extensions = new ArrayList<String>();
		extensions.add("css");
		createFileListFromExtensions(start, cssFilepathList, extensions);
		for (int i = 0; i < cssFilepathList.size(); i++) {

			File srcCss = new File(cssFilepathList.get(i));
			File dstCss = new File(Config.getComponentsPath() + this.componentName + "/css/" + this.componentName + "/"
					+ srcCss.getName());

			// Rename the file if it already exists in the folder
			if (dstCss.exists()) {
				String name = dstCss.getName();
				String ext = (name.lastIndexOf(".") == -1) ? "" : name.substring(name.lastIndexOf(".") + 1, name
						.length());
				dstCss = createUniqueFile(dstCss, ext);
			}
			copyFile(srcCss, dstCss);
			updateCssReference(doc, srcCss, dstCss);
		}
	}

	/**
	 * Handles the CSS theme file.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 * @throws PublishException
	 *             the publish exception
	 */
	private void handleCssTheme(Document doc) throws IOException, XPathExpressionException, PublishException {

		File srcCss = new File(this.appProductionPath + "css/reset_theme.css");

		if (srcCss.exists()) {
			File dstCss = new File(Config.getComponentsPath() + this.componentName + "/css/" + this.componentName
					+ "/reset_theme.css");

			if (dstCss.exists()) {
				dstCss = createUniqueFile(dstCss, "css");
			}
			copyFile(srcCss, dstCss);
			updateCssReference(doc, srcCss, dstCss);
		}
	}

	/**
	 * Update the CSS references in the document.
	 * 
	 * @param origCss
	 *            the orig css
	 * @param newCss
	 *            the new css
	 * 
	 * @throws PublishException
	 *             the publish exception
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void updateCssReference(Document doc, File origCss, File newCss) throws PublishException,
			XPathExpressionException, IOException {

		String origCssFullPath = origCss.getAbsolutePath();

		// FOR WINDOWS
		origCssFullPath = origCssFullPath.replace("\\", "/");

		String newCssFileName = newCss.getName();

		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		XPathExpression expr = xpath.compile("//@href");
		Object object = expr.evaluate(doc, XPathConstants.NODESET);
		NodeList nodes = (NodeList) object;

		for (int i = 0; i < nodes.getLength(); i++) {

			Node hrefNode = nodes.item(i);
			String hrefValue = hrefNode.getNodeValue();
			if (origCssFullPath.contains(hrefValue)) {
				hrefNode.setNodeValue("css/" + this.componentName + "/" + newCssFileName);
			}
		}
	}

	/**
	 ********************************** CSS handling (end) **************************************
	 */

	/**
	 * 
	 ********************************** Data handling (start) ***********************************
	 * 
	 * Handles the data content.
	 * 
	 * @param folderName
	 *            the folder name
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws PublishException
	 *             the publish exception
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 * 
	 * @throws XPathExpressionException
	 */

	private void handleData(Document doc, String folderName) throws IOException, PublishException,
			XPathExpressionException {

		loggerInstance.log(Level.DEBUG, "Handling data/ for \"" + folderName + "\"");

		ArrayList<String> dataFilepathList = new ArrayList<String>();

		/** Copy components' data files */
		File start = new File(this.appProductionPath + "data/" + folderName + "/");
		ArrayList<String> extensions = new ArrayList<String>();
		extensions.add("xml");
		createFileListFromExtensions(start, dataFilepathList, extensions);

		for (int i = 0; i < dataFilepathList.size(); i++) {

			File srcData = new File(dataFilepathList.get(i));
			File dstData = new File(Config.getComponentsPath() + this.componentName + "/data/" + this.componentName
					+ "/" + srcData.getName());

			// Rename the file if it already exists in the folder
			if (dstData.exists()) {
				String name = dstData.getName();
				String ext = (name.lastIndexOf(".") == -1) ? "" : name.substring(name.lastIndexOf(".") + 1, name
						.length());
				dstData = createUniqueFile(dstData, ext);
			}
			copyFile(srcData, dstData);
			updateDataReference(doc, srcData, dstData);
		}
	}

	/**
	 * Update data references.
	 * 
	 * @param origData
	 *            the orig data
	 * @param newData
	 *            the new data
	 * 
	 * @throws PublishException
	 *             the publish exception
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private void updateDataReference(Document doc, File origData, File newData) throws PublishException,
			XPathExpressionException {

		String origDataFullPath = origData.getAbsolutePath();

		// FOR WINDOWS
		origDataFullPath = origDataFullPath.replace("\\", "/");

		String newDataFileName = newData.getName();

		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();

		XPathExpression expr = xpath.compile("//@src");
		Object object = expr.evaluate(doc, XPathConstants.NODESET);

		NodeList nodes = (NodeList) object;

		for (int i = 0; i < nodes.getLength(); i++) {

			Node srcNode = nodes.item(i);
			String srcValue = srcNode.getNodeValue();
			if (origDataFullPath.contains(srcValue)) {
				srcNode.setNodeValue("data/" + this.componentName + "/" + newDataFileName);
			}
		}

		expr = xpath.compile("//@action");
		object = expr.evaluate(doc, XPathConstants.NODESET);

		nodes = (NodeList) object;

		for (int i = 0; i < nodes.getLength(); i++) {

			Node actionNode = nodes.item(i);
			String actionValue = actionNode.getNodeValue();
			if (origDataFullPath.contains(actionValue)) {
				actionNode.setNodeValue("data/" + this.componentName + "/" + newDataFileName);
			}

		}

		expr = xpath.compile("//@resource");
		object = expr.evaluate(doc, XPathConstants.NODESET);

		nodes = (NodeList) object;

		for (int i = 0; i < nodes.getLength(); i++) {

			Node actionNode = nodes.item(i);
			String actionValue = actionNode.getNodeValue();
			if (origDataFullPath.contains(actionValue)) {
				actionNode.setNodeValue("data/" + this.componentName + "/" + newDataFileName);
			}

		}
	}

	/**
	 ********************************** Data handling (end) **************************************
	 */

	/**
	 * 
	 ********************************** Db handling (start) *************************************
	 * 
	 * Handles the db content.
	 * 
	 * @param folderName
	 *            the folder name
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws PublishException
	 *             the publish exception
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 * 
	 * 
	 * @throws XPathExpressionException
	 */

	private void handleDbData(Document doc, String folderName) throws IOException, PublishException,
			XPathExpressionException {

		loggerInstance.log(Level.DEBUG, "Handling db/data/ for \"" + folderName + "\"");

		ArrayList<String> dbFilepathList = new ArrayList<String>();

		/** Copy components' data files */
		String fullFolderPath = this.appProductionPath + "WEB-INF/db/xide/" + this.applicationName + "/" + folderName
				+ "/" + "data/";
		File start = new File(fullFolderPath);
		ArrayList<String> extensions = new ArrayList<String>();
		extensions.add("xml");
		createFileListFromExtensions(start, dbFilepathList, extensions);

		for (int i = 0; i < dbFilepathList.size(); i++) {

			String fullFilePath = dbFilepathList.get(i);
			File srcDb = new File(fullFilePath);
			File dstDb = new File(Config.getComponentsPath() + this.componentName + "/db/" + this.componentName
					+ "/data/" + srcDb.getName());

			// Rename the file if it already exists in the folder
			if (dstDb.exists()) {
				String name = dstDb.getName();
				String ext = (name.lastIndexOf(".") == -1) ? "" : name.substring(name.lastIndexOf(".") + 1, name
						.length());
				dstDb = createUniqueFile(dstDb, ext);
			}
			copyFile(srcDb, dstDb);
			updateDbDataReference(doc, srcDb, dstDb, folderName);
		}
	}

	/**
	 * Update db data references.
	 * 
	 * @param origDb
	 *            the orig db
	 * @param newDb
	 *            the new db
	 * @param folderName
	 *            the folder name
	 * 
	 * @throws PublishException
	 *             the publish exception
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private void updateDbDataReference(Document doc, File origDb, File newDb, String folderName)
			throws PublishException, XPathExpressionException {

		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();

		XPathExpression expr = xpath.compile("//@datasrc");
		Object object = expr.evaluate(doc, XPathConstants.NODESET);

		NodeList nodes = (NodeList) object;

		for (int i = 0; i < nodes.getLength(); i++) {

			Attr node = (Attr) nodes.item(i);
			String value = node.getNodeValue();

			int firstLine = value.indexOf("-");
			String firstValue = value.substring(0, firstLine);

			if (value.endsWith("data-source") && firstValue.equals(folderName)) {

				Node parent = node.getOwnerElement();
				NamedNodeMap attrs = parent.getAttributes();
				Node docNode = attrs.getNamedItem("doc");

				if (docNode != null) {

					if (origDb.getName().equals(docNode.getNodeValue())) {
						docNode.setNodeValue(newDb.getName());
						value = this.componentName + value.substring(firstLine);
						node.setNodeValue(value);
					}
				}
			}
		}
	}

	/**
	 * Handle db realm.
	 * 
	 * @param folderName
	 *            the folder name
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws PublishException
	 *             the publish exception
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private void handleDbRealm(Document doc, String folderName) throws IOException, PublishException,
			XPathExpressionException {

		loggerInstance.log(Level.DEBUG, "Handling db/realm/ for \"" + folderName + "\"");

		ArrayList<String> dbFilepathList = new ArrayList<String>();

		/** Copy components' data files */
		String fullFolderPath = this.appProductionPath + "WEB-INF/db/xide/" + this.applicationName + "/" + folderName
				+ "/" + "realm/";
		File start = new File(fullFolderPath);
		ArrayList<String> extensions = new ArrayList<String>();
		extensions.add("xml");
		createFileListFromExtensions(start, dbFilepathList, extensions);

		for (int i = 0; i < dbFilepathList.size(); i++) {

			String fullFilePath = dbFilepathList.get(i);
			File srcDb = new File(fullFilePath);
			File dstDb = new File(Config.getComponentsPath() + this.componentName + "/db/" + this.componentName
					+ "/realm/" + srcDb.getName());

			// Rename the file if it already exists in the folder
			if (dstDb.exists()) {
				String name = dstDb.getName();
				String ext = (name.lastIndexOf(".") == -1) ? "" : name.substring(name.lastIndexOf(".") + 1, name
						.length());
				dstDb = createUniqueFile(dstDb, ext);
			}
			copyFile(srcDb, dstDb);
			updateDbRealmReference(doc, srcDb, dstDb, folderName);
		}
	}

	/**
	 * Update db realm references.
	 * 
	 * @param origDb
	 *            the orig db
	 * @param newDb
	 *            the new db
	 * @param folderName
	 *            the folder name
	 * 
	 * @throws PublishException
	 *             the publish exception
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private void updateDbRealmReference(Document doc, File origDb, File newDb, String folderName)
			throws PublishException, XPathExpressionException {

		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();

		XPathExpression expr = xpath.compile("//@datasrc");
		Object object = expr.evaluate(doc, XPathConstants.NODESET);

		NodeList nodes = (NodeList) object;

		for (int i = 0; i < nodes.getLength(); i++) {

			Attr node = (Attr) nodes.item(i);
			String value = node.getNodeValue();

			int firstLine = value.indexOf("-");
			String firstValue = value.substring(0, firstLine);

			if (value.endsWith("realm-source") && firstValue.equals(folderName)) {

				Node parent = node.getOwnerElement();
				NamedNodeMap attrs = parent.getAttributes();
				Node docNode = attrs.getNamedItem("doc");

				if (docNode != null) {

					if (origDb.getName().equals(docNode.getNodeValue())) {
						docNode.setNodeValue(newDb.getName());
						value = this.componentName + value.substring(firstLine);
						node.setNodeValue(value);
					}
				}
			}
		}
	}

	/**
	 ********************************** Db handling (end) **************************************
	 */

	/**
	 * 
	 ******************************** Query handling (start) ************************************
	 * 
	 * Handles query content.
	 * 
	 * @param folderName
	 *            the folder name
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws PublishException
	 *             the publish exception
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */

	private void handleQuery(Document doc, String folderName) throws IOException, PublishException,
			XPathExpressionException {

		loggerInstance.log(Level.DEBUG, "Handling query/ for \"" + folderName + "\"");

		ArrayList<String> queryFilepathList = new ArrayList<String>();

		File start = new File(this.appProductionPath + "query/" + folderName + "/");
		ArrayList<String> extensions = new ArrayList<String>();
		extensions.add("xq");
		createFileListFromExtensions(start, queryFilepathList, extensions);

		for (int i = 0; i < queryFilepathList.size(); i++) {

			File srcQuery = new File(queryFilepathList.get(i));
			File dstQuery = new File(Config.getComponentsPath() + this.componentName + "/query/" + this.componentName
					+ "/" + srcQuery.getName());

			// Rename the file if it already exists in the folder
			if (dstQuery.exists()) {
				String name = dstQuery.getName();
				String ext = (name.lastIndexOf(".") == -1) ? "" : name.substring(name.lastIndexOf(".") + 1, name
						.length());
				dstQuery = createUniqueFile(dstQuery, ext);
			}
			copyFile(srcQuery, dstQuery);
			updateQueryReference(doc, srcQuery, dstQuery);
		}
	}

	/**
	 * Update query references.
	 * 
	 * @param origQuery
	 *            the orig query
	 * @param newQuery
	 *            the new query
	 * 
	 * @throws PublishException
	 *             the publish exception
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private void updateQueryReference(Document doc, File origQuery, File newQuery) throws PublishException,
			XPathExpressionException {

		String origQueryFullPath = origQuery.getAbsolutePath();

		// FOR WINDOWS
		origQueryFullPath = origQueryFullPath.replace("\\", "/");

		String newQueryFileName = newQuery.getName();

		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();

		XPathExpression expr = xpath.compile("//@src");
		Object object = expr.evaluate(doc, XPathConstants.NODESET);

		NodeList nodes = (NodeList) object;

		for (int i = 0; i < nodes.getLength(); i++) {

			Node srcNode = nodes.item(i);
			String srcValue = srcNode.getNodeValue();
			if (origQueryFullPath.contains(srcValue)) {
				srcNode.setNodeValue("query/" + this.componentName + "/" + newQueryFileName);
			}
		}
	}

	/**
	 ********************************** Query handling (end) **************************************
	 */

	/**
	 * 
	 ********************************** Resource handling (start) **************************************
	 * 
	 *Handles resource content.
	 * 
	 * @param folderName
	 *            the folder name
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws PublishException
	 *             the publish exception
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */

	private void handleResource(Document doc, String folderName) throws IOException, PublishException,
			XPathExpressionException {

		loggerInstance.log(Level.DEBUG, "Handling resource/ for \"" + folderName + "\"");

		ArrayList<ArrayList<String>> oldNewValues = new ArrayList<ArrayList<String>>();

		/** Copy components' data files */
		ArrayList<String> extensions = new ArrayList<String>();
		extensions.add("jpg");
		extensions.add("jpeg");
		extensions.add("gif");
		extensions.add("png");
		extensions.add("js");
		ArrayList<String> resourceFilepathList = new ArrayList<String>();
		File start = new File(this.appProductionPath + "resource/" + folderName + "/");
		createFileListFromExtensions(start, resourceFilepathList, extensions);
		for (int i = 0; i < resourceFilepathList.size(); i++) {

			File srcResource = new File(resourceFilepathList.get(i));
			File dstResource = new File(Config.getComponentsPath() + this.componentName + "/resource/"
					+ this.componentName + "/" + srcResource.getName());

			// Rename the file if it already exists in the folder
			if (dstResource.exists()) {
				String name = dstResource.getName();
				String ext = (name.lastIndexOf(".") == -1) ? "" : name.substring(name.lastIndexOf(".") + 1, name
						.length());
				dstResource = createUniqueFile(dstResource, ext);
			}
			copyFile(srcResource, dstResource);
			updateResourceReference(doc, srcResource, dstResource, oldNewValues);
		}

		ArrayList<String> cssFilepathList = new ArrayList<String>();
		File componentCssRoot = new File(Config.getComponentsPath() + this.componentName + "/css/");
		ArrayList<String> cssExtension = new ArrayList<String>();
		cssExtension.add("css");
		createFileListFromExtensions(componentCssRoot, cssFilepathList, cssExtension);

		for (int i = 0; i < cssFilepathList.size(); i++) {
			String cssContent = SearchServiceImpl.readFileAsString(cssFilepathList.get(i));

			for (int j = 0; j < oldNewValues.size(); j++) {
				String oldValue = oldNewValues.get(j).get(0);
				String newValue = oldNewValues.get(j).get(1);
				cssContent = cssContent.replace("resource/" + folderName + "/" + oldValue, "resource/"
						+ this.componentName + "/" + newValue);
			}

			SearchServiceImpl.writeStringToFile(cssFilepathList.get(i), cssContent);
		}
	}

	/**
	 * Update resource references.
	 * 
	 * @param origResource
	 *            the orig resource
	 * @param newResource
	 *            the new resource
	 * @param oldNewValues
	 *            the old new values
	 * 
	 * @throws PublishException
	 *             the publish exception
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private void updateResourceReference(Document doc, File origResource, File newResource,
			ArrayList<ArrayList<String>> oldNewValues) throws PublishException, XPathExpressionException {

		String origResourceFullPath = origResource.getAbsolutePath();

		origResourceFullPath = origResourceFullPath.replace("\\", "/");

		String newResourceFileName = newResource.getName();

		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();

		XPathExpression expr = xpath.compile("//@src");
		Object object = expr.evaluate(doc, XPathConstants.NODESET);

		NodeList nodes = (NodeList) object;

		for (int i = 0; i < nodes.getLength(); i++) {

			Node srcNode = nodes.item(i);
			String srcValue = srcNode.getNodeValue();
			if (origResourceFullPath.contains(srcValue)) {
				srcNode.setNodeValue("resource/" + this.componentName + "/" + newResourceFileName);
			}
		}

		ArrayList<String> oldNewValue = new ArrayList<String>();
		oldNewValue.add(origResource.getName());
		oldNewValue.add(newResourceFileName);
		oldNewValues.add(oldNewValue);
	}

	/**
	 ********************************** Resource handling (end) **************************************
	 */

	/**
	 * 
	 * Creates a unique filename for a given file in the current directory.
	 * 
	 * @param origFile
	 *            the orig file
	 * @param extension
	 *            the extension of the file
	 * 
	 * @return a unique file
	 */

	private File createUniqueFile(File origFile, String extension) {

		File file = null;
		String parentPath = origFile.getParent();
		String fullFileName = origFile.getName();
		String fileName = (fullFileName.lastIndexOf(".") == -1) ? "" : fullFileName.substring(0, fullFileName
				.lastIndexOf("."));

		int counter = 1;
		while (true) {

			file = new File(parentPath + "/" + fileName + "(" + Integer.toString(counter) + ")." + extension);

			if (file.exists()) {
				counter++;
				continue;
			} else {
				break;
			}
		}

		return file;
	}

	/**
	 * Copy file.
	 * 
	 * @param src
	 *            the src
	 * @param dst
	 *            the dst
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void copyFile(File src, File dst) throws IOException {

		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dst);

		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}

	/**
	 * Creates a file list.
	 * 
	 * @param start
	 *            the start folder/file where to look for the files
	 * @param arrayList
	 *            the array list to which we add the found files
	 * @param extensions
	 *            the extensions of the files we want to add
	 */
	private void createFileListFromExtensions(File start, ArrayList<String> arrayList, ArrayList<String> extensions) {

		if (start.isDirectory()) {

			File fileList[] = start.listFiles();

			for (int index = 0; index < fileList.length; index++) {

				File file = fileList[index];

				createFileListFromExtensions(file, arrayList, extensions);
			}
		}

		String fileName = start.getName();
		String ext = (fileName.lastIndexOf(".") == -1) ? "" : fileName.substring(fileName.lastIndexOf(".") + 1,
				fileName.length());

		if (extensions.contains(ext)) {
			arrayList.add(start.getAbsolutePath());
		}
	}

	/**
	 * Gets the components that are used in the XIDE page.
	 * 
	 * @param uc
	 *            the used components array list to which we add the component
	 *            names
	 * 
	 * @return the used components
	 * 
	 * @throws PublishException
	 *             the publish exception
	 */
	private ArrayList<String> getUsedComponents(ArrayList<String> uc) throws PublishException {

		File xmlPage = new File(this.appSourcePath + this.pageName + ".xml");
		Document doc = createDocumentFromFile(xmlPage);
		NodeList componentNodeList = doc.getDocumentElement().getElementsByTagName("template:call-component");

		for (int i = 0; i < componentNodeList.getLength(); i++) {

			Element componentNode = (Element) componentNodeList.item(i);
			NamedNodeMap componentAttributes = componentNode.getAttributes();
			Node componentNameNode = componentAttributes.getNamedItem("name");

			if (componentNameNode != null) {
				String name = componentNameNode.getNodeValue();

				if (!uc.contains(name)) {
					uc.add(name);
				}
			}
		}

		return uc;
	}

	/**
	 * Creates a document from a file.
	 * 
	 * @param file
	 *            the file
	 * 
	 * @return the document
	 * 
	 * @throws PublishException
	 *             the publish exception
	 */
	private Document createDocumentFromFile(File file) throws PublishException {

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

			loggerInstance.log(Level.ERROR,
					"SAXException occured when creating a document from xml file. \n" + e.getMessage());

			throw new PublishException("SAXException occured when creating a document from xml file. \n"
					+ e.getMessage());

		} catch (IOException e) {

			loggerInstance.log(Level.ERROR,
					"IOException occured when creating a document from xml file. \n" + e.getMessage());

			throw new PublishException("IOException occured when creating a document from xml file. \n"
					+ e.getMessage());

		}

		return doc;
	}

	/**
	 * Writes a document to a file.
	 * 
	 * @param file
	 *            the file
	 * @param doc
	 *            the doc
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws TransformerFactoryConfigurationError
	 *             the transformer factory configuration error
	 * @throws TransformerException
	 *             the transformer exception
	 */
	private void writeDocumentToFile(File file, Document doc, boolean addDoctype) throws IOException,
			TransformerFactoryConfigurationError, TransformerException {

		DOMSource source = new DOMSource(doc);
		Result result = new StreamResult(file);
		Transformer trans = TransformerFactory.newInstance().newTransformer();

		if (addDoctype) {
			trans.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "xformsdb1.dtd");
		}
		trans.transform(source, result);

	}

	/**
	 * Removes this component from the file system and SQL database.
	 */
	public static void deleteComponentFromFileSystem(String name) {

		// Delete from file system
		PublishHandler.deleteDir(new File(Config.getComponentsPath() + name));
	}
}