package fi.tkk.media.xide.server.transformation.transformers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
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
import fi.tkk.tml.xformsdb.xml.sax.XFormsDBHandler;

/**
 * This class sets the parameters for a given component.
 * 
 * @author Sebastian Monte
 * @version 1.0
 */

public class ComponentTransformer {

	/** The component name. */
	private final String componentName;

	/** The component id. */
	private final String componentId;

	/** The user name that's publishing the application. */
	private final String userName;

	/** The XML tag that specifies the component parameters. */
	private final String parameterTag = "template:param";

	/** Parameters passed from the page */
	private final Hashtable<String, String> parametersFromPage;

	/** The component file. */
	private final File componentFile;

	/** The component file copy. */
	private final File componentFileCopy;

	/** Stores unique ID's for xforms and xformsdb elements that requires them. */
	private Hashtable<String, String> xFormsIds = new Hashtable<String, String>();

	/** The component's xml document. */
	private Document componentXML;

	/** The component's xml document as string format. */
	private String componentXmlAsString;

	/** Counter for assigning unique ids */
	private int idCounter;

	private static Logger loggerInstance = Logger.getLogger(ComponentTransformer.class);

	/**
	 * Instantiates a new component parameter transformer.
	 * 
	 * @param componentName
	 *            the component name
	 * @param componentId
	 *            the component id
	 * @param componentWithParams
	 *            the component with params
	 * @param userName
	 *            the user name
	 * 
	 * @throws PublishException
	 *             the publish exception
	 */
	public ComponentTransformer(String componentName, String componentId,
			Hashtable<String, String> componentWithParams, String userName, int idCounter) throws PublishException {

		loggerInstance.log(Level.DEBUG, "Constuctor called.");

		this.componentName = componentName;
		this.componentId = componentId;
		this.parametersFromPage = componentWithParams;
		this.userName = userName;
		this.idCounter = idCounter;

		// Open the component file(s)
		this.componentFile = new File(Config.getComponentsPath() + this.componentName + "/" + this.componentName
				+ ".xml");
		this.componentFileCopy = new File(Config.getTempFilesPath() + this.userName + "_" + this.componentId + ".xml");

		try {

			init();
			setParameters();
			handleUniqueIDs();
			writeComponentStringToFile();

		} catch (IOException e) {

			loggerInstance.log(Level.ERROR, "IOException occured when transforming a component. \n" + e.getMessage());

			throw new PublishException("IOException occured when transforming a component. \n" + e.getMessage());

		} catch (TransformerException e) {

			loggerInstance.log(Level.ERROR,
					"TransformerException occured when transforming a component. \n" + e.getMessage());

			throw new PublishException("TransformerException occured when transforming a component. \n"
					+ e.getMessage());

		} catch (ParserConfigurationException e) {

			loggerInstance.log(Level.ERROR,
					"ParserConfigurationException occured when transforming a component. \n" + e.getMessage());

			throw new PublishException("ParserConfigurationException occured when transforming a component. \n"
					+ e.getMessage());

		} catch (SAXException e) {

			loggerInstance.log(Level.ERROR, "SAXException occured when transforming a component. \n" + e.getMessage());

			throw new PublishException("SAXException occured when transforming a component. \n" + e.getMessage());

		} 

		loggerInstance.log(Level.DEBUG, "Component\"" + componentName + "\" transformation done!");

	}

	/**
	 * Creates a copy of the original component file and renames it.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ParserConfigurationException
	 *             the parser configuration exception
	 * @throws SAXException
	 *             the SAX exception
	 */
	private void init() throws IOException, ParserConfigurationException, SAXException {

		copyFile(this.componentFile, this.componentFileCopy);
		this.componentXML = createComponentXMLDocument(this.componentFileCopy);

	}

	/**
	 * Copies a single file.
	 * 
	 * @param src
	 *            The source file
	 * @param dst
	 *            The destination file
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
	 * Writes the component's string format to the component's file copy.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void writeComponentStringToFile() throws IOException {

		BufferedWriter out = new BufferedWriter(new FileWriter(this.componentFileCopy));
		out.write(this.componentXmlAsString);
		out.close();

		loggerInstance.log(Level.DEBUG, "Wrote component xml document to file.");

	}

	/**
	 * Starts the process of creating unique IDs for the necessary XML elements.
	 */
	private void handleUniqueIDs() {

		Node root = this.componentXML.getDocumentElement();
		NodeList nodes = root.getChildNodes();

		ArrayList<String> visitedNodes = new ArrayList<String>();
		createUniqueIDs(nodes, visitedNodes);
		assignUniqueIDs();
	}

