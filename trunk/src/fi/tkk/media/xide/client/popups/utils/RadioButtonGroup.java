/**
 * 
 */
package fi.tkk.media.xide.client.popups.utils;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;

import fi.tkk.media.xide.client.popups.basic.Popup;

/**
 * 
 * A group of several radio buttons. Is used in {@link #FormPopup} when user has to select one option
 * in the redio button set. 
 * 
 * @author Evgenia Samochadina
 * @date Feb 8, 2010
 *
 */
public class RadioButtonGroup extends VerticalPanel{
	// group ID is used for automatic creation a unique group name when new group is created
	public static int groupID = 0;
	
	// List of radiobuttons
	private ArrayList<RadioButton> list;
	
	// default radiobutton
	private RadioButton defauldButton;
	
	// group name
	private String groupName;
	/**
	 * Initializes empty group
	 */
	public RadioButtonGroup() {
		groupName = "group_" + groupID;
		list = new ArrayList<RadioButton>();
		defauldButton = null;
	}
	
	/**
	 * Adds a radio button with a given text and given enable status
	 * 
	 * @param isEnabled
	 * @param text
	 */
	public void add(boolean isEnabled, String text) {
		RadioButton rb = new RadioButton(groupName, text);
		rb.setEnabled(isEnabled);
		list.add(rb);
		this.add(rb);
	}
	
	/**
	 * Adds an enabled radio button with a given text
	 * @param text
	 */
	public void add(String text) {
		add(true, text);
	}
	
	/**
	 * Adds an enabled radio button with a given text and description
	 * @param text
	 */
	public void add(String text, String description) {
		add(text, description, true);
	}
	
	/**
	 * Adds radio button with a given text and description and given enable status
	 * @param text
	 */
	public void add(String text, String description, boolean isEnabled) {
		add(isEnabled, text);
		Label descr = new Label(description);
		
		descr.setWordWrap(true);
		descr.setStyleName("design-properties-table-radio-button-description");
		
		this.add(descr);
	}

	/**
	 * Adds a radio button with a given text and marks it as default one
	 * @param text
	 * @param isDefault
	 */
	public void add(String text, boolean isDefault) {
		add(text);

		if (isDefault) {
			// set as default button the last one which was recently added to the list
			defauldButton = list.get(list.size() - 1);
			defauldButton.setValue(true);
		}
	}
	
	public boolean hasSelectedButton() {
		boolean result = false;
		
		for (Iterator<RadioButton> iterator = list.iterator(); iterator.hasNext();) {
			RadioButton button =  iterator.next();
			
			if (button.getValue()) {
				result = true;
			}
		}
		return result;
		
	}
	
	public String getSelectedButtonText() {
		String result = null;
		
		for (Iterator<RadioButton> iterator = list.iterator(); iterator.hasNext();) {
			RadioButton button =  iterator.next();
			
			if (button.getValue()) {
				result = button.getText();
			}
		}
		return result;	
	}
	
	  public HandlerRegistration addValueChangeHandler(
		      ValueChangeHandler<Boolean> handler) {
	
		  HandlerRegistration reg = null;
		  if (list.size() != 0) {
			for (Iterator<RadioButton> iterator = list.iterator(); iterator.hasNext();) {
				RadioButton o =  iterator.next();
				reg = o.addValueChangeHandler(handler);
			}
		  }
		  return reg;
	  }

}
