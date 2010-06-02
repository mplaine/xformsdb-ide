/**
 * 
 */
package fi.tkk.media.xide.client.DnD;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.Tabs.SearchComponentTab;
import fi.tkk.media.xide.client.UI.Widget.Component;
import fi.tkk.media.xide.client.UI.Widget.TemplateShortInfoPanel;

/**
 * @author Evgenia Samochadina
 * @date Dec 11, 2008
 * 
 */
public class DragControllerAdding extends PickupDragController {

	private static final String CSS_DEMO_FLEX_TABLE_ROW_EXAMPLE_TABLE_PROXY = "demo-FlexTableRowExample-table-proxy";
	public Widget proxy;

	/**
	 * @param boundaryPanel
	 * @param allowDroppingOnBoundaryPanel
	 */
	public DragControllerAdding(AbsolutePanel boundaryPanel,
			boolean allowDroppingOnBoundaryPanel) {
		super(boundaryPanel, allowDroppingOnBoundaryPanel);
		this.setBehaviorMultipleSelection(false);
		this.setBehaviorDragProxy(true);
	}

	protected Widget newDragProxy(DragContext context) {

//		FlexTable proxy;
//		proxy = new FlexTable();
//		proxy.setStyleName(CSS_DEMO_FLEX_TABLE_ROW_EXAMPLE_TABLE_PROXY);
//		proxy.insertRow(0);
		
		TemplateShortInfoPanel w = (TemplateShortInfoPanel) context.selectedWidgets.get(0);
		
		// In background:  Get template information
		if (TemplateShortInfoPanel.selectedPanel != w) {
			// Store selection
			TemplateShortInfoPanel.selectedPanel = w;
			// Checking does not needed but still
//			if (getSearchTab().IsNewTemplateSelected()) {
			w.GetDetailedTemplateInfo();
//			}
		}

		// Create a proxy
		
		SimplePanel p = new SimplePanel();
		p.setSize("200px", "90px");
	// DISPLAY everything:
		
		Label labelTitle = new Label(w.getInfo().getTitle().getStringValue());
		labelTitle.setStyleName("design-LabelMiddleText");
		
		// Display description
		Label labelDescr = new Label(w.getInfo().getDescr().getStringValue());
		labelDescr.setStyleName("design-LabelSmallText");
		DockPanel dockPanel = new DockPanel();
		dockPanel.setStyleName("design-template-info-panel-dragging");
		dockPanel.add(labelTitle, DockPanel.NORTH);
		dockPanel.add(labelDescr, DockPanel.CENTER);
		
		p.add(dockPanel);
//		p.add(c);
		
//		HTML html = new HTML( DOM.getInnerHTML(c.getElement()));// new HTML(c.getElement().getInnerHTML());
//		proxy.setWidget(0, 0, html);
//		proxy.getCellFormatter().setStyleName(0, 0, "dragdrop-selected, dragdrop-dragging, dragdrop-proxy");
		
		this.proxy = p;
		return this.proxy;

//		FlexTable proxy;
//		proxy = new FlexTable();
//		proxy.setStyleName(CSS_DEMO_FLEX_TABLE_ROW_EXAMPLE_TABLE_PROXY);
//		proxy.insertRow(0);
//		
//		TemplateShortInfoPanel w = (TemplateShortInfoPanel) context.selectedWidgets.get(0);
//		Template t = w.getTemplateForComponentsCreation();
//		Component c = new Component(t, null);
//		c.Draw(true);
//
//		HTML html = new HTML( DOM.getInnerHTML(c.getElement()));// new HTML(c.getElement().getInnerHTML());
//		proxy.setWidget(0, 0, html);
//		proxy.getCellFormatter().setStyleName(0, 0, "dragdrop-selected, dragdrop-dragging, dragdrop-proxy");
//		this.proxy = proxy;
//		return this.proxy;
	}

	public void dragStart() {
		super.dragStart();
		proxy.setStyleName(CSS_DEMO_FLEX_TABLE_ROW_EXAMPLE_TABLE_PROXY);
	}
}
