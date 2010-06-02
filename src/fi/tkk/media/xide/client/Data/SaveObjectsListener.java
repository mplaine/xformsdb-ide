/**
 * 
 */
package fi.tkk.media.xide.client.Data;

import java.util.ArrayList;
import java.util.Iterator;

import fi.tkk.media.xide.client.popups.basic.LoadingPopup;
import fi.tkk.media.xide.client.popups.basic.PopupError;
import fi.tkk.media.xide.client.popups.basic.PopupErrorWithAction;
import fi.tkk.media.xide.client.popups.utils.interfaces.Action;

/**
 * @author Evgenia Samochadina
 * @date Mar 8, 2010
 *
 */
public class SaveObjectsListener {
	
	int numberOfObjectsToWait;
	int numberOfObjectsReceived;
	
	ArrayList<String> errors;
	ArrayList<Selectable> elementsToSave;
	Action action;
	public SaveObjectsListener(int number, Action action) {
		numberOfObjectsToWait = number;
		numberOfObjectsReceived = 0;
		
		this.action = action;
		elementsToSave = new ArrayList<Selectable>();
		errors = new ArrayList<String>();
	}
	
	public void addElementToSave(Selectable elementToSave) {
		elementsToSave.add(elementToSave);
	}
	
	// Initiates iterative save process
	public void doSave() {
		doCurrentSave();
	}
	
	public void doCurrentSave() {
		if (elementsToSave.size()!= 0) {

			Selectable elementToSave = elementsToSave.get(0);
			elementsToSave.remove(0);
			
			elementToSave.Saved(this);
		}
	}
	public void processResult(String error) {
		numberOfObjectsReceived++;
		if (error != null) {
			// an error occurred
			errors.add(error);
		}
		else {
		}
		
		doCurrentSave();
		
		// If all objects' responses are already received
		if (numberOfObjectsReceived == numberOfObjectsToWait) {
			LoadingPopup.hideDimmed();
			if (errors.size() == 0) {
				// Saving went successfully
				// Perform required action
				action.doAction();
			}
			else {
				// Show error popup
				// Construct exception text
				String errorTextString = "";
				for (Iterator<String> iteratorErrors = errors.iterator(); iteratorErrors.hasNext();) {
					errorTextString = errorTextString + "\n" +  iteratorErrors.next();
				}
				new PopupError("Unfortunately saving has failed!" ,  errorTextString);

			}
		}
	}
}
