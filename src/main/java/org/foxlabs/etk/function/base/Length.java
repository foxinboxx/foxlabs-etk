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

package org.foxlabs.etk.function.base;

import java.util.Collection;
import java.util.Map;

import org.foxlabs.etk.Environment;
import org.foxlabs.etk.annotation.FxNames;
import org.foxlabs.etk.function.Function;

@FxNames({"LEN", "LENGTH", "SIZE"})
public abstract class Length extends Function {
    
    protected Length(String name, String namespace, Class<?> type) {
        super(name, namespace, Integer.class, type);
    }
    
    public boolean isDetermined() {
        return true;
    }
    
    public static class StringImpl extends Length {
        
        public StringImpl(String name, String namespace) {
            super(name, namespace, String.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            String string = safeArgumentOf(arguments, 0);
            return string.length();
        }
        
    }
    
    public static class ArrayImpl extends Length {
        
        public ArrayImpl(String name, String namespace) {
            super(name, namespace, Object[].class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            Object[] array = safeArgumentOf(arguments, 0);
            return array.length;
        }
        
    }
    
    public static class CollectionImpl extends Length {
        
        public CollectionImpl(String name, String namespace) {
            super(name, namespace, Collection.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            Collection<?> collection = safeArgumentOf(arguments, 0);
            return collection.size();
        }
        
    }
    
    public static class MapImpl extends Length {
        
        public MapImpl(String name, String namespace) {
            super(name, namespace, Map.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            Map<?, ?> map = safeArgumentOf(arguments, 0);
            return map.size();
        }
        
    }
    
}
