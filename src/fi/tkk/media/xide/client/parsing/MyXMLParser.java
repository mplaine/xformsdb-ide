package fi.tkk.media.xide.client.parsing;

//import parseElements.ParseElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import com.google.gwt.user.client.ui.Panel;
//import com.google.gwt.xml.client.DOMException;
//import com.google.gwt.xml.client.Document;
//import com.google.gwt.xml.client.Node;
//import com.google.gwt.xml.client.NodeList;
//import com.google.gwt.xml.client.Text;
//import com.google.gwt.xml.client.XMLParser;

import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.UI.Widget.Slot;
import fi.tkk.media.xide.client.parser.Document;
import fi.tkk.media.xide.client.parser.Node;
import fi.tkk.media.xide.client.parsing.elements.dynamic.*;
import fi.tkk.media.xide.client.parsing.elements.form.*;
import fi.tkk.media.xide.client.parsing.elements.support.*;


public class MyXMLParser {
	
	/**
	 * Contains 
	 * @author evgeniasamochadina
	 *
	 */
	public class SlotXMLInfo{
		// Id is an id of the div element which represents the slot on the page
		// Id is given when the slot is parsed
		String id;
		String title;
		String description;
		ArrayList<ComponentCallXMLInfo> children;
		
		public SlotXMLInfo(String id) {
			this.id = id;
			children = new ArrayList<ComponentCallXMLInfo>();
		}
		
		public void setTitle(String title) {
			this.title = title;
		}

		public void setDescription(String descr) {
			this.description = descr;
		}
		
		public void addChild(ComponentCallXMLInfo component) {
			children.add(component);
		}
		
		public String getId() {
			return id;
		}
		
		public String getTitle() {
			return title;
		}
		
		public String getDescr() {
			return description;
		}
		
		public ArrayList<ComponentCallXMLInfo> getChildren(){
			return children;
		}
		
	}
	
	
	public class ComponentCallXMLInfo{
		String componentID;
		LinkedHashMap<String, String> parameters;

		/**
		 * Default constructor creates an instance of component call. It is identified by component id.
		 */
		public ComponentCallXMLInfo(String id) {
			componentID = id;
			parameters = new LinkedHashMap<String, String>();
		}
		
		public void addParameter (String pName, String pValue) {
			parameters.put(pName, pValue);
		}
	
		public String getID() {
			return componentID;
		}
		
		public LinkedHashMap<String, String> getParameters(){
			return parameters;
		}
	}
	
	ArrayList <SlotXMLInfo> slotInfoList; 
	SlotXMLInfo lastSlot;
	ComponentCallXMLInfo lastCoomponent;
	
	public static int slotCounter = 0;

	// Map contains different html-xformsdb elements which can be parsed 
	// together with corresponding ParseElement instance.
	HashMap<String, ParseElement> elementMap;

	// Parse element which corresponds to the root element of the parsing XML 
	public ParseElement rootElement;
	
	/*
	 * Example of usage: 
	    MyXMLParser xm = 
    	new MyXMLParser(
    		"<repeat ref=\"refff\">" +
    			"<label>" +
    				"Please enter something:" +
    			"</label>" +
    		"</repeat>");
      ......
      verticalPanelLC.add((Panel) xm.rootElement.Draw());						
	 */
	
//	
//	public ArrayList<String> getSlotNames() {
//		return new ArrayList<String>(slotInfoList.keySet()); 
//	}
	
	/**
	 * Gets list of slots which have been parsed from the source code. 
	 * The list is made according to slots' order in the source code. 
	 */
	public ArrayList <SlotXMLInfo> getSlotInfoList(){
		return slotInfoList;
	}
	
	/**
	 * Gets slot info object according to it's number in the list
	 * @param slotNumber
	 * @return
	 */
	public SlotXMLInfo getSlotByNumber(int slotNumber) {
		return slotInfoList.get(slotNumber);
	}
	
	/**
	 * Gets children of the slot which has requested number in the list
	 * @param slotNumber
	 * @return
	 */
	public  ArrayList<ComponentCallXMLInfo> getSlotChildren(int slotNumber) {
		
		return slotInfoList.get(slotNumber).children;
	}
	
	/**
	 * Remove slot from the list of slots for parsing. 
	 * This operation is performed when the slot has been already parsed and added to the page.
	 * @param id
	 */
	public void removeSlotInfo(String id) {
		slotInfoList.remove(id);
	}
	
	// Initializes the map if necessary and 
	// fills it with elements. Includes all elements which could be parsed.
	
