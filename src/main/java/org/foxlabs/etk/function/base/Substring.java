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

import org.foxlabs.etk.Environment;
import org.foxlabs.etk.annotation.FxNames;
import org.foxlabs.etk.function.Function;

@FxNames({"SUBSTR", "SUBSTRING"})
public abstract class Substring extends Function {
    
    protected Substring(String name, String namespace, Class<?>... argtypes) {
        super(name, namespace, String.class, argtypes);
    }
    
    public boolean isDetermined() {
        return true;
    }
    
    public static class BeginImpl extends Substring {
        
        public BeginImpl(String name, String namespace) {
            super(name, namespace, String.class, Integer.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            String string = safeArgumentOf(arguments, 0);
            Integer beginIndex = safeArgumentOf(arguments, 1);
            return string.substring(beginIndex);
        }
        
    }
    
    public static class RangeImpl extends Substring {
        
        public RangeImpl(String name, String namespace) {
            super(name, namespace, String.class, Integer.class, Integer.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            String string = safeArgumentOf(arguments, 0);
            Integer beginIndex = safeArgumentOf(arguments, 1);
            Integer endIndex = safeArgumentOf(arguments, 2);
            return string.substring(beginIndex, endIndex);
        }
        
    }
    
}
