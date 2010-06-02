package fi.tkk.media.xide.client.UI.Widget;

import java.util.Iterator;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.VerticalSplitPanel;
import com.google.gwt.user.client.ui.Widget;

public class AdvancedTripleTabPanelView {

	// 3 main tab panels
	private TabPanel tabPanelMain;
	private TabPanel tabPanelRight;
	private TabPanel tabPanelBottom;

	// Fields controls full screen feature

	// Splitter positions
	private int verticalSplitPosition = 70;
	private int horizontalSplitPosition = 80;

	// Flags indicating whether the tab panel is in full screen mode or not
	private boolean isMainTabPanelFullScreen = true;
	private boolean isRightTabPanelFullScreen = true;
	private boolean isBottomTabPanelFullScreen = true;

	// Used to make Main Split Panel abstract (from vertical and horizontal)
	private Panel mainSplitPanel;


	public VerticalSplitPanel verticalSplitPanelMain;

	// Start: Can be reduced to super class 
	public TabPanel getTabMain() {
		return tabPanelMain;
	}

	public TabPanel getTabRight() {
		return tabPanelRight;
	}

	public TabPanel getTabBottom() {
		return tabPanelBottom;
	}

	public Panel getMainSplitPanel() {
		return mainSplitPanel;
	}
	
	private void SetMainPanelHeight() {
		int screenHeight = Window.getClientHeight();

		// Change main panel height to fit the screen size

		// Calculate main panel height according to it's position on a screen
		
		// *2 coefficient is added to manage page footer which has the same size as page title
		
		// TODO: Instead of "-1" add here browser-depending code to 
		// calculate percentage correctly
		// "-1" is added for Firefox as the result is too big

		int panelPersentage =(int) (((screenHeight - mainSplitPanel.getAbsoluteTop()*2 + 3)* 100.0)
				/screenHeight) ;
		// Set height
		mainSplitPanel.setHeight(panelPersentage + "%");
//		mainSplitPanel.setHeight("700px");
	}
	
	// End: Can be reduced to super class 

	public AdvancedTripleTabPanelView(int topSliderPos, int botSloderPos) {
		// Create main (upper) panel
		verticalSplitPosition = botSloderPos;
		horizontalSplitPosition = topSliderPos;
		verticalSplitPanelMain = new VerticalSplitPanel();
		mainSplitPanel = verticalSplitPanelMain;
	}

	public void CreateView() {

		// Set size
		verticalSplitPanelMain.setWidth("100%");
		SetMainPanelHeight();
//		verticalSplitPanelMain.setSize("95%", "90%");
		
		verticalSplitPanelMain.setSplitPosition(verticalSplitPosition + "%");

		// Add second split panel (child split panel)
		final HorizontalSplitPanel horizontalSplitPanelMain = new HorizontalSplitPanel();

		// TODO: make this panel rendered even if it is  small
		//horizontalSplitPanelMain.setSize("100%", "100%");
		
		horizontalSplitPanelMain.setSize("100%", "100%");
		horizontalSplitPanelMain
				.setStyleName("gwt-HorizontalSplitPanel .splitter");
		horizontalSplitPanelMain
				.setSplitPosition(horizontalSplitPosition + "%");

		verticalSplitPanelMain.setTopWidget(horizontalSplitPanelMain);

		// Main Tab Panel
		tabPanelMain = new TabPanel();
//		// test tab
//		VerticalPanel verticalPanel = new VerticalPanel();
//		verticalPanel.setSize("97%", "97%");
//		tabPanelMain.add(verticalPanel, "Design");
		horizontalSplitPanelMain.setLeftWidget(tabPanelMain);
		tabPanelMain.setSize("100%", "100%");
		
		tabPanelMain.getTabBar().addTabListener(new TabListener() {
			private int previousTab = 0;
			public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
				if (previousTab == tabIndex) {
					if (!isMainTabPanelFullScreen) {
						verticalSplitPanelMain.setSplitPosition("100%");
						horizontalSplitPanelMain.setSplitPosition("100%");
						isMainTabPanelFullScreen = true;
						isRightTabPanelFullScreen = false;
						isBottomTabPanelFullScreen = false;

					} else {
						verticalSplitPanelMain
								.setSplitPosition(verticalSplitPosition + "%");
						horizontalSplitPanelMain
								.setSplitPosition(horizontalSplitPosition + "%");
						isMainTabPanelFullScreen = false;
					}
				} else {
					previousTab = tabIndex;
				}

				// Window.alert("You clicked tab " + tabIndex);
			}

			public boolean onBeforeTabSelected(SourcesTabEvents sender,
					int tabIndex) {
				return true;
			}
		});
		horizontalSplitPanelMain.setLeftWidget(tabPanelMain);

		// Right TabPanel 
		tabPanelRight = new TabPanel();
//		// test tab
//		verticalPanel = new VerticalPanel();
//		tabPanelRight.add(verticalPanel, "Search");

		horizontalSplitPanelMain.setRightWidget(tabPanelRight);
		tabPanelRight.setSize("100%", "100%");

		tabPanelRight.getTabBar().addTabListener(new TabListener() {
			private int previousTab = 0;
			public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
				if (previousTab == tabIndex) {
					if (!isRightTabPanelFullScreen) {
						verticalSplitPanelMain.setSplitPosition("100%");
						horizontalSplitPanelMain.setSplitPosition("0%");
						isRightTabPanelFullScreen = true;
						isMainTabPanelFullScreen = false;
						isBottomTabPanelFullScreen = false;
					} else {
						verticalSplitPanelMain
								.setSplitPosition(verticalSplitPosition + "%");
						horizontalSplitPanelMain
								.setSplitPosition(horizontalSplitPosition + "%");
						isRightTabPanelFullScreen = false;
					}
				} else {
					previousTab = tabIndex;
				}
				// Window.alert("You clicked tab " + tabIndex);
			}

			public boolean onBeforeTabSelected(SourcesTabEvents sender,
					int tabIndex) {
				return true;
			}
		});

		horizontalSplitPanelMain.setRightWidget(tabPanelRight);
		// TabPanel_2

		tabPanelBottom = new TabPanel();
//		tabPanelBottom.setStyleName("gwt-TabPanelTop");
		
		
//		// test tab
//		verticalPanel = new VerticalPanel();
//		tabPanelBottom.add(verticalPanel, "Properties");

		verticalSplitPanelMain.setBottomWidget(tabPanelBottom);
		tabPanelBottom.setSize("100%", "100%");

		tabPanelBottom.getTabBar().addTabListener(new TabListener() {
			private int previousTab = 0;
			public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
				if (previousTab == tabIndex) {
					if (!isBottomTabPanelFullScreen) {
						verticalSplitPanelMain.setSplitPosition("0%");
						horizontalSplitPanelMain.setSplitPosition("0%");
						isBottomTabPanelFullScreen = true;
						isRightTabPanelFullScreen = false;
						isMainTabPanelFullScreen = false;
					} else {
						verticalSplitPanelMain
								.setSplitPosition(verticalSplitPosition + "%");
						horizontalSplitPanelMain
								.setSplitPosition(horizontalSplitPosition + "%");
						isBottomTabPanelFullScreen = false;
					}
				} else {
					previousTab = tabIndex;
				}
				// Window.alert("You clicked tab " + tabIndex);
			}

			public boolean onBeforeTabSelected(SourcesTabEvents sender,
					int tabIndex) {
				return true;
			}
		});
		verticalSplitPanelMain.setBottomWidget(tabPanelBottom);
//		verticalSplitPanelMain.setHeight("90%");

	}

}
