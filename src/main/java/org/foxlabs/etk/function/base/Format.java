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

import java.text.MessageFormat;

import org.foxlabs.etk.Environment;
import org.foxlabs.etk.annotation.FxNames;
import org.foxlabs.etk.function.Function;

@FxNames("FORMAT")
public class Format extends Function {
    
    public Format(String name, String namespace) {
        super(name, namespace, String.class, String.class, Object[].class);
    }
    
    public boolean isDetermined() {
        return true;
    }
    
    public Object evaluate(Environment context, Object... arguments) {
        String pattern = safeArgumentOf(arguments, 0);
        if (arguments.length > 1) {
            Object[] args = new Object[arguments.length - 1];
            for (int i = 1; i < arguments.length; i++)
                args[i - 1] = arguments[i];
            return MessageFormat.format(pattern, args);
        }
        return pattern;
    }
    
}
