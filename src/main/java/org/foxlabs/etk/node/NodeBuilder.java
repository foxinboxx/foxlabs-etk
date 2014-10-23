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

package org.foxlabs.etk.node;

import org.foxlabs.etk.Environment;
import org.foxlabs.etk.EtkException;
import org.foxlabs.etk.EvaluationException;
import org.foxlabs.etk.Expression;
import org.foxlabs.etk.bean.BeanManager;
import org.foxlabs.etk.function.FunctionLoader;
import org.foxlabs.etk.support.Signature;
import org.foxlabs.etk.variable.VariableTable;

public final class NodeBuilder {
    
    private static final int INITIAL_CAPACITY = 20;
    
    private Environment context;
    
    private Node[] oldQueue = new Node[INITIAL_CAPACITY];
    private Node[] newQueue = new Node[INITIAL_CAPACITY];
    
    private int oldSize = 0;
    private int newSize = 0;
    private int popSize = 0;
    
    private int[] marks = new int[10];
    private int marker = 0;
    
    public NodeBuilder(Environment context) {
        if (context == null)
            throw new NullPointerException();
        this.context = context;
    }
    
    public Environment getEnvironment() {
        return context;
    }
    
    public boolean isEmpty() {
        return size() == 0;
    }
    
    public int size() {
        return marker > 0 ? oldSize - marks[marker - 1] : oldSize;
    }
    
    public void reverse(int count) {
        if (oldSize < count)
            throw new IllegalStateException();
        
        for (int i = oldSize - count, j = oldSize - 1; i < j; i++, j--) {
            Node swap = oldQueue[i];
            oldQueue[i] = newQueue[i] = oldQueue[j];
            oldQueue[j] = newQueue[j] = swap;
        }
    }
    
    public void cleanup() {
        for (int i = 0; i < oldSize; i++)
            oldQueue[i] = newQueue[i] = null;
        
        oldSize = newSize = popSize = 0;
        marker = 0;
    }
    
    public NodeBuilder mark() {
        if (marker == marks.length) {
            int[] swap = new int[marks.length * 3 / 2 + 1];
            System.arraycopy(marks, 0, swap, 0, marker);
            marks = swap;
        }
        marks[marker++] = newSize;
        return this;
    }
    
    public NodeBuilder unmark() {
        if (marker == 0)
            throw new IllegalStateException();
        marker--;
        return this;
    }
    
    private void push(Node node) {
        ensureCapacity(1);
        newQueue[newSize++] = node;
    }
    
    private Node pop() {
        if (newSize == 0 || (marker > 0 && marks[marker - 1] == newSize))
            throw new IllegalStateException();
        
        Node result = newQueue[--newSize];
        newQueue[newSize] = null;
        
        if (popSize > newSize)
            popSize = newSize;
        
        return result;
    }
    
    private Node[] popn() {
        int count = marker > 0 ? newSize - marks[marker - 1] : newSize;
        if (count < 0)
            throw new IllegalStateException();
        
        Node[] result = new Node[count];
        for (int i = 0, j = newSize - count; i < count; i++, j++) {
            result[i] = newQueue[j];
            newQueue[j] = null;
        }
        
        newSize -= count;
        if (popSize > newSize)
            popSize = newSize;
        
        if (marker > 0)
            marker--;
        
        return result;
    }
    
    private void commit() {
        if (newSize > popSize)
            System.arraycopy(newQueue, popSize, oldQueue, popSize, newSize - popSize);
        
        for (int i = newSize; i < oldSize; i++)
            oldQueue[i] = null;
        
        oldSize = popSize = newSize;
    }
    
    private void rollback() {
        if (oldSize > popSize)
            System.arraycopy(oldQueue, popSize, newQueue, popSize, oldSize - popSize);
        
        for (int i = oldSize; i < newSize; i++)
            newQueue[i] = null;
        
        newSize = popSize = oldSize;
    }
    
    private void ensureCapacity(int count) {
        if (newSize + count > newQueue.length) {
            int capacity = newQueue.length * 3 / 2 + count;
            
            Node[] oldTemp = new Node[capacity];
            Node[] newTemp = new Node[capacity];
            
            System.arraycopy(oldQueue, 0, oldTemp, 0, oldSize);
            System.arraycopy(newQueue, 0, newTemp, 0, newSize);
            
            oldQueue = oldTemp;
            newQueue = newTemp;
        }
    }
    
