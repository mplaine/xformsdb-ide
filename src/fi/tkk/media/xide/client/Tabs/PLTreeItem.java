/**
 * 
 */
package fi.tkk.media.xide.client.Tabs;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.DnDController;
import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.Data.SaveObjectsListener;
import fi.tkk.media.xide.client.Data.Selectable;
import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.Server.RPC.SearchService;
import fi.tkk.media.xide.client.Server.RPC.SearchServiceAsync;
import fi.tkk.media.xide.client.UI.Widget.Component;
import fi.tkk.media.xide.client.UI.Widget.HorizontaPanelPLTab;
import fi.tkk.media.xide.client.UI.Widget.TemplateShortInfoPanel;
import fi.tkk.media.xide.client.popups.PopupTemplatePublishing;
import fi.tkk.media.xide.client.popups.basic.Popup;
import fi.tkk.media.xide.client.utils.ConnectionToServer;
import fi.tkk.media.xide.client.utils.GoodHorizontalAdjustablePanel;
import fi.tkk.media.xide.client.utils.ConnectionToServer.CallbackActions;

/**
 * @author Evgenia Samochadina
 * @date Mar 9, 2009
 *
 */
public class PLTreeItem extends TreeItem implements Selectable {
//	private Template template;
	
	public static final String PUBLISHED = "operationIcons/templates/template_make_public_16 copy.gif";
	public static final String NOTPUBLISHED = "operationIcons/templates/template_make_private_16 copy.gif";
	
	ArrayList<Selectable> listOfComponents;
	HorizontaPanelPLTab itemWidget;
	Label l;
	boolean isChanged = false;
	Button buttonPublish;
	
	//protected static SearchServiceAsync searchService;
	private static ConnectionToServer serverConnection;

	public PLTreeItem(String string) {
		super(string);
		listOfComponents = new ArrayList<Selectable>();
	}

	public PLTreeItem(Component component) {
		super();

//		this.template = component.getProperties();
		listOfComponents = new ArrayList<Selectable>();
		listOfComponents.add(component);

		buttonPublish = new Button() {
			@Override
			public void onBrowserEvent(Event event) {
//				System.out.println("clicked in button");
				event.cancelBubble(true);
				super.onBrowserEvent(event);
				if (event.getTypeInt() == Event.ONMOUSEMOVE) {
					final int x = this.getAbsoluteLeft();// event.getClientX();
					final int y = this.getAbsoluteTop();// event.getClientY();

					// p.setPopupPositionAndShow(new
					// PopupPanel.PositionCallback() {
					// public void setPosition(int offsetWidth, int
					// offsetHeight) {
					// int left = x - 3;
					// int top = y - offsetHeight - 3;
					// p.setPopupPosition(left, top);
					// p.setHeight((offsetHeight + 10) + "px");
					// p.setWidth((offsetWidth + 10) + "px"); }
					// });
					//
					// p.show();
				} else if (event.getTypeInt() == Event.ONMOUSEOUT) {
					// p.hide();
				}
			}
		};
		buttonPublish.sinkEvents(Event.ONMOUSEMOVE | Event.ONMOUSEOUT);

		
		final Template t = component.getProperties();
		buttonPublish.addClickListener(new ClickListener() {

			public void onClick(Widget sender) {
				publishItem();
//				sendPublishedTemplate(t);
			}

		});
		if (t.isPublic()) {
			buttonPublish.setHTML("<img src=\""+PUBLISHED+"\" class=\"gwt-Image\"/>");
			buttonPublish.setEnabled(false);
//			buttonPublish.setHTML("<img src=\"actionIcons/unpublish.png\" class=\"gwt-Image\"/>");
		}
		else {
			buttonPublish.setHTML("<img src=\""+NOTPUBLISHED+"\" class=\"gwt-Image\"/>");
		}


		if (component.getProperties().isPublic()) {
			l = new Label("1: "
					+ component.getProperties().getProperties().get(Property.TITLE).getStringValue() + " (Public)");
		} else {
			l = new Label("1: "
					+ component.getProperties().getProperties().get(Property.TITLE).getStringValue() + " (Private)");
		}
		l.setWordWrap(false);

		
		itemWidget = new HorizontaPanelPLTab(component.getProperties(), this);
		itemWidget.setWidth("100%");

//		if (itemWidget.getTemplate().isPublic()) {
//			buttonPublish.setEnabled(false);
//			}
//		
		GoodHorizontalAdjustablePanel panel = new GoodHorizontalAdjustablePanel(l, "left", buttonPublish, "right");
//		itemWidget.add(buttonPublish);
//		itemWidget.add(l);
		itemWidget.add(panel);
		
		super.setWidget(itemWidget);
		this.itemWidget.setStyleName("tree-item");
		
//		InitSearchService();

	}
//
//	/**
//	 * Inits search engine class
//	 * Works for all implementations of the Search Tab
//	 */
//	public void InitSearchService(){
//		searchService = (SearchServiceAsync) GWT.create(SearchService.class);
//        ServiceDefTarget endpoint = (ServiceDefTarget) searchService;
//        String moduleRelativeURL = GWT.getModuleBaseURL() + "SearchService";
//        endpoint.setServiceEntryPoint(moduleRelativeURL);
//
//	}
	
