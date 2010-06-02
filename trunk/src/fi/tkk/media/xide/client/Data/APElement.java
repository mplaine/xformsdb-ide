package fi.tkk.media.xide.client.Data;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.Tabs.ExplorerTreeItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TreeItem;

import fi.tkk.media.xide.client.UI.Widget.AdvancedTableV2;
import fi.tkk.media.xide.client.UI.Widget.ContextMenu;
import fi.tkk.media.xide.client.utils.ActionWithText;
import fi.tkk.media.xide.client.utils.ActionWithTextAndIcon;
import fi.tkk.media.xide.client.utils.ConnectionToServer;
import fi.tkk.media.xide.client.fs.XIDEFile;
import fi.tkk.media.xide.client.fs.XIDEFolder;
import fi.tkk.media.xide.client.popups.PopupCreateNewApplicationSelectOption;
import fi.tkk.media.xide.client.popups.basic.Popup;
import fi.tkk.media.xide.client.popups.utils.interfaces.Action;
import fi.tkk.media.xide.client.utils.Icons;
import fi.tkk.media.xide.client.utils.ConnectionToServer.CallbackActions;

public class APElement implements HasDisplayableProperties, Serializable, IsSerializable{
	public APElement parent;
	transient private TreeItem item;


	public static final String ROOT_ELEMENT_TITLE = "All applications";
	
	public static final int ACTION_EDIT 	= 0;
	public static final int ACTION_PUBLISH 	= 1;
	public static final int ACTION_ADD 		= 2;
	public static final int ACTION_DELETE	= 3;
	
	public static final int ACTION_NUMBER = 4;
	
	// Element's properties
	public HashMap<String, Property> properties;
	
	// Element's type
	protected int type;
	
	// Folder Structure: root folder
	XIDEFolder rootFolder;
	
	transient protected AccessRights accessRights;

	ArrayList<APElement> children;
	
	transient static ConnectionToServer connectionToServer;
	
	// List of actions for this element type
	// For all APElements the list has ACTION_NUMBER possible actions ( currently Edit, Publish, Create, Delete)
	// If element does not have an action, it has null on the corresponding place
	private transient static ActionWithTextAndIcon[] actionListForAPE;
	
	public static final int ALL = 0;
	public static final int APPLICATION = 1;
	public static final int WPAGE = 2;
	
//	public static final String PAGEICON = "elementIcons/page_16 copy.gif";
//	public static final String APPICON = "elementIcons/application_16 copy.gif";
//	public static final String ALLICON = "elementIcons/home.gif";
	public static final String PUBLISHED = "operationIcons/applications/application_publish_16 copy.gif";
	
	public static final String DATE_PUB = "Date published";
	public static final String DATE_CR = "Date created";
	public static final String DATE_MOD = "Date modified";
	public static final String TEMPLATES = "Templates used";
	public static final String ISPUBLISHED = "Is published";
	
	
	public APElement cloneIt() {
		// Clone properties hash map
		HashMap<String, Property> propertiesCopy = new HashMap<String, Property>();
		Set<String> keys = properties.keySet();

		for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
			String o = iterator.next();
			propertiesCopy.put(o, properties.get(o).clone());
		}
		
		APElement element =  new APElement(this.getTitle(), propertiesCopy, this.parent);
		element.accessRights = accessRights;
		element.children = this.children;
		return element;
	}
	
	protected HashMap<String, Property> fakeProperties(String title) {
		HashMap<String, Property> properties = new HashMap<String, Property>();
		
		Date d = new Date(109, 1, 1 + ((int)(Math.random()*10)), 10, 10, 10);
		properties.put(Property.ID, new BaseProperty("ID", "id_" + title, true) );
		properties.put(Property.TITLE, new BaseProperty("Title", title, true) );
		properties.put(Property.DESCR, new BaseProperty("Descr", "fake descr", true) );
		
		properties.put(Property.DATE_CR, new BaseProperty(DATE_CR, AdvancedTableV2.date(d), false));
		properties.put(Property.DATE_MOD, new BaseProperty(DATE_MOD, AdvancedTableV2.date(d), false));
		properties.put(Property.DATE_PUB, new BaseProperty(DATE_PUB, AdvancedTableV2.date(d), false));
		
		properties.put (Property.RELATED_URL, new BaseProperty("Name for publishing", "", true)) ;
		
		if ((parent != null) && (parent.properties != null) && parent.properties.get(Property.IS_PUBLISHED)!=null) {
			properties.put(Property.IS_PUBLISHED, new BaseProperty(ISPUBLISHED, parent.properties.get(Property.IS_PUBLISHED).getStringValue(), false) );
		}
		else {
			if (d.getDay() > 3) {
				properties.put(Property.IS_PUBLISHED, new BaseProperty(ISPUBLISHED, "true", false)) ;
				
			}
			else {
				properties.put(Property.IS_PUBLISHED, new BaseProperty(ISPUBLISHED, "false", false)) ;
			}
		}		
		
		return properties;
	}
	
	/**
	 * Used as a constructor when root element is constructed
	 */
	public APElement() {
		// Does not have parrent
		parent = null;
		// Does not have properties
		properties = null;
		// Does  not have file structure
		this.rootFolder = null;
		// Has type ALL which indicates that this is not application or page
		this.type = ALL;
		// Has children (all applications of the current user)
		children = new ArrayList<APElement>();
		
	}
	
