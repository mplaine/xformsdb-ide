package fi.tkk.media.xide.client.DnDTree;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.allen_sauer.gwt.dnd.client.util.DOMUtil;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.DnD.FlexTableUtil;
import fi.tkk.media.xide.client.Tabs.NavigatorTab;
import fi.tkk.media.xide.client.UI.Widget.BasicPageElement;
import fi.tkk.media.xide.client.UI.Widget.Component;
import fi.tkk.media.xide.client.UI.Widget.Slot;


public class DnDTreeController{

	private static int ii = 0;
	
	public static final int MIN_X_STEP = 10;
	public static final int MIN_Y_STEP = 10;
	private HashMap treeItems;
	private Widget movingWidget;
	private long itemCount;
	private DnDTree tree;
	private boolean dragging = false;
	private Widget proxy;
	private AbsolutePanel boundaryPanel;
	
	// Array Lists used to search target Slot
	
	// Stores Slots which can be a dnd-target in vertical order (starting from the highest one)
	// Slot which contains another slot appears twice: before child slot and after it as there are 2 ways how to add a component to it
	private ArrayList<DnDTreeItem> slotList;
	// Stores int vertical coordinate where for each Slot entrance. Coordinate indicates from where current slot dnd-area starts
	private ArrayList<Integer> startSlot;
	
	private int prevX,prevY = 0;
	
	private DnDTreeItem positionerSlotItem;
	private int positionerItemNumber;
	
	private int boundaryPanelHeight = 0;

