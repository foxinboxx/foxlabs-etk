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

import org.foxlabs.etk.xml.XmlException;

public interface SimpleXmlWriter<T> {
    
    T getTarget();
    
    // Namespace
    
    String getNamespaceURI(String prefix) throws XmlException;
    
    String getPrefix(String nsURI) throws XmlException;
    
    // Element
    
    void appendEmptyElement(String localName) throws XmlException;
    
    void appendEmptyElement(String localName, String nsURI) throws XmlException;
    
    void appendEmptyElement(String prefix, String localName, String nsURI) throws XmlException;
    
    void appendStartElement(String localName) throws XmlException;
    
    void appendStartElement(String localName, String nsURI) throws XmlException;
    
    void appendStartElement(String prefix, String localName, String nsURI) throws XmlException;
    
    void appendEndElement() throws XmlException;
    
    // Attribute
    
    void appendAttribute(String localName, String value) throws XmlException;
    
    void appendAttribute(String localName, String nsURI, String value) throws XmlException;
    
    void appendAttribute(String prefix, String localName, String nsURI, String value) throws XmlException;
    
    void appendNamespace(String prefix, String nsURI) throws XmlException;
    
    // Text
    
    void appendText(String text) throws XmlException;
    
    void appendCData(String text) throws XmlException;
    
    void appendComment(String text) throws XmlException;
    
}
