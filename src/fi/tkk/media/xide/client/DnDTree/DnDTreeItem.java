package fi.tkk.media.xide.client.DnDTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.Data.SaveObjectsListener;
import fi.tkk.media.xide.client.Data.Selectable;
import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.UI.Widget.BasicPageElement;
import fi.tkk.media.xide.client.UI.Widget.Component;
import fi.tkk.media.xide.client.UI.Widget.Slot;
import fi.tkk.media.xide.client.UI.Widget.WebPage;

public class DnDTreeItem extends TreeItem implements Selectable{

	public static final int COMPONENT = 1;
	public static final int SLOT = 2;
	public static final int QUERY = 3;
	public static final int DI = 4;
	public static final int WPAGE = 5;

	public static DnDTreeItem selectedItem ;
	// Focus panel which is first-layer panel of the item
	// used for DND
	private FocusPanel focus;
	// Type of the element which is represented by the item
	private int type;
	// Widget which represents the item
	// Now it's a label
	public Panel content;
	
	// Label text
	private HTML text;
	
	// Corresponding page element
	private BasicPageElement pageElement; 
	
	public BasicPageElement getPageElement() {
		return pageElement;
	}

	public DnDTreeItem(){
		super();
	}
	
	public String getTypeName() {
		return pageElement.getTypeName();
	}

//	public DnDTreeItem(String html){
//		this(new Label(html));
//		resize(html.length());
//	}

	public DnDTreeItem(String html, int type, BasicPageElement pageElement){
		super();
		init(new HTML(html), pageElement);
		this.setType(type);
		resize(html.length());
		this.pageElement = pageElement;
	}

//	/**
//	   * Adds a child tree item containing the specified widget.
//	   * 
//	   * @param widget the widget to be added
//	   * @return the item that was added
//	   */
//	  public DnDTreeItem addItem(Widget widget, BasicPageElement pageElement) {
//	    DnDTreeItem ret = new DnDTreeItem(widget, pageElement);
//	    addItem(ret);
//	    return ret;
//	  }
	  
	public DnDTreeItem( BasicPageElement pageElement) {
		super();
		
		String elementTitle = pageElement.getTemplate().getProperties().get(Property.TITLE).getStringValue();
		String type = pageElement.getTypeName();
		
		HTML label = new HTML(elementTitle + "<i style=\"font-size:70%; padding: 0px 3px 0px 3px;\"> (" + type + ") </i>", true);
		
		init(label, pageElement);
	}
	
	public void updateItemText() {
		String elementTitle = pageElement.getTemplate().getProperties().get(Property.TITLE).getStringValue();
		String type = pageElement.getTypeName();
		
		HTML label = new HTML(elementTitle + "<i style=\"font-size:70%; padding: 0px 3px 0px 3px;\"> (" + type + ") </i>", true);

		text.removeFromParent();
		text = label;
		content.add(label);
		
	}
	
	private void init(HTML widget, BasicPageElement pageElement) {
		this.pageElement = pageElement;
		text = widget;
		// Set a link to the corresponding tree item to the element
		pageElement.setTreeItem(this);
		
		Template t = pageElement.getTemplate();
//		// TODO: add normal check on type
//		if (title.contains("omp")) {
//			this.type = COMPONENT;
//		}
//		else {
//			this.type = SLOT;
//		}
		if (pageElement instanceof WebPage) {
			this.type = WPAGE;
			
		}
		else if (pageElement instanceof Slot) {
			this.type = SLOT;
		}

		else if (pageElement instanceof Component) {
			this.type = COMPONENT;
		}
		
		HorizontalPanel panel = new HorizontalPanel();
		panel.add(pageElement.getImage());
		DOM.setStyleAttribute(panel.getWidget(0).getElement(), "paddingRight", "2px");
		
		panel.add(widget);
		content = panel;
		// Style
		content.setStyleName("tree-item");
		
		focus = new FocusPanel(content);
		focus.getElement().setAttribute("id", "tree-item-id");
		focus.setHeight("22px");
//		focus.setPixelSize(200, 20);
		focus.setTabIndex(-1);
		setWidget(focus);
	}
	
	public DnDTreeItem getChild(int index) {
		return (DnDTreeItem)super.getChild(index);
	}

	public void setIndex(long index) {
		focus.setTitle(index+"");
	}

