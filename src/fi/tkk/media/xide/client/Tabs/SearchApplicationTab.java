package fi.tkk.media.xide.client.Tabs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.Data.TemplateShortInfo;
import fi.tkk.media.xide.client.DnD.Book;
import fi.tkk.media.xide.client.DnD.FlexTableRowDropController;
import fi.tkk.media.xide.client.Server.RPC.SearchService;
import fi.tkk.media.xide.client.Server.RPC.SearchServiceAsync;
import fi.tkk.media.xide.client.Tabs.basetabs.SearchTabBase;
import fi.tkk.media.xide.client.UI.Widget.Component;
import fi.tkk.media.xide.client.UI.Widget.TemplateShortInfoPanel;
import fi.tkk.media.xide.client.popups.basic.PopupError;

public class SearchApplicationTab extends SearchTabBase {
	protected void LoadIcons() {
		super.LoadIcons();
		TagIconsHashMap.put("Component Template", "TagIcons/exclamation_1.gif");
//		alwaysSelectedTags.add("Component Template");
	}
	
	/**
	 * Displays search result (list of templates) on the tab. 
	 * Adds listener to handle mouse click on the 
	 * concrete template and initiate its selection and corresponding actions. 
	 * Manage dnd stuff. 
	 * Before displaying new set of templates removes old one.
	 *  	 
	 * @param result List of templates to display
	 */
	protected void DisplaySearchResult(TemplateShortInfo[] result){
		
		
		// TODO: think about
		TemplateShortInfoPanel.setSearchTab(this);
		
		// Remove previous search results
		if (panelTemplateInfoList != null){
			panelTemplateInfoList.removeFromParent();
		}

		// Panel which contains all search result 
		panelTemplateInfoList = new FlowPanel();
		panelTemplateInfoList.setStyleName("design-template-info-main-panel");
		
		for (int i = 0; i < result.length; i++){
			if (result[i] != null) {
				TemplateShortInfoPanel panel = new TemplateShortInfoPanel(result[i], false);
				panelTemplateInfoList.add(panel);
			}
		}
		
		this.add(panelTemplateInfoList);
		
	}

	public void GetDetailedTemplateInfo(String templateID) {
		// Create an asynchronous callback to handle the result.
		final Template templateResult = null;

		AsyncCallback callback = new AsyncCallback() {

			public void onFailure(Throwable caught) {
				// do some UI stuff to show failure
				new PopupError("Unfortunately getting template details procedure has failed on server!",  caught.getMessage());
			}

			public void onSuccess(Object result) {
				// Display search result
				if (result != null) {
					Template t = (Template)result;
					// TODO: Check is it the same template as selected now
					
//					t.setInfo(TemplateShortInfoPanel.selectedPanel.getInfo());
//					t.properties.putAll(TemplateShortInfoPanel.selectedPanel.getInfo().getPropertiesAsMap());
					TemplateShortInfoPanel.selectedPanel.setTemplate(t);
					// Set selected element and initiate further changes
					Main.getInstance().setSelectedElement(TemplateShortInfoPanel.selectedPanel);
				}
				else{
					// TODO: if no result!!!
				}
			}
		};

		// Make the call
		searchService.getTemplateDetailedInfo(templateID, callback);
	}


	public void SearchTemplatesOnServer() {
		// Create an asynchronous callback to handle the result.
		AsyncCallback callback = new AsyncCallback() {

			public void onFailure(Throwable caught) {
				new PopupError("Unfortunately getting template list procedure has failed on server!",  caught.getMessage());
			}

			public void onSuccess(Object result) {
				// Display search result
				if (result != null) {
					DisplaySearchResult((TemplateShortInfo[]) result);
				}
				else{
					// TODO: if no result!!!
				}
			}
		};

		// Make the call
		String keyString = textBoxSearchString.getText();
		searchService.getTemplates(keyString, selectedTags.toArray(new String[selectedTags.size()]), Template.COMPONENT, callback);
	}
	
}
