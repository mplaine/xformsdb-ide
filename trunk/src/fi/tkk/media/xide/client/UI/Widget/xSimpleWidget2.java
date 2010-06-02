package fi.tkk.media.xide.client.UI.Widget;

import java.util.Iterator;

import com.google.gwt.dom.client.Document;
//import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetCollection;
import com.google.gwt.xml.client.Node;

public class xSimpleWidget2 extends Panel {

	private  com.google.gwt.dom.client.Element blankDivElement;
	private  Element blankXElement;
	
	private int hierarchyLevel;

	private WidgetCollection children = new WidgetCollection(this);
	  
	protected xSimpleWidget2() {
		this("<div/>");
	}

	public xSimpleWidget2(String xHtml) {
//		System.out.println("xSimpleWidget: html = " + xHtml);
		hierarchyLevel = 0;
		blankDivElement = Document.get().createDivElement();
		blankXElement = xDiv();

		com.google.gwt.dom.client.Element element = xMakeHtml( (xHtml.startsWith("<")?xHtml:
			"<div style='display:inherit;'> "+xHtml+" </div>").replaceAll("\r|\n", "<br/>")); 
//		System.out.println("element: " + element.getInnerHTML());
		setElement(element);
//		System.out.println("this.getElement " + this.getElement().getInnerHTML());

//		setElement(xMakeHtml((xHtml.startsWith("<") ? xHtml
//				: "<div style='display:inherit;'> " + xHtml + " </div>")
//				.replaceAll("\r|\n", "<br/>")));

	}

	public xSimpleWidget2(Element x) {
		blankDivElement = Document.get().createDivElement();
		blankXElement = xDiv();
		setElement(x);
	}

	public  Element xDiv() {

		return blankDivElement.cloneNode(false).cast();
	}

	public com.google.gwt.dom.client.Element xMakeHtml(String x) {
		if (x.contains("<tr") && x.contains("/tr>")) {
			blankXElement.setInnerHTML("<table>" + x + "</table>");
		}
		else if (x.contains("<td") && x.contains("/td>")) {
			blankXElement.setInnerHTML("<table><tr>" + x + "</tr></table>");
		}
		else {
			blankXElement.setInnerHTML(x);
		}
		
		
//		System.out.println("blankX after setting html name " + blankXElement.getNodeName());
//		System.out.println("blankX after setting html  inner" + blankXElement.getInnerHTML());
		
//		 if(xUserAgent.xStandards())//Non IE browsers don't have to forget
			 				// //their reference to the new child
//		 return blankXElement.getFirstChildElement();

		// Get the element created inside our div
		com.google.gwt.dom.client.Element xRet = null;
		
		
//		if (x.contains("table")) {
//			xRet = blankXElement.getFirstChildElement();
//			
//			// Remove <tbody> element
//			if (xRet.hasChildNodes()) {
//				xRet.removeChild(xRet.getFirstChild());
//			}
//			// remove extra line
//			if (xRet.hasChildNodes()) {
//				System.out.println("extra line removed");
//				xRet.removeChild(xRet.getFirstChild());
//			}
//		}
//		else 
			if (x.contains("<tr") && x.contains("/tr>")) {
			xRet = blankXElement.getFirstChildElement().getFirstChildElement().getFirstChildElement();
		}
		else if (x.contains("<td") && x.contains("/td>")) {
			xRet = blankXElement.getFirstChildElement().getFirstChildElement().getFirstChildElement().getFirstChildElement();
		}
		else {
			xRet = blankXElement.getFirstChildElement();
		}
		
		com.google.gwt.dom.client.Element hierarchyTestElement = xRet;
		while((hierarchyTestElement != null) && hierarchyTestElement.hasChildNodes()) {
//			while((hierarchyTestElement != null) && (hierarchyTestElement.hasChildNodes())) {
			hierarchyTestElement = hierarchyTestElement.getFirstChildElement();
			if(((hierarchyTestElement != null) && (hierarchyTestElement.getNodeType() == Node.ELEMENT_NODE))) {
				hierarchyLevel++;
			}
			else {
				break;
			}
		}
//		System.out.println("Final output el name : " + xRet.getNodeName());
		// Reset the div so IE won't lose it's reference the
		// next time the blankXElement is overwritten
		blankXElement = xDiv();
		if (xRet != null) {
//			System.out.println(" inner : " + xRet.getInnerHTML());
		}
		if (xRet == null) {
			xRet = blankXElement;
		}
		return xRet;
	}

	  public Widget getWidget(int index) {
		    return getChildren().get(index);
		  }

		  public int getWidgetCount() {
		    return getChildren().size();
		  }

		  public int getWidgetIndex(Widget child) {
		    return getChildren().indexOf(child);
		  }

		  public Iterator<Widget> iterator() {
		    return getChildren().iterator();
		  }

		  public boolean remove(int index) {
		    return remove(getWidget(index));
		  }

