package fi.tkk.media.xide.client.fs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.popups.PopupAddNewFolder;
import fi.tkk.media.xide.client.popups.UploadFilePopup;
import fi.tkk.media.xide.client.popups.basic.Popup;
import fi.tkk.media.xide.client.popups.basic.PopupAreYouSure;
import fi.tkk.media.xide.client.popups.basic.PopupError;
import fi.tkk.media.xide.client.popups.utils.interfaces.Action;
import fi.tkk.media.xide.client.utils.ActionWithText;
import fi.tkk.media.xide.client.utils.ActionWithTextAndIcon;
import fi.tkk.media.xide.client.utils.ConnectionToServer;
import fi.tkk.media.xide.client.utils.Icons;
import fi.tkk.media.xide.client.utils.ConnectionToServer.CallbackActions;

public class XIDEFolder implements Serializable, FileStructureElement{
	private transient ConnectionToServer connectionToServer; 
	
	// related path on the server
	String relatedServerPath;
	// absolute path on the server
	String absoluteServerPath;
	
	String name;
	
	XIDEFolder parentFolder;
	
	ArrayList<XIDEFile> childrenFiles;
	ArrayList<XIDEFolder> childrenFolders;
	
	ArrayList<String> possibleFileExtensions;
	
	public XIDEFolder() {}
	/**
	 * Conctructor for root folder (has no parent folder)
	 * @param absoluteServerPath
	 * @param name
	 * @param possibleFileExtensions
	 */
	public XIDEFolder(String absoluteServerPath, String name, ArrayList<String> possibleFileExtensions) {
		this.absoluteServerPath = absoluteServerPath;
		this.possibleFileExtensions = possibleFileExtensions;
		this.name = name;
		this.parentFolder = null;
		
		childrenFiles = new ArrayList<XIDEFile>();
		childrenFolders = new ArrayList<XIDEFolder>();
	}
	
	/**
	 * Constructor for middle folder (has a parent, no need for absolute path entering)
	 * 
	 * @param name
	 * @param possibleFileExtensions
	 * @param parentFolder
	 */
	public XIDEFolder(String name, ArrayList<String> possibleFileExtensions, XIDEFolder parentFolder) {
		this.absoluteServerPath = parentFolder.getAbsolutePath() + name + "/";
		this.possibleFileExtensions = possibleFileExtensions;
		this.name = name;
		this.parentFolder = parentFolder;
		
		// Add folder into parent's children list
		if (this.parentFolder != null) {
			this.parentFolder.addChild(this);
		}
		
		childrenFiles = new ArrayList<XIDEFile>();
		childrenFolders = new ArrayList<XIDEFolder>();
		
	}

	/**
	 * Returns true if this folder can have a child folder
	 * @return
	 */
	public boolean isChildFolderManagementAllowed() {
		if (parentFolder != null) {
			if (parentFolder.parentFolder != null) {
				if (absoluteServerPath.contains("db")) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns true if this folder can be deleted
	 * @return
	 */
	public boolean isFolderDeletionAllowed() {
		if (parentFolder != null) {
			if (parentFolder.parentFolder != null) {
				if (parentFolder.parentFolder.parentFolder != null) {
					if (absoluteServerPath.contains("db")) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public String getName() {
		return name;
	}
	
	public String getRelatedPath() {
		return relatedServerPath;
	}
	
	public String getAbsolutePath() {
		return absoluteServerPath;
	}
	
	public void addChild(XIDEFile file) {
		childrenFiles.add(file);
	}

	public void addChild(XIDEFolder folder) {
		childrenFolders.add(folder);
	}

	public void deleteElement() {
			// Only XIDEFolder can be a parent element
			((XIDEFolder)this.getParent()).removeChild(this);
	//		this.remove();
			ConnectionToServer.makeACall(new CallbackActions() {
	
				public void execute(AsyncCallback callback) {
					connectionToServer.searchService.deleteFile(
							XIDEFolder.this.getAbsolutePath(), callback);
				}
	
				public void onFailure(Throwable caught) {
					new PopupError("Unfortunately folder was not deleted!",  caught.getMessage());
				}
	
				public void onSuccess(Object result) {
						// No page has found
						new Popup( 	"Folder is deleted! ");
						
						// Update page/application
						Main.getInstance().updateDateOfSelectedElement();
				}});
			// TODO: deletion
			Main.getInstance().UpdateUI();
		}
	public ArrayList<XIDEFile> getFileChild () {
		return childrenFiles;
	}
	
	public ArrayList<XIDEFolder> getFolderChild () {
		return childrenFolders;
	}

	public ActionWithTextAndIcon[] getContextMenuItems() {
		ActionWithTextAndIcon[] result ;
		// under data/.../ it is allowed to add and delete folders
		if(isFolderDeletionAllowed()) {
			result = new ActionWithTextAndIcon[4];
		} 
		// for data/... it is allowed to add new child files, but not allowed to delete itself
		else if (isChildFolderManagementAllowed()){
			result = new ActionWithTextAndIcon[3];
		}
		else {
			result = new ActionWithTextAndIcon[3];
		}
		
		ActionWithTextAndIcon action = new ActionWithTextAndIcon(18, Icons.FILE_CREATE, 
				new Action() {
					public void doAction() {
						new UploadFilePopup(XIDEFolder.this);
//						(new PopupFileUpload("Select the file")).showPopup(false);
//						(new ModalDimmedPopup("This is not implemented yet!")).showPopup(false);
					}});
		result[0] = action;

		action = new ActionWithTextAndIcon(22,  Icons.FOLDER_DELETE,
				new Action() {

					public void doAction() {
						new Popup("This is not implemented yet!");
					}});
		result[1] = action;

		if(isChildFolderManagementAllowed()) {
			action = new ActionWithTextAndIcon(20, Icons.FOLDER_ADD, 
					new Action() {
						
						public void doAction() {
							new PopupAddNewFolder(XIDEFolder.this);
						}});
			result[2] = action;
		}
		if (isFolderDeletionAllowed()) {
			action = new ActionWithTextAndIcon(21, Icons.FOLDER_DELETE_CHILDREN,
					new Action() {
						
						public void doAction() {
							new PopupAreYouSure("You are going to delete a folder. Are you sure?", new ClickListener() {
								public void onClick(Widget sender) {
									deleteElement();
								}
								
							},null);

						}});
			result[3] = action;
		} 
		return result;
	}
	
	public void removeChild(FileStructureElement child) {
		
		if (child instanceof XIDEFile) {
			// File
				if (childrenFiles.contains(child)) {
					childrenFiles.remove(child);
				}
		}
		else {
			// Folder
			if (childrenFolders.contains(child)) {
				childrenFolders.remove(child);
				
				// TODO: do smth hierarchically with other children
			}
		}
	}
	
	public void removeFromParent() {
		parentFolder.removeChild(this);
	}
	
	public FileStructureElement getParent() {
		return parentFolder;
	}
}
