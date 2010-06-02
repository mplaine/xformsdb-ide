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

import fi.tkk.media.xide.client.Data.ArrayProperty;
import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.popups.basic.Popup;
import fi.tkk.media.xide.client.popups.basic.PopupError;
import fi.tkk.media.xide.client.popups.utils.FormPopup;
import fi.tkk.media.xide.client.popups.utils.LocalFieldCheckers;
import fi.tkk.media.xide.client.popups.utils.RemoteFieldCheckers;
import fi.tkk.media.xide.client.popups.utils.TextBoxBaseWrapper;
import fi.tkk.media.xide.client.popups.utils.interfaces.Action;
import fi.tkk.media.xide.client.popups.utils.interfaces.AsyncEventListener;
import fi.tkk.media.xide.client.utils.ConnectionToServer;
import fi.tkk.media.xide.client.utils.ConnectionToServer.CallbackActions;


/**
 * Creates a form, which is shown when a page is saved as a new component.
 * It asks a details about new component from  a user, checks that all fields are
 * filled in appropriate way and sends a request to the server
 * 
 * @author Evgenia Samochadina
 * @date Sep 23, 2009
 *
 */
public class PopupSaveAsNewComponent extends FormPopup  implements AsyncEventListener{

	// Template of the page which is now under development
	Template template;
	
	/**
	 * Creates a form, which is shown when a page is saved as a new component.
	 * It asks a details about new component from  a user, checks that all fields are
	 * filled in appropriate way and sends a request to the server
	 * 
	 */
	public PopupSaveAsNewComponent(Template template) {
		super("Save page as new component", "You are going to save this page as a new component. You can access new component through the database and add it on your page." + 
				" Now you have to specify new component's details. Please fill in the following fields and press OK button");
		
		this.template = template;
		
		TextBoxBase tb;
		TextArea ta;
		TextBoxBaseWrapper wr;
		
		// New component id (tpl_id)
		tb = new TextBox();
		String pageName = template.getProperties().get(Property.RELATED_URL).getStringValue();
		
		// Try to construct a proper id
		if (!pageName.startsWith("tpl_")) {
			pageName = "tpl_" + pageName;
		}
		
		tb.setText(pageName);
		wr = new TextBoxBaseWrapper(tb, true, this, new RemoteFieldCheckers.RemoteTemplateIDChecker(tb) , new LocalFieldCheckers.TemplateIDChecker(tb));
		
		addUIElement("New component's system name", "System ID of the component. Should start with 'tpl_', contain only '_', numbers and letters and be unique.", 
				wr);

		
		// Component's title
		tb = new TextBox();
		tb.setText(template.getProperties().get(Property.TITLE).getStringValue());
		wr =new TextBoxBaseWrapper(tb, true, this);
		addUIElement("New component's title", "Title of the component.This name will be displayed in the search. Should contain only '_', numbers and letters", wr);
		
		// Component's description
		ta = new TextArea();
		ta.setText(template.getProperties().get(Property.DESCR).getStringValue());
		wr =new TextBoxBaseWrapper(ta, true, this);
		addUIElement("New component's description", "Component description.This name will be displayed in the search. Should contain only '_', numbers and letters", wr);
	
		// Component's tags
		tb = new TextBox();
		tb.setText("");

		final ArrayList<String> tags = new ArrayList<String>();
		tb.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				new TagsSearchPopup(tags, null, new Action() {
					
					public void doAction() {
						((TextBoxBase)uiElements.get(UI_TAGS).getElement()).setText(ArrayProperty.getStringFromArray(tags));
						
					}}
					);
			}});
		wr =new TextBoxBaseWrapper(tb, false, this, null);
			
		addUIElement("New component's tags", "Tags that should be assigned to the new component. Click into the textbox to start tag selection.", wr);
		
		showPopup();
		
	}
	public void onSuccessfullyFilled() {
		// Process Next button
		savePageAsComponent(((TextBoxBase)uiElements.get(UI_ID).getElement()).getText(), 
				((TextBoxBase)uiElements.get(UI_TITLE).getElement()).getText(),
				((TextBoxBase)uiElements.get(UI_DESCR).getElement()).getText(),
				((TextBoxBase)uiElements.get(UI_TAGS).getElement()).getText(),
				PopupSaveAsNewComponent.this.template.getProperties().get(Property.APP_ID).getStringValue(),
				PopupSaveAsNewComponent.this.template.getProperties().get(Property.ID).getStringValue());
	}
	
	/**
	 * Makes a call to the server requesting to save page as new component
	 * @param id
	 * @param title
	 * @param descr
	 * @param tags
	 * @param appID
	 * @param pageID
	 */
	private void savePageAsComponent(final String id, final String title, final String descr, final String tags,
			final  String appID, final String pageID  ) {
		ConnectionToServer.makeACall(new CallbackActions() {

			public void execute(AsyncCallback callback) {
				connectionToServer.searchService.savePageAsComponent(
						id, title,  descr, tags, appID, pageID, callback);
			}

			public void onFailure(Throwable caught) {
				closePopup();
				new PopupError("Unfortunately new component was not saved from this page!",  caught.getMessage());
			}

			public void onSuccess(Object result) {
					// No page has found
				closePopup();
					new Popup("Page is saved as component!");
			}});
	}

}
