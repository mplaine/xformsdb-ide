/**
 * 
 */
package fi.tkk.media.xide.client.Tabs;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.View;
import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.Data.Selectable;
import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.Data.VariableProperties;
import fi.tkk.media.xide.client.DnDTree.DnDTreeItem;
import fi.tkk.media.xide.client.UI.Widget.AdvancedTableV2;
import fi.tkk.media.xide.client.UI.Widget.BasicPageElement;
import fi.tkk.media.xide.client.UI.Widget.Component;
import fi.tkk.media.xide.client.utils.PagePanel;

/**
 * @author Evgenia Samochadina
 * @date Nov 25, 2008
 * 
 */
public class PropertiesTab extends BottomBasicTab {
	public final int tabID;
	
	// UI widgets
	private AdvancedTableV2 parameterTable = null;
	private AdvancedTableV2 propertyTable = null;

	public static String[] HEADER_PROPERTIES = {"Name", "Value"};
	public static String[] HEADER_PARAMETERS = {"Name", "Type", "Value", "Actions"};
	
	private Panel hp;
	
//	public static PropertiesTab getInstance() {
//		return instance;
//	}
	
	private Label welcomeLabel;
	
	public PropertiesTab(boolean showPropertiesTab, boolean showParametersTab, boolean showSplitter, int tabID) {
		super();
		this.tabID = tabID;
		// Main horizontal split panel
		if (showPropertiesTab&showParametersTab || showSplitter) {
			hp = new HorizontalSplitPanel();
			hp.setWidth("100%");
			hp.setHeight("100%");
			((HorizontalSplitPanel)hp).setSplitPosition("50%");
			
			if (showPropertiesTab) {
				propertyTable = new AdvancedTableV2(HEADER_PROPERTIES);
				((HorizontalSplitPanel)hp).setLeftWidget(propertyTable.getTable());
			}
			
			if (showParametersTab) {
				parameterTable = new AdvancedTableV2(HEADER_PARAMETERS);
				((HorizontalSplitPanel)hp).setRightWidget(parameterTable.getTable());
			}
		}
		else {
			hp = new HorizontalPanel();
			hp.setWidth("100%");
			hp.setHeight("100%");
			
				if (showPropertiesTab) {
					propertyTable = new AdvancedTableV2(HEADER_PROPERTIES);
					AbsolutePanel panel = new AbsolutePanel();
					panel.setSize("100%", "100%");
					panel.add(new HTML("<div style=\"background:#E5f5cd; position: absolute; padding:20px 10px 12px 10px; left:0px; right:0px; top:0px; z-index: -1;\"> </div>"));
					panel.add(propertyTable.getTable());
					panel.add(new HTML("<div style=\"url('styleImages/bottom_gradient.png') repeat-x top; position: absolute; padding:18px 13px 18px 13; left:0px; right:0px; bottom:0px; z-index: -1;\"> </div>"));

					hp.add(panel);
				}
				
				if (showParametersTab) {
					parameterTable = new AdvancedTableV2(HEADER_PARAMETERS);
					hp.add(parameterTable.getTable());
				}
//			hp.getElement().appendChild(new HTML("<div style=\"background:#E0E6EA; position: absolute; heigth:20px; right:0px; top:0px; bottom:0px;\"> </div>"));
		}
		
		welcomeLabel = new Label("Select element from the left to see it's properties");
		welcomeLabel.setStyleName("design-properties-table-welcome-text");
		this.add(welcomeLabel);
//		System.out.println("properties init finished");
//		this.add(hp);

	}

	void ClearProperties() {

	}

//	public static void UpdateProperties(){
//		for (Iterator<PropertiesTab> iterator = instance.iterator(); iterator.hasNext();) {
//			iterator.next().UpdatePropertiesOfThisTab();
//		}
//	}

//	public static void UpdateAllStyles(){
//		for (Iterator<PropertiesTab> iterator = instance.iterator(); iterator.hasNext();) {
//			iterator.next().UpdateStyles();
//		}
//	}

