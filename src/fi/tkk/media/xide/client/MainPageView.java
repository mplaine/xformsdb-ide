package fi.tkk.media.xide.client;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Data.APElementApplication;
import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.Tabs.CAppTreeTab;
import fi.tkk.media.xide.client.Tabs.FileTreeTab;
import fi.tkk.media.xide.client.Tabs.MainPageTab;
import fi.tkk.media.xide.client.Tabs.PropertiesTab;
import fi.tkk.media.xide.client.Tabs.Tab;
import fi.tkk.media.xide.client.popups.PopupApplicationPublishing;
import fi.tkk.media.xide.client.popups.PopupApplicationRePublishing;
import fi.tkk.media.xide.client.popups.basic.Popup;
import fi.tkk.media.xide.client.popups.basic.PopupAreYouSure;
import fi.tkk.media.xide.client.popups.utils.interfaces.Action;

public class MainPageView extends View{
	
	static MainPageView instance;
	
	public MainPageView() {
		
		CENTER = MAINPAGE;
		RIGHT = 0;
		BOTTOM = 0;
		
		MainPageTab tab = new MainPageTab();
		addTabToCenter(tab);
		
		this.mainPanel = tab;

	}
	
	public static View getInstance() {
		if (instance == null) {
			instance = new MainPageView();
		}
		
		instance.onLoad();
		
		return instance;
	}
	
	public void onLoad() {
	}
}
