/**
 * 
 */
package fi.tkk.media.xide.client.Tabs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import org.exist.xquery.functions.session.SetAttribute;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.Data.APElement;
import fi.tkk.media.xide.client.Data.APElementApplication;
import fi.tkk.media.xide.client.Data.APElementHolder;
import fi.tkk.media.xide.client.Data.APElementPage;
import fi.tkk.media.xide.client.Data.EventClickListener;
import fi.tkk.media.xide.client.Data.HasDisplayableProperties;
import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.Data.SaveObjectsListener;
import fi.tkk.media.xide.client.Data.Selectable;
import fi.tkk.media.xide.client.UI.Widget.GoodImage;
import fi.tkk.media.xide.client.UI.Widget.ToggleWidget;
import fi.tkk.media.xide.client.popups.PopupApplicationPublishing;
import fi.tkk.media.xide.client.popups.basic.PopupAreYouSure;
import fi.tkk.media.xide.client.utils.ActionWithTextAndIcon;
import fi.tkk.media.xide.client.utils.ConnectionToServer;
import fi.tkk.media.xide.client.utils.GoodHorizontalAdjustablePanel;
import fi.tkk.media.xide.client.utils.Icons;
import fi.tkk.media.xide.client.utils.ToggleActionWithTextAndAction;
import fi.tkk.media.xide.client.utils.ConnectionToServer.CallbackActions;

/**
 * @author Evgenia Samochadina
 * @date Mar 8, 2009
 *
 */
public class ExplorerTreeItem extends TreeItem implements Selectable, Serializable, APElementHolder{
	public APElement element;
	public APElement elementBackup = null;
	
	protected boolean isChanged = false;

	transient protected Label itemTextLabel;
	transient protected HorizontalPanel icons;
	
	// Connection to server
	ConnectionToServer connection;
	
	protected void SetSelectable() {
		Main.getInstance().setSelectedElement(this);
	}
	
	public ExplorerTreeItem() {
		super();
//		connection = new ConnectionToServer();
	}
	
	public ExplorerTreeItem(final APElement elementInitial){
		this();
		this.element = elementInitial;
		this.element.setItem( this);
			HorizontalPanel hp  = new HorizontalPanel() {
				public void onBrowserEvent(Event event) 
				{
					switch (event.getTypeInt()) {
					case Event.ONDBLCLICK:
//						Main.getInstance().SwitchP(Main.PP, (APElementPage)element);
						break;
					case Event.ONCLICK:
						break;
					}
					SetSelectable();
				}
		};	
		hp.add(elementInitial.getImage());
		
		DOM.setStyleAttribute(hp.getWidget(0).getElement(), "paddingLeft", "3px");
		DOM.setStyleAttribute(hp.getWidget(0).getElement(), "paddingRight", "3px");

		// Title
		itemTextLabel = new Label(elementInitial.getTitle());
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
//				if (actionList[k] instanceof ToggleActionWithTextAndAction) {
//					System.out.println("on click toggle 1");
//					if (elementInitial.properties.get(Property.IS_PUBLISHED).getStringValue().equals("true")) {
//						System.out.println("on click toggle ");
//						((ToggleWidget)action).onClick();
//					}
//				}
			}
			else {
				// Draw
				action = new SimplePanel();
				action.setSize("16px", "16px");
			}
			
			DOM.setStyleAttribute(action.getElement(), "paddingLeft", "2px");
			DOM.setStyleAttribute(action.getElement(), "paddingRight", "2px");
			icons.add(action);
		}

