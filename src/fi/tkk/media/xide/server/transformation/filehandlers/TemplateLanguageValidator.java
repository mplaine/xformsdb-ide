package fi.tkk.media.xide.server.transformation.filehandlers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import fi.tkk.media.xide.client.Server.RPC.XIDEException;
import fi.tkk.media.xide.client.fs.XIDEFile;
import fi.tkk.media.xide.server.Config;
import fi.tkk.tml.xformsdb.xml.sax.XFormsDBHandler;

public class TemplateLanguageValidator {

	public TemplateLanguageValidator(File xmlFile, int type) throws ParserConfigurationException, IOException,
			SAXException {

		if (type == XIDEFile.VALIDATE_PAGE_SOURCE) {
			validatePage(xmlFile);
		} else if (type == XIDEFile.VALIDATE_TEMPLATE_SOURCE) {
			validateTemplate(xmlFile);
		}

	}

	private void validatePage(File xmlFile) throws ParserConfigurationException, IOException, SAXException {

		DocumentBuilderFactory factory = new org.apache.xerces.jaxp.DocumentBuilderFactoryImpl();

		factory.setFeature("http://apache.org/xml/features/validation/schema", true);
		factory.setFeature("http://xml.org/sax/features/validation", true);
		factory.setFeature("http://xml.org/sax/features/namespaces", true);
		factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage",
				"http://www.w3.org/2001/XMLSchema");

		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(
				Config.getPageSchema());
		factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource", inputStream);

		factory.setNamespaceAware(true);

		DocumentBuilder builder = factory.newDocumentBuilder();
		XFormsDBHandler xformsdbHandler = new XFormsDBHandler();
		builder.setErrorHandler(new SaxErrorHandler());
		builder.setEntityResolver(xformsdbHandler);
		builder.parse(xmlFile);
	}

	private void validateTemplate(File xmlFile) throws ParserConfigurationException, IOException, SAXException {

		DocumentBuilderFactory factory = new org.apache.xerces.jaxp.DocumentBuilderFactoryImpl();

		factory.setFeature("http://apache.org/xml/features/validation/schema", true);
		factory.setFeature("http://xml.org/sax/features/validation", true);
		factory.setFeature("http://xml.org/sax/features/namespaces", true);
		factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage",
				"http://www.w3.org/2001/XMLSchema");

		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(
				Config.getTemplateSchema());

		factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource", inputStream);

		factory.setNamespaceAware(true);

		DocumentBuilder builder = factory.newDocumentBuilder();
		XFormsDBHandler xformsdbHandler = new XFormsDBHandler();

		builder.setErrorHandler(new SaxErrorHandler());

		builder.setEntityResolver(xformsdbHandler);
		builder.parse(xmlFile);
	}
}

class SaxErrorHandler implements ErrorHandler {

	public void error(SAXParseException e) throws SAXException {

		int line = e.getLineNumber();
		throw new SAXException("Line " + line + ": " + e.getMessage());
	}

	public void fatalError(SAXParseException e) throws SAXException {

		int line = e.getLineNumber();
		throw new SAXException("Line " + line + ": " + e.getMessage());
	}

	public void warning(SAXParseException e) throws SAXException {

		int line = e.getLineNumber();
		throw new SAXException("Line " + line + ": " + e.getMessage());
	}

}
