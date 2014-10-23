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

package org.foxlabs.etk.io;

import java.math.BigInteger;
import java.math.BigDecimal;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import java.io.IOException;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;

import org.foxlabs.etk.node.*;
import org.foxlabs.etk.support.Types;

public class NodeOutputStream extends ObjectOutputStream {
    
    public NodeOutputStream(OutputStream out) throws IOException {
        super(out);
    }
    
    public void writeNode(Node node) throws IOException {
        Writer writer = new Writer();
        writer.write(node);
        writer.writeCITable();
        writer.flush();
    }
    
    public void writeNodes(Node[] nodes) throws IOException {
        Writer writer = new Writer();
        for (Node node : nodes)
            writer.write(node);
        writer.writeCITable();
        writer.flush();
    }
    
    public void writeNodes(Collection<Node> nodes) throws IOException {
        Writer writer = new Writer();
        for (Node node : nodes)
            writer.write(node);
        writer.writeCITable();
        writer.flush();
    }
    
    // Writer
    
    protected class Writer {
        
        private final List<String> ctbl;
        private final Map<String, Integer> cmap;
        private final ByteArrayOutputStream buffer;
        private final DataOutputStream out;
        private final NodeVisitor<IOException> visitor;
        
        private int count = 0;
        
        public Writer() throws IOException {
            this.ctbl = new ArrayList<String>();
            this.cmap = new HashMap<String, Integer>();
            this.buffer = new ByteArrayOutputStream(1024);
            this.out = new DataOutputStream(buffer);
            this.visitor = new Visitor(this);
        }
        
        public void write(Node node) throws IOException {
            node.accept(visitor);
            count++;
        }
        
        public void writeCITable() throws IOException {
            NodeOutputStream.this.writeInt(ctbl.size());
            for (String ci : ctbl)
                NodeOutputStream.this.writeUTF(ci);
        }
        
        public void clearCITable() {
            ctbl.clear();
            cmap.clear();
        }
        
        public int setCIString(String ci) {
            Integer index = cmap.get(ci);
            if (index == null) {
                index = ctbl.size();
                ctbl.add(ci);
                cmap.put(ci, index);
            }
            return index;
        }
        
        public int setCIType(Class<?> type) {
            return setCIString(Types.toString(type));
        }
        
        public void flush() throws IOException {
            out.flush();
            NodeOutputStream.this.writeInt(count);
            NodeOutputStream.this.write(buffer.toByteArray());
            
            count = 0;
            buffer.reset();
        }
        
        // Write methods
        
        public void writeNull(NullNode node) throws IOException {
            writeHeader(node);
        }
        
        public void writeTrue(TrueNode node) throws IOException {
            writeHeader(node);
        }
        
        public void writeFalse(FalseNode node) throws IOException {
            writeHeader(node);
        }
        
        public void writeEnum(EnumNode node) throws IOException {
            writeHeader(node);
            out.writeInt(setCIType(node.getType()));
            out.writeInt(setCIString(node.getValue().name()));
        }
        
        public void writeChar(CharNode node) throws IOException {
            writeHeader(node);
            out.writeInt(node.getValue().charValue());
        }
        
        public void writeNumber(NumberNode node) throws IOException {
            writeHeader(node);
            
            int index = Types.indexOf(node.getType());
            out.writeByte((byte) index);
            
            switch (index) {
                case Types.BYTE:
                    out.writeByte(node.getValue().byteValue());
                    break;
                    
                case Types.SHORT:
                    out.writeShort(node.getValue().shortValue());
                    break;
                    
                case Types.INTEGER:
                    out.writeInt(node.getValue().intValue());
                    break;
                    
                case Types.LONG:
                    out.writeLong(node.getValue().longValue());
                    break;
                    
                case Types.FLOAT:
                    out.writeFloat(node.getValue().floatValue());
                    break;
                    
                case Types.DOUBLE:
                    out.writeDouble(node.getValue().doubleValue());
                    break;
                    
                case Types.BIGINTEGER:
                    out.writeUTF(((BigInteger) node.getValue()).toString());
                    break;
                    
                case Types.BIGDECIMAL:
                    out.writeUTF(((BigDecimal) node.getValue()).toString());
                    break;
                    
                default:
                    throw new InternalError();
            }
        }
        
