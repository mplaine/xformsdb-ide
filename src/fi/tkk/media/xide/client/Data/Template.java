/**
 * 
 */
package fi.tkk.media.xide.client.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.fs.XIDEFile;
import fi.tkk.media.xide.client.fs.XIDEFolder;
import fi.tkk.media.xide.client.popups.basic.LoadingPopup;
import fi.tkk.media.xide.client.popups.basic.Popup;
import fi.tkk.media.xide.client.popups.basic.PopupError;
import fi.tkk.media.xide.client.popups.basic.PopupErrorWithAction;
import fi.tkk.media.xide.client.popups.utils.interfaces.Action;
import fi.tkk.media.xide.client.utils.ActionWithText;
import fi.tkk.media.xide.client.utils.ConnectionToServer;
import fi.tkk.media.xide.client.utils.ConnectionToServer.CallbackActions;

/**
 * @author Evgenia Samochadina
 * @date Nov 25, 2008
 *
 */
public class Template implements Serializable, HasDisplayableProperties{
	
	// Template constants
	public static final int APPLICATION = 1;
	public static final int PAGE 		= 2;
	public static final int COMPONENT 	= 3;
	// That is: 
//	String title;
//	String uniqueTemplateID;
//	String description;
//	String [] tags;
	
	// Files
	private LinkedHashMap <String, XIDEFile> queries;
	private LinkedHashMap<String, XIDEFile> dataInstances;
	private LinkedHashMap<String, XIDEFile> db;
	private LinkedHashMap <String, XIDEFile> sourceCode;
	private LinkedHashMap <String, XIDEFile> resources;
	private LinkedHashMap <String, XIDEFile> css;
	
	private XIDEFolder rootFolder;
	
	public HashMap<String, Property> parameters;
	public HashMap<String, Property> properties;

	
	private boolean isPublic;
	
	transient ConnectionToServer connectionToServer;
	
//	public boolean isChanged = false;
	
	public Template(){
	}
	
	public Template cloneIt() {

		// Clone parameters hash map
		HashMap<String, Property> parametersCopy = new HashMap<String, Property>();
		if (parameters != null) {
			Set<String> keys = parameters.keySet();
			for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
				String o = iterator.next();
				parametersCopy.put(o, parameters.get(o).clone());
			}
		}
		
		// Clone properties hash map
		HashMap<String, Property> propertiesCopy = new HashMap<String, Property>();
		Set<String> keys = properties.keySet();

		for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
			String o = iterator.next();
			propertiesCopy.put(o, properties.get(o).clone());
		}
		
		LinkedHashMap <String, XIDEFile> queriesN = new LinkedHashMap<String, XIDEFile>();
		for (Iterator<String> iterator = queries.keySet().iterator(); iterator.hasNext();) {
			String o = iterator.next();
			queriesN.put(o,queries.get(o));
		}
		
		LinkedHashMap <String, XIDEFile> dataInstancesN = new LinkedHashMap<String, XIDEFile>();
		for (Iterator<String> iterator = dataInstances.keySet().iterator(); iterator.hasNext();) {
			String o = iterator.next();
			dataInstancesN.put(o,dataInstances.get(o));
		}
		
		LinkedHashMap <String, XIDEFile> sourceCodeN = new LinkedHashMap<String, XIDEFile>();
		for (Iterator<String> iterator = sourceCode.keySet().iterator(); iterator.hasNext();) {
			String o = iterator.next();
			sourceCodeN.put(o,sourceCode.get(o));
		}

		LinkedHashMap <String, XIDEFile> resourcesN = new LinkedHashMap<String, XIDEFile>();
		for (Iterator<String> iterator = resources.keySet().iterator(); iterator.hasNext();) {
			String o = iterator.next();
			resourcesN.put(o,resources.get(o));
		}

		LinkedHashMap <String, XIDEFile> dbN = new LinkedHashMap<String, XIDEFile>();
		for (Iterator<String> iterator = db.keySet().iterator(); iterator.hasNext();) {
			String o = iterator.next();
			dbN.put(o,db.get(o));
		}
		
		LinkedHashMap <String, XIDEFile> cssN = new LinkedHashMap<String, XIDEFile>();
		for (Iterator<String> iterator = css.keySet().iterator(); iterator.hasNext();) {
			String o = iterator.next();
			cssN.put(o,css.get(o));
		}
		
