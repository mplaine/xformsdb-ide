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
import fi.tkk.media.xide.client.View;
import fi.tkk.media.xide.client.Data.APElement;
import fi.tkk.media.xide.client.Data.APElementApplication;
import fi.tkk.media.xide.client.Data.APElementPage;
import fi.tkk.media.xide.client.Data.HasDisplayableProperties;
import fi.tkk.media.xide.client.Data.Selectable;
import fi.tkk.media.xide.client.Data.Tag;
import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.UI.Widget.ContextMenu;
import fi.tkk.media.xide.client.fs.FileStructureElement;
import fi.tkk.media.xide.client.fs.XIDEFile;
import fi.tkk.media.xide.client.fs.XIDEFolder;
import fi.tkk.media.xide.client.utils.ConnectionToServer;
import fi.tkk.media.xide.client.utils.ConnectionToServer.CallbackActions;

public class FileTreeTab extends TopTab {

	public final int tabID;
	
	private XIDEFolder rootFolder;
	private Tree tree;
	
	// Connection to server
	ConnectionToServer connectionToServer;
	
	private native void disableContextMenu(Element elem) /*-{
	elem.oncontextmenu=function(a,b) {return false};
}-*/;
	
	public FileTreeTab(int tabID) 
	{
		super();
		this.tabID = tabID;
		tree = new Tree() {
			
			public void addItem(TreeItem item) {
				super.addItem(item);
				DOM.setStyleAttribute(DOM.getFirstChild(super.getItem(0).getElement()), "width", "100%");
			}
			
			@Override
			public void onBrowserEvent(Event event) {
				super.onBrowserEvent(event);
					if (DOM.eventGetButton(event) == Event.BUTTON_RIGHT) {
						if (event.getTypeInt() == Event.ONMOUSEDOWN) {
							// If that's selected element
								disableContextMenu(this.getElement());
								ContextMenu menu = new ContextMenu(((FileTreeItem)this.getSelectedItem()).element.getContextMenuItems()) ;
								menu.setPopupPosition((event.getClientX() + 3), (event.getClientY() + 3));
								menu.show();
						}
					}

			}
			
		};
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
		
		addWidget(tree);
//		connectionToServer = new ConnectionToServer();
		updateFolderStructure();
	}
	
	private void updateFolderStructure() {
		if (Main.getInstance().getSelectedElement() != null) {
			XIDEFolder result = Main.getInstance().getSelectedElement().getProperties().getRootFolder();
			if (result != null) {
				rootFolder = result;
				FileTreeItem mainItem = new FileTreeItem(rootFolder);
				tree.addItem(mainItem);
				displayRootFolderContent((XIDEFolder)result, mainItem);
			}
		}
	}
	
	private void displayRootFolderContent(XIDEFolder folder, FileTreeItem treeitem) {
			for (Iterator<XIDEFolder> iterator = folder.getFolderChild().iterator(); iterator.hasNext();) {
				XIDEFolder childFolder = iterator.next();
				// Application: Test application
				FileTreeItem childFolderItem = new FileTreeItem(childFolder);
				treeitem.addItem(childFolderItem);
				displayRootFolderContent(childFolder, childFolderItem);
			}

			for (Iterator<XIDEFile> iterator = folder.getFileChild().iterator(); iterator.hasNext();) {
				XIDEFile childFolder = iterator.next();
				FileTreeItem childFileItem = new FileTreeItem(childFolder);
				treeitem.addItem(childFileItem);
			}
			treeitem.setState(true);
	}
	
	public void UpdateUI() {
//		if(Main.getInstance().getSelectedElement().getProperties() instanceof APElementPage) {
//			// Do not update since in CAP when the page is selected the same hierarchy is displayed
//		}
//		else{
			tree.clear();
			updateFolderStructure();
//		}
			
//		((FileTreeItem)tree.getItem(0)).update(true);
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
//	
}
