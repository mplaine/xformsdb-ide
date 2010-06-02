/**
 * 
 */
package fi.tkk.media.xide.client.fs;

import fi.tkk.media.xide.client.utils.ActionWithText;
import fi.tkk.media.xide.client.utils.ActionWithTextAndIcon;

/**
 * @author Evgenia Samochadina
 * @date Jun 14, 2009
 *
 */
public interface FileStructureElement {
	/**
	 *  Gets the name of element
	 * @return
	 */
	public String getName();
		
	/**
	 *  Gets absolute path of the element
	 * @return
	 */
	public String getAbsolutePath();
	
	/**
	 * Gets context menu, specified for the element
	 * @return
	 */
	public ActionWithTextAndIcon[] getContextMenuItems();

	/**
	 * Gets parent element of the element
	 * (in practice that's either null or folder)
	 * @return
	 */
	public FileStructureElement getParent();

	public void removeFromParent();
}
