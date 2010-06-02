package fi.tkk.media.xide.client.fs;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.Data.HasDisplayableProperties;
import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.popups.basic.Popup;
import fi.tkk.media.xide.client.popups.basic.PopupAreYouSure;
import fi.tkk.media.xide.client.popups.basic.PopupError;
import fi.tkk.media.xide.client.popups.utils.interfaces.Action;
import fi.tkk.media.xide.client.utils.ActionWithText;
import fi.tkk.media.xide.client.utils.ActionWithTextAndIcon;
import fi.tkk.media.xide.client.utils.ConnectionToServer;
import fi.tkk.media.xide.client.utils.Icons;
import fi.tkk.media.xide.client.utils.ConnectionToServer.CallbackActions;
import fi.tkk.media.xide.server.Config;

public class XIDEFile implements Serializable, FileStructureElement{
	
	// File types
	public static final String CSS_FILE = "CSS";
	public static final String DI_FILE = "Data Instance";
	public static final String DATA_FILE = "Initial XML Data";
	public static final String SOURCE_FILE = "Source Code";
	public static final String QUERY_FILE = "Query";
	public static final String RESOURCE_FILE = "Resources";
	
	
	// Validation types
	// Currently this type is set only for source codes files (in constructor of Template and WebPage classes)
	// It is assumed that there is only one source code file
	public static final int NO_VALIDATION = 0;
	public static final int VALIDATE_TEMPLATE_SOURCE = 1;
	public static final int VALIDATE_PAGE_SOURCE = 2;
	
	
	// Constants which indicates who is the owner of this file
	// Is usually used for pages: they can have their own files (stored in page-specific folders)
	// or common files (can be used by all applciation's pages)
	public static final int FILE_OWNER_NOT_DEF = 0;
	public static final int FILE_COMMON = 1;
	public static final int FILE_PRIVATE = 2; 
	
	// related path on the server
	String relatedServerPath;
	// absolute path on the server
	String absoluteServerPath;
	String name;
	
	int fileOwner;
	
	int validationType;
	
//	ArrayList<HasText> listOfUIObjects;
	transient HasText linkedUIObject;
	
	XIDEFolder parentFolder;
	
	String fileContent = null;
	
	public boolean hasChanged = false;
	
	transient static ConnectionToServer connectionToServer ;//= new ConnectionToServer();
	
	public XIDEFile() {
	//	getContent();
	}
	
	public XIDEFile(String name, XIDEFolder parentFolder) {
		this.absoluteServerPath = parentFolder.getAbsolutePath() + name;
		this.name = name;
		this.parentFolder = parentFolder;
		
		this.fileContent = null;
		this.validationType = NO_VALIDATION;
		// Add folder into parent's children list
		this.parentFolder.addChild(this);

		fileOwner = FILE_OWNER_NOT_DEF;
	}
	
	/**
	 * Used to create a file with owner type
	 * 
	 * @param name
	 * @param parentFolder
	 * @param fileOwner
	 */
	public XIDEFile(String name, XIDEFolder parentFolder, int fileOwner) {
		this(name, parentFolder);
		this.fileOwner = fileOwner;
	}
	
	public void setValidationType(int type) {
		validationType = type;
	}
	
	public int getValidationType() {
		return validationType;
	}
	
	public String getName() {
		return name;
	}

	public int getFileOwnerType() {
		return fileOwner;
	}
	/**
	 * Gets the content of file or null if there is no content. This is used on the server to get the value of the file.
	 * @return content
	 */
	public String getFileContentValue() {
		
		return fileContent;
	}
	
	
	/**
	 * Gets the content of file. If there is no file content then it requests it form server.
	 * Client method only. 
	 * @return
	 */
	public String getContent() {
		if (fileContent == null) {
			getContentFromServer();
		}
		return fileContent;
	}

	public String getContent(Action action) {
		if (fileContent == null) {
			// Get file content
			// Action will be executed when the answer has received
			getContentFromServer(action);
		}
		else {
			// File text already exist
			// Execute the action
			action.doAction();
		}
		return fileContent;
	}
	
	public void setLinkedUIObject(HasText object) {
		this.linkedUIObject = object;
	}
	
	public void getContentFromServer() {

		ConnectionToServer.makeACall(new CallbackActions() {

		public void execute(AsyncCallback callback) {
			connectionToServer.searchService.getFileContent(absoluteServerPath, callback);
		}

		public void onFailure(Throwable caught) {
//			System.out.println("XIDEFile:Error from getFileValue from server method!");
			
		}

		public void onSuccess(Object result) {
			if (result instanceof String) {
				fileContent = (String) result;
				if (linkedUIObject != null){
					linkedUIObject.setText(fileContent);
				}
////				for (Iterator<HasText> iterator = listOfUIObjects.iterator(); iterator.hasNext();) {
//					HasText UIObject = iterator.next();
//					UIObject.setText(fileContent);
//				}
				
			}
		}});
	}
	
	public void getContentFromServer(final Action action) {

		ConnectionToServer.makeACall(new CallbackActions() {

		public void execute(AsyncCallback callback) {
			ConnectionToServer.searchService.getFileContent(absoluteServerPath, callback);
		}

		public void onFailure(Throwable caught) {
//			System.out.println("XIDEFile: Error from getFileValue from server method!");
			
		}

		public void onSuccess(Object result) {
			if (result instanceof String) {
				fileContent = (String) result;
				if (action != null){
					action.doAction();
				}
////				for (Iterator<HasText> iterator = listOfUIObjects.iterator(); iterator.hasNext();) {
//					HasText UIObject = iterator.next();
//					UIObject.setText(fileContent);
//				}
				
			}
		}});
	}
	