		  @Override
		  public boolean remove(Widget w) {
		    // Validate.
		    if (w.getParent() != this) {
		      return false;
		    }
		    // Orphan.
		    orphan(w);

		    // Physical detach.
		    Element elem = w.getElement();
		    DOM.removeChild(DOM.getParent(elem), elem);

		    // Logical detach.
		    getChildren().remove(w);
		    return true;
		  }
		  
		  /**
		   * Adds a child widget to this panel.
		   * 
		   * @param w the child widget to be added
		   */
		  @Override
		  public void add(Widget w) {
			  if (hierarchyLevel == 0) {
				  this.add(w, getElement());
			  }
			  else {
				  int i = hierarchyLevel;
				  com.google.gwt.dom.client.Element hierElement = getElement();
				  while(i >0) {
					  hierElement = hierElement.getFirstChildElement();
					  i--;
				  }
//				  System.out.println("after hier actions");
				  this.add(w, (Element)hierElement);
			  }
		  }

		  /**
		   * Adds a new child widget to the panel, attaching its Element to the
		   * specified container Element.
		   * 
		   * @param child the child widget to be added
		   * @param container the element within which the child will be contained
		   */
		  protected void add(Widget child, Element container) {
		    // Detach new child.
		    child.removeFromParent();

		    // Logical attach.
		    getChildren().add(child);

		    // Physical attach.
//		    System.out.println("xsingleelement: add");
		    DOM.appendChild(container, child.getElement());

		    // Adopt.
		    adopt(child);
		  }

		  /**
		   * Adjusts beforeIndex to account for the possibility that the given widget is
		   * already a child of this panel.
		   * 
		   * @param child the widget that might be an existing child
		   * @param beforeIndex the index at which it will be added to this panel
		   * @return the modified index
		   */
		  protected int adjustIndex(Widget child, int beforeIndex) {
		    checkIndexBoundsForInsertion(beforeIndex);

		    // Check to see if this widget is already a direct child.
		    if (child.getParent() == this) {
		      // If the Widget's previous position was left of the desired new position
		      // shift the desired position left to reflect the removal
		      int idx = getWidgetIndex(child);
		      if (idx < beforeIndex) {
		        beforeIndex--;
		      }
		    }

		    return beforeIndex;
		  }

		  /**
		   * Checks that <code>index</code> is in the range [0, getWidgetCount()),
		   * which is the valid range on accessible indexes.
		   * 
		   * @param index the index being accessed
		   */
		  protected void checkIndexBoundsForAccess(int index) {
		    if (index < 0 || index >= getWidgetCount()) {
		      throw new IndexOutOfBoundsException();
		    }
		  }

		  /**
		   * Checks that <code>index</code> is in the range [0, getWidgetCount()],
		   * which is the valid range for indexes on an insertion.
		   * 
		   * @param index the index where insertion will occur
		   */
		  protected void checkIndexBoundsForInsertion(int index) {
		    if (index < 0 || index > getWidgetCount()) {
		      throw new IndexOutOfBoundsException();
		    }
		  }

		  /**
		   * Gets the list of children contained in this panel.
		   * 
		   * @return a collection of child widgets
		   */
		  protected WidgetCollection getChildren() {
		    return children;
		  }

		  /**
		   * This method was used by subclasses to insert a new child Widget. It is now
		   * deprecated because it was ambiguous whether the <code>child</code> should
		   * be appended to <code>container</code> element versus inserted into
		   * <code>container</code> at <code>beforeIndex</code>. Use
		   * {@link #insert(Widget, Element, int, boolean)}, which clarifies this
		   * ambiguity.
		   * 
		   * @deprecated Use {@link #insert(Widget, Element, int, boolean)}.
		   */
		  @Deprecated
		  protected void insert(Widget child, Element container, int beforeIndex) {
		    if (container == null) {
		      throw new NullPointerException("container may not be null");
		    }
		    insert(child, container, beforeIndex, false);
		  }

		  /**
		   * Insert a new child Widget into this Panel at a specified index, attaching
		   * its Element to the specified container Element. The child Element will
		   * either be attached to the container at the same index, or simply appended
		   * to the container, depending on the value of <code>domInsert</code>.
		   * 
		   * @param child the child Widget to be added
		   * @param container the Element within which <code>child</code> will be
		   *          contained
		   * @param beforeIndex the index before which <code>child</code> will be
		   *          inserted
		   * @param domInsert if <code>true</code>, insert <code>child</code> into
		   *          <code>container</code> at <code>beforeIndex</code>; otherwise
		   *          append <code>child</code> to the end of <code>container</code>.
		   */
		  protected void insert(Widget child, Element container, int beforeIndex,
		      boolean domInsert) {
		    // Validate index; adjust if the widget is already a child of this panel.
		    beforeIndex = adjustIndex(child, beforeIndex);

		    // Detach new child.
		    child.removeFromParent();

		    // Logical attach.
		    getChildren().insert(child, beforeIndex);

		    // Physical attach.
		    if (domInsert) {
		      DOM.insertChild(container, child.getElement(), beforeIndex);
		    } else {
		      DOM.appendChild(container, child.getElement());
		    }

		    // Adopt.
		    adopt(child);
		  }


	
}
