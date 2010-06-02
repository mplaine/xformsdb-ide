/**
 * 
 */
package fi.tkk.media.xide.client.UI.Widget;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.DnDController;
import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.Data.AccessRights;
import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.Data.SaveObjectsListener;
import fi.tkk.media.xide.client.Data.Selectable;
import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.DnD.DemoFlexTable;
import fi.tkk.media.xide.client.DnD.FlexTableRowDragController;
import fi.tkk.media.xide.client.DnD.FlexTableRowDropController;
import fi.tkk.media.xide.client.DnDTree.DnDTreeItem;
import fi.tkk.media.xide.client.Tabs.NavigatorTab;
import fi.tkk.media.xide.client.Tabs.PLTab;
import fi.tkk.media.xide.client.utils.Icons;

/**
 * @author Evgenia Samochadina
 * @date Dec 8, 2008
 * 
 */
public class Slot extends FlexTable implements BasicPageElement {
	private ArrayList<Integer> accessRightsSettings;
	
	public static String WELCOME_STRING = "Drop new component here...";
	Template template;
	Component parent;
	ArrayList<BasicPageElement> children;
	private DnDTreeItem treeItem = null; 
	// Shows whether component is empty (only pne fake label is displayed)
	private boolean isEmpty = true; 
	private boolean isChanged;
	public FlexTableRowDropController dropController;
	/**
	 * @param template
	 * @param parent
	 */
	public Slot(Template template, Component parent) {
		super();
		addStyleName("design-Slot");

		this.template = template;
		setParentElement(parent);
		
		this.children = new ArrayList<BasicPageElement>();
		this.sinkEvents(Event.ONMOUSEDOWN);
//		this.sinkEvents(Event.ONCLICK);

		// TODO Add slot as a drop target for dnd from search tab
		// TODO Add slot as a drop target for dnd between slots
	}
	
	public void onBrowserEvent(Event event) {
		// + get size of the component when dnd
		if ((DOM.eventGetType(event) == Event.ONMOUSEDOWN) ){
			if (event.getCurrentEventTarget() == event.getEventTarget() ){
				// This is an event from the slot. Process event
				Selectable selectedElement = Main.getInstance().getSelectedElement();
				if (selectedElement != this) {
					Main.getInstance().setSelectedElement(this);
					event.stopPropagation();
				}
				else {
					event.stopPropagation();
//					super.onBrowserEvent(event);
				}
				if (DOM.eventGetButton(event)==Event.BUTTON_RIGHT) {
					
				}
			}
			else {
				event.stopPropagation();
			}
//			else{
//				// event from Component. Process further
//				super.onBrowserEvent(event);
//			}
		
		}
		
//		Selectable selectedElement = Main.getInstance().getSelectedElement();
//		// If this element is not already selected and 
//		// This element is a target of event (it is not bubbled event)
//		if ((selectedElement != this) &&
//				(this.getElement() == event.getTarget())) {
//			// Select this element
//			Main.getInstance().setSelectedElement(this);
//		}
	
	}

	public String getTypeName() {
		return "container";
	}

	public String getXMLCode() {
		String result = null;
		result = "<template:container>\n";
		result += "<template:meta name=\"title\">" + template.properties.get(Property.TITLE).getStringValue() + "</template:meta>\n";
		result += "<template:meta name=\"description\">"+ template.properties.get(Property.DESCR).getStringValue() + "</template:meta>\n";
		result += "<template:head />\n";
		result += "		<template:body>\n";
		for (Iterator<BasicPageElement> iterator = children.iterator(); iterator.hasNext();) {
			result += iterator.next().getXMLCode();
		}
		
		result += "		</template:body>\n";
		result += "</template:container>";

		return result;
	}
	
	public Widget GetPanel() {
		return this;
	}
	
	public void Draw() {
		// ParseCode
		Draw(false);

	}

