package fi.tkk.media.xide.client.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.Tabs.ExplorerTreeItem;
import fi.tkk.media.xide.client.fs.XIDEFolder;
import fi.tkk.media.xide.client.popups.basic.Popup;
import fi.tkk.media.xide.client.popups.basic.PopupAreYouSure;
import fi.tkk.media.xide.client.popups.basic.PopupError;
import fi.tkk.media.xide.client.popups.utils.interfaces.Action;
import fi.tkk.media.xide.client.utils.ActionWithText;
import fi.tkk.media.xide.client.utils.ActionWithTextAndIcon;
import fi.tkk.media.xide.client.utils.ConnectionToServer;
import fi.tkk.media.xide.client.utils.Icons;
import fi.tkk.media.xide.client.utils.ConnectionToServer.CallbackActions;

public class APElementPage extends APElement {
	public static final int ID 					= 0;
	public static final int TITLE 				= 1;
	public static final int DESCRIPTION 		= 2;
	public static final int NAME_PUBLISHING		= 3;
	public static final int DATE_CREATED		= 4;
	public static final int DATE_MODIFIED 		= 5;
	
	public static final String[] TITLES = {		"ID", 						//0
												"Title", 					//1	
												"Description", 				//2
												"Name for publishing",		//3
												"Date of creation",			//4
												"Date of modification",		//5
												};		
	public static final String[] DESCRIPTIONS = {	"", 			//0
												"",					//1
												"",					//2
												"",					//3	
												"",					//4
												""					//5	
												};
	
	// List of actions for this element type
	// For all APElements the list has 4 possible actions (Edit, Publish, Create, Delete)
	// If element does not have an action, it has null on the corresponding place
	protected transient static ActionWithTextAndIcon[] actionListForAPEPage;

	
	public APElementPage() {}
	
//	public APElementPage(String title, APElement p) {
//		parent = p;
//		if (parent != null) {
//			parent.addChild(this);
//		}
//		properties = fakeProperties(title);
//		this.type = WPAGE;
//		children = new ArrayList<APElement>();
//	}
	
	public APElementPage( HashMap<String, Property> properties, APElement p, XIDEFolder rootFolder) {
		parent = p;
		if (parent != null) {
			parent.addChild(this);
		}
		this.rootFolder = rootFolder;
		this.properties = properties;
		this.type = WPAGE;
		children = new ArrayList<APElement>();
	}
	
	public APElementPage cloneIt() {
		// Clone properties hash map
		HashMap<String, Property> propertiesCopy = new HashMap<String, Property>();
		Set<String> keys = properties.keySet();

		for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
			String o = iterator.next();
			propertiesCopy.put(o, properties.get(o).clone());
		}
		
		APElementPage element =  new APElementPage(propertiesCopy, this.parent, this.rootFolder);
		element.accessRights = accessRights;
		element.children = this.children;
		return element;
	}
	
	public String getTypeName() {
		return "web page";
	}
	
	public static void createActionList() {
		actionListForAPEPage = new ActionWithTextAndIcon[ACTION_NUMBER];
		
		ActionWithTextAndIcon action = new ActionWithTextAndIcon("Edit this page", Icons.ICON_PAGE_EDIT, 12,
				new Action() {

					public void doAction() {
						
						Main.getInstance().setLoadedPageAPElement( ((APElementPage)((APElementHolder)Main.getInstance().getSelectedElement()).getAPElement()));
						Main.getInstance().onInternalViewChanged(Main.PP, new Action() {

							public void doAction() {
								// TODO Auto-generated method stub
								Main.getInstance().setSelectedElement(null);
							}});
						
//						(new ModalDimmedPopup("This is not implemented yet!")).showPopup(false);
					}});
		actionListForAPEPage[ACTION_EDIT] = action;
		actionListForAPEPage[ACTION_PUBLISH] = null;
		actionListForAPEPage[ACTION_ADD] = null;
		 action = new ActionWithTextAndIcon("Delete this page", Icons.ICON_PAGE_DELETE, 7,
				new Action() {

					public void doAction() {
						new PopupAreYouSure("You are going to delete an object. Are you sure?", new ClickListener() {
							public void onClick(Widget sender) {
								((APElementHolder)Main.getInstance().getSelectedElement()).onDelete();
								Main.getInstance().UpdateUI();
							}
						}, null);
//						(new ModalDimmedPopup("This is not implemented yet!")).showPopup(false);
					}
					});
		actionListForAPEPage[ACTION_DELETE] = action;

		

		
	}
	
	public ActionWithTextAndIcon[] getContextMenuItems(){
		if (actionListForAPEPage == null) {
			createActionList();
		}
		return actionListForAPEPage;
	}
	
//	public ActionWithText[] getContextMenuItems(){
//		return result;
//	}
	
	public Image getImage() {
		return new Image(Icons.ICON_PAGE);
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
					ConnectionToServer.searchService.deletePage(properties.get(Property.ID).getStringValue()
							, callback);
				}
		
				public void onFailure(Throwable caught) {
					new PopupError("Unfortunately page was not deleted!",  caught.getMessage());
				}
		
				public void onSuccess(Object result) {
					new Popup("Page was successfully deleted! ");
					APElementPage.super.delete();
					Main.getInstance().setSelectedElement(null);

					// Update parent element (date and main page properties)
					APElementApplication parentElement= ((APElementApplication)APElementPage.this.parent);
					Template.updateTemplateProperties(parentElement.getProperties(), result);
					Main.getCurrentView().updatePropertiesListIfSelected(parentElement);
//					((APElementApplication)APElementPage.this.parent).updateMainPagePropertyOptions();
					
				}});
	}
	
	public XIDEFolder getRootFolder() {
		return parent.rootFolder;
	}
}
