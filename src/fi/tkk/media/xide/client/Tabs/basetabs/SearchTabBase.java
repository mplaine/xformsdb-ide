package fi.tkk.media.xide.client.Tabs.basetabs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DisclosureEvent;
import com.google.gwt.user.client.ui.DisclosureHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.Data.TemplateShortInfo;
import fi.tkk.media.xide.client.Server.RPC.SearchService;
import fi.tkk.media.xide.client.Server.RPC.SearchServiceAsync;
import fi.tkk.media.xide.client.Tabs.Tab;
import fi.tkk.media.xide.client.UI.Widget.GoodButton;
import fi.tkk.media.xide.client.UI.Widget.StyledButton;
import fi.tkk.media.xide.client.UI.Widget.TagsSearchPanel;
import fi.tkk.media.xide.client.UI.Widget.TemplateShortInfoPanel;
import fi.tkk.media.xide.client.UI.Widget.TextBoxWithRequestString;
import fi.tkk.media.xide.client.parser.Document;
import fi.tkk.media.xide.client.parser.Node;
import fi.tkk.media.xide.client.popups.utils.interfaces.Action;

public abstract class SearchTabBase extends Tab implements ClickListener {
	
	public static HashMap<String, String> TagIconsHashMap;

	// Search request data
//	protected HashMap<String,Label> selectedTagsLabels;
	protected ArrayList<String> selectedTags;
	// Flow panel which contains selected tag list
//	FlowPanel panelTagsList;
	
	protected TextBoxWithRequestString textBoxSearchString;
	protected FlowPanel panelTemplateInfoList;
	
	protected TemplateShortInfo selectedTemplateInfo;
	protected DockPanel selectedTemplateInfoPanel;

	TagsSearchPanel p;
	
	protected static SearchServiceAsync searchService;

	// TODO: may be not main but just link to SelectedElements
	public SearchTabBase() {
		super();
		
//		  String xml = "<fr:element att=\"some attribute\"> \n\n some text \n</elemejlkjknt>";
//		  System.out.println(xml);
//		  Document doc = Document.xmlParse(xml);
//
//		  Node node0 = (Node)doc.getChildren().get(0);
//		  node0.getName(); // "element"
//		  node0.getValue(); // null
//		  node0.getAttribute("att"); // "some attribute"
//		  node0.getType(); // DOM_ELEMENT_NODE
//		  System.out.println("" + node0.getName() + " " + node0.getAttribute("att"));
//
//		  Node node1 = (Node)node0.getChildNodes().get(0);
//		  node1.getName(); // #text
//		  node1.getValue(); // "some text"
//		  node1.getType(); // DOM_TEXT_NODE
//		  System.out.println("" + node1.getName() + " " + node1.getValue());
		  
//		  this.add(new Label("" + node0.getName()));
		  
////		// Working test 1
////		xSimpleWidget2 html = new xSimpleWidget2("<b>ttt</b>");
//////		html.add(new Label("tttrrr"));
////		xSimpleWidget2 html2 = new xSimpleWidget2("<i>rrr</i>");
//////		xSimpleWidget2 html3 = new xSimpleWidget2("<td></td>");
////		html.add(html2);
////		System.out.println("!! " + html );
////		this.add(html);
//		xSimpleWidget2 html = new xSimpleWidget2("<table></table>");
//		System.out.println("html.getE " + html.getElement().getInnerHTML());
//		xSimpleWidget2 html2 = new xSimpleWidget2("<tr></tr>");
//		System.out.println(html2.getElement().getInnerHTML());
//		xSimpleWidget2 html3 = new xSimpleWidget2("<td></td>");
//		xSimpleWidget2 html5 = new xSimpleWidget2("ttt1");
//		html.add(html2);
//		html2.add(html3);
//		html3.add(html5);
////		html3.add(new Label("tttrrr"));
////
////		xSimpleWidget2 html5 = new xSimpleWidget2("<td></td>");
////		html2.add(html5);
////		html5.add(new Label("rrrttt"));
//		Label htmlText = new Label ("!! " + html);
//		this.add(htmlText);
//		System.out.println("!! " + html );
////		this.add(html2);
////		this.add(new Label("rrrttt"));
//		add(html);
//		this.add(new Label("header"));

		
//		AbsolutePanel p0  = new AbsolutePanel();
//		p0.setSize("100%", "100%");
//		DOM.setStyleAttribute(p0.getElement(), "backgroundColor", "blue");
//		DOM.setStyleAttribute(p0.getElement(), "position", "relative");
//		
//		DockPanel vp = new DockPanel();
//		vp.setSize("100%","100%");
//		
//		Label l1 = new Label("text");
//		vp.add(l1, DockPanel.NORTH);
//		
//		vp.add(p0, DockPanel.CENTER);
//		vp.setCellHeight(l1, l1.getOffsetHeight() + "px");
//		this.add(vp);
//		
//		
//		FlowPanel comps = new FlowPanel();
//		for (int i = 0; i < 15; i++) {
//			Label l = new Label ("compppp" + i);
//			DOM.setStyleAttribute(l.getElement(), "padding", "5px");
//			comps.add(l);
//		}
//		DOM.setStyleAttribute(comps.getElement(), "overflowY", "scroll");
//		DOM.setStyleAttribute(comps.getElement(), "height", "100%");
//		p0.add(comps);
		
	
		

		
//		VerticalPanel hp = new VerticalPanel();
//		DOM.setStyleAttribute(hp.getElement(), "height", "100%");
//		DOM.setStyleAttribute(hp.getElement(), "width", "100%");
//		DOM.setStyleAttribute(hp.getElement(), "overflow", "hidden");
//		Label l = new Label("header");
//		DOM.setStyleAttribute(l.getElement(), "height", "20px");
//		hp.add(l);
//		hp.add(comps);
//		
//		p.add(hp);
		
		
		// Initiate selected tags
    	selectedTags = new ArrayList<String>();
//    	selectedTagsLabels = new LinkedHashMap<String, Label>();
		
		
		// Load tag icons hash map
    	
		LoadIcons();
		
		InitSearchService();
		
		
		// displayUI();
		// Initialize displayed information
		InitDisplay();
		
		// Display some templates
		SearchTemplatesOnServer();
		
	}
	

