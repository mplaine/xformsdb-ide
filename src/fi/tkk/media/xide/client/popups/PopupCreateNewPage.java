/**
 * 
 */
package fi.tkk.media.xide.client.popups;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.Data.APElementApplication;
import fi.tkk.media.xide.client.Data.APElementPage;
import fi.tkk.media.xide.client.Data.ArrayProperty;
import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.Tabs.CAppTreeItem;
import fi.tkk.media.xide.client.Tabs.CAppTreeTab;
import fi.tkk.media.xide.client.popups.basic.LoadingPopup;
import fi.tkk.media.xide.client.popups.basic.PopupError;
import fi.tkk.media.xide.client.popups.utils.CheckBoxWrapper;
import fi.tkk.media.xide.client.popups.utils.FormPopup;
import fi.tkk.media.xide.client.popups.utils.LocalFieldCheckers;
import fi.tkk.media.xide.client.popups.utils.RemoteFieldCheckers;
import fi.tkk.media.xide.client.popups.utils.TextBoxBaseWrapper;
import fi.tkk.media.xide.client.popups.utils.interfaces.AsyncEventListener;
import fi.tkk.media.xide.client.utils.ConnectionToServer;
import fi.tkk.media.xide.client.utils.ConnectionToServer.CallbackActions;


/**
 * Creates a form, which is shown when user wants to create a new page
 * It asks a details about new page from  a user, checks that all fields are
 * filled in appropriate way and sends a request to the server
 * 
 * @author Evgenia Samochadina
 * @date Sep 23, 2009
 *
 */
public class PopupCreateNewPage extends FormPopup  implements AsyncEventListener{

	APElementApplication parentApp;
	 
	public PopupCreateNewPage( APElementApplication parentApp) {
		super("Create new page", "You are going to create new page. It will appear in the list of application's pages right after creation procedure will be completed. " +
				"You can modify some of the values you enter in this form later and edit the page." + 
				" Please fill in the following fields and press OK button");
		
		
		this.parentApp = parentApp;
		
		TextBoxBase tb;
		TextArea ta;
		TextBoxBaseWrapper wr;
		
		// New application title
		tb = new TextBox();
		wr = new TextBoxBaseWrapper(tb, true, this);
		
		addUIElement("New page's title", "This title wil be displayed in the page list. " +
				"This title wil be displayed in the browser header (like html title tag does) when you will access this page. " +
				"Should not contain any special symbols (e.g. ', &). E.g. 'Buy books online!'", 
				wr);

		// Application's description
		ta = new TextArea();
		wr =new TextBoxBaseWrapper(ta, true, this);
		addUIElement("New page's description", "This desctiption will be displayed in the page list and help you to understand what is this page for." +
				"Should not contain any special symbols (e.g. ', &). E.g. 'Online shop with paper and audio books'", wr);

		
		// New application related url
		tb = new TextBox();
		wr = new TextBoxBaseWrapper(tb, true, this, new RemoteFieldCheckers.RemotePageRelURLChecker(tb, 
				parentApp.getProperties().get(Property.ID).getStringValue()) , new LocalFieldCheckers.NoSpaceAndSSymbChecker(tb));
		
		addUIElement("New appliation's related url", "One word sequence; how the application will be displayed in the url of the browser" +
				"Should contain only '_', numbers and letters. E.g. 'buybook'", 
				wr);
		

		// Mark as main page checkbox
		CheckBox cb = new CheckBox();
		cb.setValue(true);
		CheckBoxWrapper cwr =new CheckBoxWrapper(cb, false, this);
		addUIElement("I want this page to be main page of the application", "This page will be loaded, when you enter application URL in the browser", cwr);

	
		// Generate default xml
		cb = new CheckBox();
		cb.setValue(true);
		cwr =new CheckBoxWrapper(cb, false, this);
		addUIElement("I want to generate basic page structure", "Page default XML structure be created. If not selected, page will be an empty xml file", cwr);
	
		// Add default container
		cb = new CheckBox();
		cb.setValue(true);
		cwr =new CheckBoxWrapper(cb, false, this);
		addUIElement("I want to add a container for components", "Page will have one container so you can start drag-n-drop right away", cwr);
		
		showPopup();
		
	}
	

	
	public void onSuccessfullyFilled() {
		createPage(getTBValue(CNP_TITLE), getTBValue(CNP_REL_URL), getTBValue(CNP_DESCR), getCBValue(CNP_GEN_XML), 
				getCBValue(CNP_ADD_CONTAINER), getCBValue(CNP_IS_MAIN_PAGE));
//		savePageAsComponent(((TextBoxBase)uiElements.get(UI_ID).getElement()).getText(), 
//				((TextBoxBase)uiElements.get(UI_TITLE).getElement()).getText(),
//				((TextBoxBase)uiElements.get(UI_DESCR).getElement()).getText(),
//				((TextBoxBase)uiElements.get(UI_TAGS).getElement()).getText(),
//				PopupCreateNewApplication.this.template.getProperties().get(Property.APP_ID).getStringValue(),
//				PopupCreateNewApplication.this.template.getProperties().get(Property.ID).getStringValue());
	}
	
	private void createPage(final String pageName, final String pagePath, 
			final String pageDescription, final boolean addDefaultXML, final boolean addDefaultCont, 
			final boolean markAsMain) {
		ConnectionToServer.makeACall(new CallbackActions() {

		public void execute(AsyncCallback callback) {
			ConnectionToServer.searchService.createPage(parentApp, pagePath, pageName, 
					addDefaultXML, addDefaultCont, markAsMain,
					pageDescription,  callback);
			LoadingPopup.showDimmed();
		}

		public void onFailure(Throwable caught) {
			popup.hide();
			LoadingPopup.hideDimmed();
			new PopupError("Unfortunately page was not created!",  caught.getMessage());
		}

		public void onSuccess(Object result) {
			popup.hide();
			LoadingPopup.hideDimmed();
//			ExplorerTreeTab.getInstance().updateApplicationList();
			if (result instanceof APElementPage) {
				parentApp.addChild(((APElementPage) result));
//				if (markAsMain) {
//					parentApp.properties.get(Property.MAIN_PAGE).setValue(
//							((APElementPage) result).getProperties().get(Property.ID).getStringValue());
//				}
				CAppTreeItem pageItem = new CAppTreeItem((APElementPage)result);
				pageItem.element.parent = parentApp;
				CAppTreeTab.getInstance().addPage(pageItem);
				Main.getInstance().setSelectedElement(pageItem);
				parentApp.updateMainPagePropertyOptions();
				
			}
			
		}});
	}
}
