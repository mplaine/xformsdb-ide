package fi.tkk.media.xide.client.popups.utils;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.UI.Widget.StyledButton;
import fi.tkk.media.xide.client.popups.utils.interfaces.Action;
import fi.tkk.media.xide.client.utils.GoodHorizontalAdjustablePanel;

/**
 * Base class for popup singleton classes. Contains
 * - fields to store panels, which form popup (header, content and footer); 
 * - bottom button panel management;
 * - glass management
 * 
 * @author evgeniasamochadina
 *
 */
public class PopupContainerBase extends DialogBox{
	// Glass panel to hide the background and show really dimmed popup 
	static PopupPanel glass;
	
	// Panel to add buttons to
	HorizontalPanel buttonPanel;

	Widget header = null;
	Widget content = null;
	Widget footer = null;
	
	public static final int HEADER_TEXT = 1;
	public static final int HEADER_1_TEXT = 2;
	public static final int HEADER_2_TEXT = 3;
	public static final int NORMAL = 4;
	public static final int SMALL = 5;

	// Error message popup
	public static final int ERROR_MESSAGE_MAIN_TEXT = 6;
	public static final int ERROR_MESSAGE_SERVER_LABEL = 7;
	public static final int ERROR_MESSAGE_SERVER_TEXT = 8;
	public static final int ERROR_MESSAGE_ACTION_TEXT = 9;
	
	public static final int HEADER_DESCRIPTION = 10;

	/**
	 * Initialize and return a panel for buttons on the top. The panel needs to be added to the footer
	 * @return
	 */
	protected Panel initButtonPanel() {
		buttonPanel = new HorizontalPanel();
		return new GoodHorizontalAdjustablePanel(new Label(""), "left", buttonPanel, "right");
	}
	
	/**
	 * Initializes glass panel
	 */
	public void initDimmedPopup() {
		if (glass == null) {
			// A glass panel or 'blinder'
			glass = new PopupPanel();
	
			// Set full screen
			DOM.setStyleAttribute(glass.getElement(), "width", "100%");
			DOM.setStyleAttribute(glass.getElement(), "height", "100%");
			
			// Add styles
			DOM.setStyleAttribute(glass.getElement(), "backgroundColor", "#A0A0A0");
			DOM.setStyleAttribute(glass.getElement(), "opacity", "0.60");
			DOM.setStyleAttribute(glass.getElement(), "filter", " alpha(opacity=60)");
			DOM.setStyleAttribute(glass.getElement(), "position", "relative");
			DOM.setStyleAttribute(glass.getElement(), "overflow", "scroll");
		}
	}

	
	/**
	 * Adds a button with user defined style, which closes a popup and performs user defined action
	 * @param buttonTitle
	 * @param style
	 * @param action
	 */
	public void addCloseButton(String buttonTitle, int style , final Action action) {
		// A button to close the popup
		Widget button = (new StyledButton(buttonTitle, null, new ClickHandler(){

			public void onClick(ClickEvent event) {
				PopupContainerBase.this.hide();
				if (action != null) {
					action.doAction();
				}
				
			}}, style)).getButton();
//		buttonPanel.add(closeButton);
		buttonPanel.add(button);
	}
	
	/**
	 * Adds a button with user defined style, which closes a popup
	 * @param buttonTitle
	 * @param style
	 */
	public void addCloseButton(String buttonTitle, int style) {
		addCloseButton(buttonTitle, style, null);
	}
	
	/**
	 * Adds a button with default style, which closes a popup
	 * @param buttonTitle
	 */
	public void addCloseButton(String buttonTitle) {
		addCloseButton(buttonTitle, StyledButton.DEFAULT_STYLE);
	}

