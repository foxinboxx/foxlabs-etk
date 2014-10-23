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

import static org.foxlabs.etk.support.Types.*;

public abstract class Calculator extends Operator {
    
    Calculator() {}
    
    public abstract Class<? extends Number> getType();
    
    public abstract Number negate(Object v);
    
    public abstract Number add(Object v1, Object v2);
    
    public abstract Number subtract(Object v1, Object v2);
    
    public abstract Number multiply(Object v1, Object v2);
    
    public abstract Number divide(Object v1, Object v2);
    
    public abstract Number modulo(Object v1, Object v2);
    
    private static final Calculator[][] calculators = new Calculator[PRIMITIVE_COUNT][PRIMITIVE_COUNT];
    
    static {
        // Null
        calculators[NULL][NULL]             = null;
        calculators[NULL][BOOLEAN]          = null;
        calculators[NULL][CHARACTER]        = null;
        calculators[NULL][STRING]           = null;
        calculators[NULL][ENUM]             = null;
        calculators[NULL][BYTE]             = null;
        calculators[NULL][SHORT]            = null;
        calculators[NULL][INTEGER]          = null;
        calculators[NULL][LONG]             = null;
        calculators[NULL][FLOAT]            = null;
        calculators[NULL][DOUBLE]           = null;
        calculators[NULL][BIGINTEGER]       = null;
        calculators[NULL][BIGDECIMAL]       = null;
        // Boolean
        calculators[BOOLEAN][NULL]          = null;
        calculators[BOOLEAN][BOOLEAN]       = null;
        calculators[BOOLEAN][CHARACTER]     = null;
        calculators[BOOLEAN][STRING]        = null;
        calculators[BOOLEAN][ENUM]          = null;
        calculators[BOOLEAN][BYTE]          = null;
        calculators[BOOLEAN][SHORT]         = null;
        calculators[BOOLEAN][INTEGER]       = null;
        calculators[BOOLEAN][LONG]          = null;
        calculators[BOOLEAN][FLOAT]         = null;
        calculators[BOOLEAN][DOUBLE]        = null;
        calculators[BOOLEAN][BIGINTEGER]    = null;
        calculators[BOOLEAN][BIGDECIMAL]    = null;
        // Character
        calculators[CHARACTER][NULL]        = null;
        calculators[CHARACTER][BOOLEAN]     = null;
        calculators[CHARACTER][CHARACTER]   = IntegerCalculator.singleInstance;
        calculators[CHARACTER][STRING]      = null;
        calculators[CHARACTER][ENUM]        = null;
        calculators[CHARACTER][BYTE]        = IntegerCalculator.singleInstance;
        calculators[CHARACTER][SHORT]       = IntegerCalculator.singleInstance;
        calculators[CHARACTER][INTEGER]     = IntegerCalculator.singleInstance;
        calculators[CHARACTER][LONG]        = LongCalculator.singleInstance;
        calculators[CHARACTER][FLOAT]       = FloatCalculator.singleInstance;
        calculators[CHARACTER][DOUBLE]      = DoubleCalculator.singleInstance;
        calculators[CHARACTER][BIGINTEGER]  = BigIntegerCalculator.singleInstance;
        calculators[CHARACTER][BIGDECIMAL]  = BigDecimalCalculator.singleInstance;
        // String
        calculators[STRING][NULL]           = null;
        calculators[STRING][BOOLEAN]        = null;
        calculators[STRING][CHARACTER]      = null;
        calculators[STRING][STRING]         = null;
        calculators[STRING][ENUM]           = null;
        calculators[STRING][BYTE]           = null;
        calculators[STRING][SHORT]          = null;
        calculators[STRING][INTEGER]        = null;
        calculators[STRING][LONG]           = null;
        calculators[STRING][FLOAT]          = null;
        calculators[STRING][DOUBLE]         = null;
        calculators[STRING][BIGINTEGER]     = null;
        calculators[STRING][BIGDECIMAL]     = null;
        // Enum
        calculators[ENUM][NULL]             = null;
        calculators[ENUM][BOOLEAN]          = null;
        calculators[ENUM][CHARACTER]        = null;
        calculators[ENUM][STRING]           = null;
        calculators[ENUM][ENUM]             = null;
        calculators[ENUM][BYTE]             = null;
        calculators[ENUM][SHORT]            = null;
        calculators[ENUM][INTEGER]          = null;
        calculators[ENUM][LONG]             = null;
        calculators[ENUM][FLOAT]            = null;
        calculators[ENUM][DOUBLE]           = null;
        calculators[ENUM][BIGINTEGER]       = null;
        calculators[ENUM][BIGDECIMAL]       = null;
        // Byte
        calculators[BYTE][NULL]             = null;
        calculators[BYTE][BOOLEAN]          = null;
        calculators[BYTE][CHARACTER]        = IntegerCalculator.singleInstance;
        calculators[BYTE][STRING]           = null;
        calculators[BYTE][ENUM]             = null;
        calculators[BYTE][BYTE]             = IntegerCalculator.singleInstance;
        calculators[BYTE][SHORT]            = IntegerCalculator.singleInstance;
        calculators[BYTE][INTEGER]          = IntegerCalculator.singleInstance;
        calculators[BYTE][LONG]             = LongCalculator.singleInstance;
        calculators[BYTE][FLOAT]            = FloatCalculator.singleInstance;
        calculators[BYTE][DOUBLE]           = DoubleCalculator.singleInstance;
        calculators[BYTE][BIGINTEGER]       = BigIntegerCalculator.singleInstance;
        calculators[BYTE][BIGDECIMAL]       = BigDecimalCalculator.singleInstance;
        // Short
        calculators[SHORT][NULL]            = null;
        calculators[SHORT][BOOLEAN]         = null;
        calculators[SHORT][CHARACTER]       = IntegerCalculator.singleInstance;
        calculators[SHORT][STRING]          = null;
        calculators[SHORT][ENUM]            = null;
        calculators[SHORT][BYTE]            = IntegerCalculator.singleInstance;
        calculators[SHORT][SHORT]           = IntegerCalculator.singleInstance;
        calculators[SHORT][INTEGER]         = IntegerCalculator.singleInstance;
        calculators[SHORT][LONG]            = LongCalculator.singleInstance;
        calculators[SHORT][FLOAT]           = FloatCalculator.singleInstance;
        calculators[SHORT][DOUBLE]          = DoubleCalculator.singleInstance;
        calculators[SHORT][BIGINTEGER]      = BigIntegerCalculator.singleInstance;
        calculators[SHORT][BIGDECIMAL]      = BigDecimalCalculator.singleInstance;
        // Integer
        calculators[INTEGER][NULL]          = null;
        calculators[INTEGER][BOOLEAN]       = null;
        calculators[INTEGER][CHARACTER]     = IntegerCalculator.singleInstance;
        calculators[INTEGER][STRING]        = null;
        calculators[INTEGER][ENUM]          = null;
        calculators[INTEGER][BYTE]          = IntegerCalculator.singleInstance;
        calculators[INTEGER][SHORT]         = IntegerCalculator.singleInstance;
        calculators[INTEGER][INTEGER]       = IntegerCalculator.singleInstance;
        calculators[INTEGER][LONG]          = LongCalculator.singleInstance;
        calculators[INTEGER][FLOAT]         = FloatCalculator.singleInstance;
        calculators[INTEGER][DOUBLE]        = DoubleCalculator.singleInstance;
        calculators[INTEGER][BIGINTEGER]    = BigIntegerCalculator.singleInstance;
        calculators[INTEGER][BIGDECIMAL]    = BigDecimalCalculator.singleInstance;
        // Long
        calculators[LONG][NULL]             = null;
        calculators[LONG][BOOLEAN]          = null;
        calculators[LONG][CHARACTER]        = LongCalculator.singleInstance;
        calculators[LONG][STRING]           = null;
        calculators[LONG][ENUM]             = null;
        calculators[LONG][BYTE]             = LongCalculator.singleInstance;
        calculators[LONG][SHORT]            = LongCalculator.singleInstance;
        calculators[LONG][INTEGER]          = LongCalculator.singleInstance;
        calculators[LONG][LONG]             = LongCalculator.singleInstance;
        calculators[LONG][FLOAT]            = FloatCalculator.singleInstance;
        calculators[LONG][DOUBLE]           = DoubleCalculator.singleInstance;
        calculators[LONG][BIGINTEGER]       = BigIntegerCalculator.singleInstance;
        calculators[LONG][BIGDECIMAL]       = BigDecimalCalculator.singleInstance;
        // Float
        calculators[FLOAT][NULL]            = null;
        calculators[FLOAT][BOOLEAN]         = null;
        calculators[FLOAT][CHARACTER]       = FloatCalculator.singleInstance;
        calculators[FLOAT][STRING]          = null;
        calculators[FLOAT][ENUM]            = null;
        calculators[FLOAT][BYTE]            = FloatCalculator.singleInstance;
        calculators[FLOAT][SHORT]           = FloatCalculator.singleInstance;
        calculators[FLOAT][INTEGER]         = FloatCalculator.singleInstance;
        calculators[FLOAT][LONG]            = FloatCalculator.singleInstance;
        calculators[FLOAT][FLOAT]           = FloatCalculator.singleInstance;
        calculators[FLOAT][DOUBLE]          = DoubleCalculator.singleInstance;
        calculators[FLOAT][BIGINTEGER]      = BigDecimalCalculator.singleInstance;
        calculators[FLOAT][BIGDECIMAL]      = BigDecimalCalculator.singleInstance;
        // Double
        calculators[DOUBLE][NULL]           = null;
        calculators[DOUBLE][BOOLEAN]        = null;
        calculators[DOUBLE][CHARACTER]      = DoubleCalculator.singleInstance;
        calculators[DOUBLE][STRING]         = null;
        calculators[DOUBLE][ENUM]           = null;
        calculators[DOUBLE][BYTE]           = DoubleCalculator.singleInstance;
        calculators[DOUBLE][SHORT]          = DoubleCalculator.singleInstance;
        calculators[DOUBLE][INTEGER]        = DoubleCalculator.singleInstance;
        calculators[DOUBLE][LONG]           = DoubleCalculator.singleInstance;
        calculators[DOUBLE][FLOAT]          = DoubleCalculator.singleInstance;
        calculators[DOUBLE][DOUBLE]         = DoubleCalculator.singleInstance;
        calculators[DOUBLE][BIGINTEGER]     = BigDecimalCalculator.singleInstance;
        calculators[DOUBLE][BIGDECIMAL]     = BigDecimalCalculator.singleInstance;
        // BigInteger
        calculators[BIGINTEGER][NULL]       = null;
        calculators[BIGINTEGER][BOOLEAN]    = null;
        calculators[BIGINTEGER][CHARACTER]  = BigIntegerCalculator.singleInstance;
        calculators[BIGINTEGER][STRING]     = null;
        calculators[BIGINTEGER][ENUM]       = null;
        calculators[BIGINTEGER][BYTE]       = BigIntegerCalculator.singleInstance;
        calculators[BIGINTEGER][SHORT]      = BigIntegerCalculator.singleInstance;
        calculators[BIGINTEGER][INTEGER]    = BigIntegerCalculator.singleInstance;
        calculators[BIGINTEGER][LONG]       = BigIntegerCalculator.singleInstance;
        calculators[BIGINTEGER][FLOAT]      = BigDecimalCalculator.singleInstance;
        calculators[BIGINTEGER][DOUBLE]     = BigDecimalCalculator.singleInstance;
        calculators[BIGINTEGER][BIGINTEGER] = BigIntegerCalculator.singleInstance;
        calculators[BIGINTEGER][BIGDECIMAL] = BigDecimalCalculator.singleInstance;
        // BigDecimal
        calculators[BIGDECIMAL][NULL]       = null;
        calculators[BIGDECIMAL][BOOLEAN]    = null;
        calculators[BIGDECIMAL][CHARACTER]  = BigDecimalCalculator.singleInstance;
        calculators[BIGDECIMAL][STRING]     = null;
        calculators[BIGDECIMAL][ENUM]       = null;
        calculators[BIGDECIMAL][BYTE]       = BigDecimalCalculator.singleInstance;
        calculators[BIGDECIMAL][SHORT]      = BigDecimalCalculator.singleInstance;
        calculators[BIGDECIMAL][INTEGER]    = BigDecimalCalculator.singleInstance;
        calculators[BIGDECIMAL][LONG]       = BigDecimalCalculator.singleInstance;
        calculators[BIGDECIMAL][FLOAT]      = BigDecimalCalculator.singleInstance;
        calculators[BIGDECIMAL][DOUBLE]     = BigDecimalCalculator.singleInstance;
        calculators[BIGDECIMAL][BIGINTEGER] = BigDecimalCalculator.singleInstance;
        calculators[BIGDECIMAL][BIGDECIMAL] = BigDecimalCalculator.singleInstance;
    }
    
