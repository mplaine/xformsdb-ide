package fi.tkk.media.xide.client.utils;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Special dock panel which has header and footer strictly docked 
 * to the top and bottom respectively. Header and footer occupy their own size while 
 * central main object occupies remaining volume
 * @author evgeniasamochadina
 *
 */
public class PagePanel extends DockPanel {
	Widget header;
	Widget footer;
	Widget main;
//	Panel parentPanel;

	public PagePanel(Widget header, Widget footer, Widget main, String width, String height) {
		super();
		
		this.header = header;
		this.footer = footer;
		this.main = main;
//		this.parentPanel = parentPanel;
		
		// Set user required size
		setSize(width, height);
		
		// Set widgets into corresponding places 
		
		//Main widget should occupy remaining volume
		this.main.setSize("100%", "100%");
		
		// Add widgets according to their position
		if(header != null) {
			add(header, NORTH);
		}
		if (main != null) {
			add(main, CENTER);
		}
		if (footer != null) {
			add(footer, SOUTH);
		}
	}	
	
	public PagePanel(Widget header, Widget footer, Widget main) {
		this(header, footer, main, "", "100%");
	}
	
	public PagePanel(String width, String height) {
		setSize(width, height);
	}
	
	public PagePanel() {
		setSize("100%", "100%");
	}
	
	public void addHeader(Widget header) {
		this.header = header;
		add(header, NORTH);
	}
	
	public void addFooter(Widget footer) {
		this.footer = footer;
		add(footer, SOUTH);
	}
	public void addMain(Widget main) {
		this.main = main;
//		this.main.setSize("100%", "100%");
		add(main, CENTER);
	}
	
	public void setWidgetAndSize(Panel parentPanel) {
		// Add page to the parent panel
		// After adding header and footer will have their offset heights values setted
		
		parentPanel.add(this);
		
		// Set header and footer size according to their real zise in pixels
		if (header != null) {
			setCellHeight(header, header.getOffsetHeight() + "px");
		}
		if (footer != null) {
			setCellHeight(footer, footer.getOffsetHeight() + "px");
		}
	}
}
