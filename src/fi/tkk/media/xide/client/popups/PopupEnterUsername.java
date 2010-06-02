package fi.tkk.media.xide.client.popups;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.popups.basic.Popup;
import fi.tkk.media.xide.client.popups.basic.PopupError;
import fi.tkk.media.xide.client.popups.utils.PopupBase;
import fi.tkk.media.xide.client.popups.utils.PopupContainer;
import fi.tkk.media.xide.client.utils.ConnectionToServer;
import fi.tkk.media.xide.client.utils.ConnectionToServer.CallbackActions;

/**
 * This is a simple popup, which is used to send a recover password email to the user. 
 * Is called when user clicks on corresponding lanel.
 * @author evgeniasamochadina
 *
 */
public class PopupEnterUsername extends PopupBase{
		
		public PopupEnterUsername() {
			super("Recover password", "Please enter the email you specified during the registration. A password recovery letter will be send to this email.");
			FlexTable panel = new FlexTable();
			
			Label emailLbl = new Label ("Email");
			final TextBox emailTB = new TextBox();
			
			panel.getFlexCellFormatter().setColSpan(0, 0, 2);
			
			panel.setWidget(0, 0, emailLbl);
			panel.setWidget(0, 1, emailTB);
			
			// Message
			final Label emailMsg = new Label("");
			emailMsg.getElement().setAttribute("style", "max-width: 480px; color: red;");
			emailLbl.setWordWrap(true);
			
			panel.setWidget(1, 0, emailMsg);
			
			panel.getFlexCellFormatter().setColSpan(1, 0, 2);
			popup.addContent(panel);
			
			popup.addButton("Send email", new ClickHandler() {

				public void onClick(ClickEvent event) {
					emailMsg.setText("");
					if (emailTB.getText() == null || emailTB.getText().equals("")) {
						// Inform the email cannot be blank
						emailMsg.setText("Email cannot be blank!");
					}
					else {
						ConnectionToServer.makeACall(new CallbackActions() {
	
							public void execute(AsyncCallback callback) {
								ConnectionToServer.searchService.recoverPassword(emailTB.getText(), callback);
							}
	
							public void onFailure(Throwable caught) {
								new PopupError("Unfortunately password recovery email was not sent!",  caught.getMessage());
							}
	
							public void onSuccess(Object result) {
								new Popup("Message has been sent!");
							}
						});			
					}
				}});
			popup.addCloseButton("Cancel");
			
			popup.showPopup();
		}
}
