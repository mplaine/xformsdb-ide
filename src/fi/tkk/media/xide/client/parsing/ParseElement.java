package fi.tkk.media.xide.client.parsing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.NamedNodeMap;


public class ParseElement {
	
	public static final String FULL 		= "full";
	public static final String COMPACT		= "compact";
	public static final String MIN 			= "minimal";
	
	protected HashMap<String, String> parameters;
	protected ArrayList<ParseElement> children;
	protected String value = null;
	protected String elementName;
	
	public ParseElement(String name) {
		elementName = name;
		children = new ArrayList<ParseElement>();
		parameters = new HashMap<String, String>();
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String v) {
		value = new String(v);
	}
	
	public String getName() {
		return elementName;
	}
	
	/**
	 * Finds element which name equals to the requested one. Returns first appropriate element frm the list.
	 * If no element found returns null.
	 * @param name String representing name of the searching element
	 * @return first element which name matches the search string
	 */
	public ParseElement getChildByName(String name) {
		ParseElement result = null;
		for (Iterator<ParseElement> iterator = children.iterator(); iterator.hasNext();) {
			ParseElement o = iterator.next();
			if (o.getName().equals(name)) {
				result = o;
			}
		}
		return result;
		
	}
	
	public String getAttributeValueByName(String attName) {
		return parameters.get(attName);
		
	}
			
	public void setParameters (String parameterName, String parameterValue){
		parameters.put(parameterName, parameterValue);
	}

	// Returns new instance of the element class
	public ParseElement getInstance(){
		return null;
	}

	public ArrayList<ParseElement> getChildren(){
		return children;
	}
	//Adds child to the child list
	public void addChild(ParseElement e) {
		children.add(e);
	}

	// Draws element UI. Should be used after parsing has finished
	public Widget draw(){
//		System.out.println("ParseElement: CLass "+ this.toString()+ " does not support drawing without parameters");
		return null;
	} 
}

