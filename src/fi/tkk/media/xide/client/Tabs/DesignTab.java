/**
 * 
 */
package fi.tkk.media.xide.client.Tabs;

import java.util.LinkedHashMap;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FocusPanel;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.View;
import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.UI.Widget.Component;
import fi.tkk.media.xide.client.UI.Widget.WebPage;
import fi.tkk.media.xide.client.utils.Cons;

/**
 * @author Evgenia Samochadina
 * @date Dec 8, 2008
 *
 */
public class DesignTab extends TopTab {
	public final int tabID = View.DESIGN;

	// The timer which executes redraw process
	protected Timer timer;
	
	public DesignTab(String code) {
		super();
		
		// Initiate timer
		timer = new Timer() {
			public void run() {
				// if (previousDate)
				UpdateUITimerAction();
			}
		};
		
		// TODO: change style name
//		this.addStyleName(".gwt-ScrollTable");
//		DOM.setStyleAttribute(this.getElement(), "backgroundImage", "url('canvas.png')");
		
//		Component c = new Component(Template.fakeTemplate("web page"),null);
//		c.Draw();
//		this.add(c);
		
		
//		this.add(Main.getInstance().getWebPage());
	}
	
	public void setPage() {
		FocusPanel p = new FocusPanel();
		p.add(Main.getInstance().getWebPage());
		addWidget(p);
	}
	
	private void UpdateUITimerAction() {
		Main.getInstance().getWebPage().Draw();
	}
	@Override
	public void UpdateUI() {
		timer.schedule(Cons.UI_UPDATE_DELAY_PERIOD_MS);
	}
}