    public static final Calculator getInstance(Class<?> type) {
        int index = indexOf(type);
        return index < 0 ? null : calculators[index][index];
    }
    
    public static final Calculator getInstance(Class<?> type1, Class<?> type2) {
        if (type1 == type2)
            return getInstance(type1);
        
        int index1 = indexOf(type1);
        if (index1 < 0)
            return null;
        
        int index2 = indexOf(type2);
        if (index2 < 0)
            return null;
        
        return calculators[index1][index2];
    }
    
}

final class IntegerCalculator extends Calculator {
    
    static final Calculator singleInstance = new IntegerCalculator();
    
    private IntegerCalculator() {}
    
    public Class<? extends Number> getType() {
        return Integer.class;
    }
    
    public Number negate(Object v) {
        return -safeValueOf((Integer) v);
    }
    
    public Number add(Object v1, Object v2) {
        return safeValueOf((Integer) v1) + safeValueOf((Integer) v2);
    }
    
    public Number subtract(Object v1, Object v2) {
        return safeValueOf((Integer) v1) - safeValueOf((Integer) v2);
    }
    
    public Number multiply(Object v1, Object v2) {
        return safeValueOf((Integer) v1) * safeValueOf((Integer) v2);
    }
    
    public Number divide(Object v1, Object v2) {
        return safeValueOf((Integer) v1) / safeDivisorOf((Integer) v2);
    }
    
