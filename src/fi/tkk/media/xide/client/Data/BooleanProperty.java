package fi.tkk.media.xide.client.Data;

import java.util.ArrayList;

public class BooleanProperty extends Property {

	private boolean value;

	/**
	 * Constructor for serialization
	 */
	public BooleanProperty(){
		
	}
	
	public BooleanProperty(String name, boolean isHidden, boolean isNeverEditable, String descr){
		super(name, isHidden, isNeverEditable, descr); 
	}
	
	public BooleanProperty(String name, boolean isHidden, boolean isNeverEditable, String descr, boolean value){
		super(name, isHidden, isNeverEditable, descr); 
		setValue(value);
	}

//	public BooleanProperty(String name, String value, boolean isEditable){
//		this.name = name;
//		setValue(value);
//		this.isEditableNow = isEditable;
//		this.property_type = TYPE_DEFAULT;
//	}
	
	public BooleanProperty(String name, boolean value, String type, 
			String defaultValue, String description, boolean isEditable, boolean isHidden, boolean isNeverEditable, int ptype){
		super(name, type, defaultValue, description, isEditable, isHidden, isNeverEditable, ptype);
		setValue(value);
		}
	

	/**
	 * Sets the value which is represented as string. Parse array from the string
	 * @param value
	 */
	public void setValue(String value) {
		if (value.equals(Property.BOOLEAN_TRUE)) {
			this.value = true;
		}
		else {
			this.value = false;
		}
	}
		
	public void setValue( boolean value) {
		this.value = value;
	}
	
	public BooleanProperty clone(){
			return new BooleanProperty(this.name, value, this.type, this.defaultValue,
					this.description, this.isEditableNow, this.isHidden, this.isNeverEditable, this.property_type);
	}

	public Object getValue() {
			return value;
	}

	public String getStringValue() {
		if (value == false) {
			return Property.BOOLEAN_FALSE;
		}
		else {
			return Property.BOOLEAN_TRUE;
		}
	}

	@Override
	public boolean isNull() {
			return true;
	}
}
