/**
 * 
 */
package fi.tkk.media.xide.client.UI.Widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.SliderBar;

import fi.tkk.media.xide.client.DnDController;
import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.Data.AccessRights;
import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.Data.SaveObjectsListener;
import fi.tkk.media.xide.client.Data.Selectable;
import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.DnDTree.DnDTreeItem;
import fi.tkk.media.xide.client.Server.RPC.SearchService;
import fi.tkk.media.xide.client.Tabs.NavigatorTab;
import fi.tkk.media.xide.client.Tabs.PLTab;
import fi.tkk.media.xide.client.Tabs.PLTreeItem;
import fi.tkk.media.xide.client.Tabs.PropertiesTab;
import fi.tkk.media.xide.client.parsing.MyXMLParser;
import fi.tkk.media.xide.client.parsing.MyXMLParser.ComponentCallXMLInfo;
import fi.tkk.media.xide.client.parsing.MyXMLParser.SlotXMLInfo;
import fi.tkk.media.xide.client.popups.basic.Popup;
import fi.tkk.media.xide.client.popups.basic.PopupError;
import fi.tkk.media.xide.client.utils.ConnectionToServer;
import fi.tkk.media.xide.client.utils.Icons;
import fi.tkk.media.xide.client.utils.ConnectionToServer.CallbackActions;

/**
 * @author Evgenia Samochadina
 * @date Dec 8, 2008
 * 
 */
public class Component extends FocusPanel implements BasicPageElement {
	public static final int ID = 0;
	public static final int TITLE = 1;
	public static final int DESCRIPTION = 2;
	public static final int TAGS = 3;
	public static final int AUTHOR = 4;
	public static final int DATE_CREATED = 5;
	public static final int DATE_MODIFIED = 6;
	public static final int DATE_PUBLISHED = 7;
	public static final int IS_PUBLIC = 8;
	// public static final int TYPE = 9;

	ArrayList<Integer> accessRightsSettings;

	public static final boolean switchComponent = false;
	// public static final String[] IDs = {"component_0_id",
	// "component_1_id",
	// "component_2_id",
	// "component_3_id",
	// "component_4_id",
	// "component_5_id",
	// "component_6_id",
	// "component_7_id"
	// };

	public Template template;
	BasicPageElement parent;
	Template templateBackup;
	// Illustrates whether this component now has modified properties or other parts which are not saved
	boolean isChanged;

	// Illustrates whether this component has saved modifications which have never been rendered on Design tab
	public boolean hasChanged;

	public HashMap<String, Property> parameters;

	static int radioButtonGroups;// = 0;

	ArrayList<BasicPageElement> children;
	protected HTMLPanel panel;
	private DnDTreeItem treeItem;

	private PLTreeItem treeItemPL;
	String htmlPanelString;

	transient ConnectionToServer connectionToServer;

	// Dragging counter identifying how many times did the onBrowserEvent(click) called
	static public int i = 0;

	/**
	 * @param template
	 * @param parent
	 */
	public Component(Template template, Slot parent) {
		// super(template, parent);
		this.template = template;
		this.template.MakeEditable(true);

		this.children = new ArrayList<BasicPageElement>();
		this.isChanged = false;
		this.hasChanged = true;
		setParentElement(parent);

		// Clone template parameters
		// Clone parameters hash map
		this.parameters = new HashMap<String, Property>();
		if (template.parameters != null) {
			Set<String> keys = template.parameters.keySet();

			for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
				String o = iterator.next();
				this.parameters.put(o, template.parameters.get(o).clone());
				this.parameters.get(o).setEditableNow(true);
			}
		}