//		Template clone = new Template(info.clone(), 
//				(LinkedHashMap <String, String>)queries.clone(), 
//				null,
//				new Property[parameters.length], 
//				new String(sourceCode), 
//				new String(css), isPublic);
		Template clone = new Template( 
				cssN,
				dataInstancesN,
				dbN,
				queriesN, 
				resourcesN, 
				sourceCodeN, 
				parametersCopy, 
				propertiesCopy,
				rootFolder, isPublic);
//		clone = Template.fakeTemplate("f", "d");
		return clone;
	}
	
	public static Template fakeTemplate(String name, String id) {
		// Template 		
		LinkedHashMap <String, XIDEFile> queries = new LinkedHashMap<String, XIDEFile>();
		
		LinkedHashMap<String, XIDEFile> dataInstances = new LinkedHashMap<String, XIDEFile>();
		HashMap<String, Property> properties = new HashMap<String, Property>();
		
		properties.put(Property.ID, new BaseProperty("ID", id, true) );
		properties.put(Property.TITLE, new BaseProperty("Title", name, true) );
		properties.put(Property.DESCR, new BaseProperty("Descr", "That's an empty template", true) );
		
		HashMap<String, Property> parameters = new HashMap<String, Property>();
		
		LinkedHashMap<String, XIDEFile> sc = new LinkedHashMap<String, XIDEFile>();
		LinkedHashMap<String, XIDEFile> css = new LinkedHashMap<String, XIDEFile>();
		LinkedHashMap<String, XIDEFile> res = new LinkedHashMap<String, XIDEFile>();
		LinkedHashMap<String, XIDEFile> db = new LinkedHashMap<String, XIDEFile>();
//		sc.put(id + ".xml", new XIDEFile());

		Template t= new Template(css, dataInstances, db, queries, res, sc, parameters, properties, null, true);

		return t; 
	}

	public static Template fakeTemplate(String name, String desctiption, String id) {
		// Template 		
		LinkedHashMap <String, XIDEFile> queries = new LinkedHashMap<String, XIDEFile>();
		
		LinkedHashMap<String, XIDEFile> dataInstances = new LinkedHashMap<String, XIDEFile>();
		HashMap<String, Property> properties = new HashMap<String, Property>();
//		properties.put(Property.RELATED_URL, new BaseProperty("Name for publishing", "", "string", "", "titletitle") );
		
		properties.put(Property.ID, new BaseProperty("ID", id, true) );
		properties.put(Property.TITLE, new BaseProperty("Title", name, true) );
		properties.put(Property.DESCR, new BaseProperty("Descr", desctiption, true) );
		
		HashMap<String, Property> parameters = new HashMap<String, Property>();
//		parameters.put("param1", new BaseProperty("param1", "", "string", "", "titletitle", true, false, false, Property.TYPE_PARAMETER) );
		
		LinkedHashMap<String, XIDEFile> sc = new LinkedHashMap<String, XIDEFile>();
		LinkedHashMap<String, XIDEFile> css = new LinkedHashMap<String, XIDEFile>();
		LinkedHashMap<String, XIDEFile> res = new LinkedHashMap<String, XIDEFile>();
		LinkedHashMap<String, XIDEFile> source = new LinkedHashMap<String, XIDEFile>();
		LinkedHashMap<String, XIDEFile> db = new LinkedHashMap<String, XIDEFile>();
		source.put(id + ".xml", new XIDEFile());

		Template t= new Template(css, dataInstances, db, queries, res, sc, parameters, properties, null, true);

		return t; 
	}
	
	public void addParameter(String parameterName, Property parameter) {
		if (parameters == null) {
			parameters = new HashMap<String, Property>();
		}
		
		parameters.put(parameterName, parameter);
		
	}

	
	public void MakeEditable(boolean isEditableNow) {

		if (parameters != null) {
			Set<String> keys = parameters.keySet();

			for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
				String o = iterator.next();
				parameters.get(o).isEditableNow = isEditableNow;
			}
		}
		if (properties != null) {
			Set<String> keys = properties.keySet();

			for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
				String o = iterator.next();
				properties.get(o).isEditableNow = isEditableNow;
			}

		}

	}
	
	public Template(
			LinkedHashMap<String, XIDEFile> css, 
			LinkedHashMap<String, XIDEFile> dataInstances, 
			LinkedHashMap<String, XIDEFile> db, 
			LinkedHashMap <String, XIDEFile> queries, 
			LinkedHashMap<String, XIDEFile> resources,
			LinkedHashMap<String, XIDEFile> sourceCode, 
			HashMap<String, Property> parameters, 
			HashMap<String, Property> properties,
			XIDEFolder rootFolder){
		this.queries = queries;
		this.dataInstances = dataInstances;
		this.db = db;
		if (parameters != null) {
			this.parameters = parameters;
		}
		else {
			this.parameters = new HashMap<String, Property>();
		}
		this.properties = properties;
		setSourceCode(sourceCode);
		this.css = css;
		this.resources = resources;
		this.isPublic = true;
		this.rootFolder = rootFolder;
		
	}
	
	public Template(	LinkedHashMap<String, XIDEFile> css, 
			LinkedHashMap<String, XIDEFile> dataInstances, 
			LinkedHashMap<String, XIDEFile> db, 
			LinkedHashMap <String, XIDEFile> queries, 
			LinkedHashMap<String, XIDEFile> resources,
			LinkedHashMap<String, XIDEFile> sourceCode, 
			HashMap<String, Property> parameters, 
			HashMap<String, Property> properties,
			XIDEFolder rootFolder,
			boolean isPublic){
		this(css, dataInstances, db, queries, resources, sourceCode, parameters, properties, rootFolder);
		this.isPublic = isPublic;
	}
	public void isPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}
	public boolean isPublic() {
		return isPublic;
	}
	
	// TODO: make something more intellectual 
	static String getUniqueTemplateId(String prevID){
		return (prevID + "1");
	}

	/**
	 * @return the queries
	 */
	public LinkedHashMap <String, XIDEFile> getQueries() {
		return queries;
	}


	
	public void setSourceCode(LinkedHashMap<String, XIDEFile> sourceCode) {
		this.sourceCode = sourceCode;
		
		// set validation type to the source file
		// this is hardcoded since it is difficult to maintain source code creation
		if (sourceCode!= null && getSourceCodeFirstFile()!= null) {
			// there is only 2 types of source codes: template (component) and page
			
			// if validation type is page validation
			if (this.getSourceCodeFirstFile().getValidationType() == XIDEFile.VALIDATE_PAGE_SOURCE) {
				// do nothing, leave as it is
			}
			else {
				// set template validation
				this.getSourceCodeFirstFile().setValidationType(XIDEFile.VALIDATE_TEMPLATE_SOURCE);
			}
		}

	}

	public LinkedHashMap<String, XIDEFile> getSourceCode() {
		return sourceCode;
	}
	public XIDEFile getSourceCodeFirstFile() {
		if (sourceCode != null && !sourceCode.isEmpty()) {
			return sourceCode.get(sourceCode.keySet().iterator().next());
		}
		else {
			return null;
		}
			
	}
	
	public LinkedHashMap<String, XIDEFile> getCSS() {
		return css;
	}
	
	public LinkedHashMap<String, XIDEFile> getResources() {
		return resources;
	}

	public LinkedHashMap<String, XIDEFile> getDataInstances() {
		return dataInstances;
	}

	public HashMap<String, Property> getParameters() {
		
		return parameters;
	}

	public HashMap<String, Property> getProperties() {
		return properties;
	}

	public void setCSS(LinkedHashMap<String, XIDEFile> css) {
		this.css = css;
	}

	public void setDataInstances(LinkedHashMap<String, XIDEFile> di) {
		this.dataInstances = di;
	}

	public void setParameters(Property[] parameters) {
		this.parameters = null;
	}

	public void setProperties(Property[] properties) {
		this.properties = null;
	}

	public void setQueries(LinkedHashMap<String, XIDEFile> queries) {
		this.queries = queries;
		
	}

	public LinkedHashMap<String, XIDEFile> getDB() {
		// TODO Auto-generated method stub
		return db;
	}

	public XIDEFolder getRootFolder() {
		return rootFolder;
	}
	
