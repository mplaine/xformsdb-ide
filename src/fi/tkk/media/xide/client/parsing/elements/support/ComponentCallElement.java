package fi.tkk.media.xide.client.parsing.elements.support;

import java.util.ArrayList;
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
import com.google.gwt.user.client.ui.TextBox;
//import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.UI.Widget.Slot;
import fi.tkk.media.xide.client.parsing.MyXMLParser;
import fi.tkk.media.xide.client.parsing.ParseElement;
import fi.tkk.media.xide.client.utils.GoodHorizontalAdjustablePanel;

public class ComponentCallElement extends ParseElement {
	int id;
	
		public ComponentCallElement(){
			super("ComponentCall");
			id = MyXMLParser.slotCounter;
		}
		
		public ParseElement getInstance(){
			return (new ComponentCallElement()); 	
		}


		public Widget draw() {
			return null;
		}
	}
