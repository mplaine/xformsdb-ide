/**
 * 
 */
package fi.tkk.media.xide.client.Tabs;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.View;
import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.Data.Selectable;
import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.UI.Widget.BasicPageElement;
import fi.tkk.media.xide.client.UI.Widget.TemplateShortInfoPanel;
import fi.tkk.media.xide.client.fs.XIDEFile;
import fi.tkk.media.xide.client.popups.utils.interfaces.Action;
import fi.tkk.media.xide.client.utils.ConnectionToServer;
import fi.tkk.media.xide.client.utils.Cons;
import fi.tkk.media.xide.client.utils.ConnectionToServer.CallbackActions;

/**
 * Base class for a tab, which displays a file content.
 * 
 * Has a list box with files available and a text area, which shows selected file text. Text can be highlighted according to the type of the file. 
 * 
 * Maintains:
 * - adding/removing files to the tab depending on the element selected
 * - showing selected file text
 * - highlighting the text 
 * 
 * @author evgeniasamochadina
 *
 */
public abstract class FileTab extends BottomBasicTab {
	/**
	 * ID of the tab type which is used in UI updating to update only tabs of specific type. See {@link View} constants for details. 
	 * Since this is an abstract tab, it is set to 0. 
	 */
	public final int tabID = 0;

	// Counts file tabs created to generate unique id of the tab.
	// This id will be used by highlighting JavaScript to refer to the corresponding text area object
	private static int id = 0;

	
	//Base string of the unique id of text area html object in {@link #FileTab}. 
	private static final String FILE_TAB_ID_BASE = "code";

	// String to display in the file list box when there is no file available
	private static final String No_FILE_TEXT = "No file of this type";

	// Private id of this file tab's text area
	// Is used to be able to get this tab's text area placed on the window
	protected String idTab;

	// Represents list of files available
	protected ListBox listBoxFileNames;
	// Represents selected file content
	protected TextArea textAreaOfFileText;

	// Contains list of files, which is now available on this tab
	protected LinkedHashMap<String, XIDEFile> files;

	// Indicates whether this tab has already been opened
	// Is used to display general UI only at the first time when tab is opened
	public boolean hasAlreadyOpened;

	// Indicates whether this tab is opened now
	public boolean isOpened;

	// Indicate whether the highlight script has already run
	// Is used to define whether script should be run when the tab is opened
	public boolean hasAlreadyRunScript;
	
	// Indicates whether some file is displayed now
	public boolean isSmthDisplayed;
	
	/**
	 * Indicates whether new text was just set externally to the text area with highlight and onChenge event is not yet processed.
	 * 
	 * Is a workaround for the following bug:
	 * When new element is selected the UI update starts. If some file tab was opened and its text area was initialized,
	 * it will send onChange event when new text will be set. This event cause setting element to changed status. However element is not
	 * changed. 
	 * 
	 * Is set to true when new text is set to text are externally, and set back to false when onChange event caused by this setting is stopped.
	 * 
	 * Please note EACH CHILD CLASS of the {@link FileTab} should have its own {@link newTextHasJustSet} defined 
	 * and it should be correctly set to false when onChange event comes. 
	 */
	public boolean newTextHasJustSet = false;

	
	/**
	 * Runs script to perform a auto highlight. It has a onChange optional parameter, which contains a function to call when
	 * the text has changed.
	 * 
	 * Each file tab should overwrite this method in order to have its own script settings and script variable,
	 * as well as {@link #setNewText}, {@link #onTextChanged} and {@link #initizlizeJSLink}.
	 * 
	 * See any of child classes for example. 
	 * 
	 * OLD COMMENTS 
	 * This function works ok if it contains simple javascript: onChange: function (n) {
	 * alert(editor.getCode()); } But it doent't work in case on JSNI call inside.
	 * 
	 * For the hosted mode (for windows) the following combination works (onTextChanged is a native method): function
	 * bl(){this.@fi.tkk.media.xide.client.Tabs.FileTab::onTextChanged()();} ... onChange: function al(){bl();},
	 * 
	 * But for web mode it failed with "this.CF is not a function" error
	 * 
	 * It seems that the call to the native Java method is not processed correctly into the Javascript and the
	 * codemirror object doesn't see it.
	 * 
	 * The only option, which's not tried yet, is a manual call of the function, defined on the page. This function
	 * should contain the call of the Java method. It should be called manually everywhere instead of onChange call.
	 */
	
	public native void runScript() /*-{
	}-*/;

	/**
	 * Sets new text to the highlighted text area. 
	 * 
	 * Should be redefined in child class together with {@link #runScript}, {@link #onTextChanged} and {@link #initizlizeJSLink}
	 * @param text
	 */
	public native void setNewText(String text)/*-{
	}-*/;

