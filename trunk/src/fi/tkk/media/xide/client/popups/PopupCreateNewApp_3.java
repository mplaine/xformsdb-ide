package fi.tkk.media.xide.client.popups;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.UI.Widget.TextBoxWithRequestString;
import fi.tkk.media.xide.client.popups.utils.PopupBase;

public class PopupCreateNewApp_3 extends PopupBase{
	public PopupCreateNewApp_3() {
		super("Welcome to New Application Wizard", "Please enter the URL of the application you want to copy");
//		setSize("80%", "80%");
		
		// Search text box
		HorizontalPanel hp = new HorizontalPanel();
		hp.setWidth("100%");
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		TextBoxWithRequestString textBoxSearchString = new TextBoxWithRequestString("Enter URL here");
		textBoxSearchString.setWidth("100%");
	
		hp.add(textBoxSearchString);

		// 2. Search button
		Button buttonSearch = new Button("Check");
		buttonSearch.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				// Send request to server
//				SearchTemplatesOnServer();
			}
		});
//		buttonSearch.setStyleName("design-button-right");
		hp.add(buttonSearch);
		
		popup.addContent(hp);
		
		// Buttons
//		addPreviousButton();
		
		popup.addCloseButton("Next");
		
		popup.addCloseButton("Cancel");
	}
}
