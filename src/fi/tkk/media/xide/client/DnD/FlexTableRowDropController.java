package fi.tkk.media.xide.client.DnD;

/*
 * Copyright 2008 Fred Sauer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.DragController;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.AbstractPositioningDropController;
import com.allen_sauer.gwt.dnd.client.util.CoordinateLocation;
import com.allen_sauer.gwt.dnd.client.util.DOMUtil;
import com.allen_sauer.gwt.dnd.client.util.Location;
import com.allen_sauer.gwt.dnd.client.util.LocationWidgetComparator;
import com.allen_sauer.gwt.dnd.client.util.WidgetLocation;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.Data.Selectable;
import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.Tabs.NavigatorTab;
import fi.tkk.media.xide.client.Tabs.PLTab;
import fi.tkk.media.xide.client.Tabs.PropertiesTab;
import fi.tkk.media.xide.client.UI.Widget.BasicPageElement;
import fi.tkk.media.xide.client.UI.Widget.Component;
import fi.tkk.media.xide.client.UI.Widget.HorizontaPanelPLTab;
import fi.tkk.media.xide.client.UI.Widget.Slot;
import fi.tkk.media.xide.client.UI.Widget.TemplateShortInfoPanel;

/**
 * Allows one or more table rows to be dropped into an existing table.
 */