	/**
	 * Gets the text from highlighted text area. 
	 * 
	 * Should be redefined in child class together with {@link #runScript}, {@link #setNewText} and {@link #initizlizeJSLink}
	 * 
	 * @param text the text area's text
	 */
	public native String getChangedText()/*-{
	}-*/;
	
	/**
	 * Initializes JavaScript variable to be used as a link to the method, which is called when onVhange event comes from text area.
	 * The name of the variable should be specific for each tab since each tab has a unique script instance 
	 * 
	 * Should be redefined in child class together with {@link #runScript}, {@link #setNewText} and {@link #onTextChanged}
	 * @param text
	 */
	public static native void initizlizeJSLink() /*-{
	}-*/;
	
	/**
	 * Gets unique tab id, initializes all flags;
	 */
	public FileTab() {
		super();

		// Calculate unique id of the tab
		idTab = FILE_TAB_ID_BASE + id;
		
		// Increase counter for the next tab creation 
		id++;

		// Initialize the flags
		
		// The tab has never been opened 
		hasAlreadyOpened = false;
		// The script has never been run
		hasAlreadyRunScript = false;
		// The tab is not opened now
		isOpened = false;
		// No file is displayed now
		isSmthDisplayed = false;

	}

	/**
	 * Initialize UI of the file tab (adds list box for file name and text area for file text)
	 */
	public void initUI() {

		// Take all available space
		this.setSize("100%", "100%");

		// Draw everything

		// Label and list box for file name selection
		AbsolutePanel panelTopWrapper = new AbsolutePanel();
		panelTopWrapper.setWidth("100%");
		if (!Main.IS_RUNNING_ON_WINDOWS) {
			DOM.setStyleAttribute(panelTopWrapper.getElement(), "borderBottom", "3px solid grey");
		}
		
		
		HorizontalPanel panelListBox = new HorizontalPanel();
		
		
		panelListBox.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		panelTopWrapper.add(panelListBox);
//		this.add(panelListBox);

		// Label with welcome string
		final Label labelSelectFileName = new Label("Select the file name: ");
		DOM.setStyleAttribute(labelSelectFileName.getElement(), "paddingLeft", "5px");
		DOM.setStyleAttribute(labelSelectFileName.getElement(), "paddingRight", "5px");
		
		panelListBox.add(labelSelectFileName);

		// List box which contains list of files
		listBoxFileNames = new ListBox();
		listBoxFileNames.setVisibleItemCount(1);
		listBoxFileNames.addChangeHandler(new ChangeHandler() {

			public void onChange(ChangeEvent event) {
				String diName = ((ListBox) event.getSource()).getItemText(((ListBox) event.getSource()).getSelectedIndex());
				// Set DI text of the selected DI to the text area
				// files.get(diName).setLinkedUIObject(textAreaOfFileText);
				files.get(diName).getContent(new Action() {

					public void doAction() {
						updateTextArea();
					}

				});
			}
		});
		setListBoxNoFiles();
		panelListBox.add(listBoxFileNames);

		// Text area with file text
		textAreaOfFileText = new TextArea();
		
		DOM.setStyleAttribute(textAreaOfFileText.getElement(), "backgroundColor", "#E5F5DC");
		// Link text area with the unique id
		DOM.setElementAttribute(textAreaOfFileText.getElement(), "id", idTab);

		textAreaOfFileText.addClickListener(this);
		textAreaOfFileText.addKeyboardListener(this);
		textAreaOfFileText.setText("");

		// Absolute panel is added in order to remove the bug following bug:
		// When Text Area has 100% size (without a panel) it is reduced to 2 lines
		// after first mouse click. No event is sent.
		AbsolutePanel panel = new AbsolutePanel() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see com.google.gwt.user.client.ui.Widget#onBrowserEvent(com.google.gwt.user.client.Event)
			 */
			@Override
			public void onBrowserEvent(Event event) {
				// TODO Auto-generated method stub
				// System.out.println("on event");
				super.onBrowserEvent(event);
			}
		};
		panel.sinkEvents(Event.KEYEVENTS);

		panel.setSize("100%", "100%");
		AbsolutePanel panel1 = new AbsolutePanel() ;
		panel1.setSize("100%", "100%");
		panel1.add(panelTopWrapper);
		panel1.add(panel);
		this.add(panel1);

		textAreaOfFileText.setSize("100%", "100%");
		textAreaOfFileText.setCharacterWidth(60);

