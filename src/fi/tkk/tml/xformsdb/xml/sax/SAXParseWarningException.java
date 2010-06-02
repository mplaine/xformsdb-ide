package fi.tkk.tml.xformsdb.xml.sax;

import org.xml.sax.Locator;
import org.xml.sax.SAXParseException;


/**
 * Encapsulate an XML parse warning.
 * 
 * 
 * @author Markku Laine
 * @version 1.0	 Created on February 8, 2007
 */
public class SAXParseWarningException extends SAXParseException {



	// PUBLIC STATIC FINAL VARIABLES
	public static final long serialVersionUID	= 1;
	
	
	// PUBLIC CONSTRUCTORS
	public SAXParseWarningException( String message, Locator locator ) {
		super( message, locator );
	}
	public SAXParseWarningException( String message, Locator locator, Exception e ) {
		super( message, locator, e );
	}
	public SAXParseWarningException( String message, String publicId, String systemId, int lineNumber, int columnNumber ) {
		super( message, publicId, systemId, lineNumber, columnNumber );
	}
	public SAXParseWarningException( String message, String publicId, String systemId, int lineNumber, int columnNumber, Exception e ) {
		super( message, publicId, systemId, lineNumber, columnNumber, e );
	}
}