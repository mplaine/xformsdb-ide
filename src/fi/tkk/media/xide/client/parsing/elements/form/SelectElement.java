package fi.tkk.media.xide.client.parsing.elements.form;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.parsing.ParseElement;


public class SelectElement extends ParseElement {
	
		public SelectElement(){
			super("select");
		}
		
		public ParseElement getInstance(){
			return (new SelectElement()); 	
		}


		public Widget draw() {
			//return super.Create();
			HorizontalPanel hp = new HorizontalPanel();
			hp.setWidth("100%");
//			TODO: add css
//			hp.addStyleName(style);

			hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			hp.setWidth("100%");

			// Draw label 
			ParseElement label = getChildByName("label");
			if (label != null) {
				hp.add ( label.draw() );
			}
			String type = parameters.get("appearance");
			if (type.equals(FULL)) {
				for (Iterator<ParseElement> iterator = children.iterator(); iterator.hasNext();) {
					ParseElement item = iterator.next();
					if (item.getName().equals("item")) {
						CheckBox rb_1 = new CheckBox("");
						rb_1.setEnabled(false);
						hp.add(rb_1);
						ParseElement buttonLabel = item.getChildByName("label");
						if (buttonLabel != null) {
							hp.add ( buttonLabel.draw() );
						}
					}
					
				}
			}
			else if (type.equals(COMPACT) || type.equals(MIN)) {
				final ListBox listBox = new ListBox();
				for (Iterator<ParseElement> iterator = children.iterator(); iterator.hasNext();) {
					ParseElement o = iterator.next();
					if (o.getName().equals("item")) {
						String textLabel;
						ParseElement buttonLabel = o.getChildByName("label");
						if (buttonLabel != null) {
							textLabel = o.getChildByName("label").getValue();
							listBox.addItem(textLabel);
						}
					}
				}
				listBox.setMultipleSelect(false);
				if (type.equals(COMPACT)) {
					listBox.setVisibleItemCount(listBox.getItemCount());
				}
				
				hp.add(listBox );
			}
				

			return hp;
		}
	}