	public String getTypeName() {
		return "component's template";
	}
	
	public void sendPublishedTemplate(final Template template) {
//		final Template template = t;
//		if (serverConnection == null) {
//			serverConnection = new ConnectionToServer();
//		}
		serverConnection.makeACall(new CallbackActions() {

			public void execute(AsyncCallback callback) {
				serverConnection.searchService.saveNewTemplate(template, callback);
			}

			public void onFailure(Throwable caught) {
			}

			public void onSuccess(Object result) {
				if (result != null) {
					if ((Boolean)result == true) {
						// Successful publishing
						itemWidget.getTemplate().isPublic(true);
						updateItem();
						buttonPublish.setEnabled(false);
						// TODO: remove to PL tab
					}
					else {
						// Unsuccessful publishing
					}
				}
				else{
					// TODO: if no result!!!
				}
			}});
//		// Create an asynchronous callback to handle the result.
//		final boolean isSuccess;
//
//		AsyncCallback callback = new AsyncCallback() {
//
//			public void onFailure(Throwable caught) {
//				// do some UI stuff to show failure
//				// TODO: add normal error handler
//				System.out.println("Error while receiving information about template");
//			}
//
//			public void onSuccess(Object result) {
//				// Display search result
//				if (result != null) {
//					if ((Boolean)result == true) {
//						// Successful publishing
//						itemWidget.getTemplate().isPublic(true);
//						updateItem();
//						buttonPublish.setEnabled(false);
//						// TODO: remove to PL tab
//					}
//					else {
//						// Unsuccessful publishing
//					}
//				}
//				else{
//					// TODO: if no result!!!
//				}
//			}
//		};
//
//		// Make the call
//		System.out.println("publish call is sent");
//		searchService.saveNewTemplate(template, callback);
	}

	
	public void publishItem() {
		if (!itemWidget.getTemplate().isPublic()) {
			new PopupTemplatePublishing(itemWidget.getTemplate(), this);
		}
		else {
			new Popup("Template is public! ");
		}
	}
	
	public void updateItem() {
		int n = listOfComponents.size();

		// setHTML(n + ": " +
		// template.info.getProperty(ShortInfo.TITLE).getStringValue());
		String text = n
		+ ": "
		+ itemWidget.getTemplate().getProperties().get(Property.TITLE).getStringValue();
		
		if (itemWidget.getTemplate().isPublic()) {
			text = text + " (Public)";
			buttonPublish.setHTML("<img src=\""+PUBLISHED+"\" class=\"gwt-Image\"/>");
			buttonPublish.setEnabled(false);

		} else {
			text = text + " (Private)";
			buttonPublish.setHTML("<img src=\""+NOTPUBLISHED+"\" class=\"gwt-Image\"/>");
			buttonPublish.setEnabled(true);
		}
		if (isChanged) {
			text = text + "*";
		}
		l.setText( text);
	}

	 /**
	   * Adds another item as a child to this one.
	   * 
	   * @param item the item to be added
	   */
	  public void addItem(TreeItem item) {
		  super.addItem(item);
		  DOM.setStyleAttribute(DOM.getFirstChild(DOM.getFirstChild(getElement())), "width", "100%");
	  }
	
	public ArrayList<Selectable> GetLinkedObjects() {
		return listOfComponents;
	}

	public Template getProperties() {
		return itemWidget.getTemplate();
	}

	public Selectable getValuableElement() {
		return this;
	}
	
	public void Select() {
		
		this.changeStyle(SELECTED);
		DnDController.getDragControllerPLAdding().makeDraggable(this.itemWidget);
	}

	public void Unselect() {
		this.changeStyle(RESET);
		
//		Main.getInstance().getDragControllerPLAdding().makeNotDraggable(this.itemWidget);
//		this.itemWidget.isDraggable = false;
	}
	

	public void Changed() {
		if (!isChanged) {
			isChanged = true;
//			if (itemWidget.template.isPublic()) {
				if(itemWidget.templateBackup == null) {
					itemWidget.templateBackup = itemWidget.template;
					itemWidget.template = itemWidget.template.cloneIt();
				}
				
				Main.getCurrentView().updatePropertiesList();
				// Update template through all components
				
				// Copy parameter values
				for (Iterator<Selectable> iterator = listOfComponents.iterator(); iterator.hasNext();) {
					Component o =  (Component)iterator.next();
					o.setTemplate(itemWidget.template);
	//				o.copyParametersFromTemplate();
				}
//			}
				changeStyle(CHANGED);
		}
		
//		if (!isChanged) {
//			if(itemWidget.templateBackup == null) {
////				itemWidget.template = itemWidget.template;					
//				itemWidget.templateBackup = itemWidget.template.cloneIt();
//
//			}
//			isChanged = true;
//			//PropertiesTab.getInstance().UpdateProperties();
//		}
		
	}
	
