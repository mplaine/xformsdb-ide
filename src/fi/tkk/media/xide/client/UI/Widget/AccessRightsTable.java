package fi.tkk.media.xide.client.UI.Widget;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Data.AccessRights;
import fi.tkk.media.xide.client.utils.GoodHorizontalAdjustablePanel;

public class AccessRightsTable extends Grid{
//	public class AccessRightsTable extends Grid implements ClickListener, TableListener{
	 ArrayList<String> listBoxSuggestionsPositive; 
	 ArrayList<String> listBoxSuggestionsNegative;
	
	public AccessRightsTable() {
		super();
		setStyleName("design-AccessRightsRable");
	}

	public AccessRightsTable(ArrayList<String> headers, ArrayList<String> leftHeaders, int[][] info, boolean isEditable) {
		this();
		Init(headers, leftHeaders, info, isEditable);
	}
	
	public AccessRightsTable(ArrayList<String> headers, ArrayList<String> leftHeaders) {
		this();
		Init(headers, leftHeaders);
	}
	
	public void setListBoxSuggestions(ArrayList<String> listBoxSuggestionsPositive, ArrayList<String> listBoxSuggestionsNegative) {
		this.listBoxSuggestionsNegative = listBoxSuggestionsNegative;
		this.listBoxSuggestionsPositive = listBoxSuggestionsPositive;
	}
	
	public void addHeaderButton(Button button) {
		resizeColumns(getColumnCount() + 1);
		int lastColumn = getColumnCount() - 1;
//		for(int i = 0; i < this.getRowCount(); i++) {
//			getCellFormatter().setStyleName(i, lastColumn, "");
//		}
		setWidget(0, lastColumn, button);
	}
	
	public void addLeftHeaderButton(Button button) {
		resizeRows(getRowCount() + 1);
		int lastRow = getRowCount() - 1;
//		for(int i = 0; i < this.getColumnCount(); i++) {
//			getCellFormatter().setStyleName( lastRow, i, "");
//		}
		setWidget(lastRow, 0, button);
	}
	
	public void Init(ArrayList<String> headers, ArrayList<String> leftHeaders, int[][] info, boolean isEditable) {
		// Horizontal: length of headers + 1 for left vertical title column
		// Vertical: height of info matrix + 1 for title row
//		resize(info.length + 1, headers.size() + 1);
//		this.addTableListener(this);
		
		fillHeaders(headers, leftHeaders);
		fillTable(info, isEditable);
		
	}
	
	public void Init(ArrayList<String>headers, ArrayList<String> leftHeaders) {
		// Horizontal: length of headers + 1 for left vertical title column
		// Vertical: height of info matrix + 1 for title row
//		resize(leftHeaders.size() + 1, headers.size() + 1);
//		this.addTableListener(this);
		fillHeaders(headers, leftHeaders);
		
	}
	
	public void Clear() {
//		this.clear();
//		while (this.getRowCount() > 0) {
//			this.removeRow(0);
//		}
//		this.resize(0, 0);
	}
	
