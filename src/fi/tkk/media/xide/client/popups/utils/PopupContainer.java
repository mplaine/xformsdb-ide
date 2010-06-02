package fi.tkk.media.xide.client.popups.utils;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.utils.PagePanel;

public class PopupContainer extends PopupContainerBase{

		protected static PopupContainer singleton = null;
		
		/**
		 * Gets an instance of the popup. 
		 * @return
		 */
		public static PopupContainer getPopup() {
			if (singleton == null) {
				singleton = new PopupContainer();
			}
			singleton.clearPopup();
			return singleton;
		}

		protected void clearPopup() {
			  // Clear
			setText("Message");
		    buttonPanel.clear();
		    ((FlowPanel)header).clear();
		    ((FlowPanel)content).clear();
		}
		
		public PopupContainer showPopup() {
			center();
			return this;
		}

		protected PopupContainer() {
			super();
			initDimmedPopup();
			init();
			this.setModal(true);
			// Do not allow direct instantiation.
		}

		public void center() {
			glass.show();
			super.center();
		}

		/**
		 * Hides popup. Clears popup's header, content and buttons for the next use.
		 */
		  @Override
		  public void hide() {
			 
			glass.hide();
		    super.hide();
		    
		  }
		
		  /**
		   * Initializes the content of the popup
		   */
		public void init() {
			DOM.setStyleAttribute(this.getElement(), "maxWidth", "70%");
			setModal(true);
			
			setText("XIDE message");
			
			header = new FlowPanel();
			content = new FlowPanel();
			
			PagePanel p = new PagePanel(header, initButtonPanel(), content);
			this.add(p);
		}
		
		public void  addHeader(String text) {
			((FlowPanel)header).add(new Label(text));
			
		}

		public void  addHeader(Widget w) {
			((FlowPanel)header).add(w);
			
		}


		
		/**
		 * Adds a widget as a content. Several widgets can be added.
		 * @param w
		 */
		public void addContent(Widget w) {
			((FlowPanel)content).add(w);
		}
		
}
