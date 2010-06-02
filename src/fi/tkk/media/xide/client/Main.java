package fi.tkk.media.xide.client;

// Preview changes (when editing file!) 
// DB update (template modification date)
// Server refactor: stop sending all properties!
// Popup buttons (firefox, when play/stop)
// Link to the download messes other links!
// Remove sysouts everywhere
// add username management
// add thinking feature
//podpisi k linkam
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Data.APElement;
import fi.tkk.media.xide.client.Data.APElementApplication;
import fi.tkk.media.xide.client.Data.APElementPage;
import fi.tkk.media.xide.client.Data.AccessRights;
import fi.tkk.media.xide.client.Data.BaseProperty;
import fi.tkk.media.xide.client.Data.SaveObjectsListener;
import fi.tkk.media.xide.client.Data.Selectable;
import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.DnD.DeleteDropController;
import fi.tkk.media.xide.client.DnD.DragControllerAdding;
import fi.tkk.media.xide.client.DnD.DragControllerPLAdding;
import fi.tkk.media.xide.client.DnD.FlexTableRowDragController;
import fi.tkk.media.xide.client.DnD.FlexTableRowDropController;
import fi.tkk.media.xide.client.DnDTree.DnDTreeItem;
import fi.tkk.media.xide.client.Tabs.AppTreeTab;
import fi.tkk.media.xide.client.Tabs.CAppTreeItem;
import fi.tkk.media.xide.client.Tabs.CAppTreeTab;
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
import fi.tkk.media.xide.client.UI.Widget.Component;
import fi.tkk.media.xide.client.UI.Widget.Slot;
import fi.tkk.media.xide.client.UI.Widget.WebPage;
import fi.tkk.media.xide.client.popups.DeleteFolderByNamePopup;
import fi.tkk.media.xide.client.popups.PopupApplicationPublishing;
import fi.tkk.media.xide.client.popups.PopupApplicationRePublishing;
import fi.tkk.media.xide.client.popups.PopupCreateNewApplication;
import fi.tkk.media.xide.client.popups.PopupCreateNewApplicationSelectOption;
import fi.tkk.media.xide.client.popups.PopupSaveAsNewComponent;
import fi.tkk.media.xide.client.popups.PopupSelectInstance;
import fi.tkk.media.xide.client.popups.PopupShowUnsavedElements;
import fi.tkk.media.xide.client.popups.basic.Popup;
import fi.tkk.media.xide.client.popups.basic.PopupAreYouSure;
import fi.tkk.media.xide.client.popups.basic.PopupError;
import fi.tkk.media.xide.client.popups.utils.PopupContainerWithScroll;
import fi.tkk.media.xide.client.popups.utils.interfaces.Action;
import fi.tkk.media.xide.client.utils.ConnectionToServer;
import fi.tkk.media.xide.client.utils.GoodHorizontalAdjustablePanel;
import fi.tkk.media.xide.client.utils.ConnectionToServer.CallbackActions;

public class Main implements EntryPoint, ValueChangeHandler<String> {
	public static final String IP = "home";
	public static final String PP = "pp";
	public static final String AP = "ap";
	public static final String CAP = "cap";

	public static final int DESIGN = 1;

	// 2 and 4 reserved
	public static final int SEARCH = 8;
	public static final int NAVIGATOR = 16;
	public static final int PL = 32;
	// 64 reserved

	public static final int PROPERTIES = 64;
	public static final int CSS = 128;
	public static final int DIS = 256;
	public static final int QUERIES = 512;
	public static final int SOURSECODE = 1024;

	public static final int MAIN_TAB = DESIGN;
	public static final int RIGHT_TAB = SEARCH | NAVIGATOR | PL;
	public static final int BOTTOM_TAB = PROPERTIES | CSS | DIS | QUERIES
			| SOURSECODE;

	public static final boolean SHOW_HINTS = false;
	public static final boolean USE_NEW_DESIGN = true;

