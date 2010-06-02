package fi.tkk.media.xide.client.parsing.elements.dynamic;

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

public class SwitchElement extends ParseElement {
	// Repeat Icon address
	public static final String SWITCH = "styleImages/XIDE icons/switch_24.png";
	boolean showAll = true;
	// Structure of the Input Parameters :
	// Ref
	// Label ref

		public SwitchElement(){
			super("switch");
		}
		
		public ParseElement getInstance(){
			return (new SwitchElement()); 	
		}


		public Widget draw() {
			//return super.Create();
			VerticalPanel mainVerticalPanel = new VerticalPanel();
			mainVerticalPanel.setStyleName("design-parse-element-group");
//			DOM.setStyleAttribute(mainVerticalPanel.getElement(), "border", "4px solid gray");
//			DOM.setStyleAttribute(mainVerticalPanel.getElement(), "padding", "3px");
			mainVerticalPanel.setWidth("100%");
			
			Button buttonRepeat = new Button() {
				@Override
				public void onBrowserEvent(Event event) {
					if (event.getTypeInt() == Event.ONMOUSEDOWN) {
						showAll = !showAll;
						event.cancelBubble(true);
					}
					else {
						super.onBrowserEvent(event);
					}
				}
			};
			buttonRepeat.sinkEvents(Event.ONMOUSEDOWN);
			buttonRepeat.setHTML("<img src=\""+SWITCH +"\" class=\"gwt-Image\"/>");
			DOM.setStyleAttribute(buttonRepeat.getElement(), "marginTop", "-15px");
			DOM.setStyleAttribute(buttonRepeat.getElement(), "marginLeft", "-13px");
			DOM.setStyleAttribute(buttonRepeat.getElement(), "border", "1px dotted #FFFFFF");
			DOM.setStyleAttribute(buttonRepeat.getElement(), "padding", "3px");
			DOM.setStyleAttribute(buttonRepeat.getElement(), "background", "#ADBEBE");
//			buttonRepeat.
		    // Make a new button that does something when you click it.
			
			// TODO: add styles
//		    buttonRepeat.addClickListener(new ClickListener() {
//		      public void onClick(Widget sender) {
//		        Window.alert("Crash...");
//		      }
//		    });

//		    HorizontalPanel hp = new HorizontalPanel();
//			hp.setWidth("100%");
			
			GoodHorizontalAdjustablePanel buttonHP = new GoodHorizontalAdjustablePanel("left", "right");
			buttonHP.setLeftWidget(buttonRepeat);
			
			mainVerticalPanel.add(buttonHP);
//			DOM.setStyleAttribute(hp.getElement(), "", "");
//			TODO: add css
//			hp.addStyleName(style);



			// At the beginning draw only first case
			if (showAll) {
				for (int i = 0; i < children.size(); i++) {
					mainVerticalPanel.add ( ((ParseElement)children.get(i)).draw() );
				}
			}
			else {
				mainVerticalPanel.add ( ((ParseElement)children.get(0)).draw() );
			}


			return mainVerticalPanel;
		}
	}
