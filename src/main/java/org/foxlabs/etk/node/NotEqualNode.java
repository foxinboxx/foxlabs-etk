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

public final class NotEqualNode extends BinaryNode implements RelationalNode {
    
    NotEqualNode(Node operand1, Node operand2) {
        super(operand1, operand2);
    }
    
    public int getId() {
        return NE_NODE;
    }
    
    public Class<Boolean> getType() {
        return Boolean.class;
    }
    
    public Boolean evaluate(Environment context) throws EvaluationException {
        Object obj1 = operand1.evaluate(context);
        Object obj2 = operand2.evaluate(context);
        try {
            if (obj1 == obj2)
                return Boolean.FALSE;
            
            if (obj1 == null || obj2 == null)
                return Boolean.TRUE;
            
            if (obj1.equals(obj2)) {
                if (obj1.getClass() == obj2.getClass() || obj2.equals(obj1))
                    return Boolean.FALSE;
            }
            
            return Boolean.TRUE;
        } catch (Exception e) {
            throw new EvaluationException(this, e);
        }
    }
    
    public <T extends Throwable> void accept(NodeVisitor<T> visitor) throws T {
        visitor.visit(this);
    }
    
}