	/**
	 * Sets the parameters that are given to the component.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws TransformerException
	 *             The transformer exception
	 * @throws PublishException
	 */
	private void setParameters() throws IOException, TransformerException, PublishException {

		loggerInstance.log(Level.DEBUG, "Starting to write parameters for component \"" + this.componentName + "\"");

		addComponentParametersFromPageToDocument(this.componentXML);

		Hashtable<String, ArrayList<String>> allParameters = getAllParameters(this.componentXML);

		this.componentXmlAsString = getStringFromDocument(this.componentXML);

		Enumeration<String> e = allParameters.keys();

		while (e.hasMoreElements()) {

			String key = e.nextElement();
			ArrayList<String> valueAndType = allParameters.get(key);

			String value = valueAndType.get(0);
			// String type = valueAndType.get(1);

			// Replace all variables with the format $variable
			String targetString1 = "$" + key;

			this.componentXmlAsString = this.componentXmlAsString.replace(targetString1, value);
		}
	}

	/**
	 * Creates <template:param> elements to the document for the parameters that
	 * were passed from the page.
	 * 
	 * @param doc
	 *            The document where to add the parameters
	 */
	private void addComponentParametersFromPageToDocument(Document doc) {

		NodeList parameterNodeList = doc.getDocumentElement().getElementsByTagName(this.parameterTag);

		Enumeration<String> e = this.parametersFromPage.keys();

		while (e.hasMoreElements()) {

			// Get the key and value

			String name = e.nextElement();
			String value = this.parametersFromPage.get(name);
			String type = "text";

			// Remove the components default parameter from the
			// document, if the same parameter is passed from the page.
			for (int i = 0; i < parameterNodeList.getLength(); i++) {

				Node paramNode = parameterNodeList.item(i);
				NamedNodeMap paramAttributes = paramNode.getAttributes();
				Node paramNameNode = paramAttributes.getNamedItem("name");
				String paramName = paramNameNode.getNodeValue().trim();

				if (paramName.equals(name)) {

					Node parameterTypeNode = paramAttributes.getNamedItem("type");

					if (parameterTypeNode != null) {
						type = parameterTypeNode.getNodeValue();
					}

					paramNode.getParentNode().removeChild(paramNode);
				}

			}

			Element newParam = doc.createElement(this.parameterTag);

			newParam.setAttribute("name", name);
			newParam.setAttribute("type", type);
			newParam.appendChild(doc.createTextNode(value));

			loggerInstance.log(Level.DEBUG,
					"Added a template parameter from page, Name: " + name + ", Value: " + value + " , Type: " + type);

			Element rootElement = doc.getDocumentElement();
			rootElement.appendChild(newParam);
		}
	}

	/**
	 * Gets all the components parameters.
	 * 
	 * @param doc
	 *            XML document where to look for the parameters.
	 * 
	 * @return Hashtable that's key is the parameter's name. Value is an
	 *         ArrayList that stores the value at index 0 and type at index 1.
	 * @throws PublishException
	 */
	private Hashtable<String, ArrayList<String>> getAllParameters(Document doc) throws PublishException {

		loggerInstance.log(Level.DEBUG, "Getting all component parameters.");

		Hashtable<String, ArrayList<String>> allParameters = new Hashtable<String, ArrayList<String>>();
		NodeList componentParametersNodeList = doc.getDocumentElement().getElementsByTagName(this.parameterTag);

		for (int i = 0; i < componentParametersNodeList.getLength(); i++) {

			ArrayList<String> valueAndType = new ArrayList<String>();
			String parameterName;
			String parameterValue = "";
			String parameterType = "text";

			Node parameterNode = componentParametersNodeList.item(i);
			NamedNodeMap parameterAttributes = parameterNode.getAttributes();

			parameterName = parameterAttributes.getNamedItem("name").getNodeValue();

			Node parameterTypeNode = parameterAttributes.getNamedItem("type");
			if (parameterTypeNode != null) {
				parameterType = parameterTypeNode.getNodeValue();
			}

			Node parameterValueNode = parameterNode.getLastChild();

			// Make sure that some parameter value is available
			if (parameterValueNode != null) {

				// We have a simple value assigned
				if (parameterValueNode.getNodeType() == Node.TEXT_NODE) {
					parameterValue = parameterValueNode.getNodeValue();
				}

				// We have options for parameter value
				else {
					NodeList parameterOptionsNodeList = parameterNode.getChildNodes();
					parameterValue = getParameterValueFromOptions(parameterOptionsNodeList);
				}
			}

			// There is no parameter value, throw an exception
			/**
			 * else { throw new PublishException(
			 * "Parameter value is missing for parameter " + parameterName);
			 * 
			 * }
			 */
			valueAndType.add(parameterValue);
			valueAndType.add(parameterType);
			allParameters.put(parameterName, valueAndType);
		}

		return allParameters;

	}

