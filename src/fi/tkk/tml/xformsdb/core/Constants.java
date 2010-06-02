package fi.tkk.tml.xformsdb.core;


/**
 * Application constants.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on July 07, 2009
 */
public class Constants {
	

	
	// PUBLIC STATIC FINAL VARIABLES
	// Web application file (web.xml)
	public static final String WEB_APPLICATION_FILE_PATH									= "WEB-INF/web.xml";
	
	
	// XFormsDB configuration file (conf.xml)
	public static final String XFORMSDB_CONFIG_FILE_PATH									= "WEB-INF/conf.xml";
	// XML Namespace
	public static final String XFORMSDB_CONFIG_NAMESPACE_PREFIX								= "";
	public static final String XFORMSDB_CONFIG_NAMESPACE_URI								= "http://www.tml.tkk.fi/2007/xformsdb";
	// MIME mapping defaults
	public static final String XFORMSDB_CONFIG_DEFAULT_MIME_MAPPING_EXTENSION				= "xformsdb";
	public static final String XFORMSDB_CONFIG_DEFAULT_MIME_MAPPING_MIME_TYPE				= "application/xhtml+xml";
	// Character encoding defaults
	public static final String XFORMSDB_CONFIG_DEFAULT_ENCODING								= "UTF-8"; // Unfortunately, not supported by X-Smiles
	// Data ID attribute defaults
	public static final String XFORMSDB_CONFIG_DEFAULT_DATA_ID_ATTRIBUTE_NAMESPACE_PREFIX	= "xformsdb";
	public static final String XFORMSDB_CONFIG_DEFAULT_DATA_ID_ATTRIBUTE_NAMESPACE_URI		= "http://www.tml.tkk.fi/2007/xformsdb";
	public static final String XFORMSDB_CONFIG_DEFAULT_DATA_ID_ATTRIBUTE_LOCAL_NAME			= "id";
	public static final String XFORMSDB_CONFIG_DEFAULT_DATA_ID_ATTRIBUTE_VALUE_PREFIX		= "xformsdb_";
	// Data sources defaults
	public static final String XFORMSDB_CONFIG_DEFAULT_DATA_SOURCE_ID						= "XFORMSDB_DEFAULT_DATA_SOURCE";
	public static final String XFORMSDB_CONFIG_DEFAULT_DATA_SOURCE_TYPE						= "1";
	public static final String XFORMSDB_CONFIG_DEFAULT_DATA_SOURCE_URI						= ""; // Replaced later with the root path of the XFormsDB installation
	public static final String XFORMSDB_CONFIG_DEFAULT_DATA_SOURCE_USERNAME					= "";
	public static final String XFORMSDB_CONFIG_DEFAULT_DATA_SOURCE_PASSWORD					= "";
	public static final String XFORMSDB_CONFIG_DEFAULT_DATA_SOURCE_COLLECTION				= "/WEB-INF/data";
	public static final String XFORMSDB_CONFIG_DEFAULT_DATA_SOURCE_COLLECTION_FILE_NAME		= "collection.xml";
	// Data source for files metadata defaults
	public static final String XFORMSDB_CONFIG_DEFAULT_FILES_METADATA_ID					= "XFORMSDB_DEFAULT_FILES_METADATA";
	public static final String XFORMSDB_CONFIG_DEFAULT_FILES_METADATA_TYPE					= "1";
	public static final String XFORMSDB_CONFIG_DEFAULT_FILES_METADATA_URI					= ""; // Replaced later with the root path of the XFormsDB installation
	public static final String XFORMSDB_CONFIG_DEFAULT_FILES_METADATA_USERNAME				= "";
	public static final String XFORMSDB_CONFIG_DEFAULT_FILES_METADATA_PASSWORD				= "";
	public static final String XFORMSDB_CONFIG_DEFAULT_FILES_METADATA_COLLECTION			= "/WEB-INF/files";
	public static final String XFORMSDB_CONFIG_DEFAULT_FILES_METADATA_COLLECTION_FILE_NAME	= "collection.xml";
	// Files folder defaults
	public static final String XFORMSDB_CONFIG_DEFAULT_FILES_FOLDER							= "/WEB-INF/files";
	// Security file extension defaults
	public static final String XFORMSDB_CONFIG_DEFAULT_SECURITY_FILE_EXTENSION_XQ			= "xq";
	
	
	// XQueries
	// Select the highest data ID attribute of a data source XQuery file (select_highestdataidattributevalue.xq)
	public static final String XQ_SELECT_HIGHEST_DATA_ID_ATTRIBUTE_VALUE_FILE_PATH			= "fi/tkk/tml/xformsdb/resource/xq/select_highestdataidattributevalue.xq";
	// Authenticate user XQuery file (authenticate_user.xq)
	public static final String XQ_AUTHENTICATE_USER_FILE_PATH								= "fi/tkk/tml/xformsdb/resource/xq/authenticate_user.xq";
	// Select files XQuery file (select_files.xq)
	public static final String XQ_SELECT_FILES_FILE_PATH									= "fi/tkk/tml/xformsdb/resource/xq/select_files.xq";
	// Select a file XQuery file (select_file.xq)
	public static final String XQ_SELECT_FILE_FILE_PATH										= "fi/tkk/tml/xformsdb/resource/xq/select_file.xq";
	// Insert files XQuery file (insert_files.xq)
	public static final String XQ_INSERT_FILES_FILE_PATH									= "fi/tkk/tml/xformsdb/resource/xq/insert_files.xq";
	// Delete files XQuery file (delete_files.xq)
	public static final String XQ_DELETE_FILES_FILE_PATH									= "fi/tkk/tml/xformsdb/resource/xq/delete_files.xq";
	// Update files XQuery file (update_files.xq)
	public static final String XQ_UPDATE_FILES_FILE_PATH									= "fi/tkk/tml/xformsdb/resource/xq/update_files.xq";