    public Number modulo(Object v1, Object v2) {
        return safeValueOf((Integer) v1) % safeDivisorOf((Integer) v2);
    }
    
}

final class LongCalculator extends Calculator {
    
    static final Calculator singleInstance = new LongCalculator();
    
    private LongCalculator() {}
    
    public Class<? extends Number> getType() {
        return Long.class;
    }
    
    public Number negate(Object v) {
        return -safeValueOf((Long) v);
    }
    
    public Number add(Object v1, Object v2) {
        return safeValueOf((Long) v1) + safeValueOf((Long) v2);
    }
    
    public Number subtract(Object v1, Object v2) {
        return safeValueOf((Long) v1) - safeValueOf((Long) v2);
    }
    
    public Number multiply(Object v1, Object v2) {
        return safeValueOf((Long) v1) * safeValueOf((Long) v2);
    }
    
    public Number divide(Object v1, Object v2) {
        return safeValueOf((Long) v1) / safeDivisorOf((Long) v2);
    }
    
    public Number modulo(Object v1, Object v2) {
        return safeValueOf((Long) v1) % safeDivisorOf((Long) v2);
    }
    
}

final class FloatCalculator extends Calculator {
    
    static final Calculator singleInstance = new FloatCalculator();
    
    private FloatCalculator() {}
    