	// Indicates whether XIDE is running on Windows
	// GWT compiler for Windows doesn't allow several css settings

	public static final boolean IS_RUNNING_ON_WINDOWS = true;

	private static Main instance;

	//  username of the user, which is currently logged in
//	public String username = "";

	// Panel contains footer for the PP s
	GoodHorizontalAdjustablePanel footerPanel;

	// Currently selected element
	private Selectable selectedElement;


	// main access rights
	private AccessRights accessRights;

//	// Popup showing a hint help messages
//	private static AnimatedHintPopup hintPopup;
//

	// Header
	AbsolutePanel header;
	
	
	// Login and Logout links are all the time the same
	Widget loginLink;
	Widget logoutLink;
	
	private ArrayList<Selectable> changedElements;

	String currentHistoryToken = null;
	
	// Login info panel
	AbsolutePanel panelLoginInfo;
	// Top links (Home and All Applications)
	AbsolutePanel panelTopLinks;
	
	// Bottom links wrapper (used to display a border)
	HTMLPanel panelBottomLinksWrapper;
	
	// Bottom links panel
	AbsolutePanel panelBottomLinks;
	// Start XIDE link
	Label startXIDELink;
	
	public String serverURL;


	Tab propertiesTab;
	public APElementApplication application = null;
	

	/**
	 * Web page loading management
	 */
	// APElement which represent selected page
	private APElementPage loadedPageAPElement;
	

	// Template of the selected element (received based on page object from the server)
	private Template loadedPageTemplate;
	
	// Web Page object which is constructed on the client based on Template
	// This web page is the one which is shown on Design Tab 
	private  WebPage webPage;
	
	
	static View currentView;

	/**
	 * Getters
	 * @return
	 */
	public static View getCurrentView() {
		return currentView;
	}

	public WebPage getWebPage() {
		return webPage;
	}
	public void setWebPage(WebPage webPage) {
		this.webPage = webPage;
	}

	public APElementPage getLoadedPageAPElement() {
		return loadedPageAPElement;
	}

	public void setLoadedPageAPElement(APElementPage page) {
		this.loadedPageAPElement = page;
	}

	public Template getLoadedPageTemplate() {
		return loadedPageTemplate;
	}

	public void setLoadedPageTemplate(Template templatePage) {
		this.loadedPageTemplate = templatePage;
	}


	public AccessRights getAccessRights() {
		return accessRights;
	}

	public void addNewRole(String role) {
		accessRights.addRole(role);
		if (webPage != null) {
			webPage.addNewRole(accessRights.getRoles().size() - 1,
					AccessRights.RIGHT_GRANTED);
		}
	}

