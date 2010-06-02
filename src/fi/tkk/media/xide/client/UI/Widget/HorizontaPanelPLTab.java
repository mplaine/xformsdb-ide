package fi.tkk.media.xide.client.UI.Widget;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.TreeItem;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.Tabs.PLTab;

public class HorizontaPanelPLTab extends HorizontalPanel implements
		SourcesMouseEvents {
	private MouseListenerCollection mouseListeners;
	public Template template;
	public Template templateBackup;
	public TreeItem item;
//	public boolean isDraggable = false;

	private Event event;

	public HorizontaPanelPLTab(Template template, TreeItem item) {
		super();
		this.template = template;
		this.item = item;
		sinkEvents(Event.MOUSEEVENTS);
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public void addMouseListener(MouseListener listener) {
		if (mouseListeners == null) {
			mouseListeners = new MouseListenerCollection();
			sinkEvents(Event.MOUSEEVENTS);
		}
		mouseListeners.add(listener);

	}

	public void removeMouseListener(MouseListener listener) {
		if (mouseListeners != null) {
			mouseListeners.remove(listener);
		}
	}

	public void onBrowserEvent(Event event) {

		// // Handle mouse selection
		// super.onBrowserEvent(event);

		switch (DOM.eventGetType(event)) {
		// case Event.ONCLICK:
		case Event.ONMOUSEDOWN:
			// System.out.println("1mouse down. event: " + this.toString());
		case Event.ONMOUSEUP:
			// System.out.println("2mouse up. event: " + this.toString());
		case Event.ONMOUSEMOVE:
		case Event.ONMOUSEOVER:
		case Event.ONMOUSEOUT:
//			// System.out.println("on browser event");
//			if ((DOM.eventGetType(event) == Event.ONMOUSEDOWN)
//					|| (DOM.eventGetType(event) == Event.ONMOUSEUP)) {
////				System.out.println(this.getTemplate().info.getProperty( ShortInfo.TITLE).getStringValue()+ " MouseUp Size: " + mouseListeners.size());
//				if ((!isDraggable) && (item.isSelected())) {
//					
//					System.out.println("making draggable");
//					Main.getInstance().getDragControllerPLAdding()
//							.makeDraggable(this);
//					isDraggable = true;
//				}
//			}
			if (mouseListeners != null) {

				mouseListeners.fireMouseEvent(this, event);
			}
			break;
		}
	}
}