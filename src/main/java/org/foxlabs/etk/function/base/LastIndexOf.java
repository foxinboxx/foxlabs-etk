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

import java.util.Arrays;
import java.util.List;

import org.foxlabs.etk.Environment;
import org.foxlabs.etk.annotation.FxNames;
import org.foxlabs.etk.function.Function;

@FxNames({"LASTINDEX", "LASTINDEXOF"})
public abstract class LastIndexOf extends Function {
    
    protected LastIndexOf(String name, String namespace,
            Class<?> collectionType, Class<?> elementType) {
        super(name, namespace, Integer.class, collectionType, elementType);
    }
    
    public boolean isDetermined() {
        return true;
    }
    
    public static class StringCharImpl extends LastIndexOf {
        
        public StringCharImpl(String name, String namespace) {
            super(name, namespace, String.class, Character.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            String string = safeArgumentOf(arguments, 0);
            Character ch = safeArgumentOf(arguments, 1);
            return string.lastIndexOf(ch);
        }
        
    }
    
    public static class StringStringImpl extends LastIndexOf {
        
        public StringStringImpl(String name, String namespace) {
            super(name, namespace, String.class, String.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            String string = safeArgumentOf(arguments, 0);
            String substring = safeArgumentOf(arguments, 1);
            return string.lastIndexOf(substring);
        }
        
    }
    
    public static class ArrayImpl extends LastIndexOf {
        
        public ArrayImpl(String name, String namespace) {
            super(name, namespace, Object[].class, Object.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            Object[] array = safeArgumentOf(arguments, 0);
            return Arrays.asList(array).lastIndexOf(arguments[1]);
        }
        
    }
    
    public static class ListImpl extends LastIndexOf {
        
        public ListImpl(String name, String namespace) {
            super(name, namespace, List.class, Object.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            List<?> list = safeArgumentOf(arguments, 0);
            return list.lastIndexOf(arguments[1]);
        }
        
    }
    
}
