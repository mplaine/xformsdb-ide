//package fi.tkk.media.xide.client.popups.garbage;
//
//import com.MJC.client.Effects.Blind;
//import com.MJC.client.Effects.Blinds.BlindUp;
//import com.google.gwt.user.client.DOM;
//import com.google.gwt.user.client.ui.Button;
//import com.google.gwt.user.client.ui.ClickListener;
//import com.google.gwt.user.client.ui.Label;
//import com.google.gwt.user.client.ui.RootPanel;
//import com.google.gwt.user.client.ui.Widget;
//
//
///**
// * Class binds together different things to show hint popup.
// * The popup is shown on the top of the window when it is requested in the code
// * and hided when user presses hide button on it. It also contains button which hides and never shows hint popups
// * @author evgeniasamochadina
// *
// */
//public class AnimatedHintPopup extends GoodPopupScrollablePanel {
//
//	final static public String[] hintTexts = {
//			"You've started to modify selected component's template. All modifications will be showed on the Design Tab imideately. If you want to save your changes please press save button. Otherwise press Cancel button and all changes will be reverted", // On component changed 
//			"You've started to modify template. Its public template so if you will save your chnages it becomes private" // On public template modified
//	};
//	
//	// Animation specific fields
//	private Blind blindup;
//	private BlindUpChangebleWidget blindupinst;
//	
//	// Shows whether popup could be shown or not
//	boolean isDisplayable = true;
//	/**
//	 * Creates popup with animation and initiates it with correct position
//	 */
//	public AnimatedHintPopup() {
//		super("XIDE help message", null, "_");
//		DOM.setStyleAttribute(this.getElement(), "maxWidth", "40%");
//		String rect = ("px, " + 0 + "px, " + 0 + "px, 0px)");
////		DOM.setStyleAttribute(getElement(), "clip", ("rect(" + 0 + rect));	
////		DOM.setStyleAttribute(getElement(), "clip", ("rect(0px, 0px, 0px, 0px)"));
//		DOM.setStyleAttribute(this.getElement(), "opacity", "0.8");
//		//Close button
//		Button closeButton = new Button();
//		closeButton.setText("Hide popup");
//		// Add it to the panel before animation initialized to set correct popup size to the animation
//		addButton(closeButton);
//		
//		// Set clip attribute to total 0 so that popup is invisible
//		// Attribute value will be changed when popup show will be called
//		DOM.setStyleAttribute(getElement(), "clip", ("rect(0px, 0px, 0px, 0px)"));
//
//		// Add popup to the panel before animation init
//		RootPanel.get().add(this, 200, 0);
//		
//		// Animation initialization
//		blindupinst = new BlindUpChangebleWidget(this);
//		blindup = new Blind(this, blindupinst);
//		
//		// Close button action
//		closeButton.addClickListener(new ClickListener() {;
//			public void onClick(Widget sender) {
//				 hide();
//			}});
//	  
//		// Close forever button hides popup and do not allow it to rais again
//		Button closeForeverButton = new Button();
//		closeForeverButton.setText("Hide forever");
//		closeForeverButton.addClickListener(new ClickListener() {
//
//			public void onClick(Widget sender) {
//				isDisplayable(false);
//				hide();
//			}});
//		addButton(closeForeverButton);
//		
//	    blindup.setFPS(50);
//	    
////	    blindupinst.update();
////	    blindupinst.finish();
////	    blindupinst.onWidgetSizeChanged();
////        this.setVisible(false);
////		blindup.toggle();
//	}
//	/**
//	 * Sets whether popup is displayable now or not 
//	 */
//	public void isDisplayable(boolean isD) {
//		isDisplayable = isD;
//	}
//	/**
//	 * Changes the main text displayed on the popup
//	 * @param textString new text to be displayed
//	 */
//	public void changePopupText(String textString) {
//		blindupinst.beforeWidgetSizeChanged();
//		AnimatedHintPopup.this.changeText(textString);
//		blindupinst.onWidgetSizeChanged();
//	}
//	
//	/**
//	 * Changes the main text displayed on the popup and initiates popup showing animation 
//	 * @param textString new text to be displayed
//	 */
//	public void changePopupTextAndShow(String textString) {
//		blindupinst.beforeWidgetSizeChanged();
//		changeText(textString);
//		blindupinst.onWidgetSizeChanged();
//		show();
//	}
//	
//	/**
//	 * Initiates popup showing animation 
//	 */
//	public void show() {
//		if (isDisplayable) {
//			blindup.toggle();
//		}
//	}
//	
//	/**
//	 * Initiates popoup hiding animation
//	 */
//	public void hide() {
//		blindup.toggle();
//	}
//}
