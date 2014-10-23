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

import java.text.SimpleDateFormat;

import org.foxlabs.etk.Environment;
import org.foxlabs.etk.annotation.FxNames;
import org.foxlabs.etk.function.Function;

@FxNames("DATEFORMAT")
public class DateFormat extends Function {
    
    public DateFormat(String name, String namespace) {
        super(name, namespace, java.util.Date.class, String.class);
    }
    
    public boolean isDetermined() {
        return true;
    }
    
    public Object evaluate(Environment context, Object... arguments)
            throws Exception {
        java.util.Date date = safeArgumentOf(arguments, 0);
        String format = safeArgumentOf(arguments, 1);
        return (new SimpleDateFormat(format)).format(date);
    }
    
}
