package fi.tkk.media.xide.client.Data;

import java.util.HashMap;
import java.util.LinkedHashMap;

import fi.tkk.media.xide.client.fs.XIDEFile;
import fi.tkk.media.xide.client.fs.XIDEFolder;

public interface HasDisplayableProperties {
	
//	public ShortInfo getShortInfo();
	public LinkedHashMap<String, XIDEFile> getQueries();
	public LinkedHashMap<String, XIDEFile> getDataInstances();
	public LinkedHashMap<String, XIDEFile> getDB();
	public HashMap<String, Property> getProperties();
	public HashMap<String, Property> getParameters();
	public LinkedHashMap<String, XIDEFile> getSourceCode();
	public LinkedHashMap<String, XIDEFile> getCSS();
	public LinkedHashMap<String, XIDEFile> getResources();
	public XIDEFolder getRootFolder();
//	public int[] getAccessRightsSettings();

	public void setQueries(LinkedHashMap<String, XIDEFile> queries);
	public void setDataInstances(LinkedHashMap<String, XIDEFile>  di);
	public void setProperties(Property[]  properties);
	public void setParameters(Property[] parameters);
	public void setSourceCode(LinkedHashMap<String, XIDEFile> sc);
	public void setCSS(LinkedHashMap<String, XIDEFile> css);

}
