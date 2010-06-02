package fi.tkk.media.xide.client.DnD;

import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.UI.Widget.Component;

/**
 * Sample SimpleDropController which discards draggable widgets which are
 * dropped on it.
 */

public class DeleteDropController extends SimpleDropController {

	private static final String CSS_DEMO_BIN_DRAGGABLE_ENGAGE = "demo-bin-draggable-engage";

//	private Bin bin;
	private Widget panel;

	public DeleteDropController(Widget panel) {
		
		super(panel);
		this.panel = panel;
	}

	@Override
	public void onDrop(DragContext context) {
		
		
		
		for (Widget widget : context.selectedWidgets) {
			// Slot and web page should be changed since a set of components has been changed
			Main.onSlotChangedFirst((Component)widget);
			((Component)widget).delete();
			
			Main.onSlotValueChanged();
			Main.getInstance().UpdateUI(Main.BOTTOM_TAB);
//			bin.eatWidget(widget);
		}
		super.onDrop(context);
	}

	@Override
	public void onEnter(DragContext context) {
//		context.dragEndCleanup();
		super.onEnter(context);
		Widget widget = ((FlexTableRowDragController)context.dragController).proxy; 
			widget.setStyleName("demo-FlexTableRowExample-table-proxy-delete");
//			Widget comp = ((FlexTable)widget).getWidget(0, 0);
//			comp.setStyleName("demo-FlexTableRowExample-table-proxy-delete");
//			widget.addStyleName(CSS_DEMO_BIN_DRAGGABLE_ENGAGE);
		
//		bin.setEngaged(true);
	}

	@Override
	public void onLeave(DragContext context) {
		super.onLeave(context);
		Widget widget = ((FlexTableRowDragController)context.dragController).proxy; 
		widget.setStyleName("demo-FlexTableRowExample-table-proxy");
		
	}

	@Override
	public void onPreviewDrop(DragContext context) throws VetoDragException {
		super.onPreviewDrop(context);
//		if (!bin.isWidgetEater()) {
//			throw new VetoDragException();
//		}
	}
}
