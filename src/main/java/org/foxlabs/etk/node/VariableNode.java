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

public final class VariableNode extends Node {
    
    private final Class<?> type;
    private final String name;
    
    VariableNode(Class<?> type, String name) {
        this.type = type;
        this.name = name;
    }
    
    public int getId() {
        return VARIABLE_NODE;
    }
    
    public Class<?> getType() {
        return type;
    }
    
    public String getVariableName() {
        return name;
    }
    
    public Object evaluate(Environment context) {
        try {
            return context.getVariableTable().resolveVariable(name, context);
        } catch (Exception e) {
            throw new EvaluationException(this, e);
        }
    }
    
    public <T extends Throwable> void accept(NodeVisitor<T> visitor) throws T {
        visitor.visit(this);
    }
    
    public boolean isDetermined() {
        return false;
    }
    
    public int hashCode() {
        int hash = getId();
        hash = 31 * hash + type.hashCode();
        hash = 31 * hash + name.hashCode();
        return hash;
    }
    
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        
        if (obj instanceof VariableNode) {
            VariableNode other = (VariableNode) obj;
            return getType() == other.getType() &&
                   name.equals(other.name);
        }
        
        return false;
    }
    
}
