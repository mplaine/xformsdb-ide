/**
 * 
 */
package fi.tkk.media.xide.client.Tabs;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.View;
import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.Data.Selectable;
import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.DnDTree.DnDTreeItem;
import fi.tkk.media.xide.client.UI.Widget.Component;
import fi.tkk.media.xide.client.UI.Widget.HorizontaPanelPLTab;
import fi.tkk.media.xide.client.utils.GoodHorizontalAdjustablePanel;

/**
 * @author Evgenia Samochadina
 * @date Dec 16, 2008
 * 
 */
public class PLTab extends TopTab {
	public final int tabID = View.PL;
	
	Tree tree;
	static PLTab instance;

	public static PLTab getInstance() {
		return instance;
	}
//	
//
//	class PLTree extends Tree {
//
//	}

	public PLTab() {
		super();
		
		instance = this;
		tree = new Tree() {
			
			public void onBrowserEvent(Event event) {
				// Disabling the default context menu for the tree
				disableContextMenu(getElement());

				// Parent method
				if ((DOM.eventGetType(event) == Event.ONMOUSEDOWN) || (DOM.eventGetType(event) == Event.ONMOUSEUP)){
					super.onBrowserEvent(event);
				}

				// Get current tree item
				PLTreeItem currentTreeItem = (PLTreeItem) tree.getSelectedItem();

				// When the button mouse is released
				
				if (DOM.eventGetType(event) == Event.ONMOUSEUP) {
					switch (DOM.eventGetButton(event)) {
					// If it is right click
					case Event.BUTTON_RIGHT:
						break;
					case Event.BUTTON_LEFT:
						Main.getInstance().setSelectedElement(
								(PLTreeItem) currentTreeItem);
						break;
					}

				}
			}

			private native void disableContextMenu(Element elem) /*-{
						elem.oncontextmenu=function(a,b) {return false};
						}-*/;

		};
		// tree.sinkEvents(eventBitsToAdd)
		// tree.re
		// tree.addItem(new PLTreeItem("string"));
		addWidget(tree);
		// tree.add()
	}

	public void UpdateUI() {

	}

	static int templateCounter = 0;
	private String getNewTemplateID(String ID) {
		templateCounter++;
		return (ID + "_new_" + templateCounter);  
	}
	
	public void componentHasChanged(Component component) {
		Template templateBackup = component.getTemplateBackup();
		PLTreeItem item = SearchTemplateItem(templateBackup);

		if (item == null) {
//			System.out.println("PL: There is no such componnt in the PL Library");
		} else {
			if (item.listOfComponents.size() == 1) {
				// This element is the one element of such type

				// If element type is Library type
				if (component.getTemplate().isPublic()) {
					// create new template
					// Remove component from current template
					item.listOfComponents.remove(component);
					item.remove();

					// Generate new template id
					// TODO: apply more effective mechanism
					Property id = component.getTemplate().getProperties().get(Property.ID);
					id.setValue( getNewTemplateID ( id.getStringValue() ) );

					// Mark template as private one
					component.getTemplate().isPublic(false);

					addComponent(component);

				} else {
					// else if element type is user's own type
					// do nothing
				}

			} else {
				// There are other elements of such type

				// Remove component from current template
				item.listOfComponents.remove(component);

				// Generate new template id
				// TODO: apply more effective mechanism
				Property id = component.getTemplate().getProperties().get(Property.ID);
				id.setValue( getNewTemplateID ( id.getStringValue() ) );
				
				// Mark template as private one
				component.getTemplate().isPublic(false);

				addComponent(component);
				// Create new template
				// Assign modified component to the new template

			}
			item.updateItem();
			// TODO: update component number of the new template
		}
	}

	public void templateHasChanged(Template newTemplate, Template backupTemplate) {
		// TODO: change according to current sitation in this part of code
//		Template templateBackup = template;
		PLTreeItem item;
		if (backupTemplate != null) {
			item = SearchTemplateItem(backupTemplate);
		}
		else {
			item = SearchTemplateItem(newTemplate);
		}

		if (item == null) {
//			System.out.println("PL: There is no such component in the PL Library");
		} else {
				// This element is the one element of such type

				// If element type is Library type
				if (newTemplate.isPublic()) {
					// create new template
//					item.remove();

					// Generate new template id
					// TODO: apply more effective mechanism
					Property id = newTemplate.getProperties().get(Property.ID);
					id.setValue( getNewTemplateID ( id.getStringValue() ) );

					// Mark template as private one
					newTemplate.isPublic(false);

//					addComponent(component);

				} else {
					// else if element type is user's own type
					// do nothing
				}
			item.updateItem();
			// TODO: update component number of the new template
		}
	}
	
	public void addComponent(Component component) {
		PLTreeItem item = SearchTemplateItem(component.getTemplate());
		if (item == null) {
			// Add new item to the PL library
			tree.addItem(new PLTreeItem(component));
		} else {
			// Add link to the template for the component
			item.listOfComponents.add(component);
			item.updateItem();
		}

	}

	public void removeComponent(Component component) {
		PLTreeItem item = SearchTemplateItem(component.getTemplate());
		if (item == null) {
			// Add new item to the PL library
//			System.out.println("PL: No such template in the PL!");
		}
		// Only one component for such template
		else if (item.listOfComponents.size() == 1) {
			item.listOfComponents.clear();
			item.remove();
		}
		// Several components for such template
		else {
			// Add link to the template for the component
			item.listOfComponents.remove(component);
			item.updateItem();
		}

	}

	public PLTreeItem SearchTemplateItem(Template template) {
		if (template != null) {
			int childrenCount = tree.getItemCount();
			String templateID = template.getProperties().get(Property.ID).getStringValue();
			PLTreeItem item = null;
			for (int i = 0; i < childrenCount; i++) {
				item = (PLTreeItem) tree.getItem(i);
				if (item.getProperties().getProperties().get(Property.ID)
						.getStringValue().equals(templateID)) {
					return item;
				}
			}
			}
		return null;
	}

	public void switchToBackupTemplate(Template template,
			Template templateBackup) {
		PLTreeItem item = SearchTemplateItem(template);

		if (item == null) {
//			System.out.println("PL: There is no such componnt in the PL Library");
		} else {
			for (Iterator<Selectable> iterator = item.listOfComponents
					.iterator(); iterator.hasNext();) {
				Component component = (Component) iterator.next();
				component.setTemplate(templateBackup);
			}

		}
	}

}
