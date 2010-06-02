/**
 * 
 */
package fi.tkk.media.xide.client.popups.utils;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Data.ArrayProperty;
import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.UI.Widget.StyledButton;
import fi.tkk.media.xide.client.popups.utils.interfaces.Action;
import fi.tkk.media.xide.client.popups.utils.interfaces.UIElementWithCheck;
import fi.tkk.media.xide.client.utils.ConnectionToServer;

/**
 * 
 * Represents a form object, shown as a popup. Form can automatically check the
 * filling of its fields. The check procedure can include only non-empty check,
 * client-side check or server-side check. It is required that field implements
 * {@link UIElementWithCheck} interface.
 * 
 * @author Evgenia Samochadina
 * @date Sep 23, 2009
 * 
 */
public class FormPopup {

	protected PopupContainer popup;
	
	// List of UI elements
	protected ArrayList<UIElementWithCheck> uiElements;

	// IDs, which can be used to get the the element
	// Save page as new component
	public static final int UI_ID 				= 0;
	public static final int UI_TITLE 			= 1;
	public static final int UI_DESCR 			= 2;
	public static final int UI_TAGS 			= 3;

	// Create new application
	public static final int CNA_TITLE 			= 0;
	public static final int CNA_DESCR 			= 1;
	public static final int CNA_REL_URL 		= 2;
	public static final int CNA_CREATE_PAGE 	= 3;
	public static final int CNA_IS_PUBLIC 		= 4;
	
	// Create new page
	public static final int CNP_TITLE 			= 0;
	public static final int CNP_DESCR 			= 1;
	public static final int CNP_REL_URL 		= 2;
	public static final int CNP_IS_MAIN_PAGE 	= 3;
	public static final int CNP_GEN_XML 		= 4;
	public static final int CNP_ADD_CONTAINER 	= 5;
	
	// Create new tag
	public static final int CNT_TITLE 			= 0;
	public static final int CNT_DESCR 			= 1;
	
	// Width of the left column in the table representing UI elements in %
	// Default value is 50%, can be changed in the constructor of the class
	public  int LEFT_COLUMN_WIDTH = 50;
	
	// Total number of UI elements, which needs to be checked
	// Is initiated when new check has started, and decreases when answer has come
	int numberOfAnswersRequired;

	// Connection to server
	protected ConnectionToServer connectionToServer;

	// Table where UI elements are displayed
	FlexTable table;
	
	// Vertical panel for custom widgets. Is placed on the top of the form, under title
	VerticalPanel customVP;

	/**
	 * Default constructor for FormPopup. Creates an empty form, initiates all
	 * internal fields, creates empty table grid for UI elements and OK button.
	 * When user presses OK button, it initiates the check procedure iteratively
	 * for each UI element.
	 */
	public FormPopup(String title) {
//		super(title);
		
		popup = PopupContainer.getPopup(); 
		popup.addHeader(title);
		
		initiateForm();
	}

	/**
	 * Default constructor for FormPopup. Creates an empty form, initiates all
	 * internal fields, creates empty table grid for UI elements and OK button.
	 * When user presses OK button, it initiates the check procedure iteratively
	 * for each UI element.
	 * 
	 * Changes popup dnd header text to a {@
	 */
	
	
	/**
	 * Default constructor for FormPopup. Creates an empty form, initiates all
	 * internal fields, creates empty table grid for UI elements and OK button.
	 * When user presses OK button, it initiates the check procedure iteratively
	 * for each UI element.
	 * 
	 * Changes popup d-n-n header text to @param title, adds a header with the @param secondTitle text
	 * @param title
	 * @param secondTitle
	 */
	public FormPopup(String title, String secondTitle) {
//		super(title, secondTitle);

		popup = PopupContainer.getPopup();
		popup.setText(title);
		
		Label headerDescr = new Label(secondTitle);
		PopupContainerBase.addStyle(headerDescr, PopupContainerBase.HEADER_DESCRIPTION);
		popup.addHeader(headerDescr);
		
		initiateForm();
	}

