package fi.tkk.media.xide.client.popups;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.popups.utils.PopupBase;
import fi.tkk.media.xide.client.popups.utils.interfaces.Action;

public class PropertyEditingPopup extends PopupBase implements ClickListener{

	Property parameterProperty;
	TextArea textArea;
	
	public PropertyEditingPopup(Property pProperty) {

		
		super("Edit parameter", "You are going to edit" + pProperty.getName() + " parameter ("+ "" + pProperty.getDescription() + "). Please edit the value and press OK button.");
		
		this.parameterProperty = pProperty;
		
		VerticalPanel hp = new VerticalPanel();
		hp.setSize("100%", "100%");
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
//		Label paramName = new Label ("" + parameterProperty.getName());
//		Label paramDescr = new Label ("" + parameterProperty.getDescription());
//		Label paramType = new Label ("" + parameterProperty.getProperty_type());

//		hp.add(paramName);
//		hp.add(paramDescr);
//		hp.add(paramType);
		
		textArea = new TextArea();
		textArea.setSize("100%", "44px");
		DOM.setStyleAttribute(textArea.getElement(), "fontFamily", "arial,sans-serif");
		DOM.setStyleAttribute(textArea.getElement(), "padding", "3px 0px 8px 12px ");
		textArea.setWidth("100%");
		
		textArea.setText(parameterProperty.getStringValue());
		hp.add(textArea);
		
		popup.addContent(hp);
		
		popup.addButton("Ok", new ClickHandler() {

			public void onClick(ClickEvent event) {
				parameterProperty.setValue(textArea.getText());
				popup.hide();
				Main.getInstance().UpdateUI(Main.BOTTOM_TAB);				
			}});
		popup.addButton("Cancel", new ClickHandler() {

			public void onClick(ClickEvent event) {
				popup.hide();
				Main.getInstance().UpdateUI(Main.BOTTOM_TAB);				
			}});
		
		popup.showPopup();

	}

	public void onClick(Widget sender) {
	}
	
	

}
