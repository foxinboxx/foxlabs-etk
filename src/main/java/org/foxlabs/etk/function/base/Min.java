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

import java.math.BigInteger;
import java.math.BigDecimal;

import org.foxlabs.etk.Environment;
import org.foxlabs.etk.annotation.FxNames;
import org.foxlabs.etk.function.Function;
import org.foxlabs.etk.support.Types;

@FxNames("MIN")
public abstract class Min extends Function {
    
    public Min(String name, String namespace, Class<?> type) {
        super(name, namespace, type, type, Types.arraytypeOf(type));
    }
    
    public boolean isDetermined() {
        return true;
    }
    
    public static class CharImpl extends Min {
        
        public CharImpl(String name, String namespace) {
            super(name, namespace, Character.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            Character min = safeArgumentOf(arguments, 0);
            for (int i = 1; i < arguments.length; i++) {
                Character x = safeArgumentOf(arguments, i);
                if (x.compareTo(min) < 0)
                    min = x;
            }
            return min;
        }
        
    }
    
    public static class IntegerImpl extends Min {
        
        public IntegerImpl(String name, String namespace) {
            super(name, namespace, Integer.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            Integer min = safeArgumentOf(arguments, 0);
            for (int i = 1; i < arguments.length; i++) {
                Integer x = safeArgumentOf(arguments, i);
                if (x.compareTo(min) < 0)
                    min = x;
            }
            return min;
        }
        
    }
    
    public static class LongImpl extends Min {
        
        public LongImpl(String name, String namespace) {
            super(name, namespace, Long.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            Long min = safeArgumentOf(arguments, 0);
            for (int i = 1; i < arguments.length; i++) {
                Long x = safeArgumentOf(arguments, i);
                if (x.compareTo(min) < 0)
                    min = x;
            }
            return min;
        }
        
    }
    
    public static class FloatImpl extends Min {
        
        public FloatImpl(String name, String namespace) {
            super(name, namespace, Float.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            Float min = safeArgumentOf(arguments, 0);
            for (int i = 1; i < arguments.length; i++) {
                Float x = safeArgumentOf(arguments, i);
                if (x.compareTo(min) < 0)
                    min = x;
            }
            return min;
        }
        
    }
    
    public static class DoubleImpl extends Min {
        
        public DoubleImpl(String name, String namespace) {
            super(name, namespace, Double.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            Double min = safeArgumentOf(arguments, 0);
            for (int i = 1; i < arguments.length; i++) {
                Double x = safeArgumentOf(arguments, i);
                if (x.compareTo(min) < 0)
                    min = x;
            }
            return min;
        }
        
    }
    
    public static class BigIntegerImpl extends Min {
        
        public BigIntegerImpl(String name, String namespace) {
            super(name, namespace, BigInteger.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            BigInteger min = safeArgumentOf(arguments, 0);
            for (int i = 1; i < arguments.length; i++) {
                BigInteger x = safeArgumentOf(arguments, i);
                if (x.compareTo(min) < 0)
                    min = x;
            }
            return min;
        }
        
    }
    
    public static class BigDecimalImpl extends Min {
        
        public BigDecimalImpl(String name, String namespace) {
            super(name, namespace, BigDecimal.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            BigDecimal min = safeArgumentOf(arguments, 0);
            for (int i = 1; i < arguments.length; i++) {
                BigDecimal x = safeArgumentOf(arguments, i);
                if (x.compareTo(min) < 0)
                    min = x;
            }
            return min;
        }
        
    }
    
    public static class StringImpl extends Min {
        
        public StringImpl(String name, String namespace) {
            super(name, namespace, String.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            String min = safeArgumentOf(arguments, 0);
            for (int i = 1; i < arguments.length; i++) {
                String x = safeArgumentOf(arguments, i);
                if (x.compareTo(min) < 0)
                    min = x;
            }
            return min;
        }
        
    }
    
}
