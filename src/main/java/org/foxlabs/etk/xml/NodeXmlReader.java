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

package org.foxlabs.etk.xml;

import java.math.BigInteger;
import java.math.BigDecimal;

import javax.xml.XMLConstants;

import org.foxlabs.etk.Environment;
import org.foxlabs.etk.EtkException;
import org.foxlabs.etk.node.Node;
import org.foxlabs.etk.node.NodeBuilder;
import org.foxlabs.etk.resource.ResourceManager;
import org.foxlabs.etk.support.Signature;
import org.foxlabs.etk.support.Types;
import org.foxlabs.etk.xml.wrapper.DomReader;
import org.foxlabs.etk.xml.wrapper.SimpleXmlReader;
import org.foxlabs.etk.xml.wrapper.StaxReader;

public class NodeXmlReader implements XmlConstants {
    
    private static NodeXmlReader defaultReader;
    
    public static NodeXmlReader getDefault() {
        if (defaultReader == null)
            defaultReader = new NodeXmlReader(DEFAULT_NS_URI);
        return defaultReader;
    }
    
    protected final String nsURI;
    
    public NodeXmlReader(String nsURI) {
        this.nsURI = nsURI == null ? XMLConstants.NULL_NS_URI : nsURI;
    }
    
    public final String getNamespaceURI() {
        return nsURI;
    }
    
    public Node readNode(org.w3c.dom.Node root, Environment context)
            throws XmlException {
        return readNode(new DomReader(root), context);
    }
    
    public Node readNode(javax.xml.stream.XMLStreamReader in, Environment context)
            throws XmlException {
        return readNode(new StaxReader(in), context);
    }
    
    public Node readNode(SimpleXmlReader<?> in, Environment context)
            throws XmlException {
        Reader reader = new Reader(in, context);
        return reader.read();
    }
    
    // Reader
    
    protected class Reader {
        
        protected final SimpleXmlReader<?> in;
        protected final Environment context;
        protected final NodeBuilder builder;
        
        public Reader(SimpleXmlReader<?> in, Environment context) {
            this.in = in;
            this.context = context;
            this.builder = new NodeBuilder(context);
        }
        
        public Node read() throws XmlException {
            in.requireStartElement();
            readNode();
            return builder.popNode();
        }
        
        // Read methods
        
        public void readNode() throws XmlException {
            String localName = in.getElementLocalName();
            String nsURI = in.getElementNamespaceURI();
            
            if (nsURI.equals(nsURI)) {
                try {
                    if (XML_NULL.equals(localName)) {
                        readNull();
                    } else if (XML_TRUE.equals(localName)) {
                        readTrue();
                    } else if (XML_FALSE.equals(localName)) {
                        readFalse();
                    } else if (XML_ENUM.equals(localName)) {
                        readEnum();
                    } else if (XML_CHAR.equals(localName)) {
                        readChar();
                    } else if (XML_NUMBER.equals(localName)) {
                        readNumber();
                    } else if (XML_STRING.equals(localName)) {
                        readString();
                    } else if (XML_VARIABLE.equals(localName)) {
                        readVariable();
                    } else if (XML_ARRAY.equals(localName)) {
                        readArray();
                    } else if (XML_FUNCTION.equals(localName)) {
                        readFunction();
                    } else if (XML_CAST.equals(localName)) {
                        readCast();
                    } else if (XML_NEG.equals(localName)) {
                        readNegate();
                    } else if (XML_ADD.equals(localName)) {
                        readAdd();
                    } else if (XML_SUB.equals(localName)) {
                        readSubtract();
                    } else if (XML_MUL.equals(localName)) {
                        readMultiply();
                    } else if (XML_DIV.equals(localName)) {
                        readDivide();
                    } else if (XML_MOD.equals(localName)) {
                        readModulo();
                    } else if (XML_EQ.equals(localName)) {
                        readEquals();
                    } else if (XML_NE.equals(localName)) {
                        readNotEquals();
                    } else if (XML_GT.equals(localName)) {
                        readGreaterThan();
                    } else if (XML_GE.equals(localName)) {
                        readGreaterThanEqual();
                    } else if (XML_LT.equals(localName)) {
                        readLessThan();
                    } else if (XML_LE.equals(localName)) {
                        readLessThanEqual();
                    } else if (XML_NOT.equals(localName)) {
                        readNot();
                    } else if (XML_AND.equals(localName)) {
                        readAnd();
                    } else if (XML_OR.equals(localName)) {
                        readOr();
                    } else if (XML_CONDITION.equals(localName)) {
                        readCondition();
                    } else if (XML_CONCAT.equals(localName)) {
                        readConcat();
                    } else if (XML_INDEX.equals(localName)) {
                        readIndex();
                    } else if (XML_PROPERTY.equals(localName)) {
                        readProperty();
                    } else if (XML_METHOD.equals(localName)) {
                        readMethod();
                    }
                } catch (EtkException e) {
                    throw ResourceManager.newXmlException(in, e);
                }
                
                in.requireEndElement();
                return;
            }
            
            throw ResourceManager.newXmlException(in, "xml.nodeElementExpected",
                    nsURI, in.getElementQualifiedName());
        }
        
