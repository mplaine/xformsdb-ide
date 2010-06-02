package fi.tkk.media.xide.client.popups.garbage;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.utils.GoodHorizontalAdjustablePanel;
import fi.tkk.media.xide.client.utils.PagePanel;

/**
 * Good PopUp panel is a panel which has a style of popUp. 
 * Was designed instead of GWT PopUp because of scroll bars appeared incorrectly. 
 * 
 * Contains Title, second title (smaller one), main content area and button block on the top
 * 
 * @author evgeniasamochadina
 *DeckPanel
 */
public class GoodPopupScrollablePanel extends DialogBox{
	HTMLPanel mainPanel;
	Label title;
	Label text;
	HorizontalPanel buttonPanel;

		  
	/*
	 * Creates empty popup panel without any title, content or buttons
	 */
	public GoodPopupScrollablePanel() {
		super();
		// Add XIDE popup style
		this.setStyleName("design-handmade-popup");
		this.addStyleDependentName("styled");
		
		mainPanel = new HTMLPanel("<div id=\"design-handmade-popup-header\"></div>" +
									"<div id=\"design-handmade-popup-content\"> </div>"+
								  "<div id=\"design-handmade-popup-footer\"></div>"); 
//		mainPanel = new HTMLPanel("<div id=\"design-handmade-popup-header\"></div>" + 
//				"<div id=\"design-handmade-popup-content\"> </div>"+
//			  "<div id=\"design-handmade-popup-footer\"></div>"); 
		DOM.setStyleAttribute(mainPanel.getElement(), "height", "100%");
		this.add(mainPanel);
		
		DOM.setStyleAttribute(DOM.getParent(mainPanel.getElement()), "position", "absolute");
		DOM.setStyleAttribute(DOM.getParent(mainPanel.getElement()), "width", "100%");
		DOM.setStyleAttribute(DOM.getParent(mainPanel.getElement()), "height", "100%");
		
		DOM.setStyleAttribute(DOM.getChild(this.getElement(), 0), "width", "100%");
		DOM.setStyleAttribute(DOM.getChild(DOM.getChild(this.getElement(), 0),0), "width", "100%");
		// Add styles
//		DOM.setStyleAttribute(this.getElement(), "backgroundColor", "#fff");
//		DOM.setStyleAttribute(this.getElement(), "maxWidth", "40%");
	}

