/**
 * 
 */
package fi.tkk.media.xide.client.popups.utils;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;

/**
 * @author Evgenia Samochadina
 * @date Feb 4, 2010
 *
 */
public class PopupBase {
	protected PopupContainer popup;
	
	/**
	 * 
	 */
	public PopupBase() {
		popup = PopupContainer.getPopup();
	}
	
	public PopupBase(String title) {
		this();
		popup.setText(title);
	}
	
	public PopupBase(String title, String descr) {
		this(title, descr, false);

	}

	public PopupBase(String title, String descr, boolean asHTML) {
		this(title);
		
		Label headerDescr = new HTML(descr, asHTML);
		headerDescr.setWordWrap(true);
		PopupContainerBase.addStyle(headerDescr, PopupContainerBase.HEADER_DESCRIPTION);
		popup.addHeader(headerDescr);

	}
}