	private MouseListenerAdapter mouseListenerAdapter = new MouseListenerAdapter() {
		private DnDTreeItem dndMovingItem;
		private void RemovePositioner (){
			
			if (positionerSlotItem != null){
				// Remove previous positioner
				boundaryPanel.remove(positioner);
				positionerSlotItem.getContent().removeStyleDependentName("draggin-slot-locked");
			}
			positionerSlotItem =null;
			positionerItemNumber = -1;
		}  
		
		private Widget positioner;

		public void onMouseUp(Widget sender, int x, int y) {

			if (dragging) {
				// Stop dragging
				dragging = false;
	
				// Release mouse capture from the moving widget
				DOM.releaseCapture(movingWidget.getElement());
	
				// If moved item was component
				if (dndMovingItem.getType() == DnDTreeItem.COMPONENT){
					// TODO: add normal styles changing to the selected item
	
					//Set default slyle name
					dndMovingItem.getContent().removeStyleDependentName("dragged-locked");
					
					// Get initial position of the moving item 
					int itemPreviousPositionN = dndMovingItem.getParentItem().getChildIndex(dndMovingItem);
	
					// There is a new position which differs from the old one 
					if ((positionerSlotItem != dndMovingItem.getParentItem()) ||
							((positionerItemNumber != itemPreviousPositionN)&&
							(positionerItemNumber != itemPreviousPositionN+1))){
						
						//If there was a positioner
						if (positionerSlotItem != null){
	
							// If old item position is before new one then
							if ((dndMovingItem.getParentItem() == positionerSlotItem) &&
									(itemPreviousPositionN< positionerItemNumber)){
								// Then it should be shifted up
								positionerItemNumber--;
							}
	
							// move dndMovingItem (slot dndMI.Parent) into 
							// position number positionerItemNumber (slot positionerSlotItem)
							Slot sourseSlot = (Slot)((DnDTreeItem)dndMovingItem.getParentItem()).getPageElement();
							Slot targetSlot = (Slot)((DnDTreeItem)positionerSlotItem).getPageElement();
							int sourseRow = itemPreviousPositionN;
							int targetRow = positionerItemNumber;

						    BasicPageElement child = sourseSlot.getChildrenElements().get(sourseRow);
						    
						    // Remove 
						    sourseSlot.RemoveComponent((Component)child);
						    
						    // Insert 
						    targetSlot.AddChild(child, targetRow);

//							tree.InsertTreeItem(positionerSlotItem, positionerItemNumber, dndMovingItem);

							NavigatorTab.getInstance().InsertItem(positionerSlotItem, targetRow, dndMovingItem);
						    
//							if ((sourseSlot != targetSlot) || (sourseRow > targetRow)) {
//								
//								FlexTableUtil.moveRow(sourseSlot, targetSlot, sourseRow, targetRow);
//							}
//							else {
//								FlexTableUtil.moveRow(sourseSlot, targetSlot, sourseRow, targetRow+1);
//							} 
//
//							BasicPageElement child= sourseSlot.getChildrenElements().get(sourseRow);
//							sourseSlot.getChildrenElements().remove(sourseRow);
//							
//						//	sourseSlot.RemoveComponent((Component)child);
////							if (sourseRow > targetRow) {
////								targetSlot.getChildrenElements().add(targetRow+1, child);
////							}
////							else {
//								targetSlot.getChildrenElements().add(targetRow, child);
////								child.setParentElement(targetSlot);
////							} 
//
//						  //  FlexTableUtil.moveRow(sourseSlot, targetSlot, sourseRow, targerRow);
//
//							// Insert moving item into selected place
						}
					}
					
					// Remove previous positioner
					RemovePositioner ();
	
					// Remove proxy item from parent
					proxy.removeFromParent();
					dndMovingItem.Select();
				}
				else {
				}
			}
//			boundaryPanel.setHeight(boundaryPanelHeight + "px");
		}

		public void onMouseDown(Widget sender, int x, int y) {
			// Removed. Don't have any idea why it was needed
//			boundaryPanelHeight = boundaryPanel.getOffsetHeight();
//			boundaryPanel.setHeight((boundaryPanel.getOffsetHeight()*110)/100 + "px");
			
				// Set moving widget as a panel which is going to be dragged
				movingWidget = sender;
				dndMovingItem = (DnDTreeItem) treeItems.get(movingWidget.getTitle());
				if (DnDTreeItem.selectedItem != dndMovingItem)
				{
					// Deselect previously seleced item
					if (DnDTreeItem.selectedItem != null) {
//						DnDTreeItem.selectedItem.content.removeStyleName("tree-item-selected");
					}
					// Item is not selected. Just select it
					DnDTreeItem.selectedItem  = dndMovingItem;
					Main.getInstance().setSelectedElement(dndMovingItem);
//					DnDTreeItem.selectedItem.content.setStyleName("tree-item-selected");
				}
				else
				{
					// This item has already been selected (DnDTreeItem.selectedItem == dndMovingItem) 
					
					// If selected item is a component
					if (dndMovingItem.getType() == DnDTreeItem.COMPONENT){
						// TODO: add normal styles changing to the selected item
						dndMovingItem.getContent().addStyleDependentName("dragged-locked");
						
						// Start dragging
						dragging = true;
	
						// Create proxy item
						proxy = new Label(((Label)dndMovingItem.getContent()).getText());
						proxy.setStyleName("tree-item-clone");
						
						// Add proxy to the boundary panel
						boundaryPanel.add(proxy,x+movingWidget.getAbsoluteLeft()-boundaryPanel.getAbsoluteLeft(), y+movingWidget.getAbsoluteTop()-boundaryPanel.getAbsoluteTop());
	
						DOM.setCapture(movingWidget.getElement());
					}
				}
		}

		public void onMouseMove(Widget sender, int x, int y) {
			if (!dragging) return;

			// Cancel text selection when dragging
			DOMUtil.cancelAllDocumentSelections();
			
			boundaryPanel.setWidgetPosition(proxy, 
					x+sender.getAbsoluteLeft()-boundaryPanel.getAbsoluteLeft(), 
					y+sender.getAbsoluteTop()-boundaryPanel.getAbsoluteTop());


			// If new coordinates differ a lot from previous then process mouse moving
			if (Math.abs(y-prevY) > MIN_Y_STEP){
				// TODO: think about X coordinate
				
				// Get overlapped slot and the number of item before positioner
				DnDTreeItem positionerNewSlotItem = GetDropTargetSlot(tree);
				positionerNewSlotItem.setState(true);

				int positionerNewItemNumber = GetDropTargetItemNumber(positionerNewSlotItem);
				int movingItemPositionNumber = dndMovingItem.getParentItem().getChildIndex(dndMovingItem);
			
				
				// If new mouse position equals initial ITEM position 
				if ((positionerNewSlotItem == dndMovingItem.getParentItem()) &&
						((positionerNewItemNumber == movingItemPositionNumber)||
						(positionerNewItemNumber == movingItemPositionNumber+1))){
					// Remove old positioner
					RemovePositioner();
				} 
				// Mouse position requires to move (or create new) positioner 
				else if ((positionerNewSlotItem != positionerSlotItem) ||
						(positionerNewItemNumber != positionerItemNumber)){

					// Remove old positioner
					RemovePositioner();
					// If the new slot for positioner has found 
					if (positionerNewSlotItem != null) {

						// If there are still no positioner
						if (positioner == null){
							// Create positioner
							positioner = new SimplePanel();
						    positioner.addStyleName("tree-positioner");
						    positioner.setPixelSize(60, 10);
						}

						if (positionerNewItemNumber < positionerNewSlotItem.getChildCount()){
							boundaryPanel.add(positioner, positionerNewSlotItem.getChild(positionerNewItemNumber).getAbsoluteLeft()-boundaryPanel.getAbsoluteLeft() + 23,
						    		positionerNewSlotItem.getChild(positionerNewItemNumber).getAbsoluteTop()-boundaryPanel.getAbsoluteTop()-5);
						}
						// Position should be placed in the empty slot (no children items)
						else if (positionerNewSlotItem.getChildCount() ==0 ){

						    boundaryPanel.add(positioner, positionerNewSlotItem.getAbsoluteLeft()-boundaryPanel.getAbsoluteLeft() + 16 + 23,
						    		positionerNewSlotItem.getAbsoluteTop()+ positionerNewSlotItem.getOffsetHeight()-boundaryPanel.getAbsoluteTop());
						}
						// Positioner should be placed after the last item in the list
						else if (positionerNewItemNumber == positionerNewSlotItem.getChildCount()){
						    boundaryPanel.add(positioner, positionerNewSlotItem.getChild(positionerNewItemNumber-1).getAbsoluteLeft()-boundaryPanel.getAbsoluteLeft() + 23,
						    		positionerNewSlotItem.getChild(positionerNewItemNumber-1).getAbsoluteTop()+ positionerNewSlotItem.getChild(positionerNewItemNumber-1).getOffsetHeight()-boundaryPanel.getAbsoluteTop());
						}

						// Set styling for the slot which contains positioner
						positionerNewSlotItem.getContent().addStyleDependentName("draggin-slot-locked");

						positionerSlotItem = positionerNewSlotItem;
						positionerItemNumber = positionerNewItemNumber;
					}
					
					}
	
					// Store new coordinates
					prevX = x;
					prevY = y;
				}
		}

	};