	public void Draw(boolean isSlotProxy) {
		this.setWidth("95%");
		this.setHeight("40px");
		this.addStyleName("design-Slot");
		if (!isSlotProxy) {
			DnDController.addDropController(this);
		}
		if (!children.isEmpty()) {
			DrawChildren();
			}
		else {
			setWidget(0, 0, new Label (WELCOME_STRING));
		}
		// Label addHereLabel = new Label("Add component here..");
		// addHereLabel.setStyleName("design-LabelMiddleText ");
		// this.add(addHereLabel);

		// for (Iterator<Component> iterator = children.iterator();
		// iterator.hasNext();) {
		// Component pageElement = iterator.next();
		//
		// // TODO do not need this draw here? Just one draw when component is
		// added on the page
		// pageElement.Draw();
		// this.add(pageElement);
		// }
		
	}

	public void SaveAsNewComponent() {
		// TODO Auto-generated method stub

	}
	
	public void delete() {
		for (int i =children.size()-1; i >= 0; i--) {
			children.get(i).delete();
		}
		NavigatorTab.getInstance().removeElement(treeItem);		
	}

	public void DrawChildren() {
		DrawChildren(false);
	}
	
	public void DrawChildren(boolean isComponentProxy) {
		for (Iterator<BasicPageElement> iterator = children.iterator(); iterator.hasNext();) {
			BasicPageElement o =  iterator.next();
			o.Draw(isComponentProxy);
		}
	}
	
	
	/**
	 * Adds new child element. Used when new component or slot should be added to the slot. 
	 * Determines the time of adding element and calls corresponding method 
	 */
	public void AddChild(BasicPageElement element, int row) {
		// If adding element is a component
		if (element instanceof Component) {
			AddComponentChild((Component) element, row);
		}
		// else if adding element is a slot
		else if (element instanceof Slot) {
			AddSlotChild((Slot) element, row);
		}
//		System.out.println(getXMLCode());
		parent.updateSlotInfo();
	}
	


	/**
	 * Adds slot as a child
	 * @param slot adding slot
	 * @param row place where to add
	 */
	public void AddSlotChild(Slot slot, int row) {
		setWidget(row , 0, slot);
		children.add(row, slot);
		
		NavigatorTab.getInstance().addElement(this.getTreeItem(), slot, row);
		// TODO: add parent to Slot
		//slot.setParent(this);
		isEmpty = false;
	}
	

	/**
	 * Adds component as a child
	 */
	public void AddComponentChild(Component component, int row) {
		//component.Draw();
		if (isEmpty) {
			// Remove line representing welcome string 
			removeRow(0);
			// Add element on the first row 
			row = 0;
		}
		insertRow(row);
		// Set adding component on the corresponding place in the slot
		setWidget(row, 0, component);
		// Make newly added component draggable for Mooving
		DnDController.getDragControllerMoving().makeDraggable(component);
		// Logically add new component in the list of slot children 
		children.add(row, component);
		
		component.setParentElement(this);
		

//		// NavigatorTab managing!
//		NavigatorTab.getInstance().addElement(this.getTreeItem(), component, row);
		
		// Set flag
		isEmpty = false;
	}

//	/**
//	 * Adds component as a child
//	 */
//	public void AddComponentChild(Component component, int row) {
//		//component.Draw();
//		if (isEmpty) {
//			System.out.println("Add component: is empty!");
//			// Remove line representing welcome string 
//			removeRow(0);
//			// Add element on the first row 
//			row = 0;
//		}
//		// Set adding component on the corresponding place in the slot
//		setWidget(row, 0, component);
//		// Make newly added component draggable for Mooving
//		Main.getInstance().getDragControllerMoving().makeDraggable(component);
//		// Logically add new component in the list of slot children 
//		children.add(row, component);
//		// error^
//		
//		component.setParentElement(this);
//		
//
////		// NavigatorTab managing!
////		NavigatorTab.getInstance().addElement(this.getTreeItem(), component, row);
//		
//		// Set flag
//		isEmpty = false;
//	}

