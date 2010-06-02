/**
 * 
 */
package fi.tkk.media.xide.client.Data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Basic property which stores string (text) value
 * @author Evgenia Samochadina
 * @date Dec 3, 2008
 *
 */
public class BaseProperty extends Property implements Serializable{
	private String value;

	public BaseProperty(){
		
	}
	
	
	public void setValue(String value) {
		this.value = value;
	}

	
	/**
	 * Only value(String). Used when new application/page is saved in the database
	 * @param name
	 * @param value
	 * @param isEditable
	 */
	public BaseProperty(String value){
		this.name = "";
		this.value = value;
		this.isEditableNow = true;
		this.property_type = TYPE_DEFAULT;
	}
	
	/**
	 * Only name, value(String) and editability settings
	 * @param name
	 * @param value
	 * @param isEditable
	 */
	public BaseProperty(String name, String value, boolean isEditable){
		this.name = name;
		this.value = value;
		this.isEditableNow = isEditable;
		this.property_type = TYPE_DEFAULT;
	}
	

	
	/**
	 * Used by server to construct properties from DB
	 * @param name
	 * @param isHidden
	 * @param isNeverEditable
	 * @param descr
	 */
	public BaseProperty(String name, boolean isHidden, boolean isNeverEditable, String descr){
		super(name, isHidden, isNeverEditable, descr); 
		this.value = null;
	}




	public BaseProperty(String name, String value, String type, 
			String defaultValue, String description, int ptype){
		super(name, type, defaultValue, description, ptype);
		this.value = value;
		}

	
	public BaseProperty(String name, String value, String type, 
			String defaultValue, String description, boolean isEditable, boolean isHidden, boolean isNeverEditable, int ptype){
		super(name, type, defaultValue, description, isEditable, isHidden, isNeverEditable, ptype);
		this.value = value;
		}

	public BaseProperty(String name, String value, String type, 
			String defaultValue, String description){
		super(name, type, defaultValue, description, TYPE_DEFAULT);
		this.value = value;
		}

	public BaseProperty(String name, String[] arrayValue, 
			String defaultValue, String description, boolean isEditable){
		this.name = name;
		this.value = null;
		this.defaultValue = defaultValue;
		this.description = description;
		this.isEditableNow = isEditable;
		this.isHidden = false;
		this.isNeverEditable = false;
		this.property_type = TYPE_DEFAULT;
	}

	public Property clone(){
			return new BaseProperty(this.name, this.value, this.type, this.defaultValue,
					this.description, this.isEditableNow, this.isHidden, this.isNeverEditable, this.property_type);
	}
	

	public Object getValue() {
			return value;
	}



	
	public String getStringValue() {
		if (value != null){
			return value;
		}
		else {
			// no value for property
			if (defaultValue != null ) {
				return defaultValue;
			}
			else {
				return "";
			}
		}

	}
	
	
	@Override
	public boolean isNull() {
		if (value == null) {
			return true;
		}
		else {
			return false;
		}
	}

}
