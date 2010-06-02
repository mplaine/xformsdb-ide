package fi.tkk.media.xide.client.popups;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Tabs.ExplorerTreeTab;
import fi.tkk.media.xide.client.Tabs.SearchApplicationTab;
import fi.tkk.media.xide.client.Tabs.SearchComponentTab;
import fi.tkk.media.xide.client.popups.utils.PopupBase;

public class PopupCreateNewApp_1 extends PopupBase{
	public PopupCreateNewApp_1() {
		super("Welcome to New Application Wizard", "Please search for the application template that is suitable for your purpose." +  "\n" +  	 	   	
			  	"You can check the structure and detailed information about seleted template."  	);
//		setSize("80%", "80%");
	
		HorizontalSplitPanel mainPanel = new HorizontalSplitPanel();
//		mainPanel.setHeight("70%");
		TabPanel leftTab = new TabPanel();
		leftTab.add(new ExplorerTreeTab(), "App structure");
		leftTab.getTabBar().selectTab(0);
		leftTab.add(new Label("Here should be a preview of the application"), "Preview");
		leftTab.add(new Label("Here should be a list of applications made based o n selected template"), "List of Applications");


		TabPanel rightTab = new TabPanel();
		rightTab.add(new SearchApplicationTab(), "Search");
		rightTab.getTabBar().selectTab(0);

		mainPanel.setLeftWidget(leftTab);
		mainPanel.setRightWidget(rightTab);
		
		popup.addContent(mainPanel);
		
//		addPreviousButton();
		
		popup.addCloseButton("Next");
		
		popup.addCloseButton("Cancel");
		
	
	}
}