		htmlPanelString = "MCP_" + template.getProperties().get(Property.ID).getStringValue() + "_" + i;
		i++;
		panel = new HTMLPanel("<div id = \"" + htmlPanelString + "\" </div>");
		panel.addStyleName("design-Component");
		panel.getElement().setAttribute("id", "component-id");
		panel.setWidth("");
		this.add(panel);

//		this.sinkEvents(Event.ONDBLCLICK | Event.ONCLICK);

	}

	public void onBrowserEvent(Event event) {
//		System.out.println("enevt");
		if ((DOM.eventGetType(event) == Event.ONMOUSEDOWN && DOM.eventGetButton(event) == Event.BUTTON_LEFT)) {
				Selectable selectedElement = Main.getInstance().getSelectedElement();

				if (selectedElement != this) {
					Main.getInstance().setSelectedElement(this);
					event.stopPropagation();
					super.onBrowserEvent(event);
				} else {
//					event.stopPropagation();
					super.onBrowserEvent(event);
				}
				if (DOM.eventGetButton(event) == Event.BUTTON_RIGHT) {
				}
		}
	}

	//
	// public void setTreeItem(DnDTreeItem treeItem) {
	// this.treeItem = treeItem;
	// }
	public String getXMLCode() {
		String result = null;
		// <template:call-component name="tpl_news_widget">
		// <template:with-param name="pRSSFeedURL" select="$pRSSFeedURL" />
		// </template:call-component>
		result = "<template:call-component name=\"" + template.properties.get(Property.ID).getStringValue() + "\"> \n";
		for (Iterator<String> iterator = parameters.keySet().iterator(); iterator.hasNext();) {
			String paramName = iterator.next();
			// if (parameters.get(paramName).getValue() != null && !parameters.get(paramName).getValue().equals("")) {
			if (parameters.get(paramName).getValue() != null) {
				result += "<template:with-param " + "name" + "=\"" + paramName + "\"" + ">" + parameters.get(paramName).getStringValue()
						+ "</template:with-param>" + "\n";
			}
		}

		result += "</template:call-component> \n";
		return result;
	}

	public void updateSlotInfo() {
		for (Iterator<BasicPageElement> iterator = children.iterator(); iterator.hasNext();) {
			BasicPageElement o = iterator.next();
			if (o instanceof Slot) {
				Slot slot = (Slot) o;
				slot.updateSlotInfo();
			}
		}

	}

	public void setParameterValue(String paramName, String value) {
		if (parameters != null && parameters.containsKey(paramName) == true) {
			parameters.get(paramName).setValue(value);
		}
	}

	public void Draw(boolean isComponentProxy) {
		// removeDropTarget();
		parseCode(isComponentProxy);
	}

	public void Draw() {
		Draw(false);
	}

	public void SaveAsNewComponent() {
		// TODO SaveAsNewComponent

	}

	/**
	 * Search for slots which are children of this component and initiate their recursive removing from drop target list
	 */
	public void removeDropTarget() {
		for (Iterator<BasicPageElement> iterator = children.iterator(); iterator.hasNext();) {
			BasicPageElement o = iterator.next();
			if (o instanceof Slot) {
				Slot slot = (Slot) o;
				slot.removeDropTarget();
			}
		}
	}

	public void RemoveChildren() {
		// children.clear();
		for (Iterator<BasicPageElement> iterator = children.iterator(); iterator.hasNext();) {
			Slot o = (Slot) iterator.next();
			// o.removeFromParent();
		}

	}

	public void DrawChildren() {
		DrawChildren(false);
	}

	public void DrawChildren(boolean isProxy) {
		for (Iterator<BasicPageElement> iterator = children.iterator(); iterator.hasNext();) {
			BasicPageElement o = iterator.next();
			o.Draw(isProxy);
			// if (o instanceof Slot) {
			//				
			// }
		}
	}

	private Panel getEmptyComponent() {
		VerticalPanel panel = new VerticalPanel();
		panel.add(new Label("Sorry the component is not avaliable"));
		return panel;
	}

	/**
	 * Substitutes values of the parameters into source code of the component. If parameter has no value provided by the
	 * template:call, its default value is taken.
	 * 
	 * @param string
	 *            source code
	 * @return source code with parameter values
	 */
	public String substitudeParamValues(String string) {
		String result = string;
		// Iterate on each parameter which is defined in the component template
		for (Iterator<String> iterator = template.parameters.keySet().iterator(); iterator.hasNext();) {
			// Parameter name (should include $ sign)
			String paramName = iterator.next();
			// Parameter value
			String paramValue = null;
			// If there is parameter value provided by the template call
			if (parameters.containsKey(paramName)) {
				// take this value
				paramValue = parameters.get(paramName).getStringValue();
			} else {
				paramValue = template.parameters.get(paramName).getStringValue();
			}
			// if (paramValue.startsWith("'") && paramValue.endsWith("'")) {
			// // Trim starting and ending ' which are made for xformsdb process
			// paramValue = paramValue.substring(1, paramValue.length()-1);
			// }
			// If parameter's type is text or html then it needs to be wrappered in '' quotations
			// if (parameters.get(paramName).getType().equals("text") ||
			// parameters.get(paramName).getType().equals("html")) {
			// paramValue = "'" + paramValue + "'";
			// }
			result = result.replace("$" + paramName, paramValue);
		}

		return result;
	}

	// TODO: move to parser!
	public void parseCode(boolean isComponentProxy) {
		// System.out.println("Draw component " + template.properties.get(Property.ID).getStringValue() +
		// " , has changed =  " + hasChanged);
		String style = panel.getStyleName();

		if (hasChanged) {
			if (!isChanged) {
				hasChanged = false;
			}
			// panel init
			this.remove(panel);

			panel = new HTMLPanel("<div id = \"" + htmlPanelString + "\" </div>");
			this.add(panel);

			// Set style
			panel.addStyleName(style);
			panel.setWidth("");

			// Check if component is not added to the Navigator:

			if (treeItem == null && this instanceof WebPage) {
				// if (parent != null) {
				// NavigatorTab.getInstance().addElement(parent.getTreeItem(), this, 0 );
				// }
				// else {
				NavigatorTab.getInstance().addElement(null, this, 0);
				// }
			}
			// TODO: read source code normally
			if (template.getSourceCode().size() == 0) {
				// No source for template
				panel.add(getEmptyComponent(), htmlPanelString);
			} else {

				String initialSourceCode = template.getSourceCodeFirstFile().getContent();
				String sourceCodeWithParamValues = substitudeParamValues(initialSourceCode);
				MyXMLParser xm = null;

				/**
				 * TRY
				 */
//				try {
					xm = new MyXMLParser(sourceCodeWithParamValues);
					Widget w = xm.rootElement.draw();

					panel.add(w, htmlPanelString);

					// }

					// new version
					ArrayList<SlotXMLInfo> slotListFromParser = xm.getSlotInfoList();
					boolean isWaitingForTemplates = false;

					// For each child slot object that was created previously
					for (Iterator<BasicPageElement> iterator = children.iterator(); iterator.hasNext();) {
						// Component's child type can be only slot
						Slot slotObject = (Slot) iterator.next();

						// If there is a slot info object for this slot
						if (slotListFromParser.size() != 0) {
							// Update information about slot and take/add necessary child components

							// Take the first one in the list
							// Current slot info object
							SlotXMLInfo slotFromParser = slotListFromParser.get(0);

							// TODO: update slot's title and description
							// Update slot info into the current slot object

							// The most important thing! update slot's id in order to place in into correct place on the
							// page
							String newSlotID = slotFromParser.getId();
							slotObject.getProperties().properties.get(Property.ID).setValue(newSlotID);
							slotObject.getProperties().properties.get(Property.TITLE).setValue(slotFromParser.getTitle());
							slotObject.getProperties().properties.get(Property.DESCR).setValue(slotFromParser.getDescr());
							slotObject.getTreeItem().updateItemText();
							// Add a slot to the panel
							panel.add(slotObject, newSlotID);

							// Process children components
							for (Iterator<BasicPageElement> iteratorChildPrev = slotObject.children.iterator(); iteratorChildPrev.hasNext();) {
								// TODO: process slot child as well
								Component componentObject = (Component) iteratorChildPrev.next();
								boolean correspondingComponentObjectHasFound = false;
								for (Iterator<ComponentCallXMLInfo> iteratorChildNew = slotFromParser.getChildren().iterator(); iteratorChildNew
										.hasNext();) {
									ComponentCallXMLInfo componentInfo = iteratorChildNew.next();
									if (componentInfo.getID().equals(componentObject.getProperties().properties.get(Property.ID).getStringValue())) {
										// Show that the component has found
										// TODO: later add more intellectual algorithm, otherwise 2 components of the
										// same type can be
										// overwritten just because they are in the wrong order
										correspondingComponentObjectHasFound = true;
										// Components has the same ID
										// Check parameter values
										// Value that indicates that the parameter names and values are the one which
										// are expected
										boolean hasSameParameterValues = false;
										if (componentInfo.getParameters().size() == componentObject.getParameters().size()) {
											for (Iterator<String> iteratorParameters = componentInfo.getParameters().keySet().iterator(); iteratorParameters
													.hasNext();) {
												String paramName = iteratorParameters.next();
												if (!(componentObject.getParameters().containsKey(paramName) && componentObject.getParameters().get(
														paramName).getStringValue().equals(componentInfo.getParameters().get(paramName)))) {
													hasSameParameterValues = true;
													break;
												}
											}

											if (!hasSameParameterValues) {
												// Component object can be reused
												// Do nothing
											} else {
												// New component has different parameters
												// TODO: Update parameters and redraw it

												// If parsed component has other set of parameters then current version,
												// then they should be used instead

												// So, all current parameters are set to null
												// Later, if some new value is parsed, it is set
												for (Iterator<String> iteratorParameters = componentObject.getParameters().keySet().iterator(); iteratorParameters
														.hasNext();) {
													String paramName = iteratorParameters.next();
													String paramValue = componentInfo.getParameters().get(paramName);

													componentObject.parameters.get(paramName).setValue(null);
													if (componentInfo.getParameters().containsKey(paramName)) {
														componentObject.parameters.get(paramName).setValue(
																componentInfo.getParameters().get(paramName));
													}
												}

												// Mark containing slot as changed
												if (hasChanged) {
													// Main.onSlotChanged(componentObject);
												}
												componentObject.Draw(isComponentProxy);

											}
										} else {
											// New component has different amount of parameters
											// TODO: Update parameters and redraw it
											// !!!!!!!!

											// If parsed component has other set of parameters then current version,
											// then they should be used instead

											// So, all current parameters are set to null
											// Later, if some new value is parsed, it is set
											for (Iterator<String> iteratorParameters = componentObject.getParameters().keySet().iterator(); iteratorParameters
													.hasNext();) {
												String paramName = iteratorParameters.next();
												String paramValue = componentInfo.getParameters().get(paramName);

												componentObject.parameters.get(paramName).setValue(null);
												if (componentInfo.getParameters().containsKey(paramName)) {
													componentObject.parameters.get(paramName).setValue(componentInfo.getParameters().get(paramName));
												}
											}
											// Mark containing slot as changed
											if (hasChanged) {
												// Main.onSlotChanged(componentObject);
											}

											componentObject.Draw(isComponentProxy);
										}

										// Remove component info from the list of components to parse
										slotFromParser.getChildren().remove(componentInfo);

										// Do not need to search for corresponding components anymore
										break;
									}
								}
								if (!correspondingComponentObjectHasFound) {
									// Mark containing slot as changed
									if (hasChanged) {
										// Main.onSlotChanged(componentObject);
									}

									// Component is not necessary anymore
									componentObject.delete();
								}

							}

							if (!slotFromParser.getChildren().isEmpty()) {
								// There are some component infos to process
								Component[] components = new Component[slotFromParser.getChildren().size()];
								for (Iterator<ComponentCallXMLInfo> iteratorC = slotFromParser.getChildren().iterator(); iteratorC.hasNext();) {
									ComponentCallXMLInfo componentInfo = iteratorC.next();

									getTemplateFromServer(isComponentProxy, components, componentInfo, slotFromParser.getChildren().indexOf(
											componentInfo), slotObject);
								}
							}

							// Remove info about processed slot from the list
							slotListFromParser.remove(0);
						} else {
							// There is no parsed slots anymore
							// Delete the slot
							slotObject.delete();
						}
					}

					// If there are still unprocessed slots
					if (slotListFromParser.size() != 0) {
						// For each slot info
						for (Iterator<SlotXMLInfo> iterator = slotListFromParser.iterator(); iterator.hasNext();) {
							SlotXMLInfo slotFromParser = iterator.next();
							// Create new slot and add it to the component
							Slot slot = new Slot(Template.fakeTemplate(slotFromParser.getTitle(), slotFromParser.getDescr(), slotFromParser.getId()),
									this);
							AddChild(slot, slotFromParser.getId());
							// If Nav tree is already created
							// if (treeItem != null) {
							// Add a slot to as a corresponding child
							NavigatorTab.getInstance().addElement(this.treeItem, slot, this.treeItem.getChildCount());
							// }
							// else {
							// NavigatorTab.getInstance().addElement(this.treeItem, slot, 0 );
							// }
							slot.Draw(isComponentProxy);
							// Mark containing slot as changed
							if (hasChanged) {
								// Main.onSlotChanged(slot);
							}

							ArrayList<ComponentCallXMLInfo> componentListFromServer = slotFromParser.getChildren();
							// If slot contains children
							if (!componentListFromServer.isEmpty()) {
								Component[] components = new Component[componentListFromServer.size()];
								for (Iterator<ComponentCallXMLInfo> iteratorC = componentListFromServer.iterator(); iteratorC.hasNext();) {
									ComponentCallXMLInfo componentInfo = iteratorC.next();

									getTemplateFromServer(isComponentProxy, components, componentInfo,
											componentListFromServer.indexOf(componentInfo), slot);
								}
							}

						}
					}
//				} catch (Exception e) {
//					new PopupError("Unfortunately there is an error while parsing the page. The system'll try to display as much as possible.", e
//							.getMessage()
//							+ "\n" + e.getStackTrace());
//					Label cannotBeDisplayed = new Label("Unfortunately this cannot be displayed. Please check the sourse code!");
//					cannotBeDisplayed.setStyleName("design-label-component-cannot-be-displayed");
//					panel.add(cannotBeDisplayed, htmlPanelString);
//				}
			}
		} else {
			DrawChildren(isComponentProxy);
		}

	}

	// public void parseComponents

	/**
	 * 
	 * @param isComponentProxy
	 *            indicates that the parsing is done for rendering of the proxy object (does not require adding drop
	 *            controllers)
	 * @param components
	 *            list of component objects which is filled when components come back from server
	 * @param componentInfo
	 *            information about component call
	 * @param row
	 *            number of component
	 * @param o
	 *            parent slot object
	 * @param last
	 *            final number of components for the slot
	 */
	private void getTemplateFromServer(final boolean isComponentProxy, final Component[] components, final ComponentCallXMLInfo componentInfo,
			final int row, final Slot o) {

		ConnectionToServer.makeACall(new CallbackActions() {

			public void execute(AsyncCallback callback) {
				ConnectionToServer.searchService.getTemplateDetailedInfo(componentInfo.getID(), callback);
			}

			public void onFailure(Throwable caught) {
				// new PopupError("Unfortunately getting component details procedure for component " +
				// componentInfo.getID() + "(N " + row
				// + ") has failed on server!", caught.getMessage());
			}

			public void onSuccess(Object result) {
				if (result == null) {
					// No template has found
					// Say smth
				} else {
					Component component = new Component((Template) result, o);
					for (Iterator<String> iterator = componentInfo.getParameters().keySet().iterator(); iterator.hasNext();) {
						String paramName = iterator.next();
						String paramValue = componentInfo.getParameters().get(paramName);
						component.setParameterValue(paramName, paramValue);
					}

					components[row] = component;

					boolean arrayIsFull = true;
					for (int k = 0; k < components.length; k++) {
						if (components[k] == null) {
							arrayIsFull = false;
						}
					}
					if (arrayIsFull) {
						for (int i = 0; i < components.length; i++) {
							o.AddComponentChild(components[i], i);
							NavigatorTab.getInstance().addElement(o.getTreeItem(), components[i], i);
							PLTab.getInstance().addComponent(components[i]);
						}
						o.DrawChildren();
					}
				}
			}
		});
	}

	public void makeChildSlotsDraggable() {
		for (Iterator<BasicPageElement> iterator = children.iterator(); iterator.hasNext();) {
			Object o = iterator.next();
			if (o instanceof Slot) {
				DnDController.addDropController((Slot) o);
			}
		}

	}

	public void delete() {
		// Main.getInstance().PrintDropTargets();
		removeDropTarget();

		for (Iterator<BasicPageElement> iterator = children.iterator(); iterator.hasNext();) {
			Slot o = (Slot) iterator.next();
			o.delete();
		}

		((Slot) parent).RemoveComponent(this);
		PLTab.getInstance().removeComponent(this);

		if (Main.getInstance().getSelectedElement() == this) {
			Main.getInstance().setSelectedElement(null);
		}

		NavigatorTab.getInstance().removeElement(treeItem);

		// Main.getInstance().PrintDropTargets();
	}

	public void copyParametersFromTemplate() {
		Set<String> keys = template.parameters.keySet();

		for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
			String o = iterator.next();
			parameters.get(o).setDefaultValue(template.parameters.get(o).getStringValue());
		}
	}

	public String getTypeName() {
		return "component";
	}

	public Property getParameter(String name) {

		return parameters.get(name);

	}

	public ArrayList<BasicPageElement> getChildrenElements() {
		return children;
	}

	public BasicPageElement getParentElement() {
		return parent;
	}

	public Template getTemplate() {
		return template;
	}

	public Template getTemplateBackup() {
		return templateBackup;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fi.tkk.media.xide.client.UI.Widget.BasicPageElement#setChildrenElements(java.util.ArrayList)
	 */
	public void setChildrenElements(ArrayList<BasicPageElement> children) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seefi.tkk.media.xide.client.UI.Widget.BasicPageElement#setParentElement(fi.tkk.media.xide.client.UI.Widget.
	 * BasicPageElement)
	 */
	public void setParentElement(BasicPageElement parent) {
		this.parent = parent;
		if (parent != null) {
			accessRightsSettings = AccessRights.getAccessRightsSettingsForChild(parent.getAccessRightsSettings());
			;
			if (!children.isEmpty()) {
				for (Iterator<BasicPageElement> iterator = children.iterator(); iterator.hasNext();) {
					iterator.next().setAccessRightsSettings(AccessRights.getAccessRightsSettingsForChild(parent.getAccessRightsSettings()));
				}

			}
		} else {
			accessRightsSettings = new ArrayList<Integer>();
		}

	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public Widget GetPanel() {
		return this;
	}

	/**
	 * Only slots can be added into component. Thus there is no need to add slot on specified place. Row parameter is
	 * fake one.
	 */
	public void AddChild(BasicPageElement element, int row) {
		// Physical adding
		panel.add(element.GetPanel(), htmlPanelString);
		// Logical adding
		children.add(element);
	}

	/**
	 * Add element by html ID
	 */
	public void AddChild(BasicPageElement element, String id) {
		if (element instanceof Slot) {
			String styleName = "slot";
			String result = "<table id=\"" + styleName + "-id\" cellspacing=\"0\" cellpadding=\"0\" style =\"width:100%\"> " + "<tr>" + "<td>"
					+ "<div class = \"" + styleName + "-top-left-corner\"></div>" + "</td>" + "<td style=\"width: 100%;\">"
					+ "<div id=\"button-label-id-" + i + "\"  class = \"" + styleName + "-top\"></div>" + "</td>" + "<td>" + "<div  class = \""
					+ styleName + "-top-right-corner\"></div>" + " </td>" + " </tr> " + "<tr>" + "<td>" + "<div class = \"" + styleName
					+ "-left\"></div>" + "</td>" + "<td style=\"width: 100%;\">" + "<div id=\"slot-id-id-" + "\"  class = \"" + styleName
					+ "-middle\"></div>" + "</td>" + "<td>" + "<div  class = \"" + styleName + "-right\"></div>" + " </td>" + " </tr> " + "<tr>"
					+ "<td>" + "<div class = \"" + styleName + "-bottom-left-corner\"></div>" + "</td>" + "<td style=\"width: 100%;\">"
					+ "<div id=\"button-label-id-" + i + "\"  class = \"" + styleName + "-bottom\"></div>" + "</td>" + "<td>" + "<div   class = \""
					+ styleName + "-bottom-right-corner\"></div>" + " </td>" + " </tr> " + "</table>";
			HTMLPanel p = new HTMLPanel(result);
			// p.add(element.GetPanel(), "slot-id-id-");
			// panel.add(p, id);
		}
		// else {
		panel.add(element.GetPanel(), id);
		// }
		// Logical adding
		children.add(element);
	}

	public ArrayList<Selectable> GetLinkedObjects() {
		ArrayList<Selectable> list = new ArrayList<Selectable>();
		if (treeItem != null) {
			list.add(treeItem);
		}

		PLTreeItem item;
		if (templateBackup != null) {
			item = PLTab.getInstance().SearchTemplateItem(templateBackup);
		} else {
			item = PLTab.getInstance().SearchTemplateItem(template);
		}
		if (item != null) {
			list.add(item);
		}
		return list;
	}

	public Template getProperties() {
		return template;
	}

	public Selectable getValuableElement() {
		return this;
	}

	public void Select() {
		this.changeStyle(SELECTED);

		// if(templateBackup == null) {
		// templateBackup = template;
		// template = template.clone();
		// }
	}

	public void Unselect() {
		this.changeStyle(RESET);
	}

	public void setTreeItem(DnDTreeItem treeItem) {
		this.treeItem = treeItem;
	}

	public HashMap<String, Property> getParameters() {

		return parameters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fi.tkk.media.xide.client.Data.Selectable#Changed()
	 */
	public void Changed() {
		if (!isChanged) {
			if (templateBackup == null) {
				templateBackup = template;
				template = template.cloneIt();
			}
			isChanged = true;
			hasChanged = true;
			Main.getCurrentView().updatePropertiesList();
			changeStyle(CHANGED);

		}

	}
	
	public void afterSaved() {
	}
	
	public void Saved(SaveObjectsListener listener) {
		
	}
	
	public void Saved() {
		if (isChanged) {
			// For components
			PLTab.getInstance().componentHasChanged(this);
			isChanged = false;
			templateBackup = null;
			// Change style
			changeStyle(RESET);
		}
	}

	public boolean isChanged() {
		return isChanged;
	}

	public void Canceled() {
		if ((isChanged) && (templateBackup != null)) {
			template = templateBackup;
			templateBackup = null;
			isChanged = false;
			// Save slots
			for (Iterator<BasicPageElement> iterator = children.iterator(); iterator.hasNext();) {
				BasicPageElement o = iterator.next();
				if (o instanceof Slot) {
					Slot slot = (Slot) o;
					Main.getInstance().setElementCanceled(slot);
				}
			}

			Main.getCurrentView().updatePropertiesList();
			Main.getInstance().UpdateUI();
			changeStyle(Selectable.RESET);
		}

	}

	public void changeStyle(int event) {
		if ((event & Selectable.CHANGED) > 0) {
			for (Iterator<Selectable> iterator = GetLinkedObjects().iterator(); iterator.hasNext();) {
				iterator.next().changeStyle(Selectable.CHANGED_LINKED);
			}
		} else if ((event & Selectable.CHANGED_LINKED) > 0) {

		} else if ((event & Selectable.SELECTED) > 0) {
			this.panel.addStyleDependentName("selected");
			for (Iterator<Selectable> iterator = GetLinkedObjects().iterator(); iterator.hasNext();) {
				iterator.next().changeStyle(Selectable.SELECTED_LINKED);
			}

		} else if ((event & Selectable.SELECTED_LINKED) > 0) {
			this.panel.addStyleDependentName("selected-linked");
		} else if ((event & Selectable.RESET) > 0) {
			this.panel.removeStyleDependentName("selected");
			this.panel.removeStyleDependentName("selected-linked");
			if (Main.getInstance().getSelectedElement() == this) {
				for (Iterator<Selectable> iterator = GetLinkedObjects().iterator(); iterator.hasNext();) {
					iterator.next().changeStyle(Selectable.RESET);
				}
			}
		}

	}

	public void addNewRole(int roleID, int value) {
		accessRightsSettings.add(roleID, value);
		if (value == AccessRights.RIGHT_NOT_GRANTED) {
			value = AccessRights.RIGHT_DISABLED;
		}
		for (Iterator<BasicPageElement> iterator = children.iterator(); iterator.hasNext();) {
			iterator.next().addNewRole(roleID, value);
		}
	}

	public void removeRole(int roleID) {
		accessRightsSettings.remove(roleID);
		for (Iterator<BasicPageElement> iterator = children.iterator(); iterator.hasNext();) {
			iterator.next().removeRole(roleID);
		}
	}

	public void setRightToRole(int roleID, int value, boolean propagateToChildren) {
		accessRightsSettings.remove(roleID);
		accessRightsSettings.add(roleID, value);

		if (value == AccessRights.RIGHT_NOT_GRANTED) {
			value = AccessRights.RIGHT_DISABLED;
		}
		if ((propagateToChildren) || value == (AccessRights.RIGHT_NOT_GRANTED)) {
			for (Iterator<BasicPageElement> iterator = children.iterator(); iterator.hasNext();) {
				iterator.next().setRightToRole(roleID, value, propagateToChildren);
			}
		}
	}

	public ArrayList<Integer> getAccessRightsSettings() {
		return accessRightsSettings;
	}

	public void setAccessRightsSettings(ArrayList<Integer> accessRightsSettings) {
		this.accessRightsSettings = accessRightsSettings;
	}

	public void Deleted() {
		this.delete();

	}

	public Image getImage() {
		return new Image(Icons.ICON_COMPONENT);
	}

	public DnDTreeItem getTreeItem() {
		return treeItem;
	}
}
