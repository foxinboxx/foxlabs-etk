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

import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.XMLStreamException;

import org.foxlabs.etk.xml.XmlException;

public class StaxWriter extends AbstractXmlWriter<XMLStreamWriter>{
    
    public StaxWriter(XMLStreamWriter target) {
        super(target);
    }
    
    // Namespace
    
    public String getNamespaceURI(String prefix) throws XmlException {
        return target.getNamespaceContext().getNamespaceURI(prefix);
    }
    
    public String getPrefix(String nsURI) throws XmlException {
        return target.getNamespaceContext().getPrefix(nsURI);
    }
    
    // Element
    
    public void appendEmptyElement(String localName) throws XmlException {
        try {
            target.writeEmptyElement(localName);
        } catch (XMLStreamException e) {
            throw newXmlException(e);
        }
    }
    
    public void appendEmptyElement(String localName, String nsURI)
            throws XmlException {
        try {
            target.writeEmptyElement(nsURI, localName);
        } catch (XMLStreamException e) {
            throw newXmlException(e);
        }
    }
    
    public void appendEmptyElement(String prefix, String localName, String nsURI)
            throws XmlException {
        try {
            if (isPrefixBinded(prefix, nsURI)) {
                target.writeEmptyElement(prefix, localName, nsURI);
            } else {
                target.writeStartElement(prefix, localName, nsURI);
                target.writeNamespace(prefix, nsURI);
                target.writeEndElement();
            }
        } catch (XMLStreamException e) {
            throw newXmlException(e);
        }
    }
    
    public void appendStartElement(String localName) throws XmlException {
        try {
            target.writeStartElement(localName);
        } catch (XMLStreamException e) {
            throw newXmlException(e);
        }
    }
    
    public void appendStartElement(String localName, String nsURI) throws XmlException {
        try {
            target.writeStartElement(nsURI, localName);
        } catch (XMLStreamException e) {
            throw newXmlException(e);
        }
    }
    
    public void appendStartElement(String prefix, String localName, String nsURI)
            throws XmlException {
        try {
            boolean flag = isPrefixBinded(prefix, nsURI);
            target.writeStartElement(prefix, localName, nsURI);
            if (!flag)
                target.writeNamespace(prefix, nsURI);
        } catch (XMLStreamException e) {
            throw newXmlException(e);
        }
    }
    
    public void appendEndElement() throws XmlException {
        try {
            target.writeEndElement();
        } catch (XMLStreamException e) {
            throw newXmlException(e);
        }
    }
    
    // Attribute
    
    public void appendAttribute(String localName, String value) throws XmlException {
        try {
            target.writeAttribute(localName, value);
        } catch (XMLStreamException e) {
            throw newXmlException(e);
        }
    }
    
    public void appendAttribute(String localName, String nsURI, String value)
            throws XmlException {
        try {
            target.writeAttribute(nsURI, localName, value);
        } catch (XMLStreamException e) {
            throw newXmlException(e);
        }
    }
    
    public void appendAttribute(String prefix, String localName, String nsURI, String value)
            throws XmlException {
        try {
            boolean flag = isPrefixBinded(prefix, nsURI);
            target.writeAttribute(prefix, nsURI, localName, value);
            if (!flag)
                target.writeNamespace(prefix, nsURI);
        } catch (XMLStreamException e) {
            throw newXmlException(e);
        }
    }
    
    public void appendNamespace(String prefix, String nsURI) throws XmlException {
        try {
            target.writeNamespace(prefix, nsURI);
        } catch (XMLStreamException e) {
            throw newXmlException(e);
        }
    }
    
    // Text
    
    public void appendText(String text) throws XmlException {
        try {
            target.writeCharacters(text);
        } catch (XMLStreamException e) {
            throw newXmlException(e);
        }
    }
    
    public void appendCData(String text) throws XmlException {
        try {
            target.writeCData(text);
        } catch (XMLStreamException e) {
            throw newXmlException(e);
        }
    }
    
    public void appendComment(String text) throws XmlException {
        try {
            target.writeComment(text);
        } catch (XMLStreamException e) {
            throw newXmlException(e);
        }
    }
    
    // Utility
    
    private XmlException newXmlException(Throwable cause) {
        return new XmlException("StAX error", cause);
    }
    
}
