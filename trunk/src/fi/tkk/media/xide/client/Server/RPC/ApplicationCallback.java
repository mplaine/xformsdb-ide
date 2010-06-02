package fi.tkk.media.xide.client.Server.RPC;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;

import fi.tkk.media.xide.client.popups.basic.LoadingPopup;

/**
 * 
 * @author nicholas
 */
public abstract class ApplicationCallback implements AsyncCallback, Command {

	public void onFailure(Throwable caught) {

		onFailureBefore(caught);

		if (caught instanceof AuthenticationException) {
			LoadingPopup.hideDimmed();
			LoginDialog.getLoginDialog(caught.getMessage(), this);
		}
		else {


		  onFailureAfter(caught);
		}
	}

	protected void onSuccessAfter(Object result) {
	}

	protected void onFailureBefore(Throwable caught) {

	}

	protected void onFailureAfter(Throwable caught) {
	}

	public void onSuccess(Object result) {
		onSuccessAfter(result);
	}

	public void execute() {
		try {
			authenticateAndExecute();

		} catch (AuthenticationException ex) {
			
			LoginDialog.getLoginDialog(ex.getMessage(), this);
		}
	}

	public void authenticateAndExecute() throws AuthenticationException {
	}

}
