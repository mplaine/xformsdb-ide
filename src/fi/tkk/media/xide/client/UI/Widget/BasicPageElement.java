/**
 * 
 */
package fi.tkk.media.xide.client.UI.Widget;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Data.Selectable;
import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.DnDTree.DnDTreeItem;

/**
 * @author Evgenia Samochadina
 * @date Dec 8, 2008
 *
 */
public interface BasicPageElement extends Selectable {


	/**
	 * Constructor 
	 */
//	public BasicPageElement(Template template, BasicPageElement parent) {
//		super();
//		this.setTemplate(template);
//		this.setParentElement(parent);
//		this.setChildrenElements(new ArrayList<BasicPageElement>());
//	}
	
	public abstract void SaveAsNewComponent();

	// Used for drawing (and redrawing) element's body
	//Manages DnD functionality as well
	public abstract void Draw();
	public abstract void Draw(boolean isProxy);
	
	// Logically adds child to the element and initiate redraw process
//	public abstract void AddChild();

	/**
	 * Adds new child to the element logically and graphically
	 */
	public void AddChild(BasicPageElement element, int row);

	public void setTreeItem(DnDTreeItem item);
	
	public Widget GetPanel();
	
	// Delete element logically and physically 
	public void delete();
	
	public void setTemplate(Template template);
	public Template getTemplate();

	public void setParentElement(BasicPageElement parent);
	public BasicPageElement getParentElement(); 
	
	public void setChildrenElements(ArrayList<BasicPageElement> children);
	public ArrayList<BasicPageElement> getChildrenElements();
	
	public void addNewRole(int roleID, int value);
	public void removeRole(int roleID);
	public void setRightToRole(int roleID, int value, boolean propagateToChildren);
	
	public ArrayList<Integer> getAccessRightsSettings();
	public void setAccessRightsSettings(ArrayList<Integer> accessRightsSettings);

	public String getXMLCode();
	// Removes current element from drop target list and initiates remove 
	// process for children
	public void removeDropTarget();
//	public int getRightofRole(int roleID);
	
	// TreeItem is a link to the item in the Navgation Tab
	public DnDTreeItem getTreeItem();

	/**
	 * Gets the image that shows the element type (webPage, component, container)
	 * @return
	 */
	public abstract Image getImage();
}

