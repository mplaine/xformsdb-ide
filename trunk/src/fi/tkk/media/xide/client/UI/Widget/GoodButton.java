package fi.tkk.media.xide.client.UI.Widget;

import java.util.HashMap;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.popups.garbage.GoodPopupScrollablePanel;
import fi.tkk.media.xide.client.popups.garbage.SimplePopup;
import fi.tkk.media.xide.client.popups.garbage.SmallHintPopup;
import fi.tkk.media.xide.client.utils.PagePanel;

/**
 * Extends Button class to be able to add a hint to a button. 
 * Creates corresponding SmallHintPopup and manage its visibility.
 * When mouse is over the button popup is shown, otherwise it is hidden. 
 * @author evgeniasamochadina
 *
 */
public class GoodButton extends Button {
	// Small hint popup with hint text which is shown when mouse is over the button
	SmallHintPopup popup;

	
	// Hint for different buttons
	public static String[] hintTexts = {
		// PP footer
		"Saves changes made in currently selected component's template", 				// 0 Save button in PP footer
		"Cancel changes made in currently selected component's template",				// 1 Cancel button in PP footer
		"Delete currently selected component",											// 2 Delete button in PP footer
		"Search for template in the main database",										// 3 Search button on Search Tab
		// PP header
		"Log out from XIDE",															// 4 Header: Log out button
		"Go back to the Application Perspective where ou can see all you applications",	// 5 Header: Back to application button
		// AP details table
		"Add one more element", 														// 6 AP: Add button under details table
		"Delete element", 																// 7 AP: Delete action button in a table
		"Publish Application", 															// 8 AP: Publish action button in a table
		"Edit Page", 																	// 9 AP: Edit action button in a table
		"Unpublish Applicaion", 														// 10 AP: Unpublish Application action button in a table
		"Delete all applications",														// 11 AP: Delete all applications
		"Edit page content", 															// 12 Edit clickable icon in Explorer Tree Item
		"Publish application",				 											// 13 Publish clickable icon in Explorer Tree Item
		"Add child page",	 															// 14 Add child element
		"Delete element", 																// 15 Delete element
		"Add new application",															// 16 Add new application
		"Save this page as a new component",											// 17 Save page as component
		// File Structure tab
		"Upload new file",																// 18 Upload new file
		"Delete file",																	// 19 Delete file
		"Add new child folder",															// 20 Add new child folder
		"Delete this folder",															// 21 Delete this folder
		"Delete all files from this folder",											// 22 Delete all files from this folder
		"Edit this file",																// 23 Edit this file
		"Delete this file",																// 24 Delete this file
		"Delete this application",														// 25 Delete this application
	};

	/**
	 * Initializes the button and the popup with the hint text
	 * @param hintText text of hint message
	 */
	public GoodButton(String hintText) {
		super();
		popup = new SmallHintPopup(this);
		popup.initPopup(hintText);
		this.sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEMOVE
				| Event.ONMOUSEOUT|Event.ONCLICK);
	}
	
	/**
	 * Initializes the button with the init text and the popup with the hint text
	 * @param hintText text of hint message
	 */
	public GoodButton(String text, String hintText) {
		super(text);
		popup = new SmallHintPopup(this);
		popup.initPopup(hintText);
		this.sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEMOVE
				| Event.ONMOUSEOUT|Event.ONCLICK);
	}
	
	/**
	 * Initializes the button and the popup with the hint text from the hint array
	 * @param hintNumber
	 */
	public GoodButton(int hintNumber) {
		this(hintNumber > 0 ? 
				(hintNumber < hintTexts.length?  hintTexts[hintNumber] : "No avaliable hint")
				: "No avaliable hint" );

	}
	
	/**
	 * Initializes the button with the init text and the popup with the hint text from the hint array
	 * @param hintNumber
	 */
	public GoodButton(String text, int hintNumber) {
		this(text, hintNumber > 0 ? 
				(hintNumber < hintTexts.length?  hintTexts[hintNumber] : "No avaliable hint"): "No avaliable hint" );

	}
	
	/*
	 * Manage mouse events and show/hide popup when necssary
	 * @see com.google.gwt.user.client.ui.FocusWidget#onBrowserEvent(com.google.gwt.user.client.Event)
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
		}
		super.onBrowserEvent(event);
	}

}
