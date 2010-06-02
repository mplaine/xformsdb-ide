package fi.tkk.media.xide.client.parsing.elements.support;

import java.util.ArrayList;

import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.parsing.ParseElement;
import fi.tkk.media.xide.client.parsing.elements.form.Select1Element;
import fi.tkk.media.xide.client.utils.GoodHorizontalAdjustablePanel;

public class ItemSetElement extends ParseElement {

		public ItemSetElement(){
			super("item");
		}
		
		public ParseElement getInstance(){
			return (new ItemSetElement()); 	
		}


		public Widget draw(int type) {

			HorizontalPanel hp = new HorizontalPanel();
			hp.setWidth("100%");
			hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

//			switch (type) {
//			case Select1Element.RADIOBUTTON:
//				RadioButton rb_1 = new RadioButton("");
//				hp.add(rb_1);
//				break;
//			case Select1Element.FULLLIST:
//				// just label
//				break;
//			case Select1Element.COMBOBOX:
//				//just label
//				break;
//			case Select1Element.CHECKBOX:
//				CheckBox ch = new CheckBox("");
//				hp.add(ch);
//				break;
//			}
		
			
			ParseElement label = getChildByName("label");
			if (label != null) {
				hp.add ( label.draw() );
			}
			
			return hp;
		}
	}
