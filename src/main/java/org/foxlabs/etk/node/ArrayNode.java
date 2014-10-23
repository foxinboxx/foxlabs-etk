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
import org.foxlabs.etk.support.Types;

public final class ArrayNode extends Node {
    
    private static final Object[] emptyArray = new Object[0];
    
    private final Node[] items;
    private final Class<?> type;
    
    private transient int hash = 0;
    
    ArrayNode(Node... items) {
        Class<?>[] types = new Class<?>[items.length];
        for (int i = 0; i < types.length; i++)
            types[i] = items[i].getType();
        
        this.type = Types.arraytypeOf(Types.supertypeOf(types));
        this.items = items;
    }
    
    public int getId() {
        return ARRAY_NODE;
    }
    
    public Class<?> getType() {
        return type;
    }
    
    public int getItemCount() {
        return items.length;
    }
    
    public Node getItem(int index) {
        return items[index];
    }
    
    public Node[] getItems() {
        return items.length == 0 ? items : items.clone();
    }
    
    public Object[] evaluate(Environment context) throws EvaluationException {
        int itemcount = items.length;
        if (itemcount == 0)
            return emptyArray;
        
        Object[] objs = new Object[itemcount];
        for (int i = 0; i < itemcount; i++)
            objs[i] = items[i].evaluate(context);
        
        return objs;
    }
    
    public <T extends Throwable> void accept(NodeVisitor<T> visitor) throws T {
        visitor.visit(this);
    }
    
    public boolean isDetermined() {
        for (Node item : items)
            if (!item.isDetermined())
                return false;
        return true;
    }
    
    public int hashCode() {
        if (hash == 0) {
            hash = getId();
            for (Node item : items)
                hash = 31 * hash + item.hashCode();
        }
        return hash;
    }
    
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        
        if (obj instanceof ArrayNode) {
            ArrayNode other = (ArrayNode) obj;
            if (getType() == other.getType()) {
                int itemcount = items.length;
                if (itemcount == other.items.length) {
                    for (int i = 0; i < itemcount; i++)
                        if (!items[i].equals(other.items[i]))
                            return false;
                    return true;
                }
            }
        }
        
        return false;
    }
    
}
