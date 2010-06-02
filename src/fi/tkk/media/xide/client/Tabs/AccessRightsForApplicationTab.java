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

public class AccessRightsForApplicationTab extends BottomBasicTab {
	
	AccessRightsTableForApplication table;

	class AccessRightsTableForApplication extends AccessRightsTable{
		ArrayList<User> users;
		ArrayList<String> roles;
		
		public static final String ADD_USER_TO_GROUP 			= "Add user to the group";
		public static final String REMOVE_USER_FROM_GROUP 		= "Remove user from the group";

	
		public AccessRightsTableForApplication(ArrayList<String> roles, ArrayList<User> users) {
			super();
			this.users = users;
			this.roles = roles;
			
			setListBoxSuggestions();
			
			ArrayList<String> userNames = new ArrayList<String>();
			for (int i = 0; i < users.size(); i++) {
				userNames.add(users.get(i).getName());
			}
			Init(roles, userNames);
			
		}
		
		public void setListBoxSuggestions() {
			ArrayList<String> listBoxSuggestionsPositive; 
			ArrayList<String> listBoxSuggestionsNegative;

			listBoxSuggestionsPositive = new ArrayList<String>();
			listBoxSuggestionsPositive.add(ADD_USER_TO_GROUP);
			
			listBoxSuggestionsNegative = new ArrayList<String>();
			listBoxSuggestionsNegative.add(REMOVE_USER_FROM_GROUP);
			
			setListBoxSuggestions(listBoxSuggestionsPositive, listBoxSuggestionsNegative);
		}
		
		public void fillTable(ArrayList<User> users) {
			int[][] info = new int[users.size()][roles.size()];
			for (int i = 0; i < users.size(); i++) {
				for (int j = 0; j < roles.size(); j++) {
					if (users.get(i).getRoles().contains(roles.get(j))) {
						info[i][j] = AccessRights.RIGHT_GRANTED;
					}
					else {
						info[i][j] = AccessRights.RIGHT_NOT_GRANTED;
					}
				}
			}
			fillTable(info, true);
		}
		
		public void onCheckBoxClicked(String suggestionSelected, int row, int column) {
			Main.getInstance().setSelectedElementChanged();
			
			// New role is added
				if (suggestionSelected.equals(ADD_USER_TO_GROUP)) {
					users.get(row).addRole(roles.get(column));
				}
				else if (suggestionSelected.equals(REMOVE_USER_FROM_GROUP)) {
					users.get(row).removeRole(roles.get(column));
				}
			// TODO: update only after first checkbox touched.
			Main.getInstance().UpdateUI(Main.BOTTOM_TAB);

		}
	}
	
	Button addNewRoleButton;
	Button addNewUserButton;
	
		public AccessRightsForApplicationTab() {
			
		table = new AccessRightsTableForApplication(
				Main.getInstance().getAccessRights().getRoles(), 
				Main.getInstance().getAccessRights().getUsers());
		 
		
		addNewRoleButton = new Button("Add new Role", new ClickListener() {

			public void onClick(Widget sender) {
				final Grid hp = new Grid();
				hp.resize(2, 2);
				DOM.setStyleAttribute(hp.getElement(), "border", "1px solid gray");
				
				final TextBox tbRoleName = new TextBox();
				hp.setWidget(0, 1, tbRoleName); 
				hp.setHTML(0, 0, "Enter role name");
				
				Button saveButton = new Button("Save role", new ClickListener() {

					public void onClick(Widget sender) {
						Main.getInstance().addNewRole(tbRoleName.getText());
						hp.removeFromParent();
						Main.getInstance().UpdateUI();
					}});
				hp.setWidget(1, 1, saveButton);
				
				AccessRightsForApplicationTab.this.add(hp);

				
			}});
		addNewUserButton = new Button("Add new User", new ClickListener() {


				public void onClick(Widget sender) {
					final Grid hp = new Grid();
					hp.resize(4, 2);
					DOM.setStyleAttribute(hp.getElement(), "border", "1px solid gray");
					
					final TextBox tbUserId = new TextBox();
					hp.setWidget(0, 1, tbUserId); 
					hp.setHTML(0, 0, "Enter user login");
					
					final TextBox tbUserName = new TextBox();
					hp.setWidget(1, 1, tbUserName); 
					hp.setHTML(1, 0, "Enter user name");
					
					final PasswordTextBox tbUserPsw = new PasswordTextBox();
					hp.setWidget(2, 1, tbUserPsw); 
					hp.setHTML(2, 0, "Enter user pswrd");
					Button saveButton = new Button("Save user", new ClickListener() {

						public void onClick(Widget sender) {
							Main.getInstance().getAccessRights().addUser(
									new User(tbUserId.getText(), tbUserName.getText(), tbUserPsw.getText()));
							Main.getInstance().UpdateUI();
							hp.removeFromParent();
						}});
					hp.setWidget(3, 1, saveButton);
					
					AccessRightsForApplicationTab.this.add(hp);
					
			}});
		this.add(table);
		table.addHeaderButton(addNewRoleButton);
		table.addLeftHeaderButton(addNewUserButton);
	}
	
	public void UpdateUI() {

		super.UpdateUI();
		table.Clear();
		
		if ((Main.getInstance().getAccessRights().getUsers().size() != table.getRowCount()-2) || 
				(Main.getInstance().getAccessRights().getRoles().size() != table.getColumnCount()-2)){
			table.removeFromParent();
			table = new AccessRightsTableForApplication(
					Main.getInstance().getAccessRights().getRoles(), 
					Main.getInstance().getAccessRights().getUsers());
			this.add(table);
			table.fillTable(Main.getInstance().getAccessRights().getUsers());
			
			table.addHeaderButton(addNewRoleButton);
			table.addLeftHeaderButton(addNewUserButton);
		}
		else {
			table.fillTable(Main.getInstance().getAccessRights().getUsers());
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