		panel.add(textAreaOfFileText);
	}

	/**
	 * Clear list box with file names and text area
	 */
	public void clearFileNamesAndText() {
		// if has smth 
		if (!isSmthDisplayed) {
			// do nothing
		}
		else {
			//needs to clear everythng
			setListBoxNoFiles();
			updateTextArea("");
			files = null;
			isSmthDisplayed = false;
		}
		
//		// If there are some entries displayed
//		if (listBoxFileNames.getItemCount() != 0) {
//			// Clear list box
//			listBoxFileNames.clear();
//
//			if (files != null) {
//				updateTextArea("");
//
//				// Clear DI set
//				files = null;
//			}
//
//		}
	}

	public void addFiles(LinkedHashMap<String, XIDEFile> files) {
		this.files = files;
	}

	public void setListBoxNoFiles() {
		listBoxFileNames.clear();
		listBoxFileNames.setEnabled(false);
		listBoxFileNames.addItem(No_FILE_TEXT);
	}
	/**
	 * Displays files on the tab: file names in the list box, files in the text area
	 */
	public void displayFiles() {
		if (files != null && files.size() > 0) {
			if (!isSmthDisplayed) {
				isSmthDisplayed = true;
			
			}
			listBoxFileNames.clear();
			listBoxFileNames.setEnabled(true);
			// Logical addition

			for (Iterator<String> iterator = files.keySet().iterator(); iterator.hasNext();) {
				String o = iterator.next();
				// Add DI name to the list
				listBoxFileNames.addItem(o);
			}

			// No item is selected beforehand
			listBoxFileNames.setSelectedIndex(0);
			String diName = listBoxFileNames.getItemText(0);
			// Set DI text of the selected DI to the text area
			// files.get(diName).setLinkedUIObject(textAreaOfFileText);

			files.get(diName).getContent(new Action() {
				public void doAction() {
					updateTextArea();
				}

			});

		} else {
			clearFileNamesAndText();
		}

	}

	public void UpdateUI() {

		if (isOpened) {

			// Update UI

			Selectable selectedElement = Main.getInstance().getSelectedElement();
			if (selectedElement != null) {
				// If there is a selected element

				// Get new files from newly selected element
				getFiles();

				// Display those files
				displayFiles();

				// Hardcoded stuff
				// Disable template editing
				if (selectedElement instanceof TemplateShortInfoPanel) {
					textAreaOfFileText.setEnabled(true);
				} else {
					textAreaOfFileText.setEnabled(true);
				}
			}
			else {
				// Clear previous files if any
				clearFileNamesAndText();
			}
		} else {
			// Get new file list from the element
			getFiles();
		}
	}

//	"Design view fails when select an element too fast" bug finally fixed
//	File tab switch testing
	/**
	 * Runs when the tab is opened
	 */
	public void onTabOpened() {
		// Set the flag
		isOpened = true;
		
		
		if (!hasAlreadyOpened) {
			// If this tab has been never opened
			hasAlreadyOpened = true;
			
			// Draw default UI
			initUI();

			// Get files and display them
			UpdateUI();
			
		

		} else {
			// Tab has opened for the second (or more) time
			UpdateUI();
		}

	}

	/**
	 * Runs when the tab is closed
	 */
	public void onTabClosed() {
		// set the flag
		isOpened = false;
	}

	/**
	 * Gets files to display form selected element
	 * 
	 * @return
	 */
	public abstract void getFiles();

	/**
	 * Updates text area with the file content. This method runs when the content is already loaded.
	 */
	private void updateTextArea() {
		String diName = listBoxFileNames.getItemText(listBoxFileNames.getSelectedIndex());
		String text = files.get(diName).getContent();

		updateTextArea(text);
	}

	/**
	 * Updates text area with the given text. Updates text area or text editor depending on whether the script has
	 * already run.
	 */
	private void updateTextArea(String text) {
		
		// If script has already been run, then setting new value will cause onChanged event 
		if (hasAlreadyRunScript) {
			// So set flag to true to process this event correctly
			newTextHasJustSet = true;
		}
		
		if (!hasAlreadyRunScript) {
			textAreaOfFileText.setText(text);
		} else {
			setNewText(text);
		}

		// If it is the first time when the text is set and highlight is required
		if (Cons.IS_TEXT_HIGHLIGHT_USED && !hasAlreadyRunScript) {
			// Run highlight script
			runScript();
			/**
			 *  Script should be run after setting a value to the text area (or using {@link #setNewText()})
			 *  Otherwise an error occurs since the editor initialization can be not finished when setNewText is called 
			 */
			hasAlreadyRunScript = true;
			
		}
	}

	@Override
	public void updateWhenKeyUp() {
		String diValue = null;
		if (Cons.IS_TEXT_HIGHLIGHT_USED) {
			diValue = getChangedText();
		} else {
			
			diValue = textAreaOfFileText.getText();
		}

		if (diValue != null) {
			updateFileWithNewValue(diValue);
		}
	}

	public void updateFileWithNewValue(String diValue) {
		String diName = (listBoxFileNames.getItemText(listBoxFileNames.getSelectedIndex()));

		XIDEFile diFile = files.get(diName);
		diFile.updateContent(diValue);
	}

}
