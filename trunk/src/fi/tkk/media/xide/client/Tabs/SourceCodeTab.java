/**
 * 
 */
package fi.tkk.media.xide.client.Tabs;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.View;
import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.Data.Selectable;
import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.UI.Widget.BasicPageElement;
import fi.tkk.media.xide.client.UI.Widget.TemplateShortInfoPanel;

public class SourceCodeTab extends FileTab {
	
	public final int tabID = View.SOURSECODE;
	
	// Stores current instance of the tab
	static FileTab instance;
	public boolean newTextHasJustSet = false;

	public SourceCodeTab() {
		super();
		instance = this;
		// Initialize global jJavaScript variable to store onChange method
		initizlizeJSLink();
	}
	public native void runScript() /*-{
	$wnd.editor_source = $wnd.CodeMirror.fromTextArea(
	this.@fi.tkk.media.xide.client.Tabs.FileTab::idTab, {
    				parserfile: "parsexml.js",
    				stylesheet: "css/xmlcolors.css",
				    path: "js/",
				    onChange: function (n) {$wnd.sourceCodeOnChange();},
				    lineNumbers: true,
				    continuousScanning: 500,
				    tabMode: "shift"
 			 });
 	
}-*/;
	
	/**
	 * Method to be called when highlighted text are detects the text area value has changed - onChange event comes (both by typing the text or
	 * by setting new value externally)
	 */
	public static void processOnChangeEvent() {
		// If a text was set externally 
		if (instance.newTextHasJustSet) {
			// Do not process event
			
			// Remove flag
			instance.newTextHasJustSet = false;
		} else {
			// Set element changed
			instance.setElementChanged();
		}
	}
	 
	 /**
	  * Initialize global jJavaScript variable (for this tab type) to store onChange method
	  * When text has changed, processOnChangeEvent static Java method is called via the variable 
	  */
	 public static native void initizlizeJSLink() /*-{
			$wnd.sourceCodeOnChange =
			@fi.tkk.media.xide.client.Tabs.SourceCodeTab::processOnChangeEvent();
		}-*/;

	public native void setNewText(String text)/*-{
	if ($wnd.editor_source != null){
		$wnd.editor_source.setCode(text);
	}
}-*/;
	
	public native String getChangedText() 
	/*-{
	if ($wnd.editor_source != null){
		return $wnd.editor_source.getCode();
		}
		else{
		return null;
		}
	}-*/;
	
	@Override
	public void updateWhenClicked() {
//		System.out.println("on click " + this.textAreaOfFileText.getOffsetHeight());
		files = Main.getInstance().getSelectedElement().getProperties().getSourceCode();
	}

	@Override
	public void getFiles() {
		Selectable selectedElement = Main.getInstance().getSelectedElement();

		if(selectedElement != null) {
			if (selectedElement.getProperties().getSourceCode() != null) {
				addFiles(selectedElement.getProperties().getSourceCode());
			}
		}
	}
	

}
