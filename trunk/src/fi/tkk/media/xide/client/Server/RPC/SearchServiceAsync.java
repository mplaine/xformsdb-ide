/**
 * 
 */
package fi.tkk.media.xide.client.Server.RPC;

import java.util.ArrayList;
import java.util.HashMap;

import fi.tkk.media.xide.client.Data.APElement;
import fi.tkk.media.xide.client.Data.APElementApplication;
import fi.tkk.media.xide.client.Data.APElementPage;
import fi.tkk.media.xide.client.Data.OptionProperty;
import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.Data.Tag;
import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.Data.TemplateShortInfo;
import fi.tkk.media.xide.client.UI.Widget.WebPage;
import fi.tkk.media.xide.client.fs.XIDEFolder;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Evgenia Samochadina
 * @date Nov 20, 2008
 *
 */
public interface SearchServiceAsync{
	// User management 
	/**
	 * Method to Authenticate user in XIDE
	 */
	public void login(String username, String password, AsyncCallback<String> callback);
	/**
	 * Logout from XIDE
	 */
	public void logout(AsyncCallback<?> callback);
	public void changePassword(String newPass, AsyncCallback<?> callback);
	public void recoverPassword(String email, AsyncCallback<?> callback);
	public void register(String username, String password, String email, AsyncCallback<?> callback);
	public void checkUser(AsyncCallback<?> callback);
	public void getUserName(AsyncCallback<String> callback);
	
	
	public void getTemplates (String searchString, String[] tags, int templateType, AsyncCallback<TemplateShortInfo[]> callback);
//	public Template GetTemplates (String searchString, String[] tags );
	public void getTemplateDetailedInfo(String templateID, AsyncCallback<Template> callback);
	public void saveNewTemplate(Template template, AsyncCallback<Boolean> callback);
	public void updateTemplateInfo(HashMap<String, Property> elementProperties, AsyncCallback<HashMap<String, Property>> callback);
	public void checkTemplateID(String id, AsyncCallback<?> callback); 
	
	public void onBeforePreview(String appID, String pageID, AsyncCallback<Boolean> callback);
	public void onStartPreview(String appID, String pageID, AsyncCallback<String> callback);
	public void onStopPreview(String appID, String pageID, AsyncCallback<?> callback);
	public void onAfterPreview(String appID, String pageID, AsyncCallback<Boolean> callback);
	
	// Tags
	public void deleteTag(String tagTitle, AsyncCallback<Boolean> callback);
	public void addTag(String tagTitle, String tagDescription, AsyncCallback<Integer> callback);
	public void getAllTags(AsyncCallback<Tag[]> callback);
	public void checkTagName(String tagName, AsyncCallback<?> callback);
	public void saveNewTag(Tag tag, AsyncCallback<Boolean> callback);
	
	// AP services
	public void runTesting(AsyncCallback<Boolean> callback);
	public void getApplication(String apID, boolean includeOnlyMainInfo, AsyncCallback<APElement> callback);
	public void getApplicationsList(boolean showPublicApps, AsyncCallback<ArrayList<String>> callback);
	public void updateAPElementInfo(HashMap<String, Property> elementProperties, int type, AsyncCallback<HashMap<String, Property>> callback);
	public void updateAPElementModifiedDate(String id, int type, AsyncCallback<HashMap<String, Property>> callback);
	
	public void publishApplication(APElementApplication application, boolean doReloadDB, AsyncCallback<HashMap<String, Property>> callback);
	public void unpublishApplication(APElementApplication application, boolean doReloadDB, AsyncCallback<HashMap<String, Property>> callback);
	public void republishApplication(APElementApplication application, boolean doReloadDB, AsyncCallback<HashMap<String, Property>> callback);
	public void stopApplication(APElementApplication application, AsyncCallback<HashMap<String, Property>> callback);
	public void playApplication(APElementApplication application, AsyncCallback<HashMap<String, Property>> callback);
	
	public void getWebPage(APElementPage page, AsyncCallback<Template> callback);
	public void savePageAsComponent(String id, String title, String descr, String tags, String appID, String pageID, AsyncCallback<Template> callback);
	public void getServerURL(AsyncCallback<String> callback);
	public void addNewFolder(XIDEFolder folder, AsyncCallback<Boolean> callback);
	
	public void deleteApplication (String applicationID, AsyncCallback<?> callback);
	public void deletePage (String pageID, AsyncCallback<HashMap<String, Property>> callback);
	
	public void createApplication(String appName, String relPath, String appDescription, boolean isPublic, AsyncCallback<APElementApplication> callback);
	public void createPage(APElementApplication application, String pageName, String relPath,
			boolean addXML, boolean addDefaultContainer, boolean markAsMain, String pageDescription, AsyncCallback<APElementPage> callback);
	public void checkAppRelURL(String rel_url, AsyncCallback<?> callback); 
	public void checkPageRelURL(String rel_url, String app_id, AsyncCallback<?> callback); 
	public void getPageList(String app_id, AsyncCallback<OptionProperty> callback);
	// Files and folders
	public void getFileContent(String absolutePath, AsyncCallback<String> callback);
	
	public void onBeforeFileSetIUpdate(int numberOfFiles, AsyncCallback<?> callback);
	public void updateFileContent(boolean isSetSaving, boolean doValidate, String absolutePath, String content, int validationType, AsyncCallback<?> callback);
	// Checks whether file content fits the rules defined for this file
	public void checkFileContent(String absolutePath, String content, int validationType, AsyncCallback<?> callback);
	public void updateFileContentForPreview(String absolutePath, String content, AsyncCallback<Boolean> callback);
	// Is used for both file and folder deletion
	public void deleteFile(String absolutePath, AsyncCallback<String> callback);
}
