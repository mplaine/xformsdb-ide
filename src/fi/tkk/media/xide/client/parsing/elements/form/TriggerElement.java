package fi.tkk.media.xide.client.parsing.elements.form;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.parsing.ParseElement;

public class TriggerElement extends ParseElement {
	
	public TriggerElement(){
		super("trigger");
	}
	
	public ParseElement getInstance(){
		return (new TriggerElement()); 	
	}


	public Widget draw() {
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSize("100%", "100%");
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		// Draw label 
		ParseElement label = getChildByName("label");
		//If there is a label incide
		
//			hp.add ( label.draw() );
		if (label != null) {	
			String type = parameters.get("appearance");
			if (type.equals("full")) {
				// Just button
				
					Button b = new Button(label.draw().getElement().getInnerHTML()) {
						@Override
						public void onBrowserEvent(Event event) {
							// TODO Auto-generated method stub
							if (event.getTypeInt() == Event.ONCLICK || event.getTypeInt() ==Event.ONMOUSEDOWN)   {
								int i = 0;
								i = i/i;
							}
							else {
							super.onBrowserEvent(event);
							}
						}
					};
					b.sinkEvents(Event.BUTTON_LEFT| Event.ONMOUSEDOWN | Event.MOUSEEVENTS);
//					b.setClickListener(null);
//					b.setEnabled(false);
					hp.add(b);
				}
			
			else if (type.equals(MIN)) {
				// Link or image
				// if there is a link
//				if (label.getValue() != null) {
//					Hyperlink b = new Hyperlink();
//					b.setText(label.getValue());
//					hp.add(b);
//				}
//				else {
//					ParseElement image = label.getChildByName("img");
//					hp.add(image.draw());
//				}
				hp.add(label.draw());
			}
	}			

		return hp;
	}
}

