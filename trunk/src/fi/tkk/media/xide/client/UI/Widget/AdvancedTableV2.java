package fi.tkk.media.xide.client.UI.Widget;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.gen2.table.client.FixedWidthFlexTable;
import com.google.gwt.gen2.table.client.FixedWidthGrid;
import com.google.gwt.gen2.table.client.ScrollTable;
import com.google.gwt.gen2.table.client.AbstractScrollTable.ScrollPolicy;
import com.google.gwt.gen2.table.client.AbstractScrollTable.SortPolicy;
import com.google.gwt.gen2.table.override.client.FlexTable.FlexCellFormatter;
import com.google.gwt.gen2.table.override.client.HTMLTable.CellFormatter;
import com.google.gwt.gen2.table.override.client.HTMLTable.RowFormatter;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.Data.APElement;
import fi.tkk.media.xide.client.Data.APElementApplication;
import fi.tkk.media.xide.client.Data.APElementPage;
import fi.tkk.media.xide.client.Data.BooleanProperty;
import fi.tkk.media.xide.client.Data.Option;
import fi.tkk.media.xide.client.Data.OptionProperty;
import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.Data.Selectable;
import fi.tkk.media.xide.client.DnDTree.DnDTreeItem;
import fi.tkk.media.xide.client.Tabs.PLTreeItem;
import fi.tkk.media.xide.client.Tabs.PropertiesTab;
import fi.tkk.media.xide.client.fs.XIDEFolder;
import fi.tkk.media.xide.client.popups.PopupApplicationPublishing;
import fi.tkk.media.xide.client.popups.PopupSelectInstance;
import fi.tkk.media.xide.client.popups.PropertyEditingPopup;
import fi.tkk.media.xide.client.popups.TagsSearchPopup;
import fi.tkk.media.xide.client.popups.UploadFilePopup;
import fi.tkk.media.xide.client.popups.basic.Popup;
import fi.tkk.media.xide.client.popups.basic.PopupAreYouSure;
import fi.tkk.media.xide.client.popups.utils.interfaces.Action;
import fi.tkk.media.xide.client.utils.ActionWithTextAndIcon;

public class AdvancedTableV2 {

	private FixedWidthGrid dataTable;
	int colNumber;
	private Label labelHint;
	public ScrollTable table;
	
	public ScrollTable getTable() {
		return table;
	}


	private HashMap<String, PropertyWidget> properties;
	private ScrollTable scrollTable;

	 public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

	  public static String date(Date date) {
	    return (date.toString()).substring(4, 11) + (date.toString()).substring(24, 28);
	  }

//	  public static String date() {
//		    Date d = new Date();
//		    return (d.toString()).substring(4, 11) + (d.toString()).substring(25, 29);
//		  }

	  public static String dateFullFormat() {
		    Date d = new Date();
		    String formattedDate = (1900 + d.getYear()) + "-" + (d.getMonth()+1) + "-" + d.getDate();
		    formattedDate = formattedDate + " " + d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds();
		    
		    return formattedDate;
		  }

	  
	  
//	  public static String date(Date date) {
//	    return (date.toString()).substring(4, 11) + (date.toString()).substring(24, 28);
//	  }

		// Fri Oct 30 14:07:39 EET 2009
		// 01234567890123456789012345678
	  public static long getTodayMidnight() {
		  Date d = new Date();
		  int seconds;
		  int minutes;
		  int hours;
		  
		  try {
				hours = Integer.parseInt(d.toString().substring(11, 13));
				minutes = Integer.parseInt(d.toString().substring(14, 16));
				seconds = Integer.parseInt(d.toString().substring(17, 19));
				return (d.getTime() -  (seconds + 60*minutes + 60*60*hours)*1000);
			}
			catch (NumberFormatException e) {
				// Day or month has not been parsed
				return d.getTime();
			}
		}
			
	  
//	  public static String date() {
//		    Date d = new Date();
//		    return (d.toString()).substring(4, 11) + (d.toString()).substring(25, 29);
//		  }

//	  public static String dateFullFormat() {
//		    Date d = new Date();
//		    String formattedDate = (1900 + d.getYear()) + "-" + (d.getMonth()+1) + "-" + d.getDate();
//		    formattedDate = formattedDate + " " + d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds();
//		    
//		    return formattedDate;
//		  }

	  
	  FixedWidthFlexTable footer;
	  FixedWidthFlexTable headerTable;
	  
