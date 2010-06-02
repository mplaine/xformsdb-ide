package fi.tkk.media.xide.client.popups.utils;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;

import fi.tkk.media.xide.client.utils.ConnectionToServer;

/**
 * Contains a set of checkers used for remote check of the field value
 * @author evgeniasamochadina
 *
 */
public class RemoteFieldCheckers {
	
	public static interface RemoteChecker {
		public void doCheck(AsyncCallback callback);
	}
	
	public static class RemoteTemplateIDChecker implements RemoteChecker{
		TextBoxBase tb;
		public RemoteTemplateIDChecker(TextBoxBase tb) {
			this.tb = tb;
		}
		
		public void doCheck(AsyncCallback callback) {
			ConnectionToServer.searchService.checkTemplateID(tb.getText(), callback); 
		}
	}

	public static class RemoteAppRelURLChecker implements RemoteChecker{
		TextBoxBase tb;
		public RemoteAppRelURLChecker(TextBoxBase tb) {
			this.tb = tb;
		}
		
		public void doCheck(AsyncCallback callback) {
			ConnectionToServer.searchService.checkAppRelURL(tb.getText(), callback); 
		}
	}
	
	public static class RemotePageRelURLChecker implements RemoteChecker{
		TextBoxBase tb;
		String appID;
		public RemotePageRelURLChecker(TextBoxBase tb, String appID) {
			this.tb = tb;
			this.appID = appID;
		}
		
		public void doCheck(AsyncCallback callback) {
			ConnectionToServer.searchService.checkPageRelURL(tb.getText(), appID, callback); 
		}
	}
	
	public static class RemoteTagTitleChecker implements RemoteChecker{
		TextBoxBase tb;
		public RemoteTagTitleChecker(TextBoxBase tb) {
			this.tb = tb;
		}
		
		public void doCheck(AsyncCallback callback) {
			ConnectionToServer.searchService.checkTagName(tb.getText(), callback); 
		}
	}
}
