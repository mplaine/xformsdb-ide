package fi.tkk.media.xide.client.Tabs;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class TopTab extends Tab {
	FlowPanel mainPanel;
	
	public TopTab() {
		super();
        AbsolutePanel p  = new AbsolutePanel();
        p.setSize("100%", "100%");
        DOM.setStyleAttribute(p.getElement(), "position", "absolute");
        this.add(p);
       
        DOM.setStyleAttribute(DOM.getParent(p.getElement()), "position", "relative");
       
        mainPanel = new FlowPanel();
//        for (int i = 0; i < 15; i++) {
//            Label l = new Label ("compppp" + i);
//            DOM.setStyleAttribute(l.getElement(), "padding", "5px");
//            mainPanel.add(l);
//        }
        DOM.setStyleAttribute(mainPanel.getElement(), "overflowY", "auto");
        DOM.setStyleAttribute(mainPanel.getElement(), "height", "100%");
        p.add(mainPanel);
	}
	
	public void addWidget(Widget w) {
		mainPanel.add(w);
	}
	
}
