package fi.tkk.media.xide.client.Data;

import java.io.Serializable;


public class OptionProperty extends Property {

	private Option[] optionValues;
	private int defaultOptionNumber;
	private int currentNumber;

	/**
	 * Constructor for serialization
	 */
	public OptionProperty(){
		
	}
	
//	public BooleanProperty(String name, String value, boolean isEditable){
//		this.name = name;
//		setValue(value);
//		this.isEditableNow = isEditable;
//		this.property_type = TYPE_DEFAULT;
//	}
	
	/**
	 * Constructs the property with a set of optional values and selected value (no default value)
	 * Used to construct a select-main-page option list
	 */
	public OptionProperty(String name, Option[] options, int currentNumber, 
			 String description, boolean isEditable){
		super(name, "", getDefaultOption(options, -1).getValue(), description, isEditable, false, false, Property.TYPE_SYSTEMPROPERTIES);
		this.optionValues = options;
		this.defaultOptionNumber = -1;
		this.currentNumber = currentNumber;
	}
	
	/**
	 * Constructs the parameter with a set of optional values and default value
	 */
	public OptionProperty(String name, Option[] options, int defaultOptionN, String type, 
			 String description, boolean isEditable){
		super(name, type, getDefaultOption(options, defaultOptionN).getValue(), description, isEditable, false, false, Property.TYPE_PARAMETER);
		this.optionValues = options;
		this.defaultOptionNumber = defaultOptionN;
		this.currentNumber = -1;
	}
	
	/**
	 * Constructs the parameter with a set of optional values. default value is the first one.
	 */
	public OptionProperty(String name, Option[] options, String type, 
			 String description, boolean isEditable){
		super(name, type, getDefaultOption(options, 0).getValue(), description, isEditable, false, false, Property.TYPE_PARAMETER);
		this.optionValues = options;
		this.defaultOptionNumber = 0;
		this.currentNumber = -1;
	}

	
	public OptionProperty(String name,  Option[] options, int defaultOptionN, int currentNumber, String type, 
			String defaultValue, String description, boolean isEditable, boolean isHidden, boolean isNeverEditable, int ptype){
		super(name, type, getDefaultOption(options, defaultOptionN).getValue(), description, isEditable, isHidden, isNeverEditable, ptype);
		this.optionValues = options;
		this.defaultOptionNumber = defaultOptionN;
		this.currentNumber = currentNumber;
		}

	/**
	 * Gets default option. 
	 * If {@link #defaultOptionNumber} is set then gets corresponding option from the list.
	 * If {@link #defaultOptionNumber} is set to -1 then checks if any option exist and gets first one
	 * @return
	 */
	private static Option getDefaultOption(Option[] optionValues, int defaultOptionNumber) {
		if(optionValues != null) {
			if (defaultOptionNumber != -1) {
				return optionValues[defaultOptionNumber];
			}
			else {
				return optionValues[0];
			}
		}
		else {
			return new Option("No options to choose", "");
		}
	}
	/**
	 * Sets the value which is represented as string.
	 * This value is considered to be the label of one of the options.
	 * If there is no options,  it is saved as string
	 * @param value
	 */
	public void setValue(String value) {
		// If there is no options then store as a value
			// Search through the different options
			currentNumber = -1;
			for(int i = 0; i < optionValues.length; i++) {
				String val1 = optionValues[i].value;
				String val2 = value;
				if (val1.equals(val2)) {
					currentNumber = i;
					break;
				}
			}
	}
	
	public void setDefaultItemNumber(int n) {
		defaultOptionNumber = n;
	}
		
	
	public Property clone(){ 
//		if (opt)
			return new OptionProperty(this.name, optionValues, defaultOptionNumber, currentNumber, this.type, this.defaultValue,
					this.description, this.isEditableNow, this.isHidden, this.isNeverEditable, this.property_type);
	}

	public Object getValue() {
			return optionValues;
	}

	public int getItemNumberToShow() {
			if (currentNumber >=0 ) {
				return currentNumber;
			}
			if (defaultOptionNumber >= 0) {
				return defaultOptionNumber;
			}
			if(optionValues != null) {
				return 0;
			}
			return -1;
	}
	
	public String getDefaultValue() {
		Option defaultOption = getDefaultOption(optionValues, defaultOptionNumber);
		return defaultOption.getValue();
//		if (defaultOptionNumber >= 0) {
//			return optionValues[defaultOptionNumber].getValue();
//		}
//		else {
//			return optionValues[0].getValue();
//		}
	}
	
	/**
	 * Sets default option based on the given string value.
	 * Value is considered to be a label of the option which should be set as default
	 * If there is no value in the options, then -1 is set.
	 */
	public void setDefaultValue(String value) {
		defaultOptionNumber = -1;
		for (int i = 0; i < optionValues.length; i++) {
			if (optionValues[i].getValue().equals(value)) {
				defaultOptionNumber = i;
				break;
			}
		}
	}
	
	/**
	 * For OptionProperty returns a value of the option which is currently selected or default one.
	 * If there is no options it returns the string value.
	 * 
	 */
	public String getStringValue() {
			if (optionValues != null) {
				// Return one of the options
				if (currentNumber >=0) {
					return optionValues[currentNumber].getValue();
				}
				else {
					return getDefaultOption(optionValues, defaultOptionNumber).getValue();
				}
			}
			else return null;
	}
	
	@Override
	public boolean isNull() {
		if (optionValues == null) {
			return true;
		}
		else {
			return false;
		}
	}
}