//		if (elementInitial.getType() == APElement.WPAGE) {
//			final GoodImage image = new GoodImage(0, Icons.ICON_PAGE_EDIT);
//			image.setClickListener(new EventClickListener() {
//
//				public void onClick(Event event) {
//					(new ModalDimmedPopup("Unfortunately this is not implemented yet" )).showPopup(true);
//					event.cancelBubble(true);
//				}
//				
//			});
//			i = image;
//		}
//		else {
////			i = new Image("actionIcons/none.png");
//			i = new SimplePanel();
//			i.setSize("16px", "16px");
//		}
//		DOM.setStyleAttribute(i.getElement(), "paddingLeft", "2px");
//		DOM.setStyleAttribute(i.getElement(), "paddingRight", "2px");
//
//		i.sinkEvents(Event.ONCLICK);
//		icons.add(i);
//		
//		// Publish
//		if (elementInitial.getType() == APElement.APPLICATION) {
//			final GoodImage image = new GoodImage(1);
//			image.setClickListener(new EventClickListener() {
//
//				public void onClick(Event event) {
//					publishElement();
//					event.cancelBubble(true);
//				}
//				
//			});
//			i = image;
//		}
//		else {
////			i = new Image("actionIcons/none.png");
//			i = new SimplePanel();
//			i.setSize("16px", "16px");
//		}
//		DOM.setStyleAttribute(i.getElement(), "paddingLeft", "2px");
//		DOM.setStyleAttribute(i.getElement(), "paddingRight", "2px");
//
//		i.sinkEvents(Event.ONCLICK);
//		icons.add(i);
//		
//		
//		//Add
//		if (elementInitial.getType() != APElement.WPAGE) {
//			final GoodImage image = new GoodImage(2, Icons.ICON_APP_ADD);
//			image.setClickListener(new EventClickListener() {
//
//				public void onClick(Event event) {
//					(new ModalDimmedPopup("Unfortunately this is not implemented yet" )).showPopup(true);
//					event.cancelBubble(true);
//				}
//				
//			});
//			i = image;
//			}
//		else {
//	//		i = new Image("actionIcons/none.png");
//			i = new SimplePanel();
//			i.setSize("16px", "16px");
//		}
//		
//		DOM.setStyleAttribute(i.getElement(), "paddingLeft", "2px");
//		DOM.setStyleAttribute(i.getElement(), "paddingRight", "2px");
//		i.sinkEvents(Event.ONCLICK);
//		icons.add(i);
//
//		// Delete
//		final GoodImage image = new GoodImage(3, Icons.ICON_APP_DELETE);
//		image.setClickListener(new EventClickListener() {
//
//			public void onClick(Event event) {
//				(new PopupAreYouSure("You are going to delete an object. Are you sure?", new ClickListener() {
//					public void onClick(Widget sender) {
//						deleteElement();
//					}
//					
//				},null)).showPopup(true);
//				
//				event.cancelBubble(true);
//			}
//			
//		});
//		i = image;
//		DOM.setStyleAttribute(i.getElement(), "paddingLeft", "2px");
//		DOM.setStyleAttribute(i.getElement(), "paddingRight", "2px");
//
//		i.sinkEvents(Event.ONCLICK);
//		icons.add(i);
		updateIcons();
		GoodHorizontalAdjustablePanel panel = new  GoodHorizontalAdjustablePanel(hp, "left", icons, "right");
		
//		HorizontalPanel panel100 = new HorizontalPanel();
//		panel100.setWidth("100%");
//		panel100.setStyleName("tree-item");
//		panel100.add(panel);
//		setWidget(panel100);

		setWidget(panel);
		hp.sinkEvents(Event.ONCLICK|Event.ONDBLCLICK);
		getWidget().setStyleName("tree-item");
	}

//	public void publishElement() {
//		if (element.getProperties().get(Property.IS_PUBLISHED).getStringValue().equals("false")) {
//			//				(new ModalDimmedPopup("Unfortunately this is not implemented yet" )).showPopup(false);
//								(new PopupApplicationPublishing((APElementApplication)element)).showPopup(true);
//							}
//							else {
//								(new PopupAreYouSure("You are going to unpublish the application. Are you sure?", new ClickListener() {
//									public void onClick(Widget sender) {
//										unpublishApplication();
//									}
//									
//								},null)).showPopup(true);
//;
//							}						
//	}
	
	public String getTypeName() {
		return "explorer";
	}
	
	public void onAppSuccessfullyUnpublished() {
		
	}
	
