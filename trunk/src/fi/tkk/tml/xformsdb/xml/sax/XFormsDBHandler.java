package fi.tkk.tml.xformsdb.xml.sax;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import fi.tkk.tml.xformsdb.core.Constants;


/**
 * Handle SAX2 events of an original XML file authored in the XFormsDB format.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on July 07, 2008
 */
public class XFormsDBHandler extends BaseHandler {
	
	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final String NAME_DTD							= "[dtd]";
	private static final String NAME_PARAMETER_ENTITY				= "%";
	//private static final String DOCTYPE_PREFIX					= "<!DOCTYPE";
	//private static final String DOCTYPE_SYSTEM					= "SYSTEM";
	//private static final String DOCTYPE_PUBLIC					= "PUBLIC";
	//private static final String DOCTYPE_SUFFIX					= ">";
	//private static final String QUOTATION_MARK					= "\"";
	
	private static  Logger loggerInstance = Logger.getLogger(XFormsDBHandler.class);
	private static Logger getLogger(){
		if (loggerInstance == null) {
			loggerInstance = Logger.getLogger(XFormsDBHandler.class);
		}
		return loggerInstance;
	}
	
	// PUBLIC STATIC FINAL VARIABLES
	// FEATURE IDS
	// Namespaces feature id (http://xml.org/sax/features/namespaces)
	public static final String NAMESPACES_FEATURE_ID				= "http://xml.org/sax/features/namespaces";	
	// Namespace prefixes feature id (http://xml.org/sax/features/namespace-prefixes)
	public static final String NAMESPACE_PREFIXES_FEATURE_ID		= "http://xml.org/sax/features/namespace-prefixes";	
		
	// PROPERTY IDS
	// Lexical handler property id (http://xml.org/sax/properties/lexical-handler)
	public static final String LEXICAL_HANDLER_PROPERTY_ID			= "http://xml.org/sax/properties/lexical-handler";


	// PRIVATE VARIABLES
	private String publicId;
	private String systemId;


