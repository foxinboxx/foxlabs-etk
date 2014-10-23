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

public final class ConcatNode extends NaryNode {
    
    ConcatNode(Node... operands) {
        super(operands);
    }
    
    public int getId() {
        return CONCAT_NODE;
    }
    
    public Class<?> getType() {
        return String.class;
    }
    
    public String evaluate(Environment context) throws EvaluationException {
        StringBuilder buf = new StringBuilder();
        for (Node operand : operands) {
            Object obj = operand.evaluate(context);
            if (obj != null)
                buf.append(obj);
        }
        return buf.toString();
    }
    
    public <T extends Throwable> void accept(NodeVisitor<T> visitor) throws T {
        visitor.visit(this);
    }
    
}
