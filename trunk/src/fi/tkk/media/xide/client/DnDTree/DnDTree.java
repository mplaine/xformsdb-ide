package fi.tkk.media.xide.client.DnDTree;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.TreeListener;
import com.google.gwt.user.client.ui.Widget;

public class DnDTree extends Tree {
	
	@Override
	public void setSelectedItem(TreeItem item, boolean fireEvents) {
		
	}
	 
	private native void disableContextMenu(Element elem) /*-{
	elem.oncontextmenu=function(a,b) {return false};
}-*/;

	/**
	 * Gets Tree Item which contains specified index
	 * @param w
	 * @return
	 */
	public TreeItem GetTreeItem(Widget w){
		Iterator<TreeItem> it = this.treeItemIterator();
		while(it.hasNext()){
			TreeItem treeItem = it.next();
			if (treeItem.getWidget() == w){
				return treeItem;
			}
		}
		return null;
	}
	/**
	 * Gets the index of the specified child item from the first level.
	 * 
	 * @param child Tree Item
	 * @return Index
	 */
	 
	
	  public int getChildIndex(TreeItem child) {
		  
		  int numberOfChilren = this.getItemCount();
		  for (int i = 0; i < numberOfChilren; i++){
			TreeItem item = this.getItem(i);
			if (item == child){
				return i;
			}
		  }
		  return -1;
	 }
	  
	/**
	 * Gets Tree Item which is next after the given tree item.
	 * Returns null if the item is last in the list.
	 * @param w
	 * @return
	 */
	public DnDTreeItem GetNextTreeItem(DnDTreeItem item){
		// Parent Item
		DnDTreeItem parent = (DnDTreeItem)item.getParentItem();
		
		int itemIndex;
		int numberOfChilren;
		// If the item is not on the first level
		if (parent != null){
			// Index of the item in the list of parent's children
			itemIndex = parent.getChildIndex(item);
			numberOfChilren = parent.getChildCount();
			// If the item is not last item
			if (itemIndex + 1 < numberOfChilren){
				// Return next item in the list
				return (DnDTreeItem)parent.getChild(itemIndex + 1);
			}
			else {
				// Else return null
				return null;
			}
		}
		// If the item is on the first level
		else {
			// Index of the item in the list of root's children
			itemIndex = this.getChildIndex(item);
			numberOfChilren = this.getItemCount();
			// If the item is not last item
			if (itemIndex + 1 < numberOfChilren){
				// Return next item in the list
				return (DnDTreeItem)this.getItem(itemIndex + 1);
			}
			else {
				// Else return null
				return null;
			}
		}

	}

	/**
	 * Insert Tree Item created based on widget before specified index
	 * @param index
	 * @return
	 */
	public boolean InsertTreeItem (DnDTreeItem parentItem, int index, Widget w){
		if (parentItem != null) {
			int itemsSize = parentItem.getChildCount(); 
			if (index <= itemsSize){
				DnDTreeItem[] items = new DnDTreeItem[itemsSize];
				for (int i = 0; i < itemsSize; i++){
					items[i] = (DnDTreeItem)parentItem.getChild(i);
				}
				parentItem.removeItems();
				
				for (int i = 0; i <= index-1; i++){
					parentItem.addItem(items[i]);
				}
				parentItem.addItem(w);
				for (int i = index; i < itemsSize; i++){
					parentItem.addItem(items[i]);
				}
				parentItem.setState(true);
				return true;
			}
			else {
				return false;
			}

			}
			else {
				return false;
			}
	}

	/**
	 * Insert Tree Item before specified index
	 * @param index
	 * @return
	 */
	public boolean InsertTreeItem(DnDTreeItem parentItem, int index,
			DnDTreeItem item) {

		TreeItem parent = item.getParentItem();
		item.remove();

		if (parentItem != null) {
			int itemsSize = parentItem.getChildCount();

			if (index <= itemsSize) {
				DnDTreeItem copyTree = new DnDTreeItem();

				for (int i = index; i < itemsSize; i++) {
					copyTree.addItem((DnDTreeItem) parentItem.getChild(index));
				}

				parentItem.addItem(item);

				for (int i = index; i < itemsSize; i++) {
					parentItem.addItem(copyTree.getChild(0));
				}
				if (parent != null) {
					parent.getElement().getStyle().setProperty("padding", "0px");
				}
				parentItem.getElement().getStyle().setProperty("padding", "0px");
				parentItem.setState(true);

				return true;
			} 
			else {	return false;	}

		} else {	
			// Add element to the top
			this.addItem(item);
			return true;	
			}

	}

public DnDTreeItem getItem(int index) {
	return (DnDTreeItem)super.getItem(index);
}
public void onBrowserEvent(Event event) {
	// Disabling the default context menu for the tree  
	disableContextMenu(getElement());

	// Parent method 
//	super.onBrowserEvent(event);

	// Where the button mouse has been released
	final int mouseX = DOM.eventGetClientX(event);
	final int mouseY = DOM.eventGetClientY(event);

	// When the button mouse is released
	if (DOM.eventGetType(event) == Event.ONMOUSEDOWN) {
		switch (DOM.eventGetButton(event)) {
			// If it is right click
			case Event.BUTTON_RIGHT :
				// Get current tree item
				TreeItem currentTreeItem = this.getSelectedItem();
				
				// Create new PopUpPanel
				final PopupPanel p = new PopupPanel(true);

				// Set popup position according to the place of mouse click

				p.setPopupPosition(mouseX, mouseY);
				p.show();

				p.setWidget(new Label("Popup text!"));
				p.setStyleName("design-Popup");

				break;
			}
	}
}

}