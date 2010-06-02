package fi.tkk.media.xide.client.popups;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.Data.ArrayProperty;
import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.Data.TemplateShortInfo;
import fi.tkk.media.xide.client.Tabs.PLTreeItem;
import fi.tkk.media.xide.client.fs.XIDEFolder;
import fi.tkk.media.xide.client.popups.basic.Popup;
import fi.tkk.media.xide.client.popups.basic.PopupError;
import fi.tkk.media.xide.client.popups.utils.PopupBase;
import fi.tkk.media.xide.client.popups.utils.TextBoxBaseWrapper;
import fi.tkk.media.xide.client.popups.utils.interfaces.AsyncEventListener;
import fi.tkk.media.xide.client.popups.utils.interfaces.UIElementWithCheck;
import fi.tkk.media.xide.client.utils.ConnectionToServer;
import fi.tkk.media.xide.client.utils.ConnectionToServer.CallbackActions;

public class PopupAddNewFolder extends PopupBase implements AsyncEventListener{
	ArrayList<UIElementWithCheck> uiElements;
	public static final int UI_FOLDER_NAME = 0;
	
	int uiElementNumber; 
	
	XIDEFolder parentFolder;
	private ConnectionToServer connectionToServer; 
	
	// Table where properties are displayed
	Grid table ;
	
	public PopupAddNewFolder(XIDEFolder parentFolder) {
		super("Add new folder ",  "You are going to create a folder under " + 
				parentFolder.getAbsolutePath() + ". Please enter the folder name and press OK button");

		this.parentFolder = parentFolder;

		VerticalPanel vp = new VerticalPanel();
		vp.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
		
		table = new Grid();
		vp.add(table);
		
		table.resize(1, 3);
		uiElements = new ArrayList<UIElementWithCheck>();
		
		displayProperty("Folder name", "Folder name", 
				"", Property.TAGS, UI_FOLDER_NAME);
		
		popup.addContent(vp);
		
		

		popup.addButton("OK", new ClickHandler () {
			public void onClick(ClickEvent event) {
				
				uiElementNumber = uiElements.size();
				
				for (Iterator<UIElementWithCheck> iterator = uiElements.iterator(); iterator.hasNext();) {
					UIElementWithCheck element = iterator.next();
					element.doRequiredCheck();
				}
				
			}

		});
		
		popup.addCloseButton("Cancel");

		popup.showPopup();
	}
	
	public void processResult(boolean result) {
		if (result == true) {
			uiElementNumber--;
			if (uiElementNumber == 0) {
				// Process Next button
				XIDEFolder folder = new XIDEFolder(((TextBoxBase)uiElements.get(UI_FOLDER_NAME).getElement()).getText(),
						null, parentFolder);
				createFolder(folder);
			}
		}
		else {
			// result == false
//			(new ModalDimmedPopup("Form filed incorrectly! Check UI elements marked by red color.")).showPopup(true); 
		}
	}

	private void displayProperty( String propertyName, String propertyDescr, String propertyValue, 
			 String propertyCode, int rowNumber) {
		table.getCellFormatter().setWidth(rowNumber, 2, "10px");
		table.getCellFormatter().setWordWrap(rowNumber, 1,true);
		
		Label title = new Label(propertyName);

		TextBoxBase tb = null;
		// TODO: update checking
				tb = new TextBox();
//				tb.setWidth("100%");
				DOM.setStyleAttribute(tb.getElement(), "margin", "0px");
		
		
		// Add ui element
			uiElements.add(new TextBoxBaseWrapper(tb, true, this, null));
		
		tb.setText(propertyValue);
		table.setWidget(rowNumber, 0, title);
		table.setWidget(rowNumber, 1, tb);
		
	}



	private void createFolder(final XIDEFolder folder){
		ConnectionToServer.makeACall(new CallbackActions() {

			public void execute(AsyncCallback callback) {
				connectionToServer.searchService.addNewFolder(folder, callback);
			}

			public void onFailure(Throwable caught) {
				popup.hide();
				new PopupError("Unfortunately application was not created!",  caught.getMessage());
				folder.removeFromParent();
				
			}

			public void onSuccess(Object result) {
				popup.hide();
				new Popup( "Folder has been created");
				Main.getInstance().updateDateOfSelectedElement();
				
				Main.getInstance().UpdateUI(Main.BOTTOM_TAB);
				Main.getInstance().UpdateUI(Main.RIGHT_TAB);
			}});
	}
}
