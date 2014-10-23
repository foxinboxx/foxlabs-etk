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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Collections;

import javax.xml.namespace.QName;

import org.foxlabs.etk.xml.XmlException;

public abstract class AbstractXmlReader<S> implements SimpleXmlReader<S> {
    
    protected static enum Event { START_ELEMENT, END_ELEMENT, TEXT }
    
    protected static class Element {
        
        String prefix;
        String localName;
        String nsURI;
        
        Map<QName, String> attributes;
        
    }
    
    protected final S source;
    
    private Event event;
    private LinkedList<Element> elements = new LinkedList<Element>();
    
    protected AbstractXmlReader(S source) {
        this.source = source;
    }
    
    public final S getSource() {
        return source;
    }
    
    public String getLocationString() {
        StringBuilder buf = new StringBuilder();
        
        for (Element element : elements) {
            buf.append('/');
            if (element.prefix != null) {
                buf.append(element.prefix);
                buf.append(':');
            }
            buf.append(element.localName);
        }
        
        return buf.toString();
    }
    
    // Element
    
    public String getElementPrefix() throws XmlException {
        return getElement().prefix;
    }
    
    public String getElementLocalName() throws XmlException {
        return getElement().localName;
    }
    
    public String getElementQualifiedName() throws XmlException {
        Element e = getElement();
        return e.prefix == null ? e.localName : e.prefix + ":" + e.localName;
    }
    
    public String getElementNamespaceURI() throws XmlException {
        return getElement().nsURI;
    }
    
    public boolean startElement() throws XmlException {
        return startElement(null, null);
    }
    
    public boolean startElement(String localName) throws XmlException {
        return startElement(localName, null);
    }
    
    public boolean startElement(String localName, String nsURI) throws XmlException {
        if (getEvent() == Event.START_ELEMENT) {
            Element e = new Element();
            readElement(e);
            
            if (localName == null || localName.equals(e.localName)) {
                if (nsURI == null || nsURI.equals(e.nsURI)) {
                    e.attributes = new HashMap<QName, String>();
                    readAttributes(e.attributes);
                    e.attributes = Collections.unmodifiableMap(e.attributes);
                    
                    elements.addLast(e);
                    event = null;
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public boolean endElement() throws XmlException {
        if (getEvent() == Event.END_ELEMENT) {
            if (elements.isEmpty())
                throw new IllegalStateException();
            
            elements.removeLast();
            event = null;
            return true;
        }
        
        return false;
    }
    
    public void requireStartElement() throws XmlException {
        requireStartElement(null, null);
    }
    
    public void requireStartElement(String localName) throws XmlException {
        requireStartElement(localName, null);
    }
    
    public void requireStartElement(String localName, String nsURI) throws XmlException {
        if (!startElement(localName, nsURI))
            throw new XmlException("End element required: \"" +
                    nsURI + "\":" + localName);
    }
    
    public void requireEndElement() throws XmlException {
        if (!endElement())
            throw new XmlException("End element required: \"" +
                    getElementNamespaceURI() + "\":" + getElementLocalName());
    }
    
    public void skipElement() throws XmlException {
        if (elements.isEmpty())
            throw new IllegalStateException();
        
        int level = 0;
        event = getEvent();
        while (event != Event.END_ELEMENT || level > 0) {
            if (event == Event.START_ELEMENT) {
                level++;
            } else if (event == Event.END_ELEMENT) {
                level--;
            }
            event = readEvent();
        }
        
        elements.removeLast();
        event = null;
    }
    
    // Attribute
    
    public String getAttribute(String localName) throws XmlException {
        return getAttribute(localName, null);
    }
    
    public String getAttribute(String localName, String nsURI) throws XmlException {
        return getElement().attributes.get(new QName(nsURI, localName));
    }
    
    public String requireAttribute(String localName) throws XmlException {
        return requireAttribute(localName, null);
    }
    
    public String requireAttribute(String localName, String nsURI) throws XmlException {
        String value = getAttribute(localName, nsURI);
        if (value == null)
            throw new XmlException("Attribute required: \"" + nsURI + "\":" + localName);
        return value;
    }
    
    public Map<QName, String> getAttributes() throws XmlException {
        return getElement().attributes;
    }
    
    // Text
    
    public String getText() throws XmlException {
        return getText(false);
    }
    
    public String getText(boolean coalescing) throws XmlException {
        if (getEvent() == Event.TEXT) {
            StringBuilder buf = new StringBuilder();
            readText(buf);
            
            if (coalescing) {
                while ((event = readEvent()) == Event.TEXT)
                    readText(buf);
            } else {
                event = null;
            }
            
            return buf.toString();
        }
        
        return null;
    }
    
    public String requireText() throws XmlException {
        return requireText(false);
    }
    
    public String requireText(boolean coalescing) throws XmlException {
        String text = getText(coalescing);
        if (text == null)
            throw new XmlException("Text required");
        return text;
    }
    
    // Utility
    
    protected Event getEvent() {
        if (event == null)
            event = readEvent();
        return event;
    }
    
    protected Element getElement() {
        if (elements.isEmpty())
            throw new IllegalStateException();
        return elements.getLast();
    }
    
    protected abstract Event readEvent() throws XmlException;
    
    protected abstract void readElement(Element element) throws XmlException;
    
    protected abstract void readAttributes(Map<QName, String> attrs) throws XmlException;
    
    protected abstract void readText(StringBuilder buf) throws XmlException;
    
}
