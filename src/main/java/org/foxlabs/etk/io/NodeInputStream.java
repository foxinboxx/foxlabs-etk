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

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import org.foxlabs.etk.Environment;
import org.foxlabs.etk.EtkException;
import org.foxlabs.etk.node.*;
import org.foxlabs.etk.resource.ResourceManager;
import org.foxlabs.etk.support.Types;

import static org.foxlabs.etk.node.Node.*;

public class NodeInputStream extends ObjectInputStream {
    
    public NodeInputStream(InputStream in) throws IOException {
        super(in);
    }
    
    public Node readNode(Environment context) throws IOException {
        Reader reader = new Reader(context);
        reader.readCITable();
        readInt();
        return reader.read();
    }
    
    public Node[] readNodes(Environment context) throws IOException {
        Reader reader = new Reader(context);
        reader.readCITable();
        Node[] nodes = new Node[readInt()];
        for (int i = 0; i < nodes.length; i++)
            nodes[i] = reader.read();
        return nodes;
    }
    
    public int readNodes(Collection<Node> nodes, Environment context) throws IOException {
        Reader reader = new Reader(context);
        reader.readCITable();
        int count = readInt();
        for (int i = 0; i < count; i++)
            nodes.add(reader.read());
        return count;
    }
    
    protected class Reader {
        
        protected final ObjectInputStream in;
        protected final Environment context;
        protected final NodeBuilder builder;
        
        private String[] ctbl;
        
        public Reader(Environment context) {
            this.in = NodeInputStream.this;
            this.context = context;
            this.builder = new NodeBuilder(context);
        }
        
        public Node read() throws IOException {
            try {
                readNode();
            } catch (EtkException e) {
                throw new IOException(e);
            }
            return builder.popNode();
        }
        
        public void readCITable() throws IOException {
            int size = in.readInt();
            ctbl = new String[size];
            for (int i = 0; i < size; i++)
                ctbl[i] = in.readUTF();
        }
        
        public String getCIString(int index) throws IOException {
            if (ctbl == null)
                throw new IllegalStateException();
            try {
                return ctbl[index];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw ResourceManager.newIOException(
                        "io.corruptedInputStream");
            }
        }
        
        public Class<?> getCIType(int index) throws IOException {
            String name = getCIString(index);
            return context.resolveType(name);
        }
        
        // Read methods
        
        public void readNode() throws IOException {
            int nodeId = in.readByte();
            int line = in.readInt();
            int column = in.readInt();
            
            switch (nodeId) {
                case NULL_NODE:
                    readNull();
                    break;
                    
                case TRUE_NODE:
                    readTrue();
                    break;
                    
                case FALSE_NODE:
                    readFalse();
                    break;
                    
                case ENUM_NODE:
                    readEnum();
                    break;
                    
                case CHAR_NODE:
                    readChar();
                    break;
                    
                case NUMBER_NODE:
                    readNumber();
                    break;
                    
                case STRING_NODE:
                    readString();
                    break;
                    
                case VARIABLE_NODE:
                    readVariable();
                    break;
                    
                case ARRAY_NODE:
                    readArray();
                    break;
                    
                case FUNCTION_NODE:
                    readFunction();
                    break;
                    
                case CAST_NODE:
                    readCast();
                    break;
                    
                case NEG_NODE:
                    readNegate();
                    break;
                    
                case ADD_NODE:
                    readAdd();
                    break;
                    
                case SUB_NODE:
                    readSubtract();
                    break;
                    
                case MUL_NODE:
                    readMultiply();
                    break;
                    
                case DIV_NODE:
                    readDivide();
                    break;
                    
                case MOD_NODE:
                    readModulo();
                    break;
                    
                case EQ_NODE:
                    readEqual();
                    break;
                    
                case NE_NODE:
                    readNotEqual();
                    break;
                    
                case GT_NODE:
                    readGreaterThan();
                    break;
                    
                case GE_NODE:
                    readGreaterThanEqual();
                    break;
                    
                case LT_NODE:
                    readLessThan();
                    break;
                    
                case LE_NODE:
                    readLessThanEqual();
                    break;
                    
                case NOT_NODE:
                    readNot();
                    break;
                    
                case AND_NODE:
                    readAnd();
                    break;
                    
                case OR_NODE:
                    readOr();
                    break;
                    
                case CONDITION_NODE:
                    readCondition();
                    break;
                    
                case CONCAT_NODE:
                    readConcat();
                    break;
                    
                case INDEX_NODE:
                    readIndex();
                    break;
                    
                case PROPERTY_NODE:
                    readProperty();
                    break;
                    
                case METHOD_NODE:
                    readMethod();
                    break;
                    
                default:
                    throw ResourceManager.newIOException(
                            "io.corruptedInputStream");
            }
            
            Node node = builder.getNode();
            node.setSourcePosition(line, column);
        }
        
        public void readNull() throws IOException {
            builder.pushNull();
        }
        