	public void fillHeaders(ArrayList<String> headers, ArrayList<String> leftHeaders) {
		resize(leftHeaders.size() + 1, headers.size() + 1);
		// Header
		// Starting from 1 (0 row reserved for left vertical title column)
		for (int i = 0; i < headers.size(); i++) {
			this.setHTML(0, i+1, headers.get(i));
			this.getCellFormatter().setStyleName(0, i+1, "design-AccessRightsTable-header");
		}
		
		for (int j = 0; j < leftHeaders.size(); j++) {
			// Left vertical title
				this.getCellFormatter().setStyleName(j+1, 0, "design-AccessRightsTable-header");
				this.setHTML(j+1, 0, leftHeaders.get(j));
		}
	}
	
	
	
//	public void fillTable(int[][] info) {
		public void fillTable(int[][] info, boolean isEditable) {
//		// Header
//		// Starting from 1 (0 row reserved for left vertical title column)
//		for (int i = 0; i < headers.length; i++) {
//			this.setHTML(0, i+1, headers[i]);
//			this.getCellFormatter().setStyleName(0, i+1, "design-AccessRightsTable-header");
//		}
		
		// Rows
		for (int i = 0; i < info[0].length; i++) {
			for (int j = 0; j < info.length; j++) {
				final int iK = i;
				final int jK = j;
//				// Left vertical title
//				if (i==0) {
//					this.getCellFormatter().setStyleName(j+1, i, "design-AccessRightsTable-header");
//					this.setHTML(j+1, i, info[j][i]);
//				}
//				// Table value
//				else {
//					final CheckBox checkbox = new CheckBox();
				final Image grantedRightsImage = new Image("TagIcons/drop-yes.gif");
				final Image nonGrantedRightsImage = new Image("TagIcons/drop-no.gif");
				
				
//					checkbox.addClickListener(this);
					ListBox lb = new ListBox();
					
					// Adding suggestions
					for (Iterator<String> iterator = listBoxSuggestionsNegative.iterator(); iterator.hasNext();) {
						lb.addItem(iterator.next());
					}
					for (Iterator<String> iterator = listBoxSuggestionsPositive.iterator(); iterator.hasNext();) {
						lb.addItem(iterator.next());
					}
					
					final GoodHorizontalAdjustablePanel hp;
					// Has permission
					if (info[j][i]== AccessRights.RIGHT_GRANTED) {
						hp = new GoodHorizontalAdjustablePanel(grantedRightsImage, lb);
//						checkbox.setChecked(true);
					}
					// Does not have permission
					else {
						hp = new GoodHorizontalAdjustablePanel(nonGrantedRightsImage, lb);
//						checkbox.setChecked(false);
						if(info[j][i]== AccessRights.RIGHT_DISABLED) {
							lb.setEnabled(false);
						}
						

					}
//					checkbox.setEnabled(false);
//					this.getCellFormatter().setStyleName(j+1, i, "design-AccessRightsTable-cell");
					
					lb.setSelectedIndex(-1);
					lb.addChangeListener(new ChangeListener() {

						public void onChange(Widget sender) {
							if (sender instanceof ListBox) {
								// Get currently newly query
								String suggestionName = ((ListBox)sender).getItemText(((ListBox)sender).getSelectedIndex());
								if (listBoxSuggestionsPositive.contains(suggestionName)) {
									hp.setLeftWidget(grantedRightsImage);
//									checkbox.setChecked(true);
									onCheckBoxClicked(suggestionName, jK, iK);
								}
								else if (listBoxSuggestionsNegative.contains(suggestionName)){
									hp.setLeftWidget(nonGrantedRightsImage);
//									checkbox.setChecked(false);
									onCheckBoxClicked(suggestionName, jK, iK);
								}
								((ListBox)sender).setSelectedIndex(-1);
							}
							
//							System.out.println("changes made in " + jK +", "+ iK);
//							if (( ( (ListBox)sender) .getItemText(((ListBox)sender).getSelectedIndex())).equals("Check")) {
//								hp.setLeftWidget(grantedRightsImage);
////								checkbox.setChecked(true);
//								onCheckBoxClicked(true, jK, iK);
//							}
//							else {
//								hp.setLeftWidget(nonGrantedRightsImage);
////								checkbox.setChecked(false);
//								onCheckBoxClicked(false, jK, iK);
//							}
						}});
					
					this.setWidget(j+1, i+1, hp);
					if (!isEditable) {
						lb.setEnabled(false);
					}
					if ((j/2)*2 == j ) {
						this.getCellFormatter().addStyleName(j+1, i+1, "design-AccessRightsTable-cell-1");
					}
					else {
						this.getCellFormatter().addStyleName(j+1, i+1, "design-AccessRightsTable-cell-2");
					}
//				}
				
			}
		}
		
		
	}

//	public void onClick(Widget sender) {
//		System.out.println("on click");
//		if (( (CheckBox)sender) .isEnabled()) {
////			(	(CheckBox) sender) .setChecked(!((CheckBox)sender).isChecked());
//		}
//		
//	}

	public void onCheckBoxClicked(String suggestionSelected, int row, int column) {
		// DO something			
	}
	
	public void onCellClicked(SourcesTableEvents sender, int row, int cell) {
//		if (((CheckBox)sender).isChecked()) {
//			System.out.println("clicked and checked");
//			}
//		else {
//		if( (row > 0) && (cell > 0)) {
//			if ( ( (CheckBox)this.getWidget(row, cell)) .isEnabled() ){
//				((CheckBox)this.getWidget(row, cell)).setChecked(false);
//				System.out.println("value" + ((CheckBox)this.getWidget(row, cell)).isChecked());
//
////				((CheckBox)this.getWidget(row, cell)).setChecked(!((CheckBox)this.getWidget(row, cell)).isChecked()); 			
//				onCheckBoxClicked(((CheckBox)this.getWidget(row, cell)).isChecked(), row-1, cell-1);
//			}
//			}
//		
//		}
	}
}
