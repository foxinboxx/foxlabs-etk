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
import org.foxlabs.etk.support.Calculator;

public final class DivideNode extends BinaryNode implements ArithmeticalNode {
    
    private final Calculator calculator;
    
    DivideNode(Calculator calculator, Node operand1, Node operand2) {
        super(operand1, operand2);
        this.calculator = calculator;
    }
    
    public int getId() {
        return DIV_NODE;
    }
    
    public Class<? extends Number> getType() {
        return calculator.getType();
    }
    
    public Number evaluate(Environment context) throws EvaluationException {
        Object obj1 = operand1.evaluate(context);
        Object obj2 = operand2.evaluate(context);
        try {
            return calculator.divide(obj1, obj2);
        } catch (Exception e) {
            throw new EvaluationException(this, e);
        }
    }
    
    public <T extends Throwable> void accept(NodeVisitor<T> visitor) throws T {
        visitor.visit(this);
    }
    
}
