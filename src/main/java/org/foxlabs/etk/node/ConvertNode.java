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
import org.foxlabs.etk.support.Converter;

public final class ConvertNode extends Node {
    
    private final Node operand;
    private final Converter converter;
    
    ConvertNode(Converter converter, Node operand) {
        this.operand = operand;
        this.converter = converter;
    }
    
    public int getId() {
        return CONVERT_NODE;
    }
    
    public Class<?> getType() {
        return converter.getType();
    }
    
    public Node getOperand() {
        return operand;
    }
    
    public Object evaluate(Environment context) throws EvaluationException {
        Object obj = operand.evaluate(context);
        try {
            return converter.convert(obj);
        } catch (Exception e) {
            throw new EvaluationException(this, e);
        }
    }
    
    public <T extends Throwable> void accept(NodeVisitor<T> visitor) throws T {
        visitor.visit(this);
    }
    
    public boolean isDetermined() {
        return operand.isDetermined();
    }
    
    public int hashCode() {
        int hash = getId();
        hash = 31 * hash + converter.getType().hashCode();
        hash = 31 * hash + operand.hashCode();
        return hash;
    }
    
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        
        if (obj instanceof ConvertNode) {
            ConvertNode other = (ConvertNode) obj;
            return getType() == other.getType() &&
                   operand.equals(other.operand);
        }
        
        return false;
    }
    
}
