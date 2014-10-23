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

import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Comment;
import org.w3c.dom.CDATASection;
import org.w3c.dom.DOMException;

import org.foxlabs.etk.xml.XmlException;

public class DomWriter extends AbstractXmlWriter<Node> {
    
    private final Document document;
    private Node cursor;
    
    public DomWriter(Node target) {
        super(target);
        document = target.getNodeType() == Node.DOCUMENT_NODE
            ? (Document) target
            : target.getOwnerDocument();
        cursor = target;
    }
    
    // Namespace
    
    public String getNamespaceURI(String prefix) throws XmlException {
        return target.lookupNamespaceURI(prefix);
    }
    
    public String getPrefix(String nsURI) throws XmlException {
        return target.lookupPrefix(nsURI);
    }
    
    // Element
    
    public void appendEmptyElement(String localName) throws XmlException {
        try {
            Element el = document.createElement(localName);
            cursor.appendChild(el);
        } catch (DOMException e) {
            throw newXmlException(e);
        }
    }
    
    public void appendEmptyElement(String localName, String nsURI) throws XmlException {
        try {
            Element el = document.createElementNS(nsURI, qualify(getPrefix(nsURI), localName));
            cursor.appendChild(el);
        } catch (DOMException e) {
            throw newXmlException(e);
        }
    }
    
    public void appendEmptyElement(String prefix, String localName, String nsURI)
            throws XmlException {
        try {
            Element el = document.createElementNS(nsURI, qualify(prefix, localName));
            cursor.appendChild(el);
        } catch (DOMException e) {
            throw newXmlException(e);
        }
    }
    
    public void appendStartElement(String localName) throws XmlException {
        try {
            Element el = document.createElement(localName);
            cursor = cursor.appendChild(el);
        } catch (DOMException e) {
            throw newXmlException(e);
        }
    }
    
    public void appendStartElement(String localName, String nsURI) throws XmlException {
        try {
            Element el = document.createElementNS(nsURI, qualify(getPrefix(nsURI), localName));
            cursor = cursor.appendChild(el);
        } catch (DOMException e) {
            throw newXmlException(e);
        }
    }
    
    public void appendStartElement(String prefix, String localName, String nsURI)
            throws XmlException {
        try {
            Element el = document.createElementNS(nsURI, qualify(prefix, localName));
            cursor = cursor.appendChild(el);
        } catch (DOMException e) {
            throw newXmlException(e);
        }
    }
    
    public void appendEndElement() throws XmlException {
        try {
            cursor = cursor.getParentNode();
        } catch (DOMException e) {
            throw newXmlException(e);
        }
    }
    
    // Attribute
    
    public void appendAttribute(String localName, String value) throws XmlException {
        try {
            ((Element) cursor).setAttribute(localName, value);
        } catch (DOMException e) {
            throw newXmlException(e);
        }
    }
    
    public void appendAttribute(String localName, String nsURI, String value)
            throws XmlException {
        try {
            ((Element) cursor).setAttributeNS(nsURI, qualify(getPrefix(nsURI), localName), value);
        } catch (DOMException e) {
            throw newXmlException(e);
        }
    }
    
    public void appendAttribute(String prefix, String localName, String nsURI, String value)
            throws XmlException {
        try {
            ((Element) cursor).setAttributeNS(nsURI, qualify(prefix, localName), value);
        } catch (DOMException e) {
            throw newXmlException(e);
        }
    }
    
    public void appendNamespace(String prefix, String nsURI) throws XmlException {
        appendAttribute("xmlns:" + prefix, nsURI);
    }
    
    // Text
    
    public void appendText(String text) throws XmlException {
        try {
            Text txt = document.createTextNode(text);
            cursor.appendChild(txt);
        } catch (DOMException e) {
            throw newXmlException(e);
        }
    }
    
    public void appendCData(String text) throws XmlException {
        try {
            CDATASection cdata = document.createCDATASection(text);
            cursor.appendChild(cdata);
        } catch (DOMException e) {
            throw newXmlException(e);
        }
    }
    
    public void appendComment(String text) throws XmlException {
        try {
            Comment rem = document.createComment(text);
            cursor.appendChild(rem);
        } catch (DOMException e) {
            throw newXmlException(e);
        }
    }
    
    // Utility
    
    private XmlException newXmlException(Throwable cause) {
        return new XmlException("DOM error", cause);
    }
    
    private static String qualify(String prefix, String localName) {
        return prefix == null || prefix.length() == 0
            ? localName
            : prefix + ":" + localName;
    }
    
}