	private int CreateElementMap(){
		if (elementMap == null) {
			elementMap = new HashMap<String, ParseElement>(10);
		}
		try {
////			elementMap.put("select1", new Select1Element());
//			elementMap.put("input", new InputElement());
//			elementMap.put("secret", new SecretElement());
//			elementMap.put("label", new LabelElement());
//			elementMap.put("textarea", new TextAreaElement());
//			elementMap.put("range", new RangeElement());
//			elementMap.put("submit", new SubmitElement());
//			elementMap.put("select", new SelectElement());
//			elementMap.put("select1", new Select1Element());
//			elementMap.put("item", new ItemElement());
//			elementMap.put("repeat", new RepeatElement());
//			elementMap.put("switch", new SwitchElement());
//			elementMap.put("case", new CaseElement());
//			elementMap.put("sourcecode", new WrapElement());
//			elementMap.put("slot", new SlotElement());
			elementMap.put("xforms:input", new InputElement());
			elementMap.put("xforms:output", new OutputElement());
			elementMap.put("xforms:secret", new SecretElement());
			elementMap.put("xforms:label", new LabelElement());
			elementMap.put("xforms:textarea", new TextAreaElement());
			elementMap.put("xforms:range", new RangeElement());
			elementMap.put("xforms:submit", new SubmitElement());
			elementMap.put("xforms:trigger", new TriggerElement());
			elementMap.put("xforms:select", new SelectElement());
			elementMap.put("xforms:select1", new Select1Element());
			elementMap.put("xforms:item", new ItemElement());
			elementMap.put("xforms:itemset", new ItemSetElement());
			elementMap.put("xforms:repeat", new RepeatElement());
			elementMap.put("xforms:switch", new SwitchElement());
			elementMap.put("xforms:case", new CaseElement());
			elementMap.put("xforms:sourcecode", new WrapElement());
			elementMap.put("template:container", new SlotElement());
//			elementMap.put("template:call-component", new ComponentCallElement());
		}
		catch(Exception exc){
			return -1;
		}
		return 0;
	}
	
	// Initializes parser 
	public MyXMLParser(String string) {

//		myEmpls = new ArrayList();
//		stack = new Stack();
		
		//Initialize the map
		CreateElementMap();
		
		slotInfoList = new ArrayList<SlotXMLInfo>();
		lastSlot = null;
		lastCoomponent = null;
//		System.out.println("MYXMLParser: before parsing");
		// Parse the XML document into a DOM
		Document doc = Document.xmlParse(string);
//		System.out.println("MYXMLParser: after parsing");
			 
		trimExtraTextFromDocument(doc);
//		System.out.println(string);

		// Create structure of the parsed elements
		rootElement = parseDocument(doc.getChildren().get(0));

	}
	
	 private void trimExtraTextFromDocument(Document document) {
         for(int i=0; i<document.getChildren().size(); i++) {
                 trimExtraTextNodes((Node) document.getChildren().get(i));
         }
 }

	 private void trimExtraTextNodes(Node node) {
         List textSubNodes = new ArrayList();
         int subElementNodes = 0;

         for(int i=0; i<node.getChildNodes().size(); i++) {
                 Node subNode = (Node) node.getChildNodes().get(i);
                 if (subNode.getType().equals(Document.DOM_ELEMENT_NODE)) {
                         subElementNodes++;
                         trimExtraTextNodes(subNode);
                 } else if (subNode.getType().equals(Document.DOM_TEXT_NODE)) {
                         textSubNodes.add(subNode);
                 }
         }

         if (subElementNodes > 0) {
                 // There can be no text node children if there is at least one
                 // element node.  Nuke all of the text children.
                 for(int i=0; i<textSubNodes.size(); i++) {
                         Node textNode = (Node) textSubNodes.get(i);
                         node.removeChild(textNode);
                 }
         }
 } 
	
