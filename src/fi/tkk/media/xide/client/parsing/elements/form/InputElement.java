package fi.tkk.media.xide.client.parsing.elements.form;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.parsing.ParseElement;

public class InputElement extends ParseElement {
	// Structure of the Input Parameters :
	// Ref
	// Label ref

		public InputElement(){
			super("input");
		}
		
		public ParseElement getInstance(){
			return (new InputElement()); 	
		}


		public Widget draw() {
			//return super.Create();
			HorizontalPanel hp = new HorizontalPanel();
			hp.setWidth("100%");
//			TODO: add css
//			hp.addStyleName(style);

			hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

//			Label inputLabel = new Label(label);
//			inputLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//			hp.add(inputLabel);
			ParseElement label = getChildByName("label");
			if (label != null) {
				hp.add ( label.draw() );
			}

			final TextBox inputTextBox = new TextBox();
			inputTextBox.setReadOnly(true);
//			inputTextBox.s
			hp.add(inputTextBox );

			// Set default value if any
			if (value != null) {
				inputTextBox.setText(value);
			}

			return hp;
		}
	}