	  public void setStyle() {
		  DOM.setStyleAttribute(headerTable.getElement(), "width", "100%");
		  DOM.setStyleAttribute(footer.getElement(), "width", "100%");
	  }
	public  AdvancedTableV2 (String[] header) {
		// initiate property list
		properties = new HashMap<String, PropertyWidget>();
		
		colNumber = header.length;
		headerTable = createHeaderTable(header);
		dataTable = CreateDataTable();
		dataTable.resizeColumns(header.length);
		
		AddFakeLine();

		scrollTable = new ScrollTable(dataTable, headerTable);
		footer  = createFooterTable();
		scrollTable.setFooterTable(footer);
		
		
		// Trying to remove small spaces on the right (when table is displayed without scroll)
//		DOM.setStyleAttribute(headerTable.getElement(), "width", "");
//		DOM.setStyleAttribute(footer.getElement(), "width", "");
		
//		// Add footer
//		scrollTable.setFooterTable(createFooterTable());

		// Set some options in the scroll table
		scrollTable.setSortPolicy(SortPolicy.MULTI_CELL);

		scrollTable.setResizePolicy(ScrollTable.ResizePolicy.FILL_WIDTH);

		// TODO: manage with explorer and test case when the window is firstly
		// opened
		// on a full screen (the table is adjusted) and then as a small window
		// (table is not adjusted back)
		scrollTable.setScrollPolicy(ScrollPolicy.BOTH);
		scrollTable.setHeight("100%");
		scrollTable.setWidth("100%");
//		dataTable.setWidth("100%");

		//If scroll table runs for properties window
		if (colNumber < 5 ) {
			
			scrollTable.setColumnWidth(0, 70);

			// Parameter table: contains extra column
			if (colNumber > 2){
				scrollTable.setColumnWidth(1, -1);
				scrollTable.setColumnWidth(3, -1);
//				scrollTable.setMaximumColumnWidth(1, 40);
//				scrollTable.setMaximumColumnWidth(3, 40);
				scrollTable.setMaximumColumnWidth(1, -1);
				scrollTable.setMaximumColumnWidth(3, -1);
			}
		}
		else {
			// AP table
//			scrollTable.setColumnWidth(title.length-1, 25);
//			scrollTable.setColumnWidth(title.length-2, 40);
//			scrollTable.setColumnWidth(title.length-3, 40);
//			scrollTable.setColumnWidth(title.length-4, 40);
			
//			scrollTable.setColumnWidth(0, 200);
//			scrollTable.setMinimumColumnWidth(0, 200);
//			scrollTable.setColumnWidth(1, 200);
//			scrollTable.setMinimumColumnWidth(1, 200);

		}

//		
//		scrollTable.setColumnWidth(0, 70);
//		// Parameter table
//		if (colNumber > 2){
//			scrollTable.setColumnWidth(1, 40);
//		}
		
		
		
		// Add the components to the page
		table = scrollTable;
		


	}

	/**
	 * Create the data table.
	 * 
	 * @return a data table
	 */
	private FixedWidthGrid CreateDataTable() {
		FixedWidthGrid dataTable = new FixedWidthGrid()		{
//			@Override
//			public void onBrowserEvent(Event event) {
//				if (DOM.eventGetType(event) == Event.ONMOUSEMOVE) {
//	//				switch (DOM.eventGetButton(event)) {
//					Element elem = this.getEventTargetRow(event);	
//					int i = this.getRowIndex(elem);
//					if ((i >=0) && (i < properties.size())){
//						labelHint.setText(properties.get(i).getDescription());
//					}
//					else {
//						labelHint.setText("&nbsp;");
//						}
//	//					// If it is right click
//	//					case Event.BUTTON_LEFT :
//	//						break;
//	//				}
//				}
//				else if (DOM.eventGetType(event) ==Event.ONMOUSEOUT){
//					labelHint.setText("&nbsp;");
//				}
//	//			super.onBrowserEvent(event);
//	//			Element elem = this.getEventTargetRow(event);
//			}
	};
	
	dataTable.sinkEvents(Event.ONMOUSEOUT| Event.ONMOUSEMOVE);
//	dataTable.setStyleName("dataWrapper");
	
	return dataTable; 
	}

	public void RemoveAllData(){
		// TODO: check if there is smth to delete 
		int dataTableRowNumber = dataTable.getRowCount();
		for (int i = 0; i < dataTableRowNumber; i++){
			dataTable.removeRow(0);
		}
		properties.clear();
		
	}
	
	/**
	 * Formats string data (in format YYYY-MM-DD HH:MM:SS) to the appropriate one. 
	 * Does not show null dates (0000-00-00 00:00:00)
	 * @return
	 */
	public String displayData(String data) {
//		String result;
		if (data.equals("0000-00-00 00:00:00")) {
			return "";
		}
		else {
			return data;
		}
	}
	
	/**
	 * Formats string boolean value (true or false) to yes/no answers
	 * True == Yes
	 * False == No
	 * @param booleanVariable
	 * @return
	 */
	public String displayBooleanYesNo(String booleanVariable) {
		if (booleanVariable.equals("false")) {
			return "no";
		}
		else {
			return "yes";
		}
	}
	
	public void redrawTable() {
		scrollTable.redraw();
	}
	// Adds to the table information about the element
	public FixedWidthGrid AddAllData(final APElement element) {
		if (element != null) {
			CellFormatter formatter = dataTable.getCellFormatter();
//			RowFormatter rowFormatter = dataTable.getRowFormatter();
			
			dataTable.insertRow(dataTable.getRowCount());
			
			int i = 0;
			for (Iterator<String> iterator = element.getProperties().keySet().iterator(); iterator.hasNext();) {
				String propName = iterator.next();
				
				final Property dataItem = element.getProperties().get(propName);
				
				if (!dataItem.isHiden()) {

					// Value
					HorizontalPanel hp = new HorizontalPanel();
					hp.setWidth("100%");

					PropertyWidget propertyWidget = null;
					if (dataItem instanceof OptionProperty) {
						propertyWidget = new GoodListBox(dataItem);
					}
					else if (dataItem.getStringValue() != null) {
						if (dataItem instanceof BooleanProperty) { 
//						if (dataItem.getStringValue().equals("true") || 
//								dataItem.getStringValue().equals("false")) {
							propertyWidget = new GoodCheckBox(dataItem);
						}
						else 
//							if (dataItem.getStringValue().equals("test"))
							{
							propertyWidget = new GoodTextArea(dataItem, false);
						}
//						else {
//							propertyWidget = new VeryGoodTextBox(dataItem);
//						}
					}
					else {
						propertyWidget = new VeryGoodTextBox(dataItem);
					}
					Widget tb = propertyWidget.getWidget();
					hp.add(tb);
					
					// Logical addition
					properties.put(propName, propertyWidget);
					
					// Styles
//					if ((i/2)*2 == i ) {
//						dataTable.getRowFormatter().addStyleName(i, "design-Advanced-Table-Row-1");
//						tb.setStyleName("design-Advanced-Table-Row-1");
//					}
//					else {
//						dataTable.getRowFormatter().addStyleName(i, "design-Advanced-Table-Row-2");
//						tb.setStyleName("design-Advanced-Table-Row-2");
//					}
					
//					DOM.setStyleAttribute(tb.getElement(), "border", "none");
//					DOM.setStyleAttribute(tb.getElement(), "fontSize", "small");

					// Set property widget and other necessary widgets to appropriate place
					// depending on type of property (property or parameter)
					
					// Not a parameter (only title and value)
						dataTable.setWidget(dataTable.getRowCount() - 1, i, hp);
						i++;
						
					}
			}
			
		}
		return dataTable;
	}
		