    public NodeBuilder pushNode(Node node) {
        push(node);
        commit();
        return this;
    }
    
    public Node popNode() {
        Node node = pop();
        commit();
        return node;
    }
    
    public Node getNode() {
        if (newSize == 0)
            throw new IllegalStateException();
        
        return newQueue[newSize - 1];
    }
    
    public NodeBuilder pushExpression(Expression expr) {
        return pushNode(expr.getRootNode());
    }
    
    public Expression popExpression() {
        return new Expression(popNode());
    }
    
    public NodeBuilder pushResult()
            throws EtkException, EvaluationException {
        try {
            push(NodeFactory.createLiteralNode(pop().evaluate(context)));
            commit();
            return this;
        } finally {
            rollback();
        }
    }
    
    public NodeBuilder pushNull() {
        push(NodeFactory.createNullNode());
        commit();
        return this;
    }
    
    public NodeBuilder pushLiteral(boolean value) {
        push(NodeFactory.createLiteralNode(value));
        commit();
        return this;
    }
    
    public NodeBuilder pushLiteral(char value) {
        push(NodeFactory.createLiteralNode(value));
        commit();
        return this;
    }
    
    public NodeBuilder pushLiteral(byte value) {
        push(NodeFactory.createLiteralNode(value));
        commit();
        return this;
    }
    
    public NodeBuilder pushLiteral(short value) {
        push(NodeFactory.createLiteralNode(value));
        commit();
        return this;
    }
    
    public NodeBuilder pushLiteral(int value) {
        push(NodeFactory.createLiteralNode(value));
        commit();
        return this;
    }
    
    public NodeBuilder pushLiteral(long value) {
        push(NodeFactory.createLiteralNode(value));
        commit();
        return this;
    }
    
    public NodeBuilder pushLiteral(float value) {
        push(NodeFactory.createLiteralNode(value));
        commit();
        return this;
    }
    
    public NodeBuilder pushLiteral(double value) {
        push(NodeFactory.createLiteralNode(value));
        commit();
        return this;
    }
    
    public NodeBuilder pushLiteral(Object value) throws EtkException {
        push(NodeFactory.createLiteralNode(value));
        commit();
        return this;
    }
    
    public NodeBuilder pushVariable(String name) throws EtkException {
        VariableTable table = context.getVariableTable();
        if (table == null)
            throw new UnsupportedOperationException();
        push(NodeFactory.createVariableNode(table, name));
        commit();
        return this;
    }
    
    public NodeBuilder negate() throws EtkException {
        try {
            push(NodeFactory.createNegateNode(pop()));
            commit();
            return this;
        } finally {
            rollback();
        }
    }
    
    public NodeBuilder add() throws EtkException {
        try {
            Node operand2 = pop();
            Node operand1 = pop();
            push(NodeFactory.createAddNode(operand1, operand2));
            commit();
            return this;
        } finally {
            rollback();
        }
    }
    
    public NodeBuilder subtract() throws EtkException {
        try {
            Node operand2 = pop();
            Node operand1 = pop();
            push(NodeFactory.createSubtractNode(operand1, operand2));
            commit();
            return this;
        } finally {
            rollback();
        }
    }
    
    public NodeBuilder multiply() throws EtkException {
        try {
            Node operand2 = pop();
            Node operand1 = pop();
            push(NodeFactory.createMultiplyNode(operand1, operand2));
            commit();
            return this;
        } finally {
            rollback();
        }
    }
    
    public NodeBuilder divide() throws EtkException {
        try {
            Node operand2 = pop();
            Node operand1 = pop();
            push(NodeFactory.createDivideNode(operand1, operand2));
            commit();
            return this;
        } finally {
            rollback();
        }
    }
    
    public NodeBuilder modulo() throws EtkException {
        try {
            Node operand2 = pop();
            Node operand1 = pop();
            push(NodeFactory.createModuloNode(operand1, operand2));
            commit();
            return this;
        } finally {
            rollback();
        }
    }
    
    public NodeBuilder eq() throws EtkException {
        try {
            Node operand2 = pop();
            Node operand1 = pop();
            push(NodeFactory.createEqualNode(operand1, operand2));
            commit();
            return this;
        } finally {
            rollback();
        }
    }
    
