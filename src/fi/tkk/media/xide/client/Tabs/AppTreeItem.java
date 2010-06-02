/**
 * 
 */
package fi.tkk.media.xide.client.Tabs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.Data.APElement;
import fi.tkk.media.xide.client.Data.APElementApplication;
import fi.tkk.media.xide.client.Data.APElementHolder;
import fi.tkk.media.xide.client.Data.APElementPage;
import fi.tkk.media.xide.client.Data.EventClickListener;
import fi.tkk.media.xide.client.Data.HasDisplayableProperties;
import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.Data.SaveObjectsListener;
import fi.tkk.media.xide.client.Data.Selectable;
import fi.tkk.media.xide.client.UI.Widget.AdvancedTableV2;
import fi.tkk.media.xide.client.UI.Widget.GoodImage;
import fi.tkk.media.xide.client.UI.Widget.ToggleWidget;
import fi.tkk.media.xide.client.popups.PopupApplicationPublishing;
import fi.tkk.media.xide.client.popups.basic.PopupAreYouSure;
import fi.tkk.media.xide.client.utils.ActionWithTextAndIcon;
import fi.tkk.media.xide.client.utils.ConnectionToServer;
import fi.tkk.media.xide.client.utils.GoodHorizontalAdjustablePanel;
import fi.tkk.media.xide.client.utils.Icons;
import fi.tkk.media.xide.client.utils.ToggleActionWithTextAndAction;
import fi.tkk.media.xide.client.utils.ConnectionToServer.CallbackActions;

/**
 * @author Evgenia Samochadina
 * @date Mar 8, 2009
 *
 */
public class AppTreeItem extends TreeItem implements Selectable, Serializable, APElementHolder{
	public APElement element;
	public APElement elementBackup = null;

	protected boolean isChanged = false;

	transient protected Label itemTextLabel;
	
	// Connection to server
	ConnectionToServer connection;
	
//	private void SetSelectable() {
//		Main.getInstance().setSelectedElement(this);
//	}
	
	public AppTreeItem() {
		super();
		this.element = null;
	}
	
	public AppTreeItem(final APElement elementInitial){
		this();
		initWithAPElement(elementInitial);
	}
	
	public void initWithAPElement(final APElement elementInitial) {
		this.element = elementInitial;
		this.element.setItem(this);
		AbsolutePanel hp  = new AbsolutePanel() {
			public void onBrowserEvent(Event event) 
			{
				switch (event.getTypeInt()) {
//				case Event.ONDBLCLICK:
//					Main.getInstance().SwitchP(Main.PP, (APElementPage)element);
//					break;
				case Event.ONCLICK:
					break;
				}
			}
		};	
		hp.setWidth("100%");
		Image im = elementInitial.getImage();
		if (elementInitial.getType() == APElement.APPLICATION && elementInitial.properties.get(Property.AUTHOR).getStringValue().equals("markku")) {
			im = new Image("elementIcons/application_32_readonly.png");
		}
		im.setStyleName("design-new-left");
		hp.add(im);
		
		DOM.setStyleAttribute(hp.getWidget(0).getElement(), "paddingLeft", "3px");
		DOM.setStyleAttribute(hp.getWidget(0).getElement(), "paddingRight", "3px");

		// Title
		itemTextLabel = new Label(elementInitial.getTitle());

		Widget nameDisplay = null;
		if (elementInitial.getType() == APElement.APPLICATION) {
			nameDisplay  = new Label(elementInitial.getTitle());
			DOM.setElementProperty(nameDisplay.getElement(), "id",
						"label-like-link-top");

			((Label)nameDisplay).addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					Main.getInstance().application = (APElementApplication)elementInitial;
					Main.getInstance().onInternalViewChanged("cap;app=" + elementInitial.properties.get(Property.ID).getStringValue());
				}
			});
		}

//			nameDisplay = new Hyperlink(elementInitial.getTitle(), "cap;app=" + elementInitial.properties.get(Property.ID).getStringValue());
//			((Hyperlink)nameDisplay).addClickHandler(new ClickHandler() {
//
//			public void onClick(ClickEvent event) {
//				
//			}
			
//		});}
		else {
			nameDisplay = new Label(elementInitial.getTitle());
		}
		
		if(elementInitial instanceof APElementApplication) {
			nameDisplay.setStyleName("design-new-app-title");
		}
		else {
			nameDisplay.setStyleName("design-new-page-title");
		}
		
		Label description = new Label(elementInitial.properties.get(Property.DESCR).getStringValue());
		description.setStyleName("design-new-app-descr");
