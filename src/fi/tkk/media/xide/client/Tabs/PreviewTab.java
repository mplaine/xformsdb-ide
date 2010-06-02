package fi.tkk.media.xide.client.Tabs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.View;
import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.Data.Tag;
import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.UI.Widget.GoodButton;
import fi.tkk.media.xide.client.UI.Widget.StyledButton;
import fi.tkk.media.xide.client.UI.Widget.TagsSearchPanel;
import fi.tkk.media.xide.client.UI.Widget.TextBoxWithRequestString;
import fi.tkk.media.xide.client.fs.XIDEFile;
import fi.tkk.media.xide.client.popups.basic.Popup;
import fi.tkk.media.xide.client.popups.basic.PopupError;
import fi.tkk.media.xide.client.popups.utils.interfaces.Action;
import fi.tkk.media.xide.client.utils.ConnectionToServer;
import fi.tkk.media.xide.client.utils.ConnectionToServer.CallbackActions;

/**
 * Preview tab makes a preview of the page which is currently loaded in the PP.
 * When user clicks to the preview tab, it sends a preview request to server.
 * A page is prepared to be previewed on a server, and then a link is sent back. The link is loaded into the iframe and showed to a user.
 * When user closes preview tab, a request is sent to the server and preview data is deleted.
 *  
 * @author evgeniasamochadina
 *
 */
public class PreviewTab extends TopTab {
	public final int tabID = View.PREVIEW;
	
	FlowPanel panelTemplateInfoList;
	
	ConnectionToServer connectionToServer;
	String link = null;
	Frame frame;
	Label label ;
	boolean isWaitingForReload;
	boolean isOpened;
	public PreviewTab() {
		// Show welcome string and reload button
		HorizontalPanel hp =new HorizontalPanel();
		isWaitingForReload = false;
		isOpened = false;
		hp.setWidth("100%");
		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hp.getElement().setAttribute("style", "width: 100%; background: #A7C896");
		
		Label welcomeStringLabel = new HTML ("If you see an error or want to forse reloading the preview please click <b>Preview</b> button");
		
		hp.add(welcomeStringLabel);
		Widget reload = new StyledButton("Reload", null, new ClickHandler() {

			public void onClick(ClickEvent event) {
				UpdateUI();
			}
		}).getButton() ;
		DOM.setStyleAttribute(hp.getElement(), "paddingBottom", "3px");
		DOM.setStyleAttribute(hp.getElement(), "paddingTop", "1px");
		hp.add(reload);
		
		label = new Label("Preview is being prepared");
		label.setStyleName("design-properties-table-welcome-text");
		frame= new Frame();
		
		
//		addWidget(hp);
		
		

		VerticalPanel vp = new VerticalPanel();
		
		vp.add(hp);

		// Tab panel example
		// External tab panel (under high lvel tab )
		AbsolutePanel p0  = new AbsolutePanel();
		p0.setSize("100%", "100%");
		DOM.setStyleAttribute(p0.getElement(), "position", "relative");
		DOM.setStyleAttribute(p0.getElement(), "background", "#EAEAEA");
		
		this.add(p0);
		
		// A wrapper for the flow panel to set it's size to 100% 
		//of the remaining space
		AbsolutePanel p1  = new AbsolutePanel();
		p1.setSize("100%", "100%");
		DOM.setStyleAttribute(p1.getElement(), "position", "absolute");
		
		// Panel to scroll
		panelTemplateInfoList = new FlowPanel();
//		FlowPanel comps = new FlowPanel();
//		for (int i = 0; i < 15; i++) {
//			Label l = new Label ("compppp" + i);
//			DOM.setStyleAttribute(l.getElement(), "padding", "5px");
//			comps.add(l);
//		}
		DOM.setStyleAttribute(panelTemplateInfoList.getElement(), "overflowY", "scroll");
		DOM.setStyleAttribute(panelTemplateInfoList.getElement(), "height", "100%");
		DOM.setStyleAttribute(panelTemplateInfoList.getElement(), "paddingLeft", "2px");
		DOM.setStyleAttribute(panelTemplateInfoList.getElement(), "paddingRight", "2px");
		p1.add(panelTemplateInfoList);
		
		DeckPanel dock = new DeckPanel();
		dock.setStyleName("gwt-TabPanelBottom");
		dock.add(p1);
		dock.showWidget(0);

//		Label header = new Label("header");
		
		VerticalPanel panel = new VerticalPanel();
		panel.setStyleName("design-new-search-tab");
		panel.setSize("100%", "100%");
	    panel.add(vp);
	    panel.add(dock);
	    
	    panel.setCellHeight(dock, "100%");
	    vp.setWidth("100%");

		p0.add(panel);
		

//		tp.getTabBar().selectTab(0);
		
		DOM.setStyleAttribute(DOM.getParent(dock.getElement()), "size", "inherit");
		DOM.setStyleAttribute(DOM.getParent(p1.getElement()), "position", "relative");

		
	}
	
	public void onTabOpened() {
		isOpened = true;

		beforePagePreview();
		
	}
	public void onTabClosed() {
		isOpened = false;
//		stopPagePreview();
		
		if (frame!=null) {
			frame.removeFromParent();
		}
		if (label.isAttached()) {
			label.removeFromParent();
		}
	}
	