//	public APElement(String title,  APElement parent, XIDEFolder rootFolder) {
//		this.parent = parent;
//		properties = null;
//		this.rootFolder = rootFolder;
////		properties = fakeProperties(title);
//		this.type = ALL;
//		children = new ArrayList<APElement>();
//		}

	/**
	 * Used as a constructor for AP Element clone operation
	 */
	public APElement(String title,  HashMap<String, Property> properties, APElement parent) {
		this.parent = parent;
		this.properties = properties;
		this.rootFolder = null;
		this.type = ALL;
		children = new ArrayList<APElement>();
		}
	
	public static void createActionList() {
		
			actionListForAPE = new ActionWithTextAndIcon[ACTION_NUMBER];
			actionListForAPE[ACTION_EDIT] = null;
			actionListForAPE[ACTION_PUBLISH] = null;
			
			ActionWithTextAndIcon action = new ActionWithTextAndIcon("Add new application", Icons.ICON_APP_ADD, 16,
					new Action() {
						public void doAction() {
							// Add application popup
							new PopupCreateNewApplicationSelectOption();
						}});
			actionListForAPE[ACTION_ADD] = action;
			
			action = new ActionWithTextAndIcon("Delete all aplications", Icons.DELETE, 11,
					new Action() {

						public void doAction() {
							new Popup("This is not implemented yet!");
						}});
			actionListForAPE[ACTION_DELETE] = action;
	}
	
	public ActionWithTextAndIcon[] getContextMenuItems(){
		if (actionListForAPE == null) {
			createActionList();
		}
		return actionListForAPE;
	}
	
	public String getTypeName() {
		return "basic element";
	}
	
	public Image getImage() {
		return new Image(Icons.ICON_ALL_APPS);
	}
	
	public String getTitle() {
		return ROOT_ELEMENT_TITLE;
	}
	
	public void setPublishedState(boolean isPublished) {
//		getProperties().get(Property.DATE_PUB).setValue(AdvancedTableV1.dateFullFormat());
		if (isPublished) {
			getProperties().get(Property.IS_PUBLISHED).setValue("true");
		}
		else {
			getProperties().get(Property.IS_PUBLISHED).setValue("false");
		} 
		// Publishing children: 
//		for (Iterator<APElement> iterator = getChildren().iterator(); iterator.hasNext();) {
//			APElement o =  iterator.next();
//			o.getProperties().get(Property.DATE_PUB).setValue(AdvancedTableV1.date());
//			if (isPublished) {
//				o.getProperties().get(Property.IS_PUBLISHED).setValue("true");
//			}
//			else {
//				getProperties().get(Property.IS_PUBLISHED).setValue("false");
//			} 
//		}

	}
	
	public AccessRights getAccessRights() {
		return accessRights;
	}
	
	public void beforeDeletion() {
		if (parent != null) {
			parent.children.remove(this);
		}
	}
	
	/**
	 * Removes element from parent
	 */
	public void removeFromParent() {
		if (parent != null) {
			parent.children.remove(this);
		}
	}
	
	public void delete() {
		deleteChildren();
		if (parent != null) {
			parent.children.remove(this);
		}
		// If item is attached to the tree
		//If it is attached to CAP the panel maintains deletion itself
//		if (item != null) {
//			item.remove();
//		}
	}
	
	public void deleteChildren() {
		APElement[] childrenArray = children.toArray(new APElement[children.size()]);
		for(int i = 0; i < childrenArray.length; i++) {
			childrenArray[i].delete();
		}
	}
	
	public void addChild(APElement child) {
		children.add(child);
	}
	
	public void deleteChild(APElement child) {
		children.remove(child);
	}
	
	public ArrayList<APElement> getChildren() {
		return children;
	}
	
	// These getters do not return anything
	public LinkedHashMap<String, XIDEFile> getCSS() {
		return null;
	}
	public LinkedHashMap<String, XIDEFile> getResources() {
		return null;
	}

	public LinkedHashMap<String, XIDEFile> getDataInstances() {
		return null;
	}
	public HashMap<String, Property> getParameters() {
		return null;
	}
	public LinkedHashMap<String, XIDEFile> getQueries() {
		return null;
	}
	public LinkedHashMap<String, XIDEFile> getSourceCode() {
		return null;
	}
	
	// Working getters
	public HashMap<String, Property> getProperties() {
		return properties;
	}

	public int getType() {
		return type;
	}
	
	public void setItem(TreeItem item) {
		this.item = item;
	}

	public void setCSS(String css) {
		// TODO Auto-generated method stub
		
	}

	public void setDataInstances(LinkedHashMap<String, XIDEFile> di) {
		// TODO Auto-generated method stub
		
	}

	public void setParameters(Property[] parameters) {
		// TODO Auto-generated method stub
		
	}

	public void setProperties(Property[] properties) {
		// TODO Auto-generated method stub
		
	}

	public void setQueries(LinkedHashMap<String, XIDEFile> queries) {
		// TODO Auto-generated method stub
		
	}

	public void setSourceCode(LinkedHashMap<String, XIDEFile> sc) {
		// TODO Auto-generated method stub
		
	}

	public LinkedHashMap<String, XIDEFile> getDB() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setCSS(LinkedHashMap<String, XIDEFile> css) {
		// TODO Auto-generated method stub
		
	}

	public XIDEFolder getRootFolder() {
		return rootFolder;
	}

	// Again not working setters

	/**
	 * Used when AP element needs to be updated on the server because it has changed:
	 * - Page/application properties has been modified
	 */
	public void updateAPElement(final boolean doUpdateUI) {
		ConnectionToServer.makeACall(new CallbackActions() {

			public void execute(AsyncCallback callback) {
				ConnectionToServer.searchService.updateAPElementInfo(APElement.this.getProperties(), APElement.this.getType(), callback);
			}
			public void onFailure(Throwable caught) {
				
			}

			public void onSuccess(Object result) {
//				System.out.println("properties should be updated");
				Template.updateTemplateProperties(APElement.this.getProperties(), result);
				// If this element is a web page
				if (APElement.this.getType() == APElement.WPAGE) {
					// Parent application needs to have a modified date property updated
					APElement page = (APElementPage) APElement.this;
					page.parent.updateModifiedDateFromChild(page);
				}
				Main.getInstance();
				Main.getCurrentView().updatePropertiesValues();
				
				if (doUpdateUI) {
					Main.getInstance().UpdateUI(Main.MAIN_TAB);
					Main.getInstance().UpdateUI(Main.RIGHT_TAB);
				}
			}});
	}

	/**
	 * Used when AP element modified date needs to be updated because of external events:
	 * - File management
	 * - For application: page changes
	 */
	public void updateAPElementModifiedDate(final boolean doUpdateUI) {
		ConnectionToServer.makeACall(new CallbackActions() {
			
			public void execute(AsyncCallback callback) {
				ConnectionToServer.searchService.updateAPElementModifiedDate(APElement.this.getProperties().get(Property.ID).getStringValue(),
						APElement.this.getType(), callback);
			}
			public void onFailure(Throwable caught) {
				
			}
			
			public void onSuccess(Object result) {
//				System.out.println("properties should be updated");
				Template.updateTemplateProperties(APElement.this.getProperties(), result);
				// If this element is a web page
				if (APElement.this.getType() == APElement.WPAGE) {
					// Parent application needs to have a modified date property updated
					APElement page = (APElementPage) APElement.this;
					page.parent.updateModifiedDateFromChild(page);
				}
				
				Main.getInstance();
				Main.getCurrentView().updatePropertiesValues();
				
				if (doUpdateUI) {
					Main.getInstance().UpdateUI(Main.MAIN_TAB);
					Main.getInstance().UpdateUI(Main.RIGHT_TAB);
				}
			}});
	}

	/**
	 * Copies Modified Date property value from element to this element. 
	 * Used when current APElement modified date needs to be updated according to the modified date of the given element (child page).
	 * 
	 * @param child
	 */
	public void updateModifiedDateFromChild(APElement child) {
		((DateProperty)getProperties().get(Property.DATE_MOD)).setValue(
				(Date)((DateProperty)child.getProperties().get(Property.DATE_MOD)).getValue(), 
				child.getProperties().get(Property.DATE_MOD).getStringValue());
	}


}
