package fi.tkk.media.xide.client.utils;

/**
 * Different constants are defined in this class
 * 
 * @author evgeniasamochadina
 *
 */
public class Cons {

	// A delay period between a moment when an update comes from user and Design Tab update starts. Is made to start redraw when user stops typing
	public static final int UI_UPDATE_DELAY_PERIOD_MS = 1;
	// A cookie age. Describes how long user can do nothing with the XIDE while it is logged out
	// is set in seconds
	public static final int COOKIE_LIVE_TIME = 60 * 10;
	
	/**
	 *  Indicates if text highlight is used in the tabs showing files
	 */
	public static final boolean IS_TEXT_HIGHLIGHT_USED = true;
	
	
	// Welcome strings and others
	public static final String ERROR_POPUP_HEADER = "XIDE Error message";
	public static final String ERROR_POPUP_SERVER_MESSAGE_LABEL = "For more information here is an error from server:";
	
}
