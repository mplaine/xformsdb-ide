package fi.tkk.media.xide.client.popups.basic;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.UI.Widget.StyledButton;
import fi.tkk.media.xide.client.popups.utils.PopupContainerBase;
import fi.tkk.media.xide.client.popups.utils.PopupErrorContainer;
import fi.tkk.media.xide.client.utils.Cons;
import fi.tkk.media.xide.client.utils.GoodHorizontalAdjustablePanel;
import fi.tkk.media.xide.client.utils.PagePanel;

/**
 * Is called when an error occurs and it should be reported to the user
 * @author Evgenia Samochadina
 * @date Jan 27, 2010
 *
 */
public class PopupError {
	public PopupError(String message, String serverMessage) {
		PopupErrorContainer popup = PopupErrorContainer.getPopup();
		
		if (serverMessage != null) {
			VerticalPanel vp = new VerticalPanel();
//			vp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			
			Label errorMessage = new Label(message);
			PopupContainerBase.addStyle(errorMessage, PopupContainerBase.ERROR_MESSAGE_MAIN_TEXT);
			
			Label errorServerLabel = new Label(Cons.ERROR_POPUP_SERVER_MESSAGE_LABEL);
			PopupContainerBase.addStyle(errorServerLabel, PopupContainerBase.ERROR_MESSAGE_SERVER_LABEL);
			
			Label errorServerMessage = new Label(serverMessage);
			PopupContainerBase.addStyle(errorServerMessage, PopupContainerBase.ERROR_MESSAGE_SERVER_TEXT);
			
			vp.add(errorMessage);
			vp.add(errorServerLabel);
			vp.add(errorServerMessage);
			
			popup.addContent(vp);
		}
		else {
			popup.addContent(new Label(message));
		}
		popup.addCloseButton("Close");
		popup.showPopup();
	}

}
