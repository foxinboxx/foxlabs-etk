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

package org.foxlabs.etk.support;

import java.util.List;
import java.util.Map;

public abstract class Indexer extends Operator {
    
    Indexer() {}
    
    public abstract Class<?> getType();
    
    public abstract Object lookup(Object collection, Object key);
    
    public static final Indexer getInstance(Class<?> collectionType, Class<?> keyType) {
        if (collectionType == null || keyType == null)
            return null;
        
        if (Map.class.isAssignableFrom(collectionType))
            return MapIndexer.singleInstance;
        
        if (keyType == Integer.class) {
            if (collectionType.isArray())
                return new ArrayIndexer(collectionType.getComponentType());
            
            if (List.class.isAssignableFrom(collectionType))
                return ListIndexer.singleInstance;
            
            if (collectionType == String.class)
                return StringIndexer.singleInstance;
        }
        
        return null;
    }
    
    public static final Class<?> getKeyType(Class<?> collectionType) {
        if (collectionType == null)
            return null;
        
        if (collectionType.isArray() ||
            String.class.isAssignableFrom(collectionType) ||
            List.class.isAssignableFrom(collectionType))
            return Integer.class;
            
        if (Map.class.isAssignableFrom(collectionType))
            return Object.class;
        
        return null;
    }
    
}

final class ArrayIndexer extends Indexer {
    
    private final Class<?> type;
    
    ArrayIndexer(Class<?> type) {
        this.type = type;
    }
    
    public Class<?> getType() {
        return type;
    }
    
    public Object lookup(Object array, Object index) {
        Object[] c = safeValueOf((Object[]) array);
        return c[safeIndexOf((Integer) index, c.length)];
    }
    
}

final class ListIndexer extends Indexer {
    
    static final Indexer singleInstance = new ListIndexer();
    
    private ListIndexer() {}
    
    public Class<?> getType() {
        return Object.class;
    }
    
    public Object lookup(Object list, Object index) {
        List<?> c = safeValueOf((List<?>) list);
        return c.get(safeIndexOf((Integer) index, c.size()));
    }
    
}

final class MapIndexer extends Indexer {
    
    static final Indexer singleInstance = new MapIndexer();
    
    private MapIndexer() {}
    
    public Class<?> getType() {
        return Object.class;
    }
    
    public Object lookup(Object map, Object key) {
        return safeValueOf((Map<?, ?>) map).get(key);
    }
    
}

final class StringIndexer extends Indexer {
    
    static final Indexer singleInstance = new StringIndexer();
    
    private StringIndexer() {}
    
    public Class<?> getType() {
        return Character.class;
    }
    
    public Object lookup(Object string, Object index) {
        String c = safeValueOf((String) string);
        return c.charAt(safeIndexOf((Integer) index, c.length()));
    }
    
}