	public void addHeader(Widget w) {
		mainPanel.add(w, "design-handmade-popup-header");
		DOM.setStyleAttribute(DOM.getChild(w.getElement(), 0), "border", "2px solid rgb(169, 169, 169)");
//		this.setText("XIDE message");
		HTMLPanel panelBottomLinksWrapper = new HTMLPanel(
				"<div class=\"header-bottom-panel-style-wrapper\" style = \"width:100%\">"
						+ "<div id=\"header-bottom-panel-style-id-2\" class=\"header-bottom-panel-style\" >"
						+ "</div>" + "</div>");

		AbsolutePanel panelBottomLinks = new AbsolutePanel();
		panelBottomLinks.setStyleName("design-new-header-temp-links");

		panelBottomLinksWrapper.add(panelBottomLinks,
				"header-bottom-panel-style-id-2");
		
		panelBottomLinks.add(new Label ("XIDE message"));

//		String div = "<div style = \"width:100%; height: 20px; background:red;\"></div>";
		String div = "<table cellspacing=\"0\" cellpadding=\"0\" style =\"width:100%\"> " +
   "<tr>" +
     "<td>" +
       "<div style=\"width:15; height: 29; background:transparent url(http://localhost:8888/fi.tkk.media.xide.Main/styleImages/header_tab_bg_start.png) no-repeat \"></div>" +
    " </td>" +
     "<td style=\"width: 100%;\">" +
"<div style=\"line-height:29px; height: 29;background:transparent url(http://localhost:8888/fi.tkk.media.xide.Main/styleImages/header_tab_bg_middle.png) repeat-x \"> XIDE message</div>" +
     "</td>" +
     "<td>" +
"<div style=\"width:12; height: 29; background:transparent url(http://localhost:8888/fi.tkk.media.xide.Main/styleImages/header_tab_bg_end.png) no-repeat\"></div>" +
    " </td>" +
  " </tr> " +
"</table>";
		this.setHTML(div);
//		this.setHTML(panelBottomLinksWrapper.getElement().getString());

		
	}
	public void addFooter(Widget w) {
		mainPanel.add(w, "design-handmade-popup-footer");
		DOM.setStyleAttribute(DOM.getChild(w.getElement(), 0), "border", "2px solid rgb(169, 169, 169)");
	}
	public void addContent(Widget w) {
		mainPanel.add(w, "design-handmade-popup-content");
//		DOM.setStyleAttribute(DOM.getChild(w.getElement(), 0), "border", "2px solid rgb(169, 169, 169)");
	}
	/**
	 * Creates popup with title
	 * @param titleString title
	 */
	public GoodPopupScrollablePanel(String titleString) {
		this();
		if (titleString != null) {
			// The title element
			title = new Label(titleString);
			title.setWidth("100%");
	//		title.setStyleName("design-handmade-popup-title");
	
//			DOM.setStyleAttribute(title.getElement(), "backgroundColor", "lightgray");
//			DOM.setStyleAttribute(title.getElement(), "backgroundColor", "#E0E6EA");
//			DOM.setStyleAttribute(title.getElement(), "borderBottom", "3px solid #A9A9A9");
			DOM.setStyleAttribute(title.getElement(), "fontWeight", "bold");
			DOM.setStyleAttribute(title.getElement(), "fontSize", "90%");
			DOM.setStyleAttribute(title.getElement(), "padding", "5px");
			DOM.setStyleAttribute(title.getElement(), "textAlign", "center");
	
			addHeader(title);
		}
		
	}

	
	public GoodPopupScrollablePanel(String titleString, String smallTitleString) {
		this();
		VerticalPanel vp = new VerticalPanel();
		vp.setWidth("100%");
		
		DOM.setStyleAttribute(vp.getElement(), "backgroundColor", "#E0E6EA");
//		DOM.setStyleAttribute(vp.getElement(), "borderBottom", "3px solid #A9A9A9");
		DOM.setStyleAttribute(vp.getElement(), "padding", "5px");
		
		if (titleString != null) {
			// The title element
			title = new Label(titleString);

			addStyle(title, HEADER_TEXT);
			
			vp.add(title);
		}
		
		if (smallTitleString != null) {
			// The small title element
			Label smallTitle = new Label(smallTitleString);
			addStyle(smallTitle, HEADER_1_TEXT);
			vp.add(smallTitle);
		}
		addHeader(vp);
		
	}
	
//	public GoodPopupScrollablePanel(String text, Widget widget) {
//		this();
////		setSize("90%", "90%");
//		buttonPanel = new HorizontalPanel();
//		GoodHorizontalAdjustablePanel panel = new GoodHorizontalAdjustablePanel(new Label(""), buttonPanel);
//		addHeader(new Label (text));
//		addFooter(widget);
////		mainPanel = new PagePanel(new Label (text), widget, this, "100%", "100%");
//		addCloseButton("close");
//		
//	}
	
	/**
	 * Creates information popup with title and message
	 * @param titleString title
	 * @param messageString message to display
	 */
	public GoodPopupScrollablePanel(String titleString, String smallTitleString, String messageString) {
		this(titleString, smallTitleString);
		
		// The main element
		text = new Label(messageString);
		addStyle(text, HEADER_1_TEXT);
		addContent(text);
		
	}
	
