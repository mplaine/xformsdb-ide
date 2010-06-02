package fi.tkk.media.xide.client.popups.utils.interfaces;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;

public interface UIElementWithCheck {
	
	public boolean isObligatory();
	
	/**
	 * Gets UI element
	 * @return
	 */
	public Widget getElement();
	
	public void setErrorLabel(Label label);
	/**
	 * Does the check if the element has a value (if it is defined as obligatory element) 
	 * and then a check against checking procedure
	 * @return
	 */
	public void doRequiredCheck();
	/**
	 * Does the check against checking procedure defined for the element
	 * @return
	 */
	public void doActionCheck() ;

//	/**
//	 * Does the check if the element contains any value and then against checking procedure defined for the element
//	 * @return
//	 */
//	public boolean doFullCheck() ;
}