	/**
	 * Default constructor for FormPopup. Creates an empty form, initiates all
	 * internal fields, creates empty table grid for UI elements and OK button.
	 * When user presses OK button, it initiates the check procedure iteratively
	 * for each UI element.
	 * 
	 * Changes popup d-n-n header text to @param title, adds a header with the @param secondTitle text
	 * @param title
	 * @param secondTitle
	 */
	public FormPopup(String title, String secondTitle, int leftColumnWidth) {

		this(title, secondTitle);
		
		LEFT_COLUMN_WIDTH = leftColumnWidth;
	}

	
	private void initiateForm() {
		
		// Initiate internal variables
		uiElements = new ArrayList<UIElementWithCheck>();
		
		// A panel, which contains child-specific widgets and the table with form elements  
		VerticalPanel vp = new VerticalPanel();
		vp.setWidth("100%");
		vp.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
		
		customVP = new VerticalPanel();
		customVP.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
		vp.add(customVP);
		
		// Initiate table grid for widgets
		table = new FlexTable();
//		table.setWidth("100%");
		table.getElement().setAttribute("width", "100%");
//		table.resizeColumns(2);
		
		vp.add(table);
		
		popup.addContent(vp);

		

		// Initiate OK button which performs the check procedure

		
		popup.addButton("OK" ,new Action() {
			public void doAction() {

				// Set the number of answers, which should be received from UI element's checkers
				numberOfAnswersRequired = uiElements.size();

				// For each UI element
				for (Iterator<UIElementWithCheck> iterator = uiElements
						.iterator(); iterator.hasNext();) {
					UIElementWithCheck element = iterator.next();

					// Perform check, which is required. If element is
					// obligatory-to-fill-in,
					// it checks whether it has a value or not in addition to
					// the check defined for element
					element.doRequiredCheck();

				}
			}

		});
		
		// Initiate Cancel button
		popup.addCloseButton("Cancel");
	}

