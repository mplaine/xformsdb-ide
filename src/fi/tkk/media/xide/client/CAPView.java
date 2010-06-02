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
import fi.tkk.media.xide.client.Tabs.PropertiesTab;
import fi.tkk.media.xide.client.Tabs.Tab;
import fi.tkk.media.xide.client.popups.PopupApplicationPublishing;
import fi.tkk.media.xide.client.popups.PopupApplicationRePublishing;
import fi.tkk.media.xide.client.popups.basic.Popup;
import fi.tkk.media.xide.client.popups.basic.PopupAreYouSure;
import fi.tkk.media.xide.client.popups.utils.interfaces.Action;

public class CAPView extends View{
	
	static CAPView instance;
	// Application which is now open
	private APElementApplication application;
	
	public CAPView() {
		
		CENTER = CAPTREE;
		RIGHT = CAPDETAILS | CAPFILES;
		BOTTOM = 0;
		
//		this.application = application;
		
		HorizontalSplitPanel mainPanel = new HorizontalSplitPanel();
		DOM.setStyleAttribute(mainPanel.getElement(), "position", "relative");
		DOM.setElementProperty(mainPanel.getElement(), "id",
				"design-new-cap-horizontal-split-panel");
		mainPanel.setWidth("100%");

		VerticalPanel buttons = new VerticalPanel();
		buttons.setWidth("100%");
		buttons.setStyleName("design-new-cap-header");

		AbsolutePanel panel = new AbsolutePanel();
		panel.setSize("100%", "");

		panel.add(buttons);
//		CAppTreeTab treeTab = new CAppTreeTab(application);
//		panel.add(treeTab);
		
		mainPanel.setLeftWidget(panel);
		DOM.setStyleAttribute(DOM.getChild(mainPanel.getElement(), 0), "marginTop", "10px");
		
		Tab propertyTab  = new PropertiesTab(true, false, false, View.CAPDETAILS);
		Tab fileTreeTab = new FileTreeTab(View.CAPFILES);
		
		TabPanel tabPanelRight = new TabPanel();
		tabPanelRight.getDeckPanel().addStyleDependentName("cap-right");
		tabPanelRight.setSize("100%", "100%");

		tabPanelRight
				.add(
						propertyTab,
						new HTML(
								"<div class=\"tabHeader\"> <div class=\"spn\"> <span>Properties</span> </div> </div>"));
		tabPanelRight
				.add(
						fileTreeTab,
						new HTML(
								"<div class=\"tabHeader\"> <div class=\"spn\"> <span>Files</span> </div> </div>"));

		tabPanelRight.getTabBar().selectTab(0);
		
		mainPanel.setRightWidget(tabPanelRight);
		
//		addTabToCenter(treeTab);
		addTabToRight(propertyTab, true);
		addTabToRight(fileTreeTab, false);
		
		this.mainPanel = mainPanel;

	}
	
	public static CAPView getInstance(APElementApplication newApplication) {
		if (instance == null) {
			instance = new CAPView();
//			instance.onLoad(newApplication);
		}
		
		instance.onLoad(newApplication);
		
		return instance;
	}
	
