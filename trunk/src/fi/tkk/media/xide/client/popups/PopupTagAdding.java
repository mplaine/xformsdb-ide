package fi.tkk.media.xide.client.popups;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.Data.Tag;
import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.Tabs.PLTreeItem;
import fi.tkk.media.xide.client.popups.basic.Popup;
import fi.tkk.media.xide.client.popups.basic.PopupError;
import fi.tkk.media.xide.client.popups.utils.FormPopup;
import fi.tkk.media.xide.client.popups.utils.LocalFieldCheckers;
import fi.tkk.media.xide.client.popups.utils.RemoteFieldCheckers;
import fi.tkk.media.xide.client.popups.utils.TextBoxBaseWrapper;
import fi.tkk.media.xide.client.popups.utils.interfaces.Action;
import fi.tkk.media.xide.client.popups.utils.interfaces.AsyncEventListener;
import fi.tkk.media.xide.client.utils.ConnectionToServer;
import fi.tkk.media.xide.client.utils.ConnectionToServer.CallbackActions;

public class PopupTagAdding extends FormPopup implements AsyncEventListener{

	// Action to perform when user press OK button
	Action actionOnOK;

	public PopupTagAdding( Action actionOnOK) {
		super("Add new tag", "You are going to create new tag. Tag will appear in the tag list after creation procedure has been completed and " +
				"will be visible to all XIDE users. " +
				"So please check that the tag you want to add (or similar) does not exist. Please fill the following fields and press OK button.");
		
		this.actionOnOK = actionOnOK;
		
		
		TextBoxBase tb;
		TextArea ta;
		TextBoxBaseWrapper wr;
		
		// New tag title
		tb = new TextBox();
		wr = new TextBoxBaseWrapper(tb, true, this, new RemoteFieldCheckers.RemoteTagTitleChecker(tb) , new LocalFieldCheckers.NoSpaceAndSSymbChecker(tb));
		
		addUIElement("New tag's title", "This title wil be displayed in the tag list. " +
				"Should not contain any special symbols (e.g. ', &). E.g. 'self-sufficient'", 
				wr);

		// Application's description
		ta = new TextArea();
		wr =new TextBoxBaseWrapper(ta, true, this);
		addUIElement("New tag's description", "This desctiption will be displayed in the tag list and help users to understand what is this tag for." +
				"Should not contain any special symbols (e.g. ', &). E.g. 'component can work right away after it has " +
				"been added to the page without any additional settings'", wr);
		
	}

	
	public void onSuccessfullyFilled() { 
		// Save the tag
		Tag tag = new Tag(getTBValue(CNT_TITLE), getTBValue(CNT_DESCR));
		
		SaveTag(tag);
	}
	/**
	 * Check whether there is already a tag with the given name
	 */
	public void SaveTag(final Tag tag) {
		ConnectionToServer.makeACall(new CallbackActions() {

			public void execute(AsyncCallback callback) {
				ConnectionToServer.searchService.saveNewTag(tag, callback);
			}

			public void onFailure(Throwable caught) {
				new PopupError("Unfortunately tag was not added!",  caught.getMessage());
			}

			public void onSuccess(Object result) {
//					PopupTagAdding.this.closePopup();
//					new Popup("Tag is sucessfully added!");
					actionOnOK.doAction();
			}});
	}
	
}
