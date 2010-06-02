package fi.tkk.media.xide.client.UI.Widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.Data.Tag;
import fi.tkk.media.xide.client.popups.basic.PopupError;
import fi.tkk.media.xide.client.popups.garbage.GoodPopupScrollablePanel;
import fi.tkk.media.xide.client.popups.utils.interfaces.Action;
import fi.tkk.media.xide.client.utils.ConnectionToServer;
import fi.tkk.media.xide.client.utils.ConnectionToServer.CallbackActions;
/**
 * Panel represents list of available tagListFromServer from server together with action buttons (add/remove tag) 
 * and input text field to search through the list. 
 * Takes Action interface as a parameter to pass necessary actions which should be performed when list is modified
 * @author evgeniasamochadina
 *
 */
public class TagsSearchPanel extends VerticalPanel implements KeyboardListener {
	public static final String ADD_TAG = "actionIcons/add.png";
	public static final String REMOVE_TAG = "actionIcons/navigate_minus.png";
	
	// Hashset contains list of HP each of them represents one tag
	HashMap<String, HorizontalPanel> tagPanelMap;
	
	// List of tags received from server
	Tag[] tagListFromServer;
	
	// Connection to server
	ConnectionToServer connectionToServer;
	
	// List of tagListFromServer which are currently selected. List is received in constructor and stays up-to-date while panel is used. 
	ArrayList<String> tagListSelected;
	
	// Action interface instance 
	Action action;
	
	// Panel contains tags received from server
	FlowPanel main;
	
	/**
	 * Initiates the panel and draw tag list
	 * @param tagListSelected list of selected tagListFromServer
	 * @param action action
	 */
	public TagsSearchPanel(ArrayList<String> tagsList, Action action) {
		// Initiate all the lists
		this.tagListSelected = tagsList;
		this.action = action;
		this.tagPanelMap = new HashMap<String, HorizontalPanel>();
		tagListFromServer = new Tag[0];
		// Initiate connection to server
//		connectionToServer = new ConnectionToServer();
		// Draw search text box 
		drawUI();
		// Get tagListFromServer from server and show them
		getTagsAndShow();
	}
	
	public void updateTagList() {
		getTagsAndShow();
	}
	
	public void drawUI() {
		// Search text box
		TextBox search = new TextBoxWithRequestString("Search for tag here");
		search.addKeyboardListener(this);
		this.setWidth("100%");
		this.add(search);
		main = new FlowPanel();
		this.add(main);
	}
	
	/**
	 * Displays tags received according to their status (selected or not)
	 * @param tags
	 */
	private void displayTags(Tag[] tags) {
		// Check if tag list has changed
		if (tagListFromServer.length == tags.length) {
			boolean equal = true;
			for (int i=0; i < tags.length; i++ ) {
				if (!tagListFromServer[i].getTitle().equals(tags[i].getTitle())) {
					equal = false;
				}
			}
			// If list is equal, then do not do anything
			if (equal == true) {
				return;
			}
		}
		// Update tag list
		this.tagListFromServer = tags;
		
		// Clear tag panel list
		main.clear();
		tagPanelMap.clear();


		// Adding all tagListFromServer
		for(int i = 0; i < tagListFromServer.length; i++) {
			final String currentTagTitle = tagListFromServer[i].getTitle();
			
			HorizontalPanel hp = new HorizontalPanel();
			hp.setStyleName("design-single-tag-panel");
			
			hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			
			// Image representing delete tag functionality
			GoodImage add = new GoodImage("Add a tag", ADD_TAG);

			// Image representing add tag functionality
			GoodImage delete = new GoodImage("Delete a tag", REMOVE_TAG);

			Label l = new GoodLabel(tagListFromServer[i].getDescription(), tagListFromServer[i].getTitle());
			l.setWordWrap(false);

			final FocusPanel iconAndText1 = new FocusPanel();
			HorizontalPanel panel = new HorizontalPanel();
			panel.add(add);
			panel.add(l);
			iconAndText1.add(panel);

			final FocusPanel iconAndText2 = new FocusPanel();
			l = new GoodLabel(tagListFromServer[i].getDescription(), tagListFromServer[i].getTitle());
			DOM.setStyleAttribute(l.getElement(), "fontWeight", "bold");
			panel = new HorizontalPanel();
			
			panel.add(delete);
			panel.add(l);
			iconAndText2.add(panel);

			// Toggle oblect maintains that correct image is shown
			final ToggleWidget toggleWidget;
			
			// If this tag is already assigned to the element
			if (tagListSelected.contains(tagListFromServer[i].getTitle())) {
				// Show firstly delete tag picture
				toggleWidget = new ToggleWidget(iconAndText2, iconAndText1);
			}
			else{
				// Show firstly add tag picture
				toggleWidget = new ToggleWidget(iconAndText1, iconAndText2);
			}
			
			iconAndText1.addClickListener(new ClickListener() {

				public void onClick(Widget sender) {
					toggleWidget.onClick();
					tagListSelected.remove(currentTagTitle);
					if (action != null) {
						action.doAction();
					}
				}
			});
			
			iconAndText2.addClickListener(new ClickListener() {

				public void onClick(Widget sender) {
					toggleWidget.onClick();
					tagListSelected.add(currentTagTitle);
					if (action != null) {
						action.doAction();
					}
				}
			});
			
			hp.add(toggleWidget);

			// Title label with the hind showing a tag description
//			hp.add(l);

			tagPanelMap.put(tagListFromServer[i].getTitle(), hp);
			DOM.setStyleAttribute(hp.getElement(), "float", "left");
			
			main.add(hp);
		}
	}
	
	
	/**
	 * Gets tags from server and initiate their showing
	 */
	private void getTagsAndShow() {
		connectionToServer.makeACall(new CallbackActions() {

			public void execute(AsyncCallback callback) {
				connectionToServer.searchService.getAllTags(callback);
			}

			public void onFailure(Throwable caught) {
				new PopupError("Unfortunately tags cannot be received!",  caught.getMessage());

			}

			public void onSuccess(Object result) {
				if (result instanceof Tag[]) {
					displayTags((Tag[])result);
				}
			}});
	}

	
	public void unselectTag(String tagName) {
		HorizontalPanel currentPanel = tagPanelMap.get(tagName);
		if (currentPanel.getWidget(0) instanceof ToggleWidget) {
			((ToggleWidget)currentPanel.getWidget(0)).changeIcon();
		}
		
	}
	
	public void onKeyDown(Widget sender, char keyCode, int modifiers) {
	}

	public void onKeyPress(Widget sender, char keyCode, int modifiers) {
		
	}

	/**
	 * Maintains search through the displayed list of tags. Does not send any request to server 
	 * but just filter the existing tag list
	 */
	public void onKeyUp(Widget sender, char keyCode, int modifiers) {
		if (sender instanceof TextBox) {
			String searchString = ((TextBox) sender).getText();
//			if (!searchString.equals("")) {
				for (int i = 0; i < tagListFromServer.length; i++) {
					// If tag title or tag description contains the searching word
					if (tagListFromServer[i].getTitle().toLowerCase().contains(searchString.toLowerCase()) 
							|| tagListFromServer[i].getDescription().toLowerCase().contains(searchString.toLowerCase())) {
						// Then show tag panel
						tagPanelMap.get(tagListFromServer[i].getTitle()).setVisible(true);
					}
					else {
						// Else do not show tag panel
						tagPanelMap.get(tagListFromServer[i].getTitle()).setVisible(false);
					}
				}
				
				
//			}
		}
	}

}
