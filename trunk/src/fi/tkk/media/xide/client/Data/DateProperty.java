package fi.tkk.media.xide.client.Data;

import java.util.Date;

public class DateProperty  extends Property {

	private Date value = null;
	private String unparsedValue;

	/**
	 * Constructor for serialization
	 */
	public DateProperty(){
		
	}
	
	public DateProperty(String name, boolean isHidden, boolean isNeverEditable, String descr){
		super(name, isHidden, isNeverEditable, descr); 
	}
//	public BooleanProperty(String name, String value, boolean isEditable){
//		this.name = name;
//		setValue(value);
//		this.isEditableNow = isEditable;
//		this.property_type = TYPE_DEFAULT;
//	}
	
	public DateProperty(String name, Date value, String unparsedValue, String type, 
			String defaultValue, String description, boolean isEditable, boolean isHidden, boolean isNeverEditable, int ptype){
		super(name, type, defaultValue, description, isEditable, isHidden, isNeverEditable, ptype);
		setValue(value);
		this.unparsedValue = unparsedValue;
		}
	

	/**
	 * Sets the value which is represented as string. Parse array from the string
	 * @param value
	 */
	public void setValue(Date value) {
			this.value = value;
			
	}
		
	/**
	 * Sets the value which is represented as string. Parse data from the string
	 * @param value
	 */
	public void setValue(String value) {
		try {
			this.value = new Date(value);
			
		}
		catch(Exception e){
			// Data format is incorrect
			this.unparsedValue = value;
		}
	}
	
	/**
	 * Sets the value which is represented as int. Parse data from the string
	 * @param value
	 */
//	public void setValue(long value) {
//			this.value = new Date(value);
//	}
	
	/**
	 * Sets the value which is represented as date. Additionally saves string representation of the date received from 
	 * DB server. Is used to have one format of the date everywhere.
	 * @param value
	 */
	public void setValue(Date date, String stringValue) {
			this.value = date;
			this.unparsedValue = stringValue;
	}
	
	/**
	 * Sets the value which is represented as int. Additionally saves string representation of the date received from 
	 * DB server. Is used to have one format of the date everywhere.
	 * @param value
	 */
	public void setValue(long value, String stringValue) {
			this.value = new Date(value);
			this.unparsedValue = stringValue;
	}
	
	public Property clone(){
			return new DateProperty(this.name, value, this.unparsedValue, this.type, this.defaultValue,
					this.description, this.isEditableNow, this.isHidden, this.isNeverEditable, this.property_type);
	}

	public Object getValue() {
		if (value != null) {
			return value;
		}
		else{
			return unparsedValue;
		}
	}

	public String getStringValue() {
		if (unparsedValue != null) {
			return unparsedValue;
		}
		else { 
			return value.toString().substring(0, value.toString().length()-2);
		}
		
	}

	@Override
	public boolean isNull() {
		if (value == null) {
			if(unparsedValue == null) {
				return true;
			}
			else {
				return false;
			}
		}
		else if (value.getTime() == 0){
			return true;
		}
		else {
			return false;
		}
	}
}
