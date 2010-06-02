package fi.tkk.media.xide.client.parsing.elements.form;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.parsing.ParseElement;

public class OutputElement extends ParseElement {
	public OutputElement(){
		super("output");
	}
	
	public ParseElement getInstance(){
		return (new OutputElement()); 	
	}


	public Widget draw() {
		Widget inputLabel = null;
		if (this.parameters.containsKey("value")) {
			String valueAttribute = parameters.get("value");
			
			// If output has the mediatype attribute it is not displayed as label
			if (this.parameters.containsKey("mediatype")) {
				String mediaType = this.parameters.get("mediatype");
				// If attribute value equals to text/html then the output should be displayed as html 
				if (mediaType.equals("text/html")) {
					inputLabel = new HTML(valueAttribute);
				}
				// TODO: add other options for mediatype
			}
			else {
//			if (valueAttribute.startsWith("'") && valueAttribute.endsWith("'")) {
				inputLabel = new Label(parameters.get("value"));
				((Label)inputLabel).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//			}
//			else {
//				inputLabel = new Label("Undisplayable text here");
//			}
				}
			}
		else {
			inputLabel = new Label("Undisplayable text here");
			((Label)inputLabel).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		}
		
		DOM.setStyleAttribute(inputLabel.getElement(), "paddingLeft", "2px");
		DOM.setStyleAttribute(inputLabel.getElement(), "paddingRight", "2px");
			if (children==null) {
				return inputLabel ;
				}
			else {
				// Has children. Needs HP
				HorizontalPanel hp = new HorizontalPanel();
				hp.setWidth("100%");
				hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
				
				hp.add(inputLabel);
				
				for (int i = 0; i < children.size(); i++) {
					hp.add(children.get(i).draw());
				}
				return hp;
			}

	}

}
