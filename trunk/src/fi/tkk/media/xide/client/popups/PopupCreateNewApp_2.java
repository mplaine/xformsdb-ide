package fi.tkk.media.xide.client.popups;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Tabs.ExplorerDetailsTab;
import fi.tkk.media.xide.client.Tabs.ExplorerTreeTab;
import fi.tkk.media.xide.client.UI.Widget.TextBoxWithRequestString;
import fi.tkk.media.xide.client.popups.utils.PopupBase;

public class PopupCreateNewApp_2 extends PopupBase{
	protected TextBoxWithRequestString textBoxSearchString;
	ExplorerDetailsTab tab;
	
	public PopupCreateNewApp_2() {
		super("Welcome to New Application Wizard", " 	Please select the application that you want to copy."  	);
//		setSize("80%", "80%");
		
		VerticalPanel vp = new VerticalPanel();
		vp.setWidth("100%");
//		vp.setSize("100%", "100%");
		
		Label label = new Label(" 	When you select appropriate application please click \"Next\" button.");
		DOM.setStyleAttribute(label.getElement(), "fontSize", "80%");
		DOM.setStyleAttribute(label.getElement(), "padding", "3px");
		vp.add(label);
		
		// Search text box
		HorizontalPanel hp = new HorizontalPanel();
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		textBoxSearchString = new TextBoxWithRequestString("Search here");
	
		hp.add(textBoxSearchString);

		// 2. Search button
		Button buttonSearch = new Button("Search");
		buttonSearch.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				// TODO: Send request to server
				if (tab != null) {
					tab.addFakeData();
					
				}
			}
		});
//		buttonSearch.setStyleName("design-button-right");
		hp.add(buttonSearch);
		vp.add(hp);

////		TabPanel leftTab = new TabPanel();
////		leftTab.setHeight("100%");
////		
//		tab = new ExplorerDetailsTab();
////		DOM.setStyleAttribute(tab.getElement(), "overflow", "scroll");
//////		tab.addFakeData();
//		
//		leftTab.add(tab, "List of applications");
//		leftTab.getTabBar().selectTab(0);
//		vp.add(leftTab);
		tab = new ExplorerDetailsTab();
//		tab.setHeight("100%");
		tab.addFakeData();
		vp.add(tab);
		
		popup.addContent(vp);
		
		//Buttons
//		addPreviousButton();
		
		popup.addCloseButton("Next");
		
		popup.addCloseButton("Cancel");
		
		
	}
}