    public Class<? extends Number> getType() {
        return Float.class;
    }
    
    public Number negate(Object v) {
        return -safeValueOf((Float) v);
    }
    
    public Number add(Object v1, Object v2) {
        return safeValueOf((Float) v1) + safeValueOf((Float) v2);
    }
    
    public Number subtract(Object v1, Object v2) {
        return safeValueOf((Float) v1) - safeValueOf((Float) v2);
    }
    
    public Number multiply(Object v1, Object v2) {
        return safeValueOf((Float) v1) * safeValueOf((Float) v2);
    }
    
    public Number divide(Object v1, Object v2) {
        return safeValueOf((Float) v1) / safeDivisorOf((Float) v2);
    }
    
    public Number modulo(Object v1, Object v2) {
        return safeValueOf((Float) v1) % safeDivisorOf((Float) v2);
    }
    
}

final class DoubleCalculator extends Calculator {
    
    static final Calculator singleInstance = new DoubleCalculator();
    
    private DoubleCalculator() {}
    
    public Class<? extends Number> getType() {
        return Double.class;
    }
    
    public Number negate(Object v) {
        return -safeValueOf((Double) v);
    }
    
    public Number add(Object v1, Object v2) {
        return safeValueOf((Double) v1) + safeValueOf((Double) v2);
    }
    
