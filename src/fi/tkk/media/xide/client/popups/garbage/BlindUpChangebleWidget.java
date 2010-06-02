//package fi.tkk.media.xide.client.popups.garbage;
//
//import com.MJC.client.Effects.Blinds.BlindUp;
//import com.google.gwt.user.client.DOM;
//import com.google.gwt.user.client.ui.RootPanel;
//import com.google.gwt.user.client.ui.SimplePanel;
//import com.google.gwt.user.client.ui.Widget;
//
///**
// * An extension of the class BlindUp used for making changes in animating widget. 
// * The only one method which is added recalculates rect, target and change values which are used 
// * in the animation according to the new size of the widget
// * @author evgeniasamochadina
// *
// */
//public class BlindUpChangebleWidget extends BlindUp{
//	
//	public BlindUpChangebleWidget(Widget wdg) {
//		super(wdg);
//	}
//	
//	/**
//	 * Runs before the widget will be changed. Sets clip attribute of the widget so that widget cannot be seen.
//	 */
//	public void beforeWidgetSizeChanged() {
//		rect = ("px, " + getWidget().getOffsetWidth() + "px, " + 0 + "px, 0px)");
//		DOM.setStyleAttribute(widget.getElement(), "clip", ("rect(" + ((int) Math.round(current)) + rect));
//
//	}
//	/**
//	 * Recalculates different internal values according to the new size of the widget
//	 * after the widget has changed externally. Sets rect value but does not assign it to the element. 
//	 * This will be done when update method will be called for the first time.
//	 */
//	public void onWidgetSizeChanged() {
//		target = getWidget().getOffsetHeight();
//		// 50 is default value. Can be changed later on if necessary
//		change = widget.getOffsetHeight() / (1 * 50);
//		current = target;
//		rect = ("px, " + getWidget().getOffsetWidth() + "px, " + target + "px, 0px)");
//
//	}
//	
//	/**
//	 * Overrides update method of the parent. The only modification which is made 
//	 * is changing order of setWidgetPosition and setStyleAttribute. This was done to 
//	 * be sure that when update runs for the first time after changing the widget 
//	 * the widget will previously set on the correct position according its new size and
//	 * only after that clip attribute will be set. 
//	 */
//	public boolean update() {
//		current += change;
//		if((current > target) || (current < 0)){
//			finish();
//			return true;
//		}
//		RootPanel.get().setWidgetPosition(widget, posn.getX(), posn.getY() - ((int) Math.round(current)));
//		DOM.setStyleAttribute(widget.getElement(), "clip", ("rect(" + ((int) Math.round(current)) + rect));
//		return false;	}
//}
