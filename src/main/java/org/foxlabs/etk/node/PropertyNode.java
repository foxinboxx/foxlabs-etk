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
import org.foxlabs.etk.EvaluationException;
import org.foxlabs.etk.bean.PropertyAccessor;
import org.foxlabs.etk.support.Operator;

public final class PropertyNode extends Node implements OperatorNode {
    
    private final Node object;
    private final PropertyAccessor accessor;
    
    PropertyNode(PropertyAccessor accessor, Node object) {
        this.object = object;
        this.accessor = accessor;
    }
    
    public int getId() {
        return PROPERTY_NODE;
    }
    
    public int getPrecedence() {
        return operatorPrecedences[getId()];
    }
    
    public String getSymbol() {
        return operatorSymbols[getId()];
    }
    
    public String getPropertyName() {
        return accessor.getName();
    }
    
    public Class<?> getType() {
        return accessor.getType();
    }
    
    public Node getObject() {
        return object;
    }
    
    public Object evaluate(Environment context) throws EvaluationException {
        Object obj = object.evaluate(context);
        try {
            return accessor.resolve(context, Operator.safeValueOf(obj));
        } catch (Throwable t) {
            throw new EvaluationException(this, t);
        }
    }
    
    public <T extends Throwable> void accept(NodeVisitor<T> visitor) throws T {
        visitor.visit(this);
    }
    
    public boolean isDetermined() {
        return accessor.isDetermined() &&
               object.isDetermined();
    }
    
    public int hashCode() {
        int hash = getId();
        hash = 31 * hash + getPropertyName().hashCode();
        hash = 31 * hash + object.hashCode();
        return hash;
    }
    
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        
        if (obj instanceof PropertyNode) {
            PropertyNode other = (PropertyNode) obj;
            return getType() == other.getType() &&
                   getPropertyName().equals(other.getPropertyName()) &&
                   object.equals(other.object);
        }
        
        return false;
    }
    
}
