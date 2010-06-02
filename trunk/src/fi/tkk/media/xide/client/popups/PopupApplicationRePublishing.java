package fi.tkk.media.xide.client.popups;

import java.util.Iterator;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.Data.APElement;
import fi.tkk.media.xide.client.Data.APElementApplication;
import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.UI.Widget.StyledButton;
import fi.tkk.media.xide.client.popups.basic.LoadingPopup;
import fi.tkk.media.xide.client.popups.basic.PopupError;
import fi.tkk.media.xide.client.popups.utils.CheckBoxWrapper;
import fi.tkk.media.xide.client.popups.utils.FormPopup;
import fi.tkk.media.xide.client.popups.utils.PopupBase;
import fi.tkk.media.xide.client.popups.utils.PopupContainer;
import fi.tkk.media.xide.client.popups.utils.interfaces.AsyncEventListener;
import fi.tkk.media.xide.client.utils.ConnectionToServer;
import fi.tkk.media.xide.client.utils.ConnectionToServer.CallbackActions;

public class PopupApplicationRePublishing extends FormPopup  implements AsyncEventListener{
	boolean					publish;
	APElementApplication	application;

	public PopupApplicationRePublishing(APElementApplication element) {
		super("Republish application", "You are going to republish " + element.getProperties().get(Property.TITLE).getStringValue()
						+ " application. All application related files and folders on the server will be updated and you will be able to access the application online." +
								" If previous version of the application had some information in the database, you can decide not to reinitialize the database. " +
								"In this case the application will be able to use this information." +
								"Please choose whether you want to reinitialize the database and click OK button ", 70);

		application = element;

		publish = true;

		String serverURL = Main.getInstance().serverURL;
		String refURL =
				application.properties.get(Property.AUTHOR).getStringValue() + "/"
						+ application.getProperties().get(Property.RELATED_URL).getStringValue();
		
		
		// Mark as main page checkbox
		CheckBox cb = new CheckBox();
		cb.setValue(true);
		CheckBoxWrapper cwr =new CheckBoxWrapper(cb, false, this);
		addUIElement("Yes, I would like to reinitialize application database", "Database will be cleared and initialized with init data, but all previous changes will be lost.", cwr);

		
		popup.showPopup();
		// connectionToServer = new ConnectionToServer();

	}

	public void onSuccessfullyFilled() { 

		ConnectionToServer.makeACall(new CallbackActions() {

			public void execute(AsyncCallback callback) {

				ConnectionToServer.searchService.republishApplication(application, getCBValue(0),  callback);
				LoadingPopup.showDimmed();
			}

			public void onFailure(Throwable caught) {

				popup.hide();
				LoadingPopup.hideDimmed();
				new PopupError("Unfortunately application was not republished!",  caught.getMessage());
			}

			public void onSuccess(Object result) {

				
				popup.hide();
				LoadingPopup.hideDimmed();
				
				application.onPublished(result);
				// Show popup, informing that the application is now published
				new PopupApplicationSuccessfullyPublished(application.properties.get(Property.URL).getStringValue());
			}
		});
	}
}
