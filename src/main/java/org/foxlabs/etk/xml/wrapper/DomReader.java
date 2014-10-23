/* 
 * Copyright (C) 2008 FoxLabs
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.foxlabs.etk.xml.wrapper;

import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.DOMException;

import org.foxlabs.etk.xml.XmlException;

public class DomReader extends AbstractXmlReader<Node> {
    
    private Node cursor;
    private boolean down = true;
    
    public DomReader(Node source) {
        super(source);
    }
    
    public String getLocationString() {
        StringBuilder buf = new StringBuilder(super.getLocationString());
        
        String uri = source.getOwnerDocument().getDocumentURI();
        if (uri != null) {
            buf.append(" - ");
            buf.append(uri);
        }
        
        return buf.toString();
    }
    
    // Namespace
    
    public String getNamespaceURI(String prefix) throws XmlException {
        return source.lookupNamespaceURI(prefix);
    }
    
    public String getPrefix(String nsURI) throws XmlException {
        return source.lookupPrefix(nsURI);
    }
    
    // Utility
    
    protected Event readEvent() throws XmlException {
        if (cursor == null) {
            cursor = source;
        } else if (isElementNode(cursor)) {
            if (down) {
                if (cursor.hasChildNodes()) {
                    cursor = cursor.getFirstChild();
                } else {
                    down = false;
                    return Event.END_ELEMENT;
                }
            } else {
                Node node = cursor.getNextSibling();
                if (node == null) {
                    cursor = getParentElement();
                    return Event.END_ELEMENT;
                } else {
                    cursor = node;
                    down = true;
                }
            }
        } else {
            Node node = cursor.getNextSibling();
            if (node == null) {
                cursor = getParentElement();
                return Event.END_ELEMENT;
            } else {
                cursor = node;
                down = true;
            }
        }
        
        if (isElementNode(cursor))
            return Event.START_ELEMENT;
        if (isTextNode(cursor))
            return Event.TEXT;
        
        return readEvent();
    }
    
    private Node getParentElement() {
        Node node = cursor.getParentNode();
        if (node == null || !isElementNode(node))
            throw new IllegalStateException();
        down = false;
        return node;
    }
    
    protected void readElement(Element element) throws XmlException {
        element.prefix = cursor.getPrefix();
        element.nsURI = cursor.getNamespaceURI();
        element.localName = cursor.getLocalName();
        if (element.localName == null)
            element.localName = cursor.getNodeName();
    }
    
    protected void readAttributes(Map<QName, String> attrs) throws XmlException {
        NamedNodeMap map = cursor.getAttributes();
        for (int i = 0, len = map.getLength(); i < len; i++) {
            Node node = map.item(i);
            String nsURI = node.getNamespaceURI();
            String prefix = node.getPrefix();
            attrs.put(new QName(nsURI == null ? XMLConstants.NULL_NS_URI : nsURI,
                                node.getLocalName(),
                                prefix == null ? XMLConstants.DEFAULT_NS_PREFIX : prefix),
                      node.getTextContent());
        }
    }
    
    protected void readText(StringBuilder buf) throws XmlException {
        try {
            buf.append(cursor.getTextContent());
        } catch (DOMException e) {
            throw new XmlException("DOM error", e);
        }
    }
    
    private static boolean isElementNode(Node node) {
        return node.getNodeType() == Node.ELEMENT_NODE;
    }
    
    private static boolean isTextNode(Node node) {
        int type = node.getNodeType();
        return type == Node.TEXT_NODE || type == Node.CDATA_SECTION_NODE;
    }
    
}