    public Number subtract(Object v1, Object v2) {
        return safeValueOf((Double) v1) - safeValueOf((Double) v2);
    }
    
    public Number multiply(Object v1, Object v2) {
        return safeValueOf((Double) v1) * safeValueOf((Double) v2);
    }
    
    public Number divide(Object v1, Object v2) {
        return safeValueOf((Double) v1) / safeDivisorOf((Double) v2);
    }
    
    public Number modulo(Object v1, Object v2) {
        return safeValueOf((Double) v1) % safeDivisorOf((Double) v2);
    }
    
}

final class BigIntegerCalculator extends Calculator {
    
    static final Calculator singleInstance = new BigIntegerCalculator();
    
    private BigIntegerCalculator() {}
    
    public Class<? extends Number> getType() {
        return BigInteger.class;
    }
    
    public Number negate(Object v) {
        return safeValueOf((BigInteger) v).negate();
    }
    
    public Number add(Object v1, Object v2) {
        return safeValueOf((BigInteger) v1).add(safeValueOf((BigInteger) v2));
    }
    
    public Number subtract(Object v1, Object v2) {
        return safeValueOf((BigInteger) v1).subtract(safeValueOf((BigInteger) v2));
    }
    
    public Number multiply(Object v1, Object v2) {
        return safeValueOf((BigInteger) v1).multiply(safeValueOf((BigInteger) v2));
    }
    
    public Number divide(Object v1, Object v2) {
        return safeValueOf((BigInteger) v1).divide(safeDivisorOf((BigInteger) v2));
    }
    
    public Number modulo(Object v1, Object v2) {
        return safeValueOf((BigInteger) v1).remainder(safeDivisorOf((BigInteger) v2));
    }
    
}

final class BigDecimalCalculator extends Calculator {
    
    static final Calculator singleInstance = new BigDecimalCalculator();
    
    private BigDecimalCalculator() {}
    
    public Class<? extends Number> getType() {
        return BigDecimal.class;
    }
    
    public Number negate(Object v) {
        return safeValueOf((BigDecimal) v).negate();
    }
    
    public Number add(Object v1, Object v2) {
        return safeValueOf((BigDecimal) v1).add(safeValueOf((BigDecimal) v2));
    }
    
    public Number subtract(Object v1, Object v2) {
        return safeValueOf((BigDecimal) v1).subtract(safeValueOf((BigDecimal) v2));
    }
    
    public Number multiply(Object v1, Object v2) {
        return safeValueOf((BigDecimal) v1).multiply(safeValueOf((BigDecimal) v2));
    }
    
    public Number divide(Object v1, Object v2) {
        return safeValueOf((BigDecimal) v1).divide(safeDivisorOf((BigDecimal) v2));
    }
    
    public Number modulo(Object v1, Object v2) {
        return safeValueOf((BigDecimal) v1).remainder(safeDivisorOf((BigDecimal) v2));
    }
    
}