	/**
	 * Checks whether the file has changed and sends it to the server preview directory if necessary 
	 * @return
	 */
	public boolean updateContentToServerTemp() {
		if (hasChanged) {
			updateContentToServerForPreview(absoluteServerPath);
			return true;
		}
		else {
			return false;
		}
	}
	
	public void updateContent ( String newContent) {
		fileContent = newContent;
		hasChanged = true;
	}
	
	public void updateContentToServer(boolean isSet, boolean doValidate) {
		if (hasChanged) {
			updateContentToServer(isSet, absoluteServerPath, doValidate);
		}
	}

	public void updateContentToServer(final boolean isSet, final String absoluteServerPath, final boolean doValidate) {
		
		ConnectionToServer.makeACall(new CallbackActions() {

			public void execute(AsyncCallback callback) {
				ConnectionToServer.searchService.updateFileContent(isSet, doValidate, absoluteServerPath, fileContent, validationType, callback);
			}

			public void onFailure(Throwable caught) {
				new PopupError("Unfortunately saving has failed!" ,  caught.getMessage());
//				System.out.println("XIDEFile: Error from getFileValue from server method!");
				
			}

			public void onSuccess(Object result) {
				if (isSet) {
					// File is send as a part of a set. Do nothing
				}
				else {
					// Individual sending
					hasChanged = false;
				}
			}});
		
	}

	/**
	 * Checks whether the file has changed and needs to be updated to the server 
	 */
	public boolean needsToBeSent() {
		// If this file has changed since downloaded from server
		// and it has some valuable validation type defined
		if (hasChanged) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	public void updateContentToServerForPreview(final String absoluteServerPath) {
		
		ConnectionToServer.makeACall(new CallbackActions() {

			public void execute(AsyncCallback callback) {
				ConnectionToServer.searchService.updateFileContentForPreview(absoluteServerPath, fileContent, callback);
			}

			public void onFailure(Throwable caught) {
//				System.out.println("XIDEFile: Error from getFileValue from server method!");
				
			}

			public void onSuccess(Object result) {
				if (result instanceof String) {
					fileContent = (String) result;
				}
			}});
		
	}

	public String getAbsolutePath() {
		return absoluteServerPath;
	}

	public ActionWithTextAndIcon[] getContextMenuItems() {
		ActionWithTextAndIcon[] result = new ActionWithTextAndIcon[2];
		
		ActionWithTextAndIcon action = new ActionWithTextAndIcon(23, Icons.FILE_EDIT, 
				new Action() {
					public void doAction() {
						new Popup("This is not implemented yet!");
					}});
		result[0] = action;
		
		action = new ActionWithTextAndIcon(24, Icons.FILE_DELETE, 
				new Action() {

					public void doAction() {
						new PopupAreYouSure("You are going to delete a file. Are you sure?", new ClickListener() {
						public void onClick(Widget sender) {
							deleteElement();
						}
						
					},null);

					}});
		result[1] = action;
		
		return result;
	}
	
	public void deleteElement() {
		// Only XIDEFolder can ba a parent element

//		this.remove();
		ConnectionToServer.makeACall(new CallbackActions() {

			public void execute(AsyncCallback callback) {
				connectionToServer.searchService.deleteFile(
						getAbsolutePath(), callback);
			}

			public void onFailure(Throwable caught) {
				new PopupError("Unfortunately file was not deleted!",  caught.getMessage());
				
			}

			public void onSuccess(Object result) {
				
				
				HashMap list = XIDEFile.getListByFileType((String)result, Main.getInstance().getSelectedElement().getProperties());
				
				if (list != null) {
					list.remove(XIDEFile.this.getName());
				}
				
				// Remove file
				parentFolder.removeChild(XIDEFile.this);
				
				Main.getInstance().UpdateUI(Main.BOTTOM_TAB);
				Main.getInstance().UpdateUI(Main.RIGHT_TAB);
				
				// Update page/application
				Main.getInstance().updateDateOfSelectedElement();
				
//				Main.getInstance().getSelectedElement()
				new Popup(	"File is deleted! ");
			}});
		// TODO: deletion
		Main.getInstance().UpdateUI();
	}

	
	public FileStructureElement getParent() {
		return parentFolder;
	}
	
	public void removeFromParent() {
		parentFolder.removeChild(this);
	}

	/**
	 *  Parses file type from the response of the upload servlet and selects to which file list is (should) this file type belongs to
	 * @param result
	 * @param properties
	 * @return
	 */
	public static final LinkedHashMap<String, XIDEFile> getListByFileType(String result, HasDisplayableProperties properties) {
		if (result.contains(XIDEFile.CSS_FILE)) {
			return properties.getCSS();
		} else if (result.contains(XIDEFile.DATA_FILE)) {
			return properties.getDB();
		} else if (result.contains(XIDEFile.DI_FILE)) {
			return properties.getDataInstances();
		} else if (result.contains(XIDEFile.QUERY_FILE)) {
			return properties.getQueries();
		} else if (result.contains(XIDEFile.RESOURCE_FILE)) {
			return properties.getResources();
		} else if (result.contains(XIDEFile.SOURCE_FILE)) {
			return properties.getSourceCode();
		}
		else return null;
	}
}