	public void onLoad(APElementApplication newApplication) {
		
		if (this.application == null || !newApplication.getProperties().get(Property.ID).equals(
				this.application.getProperties().get(Property.ID))) {
			
			this.application = newApplication;
			
			// Load new application
			VerticalPanel buttons = new VerticalPanel();
			buttons.setWidth("100%");
			buttons.setStyleName("design-new-cap-header");
			
			AbsolutePanel panel = new AbsolutePanel();
			panel.setSize("100%", "");

			panel.add(buttons);
			CAppTreeTab treeTab = new CAppTreeTab(application);
			panel.add(treeTab);
			
			((HorizontalSplitPanel)mainPanel).setLeftWidget(panel);

			// remove old tab
			if (centerTabList.size() > 0) {
				centerTabList.remove(0);
			}
			// add current
			addTabToCenter(treeTab);
		}
		else {
			// Update properties values in the table (if they have been changed) 
			updatePropertiesValues();
		}
		/*
		 * - Server-side refactoring (creates light version of application)  Export file servlet 
		 */
		String link = GWT.getModuleBaseURL() + "DownloadFile?" + application.properties.get(Property.ID).getStringValue();
		
		HTML exportLink = new HTML("<a id = \"label-like-link\" style=\"header-right-element\"  href=\"" + link + "\">Export</a>");
		exportLink.setStyleName("header-right-element");
		Main.getInstance().panelBottomLinks.add(exportLink); 

		/**
		 * There are 3 options when user clicks on this button:
		 * - Publish the application: if the application has never been published. In this case it is published, all files are copied, database is initialized 
		 * 		and application descriptor is loaded into Tomcat
		 * - Stop the application (if now it is published and running). The application descriptor is removed from Tomcat, all other files remain.
		 * - Start the application: if application was published and then stopped. If application has changed since previous publishing (published date < modified date), the user is asked
		 * 		whether he wants to start old copy of the application or republish it, so new changes apply. If application hasn't changed, 
		 * 		it asks whether user wants to reinitialize the database when starting the application
		 */
		Main.getInstance().createTempNewLinkAndAdd("Publish/Unpulish", new Action() {

			public void doAction() {
				// If application is now published
				if (application.getProperties().get(Property.IS_PUBLISHED)
						.getStringValue().equals(Property.BOOLEAN_TRUE)) {
					
					// Stop it
					new PopupAreYouSure(
							"You are going to stop the application. It will not be available online. Are you sure?",
							new ClickListener() {
								public void onClick(Widget sender) {
//									System.out.println("going to stop, " + application.properties.get(Property.IS_PUBLISHED).getStringValue() );
									application
											.playStopApplication(false);
								}

							}, null);
				} else {
					processRePublishEvent();
				}
			}
		});

		
		Main.getInstance().createTempNewLinkAndAdd("Reload", new Action() {
			public void doAction() {
				if (application.getProperties().get(Property.IS_PUBLISHED)
						.getStringValue().equals("true")) {
						new PopupApplicationRePublishing(application);
				}
				else {
					
					processRePublishEvent();
				}
			}
		});

		Main.getInstance().createTempNewLinkAndAdd("Delete", new Action() {
			public void doAction() {
				new PopupAreYouSure(
						"You are going to delete the application. Are you sure?",
						new ClickListener() {
							public void onClick(Widget sender) {
								application.delete();
							}

						}, null);

			}
		});

		Main.getInstance().setSelectedElement(CAppTreeTab.getInstance().application);
	}
	
	/**
	 * Is called when user press Publish or Reload button and the application is not published. 
	 * There are 3 cases: 
	 * - Publish the application: if the application has never been published. In this case it is published, all files are copied, database is initialized 
	 * 		and application descriptor is loaded into Tomcat
	 * - Start the application: if application was published and then stopped. 
	 * 		- If application has changed since previous publishing (published date < modified date), the user is asked
	 * 		whether he wants to start old copy of the application or republish it, so new changes apply. 
	 * 		- If application hasn't changed, 
	 * 		it asks whether user wants to reinitialize the database when starting the application
	 */
	public void processRePublishEvent() {
		
		// Application is not published now
		if (application.getProperties().get(Property.DATE_PUB).isNull()) {
			// Application is never published
			// Run Publishing popup
			new PopupApplicationPublishing(application);
		}
		else{
			// Application was published but now is stopped
			Date lastDate = null;
			Date pubDate = (Date)application.getProperties().get(Property.DATE_PUB).getValue();
			if (!application.getProperties().get(Property.DATE_MOD).isNull()) {
				lastDate = (Date)application.getProperties().get(Property.DATE_MOD).getValue();
			}
			else {
				lastDate = (Date)application.getProperties().get(Property.DATE_CR).getValue();
			}
			
			// Compare the dates
			if (pubDate.before(lastDate)) {
				// Application was modified after the last publishing
				
				new PopupAreYouSure(
						"You are going to publish the application again. It has been changed after you previously publish it. " +
						"Would you like to publish new version? Press Yes to reload new sources, press No to play the application you have on the server",
						new ClickListener() {
							public void onClick(Widget sender) {
								new PopupApplicationRePublishing(application);
							}

						}, new ClickListener() {
							public void onClick(Widget sender) {
//								System.out.println("going to play, " + application.properties.get(Property.IS_PUBLISHED).getStringValue() );
								application
										.playStopApplication(true);
							}

						}
						);
			}
			else {
				// No modifications after last publishing. Just play is needed.
				new PopupAreYouSure(
						"You are going to play the application. Are you sure?",
						new ClickListener() {
							public void onClick(Widget sender) {
//								System.out.println("going to play, " + application.properties.get(Property.IS_PUBLISHED).getStringValue() );

								application
										.playStopApplication(true);
							}

						}, null);
			}
		}
	}
}
