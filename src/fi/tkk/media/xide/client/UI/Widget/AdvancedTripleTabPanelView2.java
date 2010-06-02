package fi.tkk.media.xide.client.UI.Widget;

import java.util.Iterator;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.VerticalSplitPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Tabs.Tab;

public class AdvancedTripleTabPanelView2 {

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
	
//	private void SetMainPanelHeight() {
//		int screenHeight = Window.getClientHeight();
//
//		// Change main panel height to fit the screen size
//
//		// Calculate main panel height according to it's position on a screen
//		
//		// *2 coefficient is added to manage page footer which has the same size as page title
//		
//		// TODO: Instead of "-1" add here browser-depending code to 
//		// calculate percentage correctly
//		// "-1" is added for Firefox as the result is too big
//
//		int panelPersentage =(int) (((screenHeight - mainSplitPanel.getAbsoluteTop()*2 + 3)* 100.0)
//				/screenHeight) ;
//		// Set height
//		mainSplitPanel.setHeight(panelPersentage + "%");
////		mainSplitPanel.setHeight("700px");
//	}
	
	// End: Can be reduced to super class 

	public AdvancedTripleTabPanelView2(int topSliderPos, int botSloderPos) {
		// Create main (upper) panel
		verticalSplitPosition = botSloderPos;
		horizontalSplitPosition = topSliderPos;
		verticalSplitPanelMain = new VerticalSplitPanel();
		DOM.setElementProperty(verticalSplitPanelMain.getElement(),"id", "design-new-pp-vertical-split-panel");
		DOM.setStyleAttribute(DOM.getChild(verticalSplitPanelMain.getElement(), 0), "marginTop", "10px");

		mainSplitPanel = verticalSplitPanelMain;
		CreateView();
	}

	public void CreateView() {

		// Set size
		verticalSplitPanelMain.setWidth("100%");
//		SetMainPanelHeight();
//		verticalSplitPanelMain.setSize("95%", "90%");
		
		verticalSplitPanelMain.setSplitPosition(verticalSplitPosition + "%");

		// Add second split panel (child split panel)
		final HorizontalSplitPanel horizontalSplitPanelMain = new HorizontalSplitPanel();

		// TODO: make this panel rendered even if it is  small
		//horizontalSplitPanelMain.setSize("100%", "100%");
		
		horizontalSplitPanelMain.setSize("100%", "100%");
		DOM.setElementProperty(horizontalSplitPanelMain.getElement(),"id", "design-new-pp-horizontal-split-panel");
		
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
		
		tabPanelMain.getTabBar().addSelectionHandler(new SelectionHandler<Integer>(){
			
			private int previousTab = 0;
			
			public void onSelection(SelectionEvent<Integer> event) {
				
					if (previousTab == event.getSelectedItem()) {
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
						// Launch onTabClosed for previously selected tab
						((Tab)tabPanelMain.getWidget(previousTab)).onTabClosed();
						
						// Launch onTabOpened for newly selected tab
						previousTab = event.getSelectedItem();
						
						((Tab)tabPanelMain.getWidget(previousTab)).onTabOpened();
					}

					// Window.alert("You clicked tab " + tabIndex);

			}});
//		addTabListener(new TabListener() {
//			private int previousTab = 0;
//			public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
//				if (previousTab == tabIndex) {
//					if (!isMainTabPanelFullScreen) {
//						verticalSplitPanelMain.setSplitPosition("100%");
//						horizontalSplitPanelMain.setSplitPosition("100%");
//						isMainTabPanelFullScreen = true;
//						isRightTabPanelFullScreen = false;
//						isBottomTabPanelFullScreen = false;
//
//					} else {
//						verticalSplitPanelMain
//								.setSplitPosition(verticalSplitPosition + "%");
//						horizontalSplitPanelMain
//								.setSplitPosition(horizontalSplitPosition + "%");
//						isMainTabPanelFullScreen = false;
//					}
//				} else {
//					previousTab = tabIndex;
//					
//				}
//
//				// Window.alert("You clicked tab " + tabIndex);
//			}
//
//			public boolean onBeforeTabSelected(SourcesTabEvents sender,
//					int tabIndex) {
//				return true;
//			}
//		});
		horizontalSplitPanelMain.setLeftWidget(tabPanelMain);

		// Right TabPanel 
		tabPanelRight = new TabPanel();
//		// test tab
//		verticalPanel = new VerticalPanel();
//		tabPanelRight.add(verticalPanel, "Search");
//		SimplePanel p = new SimplePanel();
//		DOM.setStyleAttribute(p.getElement(), "width", "100%");
//		p.add(tabPanelRight);
		horizontalSplitPanelMain.setRightWidget(tabPanelRight);
//		horizontalSplitPanelMain.
		DOM.setStyleAttribute(DOM.getParent(tabPanelRight.getElement()), "overflow", "hidden");
		tabPanelRight.setSize("100%", "100%");

		tabPanelRight.getTabBar().addSelectionHandler(new SelectionHandler<Integer>() {
			private int previousTab = 0;
			public void onSelection(SelectionEvent<Integer> event) {
				if (previousTab == event.getSelectedItem()) {
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
					// Launch onTabClosed for previously selected tab
					((Tab)tabPanelRight.getWidget(previousTab)).onTabClosed();
					
					// Launch onTabOpened for newly selected tab
					previousTab = event.getSelectedItem();
					
					((Tab)tabPanelRight.getWidget(previousTab)).onTabOpened();
					
				}
				// Window.alert("You clicked tab " + tabIndex);
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

		tabPanelBottom.getTabBar().addSelectionHandler(new SelectionHandler<Integer>(){
			private int previousTab = 0;
			public void onSelection(SelectionEvent<Integer> event) {
				if (previousTab == event.getSelectedItem()) {
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
					// Launch onTabClosed for previously selected tab
					((Tab)tabPanelBottom.getWidget(previousTab)).onTabClosed();
					
					// Launch onTabOpened for newly selected tab
					previousTab = event.getSelectedItem();
					
					((Tab)tabPanelBottom.getWidget(previousTab)).onTabOpened();
				}
				// Window.alert("You clicked tab " + tabIndex);
			}
		});
		verticalSplitPanelMain.setBottomWidget(tabPanelBottom);
//		verticalSplitPanelMain.setHeight("90%");

	}

}
