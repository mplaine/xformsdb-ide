package fi.tkk.media.xide.client.popups.utils;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.popups.utils.LocalFieldCheckers.LocalChecker;
import fi.tkk.media.xide.client.popups.utils.RemoteFieldCheckers.RemoteChecker;
import fi.tkk.media.xide.client.popups.utils.interfaces.ActionWithListener;
import fi.tkk.media.xide.client.popups.utils.interfaces.AsyncEventListener;
import fi.tkk.media.xide.client.popups.utils.interfaces.UIElementWithCheck;
import fi.tkk.media.xide.client.utils.ConnectionToServer;
import fi.tkk.media.xide.client.utils.ConnectionToServer.CallbackActions;

/**
 * Wrapper for TextBox and Text area, which is used in different forms. The element contains a check method,
 * which should be run before any action will be performed. 
 * @author evgeniasamochadina
 *
 */
public abstract class UIWrapper implements UIElementWithCheck, ActionWithListener{
//	
//	// Textbox or text area
//	private TextBoxBase element;

	protected boolean isObligatory;
	
	// Check process related stuff: 
	
	// Link to a label which displays the error
	protected Label fieldErrorTextLabel;
	
	// Callback object to process server-side check
	protected CallbackActions callBackAction;
	// User-defined action used to client-side check
	protected LocalChecker localChecker;

	// Listener for the check results
	protected AsyncEventListener listener;
	


	public void setErrorLabel(Label label) {
		fieldErrorTextLabel = label;
		DOM.setStyleAttribute(fieldErrorTextLabel.getElement(), "background", "#EAEAEA");
	}
	
	public abstract Widget getElement();
	
	public boolean isObligatory() {
		return  isObligatory;
	}
	
	/**
	 * Processes the result of the check. Sets corresponding style, adds error message if necessary and sends 
	 * a check result to the listener
	 */
	protected void processCheckResult(boolean result, String errorText) {
		if (result)	{
			// Success check
			setStyleNormalValue();
		}
		else {
			// Failed check
			setStyleIncorrectValue(errorText);
			// TODO: Set error text
		}
		// Process result
		listener.processResult(result);
	}
	public abstract void setStyleIncorrectValue(String errorText);
	
	public abstract void setStyleNormalValue() ;
	
	
	/**
	 * Does the check against checking procedure defined for the element
	 * @return
	 */
	public void doActionCheck() {
			this.checkValue(listener);
	}

	/**
	 * Checks whether the value in the UI element is OK.
	 * Firstly checks using local checker (if defined), then remote checker (if defined).
	 * The result is forwarded to the result processor via calling {@link #processCheckResult}
	 */
	public void checkValue(AsyncEventListener listener) {
		// Process local check if it exists
		if (localChecker != null) {
			String resultLocalCheck = 
				localChecker.doCheck();
			if (resultLocalCheck.length() == 0) {
				// Local check is ok 
				
				// Perform server check if it is specified
				if (callBackAction != null) {
					ConnectionToServer.makeACall(callBackAction);
				}
				else {
					processCheckResult(true, null);
//					listener.processResult(true);
//					setStyleCorrectValue();
				}
			}
			else {
				// Local check failed. Send failed result 
				processCheckResult(false, resultLocalCheck);
			}

		}
		else {
			// No local check is required
			// Perform server check if it is specified
			if (callBackAction != null) {
				ConnectionToServer.makeACall(callBackAction);
			}
			else {
				processCheckResult(true, null);
			}
			
		}
		
		
	}

}