public final class FlexTableRowDropController extends
		AbstractPositioningDropController {

	private static final String CSS_DEMO_TABLE_POSITIONER = "demo-table-positioner";

	private Slot flexTable;

	public Slot getFlexTable() {
		return flexTable;
	}

	private IndexedPanel flexTableRowsAsIndexPanel = new IndexedPanel() {

		public Widget getWidget(int index) {
			return flexTable.getWidget(index, 0);
		}

		public int getWidgetCount() {
			return flexTable.getRowCount();
		}

		public int getWidgetIndex(Widget child) {
			throw new UnsupportedOperationException();
		}

		public boolean remove(int index) {
			throw new UnsupportedOperationException();
		}
	};

	private Widget positioner = null;

	private int targetRow;

	public FlexTableRowDropController(Slot flexTable) {
		super(flexTable);
		this.flexTable = flexTable;
	}

	@Override
	public void onDrop(DragContext context) {
		
		// Slot and web page should be changed since a set of components has been changed
		
		Main.onSlotChangedFirst(flexTable);
		
	
		DragController controller = context.dragController;
		int targetRow;
		if (flexTable.isEmpty()) {
			targetRow = 0;
		}
		else {
			targetRow = this.targetRow + 1;
		}
		
		if (controller instanceof FlexTableRowDragController) {

			// Between slot moving
			FlexTableRowDragController trDragController = (FlexTableRowDragController) context.dragController;

			// Graphical moving
//			FlexTableUtil.moveRow(trDragController.getDraggableTable(),
//					flexTable, trDragController.getDragRow(), targetRow + 1);

			Slot sourseSlot = (Slot)trDragController.getDraggableTable();
			Slot targetSlot = flexTable;
			int sourseRow = trDragController.getDragRow();
		    
			// Move correstponding elements in Navigation Tab
		    NavigatorTab.getInstance().MoveElement(
					sourseSlot.getTreeItem(), flexTable.getTreeItem(),
					sourseRow, targetRow);
		    
		    if (sourseSlot == targetSlot && sourseRow < targetRow) {
		        targetRow --;
		      }

		    BasicPageElement child = sourseSlot.getChildrenElements().get(sourseRow);

		    // Remove 
		    sourseSlot.RemoveComponent((Component)child);
		    
		    // Insert 
//		    targetSlot.insertRow(targetRow);
		    targetSlot.AddChild(child, targetRow);
		    


		} else if (controller instanceof DragControllerAdding) {
			
			// Adding new Component from SearchComponentTab
			
			TemplateShortInfoPanel w = (TemplateShortInfoPanel) context.selectedWidgets.get(0);
			Template t = w.getTemplateForComponentsCreation();
			Component c = new Component(t, flexTable);
			c.Draw(true);

			// TODO: change this stupid into something more logical: take component which is drag proxy
			// remove flexitable from drag proxy
			
			c.makeChildSlotsDraggable();

			// NavigatorTab managing! Important to be before addChild as then
			// the slot will not be empty
			NavigatorTab.getInstance().addElement(flexTable.getTreeItem(),
					c, targetRow );
			flexTable.AddChild(c, targetRow);



			// PL managing!
			PLTab.getInstance().addComponent(c);

			super.onDrop(context);

		} else if (controller instanceof DragControllerPLAdding) {
			// Adding new Component from SearchComponentTab
			Component c = (Component)((SimplePanel)((DragControllerPLAdding)controller).proxy).getWidget();
			c.makeChildSlotsDraggable();
			
			NavigatorTab.getInstance().addElement(flexTable.getTreeItem(),
					c, targetRow);

			flexTable.AddChild(c, targetRow);

			// PL managing!
			PLTab.getInstance().addComponent(c);

			super.onDrop(context);

		}
		
		Main.onSlotValueChanged();
		Main.getInstance().UpdateUI(Main.BOTTOM_TAB);
	}



	@Override
	public void onEnter(DragContext context) {

		if (context.dragController instanceof FlexTableRowDragController) {
			// Adding
			Widget widget = ((FlexTableRowDragController) context.dragController).proxy;
			widget.setStyleName("demo-FlexTableRowExample-table-proxy-move");
		}
		else if (context.dragController instanceof DragControllerAdding) {
			Widget widget = ((DragControllerAdding) context.dragController).proxy;
			widget.setStyleName("demo-FlexTableRowExample-table-proxy-add");
		}
		else if (context.dragController instanceof DragControllerPLAdding) {
			Widget widget = ((DragControllerPLAdding) context.dragController).proxy;
			widget.setStyleName("demo-FlexTableRowExample-table-proxy-add");
		}
		
//		context.dragEndCleanup();
		super.onEnter(context);
		positioner = newPositioner(context);
	}

	@Override
	public void onLeave(DragContext context) {
		if (context.dragController instanceof FlexTableRowDragController) {
			// Adding
			Widget widget = ((FlexTableRowDragController) context.dragController).proxy;
			widget.setStyleName("demo-FlexTableRowExample-table-proxy");
		}
		else if (context.dragController instanceof DragControllerAdding) {
			Widget widget = ((DragControllerAdding) context.dragController).proxy;
			widget.setStyleName("demo-FlexTableRowExample-table-proxy");
		}
		else if (context.dragController instanceof DragControllerPLAdding) {
			Widget widget = ((DragControllerPLAdding) context.dragController).proxy;
			widget.setStyleName("demo-FlexTableRowExample-table-proxy");
		}
		positioner.removeFromParent();
		positioner = null;
		super.onLeave(context);
	}

	@Override
	public void onMove(DragContext context) {
		super.onMove(context);
		targetRow = DOMUtil.findIntersect(flexTableRowsAsIndexPanel,
				new CoordinateLocation(context.mouseX, context.mouseY),
				LocationWidgetComparator.BOTTOM_HALF_COMPARATOR) - 1;

		Widget w = flexTable.getWidget(targetRow == -1 ? 0 : targetRow, 0);
		Location widgetLocation = new WidgetLocation(w, context.boundaryPanel);
		Location tableLocation = new WidgetLocation(flexTable,
				context.boundaryPanel);
		context.boundaryPanel.add(positioner, tableLocation.getLeft(),
				widgetLocation.getTop()
						+ (targetRow == -1 ? 0 : w.getOffsetHeight()));
	}

	Widget newPositioner(DragContext context) {
		Widget p = new SimplePanel();
		p.addStyleName(CSS_DEMO_TABLE_POSITIONER);
		p.setPixelSize(flexTable.getOffsetWidth(), 1);
		return p;
	}
}
