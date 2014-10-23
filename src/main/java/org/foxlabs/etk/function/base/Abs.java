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

@FxNames("ABS")
public abstract class Abs extends Function {
    
    protected Abs(String name, String namespace, Class<?> type) {
        super(name, namespace, type, type);
    }
    
    public boolean isDetermined() {
        return true;
    }
    
    public static class IntegerImpl extends Abs {
        
        public IntegerImpl(String name, String namespace) {
            super(name, namespace, Integer.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            Integer a = safeArgumentOf(arguments, 0);
            return Math.abs(a);
        }
        
    }
    
    public static class LongImpl extends Abs {
        
        public LongImpl(String name, String namespace) {
            super(name, namespace, Long.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            Long a = safeArgumentOf(arguments, 0);
            return Math.abs(a);
        }
        
    }
    
    public static class FloatImpl extends Abs {
        
        public FloatImpl(String name, String namespace) {
            super(name, namespace, Float.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            Float a = safeArgumentOf(arguments, 0);
            return Math.abs(a);
        }
        
    }
    
    public static class DoubleImpl extends Abs {
        
        public DoubleImpl(String name, String namespace) {
            super(name, namespace, Double.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            Double a = safeArgumentOf(arguments, 0);
            return Math.abs(a);
        }
        
    }
    
    public static class BigIntegerImpl extends Abs {
        
        public BigIntegerImpl(String name, String namespace) {
            super(name, namespace, BigInteger.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            BigInteger a = safeArgumentOf(arguments, 0);
            return a.abs();
        }
        
    }
    
    public static class BigDecimalImpl extends Abs {
        
        public BigDecimalImpl(String name, String namespace) {
            super(name, namespace, BigDecimal.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            BigDecimal a = safeArgumentOf(arguments, 0);
            return a.abs();
        }
        
    }
    
}
