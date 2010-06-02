package fi.tkk.media.xide.client.popups.basic;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.UI.Widget.StyledButton;
import fi.tkk.media.xide.client.popups.utils.PopupBase;
import fi.tkk.media.xide.client.popups.utils.PopupContainer;

/**
 * Popup, which shows a question and 2 optional answers (Yes and No). For each question
 * it is possible to add click listener which will be called when the button is clicked. 
 *  
 * @author evgeniasamochadina
 *
 */
public class PopupAreYouSure extends PopupBase {
	public PopupAreYouSure(String question, final ClickListener listenerForYesButton, final ClickListener listenerForNoButton) {
		super("Are you sure?", question);
//		this.setHeight("40%");
//		this.get
		StyledButton yesButton = null;
		StyledButton noButton= null;
		
		if (listenerForYesButton != null) {
			yesButton = new StyledButton("Yes", null, new ClickHandler() {

				public void onClick(ClickEvent event) {
					popup.hide();
					listenerForYesButton.onClick((Widget)event.getSource());					
				}
			}, StyledButton.STYLE_GREY);
		}
		else {
			yesButton = new StyledButton("Yes", null, new ClickHandler() {

				public void onClick(ClickEvent event) {
					popup.hide();				
				}
			}, StyledButton.STYLE_GREY);
		}
		if (listenerForNoButton != null) {
			noButton = new StyledButton("No", null, new ClickHandler() {

				public void onClick(ClickEvent event) {
					listenerForNoButton.onClick((Widget)event.getSource());					
				}
			}, StyledButton.STYLE_GREY);
		}
		else {
			noButton = new StyledButton("No", null, new ClickHandler() {

				public void onClick(ClickEvent event) {
					popup.hide();				
				}
			}, StyledButton.STYLE_GREY);
		}
		
		popup.addButton(yesButton.getButton());
		popup.addButton(noButton.getButton());
		popup.addCloseButton("Cancel");
		popup.showPopup();
	}
}
