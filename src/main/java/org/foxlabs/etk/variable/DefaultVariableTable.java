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

package org.foxlabs.etk.variable;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.foxlabs.etk.DefaultEnvironment;
import org.foxlabs.etk.Environment;
import org.foxlabs.etk.EvaluationException;
import org.foxlabs.etk.Expression;
import org.foxlabs.etk.codec.ExpressionDecoder;
import org.foxlabs.etk.node.Node;

public class DefaultVariableTable implements MutableVariableTable {
    
    protected transient Map<String, Variable> table;
    private final boolean caseSensitive;
    
    public DefaultVariableTable() {
        this(false);
    }
    
    public DefaultVariableTable(boolean caseSensitive) {
        this.table = new ConcurrentHashMap<String, Variable>();
        this.caseSensitive = caseSensitive;
    }
    
    public final boolean isCaseSensitive() {
        return caseSensitive;
    }
    
    public boolean hasVariable(String name) {
        return table.containsKey(internalNameOf(name));
    }
    
    public Iterator<String> getVariableNames() {
        return new NameItr();
    }
    
    private final class NameItr implements Iterator<String> {
        Iterator<Variable> itr = table.values().iterator();
        public boolean hasNext() {
            return itr.hasNext();
        }
        public String next() {
            return itr.next().name;
        }
        public void remove() {
            itr.remove();
        }
    }
    
    public Class<?> getVariableType(String name)
            throws VariableNotFoundException {
        return lookupVariable(name).getType();
    }
    
    public Object resolveVariable(String name, Environment context)
            throws VariableNotFoundException, EvaluationException {
        return lookupVariable(name).resolve(context);
    }
    
    public void putVariable(String name, Object value) {
        if (value instanceof Expression) {
            putVariable(name, (Expression) value);
        } else if (value instanceof Node) {
            putVariable(name, (Node) value);
        } else {
            putVariable(name, new Const(name, value));
        }
    }
    
    public void putVariable(String name, Node root) {
        putVariable(name, new Expr(name, root));
    }
    
    public void putVariable(String name, Expression expr) {
        putVariable(name, new Expr(name, expr));
    }
    
    public void putVariable(String name, String source) {
        putVariable(name, source, DefaultEnvironment.getGlobalContext());
    }
    
    public void putVariable(String name, String source, Environment context) {
        Node root = ExpressionDecoder.getDefault().decode(source, context);
        putVariable(name, new Expr(name, new Expression(root, source)));
    }
    
    private void putVariable(String name, Variable var) {
        if (name == null)
            throw new NullPointerException();
        table.put(internalNameOf(var.name), var);
    }
    
    public boolean removeVariable(String name) {
        return table.remove(internalNameOf(name)) != null;
    }
    
    public void removeAll() {
        table.clear();
    }
    
    protected final String internalNameOf(String name) {
        return caseSensitive ? name : name.toUpperCase();
    }
    
    protected Variable lookupVariable(String name)
            throws VariableNotFoundException {
        Variable var = table.get(internalNameOf(name));
        if (var == null)
            throw new VariableNotFoundException(name);
        return var;
    }
    
    public String toString() {
        StringBuilder buf = new StringBuilder();
        for (Variable var : table.values()) {
            buf.append(var);
            buf.append('\n');
        }
        return buf.toString();
    }
    
    // Variable
    
    protected static abstract class Variable {
        
        protected final String name;
        
        public Variable(String name) {
            this.name = name;
        }
        
        public final String getName() {
            return name;
        }
        
        public abstract Class<?> getType();
        
        public abstract Object resolve(Environment context) throws EvaluationException;
        
    }
    
    // Constant
    
    protected static class Const extends Variable {
        
        protected final Object value;
        
        public Const(String name, Object value) {
            super(name);
            this.value = value;
        }
        
        public Class<?> getType() {
            return value == null ? null : value.getClass();
        }
        
        public Object resolve(Environment context) {
            return value;
        }
        
        public String toString() {
            return name + "=" + value;
        }
        
    }
    
    // Expression
    
    protected static class Expr extends Variable {
        
        protected final Expression expr;
        
        public Expr(String name, Expression expr) {
            super(name);
            this.expr = expr;
        }
        
        public Expr(String name, Node root) {
            super(name);
            this.expr = new Expression(root);
        }
        
        public Class<?> getType() {
            return expr.getType();
        }
        
        public Object resolve(Environment context) throws EvaluationException {
            try {
                return expr.evaluate(context);
            } catch (EvaluationException e) {
                e.setOwnerName(name);
                throw e;
            }
        }
        
        public String toString() {
            return name + "=" + expr;
        }
        
    }
    
}
