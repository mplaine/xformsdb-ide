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
import fi.tkk.media.xide.client.popups.garbage.SmallHintPopup;
import fi.tkk.media.xide.client.utils.GoodHorizontalAdjustablePanel;

public class RepeatElement extends ParseElement {
	// Repeat Icon address
	public static final String REPEAT = "styleImages/XIDE icons/loop_24.png";
	// Structure of the Input Parameters :
	// Ref
	// Label ref

		public RepeatElement(){
			super("repeat");
		}
		
		public ParseElement getInstance(){
			return (new RepeatElement()); 	
		}


		public Widget draw() {
			//return super.Create();
			VerticalPanel mainVerticalPanel = new VerticalPanel();
			mainVerticalPanel.setStyleName("design-parse-element-group");
//			DOM.setStyleAttribute(mainVerticalPanel.getElement(), "border", "1px dotted #FFFFFF");
//			DOM.setStyleAttribute(mainVerticalPanel.getElement(), "padding", "3px");
//			DOM.setStyleAttribute(mainVerticalPanel.getElement(), "background", "#ADBEBE");
			mainVerticalPanel.setWidth("100%");
			
			final SmallHintPopup popup = new SmallHintPopup();

			
			Button buttonRepeat = new Button() {
				@Override
				public void onBrowserEvent(Event event) {
					if (event.getTypeInt() == Event.ONMOUSEOVER) {
						popup.displayPopupCorrectly();
					}
					if (event.getTypeInt() == Event.ONMOUSEOUT) {
							popup.hide();
					}
					if (event.getTypeInt() == Event.ONCLICK) {
						popup.hide();
					}
					super.onBrowserEvent(event);
				}
			};
			popup.setWidget(buttonRepeat);
			popup.initPopup("This is a repeat element, which allows you to get a list of repeating data elements out to your screen");
			buttonRepeat.sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEMOVE
					| Event.ONMOUSEOUT|Event.ONCLICK);
			
			buttonRepeat.sinkEvents(Event.ONMOUSEDOWN);
			buttonRepeat.setHTML("<img src=\""+REPEAT +"\" class=\"gwt-Image\"/>");
//			buttonRepeat.setStyleName("design-parse-element-action-icon");
			DOM.setStyleAttribute(buttonRepeat.getElement(), "marginTop", "-15px");
			DOM.setStyleAttribute(buttonRepeat.getElement(), "marginLeft", "-13px");
			DOM.setStyleAttribute(buttonRepeat.getElement(), "border", "1px dotted #FFFFFF");
			DOM.setStyleAttribute(buttonRepeat.getElement(), "padding", "3px");
			DOM.setStyleAttribute(buttonRepeat.getElement(), "background", "#ADBEBE");
//			buttonRepeat.
		    // Make a new button that does something when you click it.
			
//			// TODO: add styles
//		    buttonRepeat.addClickListener(new ClickListener() {
//		      public void onClick(Widget sender) {
//		        Window.alert("Crash...");
//		      }
//		    });

//		    HorizontalPanel hp = new HorizontalPanel();
//			hp.setWidth("100%");
			
			GoodHorizontalAdjustablePanel buttonHP = new GoodHorizontalAdjustablePanel("left", "right") ;
			buttonHP.setLeftWidget(buttonRepeat);
			
			

			
			mainVerticalPanel.add(buttonHP);
//			DOM.setStyleAttribute(hp.getElement(), "", "");
//			TODO: add css
//			hp.addStyleName(style);



//			Label inputLabel = new Label(label);
//			inputLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//			hp.add(inputLabel);
			for (int i = 0; i < children.size(); i++) {
				mainVerticalPanel.add ( ((ParseElement)children.get(i)).draw() );
			}

			final TextBox inputTextBox = new TextBox();
			mainVerticalPanel.add(inputTextBox );

			return mainVerticalPanel;
		}
	}