	public static final int HEADER_TEXT = 1;
	public static final int HEADER_1_TEXT = 2;
	public static final int HEADER_2_TEXT = 3;
	public static final int NORMAL = 4;
	public static final int SMALL = 5;
	
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
			DOM.setStyleAttribute(widget.getElement(), "margin", "3px");
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
		}
	}
	
	
	/**
	 * Changes main text value (if there is a main text object)
	 * @param text
	 */
	public void changeText(String textString) {
//		Panel p = (Panel)title.getParent();
//		title.removeFromParent();
		if (text != null) {
			text.setText(textString);
		}
//		DOM.setStyleAttribute(title.getElement(), "fontWeight", "bold");
//		DOM.setStyleAttribute(title.getElement(), "fontSize", "110%");
//
//		DOM.setStyleAttribute(title.getElement(), "textAlign", "center");
//		p.add(title);
	}
	 
	
	private void initButtonPanel() {
		buttonPanel = new HorizontalPanel();
		GoodHorizontalAdjustablePanel panel = new GoodHorizontalAdjustablePanel(new Label(""), "left", buttonPanel, "center");
		
//		DOM.setStyleAttribute(panel.getElement(), "backgroundColor", "lightgray");
		DOM.setStyleAttribute(panel.getElement(), "borderTop", "3px solid #A9A9A9");
		
		addFooter(panel);
	}
	
	/**
	 * Add styles to a button and adds it to the button panel
	 * @param b
	 */
	public void addButton(Button b) {
		if (buttonPanel == null) {
			initButtonPanel();
		}
		DOM.setStyleAttribute(b.getElement(), "padding", "3px");
		DOM.setStyleAttribute(b.getElement(), "fontWeight", "bold");
		DOM.setStyleAttribute(b.getElement(), "minWidth", "80px");
		buttonPanel.add(b);
	}
	
	
	public void addMain(Widget w) {
		DOM.setStyleAttribute(w.getElement(), "padding", "10px");
//		super.add(w);
		addContent(w);
	}

	public void addCloseButton(String buttonTitle) {
		// A button to close the popup
		Button closeButton = new Button(buttonTitle, new ClickListener() {

			public void onClick(Widget sender) {
				GoodPopupScrollablePanel.this.hide();
			}
		});
//		buttonPanel.add(closeButton);
		addButton(closeButton);
	}
	
//	public GoodPopupScrollablePanel(String headerString, String messageString, Widget buttonClose) {
//		this(headerString, null, messageString);
//		add(buttonClose, 
//				getAbsoluteLeft() + getOffsetWidth()-buttonClose.getOffsetWidth() - 2, 
//				getAbsoluteTop() + 2);
//
//		DOM.setStyleAttribute(buttonClose.getElement(), "width", "80px");
//
//	}
	
//	/**
//	 * Sets new test to the message which popup is showing
//	 * @param newText
//	 */
//	public void setText(String newText) {
////		DOM.setStyleAttribute(this.getElement(), "clip", "rect(auto, auto, auto, auto)");
//		if (text != null) {
//			text.setText(newText);
//		}
////		DOM.setStyleAttribute(this.getElement(), "clip", "rect(auto, auto, auto, auto)");
//	}
	
//	public void addButton(Widget buttonClose, int left, int top) {
//		add(buttonClose, left, top);
//	}
	
	
	public void center() {
		int left = Window.getClientWidth()/2;
	    int top = Window.getClientHeight()/2;
	    RootPanel.get().add(this, left, top);
//	    if (mainPanel != null) {
//	    	mainPanel.setWidgetAndSize(this);
//	    }
	    
	    left = left - (this.getOffsetWidth()/2);
	    top = top - (this.getOffsetHeight()/2);
	    RootPanel.get().setWidgetPosition(this, left, top);
	}
	
	public void show() {
//		RootPanel.get().add(this, 0, 0);
    	RootPanel.get().add(this, 0, 0);
//	    if (mainPanel != null) {
//	    	mainPanel.setWidgetAndSize(this);
//	    }
	} 
	
//	public void show(int left, int top) {
//		RootPanel.get().add(this, left, top);
//	} 
	
	public void hide() {
		this.removeFromParent();
	}
}
