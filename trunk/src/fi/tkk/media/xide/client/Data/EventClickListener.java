package fi.tkk.media.xide.client.Data;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;

/**
 * Desighed to transfer necessary actions to the widget's event handler methods. 
 * See GoodIcon for example of usage.
 * 
 * @author evgeniasamochadina
 *
 */
public interface EventClickListener {

	/**
	   * Fired when the browser sends event from the object.
	   * 
	   * @param event event sended by browser
	   */
	  void onClick(Event event);
	}
