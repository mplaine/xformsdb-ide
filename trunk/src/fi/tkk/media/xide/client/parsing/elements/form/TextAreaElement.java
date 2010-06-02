package fi.tkk.media.xide.client.parsing.elements.form;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.parsing.ParseElement;

public class TextAreaElement extends ParseElement {
	// Structure of the Input Parameters :
	// Ref
	// Label ref

		public TextAreaElement(){
			super("textarea");
		}
		
		public ParseElement getInstance(){
			return (new TextAreaElement()); 	
		}


		public Widget draw() {
			//return super.Create();
			HorizontalPanel hp = new HorizontalPanel();
			hp.setWidth("100%");
			hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

			// Label
			hp.add ( ((ParseElement)children.get(0)).draw() );

			final TextArea inputTextArea = new TextArea();
			inputTextArea.setReadOnly(true);
			inputTextArea.setSize("100%", "100%");

			// TODO: get know what is the font size (in px) and use this info in height definition
			inputTextArea.setHeight("40px");
			inputTextArea.setCharacterWidth(30);
			hp.add(inputTextArea );

			return hp;
		}
	}