	/**
	 * Adds a button with default style, which closes a popup and performs user defined action
	 * @param buttonTitle
	 * @param action
	 */
	public void addCloseButton(String buttonTitle,  final Action action) {
		addCloseButton(buttonTitle, StyledButton.DEFAULT_STYLE, action);
	}
	
	
	public static void addStyle (Widget widget, int style) {
		switch (style) {
		case HEADER_TEXT:
			DOM.setStyleAttribute(widget.getElement(), "fontWeight", "bold");
			DOM.setStyleAttribute(widget.getElement(), "fontSize", "110%");

			DOM.setStyleAttribute(widget.getElement(), "textAlign", "center");
			break;
		case HEADER_1_TEXT:
			DOM.setStyleAttribute(widget.getElement(), "fontSize", "90%");
			DOM.setStyleAttribute(widget.getElement(), "fontWeight", "bold");
			DOM.setStyleAttribute(widget.getElement(), "margin", "0 3px 3px 3px");
			DOM.setStyleAttribute(widget.getElement(), "paddingBottom", "3px");
			DOM.setStyleAttribute(widget.getElement(), "paddingTop", "8px");
			break;		
		case HEADER_2_TEXT:
				
			break;
		case NORMAL:

			DOM.setStyleAttribute(widget.getElement(), "fontSize", "80%");
			DOM.setStyleAttribute(widget.getElement(), "padding", "15px");
			break;
		case SMALL:
			
			break;
		case ERROR_MESSAGE_MAIN_TEXT:
			DOM.setStyleAttribute(widget.getElement(), "fontSize", "125%");
			DOM.setStyleAttribute(widget.getElement(), "padding", "13px 13px 26px 13px ");
			DOM.setStyleAttribute(widget.getElement(), "textAlign", "center");
			break;
		case ERROR_MESSAGE_SERVER_LABEL:
			DOM.setStyleAttribute(widget.getElement(), "fontSize", "77%");
			DOM.setStyleAttribute(widget.getElement(), "fontWeight", "bold");
			
			break;
		case ERROR_MESSAGE_SERVER_TEXT:
			DOM.setStyleAttribute(widget.getElement(), "fontSize", "80%");
			DOM.setStyleAttribute(widget.getElement(), "fontStyle", "italic");
			DOM.setStyleAttribute(widget.getElement(), "padding", "4px 10px 10px 10px ");
			DOM.setStyleAttribute(widget.getElement(), "textAlign", "center");
			
			break;
		case ERROR_MESSAGE_ACTION_TEXT:
			DOM.setStyleAttribute(widget.getElement(), "fontSize", "125%");
			DOM.setStyleAttribute(widget.getElement(), "padding", "13px 13px 26px 13px ");
			DOM.setStyleAttribute(widget.getElement(), "textAlign", "center");
			
			break;
		case HEADER_DESCRIPTION:
			DOM.setStyleAttribute(widget.getElement(), "fontSize", "90%");
			DOM.setStyleAttribute(widget.getElement(), "padding", "3px 0px 8px 12px ");
			DOM.setStyleAttribute(widget.getElement(), "textAlign", "left");
			DOM.setStyleAttribute(widget.getElement(), "color", "#17396A");
			
			break;
		}
	}
	
	/**
	 * Adds a button widget to the button panel
	 * @param w button
	 */
	public void addButton(Widget w) {
		this.buttonPanel.add(w);
	}
	
	/**
	 * Creates {@link #StyledButton} object (with default style {@link #StyledButton.DEFAULT_STYLE}) and adds it to the button panel
	 * 
	 * @param buttonText text
	 * @param action action to be performed when the button is clicked
	 */
	public void addButton(String buttonText, final Action action) {
		addButton((new StyledButton(buttonText, null, new ClickHandler() {

			public void onClick(ClickEvent event) {
				action.doAction();
			}})).getButton());
	}
	
	/**
	 * Creates {@link #StyledButton} object (with default style {@link #StyledButton.DEFAULT_STYLE}) and adds it to the button panel
	 * 
	 * @param buttonText text
	 * @param action action to be performed when the button is clicked
	 */
	public void addButton(String buttonText, ClickHandler handler) {
		addButton((new StyledButton(buttonText, null, handler)).getButton());
	}
	
	/**
	 * Creates {@link #StyledButton} object (with required style) and adds it to the button panel
	 * 
	 * @param buttonText text
	 * @param action action to be performed when the button is clicked
	 */
	public void addButton(String buttonText, ClickHandler handler, int style) {
		addButton((new StyledButton(buttonText, null, handler, style)).getButton());
	}
}
