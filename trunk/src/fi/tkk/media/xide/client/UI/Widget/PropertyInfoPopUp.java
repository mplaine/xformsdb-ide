/**
 * 
 */
package fi.tkk.media.xide.client.UI.Widget;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.Data.Property;

/**
 * @author Evgenia Samochadina
 * @date Dec 18, 2008
 *
 */
public class PropertyInfoPopUp extends PopupPanel {

	private TextBox textBox;
	private Property p;

	private void UpdateP(String value) {
		p.setValue(value);
		Main.getInstance().UpdateUI();
	}
	
	public PropertyInfoPopUp(Property p) {
		super(false, true);
		this.p = p;
		this.setStyleName("design-Popup");
		//this.setWidth("460px");
		VerticalPanel panel = new VerticalPanel();

		HorizontalPanel hp = new HorizontalPanel();
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		
		Label title = new Label(p.getName());
		title.addStyleName("design-LabelMiddleText-bold");
		hp.add(title);
//		panel.add(title);
		
		if (p.getProperty_type() == Property.TYPE_PARAMETER){
			
			Label type = new Label("   ("  + p.getType() + ")");
			type.addStyleName("design-LabelSmallText-Non-Italic");
			
			hp.add(type);
		}
		
		panel.add(hp);
		

		Label descr = new Label(p.getDescription());
		descr.addStyleName("design-LabelSmallText");
		
		panel.add(descr);
		
		panel.add(descr);
		

		// Buttons
		HorizontalPanel hpButton = new HorizontalPanel();
		hpButton.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		hpButton.addStyleName("design-button-right");
		// Cancel button
		Button buttonCancel = new Button("Cancel");
		buttonCancel.addClickListener(new ClickListener(){
	    	  public void onClick(Widget sender) {
	    		  (PropertyInfoPopUp.this).hide();
	    	}
	      });
//		buttonCancel.addStyleName("design-button-right");
		hpButton.add(buttonCancel);
		
		if (p.isEditableNow()) {
			textBox = new TextBox();
			textBox.setWidth("97%");
			if (p.getValue() != null){
				textBox.setText(p.getStringValue());
			}
			else if (p.getDefaultValue() != null){
				textBox.setText(p.getDefaultValue());
			}
			else {
				textBox.setText("&nbsp;");
			}
			panel.add(textBox);
			
			// OK button
			Button buttonSave = new Button("  Ok  ");
			buttonSave.addClickListener(new ClickListener(){
		    	  public void onClick(Widget sender) {
		    		  if (textBox != null) {
		    			  UpdateP(textBox.getText());
		    			  }
		    		  (PropertyInfoPopUp.this).hide();
		    	}
		      });
		      
//			buttonSave.addStyleName("design-button-Save");
			hpButton.add(buttonSave);
		
		}
		else {
			Label text;
			
			if (p.getValue() != null){
				text = new Label(p.getStringValue());
			}
			else if (p.getDefaultValue() != null){
				text = new Label(p.getDefaultValue());
			}
			else {
				text = new Label("  ");
			}
			text.addStyleName("design-label-disabled");
			panel.add(text);
		}
		panel.add(hpButton);

		this.setWidget(panel);
		this.setWidth("460px");
	}
}
