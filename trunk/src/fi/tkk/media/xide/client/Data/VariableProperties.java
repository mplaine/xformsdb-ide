/**
 * 
 */
package fi.tkk.media.xide.client.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * @author Evgenia Samochadina
 * @date Nov 25, 2008
 *
 */
public class VariableProperties {
	LinkedHashMap<String, String> parameters;
	ArrayList<String> accessRights;
	LinkedHashMap<String, String> SystemProperties;
	
	public VariableProperties(String[] parameterNames) {
		// Create parameter list
		parameters = new LinkedHashMap<String, String>();
		for (int i = 0; i > parameterNames.length; i++){
			parameters.put(parameterNames[i], "");
		}
		accessRights = new ArrayList<String>();
		// TODO: create system properties
	}
}
