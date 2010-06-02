package fi.tkk.media.xide.client.Server.RPC;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.event.KeyboardHandler;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.UI.Widget.StyledButton;
import fi.tkk.media.xide.client.popups.PopupEnterUsername;
import fi.tkk.media.xide.client.popups.PopupSaveAsNewComponent;
import fi.tkk.media.xide.client.popups.basic.LoadingPopup;
import fi.tkk.media.xide.client.popups.garbage.GoodPopupNotScrollablePanel;
import fi.tkk.media.xide.client.popups.utils.PopupContainerBase;
import fi.tkk.media.xide.client.popups.utils.PopupContainer;
import fi.tkk.media.xide.client.popups.utils.PopupErrorContainer;
import fi.tkk.media.xide.client.utils.ConnectionToServer;
import fi.tkk.media.xide.client.utils.ConnectionToServer.CallbackActions;
import fi.tkk.media.xide.server.Config;
import fi.tkk.media.xide.server.SearchServiceImpl;
//import com.nescomputers.techshop.client.TechShopEntryPoint;
//import com.nescomputers.techshop.client.services.CustomerServiceAsync;
//import com.nescomputers.techshop.objects.TechShopUser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Forms a login popup, which is showed for the user any time when he/she requests the information from server 
 * and is not logged into the system yet. 
 * 
 * Also maintains the queue of tasks, which are unfinished because of Authorization Exception. When user successfully logged in, 
 * runs the tasks from the queue. 
 * 
 */
public class LoginDialog implements KeyPressHandler {

	/**
	 * Hardcoded user credentials
	 */
	
	private final TextBox username = new TextBox();
	private ArrayList commandQueue = new ArrayList();
	
	static LoginDialog singleton;
	Label passwordMsg;
	PopupContainer popup;
	Label emailMsg;
	PasswordTextBox passwordTB;

	  
	public static LoginDialog getLoginDialog(String message,
			ApplicationCallback callback) {
//
//		if (singleton == null) {
//			singleton = new LoginDialog();
//		}
//		System.out.println("quieue some command");
//		singleton.queueCommand(callback);
//		singleton.init();
//		singleton.setModal(true);
//		singleton.center();
////		singleton.
		return getLoginDialog(message, callback, true);
	}
	
	public static LoginDialog getLoginDialog(String message,
			ApplicationCallback callback, boolean queCommand) {

		if (singleton == null) {
			singleton = new LoginDialog();
		}

		if(queCommand) {
//			System.out.println("quieue some command");
			singleton.queueCommand(callback);
		}
		singleton.init();
		return singleton;
	}


	
	private void queueCommand(ApplicationCallback callback) {
		commandQueue.add(callback);
	}

