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

import java.math.BigDecimal;
import java.math.BigInteger;

import org.foxlabs.etk.Environment;
import org.foxlabs.etk.annotation.FxNames;
import org.foxlabs.etk.function.Function;
import org.foxlabs.etk.support.Types;

@FxNames("SUM")
public abstract class Sum extends Function {
    
    protected Sum(String name, String namespace, Class<?> type) {
        super(name, namespace, type, type, Types.arraytypeOf(type));
    }
    
    public boolean isDetermined() {
        return true;
    }
    
    public static class IntegerImpl extends Sum {
        
        public IntegerImpl(String name, String namespace) {
            super(name, namespace, Integer.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            int sum = 0;
            for (int i = 0; i < arguments.length; i++) {
                Integer x = safeArgumentOf(arguments, i);
                sum += x.intValue();
            }
            return sum;
        }
        
    }
    
    public static class LongImpl extends Sum {
        
        public LongImpl(String name, String namespace) {
            super(name, namespace, Long.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            long sum = 0;
            for (int i = 0; i < arguments.length; i++) {
                Long x = safeArgumentOf(arguments, i);
                sum += x.longValue();
            }
            return sum;
        }
        
    }
    
    public static class FloatImpl extends Sum {
        
        public FloatImpl(String name, String namespace) {
            super(name, namespace, Float.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            float sum = 0;
            for (int i = 0; i < arguments.length; i++) {
                Float x = safeArgumentOf(arguments, i);
                sum += x.floatValue();
            }
            return sum;
        }
        
    }
    
    public static class DoubleImpl extends Sum {
        
        public DoubleImpl(String name, String namespace) {
            super(name, namespace, Double.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            double sum = 0;
            for (int i = 0; i < arguments.length; i++) {
                Double x = safeArgumentOf(arguments, i);
                sum += x.doubleValue();
            }
            return sum;
        }
        
    }
    
    public static class BigIntegerImpl extends Sum {
        
        public BigIntegerImpl(String name, String namespace) {
            super(name, namespace, BigInteger.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            BigInteger sum = BigInteger.ZERO;
            for (int i = 0; i < arguments.length; i++) {
                BigInteger x = safeArgumentOf(arguments, i);
                sum.add(x);
            }
            return sum;
        }
        
    }
    
    public static class BigDecimalImpl extends Sum {
        
        public BigDecimalImpl(String name, String namespace) {
            super(name, namespace, BigDecimal.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            BigDecimal sum = BigDecimal.ZERO;
            for (int i = 0; i < arguments.length; i++) {
                BigDecimal x = safeArgumentOf(arguments, i);
                sum.add(x);
            }
            return sum;
        }
        
    }
    
}
