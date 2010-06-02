/**
 * 
 */
package fi.tkk.media.xide.client.UI.Widget;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.Data.APElementPage;
import fi.tkk.media.xide.client.Data.AccessRights;
import fi.tkk.media.xide.client.Data.SaveObjectsListener;
import fi.tkk.media.xide.client.Data.Selectable;
import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.DnD.DemoFlexTable;
import fi.tkk.media.xide.client.DnD.FlexTableRowDragController;
import fi.tkk.media.xide.client.DnD.FlexTableRowDropController;
import fi.tkk.media.xide.client.Tabs.PLTab;
import fi.tkk.media.xide.client.fs.XIDEFile;
import fi.tkk.media.xide.client.utils.Icons;

/**
 * @author Evgenia Samochadina
 * @date Dec 8, 2008
 *
 */
public class WebPage extends Component {
//	Template template;
//	BasicPageElement parent;
//	ArrayList<BasicPageElement> children;
	
	String URL;
	APElementPage pageAPElement;
	// Additional properties
	
//	public WebPage() {}
	
	public WebPage(Template template) {
		super(template, null);
		
		// Set the link to the corresponding page element
		this.pageAPElement = Main.getInstance().getLoadedPageAPElement();
		
		// Web Page does not have a parent
		this.template = template;
		this.children = new ArrayList<BasicPageElement>();
//		this.unsinkEvents(Event.ONMOUSEDOWN);

		this.template.getSourceCodeFirstFile().setValidationType(XIDEFile.VALIDATE_PAGE_SOURCE);
		this.panel.setStyleName("design-web-page");
		this.panel.getElement().setAttribute("id", "web-page-id");

		
		accessRightsSettings = new ArrayList<Integer>();
		accessRightsSettings.add(AccessRights.RIGHT_GRANTED);
		accessRightsSettings.add(AccessRights.RIGHT_DISABLED);
		
		this.sinkEvents(Event.ONMOUSEDOWN);
		// DND test

//	    Slot table1 = new Slot(Template.fakeTemplate("Slot1", "slot_id_1"), this);
//		AddChild(table1, 0);

//	    Slot table2 = new Slot(Template.fakeTemplate("Slot2", "slot_id_2"), this);
//		AddChild(table2, 0);
//		parseCode(false);
//		
//		DrawChildren(false);
//	    panel.add(new Label("or here: "));
//		AddNewChild(table2);

	    
		// Register components
//	    Main.getInstance().addDropController(table1);
//	    Main.getInstance().addDropController(table2);

	}
	
	public void onBrowserEvent(Event event) {
//		System.out.println("enevt");
		if ((DOM.eventGetType(event) == Event.ONMOUSEDOWN && DOM.eventGetButton(event) == Event.BUTTON_LEFT)) {
//			if (!initialTarget.contains("component") && !initialTarget.contains("container") ){
				Selectable selectedElement = Main.getInstance().getSelectedElement();
				
				if (selectedElement != this) {
					Main.getInstance().setSelectedElement(this);
					event.stopPropagation();
				} else {
					event.stopPropagation();
//					super.onBrowserEvent(event);
				}
				if (DOM.eventGetButton(event) == Event.BUTTON_RIGHT) {
				}
//			}
//			else{
//				// event from Component. Process further
//				super.onBrowserEvent(event);
//			}
		}
	}

	public void Draw() {
		
//		removeDropTarget();
		parseCode(false);
//	    Main.getInstance().PrintDropTargets();
//	    DrawChildren();
	}
	
	public void delete() {
	}
	
	public String getTypeName() {
		return "page";
	}
	
	public void Saved() {
		if (isChanged) {
			updateSlotInfo();

			// Send updated files
			template.sendUpdatedFilesToServer(null, this, true);
		}
	}
	
	public void Saved(SaveObjectsListener listener) {
		if (isChanged) {
			updateSlotInfo();

			// Send updated files
			template.sendUpdatedFilesToServer(listener, this, true);
		}
	}
	
	public void afterSaved() {
		if (isChanged) {
			
			// Update page properties to the server (includes parent application element update)
			pageAPElement.updateAPElement(false);
			
//			template.getSourceCodeFirstFile().updateContentToServer();
			isChanged = false;
			templateBackup = null;
			// Save slots
			for (Iterator<BasicPageElement> iterator = children.iterator(); iterator.hasNext();) {
				BasicPageElement o = iterator.next();
				if (o instanceof Slot) {
					Slot slot = (Slot)o;
					Main.getInstance().setElementSaved(slot);
				}
			}
			// Change style
			changeStyle(RESET);
		}
	}
	/**
	 * This method is called when a web page is changed from the UI and the web page source code should be updated. 
	 * Is is done when: 
	 * - Component is added, removed or moved
	 * - Parameters/Properties are changed by the user
	 */
	public void onChangedExternaly() {
		// Update slots content
		// Update properties		
	}
	
//	public void DrawChildren(boolean isProxy) {
//		for (Iterator<BasicPageElement> iterator = children.iterator(); iterator.hasNext();) {
//			BasicPageElement o =  iterator.next();
//			o.Draw(isProxy);
////			if (o instanceof Slot) {
////				
////			}
//		}
//	}

//	// TODO: should be inherited from component
//	public void Draw() {
//		
//	}
	
	public Image getImage() {
		return new Image(Icons.ICON_PAGE);
	}
}