//		Label author = new Label(elementInitial.properties.get(Property.AUTHOR).getStringValue());
//		author.setStyleName("design-LabelSmallText");

		hp.add(nameDisplay);

		// Read only mark
//		if (elementInitial.getType() == APElement.APPLICATION && elementInitial.properties.get(Property.AUTHOR).getStringValue().equals("markku")) {
//			Label readOnly = new Label("read-only mode");
//			readOnly.setStyleName("design-new-app-date-explanation");
//			hp.add(readOnly);
//		}

		
		if(elementInitial instanceof APElementApplication) {
			Label dateWelcomeText = null; 
			Label publishedAddress = null; 
			Label dateValue = null;

			// Modified or created on
			if (elementInitial.properties.get(Property.DATE_MOD).isNull()) {
				dateWelcomeText = new Label("created");
				dateValue = new Label(
						getDate((Date)elementInitial.properties.get(Property.DATE_CR).getValue())
						);
			}
			else {
				dateWelcomeText = new Label("last modified");
				dateValue = new Label(
						getDate((Date)elementInitial.properties.get(Property.DATE_MOD).getValue())
						);
			}
			dateWelcomeText.setStyleName("design-new-app-date-explanation");
			dateWelcomeText.addStyleDependentName("date");

			dateValue.setStyleName("design-new-app-date");
		
			// Delete icon
			GoodImage icon  = new GoodImage(25, "styleImages/delete_big.png");
				icon.setClickListener(new EventClickListener() {
					public void onClick(Event event) {
						element.delete();
					}
				});
				
			icon.setStyleName("design-new-app-delete");
			
//			DOM.setStyleAttribute(icon.getElement(), "float", "right");
//			DOM.setStyleAttribute(icon.getElement(), "marginTop", "-12px");
//			DOM.setStyleAttribute(icon.getElement(), "padding", "0 7px 0 7px");

			
			hp.add(icon);
			hp.add(dateValue);
			hp.add(dateWelcomeText);
			
			// Published
			
			if ((Boolean)elementInitial.properties.get(Property.IS_PUBLISHED).getValue()) {
				// If pubished
				dateWelcomeText = new Label("published");
				publishedAddress = new Label (elementInitial.properties.get(Property.URL).getStringValue());
				dateValue = new Label(
						getDate((Date)elementInitial.properties.get(Property.DATE_PUB).getValue())
						);
				publishedAddress.setStyleName("design-new-app-date-explanation");
				publishedAddress.addStyleDependentName("published-link");
				
				dateWelcomeText.setStyleName("design-new-app-date-explanation");
				dateWelcomeText.addStyleDependentName("published");
				
				dateValue.setStyleName("design-new-app-date");
			
				hp.add(dateValue);
				hp.add(publishedAddress);
				hp.add(dateWelcomeText);
			}

		}

		hp.add(description);
		
		
		setWidget(hp);
		hp.sinkEvents(Event.ONCLICK|Event.ONDBLCLICK);
		getWidget().setStyleName("tree-item-ap");
		DOM.setStyleAttribute(this.getElement(), "borderBottom", "1px solid #EFEFEF");

	}

	public String getTypeName() {
		return element.getTypeName();
	}
	
	public static AppTreeItem getHeaderItem() {
		AppTreeItem fakeItem= new AppTreeItem();
		AbsolutePanel hp  = new AbsolutePanel();
		hp.setWidth("90%");
		Label modDate = new Label("Last modified");
		modDate.setStyleName("design-new-app-date");
		hp.add(modDate);
		fakeItem.setWidget(hp);
		DOM.setStyleAttribute(fakeItem.getElement(), "paddingBottom", "15px");
		return fakeItem;
	}
	/**
	 * Gets date in a format, which is used for application tree in AP
	 * It displays today, yesterday or day.month.year
	 * @param data
	 */
	public static final String getDate(Date data) {
		// Fri Oct 30 14:07:39 EET 2009
		// 01234567890123456789012345678
		// Thu Oct 22 00:54:32 EEST 2009
		// Tue Nov 24 10:44:37 GMT+200 2009
		// 012345678901234567890123456789012
		
		// If date is in known format
		if (data.getTime() == 0) {
			return "never";
		}
		else if (data.getTime() - AdvancedTableV2.getTodayMidnight() > 0) {
			// Today
			return "today, " + data.toString().substring(11, 16);
		}
		else if (AdvancedTableV2.getTodayMidnight() - data.getTime() < 60*60*24*1000) {
			// Yesterday
			return "yesterday, " + data.toString().substring(11, 16);
		}
		else {
			// Parse
			if (data.toString().length() == 28) {

				return data.toString().substring(4, 10) + ", " + data.toString().substring(24, 28);
			}
			else if (data.toString().length() == 29) {
				return data.toString().substring(4, 10) + ", " + data.toString().substring(25, 29);
			}
			else if (data.toString().length() == 32) {
				return data.toString().substring(4, 10) + ", " + data.toString().substring(28, 32);
			}
		}
		return data.toString();
	}
	
	public void onAppSuccessfullyUnpublished() {
		
	}
	
	public void deleteElement() {
		element.delete();
		Main.getInstance().UpdateUI();
	}
	
	public void update(boolean propagate) {
		if (element.getProperties() != null) {
			itemTextLabel.setText(element.getProperties().get(Property.TITLE).getStringValue());
		}
		updateIcons();
		if (propagate) {
			for(int i = 0; i < getChildCount(); i++) {
				((AppTreeItem)getChild(i)).update(true);
			}
			
		}
//		for (int i = 0; i < icons.getWidgetCount(); i++) {
//			Object o = icons.getWidget(i);
//			if (o instanceof Image) {
//			}
//		}
	}
	
	private void updateAPElement() {
		element.updateAPElement(true);
}
	
	/**
	 * Icons:
	 * 	- Publish
	 * 	- Edit
	 * 	- New
	 * 	- Delete
	 */
	public void updateIcons() {

	}
	
	public String getText() {
		return element.getTitle();
	}
	public void addItem(AppTreeItem item) {
		super.addItem(item);
//		this.element.addChild(item.element);
		DOM.setStyleAttribute(DOM.getFirstChild(getElement()), "width", "100%");
		DOM.setStyleAttribute(DOM.getFirstChild(DOM.getFirstChild(DOM.getFirstChild(DOM.getFirstChild(getElement())))), "width", "16px");
		
//		DOM.setStyleAttribute(DOM.getFirstChild(DOM.getFirstChild(getElement())), "width", "100%");
	}
