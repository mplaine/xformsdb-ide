package fi.tkk.media.xide.client.parsing.elements.form;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.parsing.ParseElement;

public class SecretElement extends ParseElement {
	// Structure of the Input Parameters :
	// Ref
	// Label ref

		public SecretElement(){
			super("secret");
		}
		
		public ParseElement getInstance(){
			return (new SecretElement()); 	
		}


		public Widget draw() {
			//return super.Create();
			HorizontalPanel hp = new HorizontalPanel();
			//hp.setSize("100%", "100%");
//			TODO: add css
//			hp.addStyleName(style);

			hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			hp.setWidth("100%");

//			Label inputLabel = new Label(label);
//			inputLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//			hp.add(inputLabel);
			
			ParseElement label = getChildByName("label");
			if (label != null) {
				hp.add ( label.draw() );
			}

			final PasswordTextBox inputTextBox = new PasswordTextBox();
			inputTextBox.setReadOnly(true);
			hp.add(inputTextBox );

			// Set default value if any
			if (value != null) {
				inputTextBox.setText(value);
			}
			return hp;
		}
	}
