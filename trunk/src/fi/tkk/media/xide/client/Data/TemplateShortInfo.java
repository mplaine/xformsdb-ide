package fi.tkk.media.xide.client.Data;

import java.io.Serializable;
import java.util.HashMap;

import com.google.gwt.user.client.ui.Panel;

/**
 * Special data structure which represents  
 * short information about template element. 
 * It is received from server after search request and 
 * displayed in the search list result on the Search tab 
 * 
 * @author Evgenia Samochadina
 * @date Nov 19, 2008
 *
 */
public class TemplateShortInfo implements Serializable{

	public static final String TITLE = "Title";
	public static final String ID = "ID";
	public static final String DESCR = "Description";
	public static final String TAGS = "Tags";
	public static final String DO_WORK = "Do work";
	// TODO:  Think about access modifiers
//	String title;
//	String uniqueTemplateID;
//	String description;
//	String [] tags;
	private Property id;
	private Property title;
	private Property description;
	private Property tags;
	private BooleanProperty doWork;
	
	public Property[] properties;
	
	private String sourceCode;
	
	public HashMap<String, Property> getPropertiesAsMap(){
		HashMap<String, Property>  result = new HashMap<String, Property>();
		result.put(Property.ID, id);
		result.put(Property.TITLE, title);
		result.put(Property.DESCR, description);
		result.put(Property.TAGS, tags);
		result.put(Property.DO_WORK, doWork);
		return result;
	}
	static TemplateShortInfo fakeTemplateShortInfo(String nameT, String idT) {
		String[] fakeTags = {"Public", "Private", "Atomic"};
		Property title = new BaseProperty(TITLE, nameT, true);
		Property id = new BaseProperty(ID, idT, true);
		Property descr = new  BaseProperty(DESCR, "This is a description", true);
		Property tags = new ArrayProperty (TAGS, fakeTags, true);
		BooleanProperty doWork = new BooleanProperty (DO_WORK, false, false, "does this work");
		String source = "";
 
		TemplateShortInfo tsi= new TemplateShortInfo(id, title, descr, tags, doWork, source);
		return tsi;
	}
	public TemplateShortInfo(){
		
	}
	public Property getID() {
		return id;
	}
	public Property getTitle() {
		return title;
	}
	public Property getDescr() {
		return description;
	}
	public Property getTags() {
		return tags;
	}
	public BooleanProperty getDoWork() {
		return doWork;
	}
//	public Property getProperty(String name){
//		for (int i = 0; i < properties.length; i++){
//			if (properties[i].name.equals(name)){
//				return properties[i];
//			}
//		}
//		return null;
//	}
//	public ShortInfo(Property[] p) {
//		this.properties = p;
//	}
	public TemplateShortInfo(Property id, Property title, Property descr, Property tags, BooleanProperty doWork, String sourceCode) {
		this.id = id;
		this.title = title;
		this.description = descr;
		this.tags = tags;
		this.doWork = doWork;
		this.properties = new Property[5];
		this.properties[0] = this.id;
		this.properties[1] = this.title;
		this.properties[2] = this.description;
		this.properties[3] = this.tags;
		this.properties[4] = this.doWork;
		this.sourceCode = sourceCode;
	}
	
	public TemplateShortInfo clone(){
		Property id = this.id.clone();
		Property title = this.title.clone();
		Property descr = this.description.clone();
		Property tags = this.tags.clone();
		BooleanProperty doWork = this.doWork.clone();
		String source = new String(sourceCode);
		
		return new TemplateShortInfo(id, title, descr, tags, doWork, source);
	}
}