        public void writeString(StringNode node) throws IOException {
            writeHeader(node);
            out.writeInt(setCIString(node.getValue()));
        }
        
        public void writeVariable(VariableNode node) throws IOException {
            writeHeader(node);
            out.writeInt(setCIString(node.getVariableName()));
        }
        
        public void writeArray(ArrayNode node) throws IOException {
            writeHeader(node);
            
            int itemcount = node.getItemCount();
            out.writeInt(itemcount);
            for (int i = 0; i < itemcount; i++)
                node.getItem(i).accept(visitor);
        }
        
        public void writeFunction(FunctionNode node) throws IOException {
            writeHeader(node);
            
            out.writeInt(setCIString(node.getFunctionLocalName()));
            out.writeInt(setCIString(node.getFunctionNamespace()));
            
            int argcount = node.getArgumentCount();
            out.writeInt(argcount);
            for (int i = 0; i < argcount; i++)
                node.getArgument(i).accept(visitor);
        }
        
        public void writeCast(CastNode node) throws IOException {
            writeHeader(node);
            out.writeInt(setCIType(node.getType()));
            node.getOperand().accept(visitor);
        }
        
        public void writeConvert(ConvertNode node) throws IOException {
            node.getOperand().accept(visitor);
        }
        
        public void writeNegate(NegateNode node) throws IOException {
            writeUnary(node);        
        }
        
        public void writeAdd(AddNode node) throws IOException {
            writeBinary(node);        
        }
        
        public void writeSubtract(SubtractNode node) throws IOException {
            writeBinary(node);        
        }
        
        public void writeMultiply(MultiplyNode node) throws IOException {
            writeBinary(node);        
        }
        
        public void writeDivide(DivideNode node) throws IOException {
            writeBinary(node);        
        }
        
        public void writeModulo(ModuloNode node) throws IOException {
            writeBinary(node);        
        }
        
        public void writeEqual(EqualNode node) throws IOException {
            writeBinary(node);        
        }
        
        public void writeNotEqual(NotEqualNode node) throws IOException {
            writeBinary(node);        
        }
        
        public void writeGreaterThan(GreaterThanNode node) throws IOException {
            writeBinary(node);        
        }
        
        public void writeGreaterThanEqual(GreaterThanEqualNode node) throws IOException {
            writeBinary(node);        
        }
        
        public void writeLessThan(LessThanNode node) throws IOException {
            writeBinary(node);        
        }
        
        public void writeLessThanEqual(LessThanEqualNode node) throws IOException {
            writeBinary(node);        
        }
        
        public void writeNot(NotNode node) throws IOException {
            writeUnary(node);        
        }
        
        public void writeAnd(AndNode node) throws IOException {
            writeBinary(node);        
        }
        
        public void writeOr(OrNode node) throws IOException {
            writeBinary(node);
        }
        
        public void writeCondition(ConditionNode node) throws IOException {
            writeTernary(node);
        }
        
        public void writeConcat(ConcatNode node) throws IOException {
            writeNary(node);
        }
        
        public void writeIndex(IndexNode node) throws IOException {
            writeHeader(node);
            node.getCollection().accept(visitor);
            node.getKey().accept(visitor);
        }
        
        public void writeProperty(PropertyNode node) throws IOException {
            writeHeader(node);
            out.writeInt(setCIString(node.getPropertyName()));
            node.getObject().accept(visitor);
        }
        
        public void writeMethod(MethodNode node) throws IOException {
            writeHeader(node);
            
            out.writeInt(setCIString(node.getMethodName()));
            
            node.getObject().accept(visitor);
            
            int argcount = node.getArgumentCount();
            out.writeInt(argcount);
            for (int i = 0; i < argcount; i++)
                node.getArgument(i).accept(visitor);
            
        }
        
        // Utility methods
        
        protected void writeHeader(Node node) throws IOException {
            out.writeByte((byte) node.getId());
            out.writeInt(node.getSourceLine());
            out.writeInt(node.getSourceColumn());
        }
        
