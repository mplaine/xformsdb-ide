package fi.tkk.media.xide.client.utils;

import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.UI.Widget.ToggleWidget;

public class ToggleActionWithTextAndAction extends ActionWithTextAndIcon{
	
	ActionWithTextAndIcon actionOne; 
	ActionWithTextAndIcon actionTwo;
	ToggleWidget toggleWidget;
	/**
	 * Constructs an object which represents 2 toggled actions with icons
	 * Only action with icons which has real icon with action attached can be added! 
	 * @param actionOne First Action
	 * @param actionTwo Second Action
	 */
	public ToggleActionWithTextAndAction(ActionWithTextAndIcon actionOne, ActionWithTextAndIcon actionTwo) {
		this.actionOne = actionOne;
		this.actionTwo = actionTwo;
		
	}
	
	public Widget getIcon() {
//		if (toggleWidget == null) {
			toggleWidget = new ToggleWidget((SourcesClickEvents)actionOne.getIcon(), (SourcesClickEvents)actionTwo.getIcon());
//		}
		return toggleWidget;
	}
	
	
//	/**
//	 * Returns text of the currently selected action
//	 */
//	public String getText() {
//			return actionOne.text;
//	}
//
//	/**
//	 * Performs the action of the currently selected action object
//	 */
//	public void doAction() {
//		if (((ToggleWidget)icon).getCurrentWidget() == actionOne.getIcon()) {
//			actionOne.doAction();
//		}
//		else {
//			actionTwo.doAction();
//		}
//		
//	}
}
