package fi.tkk.media.xide.client.parsing.elements.support;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
//import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.UI.Widget.Slot;
import fi.tkk.media.xide.client.parsing.MyXMLParser;
import fi.tkk.media.xide.client.parsing.ParseElement;
import fi.tkk.media.xide.client.utils.GoodHorizontalAdjustablePanel;

public class SlotElement extends ParseElement {
	String id;
//	public ArrayList<String> components;
 		public SlotElement(){
			super("slot");
//			id = MyXMLParser.slotCounter;
//			components = new ArrayList<String>();
		}
		
		public ParseElement getInstance(){
			return (new SlotElement()); 	
		}

		public void setID (String id) {
			this.id = id;
		}

		public Widget draw() {
			
			//return super.Create();
			HTML html = new HTML("<div id = \"" + id +  "\"" + "</div>");

//			components.clear();
//			ArrayList<ParseElement> compCalls = children.iterator().next().getChildren();
//			for (Iterator<ParseElement> iterator = compCalls.iterator(); iterator.hasNext();) {
//				ParseElement o = iterator.next();
//				if (o instanceof ComponentCallElement) {
////					ComponentCallElement compCall = (ComponentCallElement) o;
//					components.add(o.getAttributeValueByName("name"));
//				}
//			}
			
			return html;
		}
	}
