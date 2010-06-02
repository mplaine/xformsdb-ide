package fi.tkk.media.xide.client.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.Tabs.ExplorerTreeItem;
import fi.tkk.media.xide.client.fs.XIDEFolder;
import fi.tkk.media.xide.client.popups.PopupApplicationPublishing;
import fi.tkk.media.xide.client.popups.PopupApplicationSuccessfullyPublished;
import fi.tkk.media.xide.client.popups.PopupCreateNewPage;
import fi.tkk.media.xide.client.popups.basic.LoadingPopup;
import fi.tkk.media.xide.client.popups.basic.Popup;
import fi.tkk.media.xide.client.popups.basic.PopupAreYouSure;
import fi.tkk.media.xide.client.popups.basic.PopupError;
import fi.tkk.media.xide.client.popups.utils.interfaces.Action;
import fi.tkk.media.xide.client.utils.ActionWithText;
import fi.tkk.media.xide.client.utils.ActionWithTextAndIcon;
import fi.tkk.media.xide.client.utils.ConnectionToServer;
import fi.tkk.media.xide.client.utils.Icons;
import fi.tkk.media.xide.client.utils.ToggleActionWithTextAndAction;
import fi.tkk.media.xide.client.utils.ConnectionToServer.CallbackActions;

public class APElementApplication extends APElement {
	

	// List of actions for this element type
	// For all APElements the list has 4 possible actions (Edit, Publish, Create, Delete)
	// If element does not have an action, it has null on the corresponding place
	protected transient static ActionWithTextAndIcon[] actionListForAPEApp;
	
	public APElementApplication() {}
//	public APElementApplication(String title, APElement p) {
//		parent = p;
//		if (parent != null) {
//			parent.addChild(this);
//		}
//		properties = fakeProperties(title);
//
//		this.type = APPLICATION;
//		children = new ArrayList<APElement>();
//
//		accessRights = new AccessRights();
//		accessRights.setFakeAccessRights();
//	}
	
	public APElementApplication(HashMap<String, Property> properties, APElement p, XIDEFolder rootFolder) {
		parent = p;
		if (parent != null) {
			parent.addChild(this);
		}
		this.properties = properties;
		this.rootFolder = rootFolder;
		this.type = APPLICATION;
		children = new ArrayList<APElement>();
		accessRights = new AccessRights();
		accessRights.setFakeAccessRights();
	}
	
	public APElementApplication cloneIt() {
		// Clone properties hash map
		HashMap<String, Property> propertiesCopy = new HashMap<String, Property>();
		Set<String> keys = properties.keySet();

		for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
			String o = iterator.next();
			propertiesCopy.put(o, properties.get(o).clone());
		}
		
