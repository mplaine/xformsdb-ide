/**
 * 
 */
package fi.tkk.media.xide.client.popups.utils;

import com.google.gwt.user.client.ui.Label;

/**
 * @author Evgenia Samochadina
 * @date Feb 4, 2010
 *
 */
public class PopupWithScrollBase {
	protected PopupContainerWithScroll popup;
	
	/**
	 * 
	 */
	public PopupWithScrollBase() {
		popup = PopupContainerWithScroll.getPopup();
	}
	
	public PopupWithScrollBase(String title) {
		this();
		popup.setText(title);
	}
	
	public PopupWithScrollBase(String title, String descr) {
		this(title);
		
		Label headerDescr = new Label(descr);
		PopupContainerBase.addStyle(headerDescr, PopupContainerBase.HEADER_DESCRIPTION);
		popup.addHeader(headerDescr);

	}

}

