package fi.tkk.media.xide.client.parsing.elements.support;

import java.util.ArrayList;
import java.util.TreeMap;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
//import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.parsing.ParseElement;
import fi.tkk.media.xide.client.utils.GoodHorizontalAdjustablePanel;

public class WrapElement extends ParseElement {
		public WrapElement(){
			super("sourcecode");
		}
		
		public ParseElement getInstance(){
			return (new WrapElement()); 	
		}


		public Widget draw() {
			//return super.Create();
			VerticalPanel mainVerticalPanel = new VerticalPanel();
			DOM.setStyleAttribute(mainVerticalPanel.getElement(), "border", "4px solid gray");
			DOM.setStyleAttribute(mainVerticalPanel.getElement(), "padding", "3px");
			mainVerticalPanel.setSize("100%", "100%");
			
			for (int i = 0; i < children.size(); i++) {
				mainVerticalPanel.add ( ((ParseElement)children.get(i)).draw() );
			}

			return mainVerticalPanel;
		}
	}