	public void ClearTagSelection() {
//		Object[] selectedTagKeys = selectedTagsOld.toArray();

		// Physical cleaning
//		panelTagsList.clear();
		
//		selectedTagsLabels.clear();
		//		
//		for (int i = 0; i < selectedTagKeys.length; i++) {
//			Label label = (Label) selectedTagKeys[i];
//			label.removeStyleDependentName("selected");
//			selectedTagsOld.remove(label);
//		}

	}
	
	public void ClearTags() {
//		selectedTags.clear();
//		// Physical cleaning
//		panelTagsList.clear();
//	
//		selectedTagsLabels.clear();
		// Remove selection from tag search
		int size = selectedTags.size();
		for (int i = 0; i < size; i++) {
			removeTag(selectedTags.get(0));
		}
//		panelTagsList.clear();
				
	}
	
	public void displayTags() {
		for (Iterator<String> iterator = selectedTags.iterator(); iterator.hasNext();) {
			String tagText = iterator.next();
			addTagLabel(tagText);
		}
	}

	private void addTagLabel(String tagText) {
		final Label label = new Label(tagText);
		label.setStyleName("design-LabelLink");
		label.sinkEvents(Event.ONCLICK | Event.ONDBLCLICK);
		
		final HorizontalPanel panel = new HorizontalPanel();
		panel.setStyleName("design-single-tag-panel");
		
		if (TagIconsHashMap.containsKey(tagText)) {
			Image image = new Image(TagIconsHashMap.get(tagText));
			
			panel.add(image);
		}
		panel.add(label);
		Image deleteImage = new Image ("actionIcons/delete_small.png");
		deleteImage.addClickListener(new ClickListener() {
	
			public void onClick(Widget sender) {
				selectedTags.remove(label.getText());
				p.unselectTag(label.getText());
				panel.removeFromParent();
			}});
		panel.add(deleteImage);
//		panelTagsList.add(panel);
	}
	
