package fi.tkk.tml.xformsdb.xml.sax;

import org.xml.sax.Locator;
import org.xml.sax.SAXParseException;


/**
 * Encapsulate an XML parse fatal error.
 * 
 * 
 * @author Markku Laine
 * @version 1.0	 Created on February 8, 2007
 */
public class SAXParseFatalErrorException extends SAXParseException {



	// PUBLIC STATIC FINAL VARIABLES
	public static final long serialVersionUID	= 1;
	
	
	// PUBLIC CONSTRUCTORS
	public SAXParseFatalErrorException( String message, Locator locator ) {
		super( message, locator );
	}
	public SAXParseFatalErrorException( String message, Locator locator, Exception e ) {
		super( message, locator, e );
	}
	public SAXParseFatalErrorException( String message, String publicId, String systemId, int lineNumber, int columnNumber ) {
		super( message, publicId, systemId, lineNumber, columnNumber );
	}
	public SAXParseFatalErrorException( String message, String publicId, String systemId, int lineNumber, int columnNumber, Exception e ) {
		super( message, publicId, systemId, lineNumber, columnNumber, e );
	}
}