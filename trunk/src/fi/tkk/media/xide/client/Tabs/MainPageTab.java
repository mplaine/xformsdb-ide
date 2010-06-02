/**
 * 
 */
package fi.tkk.media.xide.client.Tabs;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.popups.utils.interfaces.Action;

/**
 * Represents main XIDE page (iframe)
 * 
 * @author Evgenia Samochadina
 * @date Feb 16, 2010
 *
 */
public class MainPageTab extends Tab {
	Widget mainHomePage;
	
	public MainPageTab() {
		HTMLPanel all = new HTMLPanel(
				"<table cellspacing=\"0\" cellpadding=\"0\" style=\"width: 100%; height: 100%;\">"+
				"<tbody>"+
				"	<tr>"+
				"		<td align=\"left\" style=\"vertical-align: top;\">"+
				"			<div style=\"overflow: hidden; position: relative; height: 85px\">"+
				"				<h3 style=\"font-size:165%; padding-left:60px;\"> If you are ready you can <div style=\"display: inline;\" id=\"start-link-id\"> </div>"+ 
				"				</h3>"+
				"			</div>"+
				"		</td>"+
				"	</tr>"+
				"	<tr>"+
				"		<td align=\"left\" style=\"vertical-align: top;\" height=\"100%\">"+
				"			<div style=\"height:100%; overflow:hidden;\">"+
				"				<div style=\"width: 100%; height: 100%; position: relative;\">"+
				"					  <div style=\"overflow: hidden; position: absolute; width: 100%; height: 100%;background-color: #eaeaea;\">"+
				"						   <div style=\"overflow-y: auto; height: 100%;\">"+
				"						      <div id = \"frame-id\">"+
				"							  </div>"+
				"					   	</div>"+
				"				  	</div>"+
				"				</div>"+
				"			</div>"+
				"		</td>"+
				"	</tr>"+
				"</tbody>"+
				"</table>"
			
				);
		
//		rootPanel.add(all);
		mainHomePage = all;
		this.add(mainHomePage);
		
		Widget l = Main.getInstance().createTempNewLink("Start the XIDE", new Action() {

			public void doAction() {
				// Gets username and load AP 
				Main.getInstance().onEnterToXIDE(true);
				
			}}, false);
		DOM.setStyleAttribute(l.getElement(), "fontSize", "130%");
		DOM.setStyleAttribute(l.getElement(), "display", "inline");
		DOM.setStyleAttribute(l.getElement(), "float", "none");
		l.removeStyleName("header-right-element");
		all.add(l, "start-link-id");

		HTML frame = new HTML("<div class=\"central-1\"><iframe class=\"border\" src=\"login.html\" width=\"100%\" height=\"100%\""+
		"frameborder=\"0\"></iframe></div>");
		all.add(frame, "frame-id");
	}
	
	
}