	// Adds to the table information about the element
	public FixedWidthGrid AddData(APElement element) {
		final APElement el = element;
//		scrollTable.fillWidth();

//		if (dataTable.getColumnCount() == 0) {
//			dataTable.resizeColumns(6);
//		}
		if (element != null){
			CellFormatter formatter = dataTable.getCellFormatter();
//			RowFormatter rowFormatter = dataTable.getRowFormatter();
			
			dataTable.insertRow(dataTable.getRowCount());
			


			// Draw corresponding properties
			Label l = new Label(element.getProperties().get(Property.TITLE).getStringValue());
			l.setWordWrap(false);
			DOM.setStyleAttribute(l.getElement(), "width", "100%");
//			dataTable.setHTML(dataTable.getRowCount()-1, 0, element.getShortInfo().getProperty(ShortInfo.TITLE).getStringValue());
			dataTable.setWidget(dataTable.getRowCount()-1, 0, l);
			formatter.setStyleName(dataTable.getRowCount()-1, 0, "design-advanced-table-header-element");
			
			l = new Label(element.getProperties().get(Property.DESCR).getStringValue());
			l.setWordWrap(false);
			dataTable.setWidget(dataTable.getRowCount()-1, 1, l);
			formatter.setStyleName(dataTable.getRowCount()-1, 1, "design-advanced-table-header-element");

			l = new Label(element.getProperties().get(Property.DATE_CR).getStringValue());
			l.setWordWrap(false);
			dataTable.setWidget(dataTable.getRowCount()-1, 2, l);
			formatter.setStyleName(dataTable.getRowCount()-1, 2, "design-advanced-table-header-element");
			
			if (element.getProperties().get(Property.URL) != null) {
				String url = element.getProperties().get(Property.URL).getStringValue();
//				l = new Label(element.getProperties().get(Property.URL).getStringValue());
				l = new HTML ("<a href=\" "+ url+ " \"> " + url+ "</a> ");
				l.setWordWrap(false);
			}
			else {
				l = new Label("");
			}
			dataTable.setWidget(dataTable.getRowCount()-1, 3, l);
			formatter.setStyleName(dataTable.getRowCount()-1, 3, "design-advanced-table-header-element");

			if (element.getProperties().get(Property.DATE_PUB) != null) {
				l = new Label(displayData(element.getProperties().get(Property.DATE_PUB).getStringValue()));
			}
			else {
				l = new Label("");
			}
			l.setWordWrap(false);
			dataTable.setWidget(dataTable.getRowCount()-1, 4, l);
			formatter.setStyleName(dataTable.getRowCount()-1, 4, "design-advanced-table-header-element");
			
			if (element.getProperties().get(Property.IS_PUBLISHED) != null) {
				l = new Label(displayBooleanYesNo(element.getProperties().get(Property.IS_PUBLISHED).getStringValue()));
			}
			else {
				l = new Label("");
			}
			l.setWordWrap(false);
			dataTable.setWidget(dataTable.getRowCount()-1, 5, l);
			formatter.setStyleName(dataTable.getRowCount()-1, 5, "design-advanced-table-header-element");

			// Draw buttons
//			// Delete button
//			Button deleteButton = new GoodButton(7);
////			deleteButton.setText("Delete");
//			deleteButton
//			.setHTML("<img src=\""+DELETE +"\" class=\"gwt-Image\"/>");
//			deleteButton.addClickListener(new ClickListener() {
//				public void onClick(Widget sender) {
//					(new PopupAreYouSure("You are going to delete an object. Are you sure?", new ClickListener() {
//						public void onClick(Widget sender) {
//							el.delete();
//							Main.getInstance().UpdateUI();
//						}
//					}, null)).showPopup(true);
//					
//				}
//			});
//			
//			// Action button
//			Button actionButton =null;
//			if (element.getType() == APElement.APPLICATION) {
//				if (element.getProperties().get(Property.IS_PUBLISHED).getStringValue().equals("false")) {
//			// Publish
//					// Not published
//					actionButton = new GoodButton("Publish", 8);
//					actionButton
//					.setHTML("<img src=\""+NOTPUBLISHED +"\" class=\"gwt-Image\"/>");
//					actionButton.addClickListener(new ClickListener() {
//					public void onClick(Widget sender) {
//						(new PopupApplicationPublishing((APElementApplication)el)).showPopup(true);
//						}
//									
//					});
//					DOM.setStyleAttribute(actionButton.getElement(), "color", "green");
//				}
//				else {
//			// Unpublish
//					actionButton = new GoodButton("UnPublish", 10);
//					actionButton
//					.setHTML("<img src=\""+PUBLISHED +"\" class=\"gwt-Image\"/>");
//					actionButton.addClickListener(new ClickListener() {
//					public void onClick(Widget sender) {
//						new ModalDimmedPopup("Application is successfully unpublished! ").showPopup(true);
//						el.getProperties().get(Property.DATE_PUB).setValue("0000-00-00 00:00:00");
//						el.getProperties().get(Property.IS_PUBLISHED).setValue("false");
//						el.getProperties().get(Property.URL).setValue("");
//						Main.getInstance().UpdateUI();
//					}
//					});
//
//					DOM.setStyleAttribute(actionButton.getElement(), "color", "red");
//				}
//				actionButton.setWidth("100%");
//			}
//			else if (element.getType() == APElement.WPAGE) {
//				
//			// Edit
//				
//				actionButton = new GoodButton("Edit", 8);
//				actionButton
//				.setHTML("<img src=\""+EDIT +"\" class=\"gwt-Image\"/>");
//				final APElementPage ell = (APElementPage) element;
//				actionButton.addClickListener(new ClickListener() {
//				public void onClick(Widget sender) {
//					Main.getInstance().SwitchP(Main.PP, ell);
//					}
//				});
//				actionButton.setWidth("100%");
//				
//			}
//			
			// Panel which contains buttons
			
			// Get action list
			ActionWithTextAndIcon[] actionList = element.getContextMenuItems();
			
			DockPanel hp = new DockPanel();
			hp.setWidth("100%");
			
			for(int k = 0; k < actionList.length; k++) {
				// If action exist
				
				if (actionList[k] != null) {
					// Get icon
					Widget action = actionList[k].getIcon();
					DOM.setStyleAttribute(action.getElement(), "paddingLeft", "2px");
					DOM.setStyleAttribute(action.getElement(), "paddingRight", "2px");
					hp.add(action, DockPanel.WEST);
				}
			}
			
//			dataTable.setWidget(dataTable.getRowCount()-1, 6, hp);
			
//			formatter.removeStyleName(dataTable.getRowCount()-1, 6, "design-published-application");
			if (element.getProperties().get(Property.IS_PUBLISHED) != null) {
				
			
				if (element.getProperties().get(Property.IS_PUBLISHED).getStringValue().equals("true")) {
					formatter.setStyleName(dataTable.getRowCount()-1, 0,"design-published-application");
					formatter.setStyleName(dataTable.getRowCount()-1, 1,"design-published-application");
					formatter.setStyleName(dataTable.getRowCount()-1, 2,"design-published-application");
					formatter.setStyleName(dataTable.getRowCount()-1, 3,"design-published-application");
					formatter.setStyleName(dataTable.getRowCount()-1, 4,"design-published-application");
					formatter.setStyleName(dataTable.getRowCount()-1, 5,"design-published-application");
				}
			}
		}
			
		scrollTable.redraw();
		return dataTable; 
	}
	