	public void removeRole(String role) {
	}

	
	public void onModuleLoad() {

//		exportStaticMethod();
		// Initialize static instance which will be given to all project classes
		// when they require instance of Main class
		instance = this;
		// connectionToServer = new ConnectionToServer();

		History.addValueChangeHandler(this);
		// InitAP();
		// Init hint popup
//		hintPopup = new AnimatedHintPopup();
		getServerURL();
		// Init access rights
		accessRights = new AccessRights();
		accessRights.setFakeAccessRights();

		// Initialize list to store currently changed elements
		changedElements = new ArrayList<Selectable>();
		
		// Initialize header, panels for links
		initHeader();

		// Initialize login and logout links
		loginLink = createTempNewLink("Login",
				new Action() {

			public void doAction() {
				// Just gets username, doesn't switch to AP
				new PopupCreateNewApplicationSelectOption();
//				Property p = new BaseProperty("name", true, false, "descr");
//				new PopupSelectInstance(p);
//				new PopupError("Unfortunately login has failed", "The server is not responding now, please contact your administrator!The server is not responding now, please contact your administrator!The server is not responding now, please contact your administrator!");
//				onEnterToXIDE(false);
			}
		}, true);
		
		
		
		logoutLink= createTempNewLink("Logout", new Action() {

			public void doAction() {
				
				onInternalViewChanged(IP, 
						// Action to perform when unsaved elements will be checked
					new Action() {
					public void doAction() {
					
						ConnectionToServer.makeACall(new CallbackActions() {
		
							public void execute(AsyncCallback callback) {
								ConnectionToServer.searchService
										.logout(callback);
							}
		
							public void onFailure(Throwable caught) {
								new PopupError(" Logout has finished with errors. Please refresh the page! ",  caught.getMessage());
							}
		
							public void onSuccess(Object result) {
								onLogOut();
							}
						});
					}});
			}
		}, true);
		
		// Add message that user is not logged in
		createLoginInfo("You are not logged in.", loginLink);

	
		// History.newItem(AP);
		// Make a call to check if user is logged in to show the correct
		// message.
		ConnectionToServer.makeUnsecuredCall(new CallbackActions() {

			public void execute(AsyncCallback callback) {
				ConnectionToServer.searchService.getUserName(callback);
			}

			public void onFailure(Throwable caught) {
//				new PopupError(" Cannot get username!",  caught.getMessage());
			}

			public void onSuccess(Object result) {
				if (result != null) {
					// User is logged in. Message should be changed
					// Add message that user is not logged in
					onLogin((String) result);
				}
			}
		});

//		InitLoadPage();

		History.newItem(IP);
		// createLoginInfo("You are not logged in. ",
		// createTempNewHyperLink("Login", new Action() {
		//
		// public void doAction() {
		// // History.newItem(AP);
		// }}, ""));

		// History.newItem(AP);
	}

	/**
	 * Sends request to the server trying to access the username. Combines several functions:
	 * - If user is authenticated, get's its username and shows it in the login info string
	 * - If user is not logged yet, initiates login dialog and then gets the username (if user logged in)
	 * - After login info string is updated can switch to AP (depending on the parameter value)
	 * 
	 * @param showAP if set to true, aplication will switch to AP when a username is received
	 */
	public void onEnterToXIDE(final boolean showAP) {
		ConnectionToServer.makeACall(new CallbackActions() {

			public void execute(AsyncCallback callback) {
				ConnectionToServer.searchService
						.getUserName(callback);
			}

			public void onFailure(Throwable caught) {

			}

			public void onSuccess(Object result) {
				if (result instanceof String) {
					onLogin((String) result);
					//addLoggedInInfo();
				}
				if(showAP) {
					onAllApplications();
					onInternalViewChanged(AP, null);
				}
			}
		});

	}
	
	/**
	 * Changes login info to "User is not loggend in. Login" and initiates Login button.
	 *  Is called when user is logged out. 
	 */
	private void addLoggedOutInfo() {
		createLoginInfo("You are not logged in.", loginLink);
	}

	/**
	 * Changes login info to "XXX is logged in. Logout" and initiates Logout button.
	 * Is called when user is logged in.
	 */
	private void addLoggedInInfo(String name) {
		createLoginInfo("You are logged in as " + name + ". ", logoutLink);
	}

	/**
	 * Is called when user loggs in
	 * - Change login info accordingly
	 * - Add Home and All Applications links
	 */
	private void onLogin(String username) {
		
		addLoggedInInfo(username);
		
		// If buttons are not added yet
		if (panelTopLinks.getWidgetCount() == 0 ) {
			
			createTopLinkAndAdd("My applications", new Action() {
	
				public void doAction() {
					onInternalViewChanged(AP, new Action() {

						public void doAction() {
							onAllApplications();							
						}});
					
				}
	
			});
	
			createTopLinkAndAdd("Home", new Action() {
	
				public void doAction() {
					onInternalViewChanged(IP, new Action() {

						public void doAction() {
							onHome();
						}});
					 
				}
	
			});
		}
	}
	
	/**
	 * Is called when user loggs out
	 * - Change login info accordingly
	 * - Remove Home and All Applications links
	 * - Remove bottom links
	 * - Show home page
	 */
	public void onLogOut() {
		
		addLoggedOutInfo();
		
		clearTopLinksPanel();
		removeBottomLinksPanel();
		
//		changeView(IP);
//		History.newItem(IP);
	}	
	
