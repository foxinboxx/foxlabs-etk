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

@FxNames({"EMPTY", "ISEMPTY"})
public abstract class IsEmpty extends Function {
    
    protected IsEmpty(String name, String namespace, Class<?> type) {
        super(name, namespace, Boolean.class, type);
    }
    
    public boolean isDetermined() {
        return true;
    }
    
    public static class ObjectImpl extends IsEmpty {
        
        public ObjectImpl(String name, String namespace) {
            super(name, namespace, Object.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            return arguments[0] == null;
        }
        
    }
    
    public static class StringImpl extends IsEmpty {
        
        public StringImpl(String name, String namespace) {
            super(name, namespace, String.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            String string = (String) arguments[0];
            return string == null || string.trim().length() == 0;
        }
        
    }
    
    public static class ArrayImpl extends IsEmpty {
        
        public ArrayImpl(String name, String namespace) {
            super(name, namespace, Object[].class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            Object[] array = (Object[]) arguments[0];
            return array == null || array.length == 0;
        }
        
    }
    
    public static class CollectionImpl extends IsEmpty {
        
        public CollectionImpl(String name, String namespace) {
            super(name, namespace, Collection.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            Collection<?> collection = (Collection<?>) arguments[0];
            return collection == null || collection.isEmpty();
        }
        
    }
    
    public static class MapImpl extends IsEmpty {
        
        public MapImpl(String name, String namespace) {
            super(name, namespace, Map.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            Map<?, ?> map = (Map<?, ?>) arguments[0];
            return map == null || map.isEmpty();
        }
        
    }
    
}