		APElementApplication element =  new APElementApplication(propertiesCopy, this.parent, this.rootFolder);
		element.accessRights = accessRights;
		element.children = this.children;
		return element;
	}
	
	public static void createActionList() {
		actionListForAPEApp = new ActionWithTextAndIcon[ACTION_NUMBER];
		actionListForAPEApp[ACTION_EDIT] = null;
		
		ActionWithTextAndIcon action;
		ActionWithTextAndIcon action1 = new ActionWithTextAndIcon("Publish this application", Icons.ICON_APP_PUBLISHED, 13,
				new Action() {
				
									public void doAction() {
										new PopupApplicationPublishing(
												(APElementApplication)
												((APElementHolder)Main.getInstance().getSelectedElement()).getAPElement())		
										;
//										(new ModalDimmedPopup("This is not implemented yet!")).showPopup(false);
									}});
		ActionWithTextAndIcon action2= new ActionWithTextAndIcon("Unpublish this application", Icons.ICON_APP_NOT_PUBLISHED, 10,
				new Action() {
			
			public void doAction() {
				new PopupAreYouSure("You are going to unpublish the application. Are you sure?", new ClickListener() {
					public void onClick(Widget sender) {
						unpublishApplication(
								(APElementApplication)
								((APElementHolder)Main.getInstance().getSelectedElement()).getAPElement());
					}
					
				},null);
			}});
		action = new ToggleActionWithTextAndAction(action1, action2);
//		action = new ActionWithTextAndIcon("Publish this application", Icons.ICON_APP_NOT_PUBLISHED, 13,
//				new Action() {
//
//					public void doAction() {
//						(new PopupApplicationPublishing(
//								(APElementApplication)((ExplorerTreeItem)Main.getInstance().getSelectedElement()).element)		
//						).showPopup(true);
////						(new ModalDimmedPopup("This is not implemented yet!")).showPopup(false);
//					}});
		actionListForAPEApp[ACTION_PUBLISH] = action;

		 action = new ActionWithTextAndIcon("Add new page", Icons.ICON_PAGE_ADD, 14,
				new Action() {
					public void doAction() {
						new PopupCreateNewPage( 
					(APElementApplication)((APElementHolder)Main.getInstance().getSelectedElement()).getAPElement());
					}});
		actionListForAPEApp[ACTION_ADD] = action;
		

		action = new ActionWithTextAndIcon("Delete this application", Icons.ICON_APP_DELETE, 15,
				new Action() {

					public void doAction() {
						new PopupAreYouSure("You are going to delete an object. Are you sure?", new ClickListener() {
							public void onClick(Widget sender) {
								((APElementApplication)((APElementHolder)Main.getInstance().getSelectedElement()).getAPElement()).delete();
								Main.getInstance().UpdateUI();
							}
						}, null);
//						(new ModalDimmedPopup("This is not implemented yet!")).showPopup(false);
					}});
		actionListForAPEApp[ACTION_DELETE] = action;

	}
	
	public String getTypeName() {
		return "application";
	}
	
	public ActionWithTextAndIcon[] getContextMenuItems(){
		if (actionListForAPEApp == null) {
			createActionList();
		}
//		return actionListForAPEApp;
		return new ActionWithTextAndIcon[0];
	}
	
//	public ActionWithText[] getContextMenuItems(){
//		ActionWithText[] result = new ActionWithText[4];
//		
//		ActionWithText action = new ActionWithText("Add new page",
//				new Action() {
//					public void doAction() {
//						(new PopupCreateNewPageFromScratch(null, APElementApplication.this)).showPopup(true);
//					}});
//		result[0] = action;
//		
//		action = new ActionWithText("Publish this application", 
//				new Action() {
//
//					public void doAction() {
//						(new PopupApplicationPublishing(APElementApplication.this)).showPopup(true);
////						(new ModalDimmedPopup("This is not implemented yet!")).showPopup(false);
//					}});
//		result[1] = action;
//
//		action = new ActionWithText("Delete this application", 
//				new Action() {
//
//					public void doAction() {
//						(new PopupAreYouSure("You are going to delete an object. Are you sure?", new ClickListener() {
//							public void onClick(Widget sender) {
//								APElementApplication.this.delete();
//								Main.getInstance().UpdateUI();
//							}
//						}, null)).showPopup(true);
////						(new ModalDimmedPopup("This is not implemented yet!")).showPopup(false);
//					}});
//		result[2] = action;
//
//		action = new ActionWithText("Delete all pages from this application", 
//				new Action() {
//
//					public void doAction() {
//						(new PopupAreYouSure("You are going to delete several objects. Are you sure?", new ClickListener() {
//							public void onClick(Widget sender) {
//								APElementApplication.this.deleteChildren();
//								Main.getInstance().UpdateUI();
//							}
//						}, null)).showPopup(true);
//
////						(new ModalDimmedPopup("This is not implemented yet!")).showPopup(false);
//					}});
//		result[3] = action;
//
//		return result;
//	}
	
	public static void unpublishApplication(final APElementApplication element) {
		ConnectionToServer.makeACall(new CallbackActions() {

			public void execute(AsyncCallback callback) {
				ConnectionToServer.searchService.unpublishApplication((APElementApplication)element, true, callback);
			}

			public void onFailure(Throwable caught) {
				new PopupError("Unfortunately application was not unpublished! ", caught.getMessage());
			}

			public void onSuccess(Object result) {
				Template.updateTemplateProperties(element.properties, result);
				Main.getInstance().UpdateUI(Main.MAIN_TAB);
				Main.getInstance().UpdateUI(Main.RIGHT_TAB);

				new Popup("Application is successfully unpublished! ");
//				element.getProperties().get(Property.DATE_PUB).setValue("0000-00-00 00:00:00");
//				element.getProperties().get(Property.IS_PUBLISHED).setValue("false");
//				element.getProperties().get(Property.URL).setValue("");
//				element.setPublishedState(false);
//				Main.getInstance().UpdateUI();
			}});
	}
	