	/**
	 * Is called when logged user clicks Home button
	 * - Remove bottom links
	 * - Show home page
	 */
	public void onHome() {
		removeBottomLinksPanel();
//		changeView(IP);
//		History.newItem(IP);
	}
	
	/**
	 * Is called when logged user clicks All Applications button
	 * - Show bottom links
	 * - Show AP
	 */
	public void onAllApplications(){
		initBottomLinksPanel();
//		changeView(AP);
//		History.newItem(AP);
	}
	
	public void onInternalViewChanged(String historyToken) {
//		clearBottomLinksPanel();
		changeView(historyToken, null);
//		History.newItem(historyToken);
	}
	
	public void onInternalViewChanged(String historyToken, Action action) {
		
		changeView(historyToken, action);
//		History.newItem(historyToken);
	}
	
	
	/**
	 * Initializes header panel for home page. Is used when the XIDE initial page is loaded.
	 * Does not include bottom link panel initialization, since it is needed when user loggs in and is done in {@link #initBottomLinksPanel}
	 * 
	 */
	
	private void initHeader() {
		// Header
		RootPanel header = RootPanel.get("design-new-header");

		panelTopLinks = new AbsolutePanel();
		panelTopLinks.setStyleName("design-new-header-perm-links");
		
		panelLoginInfo = new AbsolutePanel();
		DOM.setStyleAttribute(panelLoginInfo.getElement(), "float", "right");

		// Top panel
		AbsolutePanel topPanel = new AbsolutePanel();
		topPanel.setStyleName("design-new-header-top-panel");

		topPanel.add(panelTopLinks);
		topPanel.add(panelLoginInfo);

		header.add(topPanel);

	}

	/**
	 * Initializes bottom links panel of the XIDE header. Creates wrapper (used for border) and internal panel, which contains links. 
	 * E.g.is used when the XIDE AP perspective is loaded.
	 */
	private void initBottomLinksPanel() {
		// Header
		RootPanel header = RootPanel.get("design-new-header");

		if (panelBottomLinksWrapper == null){
			panelBottomLinksWrapper = new HTMLPanel(
				"<div class=\"header-bottom-panel-style-wrapper\">"
						+ "<div id=\"header-bottom-panel-style-id\" class=\"header-bottom-panel-style\" >"
						+ "</div>" + "</div>");
			panelBottomLinks = new AbsolutePanel();
			panelBottomLinks.setStyleName("design-new-header-temp-links");

			panelBottomLinksWrapper.add(panelBottomLinks,
					"header-bottom-panel-style-id");
		}
		header.add(panelBottomLinksWrapper);

	}

	/**
	 * Clears header when logout operation is performed.
	 * Removes links since it shouldn't be shown when user is not logged in.
	 * Is called every time when Logout button is pressed, so need to check if header panels were initialized. 
	 */
	public void clearTopLinksPanel() {
		if (panelTopLinks != null) {
			panelTopLinks.clear();
			}
	}

	/**
	 * Clears header when home page is loaded operation is performed.
	 * Removes wrapper of the bottom panel since it shouldn't be shown when user is on home page.
	 */
	public void removeBottomLinksPanel() {
		if (panelBottomLinksWrapper!= null) {
			panelBottomLinks.clear();
			panelBottomLinksWrapper.removeFromParent();
			
		}
	}
	
	public void clearBottomLinksPanel() {
		// panelTopLinks.clear();
		if (panelBottomLinks != null) {
			panelBottomLinks.clear();
		}
	}
	
