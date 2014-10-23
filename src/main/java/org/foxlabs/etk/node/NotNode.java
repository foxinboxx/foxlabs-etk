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
import org.foxlabs.etk.support.Operator;

public final class NotNode extends UnaryNode implements LogicalNode {
    
    NotNode(Node operand) {
        super(operand);
    }
    
    public int getId() {
        return NOT_NODE;
    }
    
    public Class<Boolean> getType() {
        return Boolean.class;
    }
    
    public Boolean evaluate(Environment context) throws EvaluationException {
        Object obj = operand.evaluate(context);
        try {
            return Operator.safeValueOf((Boolean) obj)
                ? Boolean.FALSE
                : Boolean.TRUE;
        } catch (Exception e) {
            throw new EvaluationException(this, e);
        }
    }
    
    public <T extends Throwable> void accept(NodeVisitor<T> visitor) throws T {
        visitor.visit(this);
    }
    
}
