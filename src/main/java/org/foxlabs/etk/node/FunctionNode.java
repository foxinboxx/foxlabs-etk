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
import org.foxlabs.etk.function.Function;

public final class FunctionNode extends Node {
    
    private final Function function;
    private final Node[] arguments;
    
    private transient int hash = 0;
    
    FunctionNode(Function function, Node... arguments) {
        this.function = function;
        this.arguments = arguments;
    }
    
    public int getId() {
        return FUNCTION_NODE;
    }
    
    public Function getFunction() {
        return function;
    }
    
    public String getFunctionNamespace() {
        return function.getNamespace();
    }
    
    public String getFunctionLocalName() {
        return function.getLocalName();
    }
    
    public String getFunctionQualifiedName() {
        return function.getQualifiedName();
    }
    
    public Class<?> getType() {
        return function.getType();
    }
    
    public int getArgumentCount() {
        return arguments.length;
    }
    
    public Node getArgument(int index) {
        return arguments[index];
    }
    
    public Node[] getArguments() {
        return arguments.length == 0 ? arguments : arguments.clone();
    }
    
    public Object evaluate(Environment context) throws EvaluationException {
        int argcount = arguments.length;
        
        try {
            if (argcount == 0)
                return function.evaluate(context);
        } catch (Exception e) {
            throw new EvaluationException(this, e);
        }
        
        Object[] argumentObjs = new Object[arguments.length];
        for (int i = 0; i < argcount; i++)
            argumentObjs[i] = arguments[i].evaluate(context);
        
        try {
            return function.evaluate(context, argumentObjs);
        } catch (Exception e) {
            throw new EvaluationException(this, e);
        }
    }
    
    public <T extends Throwable> void accept(NodeVisitor<T> visitor) throws T {
        visitor.visit(this);
    }
    
    public boolean isDetermined() {
        if (!function.isDetermined())
            return false;
        
        for (Node argument : arguments)
            if (!argument.isDetermined())
                return false;
        
        return true;
    }
    
    public int hashCode() {
        if (hash == 0) {
            hash = getId() ^ function.hashCode();
            for (Node argument : arguments)
                hash = 31 * hash + argument.hashCode();
        }
        return hash;
    }
    
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        
        if (obj instanceof FunctionNode) {
            FunctionNode other = (FunctionNode) obj;
            int argcount = arguments.length;
            if (argcount == other.arguments.length) {
                if (function.equals(other.function)) {
                    for (int i = 0; i < argcount; i++)
                        if (!arguments[i].equals(other.arguments[i]))
                            return false;
                    return true;
                }
            }
        }
        
        return false;
    }
    
}