	public void Saved() {
		if (isChanged) {
//			PLTab.getInstance().componentHasChanged(this);


			PLTab.getInstance().templateHasChanged(itemWidget.template, itemWidget.templateBackup);
			
			isChanged = false;
			itemWidget.templateBackup = null;
			
			// Change style
			changeStyle(RESET);
			// Copy parameter values
			for (Iterator<Selectable> iterator = listOfComponents.iterator(); iterator.hasNext();) {
				Component o =  (Component)iterator.next();
				o.copyParametersFromTemplate();
			}
		}
		
//		if (isChanged) {
//			((PLTreeItem)itemWidget.item).updateItem();
//			isChanged = false;
//			itemWidget.templateBackup = null;
//			// Change style
//			PLTab.getInstance().templateHasChanged(itemWidget.template);
//			
//			// Copy parameter values
//			for (Iterator<Selectable> iterator = listOfComponents.iterator(); iterator.hasNext();) {
//				Component o =  (Component)iterator.next();
//				o.copyParametersFromTemplate();
//			}
//		}
	}
	
	public void afterSaved() {
		// TODO: manage saved and aftersaved
	}

	public void Saved(SaveObjectsListener listener) {
		
	}

	public boolean isChanged() {
		return isChanged;
	}

	public void Canceled() {
		if ((isChanged)&& (itemWidget.templateBackup != null)) {
			itemWidget.template = itemWidget.templateBackup;
			itemWidget.templateBackup = null;
			isChanged = false;
			Main.getCurrentView().updatePropertiesList();
			// Update template for all components
			for (Iterator<Selectable> iterator = listOfComponents.iterator(); iterator.hasNext();) {
				Component o = (Component)iterator.next();
				o.setTemplate(itemWidget.template);
			}
			
			Main.getInstance().UpdateUI();
			changeStyle(RESET);
		}
		
//		if ((isChanged)&& (itemWidget.templateBackup != null)) {
//			itemWidget.template = itemWidget.templateBackup;
//			
//			for (Iterator<Selectable> iterator = listOfComponents.iterator(); iterator.hasNext();) {
//				Component o = (Component)iterator.next();
//				o.setTemplate(itemWidget.template);
//			}
//			
//			itemWidget.templateBackup = null;
//			isChanged = false;
//			// Update templateForComponent in the SearchTab
//			PropertiesTab.getInstance().UpdateProperties();
//			Main.getInstance().UpdateUI();
//		}
	}
	boolean isStarred = false;
	private void addStar() {
		if (!isStarred) {
			String text = l.getText();
			l.setText(text + "*");
			isStarred = true;
		}
	}
	
	private void removeStar() {
		if (isStarred) {
			String text = l.getText();
			l.setText(text.substring(0, text.length()-1));
			isStarred = false;
		}
	}
	public void changeStyle(int event) {
		if ((event&Selectable.CHANGED) >0) {
			addStar();
			for (Iterator<Selectable> iterator = GetLinkedObjects().iterator(); iterator.hasNext();) {
				iterator.next().changeStyle(Selectable.CHANGED_LINKED);
			}
		}
		else if ((event&Selectable.CHANGED_LINKED) >0) {
			addStar();
		}
		else if ((event&Selectable.SELECTED) >0) {
			this.itemWidget.addStyleDependentName("selected");
			for (Iterator<Selectable> iterator = GetLinkedObjects().iterator(); iterator.hasNext();) {
				iterator.next().changeStyle(Selectable.SELECTED_LINKED);
			}
		}
		else if ((event&Selectable.SELECTED_LINKED) >0) {
		}
		else if ((event&Selectable.RESET) >0) {
			this.itemWidget.removeStyleDependentName("selected");
			removeStar();
			if (Main.getInstance().getSelectedElement()== this) {
				for (Iterator<Selectable> iterator = GetLinkedObjects().iterator(); iterator.hasNext();) {
					iterator.next().changeStyle(Selectable.RESET);
				}
			}

		}
		
	}

	public void Deleted() {
		// TODO Auto-generated method stub
		// Delete corresponding components
		Selectable[] array = new Selectable[listOfComponents.size()];
		listOfComponents.toArray(array);
		for(int i = 0; i < array.length; i++) {
			array[i].Deleted();
		}
//		
//		for (Iterator<Selectable> iterator = listOfComponents.iterator(); iterator.hasNext();) {
//			iterator.next().Deleted();
//		}
		
		
		// Delete Nav tab instance
		// Delete tree item itself
	}


}