//	 /**
//	   * Adds another item as a child to this one.
//	   * 
//	   * @param item the item to be added
//	   */
//	  public void addItem(TreeItem item) {
//		  super.addItem(item);
//		  
//	  }
	
	public void Select() {
		this.changeStyle(SELECTED);
		
	}
	public void Unselect() {
		this.changeStyle(RESET);
		
	}

	public ArrayList<Selectable> GetLinkedObjects() {
		return new ArrayList<Selectable>();
	}

	public HasDisplayableProperties getProperties() {
		return element;
	}

	public Selectable getValuableElement() {
		return this;
	}
	
	/* (non-Javadoc)
	 * @see fi.tkk.media.xide.client.Data.Selectable#Changed()
	 */
	public void Changed() {
		if (!isChanged) {
//			isChanged = true;
//			changeStyle(CHANGED);
			if(elementBackup == null) {
				elementBackup = element;
				element = element.cloneIt();
			}
			isChanged = true;
			Main.getCurrentView().updatePropertiesList();
			changeStyle(CHANGED);
		}
		
	}

	public void Saved() {
		if (isChanged) {
			isChanged = false;
			elementBackup.beforeDeletion();
			elementBackup = null;
			changeStyle(RESET);
			updateAPElement();
		}
	}
	
	public void Saved(SaveObjectsListener listener) {
		
	}
	
	public void afterSaved() {
		// TODO: manage saved and aftersaved
	}

	public boolean isChanged() {
		return isChanged;
	}

	public void Canceled() {
		if ((isChanged)&& (elementBackup != null)) {
			element = elementBackup;
			elementBackup = null;
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
			getWidget().addStyleDependentName("selected");
			for (Iterator<Selectable> iterator = GetLinkedObjects().iterator(); iterator.hasNext();) {
				iterator.next().changeStyle(Selectable.SELECTED_LINKED);
			}
		}
		else if ((event&Selectable.SELECTED_LINKED) >0) {
			getWidget().addStyleDependentName("selected-linked");
		}
		else if ((event&Selectable.RESET) >0) {
			getWidget().removeStyleDependentName("selected");
			getWidget().removeStyleDependentName("selected-linked");
			if (Main.getInstance().getSelectedElement()== this) {
				for (Iterator<Selectable> iterator = GetLinkedObjects().iterator(); iterator.hasNext();) {
					iterator.next().changeStyle(Selectable.RESET);
				}
			}

		}

	}

	public void Deleted() {
		// TODO implement smth
		element.delete();
	}

	public APElement getAPElement() {
		return element;
	}

	public void onDelete() {
		element.delete();
		this.remove();
	}

	public void onAddNewElement(APElement child) {
		AppTreeTab.getInstance().addElement(this.element, child);		
	}



}
