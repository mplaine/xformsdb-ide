/**
 * 
 */
package fi.tkk.media.xide.client.parser;

/**
 * @author Evgenia Samochadina
 * @date Jun 8, 2009
 *
 */
/*
Simple XML Parser for GWT
Copyright (C) 2006 musachy http://gwt.components.googlepages.com/

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA

*/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Node {

  private Map<String, String> attributes = new HashMap<String, String>();
  private ArrayList<Node> childNodes = new ArrayList<Node>();
  private String type;
  private String name;
  private String value;
  private Document document;
  private Node parent;

  Node(String nodeType, String nodeName, String nodeValue, Document ownerDocument) {
    this.type = nodeType;
    this.name = nodeName;
    this.value = nodeValue;
    this.document = ownerDocument;
  }

  public void appendChild(Node child) {
    child.setParent(this);
    childNodes.add(child);
  }

  public String getAttribute(String name) {
    return attributes.get(name);
  }

  public Map<String, String> getAttributes () {
	 return attributes;
  }
  
  public Set<String> getAttributesNames() {
	  return attributes.keySet();
  }
  
  public void setAttribute(String name, String value) {
    attributes.put(name, value);
  }

  public Node getParent() {
    return parent;
  }

  private void setParent(Node parent) {
    this.parent = parent;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public ArrayList<Node> getChildNodes() {
    return childNodes;
  }

  public Document getDocument() {
    return document;
  }
  
  public void removeChild(Node child) {
      child.setParent(null);
      childNodes.remove(child);
} 
} 