package fi.tkk.media.xide.server;

//import ParserWrapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Level;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fi.tkk.media.xide.client.Data.BaseProperty;
import fi.tkk.media.xide.client.Data.Option;
import fi.tkk.media.xide.client.Data.OptionProperty;
import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.Server.RPC.PublishException;
import fi.tkk.media.xide.client.Server.RPC.XIDEException;
import fi.tkk.media.xide.client.UI.Widget.Component;
import fi.tkk.media.xide.client.UI.Widget.Slot;
import fi.tkk.media.xide.client.UI.Widget.WebPage;
import fi.tkk.media.xide.client.parsing.HTMLElement;
import fi.tkk.media.xide.client.parsing.ParseElement;
import fi.tkk.media.xide.client.parsing.elements.dynamic.RepeatElement;
import fi.tkk.media.xide.client.parsing.elements.dynamic.SwitchElement;
import fi.tkk.media.xide.client.parsing.elements.form.InputElement;
import fi.tkk.media.xide.client.parsing.elements.form.LabelElement;
import fi.tkk.media.xide.client.parsing.elements.form.RangeElement;
import fi.tkk.media.xide.client.parsing.elements.form.SecretElement;
import fi.tkk.media.xide.client.parsing.elements.form.Select1Element;
import fi.tkk.media.xide.client.parsing.elements.form.SelectElement;
import fi.tkk.media.xide.client.parsing.elements.form.SubmitElement;
import fi.tkk.media.xide.client.parsing.elements.form.TextAreaElement;
import fi.tkk.media.xide.client.parsing.elements.support.CaseElement;
import fi.tkk.media.xide.client.parsing.elements.support.ItemElement;
import fi.tkk.media.xide.client.parsing.elements.support.SlotElement;
import fi.tkk.media.xide.client.parsing.elements.support.WrapElement;
import fi.tkk.media.xide.client.popups.utils.interfaces.Action;
import fi.tkk.tml.xformsdb.xml.sax.XFormsDBHandler;

public class PageXMLParser {
	// Name of the attributes of the template:param element
	public static final String PARAM_NAME = "name";
	public static final String PARAM_DESCR = "description";
	public static final String PARAM_TYPE = "type";
	
	// Name of the attributes of the template:option element
	public static final String OPTION_DESCR = "description";
	public static final String OPTION_SELECTED = "selected";
	
	
    /** Default parser name (dom.wrappers.Xerces). */
    protected static final String DEFAULT_PARSER_NAME = "fi.tkk.media.xide.server.Xerces";
    
	ArrayList<String> slots; 

	public static int slotCounter = 0;

	// Map contains different html-xformsdb elements which can be parsed 
	// together with corresponding ParseElement instance.
	HashMap<String, Action> elementMap;
	
	
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
	
	private ArrayList<Option> lastParamOptions;
	int lastDefaultParamOption;

	Template template;
	
	// Initializes the map if necessary and 
	// fills it with elements. Includes all elements which could be parsed.
	private int CreateElementMap(){
		if (elementMap == null) {
			elementMap = new HashMap<String, Action>(10);
		}
		try {
			elementMap.put("template:param", new Action() {

				public void doAction() {

					// If there is no options for this parameter
					if (lastParamOptions.size() == 0) {
						// Create a property which has only one value
								template.addParameter(attributes.get(PARAM_NAME), 
										new BaseProperty(attributes.get(PARAM_NAME), null, attributes.get(PARAM_TYPE), value, 
												attributes.get(PARAM_DESCR), Property.TYPE_PARAMETER));
					}
					else {
						template.addParameter(attributes.get(PARAM_NAME), 
								new OptionProperty(attributes.get(PARAM_NAME), lastParamOptions.toArray(new Option[lastParamOptions.size()]),
										lastDefaultParamOption,
										attributes.get(PARAM_TYPE),  
										attributes.get(PARAM_DESCR), false));
					}
//					attributes.remove(PARAM_NAME);
//					attributes.remove(PARAM_TYPE);
//					attributes.remove(PARAM_DESCR);
					value = null;
					lastParamOptions.clear();
					lastDefaultParamOption = -1;
				}});
			elementMap.put("template:option", new Action() {

				public void doAction() {
					if(attributes.containsKey(OPTION_SELECTED)) {
						// This option is a default one
						lastDefaultParamOption = lastParamOptions.size();
						attributes.remove(OPTION_SELECTED);
					} 
					lastParamOptions.add(new Option(attributes.get(OPTION_DESCR), value));
//					attributes.remove(OPTION_DESCR);
				}});
			
			elementMap.put("template:container", new Action() {
				
				public void doAction() {
//					webPage.AddChild(new Slot(null, webPage), 0);
					
				}});
			elementMap.put("template:call-component", new Action(){

				public void doAction() {
					
				}});
		}
		catch(Exception exc){
			return -1;
		}
		return 0;
	}
	
