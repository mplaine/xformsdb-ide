package fi.tkk.media.xide.client.Tabs;

import java.util.ArrayList;
import java.util.Iterator;


import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.TreeListener;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.Data.APElement;
import fi.tkk.media.xide.client.Data.APElementApplication;
import fi.tkk.media.xide.client.Data.APElementPage;
import fi.tkk.media.xide.client.Data.HasDisplayableProperties;
import fi.tkk.media.xide.client.Data.Selectable;
import fi.tkk.media.xide.client.Data.Tag;
import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.UI.Widget.ContextMenu;
import fi.tkk.media.xide.client.utils.ConnectionToServer;
import fi.tkk.media.xide.client.utils.ConnectionToServer.CallbackActions;

public class ExplorerTreeTab extends Tab {

	private static ExplorerTreeTab instance; 
	public APElement all_app;
	private Tree tree;
	
	// Connection to server
	ConnectionToServer connectionToServer;
	
	private native void disableContextMenu(Element elem) /*-{
	elem.oncontextmenu=function(a,b) {return false};
}-*/;
	
	public static ExplorerTreeTab getInstance() {
		return instance;
	}
	
	@Override
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);
		if (event.getTarget().equals(this.getElement())) {
			if (DOM.eventGetButton(event) == Event.BUTTON_RIGHT) {
				if (event.getTypeInt() == Event.ONMOUSEDOWN) {
						disableContextMenu(this.getElement());
						ContextMenu menu = new ContextMenu(all_app.getContextMenuItems()) ;
						menu.setPopupPosition((event.getClientX() + 3), (event.getClientY() + 3));
						menu.show();
//					}
				}
			}		}
//		else {
//			System.out.println("drugoy");
//		}
		
//		if (event.getTypeInt() == Event.ONCLICK) {
		
	}
	public ExplorerTreeTab() 
	{
		super();
		instance = this;
		
		disableContextMenu(this.getElement());
		this.sinkEvents(Event.MOUSEEVENTS);
		tree = new Tree() {
			
			public void addItem(TreeItem item) {
				super.addItem(item);
				DOM.setStyleAttribute(DOM.getFirstChild(super.getItem(0).getElement()), "width", "100%");
			}
			
			@Override
			public void onBrowserEvent(Event event) {
				super.onBrowserEvent(event);
//				if (event.getTypeInt() == Event.ONCLICK) {
					if (DOM.eventGetButton(event) == Event.BUTTON_RIGHT) {
						if (event.getTypeInt() == Event.ONMOUSEDOWN) {
							// If that's selected element
//							if (
//									(this.getSelectedItem().getAbsoluteLeft() < event.getClientX()) &&
//									(this.getSelectedItem().getAbsoluteLeft() + this.getSelectedItem().getOffsetWidth() > event.getClientX()) &&
//									(this.getSelectedItem().getAbsoluteTop() < event.getClientY()) &&
//									(this.getSelectedItem().getAbsoluteTop() + this.getSelectedItem().getOffsetHeight()> event.getClientY())
//											) {
								disableContextMenu(this.getElement());
								ContextMenu menu = new ContextMenu(((ExplorerTreeItem)this.getSelectedItem()).element.getContextMenuItems()) ;
								menu.setPopupPosition((event.getClientX() + 3), (event.getClientY() + 3));
			//					menu.show(event.getTarget().getAbsoluteLeft(), event.getTarget().getAbsoluteTop());
								menu.show();
//							}
						}
					}
//				}

			}
			
		};
		tree.setAnimationEnabled(true);
		tree.addTreeListener(new TreeListener() {

			public void onTreeItemSelected(TreeItem item) {
				Main.getInstance().setSelectedElement((Selectable)item);				
			}

			public void onTreeItemStateChanged(TreeItem item) {
				
			}
			
		});
		
		
//			public void onBrowserEvent(Event event) {
//
//
//				// Where the button mouse has been released
//				final int mouseX = DOM.eventGetClientX(event);
//				final int mouseY = DOM.eventGetClientY(event);
//
//				// When the button mouse is released
//				if (DOM.eventGetType(event) == Event.ONMOUSEDOWN) {
//					// Get current tree item
//					ExplorerTreeItem currentTreeItem = (ExplorerTreeItem) this.getSelectedItem();
//					currentTreeItem.Select();
//				}
//			}
//		};
		
//		connectionToServer = new ConnectionToServer();
		getApplication();
		
		
		
		this.add(tree);
		
//		mainItem.setState(true);
	}
	
	/**
	 * Gets application from server and initiate their showing
	 */
	private void getApplication() {
		connectionToServer.makeACall(new CallbackActions() {

			public void execute(AsyncCallback callback) {
				connectionToServer.searchService.getApplication(null, false, callback);
			}

			public void onFailure(Throwable caught) {
				
			}

			public void onSuccess(Object result) {
				if (result instanceof ArrayList) {
					displayApplications((ArrayList<APElement>) result);
				}
			}});
	}
	private void displayApplications(ArrayList<APElement> headElementChildren) {
			all_app =  new APElement();
//			tree.addItem(mainItem);
			for (Iterator<APElement> iterator = headElementChildren.iterator(); iterator.hasNext();) {
				APElement app = iterator.next();
				// Application: Test application
				ExplorerTreeItem appItem = new ExplorerTreeItem(app);
				tree.addItem(appItem);
				for (Iterator<APElement> iteratorPage = app.getChildren().iterator(); iteratorPage.hasNext();) {
					APElement page = iteratorPage.next();
					// Application: Test application
					ExplorerTreeItem pageItem = new ExplorerTreeItem(page);
					appItem.addItem(pageItem);
				}
			}
//			mainItem.setState(true);
//			tree.setSelectedItem(mainItem, true);

	}
	
	public void UpdateUI() {
		((ExplorerTreeItem)tree.getItem(0)).update(true);
//		tree.clear();
//		ExplorerTreeItem main = new ExplorerTreeItem(all_app);
//		tree.addItem(main);
//		
//		AddChildren(main);
//		this.add(tree);
//		main.setState(true);
	}
	
	public void addElement(APElement parent, APElement element) {
		if (parent == null) {
			// add to the All Aplications
			all_app.addChild(element);
			element.parent = all_app;
		}
		else {
			parent.addChild(element);
			element.parent = parent;
		}
		updateApplicationTree();
	}
	
	public void updateApplicationListFromServer() {
		tree.clear();
		getApplication();
	}
	
	public void updateApplicationTree() {
		tree.clear();
		displayApplications(all_app.getChildren());
	}
//	public void AddChildren(ExplorerTreeItem item) {
//		for (int i = 0; i < item.element.getChildren().size(); i++) {
//			APElement o = item.element.getChildren().get(i);
//			ExplorerTreeItem childItem = new ExplorerTreeItem(o);
//			item.addItem(childItem);
//			AddChildren(childItem);
//		}
//		
//	}
	
}
