package fi.tkk.media.xide.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import fi.tkk.media.xide.server.transformation.filehandlers.WidgetMaker;
import fi.tkk.tml.xformsdb.core.Constants;


/**
 * Convenience class for handling XML documents.
 * 
 * 
 * @author Markku Laine
 * @version 1.0	 Created on August 04, 2008
 */
public class XMLUtils {
	
	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static  Logger loggerInstance = Logger.getLogger(XMLUtils.class);
	private static Logger getLogger(){
		if (loggerInstance == null) {
			loggerInstance = Logger.getLogger(XMLUtils.class);
		}
		return loggerInstance;
	}
	
	// PRIVATE CONSTRUCTORS
	/**
	 * Prevent object construction outside of this class.
	 */
	private XMLUtils() {
		getLogger().log( Level.DEBUG, "Constructor has been called." );
	}
	
	

	// PUBLIC STATIC METHODS
	/**
	 * Retrieve the <code>Node</code> as a string.
	 * The result will be encoded using the given encoding.
	 * 
	 * 
	 * @param node				The node.
	 * @param encoding			The character encoding to be used.
	 * @return xmlString		The node as a string.
	 * @throws XMLException		XML exception.
	 */
	public static String getNodeAsString( Node node, String encoding ) throws XMLException {
		getLogger().log( Level.DEBUG, "Method has been called." );
		TransformerFactory tf	= null;
		Transformer t			= null;
		OutputStream baos		= null;
		OutputStream bos		= null;
		Writer osw				= null;
		Result result			= null;
		Source source			= null;
		String xmlString		= null;
		
		
		if ( node != null ) {
			getLogger().log( Level.DEBUG, "Node is okay." );

			try {
				// Set up a transformer
				tf				= TransformerFactory.newInstance();
				t				= tf.newTransformer();
				//t.setOutputProperty( OutputKeys.OMIT_XML_DECLARATION, Constants.OUTPUT_XML_OMIT_XML_DECLARATION_NO );
				t.setOutputProperty( OutputKeys.METHOD, Constants.OUTPUT_XML_METHOD_XML );
				t.setOutputProperty( OutputKeys.VERSION, Constants.OUTPUT_XML_VERSION_1_0 );
				t.setOutputProperty( OutputKeys.ENCODING, encoding );
				t.setOutputProperty( OutputKeys.INDENT, Constants.OUTPUT_XML_INDENT_YES );
				//t.setOutputProperty( OutputKeys.STANDALONE, "no" );
				//t.setOutputProperty( OutputKeys.MEDIA_TYPE, "text/xml" );
	
				// Create string from node
				baos			= new ByteArrayOutputStream();
				bos				= new BufferedOutputStream( baos );
				osw				= new OutputStreamWriter( bos, encoding );
				result			= new StreamResult( osw );
				source			= new DOMSource( node );
	
				// Transform
				t.transform( source, result );
				xmlString		= baos.toString();
				getLogger().log( Level.DEBUG, "XML string has been created." );
			} catch ( Exception ex ) {
				throw new XMLException( "Failed to get the node as a string. " + ex.getMessage(), ex );
			} finally {
				try {
					if ( baos != null ) {
						baos.close();
					}
					if ( bos != null ) {
						bos.close();
					}
					if ( osw != null ) {
						osw.close();
					}
				} catch ( IOException ioex ) {
					getLogger().log( Level.ERROR, "Failed to close the streams.", ioex );					
				}
			}
		}
		else {
			throw new XMLException( "Node is not okay." );
		}
		
		
		return xmlString;
	}


	/**
	 * Retrieve the <code>InputStream</code> as a string.
	 * The result will be encoded using the given encoding.
	 * 
	 * 
	 * @param inputStream		The input stream.
	 * @param encoding			The character encoding to be used.
	 * @return xmlString		The input stream as a string.
	 * @throws XMLException		XML exception.
	 */
	public static String getInputStreamAsString( InputStream inputStream, String encoding ) throws XMLException {
		getLogger().log( Level.DEBUG, "Method has been called." );
		TransformerFactory tf	= null;
		Transformer t			= null;
		OutputStream baos		= null;
		OutputStream bos		= null;
		Writer osw				= null;
		Result result			= null;
		Source source			= null;
		String xmlString		= null;
		
		
		if ( inputStream != null ) {
			getLogger().log( Level.DEBUG, "Input stream is okay." );

			try {
				// Set up a transformer
				tf				= TransformerFactory.newInstance();
				t				= tf.newTransformer();
				//t.setOutputProperty( OutputKeys.OMIT_XML_DECLARATION, Constants.OUTPUT_XML_OMIT_XML_DECLARATION_NO );
				t.setOutputProperty( OutputKeys.METHOD, Constants.OUTPUT_XML_METHOD_XML );
				t.setOutputProperty( OutputKeys.VERSION, Constants.OUTPUT_XML_VERSION_1_0 );
				t.setOutputProperty( OutputKeys.ENCODING, encoding );
				t.setOutputProperty( OutputKeys.INDENT, Constants.OUTPUT_XML_INDENT_YES );
				//t.setOutputProperty( OutputKeys.STANDALONE, "no" );
				//t.setOutputProperty( OutputKeys.MEDIA_TYPE, "text/xml" );
	
				// Create string from input stream
				baos			= new ByteArrayOutputStream();
				bos				= new BufferedOutputStream( baos );
				osw				= new OutputStreamWriter( bos, encoding );
				result			= new StreamResult( osw );
				source			= new StreamSource( inputStream );
	
				// Transform
				t.transform( source, result );
				xmlString		= baos.toString();
				getLogger().log( Level.DEBUG, "XML string has been created." );
			} catch ( Exception ex ) {
				throw new XMLException( "Failed to get the input stream as a string. " + ex.getMessage(), ex );
			} finally {
				try {
					if ( inputStream != null ) {
						inputStream.close();
					}
					if ( baos != null ) {
						baos.close();
					}
					if ( bos != null ) {
						bos.close();
					}
					if ( osw != null ) {
						osw.close();
					}
				} catch ( IOException ioex ) {
					getLogger().log( Level.ERROR, "Failed to close the streams.", ioex );
				}
			}
		}
		else {
			throw new XMLException( "Input stream is not okay." );
		}
		
		
		return xmlString;
	}
	

	/**
	 * Filter the XML declaration.
	 * 
	 * 
	 * @param xmlString				The XML as a string.
	 * @return filteredXMLString	The filtered XML as a string.
	 * @throws XMLException			XML exception.
	 */
	public static String filterXMLDeclaration( String xmlString ) throws XMLException {
		getLogger().log( Level.DEBUG, "Method has been called." );
		String filteredXMLString	= null;
		String xmlDeclaration		= "<?xml ";
		
		
		if ( xmlString != null ) {
			getLogger().log( Level.DEBUG, "XML is okay." );
			
			// Start with XML declaration
			if ( xmlString.startsWith( xmlDeclaration, 0 ) ) {
				// Find the start tag of next element
				int indexOfNextElement	= xmlString.indexOf( "<", xmlDeclaration.length() );
				if ( indexOfNextElement != -1 ) {
					filteredXMLString	= xmlString.substring( indexOfNextElement );
				}
				else {
					throw new XMLException( "XML is not okay." );					
				}
			}
			// Original XML string
			else {
				filteredXMLString		= xmlString;
			}
		}
		else {
			throw new XMLException( "XML is not okay." );
		}
		
		
		return filteredXMLString;
	}
}