/**
 * 
 */
package fi.tkk.media.xide.client.utils;

import fi.tkk.media.xide.client.popups.utils.interfaces.Action;

/**
 * @author Evgenia Samochadina
 * @date May 11, 2009
 *
 */
public class ActionWithText{
	protected String text;
	protected Action action;
	
	public ActionWithText() {
		this.text = null;
		this.action = null;
	}
	
	public ActionWithText (String text, Action action) {
		this.text = text;
		this.action = action;
	}
	
	public String getText() {
		return text;
	}
	
	public Action getAction() {
		return action;
	}
	public void doAction() {
		if (action != null) {
			action.doAction();
		}
	}
}
