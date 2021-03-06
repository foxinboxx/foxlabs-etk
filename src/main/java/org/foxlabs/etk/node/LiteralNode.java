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

public abstract class LiteralNode<T> extends Node {
    
    LiteralNode() {}
    
    public abstract T getValue();
    
    public abstract T evaluate(Environment context);
    
    public int hashCode() {
        return getId() ^ getValue().hashCode();
    }
    
    public boolean equals(Object obj) {
        if (obj instanceof LiteralNode<?>) {
            LiteralNode<?> other = (LiteralNode<?>) obj;
            return getId() == other.getId() &&
                   getValue().equals(other.getValue());
        }
        return false;
    }
    
}
