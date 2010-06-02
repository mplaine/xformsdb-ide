package fi.tkk.media.xide.client.parsing.elements.form;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.SliderBar;

import fi.tkk.media.xide.client.parsing.ParseElement;

public class RangeElement extends ParseElement {
		/**
		 *  Attributes:
		 *  - start
		 *  - end
		 *  - step
		 *  - 
		 */
	
		public RangeElement(){
			super("range");
		}
		
		public ParseElement getInstance(){
			return (new RangeElement()); 	
		}


		public Widget draw() {
			//return super.Create();
//			TODO: add css

			HorizontalPanel hp = new HorizontalPanel();
			hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			hp.setWidth("100%");

			// Label
			hp.add ( ((ParseElement)children.get(0)).draw() );
			
			// Slider
			int start = 0;
			int end = 10;
			int step = 1;

			// Parse attributes
			try {
				if (parameters.get("start") != null) {
					start = Integer.parseInt(parameters.get("start"));
	//				start = Integer.parseInt();
				}
				if (parameters.get("end") != null) {
					end = Integer.parseInt(parameters.get("end"));
				}
				if (parameters.get("step") != null) {
					step = Integer.parseInt(parameters.get("step"));
				}
			} catch (Exception e) {
				// the value cannot be parsed
			}
			
			SliderBar slider = new SliderBar(start, end);
			slider.setEnabled(false);
			slider.setStepSize(step);
			slider.setCurrentValue(start);
			slider.setNumTicks(10);
			slider.setNumLabels(5);
			slider.setWidth("150px");
			
			hp.add(slider);
		
			return hp;
		}
	}
