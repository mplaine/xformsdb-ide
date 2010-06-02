package fi.tkk.media.xide.client.popups;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.TreeListener;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.Data.Selectable;
import fi.tkk.media.xide.client.parser.Document;
import fi.tkk.media.xide.client.parser.Node;
import fi.tkk.media.xide.client.parsing.HTMLElement;
import fi.tkk.media.xide.client.parsing.ParseElement;
import fi.tkk.media.xide.client.parsing.MyXMLParser.ComponentCallXMLInfo;
import fi.tkk.media.xide.client.parsing.MyXMLParser.SlotXMLInfo;
import fi.tkk.media.xide.client.parsing.elements.support.SlotElement;
import fi.tkk.media.xide.client.popups.utils.PopupContainerBase;
import fi.tkk.media.xide.client.popups.utils.PopupWithScrollBase;

public class PopupSelectXMLElement extends PopupWithScrollBase{

	String xml;
	String instanceName;
	Button nextButton;
	Property property;
	Tree tree;
	
	TextArea selectedItemCode;
	
	public class TreeItemXML extends TreeItem{
		private Node node;
		
		public TreeItemXML (String text, Node node) {
			super(text);
			this.node = node;
		}
		
		public Node getNode() {
			return node;
		}
	}
	public PopupSelectXMLElement(String instanceName, String xml, Property propertyToUpdate) {
		super("Binding Wizard: select element (2/2)"); 
		DOM.setStyleAttribute(popup.getElement(), "width", "800px");
		
		Label headerDescr = new Label("Now you can see all xml elemwnts of the selected instance. You can click on the element you want to bind to, and the corresponding binding expression (parameter value) " +
				"will be generated. You can edit resulting value by hands as well. Please select the element you want to use for binding and/or modify resulting expression and click OK button");
		PopupContainerBase.addStyle(headerDescr, PopupContainerBase.HEADER_DESCRIPTION);
		
		AbsolutePanel main = new AbsolutePanel();
		this.property = propertyToUpdate;
		this.xml = xml;
		this.instanceName = instanceName;
		
//		Label selectedItemDescr = new Label("Selected element code: ");
		selectedItemCode = new TextArea();
		selectedItemCode.setSize("100%", "44px");
		DOM.setStyleAttribute(selectedItemCode.getElement(), "fontFamily", "arial,sans-serif");
		DOM.setStyleAttribute(selectedItemCode.getElement(), "padding", "3px 0px 8px 12px ");
		selectedItemCode.setWidth("100%");
//		selectedItemCode.setEnabled(false);
		
		tree = new Tree();
//		tree.
		fillTree();
		tree.addSelectionHandler(new SelectionHandler<TreeItem>(){

			public void onSelection(SelectionEvent<TreeItem> event) {
				// Unlock next(finish) button since a selection has been made
//				nextButton.setEnabled(true);
				
				// Set a text of selected item to the text box
				// Tree item should be an isnance of TreeItemXML
				if (event.getSelectedItem() instanceof TreeItemXML) {
					TreeItemXML item = (TreeItemXML) event.getSelectedItem();
					selectedItemCode.setText(getSelectedItemCode(item.getNode()));
				}
				
			}});
//		tree.addTreeListener(new TreeListener() {
//
//			public void onTreeItemSelected(TreeItem item) {
//				System.out.println("item has been selected");
//				DOM.setStyleAttribute(item.getWidget().getElement(), "border", "1px solid red");				
//			}
//
//			public void onTreeItemStateChanged(TreeItem item) {
//				
//			}
//			
//		});
		main.add(headerDescr);
//		main.add(selectedItemDescr);
		main.add(selectedItemCode);

		popup.addHeader(main);
		popup.addContent(tree);
		
//		addPreviousButton();
		
		popup.addButton("OK", new ClickHandler() {

			public void onClick(ClickEvent event) {
				popup.hide();
				property.setValue(selectedItemCode.getText());
				Main.getInstance().UpdateUI(Main.BOTTOM_TAB);				
			}});
		
		
		popup.addCloseButton("Cancel");
		
		popup.showPopup();
	}
	

	
	private String getSelectedItemCode(Node node) {
		String result = "";
		if (node.getParent() != null) {
			if (node.getType() == Document.DOM_ELEMENT_NODE) {
				int number = getNumber(node.getParent().getChildNodes(), node);
				result = "/" + node.getName() + "[ " + number + " ]" + result;
			}
			else if (node.getType() == Document.DOM_TEXT_NODE) {
				result = " /text()";
			}
	
			
	
			Node currentItNode = node.getParent();
			
			while(currentItNode.getParent() != null) {
				int number = getNumber(currentItNode.getParent().getChildNodes(), currentItNode);
				result = " /" + currentItNode.getName() + "[ " + number + " ]" + result;
				currentItNode = currentItNode.getParent();
			}
		}
		
		result = "instance(&apos;" + instanceName.substring(0, instanceName.indexOf(".xml")) + "&apos; )" + result;

		return result;
		
	}
	
	public void fillTree() {
		// 
		Document doc = Document.xmlParse(xml);
		 
		trimExtraTextFromDocument(doc);
//		System.out.println(string);

		// Create structure of the parsed elements
		parseDocument(doc.getChildren().get(0), null);

	}
	

	
	 private int getNumber(ArrayList<Node> list, Node node) {
		int i = 1;
		for (Iterator<Node> iterator = list.iterator(); iterator.hasNext();) {
			Node itNode = iterator.next();
			if (itNode == node) {
				break;
			}
			if (itNode.getName().equals(node.getName())) {
				i++;
				
//				break;
			}
		}
		return i;
		
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
	
	 /**
	  * Parses the tree element starting from headNode and adds corresponding tree items to the parent item
	  * @param headNode
	  * @param parenItem
	  */
	private void parseDocument(Node headNode, TreeItem parenItem) {

		// Tree item which represents current node
		TreeItemXML currentItem = null;
		
		if (headNode.getType() == Document.DOM_ELEMENT_NODE) {
			currentItem = new TreeItemXML("" + headNode.getName(), headNode);
			// Element can contain children elements
			// Iterate and parse children
			ArrayList<Node> nodelist = headNode.getChildNodes();
			for (int i = 0; i < nodelist.size(); i++) {
					parseDocument(nodelist.get(i) ,currentItem);
			}
		}
		else if (headNode.getType() == Document.DOM_TEXT_NODE) {
			currentItem = new TreeItemXML("" + headNode.getValue(), headNode);
		}

		if (parenItem == null) {
			//That's the root element
			// Add directly to the tree
			tree.addItem(currentItem);
		}
		else {
			// Add to parent item
			parenItem.addItem(currentItem);
			parenItem.setState(true);
		}

	}
	
}
