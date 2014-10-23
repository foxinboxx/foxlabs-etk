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

@FxNames({"AVG", "AVERAGE"})
public abstract class Average extends Function {
    
    protected Average(String name, String namespace, Class<?> type, Class<?> argtypes) {
        super(name, namespace, type, argtypes, Types.arraytypeOf(argtypes));
    }
    
    public boolean isDetermined() {
        return true;
    }
    
    public static class IntegerImpl extends Average {
        
        public IntegerImpl(String name, String namespace) {
            super(name, namespace, Double.class, Integer.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            long sum = 0;
            for (int i = 0; i < arguments.length; i++) {
                Integer a = safeArgumentOf(arguments, i);
                sum += a.longValue();
            }
            return sum / arguments.length;
        }
        
    }
    
    public static class LongImpl extends Average {
        
        public LongImpl(String name, String namespace) {
            super(name, namespace, Double.class, Long.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            long sum = 0;
            for (int i = 0; i < arguments.length; i++) {
                Long a = safeArgumentOf(arguments, i);
                sum += a.longValue();
            }
            return sum / arguments.length;
        }
        
    }
    
    public static class FloatImpl extends Average {
        
        public FloatImpl(String name, String namespace) {
            super(name, namespace, Double.class, Float.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            double sum = 0;
            for (int i = 0; i < arguments.length; i++) {
                Float a = safeArgumentOf(arguments, i);
                sum += a.doubleValue();
            }
            return sum / arguments.length;
        }
        
    }
    
    public static class DoubleImpl extends Average {
        
        public DoubleImpl(String name, String namespace) {
            super(name, namespace, Double.class, Double.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            double sum = 0;
            for (int i = 0; i < arguments.length; i++) {
                Double a = safeArgumentOf(arguments, i);
                sum += a.doubleValue();
            }
            return sum / arguments.length;
        }
        
    }
    
    public static class BigIntegerImpl extends Average {
        
        public BigIntegerImpl(String name, String namespace) {
            super(name, namespace, BigDecimal.class, BigInteger.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            BigDecimal sum = BigDecimal.ZERO;
            for (int i = 0; i < arguments.length; i++) {
                BigInteger a = safeArgumentOf(arguments, i);
                sum.add(new BigDecimal(a));
            }
            return sum.divide(BigDecimal.valueOf(arguments.length));
        }
        
    }
    
    public static class BigDecimalImpl extends Average {
        
        public BigDecimalImpl(String name, String namespace) {
            super(name, namespace, BigDecimal.class, BigDecimal.class);
        }
        
        public Object evaluate(Environment context, Object... arguments) {
            BigDecimal sum = BigDecimal.ZERO;
            for (int i = 0; i < arguments.length; i++) {
                BigDecimal a = safeArgumentOf(arguments, i);
                sum.add(a);
            }
            return sum.divide(BigDecimal.valueOf(arguments.length));
        }
        
    }
    
}