	private void runCommandQueue() {
		for (Iterator it = commandQueue.iterator(); it.hasNext();) {
//			System.out.println("run command N");
			ApplicationCallback command = (ApplicationCallback) it.next();
			command.execute();
			it.remove();
		}
	}
	private void init() {
		popup = PopupContainer.getPopup();
		
		Label techShopAccountLbl = new HTML("You can login using your <a href=\"https://kassi.sizl.org/\">Kassi</a> " + 
				"or <a href=\"https://ossi.sizl.org/\">Ossi</a> account.");
		Label emailLbl = new Label("Username:");
		username.setName("username");
		
		// Hardcoded value!
		username.setFocus(true);
//		username.setValue(usernameValue);
	
		emailMsg = new Label();
		emailMsg.addStyleName("errorText");
		Label passwordLbl = new Label("Password:");
		passwordTB = new PasswordTextBox();
		passwordTB.addKeyPressHandler(this);
		passwordTB.setName("password");
		passwordTB.setStyleName("gwt-TextBox");
		passwordTB.addKeyPressHandler(this);
		// Hardcoded value!
//		passwordTB.setValue(passwordValue);
		
		passwordMsg = new Label();
		passwordMsg.getElement().setAttribute("style", "max-width: 340px; color: red;");
		passwordMsg.addStyleName("errorText");
//		CheckBox rememberMeCB = new CheckBox("Remember me on this computer.");
		
		Label cannotAccessAccountLbl = new Label("I cannot access my account");
		cannotAccessAccountLbl.addStyleName("link");
		FlexTable loginFormGrid = new FlexTable();
		DOM.setStyleAttribute(loginFormGrid.getElement(), "paddingTop", "20px");
		loginFormGrid.setWidget(0, 0, emailLbl);
		loginFormGrid.setWidget(0, 1, username);
		loginFormGrid.setWidget(1, 1, emailMsg);
		loginFormGrid.setWidget(2, 0, passwordLbl);
		loginFormGrid.setWidget(2, 1, passwordTB);
		loginFormGrid.setWidget(3, 0, passwordMsg);
		loginFormGrid.getFlexCellFormatter().setColSpan(3,0,3);
		
		Label label = new Label("Forgot your password?");
			DOM.setElementProperty(label.getElement(), "id", "label-like-link");

		label.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				new PopupEnterUsername();
			}
		});

		loginFormGrid.setWidget(4, 0, label);
		label.setStyleName("header-right-element");
		
//		loginFormGrid.setWidget(4, 1, rememberMeCB);
		VerticalPanel loginVP = new VerticalPanel();
		DOM.setStyleAttribute(loginVP.getElement(), "paddingTop", "10px");
		loginVP.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
//		loginVP.add(signInWithLbl);
		loginVP.add(techShopAccountLbl);
		loginVP.add(loginFormGrid);
//		loginVP.add(signInBtn);
//		loginVP.add(cannotAccessAccountLbl);
		Label header = new Label("To enter into XIDE you mist log in");
		PopupContainerBase.addStyle(header, popup.HEADER_1_TEXT);
		popup.addHeader(header);
		popup.addContent(loginVP);
		

		username.setFocus(true);
		
		Widget loginButton = new StyledButton("Log in", null,  new ClickHandler(){
			public void onClick(ClickEvent event) {
				onLoginButtonPressed();
			}

		}, StyledButton.STYLE_GREY).getButton();
		popup.addButton(loginButton);
		popup.addCloseButton("Cancel");
		popup.showPopup();
		username.setFocus(true);
	}
	
	private void onLoginButtonPressed() {
		boolean valid = true;

		if (username.getText().trim().length() == 0) {
			emailMsg.setText("Required field cannot be left blank");
			valid = false;
		} else {
			emailMsg.setText("");
		}

		if (passwordTB.getText().trim().length() == 0) {
			passwordMsg.setText("Required field cannot be left blank");
			valid = false;
		} else {
			passwordMsg.setText("");
		}
		if (!valid)
			return;

		// Both a username and password were supplied, so let's try
		// to authenticate.
		final String msg = "Username and password do not match. Please try again! ";
		ConnectionToServer.makeALoginCall(new CallbackActions() {

			public void execute(AsyncCallback callback) {
				ConnectionToServer.searchService.login(username.getText().trim(),
						passwordTB.getText()
						.trim(), callback);
				LoadingPopup.showDimmed();
			}

			public void onFailure(Throwable caught) {
				passwordMsg.setText(msg);
//				Main.getInstance().username = "";
				LoadingPopup.hideDimmed();
			}

			public void onSuccess(Object result) {
				String res = (String) result;
				if (res == "") {
					passwordMsg.setText(msg);
					LoadingPopup.hideDimmed();
				} else {
					LoadingPopup.hideDimmed();
					popup.hide();
//					Main.getInstance().username = res;
//					System.out.println("run commandqueue");
					runCommandQueue();
				}
			}});
	}
	
	public void onKeyPress(KeyPressEvent event) {
		if (event.getCharCode() == KeyboardHandler.KEY_ENTER) {
			onLoginButtonPressed();
		}
	}
}