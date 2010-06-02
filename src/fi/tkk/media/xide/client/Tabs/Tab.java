package fi.tkk.media.xide.client.Tabs;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Tab extends FlowPanel {
	protected Tab(){
		super();
	}
	
	public void onTabOpened() {}
	public void onTabClosed() {}
	private String tabName;
	
	public final int tabID = 0;
	
	public String getTabName() {
		return tabName;
	}
	
	Tab (String name){
		tabName = name;
	}
	
	public int getID() {
		return tabID;
	}
	
	public void Disable(){
		
	}
	public void UpdateUI(){};
}
