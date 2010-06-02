package fi.tkk.media.xide.client.UI.Widget;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

import com.google.gwt.user.client.Event;

import fi.tkk.media.xide.client.Data.EventClickListener;
import fi.tkk.media.xide.client.popups.garbage.SmallHintPopup;

/**
 * Extends Label class to be able to add a hint to a label. Creates
 * corresponding SmallHintPopup and manage its visibility. When mouse is over
 * the label popup is shown, otherwise it is hidden.
 * 
 * Takes EventClickListener as a parameter in constructor. 
 * It is done to be able to add special actions which will be initiated when event comes. 
 * 
 * @author evgeniasamochadina
 * 
 */

public class GoodLabel extends Label {

	// Small hint popup with hint text which is shown when mouse is over the
	// button
	SmallHintPopup popup;
	
	EventClickListener clickListener;

	// Hint for different labels
	public static String[] hintTexts = {
	};

	/**
	 * Initializes the image (without URL) and the popup with the hint text
	 * 
	 * @param hintText text of hint message
	 */
	public GoodLabel(String hintText) {
		super();
		popup = new SmallHintPopup(this);
		popup.initPopup(hintText);
		this.sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEMOVE
				| Event.ONMOUSEOUT | Event.ONCLICK);
	}
	
	/**
	 * Initializes the image with the url and the popup with the hint text
	 * 
	 * @param hintText text of hint message
	 */
	public GoodLabel(String hintText, String url) {
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
	public GoodLabel(int hintNumber, String url) {
		this(
				hintNumber >= 0 ? (hintNumber < hintTexts.length ? hintTexts[hintNumber]
						: "No avaliable hint")
						: "No avaliable hint", url);

	}

	/**
	 * Initializes the image (without URL) and the popup with the hint text from the hint
	 * array
	 * 
	 * @param hintNumber
	 */
	public GoodLabel(int hintNumber) {
		this(
				hintNumber >= 0 ? (hintNumber < hintTexts.length ? hintTexts[hintNumber]
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