	public DnDTreeController(){
		treeItems = new HashMap();
		itemCount = 0;
	}

	public void makeDraggable(DnDTreeItem item){
		treeItems.put(itemCount+"", item);
		item.setIndex(itemCount);
		itemCount++;
		item.addMouseListener(mouseListenerAdapter);
		

	}

	public void setTree(DnDTree tree) {
		this.tree = tree;
	}

	public void setBoundaryPanel(AbsolutePanel boundaryPanel) {
		this.boundaryPanel = boundaryPanel;
	}

	public AbsolutePanel getBoundaryPanel() {
		return boundaryPanel;
	}

	
	private void GetSlotList(DnDTreeItem treeRoot){
//		ArrayList<Slot> result = new ArrayList<Slot>();
		if (treeRoot != (DnDTreeItem) treeItems.get(movingWidget.getTitle())) {
			if (treeRoot.getPageElement() instanceof Slot) {
				slotList.add(treeRoot);
				startSlot.add(treeRoot.getAbsoluteTop());
			}
	//		ArrayList<Slot> result = new ArrayList<Slot>();
			for (int i = 0; i < treeRoot.getChildCount(); i++) {
				DnDTreeItem o = treeRoot.getChild(i);
				GetSlotList(o);
				if (o.getPageElement().getParentElement() != slotList.get(slotList.size()-1).getPageElement()) {
					if (treeRoot.getPageElement() instanceof Slot) {
						slotList.add(treeRoot);
						startSlot.add(o.getAbsoluteTop() + o.getOffsetHeight());
					}
				}
			}
		}		
//		return result;
//		}
	}
	
	/**
	 * Finds a slot where which is now under proxy object
	 * @param tree Tree
	 * @return Tree item which is corresponds to a slot
	 */

	// TODO: make it work not only with Slots on the first level

