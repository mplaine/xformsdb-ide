/**
 * 
 */
package fi.tkk.media.xide.client.Tabs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.Data.APElement;
import fi.tkk.media.xide.client.Data.APElementHolder;
import fi.tkk.media.xide.client.Data.APElementPage;
import fi.tkk.media.xide.client.Data.HasDisplayableProperties;
import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.Data.SaveObjectsListener;
import fi.tkk.media.xide.client.Data.Selectable;
import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.UI.Widget.ContextMenu;
import fi.tkk.media.xide.client.UI.Widget.GoodImage;
import fi.tkk.media.xide.client.UI.Widget.ToggleWidget;
import fi.tkk.media.xide.client.utils.ActionWithTextAndIcon;
import fi.tkk.media.xide.client.utils.ConnectionToServer;
import fi.tkk.media.xide.client.utils.GoodHorizontalAdjustablePanel;
import fi.tkk.media.xide.client.utils.Icons;
import fi.tkk.media.xide.client.utils.ConnectionToServer.CallbackActions;

/**
 * @author Evgenia Samochadina
 * @date Mar 8, 2009
 *
 */
public class CAppTreeItem extends AbsolutePanel implements Selectable, Serializable, APElementHolder{
		
	public APElement element;
	public APElement elementBackup = null;
	
	private boolean isSelected;
	private boolean isChanged = false;
	private boolean isHighlighted = false;

	transient Label itemTextLabel;
	transient private HorizontalPanel icons;
	
	// Connection to server
	ConnectionToServer connection;
	
	
	public CAppTreeItem() {
	}
	
