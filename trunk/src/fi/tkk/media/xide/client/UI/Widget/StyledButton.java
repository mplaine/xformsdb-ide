/**
 * 
 */
package fi.tkk.media.xide.client.UI.Widget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * A HTML Widget, which looks like a styled button. It has
 * - Click Handler which handles the click
 * - Text and image(optional) 
 * - rounded corner
 * - background and border
 * - styles for hover and active events.
 *  
 * Currently 2 color styles are available:
 * - dark grey
 * - blue
 * 
 * Corresponding CSS is defined in the main CSS file
 * .(stylename)-left left part of the button (rounded corners)
 * .(stylename)-right right part of the button (rounded corners)
 * .(stylename)-middle middle part of the button
 * #(stylename)-id id of the main table (used for hover and active effect)
 *  
 * @author Evgenia Samochadina
 * @date Dec 15, 2009
 *
 */
public class StyledButton {
	// Index used to make different id for each button. 
	// Used to fix bug of HTMLPanel. When popup with button is opened when there already is a button on the screen,
	// it adds a content to the id in this button, not in the correct one.
	private static int i = 0;

	public static final int STYLE_GREY = 1;
	public static final String STYLE_GREY_NAME = "styled-button-grey";
	public static final int STYLE_BLUE = 2;
	public static final String STYLE_BLUE_NAME = "styled-button-blue";
	
	public static final int DEFAULT_STYLE = STYLE_GREY;
	HTMLPanel button;
	
	public HTMLPanel getButton() {
		return button; 
	}
	/**
	 * Creates a button with text, image, handler and style
	 * @param html
	 */
	public StyledButton(String buttonText,String imageSrc, ClickHandler handler, int style ) {
		i++;
		button = new HTMLPanel (constructStyleTable(style));
		
		button.add(constructLabel(buttonText, imageSrc, handler), "button-label-id-" + i);
		
	}
	
	/**
	 * Creates a button with text, image, handler and default style (is defined by {@link #DEFAULT_STYLE})
	 * @param html
	 */
	public StyledButton(String buttonText, String imageSrc, ClickHandler handler) {
		i++;
		button = new HTMLPanel (constructStyleTable(DEFAULT_STYLE));
		
		button.add(constructLabel(buttonText, imageSrc, handler), "button-label-id-" + i);
		
	}
	
	public HTML constructLabel(String text, String imageSrc, ClickHandler handler) {
		HTML l = null;
		if (imageSrc == null || imageSrc.equals("")) {
			 l = new HTML(text);
		}
		else{
			l = new HTML("<img src=\"" + imageSrc + "\" class=\"gwt-Image\"/>" + text);
		}
		if (handler!= null) {
			l.addClickHandler(handler);
		}
		l.setWordWrap(false);
		return l;
		
	}
	public String constructStyleTable(int style) {
		String styleName = null;
		if (style == STYLE_BLUE) {
			styleName = STYLE_BLUE_NAME;
		}
		else if (style == STYLE_GREY) {
			styleName = STYLE_GREY_NAME;
		}
		String result = 
		"<table id=\""+ styleName +"-id\" cellspacing=\"0\" cellpadding=\"0\" style =\"width:100%\"> " +
		   "<tr>" +
		     "<td>" +
		        "<div class = \""+ styleName +"-left\"></div>" +
		     "</td>" +
		     "<td style=\"width: 100%;\">" +
		     	"<div id=\"button-label-id-"+ i + "\"  class = \""+ styleName +"-middle\"></div>" +
		     "</td>" +
		     "<td>" +
		     	"<div id=\"close-sign\"  class = \""+ styleName +"-right\"></div>" +
		    " </td>" +
		  " </tr> " +
		"</table>";
		return result;
	}
	
	

}
