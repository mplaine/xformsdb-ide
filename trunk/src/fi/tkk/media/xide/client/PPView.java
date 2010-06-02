package fi.tkk.media.xide.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TabPanel;

import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.DnD.DeleteDropController;
import fi.tkk.media.xide.client.Tabs.CSSTab;
import fi.tkk.media.xide.client.Tabs.DITab;
import fi.tkk.media.xide.client.Tabs.DataTab;
import fi.tkk.media.xide.client.Tabs.DesignTab;
import fi.tkk.media.xide.client.Tabs.FileTreeTab;
import fi.tkk.media.xide.client.Tabs.NavigatorTab;
import fi.tkk.media.xide.client.Tabs.PLTab;
import fi.tkk.media.xide.client.Tabs.PreviewTab;
import fi.tkk.media.xide.client.Tabs.PropertiesTab;
import fi.tkk.media.xide.client.Tabs.QueriesTab;
import fi.tkk.media.xide.client.Tabs.SearchComponentTab;
import fi.tkk.media.xide.client.Tabs.SourceCodeTab;
import fi.tkk.media.xide.client.Tabs.Tab;
import fi.tkk.media.xide.client.UI.Widget.AdvancedTripleTabPanelView2;
import fi.tkk.media.xide.client.UI.Widget.WebPage;
import fi.tkk.media.xide.client.popups.PopupSaveAsNewComponent;
import fi.tkk.media.xide.client.popups.utils.interfaces.Action;

public class PPView extends View{
	// 3 main tab panels
	private TabPanel tabPanelMain;
	private TabPanel tabPanelRight;
	private TabPanel tabPanelBottom;
	
	AdvancedTripleTabPanelView2 tripleView;
	
	private WebPage webPage;
	private Template template;
	
	static PPView instance;
	
	public PPView() {
		
		CENTER = DESIGN | PREVIEW;
		RIGHT = SEARCH | NAVIGATOR | PL;
		BOTTOM = PROPERTIES | PARAMS | QUERIES | DIS | DATA  | CSS | SOURSECODE | FILES;
		
		DnDController.InitDND();
	
		mainPanel = DnDController.getBoundaryPanel();
		
		
		// // Create view with 3 split panels
		tripleView = new AdvancedTripleTabPanelView2(60, 70);
		tripleView.getMainSplitPanel().setSize("100%", "100%");

		mainPanel.add(tripleView.getMainSplitPanel());
	
		
	
		// Set tab bar variables
		tabPanelMain = tripleView.getTabMain();
		tabPanelRight = tripleView.getTabRight();
		tabPanelBottom = tripleView.getTabBottom();
	
		// styles
		tabPanelMain.getDeckPanel().addStyleDependentName("pp-main");
		tabPanelRight.getDeckPanel().addStyleDependentName("pp-right");
		tabPanelBottom.getDeckPanel().addStyleDependentName("pp-bottom");
	
		DnDController.registerDropController(tabPanelRight);
		// Search tab
		rightTabList.add(new SearchComponentTab());
		tabPanelRight
				.add(
						rightTabList.get(rightTabList.size()-1),
						new HTML(
								"<div class=\"tabHeader\"> <div class=\"spn\"> <span>Search</span> </div> </div>"));
		tabPanelRight.getTabBar().selectTab(0);
	
		rightTabList.add(new NavigatorTab());
		tabPanelRight
				.add(
						rightTabList.get(rightTabList.size()-1),
						new HTML(
								"<div class=\"tabHeader\"> <div class=\"spn\"> <span>Page Hierarchy</span> </div> </div>"));
		rightTabList.add(new PLTab());
		tabPanelRight
				.add(rightTabList.get(rightTabList.size()-1),
						new HTML(
								"<div class=\"tabHeader\"> <div class=\"spn\"> <span>Used Components</span> </div> </div>"));
	
		

//		webPage = new WebPage(template);
//		
//		//TODO: remove hardcoded stuff. Manage page, webPage and pageTemplate in Main!
//		
//		Main.getInstance().webPage = webPage;
		
		// UpdateUI();
		centerTabList.add(new DesignTab("DesignTab"));
		tabPanelMain
				.add(centerTabList.get(centerTabList.size()-1),
						new HTML(
								"<div class=\"tabHeader\"> <div class=\"spn\"> <span>Design</span> </div> </div>"));
		
		centerTabList.add(new PreviewTab());
		tabPanelMain
				.add(centerTabList.get(centerTabList.size()-1),
						new HTML(
								"<div class=\"tabHeader\"> <div class=\"spn\"> <span>Preview</span> </div> </div>"));
		tabPanelMain.getTabBar().selectTab(0);
	
		tabPanelBottom.setSize("100%", "100%");
	
		// PropertiesTab propertiesTab = new PropertiesTab(this);
		// tabPanelBottom.setSize("100%", "100%");
		addTabToBottom(new PropertiesTab(true, false, false, View.PROPERTIES), true);
		tabPanelBottom
				.add(
						bottomTabList.get(bottomTabList.size()-1),
						new HTML(
								"<div class=\"tabHeaderBottom\"> <div class=\"spn\"> <span>Properties</span> </div> </div>"));
		addTabToBottom(new PropertiesTab(false, true, false, View.PROPERTIES), true);
		tabPanelBottom
				.add(
						bottomTabList.get(bottomTabList.size()-1),
						new HTML(
								"<div class=\"tabHeaderBottom\"> <div class=\"spn\"> <span>Parameters</span> </div> </div>"));
		Tab tab = (Tab) tabPanelBottom.getDeckPanel().getWidget(0);
		DOM.setStyleAttribute(tab.getElement(), "overflow", "hidden");
		
		addTabToBottom(new QueriesTab(), false);
		tabPanelBottom
				.add(bottomTabList.get(bottomTabList.size()-1),
						new HTML(
								"<div class=\"tabHeaderBottom\"> <div class=\"spn\"> <span>Queries</span> </div> </div>"));
		addTabToBottom(new DITab(), false);
		tabPanelBottom
				.add(bottomTabList.get(bottomTabList.size()-1),
						new HTML(
								"<div class=\"tabHeaderBottom\"> <div class=\"spn\"> <span>DIs</span> </div> </div>"));
		
		addTabToBottom(new DataTab(), false);
		tabPanelBottom
				.add(bottomTabList.get(bottomTabList.size()-1),
						new HTML(
								"<div class=\"tabHeaderBottom\"> <div class=\"spn\"> <span>Initial Data</span> </div> </div>"));
		addTabToBottom(new CSSTab(), false);
		tabPanelBottom
				.add(bottomTabList.get(bottomTabList.size()-1),
						new HTML(
								"<div class=\"tabHeaderBottom\"> <div class=\"spn\"> <span>CSS</span> </div> </div>"));
		addTabToBottom(new SourceCodeTab(), false);
		tabPanelBottom
				.add(bottomTabList.get(bottomTabList.size()-1),
						new HTML(
								"<div class=\"tabHeaderBottom\"> <div class=\"spn\"> <span>Source code</span> </div> </div>"));
		// tabPanelBottom.add(new AccessRightsForElementTab(), "Access Rights");
		addTabToBottom(new FileTreeTab(View.FILES), false);
		tabPanelBottom
				.add(bottomTabList.get(bottomTabList.size()-1),
						new HTML(
								"<div class=\"tabHeaderBottom\"> <div class=\"spn\"> <span>Files</span> </div> </div>"));
		tabPanelBottom.getTabBar().selectTab(0);
	}
	
