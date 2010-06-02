package fi.tkk.media.xide.client.popups.garbage;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Small Hint popup is a popup which is shown closed to the widget when it is under the mouse.
 * Default position of the popup is on the top of the widget 
 * but it is automatically switched to the bottom if there is no enough space on the top.
 * 
 *  * @author evgeniasamochadina
 *
 */
public class SmallHintPopup extends SimplePopup {

	//Panel which contains popup text and triangle image for drawing a cloud
	VerticalPanel vp;
	
	// Triangle images for cloud drawing
	Image tria_up = null;
	Image tria_down = null; 
	
	// Represents the UI widget which needs a popup hint. The popup is created for this widget
	Widget widget;
	
	boolean isDisplayedOnBottom;
	
	public SmallHintPopup() {
		super();
		widget = null;
	}
	/**
	 * Constructor for the popup. Saves UI widget for which the popup is created.
	 * @param w
	 */
	public SmallHintPopup(Widget w) {
		super();
		widget = w;
	}
	
	public void setWidget(Widget w) {
		widget = w;
	}
	/**
	 * Initiates popup with the text string.
	 * Assignes corresponding styles.
	 * 
	 * @param text text string
	 */
	public void initPopup(String text) {
		 vp = new VerticalPanel();
					
		 Label l = new Label(text);
		 DOM.setStyleAttribute(l.getElement(), "fontSize", "75%");
		 l.setWidth("100%");
		 DOM.setStyleAttribute(l.getElement(), "color", "white");
		 DOM.setStyleAttribute(l.getElement(), "backgroundColor",
		 "RGB(60, 60, 60)");
		 DOM.setStyleAttribute(l.getElement(), "padding", "3px");
		 vp.add(l);
		 
		 tria_down = new Image("TagIcons/tria_down.gif");
		 DOM.setStyleAttribute(tria_down.getElement(), "marginLeft", "5px");
		 isDisplayedOnBottom = true;
		 vp.add(tria_down);
					
		 DOM.setStyleAttribute(vp.getElement(), "opacity", "0.70");
		 DOM.setStyleAttribute(vp.getElement(), "filter",
		 " alpha(opacity=70)");
		
		 add(vp);

	}


	/**
	 * Sets correct position of the triangle image which illustrates from where the popup goes
	 * 
	 * There are 2 types of triangle: one for popup showed on the bottom of the widget (tria_down) 
	 * and one for popup showed on the top of the widget (tria_up)
	 * 
	 * @param isDisplayedOnBottom new position of the popup
	 */
	public void setPopupImage(boolean isDisplayedOnBottom) {
		// Check if position of popup is still the same
		if (this.isDisplayedOnBottom == isDisplayedOnBottom){
			// then do nothing
			return;
		}
		
		// Else image position should be changed
		// Save new popup position
		this.isDisplayedOnBottom = isDisplayedOnBottom;
		
		// If we should display popup on the bottom => we need triangle looking down 
		if (isDisplayedOnBottom) {
			// If it is not the first time when popup is running 
			// Remove current image (it were on the top)
			if (vp.getWidgetCount() > 1) {
				vp.remove(0);
			}
			// If there is now down image create one
			if (tria_down == null) {
				tria_down = new Image("TagIcons/tria_down.gif");
				DOM.setStyleAttribute(tria_down.getElement(), "marginLeft", "5px");
			}
			// Add image
			vp.add(tria_down);

			return;
		} 
		
		// Else we need to display popup on the pop => we need triangle looking up
		
		// If it is not the first time when popup is running 
		// Remove current image (it were on the bottom)
		if (vp.getWidgetCount() > 1) {
			vp.remove(1);
		}
		if (tria_up == null) {
			tria_up = new Image("TagIcons/tria_up.gif");
			DOM.setStyleAttribute(tria_up.getElement(), "marginLeft", "5px");
		}
		// Add image (now it is after the popup)
		vp.add(tria_up);
		// Add popup widget to the end (now image is before the popup)
		vp.add(vp.getWidget(0));
		 
	}
	
	/**
	 * Displays popup on a correct position (on the top or on the bottom of the widget)
	 * 
	 */
	public void displayPopupCorrectly() {
		// Coordinates of the widget which needs a popup
		int widgetLeft = widget.getAbsoluteLeft();
		int widgetTop = widget.getAbsoluteTop();

		// Show popup and get correct values of its offsets
		show(widgetLeft, widgetTop);
		int height = getOffsetHeight();

		// If there is no enough space on the top of the button to display a popup
		// then display it on the bottom
		if (widgetTop - height - 5 < 0) {
			// Display on the bottom
//			switchView();
			setPopupImage(false);
			show(widgetLeft + 5, widgetTop + widget.getOffsetHeight());
		}
		else {
			// Display on the top
			setPopupImage(true);
			show(widgetLeft + 5, widgetTop - height);
		}
	}

}