	// Initializes parser 
	public PageXMLParser(Template template)  throws XIDEException{
		lastParamOptions = new ArrayList<Option>();
		lastDefaultParamOption = -1;
		
		//Initialize the map
		CreateElementMap();
		
//		template = Template.fakeTemplate("web page", "w_p_1");
		this.template = template;
		String document = null;
		// Parse the XML document into a DOM
		if (template.getSourceCodeFirstFile() != null) {
			document = template.getSourceCodeFirstFile().getAbsolutePath();
		}
		else {
			throw new XIDEException("Source code of this page was not found!");
		}
		
		children = new ArrayList<Node>();
		attributes = new LinkedHashMap<String, String>();
		
		// second iteration
		Document d = null;
		try {
			// Set the DTD to point to right folder

			XFormsDBHandler xformsdbHandler = new XFormsDBHandler();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			dbf.setValidating(false);

			DocumentBuilder db = null;

			db = dbf.newDocumentBuilder();

			db.setEntityResolver(xformsdbHandler);
			
			d = db.parse(document);
			
	    	if (d.getChildNodes().getLength() > 1) {
    		parseDocument(d.getChildNodes().item(1));
    	}
    	else {
    		parseDocument(d.getChildNodes().item(0));
    	}
			
		} catch (ParserConfigurationException e) {
			throw new XIDEException("Error while parsing a page source code! " + e.getMessage());
		} catch (SAXException e) {
			throw new XIDEException("Error while parsing a page source code! " + e.getMessage());
		} catch (IOException e) {
			throw new XIDEException("Error while parsing a page source code! " + e.getMessage());
		}
		
//	    ParserWrapper parser = null; 
//	    if (parser == null) {
//
//	         // create parser
//	         try {
//	             parser = (ParserWrapper)Class.forName(DEFAULT_PARSER_NAME).newInstance();
//	             try { 
//	            	 parser.setFeature("http://xml.org/sax/features/validation", false); 
//	            	 parser.setFeature("http://xml.org/sax/features/external-general-entities", false); 
//	            	 parser.setFeature("http://xml.org/sax/features/external-parameter-entities", false); 
//	            	 parser.setFeature("http://apache.org/xml/features/continue-after-fatal-error", true); 
//	            	 parser.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false); 
//	            	 parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false); 
//	            		 } catch (SAXException e) 
//	            		 { 
//	            		 System.out.println("error in setting up parser feature"); }
//	         }
//	         catch (Exception e) {
//	             System.err.println("error: Unable to instantiate parser ("+DEFAULT_PARSER_NAME+")");
//	         }
//	     }
//		    Document d = null;
//		    try {
//		    	d = parser.parse(document); 
//		    	if (d.getChildNodes().getLength() > 1) {
//		    		parseDocument(d.getChildNodes().item(1));
//		    	}
//		    	else {
//		    		parseDocument(d.getChildNodes().item(0));
//		    	}
//				
//		    }
//		    catch (SAXException e) {
//		    	  System.out.println("SAX exception " + e.getMessage()); 
//		    }
//		    catch (IOException e) { 
//		      System.out.println(
//		       "Due to an IOException, the parser could not check " 
//		       + document + "\n" +  e.getMessage()
//		      ); 
//		    } catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		
		
           // use default parser?
//        if (parser == null) {
//
//            // create parser
//            try {                
//            	// Old parser
////            	parser = (ParserWrapper)Class.forName(DEFAULT_PARSER_NAME).newInstance();
//            
//            	final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
//            	
//
//                factory.setExpandEntityReferences(false);
//                
//                final Document doc = factory.newDocumentBuilder().parse(document);
//
////                final Element dummyRootElement = (Element)doc.getFirstChild();
//                System.out.println(doc.getChildNodes().item(1)); 
//                parseDocument(doc.getChildNodes().item(1));
//            }
//            catch (Exception e) {
//                System.err.println("error: Unable to instantiate parser ("+DEFAULT_PARSER_NAME+")");
//                System.err.println("error: " + e.getMessage());
//            }
//        }
//	    Document d = null;
//	    try {
//	    	
//	    	d = parser.parse(document); 
//	    	parseDocument(d.getChildNodes().item(1));
//			
//	    }
//	    catch (SAXException e) {
//	      System.out.println(document + " is not well-formed.");
//	    }
//	    catch (IOException e) { 
//	      System.out.println(
//	       "Due to an IOException, the parser could not check " 
//	       + document
//	      ); 
//	    } catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	    
//		Document doc = Document.xmlParse(template.getSourceCode().get(
//				template.getSourceCode().keySet().iterator().next()).getContent());
//			 
//		trimExtraTextFromDocument(doc);

	}
	