	// Adds matrix f fake properties for the explorer tab table
	public FixedWidthGrid AddData(String[][] data) {
//		scrollTable.fillWidth();

		if (data != null){
			// Add data to the end of the dataTable 
			// + 1 column fot the action button
			int dataSizeVert = data.length;
			int dataSizeHor = data[0].length+1;
			CellFormatter formatter = dataTable.getCellFormatter();
			
			for (int i = 0; i < dataSizeVert; i++) {
				dataTable.insertRow(dataTable.getRowCount());
				for (int j = 0; j < dataSizeHor; j++) {
					if (j != dataSizeHor - 1) {
						// Draw corresponding properties
						dataTable.setHTML(i, j, data[i][j]);
						formatter.setStyleName(i, j, "design-advanced-table-header-element");
					}
					else {
						// Draw button
						Button actionButton ;

						if (data[i][j-2].equals("-")) {
							// Not published
							actionButton = new Button("Publish");
							actionButton.addClickListener(new ClickListener() {

								public void onClick(Widget sender) {
//									PopUps panel= new PopUps(
//											new Property("name", "APP"),
//											new Property("name", "page1"),
//											new Property("name", "page2")); 
								}
								
							});
							DOM.setStyleAttribute(actionButton.getElement(), "color", "green");
						}
						else {
							actionButton = new Button("UnPublish");
							actionButton.addClickListener(new ClickListener() {
								public void onClick(Widget sender) {
									new Popup("Sorry, application cannot be unpublished now. ");
								}
							});
							DOM.setStyleAttribute(actionButton.getElement(), "color", "red");
						}
						actionButton.setWidth("100%");
						dataTable.setWidget(i,j, actionButton);
					}
				}
			}
			
			
		}
		return dataTable; 
	}
	
//	private int updateLevel = 0;
	