	/**
	 * Send a request to server asking to prepare temporary copy of the application sources. When a folder structure has created, 
	 * it initiates a preview process based on these sources.
	 */
	private void beforePagePreview() {
		if (frame!=null) {
			frame.removeFromParent();
		}
		label.setText("Preview is being prepared");
		panelTemplateInfoList.add(label);
		
		ConnectionToServer.makeACall(new CallbackActions() {

			public void execute(AsyncCallback callback) {
				connectionToServer.searchService.onBeforePreview(
						Main.getInstance().getWebPage().getProperties().getProperties().get(Property.APP_ID).getStringValue(),
						Main.getInstance().getWebPage().getProperties().getProperties().get(Property.ID).getStringValue(),
						callback);
			}

			public void onFailure(Throwable caught) {
				new PopupError("Unfortunately preview procedure has failed on server!",  caught.getMessage());
				showFailedLabel();
			}

			public void onSuccess(Object result) {
				// Send files and initite preview
				sendUpdatedFilesToTemp();
			}});
	}
	
	/**
	 * Checks which files have been changed and send them to the temp directory on the server.
	 * Finally initiates page preview
	 */
	public void sendUpdatedFilesToTemp(){
		
		Template template = Main.getInstance().getWebPage().getTemplate();
		
		checkAndSendFiles(template.getCSS());
		checkAndSendFiles(template.getDataInstances());
		checkAndSendFiles(template.getDB());
		checkAndSendFiles(template.getQueries());
		checkAndSendFiles(template.getResources());
		checkAndSendFiles(template.getSourceCode());
		
		startPagePreview();
	}
	
	private void checkAndSendFiles(HashMap<String, XIDEFile> filelist) {
		for (Iterator<String> iterator = filelist.keySet().iterator(); iterator.hasNext();) {
			String fileName = iterator.next();
			filelist.get(fileName).updateContentToServerTemp();
		}
		
	}
	
	/**
	 * Send a request to server asking to prepare page preview link. Link is send as a result.
	 */
	private void startPagePreview() {
		ConnectionToServer.makeACall(new CallbackActions() {

			public void execute(AsyncCallback callback) {
				connectionToServer.searchService.onStartPreview(
						Main.getInstance().getWebPage().getProperties().getProperties().get(Property.APP_ID).getStringValue(),
						Main.getInstance().getWebPage().getProperties().getProperties().get(Property.ID).getStringValue(),
						callback);
			}

			public void onFailure(Throwable caught) {
				new PopupError("Unfortunately preview procedure has failed on server!",  caught.getMessage());
				showFailedLabel();
			}

			public void onSuccess(Object result) {
				if (result instanceof String && !((String)result).equals("")) {
					// Display link
					showPreview((String)result);
				} 
			}});
	}
	
	/**
	 * Send a request to server informing that page preview stuff can be deleted since a preview has finished
	 */
	private void stopPagePreview() {
		ConnectionToServer.makeACall(new CallbackActions() {

			public void execute(AsyncCallback callback) {
				connectionToServer.searchService.onStopPreview(
						Main.getInstance().getWebPage().getProperties().getProperties().get(Property.APP_ID).getStringValue(),
						Main.getInstance().getWebPage().getProperties().getProperties().get(Property.ID).getStringValue(),
						callback);
			}

			public void onFailure(Throwable caught) {
				showFailedLabel();
			}

			public void onSuccess(Object result) {
					afterPagePreview();
//					showPreview((String)result);
			}});
	}
	
	private void afterPagePreview() {
		ConnectionToServer.makeACall(new CallbackActions() {

			public void execute(AsyncCallback callback) {
				connectionToServer.searchService.onAfterPreview(
						Main.getInstance().getWebPage().getProperties().getProperties().get(Property.APP_ID).getStringValue(),
						Main.getInstance().getWebPage().getProperties().getProperties().get(Property.ID).getStringValue(),
						callback);
			}

			public void onFailure(Throwable caught) {
				showFailedLabel();
			}

			public void onSuccess(Object result) {
					if (isWaitingForReload) {
						beforePagePreview();
					// Display link
//					showPreview((String)result);
				} 
			}});
	}
	
	private void showFailedLabel() {
		// Remove frame and label if any
		frame.removeFromParent();
		label.removeFromParent();
		
		panelTemplateInfoList.add(label);
		label.setText("Preview failed");
		
		isWaitingForReload = false;
		
	}
	private void showPreview(String link) {

		this.link = link;
		// Remove frame and label if any
		if (frame != null ) {
			frame.removeFromParent();
		}
		label.removeFromParent();
		
		frame.setUrl(link);
//		frame.setWidth("200px");
//		frame.setHeight("200px");
		frame.setWidth("100%");
		frame.setHeight("100%");
		panelTemplateInfoList.add(frame);
		isWaitingForReload = false;
	}
	
	public void UpdateUI(){
		
		if (isOpened) {
			if (frame.isAttached() || label.getText().equals("Preview failed")) {
//				System.out.println("updted");
				isWaitingForReload = true;
				stopPagePreview();		
			}
		}
	};
}