	// DOCTYPE related definitions
	// PUBLIC ID for the XFormsDB 1.0 DTD file (xformsdb1.dtd)
	public static final String DOCTYPE_XFORMSDB1_PUBLIC_ID									= "-//TKK//DTD XFORMSDB 1.0//EN";		
	// SYSTEM ID for the XFormsDB 1.0 DTD file (xformsdb1.dtd)
	public static final String DOCTYPE_XFORMSDB1_SYSTEM_ID									= "xformsdb1.dtd";		
	// System ID for the XHTML lat1 ENT file
	public static final String DOCTYPE_XHTML_LAT1_SYSTEM_ID									= "xhtml-lat1.ent";		
	// System ID for the XHTML special ENT file
	public static final String DOCTYPE_XHTML_SPECIAL_SYSTEM_ID								= "xhtml-special.ent";		
	// System ID for the XHTML symbol ENT file
	public static final String DOCTYPE_XHTML_SYMBOL_SYSTEM_ID								= "xhtml-symbol.ent";		

	
	// DTDs
	// XFormsDB 1.0 DTD file (xformsdb1.dtd)
	public static final String DTD_XFORMSDB1_FILE_PATH										= "fi/tkk/tml/xformsdb/resource/dtd/xformsdb1.dtd";		

	
	// Entities
	// XHTML lat1 (xhtml-lat1.ent)
	public static final String ENT_XHTML_LAT1_FILE_PATH										= "fi/tkk/tml/xformsdb/resource/dtd/xhtml-lat1.ent";		
	// XHTML special (xhtml-special.ent)
	public static final String ENT_XHTML_SPECIAL_FILE_PATH									= "fi/tkk/tml/xformsdb/resource/dtd/xhtml-special.ent";		
	// XHTML symbol (xhtml-symbol.ent)
	public static final String ENT_XHTML_SYMBOL_FILE_PATH									= "fi/tkk/tml/xformsdb/resource/dtd/xhtml-symbol.ent";		

	
	// XSLT stylesheets
	// XFormsDB Include XSLT stylesheet file (xformsdb_include.xsl)
	public static final String XSL_XFORMSDB_INCLUDE_FILE_PATH								= "fi/tkk/tml/xformsdb/resource/xsl/xformsdb_include.xsl";
	// XFormsDB Parse XSLT stylesheet file (xformsdb_xom.xsl)
	public static final String XSL_XFORMSDB_XOM_FILE_PATH									= "fi/tkk/tml/xformsdb/resource/xsl/xformsdb_xom.xsl";
	// XFormsDB XForms XSLT stylesheet file (xformsdb_xforms.xsl)
	public static final String XSL_XFORMSDB_XFORMS_FILE_PATH								= "fi/tkk/tml/xformsdb/resource/xsl/xformsdb_xforms.xsl";
	// Data ID attribute XSLT stylesheet file (dataidattribute.xsl)
	public static final String XSL_DATA_ID_ATTRIBUTE_FILE_PATH								= "fi/tkk/tml/xformsdb/resource/xsl/dataidattribute.xsl";

	
	// XFormsDB response proxy
	public static final String XFORMSDB_RESPONSE_PROXY_INSTANCE								= "xformsdb-response-proxy-instance";

	
	// XFormsDB user
	public static final String XFORMSDB_USER												= Constants.NAMESPACE_PREFIX_XFORMSDB + ( ( "".equals( Constants.NAMESPACE_PREFIX_XFORMSDB ) == true ) ? "" : ":" ) + "user";

	
	// XFormsDB upload
	public static final String XFORMSDB_UPLOAD												= Constants.NAMESPACE_PREFIX_XFORMSDB + ( ( "".equals( Constants.NAMESPACE_PREFIX_XFORMSDB ) == true ) ? "" : ":" ) + "upload";


