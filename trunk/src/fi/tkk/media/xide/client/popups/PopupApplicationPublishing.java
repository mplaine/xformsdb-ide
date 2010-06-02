package fi.tkk.media.xide.client.popups;

import java.util.HashMap;
import java.util.Iterator;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
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
import fi.tkk.media.xide.client.popups.utils.FormPopup;
import fi.tkk.media.xide.client.popups.utils.PopupBase;
import fi.tkk.media.xide.client.popups.utils.PopupContainer;
import fi.tkk.media.xide.client.popups.utils.interfaces.AsyncEventListener;
import fi.tkk.media.xide.client.utils.ConnectionToServer;
import fi.tkk.media.xide.client.utils.ConnectionToServer.CallbackActions;

public class PopupApplicationPublishing extends PopupBase{
	
	APElementApplication	application;
	
	public PopupApplicationPublishing(APElementApplication element) {
		super("Publish application", "You are going to publish " + element.getProperties().get(Property.TITLE).getStringValue()
				+ " application. You will be able to access the application online." +
						"Please verify the following information and click OK button ");

		application = element;


		String serverURL = Main.getInstance().serverURL;
		String refURL =
				application.properties.get(Property.AUTHOR).getStringValue() + "/"
						+ application.getProperties().get(Property.RELATED_URL).getStringValue();

		Grid grid = new Grid();
		grid.resize(1 + application.getChildren().size(), 2);
		
		Label applicationTitle = new Label("application URL");
		Label pagesTitle = new Label("pages( " + application.getChildren().size() + " ) URL");
		
		PopupContainer.addStyle(applicationTitle, PopupContainer.HEADER_DESCRIPTION);
		PopupContainer.addStyle(pagesTitle, PopupContainer.HEADER_DESCRIPTION);
		
		Label title = new Label(serverURL + refURL);

		// title.addStyleName("design-LabelMiddleText-bold");
		DOM.setStyleAttribute(title.getElement(), "fontWeight", "bold");
		DOM.setStyleAttribute(title.getElement(), "fontSize", "80%");
		DOM.setStyleAttribute(title.getElement(), "padding", "3px");
		DOM.setStyleAttribute(title.getElement(), "paddingBottom", "8px");

		grid.setWidget(0, 0, applicationTitle);
		grid.setWidget(0, 1, title);
		
		grid.setWidget(1, 0, pagesTitle);


		Label descr = null;
		int i = 1;
		for (Iterator<APElement> iterator = application.getChildren().iterator(); iterator.hasNext();) {
			String o = iterator.next().getProperties().get(Property.RELATED_URL).getStringValue();
			descr = new Label(serverURL + refURL + "/" + o);
			descr.addStyleName("design-LabelSmallText");
//			DOM.setStyleAttribute(descr.getElement(), "padding-left", "10px");
			DOM.setStyleAttribute(descr.getElement(), "padding", "3px");
			DOM.setStyleAttribute(descr.getElement(), "paddingBottom", "8px");

			grid.setWidget(i, 1, descr);
			i++;
		}

		popup.addContent(grid);

		// Buttons


		popup.addButton("OK", new ClickHandler() {

			public void onClick(ClickEvent event) {
				onSuccessfullyFilled();
				
			}});
		popup.addCloseButton("Cancel");
		popup.showPopup();


	}

	public void onSuccessfullyFilled() { 

		ConnectionToServer.makeACall(new CallbackActions() {

			public void execute(AsyncCallback callback) {

				ConnectionToServer.searchService.publishApplication(application, true, callback);
				LoadingPopup.showDimmed();
			}

			public void onFailure(Throwable caught) {

				popup.hide();
				LoadingPopup.hideDimmed();
				new PopupError("Unfortunately application was not published!",  caught.getMessage());
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