        protected void writeUnary(UnaryNode node) throws IOException {
            writeHeader(node);
            node.getOperand().accept(visitor);
        }
        
        protected void writeBinary(BinaryNode node) throws IOException {
            writeHeader(node);
            node.getOperand1().accept(visitor);
            node.getOperand2().accept(visitor);
        }
        
        protected void writeTernary(TernaryNode node) throws IOException {
            writeHeader(node);
            node.getOperand1().accept(visitor);
            node.getOperand2().accept(visitor);
            node.getOperand3().accept(visitor);
        }
        
        protected void writeNary(NaryNode node) throws IOException {
            writeHeader(node);
            int opcount = node.getOperandCount();
            out.writeInt(opcount);
            for (int i = 0; i < opcount; i++)
                node.getOperand(i).accept(visitor);
        }
        
    }
    
    // Visitor
    
    private static class Visitor implements NodeVisitor<IOException> {
        
        private final Writer writer;
        
        private Visitor(Writer writer) {
            this.writer = writer;
        }
        
        public void visit(NullNode node) throws IOException {
            writer.writeNull(node);
        }
        public void visit(TrueNode node) throws IOException {
            writer.writeTrue(node);
        }
        public void visit(FalseNode node) throws IOException {
            writer.writeFalse(node);
        }
        public void visit(EnumNode node) throws IOException {
            writer.writeEnum(node);
        }
        public void visit(CharNode node) throws IOException {
            writer.writeChar(node);
        }
        public void visit(NumberNode node) throws IOException {
            writer.writeNumber(node);
        }
        public void visit(StringNode node) throws IOException {
            writer.writeString(node);
        }
        public void visit(VariableNode node) throws IOException {
            writer.writeVariable(node);
        }
        public void visit(ArrayNode node) throws IOException {
            writer.writeArray(node);
        }
        public void visit(FunctionNode node) throws IOException {
            writer.writeFunction(node);
        }
        public void visit(CastNode node) throws IOException {
            writer.writeCast(node);
        }
        public void visit(ConvertNode node) throws IOException {
            writer.writeConvert(node);
        }
        public void visit(NegateNode node) throws IOException {
            writer.writeNegate(node);
        }
        public void visit(AddNode node) throws IOException {
            writer.writeAdd(node);
        }
        public void visit(SubtractNode node) throws IOException {
            writer.writeSubtract(node);
        }
        public void visit(MultiplyNode node) throws IOException {
            writer.writeMultiply(node);
        }
        public void visit(DivideNode node) throws IOException {
            writer.writeDivide(node);
        }
        public void visit(ModuloNode node) throws IOException {
            writer.writeModulo(node);
        }
        public void visit(EqualNode node) throws IOException {
            writer.writeEqual(node);
        }
        public void visit(NotEqualNode node) throws IOException {
            writer.writeNotEqual(node);
        }
        public void visit(GreaterThanNode node) throws IOException {
            writer.writeGreaterThan(node);
        }
        public void visit(GreaterThanEqualNode node) throws IOException {
            writer.writeGreaterThanEqual(node);
        }
        public void visit(LessThanNode node) throws IOException {
            writer.writeLessThan(node);
        }
        public void visit(LessThanEqualNode node) throws IOException {
            writer.writeLessThanEqual(node);
        }
        public void visit(NotNode node) throws IOException {
            writer.writeNot(node);
        }
        public void visit(AndNode node) throws IOException {
            writer.writeAnd(node);
        }
        public void visit(OrNode node) throws IOException {
            writer.writeOr(node);
        }
        public void visit(ConditionNode node) throws IOException {
            writer.writeCondition(node);
        }
        public void visit(ConcatNode node) throws IOException {
            writer.writeConcat(node);
        }
        public void visit(IndexNode node) throws IOException {
            writer.writeIndex(node);
        }
        public void visit(PropertyNode node) throws IOException {
            writer.writeProperty(node);
        }
        public void visit(MethodNode node) throws IOException {
            writer.writeMethod(node);
        }
        
    }
    
}