	private String getParameterValueFromOptions(NodeList optionsNodeList) {

		loggerInstance.log(Level.DEBUG, "Getting parameter value from options.");

		String result = "";

		for (int i = 0; i < optionsNodeList.getLength(); i++) {

			Node optionNode = optionsNodeList.item(i);
			NamedNodeMap optionNodeAttributes = optionNode.getAttributes();

			Node selectedAttribute = optionNodeAttributes.getNamedItem("select");

			// Check if we want to use this option (selected="true")
			if (selectedAttribute != null && selectedAttribute.getNodeValue().equals("true")) {

				Node valueNode = optionNode.getLastChild();

				if (valueNode != null) {

					result = valueNode.getNodeValue();
					return result;
				}
			}
		}

		// No selected value was found so return the first option's value
		Node firstOptionNode = optionsNodeList.item(0);
		Node firstValueNode = firstOptionNode.getLastChild();

		if (firstValueNode != null) {
			result = firstValueNode.getNodeValue();
		}

		return result;
	}

	/**
	 * Adds unique ID's for the necessary elements. Unique ID acts as a key in
	 * the HashTable and value is the original ID of the element.
	 * 
	 * @param nodes
	 *            Nodes of the XML document
	 */
	private void createUniqueIDs(NodeList nodes, ArrayList<String> visitedNodes) {

		for (int i = 0; i < nodes.getLength(); i++) {

			Node childNode = nodes.item(i);
			NamedNodeMap attributes = childNode.getAttributes();

			if (attributes != null) {

				Node idNode = attributes.getNamedItem("id");

				if (idNode != null
						&& (childNode.getNamespaceURI().equals("http://www.w3.org/2002/xforms") || childNode
								.getNamespaceURI().equals("http://www.tml.tkk.fi/2007/xformsdb"))) {

					String origID = idNode.getNodeValue();

					if (!visitedNodes.contains(origID)) {
						String uniqueID = Integer.toString(this.idCounter);
						this.xFormsIds.put(origID, uniqueID);
						visitedNodes.add(origID);
						this.idCounter++;
					}
				}
			}
			NodeList children = childNode.getChildNodes();
			if (children != null) {
				createUniqueIDs(children, visitedNodes);
			}

		}
	}

	/**
	 * Creates a XML document from a file.
	 * 
	 * @param file
	 *            The file that contains XML
	 * 
	 * @return The generated XML document
	 * 
	 * @throws ParserConfigurationException
	 *             the parser configuration exception
	 * @throws SAXException
	 *             the SAX exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private Document createComponentXMLDocument(File file) throws ParserConfigurationException, SAXException,
			IOException {

		loggerInstance.log(Level.DEBUG, "Creating a XML document from a file \"" + file.getName() + "\"");

		Document doc = null;

		XFormsDBHandler xformsdbHandler = new XFormsDBHandler();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		dbf.setExpandEntityReferences(false);
		DocumentBuilder db = dbf.newDocumentBuilder();
		db.setEntityResolver(xformsdbHandler);
		doc = db.parse(file);

		return doc;
	}

	/**
	 * Get a string format of a given document.
	 * 
	 * @param doc
	 *            XML document from where the string is generated.
	 * 
	 * @return The generated string format of the document
	 * 
	 * @throws TransformerException
	 *             the transformer exception
	 */
	private String getStringFromDocument(Document doc) throws TransformerException {

		loggerInstance.log(Level.DEBUG, "Getting string format from a document.");

		DOMSource domSource = new DOMSource(doc);
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.transform(domSource, result);

		return writer.toString();

	}

	/**
	 * Assigns unique IDs to xforms(db) and css elements.
	 */
	private void assignUniqueIDs() {

		loggerInstance.log(Level.DEBUG, "Assigning unique IDs to xforms and xformsdb elements.");

		/** XForms(db) elements */
		Enumeration<String> xFormsEnumeration = this.xFormsIds.keys();

		while (xFormsEnumeration.hasMoreElements()) {

			String origID = xFormsEnumeration.nextElement();
			String uniqueID = this.xFormsIds.get(origID);

			this.componentXmlAsString = this.componentXmlAsString.replaceAll("[\\s']+" + origID + "[\\s']+", "'"
					+ origID + "_" + uniqueID + "'");

			this.componentXmlAsString = this.componentXmlAsString.replaceAll("[\\s\"]+" + origID + "[\\s\"]+", "\""
					+ origID + "_" + uniqueID + "\" ");
		}
	}

	/**
	 * Gets the id counter.
	 * 
	 * @return the id counter
	 */
	public int getIdCounter() {

		return this.idCounter;
	}

	/**
	 * Gets the component file copy.
	 * 
	 * @return The copy of the component's file
	 */
	public File getComponentFileCopy() {

		return this.componentFileCopy;
	}
}
