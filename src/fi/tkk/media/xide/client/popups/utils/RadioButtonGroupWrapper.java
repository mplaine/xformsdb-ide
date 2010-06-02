package fi.tkk.media.xide.client.popups.utils;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBoxBase;

import fi.tkk.media.xide.client.popups.utils.LocalFieldCheckers.LocalChecker;
import fi.tkk.media.xide.client.popups.utils.RemoteFieldCheckers.RemoteChecker;
import fi.tkk.media.xide.client.popups.utils.interfaces.AsyncEventListener;
import fi.tkk.media.xide.client.utils.ConnectionToServer;
import fi.tkk.media.xide.client.utils.ConnectionToServer.CallbackActions;

/**
 * Wrapper for {@link #RadioButtonGroup}, which is used in different forms. The element contains a check method,
 * which should be run before any action will be performed. 
 * @author evgeniasamochadina
 *
 */
public class RadioButtonGroupWrapper extends UIWrapper{
	
	// a group element
	private RadioButtonGroup element;

	/**
	 * Constructs field wrapper with predefined client-side check. It controls that the field value doesn't 
	 * contain any special symbols, which can damage the system 
	 * 
	 * @param element
	 * @param isObligatory
	 * @param listener
	 * @param action
	 * @param actionWithOutServiceCall
	 */
	public RadioButtonGroupWrapper(RadioButtonGroup element, boolean isObligatory, 
			AsyncEventListener listener) {
		this(element, isObligatory, listener, null);
		
		
		this.localChecker = null;
		}

	
	/**
	 * Constructs field wrapper with both server- and clien- side check, which are defined externally
	 * 
	 * @param element
	 * @param isObligatory
	 * @param listener
	 * @param action
	 * @param actionWithOutServiceCall
	 */
	public RadioButtonGroupWrapper(RadioButtonGroup element, boolean isObligatory, 
			AsyncEventListener listener, final RemoteChecker checker, 
			 final LocalChecker localChecker) {
		this(element, isObligatory, listener, checker);
		
		this.localChecker = localChecker;
	}

	/**
	 * Constructs field wrapper with server-side check only, which is defined externally
	 * @param element
	 * @param isObligatory
	 * @param listener
	 * @param action
	 */
	public RadioButtonGroupWrapper(RadioButtonGroup element, boolean isObligatory, AsyncEventListener listener, 
			final RemoteChecker checker) {
		
		this.element = element;
		this.element.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			public void onValueChange(ValueChangeEvent<Boolean> event) {
				setStyleNormalValue();				
			}});
		
		this.isObligatory = isObligatory;
		this.localChecker = null;
		this.listener = listener;
		if (checker != null) {
			this.callBackAction =	 new CallbackActions() {
	
				public void execute(AsyncCallback callback) {
					checker.doCheck(callback);
					
				}
	
				public void onFailure(Throwable caught) {
					processCheckResult(false, caught.getMessage());
				}
	
				public void onSuccess(Object result) {
					// Successfully check
					processCheckResult(true, "");
					
				}
			};
		}
		else {
			callBackAction = null;
		}
	}

	public RadioButtonGroup getElement() {
		return element;
	}


	public void doRequiredCheck() {
		
		if (element.hasSelectedButton()) {
				this.checkValue(listener);
		}
		else if (!isObligatory){
			processCheckResult(true, null);
		}
		else {
			processCheckResult(false, "Please select one option from the group.");
		}
	}
	
	public void setStyleIncorrectValue(String errorText) {
		DOM.setStyleAttribute(element.getElement(), "border", "1px solid red");
		if (fieldErrorTextLabel != null) {
			fieldErrorTextLabel.setText(errorText);
		}
	}

	public void setStyleNormalValue() {
		DOM.setStyleAttribute(element.getElement(), "border", "1px solid gray");
		if (fieldErrorTextLabel != null) {
			fieldErrorTextLabel.setText("");
		}
	}
}
