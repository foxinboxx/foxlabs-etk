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
import org.foxlabs.etk.support.Types;

public final class NumberNode extends LiteralNode<Number> {
    
    private final Number value;
    
    NumberNode(Number value) {
        this.value = value;
    }
    
    public int getId() {
        return NUMBER_NODE;
    }
    
    public Class<? extends Number> getType() {
        return value.getClass();
    }
    
    public Number getValue() {
        return value;
    }
    
    public boolean isInteger() {
        return Types.isInteger(getType());
    }
    
    public boolean isFloat() {
        return Types.isFloat(getType());
    }
    
    public Number evaluate(Environment context) {
        return value;
    }
    
    public <T extends Throwable> void accept(NodeVisitor<T> visitor) throws T {
        visitor.visit(this);
    }
    
}
