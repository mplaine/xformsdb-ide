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
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
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
import com.google.gwt.user.client.ui.HTMLPanel;
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

import fi.tkk.media.xide.client.DnDController;
import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.View;
import fi.tkk.media.xide.client.Data.Tag;
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
import fi.tkk.media.xide.client.utils.PagePanel;

public class SearchComponentTab extends SearchTabBase {
	
	public final int tabID = View.SEARCH;
	
	protected void LoadIcons() {
		super.LoadIcons();
		TagIconsHashMap.put("Atomic", "elementIcons/atomic_16 copy.gif");
		TagIconsHashMap.put("Complex", "elementIcons/complex_16 copy.gif");
		TagIconsHashMap.put("Ready-To-Use", "elementIcons/ready_to_use_16 copy.gif");
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
			for (int i = 0; i < panelTemplateInfoList.getWidgetCount(); i ++) {
				DnDController.getDragControllerAdding().makeNotDraggable(panelTemplateInfoList.getWidget(i));
			}
			
			panelTemplateInfoList.clear();
		}

//		// Panel which contains all search result 
//		panelTemplateInfoList = new FlowPanel();
		
//		super.onLoad();
//		panelTemplateInfoList.setHeight((panelTemplateInfoList.getOffsetHeight()
//				-hp.getOffsetHeight()
//				+panelTemplateInfoList.getParent().getAbsoluteTop() - panelTemplateInfoList.getAbsoluteTop()-20) + "px");
//		panelTemplateInfoList.setWidth((panelTemplateInfoList.getOffsetWidth()
//				-20) + "px");
//	}
		
//		panelTemplateInfoList.setStyleName("design-template-info-main-panel");
		
		for (int i = 0; i < result.length; i++){
			if (result[i] != null) {
				TemplateShortInfoPanel panel = new TemplateShortInfoPanel(result[i], true);
//				SimplePanel p = new SimplePanel();
//				DOM.setStyleAttribute(p.getElement(), "width", "100%");
//				p.add(panel);
//				panelTemplateInfoList.add(p);
				panelTemplateInfoList.add(panel);
			}
		}
		
//		SimplePanel fakePanel = new SimplePanel();
//		DOM.setStyleAttribute(fakePanel.getElement(), "paddingTop", "90px");
//		panelTemplateInfoList.add(fakePanel);
//		(new PagePanel(new Label("header"), null, panelTemplateInfoList)).setWidgetAndSize(this);
//		this.add(panelTemplateInfoList);
		
//		SimplePanel fakePanel = new SimplePanel();
//		DOM.setStyleAttribute(fakePanel.getElement(), "paddingTop", "90px");
//		this.add(fakePanel);
		
	}

	public void GetDetailedTemplateInfo(String templateID) {
		// Create an asynchronous callback to handle the result.
		final Template templateResult = null;

		AsyncCallback callback = new AsyncCallback() {

			public void onFailure(Throwable caught) {
				new PopupError("Unfortunately getting template details procedure has failed on server!",  caught.getMessage());

			}

			public void onSuccess(Object result) {
				// Display search result
				if (result != null) {
					Template t = (Template)result;
					t.MakeEditable(true);
					// TODO: Check is it the same template as selected now
//					t.properties.putAll(TemplateShortInfoPanel.selectedPanel.getInfo().getPropertiesAsMap());
					//t.setInfo(TemplateShortInfoPanel.selectedPanel.getInfo());
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
//					Main.getInstance().setSelectedElement(null);
				}
				else{
					// TODO: if no result!!!
				}
			}
		};

		// Make the call
		String keyString = textBoxSearchString.getText();
		String[] selTagsArray = selectedTags.toArray(new String[selectedTags.size()]);
		searchService.getTemplates(keyString, selTagsArray, Template.COMPONENT, callback);
	}
	
}
