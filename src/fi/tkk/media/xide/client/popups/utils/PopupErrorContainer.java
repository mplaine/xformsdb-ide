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
import fi.tkk.media.xide.client.utils.Cons;
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

public class PopupErrorContainer extends PopupContainer{

		protected PopupErrorContainer() {
			super();
			this.addStyleDependentName("Error");
			setText(Cons.ERROR_POPUP_HEADER);
		}
		
		protected static PopupErrorContainer singleton = null;
		
		/**
		 * Gets an instance of the popup. 
		 * @return
		 */
		public static PopupErrorContainer getPopup() {
			if (singleton == null) {
				singleton = new PopupErrorContainer();
			}
			singleton.clearPopup();
			return singleton;
		}
}
