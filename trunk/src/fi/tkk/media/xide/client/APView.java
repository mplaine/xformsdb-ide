package fi.tkk.media.xide.client;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Data.APElementApplication;
import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.Tabs.AppTreeTab;
import fi.tkk.media.xide.client.Tabs.CAppTreeTab;
import fi.tkk.media.xide.client.Tabs.FileTreeTab;
import fi.tkk.media.xide.client.Tabs.PropertiesTab;
import fi.tkk.media.xide.client.Tabs.Tab;
import fi.tkk.media.xide.client.popups.DeleteFolderByNamePopup;
import fi.tkk.media.xide.client.popups.PopupApplicationPublishing;
import fi.tkk.media.xide.client.popups.PopupApplicationRePublishing;
import fi.tkk.media.xide.client.popups.PopupCreateNewApplication;
import fi.tkk.media.xide.client.popups.basic.Popup;
import fi.tkk.media.xide.client.popups.basic.PopupAreYouSure;
import fi.tkk.media.xide.client.popups.basic.PopupError;
import fi.tkk.media.xide.client.popups.utils.interfaces.Action;
import fi.tkk.media.xide.client.utils.ConnectionToServer;
import fi.tkk.media.xide.client.utils.ConnectionToServer.CallbackActions;

/**
 * Represents the view where application list is displayed
 * @author evgeniasamochadina
 *
 */
public class APView extends View{
	static APView instance;
	
	public APView() {
		
		CENTER = APLIST;
		RIGHT = 0;
		BOTTOM = 0;
		
		
	}
	
	public static View getInstance() {
		if (instance == null) {
			instance = new APView();
		}
		
		instance.onLoad();
		
		return instance;
	}
	public void onLoad() {
		this.mainPanel = AppTreeTab.getInstance().reloadTab();

		Main.getInstance().createTempNewLinkAndAdd("Create New Application", new Action() {
			
			public void doAction() {
				new PopupCreateNewApplication();
//				new PopupCreateNewApp();
			}
		});
		
		addTabToCenter(AppTreeTab.getInstance());
	}
	}
