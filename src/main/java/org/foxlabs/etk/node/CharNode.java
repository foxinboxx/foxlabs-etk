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

public final class CharNode extends LiteralNode<Character> {
    
    private final Character value;
    
    CharNode(char value) {
        this.value = value;
    }
    
    public int getId() {
        return CHAR_NODE;
    }
    
    public Class<Character> getType() {
        return Character.class;
    }
    
    public Character getValue() {
        return value;
    }
    
    public Character evaluate(Environment context) {
        return value;
    }
    
    public <T extends Throwable> void accept(NodeVisitor<T> visitor) throws T {
        visitor.visit(this);
    }
    
}