	private void getPageTemplate() {
		ConnectionToServer.makeACall(new CallbackActions() {

			public void execute(AsyncCallback callback) {
				ConnectionToServer.searchService.getWebPage(loadedPageAPElement, callback);
			}

			public void onFailure(Throwable caught) {
				// No page has found
			new PopupError("An error occur while receiveng the page you requested!", caught.getMessage());
			History.back();
//				Main.getInstance().onInternalViewChanged(AP);

			}

			public void onSuccess(Object result) {
				if (result == null) {
					// No page has found
					new PopupError("An error occur while receiveng the page you requested!", "No message from server");
					History.back();
//					Main.getInstance().onInternalViewChanged(AP);
					
				} else if (result instanceof Template) {
					// A page has found
					loadedPageTemplate = (Template) result;
					
					// Update properties from apElement to the template
					loadedPageTemplate.properties = loadedPageAPElement.properties;
					// TODO1: uncomment
					currentView = PPView.getInstance();
					RootPanel.get("main").add(currentView.getMainPanel());
					
//					if (mainPP == null) {
//						InitPP();
//					}
//					onLoadPP();
//					RootPanel.get("main").add(mainPP);
				}
			}
		});
	}

	private void getServerURL() {
		ConnectionToServer.makeACall(new CallbackActions() {

			public void execute(AsyncCallback callback) {
				ConnectionToServer.searchService.getServerURL(callback);
			}

			public void onFailure(Throwable caught) {

			}

			public void onSuccess(Object result) {
				if (result instanceof String) {
					serverURL = (String) result;
				}
			}
		});
	}

	public void changeView(String historyToken, final Action action) {
		
		if (currentHistoryToken == null) {
			
			// Do nothing, a home page will be loaded first anyway
			processHistoryChange(historyToken, action);
		}
		else {
			// Check if there are some unsaved elements in the list
			if (!changedElements.isEmpty()) {
				// Show dialog asking whether user wants to save or discard his changes or cancel changing view
				new PopupShowUnsavedElements(changedElements, historyToken, new Action() {

					public void doAction() {
						// Clear element list
						changedElements.clear();
						if (action != null) {
							action.doAction();
						}
					}});
			}
			else {
				// Change view
				processHistoryChange(historyToken, action);
			}
		}
	}
	
	/**
	 * Tries to perform adding of history new token. First does the action required by the call, 
	 * then either calls view update (if it is the same history token then previous) or adds new token 
	 * @param historyToken
	 * @param action
	 */
	public void processHistoryChange(String historyToken, final Action action) {
		
		if (action != null) {
			action.doAction();
		}
		if (History.getToken().equals(historyToken)) {
			System.out.println("update ui");
			getCurrentView().UpdateUI();
		}
		else {
			History.newItem(historyToken);
		}
	}

	/**
	 * Selects what view to load depending on the history token and loads it.
	 */
	public void manageView(String historyToken) {
		clearBottomLinksPanel();
//		RootPanel.get("main").clear();
		RootPanel main = RootPanel.get("main");
		
		
		main.clear();
//		clean(RootPanel.get("main").getElement());

//		clearBottomLinksPanel();
		if (historyToken.equals(PP)) {
//			currentView = CAPView.getInstance(application);
//			main.add(currentView.getMainPanel());

			getPageTemplate(); 
		} else if (historyToken.equals(AP)) {
			
			// Current view is set to AP, 
			currentView = APView.getInstance();
//			CAPView.getInstance(application);
			main.add(currentView.getMainPanel());
		} else if (historyToken.startsWith(CAP)) {

			// TODO1: uncomment
			currentView = CAPView.getInstance(application);
			Panel mainPP = currentView.getMainPanel();
			main.add(mainPP);


		} else if (historyToken.equals(IP)) {
			currentView = MainPageView.getInstance();
			main.add(currentView.getMainPanel());
		}
		currentHistoryToken = historyToken;
	}


