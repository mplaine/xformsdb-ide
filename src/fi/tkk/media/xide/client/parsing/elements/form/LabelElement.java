package fi.tkk.media.xide.client.parsing.elements.form;

import java.util.ArrayList;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.parsing.ParseElement;

public class LabelElement extends ParseElement {
	// Structure of the Input Parameters :
	// Ref
	// Label ref

	
		public LabelElement(){
			super("label");
		}
		
		public ParseElement getInstance(){
			return (new LabelElement()); 	
		}


		public Widget draw() {

			Label inputLabel = new Label((String)value);
			inputLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
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
