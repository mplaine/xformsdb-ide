package fi.tkk.media.xide.client.popups;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.popups.utils.PopupContainer;

public class PopupApplicationSuccessfullyPublished  {
	PopupContainer popup;
	
	public PopupApplicationSuccessfullyPublished(String url) {
		
		popup = PopupContainer.getPopup();
		
		VerticalPanel vp = new VerticalPanel();
//		l = new Label(element.getProperties().get(Property.URL).getStringValue());
		Label text = new HTML ("Application is now successfully published. You can find it at ");
		PopupContainer.addStyle(text, PopupContainer.HEADER_2_TEXT);
		vp.add(text);
		Label l = new HTML ("<a href=\" "+ url+ " \"> " + url+ "</a> ");
		l.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vp.add(l);
		
		popup.addContent(vp);
		
		popup.addCloseButton("Close");
		
		popup.showPopup();
	}
}