        public void readNull() throws XmlException {
            builder.pushNull();
        }
        
        public void readTrue() throws XmlException {
            builder.pushLiteral(true);
        }
        
        public void readFalse() throws XmlException {
            builder.pushLiteral(false);
        }
        
        public void readEnum() throws XmlException {
            String typeName = in.requireAttribute(XML_ENUM_TYPE);
            String literalName = in.requireText();
            
            Class<?> type = context.resolveType(typeName);
            Enum<?> value = context.resolveEnum(type, literalName);
            builder.pushLiteral(value);
        }
        
        public void readChar() throws XmlException {
            String value = in.requireText();
            if (value.length() != 1)
                throw ResourceManager.newXmlException(in, "xml.invalidCharValue",
                        value);
            
            builder.pushLiteral(value.charAt(0));
        }
        
        public void readNumber() throws XmlException {
            String typeName = in.requireAttribute(XML_NUMBER_TYPE);
            String value = in.requireText();
            try {
                switch (Types.indexOf(typeName)) {
                    case Types.BYTE:
                        builder.pushLiteral(Byte.valueOf(value));
                        break;
                        
                    case Types.SHORT:
                        builder.pushLiteral(Short.valueOf(value));
                        break;
                        
                    case Types.INTEGER:
                        builder.pushLiteral(Integer.valueOf(value));
                        break;
                        
                    case Types.LONG:
                        builder.pushLiteral(Long.valueOf(value));
                        break;
                        
                    case Types.FLOAT:
                        builder.pushLiteral(Float.valueOf(value));
                        break;
                        
                    case Types.DOUBLE:
                        builder.pushLiteral(Double.valueOf(value));
                        break;
                        
                    case Types.BIGINTEGER:
                        builder.pushLiteral(new BigInteger(value));
                        break;
                        
                    case Types.BIGDECIMAL:
                        builder.pushLiteral(new BigDecimal(value));
                        break;
                        
                    default:
                        throw ResourceManager.newXmlException(in, "xml.invalidNumberType",
                                typeName);
                }
            } catch (NumberFormatException e) {
                throw ResourceManager.newXmlException(in, "xml.invalidNumberValue",
                        typeName, value);
            }
        }
        
        public void readString() throws XmlException {
            String value = in.getText();
            builder.pushLiteral(value == null ? "" : value);
        }
        
        public void readVariable() throws XmlException {
            String variableName = in.requireText();
            builder.pushVariable(variableName);
        }
        
        public void readArray() throws XmlException {
            builder.mark();
            readNary();
            builder.array();
        }
        
        public void readFunction() throws XmlException {
            String functionName = in.requireAttribute(XML_FUNCTION_NAME);
            String functionNamespace = Signature.NULL_NS;
            int index = functionName.lastIndexOf(':');
            if (index >= 0) {
                functionNamespace = functionName.substring(0, index);
                functionName = functionName.substring(index + 1);
            }
            
            in.requireStartElement(XML_FUNCTION_ARGS, nsURI);
            builder.mark();
            readNary();
            in.requireEndElement();
            
            builder.function(functionName, functionNamespace);
        }
        