	// XFormsDB request base URI
	public static final String XFORMSDB_REQUEST_BASE_URI									= Constants.NAMESPACE_PREFIX_XFORMSDB + ( ( "".equals( Constants.NAMESPACE_PREFIX_XFORMSDB ) == true ) ? "" : ":" ) + "request-base-uri";
	public static final String XFORMSDB_REQUEST_BASE_URI_INSTANCE							= "xformsdb-request-base-uri-instance";

	
	// XFormsDB request headers
	public static final String XFORMSDB_REQUEST_HEADERS										= Constants.NAMESPACE_PREFIX_XFORMSDB + ( ( "".equals( Constants.NAMESPACE_PREFIX_XFORMSDB ) == true ) ? "" : ":" ) + "request-headers";
	public static final String XFORMSDB_REQUEST_HEADERS_INSTANCE							= "xformsdb-request-headers-instance";

	
	// XFormsDB request parameters
	public static final String XFORMSDB_REQUEST_PARAMETERS									= Constants.NAMESPACE_PREFIX_XFORMSDB + ( ( "".equals( Constants.NAMESPACE_PREFIX_XFORMSDB ) == true ) ? "" : ":" ) + "request-parameters";
	public static final String XFORMSDB_REQUEST_PARAMETERS_INSTANCE							= "xformsdb-request-parameters-instance";

	
	// XFormsDB state
	public static final String XFORMSDB_STATE												= Constants.NAMESPACE_PREFIX_XFORMSDB + ( ( "".equals( Constants.NAMESPACE_PREFIX_XFORMSDB ) == true ) ? "" : ":" ) + "state";
	public static final String XFORMSDB_STATE_INSTANCE										= "xformsdb-state-instance";

	
	// XFormsDB cookie
	public static final String XFORMSDB_COOKIE												= Constants.NAMESPACE_PREFIX_XFORMSDB + ( ( "".equals( Constants.NAMESPACE_PREFIX_XFORMSDB ) == true ) ? "" : ":" ) + "cookie";
	public static final String XFORMSDB_TEST_COOKIE											= "XFORMSDB_TEST_COOKIE";

	
	// XFormsDB request error event
	public static final String XFORMSDB_REQUEST_ERROR_EVENT									= "xformsdb-request-error";

	
	// XFormsDB error element
	public static final String XFORMSDB_ERROR_ELEMENT										= Constants.NAMESPACE_PREFIX_XFORMSDB + ":error";

		
	// MIME types
	public static final String MIME_TYPE_APPLICATION_XML									= "application/xml";
	public static final String MIME_TYPE_APPLICATION_XHTML_XML								= "application/xhtml+xml";
	public static final String MIME_TYPE_TEXT_HTML											= "text/html";
	public static final String MIME_TYPE_TEXT_XML											= "text/xml";
	
	
	// Output XML version
	public static final String OUTPUT_XML_VERSION_1_0										= "1.0";
	// Output XML omit XML declaration
	public static final String OUTPUT_XML_OMIT_XML_DECLARATION_NO							= "no";
	// Output XML method
	public static final String OUTPUT_XML_METHOD_XML										= "xml";
	// Output XML indent
	public static final String OUTPUT_XML_INDENT_YES										= "yes";
	public static final String OUTPUT_XML_INDENT_NO											= "no";
	// Output XQuery version
	public static final String OUTPUT_XQUERY_VERSION_1_0									= "1.0";

	
	// XML Namespaces
	// Prefixes
	public static final String NAMESPACE_PREFIX_EMPTY										= "";
	public static final String NAMESPACE_PREFIX_XHTML										= "xhtml";
	public static final String NAMESPACE_PREFIX_XFORMS										= "xforms";
	public static final String NAMESPACE_PREFIX_XFORMSDB									= "xformsdb";
	public static final String NAMESPACE_PREFIX_XFORMSDB_XSL_FUNCTIONS						= "func";
	// URIs
	public static final String NAMESPACE_URI_EMPTY											= "";
	public static final String NAMESPACE_URI_XHTML											= "http://www.w3.org/1999/xhtml";
	public static final String NAMESPACE_URI_XFORMS											= "http://www.w3.org/2002/xforms";
	public static final String NAMESPACE_URI_XFORMSDB										= "http://www.tml.tkk.fi/2007/xformsdb";
	public static final String NAMESPACE_URI_XFORMSDB_XSL_FUNCTIONS							= "http://www.tml.tkk.fi/2007/xformsdb/xsl/functions";
	// XMLNSs
	public static final String NAMESPACE_XMLNS_EMPTY										= "xmlns" + ( ( "".equals( Constants.NAMESPACE_PREFIX_EMPTY ) == true ) ? "" : ":" + Constants.NAMESPACE_PREFIX_EMPTY ) + "=\"" + Constants.NAMESPACE_URI_EMPTY + "\"";
	public static final String NAMESPACE_XMLNS_XHTML										= "xmlns" + ( ( "".equals( Constants.NAMESPACE_PREFIX_XHTML ) == true ) ? "" : ":" + Constants.NAMESPACE_PREFIX_XHTML ) + "=\"" + Constants.NAMESPACE_URI_XHTML + "\"";
	public static final String NAMESPACE_XMLNS_XFORMS										= "xmlns" + ( ( "".equals( Constants.NAMESPACE_PREFIX_XFORMS ) == true ) ? "" : ":" + Constants.NAMESPACE_PREFIX_XFORMS ) + "=\"" + Constants.NAMESPACE_URI_XFORMS + "\"";
	public static final String NAMESPACE_XMLNS_XFORMSDB										= "xmlns" + ( ( "".equals( Constants.NAMESPACE_PREFIX_XFORMSDB ) == true ) ? "" : ":" + Constants.NAMESPACE_PREFIX_XFORMSDB ) + "=\"" + Constants.NAMESPACE_URI_XFORMSDB + "\"";
	public static final String NAMESPACE_XMLNS_XFORMSDB_XSL_FUNCTIONS						= "xmlns" + ( ( "".equals( Constants.NAMESPACE_PREFIX_XFORMSDB_XSL_FUNCTIONS ) == true ) ? "" : ":" + Constants.NAMESPACE_PREFIX_XFORMSDB_XSL_FUNCTIONS ) + "=\"" + Constants.NAMESPACE_URI_XFORMSDB_XSL_FUNCTIONS + "\"";

	
	// Include namespace prefixes
	public static final String INCLUDE_NAMESPACE_PREFIXES_DEFAULT							= null;//"#default";
	
	
	// Replace values
	public static final String REPLACE_NONE													= "none";
	public static final String REPLACE_INSTANCE												= "instance";
	public static final String REPLACE_ALL													= "all";
	
	
	// Media types
	public static final String MEDIA_TYPE_APPLICATION_XML									= "application/xml";


