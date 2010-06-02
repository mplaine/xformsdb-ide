package fi.tkk.media.xide.client.utils;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Data.EventClickListener;
import fi.tkk.media.xide.client.UI.Widget.GoodImage;
import fi.tkk.media.xide.client.popups.basic.Popup;
import fi.tkk.media.xide.client.popups.utils.interfaces.Action;

public class ActionWithTextAndIcon extends ActionWithText {
	// Icon size in pixel
	public static final int ICON_SIZE = 16;
	
//	protected Widget icon;
	protected String iconSource;
	protected int hintID;
	
	
	public ActionWithTextAndIcon() {
		iconSource = null;
	}

//	/**
//	 * Constructs the Action with text and simple icon representing the action.
//	 * If icon source is null, a simple panel is created instead of icon 
//	 * 
//	 * @param text Title of the action
//	 * @param iconSource Source of the icon 
//	 * @param action Action to do
//	 */
//	public ActionWithTextAndIcon(String text, String iconSource, Action action) {
//		// Call super constructor
//		super(text, action);
//		
//		// Create icon
//		if (iconSource == null) {
//			// No image
//			icon = new SimplePanel();
//			icon.setSize(ICON_SIZE + "px", ICON_SIZE + "px");
//		}
//		else {
//			icon = new Image(iconSource);
//		}
//		
//	}
	

	/**
	 * Constructs the Action with text and GoodButton icon representing the action.
	 * GoodButton has a hint text (which is the same as action text) and a click listener which performs the action when the button is clocked.
	 * If icon source is null, a simple panel is created instead of icon 
	 * @param text Title of the action
	 * @param iconSource Source of the icon 
	 * @param hintID id of the hint text (Texts are stored in GoodButton class)
	 * @param action Action to do
	 */
	public ActionWithTextAndIcon(int textAndHintID, String iconSource, Action action) {
		// Call super constructor
		super(GoodImage.getHintText(textAndHintID), action);
		
		this.iconSource = iconSource;
		this.hintID = textAndHintID;
		
	}
	
	/**
	 * Constructs the Action with text and GoodButton icon representing the action.
	 * GoodButton has a hint text and a click listener which performs the action when the button is clocked.
	 * If icon source is null, a simple panel is created instead of icon 
	 * @param text Title of the action
	 * @param iconSource Source of the icon 
	 * @param hintID id of the hint text (Texts are stored in GoodButton class)
	 * @param action Action to do
	 */
	public ActionWithTextAndIcon(String text, String iconSource, int hintID, Action action) {
		// Call super constructor
		super(text, action);
		
		this.iconSource = iconSource;
		this.hintID = hintID;
		
	}
	
	public Widget getIcon() {
		Widget icon = null;
		// Create icon
		if (iconSource == null) {
			// No image
			icon = new SimplePanel();
			icon.setSize(ICON_SIZE + "px", ICON_SIZE + "px");
		}
		else {
			// Create a GoodImage with corresponding hint id and icon source
			icon  = new GoodImage(hintID, iconSource);
			// Add a click listener for the button
			if (this.action != null) {
				// If there is an action
				((GoodImage)icon).setClickListener(new EventClickListener() {
					public void onClick(Event event) {
						ActionWithTextAndIcon.this.action.doAction();
						event.cancelBubble(true);
					}
					
				});
			}
			else {
				// If there is no action
				((GoodImage)icon).setClickListener(new EventClickListener() {
					public void onClick(Event event) {
						// Add a popup 
						new Popup("Unfortunately this is not implemented yet" );
						event.cancelBubble(true);
					}
					
				});
			}
		}
		return icon;
	}

	
	
	
}
