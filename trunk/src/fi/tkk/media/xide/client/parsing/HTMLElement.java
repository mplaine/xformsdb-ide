package fi.tkk.media.xide.client.parsing;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.UI.Widget.xSimpleWidget2;
import fi.tkk.media.xide.client.parsing.ParseElement;

public class HTMLElement extends ParseElement {
	// Structure of the Input Parameters :
	// Ref
	// Label ref

		public HTMLElement(String name){
			super(name);
		}
		
		public ParseElement getInstance(){
			return (new HTMLElement(elementName)); 	
		}


		public Widget draw() {
//			System.out.println("HTML element: " + elementName);
			// <name
			String html = "<" + elementName;
			
			//attributes
			for (Iterator<String> iterator = parameters.keySet().iterator(); iterator.hasNext();) {
				String parName = iterator.next();
				String parValue = parameters.get(parName);
				html = html + " " + parName + "=\"" + parValue + "\"";
			}
			// >
			html = html + ">";
			if (this.value != null) {
				html += value;
			}
			html = html + "</" + elementName + ">";
			
			xSimpleWidget2 htmlElement = new xSimpleWidget2(html);
			for (int i = 0; i < children.size(); i++) {
				htmlElement.add(children.get(i).draw());// = html + children.get(i).draw().getElement().getInnerHTML();
			}

			return htmlElement;
		}
	}
