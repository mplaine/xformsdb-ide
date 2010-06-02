/**
 * 
 */
package fi.tkk.media.xide.client.Data;

import java.util.ArrayList;

/**
 * @author Evgenia Samochadina
 * @date Dec 15, 2008
 *
 */
public interface Selectable {
	public static final int CHANGED = 1;
	public static final int CHANGED_LINKED = 2;
	public static final int SELECTED = 4;
	public static final int SELECTED_LINKED = 8;
	public static final int RESET = 16;
	public static final int HIGHLIGHTED = 32;
	public void Select();
	public void Unselect();
	public ArrayList<Selectable> GetLinkedObjects();
//	public void Highlight();
//	public void UnHighlight();
	public HasDisplayableProperties getProperties();
	/**
	 * Returns the real valuable element if this element is a wrapper (e.g. DnDTreeItem -> Component)
	 * @return
	 */
	public Selectable getValuableElement();
	public void Changed();
	public void Saved();
	public void Saved(SaveObjectsListener listener);
	public void afterSaved();
	public void Canceled();
	public void Deleted();
	public boolean isChanged();
	public void changeStyle(int event);
	/**
	 * Gets type of the element:
	 * - Application
	 * - Web Page
	 * - Page
	 * - Component
	 * - Slot
	 * - Template
	 * @return
	 */
	public String getTypeName();
}
