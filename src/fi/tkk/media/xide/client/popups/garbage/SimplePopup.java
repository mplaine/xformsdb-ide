package fi.tkk.media.xide.client.popups.garbage;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

public class SimplePopup extends AbsolutePanel{
	public SimplePopup() {
		super();
		this.setStyleName("design-handmade-popup");
	}
	
	public void show() {
    	RootPanel.get().add(this, 0, 0);
    	
	} 
	
	public void show(int left, int top) {
		RootPanel.get().add(this, left, top);

	} 
	
	public void hide() {
		this.removeFromParent();
	}
}