	public static PPView getInstance() {
//		if (instance == null) {
//			instance = new PPView();
//		}
		instance = new PPView();
		instance.onLoad();
		
		return instance;	
	}
	
	public void onLoad() {
		Template template = Main.getInstance().getLoadedPageTemplate();
		// If it's a new template come
		if (this.template == null || !this.template.equals(template)) {
			
			this.template = template;
			webPage = new WebPage(template);
			Main.getInstance().setWebPage(webPage);
			webPage.parseCode(false);
			
			webPage.DrawChildren(false);
			
			((DesignTab)centerTabList.get(0)).setPage();
		}
		
		Main.getInstance().createTempNewLinkAndAdd("Save and back", new Action() {
			
			public void doAction() {
				
				Main.getInstance().saveEverythingFromList (new Action() {

					public void doAction() {
						Main.getInstance().onInternalViewChanged(Main.CAP
								+ ";app="
								+ webPage.getProperties().getProperties().get(
										Property.APP_ID).getStringValue());
						
					}});
			}
		});
		Main.getInstance().createTempNewLinkAndAdd("Don't save and back", new Action() {
	
			public void doAction() {
				Main.getInstance().setElementCanceled(webPage);
				Main.getInstance().onInternalViewChanged(Main.CAP
						+ ";app="
						+ webPage.getProperties().getProperties().get(
								Property.APP_ID).getStringValue());
			}
		});
	
		Main.getInstance().createTempNewLinkAndAdd("Save as new Component", new Action() {
	
			public void doAction() {
				webPage.updateSlotInfo();
				//TODO: make it more fancy!
				webPage.template.getSourceCodeFirstFile()
						.updateContentToServer(false, false);
				new PopupSaveAsNewComponent(webPage.getTemplate());
			}
		});
	}
	
}
