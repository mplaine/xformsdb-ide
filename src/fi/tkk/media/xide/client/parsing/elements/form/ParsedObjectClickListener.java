package fi.tkk.media.xide.client.parsing.elements.form;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Widget;

public class ParsedObjectClickListener implements ClickListener, FocusListener{

	public void onClick(Widget sender) {
		((FocusWidget)sender).setFocus(false);
		// TODO Auto-generated method stub
		
	}

	public void onFocus(Widget sender) {
		((FocusWidget)sender).setFocus(false);// TODO Auto-generated method stub
		
	}

	public void onLostFocus(Widget sender) {
		// TODO Auto-generated method stub
		
	}

}
