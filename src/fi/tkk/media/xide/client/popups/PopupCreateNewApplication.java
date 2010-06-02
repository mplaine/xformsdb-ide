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
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.Data.APElementApplication;
import fi.tkk.media.xide.client.Data.ArrayProperty;
import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.popups.basic.LoadingPopup;
import fi.tkk.media.xide.client.popups.basic.PopupError;
import fi.tkk.media.xide.client.popups.utils.CheckBoxWrapper;
import fi.tkk.media.xide.client.popups.utils.FormPopup;
import fi.tkk.media.xide.client.popups.utils.LocalFieldCheckers;
import fi.tkk.media.xide.client.popups.utils.RadioButtonGroup;
import fi.tkk.media.xide.client.popups.utils.RadioButtonGroupWrapper;
import fi.tkk.media.xide.client.popups.utils.RemoteFieldCheckers;
import fi.tkk.media.xide.client.popups.utils.TextBoxBaseWrapper;
import fi.tkk.media.xide.client.popups.utils.interfaces.AsyncEventListener;
import fi.tkk.media.xide.client.utils.ConnectionToServer;
import fi.tkk.media.xide.client.utils.ConnectionToServer.CallbackActions;


/**
 * Creates a form, which is shown when user wants to create a new application
 * It asks a details about new application from  a user, checks that all fields are
 * filled in appropriate way and sends a request to the server
 * 
 * @author Evgenia Samochadina
 * @date Sep 23, 2009
 *
 */
public class PopupCreateNewApplication extends FormPopup  implements AsyncEventListener{

	public PopupCreateNewApplication() {
		super("Create new application", "This is a las step before creating new application will start. New application will appear in the list of your applications right after creation procedure will be completed. " +
				"You can modify most of the values you enter in this form later and create as many pages as you want." + 
				" Please fill in the following fields and press OK button");
		
		
		
		TextBoxBase tb;
		TextArea ta;
		TextBoxBaseWrapper wr;
		
		// New application title
		tb = new TextBox();
		wr = new TextBoxBaseWrapper(tb, true, this);
		
		addUIElement("New appliation's title", "This title wil be displayed in the application list. " +
				"Should not contain any special symbols (e.g. ', &). E.g. 'BuyBook Online shop'", 
				wr);

		// Application's description
		ta = new TextArea();
		wr =new TextBoxBaseWrapper(ta, true, this);
		addUIElement("New application's description", "This desctiption will be displayed in the application list and help you to understand what is this application for." +
				"Should not contain any special symbols (e.g. ', &). E.g. 'Online shop with paper and audio books'", wr);

		
		// New application related url
		tb = new TextBox();
		wr = new TextBoxBaseWrapper(tb, true, this, new RemoteFieldCheckers.RemoteAppRelURLChecker(tb) , new LocalFieldCheckers.NoSpaceAndSSymbChecker(tb));
		
		addUIElement("New appliation's related url", "One word sequence; how the application will be displayed in the url of the browser" +
				"Should contain only '_', numbers and letters. E.g. 'buybook'", 
				wr);
		

		// Create Main page checkbox
		CheckBox cb = new CheckBox();
		CheckBoxWrapper cwr =new CheckBoxWrapper(cb, false, this);
		addUIElement("I want to create a page", "Check this if you want to create main page for this application right after the application will be created", cwr);

	
		// Is public checkbox
		cb = new CheckBox();
		cwr =new CheckBoxWrapper(cb, false, this);
		addUIElement("I want this application to be visible to others", "Check this if you want this application be shown to other users when they will look for example and allow them to make a copy of it.", cwr);

		showPopup();
		
	}
	
	public String getTBValue(int id) {
		return ((TextBoxBase)uiElements.get(id).getElement()).getText();
	}

	public Boolean getCBValue(int id) {
		return ((CheckBox)uiElements.get(id).getElement()).getValue();
	}
	
	public void onSuccessfullyFilled() {
		createApplication(getTBValue(CNA_TITLE), getTBValue(CNA_REL_URL), getTBValue(CNA_DESCR), getCBValue(CNA_IS_PUBLIC), getCBValue(CNA_CREATE_PAGE));
//		savePageAsComponent(((TextBoxBase)uiElements.get(UI_ID).getElement()).getText(), 
//				((TextBoxBase)uiElements.get(UI_TITLE).getElement()).getText(),
//				((TextBoxBase)uiElements.get(UI_DESCR).getElement()).getText(),
//				((TextBoxBase)uiElements.get(UI_TAGS).getElement()).getText(),
//				PopupCreateNewApplication.this.template.getProperties().get(Property.APP_ID).getStringValue(),
//				PopupCreateNewApplication.this.template.getProperties().get(Property.ID).getStringValue());
	}
	
	private void createApplication(final String appName, final String appPath, final String appDescription, 
			final boolean isPublic, final boolean runPageCreator) {
		ConnectionToServer.makeACall(new CallbackActions() {

		public void execute(AsyncCallback callback) {
			ConnectionToServer.searchService.createApplication(appName, appPath, appDescription, isPublic,  
					 callback);
			LoadingPopup.showDimmed();
		}

		public void onFailure(Throwable caught) {
//			popup.hide();
			LoadingPopup.hideDimmed();
			new PopupError("Unfortunately application was not created!",  caught.getMessage());
		}

		public void onSuccess(Object result) {
			popup.hide();
			LoadingPopup.hideDimmed();
			if (result instanceof APElementApplication) {
//				ExplorerTreeTab.getInstance().addElement(null, (APElementApplication) result);
				Main.getInstance().application = (APElementApplication)result;
				Main.getInstance().onInternalViewChanged("cap;app=" + ((APElementApplication)result).properties.get(Property.ID).getStringValue());

				if (runPageCreator) {
					new PopupCreateNewPage((APElementApplication) result);
//					new PopupCreateNewPageFromScratch(null, (APElementApplication) result);
				}
			}
//			Main.getInstance().UpdateUI();
			
		}});
}

}