	private DnDTreeItem GetDropTargetSlot(DnDTree tree){
		// Init slot lists
		slotList = new ArrayList<DnDTreeItem>();
		startSlot = new ArrayList<Integer>();
		
		// TODO: !!!! change this to web page! it should be web page on the top
		GetSlotList(tree.getItem(0));

		DnDTreeItem item = null;
		int slotStartsAt = 0;
		
		// Go from the bottom of the tree to find appropriate Slot
		for (int i = slotList.size()-1; i >=0 ; i--){
			// Get current slot information
			item = slotList.get(i);
			slotStartsAt = startSlot.get(i);
			// Check if the slot fits
			if (proxy.getAbsoluteTop() >= slotStartsAt
					&& (proxy.getAbsoluteLeft() <= item.getAbsoluteLeft()+3*item.getOffsetWidth())
					){
				return item;
			}
		}
//		for (int i = childrenCount-1; i >=0 ; i--){
//			item = slotListArray[i];
//			slotStartsAt = startSlot.get(i);
//			
//			if (proxy.getAbsoluteTop() >= slotStartsAt
//					&& (proxy.getAbsoluteLeft() <= item.getAbsoluteLeft()+3*item.getOffsetWidth())
//			){
//				return item;
//			}
//		}
		return item;
	}
	
//	/**
//	 * Old version
//	 * Finds a slot where which is now under proxy object
//	 * @param tree Tree
//	 * @return Tree item which is corresponds to a slot
//	 */
//
//	// TODO: make it work not only with Slots on the first level
//
//	private DnDTreeItem GetDropTargetSlot(DnDTree tree){
//		slotList = new ArrayList<DnDTreeItem>();
//		GetSlotList((DnDTreeItem)tree.getItem(0));
//		
//		int childrenCount = tree.getItemCount();
//		DnDTreeItem item = null;
//		for (int i = childrenCount-1; i >=0 ; i--){
//			item = (DnDTreeItem)tree.getItem(i);
//			if ((proxy.getAbsoluteTop() >= item.getAbsoluteTop())&& (proxy.getAbsoluteLeft() <= item.getAbsoluteLeft()+3*item.getOffsetWidth())){
//				return item;
//			}
//		}
//		return item;
//	}

	/**
	 * Finds drop target for the tree starting from the given tree item. 
	 * Searches crosses of the tree items and proxy object 
	 * 
	 * @param root head element of subtree
	 * @return drag target tree element
	 */
	// TODO: do not move Data instances and Queries !!!
	private int GetDropTargetItemNumber(DnDTreeItem root) {
		if (root == null){
			return -1;
		}
		else{
			// If there is no child items 
			if (root.getChildCount() == 0) {
				return 0;
			}
			else{
				// If there are children items
				// Find the one which is overlapped by the proxy
				int childrenCount = root.getChildCount();
				// Going from the botton of the children list
				for (int i = childrenCount-1; i >=0 ; i--){
					// If proxy is lower then the current item
					if (
							(proxy.getAbsoluteTop() >= root.getChild(i).getAbsoluteTop())
							&& (proxy.getAbsoluteLeft() <= root.getAbsoluteLeft()+root.getOffsetWidth())
							){
						// then the positioner should be placed after it
						return i+1;
					}
				}
				// If there is no child which is situated higher then proxy
				// then positioner should be placed before all children
				return 0;
			}
		}
	}
//	/**
//	 * Finds drop target for the tree starting from the given tree item. 
//	 * Searches crosses of the tree items and proxy object 
//	 * 
//	 * @param root head element of subtree
//	 * @return drag target tree element
//	 */
//	// TODO: do not move Data instances and Queries !!!
//	private int GetDropTargetItemNumber(DnDTreeItem root) {
//		if (root == null){
//			return -1;
//		}
//		else{
//			// If there is no child items 
//			if (root.getChildCount() == 0) {
//				return 0;
//			}
//			else{
//				// If there are children items
//				// Find the one which is overlapped by the proxy
//				int childrenCount = root.getChildCount();
//				// Going from the botton of the children list
//				for (int i = childrenCount-1; i >=0 ; i--){
//					// If proxy is lower then the current item
//					if (
//							(proxy.getAbsoluteTop() >= root.getChild(i).getAbsoluteTop())
//							&& (proxy.getAbsoluteLeft() <= root.getAbsoluteLeft()+root.getOffsetWidth())
//							){
//						// then the positioner should be placed after it
//						return i+1;
//					}
//				}
//				// If there is no child which is situated higher then proxy
//				// then positioner should be placed before all children
//				return 0;
//			}
//		}
//	}
}