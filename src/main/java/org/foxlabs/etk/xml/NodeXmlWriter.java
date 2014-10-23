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

import javax.xml.XMLConstants;

import org.foxlabs.etk.Expression;
import org.foxlabs.etk.node.*;
import org.foxlabs.etk.support.Types;
import org.foxlabs.etk.xml.wrapper.DomWriter;
import org.foxlabs.etk.xml.wrapper.SimpleXmlWriter;
import org.foxlabs.etk.xml.wrapper.StaxWriter;

public class NodeXmlWriter implements XmlConstants {
    
    private static NodeXmlWriter defaultWriter;
    
    public static NodeXmlWriter getDefault() {
        if (defaultWriter == null)
            defaultWriter = new NodeXmlWriter(DEFAULT_NS_PREFIX, DEFAULT_NS_URI);
        return defaultWriter;
    }
    
    protected final String prefix;
    protected final String nsURI;
    
    public NodeXmlWriter(String prefix, String nsURI) {
        this.prefix = prefix == null || prefix.length() == 0 ? null : prefix;
        this.nsURI = nsURI == null ? XMLConstants.NULL_NS_URI : nsURI;
    }
    
    public final String getPrefix() {
        return prefix;
    }
    
    public final String getNamespaceURI() {
        return nsURI;
    }
    
    public void writeNode(Node node, SimpleXmlWriter<?> out)
            throws XmlException {
        Writer writer = new Writer(out);
        writer.write(node);
    }
    
    public void writeNode(Node node, org.w3c.dom.Node parent)
            throws XmlException {
        writeNode(node, new DomWriter(parent));
    }
    
    public void writeNode(Node node, javax.xml.stream.XMLStreamWriter out)
            throws XmlException {
        writeNode(node, new StaxWriter(out));
    }
    
    public void writeExpression(Expression expr, SimpleXmlWriter<?> out)
            throws XmlException {
        writeNode(expr.getRootNode(), out);
    }
    
    public void writeExpression(Expression expr, org.w3c.dom.Node parent)
            throws XmlException {
        writeExpression(expr, new DomWriter(parent));
    }
    
    public void writeExpression(Expression expr, javax.xml.stream.XMLStreamWriter out)
            throws XmlException {
        writeExpression(expr, new StaxWriter(out));
    }
    
    // Writer
    
    protected class Writer {
        
        protected final SimpleXmlWriter<?> out;
        protected final NodeVisitor<XmlException> visitor;
        
        public Writer(SimpleXmlWriter<?> out) {
            this.out = out;
            this.visitor = new Visitor(this);
        }
        
        public void write(Node node) throws XmlException {
            node.accept(visitor);
        }
        
        // Write methods
        
        public void writeNull(NullNode node) throws XmlException {
            out.appendEmptyElement(prefix, XML_NULL, nsURI);
        }
        
        public void writeTrue(TrueNode node) throws XmlException {
            out.appendEmptyElement(prefix, XML_TRUE, nsURI);
        }
        
        public void writeFalse(FalseNode node) throws XmlException {
            out.appendEmptyElement(prefix, XML_FALSE, nsURI);
        }
        
        public void writeEnum(EnumNode node) throws XmlException {
            out.appendStartElement(prefix, XML_ENUM, nsURI);
            out.appendAttribute(XML_ENUM_TYPE, Types.toString(node.getType()));
            out.appendText(node.getValue().name());
            out.appendEndElement();
        }
        
        public void writeChar(CharNode node) throws XmlException {
            out.appendStartElement(prefix, XML_CHAR, nsURI);
            out.appendText(node.getValue().toString());
            out.appendEndElement();
        }
        
        public void writeNumber(NumberNode node) throws XmlException {
            out.appendStartElement(prefix, XML_NUMBER, nsURI);
            out.appendAttribute(XML_ENUM_TYPE, Types.toString(node.getType()));
            out.appendText(node.getValue().toString());
            out.appendEndElement();
        }
        
        public void writeString(StringNode node) throws XmlException {
            out.appendStartElement(prefix, XML_STRING, nsURI);
            out.appendText(node.getValue());
            out.appendEndElement();
        }
        
        public void writeVariable(VariableNode node) throws XmlException {
            out.appendStartElement(prefix, XML_VARIABLE, nsURI);
            out.appendText(node.getVariableName());
            out.appendEndElement();
        }
        
        public void writeArray(ArrayNode node) throws XmlException {
            out.appendStartElement(prefix, XML_ARRAY, nsURI);
            for (int i = 0, count = node.getItemCount(); i < count; i++)
                node.getItem(i).accept(visitor);
            out.appendEndElement();
        }
        
