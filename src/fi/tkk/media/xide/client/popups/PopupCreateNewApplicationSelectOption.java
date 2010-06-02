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
 * Creates a form, which is shown when user wants to create a new application. It shows different options of creating new application and initiates call of corresponding popup
 * 
 * @author Evgenia Samochadina
 * @date Sep 23, 2009
 *
 */
public class PopupCreateNewApplicationSelectOption extends FormPopup  implements AsyncEventListener{

	// Different options to select
	
	public static String TEXT1 = "Search for Application Template and create new application based on it";
	public static String TEXT2 = "Search for the XFormsDB Application and copy it's source";
//	public static String TEXT3 = "Enter URL of the published XFormsDB Application and copy it's source";
	public static String TEXT4 = "Create application from scratch";
	
	public PopupCreateNewApplicationSelectOption() {
		super("Create new application", "You are going to create new application. There are several options how you can choose. " + 
				" Please select one of the options and press OK button", 30);
		
		
		RadioButtonGroup rbgroup = new RadioButtonGroup();
		rbgroup.add(TEXT1, "There is a database of predefined application templates you can use as a base for your applications. E.g. personal page template", false);
		rbgroup.add(TEXT2, "You can search for the applications created in the system. You will be offered your own aplications and other users' applications marked as public", false);
		
//		rbgroup.add(TEXT3, "You can enter URL ");
		
		rbgroup.add(TEXT4, "Create an empy application");
		RadioButtonGroupWrapper rbgwr = new RadioButtonGroupWrapper(rbgroup, true, this);
		addUIElement("How do you want to create the application", null, rbgwr);
		showPopup();
		
	}

	
	public void onSuccessfullyFilled() {
		String currentSelectedButton = getRBGValue(0);
		if (currentSelectedButton == null) {
			// an error occurred! one option should be selected
			popup.hide();
			return;
		}
		
		if (currentSelectedButton.equals(TEXT1)) {

//			(new PopupCreateNewApp_1(this)).showPopup(true);
		}
		else if (currentSelectedButton.equals(TEXT2)) {
//			(new PopupCreateNewApp_2(this)).showPopup(true);
		}
//		else if (currentSelectedButton.equals(TEXT3)) {
//			(new PopupCreateNewApp_3(this)).showPopup(true);
//		}
		else if (currentSelectedButton.equals(TEXT4)) {
			new PopupCreateNewApplication();
		}
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
