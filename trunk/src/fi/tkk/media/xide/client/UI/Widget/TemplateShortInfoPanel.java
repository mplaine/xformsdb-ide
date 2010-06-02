/**
 * 
 */
package fi.tkk.media.xide.client.UI.Widget;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.HasMouseMoveHandlers;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.HasMouseUpHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.ClickListenerCollection;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.DnDController;
import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.Data.SaveObjectsListener;
import fi.tkk.media.xide.client.Data.Selectable;
import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.Data.TemplateShortInfo;
import fi.tkk.media.xide.client.Server.RPC.SearchServiceAsync;
import fi.tkk.media.xide.client.Tabs.PLTab;
import fi.tkk.media.xide.client.Tabs.PLTreeItem;
import fi.tkk.media.xide.client.Tabs.PropertiesTab;
import fi.tkk.media.xide.client.Tabs.SearchComponentTab;
import fi.tkk.media.xide.client.Tabs.basetabs.SearchTabBase;
import fi.tkk.media.xide.client.popups.basic.PopupAreYouSure;
import fi.tkk.media.xide.client.utils.ConnectionToServer;
import fi.tkk.media.xide.client.utils.ConnectionToServer.CallbackActions;

/**
 * @author Evgenia Samochadina
 * @date Dec 4, 2008
 *
 */
