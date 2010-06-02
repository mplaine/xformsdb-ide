//package fi.tkk.media.xide.client.UI.Widget;
//
//import java.util.Iterator;
//
//import com.google.gwt.dom.client.Document;
////import com.google.gwt.dom.client.Element;
//import com.google.gwt.user.client.Element;
//import com.google.gwt.user.client.DOM;
//import com.google.gwt.user.client.ui.HasWidgets;
//import com.google.gwt.user.client.ui.Widget;
//import com.google.gwt.user.client.ui.WidgetCollection;
//
//public class xSimpleWidget extends Widget implements HasWidgets {
//
//	private static com.google.gwt.dom.client.Element blankDivElement = Document.get().createDivElement();
//	private static Element blankXElement = xDiv();
//
//	protected xSimpleWidget() {
//		this("<div/>");
//	}
//
//	public xSimpleWidget(String xHtml) {
//		setElement(xMakeHtml((xHtml.startsWith("<") ? xHtml
//				: "<div style='display:inherit;'> " + xHtml + " </div>")
//				.replaceAll("\r|\n", "<br/>")));
//
//	}
//
//	public xSimpleWidget(Element x) {
//		setElement(x);
//	}
//
//	public static Element xDiv() {
//		return blankDivElement.cloneNode(false).cast();
//	}
//
//	public static com.google.gwt.dom.client.Element xMakeHtml(String x) {
//		blankXElement.setInnerHTML(x);
//		// if(xUserAgent.xStandards())//Non IE browsers don't have to forget
//		// //their reference to the new child
//		// return blankXElement.getFirstChildElement();
//
//		// Get the element created inside our div
//		com.google.gwt.dom.client.Element xRet = blankXElement.getFirstChildElement();
//
//		// Reset the div so IE won't lose it's reference the
//		// next time the blankXElement is overwritten
//		blankXElement = xDiv();
//		return xRet;
//	}
//	
//
//	  private WidgetCollection children = new WidgetCollection(this);
//
//	  public Widget getWidget(int index) {
//	    return getChildren().get(index);
//	  }
//
//	  public int getWidgetCount() {
//	    return getChildren().size();
//	  }
//
//	  public int getWidgetIndex(Widget child) {
//	    return getChildren().indexOf(child);
//	  }
//
//	  public Iterator<Widget> iterator() {
//	    return getChildren().iterator();
//	  }
//
//	  public boolean remove(int index) {
//	    return remove(getWidget(index));
//	  }
//
//	  public boolean remove(Widget w) {
//	    // Validate.
//	    if (w.getParent() != this) {
//	      return false;
//	    }
//	    // Orphan.
//	    orphan(w);
//
//	    // Physical detach.
//	    Element elem = w.getElement();
//	    DOM.removeChild(DOM.getParent(elem), elem);
//
//	    // Logical detach.
//	    getChildren().remove(w);
//	    return true;
//	  }
//
//	  /**
//	   * Adds a new child widget to the panel, attaching its Element to the
//	   * specified container Element.
//	   * 
//	   * @param child the child widget to be added
//	   * @param container the element within which the child will be contained
//	   */
//	  protected void add(Widget child, Element container) {
//	    // Detach new child.
//	    child.removeFromParent();
//
//	    // Logical attach.
//	    getChildren().add(child);
//
//	    // Physical attach.
//	    DOM.appendChild(container, child.getElement());
//
//	    // Adopt.
//	    adopt(child);
//	  }
//
//	  /**
//	   * Adjusts beforeIndex to account for the possibility that the given widget is
//	   * already a child of this panel.
//	   * 
//	   * @param child the widget that might be an existing child
//	   * @param beforeIndex the index at which it will be added to this panel
//	   * @return the modified index
//	   */
//	  protected int adjustIndex(Widget child, int beforeIndex) {
//	    checkIndexBoundsForInsertion(beforeIndex);
//
//	    // Check to see if this widget is already a direct child.
//	    if (child.getParent() == this) {
//	      // If the Widget's previous position was left of the desired new position
//	      // shift the desired position left to reflect the removal
//	      int idx = getWidgetIndex(child);
//	      if (idx < beforeIndex) {
//	        beforeIndex--;
//	      }
//	    }
//
//	    return beforeIndex;
//	  }
//
//	  /**
//	   * Checks that <code>index</code> is in the range [0, getWidgetCount()),
//	   * which is the valid range on accessible indexes.
//	   * 
//	   * @param index the index being accessed
//	   */
//	  protected void checkIndexBoundsForAccess(int index) {
//	    if (index < 0 || index >= getWidgetCount()) {
//	      throw new IndexOutOfBoundsException();
//	    }
//	  }
//
//	  /**
//	   * Checks that <code>index</code> is in the range [0, getWidgetCount()],
//	   * which is the valid range for indexes on an insertion.
//	   * 
//	   * @param index the index where insertion will occur
//	   */
//	  protected void checkIndexBoundsForInsertion(int index) {
//	    if (index < 0 || index > getWidgetCount()) {
//	      throw new IndexOutOfBoundsException();
//	    }
//	  }
//
//	  /**
//	   * Gets the list of children contained in this panel.
//	   * 
//	   * @return a collection of child widgets
//	   */
//	  protected WidgetCollection getChildren() {
//	    return children;
//	  }
//
//	  /**
//	   * This method was used by subclasses to insert a new child Widget. It is now
//	   * deprecated because it was ambiguous whether the <code>child</code> should
//	   * be appended to <code>container</code> element versus inserted into
//	   * <code>container</code> at <code>beforeIndex</code>. Use
//	   * {@link #insert(Widget, Element, int, boolean)}, which clarifies this
//	   * ambiguity.
//	   * 
//	   * @deprecated Use {@link #insert(Widget, Element, int, boolean)}.
//	   */
//	  @Deprecated
//	  protected void insert(Widget child, Element container, int beforeIndex) {
//	    if (container == null) {
//	      throw new NullPointerException("container may not be null");
//	    }
//	    insert(child, container, beforeIndex, false);
//	  }
//
//	  /**
//	   * Insert a new child Widget into this Panel at a specified index, attaching
//	   * its Element to the specified container Element. The child Element will
//	   * either be attached to the container at the same index, or simply appended
//	   * to the container, depending on the value of <code>domInsert</code>.
//	   * 
//	   * @param child the child Widget to be added
//	   * @param container the Element within which <code>child</code> will be
//	   *          contained
//	   * @param beforeIndex the index before which <code>child</code> will be
//	   *          inserted
//	   * @param domInsert if <code>true</code>, insert <code>child</code> into
//	   *          <code>container</code> at <code>beforeIndex</code>; otherwise
//	   *          append <code>child</code> to the end of <code>container</code>.
//	   */
//	  protected void insert(Widget child, Element container, int beforeIndex,
//	      boolean domInsert) {
//	    // Validate index; adjust if the widget is already a child of this panel.
//	    beforeIndex = adjustIndex(child, beforeIndex);
//
//	    // Detach new child.
//	    child.removeFromParent();
//
//	    // Logical attach.
//	    getChildren().insert(child, beforeIndex);
//
//	    // Physical attach.
//	    if (domInsert) {
//	      DOM.insertChild(container, child.getElement(), beforeIndex);
//	    } else {
//	      DOM.appendChild(container, child.getElement());
//	    }
//
//	    // Adopt.
//	    adopt(child);
//	  }
//
//	  public void add(Widget child) {
//		    throw new UnsupportedOperationException(
//		        "This panel does not support no-arg add()");
//		  }
//
//		  public void clear() {
//		    Iterator<Widget> it = iterator();
//		    while (it.hasNext()) {
//		      it.next();
//		      it.remove();
//		    }
//		  }
//		  
//		  protected final void orphan(Widget child) {
//			    assert (child.getParent() == this);
//			    child.setParent(null);
//			  }
//
//}