	/**
	 * Changes properties to which PropertyWidgets are related. 
	 * Iterate through the set of properties given and update corresponding property widget: replace old properties with the new one 
	 */
	public void updatePropertiesLinks(HashMap<String, Property> newPties) {

		if (newPties != null) {
			Set<String> keys = newPties.keySet();
	
			for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
				String ptiesCode = iterator.next();
				if (properties.get(ptiesCode) instanceof PropertyWidget) {
					((PropertyWidget)properties.get(ptiesCode)).setProperty(newPties.get(ptiesCode));
				}
				
			}
		}
	}
	
	/**
	 * Iterate through the set of propertyWidgets displayed and updates the value which is shown in the UI 
	 */
	public void updatePropertiesValues() {

			Set<String> widgetCodes = properties.keySet();
	
			for (Iterator<String> iterator = widgetCodes.iterator(); iterator.hasNext();) {
				String widgetCode = iterator.next();
				if (properties.get(widgetCode) instanceof PropertyWidget) {
					((PropertyWidget)properties.get(widgetCode)).showProperty();
				}
				
		}
	}
	/**
	 * Abstract base class for all widgets which represent different properties 
	 * (GoodTextBox, GoodCheckBox, GoodTextArea, GoodCheckList)
	 *  
	 * @author evgeniasamochadina
	 *
	 */
	abstract class  PropertyWidget implements KeyboardListener, ClickListener{

		FocusWidget widget;
		Property property;
		
		/*
		 * Default constructor
		 */
		public PropertyWidget () {
			
		}
		
		/**
		 * Gets the widge, which displays the property value
		 * @return
		 */
		public FocusWidget getWidget(){
			return widget;
		}
		
		/**
		 * Initiates the PropertyWidget member fields. Receives widget and the property,
		 * initiates the widget with the proper value, adds click listeners
		 * @param widget 
		 * @param property
		 */
		public void setWidgetAndProperty(FocusWidget widget, Property property) {
			
			this.widget = widget;
			this.property = property;
			showProperty();
			
			this.widget.addClickListener(this);
			this.widget.addKeyboardListener(this);
		}
		
		/**
		 *  Process key down events from keyboard, which are sent by the widget
		 */
		public void onKeyDown(Widget sender, char keyCode, int modifiers) {
		}
		
		/**
		 *  Process key press events from keyboard, which are sent by the widget
		 */
		public void onKeyPress(Widget sender, char keyCode, int modifiers) {
		}

		/**
		 *  Process key up events from keyboard, which are sent by the widget
		 *  This should be overwritten in the child class to get the value of the widget correctly
		 *  and save it into the property
		 */
		public abstract void onKeyUp(Widget sender, char keyCode, int modifiers);

		/**
		 * Process mouse click event sent by widget. Should be overwritten in the child class.
		 */
		public abstract void onClick(Widget sender);
		
		public void showHint() {
			if (!Main.getInstance().getSelectedElement().isChanged()) {

				// If current selected element is PL element
				// then make it changed if any property has edited
				if (Main.getInstance().getSelectedElement() instanceof PLTreeItem) {
						Main.showHintPopup(1);
				}
				// For all other selected elements
				// only properties which are not parameters should initiate element changing process
				else if (property.getProperty_type() != Property.TYPE_PARAMETER) {
						Main.showHintPopup(0);
				}

				// As the status is not set yet
				// set changed status

			}
		}
		
		public void setEverythingChanged() {
			if (!Main.getInstance().getSelectedElement().isChanged()) {
				
				// If current selected element is PL element
				// then make it changed if any property has edited
				if (Main.getInstance().getSelectedElement() instanceof PLTreeItem) {
						Main.getInstance().setSelectedElementChanged();
					
				}
				
				// For all other selected elements
				// only properties which are not parameters should initiate element changing process
				else if (property.getProperty_type() != Property.TYPE_PARAMETER) {
						Main.getInstance().setSelectedElementChanged();
				}
				else {
					// It's parameter
					if (Main.getInstance().getSelectedElement().getValuableElement() instanceof Component) {
						Main.onSlotChangedFirst((Component)Main.getInstance().getSelectedElement().getValuableElement());
					}
				}	
			}
		}

		public abstract void showProperty();
		public void setProperty(Property property) {
			this.property = property;
		}
		
	}
	
	class GoodCheckBox extends PropertyWidget{
		// Link to the UI widget which represents  the property of boolean type
		CheckBox checkbox;
		
		/**
		 * Constructs property object 
		 * 
		 * @param property property to display
		 */
		public GoodCheckBox (Property property) {
			super();
			checkbox = new CheckBox();
			this.setWidgetAndProperty(checkbox, property);
		}

		public void onClick(Widget sender) {
			showHint();
			setEverythingChanged();
			
			// If onClick then the value of the checkbox is definitely changed
			// Update UI

			// Get the value of the boolean property
			String value = null;
			if (((CheckBox)sender).isChecked()) {
				value = "true";
			}
			else {
				value = "false";
			}
				// If current editing property is Parameter of Template (PL tab)
				// then new value should be stored as a property default value
				if ((Main.getInstance().getSelectedElement() instanceof PLTreeItem) 
						&& (property.getProperty_type() == Property.TYPE_PARAMETER)) {
					property.setDefaultValue(value);
				}
				// In all other cases new value should be stored as a property value 
				else {
					property.setValue(value);
				}
				Main.onSlotValueChanged();
				
				Main.getInstance().UpdateUI(Main.MAIN_TAB);
				Main.getCurrentView().updatePropertiesStyles();
					
		}

		public void onKeyUp(Widget sender, char keyCode, int modifiers) {
			
		}

		public void showProperty() {

			if (property.getStringValue().equals(Property.BOOLEAN_TRUE)) {
				checkbox.setValue(true);
			}
			else {
				checkbox.setValue(false);
			}

			// Indicates is this property editable now
			boolean isEditableNow= (property.isEditableNow() && !property.isNeverEditable());
			checkbox.setEnabled(isEditableNow);
			checkbox.setWidth("99%");
			DOM.setStyleAttribute(checkbox.getElement(), "background", "url('canvasChessYellow2.png')");
		}
	}
	
	class GoodListBox extends PropertyWidget{
		// Link to the UI widget which represents  the property of boolean type
		ListBox listBox;
		
		/**
		 * Constructs property object 
		 * 
		 * @param property property to display
		 */
		public GoodListBox (Property property) {
			super();
			listBox = new ListBox();
			this.setWidgetAndProperty(listBox, property);
		}

		public void onClick(Widget sender) {
			showHint();
			
			
			int oldValue = ((OptionProperty)property).getItemNumberToShow();
			
			int itemNumber = ((ListBox)sender).getSelectedIndex();

			if (oldValue != itemNumber) {
				
				// Value has changed!
				setEverythingChanged();
			
				// If current editing property is Parameter of Template (PL tab)
				// then new value should be stored as a property default value
				if ((Main.getInstance().getSelectedElement() instanceof PLTreeItem) 
						&& (property.getProperty_type() == Property.TYPE_PARAMETER)) {
					// TODO: pay attention to here!
					((OptionProperty)property).setDefaultValue(((ListBox)sender).getValue(itemNumber));
				}
				// In all other cases new value should be stored as a property value 
				else {
					property.setValue(((ListBox)sender).getValue(itemNumber));
				}
				
				Main.onSlotValueChanged();
				Main.getInstance().UpdateUI(Main.MAIN_TAB);
				Main.getCurrentView().updatePropertiesStyles();
			}		
		}

		public void onKeyUp(Widget sender, char keyCode, int modifiers) {
			
		}

		public void updateListContent() {
			listBox.clear();
			OptionProperty p = (OptionProperty)property;
			if(property.getValue() != null) {
				Option[] options = (Option[])property.getValue();
			
				for (int i = 0; i < options.length; i++) {
					listBox.addItem(options[i].getLabel(), options[i].getValue());
				}
				listBox.setItemSelected(p.getItemNumberToShow(), true);
			}
		}
		
		public void showProperty() {
			
			updateListContent();
			boolean isEditableNow= (property.isEditableNow() && !property.isNeverEditable());
			
			listBox.setEnabled(isEditableNow);
			listBox.setWidth("99%");
			DOM.setStyleAttribute(listBox.getElement(), "background", "url('canvasChessYellow2.png')");
		}
	}

	
	public abstract class GoodTextBoxBase extends PropertyWidget{

		// Link to the UI widget which represents  the property of boolean type
		protected TextBoxBase textArea;
		String currentValue;
		/**
		 * Constructs property object 
		 * 
		 * @param property property to display
		 */
		public GoodTextBoxBase (Property property) {
			super();
			
			// Text area object will be created and set in child class
//			textArea = new TextArea();
//			currentValue = property.getStringValue();
//			this.setWidgetAndProperty(textArea, property);
			
		}

		public void onClick(Widget sender) {
			showHint();
			
			if(property.getValue() instanceof ArrayList) {
				new TagsSearchPopup((ArrayList<String>)property.getValue(), null,
						new Action() {
								public void doAction() {
									if (!Main.getInstance().getSelectedElement().isChanged()) {
												Main.getInstance().setSelectedElementChanged();
										}

									Main.getInstance().UpdateUI(Main.BOTTOM_TAB);
									
								}}
					);
			}

			
		}

		public void onKeyUp(Widget sender, char keyCode, int modifiers) {
			// Set currently selected element status as changed
			
			setEverythingChanged();

			if (!currentValue.equals(((TextBoxBase)sender).getText())) {
				currentValue = ((TextBoxBase)sender).getText();
			
				// If current editing property is Parameter of Template (PL tab)
				// then new value should be stored as a property default value
				if ((Main.getInstance().getSelectedElement() instanceof PLTreeItem) 
						&& (property.getProperty_type() == Property.TYPE_PARAMETER)) {
					property.setDefaultValue(currentValue);
				}
				// In all other cases new value should be stored as a property value 
				else {
					property.setValue(currentValue);
				}
				
//				// Update web page source code if a component has been changed
//				if (Main.getInstance().getSelectedElement() instanceof WebPage) {
////					((WebPage)Main.getInstance().getSelectedElement()).updateSlotInfo();
//				}
//				else if (Main.getInstance().getSelectedElement() instanceof Component) {
//					// TODO: remove hardcoded getWebPage. Probably slot has to be changed instead of a page? 
//					((WebPage)((Component)Main.getInstance().getSelectedElement()).getParentElement().getParentElement()).updateSlotInfo();
//				}
//				
				Main.onSlotValueChanged();
				
				Main.getInstance().UpdateUI(Main.MAIN_TAB);
				Main.getCurrentView().updatePropertiesStyles();
			}
		}

		public void showProperty() {
			
			// Indicates is this property editable now
			boolean isEditableNow= (property.isEditableNow() && !property.isNeverEditable());
			
			if ((property.getValue() != null) && (!property.getValue().equals(""))){
				textArea.setText(property.getStringValue() );
			}
			else if ((property.getDefaultValue() != null)&& (!property.getDefaultValue().equals(""))){
				textArea.setText(property.getDefaultValue());
				if (isEditableNow) {
					DOM.setStyleAttribute(textArea.getElement(), "background", "url('canvasChess2.png')");
				}
			}
			else {
				textArea.setText("");
			}
			
			// Editability and styling

			if (isEditableNow) {
				if (textArea.getText().equals("")) {
					DOM.setStyleAttribute(textArea.getElement(), "background", "url('canvasChessYellow2.png')");
//					setStyleName("design-label-disabled-empty");
				}
				else {
					
//					setStyleName("design-label-disabled");
				}
			}
			else {
//				setStyleName("design-label-enabled");
				DOM.setStyleAttribute(textArea.getElement(), "fontStyle", "italic");
				textArea.setEnabled(false);
			}

			textArea.setWidth("99%");
//			textArea.setVisibleLines(1);
		}
		
	}
	class VeryGoodTextBox extends GoodTextBoxBase{

		public VeryGoodTextBox(Property property) {
			super(property);
			textArea = new TextBox();
			currentValue = property.getStringValue();
			this.setWidgetAndProperty(textArea, property);
		}
		
		 public void showProperty() {
			 super.showProperty();
		 }
		
	}

	class GoodTextArea extends GoodTextBoxBase{

		boolean isExpandable;
		/**
		 * 
		 * @param property
		 * @param isExpandable Indicates whether the text box will be expanded to text area when it is clicked
		 */
		public GoodTextArea(Property property, boolean isExpandable) {
			super(property);
			this.isExpandable = isExpandable;
			textArea = new TextArea();
			if (isExpandable) {
//				textArea.addFocusHandler( new FocusHandler() {
//
//					public void onFocus(FocusEvent event) {
//						if (!Main.IS_RUNNING_ON_WINDOWS) {
//							((TextArea)textArea).setVisibleLines(-1);
//						}
//						else {
//							((TextArea)textArea).setVisibleLines(1);
//						}
//						
//						if (((TextArea)textArea).getVisibleLines() > 6) {
//							((TextArea)textArea).setVisibleLines(6);
//						}
//						redrawTable();						
//					}});
//				
//				textArea.addFocusListener(new FocusListener() {
//									public void onFocus(Widget arg0) {
//										if (!Main.IS_RUNNING_ON_WINDOWS) {
//											((TextArea)textArea).setVisibleLines(-1);
//										}
//										else {
//											((TextArea)textArea).setVisibleLines(1);
//										}
//										
//										if (((TextArea)textArea).getVisibleLines() > 6) {
//											((TextArea)textArea).setVisibleLines(6);
//										}
//										redrawTable();
//									}
//					
//									public void onLostFocus(Widget arg0) {
//										((TextArea)textArea).setVisibleLines(1);
//									}});
			}
			currentValue = property.getStringValue();
			this.setWidgetAndProperty(textArea, property);
		}
		
		public void onClick(Widget sender) {
			super.onClick(sender);
			if (isExpandable) {
				if (!Main.IS_RUNNING_ON_WINDOWS) {
					((TextArea)textArea).setVisibleLines(-1);
				}
				else {
					((TextArea)textArea).setVisibleLines(1);
				}
			}
//			new TextArea().get
//			 ((TextArea)textArea).setVisibleLines(4);
		}
		
		 public void showProperty() {
			 super.showProperty();
			 ((TextArea)textArea).setVisibleLines(1);
		 }
		
	}

	
	/**
	 * Add properties' data to the data table. This method is used to add properties and parameters.
	 * 
	 * @return a data table
	 */
	public FixedWidthGrid AddData(HashMap<String, Property> data) {

		if (data != null){
			// Add data to the end of the dataTable
			int dataTableRowNumber = properties.size();
	
				Set<String> keys = data.keySet();
				int i = dataTableRowNumber;
				// Add some data to the data table
				for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
					final String propertyID = iterator.next();
					// Set the data in the current row
					final Property dataItem = data.get(propertyID);
					
					if (!dataItem.isHiden()) {

						// Insert row
						dataTable.insertRow(dataTable.getRowCount());
	
						dataTable.getCellFormatter().setStyleName(dataTable.getRowCount()-1, 0, "design-properties-table-row");
						dataTable.getCellFormatter().setStyleName(dataTable.getRowCount()-1, 1, "design-properties-table-row");
	
						// Title and description
						VerticalPanel vpTD = new VerticalPanel();
						vpTD.setWidth("100%");
						
						Label title = new Label(dataItem.getName());
						title.setStyleName("design-properties-table-title");
						
						vpTD.add(title);
						
						Label description = new Label (dataItem.getDescription());
						description.setWordWrap(true);
						description.setStyleName("design-properties-table-text-area-description");

						vpTD.add(description);
						dataTable.setWidget(i, 0, vpTD);
		
						// Value
						VerticalPanel hp = new VerticalPanel();
						hp.setWidth("90%");

						PropertyWidget propertyWidget = null;
						if (dataItem instanceof OptionProperty) {
							propertyWidget = new GoodListBox(dataItem);
						}
						else if (dataItem.getStringValue() != null) {
							if (dataItem instanceof BooleanProperty) { 
//							if (dataItem.getStringValue().equals("true") || 
//									dataItem.getStringValue().equals("false")) {
								propertyWidget = new GoodCheckBox(dataItem);
							}
							else 
//								if (dataItem.getStringValue().equals("test"))
								{
								propertyWidget = new GoodTextArea(dataItem, true);
							}
//							else {
//								propertyWidget = new VeryGoodTextBox(dataItem);
//							}
						}
						else {
							propertyWidget = new VeryGoodTextBox(dataItem);
						}
						Widget tb = propertyWidget.getWidget();
						
						tb.setStyleName("design-properties-table-text-area");
						
						hp.add(tb);
//						Label description = new Label (dataItem.getDescription());
//						description.setWordWrap(true);
//						description.setStyleName("design-properties-table-text-area-description");
//						hp.add(description);
						
						// Logical addition
						properties.put(propertyID, propertyWidget);
						
						// Styles
						if ((i/2)*2 == i ) {
//							dataTable.getRowFormatter().addStyleName(i, "design-Advanced-Table-Row-1");
//							tb.setStyleName("design-Advanced-Table-Row-1");
						}
						else {
//							dataTable.getRowFormatter().addStyleName(i, "design-Advanced-Table-Row-2");
//							tb.setStyleName("design-Advanced-Table-Row-2");
						}
						
//						DOM.setStyleAttribute(tb.getElement(), "border", "none");
//						DOM.setStyleAttribute(tb.getElement(), "fontSize", "small");
	
						// Set property widget and other necessary widgets to appropriate place
						// depending on type of property (property or parameter)
						
						// Not a parameter (only title and value)
						if (dataItem.getProperty_type() != Property.TYPE_PARAMETER){
							dataTable.setWidget(i, 1, hp);
							
	//						formatter.setStyleName(i, 1,
	//						"design-Advanced-Table-Row-1");
							
	//						formatter.setStyleName(i, 1,
	//						"design-advanced-table-editable-label");
						}
						// A parameter (title, type, value and buttons)
						else {
							
							// Display type
							dataTable.setHTML(i, 1, dataItem.getType());
							
							HorizontalPanel hpActionButtons = new HorizontalPanel();
							hpActionButtons.add(new Button("Edit", new ClickListener() {

								public void onClick(Widget arg0) {
									
									new PropertyEditingPopup(dataItem);
								}}));
							
							hpActionButtons.add(new Button("Bind", new ClickListener() {

								public void onClick(Widget arg0) {
									
									new PopupSelectInstance(dataItem);
								}}));
														
							dataTable.setWidget(i, 3, hpActionButtons);
							
							dataTable.setWidget(i, 2, hp);
//							DOM.setStyleAttribute(tb.getElement(), "border", "none");
						
	//						formatter.setStyleName(i, 2,
	//						"design-advanced-table-editable-label");
						}
						
						i++;
					}
				}
		}
		
		
		scrollTable.redraw();
		// Return the data table
		return dataTable;

	}

	/**
	 * Add fake data line to the data table.
	 * 
	 * @return a data table
	 */
	public FixedWidthGrid AddFakeLine() {
	
			int dataTableRowNumber = dataTable.getRowCount();
			dataTable.insertRow(dataTableRowNumber);
			// Add data to the end of the dataTable

			
			for (int i = 0; i < colNumber; i++) {
				dataTable.setHTML(dataTableRowNumber, i, "&nbsp;");
				// Styles
			}
			
			if ((dataTableRowNumber/2)*2 == dataTableRowNumber ) {
				dataTable.getRowFormatter().addStyleName(dataTableRowNumber, "design-Advanced-Table-Row-1");
			}
			else {
				dataTable.getRowFormatter().addStyleName(dataTableRowNumber, "design-Advanced-Table-Row-2");
			}
			
		// Return the data table
		return dataTable;

	}