	HashMap<String, String> attributes;
	String value;
	ArrayList<Node> children;
	
	private String encodeIntoEntities(String string) {
		String result = string;
		// Replacing basic sybols: <, >, ', " and &
		result = result.replaceAll("&", "&amp;");
		result = result.replaceAll("<", "&lt;");
		result = result.replaceAll(">", "&gt;");
		result = result.replaceAll("'", "$apos;");
		result = result.replaceAll("\"", "&quot;");
		
		return result;
	}
	
	private void parseDocument(Node headNode) {
		// If the map contains current element 
		if (elementMap.containsKey(headNode.getNodeName())) {

			// Parsing parameters
			for (int i = 0; i < headNode.getAttributes().getLength(); i++) {
				String attName = headNode.getAttributes().item(i).getNodeName();
				String attValue = encodeIntoEntities(headNode.getAttributes().item(i).getNodeValue());
				
				attributes.put(attName, attValue);
			}
			
				// Create child elements, if any
				NodeList nodelist = headNode.getChildNodes();
				for (int i = 0; i < nodelist.getLength(); i++) {

					// If child node is element node
					if (nodelist.item(i).getNodeType() == Node.ELEMENT_NODE) {
						parseDocument(nodelist.item(i));
						children.add(nodelist.item(i));
					}
					// If child node is text node
					else if (nodelist.item(i).getNodeType() == Node.TEXT_NODE) {
						value = encodeIntoEntities(nodelist.item(i).getNodeValue());
					} 
					// Else unknown type of element
					else {
						System.out.println("Unknown type of node: "
								+ headNode.getNodeType());
					}
				}
				
			elementMap.get(headNode.getNodeName()).doAction();

		}
		
//		else if (headNode.getNodeName().equals("slot")) {
//			// Fake adding. Slot
//			
//		}
		else {
		
		
			// Create child elements, if any
			NodeList nodelist = headNode.getChildNodes();
			for (int i = 0; i < nodelist.getLength(); i++) {
				// If child node is element node
				if (nodelist.item(i).getNodeType() == Node.ELEMENT_NODE) {
					parseDocument(nodelist.item(i));
				}
			}
		}
	}
	
//	 private void trimExtraTextFromDocument(Document document) {
//         for(int i=0; i<document.getChildren().size(); i++) {
//                 trimExtraTextNodes((Node) document.getChildren().get(i));
//         }
// }
//
//	 private void trimExtraTextNodes(Node node) {
//         List textSubNodes = new ArrayList();
//         int subElementNodes = 0;
//
//         for(int i=0; i<node.getChildNodes().size(); i++) {
//                 Node subNode = (Node) node.getChildNodes().get(i);
//                 if (subNode.getType().equals(Document.DOM_ELEMENT_NODE)) {
//                         subElementNodes++;
//                         trimExtraTextNodes(subNode);
//                 } else if (subNode.getType().equals(Document.DOM_TEXT_NODE)) {
//                         textSubNodes.add(subNode);
//                 }
//         }
//
//         if (subElementNodes > 0) {
//                 // There can be no text node children if there is at least one
//                 // element node.  Nuke all of the text children.
//                 for(int i=0; i<textSubNodes.size(); i++) {
//                         Node textNode = (Node) textSubNodes.get(i);
//                         node.removeChild(textNode);
//                 }
//         }
// } 

}