        public void writeFunction(FunctionNode node) throws XmlException {
            out.appendStartElement(prefix, XML_FUNCTION, nsURI);
            out.appendAttribute(XML_FUNCTION_NAME, node.getFunctionQualifiedName());
            
            out.appendStartElement(prefix, XML_FUNCTION_ARGS, nsURI);
            for (int i = 0, count = node.getArgumentCount(); i < count; i++)
                node.getArgument(i).accept(visitor);
            out.appendEndElement();
            
            out.appendEndElement();
        }
        
        public void writeCast(CastNode node) throws XmlException {
            out.appendStartElement(prefix, XML_CAST, nsURI);
            out.appendAttribute(XML_CAST_TYPE, Types.toString(node.getType()));
            node.getOperand().accept(visitor);
            out.appendEndElement();
        }
        
        public void writeConvert(ConvertNode node) throws XmlException {
            node.getOperand().accept(visitor);
        }
        
        public void writeNegate(NegateNode node) throws XmlException {
            writeUnary(XML_NEG, node);
        }
        
        public void writeAdd(AddNode node) throws XmlException {
            writeBinary(XML_ADD, node);
        }
        
        public void writeSubtract(SubtractNode node) throws XmlException {
            writeBinary(XML_SUB, node);
        }
        
        public void writeMultiply(MultiplyNode node) throws XmlException {
            writeBinary(XML_MUL, node);
        }
        
        public void writeDivide(DivideNode node) throws XmlException {
            writeBinary(XML_DIV, node);
        }
        
        public void writeModulo(ModuloNode node) throws XmlException {
            writeBinary(XML_MOD, node);
        }
        
        public void writeEqual(EqualNode node) throws XmlException {
            writeBinary(XML_EQ, node);
        }
        
        public void writeNotEqual(NotEqualNode node) throws XmlException {
            writeBinary(XML_NE, node);
        }
        
        public void writeGreaterThan(GreaterThanNode node) throws XmlException {
            writeBinary(XML_GT, node);
        }
        
        public void writeGreaterThanEqual(GreaterThanEqualNode node) throws XmlException {
            writeBinary(XML_GE, node);
        }
        
        public void writeLessThan(LessThanNode node) throws XmlException {
            writeBinary(XML_LT, node);
        }
        
        public void writeLessThanEqual(LessThanEqualNode node) throws XmlException {
            writeBinary(XML_LE, node);
        }
        
        public void writeNot(NotNode node) throws XmlException {
            writeUnary(XML_NOT, node);
        }
        
        public void writeAnd(AndNode node) throws XmlException {
            writeBinary(XML_AND, node);
        }
        
        public void writeOr(OrNode node) throws XmlException {
            writeBinary(XML_OR, node);
        }
        
        public void writeCondition(ConditionNode node) throws XmlException {
            out.appendStartElement(prefix, XML_CONDITION, nsURI);
            
            out.appendStartElement(prefix, XML_CONDITION_IF, nsURI);
            node.getOperand1().accept(visitor);
            out.appendEndElement();
            
            out.appendStartElement(prefix, XML_CONDITION_THEN, nsURI);
            node.getOperand2().accept(visitor);
            out.appendEndElement();
            
            out.appendStartElement(prefix, XML_CONDITION_ELSE, nsURI);
            node.getOperand3().accept(visitor);
            out.appendEndElement();
            
            out.appendEndElement();
        }
        
        public void writeConcat(ConcatNode node) throws XmlException {
            writeNary(XML_CONCAT, node);
        }
        
        public void writeIndex(IndexNode node) throws XmlException {
            out.appendStartElement(prefix, XML_INDEX, nsURI);
            
            out.appendStartElement(prefix, XML_INDEX_COLLECTION, nsURI);
            node.getCollection().accept(visitor);
            out.appendEndElement();
            
            out.appendStartElement(prefix, XML_INDEX_KEY, nsURI);
            node.getKey().accept(visitor);
            out.appendEndElement();
            
            out.appendEndElement();
        }
        
        public void writeProperty(PropertyNode node) throws XmlException {
            out.appendStartElement(prefix, XML_PROPERTY, nsURI);
            out.appendAttribute(XML_PROPERTY_NAME, node.getPropertyName());
            
            out.appendStartElement(prefix, XML_PROPERTY_OBJECT, nsURI);
            node.getObject().accept(visitor);
            out.appendEndElement();
            
            out.appendEndElement();
        }
        
