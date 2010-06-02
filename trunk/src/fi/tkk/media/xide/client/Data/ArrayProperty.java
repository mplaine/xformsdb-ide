package fi.tkk.media.xide.client.Data;

import java.util.ArrayList;

public class ArrayProperty extends Property {

	private ArrayList<String> arrayValue;

	/**
	 * Constructor for serialization
	 */
	public ArrayProperty(){
		
	}
	/**
	 * Only name, value (String[]) and editability settings
	 * @param name
	 * @param arrayValue
	 * @param isEditable
	 */
	
	public ArrayProperty(String name, String[] arrayValue, boolean isEditable){
		this.name = name;
		setValue(arrayValue);
		this.isEditableNow = isEditable;
		this.property_type = TYPE_DEFAULT;
	}
	
	public ArrayProperty(String name, String[] arrayValue, String type, 
			String defaultValue, String description, boolean isEditable, boolean isHidden, boolean isNeverEditable, int ptype){
		super(name, type, defaultValue, description, isEditable, isHidden, isNeverEditable, ptype);
		setValue(arrayValue);
		}
	
	public void setValue(String[] value) {
		this.arrayValue = new ArrayList<String>();
		for (int i = 0; i < value.length; i++) {
			arrayValue.add(value[i]);
		}
	}

	/**
	 * Sets the value which is represented as string. Parse array from the string
	 * @param value
	 */
	public void setValue(String value) {
		
		arrayValue = convertStringToArray(value);
	}
		
	public void setValue(String[] value, boolean convertIntoEntities) {
		this.arrayValue = new ArrayList<String>();
		
		for (int i = 0; i < value.length; i++) {
			if (convertIntoEntities) {
				arrayValue.add(encodeEncodeIntoEntities(value[i]));
			}
			else {
				arrayValue.add(value[i]);
			}
		}
	}
	
	public Property clone(){
			return new ArrayProperty(this.name, arrayValue.toArray(new String[arrayValue.size()]), this.type, this.defaultValue,
					this.description, this.isEditableNow, this.isHidden, this.isNeverEditable, this.property_type);
	}

	public Object getValue() {
			return arrayValue;
	}

	
	public static ArrayList<String> convertStringToArray(String value) {
		ArrayList<String> strings = new ArrayList<String>();
		while(value.length() != 0) {
			if (value.indexOf(";") > 0) {
				strings.add(value.substring(0, value.indexOf(";")));
				value = value.substring(value.indexOf(";")+1, value.length());
				if (value.startsWith(" ")){
					value = value.substring(1, value.length());
				}
			}
		}
		if (strings.size() == 0) {
			strings.add(value);
		}
		return strings;
	}

	public static String getStringFromArray(ArrayList<String> array) {
		String result = null;
		for (int i = 0; i < array.size(); i++){
			if (result == null) {
				result = new String(array.get(i) + "; ");
			}
			else {
				result = new String(result + array.get(i) + "; ");
			}
		}
		return result;
	}
	public String getStringValue() {
		if (arrayValue != null){
			return getStringFromArray(arrayValue);
		}
		else {
			// no value for property
			if (defaultValue != null ) {
				return defaultValue;
//				return decodeFromEntities(defaultValue);
			}
			else {
				return "";
			}
		}

	}
	@Override
	public boolean isNull() {
		if (arrayValue == null) {
			return true;
		}
		else {
			return false;
		}
	}

}