	/**
	 * Updates representation of the slot in the page file
	 * 
	 */
	public void updateSlotInfo() {
		// Get source code of the page
		String code = parent.template.getSourceCodeFirstFile().getContent();
		
//		int slot_id = code.indexOf("\"" + template.getProperties().get(Property.ID).getStringValue()+ "\"");
		
		//find the place where <template:meta name="title">component_id</template:meta> is set
		int slot_id = code.indexOf("<template:meta name=\"title\">" + template.getProperties().get(Property.TITLE).getStringValue()+"</template:meta>");
			
		int start = code.indexOf("<template:container");
		int end = code.indexOf("</template:container>");
		// <template:container id = "another_slot_id"> ...
		// <template:container id = "slot_id">..
		
		// slot_id = 50
		// start = 1
		// indexof = 1 
		
		//    end of previous slot is less then id and next slot exist
		while (end < slot_id && code.indexOf("<template:container", end) != -1) {
			// iterate to the next slot
			start = code.indexOf("<template:container", end);
			end = code.indexOf("</template:container>", end + 1);
		}
		
		if (end >= slot_id) {
			// this is the slot with required id
			
			// count </template:container> letters as well
			end += 21;
			String newCode = code.substring(0, start) + getXMLCode() + code.substring(end, code.length());
			parent.template.getSourceCodeFirstFile().updateContent(newCode);
		}
		else {
			// no slot has been found
		}
		
		
			
		// count </template:container> letters as well
//		end += 21;
//		String newCode = code.substring(0, start) + slot.getXMLCode() + code.substring(end, code.length());
//		template.getSourceCodeFirstFile().updateContent(newCode);
//		previousSlotEnd = start + slot.getXMLCode().length();
		
	}
	public void RemoveComponent(Component component) {
		
		int row = children.indexOf(component);
		remove(component);
		this.removeRow(row);
		children.remove(component);
		component.setParentElement(null);
		
		if (children.isEmpty()) {
			isEmpty = true;
			insertRow(0);
			setWidget(0, 0, new Label (WELCOME_STRING));
		}
		// TODO: remove component from drag elements
	}

	public ArrayList<BasicPageElement> getChildrenElements() {
		return children;
	}

	public BasicPageElement getParentElement() {
		return parent;
	}

	public Template getTemplate() {
		return template;
	}
	
	public DnDTreeItem getTreeItem() {
		return treeItem;
	}
	
	public boolean isEmpty() {
		return isEmpty;
	}
	