        public void readTrue() throws IOException {
            builder.pushLiteral(true);
        }
        
        public void readFalse() throws IOException {
            builder.pushLiteral(false);
        }
        
        public void readEnum() throws IOException {
            Class<?> type = getCIType(in.readInt());
            String name = getCIString(in.readInt());
            Enum<?> value = context.resolveEnum(type, name);
            builder.pushLiteral(value);
        }
        
        public void readChar() throws IOException {
            int value = in.readInt();
            builder.pushLiteral((char) value);
        }
        
        public void readNumber() throws IOException {
            int index = readByte();
            switch (index) {
                case Types.BYTE:
                    builder.pushLiteral(in.readByte());
                    break;
                    
                case Types.SHORT:
                    builder.pushLiteral(in.readShort());
                    break;
                    
                case Types.INTEGER:
                    builder.pushLiteral(in.readInt());
                    break;
                    
                case Types.LONG:
                    builder.pushLiteral(in.readLong());
                    break;
                    
                case Types.FLOAT:
                    builder.pushLiteral(in.readFloat());
                    break;
                    
                case Types.DOUBLE:
                    builder.pushLiteral(in.readDouble());
                    break;
                    
                case Types.BIGINTEGER:
                    builder.pushLiteral(new BigInteger(in.readUTF()));
                    break;
                    
                case Types.BIGDECIMAL:
                    builder.pushLiteral(new BigDecimal(in.readUTF()));
                    break;
                    
                default:
                    throw ResourceManager.newIOException(
                            "io.corruptedInputStream");
            }
        }
        
        public void readString() throws IOException {
            String value = getCIString(in.readInt());
            builder.pushLiteral(value);
        }
        
        public void readVariable() throws IOException {
            String name = getCIString(in.readInt());
            builder.pushVariable(name);
        }
        
        public void readArray() throws IOException {
            builder.mark();
            int itemcount = in.readInt();
            for (int i = 0; i < itemcount; i++)
                readNode();
            builder.array();
        }
        
        public void readFunction() throws IOException {
            String name = getCIString(in.readInt());
            String namespace = getCIString(in.readInt());
            builder.mark();
            int argcount = in.readInt();
            for (int i = 0; i < argcount; i++)
                readNode();
            builder.function(name, namespace);
        }
        
        public void readCast() throws IOException {
            Class<?> type = getCIType(in.readInt());
            readNode();
            builder.cast(type);
        }
        
        public void readNegate() throws IOException {
            readUnary();
            builder.negate();
        }
        
        public void readAdd() throws IOException {
            readBinary();
            builder.add();
        }
        
        public void readSubtract() throws IOException {
            readBinary();
            builder.subtract();
        }
        
        public void readMultiply() throws IOException {
            readBinary();
            builder.multiply();
        }
        
        public void readDivide() throws IOException {
            readBinary();
            builder.divide();
        }
        
        public void readModulo() throws IOException {
            readBinary();
            builder.modulo();
        }
        
        public void readEqual() throws IOException {
            readBinary();
            builder.eq();
        }
        
        public void readNotEqual() throws IOException {
            readBinary();
            builder.ne();
        }
        
        public void readGreaterThan() throws IOException {
            readBinary();
            builder.gt();
        }
        
        public void readGreaterThanEqual() throws IOException {
            readBinary();
            builder.ge();
        }
        
        public void readLessThan() throws IOException {
            readBinary();
            builder.lt();
        }
        
        public void readLessThanEqual() throws IOException {
            readBinary();
            builder.le();
        }
        
        public void readNot() throws IOException {
            readUnary();
            builder.not();
        }
        
        public void readAnd() throws IOException {
            readBinary();
            builder.and();
        }
        
        public void readOr() throws IOException {
            readBinary();
            builder.or();
        }
        
        public void readCondition() throws IOException {
            readTernary();
            builder.condition();
        }
        
        public void readConcat() throws IOException {
            builder.mark();
            readNary();
            builder.concat();
        }
        
        public void readIndex() throws IOException {
            readNode();
            readNode();
            builder.index();
        }
        
        public void readProperty() throws IOException {
            String name = getCIString(in.readInt());
            readNode();
            builder.property(name);
        }
        
        public void readMethod() throws IOException {
            String name = getCIString(in.readInt());
            readNode();
            builder.mark();
            int argcount = in.readInt();
            for (int i = 0; i < argcount; i++)
                readNode();
            builder.method(name);
        }
        
        // Utility methods
        
        protected void readUnary() throws IOException {
            readNode();
        }
        
        protected void readBinary() throws IOException {
            readNode();
            readNode();
        }
        
        protected void readTernary() throws IOException {
            readNode();
            readNode();
            readNode();
        }
        
        protected int readNary() throws IOException {
            int count = in.readInt();
            for (int i = 0; i < count; i++)
                readNode();
            return count;
        }
        
    }
    
}