	// Methods
	public static final String METHOD_GET													= "get";
	public static final String METHOD_POST													= "post";
	
	
	// Expression types
	public static final String EXPRESSION_TYPE_SELECT										= "select";
	public static final String EXPRESSION_TYPE_SELECT4UPDATE								= "select4update";
	public static final String EXPRESSION_TYPE_UPDATE										= "update";
	public static final String EXPRESSION_TYPE_ALL											= "all";

	
	// Replace types
	public static final String REPLACE_TYPE_NONE											= "none";
	public static final String REPLACE_TYPE_INSTANCE										= "instance";
	public static final String REPLACE_TYPE_ALL												= "all";

	
	// State types
	public static final String STATE_TYPE_GET												= "get";
	public static final String STATE_TYPE_SET												= "set";

	
	// File types
	public static final String FILE_TYPE_SELECT												= "select";
	public static final String FILE_TYPE_UPDATE												= "update";
	public static final String FILE_TYPE_INSERT												= "insert";
	public static final String FILE_TYPE_DELETE												= "delete";

	
	// Servlet context attributes
	public static final String SERVLET_CONTEXT_ATTRIBUTE_WEB_APPLICATION					= "SERVLET_CONTEXT_ATTRIBUTE_WEB_APPLICATION";
	public static final String SERVLET_CONTEXT_ATTRIBUTE_XFORMSDB_CONFIG					= "SERVLET_CONTEXT_ATTRIBUTE_XFORMSDB_CONFIG";
	public static final String SERVLET_CONTEXT_ATTRIBUTE_DAO_FACTORIES						= "SERVLET_CONTEXT_ATTRIBUTE_DAO_FACTORIES";


