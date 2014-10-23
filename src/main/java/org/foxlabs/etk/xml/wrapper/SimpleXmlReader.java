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

import org.foxlabs.etk.xml.XmlException;

public interface SimpleXmlReader<S> {
    
    S getSource();
    
    String getLocationString();
    
    // Namespace
    
    String getNamespaceURI(String prefix) throws XmlException;
    
    String getPrefix(String nsURI) throws XmlException;
    
    // Element
    
    String getElementPrefix() throws XmlException;
    
    String getElementLocalName() throws XmlException;
    
    String getElementQualifiedName() throws XmlException;
    
    String getElementNamespaceURI() throws XmlException;
    
    boolean startElement() throws XmlException;
    
    boolean startElement(String localName) throws XmlException;
    
    boolean startElement(String localName, String nsURI) throws XmlException;
    
    boolean endElement() throws XmlException;
    
    void requireStartElement() throws XmlException;
    
    void requireStartElement(String localName) throws XmlException;
    
    void requireStartElement(String localName, String nsURI) throws XmlException;
    
    void requireEndElement() throws XmlException;
    
    void skipElement() throws XmlException;
    
    // Attribute
    
    String getAttribute(String localName) throws XmlException;
    
    String getAttribute(String localName, String nsURI) throws XmlException;
    
    String requireAttribute(String localName) throws XmlException;
    
    String requireAttribute(String localName, String nsURI) throws XmlException;
    
    Map<QName, String> getAttributes() throws XmlException;
    
    // Text
    
    String getText() throws XmlException;
    
    String getText(boolean coalescing) throws XmlException;
    
    String requireText() throws XmlException;
    
    String requireText(boolean coalescing) throws XmlException;
    
}
