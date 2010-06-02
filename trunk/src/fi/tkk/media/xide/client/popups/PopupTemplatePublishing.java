package fi.tkk.media.xide.client.popups;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.Data.TemplateShortInfo;
import fi.tkk.media.xide.client.Tabs.PLTreeItem;
import fi.tkk.media.xide.client.popups.basic.Popup;
import fi.tkk.media.xide.client.popups.utils.PopupBase;

public class PopupTemplatePublishing extends PopupBase{
	Template template;
	PLTreeItem item;
	boolean publish;
	
	
	Property id; 
	Property title; 
	Property descr; 
	Property tags ;
	
	// Table where properties are displayed
	Grid table ;
	
	public PopupTemplatePublishing(Template template, PLTreeItem item) {
		super("Publish template", "You are going to make this template public. The template will be avaliable in the database for all XIDE users." +
		"Please verify and/or change template's information and press OK button");
		
		// "Welcome to Template Publish wizard! "
		
		this.template = template;
		this.item = item;
		VerticalPanel vp = new VerticalPanel();
		vp.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
		
		Label labelTitle = new Label("You are going to publish template.");
		labelTitle.setWordWrap(true);
		DOM.setStyleAttribute(labelTitle.getElement(), "fontSize", "90%");
		DOM.setStyleAttribute(labelTitle.getElement(), "fontWeight", "bold");
		DOM.setStyleAttribute(labelTitle.getElement(), "paddingBottom", "2px");
		vp.add(labelTitle);

		labelTitle = new Label("Please verify template's information:");
		labelTitle.setWordWrap(true);
		DOM.setStyleAttribute(labelTitle.getElement(), "fontSize", "90%");
		DOM.setStyleAttribute(labelTitle.getElement(), "fontWeight", "bold");
		DOM.setStyleAttribute(labelTitle.getElement(), "paddingBottom", "5px");
		vp.add(labelTitle);

		
		table = new Grid();
		vp.add(table);
		
		table.resize(4, 3);
		// Create a copy of template properties for editing
		id = template.getProperties().get(Property.ID); 
		title = template.getProperties().get(Property.TITLE); 
		descr = template.getProperties().get(Property.DESCR); 
		tags = template.getProperties().get(Property.TAGS);
		
		// Display copied properties 
		displayProperty(id, Property.ID, 0);
		displayProperty(title, Property.TITLE, 1);
		displayProperty(descr, Property.DESCR, 2);
		displayProperty(tags, Property.TAGS, 3);
		
//		final ModalDimmedPopup popup = new ModalDimmedPopup(vp);
		
		popup.addContent(vp);
		
		// Buttons
		
//		Button cancelButton = new Button();
//		cancelButton.setText("Cancel");
//		cancelButton.addClickListener(new ClickListener() {
//			public void onClick(Widget sender) {
//				PopupTemplatePublishing.this.closePopup();
//			}
//			
//		});
		
//
//		DOM.setStyleAttribute(cancelButton.getElement(), "float", "right");
//		DOM.setStyleAttribute(cancelButton.getElement(), "padding", "3px");
//		DOM.setStyleAttribute(cancelButton.getElement(), "width", "80px");
//		hpButtons.add(cancelButton);

		popup.addButton("OK", new ClickHandler() {

			public void onClick(ClickEvent event) {
				PublishTemplate();
				
				new Popup("Template has sucessfully published!");				
			}});
		
		popup.addCloseButton("Cancel");
		
//		DOM.setStyleAttribute(okButton.getElement(), "float", "right");
//		DOM.setStyleAttribute(okButton.getElement(), "padding", "3px");
//		DOM.setStyleAttribute(okButton.getElement(), "width", "80px");
//		hpButtons.add(okButton);
//		
//		vp.add(hpButtons);
		
//		popup.showPopup(true);
		
	}
	
	private void displayProperty(final Property property, String propertyCode, int rowNumber) {
		table.getCellFormatter().setWidth(rowNumber, 2, "10px");
		table.getCellFormatter().setWordWrap(rowNumber, 1,true);
		
		Label title = new Label(property.getName());
		
		TextBoxBase tb;
		// TODO: update checking
		if ((propertyCode.equals(Property.ID)) 
				|| (propertyCode.equals(Property.TITLE)))
			{
				tb = new TextBox();
				tb.setWidth("100%");
				DOM.setStyleAttribute(tb.getElement(), "margin", "0px");
			}
		else {
			tb = new TextArea();
		}
		
		tb.setText(property.getStringValue());
		tb.addChangeListener(new ChangeListener() {

			public void onChange(Widget sender) {
				property.setValue(((TextBoxBase) sender).getText());
			}
			
		});
		
		table.setWidget(rowNumber, 0, title);
		table.setWidget(rowNumber, 1, tb);
		
		// If it's ID 
		if (propertyCode.equals(Property.ID)) {
			final Button buttonCheckIdentity = new Button();
			buttonCheckIdentity.setText("Check");
			
			buttonCheckIdentity.addClickListener(new ClickListener() {
				public void onClick(Widget sender) {
//					buttonCheckIdentity.removeClickListener(this);
					int width = buttonCheckIdentity.getOffsetWidth();
					buttonCheckIdentity.setHTML("<img src=\"TagIcons/drop-yes.gif\" class=\"gwt-Image\"/>");
					buttonCheckIdentity.setEnabled(false);
					buttonCheckIdentity.setWidth(width + "px");
//					new ModalDimmedPopup("This ID is ok");
				}
			});
			
			table.setWidget(rowNumber, 2, buttonCheckIdentity);
		}
	}

	public void PublishTemplate() {
		template.properties.remove(Property.ID);
		template.properties.put(Property.ID, id);

		template.properties.remove(Property.TITLE);
		template.properties.put(Property.TITLE, title);
		
		template.properties.remove(Property.DESCR);
		template.properties.put(Property.DESCR, descr);
		
		template.properties.remove(Property.TAGS);
		template.properties.put(Property.TAGS, tags);
		item.sendPublishedTemplate(template);
		
	}
}
