package fi.tkk.media.xide.client.utils;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
/**
 * Simple panel which contains 2 widgets horizontally. Width of whole panel and of each widget can be adjusted when panel is initialized. 
 * Each widget can be horizontally positioned either on left, center or right when panel is initialized.
 * @author evgeniasamochadina
 *
 */
public class GoodHorizontalAdjustablePanel extends HTMLPanel {
	// Posiitons
	static final String LEFT = "left";
	static final String RIGHT = "right";
	static final String CENTER = "center";
	
	static final String idLeft = "design-left-place-";
	static final String idRight = "design-right-place-";
	// Absolute conter used for making unique HTML element IDs for each instance of the panel
	static int counter = 0;
	// "Number" of panel. Identifies unique id of the left and right widgets
	int counterValue;
	
	private Widget leftWidget, rightWidget;

	/**
	 * Creates a panel with 2 widgets placed on the left and on the right by default. Do not require alignment settings from the user.
	 * @param leftW left widget
	 * @param rightW right widget
	 */
	public GoodHorizontalAdjustablePanel(Widget leftW, Widget rightW) {
		this("left", "right", 100, 50);
		leftWidget = leftW;
		rightWidget = rightW;
		add(leftW, idLeft  + (counterValue));
		add(rightW, idRight + (counterValue));
	}

	/**
	 * Creates a panel with 2 widgets and position them according to user's demand. 
	 * @param leftW left widget
	 * @param leftAligh alignment for the left  widget
	 * @param rightW right widget
	 * @param rightAlign alilgnment for the right widget
	 */
	public GoodHorizontalAdjustablePanel(Widget leftW, String leftAligh, Widget rightW, String rightAlign) {
		this(leftAligh, rightAlign, 98, 40);
		leftWidget = leftW;
		rightWidget = rightW;
		add(leftW, idLeft  + (counterValue));
		add(rightW, idRight + (counterValue));
	}
	
	/**
	 * Creates panel with default width settings and alignment settings according to user's demand
	 * @param leftAlign
	 * @param rightAlign
	 */
	public GoodHorizontalAdjustablePanel(String leftAlign, String rightAlign) {
		this(leftAlign, rightAlign, 100, 50);
	}
	
	/**
	 * Default constructor for the panel (Each constructor should run it at the beginning)
	 * Initialize panel with width and alignment settings for left and right widgets. These settings could not be changed afterwards.
	 * @param leftAlign alignment for left widget
	 * @param rightAlign alignment for right widget
	 * @param width width value (%) for the whole panel 
	 * @param leftWidth width value (%) for the left widget. Width for right widget = (100 - leftWidth) 
	 */
	public GoodHorizontalAdjustablePanel(String leftAlign, String rightAlign, int width, int leftWidth) {
		super("<table width = \" " +width +"%\">" + "<tr>" + " <td width = \""+ leftWidth +"%\" align = \""
				+ leftAlign + "\"><div id = \"" + idLeft  + counter +"\"></div> </td>"
				+ " <td width = \""+ (100 - leftWidth)+"%\" align = \"" + rightAlign
				+ "\"><div id = \"" + idRight + counter +"\"></div></td>" + " </tr>"
				+ "</table>");
		counterValue = counter;
		counter++;
		
	}
	
	/*
	 * Set left widget. If there is left widget on a panel removes it before placing new one.
	 */
	public void setLeftWidget(Widget w) {
		String id = "design-left-place" + "-" + counterValue;
		if (leftWidget != null) {
			remove(leftWidget);
		}
		leftWidget = w;
		add(leftWidget, id);
	}
	/**
	 * Set right widget. If there is right widget on a panel removes it before placing new one.
	 * @param w
	 */
	public void setRightWidget(Widget w) {
		String id = "design-right-place" + "-" + counterValue;
		if (rightWidget != null) {
			remove(rightWidget);
		}
		rightWidget = w;
		add(rightWidget, id);
	}
	
}