	/**
	 * Create new link with an action
	 * 
	 * @param text
	 *            Link text
	 * @param action
	 * @return
	 */
	public Label createTempNewLink(String text, final Action action,
			boolean isTopLink) {
		Label label = new Label(text);
		if (isTopLink) {
			DOM.setElementProperty(label.getElement(), "id",
					"label-like-link-top");
		} else {
			DOM.setElementProperty(label.getElement(), "id", "label-like-link");
		}

		label.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				action.doAction();
			}
		});

		label.setStyleName("header-right-element");
		return label;
	}

	/**
	 * Initialize login info string. It can be - You are not logged in. Login -
	 * You are logged in as username. Logout So it adds corresponding styles and
	 */
	private void createLoginInfo(String text, Widget link) {
		// RootPanel element = RootPanel.get("log-in-status-label");
		// Check if there is smth to remove
		panelLoginInfo.clear();

		HTML label = new HTML(text);
		label.setStyleName("design-new-login-info-text");
		link.setStyleName("header-right-element");
		// label.addStyleName("design-header-label-button");
		panelLoginInfo.add(link);
		panelLoginInfo.add(label);
	}

	public void createTopLinkAndAdd(String text, final Action action) {
		panelTopLinks.add(createTempNewLink(text, action, true));
	}

	public void createTempNewLinkAndAdd(String text, final Action action) {
		panelBottomLinks.add(createTempNewLink(text, action, false));
	}



	public static void showHintPopup(int num) {
		if (SHOW_HINTS) {
//			hintPopup.changePopupTextAndShow(AnimatedHintPopup.hintTexts[num]);
		}
	}

	AdvancedTripleTabPanelView2 tripleView;

	/**
	 * Checks whether element should be added to the changed element list and adds it (if yes)
	 * 
	 * @param element
	 */
	public void addToChangedElements(Selectable element) {
		// DnDTree Item souldn't be added to avoid duplicates (since it already contains component/slot/webpage in it) 
		if (! (element instanceof DnDTreeItem)) {
			// Slot should not be added since it does not have any sense to have Slot object separately
			if (! (element instanceof Slot)) {
				if (! changedElements.contains(element))
				{
					changedElements.add(element);
				}
			}
		}
	}
	
	/**
	 * Checks whether element should be added to the changed element list and adds it (if yes)
	 * 
	 * @param element
	 */
	public void removeFromChangedElements(Selectable element) {
		if (! (element instanceof DnDTreeItem)) {
			changedElements.remove(element);
		}
	}
	
	/**
	 * Sets currently selected element to the changed status. Adds it to the list of the currently unsaved elements
	 */
	public void setSelectedElementChanged() {
		selectedElement.Changed();
		addToChangedElements(selectedElement);
	}
	
	/**
	 * Sets given element to the changed status. Adds it to the list of the currently unsaved elements
	 */
	public void setElementChanged(Selectable element) {
		element.Changed();
		addToChangedElements(element);
	}
	
	/**
	 * Sets currently selected element to the saved status. Removes it from the list of the currently unsaved elements
	 */
	public void setSelectedElementSaved() {
		selectedElement.Saved();
		removeFromChangedElements(selectedElement);
	}
	
	/**
	 * Sets given element to the saved status. Removes it from the list of the currently unsaved elements
	 */
	public void setElementSaved(Selectable element) {
		element.Saved();
		removeFromChangedElements(element);
	}

	public void saveEverythingFromList(Action action) {
		// Initiate listener, which will handle success/failed events from the element saving procedures
		SaveObjectsListener listener = new SaveObjectsListener(changedElements.size(), action);
		
		for (Iterator<Selectable> iterator = changedElements.iterator(); iterator.hasNext();) {
			Selectable elementToSave =  iterator.next();
			
			listener.addElementToSave(elementToSave);
		}
		listener.doSave();
		
	}
	/**
	 * Sets currently selected element to the canceled status. Removes it from the list of the currently unsaved elements
	 */
	public void setSelectedElementCanceled() {
		selectedElement.Canceled();
		removeFromChangedElements(selectedElement);
	}
	/**
	 * Sets given element to the canceled status. Removes it from the list of the currently unsaved elements
	 */
	public void setElementCanceled(Selectable element) {
		element.Canceled();
		removeFromChangedElements(element);
	}

	public Selectable getSelectedElement() {
		return selectedElement;
	}
	

	public void setSelectedElement(Selectable selectedElement) {
		// If this element is already selected
		if (this.selectedElement == selectedElement) {
			// Do nothing
			return;
		}
		// Otherwise process element selection
		
		// Unselect previously selected
		if (this.selectedElement != null) {
			this.selectedElement.Unselect();
		}

		// Make logical selection
		this.selectedElement = selectedElement;

		if (selectedElement != null) {

			// Select
			this.selectedElement.Select();


		} else {
			// selectedElementName.setText(NO_ELEMENT_SELECTED);
			// Delete button
		}
		// TODO: REMOVE HARD CODED STUFF!!!!!!!!!!!
		if (selectedElement instanceof CAppTreeItem) {
			UpdateUI(this.RIGHT_TAB);
			// propertiesTab.UpdateUI();
		} else {
			UpdateUI(this.BOTTOM_TAB | this.RIGHT_TAB);
		}

	}

	public void updateUIAction(int TABS) {
		if (currentView != null) {
	//		System.out.println("update tabs actions" + TABS);
			// Iterate in loop
			if ((TABS & MAIN_TAB) == ((MAIN_TAB & MAIN_TAB))) {
				currentView.updateUIAction(currentView.CENTER);
			}
			if ((TABS & RIGHT_TAB) == ((RIGHT_TAB & RIGHT_TAB))) {
				currentView.updateUIAction(currentView.RIGHT);
			}
			if ((TABS & BOTTOM_TAB) == ((BOTTOM_TAB & BOTTOM_TAB))) {
				currentView.updateUIAction(currentView.BOTTOM);
			}
		}
		// updateFooter();
	}

	public void UpdateUI(int TABS) {
		updateUIAction(TABS);
		// timer.schedule(UI_UPDATE_DELAY_PERIOD_MS);
	}

	public void UpdateUI() {
		// timer.schedule(UI_UPDATE_DELAY_PERIOD_MS);
		UpdateUI(MAIN_TAB | RIGHT_TAB | BOTTOM_TAB);

	}


	public static Main getInstance() {
		return instance;
	}

	public void onValueChange(ValueChangeEvent event) {
		manageView(History.getToken());
	}
	
	/**
	 * Is called when component call or component order has changed. This requires to change component's parent slot and a web page 
	 * @param component
	 */
	public static void onSlotChangedFirst(Slot parentSlot) {
		
		WebPage page = (WebPage)parentSlot.getParentElement();
		onSlotChangedFirst(parentSlot, page);
	
	}
	
	/**
	 * Is called when component call or component order has changed. This requires to change component's parent slot and a web page 
	 * @param component
	 */
	public static void onSlotChangedFirst(Component component) {
		Slot parentSlot = (Slot)component.getParentElement();
		
		WebPage page = (WebPage)parentSlot.getParentElement();
		onSlotChangedFirst(parentSlot, page);
		
	}
	
	/**
	 * Is called when component call or component order has changed. This requires to change component's parent slot and a web page 
	 * @param component
	 */
	public static void onSlotChangedFirst(Slot parentSlot, WebPage page) {
		// Mark slot as changed
//		Main.getInstance().setElementChanged(parentSlot);
		parentSlot.Changed();
		Main.getInstance().setElementChanged(page);
		
//		onSlotValueChanged();
		
		
	}
	
	public static void onSlotValueChanged() {
		WebPage page = Main.getInstance().webPage;
		page.updateSlotInfo();
	}

	/**
	 * Is called from CAP or PP when current element's (application or page) modification date needs to be updated
	 * Currently caused by file management
	 * 
	 * Method gets the application (if CAP) or page (PP) and changes modified date
	 */
	public void updateDateOfSelectedElement() {
		APElement element = null;
		if (currentView instanceof CAPView) {
			element = application;
		}
		if (currentView instanceof PPView) {
			element = loadedPageAPElement;
		}
		
		if (element != null) {
			element.updateAPElementModifiedDate(false);
		}
	}
}
