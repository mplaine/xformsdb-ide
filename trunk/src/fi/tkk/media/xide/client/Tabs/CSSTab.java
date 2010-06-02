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

public class CSSTab extends FileTab {
	public final int tabID = View.CSS;
	
	// Stores current instance of the tab
	static FileTab instance;
	public boolean newTextHasJustSet = false;

	public CSSTab() {
		super();
		instance = this;
		// Initialize global jJavaScript variable to store onChange method
		initizlizeJSLink();
	}

	public static void processOnChangeEvent() {
		 if (instance.newTextHasJustSet) {
			 instance.newTextHasJustSet = false;
		 }
		 else{
			 instance.setElementChanged();
		 }
	}

	/**
	 * Initializes global jJavaScript variable (for this tab type) to store
	 * onChange method When text has changed, processOnChangeEvent static Java
	 * method is called via the variable
	 */
	public static native void initizlizeJSLink() /*-{
			$wnd.CSSOnChange =
			@fi.tkk.media.xide.client.Tabs.CSSTab::processOnChangeEvent();
		}-*/;

	/**
	 * Initializes highlight editor object with new text and start highlighing. 
	 * Specific for each file tab (different parsers, csss, onChange function)
	 */
	public native void runScript() /*-{
		
		$wnd.editor_css = $wnd.CodeMirror.fromTextArea(
		this.@fi.tkk.media.xide.client.Tabs.FileTab::idTab, {
					    height: "100%",
					    width: "100%",
	    				parserfile: "parsecss.js",
	    				stylesheet: "css/csscolors.css",
					    path: "js/",
					    lineNumbers: true,
					    continuousScanning: 500,
					    onChange: function (n) {$wnd.CSSOnChange();},
					    tabMode: "shift"
	 			 });
	}-*/;

	public native void setNewText(String text)/*-{
	if ($wnd.editor_css != null){
		$wnd.editor_css.setCode(text);
	}
	}-*/;

	public native String getChangedText()
	/*-{
	 if ($wnd.editor_css != null){
	 return $wnd.editor_css.getCode();
	 }
	 else{
	 return null;
	 }
	 }-*/;

	@Override
	public void updateWhenClicked() {
		files = Main.getInstance().getSelectedElement().getProperties()
				.getCSS();

	}

	@Override
	public void getFiles() {
		Selectable selectedElement = Main.getInstance().getSelectedElement();

		if (selectedElement != null) {
			if (selectedElement.getProperties().getCSS() != null) {
				addFiles(selectedElement.getProperties().getCSS());
			}
		}
	}
}
