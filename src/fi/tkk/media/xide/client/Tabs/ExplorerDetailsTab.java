/**
 * 
 */
package fi.tkk.media.xide.client.Tabs;

import java.util.Iterator;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.Data.APElement;
import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.Data.Selectable;
import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.Data.VariableProperties;
import fi.tkk.media.xide.client.Tabs.ExplorerTreeItem;
import fi.tkk.media.xide.client.UI.Widget.AdvancedTableV2;
import fi.tkk.media.xide.client.UI.Widget.Component;
import fi.tkk.media.xide.client.UI.Widget.GoodButton;

/**
 * @author Evgenia Samochadina
 * @date Nov 25, 2008
 * 
 */
public class ExplorerDetailsTab extends BottomBasicTab {
//	public Panel p;
	// UI widgets
	private AdvancedTableV2 detailsTable;
	Button addNewButton; 
	
	public static String[] HEADER_DETAILS = {"Title", "Description", "Date Created", "URL", "Date Published", "Is Published?",};
//		"Title", "Description", "Date Created", "URL", "Date Published", "Is Published?"};
	
	String[][] data =  {{"Test application", "This is a description", "02.01.09", "06.01.09", "-", "none"},
			{"Contact manager", "This is a description", "12.01.09", "14.01.09", "23.01.09", "none"},
			{"Blog", "This is a description", "24.01.09", "25.01.09", "-", "none"}};
	
	public ExplorerDetailsTab() {
		
		
		detailsTable = new AdvancedTableV2(HEADER_DETAILS);
		
		this.add(detailsTable.getTable());
		addNewButton = new GoodButton(6);
//		addNewButton.setHTML("<img src=\""+ADD +"\" class=\"gwt-Image\"/> Add new child here...");
		addNewButton.setHTML("<img src=\"" +"\" class=\"gwt-Image\"/> Add new child here...");
		this.add(addNewButton);
		addNewButton.addClickListener(new ClickListener() {
			
			public void onClick(Widget sender) {
				APElement selectedElement = (APElement)((ExplorerTreeItem)Main.getInstance().getSelectedElement()).getProperties();
				if (selectedElement != null && selectedElement.getContextMenuItems()[APElement.ACTION_ADD] != null) {
					selectedElement.getContextMenuItems()[APElement.ACTION_ADD].doAction();
				}
			}});

//		UpdateUI();
	}


	public void UpdateUI() {
		detailsTable.RemoveAllData();

		if (Main.getInstance().getSelectedElement() != null) {
//			 APElement selectedElement = (APElement)((ExplorerTreeItem)Main.getInstance().getSelectedElement()).getProperties();
			 APElement selectedElement = ExplorerTreeTab.getInstance().all_app;

			// For each children of the currently selected element
			for (Iterator<APElement> iterator = selectedElement.getChildren().iterator(); iterator.hasNext();) {
				APElement childElement = iterator.next();

				// Display its properties
				detailsTable.AddData(childElement);
			}
			
			// ADD BUTTON
			// TODO: uncomment!
//			if (selectedElement != null && selectedElement.getContextMenuItems()[APElement.ACTION_ADD] != null) {
//				addNewButton.setEnabled(true);
//				this.add(addNewButton);
////				addNewButton = new GoodButton(6);
////				addNewButton.setHTML("<img src=\""+ADD +"\" class=\"gwt-Image\"/> Add new child here...");
////				this.add(addNewButton);
////				
//			}
//			else {
//				addNewButton.setEnabled(false);
//				addNewButton.removeFromParent();
//			}
			
		}
		else {
			 APElement selectedElement = ExplorerTreeTab.getInstance().all_app;
				// For each children of the currently selected element
				for (Iterator<APElement> iterator = selectedElement.getChildren().iterator(); iterator.hasNext();) {
					APElement childElement = iterator.next();

					// Display its properties
					detailsTable.AddData(childElement);
				}
		}
	}

	public void addFakeData() {
		for (int i = 0; i < 3; i++) {
			detailsTable.AddData(data);
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