	// CONSTRUCTORS
	public XFormsDBHandler() {
		super();
		getLogger().log( Level.DEBUG, "Constructor has been called." );
	}
	
	
	// PUBLIC METHODS
	// SAX2 EntityResolver2 method
	public InputSource resolveEntity( String name, String publicId, String baseURI, String systemId ) throws SAXException, IOException {
		getLogger().log( Level.DEBUG, "Method has been called." );
		InputSource inputSource				= null;

		
		// Check external entity being resolved. [dtd] = external subset, %... = parameter entity, or the name = general entity
		// Never null when invoked by a SAX2 parser, e.g. Xerces J 2.9.1
		// External subset
		if ( name != null && XFormsDBHandler.NAME_DTD.equals( name ) == true ) {
			// Trying to locate a local copy of the XFormsDB 1.0 DTD file based on the Public ID
			// Public ID for the XFormsDB 1.0 DTD file
			if ( publicId != null && Constants.DOCTYPE_XFORMSDB1_PUBLIC_ID.equals( publicId ) == true ) {
				try {
					// Retrieve a local copy of the XFormsDB 1.0 DTD file
					InputStream inputStream	= Thread.currentThread().getContextClassLoader().getResourceAsStream( Constants.DTD_XFORMSDB1_FILE_PATH );
					inputSource				= new InputSource( inputStream );
					this.publicId			= publicId;
					this.systemId			= null;
					//this.docType			= null;
					getLogger().log( Level.DEBUG, "A local copy of the XFormsDB 1.0 DTD file has been successfully retrieved based on the Public ID." );
				} catch ( Exception ex ) {
					// No action; just let the null InputSource pass through
					getLogger().log( Level.DEBUG, "Failed to retrieve a local copy of the XFormsDB 1.0 DTD file based on the Public ID." );
				}
			}
			// System ID for the XFormsDB 1.0 DTD file
			else if ( publicId == null && systemId.indexOf( Constants.DOCTYPE_XFORMSDB1_SYSTEM_ID ) != -1 ) {
				try {
					// Retrieve a local copy of the XFormsDB 1.0 DTD file
					InputStream inputStream	= Thread.currentThread().getContextClassLoader().getResourceAsStream( Constants.DTD_XFORMSDB1_FILE_PATH );
					inputSource				= new InputSource( inputStream );
					this.publicId			= null;
					this.systemId			= systemId;
					//this.docType			= null;
					getLogger().log( Level.DEBUG, "A local copy of the XFormsDB 1.0 DTD file has been successfully retrieved based on the System ID." );
				} catch ( Exception ex ) {
					// No action; just let the null InputSource pass through
					getLogger().log( Level.DEBUG, "Failed to retrieve a local copy of the XFormsDB 1.0 DTD file based on the System ID." );
				}
			}
			// Leave an "illegal" DTD file almost intact
			else {
				// Write Public ID and System ID
				if ( publicId != null && systemId != null ) {
					this.publicId			= publicId;
					this.systemId			= systemId;
					//this.docType			= XFormsDBIncludeHandler.DOCTYPE_PREFIX + " " + this.rootElement + " " + XFormsDBIncludeHandler.DOCTYPE_PUBLIC + " " + XFormsDBIncludeHandler.QUOTATION_MARK + publicId + XFormsDBIncludeHandler.QUOTATION_MARK + " " + XFormsDBIncludeHandler.QUOTATION_MARK + systemId + XFormsDBIncludeHandler.QUOTATION_MARK + XFormsDBIncludeHandler.DOCTYPE_SUFFIX;
				}
				// Write Public ID only
				else if ( publicId != null && systemId == null ) {
					this.publicId			= publicId;
					this.systemId			= null;
					//this.docType			= XFormsDBIncludeHandler.DOCTYPE_PREFIX + " " + this.rootElement + " " + XFormsDBIncludeHandler.DOCTYPE_PUBLIC + " " + XFormsDBIncludeHandler.QUOTATION_MARK + publicId + XFormsDBIncludeHandler.QUOTATION_MARK + XFormsDBIncludeHandler.DOCTYPE_SUFFIX;
				}
				// Write System ID only
				else if ( publicId == null && systemId != null ) {
					this.publicId			= null;
					this.systemId			= systemId;
					//this.docType			= XFormsDBIncludeHandler.DOCTYPE_PREFIX + " " + this.rootElement + " " + XFormsDBIncludeHandler.DOCTYPE_SYSTEM + " " + XFormsDBIncludeHandler.QUOTATION_MARK + systemId + XFormsDBIncludeHandler.QUOTATION_MARK + XFormsDBIncludeHandler.DOCTYPE_SUFFIX;
				}
				// Write nothing
				else {
					this.publicId			= null;
					this.systemId			= null;
					//this.docType			= null;
				}
			}
		}
		// Parameter entity
		else if ( name != null && name.startsWith( XFormsDBHandler.NAME_PARAMETER_ENTITY ) == true ) {
			// Trying to locate a local copy of the XHTML ENT files based on the System ID
			// System ID for the XHTML lat1 ENT file
			if ( publicId == null && systemId.indexOf( Constants.DOCTYPE_XHTML_LAT1_SYSTEM_ID ) != -1 ) {
				try {
					// Retrieve a local copy of the XHTML lat1 ENT file
					InputStream inputStream	= Thread.currentThread().getContextClassLoader().getResourceAsStream( Constants.ENT_XHTML_LAT1_FILE_PATH );
					inputSource				= new InputSource( inputStream );
					getLogger().log( Level.DEBUG, "A local copy of the XHTML lat1 ENT file has been successfully retrieved based on the System ID." );
				} catch ( Exception ex ) {
					// No action; just let the null InputSource pass through
					getLogger().log( Level.DEBUG, "Failed to retrieve a local copy of the XHTML lat1 ENT file based on the System ID." );
				}
			}
			else if ( publicId == null && systemId.indexOf( Constants.DOCTYPE_XHTML_SPECIAL_SYSTEM_ID ) != -1 ) {
				try {
					// Retrieve a local copy of the XHTML special ENT file
					InputStream inputStream	= Thread.currentThread().getContextClassLoader().getResourceAsStream( Constants.ENT_XHTML_SPECIAL_FILE_PATH );
					inputSource				= new InputSource( inputStream );
					getLogger().log( Level.DEBUG, "A local copy of the XHTML special ENT file has been successfully retrieved based on the System ID." );
				} catch ( Exception ex ) {
					// No action; just let the null InputSource pass through
					getLogger().log( Level.DEBUG, "Failed to retrieve a local copy of the XHTML special ENT file based on the System ID." );
				}
			}
			else if ( publicId == null && systemId.indexOf( Constants.DOCTYPE_XHTML_SYMBOL_SYSTEM_ID ) != -1 ) {
				try {
					// Retrieve a local copy of the XHTML symbol ENT file
					InputStream inputStream	= Thread.currentThread().getContextClassLoader().getResourceAsStream( Constants.ENT_XHTML_SYMBOL_FILE_PATH );
					inputSource				= new InputSource( inputStream );
					getLogger().log( Level.DEBUG, "A local copy of the XHTML symbol ENT file has been successfully retrieved based on the System ID." );
				} catch ( Exception ex ) {
					// No action; just let the null InputSource pass through
					getLogger().log( Level.DEBUG, "Failed to retrieve a local copy of the XHTML symbol ENT file based on the System ID." );
				}
			}
		}
		// General entity
		else if ( name != null ) {
			// Do nothing
		}
		
		
		// If nothing found, null is returned, for normal processing
		return inputSource;
	}

	
	public String getPublicId() {
		return this.publicId;
	}
	
	
	public String getSystemId() {
		return this.systemId;
	}
}