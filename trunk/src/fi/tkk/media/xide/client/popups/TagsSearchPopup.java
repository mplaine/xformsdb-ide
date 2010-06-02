package fi.tkk.media.xide.client.popups;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.UI.Widget.StyledButton;
import fi.tkk.media.xide.client.UI.Widget.TagsSearchPanel;
import fi.tkk.media.xide.client.popups.utils.PopupBase;
import fi.tkk.media.xide.client.popups.utils.interfaces.Action;

public class TagsSearchPopup extends PopupBase {
	
	public TagsSearchPopup(final ArrayList<String> tagsList, final Action action, final Action onCloseAction) {
		super("Tag search");
		final TagsSearchPanel panel = new TagsSearchPanel(tagsList, action); 
		popup.addContent(panel);
		popup.addButton("Add new tag", new ClickHandler() {

			public void onClick(ClickEvent event) {
				new PopupTagAdding(new Action() {

					public void doAction() {
						new TagsSearchPopup(tagsList, action, onCloseAction);
					}});
			}
			
		}, StyledButton.STYLE_BLUE);				
			
		popup.addCloseButton("Close", onCloseAction
//				new Action() {
//
//			public void doAction() {
//				Main.getInstance().UpdateUI(Main.BOTTOM_TAB);
//				
//			}}
		);
		popup.showPopup();
	}
}
