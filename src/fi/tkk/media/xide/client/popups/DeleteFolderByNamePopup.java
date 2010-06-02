package fi.tkk.media.xide.client.popups;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.popups.basic.Popup;
import fi.tkk.media.xide.client.popups.basic.PopupError;
import fi.tkk.media.xide.client.popups.utils.PopupBase;
import fi.tkk.media.xide.client.popups.utils.PopupContainerBase;
import fi.tkk.media.xide.client.popups.utils.PopupContainer;
import fi.tkk.media.xide.client.utils.ConnectionToServer;
import fi.tkk.media.xide.client.utils.ConnectionToServer.CallbackActions;

public class DeleteFolderByNamePopup extends PopupBase {
	PopupContainer popup;
	TextBox textbox;
	public DeleteFolderByNamePopup() {
		super("Delete folder",
				"Please enter the path to the folder you want to delete and press OK button");

		textbox = new TextBox();
		textbox.setWidth("100%");
		popup.addContent(textbox);

		popup.addButton("OK", new ClickHandler() {
			public void onClick(ClickEvent event) {
				ConnectionToServer.makeACall(new CallbackActions() {

					public void execute(AsyncCallback callback) {
						ConnectionToServer.searchService.deleteFile(textbox
								.getValue(), callback);
					}

					public void onFailure(Throwable caught) {
						new PopupError("Unfortunately folder was not deleted!",
								caught.getMessage());

					}

					public void onSuccess(Object result) {
						// No page has found
						new Popup("File is deleted! ");
					}
				});
			}
		});
		popup.addCloseButton("Cancel");

		popup.showPopup();
	}
}
