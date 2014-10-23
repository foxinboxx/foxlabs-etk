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

import java.math.BigInteger;
import java.math.BigDecimal;

import org.foxlabs.etk.resource.ResourceManager;

public abstract class Operator {
    
    protected Operator() {}
    
    public abstract Class<?> getType();
    
    public static final <T> T safeValueOf(T value) {
        if (value == null)
            throw ResourceManager.newNullPointerException();
        return value;
    }
    
    @SuppressWarnings("unchecked")
    public static final <T> T safeArgumentOf(Object[] arguments, int index) {
        if (arguments[index] == null)
            throw ResourceManager.newNullPointerArgumentException(index + 1);
        return (T) arguments[index];
    }
    
    public static final int safeDivisorOf(Integer value) {
        if (safeValueOf(value) == 0)
            throw ResourceManager.newDivisionByZeroException();
        return value;
    }
    
    public static final long safeDivisorOf(Long value) {
        if (safeValueOf(value) == 0L)
            throw ResourceManager.newDivisionByZeroException();
        return value;
    }
    
    public static final float safeDivisorOf(Float value) {
        if (safeValueOf(value) == 0F)
            throw ResourceManager.newDivisionByZeroException();
        return value;
    }
    
    public static final double safeDivisorOf(Double value) {
        if (safeValueOf(value) == 0D)
            throw ResourceManager.newDivisionByZeroException();
        return value;
    }
    
    public static final BigInteger safeDivisorOf(BigInteger value) {
        if (safeValueOf(value).equals(BigInteger.ZERO))
            throw ResourceManager.newDivisionByZeroException();
        return value;
    }
    
    public static final BigDecimal safeDivisorOf(BigDecimal value) {
        if (safeValueOf(value).equals(BigDecimal.ZERO))
            throw ResourceManager.newDivisionByZeroException();
        return value;
    }
    
    public static final int safeIndexOf(Integer index, int size) {
        int i = safeValueOf(index);
        if (i < 0 || i >= size)
            throw ResourceManager.newIndexOutOfBoundsException(i, size);
        return i;
    }
    
    public static final <T> T safeCastOf(Object value, Class<T> toType) {
        try {
            return toType.cast(value);
        } catch (ClassCastException e) {
            throw ResourceManager.newClassCastException(toType);
        }
    }
    
}
