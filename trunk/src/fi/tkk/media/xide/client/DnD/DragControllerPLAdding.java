/**
 * 
 */
package fi.tkk.media.xide.client.DnD;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.UI.Widget.Component;
import fi.tkk.media.xide.client.UI.Widget.HorizontaPanelPLTab;
import fi.tkk.media.xide.client.UI.Widget.TemplateShortInfoPanel;

/**
 * @author Evgenia Samochadina
 * @date Dec 11, 2008
 *
 */
public class DragControllerPLAdding extends PickupDragController {
	private static final String CSS_DEMO_FLEX_TABLE_ROW_EXAMPLE_TABLE_PROXY = "demo-FlexTableRowExample-table-proxy";
	public Widget proxy;

	/**
	 * @param boundaryPanel
	 * @param allowDroppingOnBoundaryPanel
	 */
	public DragControllerPLAdding(AbsolutePanel boundaryPanel,
			boolean allowDroppingOnBoundaryPanel) {
		super(boundaryPanel, allowDroppingOnBoundaryPanel);
	    this.setBehaviorMultipleSelection(false);
	    this.setBehaviorDragProxy(true);
	}

	protected Widget newDragProxy(DragContext context) {
		
		Template t = ((HorizontaPanelPLTab)context.selectedWidgets.get(0)).getTemplate();
		
		Component c = new Component(t.cloneIt(), null);
		c.Draw(false);
		SimplePanel p = new SimplePanel();
		p.add(c);
//		HTML html = new HTML( DOM.getInnerHTML(c.getElement()));// new HTML(c.getElement().getInnerHTML());
//		proxy.setWidget(0, 0, html);
//		proxy.getCellFormatter().setStyleName(0, 0, "dragdrop-selected, dragdrop-dragging, dragdrop-proxy");
		this.proxy = p;
		return proxy;

//		FlexTable proxy;
//		proxy = new FlexTable();
//		proxy.setStyleName(CSS_DEMO_FLEX_TABLE_ROW_EXAMPLE_TABLE_PROXY);
//		proxy.insertRow(0);
//		
//		Template t = ((HorizontaPanelPLTab)context.selectedWidgets.get(0)).getTemplate();
//		Component c = new Component(t.cloneIt(), null);
//		c.Draw(true);
//
//		HTML html = new HTML( DOM.getInnerHTML(c.getElement()));// new HTML(c.getElement().getInnerHTML());
//		proxy.setWidget(0, 0, html);
//		proxy.getCellFormatter().setStyleName(0, 0, "dragdrop-selected, dragdrop-dragging, dragdrop-proxy");
//		this.proxy = proxy;
//		return proxy;

      }
}
