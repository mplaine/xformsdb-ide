/**
 * 
 */
package fi.tkk.media.xide.client.Server.RPC;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.RemoteService;
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

/**
 * @author Evgenia Samochadina
 * @date Nov 20, 2008
 *
 */
public interface SearchService extends RemoteService{
	// User management 
	/**
	 * Method to Authenticate user in XIDE
	 */
	public String login(String username, String password) throws AuthenticationException;
	/**
	 * Logout from XIDE
	 */
	public void logout() throws AuthenticationException;
	public void changePassword(String newPass) throws AuthenticationException, XIDEException;
	public void recoverPassword(String email)  throws AuthenticationException, XIDEException;
	public void register(String username, String password, String email) throws AuthenticationException, XIDEException;
	public void checkUser() throws AuthenticationException;
	public String getUserName() throws AuthenticationException;
	
	
	public TemplateShortInfo[] getTemplates (String searchString, String[] tags, int templateType) throws XIDEException;
//	public Template GetTemplates (String searchString, String[] tags );
	public Template getTemplateDetailedInfo(String templateID) throws XIDEException;
	public Boolean saveNewTemplate(Template template) throws XIDEException;
	public HashMap<String, Property> updateTemplateInfo(HashMap<String, Property> elementProperties) throws XIDEException;
	public void checkTemplateID(String id) throws XIDEException; 
	
	public Boolean onBeforePreview(String appID, String pageID) throws XIDEException;
	public String onStartPreview(String appID, String pageID) throws XIDEException;
	public void onStopPreview(String appID, String pageID) throws XIDEException;
	public Boolean onAfterPreview(String appID, String pageID) throws XIDEException;
	
	// Tags
	public Boolean deleteTag(String tagTitle)  throws XIDEException;
	public Integer addTag(String tagTitle, String tagDescription)  throws XIDEException;
	public Tag[] getAllTags();
	public void checkTagName(String tagName)  throws XIDEException;
	public Boolean saveNewTag(Tag tag)  throws XIDEException;
	
	// AP services
	public Boolean runTesting()  throws XIDEException;
	public APElement getApplication(String apID, boolean includeOnlyMainInfo)  throws AuthenticationException;
	public ArrayList<String> getApplicationsList(boolean showPublicApps)  throws AuthenticationException;
	public HashMap<String, Property> updateAPElementInfo(HashMap<String, Property> elementProperties, int type);
	public HashMap<String, Property> updateAPElementModifiedDate(String id, int type);
	
	public HashMap<String, Property> publishApplication(APElementApplication application, boolean doReloadDB) throws XIDEException;
	public HashMap<String, Property> unpublishApplication(APElementApplication application, boolean doReloadDB) throws XIDEException;
	public HashMap<String, Property> republishApplication(APElementApplication application, boolean doReloadDB) throws XIDEException;
	public HashMap<String, Property> stopApplication(APElementApplication application) throws XIDEException;
	public HashMap<String, Property> playApplication(APElementApplication application) throws XIDEException;
	
	public Template getWebPage(APElementPage page)  throws XIDEException;
	public Template savePageAsComponent(String id, String title, String descr, String tags, String appID, String pageID) throws XIDEException;
	public String getServerURL();
	public Boolean addNewFolder(XIDEFolder folder)  throws XIDEException;
	
	public void deleteApplication (String applicationID) throws XIDEException;
	public HashMap<String, Property> deletePage (String pageID) throws XIDEException;
	
	public APElementApplication createApplication(String appName, String relPath, String appDescription, boolean isPublic) 
		throws XIDEException, AuthenticationException;
	public APElementPage createPage(APElementApplication application, String pageName, String relPath,
			boolean addXML, boolean addDefaultContainer, boolean markAsMain, String pageDescription) throws XIDEException;
	public void checkAppRelURL(String rel_url)  throws XIDEException; 
	public void checkPageRelURL(String rel_url, String app_id)  throws XIDEException; 
	public OptionProperty getPageList(String app_id);
	// Files and folders
	public String getFileContent(String absolutePath) throws XIDEException;
	
	public void onBeforeFileSetIUpdate(int numberOfFiles) throws XIDEException;
	public void updateFileContent(boolean isSetSaving, boolean doValidate, String absolutePath, String content, int validationType)throws XIDEException;
	// Checks whether file content fits the rules defined for this file
	public void checkFileContent(String absolutePath, String content, int validationType)throws XIDEException;
	public Boolean updateFileContentForPreview(String absolutePath, String content)throws XIDEException;
	// Is used for both file and folder deletion
	public String deleteFile(String absolutePath) throws XIDEException;
}
