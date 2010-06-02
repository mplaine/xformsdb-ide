package fi.tkk.media.xide.client.Tabs;

import java.util.ArrayList;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.Data.AccessRights;
import fi.tkk.media.xide.client.Data.User;
import fi.tkk.media.xide.client.UI.Widget.AccessRightsTable;
import fi.tkk.media.xide.client.UI.Widget.BasicPageElement;
import fi.tkk.media.xide.client.UI.Widget.TemplateShortInfoPanel;

public class AccessRightsForElementTab extends BottomBasicTab {
	
	AccessRightsTableForElement table;

	
	class AccessRightsTableForElement extends AccessRightsTable{
		ArrayList<String> roles;
		
		public static final String SET_RIGHT_AND_PROPAGATE 		= "Set right and propagate it to the children";
		public static final String SET_RIGHT_DO_NOT_PROPAGATE 	= "Set right and do not propagate it to the children";
		public static final String REMOVE_RIGHT					= "Remove right";
		
		public AccessRightsTableForElement(ArrayList<String> roles, ArrayList<Integer> accessRightsSettings, boolean isEditable ) {
			super();
			this.roles = roles;
			
			setListBoxSuggestions();
						
			int[][] info = new int[roles.size()][1];
			for (int i = 0; i < roles.size(); i++) {
						info[i][0] = accessRightsSettings.get(i);
			}
			ArrayList<String> arr = new ArrayList<String>();
			arr.add("Current Element");
			Init(arr, roles, info, isEditable);
			
		}
		
		public AccessRightsTableForElement(ArrayList<String> roles) {
			super();
			this.roles = roles;

			setListBoxSuggestions();
			
			ArrayList<String> arr = new ArrayList<String>();
			arr.add("Current Element");
			Init(arr, roles);
			
		}
		
		public void setListBoxSuggestions() {
			ArrayList<String> listBoxSuggestionsPositive; 
			ArrayList<String> listBoxSuggestionsNegative;

			listBoxSuggestionsPositive = new ArrayList<String>();
			listBoxSuggestionsPositive.add(SET_RIGHT_AND_PROPAGATE);
			listBoxSuggestionsPositive.add(SET_RIGHT_DO_NOT_PROPAGATE);
			
			listBoxSuggestionsNegative = new ArrayList<String>();
			listBoxSuggestionsNegative.add(REMOVE_RIGHT);
			
			setListBoxSuggestions(listBoxSuggestionsPositive, listBoxSuggestionsNegative);
		}
		
		public void fillTable(ArrayList<Integer> accessRightsSettings, boolean isEditable) {
			int[][] info = new int[roles.size()][1];
			for (int i = 0; i < roles.size(); i++) {
				info[i][0] = accessRightsSettings.get(i);
			}
			fillTable(info, isEditable);
		}
		
		
		public void onCheckBoxClicked(String suggestionSelected, int row, int column) {
			Main.getInstance().setSelectedElementChanged();
			
			
			if (Main.getInstance().getSelectedElement() instanceof BasicPageElement) {
				BasicPageElement element = (BasicPageElement)Main.getInstance().getSelectedElement() ;
				if (suggestionSelected.equals(SET_RIGHT_AND_PROPAGATE)) {
					element.setRightToRole(row, AccessRights.RIGHT_GRANTED, true);
				}
				else if (suggestionSelected.equals(SET_RIGHT_DO_NOT_PROPAGATE)) {
					element.setRightToRole(row, AccessRights.RIGHT_GRANTED, false);
				}
				else if (suggestionSelected.equals(REMOVE_RIGHT)){
					element.setRightToRole(row, AccessRights.RIGHT_NOT_GRANTED, true);
//					element.setRightToRole(column, AccessRights.RIGHT_DISABLED);
				}
			}
			// TODO: update only after first checkbox touched.
			Main.getInstance().UpdateUI(Main.BOTTOM_TAB);

		}
	}
	
	public AccessRightsForElementTab() {
//	public AccessRightsForElementTab(String[] rs, ArrayList<String> el) {

//		String[][] fakeGroups = {{"PublicGroup", "1"}, {"PrivateGroup", "0"}};
//		String[] fakeElement = {"SelectedElement"};
//		
////		User[] users = new User[2];
////		users[0] = new User("id_1", "john", "ddd");
////		users[1] = new User("id_3", "jane", "ddd");
////		
//		String[] roles = new String[2];
//		roles[0] = "Main person";
//		roles[1] = "Looser";
////		users[0].addRole(roles[1]);
////		users[1].addRole(roles[0]);
//		ArrayList<String> elementRoles = new ArrayList<String>();
//		elementRoles.add(roles[0]);
////		AccessRightsTable table = new AccessRightsTable(fakeElement, fakeGroups) ;
		
		table = new AccessRightsTableForElement(
				Main.getInstance().getAccessRights().getRoles());
//		table.a
		this.add(table);
	}
	
	public void UpdateUI() {

		super.UpdateUI();
		table.Clear();
		if (Main.getInstance().getSelectedElement() instanceof BasicPageElement) {
			BasicPageElement element = (BasicPageElement) Main.getInstance().getSelectedElement();
			if (element.getAccessRightsSettings() != null) {
				
				if (element instanceof TemplateShortInfoPanel) {
					table.fillTable(element.getAccessRightsSettings(), false);
				}
				else {
					table.fillTable(element.getAccessRightsSettings(), true);
				}
			
			}
		}

	}

	@Override
	public void updateWhenClicked() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateWhenKeyUp() {
		// TODO Auto-generated method stub
		
	}
}