	public void updatePropertiesListOfThisTab(){
		Selectable selectedElement = Main.getInstance().getSelectedElement();
		if (selectedElement != null) {
//			// Remove data from the table
//			if (propertyTable != null) {
//				propertyTable.RemoveAllData();
//			}
//			if (parameterTable != null) {
//				parameterTable.RemoveAllData();
//			}

			// If Property Table exist in this view
			if (propertyTable != null) {
//				if (selectedElement.getProperties().getShortInfo() != null) {
//					propertyTable.updateProperties(selectedElement.getProperties().getShortInfo().properties);
//				}
				if(selectedElement.getProperties().getProperties() != null) 
				{
					propertyTable.updatePropertiesLinks(selectedElement.getProperties().getProperties());
				}
			}
			
			// If Parameter Table exist in this view
			if (parameterTable != null) { 
				if (selectedElement instanceof Component) {
					// Show parameters of the component. Can be modified
					parameterTable.updatePropertiesLinks(((Component)selectedElement).getParameters());
				}
				else if (selectedElement instanceof DnDTreeItem) {
					// Show parameters of the corresponding element (which is represented by  the item)
					BasicPageElement element = ((DnDTreeItem)selectedElement).getPageElement();
					if (element instanceof Component) {
						parameterTable.updatePropertiesLinks(((Component)element).getParameters());
					}
					else {
						// Slot doesn't have any parameters
						parameterTable.updatePropertiesLinks(null);
					}
					
				}
				else{
					// Show parameters of the template. Cannot be modified
					parameterTable.updatePropertiesLinks(selectedElement.getProperties().getParameters());
				}
			}
			
		}

	}
	
	public void updatePropertiesValuesOfThisTab(){
		Selectable selectedElement = Main.getInstance().getSelectedElement();
		if (selectedElement != null) {
//			// Remove data from the table
//			if (propertyTable != null) {
//				propertyTable.RemoveAllData();
//			}
//			if (parameterTable != null) {
//				parameterTable.RemoveAllData();
//			}

			// If Property Table exist in this view
			if (propertyTable != null) {
					propertyTable.updatePropertiesValues();
			}
			
			// If Parameter Table exist in this view
			if (parameterTable != null) { 
				parameterTable.updatePropertiesValues();

			}
			
		}

	}
	public void UpdateUI() {

		super.UpdateUI();
//		System.out.println("update");
		if (welcomeLabel != null) {
			
			this.remove(welcomeLabel);
			welcomeLabel = null;
			this.add(hp);
//			this.add((new ScrollTableTest()).scrollTable);
//			this.add((new ScrollTableTest2col()).layout);
		}
//		// Saved/Changed style
//		if ((Main.getInstance().getSelectedElement() != null) 
//				&&(Main.getInstance().getSelectedElement().isChanged())) {
//			this.setStyleName("gwt-ScrollTable-changed");
//		}
//		else {
//			this.setStyleName("gwt-ScrollTable");
//		}
		
		// Remove data from the table
		if (propertyTable != null) {
			propertyTable.RemoveAllData();
		}
		if (parameterTable != null) {
			parameterTable.RemoveAllData();
		}

		// Add new Data
		Selectable selectedElement = Main.getInstance().getSelectedElement();
		if (selectedElement != null) {
			// If Property Table exist in this view
			if (propertyTable != null) {
//				if (selectedElement.getProperties().getShortInfo() != null) {
//					propertyTable.AddData(selectedElement.getProperties().getShortInfo().properties);
//				}
				if(selectedElement.getProperties() != null) 
				{
					propertyTable.AddData(selectedElement.getProperties().getProperties());
				}
//				propertyTable.setStyle();
			}
			
			// If Parameter Table exist in this view
			if (parameterTable != null) { 
				if (selectedElement instanceof Component) {
					// Show parameters of the component. Can be modified
					parameterTable.AddData(((Component)selectedElement).getParameters());
				}
				else if (selectedElement instanceof DnDTreeItem) {
					// Show parameters of the corresponding element (which is represented by  the item)
					BasicPageElement element = ((DnDTreeItem)selectedElement).getPageElement();
					if (element instanceof Component) {
						parameterTable.AddData(((Component)element).getParameters());
					}
					else {
						// Slot doesn't have any parameters
						parameterTable.AddData(selectedElement.getProperties().getParameters());
					}
					
				}
				else {
					// Show parameters of the template. Cannot be modified
					parameterTable.AddData(selectedElement.getProperties().getParameters());
				}
			}
			
		}

	}

	@Override
	public void updateWhenKeyUp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateWhenClicked() {
		// TODO Auto-generated method stub
		
	}

}
