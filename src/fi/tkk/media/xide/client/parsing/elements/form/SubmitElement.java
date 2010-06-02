package fi.tkk.media.xide.client.parsing.elements.form;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.parsing.ParseElement;


public class SubmitElement extends ParseElement {
	
		public SubmitElement(){
			super("submit");
		}
		
		public ParseElement getInstance(){
			return (new SubmitElement()); 	
		}


		public Widget draw() {
			HorizontalPanel hp = new HorizontalPanel();
			hp.setWidth("100%");
			hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

			// Draw label 
			ParseElement label = getChildByName("label");
			//If there is a label incide
			
//				hp.add ( label.draw() );
			if (label != null) {	
				String type = parameters.get("appearance");
				if (type ==  null) {
					// Just button
					
						Button b = new Button(label.draw().getElement().getInnerHTML());
						b.setEnabled(false);
						hp.add(b);
					}
				
				else if (type.equals(MIN)) {
					// Link or image
					// if there is a link
//					if (label.getValue() != null) {
//						Hyperlink b = new Hyperlink();
//						b.setText(label.getValue());
//						hp.add(b);
//					}
//					else {
//						ParseElement image = label.getChildByName("img");
//						hp.add(image.draw());
//					}
					hp.add(label.draw());
				}
		}			

			return hp;
		}
	}