	public void addMouseListener(MouseListener mouseListener){
		focus.addMouseListener(mouseListener);
	}

	public void removeMouseListener(MouseListener mouseListener){
		focus.removeMouseListener(mouseListener);
	}

	public String toString(){
		String newText;
		
		if (text != null) newText =  "DnDTreeItem: "+focus.getElement().toString()+" "+text.getText();
		
		else newText =  "DnDTreeItem: "+focus.getElement().toString();
		return newText;
	}

	public void resize(int width){
		//focus.setPixelSize(width*10, 20);
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public Widget getContent() {
		return text;
	}

	public ArrayList<Selectable> GetLinkedObjects() {
		ArrayList<Selectable> list = new ArrayList<Selectable>();
		list.add(pageElement);
		return list;
	}

	public Template getProperties() {
		return pageElement.getTemplate();
	}
	
	public Selectable getValuableElement() {
		return pageElement;
	}

/**
 * Gets set of parameters of the representing page element. If an element 
 * has a parameter list (Components and WebPages do), this list should be displayed instead of the 
 * template's paramters
 * @return
 */
	public HashMap<String, Property> getParameters(){
		if (pageElement instanceof Component) {
			return ((Component) pageElement).getParameters();
		}
		else {
			return pageElement.getProperties().getParameters();
		}
	}
	public void Select() {
		// Item is not selected. Just select it
		selectedItem  = this;
		this.changeStyle(SELECTED);

			//this.content = new Label("www");
			//this.getTree().setSelectedItem(this);
		
	}
	
	public void Unselect() {
		
		selectedItem = null;
		this.changeStyle(RESET);
//		this.getTree().setSelectedItem(null);
	}

		public void Changed() {
			// If it's the first initiation of the 
			if (!pageElement.isChanged()) {
				Main.getInstance().setElementChanged(pageElement);
				changeStyle(CHANGED);
			}
		}

		public void Saved() {
			if (pageElement.isChanged()) {
				Main.getInstance().setElementSaved(pageElement);
				changeStyle(RESET);
				
			}
		}

		public void Saved(SaveObjectsListener listener) {
			
		}
		
		public void afterSaved() {
			// TODO: manage saved and aftersaved
		}


		public boolean isChanged() {
			// TODO Auto-generated method stub
			return pageElement.isChanged();
		}

		public void Canceled() {
			if (pageElement.isChanged()) {
//				String text = ((Label) content).getText();
//				((Label) content).setText(text.substring(0, text.length()-1));
				Main.getInstance().setElementCanceled(pageElement);
				changeStyle(RESET);
			}			
		}

		boolean isStarred = false;
		
		private void addStar() {
			if (!isStarred) {
				text.setHTML(text.getHTML() + "*");
				isStarred = true;
			}
		}
		
		private void removeStar() {
			if (isStarred) {
				text.setHTML(text.getHTML().substring(0, text.getHTML().length()-1));
				isStarred = false;
			}
		}
		public void changeStyle(int event) {
			if ((event&Selectable.CHANGED) >0) {
				addStar();
				for (Iterator<Selectable> iterator = GetLinkedObjects().iterator(); iterator.hasNext();) {
					iterator.next().changeStyle(Selectable.CHANGED_LINKED);
				}
			}
			else if ((event&Selectable.CHANGED_LINKED) >0) {
				addStar();
			}
			else if ((event&Selectable.SELECTED) >0) {
				this.content.addStyleDependentName("selected");
				for (Iterator<Selectable> iterator = GetLinkedObjects().iterator(); iterator.hasNext();) {
					iterator.next().changeStyle(Selectable.SELECTED_LINKED);
				}
			}
			else if ((event&Selectable.SELECTED_LINKED) >0) {
				getWidget().addStyleDependentName("selected-linked");
			}
			else if ((event&Selectable.RESET) >0) {
				this.content.removeStyleDependentName("selected");
				getWidget().removeStyleDependentName("selected-linked");
				if (!isChanged()) {
					removeStar();
				}
				if (Main.getInstance().getSelectedElement()== this) {
					for (Iterator<Selectable> iterator = GetLinkedObjects().iterator(); iterator.hasNext();) {
						iterator.next().changeStyle(Selectable.RESET);
					}
				}
			}
		}

		public void Deleted() {
			// TODO Auto-generated method stub
			pageElement.Deleted();
		}

}