package fi.tkk.media.xide.client.parsing.elements.support;

import java.util.ArrayList;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.parsing.ParseElement;
import fi.tkk.media.xide.client.utils.GoodHorizontalAdjustablePanel;

public class CaseElement extends ParseElement {
	// Structure of the Input Parameters :
	// Ref
	// Label ref

	public static final String SWITCH = "arrow_right.png";
	
		public CaseElement(){
			super("case");
		}
		
		public ParseElement getInstance(){
			return (new CaseElement()); 	
		}


		public Widget draw() {
//			TODO: add css
//			hp.addStyleName(style);

			VerticalPanel mainVerticalPanel = new VerticalPanel();
			DOM.setStyleAttribute(mainVerticalPanel.getElement(), "border", "2px solid lightGray");
			DOM.setStyleAttribute(mainVerticalPanel.getElement(), "backgroundColor", "white");
			DOM.setStyleAttribute(mainVerticalPanel.getElement(), "padding", "2px");
			DOM.setStyleAttribute(mainVerticalPanel.getElement(), "margin", "4px");
			
			
			Image image = new Image(SWITCH);
			DOM.setStyleAttribute(image.getElement(), "marginTop", "-10px");
			DOM.setStyleAttribute(image.getElement(), "marginLeft", "-15px");
			DOM.setStyleAttribute(image.getElement(), "border", "2px solid lightGray");
			DOM.setStyleAttribute(image.getElement(), "backgroundColor", "white");
			DOM.setStyleAttribute(image.getElement(), "padding", "2px");

			GoodHorizontalAdjustablePanel buttonHP = new GoodHorizontalAdjustablePanel("left", "right");
			buttonHP.setLeftWidget(image);
			
			mainVerticalPanel.add(buttonHP);
			
			for (int i = 0; i < children.size(); i++) {
				mainVerticalPanel.add ( ((ParseElement)children.get(i)).draw() );
			}
			
			return mainVerticalPanel ;
		}
	}