	private ParseElement parseDocument(Node headNode) {
//		System.out.print("parseDoc: " + headNode);
		ParseElement el;
		if (
				(lastSlot == null  && headNode.getName().equals("template:meta") )||
				headNode.getName().equals("template:head") ||
				headNode.getName().equals("template:param") ) {
			return null;
		}

		// If the map contains current element 
		if (elementMap.containsKey(headNode.getName())) {
			
			// Create current element
			el = ((ParseElement) elementMap.get(headNode
					.getName())).getInstance();
		}
		else {
			//TODO: check if element is a HTML tag or is unknown type!
			 el = new HTMLElement(headNode.getName());
//			 System.out.println(" is parsed as HTML");
		}

		// Pasting slot:
		if (headNode.getName().equals("template:container")
					) {
				
				((SlotElement)el).setID("service_slot_id_" + slotCounter);
				lastSlot = new SlotXMLInfo("service_slot_id_" + slotCounter);
				slotInfoList.add(lastSlot);
				slotCounter++;
			
			}
			// Parsing parameters
		for (Iterator<String> iterator = headNode.getAttributesNames().iterator(); iterator.hasNext();) {
			String attName = iterator.next();
			String attValue = headNode.getAttributes().get(attName);
//			if (headNode.getName().equals("template:container")
//				//	&& attName.equals("id")
//					) {
//				// Fake adding. Slot
//				slotCounter++;
//				System.out.println(" new slot " + "_"+ slotCounter  + " id = " + attValue);
//				((SlotElement)el).setID(attValue);
//				lastSlot = new SlotXMLInfo(attValue);
//				slotInfoList.add(lastSlot);
//			
//			}
//			else {
				el.setParameters(attName, attValue);
//			}
		}
		
		// If there is a slot already created
		if (lastSlot != null) {
			// Then we are interested in the following: 
			
			// TODO: combine this checks with the element map check 
			
			if (headNode.getName().equals("template:meta")){
				// Add slot info
				if (headNode.getAttributesNames().iterator().hasNext()) {
					String attName = headNode.getAttributesNames().iterator().next();
					String attValue = headNode.getAttribute(attName);
					// If there is a value under current headNode 
					// (which means there is a value of the template:meta)
					if (!headNode.getChildNodes().isEmpty()) {
						// If current template:meta info contains title
						if (attValue.equals("title")) {
							// Set title
							lastSlot.setTitle(headNode.getChildNodes().get(0).getValue());
						}
						// If current template:meta info contains description
						if (attValue.equals("description")) {
							lastSlot.setDescription(headNode.getChildNodes().get(0).getValue());
						}
					}
				}
				
			}
			
			if (headNode.getName().equals("template:call-component")){
				// Add component call to the last slot
				String attName = headNode.getAttributesNames().iterator().next();
				String componentID = headNode.getAttribute(attName);
				lastCoomponent = new ComponentCallXMLInfo(componentID);
				lastSlot.addChild(lastCoomponent);
			}
			if (headNode.getName().equals("template:with-param")){
				//Add parameter value to the last component in the last slot
				String pName = null;
				String pValue = null;
				// Iterate through element's attributes
				for (Iterator<String> iterator = headNode.getAttributesNames().iterator(); iterator.hasNext();) {
					String attName = iterator.next();
					String attValue = headNode.getAttributes().get(attName);
					if (attName.equals("name")) {
						pName = attValue;
					}
//					else if (attName.equals("select")) {
//						pValue = attValue;
//					}
				}
				// Get the value inside the element
				if (headNode.getChildNodes() != null) {
					// Sometimes a construction like this <template:with-param name="pRSSFeedURL"></template:with-param> 
					// is parsed like a childNodes array list with 0 children
					// To overcome this error a following check has added
//					if (headNode.getChildNodes().size() > 0) {
						pValue = headNode.getChildNodes().get(0).getValue();
//					}
				}
				
				
				// If there is appropriate pair of paramater name and value
				if (pName != null && pValue != null) {
					// Add it to the component
					lastCoomponent.addParameter(pName, Property.encodeEncodeIntoEntities(pValue));
				}
			}
		}
			// Create child elements, if any
			ArrayList<Node> nodelist = headNode.getChildNodes();
			for (int i = 0; i < nodelist.size(); i++) {
//				System.out.println("2: " + nodelist.item(i).getNodeType() + ","
//						+ nodelist.item(i).getNodeName() + ","
//						+ nodelist.item(i).getNodeValue() + ","
//						+ nodelist.item(i).getChildNodes().getLength());

				// If child node is element node
				if (nodelist.get(i).getType() == Document.DOM_ELEMENT_NODE) {
					ParseElement childElement = parseDocument(nodelist.get(i));
					
					if (childElement != null) {
						el.addChild(childElement);
					}
				}
				// If child node is text node
				else if (nodelist.get(i).getType() == Document.DOM_TEXT_NODE) {
					// TODO parser bug!!! parses space 
					el.setValue(nodelist.get(i).getValue());
				} 
				// Else unknown type of element
				else {
//					System.out.println("Unknown type of node: "
//							+ headNode.getType());
				}
			}
			return el;
//		} 
//		else {
//			return null;
//		}
	}
	
}