	protected void SetSelectable() {
		
		Main.getInstance().setSelectedElement(this);
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	
	public CAppTreeItem(final APElement elementInitial){
		super();
		this.sinkEvents(Event.MOUSEEVENTS);
		
		this.element = elementInitial;
		this.element.setItem(null);
		HorizontalPanel hp  = new HorizontalPanel();

		// Title
		itemTextLabel = new Label(elementInitial.getTitle());
		if (elementInitial instanceof APElementPage) {
			DOM.setStyleAttribute(itemTextLabel.getElement(), "textDecoration", "underline");
			DOM.setStyleAttribute(itemTextLabel.getElement(), "cursor", "pointer");
			DOM.setStyleAttribute(itemTextLabel.getElement(), "color", "RGB(92,133,185)");
		}
		DOM.setStyleAttribute(itemTextLabel.getElement(), "fontWeight", "bolder");
		DOM.setStyleAttribute(itemTextLabel.getElement(), "fontSize", "110%");
		
		hp.add(itemTextLabel);
		
		icons = new HorizontalPanel();

		
//		Widget i;

		// Show action icons
		// Get action list
		ActionWithTextAndIcon[] actionList = elementInitial.getContextMenuItems();
		
		for(int k = 0; k < actionList.length; k++) {
			// If action exist
			Widget action = null;
			if (actionList[k] != null) {
				// Get icon
				action = actionList[k].getIcon();
			}
			else {
				// Draw
				action = new SimplePanel();
				action.setSize("24px", "24px");
			}
			
			DOM.setStyleAttribute(action.getElement(), "paddingLeft", "2px");
			DOM.setStyleAttribute(action.getElement(), "paddingRight", "2px");
			icons.add(action);
		}

		updateIcons();
		GoodHorizontalAdjustablePanel panel = new  GoodHorizontalAdjustablePanel(hp, "left", icons, "right");
		

		this.add(panel);
		hp.sinkEvents(Event.ONCLICK|Event.ONDBLCLICK);
		this.setStyleName("design-new-list-item");
	}

	public String getTypeName() {
		return element.getTypeName();
	}
	
	private native void disableContextMenu(Element elem) /*-{
	elem.oncontextmenu=function(a,b) {return false};
}-*/;
	
	@Override
	public void onBrowserEvent(Event event) {
	super.onBrowserEvent(event);
//	if (event.getTypeInt() == Event.ONCLICK) {
		if (DOM.eventGetButton(event) == Event.BUTTON_RIGHT) {
			if (event.getTypeInt() == Event.ONMOUSEDOWN) {
				// If that's selected element
//				if (
//						(this.getSelectedItem().getAbsoluteLeft() < event.getClientX()) &&
//						(this.getSelectedItem().getAbsoluteLeft() + this.getSelectedItem().getOffsetWidth() > event.getClientX()) &&
//						(this.getSelectedItem().getAbsoluteTop() < event.getClientY()) &&
//						(this.getSelectedItem().getAbsoluteTop() + this.getSelectedItem().getOffsetHeight()> event.getClientY())
//								) {
					disableContextMenu(this.getElement());
					ContextMenu menu = new ContextMenu(element.getContextMenuItems()) ;
					menu.setPopupPosition((event.getClientX() + 3), (event.getClientY() + 3));
//					menu.show(event.getTarget().getAbsoluteLeft(), event.getTarget().getAbsoluteTop());
					menu.show();
//				}
			}
		}
		else if (DOM.eventGetButton(event) == Event.BUTTON_LEFT){
			if (event.getTypeInt() == Event.ONMOUSEDOWN) {
				changeStyle(Selectable.RESET);
				SetSelectable();
			}
			else if (event.getTypeInt() == Event.ONMOUSEOVER){
				if (!isSelected) {
						isHighlighted = true;
						updateIcons();
						changeStyle(Selectable.HIGHLIGHTED);
				}
						
			}
			else if (event.getTypeInt() == Event.ONMOUSEOUT){
				if (isHighlighted&!isSelected) {
					isHighlighted = false;
					updateIcons();
					changeStyle(Selectable.RESET);
				}
			}
		}
	}
	
	public void deleteElement() {
		element.delete();
		Main.getInstance().UpdateUI();
	}
	
	public void update(boolean propagate) {
		if (element.getProperties() != null) {
			itemTextLabel.setText(element.getProperties().get(Property.TITLE).getStringValue());
		}
		updateIcons();
//		if (propagate) {
//			for(int i = 0; i < getChildCount(); i++) {
//				((CAppTreeItem)getChild(i)).update(true);
//			}
//			
//		}
		
	}
	
	private void updateAPElement() {
		ConnectionToServer.makeACall(new CallbackActions() {

		public void execute(AsyncCallback callback) {
			ConnectionToServer.searchService.updateAPElementInfo(element.getProperties(), element.getType(), callback);
		}

		public void onFailure(Throwable caught) {
			
		}

		public void onSuccess(Object result) {
//			System.out.println("properties should be updated");
			Template.updateTemplateProperties(element.getProperties(), result);
			Main.getInstance().UpdateUI(Main.MAIN_TAB);
			Main.getInstance().UpdateUI(Main.RIGHT_TAB);
		}});
}

	
	/**
	 * Icons:
	 * 	- Publish
	 * 	- Edit
	 * 	- New
	 * 	- Delete
	 */
	public void updateIcons() {
		// Now only publish icon can be updated
		// If publish icon exists
		if (icons.getWidgetCount() != 0) {
			if (icons.getWidget(APElement.ACTION_PUBLISH) instanceof ToggleWidget) {
				// Toggle button contains 2 icons. First is "Publish", second is "Unpublish"
				ToggleWidget toggleIcon = (ToggleWidget)icons.getWidget(APElement.ACTION_PUBLISH); 
				// Check if it's state is the same as element's state
				if (element.properties.get(Property.IS_PUBLISHED).getStringValue().equals("true")) {
					if (toggleIcon.isFirstWidgetSelected()) {
						toggleIcon.onClick();
					}
				}
				else {
					// Application is not published
					if (!toggleIcon.isFirstWidgetSelected()) {
						toggleIcon.onClick();
					}
				} 
			}
		}
		for (int i = 0; i < icons.getWidgetCount(); i++) {
			Object o = icons.getWidget(i);
		
			// If there is an image so it can require update
			if (o instanceof Image) {
				// Publish
				if (i == 1) {
					if (!element.getProperties().get(Property.IS_PUBLISHED).getStringValue().equals("false")) {
						((Image)o).setUrl(Icons.ICON_APP_NOT_PUBLISHED);

					}
					else {
						((Image)o).setUrl(Icons.ICON_APP_PUBLISHED);
					}
				}
			}
			
			if (o instanceof GoodImage) {
				if (!this.isSelected() && !this.isHighlighted) {
//					DOM.setStyleAttribute(((GoodImage)o).getElement(), "display", "none");
				}
				else {
//					DOM.setStyleAttribute(((GoodImage)o).getElement(), "display", "inherit");
				}
			}
		}
		
	}
	
	public String getText() {
		return element.getTitle();
	}
//	public void addItem(CAppTreeItem item) {
//		super.addItem(item);
////		this.element.addChild(item.element);
//		DOM.setStyleAttribute(DOM.getFirstChild(getElement()), "width", "100%");
//		DOM.setStyleAttribute(DOM.getFirstChild(DOM.getFirstChild(DOM.getFirstChild(DOM.getFirstChild(getElement())))), "width", "16px");
//		
////		DOM.setStyleAttribute(DOM.getFirstChild(DOM.getFirstChild(getElement())), "width", "100%");
//	}
	

	public void Select() {
		this.isSelected = true;
		this.changeStyle(SELECTED);
		updateIcons();
		
	}
	public void Unselect() {
		this.isSelected = false;
		this.changeStyle(RESET);
		updateIcons();
		
	}

	public ArrayList<Selectable> GetLinkedObjects() {
		return new ArrayList<Selectable>();
	}

	public HasDisplayableProperties getProperties() {
		return element;
	}
	
	public Selectable getValuableElement() {
		return this;
	}

	/* (non-Javadoc)
	 * @see fi.tkk.media.xide.client.Data.Selectable#Changed()
	 */
	public void Changed() {
		if (!isChanged) {
//			isChanged = true;
//			changeStyle(CHANGED);
			if(elementBackup == null) {
				elementBackup = element;
				element = element.cloneIt();
			}
			isChanged = true;
			Main.getCurrentView().updatePropertiesList();
			changeStyle(CHANGED);
		}
		
	}

	public void Saved() {
		if (isChanged) {
			isChanged = false;
			elementBackup.beforeDeletion();
			elementBackup = null;
			changeStyle(RESET);
//			updateAPElement();
			element.updateAPElement(false);
			if (element.parent != null) {
				element.parent.updateAPElement(true);
			}
		}
	}
	
	public void Saved(SaveObjectsListener listener) {
		
	}
	
	public void afterSaved() {
		// TODO: manage saved and aftersaved
	}


	public boolean isChanged() {
		return isChanged;
	}

	public void Canceled() {
		if ((isChanged)&& (elementBackup != null)) {
			element.removeFromParent();
			element = null;
			
			element = elementBackup;
			
			isChanged = false;
			Main.getCurrentView().updatePropertiesList();
			Main.getInstance().UpdateUI();
			changeStyle(Selectable.RESET);
		}
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
			this.removeStyleDependentName("highlighted");
			if (Main.getInstance().getSelectedElement()== this) {
				for (Iterator<Selectable> iterator = GetLinkedObjects().iterator(); iterator.hasNext();) {
					iterator.next().changeStyle(Selectable.RESET);
				}
			}

		}
		else if ((event&Selectable.HIGHLIGHTED) >0) {
			this.addStyleDependentName("highlighted");
		}

	}

	public void Deleted() {
		// TODO implement smth
		element.delete();
	}

	public APElement getAPElement() {
		return element;
	}

	public void onDelete() {
		element.delete();
		this.removeFromParent();
	}

	public void onAddNewElement(APElement child) {
//		System.out.println("added");
		element.addChild(child);
		
		CAppTreeTab.getInstance().addPage(new CAppTreeItem(child));
	}

	

}
