package fi.tkk.media.xide.client.popups.basic;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class LoadingPopup {
	static LoadingPopup singleton;
	
	// Glass panel to hide the background and show really dimmed popup 
	static PopupPanel glass;
	
	static HTML loadingImage;
	
	public LoadingPopup() {
		initDimmedPopup();
	}
	
	public static void centerImage() {
		if (loadingImage == null) {
			loadingImage = new HTML("<img style=\"position:absolute; z-index:1;\" src=\"styleImages/loading.gif\"/>" );
		}
		RootPanel r = RootPanel.get();
		int left = (Window.getClientWidth() -100) >> 1;
		int top = (Window.getClientHeight() - 100) >> 1;
		r.add(loadingImage, left, top);
	}
	
	public static  void hideImage() {
		if(loadingImage != null) {
			loadingImage.removeFromParent();
		}
	}
	
	public static  void hideGlass() {
		if (glass!= null) {
			glass.hide();
		}
	}
	
	public static void show() {
		initDimmedPopup();
		centerImage();

	}
	
	public static void hide() {
		hideImage();
	}

	public static void showDimmed() {
		initDimmedPopup();
		glass.center();	
		centerImage();

	}
	
	public static void hideDimmed() {
		hideImage();
		hideGlass();
	}

	/**
	 * Initializes glass panel
	 */
	public static void initDimmedPopup() {
		if (glass == null) {
			// A glass panel or 'blinder'
			glass = new PopupPanel();
	
			// Set full screen
			DOM.setStyleAttribute(glass.getElement(), "width", "100%");
			DOM.setStyleAttribute(glass.getElement(), "height", "100%");
			
			// Add styles
			DOM.setStyleAttribute(glass.getElement(), "backgroundColor", "#FFFFFF");
			DOM.setStyleAttribute(glass.getElement(), "opacity", "0.00");
			DOM.setStyleAttribute(glass.getElement(), "filter", " alpha(opacity=0)");
			DOM.setStyleAttribute(glass.getElement(), "position", "relative");
			DOM.setStyleAttribute(glass.getElement(), "overflow", "scroll");
			DOM.setStyleAttribute(glass.getElement(), "zIndex", "1");
			
		}
	}
}
