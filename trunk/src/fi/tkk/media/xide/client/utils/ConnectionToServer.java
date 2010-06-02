package fi.tkk.media.xide.client.utils;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import fi.tkk.media.xide.client.Data.APElement;
import fi.tkk.media.xide.client.Data.Tag;
import fi.tkk.media.xide.client.Server.RPC.ApplicationCallback;
import fi.tkk.media.xide.client.Server.RPC.AuthenticationException;
import fi.tkk.media.xide.client.Server.RPC.LoginDialog;
import fi.tkk.media.xide.client.Server.RPC.SearchService;
import fi.tkk.media.xide.client.Server.RPC.SearchServiceAsync;


// How to use?
//private void getSMTH() {
//	connectionToServer.makeACall(new CallbackActions() {
//
//		public void execute(AsyncCallback callback) {
//			connectionToServer.searchService.smth(callback);
//		}
//
//		public void onFailure(Throwable caught) {
//			System.out.println("Falure");
//			
//		}
//
//		public void onSuccess(Object result) {
//			System.out.println("Success");
//		}});
//}
public class ConnectionToServer {
	
	public static SearchServiceAsync searchService;
	/**
	 * Inits search engine class
	 * Works for all implementations of the Search Tab
	 */
	public interface CallbackActions{
		public void onFailure(Throwable caught);
		public void onSuccess(Object result);
		public void execute(AsyncCallback callback);
	}
	
	public ConnectionToServer() {
//		// Initiate connection to server
//		searchService = (SearchServiceAsync) GWT.create(SearchService.class);
//        ServiceDefTarget endpoint = (ServiceDefTarget) searchService;
//        String moduleRelativeURL = GWT.getModuleBaseURL() + "SearchService";
//        endpoint.setServiceEntryPoint(moduleRelativeURL);

	}
	
	public static void makeACall(CallbackActions act) {
		if (searchService == null) {
			// Initiate connection to server
			searchService = (SearchServiceAsync) GWT.create(SearchService.class);
	        ServiceDefTarget endpoint = (ServiceDefTarget) searchService;
	        String moduleRelativeURL = GWT.getModuleBaseURL() + "SearchService";
	        endpoint.setServiceEntryPoint(moduleRelativeURL);
		}
		final CallbackActions actions = act;
		// Create an asynchronous callback to handle the result.

		ApplicationCallback callback = new ApplicationCallback() {

			  public void onFailureAfter(Throwable arg0) {     
				  actions.onFailure(arg0);
		         }
		      
		         public void onSuccessAfter(Object result) { 
		        	 actions.onSuccess(result);
		         }
		      
		         public void authenticateAndExecute() {   
		        	 actions.execute(this);             
		         }
		         
			};

		// Make the call
			callback.execute();
//		actions.execute(callback);
	}
	
	public static void makeALoginCall(CallbackActions act) {
		if (searchService == null) {
			// Initiate connection to server
			searchService = (SearchServiceAsync) GWT.create(SearchService.class);
	        ServiceDefTarget endpoint = (ServiceDefTarget) searchService;
	        String moduleRelativeURL = GWT.getModuleBaseURL() + "SearchService";
	        endpoint.setServiceEntryPoint(moduleRelativeURL);
		}
		final CallbackActions actions = act;
		// Create an asynchronous callback to handle the result.

		ApplicationCallback callback = new ApplicationCallback() {

			public void onFailure(Throwable caught) {
				onFailureBefore(caught);

				if (caught instanceof AuthenticationException) {
					LoginDialog.getLoginDialog(caught.getMessage(), this, false);
				}

				onFailureAfter(caught);
			}
			
			  public void onFailureAfter(Throwable arg0) {                 
				  actions.onFailure(arg0);
		         }
		      
		         public void onSuccessAfter(Object result) { 
		        	 actions.onSuccess(result);
		         }
		      
		         public void authenticateAndExecute() throws AuthenticationException {   
		        	 actions.execute(this);             
		         }
		         
		     	public void execute() {
		    		try {
		    			authenticateAndExecute();
		    		} catch (AuthenticationException ex) {
		    			LoginDialog.getLoginDialog(ex.getMessage(), this, false);
		    		}
		    	}
		         
			};

		// Make the call
			callback.execute();
//		actions.execute(callback);
	}

	public static void makeUnsecuredCall(CallbackActions act) {
		if (searchService == null) {
			// Initiate connection to server
			searchService = (SearchServiceAsync) GWT.create(SearchService.class);
	        ServiceDefTarget endpoint = (ServiceDefTarget) searchService;
	        String moduleRelativeURL = GWT.getModuleBaseURL() + "SearchService";
	        endpoint.setServiceEntryPoint(moduleRelativeURL);
		}
		final CallbackActions actions = act;
		// Create an asynchronous callback to handle the result.

		AsyncCallback callback = new AsyncCallback() {


				public void onFailure(Throwable caught) {
					actions.onFailure(caught);				
				}

				public void onSuccess(Object result) {
					 actions.onSuccess(result);
					
				}
		         
			};

		// Make the call
		actions.execute(callback);
	}
}
