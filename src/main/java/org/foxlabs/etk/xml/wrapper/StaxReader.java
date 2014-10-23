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

import javax.xml.namespace.QName;

import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamException;

import org.foxlabs.etk.xml.XmlException;

public class StaxReader extends AbstractXmlReader<XMLStreamReader> {
    
    public StaxReader(XMLStreamReader source) {
        super(source);
    }
    
    public String getLocationString() {
        StringBuilder buf = new StringBuilder();
        
        Location location = source.getLocation();
        int line = location.getLineNumber();
        int column = location.getColumnNumber();
        
        if (line > 0 && column > 0) {
            buf.append(' ');
            buf.append('[');
            buf.append(line);
            buf.append(':');
            buf.append(column);
            buf.append(']');
        }
        
        return buf.toString();
    }
    
    // Namespace
    
    public String getNamespaceURI(String prefix) throws XmlException {
        return source.getNamespaceContext().getNamespaceURI(prefix);
    }
    
    public String getPrefix(String nsURI) throws XmlException {
        return source.getNamespaceContext().getPrefix(nsURI);
    }
    
    // Utility
    
    protected Event readEvent() throws XmlException {
        try {
            switch (source.next()) {
                case XMLStreamReader.START_ELEMENT:
                    return Event.START_ELEMENT;
                case XMLStreamReader.END_ELEMENT:
                    return Event.END_ELEMENT;
                case XMLStreamReader.CHARACTERS:
                case XMLStreamReader.CDATA:
                case XMLStreamReader.SPACE:
                    return Event.TEXT;
            }
        } catch (XMLStreamException e) {
            throw new XmlException("StAX error", e);
        }
        
        return readEvent();
    }
    
    protected void readElement(Element element) throws XmlException {
        element.prefix = source.getPrefix();
        element.localName = source.getLocalName();
        element.nsURI = source.getNamespaceURI();
    }
    
    protected void readAttributes(Map<QName, String> attrs) throws XmlException {
        int count = source.getAttributeCount();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                QName name = new QName(source.getAttributeNamespace(i),
                                       source.getAttributeLocalName(i),
                                       source.getAttributePrefix(i));
                attrs.put(name, source.getAttributeValue(i));
            }
        }
    }
    
    protected void readText(StringBuilder buf) throws XmlException {
        buf.append(source.getText());
    }
    
}
