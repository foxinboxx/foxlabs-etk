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
import org.foxlabs.etk.bean.MethodAccessor;
import org.foxlabs.etk.support.Operator;
import org.foxlabs.etk.support.Signature;

public final class MethodNode extends Node implements OperatorNode {
    
    private final Node object;
    private final Node[] arguments;
    private final MethodAccessor accessor;
    
    private transient int hash = 0;
    
    MethodNode(MethodAccessor accessor, Node object, Node... arguments) {
        this.object = object;
        this.arguments = arguments;
        this.accessor = accessor;
    }
    
    public int getId() {
        return METHOD_NODE;
    }
    
    public int getPrecedence() {
        return operatorPrecedences[getId()];
    }
    
    public String getSymbol() {
        return operatorSymbols[getId()];
    }
    
    public Signature getSignature() {
        return accessor.getSignature();
    }
    
    public String getMethodName() {
        return accessor.getSignature().getLocalName();
    }
    
    public Class<?> getType() {
        return accessor.getType();
    }
    
    public Node getObject() {
        return object;
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
        Object obj = object.evaluate(context);
        
        int argcount = arguments.length;
        if (argcount == 0) {
            try {
                return accessor.invoke(context, Operator.safeValueOf(obj));
            } catch (Throwable t) {
                throw new EvaluationException(this, t);
            }
        }
        
        Object[] argumentObjs = new Object[arguments.length];
        for (int i = 0; i < argcount; i++)
            argumentObjs[i] = arguments[i].evaluate(context);
        
        try {
            return accessor.invoke(context, Operator.safeValueOf(obj), argumentObjs);
        } catch (Throwable t) {
            throw new EvaluationException(this, t);
        }
    }
    
    public <T extends Throwable> void accept(NodeVisitor<T> visitor) throws T {
        visitor.visit(this);
    }
    
    public boolean isDetermined() {
        if (!accessor.isDetermined())
            return false;
        if (!object.isDetermined())
            return false;
        
        for (Node argument : arguments)
            if (!argument.isDetermined())
                return false;
        
        return true;
    }
    
    public int hashCode() {
        if (hash == 0) {
            hash = getId();
            hash = 31 * hash + getMethodName().hashCode();
            hash = 31 * hash + object.hashCode();
            for (Node argument : arguments)
                hash = 31 * hash + argument.hashCode();
        }
        return hash;
    }
    
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        
        if (obj instanceof MethodNode) {
            MethodNode other = (MethodNode) obj;
            if (getType() == other.getType()) {
                int argcount = arguments.length;
                if (argcount == other.arguments.length) {
                    if (getMethodName().equals(other.getMethodName())) {
                        if (object.equals(other.object)) {
                            for (int i = 0; i < argcount; i++)
                                if (!arguments[i].equals(other.arguments[i]))
                                    return false;
                            return true;
                        }
                    }
                }
            }
        }
        
        return false;
    }
    
}
