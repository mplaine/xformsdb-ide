package fi.tkk.media.xide.client.popups.utils;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Server.RPC.ApplicationCallback;
import fi.tkk.media.xide.client.UI.Widget.StyledButton;
import fi.tkk.media.xide.client.popups.PopupSaveAsNewComponent;
import fi.tkk.media.xide.client.popups.garbage.GoodPopupNotScrollablePanel;
import fi.tkk.media.xide.client.utils.ConnectionToServer;
import fi.tkk.media.xide.client.utils.GoodHorizontalAdjustablePanel;
import fi.tkk.media.xide.client.utils.PagePanel;
import fi.tkk.media.xide.client.utils.ConnectionToServer.CallbackActions;
import fi.tkk.media.xide.server.Config;
import fi.tkk.media.xide.server.SearchServiceImpl;
//import com.nescomputers.techshop.client.TechShopEntryPoint;
//import com.nescomputers.techshop.client.services.CustomerServiceAsync;
//import com.nescomputers.techshop.objects.TechShopUser;
import java.util.ArrayList;
import java.util.Iterator;

public class PopupContainerWithScroll extends PopupContainerBase{

		
		HTMLPanel mainPanel;
		
		protected static PopupContainerWithScroll singleton = null;
		
		public static PopupContainerWithScroll getPopup() {
			if (singleton == null) {
				singleton = new PopupContainerWithScroll();
			}
			singleton.clearEverything();
			return singleton;
		}

		public  PopupContainerWithScroll showPopup() {
			center();
			return this;
		}

		protected PopupContainerWithScroll() {
			super();
			initDimmedPopup();
			init();
			this.setModal(true);
			// Do not allow direct instantiation.
		}

		public void center() {
			glass.show();
			super.center();
		}

		private void clearEverything() {
		    // Clear buttons
			singleton.buttonPanel.clear();
			if (header != null) {
				header.removeFromParent();
			}
			if (content != null) {
				content.removeFromParent();
			}
		}
		
		  @Override
		  public void hide() {
			glass.hide();
		    super.hide();
		    
		  }
		
		  /**
		   * Initializes the content of the popup
		   */
		public void init() {
			this.setStyleName("design-handmade-popup");
			this.addStyleDependentName("styled");
			
			// Style divs (form border)
			mainPanel = new HTMLPanel("<div id=\"design-handmade-popup-header\"><div id=\"design-handmade-popup-header-inner\"></div></div>" +
										"<div id=\"design-handmade-popup-content\"><div id=\"design-handmade-popup-content-inner\"><div id=\"design-handmade-popup-content-inner-wrapper\"> </div> </div></div>"+
									  "<div id=\"design-handmade-popup-footer\"><div id=\"design-handmade-popup-footer-inner\"></div></div>"); 
			DOM.setStyleAttribute(mainPanel.getElement(), "height", "100%");
			this.add(mainPanel);
			
			// Add srolling
			DOM.setStyleAttribute(DOM.getParent(mainPanel.getElement()), "position", "absolute");
			DOM.setStyleAttribute(DOM.getParent(mainPanel.getElement()), "width", "100%");
			DOM.setStyleAttribute(DOM.getParent(mainPanel.getElement()), "height", "100%");
			DOM.setStyleAttribute(DOM.getChild(this.getElement(), 0), "width", "100%");
			DOM.setStyleAttribute(DOM.getChild(DOM.getChild(this.getElement(), 0),0), "width", "100%");

			String div = 
				"<table cellspacing=\"0\" cellpadding=\"0\" style =\"width:100%\"> " +
					"<tr>" +
					  "<td>" +
					    "<div style=\"width:12; height: 29; background:transparent url('styleImages/header_tab_bg_start.png') no-repeat \"></div>" +
					  " </td>" +
					  "<td style=\"width: 100%;\">" +
					  	"<div style=\"font-size:130%; text-align:center; font-weight: bold; line-height:29px; height: 29;background:transparent url('styleImages/header_tab_bg_middle.png') repeat-x \"> XIDE message</div>" +
					  "</td>" +
					  "<td>" +
					  	"<div " + "id=\"close-sign\" style=\"width:12; height: 29; background:transparent url('styleImages/header_tab_bg_end.png') no-repeat\"></div>" +
					 " </td>" +
				  " </tr> " +
				"</table>";
			this.setHTML(div);
			
			footer = initButtonPanel();
			mainPanel.add(footer, "design-handmade-popup-footer-inner");
			
//			addHeader(new Label ("header"));
//			addCloseButton("Close this");
//			VerticalPanel vp = new VerticalPanel();
//			for(int i = 0; i < 50; i++) {
//				vp.add(new Label("content"));
//			}
//			addContent(vp);
		}
		
//		public static void  addHeader(String text) {
//			singleton.header.setText(text);
//			
//		}
		

		public void addHeader(Widget w) {
			if (header != null) {
				header.removeFromParent();
			}
			header = w;

			mainPanel.add(w, "design-handmade-popup-header-inner");
//			DOM.setStyleAttribute(DOM.getChild(w.getElement(), 0), "border", "2px solid rgb(169, 169, 169)");
//			this.setText("XIDE message");


//			String div = "<div style = \"width:100%; height: 20px; background:red;\"></div>";

//			p.add(panelBottomLinksWrapper);
//			this.setHTML(panelBottomLinksWrapper.getElement().getString());
		}
		
		public void addFooter(Widget w) {
			if (footer != null) {
				footer.removeFromParent();
			}
			footer = w;
			mainPanel.add(w, "design-handmade-popup-footer-inner");
//			DOM.setStyleAttribute(DOM.getChild(w.getElement(), 0), "border", "2px solid rgb(169, 169, 169)");
		}
		public void addContent(Widget w) {
			if (content != null) {
				content.removeFromParent();
			}
			content = w;
			DOM.setStyleAttribute(w.getElement(), "padding", "10px");

			mainPanel.add(w, "design-handmade-popup-content-inner-wrapper");
//			DOM.setStyleAttribute(DOM.getChild(w.getElement(), 0), "border", "2px solid rgb(169, 169, 169)");
		}
		
//		private void initButtonPanel() {
//			buttonPanel = new HorizontalPanel();
//			GoodHorizontalAdjustablePanel panel = new GoodHorizontalAdjustablePanel(new Label(""), "left", buttonPanel, "center");
//			
////			DOM.setStyleAttribute(panel.getElement(), "backgroundColor", "lightgray");
////			DOM.setStyleAttribute(panel.getElement(), "borderTop", "3px solid #A9A9A9");
//			
//			addFooter(panel);
//		}
		

		public void addMain(Widget w) {
			DOM.setStyleAttribute(w.getElement(), "padding", "10px");
//			super.add(w);
			addContent(w);
		}


}