public class TemplateShortInfoPanel extends DockPanel  implements HasMouseDownHandlers, HasMouseMoveHandlers,
HasMouseOutHandlers, HasMouseOverHandlers, HasMouseUpHandlers, SourcesMouseEvents, Selectable{
	private TemplateShortInfo info;
	static public TemplateShortInfoPanel selectedPanel;
	private static SearchTabBase searchComponentTab;
	
	
	// Template instance which is associated with the template panel
	// Used to show information about template in the database
	private Template templateTemplate = null;
	// Template instance which is used by components of this type
	// The difference between this instance and previous one is basically in settings for editing template properties
	private Template template = null;
	
	Template templateBackup;
	boolean isChanged = false;
	
	private MouseListenerCollection mouseListeners;
	
//	private MouseListenerCollection mouseListeners;
	
	public TemplateShortInfoPanel(TemplateShortInfo info, boolean isClickable){
		this.setInfo(info); 
		if (isClickable) {
			this.sinkEvents(Event.ONCLICK | Event.ONDBLCLICK);
		}
		this.setStyleName("design-template-info-panel");

		// DISPLAY everything:
		
		Label labelTitle = new Label(info.getTitle().getStringValue());
		labelTitle.setStyleName("design-LabelMiddleText");
		
		// Display description
		Label labelDescr = new Label(info.getDescr().getStringValue());
		labelDescr.setStyleName("design-LabelSmallText");
		
		// Display tags
		// TODO: display tags in columns, go next column after several tags (avoid long column)
		// TODO: make something more readable
		VerticalPanel panelTagIcons = new VerticalPanel();
		ArrayList<String> tagsFromTemplate = (ArrayList<String>)info.getTags().getValue();
		for (int j = 0; j < tagsFromTemplate.size(); j ++){
			if (SearchComponentTab.TagIconsHashMap.containsKey(tagsFromTemplate.get(j))) {
				panelTagIcons.add(new GoodImage(tagsFromTemplate.get(j), SearchComponentTab.TagIconsHashMap.get(
						tagsFromTemplate.get(j))));
			}
		}
		this.add(panelTagIcons, DockPanel.EAST);
		this.add(labelTitle, DockPanel.NORTH);
		this.add(labelDescr, DockPanel.CENTER);
		
//		if(DnDController.getDragControllerAdding()!= null ) {
		if(DnDController.getDragControllerAdding()!= null && (Boolean)info.getDoWork().getValue()) {
			DnDController.getDragControllerAdding().makeDraggable(this);
		}

	}
	
	public String getTypeName() {
		return "component's template";
	}
	
	public void onBrowserEvent(Event event) {

//		// Handle mouse selection
//		super.onBrowserEvent(event);

	    switch (DOM.eventGetType(event)) {
	      case Event.ONCLICK:
	    	  if (!(Boolean)info.getDoWork().getValue()) {
	    		  // just select the component, no dnd
	  			selectedPanel = this;
				// Checking does not needed but still
//				if (getSearchTab().IsNewTemplateSelected()) {
				GetDetailedTemplateInfo();

	    	  }
//		  		if (selectedPanel != this) {
//					// Store selection
//					selectedPanel = this;
//					// Checking does not needed but still
////					if (getSearchTab().IsNewTemplateSelected()) {
//					getSearchTab().GetDetailedTemplateInfo(getInfo().getID().getStringValue());
////					}
//				}
	        break;
	      case Event.ONMOUSEDOWN:
	      case Event.ONMOUSEUP:
	      case Event.ONMOUSEMOVE:
	      case Event.ONMOUSEOVER:
	      case Event.ONMOUSEOUT:
	    		super.onBrowserEvent(event);
//	    	  this.fireEvent(event);
//	        if (mouseListeners != null) {
//	          mouseListeners.fireMouseEvent(this, event);
//	        }
	        break;
	    }
	}

	/**
	 * @param info the info to set
	 */
	public void setInfo(TemplateShortInfo info) {
		this.info = info;
	}
	/**
	 * @return the info
	 */
	public TemplateShortInfo getInfo() {
		return info;
	}
	/**
	 * @param searchTab the searchComponentTab to set
	 */
	public static void setSearchTab(SearchTabBase searchTab) {
		TemplateShortInfoPanel.searchComponentTab = searchTab;
	}
	/**
	 * @return the searchComponentTab
	 */
	public static SearchTabBase getSearchTab() {
		return searchComponentTab;
	}
	public void Display(){
	}

	  public void addMouseListener(MouseListener listener) {
		    if (mouseListeners == null) {
		      mouseListeners = new MouseListenerCollection();
		      sinkEvents(Event.MOUSEEVENTS);
		    }
		    mouseListeners.add(listener);
		  }
	  public void removeMouseListener(MouseListener listener) {
		    if (mouseListeners != null) {
		      mouseListeners.remove(listener);
		    }
		  }
	


//	public void addMouseListener(MouseListener listener) {
//		labelTitle.addMouseListener(listener);
//		
//	}
//	
//	public void removeMouseListener(MouseListener listener) {
//		labelTitle.removeMouseListener(listener);
//		
//	}

	
	public ArrayList<Selectable> GetLinkedObjects() {
		ArrayList<Selectable> list = new ArrayList<Selectable>(); 
		return list;
	}
	
	public Template getTemplateForComponentsCreation() {
		// If there is no template generated for component creation
		// then create it
		if (template == null) {
			// Search for such template in the template library
			PLTreeItem item = PLTab.getInstance().SearchTemplateItem(templateTemplate);
			if (item != null) {
				// There is instance of such template in the the PL
				// get existing template and return it for component creation
				template = item.getProperties();
			}
			else {
				// There is no needed instances in the PL
				// Create clone item
				template = templateTemplate.cloneIt();
			}
		}
//		// Else if template is modified and saved as public one
//		// create new template for component creation
//		else if (!template.isPublic()) {
//			template = templateTemplate.cloneIt();
//		}
		
		return template;
	}
	
	public Template getProperties() {
		return templateTemplate;
	}
	
	public Selectable getValuableElement() {
		return this;
	}
	
	public void Select() {
		this.changeStyle(SELECTED);
//		DnDController.getDragControllerAdding().makeDraggable(this);

	}
	public void Unselect() {
		selectedPanel = null;
		this.changeStyle(RESET);
//		Main.getInstance().getDragControllerAdding().makeNotDraggable(this);
		
	}
	public void setTemplate(Template template) {
		// TODO ! very important. Make author checking!!!
		if ((template.getProperties()!=null)&&(template.getProperties().get(Property.AUTHOR)!=null) ) {
			if (template.getProperties().get(Property.AUTHOR).getStringValue().equals("EvgeniaS")){
				template.MakeEditable(true);
			}
		}
		this.templateTemplate = template;
	}

	/* (non-Javadoc)
	 * @see fi.tkk.media.xide.client.Data.Selectable#Changed()
	 */
	public void Changed() {
		changeStyle(CHANGED);
		if (!isChanged) {
			if(templateBackup == null) {
				templateBackup = templateTemplate;
				templateTemplate = templateTemplate.cloneIt();
			}
			isChanged = true;
			Main.getCurrentView().updatePropertiesList();
		}
	}


	/* (non-Javadoc)
	 * @see fi.tkk.media.xide.client.Data.Selectable#Saved()
	 */
	public void Saved() {
//		(new PopupAreYouSure("Are you sure you want to change existing template in the database? ", new ClickListener() {
//
//			public void onClick(Widget sender) {
				if (isChanged) {
					templateTemplate.sendUpdatedFilesToServer(null, this, true);
				}
				
//				
//			}}, null)).show();
	}
	
	public void afterSaved() {
		// TODO: manage saved and aftersaved
		if (isChanged) {
			isChanged = false;
			templateBackup = null;
			changeStyle(RESET);
			// TODO: update it for SearchApplicationTab as well
			templateTemplate.sendUpdatedTemplate();
//			template.getSourceCodeFirstFile().getContentFromServer();
		}
	}
	
	public void Saved(SaveObjectsListener listener) {
		if (isChanged) {
			templateTemplate.sendUpdatedFilesToServer(listener, this, true);
		}

	}
	
	public boolean isChanged() {
		return isChanged;
	}

	public void Canceled() {
		if ((isChanged)&& (templateBackup != null)) {
			templateTemplate = templateBackup;
			templateBackup = null;
			isChanged = false;
			Main.getCurrentView().updatePropertiesList();
			Main.getInstance().UpdateUI();
			changeStyle(Selectable.RESET);
		}
	}

	public void changeStyle(int event) {
		if ((event&Selectable.CHANGED) >0) {
			for (Iterator<Selectable> iterator = GetLinkedObjects().iterator(); iterator.hasNext();) {
				iterator.next().changeStyle(Selectable.CHANGED_LINKED);
			}
		}
		else if ((event&Selectable.CHANGED_LINKED) >0) {}
		else if ((event&Selectable.SELECTED) >0) {
			this.addStyleDependentName("selected");
			for (Iterator<Selectable> iterator = GetLinkedObjects().iterator(); iterator.hasNext();) {
				iterator.next().changeStyle(Selectable.SELECTED_LINKED);
			}
		}
		else if ((event&Selectable.SELECTED_LINKED) >0) {}
		else if ((event&Selectable.RESET) >0) {
			this.removeStyleDependentName("selected");
			if (Main.getInstance().getSelectedElement()== this) {
				for (Iterator<Selectable> iterator = GetLinkedObjects().iterator(); iterator.hasNext();) {
					iterator.next().changeStyle(Selectable.RESET);
				}
			}

		}
		
	}

	public void Deleted() {
		// TODO add smth
		// currently nothing to do
		
	}

	  public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
		    return addDomHandler(handler, MouseDownEvent.getType());
		  }
	  public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
		    return addDomHandler(handler, MouseMoveEvent.getType());
		  }

		  public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		    return addDomHandler(handler, MouseOutEvent.getType());
		  }

		  public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		    return addDomHandler(handler, MouseOverEvent.getType());
		  }

		  public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
		    return addDomHandler(handler, MouseUpEvent.getType());
		  }
			transient ConnectionToServer connectionToServer;
			
			
			public void GetDetailedTemplateInfo() {
				ConnectionToServer.makeACall(new CallbackActions() {

					public void execute(AsyncCallback callback) {
						connectionToServer.searchService.getTemplateDetailedInfo(
								TemplateShortInfoPanel.this.getInfo().getID().getStringValue(), callback);
					}

					public void onFailure(Throwable caught) {
						
					}

					public void onSuccess(Object result) {
						if (result != null) {
							Template t = (Template)result;
							t.MakeEditable(true);
							// TODO: Check is it the same template as selected now
//							t.properties.putAll(TemplateShortInfoPanel.selectedPanel.getInfo().getPropertiesAsMap());
							//t.setInfo(TemplateShortInfoPanel.selectedPanel.getInfo());
							TemplateShortInfoPanel.selectedPanel.setTemplate(t);
							// Set selected element and initiate further changes
							Main.getInstance().setSelectedElement(TemplateShortInfoPanel.selectedPanel);
						}
						else{
							// TODO: if no result!!!
						}
					}});
			}
			
//			public void GetDetailedTemplateInfo(String templateID) {
//				// Create an asynchronous callback to handle the result.
//				final Template templateResult = null;
//
//				AsyncCallback callback = new AsyncCallback() {
//
//					public void onFailure(Throwable caught) {
//						// do some UI stuff to show failure
//						// TODO: add normal error handler
//					}
//
//					public void onSuccess(Object result) {
//						// Display search result
//						if (result != null) {
//							Template t = (Template)result;
//							t.MakeEditable(true);
//							// TODO: Check is it the same template as selected now
////							t.properties.putAll(TemplateShortInfoPanel.selectedPanel.getInfo().getPropertiesAsMap());
//							//t.setInfo(TemplateShortInfoPanel.selectedPanel.getInfo());
//							TemplateShortInfoPanel.selectedPanel.setTemplate(t);
//							// Set selected element and initiate further changes
//							Main.getInstance().setSelectedElement(TemplateShortInfoPanel.selectedPanel);
//						}
//						else{
//							// TODO: if no result!!!
//						}
//					}
//				};
//
//				// Make the call
//				searchService.getTemplateDetailedInfo(templateID, callback);
//			}

}
