package fi.tkk.media.xide.client.popups;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.Data.Selectable;
import fi.tkk.media.xide.client.popups.utils.PopupContainer;
import fi.tkk.media.xide.client.popups.utils.interfaces.Action;

/**
 * Is shown when user requiests to change the view, but current view contains some unsaved elements. 
 * User is asked whether he/she wants to save all (or some) elements, discard all changes or just cancel action. 
 * 
 * @author evgeniasamochadina
 *
 */
public class PopupShowUnsavedElements {

	PopupContainer popup;
	
	/**
	 * 
	 * @param changedElements list of changed elements
	 * @param hislotyToken 
	 * @param action action, which was requested. 
	 */
	public PopupShowUnsavedElements(final ArrayList<Selectable> changedElements, final String historyToken, final Action action) {
		
		popup = PopupContainer.getPopup();
		
		
		final VerticalPanel vpElements = new VerticalPanel();
		
		final CheckBox checkEverythingCB = new CheckBox("Check everything");
		if (!Main.IS_RUNNING_ON_WINDOWS) {
			DOM.setStyleAttribute(checkEverythingCB.getElement(), "borderBottom", "1px solid grey");
		}
		DOM.setStyleAttribute(checkEverythingCB.getElement(), "marginBottom", "3px");
		DOM.setStyleAttribute(checkEverythingCB.getElement(), "paddingBottom", "2px");

		checkEverythingCB.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				boolean value = checkEverythingCB.getValue();
				
				for (int i = 1 ; i < vpElements.getWidgetCount(); i++)
				{
					((CheckBox)vpElements.getWidget(i)).setValue(value);
				}
				
			}});
		vpElements.add(checkEverythingCB);
		
		for (Iterator<Selectable> iterator = changedElements.iterator(); iterator.hasNext();) {
			Selectable element = iterator.next();
			vpElements.add(new CheckBox(element.getProperties().getProperties().get(Property.TITLE).getStringValue() + "<i style=\"font-size:70%; padding: 0px 3px 0px 3px;\"> (" + element.getTypeName() + ") </i>" , true));
		}
		
		popup.addHeader("These elements are unsaved. To save some of them select one you want to save and click Save button. To discard all changes click Discard. To cancel click Calcel.");
		popup.addContent(vpElements);
		
		popup.addCloseButton("Save", new Action() {

			public void doAction() {
				// Iterate through selected objects and save them if required
				// first checkbox is not a element

				for (int i = 1 ; i < vpElements.getWidgetCount(); i++)
				{
					if (((CheckBox)vpElements.getWidget(i)).getValue()) {
						// If checked then this element should be saved
						// Leave it in the list
					}
					else {
						// if not checked then element should be canceled
						Main.getInstance().setElementCanceled(changedElements.get(i-1));
					}
				}

				// Arrange Save listener 
				Main.getInstance().saveEverythingFromList (new Action() {

					public void doAction() {
						// Finally perform the action required
						action.doAction();
						History.newItem(historyToken);				
						
					}});

			}});
		
		// If discard, cancel all changes
		popup.addCloseButton("Discard",new Action() {

			public void doAction() {
				// Iterate through selected objects and cancel them
				// first checkbox is not a element
				for (int i = 1 ; i < vpElements.getWidgetCount(); i++)
				{
					changedElements.get(i-1).Canceled();
				}
				// Finally perform the action required
				action.doAction();
				History.newItem(historyToken);
			}});
		
		// If cancel do nothing
		popup.addCloseButton("Cancel", new Action() {

			public void doAction() {
				// Do nothing
			}});
		popup.showPopup();
	}
	
}
