package fi.tkk.media.xide.client.Data;

/**
 * Interface for classes, which represents APElement in the UI. These are ExplorerTreeItem, APTreeItem and CAppTreeItem. 
 * @author evgeniasamochadina
 *
 */
public interface APElementHolder {
	/**
	 * Gets related APElement
	 * @return
	 */
	public APElement getAPElement();
	
	/**
	 * Is fired when APElement is going to be deleted. AP element is deleted adter this method is called.
	 */
	public void onDelete();
	
	/**
	 * Fired when new element is added to this element
	 * @param parent
	 * @param child
	 */
	public void onAddNewElement(APElement child);
}