//	  /**
//	   * Create the footer table.
//	   * 
//	   * @return a footer table
//	   */
//	  private FixedWidthFlexTable createFooterTable() {
//	    // Create a new table
//	    FixedWidthFlexTable footerTable = new FixedWidthFlexTable();
//	    FlexCellFormatter formatter = footerTable.getFlexCellFormatter();
//
//
//	    labelHint = new Label("&nbsp;");
//	    labelHint.setStyleName("design-LabelSmallText");
//	    // Level 1 headers
//	    footerTable.setWidget(0, 0, labelHint);
//    	formatter.setColSpan(0, 0, colNumber);
//
////    	formatter
////		.setStyleName(0, 0, "design-advanced-table-title-element");
//
//	    // Return the footer table
//	    return footerTable;
//	  }

	
	/**
	 * Creates title table
	 * 
	 * @param headers
	 *            List of title names for table
	 * @return
	 */
	private FixedWidthFlexTable createHeaderTable(String[] headers) {

		// Create a new table
		FixedWidthFlexTable headerTable = new FixedWidthFlexTable();
		FlexCellFormatter formatter = headerTable.getFlexCellFormatter();

		// Add headers according to the input parameters
		for (int i = 0; i < headers.length; i++) {
			headerTable.setHTML(1, i, headers[i]);
			formatter.setHorizontalAlignment(1, i,
					HasHorizontalAlignment.ALIGN_CENTER);
			formatter
					.setStyleName(1, i, "design-properties-table-header");
		}

		// Return the title table
//		DOM.setStyleAttribute(headerTable.getElement(), "width", "100%");
		return headerTable;
	}
	
	private FixedWidthFlexTable createFooterTable() {

		// Create a new table
		FixedWidthFlexTable footerTable = new FixedWidthFlexTable();
		FlexCellFormatter formatter = footerTable.getFlexCellFormatter();

		
		// Add headers according to the input parameters
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		
		Button saveButton = new Button();
		saveButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				if (Main.getInstance().getSelectedElement().isChanged()) {
					Main.getInstance().setSelectedElementSaved();
					Main.getInstance().UpdateUI();
				}
			}
        });
		saveButton.setStyleName("design-new-cap-footer-button");
		saveButton.setHTML("<img src=\"TagIcons/drop-yes.gif\" class=\"gwt-Image\"/> Save");
