package fi.tkk.media.xide.client.Tabs;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;


import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ScrollPanel;
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
import fi.tkk.media.xide.client.Server.RPC.ApplicationCallback;
import fi.tkk.media.xide.client.Server.RPC.AuthenticationException;
import fi.tkk.media.xide.client.UI.Widget.ContextMenu;
import fi.tkk.media.xide.client.popups.basic.LoadingPopup;
import fi.tkk.media.xide.client.popups.basic.PopupError;
import fi.tkk.media.xide.client.utils.ConnectionToServer;
import fi.tkk.media.xide.client.utils.Icons;
import fi.tkk.media.xide.client.utils.ConnectionToServer.CallbackActions;

public class AppTreeTab extends Tab {

	public final int tabID = 0;
	
	private static AppTreeTab instance; 
	public APElement all_app;
	private Tree tree;
	
	
	// Indicates whether to show only user's applications or public applications as well
	CheckBox chPublicApp;
	// Connection to server
//	ConnectionToServer connectionToServer;
	
	private native void disableContextMenu(Element elem) /*-{
	elem.oncontextmenu=function(a,b) {return false};
}-*/;
	
	public static AppTreeTab getInstance() {
		if (instance == null) {
			instance = new AppTreeTab();
		}
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
	public AppTreeTab() 
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
								ContextMenu menu = new ContextMenu(((AppTreeItem)this.getSelectedItem()).element.getContextMenuItems()) ;
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
//		tree.addTreeListener(new TreeListener() {
//
//			public void onTreeItemSelected(TreeItem item) {
//				Main.getInstance().setSelectedElement((Selectable)item);				
//			}
//
//			public void onTreeItemStateChanged(TreeItem item) {
//				
//			}
//			
//		});
		
		DOM.setStyleAttribute(tree.getElement(), "margin", "15px");
		
		
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
//					AppTreeItem currentTreeItem = (AppTreeItem) this.getSelectedItem();
//					currentTreeItem.Select();
//				}
//			}
//		};
		
//		connectionToServer = new ConnectionToServer();
		
//		tree.addItem(AppTreeItem.getHeaderItem());
		
		Label myAppLabel = new Label("My web-applications");
		myAppLabel.setStyleName("design-new-app-tree-header");
		
		RadioButton rb = new RadioButton("group1", "Tree");
		rb.setStyleName("design-new-app-tree-header-view");
		rb.setValue(true);
		rb.setEnabled(false);
		RadioButton rb2 = new RadioButton("group1", "Table");
		rb2.setStyleName("design-new-app-tree-header-view");
		rb2.setEnabled(false);
		
		this.add(myAppLabel);
		//<img style="vertical-align: bottom;" src="elementIcons/application_32_readonly.png"/>
		chPublicApp = new CheckBox("Show demo applications (in read-only mode <img style=\"vertical-align: middle; padding: 0px 3px 0px 3px;\" src=\"elementIcons/application_32_readonly.png\"/>)", true);
		chPublicApp.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				reloadTab();
			}});
		chPublicApp.setValue(false);
		chPublicApp.setStyleName("design-new-app-tree-checkbox");
		this.add(chPublicApp);
		
		this.add(rb);
		this.add(rb2);
//		HTMLPanel panel = new HTMLPanel("<div> <div/> <div id=\"tree_id\"> </div> </div>");
//		panel.setSize("100%", "100%");
//		DOM.setStyleAttribute(tree.getElement(), "overflow", "scroll");
//		panel.add(tree, "tree_id");
//		this.add(panel);
		this.add(tree);
