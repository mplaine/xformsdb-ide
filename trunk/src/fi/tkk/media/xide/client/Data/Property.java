





package fi.tkk.media.xide.client.Data;

import java.io.Serializable;

public abstract class Property implements Serializable {
	// Common properties
	public static final String ID = "ID";
	public static final String TITLE = "Title";
	public static final String DESCR = "Description";
	public static final String AUTHOR = "Author";
	public static final String DATE_CR = "Date_Creation";
	public static final String DATE_MOD = "Date_Modification";
	public static final String DATE_PUB = "Date_Publication";
	public static final String SERVER_FOLDER_PATH = "Folder_Path";
	// App and page
	public static final String RELATED_URL = "Related_URL";
	public static final String APP_ID = "Application_id";
	public static final String MAIN_PAGE = "Main_Page";

	// Template properties
	public static final String TAGS = "Template Tags";
	public static final String T_IS_PUBLIC = "Is_public";
	public static final String TYPE = "Type";
	public static final String DO_WORK = "Do_Work";

	// Application properties
	public static final String URL = "URL";
	public static final String IS_PUBLISHED = "Is_published";
	public static final String IS_SHOWN_PUBLIC = "Is_shown";
	public static final String IS_DEMO = "Is_Demo";

	// Page properties

	public static final String[] DESCRIPTIONS = { "", "", // 1
			"", // 2
			"", // 3
			"", // 4
			"", // 5
			"", // 6
			"", // 7
			"" }; // 8

	// Types of the parameter defined in the component
	public static final String PARAM_TYPE_TEXT = "text";
	public static final String PARAM_TYPE_HTML = "html";
	public static final String PARAM_TYPE_BINDING = "binding";

	// Types of boolean properties
	public static final String BOOLEAN_TRUE = "true";
	public static final String BOOLEAN_FALSE = "false";
	
	
	// Different types of property object
	// Parameter
	public static final int TYPE_PARAMETER = 1;
	// Meta information
	public static final int TYPE_METAINFO = 2;
	// System property
	public static final int TYPE_SYSTEMPROPERTIES = 3;
	// Others
	public static final int TYPE_DEFAULT = 4;

	// TODO: Think about access modifiers
	String name;
	String defaultValue;
	String type;
	String description;
	

	boolean isEditableNow;
	boolean isNeverEditable;
	boolean isHidden;
	// boolean isEditableNow;

	protected int property_type;

	/**
	 * Empty constructor required for serialization
	 */
	public Property() {

	}

	/**
	 * Used by server to construct properties from DB
	 * 
	 * @param name
	 * @param isHidden
	 * @param isNeverEditable
	 * @param descr
	 */
	public Property(String name, boolean isHidden, boolean isNeverEditable,
			String descr) {
		this.name = name;
		this.description = descr;
		this.isEditableNow = false;
		this.isHidden = isHidden;
		this.isNeverEditable = isNeverEditable;

		this.property_type = TYPE_DEFAULT;
	}

	public Property(String name, String type,
			String defaultValue, String description, int ptype) {
		this.name = name;
		this.type = type;
		this.defaultValue = defaultValue;
		this.description = description;
		this.isEditableNow = false;
		this.isHidden = false;
		this.isNeverEditable = false;
		this.property_type = ptype;
	}

	public Property(String name, String type,
			String defaultValue, String description, boolean isEditable,
			boolean isHidden, boolean isNeverEditable, int ptype) {
		this.name = name;
		this.type = type;
		this.defaultValue = defaultValue;
		this.description = description;
		this.isEditableNow = isEditable;
		this.isHidden = isHidden;
		this.isNeverEditable = isNeverEditable;
		this.property_type = ptype;
	}


	public Property(String name, String type,
			String defaultValue, String description, boolean isEditable) {
		this.name = name;
		this.type = type;
		this.defaultValue = defaultValue;
		this.description = description;
		this.isEditableNow = isEditable;
		this.isHidden = false;
		this.isNeverEditable = false;
		this.property_type = TYPE_DEFAULT;
	}

	public Property(String name, String defaultValue,
			String description, boolean isEditable) {
		this.name = name;
		this.defaultValue = defaultValue;
		this.description = description;
		this.isEditableNow = isEditable;
		this.isHidden = false;
		this.isNeverEditable = false;
		this.property_type = TYPE_DEFAULT;
	}

	public abstract void setValue(String value);

	public abstract Property clone();

	public abstract Object getValue();

	public abstract String getStringValue() ;

	/**
	 * Checks if the property value is null or empty
	 * @return
	 */
	public abstract boolean isNull();

	public void setDefaultValue(String value) {
		this.defaultValue = value;
	}
	
	public String getName() {
		return name;
	}
	
	public void setEditableNow(boolean isEditable) {
		this.isEditableNow = isEditable;
	}
	
	public String getType() {
		return type;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public String getDescription() {
		return description;
	}

	public boolean isEditableNow() {
		return isEditableNow;
	}

	public boolean isNeverEditable() {
		return isNeverEditable;
	}

	public boolean isHiden() {
		return isHidden;
	}

	public int getProperty_type() {
		return property_type;
	}


	public static String encodeEncodeIntoEntities(String string) {
		String result = string;
		try {
			// Replacing basic sybols: <, >, ', " and &
			result = result.replaceAll("&", "&amp;");
			result = result.replaceAll("<", "&lt;");
			result = result.replaceAll(">", "&gt;");
			result = result.replaceAll("'", "&apos;");
			result = result.replaceAll("\"", "&quot;");
		}
		catch (java.lang.IllegalArgumentException e) {
			
		}
		return result;
	}

	public static String decodeFromEntities(String string) {
		String result = string;
		// Replacing basic sybols: <, >, ', " and &
		result = result.replaceAll("&amp;", "&");
		result = result.replaceAll("&lt;", "<");
		result = result.replaceAll("&gt;", ">");
		result = result.replaceAll("&apos;", "'");
		result = result.replaceAll("&quot;", "\"");

		return result;
	}
}
