/**
 * 
 */
package fi.tkk.media.xide.client.Tabs;

import java.io.Serializable;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.UI.Widget.GoodImage;
import fi.tkk.media.xide.client.fs.FileStructureElement;
import fi.tkk.media.xide.client.fs.XIDEFile;
import fi.tkk.media.xide.client.fs.XIDEFolder;
import fi.tkk.media.xide.client.utils.ActionWithTextAndIcon;
import fi.tkk.media.xide.client.utils.ConnectionToServer;
import fi.tkk.media.xide.client.utils.GoodHorizontalAdjustablePanel;
import fi.tkk.media.xide.client.utils.Icons;

/**
 * @author Evgenia Samochadina
 * @date Mar 8, 2009
 *
 */
public class FileTreeItem extends TreeItem implements Serializable{
	private ConnectionToServer connectionToServer; 
	FileStructureElement element;

	
	private boolean isChanged = false;

	transient private Label itemTextLabel;
	transient private HorizontalPanel icons;
	
	// Connection to server
	ConnectionToServer connection;
	
	
	public FileTreeItem() {
		super();
//		connection = new ConnectionToServer();
	}
	
	public FileTreeItem(XIDEFile elementInitial){
		this();
		this.element = elementInitial;
//		this.element.item = this;
			HorizontalPanel hp  = new HorizontalPanel();	
		hp.add(new Image(Icons.FILE));
		
		DOM.setStyleAttribute(hp.getWidget(0).getElement(), "paddingLeft", "3px");
		DOM.setStyleAttribute(hp.getWidget(0).getElement(), "paddingRight", "3px");

		// Title
		itemTextLabel = new Label(elementInitial.getName());
		hp.add(itemTextLabel);
		
		icons = new HorizontalPanel();

		
		Widget i;
		
		ActionWithTextAndIcon[] menuItems = elementInitial.getContextMenuItems();
		for (int k = 0 ; k < menuItems.length; k++) {
			final ActionWithTextAndIcon action = menuItems[k];
			if (action != null) {
				i = action.getIcon();
//				image = new GoodImage(action.getText(), Icons.ADD);
//				image.setClickListener(new EventClickListener() {
//				
//							public void onClick(Event event) {
//								action.doAction();
//								event.cancelBubble(true);
//							}
//							
//						});
//				i = image;
			}
			else {
				i = new SimplePanel();
				i.setSize("16px", "16px");

			}
			DOM.setStyleAttribute(i.getElement(), "paddingLeft", "2px");
			DOM.setStyleAttribute(i.getElement(), "paddingRight", "2px");

			i.sinkEvents(Event.ONCLICK);
			icons.add(i);

		}
	
//		// Edit
//
//		GoodImage image = new GoodImage(0, Icons.EDIT);
//			image.setClickListener(new EventClickListener() {
//
//				public void onClick(Event event) {
//					(new ModalDimmedPopup("Edit a file: Unfortunately this is not implemented yet" )).showPopup(true);
//					event.cancelBubble(true);
//				}
//			});
//			i = image;
//
//		DOM.setStyleAttribute(i.getElement(), "paddingLeft", "2px");
//		DOM.setStyleAttribute(i.getElement(), "paddingRight", "2px");
//
//		i.sinkEvents(Event.ONCLICK);
//		icons.add(i);
//		
//	
//		//Add
//			i = image;
//			i = new SimplePanel();
//			i.setSize("16px", "16px");
//		
//		DOM.setStyleAttribute(i.getElement(), "paddingLeft", "2px");
//		DOM.setStyleAttribute(i.getElement(), "paddingRight", "2px");
//		i.sinkEvents(Event.ONCLICK);
//		icons.add(i);
//
//		// Delete
//		image = new GoodImage(3, Icons.DELETE);
//		image.setClickListener(new EventClickListener() {
//
//			public void onClick(Event event) {
//				(new PopupAreYouSure("You are going to delete a file. Are you sure?", new ClickListener() {
//					public void onClick(Widget sender) {
//						deleteElement();
//					}
//					
//				},null)).showPopup(true);
//				
//				event.cancelBubble(true);
//			}
//			
//		});
//		i = image;
//		DOM.setStyleAttribute(i.getElement(), "paddingLeft", "2px");
//		DOM.setStyleAttribute(i.getElement(), "paddingRight", "2px");
//
//		i.sinkEvents(Event.ONCLICK);
//		icons.add(i);
		GoodHorizontalAdjustablePanel panel = new  GoodHorizontalAdjustablePanel(hp, "left", icons, "right");
		
		panel.setStyleName("gwt-tree-file-tab-item");
//		HorizontalPanel panel100 = new HorizontalPanel();
//		panel100.setWidth("100%");
//		panel100.setStyleName("tree-item");
//		panel100.add(panel);
//		setWidget(panel100);

		setWidget(panel);
		hp.sinkEvents(Event.ONCLICK|Event.ONDBLCLICK);
//		getWidget().setStyleName("tree-item");
	}