//		System.out.println("before getapplist");
//		getApplicationList();

		
//		mainItem.setState(true);
	}
	
	/**
	 * Reloads the list of applications from the server
	 */
	public AppTreeTab reloadTab() {
		tree.clear();
		all_app = null;
		getApplicationList();	
		return this;
	}
	public void onTextChanged() {
	}
	public native void runTheScript() /*-{
		this.@fi.tkk.media.xide.client.Tabs.AppTreeTab::onTextChanged()();
}-*/;

	
	/**
	 * Gets application from server and initiate their showing
	 */
	private void getApplicationList() {
//	     ApplicationCallback getApps = new ApplicationCallback() {
//
//	         public void onFailureAfter(Throwable arg0) {                 
//	             if(!(arg0 instanceof AuthenticationException)) {
//	                 Window.alert("Couldn't retrieve customer data from the server.");
//	             }
//	         }
//	      
//	         public void onSuccessAfter(Object result) { 
//	        	 if (result instanceof ArrayList) {
//	        		 System.out.println("ok");
//						displayApplications((ArrayList<APElement>) result);
//					}
//	         }
//	      
//	         public void authenticateAndExecute() throws AuthenticationException {   
//	        	 ConnectionToServer.searchService.getApplications(null, this);             
//	         }
//	      
//	     };
//	  
//	     getApps.execute();

		ConnectionToServer.makeACall(new CallbackActions() {

			public void execute(AsyncCallback callback) {
				ConnectionToServer.searchService.getApplicationsList(chPublicApp.getValue(), callback);
			}

			public void onFailure(Throwable caught) {
				new PopupError("Unfortunately application list cannot be received!",  caught.getMessage());
			}

			public void onSuccess(Object result) {
				if (result instanceof ArrayList) {
					displayApplicationsFromList((ArrayList<String>) result);
				}
			}});
	}
	
	int applicationAmount;
	long seconds;
	private void displayApplicationsFromList(ArrayList<String> headElementChildren) {
		applicationAmount = headElementChildren.size();
		all_app =  new APElement();
		if (applicationAmount > 0) {
			callForNextApplication(headElementChildren, 0);
		}
//		tree.addItem(mainItem);
//		for (Iterator<String> iterator = headElementChildren.iterator(); iterator.hasNext();) {
//			final String appID = iterator.next();
//			ConnectionToServer.makeACall(new CallbackActions() {
//
//				public void execute(AsyncCallback callback) {
//					ConnectionToServer.searchService.getApplication(appID, true, callback);
//				}
//
//				public void onFailure(Throwable caught) {
//					
//				}
//
//				public void onSuccess(Object result) {
//					if (result instanceof APElement) {
//							displayApplication((APElement) result);
//					}
//				}});			
//		}
//		mainItem.setState(true);
//		tree.setSelectedItem(mainItem, true);
}

	private void callForNextApplication(final ArrayList<String> headElementChildren, final int currentChild) {
		final String appID = headElementChildren.get(currentChild);
		
		ConnectionToServer.makeACall(new CallbackActions() {

			public void execute(AsyncCallback callback) {
				ConnectionToServer.searchService.getApplication(appID, true, callback);
			}

			public void onFailure(Throwable caught) {
				new PopupError("Unfortunately application  cannot be received!",  caught.getMessage());

			}

			public void onSuccess(Object result) {
				if (result instanceof APElement) {
						displayApplication((APElement) result);
						if (currentChild + 1 < headElementChildren.size())
						callForNextApplication(headElementChildren, currentChild + 1);
				}
			}});			
	}
	
	private void displayApplication(APElement element) {
		applicationAmount--;
		if (applicationAmount == 0) {
			LoadingPopup.hideDimmed();
//			System.out.println(seconds);
		}
		AppTreeItem appItem = new AppTreeItem(element);
		tree.addItem(appItem);
		for (Iterator<APElement> iteratorPage = element.getChildren().iterator(); iteratorPage.hasNext();) {
			APElement page = iteratorPage.next();
			// Application: Test application
			AppTreeItem pageItem = new AppTreeItem(page);
			appItem.addItem(pageItem);
		}
//	mainItem.setState(true);
//	tree.setSelectedItem(mainItem, true);

}
	//List received      Mon Dec 07 10:12:43 EET 2009 1260173563637
	//Everything received Mon Dec 07 10:12:44 EET 2009 1260173564671
	
	private void displayApplications(ArrayList<APElement> headElementChildren) {
			for (Iterator<APElement> iterator = headElementChildren.iterator(); iterator.hasNext();) {
				APElement app = iterator.next();
				displayApplication(app);
			}
//			mainItem.setState(true);
//			tree.setSelectedItem(mainItem, true);

	}
	
	public void UpdateUI() {
		((AppTreeItem)tree.getItem(0)).update(true);
//		tree.clear();
//		AppTreeItem main = new AppTreeItem(all_app);
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
		getApplicationList();
	}
	
	public void updateApplicationTree() {
		tree.clear();
		displayApplications(all_app.getChildren());
	}
//	public void AddChildren(AppTreeItem item) {
//		for (int i = 0; i < item.element.getChildren().size(); i++) {
//			APElement o = item.element.getChildren().get(i);
//			AppTreeItem childItem = new AppTreeItem(o);
//			item.addItem(childItem);
//			AddChildren(childItem);
//		}
//		
//	}
	
}
