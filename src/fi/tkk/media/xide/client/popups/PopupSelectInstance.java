package fi.tkk.media.xide.client.popups;

import java.util.Iterator;
import java.util.LinkedHashMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.Data.Selectable;
import fi.tkk.media.xide.client.DnDTree.DnDTreeItem;
import fi.tkk.media.xide.client.Tabs.ExplorerTreeTab;
import fi.tkk.media.xide.client.Tabs.SearchComponentTab;
import fi.tkk.media.xide.client.UI.Widget.Component;
import fi.tkk.media.xide.client.UI.Widget.WebPage;
import fi.tkk.media.xide.client.fs.XIDEFile;
import fi.tkk.media.xide.client.popups.utils.PopupBase;
import fi.tkk.media.xide.client.popups.utils.PopupWithScrollBase;
import fi.tkk.media.xide.client.popups.utils.interfaces.Action;

/**
 * Creates a popup showing first step of new application creation process. 
 * It shows list of alternative ways how how you can create new application. 
 * Opens several different popups which are: 
 * PopupCreateNewApp1
 * PopupCreateNewApp2
 * PopupCreateNewApp3
 * @author evgeniasamochadina
 *
 */
public class PopupSelectInstance extends PopupWithScrollBase implements ClickListener{
	public static String TEXT1 = "Search for Application Template and make  new application based on it";
	public static String TEXT2 = "Search for the XFormsDB Application and copy it's source";
	public static String TEXT3 = "Enter URL of the published XFormsDB Application and copy it's source";
	public static String TEXT4 = "Create application from scratch";
	
	String currentSelectedButton = "";
	Button nextButton;
	
	Property bindingProperty;
	LinkedHashMap<String, XIDEFile> instances;
	public  PopupSelectInstance(Property bP) {
		super("Binding Wizard: select instance (1/2)", " You are going to bind a parameter value to the data instance element. This procedure consist of 2 steps: 1 - you will be offered to select" +
				" the instance you want to use, and then " +
				"2 - select the concrete element from the instance structure you want to bind a value to. Now you can see all instances, which are visible for selected parameter. Please select the instance you want to use for binding and click Select button.");
//		this.setSize("80%", "80%");
		DOM.setStyleAttribute(popup.getElement(), "width", "800px");
		
		bindingProperty = bP;
		
		Grid grid = new Grid();
		grid.setCellPadding(7);
		
		Selectable selected = Main.getInstance().getSelectedElement();
		int nativeInstanceNumber = 0;
		
		// Get list of instances to show
		Component element = null;
		if (selected instanceof Component) {
			element = (Component) selected;
		}
		else if (selected instanceof DnDTreeItem && ((DnDTreeItem)selected).getPageElement() instanceof Component) {
			element = (Component)((DnDTreeItem)selected).getPageElement();
		}
		if (selected != null ) {
			
			instances = selected.getProperties().getDataInstances();
			nativeInstanceNumber = instances.size();
			if (element instanceof WebPage) {}
			else {
				LinkedHashMap<String, XIDEFile> instancesOfParent = element.getParentElement().getParentElement().getProperties().getDataInstances();
				instances.putAll(instancesOfParent);
			}
			
			
			// Resize the grid according to the amount of instances + 2 for info lines
			grid.resize(instances.keySet().size() + 2, 1);
			
			Label nativeInstancesLabel =  new Label("Instances of this object:");
			grid.setWidget(0, 0, nativeInstancesLabel);
			
			int i = 1;
			for (Iterator<String> iterator = instances.keySet().iterator(); iterator.hasNext();) {
				String instanceName = iterator.next();
				
				if (i == nativeInstanceNumber + 1 && nativeInstanceNumber!=instances.size() + 1) {
					Label parentInstancesLabel =  new Label("Instances of parent object (Web Page):");
					grid.setWidget(i, 0, parentInstancesLabel);
					i++;
				}
				RadioButton button = new RadioButton("group", instanceName);
				button.addClickListener(this);
				grid.setWidget(i,0, button);
				i++;
			}
		}
	
		popup.addContent(grid);
		
		popup.addButton("Select", new ClickHandler() {

			public void onClick(ClickEvent event) {
				instances.get(currentSelectedButton).getContent(new Action() {

					public void doAction() {
						popup.hide();
						// Select element popup
						
						new PopupSelectXMLElement(currentSelectedButton, instances.get(currentSelectedButton).getContent(), bindingProperty);
						
					}});				
			}});
		
		popup.addCloseButton("Cancel");
		popup.showPopup();
	}
	
	public void onClick(Widget sender) {
		if (sender instanceof RadioButton) {
			currentSelectedButton = ((RadioButton)sender).getText();
			//nextButton.setEnabled(true);
		}
		
	}
}
