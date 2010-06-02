package fi.tkk.media.xide.client.UI.Widget;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.utils.ActionWithText;

public class ContextMenu extends PopupPanel {
//	String[] actions;

	
	public ContextMenu(ActionWithText[]actions) {
		super(true);
		DOM.setStyleAttribute(this.getElement(), "border", "1px solid gray");
		DOM.setStyleAttribute(this.getElement(), "backgroundColor", "lightgray");
		DOM.setStyleAttribute(this.getElement(), "opacity", "0.90");
		
		this.sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEMOVE | Event.ONMOUSEOUT);
		
		VerticalPanel hp = new VerticalPanel();
		DOM.setStyleAttribute(hp.getElement(), "paddingTop", "3px");
		DOM.setStyleAttribute(hp.getElement(), "paddingBottom", "3px");
		
		for (int i = 0; i < actions.length; i++) {
			if (actions[i] != null) {
				hp.add(drawItem(actions[i]));
			}
		}
		this.add(hp);
	}

	private Widget drawItem(final ActionWithText  menuItem) {
		Label title = new Label(menuItem.getText()) {
			@Override
			public void onBrowserEvent(Event event) {
				super.onBrowserEvent(event);
				switch (event.getTypeInt()) {
				case Event.ONMOUSEOVER:
					DOM.setStyleAttribute(this.getElement(), "backgroundColor", "gray");	
					DOM.setStyleAttribute(this.getElement(), "border", "1px solid darkgray");
					break;
				case Event.ONMOUSEMOVE:
					
					break;
				case Event.ONMOUSEOUT:
					DOM.setStyleAttribute(this.getElement(), "backgroundColor", "lightgray");
					DOM.setStyleAttribute(this.getElement(), "border", "1px solid lightgray");
					break;
				case Event.ONCLICK:
					ContextMenu.this.hide();
					menuItem.doAction();
					break;
				}
			}
		};
		title.sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEMOVE | Event.ONMOUSEOUT | Event.ONCLICK);
		DOM.setStyleAttribute(title.getElement(), "fontSize", "90%");
		DOM.setStyleAttribute(title.getElement(), "padding", "1px");

		return title;
	}
}