//	/**
//	 * Used in PP only
//	 * Is called when one of the objects, which contain template ( web page, component, slot, templaeInfo) is saved.
//	 * This function should send all template info to the server and also send template files, if any of them has been updated
//	 * 
//	 */
//	public void saveTemplateToServer(Selectable objectToSave,boolean isWebPage) {
//		// Update properties and other info
//		if (isWebPage) {
//			sendUpdatedPageTemplate();
//		}
//		else {
//			sendUpdatedTemplate();
//		}
//		
//		sendUpdatedFilesToServer(null, objectToSave, true);
//	}
	
	/**
	 * Checks which files have been changed and send them to the the server.
	 */
	public void sendUpdatedFilesToServer(final SaveObjectsListener listener, Selectable objectToSave, boolean doValidate){

		// List which contains all files which needs to be sent to the server
		ArrayList<XIDEFile> filesToSend = new ArrayList<XIDEFile>();
		
		// Iterate though all files and check which files are changed and need to be updated 
		checkFiles(this.getSourceCode(), filesToSend);
		checkFiles(this.getCSS(), filesToSend);
		checkFiles(this.getDataInstances(), filesToSend);
		checkFiles(this.getDB(), filesToSend);
		checkFiles(this.getQueries(), filesToSend);
		checkFiles(this.getResources(), filesToSend);
		
		// Initiates file saving. Sends number of files to send. Final report about saving will come to this method
		sendNumberOfFiles(listener, objectToSave, filesToSend);
		
		// Start file sending
		sendFilesToServer (filesToSend, doValidate);
		
	}
	
	/**
	 * Is called when list of files is already formed and they are ready to be saved. 
	 * Sends number of files to the server and initiates file sanding
	 * @param filesToSend
	 */
	public void sendNumberOfFiles(final SaveObjectsListener listener, 
			final Selectable objectToSave, final ArrayList<XIDEFile> filesToSend) {
		ConnectionToServer.makeACall(new CallbackActions() {

			public void execute(AsyncCallback callback) {
				// Lock buttons
				LoadingPopup.showDimmed();
				
				// Send number of files
				ConnectionToServer.searchService.onBeforeFileSetIUpdate(filesToSend.size(), callback);
			}

			public void onFailure(Throwable caught) {
				if (listener != null) {
					listener.processResult(objectToSave.getTypeName() + " " + objectToSave.getProperties().getProperties().get(Property.TITLE).getStringValue() + " " 
							+  caught.getMessage());
				}
				else {
					// Unlock buttons
					LoadingPopup.hideDimmed();
					new PopupErrorWithAction("Unfortunately saving has failed!" ,  caught.getMessage(), 
							new ActionWithText("You can try to save the object inspite of errors received",
									new Action() {
	
										public void doAction() {
											// Initiates file saving. Sends number of files to send. Final report about saving will come to this method
											sendNumberOfFiles(listener, objectToSave, filesToSend);
											
											// Start file sending
											sendFilesToServer (filesToSend, false);
	
										}}));
				}
			}

			public void onSuccess(Object result) {
				// Files are saved
				
				objectToSave.afterSaved();
				processFilesAfterSaving(filesToSend);

				if (listener != null) {
					// Remove object from changed object list
					Main.getInstance().removeFromChangedElements(objectToSave);

					listener.processResult(null);
				}
				else {
						// Unlock buttons
					LoadingPopup.hideDimmed();
					new Popup ("Saving finished successfully!");
				}


			}});
	}
	
	/**
	 * Iteratively sends all files from the list to the server
	 * @param filesToSend list of files to send
	 */
	public void sendFilesToServer(ArrayList<XIDEFile> filesToSend, boolean doValidate) {
		// Send all files to the server for further processing
		for (Iterator<XIDEFile> iterator = filesToSend.iterator(); iterator.hasNext();) {
			XIDEFile file =  iterator.next();
			
			file.updateContentToServer(true, doValidate);
		}

	}
	

	/**
	 * Iteratively updates all files from the list after they have been saved on a server
	 * @param filesToSend list of files to send
	 */
	public void processFilesAfterSaving(ArrayList<XIDEFile> filesToSend) {
		// Updates all files sent to the server
		for (Iterator<XIDEFile> iterator = filesToSend.iterator(); iterator.hasNext();) {
			XIDEFile file =  iterator.next();
			file.hasChanged = false;
		}

	}


	/**
	 * Check list of files of one type from template and adds files need to be updated to the server to the list
	 * @param filelist list of files from template
	 * @param filesToSend list files to send
	 */
	private void checkFiles(HashMap<String, XIDEFile> filelist, ArrayList<XIDEFile> filesToSend) {
		for (Iterator<String> iterator = filelist.keySet().iterator(); iterator.hasNext();) {
			String fileName = iterator.next();
			if (filelist.get(fileName).needsToBeSent()) {
				filesToSend.add(filelist.get(fileName));
			}
		}
	}

		
	public void sendUpdatedTemplate() {
		ConnectionToServer.makeACall(new CallbackActions() {

			public void execute(AsyncCallback callback) {
				connectionToServer.searchService.updateTemplateInfo(Template.this.getProperties(), callback);
			}

			public void onFailure(Throwable caught) {
				
			}

			public void onSuccess(Object result) {
				updateTemplateProperties(Template.this.getProperties(), result);
				Main.getInstance().UpdateUI(Main.MAIN_TAB);
				Main.getInstance().UpdateUI(Main.RIGHT_TAB);
			}});
	}

	public void sendUpdatedPageTemplate() {
		ConnectionToServer.makeACall(new CallbackActions() {
			public void execute(AsyncCallback callback) {
				connectionToServer.searchService.updateAPElementInfo(Template.this.getProperties(), APElement.WPAGE, callback);
			}

			public void onFailure(Throwable caught) {
				
			}

			public void onSuccess(Object result) {
				updateTemplateProperties(Template.this.getProperties(), result);
//				Main.getInstance().UpdateUI(Main.MAIN_TAB);
//				Main.getInstance().UpdateUI(Main.RIGHT_TAB);
			}});
	}
	
	/**
	 * Updates a template stored on the client with new values received from server.
	 * Usually is called when some process is succeed and it is required to update a client (e.g. application has published/unpublished)
	 * @param templateP template to update
	 * @param result server answer, should be cast to  HashMap<String, Property>
	 */
	public static void updateTemplateProperties(HashMap<String, Property> templateP, 
			Object result) {
		HashMap<String, Property> receivedP = null;
		if (result instanceof HashMap) {
			receivedP = (HashMap<String, Property>) result;
			
			Set <String> newPropSet = receivedP.keySet();
			
			for (Iterator<String> iterator = newPropSet.iterator(); iterator.hasNext();) {
				String propID =  iterator.next();
				if (templateP.containsKey(propID)) {
					// Update Value
					templateP.remove(propID);
					templateP.put(propID, receivedP.get(propID).clone());
				}
			}

		}
	}

}
