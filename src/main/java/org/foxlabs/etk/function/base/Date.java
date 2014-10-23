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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.foxlabs.etk.Environment;
import org.foxlabs.etk.annotation.FxNames;
import org.foxlabs.etk.function.Function;

@FxNames("DATE")
public abstract class Date extends Function {
    
    protected Date(String name, String namespace) {
        super(name, namespace, java.util.Date.class);
    }
    
    protected Date(String name, String namespace, Class<?>... argtypes) {
        super(name, namespace, java.util.Date.class, argtypes);
    }
    
    @FxNames({"DATE", "SYSDATE"})
    public static class VoidImpl extends Date {
        
        public VoidImpl(String name, String namespace) {
            super(name, namespace);
        }
        
        public boolean isDetermined() {
            return false;
        }
        
        public Object evaluate(Environment context, Object... arguments)
                throws Exception {
            return new java.util.Date();
        }
        
        
    }
    
    public static class LongImpl extends Date {
        
        public LongImpl(String name, String namespace) {
            super(name, namespace, Long.class);
        }
        
        public boolean isDetermined() {
            return true;
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            Long date = safeArgumentOf(arguments, 0);
            return new java.util.Date(date);
        }
        
    }
    
    public static class IntegerImpl extends Date {
        
        public IntegerImpl(String name, String namespace) {
            super(name, namespace, Integer[].class);
        }
        
        public boolean isDetermined() {
            return true;
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            Calendar cal = Calendar.getInstance();
            for (int i = 0; i < arguments.length; i++) {
                Integer field = safeArgumentOf(arguments, i);
                cal.set(calFields[i], field);
            }
            return cal.getTime();
        }
        
    }
    
    public static class StringImpl extends Date {
        
        public StringImpl(String name, String namespace) {
            super(name, namespace, String.class);
        }
        
        public boolean isDetermined() {
            return true;
        }
        
        public Object evaluate(Environment context, Object... arguments)
                throws Exception {
            String date = safeArgumentOf(arguments, 0);
            return DateFormat.getInstance().parse(date);
        }
        
    }
    
    public static class StringStringImpl extends Date {
        
        public StringStringImpl(String name, String namespace) {
            super(name, namespace, String.class, String.class);
        }
        
        public boolean isDetermined() {
            return true;
        }
        
        public Object evaluate(Environment context, Object... arguments)
                throws Exception {
            String date = safeArgumentOf(arguments, 0);
            String format = safeArgumentOf(arguments, 1);
            return (new SimpleDateFormat(format)).parse(date);
        }
        
    }
    
    private static int[] calFields = new int[]{
        Calendar.YEAR,
        Calendar.MONTH,
        Calendar.DAY_OF_MONTH,
        Calendar.HOUR_OF_DAY,
        Calendar.MINUTE,
        Calendar.SECOND,
        Calendar.MILLISECOND
    };
    
}
