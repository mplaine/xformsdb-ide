package fi.tkk.media.xide.client.popups.basic;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.UI.Widget.StyledButton;
import fi.tkk.media.xide.client.popups.utils.PopupContainer;
import fi.tkk.media.xide.client.utils.GoodHorizontalAdjustablePanel;
import fi.tkk.media.xide.client.utils.PagePanel;

public class Popup {
	
	public Popup(String message) {
		PopupContainer popup = PopupContainer.getPopup();
		popup.addContent(new Label(message));
		popup.addCloseButton("Close");
		popup.showPopup();
	}

	public Popup(Widget w) {
		PopupContainer popup = PopupContainer.getPopup();
		popup.addContent(w);
		popup.addCloseButton("Close");
		popup.showPopup();
	}
	
	// New version of the popup (one instance of all)
//	static Popup singleton;
//	PopupContainer popup;
//	public static Popup getLoginDialog(String message) {
//
//		if (singleton == null) {
//			singleton = new Popup();
//		}
//
//		singleton.init(message);
//		return singleton;
//	}
//
//	public Popup() {
//		popup = PopupContainer.getPopup();
//		popup.addCloseButton("Close");
//		popup.showPopup();
//	}
//	
//	public void init(String message) {
//		popup.addContent(new Label(message));
//	}
//
//	public Popup(Widget w) {
//		popup.addContent(w);
//	}
}
