package fi.tkk.media.xide.client.UI.Widget;

import com.google.gwt.user.client.ui.Image;

import com.google.gwt.user.client.Event;

import fi.tkk.media.xide.client.Data.EventClickListener;
import fi.tkk.media.xide.client.popups.garbage.SmallHintPopup;

/**
 * Extends Image class to be able to add a hint to a image. Creates
 * corresponding SmallHintPopup and manage its visibility. When mouse is over
 * the image popup is shown, otherwise it is hidden.
 * 
 * Takes EventClickListener as a parameter in constructor. 
 * It is done to be able to add special actions which will be initiated when event comes. 
 * 
 * @author evgeniasamochadina
 * 
 */

public class GoodImage extends Image {

	// Small hint popup with hint text which is shown when mouse is over the
	// button
	SmallHintPopup popup;
	
	EventClickListener clickListener;

	// Hint for different buttons
	public static String[] hintTexts = {
			"Edit page content", 					// 0 Edit clickable icon in Explorer Tree Item
			"Publish application",				 	// 1 Publish clickable icon in Explorer Tree Item
			"Add child element", 					// 2 Add child clickable icon in Explorer Tree Item
			"Delete element" 						// 3 Add child clickable icon in Explorer Tree Item
	};

//	/**
//	 * Initializes the image (without URL) and the popup with the hint text
//	 * 
//	 * @param hintText text of hint message
//	 */
//	public GoodImage(String hintText) {
//		super();
//		popup = new SmallHintPopup(this);
//		popup.initPopup(hintText);
//		this.sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEMOVE
//				| Event.ONMOUSEOUT | Event.ONCLICK);
//	}
	
	/**
	 * Initializes the image with the url and the popup with the hint text
	 * 
	 * @param hintText text of hint message
	 */
	public GoodImage(String hintText, String url) {
		super(url);
		popup = new SmallHintPopup(this);
		popup.initPopup(hintText);
		this.sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEMOVE
				| Event.ONMOUSEOUT | Event.ONCLICK);
	}


	/**
	 * Initializes the image with image url and the popup with the hint text from the hint
	 * array
	 * 
	 * @param hintNumber
	 */
	public GoodImage(int hintNumber, String url) {
		this(
				hintNumber >= 0 ? (hintNumber < GoodButton.hintTexts.length ? GoodButton.hintTexts[hintNumber]
						: "No avaliable hint")
						: "No avaliable hint", url);

	}

//	/**
//	 * Initializes the image (without URL) and the popup with the hint text from the hint
//	 * array
//	 * 
//	 * @param hintNumber
//	 */
//	public GoodImage(int hintNumber) {
//		this(
//				hintNumber >= 0 ? (hintNumber < GoodButton.hintTexts.length ? GoodButton.hintTexts[hintNumber]
//						: "No avaliable hint")
//						: "No avaliable hint");
//
//	}

	public static String getHintText(int hintID) {
		return (hintID >= 0 ? (hintID < GoodButton.hintTexts.length ? GoodButton.hintTexts[hintID]
		                                                                  						: "No avaliable hint")
		                                                                						: "No avaliable hint");
	}
	
	public void setClickListener(EventClickListener listener) {
		this.clickListener = listener;
	}

	/*
	 * Manage mouse events and show/hide popup when necssary
	 * 
	 * @see
	 * com.google.gwt.user.client.ui.FocusWidget#onBrowserEvent(com.google.gwt
	 * .user.client.Event)
	 */
	public void onBrowserEvent(Event event) {
		
		if (event.getTypeInt() == Event.ONMOUSEOVER) {
			popup.displayPopupCorrectly();
		}
		if (event.getTypeInt() == Event.ONMOUSEOUT) {
			popup.hide();
		}
		if (event.getTypeInt() == Event.ONCLICK) {
			popup.hide();
			// TODO: now on click is placed here in order not to write check on event type in the 
			// click listener. It is assumed that click listener is needed only for onClick event.
			if (clickListener != null) {
				clickListener.onClick(event);			
			}
		}
		super.onBrowserEvent(event);
	}

}