	public void showPopup() {
		popup.showPopup();
	}
	public void closePopup() {
		popup.hide();
	}
	/**
	 * Adds a widget to a vertical panel, which is situated on the top of the form.
	 * @param widget
	 */
	public void addWidgetOnTheTop(Widget widget) {
		customVP.add(widget);
	}
	
	
	public void addUIElement(String elementName, final String elementDescr,
			UIElementWithCheck element) {
		// Logical addition
		uiElements.add(element);
		
		// Insert a row to the grid
		int currentRow = table.getRowCount();
		table.insertRow(currentRow);
		table.insertRow(currentRow+1);
		
		// Table row style
//		table.getCellFormatter().setWidth(currentRow, 2, "10px");
		table.getCellFormatter().setWordWrap(currentRow, 1, true);
//		table.getCellFormatter().setStyleName(currentRow, 1, "design-properties-table-text-area-wrapper");
		DOM.setStyleAttribute(table.getCellFormatter().getElement(currentRow, 1), "width",(100- LEFT_COLUMN_WIDTH) + "%");
		
		
		// Title and description
		VerticalPanel vpTD = new VerticalPanel();
		vpTD.setWidth("100%");
		
		Label title = new Label(elementName);
		title.setStyleName("design-properties-table-title");
		
		vpTD.add(title);
		
		if(elementDescr != null) {
			Label description = new Label (elementDescr);
			description.setWordWrap(true);
			description.setStyleName("design-properties-table-text-area-description");
		
			vpTD.add(description);
		}
//		// Set style to the element
//		if (element instanceof TextBoxBaseWrapper) {
//			TextBoxBase tb = (TextBoxBase)element.getElement();
////			tb.setWidth("100%");
//			DOM.setStyleAttribute(tb.getElement(), "margin", "0px");
//		}

//		if (element.getElement() instanceof TextBoxBase) {
//			addTextBox(elementName, elementDescr, element);
//		}
//		else if (element.getElement() instanceof CheckBox) {
//			addCheckBox(elementName, elementDescr, element);
//		}
		
		Label errorLabel = new Label("");
		errorLabel.setWordWrap(true);
		
		DOM.setStyleAttribute(errorLabel.getElement(), "backgroundColor", "white");
		DOM.setStyleAttribute(errorLabel.getElement(), "color", "red");
		DOM.setStyleAttribute(errorLabel.getElement(), "fontSize", "70%");
		
		element.setErrorLabel(errorLabel);
		
		table.setWidget(currentRow, 0, vpTD);
		
		Widget uiElement = element.getElement();
		uiElement.setStyleName("design-properties-table-text-area");

		table.setWidget(currentRow, 1, uiElement);
		table.setWidget(currentRow + 1, 1, errorLabel);
	}
	
	
	public void addCheckBox(String elementName, final String elementDescr,
			UIElementWithCheck element) {
		// Insert a row to the grid
		int currentRow = table.getRowCount();
		table.insertRow(currentRow);
		table.insertRow(currentRow+1);
		
		// Table row style
//		table.getCellFormatter().setWidth(currentRow, 2, "10px");
		table.getCellFormatter().setWordWrap(currentRow, 1, true);
		table.getCellFormatter().setStyleName(currentRow, 1, "design-properties-table-text-area-wrapper");

		
		// Title and description
		VerticalPanel vpTD = new VerticalPanel();
		vpTD.setWidth("100%");
		
		Label title = new Label(elementName);
		title.setStyleName("design-properties-table-title");
		
		vpTD.add(title);
		
		Label description = new Label (elementDescr);
		description.setWordWrap(true);
		description.setStyleName("design-properties-table-text-area-description");

		vpTD.add(description);

		// Set style to the element
		if (element instanceof TextBoxBaseWrapper) {
			TextBoxBase tb = (TextBoxBase)element.getElement();
//			tb.setWidth("100%");
			DOM.setStyleAttribute(tb.getElement(), "margin", "0px");
		}

		Label errorLabel = new Label("");
		errorLabel.setWordWrap(true);
		
		DOM.setStyleAttribute(errorLabel.getElement(), "backgroundColor", "white");
		DOM.setStyleAttribute(errorLabel.getElement(), "color", "red");
		DOM.setStyleAttribute(errorLabel.getElement(), "fontSize", "70%");
		
		element.setErrorLabel(errorLabel);
		
		table.setWidget(currentRow, 0, vpTD);
		
		Widget uiElement = element.getElement();
		uiElement.setStyleName("design-properties-table-text-area");

		table.setWidget(currentRow, 1, uiElement);
		table.setWidget(currentRow + 1, 1, errorLabel);
		
		
	}
	
	public String getTBValue(int id) {
		return ((TextBoxBase)uiElements.get(id).getElement()).getText();
	}

	public Boolean getCBValue(int id) {
		return ((CheckBox)uiElements.get(id).getElement()).getValue();
	}
	
	
	public String getRBGValue(int id) {
		return ((RadioButtonGroup)uiElements.get(id).getElement()).getSelectedButtonText();
	}
	
	/**
	 * Processes the result which comes from one of the UI elements' check actions.
	 * Is fired by the UI element checker when the check procedure is finished. 
	 * If element is filled correctly, it is fired with true value, else false.
	 * The purpose of this function is to listen for all element's checkers. It shows error message if 
	 * something is  filled incorrectly or perform action on success when all answers are received and all of them are positive.
	 * @param result Result of element's check: true if it is filled appropriate, else false
	 */
	public void processResult(boolean result) {
		// If element is filled correctly
		if (result == true) {
			// Reduce number of answers which are still needed 
			numberOfAnswersRequired--;
			// If it is the last answer
			if (numberOfAnswersRequired == 0) {
				// Perform action on success
				onSuccessfullyFilled();
			}
		}
		else {
			// Element is filled incorrectly
			// If it is the first one
			if (numberOfAnswersRequired != -1) {
				// Stop counting 
				numberOfAnswersRequired = -1;
//				// Show popup informing that the form is filled incorrectly
//				new Popup("Form filed incorrectly! Check UI elements marked by red color."); 
			}
		}
	}

	/**
	 * Is called when a listener gets answers from all UI element's checkers and 
	 * all of them are positive. Should contain an action to be performed when OK button is clicked
	 * and all fields are checked.
	 * Needs to be overwritten in the child form class. 
	 */
	public void onSuccessfullyFilled() {}
}