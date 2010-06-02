package fi.tkk.media.xide.client.Data;

import java.io.Serializable;

/**
 * Represents one possible option. Each option has a label (the string which is displayed to the user)
 * and the value (the string which is used as a value)
 *  
 * @author evgeniasamochadina
 *
 */
public class Option implements Serializable{
	
	// Label string
	String label;
	// Value string
	String value;
	
	/**
	 * Constructor required by serialization
	 */
	public Option () {}
	
	/*
	 * Constructs the option object
	 */
	public Option (String label, String value) {
		this.label = label;
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}
	
}