//	public static void republishApplication(final APElementApplication element, final boolean doReloadDB) {
//		ConnectionToServer.makeACall(new CallbackActions() {
//
//			public void execute(AsyncCallback callback) {
//				ConnectionToServer.searchService.republishApplication((APElementApplication)element, doReloadDB, callback);
//			}
//
//			public void onFailure(Throwable caught) {
//				new Popup("Unfortunately application was not republished! See error frm the server: " + caught.getMessage());
//			}
//
//			public void onSuccess(Object result) {
//				Template.updateTemplateProperties(element.properties, result);
//				Main.getInstance().UpdateUI(Main.MAIN_TAB);
//				Main.getInstance().UpdateUI(Main.RIGHT_TAB);
//				new Popup("Application is successfully republished! ");
//			}});
//	}
	
	public void playStopApplication(final boolean doPlay) {
		ConnectionToServer.makeACall(new CallbackActions() {

			public void execute(AsyncCallback callback) {
				if (doPlay) {
					ConnectionToServer.searchService.playApplication(APElementApplication.this, callback);
				}
				
				else {
					ConnectionToServer.searchService.stopApplication(APElementApplication.this, callback);
				}
				LoadingPopup.showDimmed();
			}

			public void onFailure(Throwable caught) {
				LoadingPopup.hideDimmed();
				if (doPlay) {
					new PopupError("Unfortunately the Application publishing has finished with error! ", caught.getMessage());
				}
				else {
					new PopupError("Unfortunately the Application unpublishing has finished with error!" , caught.getMessage());
				}			}

			public void onSuccess(Object result) {
				LoadingPopup.hideDimmed();
				onPublished(result);
				if (doPlay) {
					// Show popup, informing that the application is now published
					new PopupApplicationSuccessfullyPublished(properties.get(Property.URL).getStringValue());
				}
				else {
					new Popup("Application is successfully stopped! ");
				}
			}});
	}
	public Image getImage() {
		return new Image(Icons.ICON_APP);
	}
	
	public String getTitle() {
		return properties.get(Property.TITLE).getStringValue();
	}
	public void delete() {
//		if (connectionToServer == null) {
//			connectionToServer= new ConnectionToServer();
//		}
		ConnectionToServer.makeACall(new CallbackActions() {
		
				public void execute(AsyncCallback callback) {
					ConnectionToServer.searchService.deleteApplication(properties.get(Property.ID).getStringValue()
							, callback);
					LoadingPopup.showDimmed();
				}
		
				public void onFailure(Throwable caught) {
					LoadingPopup.hideDimmed();
					new PopupError("Unfortunately application was not deleted!",  caught.getMessage());
				}
		
				public void onSuccess(Object result) {
					LoadingPopup.hideDimmed();
					new Popup("Application was successfully deleted!"); 
					Main.getInstance().onInternalViewChanged(Main.AP);
//					APElementApplication.super.delete();
				}});
	}
	
	/**
	 * Is called when application is successfully published or republished online
	 * @param result Object representing answer from server. Should be a HashMap<String, Property> of the application properties which should be changed
	 */
	public void onPublished(Object result) {
		// Update properties
		Template.updateTemplateProperties(this.properties, result);
		// Update UI
		Main.getInstance().UpdateUI(Main.MAIN_TAB);
		Main.getInstance().UpdateUI(Main.RIGHT_TAB);

	}
	
	public void updateMainPagePropertyOptions() {
		ConnectionToServer.makeACall(new CallbackActions() {
			
			public void execute(AsyncCallback callback) {
				ConnectionToServer.searchService.getPageList(properties.get(Property.ID).getStringValue()
						, callback);
			}
	
			public void onFailure(Throwable caught) {
			}
	
			public void onSuccess(Object result) {
				
				APElementApplication.this.getProperties().remove(Property.MAIN_PAGE);
				APElementApplication.this.getProperties().put(Property.MAIN_PAGE, (OptionProperty)result);
				
				// If Application is now selected, then the properties should be updated imidiately
				Main.getCurrentView().updatePropertiesListIfSelected(APElementApplication.this);
//				Main.getCurrentView().updateProperties();
			}});
	}
	
}
