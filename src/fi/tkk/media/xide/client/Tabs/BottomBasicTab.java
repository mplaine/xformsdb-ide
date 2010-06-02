/**
 * 
 */
package fi.tkk.media.xide.client.Tabs;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.Data.Selectable;


/**
 * @author Evgenia Samochadina
 * @date Nov 25, 2008
 *
 */
public abstract class BottomBasicTab extends Tab implements ClickListener, KeyboardListener{
	
	public BottomBasicTab() {
		this.setStyleName("design-BottomTab");
		}
	
	boolean ShowInheritedProperties;
	boolean hasChanged;
	
	public void UpdateStyles() {
		// Saved/Changed style
		if ((Main.getInstance().getSelectedElement() != null) 
				&&(Main.getInstance().getSelectedElement().isChanged())) {
			this.setStyleName("design-BottomTab-changed");			
		}
		else {
			this.setStyleName("design-BottomTab");
		} 

	}
	public void UpdateUI(){
		UpdateStyles();
	};
	public void Disable(){};
	public void OnChange(){};
	public void Save(){};
	

	
	public void onClick(Widget sender) {
		
		if (!Main.getInstance().getSelectedElement().isChanged()) {

			// If current selected element is PL element
			// then make it changed if any property has edited
			if (Main.getInstance().getSelectedElement() instanceof PLTreeItem) {
					Main.showHintPopup(1);
			}
			// For all other selected elements
			// only properties which are not parameters should initiate element changing process
			else {
				Main.showHintPopup(0);
			}
		}
		
		updateWhenClicked();
	}

	public void onKeyDown(Widget sender, char keyCode, int modifiers) {
		// TODO Auto-generated method stub
	}

	public void onKeyPress(Widget sender, char keyCode, int modifiers) {
		// TODO Auto-generated method stub
		// Nonvaluable symbols are displayed as "" or "?"
		// If keyCode is "?" it can be a real queston mark
	}

	public abstract void updateWhenClicked() ;
	public abstract void updateWhenKeyUp() ;

	public void onKeyUp(Widget sender, char keyCode, int modifiers) {
		int keyCodeInt = Integer.valueOf(keyCode);
		// If keyCode is a valuable character which changes the source code
//		if ((keyCodeInt >= 48 && keyCodeInt <=90) || (keyCodeInt >= 96 && keyCodeInt <=111) ||
//				(keyCodeInt >= 186 && keyCodeInt <=192) || (keyCodeInt >= 219 && keyCodeInt <=222)) {
//			
//		}
		// Check key code of the input symbol
		if ((keyCodeInt >= 33 && keyCodeInt <=40)) {
			// Do not use 
			// 33  	Page Up
			// 34 	Page Down
			// 35 	End
			// 36 	Home
			// 37 	Arrow Left
			// 38 	Arrow Up
			// 39 	Arrow Right
			// 40 	Arrow Down
		}
		else {
			setElementChanged() ;
			
		}
	}
	
	/*
	 * Sets currently selected element to changed status and perform corresponding actions 
	 */
	public void setElementChanged() {
//		System.out.println("element has changed");
//		Window.alert("changed!");
		// If selected element status is not set to Changed yet
		if (Main.getInstance().getSelectedElement() != null) {
			if (!Main.getInstance().getSelectedElement().isChanged()) {
			
				// Set the status
				Main.getInstance().setSelectedElementChanged();
				Main.getCurrentView().updatePropertiesStyles();
	
				// TODO: update only after first letter typed.
//				Main.getInstance().UpdateUI(0);
				}
			updateWhenKeyUp();
			
			Main.getInstance().UpdateUI(Main.MAIN_TAB);
		}
		
	}
}