//		buttonPanel.add(saveButton);

		Button cancelButton = new Button();
		cancelButton.addClickListener(new ClickListener() {

			public void onClick(Widget sender) {
				// TODO: ADD SAVING
				if (Main.getInstance().getSelectedElement().isChanged()) {
					Main.getInstance().setSelectedElementCanceled();
					Main.getInstance().UpdateUI();
				}
				
			}
			
		});
		cancelButton.setStyleName("design-new-cap-footer-button");
		cancelButton.setHTML("<img src=\"TagIcons/drop-no.gif\" class=\"gwt-Image\"/> Cancel");

//		
//		buttonPanel.add(cancelButton);

		buttonPanel.add((new StyledButton("Save", "TagIcons/drop-yes.gif", new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (Main.getInstance().getSelectedElement().isChanged()) {
					Main.getInstance().setSelectedElementSaved();
					Main.getInstance().UpdateUI();
				}
			}}, StyledButton.STYLE_GREY)).getButton());
		
		buttonPanel.add((new StyledButton("Cancel", "TagIcons/drop-no.gif", new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (Main.getInstance().getSelectedElement().isChanged()) {
					Main.getInstance().setSelectedElementCanceled();
					Main.getInstance().UpdateUI();
				}
			}}, StyledButton.STYLE_GREY)).getButton());

		footerTable.setWidget(0, 0, buttonPanel);
		formatter.setColSpan(0, 0, 2);
		
//		DOM.setStyleAttribute(footerTable.getElement(), "width", "100%");
		// Return the title table
		return footerTable;
	}
}