        public void writeMethod(MethodNode node) throws XmlException {
            out.appendStartElement(prefix, XML_METHOD, nsURI);
            out.appendAttribute(XML_METHOD_NAME, node.getMethodName());
            
            out.appendStartElement(prefix, XML_METHOD_OBJECT, nsURI);
            node.getObject().accept(visitor);
            out.appendEndElement();
            
            out.appendStartElement(prefix, XML_METHOD_ARGS, nsURI);
            for (int i = 0, count = node.getArgumentCount(); i < count; i++)
                node.getArgument(i).accept(visitor);
            out.appendEndElement();
            
            out.appendEndElement();
        }
        
        // Utility methods
        
        protected void writeUnary(String localName, UnaryNode node) throws XmlException {
            out.appendStartElement(prefix, localName, nsURI);
            node.getOperand().accept(visitor);
            out.appendEndElement();
        }
        
        protected void writeBinary(String localName, BinaryNode node) throws XmlException {
            out.appendStartElement(prefix, localName, nsURI);
            node.getOperand1().accept(visitor);
            node.getOperand2().accept(visitor);
            out.appendEndElement();
        }
        
        protected void writeNary(String localName, NaryNode node) throws XmlException {
            out.appendStartElement(prefix, localName, nsURI);
            for (int i = 0, count = node.getOperandCount(); i < count; i++)
                node.getOperand(i).accept(visitor);
            out.appendEndElement();
        }
        
    }
    
    // Visitor
    
    private static class Visitor implements NodeVisitor<XmlException> {
        
        private final Writer writer;
        
        private Visitor(Writer writer) {
            this.writer = writer;
        }
        
        public void visit(NullNode node) throws XmlException {
            writer.writeNull(node);
        }
        public void visit(TrueNode node) throws XmlException {
            writer.writeTrue(node);
        }
        public void visit(FalseNode node) throws XmlException {
            writer.writeFalse(node);
        }
        public void visit(EnumNode node) throws XmlException {
            writer.writeEnum(node);
        }
        public void visit(CharNode node) throws XmlException {
            writer.writeChar(node);
        }
        public void visit(NumberNode node) throws XmlException {
            writer.writeNumber(node);
        }
        public void visit(StringNode node) throws XmlException {
            writer.writeString(node);
        }
        public void visit(VariableNode node) throws XmlException {
            writer.writeVariable(node);
        }
        public void visit(ArrayNode node) throws XmlException {
            writer.writeArray(node);
        }
        public void visit(FunctionNode node) throws XmlException {
            writer.writeFunction(node);
        }
        public void visit(CastNode node) throws XmlException {
            writer.writeCast(node);
        }
        public void visit(ConvertNode node) throws XmlException {
            writer.writeConvert(node);
        }
        public void visit(NegateNode node) throws XmlException {
            writer.writeNegate(node);
        }
        public void visit(AddNode node) throws XmlException {
            writer.writeAdd(node);
        }
        public void visit(SubtractNode node) throws XmlException {
            writer.writeSubtract(node);
        }
        public void visit(MultiplyNode node) throws XmlException {
            writer.writeMultiply(node);
        }
        public void visit(DivideNode node) throws XmlException {
            writer.writeDivide(node);
        }
        public void visit(ModuloNode node) throws XmlException {
            writer.writeModulo(node);
        }
        public void visit(EqualNode node) throws XmlException {
            writer.writeEqual(node);
        }
        public void visit(NotEqualNode node) throws XmlException {
            writer.writeNotEqual(node);
        }
        public void visit(GreaterThanNode node) throws XmlException {
            writer.writeGreaterThan(node);
        }
        public void visit(GreaterThanEqualNode node) throws XmlException {
            writer.writeGreaterThanEqual(node);
        }
        public void visit(LessThanNode node) throws XmlException {
            writer.writeLessThan(node);
        }
        public void visit(LessThanEqualNode node) throws XmlException {
            writer.writeLessThanEqual(node);
        }
        public void visit(NotNode node) throws XmlException {
            writer.writeNot(node);
        }
        public void visit(AndNode node) throws XmlException {
            writer.writeAnd(node);
        }
        public void visit(OrNode node) throws XmlException {
            writer.writeOr(node);
        }
        public void visit(ConditionNode node) throws XmlException {
            writer.writeCondition(node);
        }
        public void visit(ConcatNode node) throws XmlException {
            writer.writeConcat(node);
        }
        public void visit(IndexNode node) throws XmlException {
            writer.writeIndex(node);
        }
        public void visit(PropertyNode node) throws XmlException {
            writer.writeProperty(node);
        }
        public void visit(MethodNode node) throws XmlException {
            writer.writeMethod(node);
        }
        
    }
    
}
