/**
 * 
 */
package fi.tkk.media.xide.client.Tabs;

import java.util.Iterator;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.View;
import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.DnDTree.DnDTree;
import fi.tkk.media.xide.client.DnDTree.DnDTreeController;
import fi.tkk.media.xide.client.DnDTree.DnDTreeItem;
import fi.tkk.media.xide.client.UI.Widget.BasicPageElement;
import fi.tkk.media.xide.client.UI.Widget.WebPage;

/**
 * @author Evgenia Samochadina
 * @date Dec 12, 2008
 *
 */
public class NavigatorTab extends TopTab {
	public DnDTree tree;
	public final int tabID = View.NAVIGATOR;
	
	DnDTreeController dndController;
	static NavigatorTab instance;
	
	public static NavigatorTab getInstance() {
		return instance;
	}
	
	public NavigatorTab() {
		super();
		
		instance = this;
		
		dndController = new DnDTreeController();
		
		AbsolutePanel absolutePanel = new AbsolutePanel();
		absolutePanel.setHeight("100%");
		addWidget(absolutePanel);
		
		
//		horizonalPanel = new HorizontalPanel();
//		absolutePanel.add(horizonalPanel);
		
		dndController.setBoundaryPanel(absolutePanel);

//		HorizontalPanel rootPanel = new HorizontalPanel();
//
//		leftPanel = new ScrollPanel();
//		horizonalPanel.add(leftPanel);
//		leftPanel.setSize("380", "480");

//		rightPanel = new ScrollPanel();
//		horizonalPanel.add(rightPanel);
//		rightPanel.setSize("380", "480");

		tree = new DnDTree();
		DOM.setStyleAttribute(tree.getElement(), "position", "absolute");
		
//		rootItem = new DnDTreeItem("MainPage");
//		treeController.makeDraggable(rootItem);
//		rootItem.setState(true);
//		tree.addItem(rootItem);
		
		absolutePanel.add(tree);
		
		dndController.setTree(tree);

//		createLeftTree();
//		CreateTreeFromPage(Main.getInstance().getWebPage());
//		CreateSubTreeFromElement(Main.getInstance().getWebPage(), null);
		
//		CreateTreeFromPageOnlySlots(Main.getInstance().getWebPage());
		
		//tree.setSelectedItem(tree.getItem(0));	
		
	}
	
	public void UpdateTree() {
		tree.clear();
		CreateTreeFromPageOnlySlots(Main.getInstance().getWebPage());
	}
	
	public void UpdateUI() {
//		tree.clear();
//		CreateTreeFromPageOnlySlots(Main.getInstance().getWebPage());
//		tree.removeFromParent();
//		this.add(tree);
		
		
//		WebPage wp = Main.getInstance().getWebPage();
//		if (!wp.getChildrenElements().isEmpty()) {
//			int i = 0;
//			for (Iterator<BasicPageElement> iterator = wp.getChildrenElements().iterator(); iterator.hasNext();) {
//				BasicPageElement o = iterator.next();
//				// If current item's element is not equal to the element from the parent
//				if (!((DnDTreeItem)tree.getItem(i)).getPageElement().equals(o)) {
//					// TODO: what if new element has added????
//					tree.removeItem(tree.getItem(i));
//				}
////				else {
//					TestTreeFromElement((DnDTreeItem)tree.getItem(i));
////					}
//					i++;
//			}
//			
//			
//		}
	}

	public void addElement(DnDTreeItem parentItem, BasicPageElement element, int index) {
		// if element is not added
		if (element.getTreeItem() == null) {
			DnDTreeItem item = new DnDTreeItem( element);
			element.setTreeItem(item);
			dndController.makeDraggable(item);
			// Add current item
			tree.InsertTreeItem(parentItem, index, item);
			
			if (!element.getChildrenElements().isEmpty()) {
				for (Iterator<BasicPageElement> iterator = element.getChildrenElements().iterator(); iterator.hasNext();) {
					BasicPageElement o = iterator.next();
					DnDTreeItem childItem = new DnDTreeItem(o);
					dndController.makeDraggable(childItem);
					// Add current item
					item.addItem(childItem);
					item.setState(true);
				}
			}
		}
	}
	
	public void MoveElement(DnDTreeItem currentParentItem, DnDTreeItem targetParentItem, int currentN, int targetN) {
		// TODO: !!! 
		if (currentParentItem == targetParentItem && currentN < targetN) {
			targetN --;
		}
		// Removing item
		DnDTreeItem item = (DnDTreeItem)currentParentItem.getChild(currentN);
		item.remove();
		// Inserting item
		tree.InsertTreeItem(targetParentItem, targetN, item);
		targetParentItem.setState(true);
		item.setState(true);
	}
	
	public void InsertItem (DnDTreeItem targetParentItem, int targetN, DnDTreeItem item) {
		// Inserting item
		tree.InsertTreeItem(targetParentItem, targetN, item);
		targetParentItem.setState(true);
		item.setState(true);
		
	}
	
	public void removeElement(DnDTreeItem item) {
		item.remove();
	}
	
	public void TestTreeFromElement(DnDTreeItem item) {
		if (item.getChildCount() != 0) {
			for (int i = 0; i < item.getChildCount(); i++) {
				BasicPageElement o = ((DnDTreeItem)item).getPageElement().getChildrenElements().get(i);
				// If current item's element is not equal to the element from the parent
				if (!((DnDTreeItem)item.getChild(i)).getPageElement().equals(o)) {
					// TODO: what if new element has added????
					tree.removeItem(tree.getItem(i));
				}
//				else {
					TestTreeFromElement((DnDTreeItem)tree.getItem(i));
//					}
					i++;
			}
		}
	}
	
	public void CreateTreeFromPage(BasicPageElement page) {
		DnDTreeItem item = new DnDTreeItem( page);
		dndController.makeDraggable(item);
		tree.addItem(item);
		
		for (Iterator<BasicPageElement> iterator = page.getChildrenElements().iterator(); iterator.hasNext();) {
			BasicPageElement o =  iterator.next();
			CreateSubTreeFromElement(o, item);
		}
		return ;
	}

	
	public void CreateTreeFromPageOnlySlots(BasicPageElement page) {
		DnDTreeItem item = 
			new DnDTreeItem(  page);
		tree.addItem(item);
		page.setTreeItem(item);
		dndController.makeDraggable(item);
	
		item.setState(true);
		for (Iterator<BasicPageElement> iterator = page.getChildrenElements().iterator(); iterator.hasNext();) {
			BasicPageElement o =  iterator.next();
			CreateSubTreeFromElement(o, item);
		}
		item.setState(true);
		return ;
	}

	public void CreateSubTreeFromElement(BasicPageElement element, DnDTreeItem parentItem) {
		DnDTreeItem item = new DnDTreeItem( element);
		dndController.makeDraggable(item);
		// Add current item
		// If it does not have a parent
		if (parentItem == null) {
			// Add directly to the 
			tree.addItem(item);
		}
		else {
			parentItem.addItem(item);
		}

//		// Add Q and DI
//		if ((!element.getTemplate().getQueries().isEmpty()) || 
//				(!element.getTemplate().getDIs().isEmpty())) {
//			item.addItem(new Label("Qs & DIs"), element);
//		}
		for (Iterator<BasicPageElement> iterator = element.getChildrenElements().iterator(); iterator.hasNext();) {
			BasicPageElement o =  iterator.next();
			CreateSubTreeFromElement(o, item);
		}
		item.setState(true);
	}

}
