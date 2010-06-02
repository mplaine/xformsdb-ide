package fi.tkk.media.xide.client;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.DnD.DeleteDropController;
import fi.tkk.media.xide.client.DnD.DragControllerAdding;
import fi.tkk.media.xide.client.DnD.DragControllerPLAdding;
import fi.tkk.media.xide.client.DnD.FlexTableRowDragController;
import fi.tkk.media.xide.client.DnD.FlexTableRowDropController;
import fi.tkk.media.xide.client.UI.Widget.Slot;

public class DnDController {

	// DND staff
	// boundary panel for dragging
	private static AbsolutePanel boundaryPanel;
	// drop controllers
	private static ArrayList<FlexTableRowDropController> dropControllers;
	// delete drop controller
	private static DeleteDropController dropControllerDelete;
	// drag controller (between slot dragging)
	private static FlexTableRowDragController dragControllerMoving;
	// drag controller (adding new component from Search tab)
	private static DragControllerAdding dragControllerAdding;
	// drag controller (adding new component from PL tab)
	private static DragControllerPLAdding dragControllerPLAdding;

	public static void InitDND() {
		
		boundaryPanel = new AbsolutePanel();
		
		DOM.setStyleAttribute(boundaryPanel.getElement(), "position", "relative");
		boundaryPanel.setSize("100%", "100%");
		boundaryPanel.setStyleName("demo-FlexTableRowExample");
		dragControllerAdding = new DragControllerAdding(boundaryPanel, false);
		dragControllerPLAdding = new DragControllerPLAdding(boundaryPanel,
				false);
		dragControllerMoving = new FlexTableRowDragController(boundaryPanel);
		dropControllers = new ArrayList<FlexTableRowDropController>();

	
	}
	
	public static void registerDropController(Widget panel) {
		dropControllerDelete = new DeleteDropController(panel);
		dragControllerMoving.registerDropController(dropControllerDelete);
	}
	
	public void PrintDropTargets() {
		if (dropControllers != null) {
			for (Iterator<FlexTableRowDropController> iterator = dropControllers
					.iterator(); iterator.hasNext();) {
				FlexTableRowDropController o = iterator.next();
			}
		}

	}

	public static ArrayList<FlexTableRowDropController> getDropControllers() {
		return dropControllers;
	}

	public static void addDropController(Slot slot) {
		// Add new slot to the system
		FlexTableRowDropController dropController = new FlexTableRowDropController(
				slot);
		slot.dropController = dropController;
		if (!dropControllers.contains(slot)) {
			dropControllers.add(dropController);
			// Register for adding components
			dragControllerAdding.registerDropController(dropController);
			// Register for adding components
			dragControllerPLAdding.registerDropController(dropController);
			// Register for moving components
			dragControllerMoving.registerDropController(dropController);
		}
	}

	public static void removeDropController(Slot slot) {
		FlexTableRowDropController o = null;
		// Find slot in the controllers
		for (Iterator<FlexTableRowDropController> iterator = dropControllers
				.iterator(); iterator.hasNext();) {
			FlexTableRowDropController object = iterator.next();
			if (object.getFlexTable() == slot) {
				o = object;
			}
		}
		if (o != null) {
			dragControllerAdding.unregisterDropController(o);
			dragControllerPLAdding.unregisterDropController(o);
			dragControllerMoving.unregisterDropController(o);
			boolean b = dropControllers.remove(o);
		}

		// this.dropControllers.add(new FlexTableRowDropController(slot));
	}

	public static FlexTableRowDragController getDragControllerMoving() {
		return dragControllerMoving;
	}

	public static DragControllerAdding getDragControllerAdding() {
		return dragControllerAdding;
	}

	public static DragControllerPLAdding getDragControllerPLAdding() {
		return dragControllerPLAdding;
	}


	public static AbsolutePanel getBoundaryPanel() {
		return boundaryPanel;
	}
}
