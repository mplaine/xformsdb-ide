package fi.tkk.media.xide.client.popups.utils.interfaces;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.TextBoxBase;

/**
 * Is a wrapper for externally-defined asynchronous action used for server-side check
 * @author evgeniasamochadina
 *
 */
public interface ExecuteAction{
	public void doExecuteCheck(HasValue element, AsyncCallback callback);
}