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
import org.foxlabs.etk.support.Types;

public final class ConditionNode extends TernaryNode {
    
    private final Class<?> type;
    
    ConditionNode(Node condition, Node case1, Node case2) {
        super(condition, case1, case2);
        this.type = Types.supertypeOf(case1.getType(), case2.getType());
    }
    
    public int getId() {
        return CONDITION_NODE;
    }
    
    public Class<?> getType() {
        return type;
    }
    
    public Object evaluate(Environment context) throws EvaluationException {
        Object obj = operand1.evaluate(context);
        
        boolean flag;
        try {
            flag = Operator.safeValueOf((Boolean) obj);
        } catch (Exception e) {
            throw new EvaluationException(this, e);
        }
        
        if (flag)
            return operand2.evaluate(context);
        return operand3.evaluate(context);
    }
    
    public <T extends Throwable> void accept(NodeVisitor<T> visitor) throws T {
        visitor.visit(this);
    }
    
}
