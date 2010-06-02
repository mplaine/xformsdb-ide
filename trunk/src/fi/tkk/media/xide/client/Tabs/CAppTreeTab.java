package fi.tkk.media.xide.client.Tabs;

import java.util.ArrayList;
import java.util.Iterator;


import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.TreeListener;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.View;
import fi.tkk.media.xide.client.Data.APElement;
import fi.tkk.media.xide.client.Data.APElementApplication;
import fi.tkk.media.xide.client.Data.APElementHolder;
import fi.tkk.media.xide.client.Data.APElementPage;
import fi.tkk.media.xide.client.Data.HasDisplayableProperties;
import fi.tkk.media.xide.client.Data.Selectable;
import fi.tkk.media.xide.client.Data.Tag;
import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.UI.Widget.ContextMenu;
import fi.tkk.media.xide.client.popups.PopupCreateNewPage;
import fi.tkk.media.xide.client.utils.ConnectionToServer;
import fi.tkk.media.xide.client.utils.Icons;
import fi.tkk.media.xide.client.utils.ConnectionToServer.CallbackActions;

public class CAppTreeTab extends Tab {

	public final int tabID = View.CAPTREE;
	
	private static CAppTreeTab instance; 
	public APElement all_app;
	private ArrayList<CAppTreeItem> pageList;
	private AbsolutePanel listPanel;
//	private Tree tree;
	
	public CAppTreeItem application;
	
	private APElementApplication app;
	// Connection to server
	ConnectionToServer connectionToServer;
	
	private native void disableContextMenu(Element elem) /*-{
	elem.oncontextmenu=function(a,b) {return false};
}-*/;
	
	public static CAppTreeTab getInstance() {
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
	public CAppTreeTab(APElementApplication app) 
	{
		super();
		DOM.setStyleAttribute(this.getElement(), "marginTop", "25px");
		
		pageList = new ArrayList<CAppTreeItem>();
		instance = this;
		this.app = app;
		
		listPanel = new AbsolutePanel();
		this.add(listPanel);
		
		FlowPanel panel = new FlowPanel();
		Image i = new Image(Icons.ICON_PAGE_BIG);
		i.setStyleName("design-single-tag-panel");
		
		DOM.setStyleAttribute(i.getElement(), "margin", "4px");
		panel.add(i);
		Label label1 = new Label ("Add new page");
		DOM.setStyleAttribute(label1.getElement(), "textDecoration", "underline");
		DOM.setStyleAttribute(label1.getElement(), "fontSize", "110%");
		DOM.setStyleAttribute(label1.getElement(), "fontWeight", "bold");
		DOM.setStyleAttribute(label1.getElement(), "cursor", "pointer");
		DOM.setStyleAttribute(label1.getElement(), "marginTop", "6px");
		label1.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				new PopupCreateNewPage((APElementApplication)all_app.getChildren().get(0));
//				new PopupCreateNewPageFromScratch(null, 
//						(APElementApplication)all_app.getChildren().get(0))
							;
			}}) ;
		panel.add(label1);
		if (!Main.IS_RUNNING_ON_WINDOWS)		{
			DOM.setStyleAttribute(panel.getElement(), "borderTop", "1px solid lightgray");
		}
//		addPage(panel);
		this.add(panel);
		
		disableContextMenu(this.getElement());
		
		
		displayApplication(app);
//		getApplication();
		
	}
	
	/**
	 * Gets application from server and initiate their showing
	 */
	private void getApplication() {
		connectionToServer.makeACall(new CallbackActions() {

			public void execute(AsyncCallback callback) {
//				connectionToServer.searchService.getApplication(appID, false, callback);
			}

			public void onFailure(Throwable caught) {
				
			}

			public void onSuccess(Object result) {
				if (result instanceof APElement) {
					displayApplication((APElement) result);
				}
			}});
	}
	
	private void addApplication(Panel item) {
		DOM.setStyleAttribute(item.getElement(), "paddingLeft", "15px");	
		listPanel.add(item);
	}
	
	public void addPage(Panel item) {
		DOM.setStyleAttribute(item.getElement(), "paddingLeft", "38px");
		DOM.setStyleAttribute(item.getElement(), "textDecoration", "underline");
		if (item instanceof CAppTreeItem) {
			pageList.add((CAppTreeItem)item);
		}
		listPanel.add(item);
	}
	
	
	
	private void displayApplication(APElement headElementChildren) {
			all_app =  new APElement();
			listPanel.clear();
			pageList.clear();
				APElement app = headElementChildren;
				all_app.addChild(app);
				// Application: Test application
				application = new CAppTreeItem(app);
				addApplication(application);
				for (Iterator<APElement> iteratorPage = app.getChildren().iterator(); iteratorPage.hasNext();) {
					APElement page = iteratorPage.next();
					// Application: Test application
					addPage(new CAppTreeItem(page));
				}
			
	}
	
	public void UpdateUI() {
		application.update(true);
		for (Iterator<CAppTreeItem> iterator = pageList.iterator(); iterator.hasNext();) {
			iterator.next().update(true);
		}
		
//		tree.clear();
//		CAppTreeItem main = new CAppTreeItem(all_app);
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
		getApplication();
	}
	
	public void updateApplicationTree() {
		UpdateUI();
	}
//	public void AddChildren(CAppTreeItem item) {
//		for (int i = 0; i < item.element.getChildren().size(); i++) {
//			APElement o = item.element.getChildren().get(i);
//			CAppTreeItem childItem = new CAppTreeItem(o);
//			item.addItem(childItem);
//			AddChildren(childItem);
//		}
//		
//	}
	
}
