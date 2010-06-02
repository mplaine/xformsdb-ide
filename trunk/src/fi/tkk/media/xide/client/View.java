package fi.tkk.media.xide.client;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Data.APElementApplication;
import fi.tkk.media.xide.client.Data.HasDisplayableProperties;
import fi.tkk.media.xide.client.Tabs.PropertiesTab;
import fi.tkk.media.xide.client.Tabs.Tab;

/**
 * Base class for different views (or perspectives), exists in XIDE
 * 
 * Is responsible for storing main panel, tab lists, process UpdateUI
 *  
 * @author evgeniasamochadina
 *
 */
public class View {
	
	// PP
	public static final int DESIGN = 1;
	public static final int PREVIEW = 2;

	// CAP
	public static final int CAPTREE = 4;
	
	// AP
	public static final int APLIST = 8;
	
	// MAIN PAGE
	public static final int MAINPAGE = 16;
	

	// PP
	public static final int SEARCH = 32;
	public static final int NAVIGATOR = 64;
	public static final int PL = 128;

	// CAP
	public static final int CAPDETAILS = 256;
	public static final int CAPFILES = 512;
	

	//PP
	public static final int PROPERTIES = 1024;
	public static final int PARAMS = 2048;
	public static final int QUERIES = 4096;
	public static final int DIS = 8192;
	public static final int DATA = 16384;
	public static final int CSS = 32768;
	public static final int SOURSECODE = 65536;
	public static final int FILES = 131072;

	public int CENTER = 0;
	public int RIGHT = 0;
	public int BOTTOM = 0;
	

	public static final int i = 0;
	// Contains a main view panel, which is added to the page when the view is loaded
	Panel mainPanel;
	
	// Lists of the tabs to update
	ArrayList<Tab> centerTabList;
	ArrayList<Tab> rightTabList;
	ArrayList<Tab> bottomTabList;
	
	// Special list to store PropertyTab since they should be processed differently
	ArrayList<Tab> propertyTabList;
	
//	static View instance;
	
//	public static View getInstance() {
//		if (instance == null) {
//			instance = new View();
//		}
//		
//		instance.onLoad();
//		
//		return instance;
//	}
	
	public View() {
		centerTabList = new ArrayList<Tab>();
		rightTabList = new ArrayList<Tab>();
		bottomTabList = new ArrayList<Tab>();
		propertyTabList = new ArrayList<Tab>();
	}
	
//	public void onLoad() {
//		}
	
	public void addTabToCenter(Tab tab) {
		centerTabList.add(tab);
	}
	
	public void addTabToRight(Tab tab, boolean isPropertyTab) {
		rightTabList.add(tab);
		if (isPropertyTab) {
			propertyTabList.add(tab);
		}
	}

	public void addTabToBottom(Tab tab, boolean isPropertyTab) {
		bottomTabList.add(tab);
		if (isPropertyTab) {
			propertyTabList.add(tab);
		}
	}

	/**
	 * Initiates update of the properties tabs: change links of the propertyWidgets to the new properties
	 */
	public void updatePropertiesList() {
		for (Iterator<Tab> iterator = propertyTabList.iterator(); iterator.hasNext();) {
			((PropertiesTab)iterator.next()).updatePropertiesListOfThisTab();
		}
	}

	/**
	 * Initiates update of the properties values shown on the tabs
	 */
	public void updatePropertiesValues() {
		for (Iterator<Tab> iterator = propertyTabList.iterator(); iterator.hasNext();) {
			((PropertiesTab)iterator.next()).updatePropertiesValuesOfThisTab();
		}
	}
	/**
	 * Initiates update of the properties tab is current element is selected now
	 * @param element
	 */
	public void updatePropertiesListIfSelected(HasDisplayableProperties element) {
		if (Main.getInstance().getSelectedElement()!= null 
				&& Main.getInstance().getSelectedElement().getProperties().equals(element)) {
			Main.getCurrentView().updatePropertiesList();
		}

	}
	
	public void updatePropertiesStyles() {
		for (Iterator<Tab> iterator = propertyTabList.iterator(); iterator.hasNext();) {
			((PropertiesTab)iterator.next()).UpdateStyles();
		}
	}
	

	/**
	 * Is called to clear lists 
	 */
	public void beforeUnloadView() {
		
	}
	public void updateUIAction(int TABS) {
		// Iterate in loop
		if ((TABS & CENTER) == ((CENTER & CENTER))) {
				if (centerTabList != null) {
					for (Iterator<Tab> iterator = centerTabList.iterator(); iterator.hasNext();) {
						iterator.next().UpdateUI();
					}
				}
		}
		else if ((TABS & RIGHT) == ((RIGHT & RIGHT))) {
			if (rightTabList != null) {
				for (Iterator<Tab> iterator = rightTabList.iterator(); iterator.hasNext();) {
					iterator.next().UpdateUI();
				}
			}
		}
		else if ((TABS & BOTTOM) == ((BOTTOM & BOTTOM))) {
			if (bottomTabList != null) {
				for (Iterator<Tab> iterator = bottomTabList.iterator(); iterator.hasNext();) {
					iterator.next().UpdateUI();
				}
			}
		}
		else {
			// a single tab or set of single tabs is required to be updated
			if (TABS < CENTER) {
				// These are center tabs
				for (Iterator<Tab> iterator = centerTabList.iterator(); iterator.hasNext();) {
					Tab tab = iterator.next();
					if ((tab.getID() & TABS) == (TABS & TABS)) {
						tab.UpdateUI();
					}
				}
				
			}
			else if (TABS < RIGHT) {
				// Right tabs
			}
			else {
				// Bottom tabs
			}
		}
	}

	public void UpdateUI(int TABS) {
		updateUIAction(TABS);
		// timer.schedule(UI_UPDATE_DELAY_PERIOD_MS);
	}

	public void UpdateUI() {
		// timer.schedule(UI_UPDATE_DELAY_PERIOD_MS);
		UpdateUI(CENTER | RIGHT | BOTTOM);

		// // PrintDropTargets();
		// int tabCount = tabPanelMain.getDeckPanel().getWidgetCount();
		// for (int i = 0; i < tabCount; i++){
		// Tab tab = (Tab)tabPanelMain.getDeckPanel().getWidget(i) ;
		// tab.UpdateUI();
		// }
		// //
		// tabCount = tabPanelRight.getDeckPanel().getWidgetCount();
		// for (int i = 0; i < tabCount; i++){
		// Tab tab = (Tab)tabPanelRight.getDeckPanel().getWidget(i);
		// tab.UpdateUI();
		// }
		// //
		// tabCount = tabPanelBottom.getDeckPanel().getWidgetCount();
		// for (int i = 0; i < tabCount; i++){
		// Tab tab = (Tab)tabPanelBottom.getDeckPanel().getWidget(i);
		// tab.UpdateUI();
		// }

		// tabPanelRight.getDeckPanel().getWidget(tabCount-1);
	}

	public Panel getMainPanel() {
		return mainPanel;
	}
}
