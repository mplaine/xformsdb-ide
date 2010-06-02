package fi.tkk.media.xide.server.transformation.transformers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

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
import fi.tkk.media.xide.server.SearchServiceImpl;
import fi.tkk.tml.xformsdb.xml.sax.XFormsDBHandler;

/**
 * This class transforms an xml file (.xml) into an xformsdb format (.xformsdb).
 * 
 * @author Sebastian Monte
 * @version 1.0
 */

public class XideToXFormsDBTransformer {

	/** The xml file to be transformed. */
	private final File						xmlFile;

	/** The xslt file. */
	// private final File xsltFile;
	/** The xformsdb file that is generated. */
	private final File						xFormsDBFile;

	/** The user name. */
	private final String					userName;

	private static  Logger loggerInstance = Logger.getLogger(XideToXFormsDBTransformer.class);

	/**
	 * Instantiates a new xide index transformer.
	 * 
	 * @param xmlFile
	 *            the xml file
	 * @param targetFile
	 *            the target file
	 * @param userName
	 *            the user name
	 * 
	 * @throws PublishException
	 *             the publish exception
	 */
	public XideToXFormsDBTransformer(File xmlFile, File targetFile, String userName) throws PublishException {

		loggerInstance.log(Level.DEBUG, "Constuctor called.");

		this.xmlFile = xmlFile;
		this.xFormsDBFile = targetFile;
		this.userName = userName;

		try {

			transform(); // Make the transformation

			setParameters(); // Set the attribute parameters

			loggerInstance.log(Level.DEBUG, "Page transformation for \"" + this.xmlFile.getName() + "\"is complete.");

		} catch (TransformerException e) {

			loggerInstance.log(Level.ERROR, "TransformerException occured when transforming the xml document. \n" + e.getMessage());

			throw new PublishException("TransformerException occured when transforming the xml document. \n" + e.getMessage());

		} catch (IOException e) {

			loggerInstance.log(Level.ERROR, "IOException occured when transforming the xml document. \n" + e.getMessage());

			throw new PublishException("IOException occured when transforming the xml document. \n" + e.getMessage());

		} catch (ParserConfigurationException e) {

			loggerInstance.log(Level.ERROR, "ParserConfigurationException occured when transforming the xml document. \n"
					+ e.getMessage());

			throw new PublishException("ParserConfigurationException occured when transforming the xml document. \n"
					+ e.getMessage());

		} catch (SAXException e) {

			loggerInstance.log(Level.ERROR, "SAXException occured when transforming the xml document. \n" + e.getMessage());

			throw new PublishException("SAXException occured when transforming the xml document. \n" + e.getMessage());
		}

	}

	/**
	 * Transforms the xml file to xformsdb format.
	 * 
	 * @throws TransformerException
	 *             the transformer exception
	 * @throws PublishException
	 */
	private void transform() throws TransformerException, PublishException {

		loggerInstance.log(Level.DEBUG, "Starting the transformation process for \"" + this.xmlFile.getName() + "\"");

		StreamSource xmlSource = new StreamSource(this.xmlFile);
		
		InputStream inputStream =
				Thread.currentThread().getContextClassLoader().getResourceAsStream(Config.getXideToXFormsDBPath());
		StreamSource xsltSource = new StreamSource(inputStream);
		
		StreamResult result = new StreamResult(this.xFormsDBFile);

		TransformerFactory transFact = TransformerFactory.newInstance();
		transFact.setURIResolver(new XSLTURIResolver());

		Transformer trans = transFact.newTransformer(xsltSource);
		trans.setParameter("componentFiles", Config.getTempFilesPath());
		trans.setParameter("userName", this.userName);
		trans.setParameter("useTheme", useTheme());
		trans.transform(xmlSource, result);

	}

	private String useTheme() throws PublishException {

		String result = "";

		Document doc = createDocumentFromFile(this.xmlFile);
		NodeList linkNodeList = doc.getDocumentElement().getElementsByTagName("link");

		for (int i = 0; i < linkNodeList.getLength(); i++) {

			Element linkNode = (Element) linkNodeList.item(i);
			NamedNodeMap linkAttributes = linkNode.getAttributes();

			if (linkAttributes != null) {

				Node themeNode = linkAttributes.getNamedItem("template:theme");

				if (themeNode != null) {

					result = themeNode.getNodeValue();
				}
			}
		}
		return result;
	}

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

			loggerInstance.log(Level.ERROR, "ParserConfigurationException occured when creating a document from xml file. \n"
					+ e.getMessage());