    public NodeBuilder ne() throws EtkException {
        try {
            Node operand2 = pop();
            Node operand1 = pop();
            push(NodeFactory.createNotEqualNode(operand1, operand2));
            commit();
            return this;
        } finally {
            rollback();
        }
    }
    
    public NodeBuilder gt() throws EtkException {
        try {
            Node operand2 = pop();
            Node operand1 = pop();
            push(NodeFactory.createGreaterThanNode(operand1, operand2));
            commit();
            return this;
        } finally {
            rollback();
        }
    }
    
    public NodeBuilder ge() throws EtkException {
        try {
            Node operand2 = pop();
            Node operand1 = pop();
            push(NodeFactory.createGreaterThanEqualNode(operand1, operand2));
            commit();
            return this;
        } finally {
            rollback();
        }
    }
    
    public NodeBuilder lt() throws EtkException {
        try {
            Node operand2 = pop();
            Node operand1 = pop();
            push(NodeFactory.createLessThanNode(operand1, operand2));
            commit();
            return this;
        } finally {
            rollback();
        }
    }
    
    public NodeBuilder le() throws EtkException {
        try {
            Node operand2 = pop();
            Node operand1 = pop();
            push(NodeFactory.createLessThanEqualNode(operand1, operand2));
            commit();
            return this;
        } finally {
            rollback();
        }
    }
    
    public NodeBuilder not() throws EtkException {
        try {
            push(NodeFactory.createNotNode(pop()));
            commit();
            return this;
        } finally {
            rollback();
        }
    }
    
    public NodeBuilder and() throws EtkException {
        try {
            Node operand2 = pop();
            Node operand1 = pop();
            push(NodeFactory.createAndNode(operand1, operand2));
            commit();
            return this;
        } finally {
            rollback();
        }
    }
    
    public NodeBuilder or() throws EtkException {
        try {
            Node operand2 = pop();
            Node operand1 = pop();
            push(NodeFactory.createOrNode(operand1, operand2));
            commit();
            return this;
        } finally {
            rollback();
        }
    }
    
    public NodeBuilder condition() throws EtkException {
        try {
            Node case2 = pop();
            Node case1 = pop();
            Node condition = pop();
            push(NodeFactory.createConditionNode(condition, case1, case2));
            commit();
            return this;
        } finally {
            rollback();
        }
    }
    
    public NodeBuilder index() throws EtkException {
        try {
            Node key = pop();
            Node collection = pop();
            push(NodeFactory.createIndexNode(collection, key));
            commit();
            return this;
        } finally {
            rollback();
        }
    }
    
    public NodeBuilder cast(Class<?> toType) throws EtkException {
        try {
            push(NodeFactory.createCastNode(pop(), toType));
            commit();
            return this;
        } finally {
            rollback();
        }
    }
    
    public NodeBuilder array() throws EtkException {
        try {
            push(NodeFactory.createArrayNode(popn()));
            commit();
            return this;
        } finally {
            rollback();
        }
    }
    
    public NodeBuilder concat() throws EtkException {
        try {
            push(NodeFactory.createConcatNode(popn()));
            commit();
            return this;
        } finally {
            rollback();
        }
    }
    
    public NodeBuilder function(String name)
            throws EtkException {
        return function(name, Signature.NULL_NS);
    }
    
    public NodeBuilder function(String name, String namespace)
            throws EtkException {
        FunctionLoader loader = context.getFunctionLoader();
        if (loader == null)
            throw new UnsupportedOperationException();
        try {
            push(NodeFactory.createFunctionNode(loader, name, namespace, popn()));
            commit();
            return this;
        } finally {
            rollback();
        }
    }
    
    public NodeBuilder property(String name) throws EtkException {
        BeanManager manager = context.getBeanManager();
        if (manager == null)
            throw new UnsupportedOperationException();
        try {
            Node object = pop();
            push(NodeFactory.createPropertyNode(manager, name, object));
            commit();
            return this;
        } finally {
            rollback();
        }
    }
    
    public NodeBuilder method(String name) throws EtkException {
        BeanManager manager = context.getBeanManager();
        if (manager == null)
            throw new UnsupportedOperationException();
        try {
            Node[] arguments = popn();
            Node object = pop();
            push(NodeFactory.createMethodNode(manager, name, object, arguments));
            commit();
            return this;
        } finally {
            rollback();
        }
    }
    
}