	// Request attributes
	public static final String REQUEST_ATTRIBUTE_REPLACETYPE								= "REQUEST_ATTRIBUTE_REPLACETYPE";

	
	// Session attributes
	public static final String SESSION_ATTRIBUTE_ACTIVE_XFORMSDB_QUERY_ELEMENTS				= "SESSION_ATTRIBUTE_ACTIVE_XFORMSDB_QUERY_ELEMENTS";
	public static final String SESSION_ATTRIBUTE_SELECT4UPDATE_XFORMSDB_QUERY_RESULTS		= "SESSION_ATTRIBUTE_SELECT4UPDATE_XFORMSDB_QUERY_RESULTS";
	public static final String SESSION_ATTRIBUTE_ACTIVE_XFORMSDB_LOGIN_ELEMENTS				= "SESSION_ATTRIBUTE_ACTIVE_XFORMSDB_LOGIN_ELEMENTS";
	public static final String SESSION_ATTRIBUTE_XFORMSDB_STATE								= "SESSION_ATTRIBUTE_XFORMSDB_STATE";
	public static final String SESSION_ATTRIBUTE_LOGGED_IN_XFORMSDB_USER					= "SESSION_ATTRIBUTE_LOGGED_IN_XFORMSDB_USER";
	public static final String SESSION_ATTRIBUTE_ACTIVE_XFORMSDB_FILE_ELEMENTS				= "SESSION_ATTRIBUTE_ACTIVE_XFORMSDB_FILE_ELEMENTS";


	// Request parameters
	public static final String REQUEST_PARAMETER_EXPRESSION_TYPE							= "expressiontype";
	public static final String REQUEST_PARAMETER_STATE_TYPE									= "statetype";
	public static final String REQUEST_PARAMETER_FILE_TYPE									= "filetype";
	public static final String REQUEST_PARAMETER_REPLACE_TYPE								= "replacetype";
	public static final String REQUEST_PARAMETER_ID											= "id";
	
	
	// Error format
	public static final int ERROR_FORMAT_XHTML												= 1;
	public static final int ERROR_FORMAT_XML												= 2;
	public static final int ERROR_FORMAT_XFORMS												= 3;
	
	
	// File last modified format
	public static final String FILE_CREATED_FORMAT											= "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";
	public static final String FILE_LAST_MODIFIED_FORMAT									= "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";
}