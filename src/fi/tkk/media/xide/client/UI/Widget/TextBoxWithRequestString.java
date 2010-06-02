package fi.tkk.media.xide.client.UI.Widget;

import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class TextBoxWithRequestString extends TextBox implements FocusListener{

	private boolean hasChanged = false;
	private String requestString;
	
	public TextBoxWithRequestString(String requestString) {
		super();
		this.requestString = requestString;
		setText(requestString);
		// Make it gray before smth is typed
		addStyleDependentName("initial");
		addFocusListener(this);
	}
	
	/**
	 * Override of the getText function. Returns valuable text or "" 
	 */
	public String getText() {
		String text = super.getText(); 
	
		if (!text.equals("Search here")) {
			return text;
		}
		else {
			return "";
		}
	}
	
	
	public void onFocus(Widget sender) {
		if (!hasChanged) {
			removeStyleDependentName("initial");
			setText("");
			hasChanged = true;
		}
	}

	public void onLostFocus(Widget sender) {
		if (getText().equals("")) {
			setText(requestString);
			addStyleDependentName("initial");
			hasChanged = false;
		}
	}
}
