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
import org.foxlabs.etk.support.Comparator;

public final class GreaterThanNode extends BinaryNode implements RelationalNode {
    
    private final Comparator comparator;
    
    GreaterThanNode(Comparator comparator, Node operand1, Node operand2) {
        super(operand1, operand2);
        this.comparator = comparator;
    }
    
    public int getId() {
        return GT_NODE;
    }
    
    public Class<Boolean> getType() {
        return Boolean.class;
    }
    
    public Boolean evaluate(Environment context) throws EvaluationException {
        Object obj1 = operand1.evaluate(context);
        Object obj2 = operand2.evaluate(context);
        
        try {
            return Boolean.valueOf(comparator.compare(obj1, obj2) > 0);
        } catch (Exception e) {
            throw new EvaluationException(this, e);
        }
    }
    
    public <T extends Throwable> void accept(NodeVisitor<T> visitor) throws T {
        visitor.visit(this);
    }
    
}