	private void removeTag(String tagText) {
//		Label l = selectedTagsLabels.get(tagText);
		// Logical removing
//		selectedTagsLabels.remove(tagText);
		selectedTags.remove(tagText);
		// Remove HP which contains a label from the list
//		l.getParent().removeFromParent();
		p.unselectTag(tagText);
		
	}
	/**
	 * Draws basic layout of the tab (search text box, tags)
	 */
	protected HorizontalPanel hp ;
	protected void InitDisplay() {
		// 1. Search text box

		
//		this.setStyleName("design-new-search-tab");
		VerticalPanel vp = new VerticalPanel();
		
//		DOM.setStyleAttribute(this.getElement(), "height", "100%");
		hp = new HorizontalPanel();
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		textBoxSearchString = new TextBoxWithRequestString("Search here");
	
		hp.add(textBoxSearchString);

		// 2. Search button
		hp.add(new StyledButton("Search", "operationIcons/general/search_16 copy.gif", new ClickHandler() {

			public void onClick(ClickEvent event) {
				// Send request to server
				SearchTemplatesOnServer();
			}}, StyledButton.STYLE_BLUE).getButton());
		

		// 2.1 Clear search button (clears search string and tags)
		hp.add(new StyledButton("Clear Search", "operationIcons/general/clear_16 copy.gif", new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClearTags();
				textBoxSearchString.setText("Search here");
				
			}}, StyledButton.STYLE_BLUE).getButton());
		

		// 2.1 Use tag
		hp.add(new StyledButton("Use tag", "operationIcons/general/search_16 copy.gif", new ClickHandler() {

				boolean opened = false;
				public void onClick(ClickEvent event) {
					if (!opened) {
						p.updateTagList();
						if (!Main.IS_RUNNING_ON_WINDOWS) {
							DOM.setStyleAttribute(p.getElement(), "display", "inherit");
						}
						else {
							p.setVisible(true);
						}
						
						
					}
					else {
						DOM.setStyleAttribute(p.getElement(), "display", "none");
					}
					opened = !opened;
				}}, 
				StyledButton.STYLE_BLUE).getButton());
		
			
		vp.add(hp);
		
		// Tag search panel, currently not displayed
		p = new TagsSearchPanel(selectedTags, new Action() {
			public void doAction() {
				ClearTagSelection();
				displayTags();
				}
		}
		);
		DOM.setStyleAttribute(p.getElement(), "display", "none");
		
		vp.add(p);
		
		// Tab panel example
		// External tab panel (under high lvel tab )
		AbsolutePanel p0  = new AbsolutePanel();
		p0.setSize("100%", "100%");
		DOM.setStyleAttribute(p0.getElement(), "position", "relative");
		
		this.add(p0);
		
		// A wrapper for the flow panel to set it's size to 100% 
		//of the remaining space
		AbsolutePanel p1  = new AbsolutePanel();
		p1.setSize("100%", "100%");
		DOM.setStyleAttribute(p1.getElement(), "position", "absolute");
		
		// Panel to scroll
		panelTemplateInfoList = new FlowPanel();

		DOM.setStyleAttribute(panelTemplateInfoList.getElement(), "overflowY", "scroll");
		DOM.setStyleAttribute(panelTemplateInfoList.getElement(), "height", "100%");
		DOM.setStyleAttribute(panelTemplateInfoList.getElement(), "paddingLeft", "2px");
		DOM.setStyleAttribute(panelTemplateInfoList.getElement(), "paddingRight", "2px");
		p1.add(panelTemplateInfoList);
		
		DeckPanel dock = new DeckPanel();
		dock.setStyleName("gwt-TabPanelBottom");
		dock.add(p1);
		dock.showWidget(0);

//		Label header = new Label("header");
		
		VerticalPanel panel = new VerticalPanel();
		panel.setStyleName("design-new-search-tab");
		panel.setSize("100%", "100%");
	    panel.add(vp);
	    panel.add(dock);
	    
	    panel.setCellHeight(dock, "100%");
	    vp.setWidth("100%");

		p0.add(panel);
		

//		tp.getTabBar().selectTab(0);
		
		DOM.setStyleAttribute(DOM.getParent(dock.getElement()), "size", "inherit");
		DOM.setStyleAttribute(DOM.getParent(p1.getElement()), "position", "relative");

		
	};
	
	/**
	 * This method is called to redraw list of tags which are selected by the user
	 */
	private void redrawTags() {
		
	}
	
	protected  void displayUI() {

	}

	/**
	 * Loads different icons which indicates common template tags
	 * Should be abstract: each subclass should load its own icons
	 */
	protected void LoadIcons() {
		TagIconsHashMap = new HashMap<String, String>(10);

		// TODO: Probably get list of tags and icon pictures from server
		TagIconsHashMap.put("Private", "TagIcons/drop-no_1.gif");
		TagIconsHashMap.put("Public", "TagIcons/drop-yes_1.gif");
		TagIconsHashMap.put("Atomic", "TagIcons/columns_1.gif");
		TagIconsHashMap.put("New", "TagIcons/exclamation_1.gif");
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
//		TemplateShortInfoPanel.setSearchTab(this);
		
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

	public abstract void GetDetailedTemplateInfo(String templateID) ;
	
	/**
	 * Abstract
	 */
	public abstract void SearchTemplatesOnServer();

	
	/**
	 * Inits search engine class
	 * Works for all implementations of the Search Tab
	 */
	public void InitSearchService(){
		searchService = (SearchServiceAsync) GWT.create(SearchService.class);
        ServiceDefTarget endpoint = (ServiceDefTarget) searchService;
        String moduleRelativeURL = GWT.getModuleBaseURL() + "SearchService";
        endpoint.setServiceEntryPoint(moduleRelativeURL);

	}

	public void onClick(Widget sender) {
		// TODO Auto-generated method stub

	}
	
}