	public FileTreeItem(XIDEFolder elementInitial){
		this();
		this.element = elementInitial;
//		this.element.item = this;
			HorizontalPanel hp  = new HorizontalPanel();	
		hp.add(new Image(Icons.FOLDER));
		
		DOM.setStyleAttribute(hp.getWidget(0).getElement(), "paddingLeft", "3px");
		DOM.setStyleAttribute(hp.getWidget(0).getElement(), "paddingRight", "3px");

		// Title
		itemTextLabel = new Label(elementInitial.getName());
		hp.add(itemTextLabel);
		
		icons = new HorizontalPanel();

		
		Widget i;
		
		// Edit

		GoodImage image;
		i = new SimplePanel();
		i.setSize("16px", "16px");
		
		DOM.setStyleAttribute(i.getElement(), "paddingLeft", "2px");
		DOM.setStyleAttribute(i.getElement(), "paddingRight", "2px");

		i.sinkEvents(Event.ONCLICK);
		icons.add(i);
		
	
		//Add
		
		ActionWithTextAndIcon[] menuItems = elementInitial.getContextMenuItems();
		for (int k = 0 ; k < menuItems.length; k++) {
			final ActionWithTextAndIcon action = menuItems[k];
			if (action != null) {
				i = action.getIcon();
//				image = new GoodImage(action.getText(), Icons.ADD);
//				image.setClickListener(new EventClickListener() {
//				
//							public void onClick(Event event) {
//								action.doAction();
//								event.cancelBubble(true);
//							}
//							
//						});
//				i = image;
			}
			else {
				i = new SimplePanel();
				i.setSize("16px", "16px");

			}
			
			DOM.setStyleAttribute(i.getElement(), "paddingLeft", "2px");
			DOM.setStyleAttribute(i.getElement(), "paddingRight", "2px");

			i.sinkEvents(Event.ONCLICK);
			icons.add(i);

		}
//		image = new GoodImage(2, Icons.ADD);
//		image.setClickListener(new EventClickListener() {
//
//			public void onClick(Event event) {
//				
////				(new ModalDimmedPopup("Unfortunately this is not implemented yet" )).showPopup(true);
//				(new UploadFilePopup((XIDEFolder)element)).showPopup(true);
//				event.cancelBubble(true);
//			}
//			
//		});
//		i = image;
//
//		
//		DOM.setStyleAttribute(i.getElement(), "paddingLeft", "2px");
//		DOM.setStyleAttribute(i.getElement(), "paddingRight", "2px");
//		i.sinkEvents(Event.ONCLICK);
//		icons.add(i);

//		// Delete
//		image = new GoodImage(3, Icons.DELETE);
//		image.setClickListener(new EventClickListener() {
//
//			public void onClick(Event event) {
//				(new PopupAreYouSure("You are going to delete a file. Are you sure?", new ClickListener() {
//					public void onClick(Widget sender) {
//						deleteElement();
//					}
//					
//				},null)).showPopup(true);
//				
//				event.cancelBubble(true);
//			}
//			
//		});
//		i = image;
//		DOM.setStyleAttribute(i.getElement(), "paddingLeft", "2px");
//		DOM.setStyleAttribute(i.getElement(), "paddingRight", "2px");
//
//		i.sinkEvents(Event.ONCLICK);
//		icons.add(i);
		GoodHorizontalAdjustablePanel panel = new  GoodHorizontalAdjustablePanel(hp, "left", icons, "right");
		
		panel.setStyleName("gwt-tree-file-tab-item");
		
//		HorizontalPanel panel100 = new HorizontalPanel();
//		panel100.setWidth("100%");
//		panel100.setStyleName("tree-item");
//		panel100.add(panel);
//		setWidget(panel100);

		setWidget(panel);
		hp.sinkEvents(Event.ONCLICK|Event.ONDBLCLICK);
//		getWidget().setStyleName("tree-item");
	}

	
	public String getText() {
			return element.getName();
	}
	
	public void addItem(FileTreeItem item) {
		super.addItem(item);
		DOM.setStyleAttribute(DOM.getFirstChild(getElement()), "width", "100%");
		DOM.setStyleAttribute(DOM.getFirstChild(DOM.getFirstChild(DOM.getFirstChild(DOM.getFirstChild(getElement())))), "width", "16px");
		
	}

}
