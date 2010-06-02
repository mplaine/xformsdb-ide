package fi.tkk.media.xide.util;


/**
 * XML exceptions.
 *  
 *
 * @author Markku Laine
 * @version 1.0	 Created on December 20, 2006
 */
public class XMLException extends Exception {

	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final long serialVersionUID	= 1;
	
	
	// PUBLIC CONSTRUCTORS
	public XMLException() {
		super();
	}
	public XMLException( String message ) {
		super( message );
	}
	public XMLException( String message, Throwable cause ) {
		super( message, cause );
	}
	public XMLException( Throwable cause ) {
		super( cause );
	}
}