//	public void unpublishApplication() {
//		connection.makeACall(new CallbackActions() {
//
//			public void execute(AsyncCallback callback) {
//				connection.searchService.unpublishApplication((APElementApplication)element, callback);
//			}
//
//			public void onFailure(Throwable caught) {
//				new ModalDimmedPopup("Unfortunately application was not unpublished! ").showPopup(true);
//			}
//
//			public void onSuccess(Object result) {
//				new ModalDimmedPopup("Application is successfully unpublished! ").showPopup(true);
//				element.getProperties().get(Property.DATE_PUB).setValue("0000-00-00 00:00:00");
//				element.getProperties().get(Property.IS_PUBLISHED).setValue("false");
//				element.getProperties().get(Property.URL).setValue("");
//				element.setPublishedState(false);
//				Main.getInstance().UpdateUI();
//			}});
//	}
//	
	public void deleteElement() {
		element.delete();
		Main.getInstance().UpdateUI();
	}
	
	public void update(boolean propagate) {
		if (element.getProperties() != null) {
			itemTextLabel.setText(element.getProperties().get(Property.TITLE).getStringValue());
		}
		updateIcons();
		if (propagate) {
			for(int i = 0; i < getChildCount(); i++) {
				((ExplorerTreeItem)getChild(i)).update(true);
			}
			
		}
//		for (int i = 0; i < icons.getWidgetCount(); i++) {
//			Object o = icons.getWidget(i);
//			if (o instanceof Image) {
//			}
//		}
	}
	
	private void updateAPElement() {
		connection.makeACall(new CallbackActions() {

		public void execute(AsyncCallback callback) {
			connection.searchService.updateAPElementInfo(element.getProperties(), element.getType(), callback);
		}

		public void onFailure(Throwable caught) {
			
		}

		public void onSuccess(Object result) {
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
			if (Main.getInstance().getSelectedElement() != this) {
				DOM.setStyleAttribute(((Image)o).getElement(), "display", "none");
			}
			
	}
		
	}
	
	public String getText() {
		return element.getTitle();
	}
	public void addItem(ExplorerTreeItem item) {
		super.addItem(item);
//		this.element.addChild(item.element);
		DOM.setStyleAttribute(DOM.getFirstChild(getElement()), "width", "100%");
		DOM.setStyleAttribute(DOM.getFirstChild(DOM.getFirstChild(DOM.getFirstChild(DOM.getFirstChild(getElement())))), "width", "16px");
		
//		DOM.setStyleAttribute(DOM.getFirstChild(DOM.getFirstChild(getElement())), "width", "100%");
	}
//	 /**
//	   * Adds another item as a child to this one.
//	   * 
//	   * @param item the item to be added
//	   */
//	  public void addItem(TreeItem item) {
//		  super.addItem(item);
//		  
//	  }
	
	public void Select() {
		this.changeStyle(SELECTED);
		updateIcons();
		
	}
	public void Unselect() {
		this.changeStyle(RESET);
		
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
			updateAPElement();
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
			element = elementBackup;
			elementBackup = null;
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
			getWidget().addStyleDependentName("selected");
			for (Iterator<Selectable> iterator = GetLinkedObjects().iterator(); iterator.hasNext();) {
				iterator.next().changeStyle(Selectable.SELECTED_LINKED);
			}
		}
		else if ((event&Selectable.SELECTED_LINKED) >0) {
			getWidget().addStyleDependentName("selected-linked");
		}
		else if ((event&Selectable.RESET) >0) {
			getWidget().removeStyleDependentName("selected");
			getWidget().removeStyleDependentName("selected-linked");
			if (Main.getInstance().getSelectedElement()== this) {
				for (Iterator<Selectable> iterator = GetLinkedObjects().iterator(); iterator.hasNext();) {
					iterator.next().changeStyle(Selectable.RESET);
				}
			}

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
		this.remove();
	}

	public void onAddNewElement(APElement child) {
		ExplorerTreeTab.getInstance().addElement(this.element, child);		
	}


}