			throw new PublishException("ParserConfigurationException occured when creating a document from xml file. \n"
					+ e.getMessage());

		} catch (SAXException e) {

			loggerInstance.log(Level.ERROR, "SAXException occured when creating a document from xml file. \n" + e.getMessage());

			throw new PublishException("SAXException occured when creating a document from xml file. \n" + e.getMessage());

		} catch (IOException e) {

			loggerInstance.log(Level.ERROR, "IOException occured when creating a document from xml file. \n" + e.getMessage());

			throw new PublishException("IOException occured when creating a document from xml file. \n" + e.getMessage());

		}

		return doc;
	}

	/**
	 * Sets the parameters of the page.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ParserConfigurationException
	 *             the parser configuration exception
	 * @throws SAXException
	 *             the SAX exception
	 * @throws TransformerException
	 *             the transformer exception
	 */
	private void setParameters() throws IOException, ParserConfigurationException, SAXException, TransformerException {

		loggerInstance.log(Level.DEBUG, "Starting to handle the parameters for \"" + this.xmlFile.getName() + "\"");

		Document generatedDocument = getGeneratedXMLDocument();

		Hashtable<String, ArrayList<String>> parameters = getParameters(generatedDocument);

		String xmlString = readFileAsString(this.xFormsDBFile.getAbsolutePath());

		Enumeration<String> e = parameters.keys();

		while (e.hasMoreElements()) {

			String key = e.nextElement();

			ArrayList<String> valueAndType = parameters.get(key);

			String value = valueAndType.get(0);
			// String type = valueAndType.get(1);

			// Replace all variables with the format $variable

			String targetString1 = "$" + key;
			xmlString = xmlString.replace(targetString1, value);
		}

		// Write the data to the xml file
		SearchServiceImpl.writeStringToFile(this.xFormsDBFile.getAbsolutePath(), xmlString);

	}

	/**
	 * Gets the parameters specified in the "<template:param>" elements.
	 * 
	 * @param doc
	 *            the doc
	 * 
	 * @return Hashtable that's key is the parameter's name. Value is an
	 *         ArrayList that stores the value at index 0 and type at index 1.
	 */
	private Hashtable<String, ArrayList<String>> getParameters(Document doc) {

		loggerInstance.log(Level.DEBUG, "Getting the parameters from the generated document");

		Hashtable<String, ArrayList<String>> parameters = new Hashtable<String, ArrayList<String>>();
		NodeList parameterNodeList = doc.getDocumentElement().getElementsByTagName("template:param");

		for (int i = 0; i < parameterNodeList.getLength(); i++) {

			ArrayList<String> valueAndType = new ArrayList<String>();

			Node parameterNode = parameterNodeList.item(i);
			NamedNodeMap parameterAttributes = parameterNode.getAttributes();
			String parameterName = parameterAttributes.getNamedItem("name").getNodeValue();
			String parameterType = parameterAttributes.getNamedItem("type").getNodeValue();
			String parameterValue = parameterNode.getLastChild().getNodeValue();

			valueAndType.add(parameterValue);
			valueAndType.add(parameterType);

			parameters.put(parameterName, valueAndType);

		}

		// Remove the "template:param" nodes from the document

		int i = 0;

		while ((parameterNodeList.item(i)) != null) {

			Node parameterNode = parameterNodeList.item(i);
			Node p = parameterNode.getParentNode();
			p.removeChild(parameterNode);
		}

		return parameters;

	}

	/**
	 * Gets the generated xformsdb document.
	 * 
	 * @return the generated xformsdb document
	 * 
	 * @throws ParserConfigurationException
	 *             the parser configuration exception
	 * @throws SAXException
	 *             the SAX exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private Document getGeneratedXMLDocument() throws ParserConfigurationException, SAXException, IOException {

		loggerInstance.log(Level.DEBUG, "Getting the generated document \"" + this.xFormsDBFile.getName() + "\"");

		Document doc = null;

		XFormsDBHandler xformsdbHandler = new XFormsDBHandler();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		dbf.setIgnoringElementContentWhitespace(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		db.setEntityResolver(xformsdbHandler);
		doc = db.parse(this.xFormsDBFile);

		return doc;
	}

	/**
	 * @param filePath
	 *            the name of the file to open.
	 */
	private static String readFileAsString(String filePath) throws java.io.IOException {

		loggerInstance.log(Level.DEBUG, "Getting a string from a filepath.");

		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		return fileData.toString();
	}

}