	public void setEmpty() {
		isEmpty = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fi.tkk.media.xide.client.UI.Widget.BasicPageElement#setChildrenElements
	 * (java.util.ArrayList)
	 */
	public void setChildrenElements(ArrayList<BasicPageElement> children) {
		this.children = children;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fi.tkk.media.xide.client.UI.Widget.BasicPageElement#setParentElement(
	 * fi.tkk.media.xide.client.UI.Widget.BasicPageElement)
	 */
	public void setParentElement(BasicPageElement parent) {
		this.parent = (Component)parent;
		if (parent != null) {
			accessRightsSettings = AccessRights.getAccessRightsSettingsForChild(parent.getAccessRightsSettings());
		}
		else {
			accessRightsSettings = new ArrayList<Integer>();
		}
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public ArrayList<Selectable> GetLinkedObjects() {
		ArrayList<Selectable> list  = new ArrayList<Selectable>();
		if (treeItem != null) {
			list.add(treeItem);
		}
		return list;
	}

	public Template getProperties() {
		return template;
	}
	
	public Selectable getValuableElement() {
		return this;
	}

	public void Select() {
		this.changeStyle(SELECTED);	
	}

	public void Unselect() {
		this.changeStyle(RESET);
	}

	public void setTreeItem(DnDTreeItem treeItem) {
		this.treeItem = treeItem;
	}

	/* (non-Javadoc)
	 * @see fi.tkk.media.xide.client.Data.Selectable#Changed()
	 */
	public void Changed() {
		changeStyle(CHANGED);
		isChanged = true;		
	}

	/* (non-Javadoc)
	 * @see fi.tkk.media.xide.client.Data.Selectable#Saved()
	 */
	public void Saved() {
		changeStyle(RESET);
		isChanged = false;
		
	}
	
	public void Saved(SaveObjectsListener listener) {
		Saved();
		listener.processResult(null);
	}

	public void afterSaved() {
		
	}
	public boolean isChanged() {
		return isChanged;
	}

	public void Canceled() {
		changeStyle(RESET);
		isChanged = false;
		
	}

	public void changeStyle(int event) {
		if ((event&Selectable.CHANGED) >0) {
			for (Iterator<Selectable> iterator = GetLinkedObjects().iterator(); iterator.hasNext();) {
				iterator.next().changeStyle(Selectable.CHANGED_LINKED);
			}
		}
		else if ((event&Selectable.CHANGED_LINKED) >0) {}
		else if ((event&Selectable.SELECTED) >0) {
			this.addStyleDependentName("selected");	
			for (Iterator<Selectable> iterator = GetLinkedObjects().iterator(); iterator.hasNext();) {
				iterator.next().changeStyle(Selectable.SELECTED_LINKED);
			}
		}
		else if ((event&Selectable.SELECTED_LINKED) >0) {
			this.addStyleDependentName("selected-linked");
		}
		else if ((event&Selectable.RESET) >0) {
			this.removeStyleDependentName("selected");	
			this.removeStyleDependentName("selected-linked");	
			if (Main.getInstance().getSelectedElement()== this) {
				for (Iterator<Selectable> iterator = GetLinkedObjects().iterator(); iterator.hasNext();) {
					iterator.next().changeStyle(Selectable.RESET);
				}
			}

			}
	}


	public ArrayList<Integer> getAccessRightsSettings() {
		return accessRightsSettings;
	}

	public void addNewRole(int roleID, int value) {
		accessRightsSettings.add(roleID, value);
		if (value == AccessRights.RIGHT_NOT_GRANTED) {
			value = AccessRights.RIGHT_DISABLED;
		}
		for (Iterator<BasicPageElement> iterator = children.iterator(); iterator.hasNext();) {
			iterator.next().addNewRole(roleID, value);
		}
	}

	public void removeRole(int roleID) {
		accessRightsSettings.remove(roleID);
		for (Iterator<BasicPageElement> iterator = children.iterator(); iterator.hasNext();) {
			iterator.next().removeRole(roleID);
		}
	}

	public void setRightToRole(int roleID, int value, boolean propagateToChildren) {
		accessRightsSettings.remove(roleID);
		accessRightsSettings.add(roleID, value);
		
		if (value == AccessRights.RIGHT_NOT_GRANTED) {
			value = AccessRights.RIGHT_DISABLED;
		}
		if ((propagateToChildren) || value == (AccessRights.RIGHT_NOT_GRANTED)) {
			for (Iterator<BasicPageElement> iterator = children.iterator(); iterator.hasNext();) {
				iterator.next().setRightToRole(roleID, value, propagateToChildren);
			}
		}
	}

	public void setAccessRightsSettings(ArrayList<Integer> accessRightsSettings) {
		this.accessRightsSettings = accessRightsSettings;
		
	}

	/* (non-Javadoc)
	 * @see fi.tkk.media.xide.client.UI.Widget.BasicPageElement#removeDropTarget()
	 */
	public void removeDropTarget() {
//		Main.getInstance().getDragControllerMoving().unregisterDropController(this.dropController);
		DnDController.removeDropController(this);

		if (!children.isEmpty()) {
			for (Iterator<BasicPageElement> iterator = children.iterator(); iterator.hasNext();) {
				iterator.next().removeDropTarget();
			}
			
		}
		
	}

	public void Deleted() {
		//
		this.delete();
	}
	
	public Image getImage() {
		return new Image(Icons.ICON_CONTAINER);
	}

}