        public void readCast() throws XmlException {
            String typeName = in.requireAttribute(XML_CAST_TYPE);
            Class<?> type = context.resolveType(typeName);
            
            readUnary();
            
            builder.cast(type);
        }
        
        public void readNegate() throws XmlException {
            readUnary();
            builder.negate();
        }
        
        public void readAdd() throws XmlException {
            readBinary();
            builder.add();
        }
        
        public void readSubtract() throws XmlException {
            readBinary();
            builder.subtract();
        }
        
        public void readMultiply() throws XmlException {
            readBinary();
            builder.multiply();
        }
        
        public void readDivide() throws XmlException {
            readBinary();
            builder.divide();
        }
        
        public void readModulo() throws XmlException {
            readBinary();
            builder.modulo();
        }
        
        public void readEquals() throws XmlException {
            readBinary();
            builder.eq();
        }
        
        public void readNotEquals() throws XmlException {
            readBinary();
            builder.ne();
        }
        
        public void readGreaterThan() throws XmlException {
            readBinary();
            builder.gt();
        }
        
        public void readGreaterThanEqual() throws XmlException {
            readBinary();
            builder.ge();
        }
        
        public void readLessThan() throws XmlException {
            readBinary();
            builder.lt();
        }
        
        public void readLessThanEqual() throws XmlException {
            readBinary();
            builder.le();
        }
        
        public void readNot() throws XmlException {
            readUnary();
            builder.not();
        }
        
        public void readAnd() throws XmlException {
            readBinary();
            builder.and();
        }
        
        public void readOr() throws XmlException {
            readBinary();
            builder.or();
        }
        
        public void readCondition() throws XmlException {
            in.requireStartElement(XML_CONDITION_IF, nsURI);
            readUnary();
            in.requireEndElement();
            
            in.requireStartElement(XML_CONDITION_THEN, nsURI);
            readUnary();
            in.requireEndElement();
            
            in.requireStartElement(XML_CONDITION_ELSE, nsURI);
            readUnary();
            in.requireEndElement();
            
            builder.condition();
        }
        
        public void readConcat() throws XmlException {
            builder.mark();
            readNary();
            builder.concat();
        }
        
        public void readIndex() throws XmlException {
            in.requireStartElement(XML_INDEX_COLLECTION, nsURI);
            readUnary();
            in.requireEndElement();
            
            in.requireStartElement(XML_INDEX_KEY, nsURI);
            readUnary();
            in.requireEndElement();
            
            builder.index();
        }
        
        public void readProperty() throws XmlException {
            String propertyName = in.requireAttribute(XML_PROPERTY_NAME);
            
            in.requireStartElement(XML_PROPERTY_OBJECT, nsURI);
            readUnary();
            in.requireEndElement();
            
            builder.property(propertyName);
        }
        
        public void readMethod() throws XmlException {
            String methodName = in.requireAttribute(XML_METHOD_NAME);
            
            in.requireStartElement(XML_METHOD_OBJECT, nsURI);
            readUnary();
            in.requireEndElement();
            
            in.requireStartElement(XML_METHOD_ARGS, nsURI);
            builder.mark();
            readNary();
            in.requireEndElement();
            
            builder.method(methodName);
        }
        
        // Utility methods
        
        protected void readUnary() throws XmlException {
            if (!in.startElement())
                throw ResourceManager.newXmlException(in, "xml.unaryElementExpected",
                        in.getElementQualifiedName());
            
            readNode();
        }
        
        protected void readBinary() throws XmlException {
            for (int i = 0; i < 2; i++) {
                if (!in.startElement())
                    throw ResourceManager.newXmlException(in, "xml.binaryElementExpected",
                            in.getElementQualifiedName());
                
                readNode();
            }
        }
        
        protected int readNary() throws XmlException {
            int count = 0;
            while (in.startElement()) {
                readNode();
                count++;
            }
            return count;
        }
        
    }
    
}
