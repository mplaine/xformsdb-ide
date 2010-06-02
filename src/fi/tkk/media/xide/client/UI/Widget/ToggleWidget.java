package fi.tkk.media.xide.client.UI.Widget;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.Widget;
/**
 * Toggle widget maintains visibility of 2 objects only one of which should be represented.
 * Objects are switched by mouse click
 *  
 * @author evgeniasamochadina
 *
 */
public class ToggleWidget extends SimplePanel implements ClickListener{
	Widget firstWidget;
	Widget secondWidget;
	boolean isFirstWidgetSelected;


	/**
	 * Initiates ToggleWidget with widgets and shows first one
	 *   
	 * @param firstWidget first widget (implements SourceClickEvent) to show
	 * @param secondWidget second widget (implements SourceClickEvent) to show
	 */
	public ToggleWidget(SourcesClickEvents firstWidget, SourcesClickEvents secondWidget) {
		this.firstWidget = (Widget)firstWidget;
		this.secondWidget = (Widget)secondWidget;
		
		isFirstWidgetSelected = true; 
			
//		firstWidget.addClickListener(this);
//		secondWidget.addClickListener(this);
		this.add(this.firstWidget);
	}

	public void onClick() {
		if (isFirstWidgetSelected) {
			this.remove(firstWidget);
			this.add(secondWidget);
			isFirstWidgetSelected = false; 
		}
		else {
				this.remove(secondWidget);
				this.add(firstWidget);
				isFirstWidgetSelected = true; 
		}
	}
	
	// Is not used anymore
	
	public void onClick(Widget sender) {
//		System.out.println("on click");
		if(sender == firstWidget) {
//			System.out.println("1");
			
			this.remove(firstWidget);
			this.add(secondWidget);
			isFirstWidgetSelected = false; 
			
		}
		if (sender == secondWidget) {
//			System.out.println("2");
			this.remove(secondWidget);
			this.add(firstWidget);
			isFirstWidgetSelected = true; 
		}
	}
	
	/**
	 * Changes the icon without click 
	 *
	 */
	public void changeIcon() {
		if (this.getWidget() == firstWidget) {
			// Should be changed to the second one
			this.remove(firstWidget);
			this.add(secondWidget);
			isFirstWidgetSelected = false;
		}
		else {
			// Should be changes to the first one
			this.remove(secondWidget);
			this.add(firstWidget);
			isFirstWidgetSelected = true;
		}
	}
	
	public Widget getCurrentWidget() {
		if (this.getWidget() == firstWidget) {
			return firstWidget;
		}
		else {
			return secondWidget;
		}
	}
	
	public boolean isFirstWidgetSelected() {
		return isFirstWidgetSelected;
	}
}
