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

public abstract class Comparator extends Operator {
    
    Comparator() {}
    
    public abstract Class<?> getType();
    
    public abstract int compare(Object v1, Object v2);
    
    private static final Comparator[][] comparators = new Comparator[PRIMITIVE_COUNT][PRIMITIVE_COUNT];
    
    static {
        // Null
        comparators[NULL][NULL]             = null;
        comparators[NULL][BOOLEAN]          = null;
        comparators[NULL][CHARACTER]        = null;
        comparators[NULL][STRING]           = null;
        comparators[NULL][ENUM]             = null;
        comparators[NULL][BYTE]             = null;
        comparators[NULL][SHORT]            = null;
        comparators[NULL][INTEGER]          = null;
        comparators[NULL][LONG]             = null;
        comparators[NULL][FLOAT]            = null;
        comparators[NULL][DOUBLE]           = null;
        comparators[NULL][BIGINTEGER]       = null;
        comparators[NULL][BIGDECIMAL]       = null;
        // Boolean
        comparators[BOOLEAN][NULL]          = null;
        comparators[BOOLEAN][BOOLEAN]       = null;
        comparators[BOOLEAN][CHARACTER]     = null;
        comparators[BOOLEAN][STRING]        = null;
        comparators[BOOLEAN][ENUM]          = null;
        comparators[BOOLEAN][BYTE]          = null;
        comparators[BOOLEAN][SHORT]         = null;
        comparators[BOOLEAN][INTEGER]       = null;
        comparators[BOOLEAN][LONG]          = null;
        comparators[BOOLEAN][FLOAT]         = null;
        comparators[BOOLEAN][DOUBLE]        = null;
        comparators[BOOLEAN][BIGINTEGER]    = null;
        comparators[BOOLEAN][BIGDECIMAL]    = null;
        // Character
        comparators[CHARACTER][NULL]        = null;
        comparators[CHARACTER][BOOLEAN]     = null;
        comparators[CHARACTER][CHARACTER]   = CharacterComparator.singleInstance;
        comparators[CHARACTER][STRING]      = StringComparator.singleInstance;
        comparators[CHARACTER][ENUM]        = IntegerComparator.singleInstance;
        comparators[CHARACTER][BYTE]        = IntegerComparator.singleInstance;
        comparators[CHARACTER][SHORT]       = IntegerComparator.singleInstance;
        comparators[CHARACTER][INTEGER]     = IntegerComparator.singleInstance;
        comparators[CHARACTER][LONG]        = LongComparator.singleInstance;
        comparators[CHARACTER][FLOAT]       = FloatComparator.singleInstance;
        comparators[CHARACTER][DOUBLE]      = DoubleComparator.singleInstance;
        comparators[CHARACTER][BIGINTEGER]  = BigIntegerComparator.singleInstance;
        comparators[CHARACTER][BIGDECIMAL]  = BigDecimalComparator.singleInstance;
        // String
        comparators[STRING][NULL]           = null;
        comparators[STRING][BOOLEAN]        = null;
        comparators[STRING][CHARACTER]      = StringComparator.singleInstance;
        comparators[STRING][STRING]         = StringComparator.singleInstance;
        comparators[STRING][ENUM]           = null;
        comparators[STRING][BYTE]           = null;
        comparators[STRING][SHORT]          = null;
        comparators[STRING][INTEGER]        = null;
        comparators[STRING][LONG]           = null;
        comparators[STRING][FLOAT]          = null;
        comparators[STRING][DOUBLE]         = null;
        comparators[STRING][BIGINTEGER]     = null;
        comparators[STRING][BIGDECIMAL]     = null;
        // Enum
        comparators[ENUM][NULL]             = null;
        comparators[ENUM][BOOLEAN]          = null;
        comparators[ENUM][CHARACTER]        = IntegerComparator.singleInstance;
        comparators[ENUM][STRING]           = null;
        comparators[ENUM][ENUM]             = IntegerComparator.singleInstance;
        comparators[ENUM][BYTE]             = IntegerComparator.singleInstance;
        comparators[ENUM][SHORT]            = IntegerComparator.singleInstance;
        comparators[ENUM][INTEGER]          = IntegerComparator.singleInstance;
        comparators[ENUM][LONG]             = LongComparator.singleInstance;
        comparators[ENUM][FLOAT]            = FloatComparator.singleInstance;
        comparators[ENUM][DOUBLE]           = DoubleComparator.singleInstance;
        comparators[ENUM][BIGINTEGER]       = BigIntegerComparator.singleInstance;
        comparators[ENUM][BIGDECIMAL]       = BigDecimalComparator.singleInstance;
        // Byte
        comparators[BYTE][NULL]             = null;
        comparators[BYTE][BOOLEAN]          = null;
        comparators[BYTE][CHARACTER]        = IntegerComparator.singleInstance;
        comparators[BYTE][STRING]           = null;
        comparators[BYTE][ENUM]             = IntegerComparator.singleInstance;
        comparators[BYTE][BYTE]             = ByteComparator.singleInstance;
        comparators[BYTE][SHORT]            = ShortComparator.singleInstance;
        comparators[BYTE][INTEGER]          = IntegerComparator.singleInstance;
        comparators[BYTE][LONG]             = LongComparator.singleInstance;
        comparators[BYTE][FLOAT]            = FloatComparator.singleInstance;
        comparators[BYTE][DOUBLE]           = DoubleComparator.singleInstance;
        comparators[BYTE][BIGINTEGER]       = BigIntegerComparator.singleInstance;
        comparators[BYTE][BIGDECIMAL]       = BigDecimalComparator.singleInstance;
        // Short
        comparators[SHORT][NULL]            = null;
        comparators[SHORT][BOOLEAN]         = null;
        comparators[SHORT][CHARACTER]       = IntegerComparator.singleInstance;
        comparators[SHORT][STRING]          = null;
        comparators[SHORT][ENUM]            = IntegerComparator.singleInstance;
        comparators[SHORT][BYTE]            = ShortComparator.singleInstance;
        comparators[SHORT][SHORT]           = ShortComparator.singleInstance;
        comparators[SHORT][INTEGER]         = IntegerComparator.singleInstance;
        comparators[SHORT][LONG]            = LongComparator.singleInstance;
        comparators[SHORT][FLOAT]           = FloatComparator.singleInstance;
        comparators[SHORT][DOUBLE]          = DoubleComparator.singleInstance;
        comparators[SHORT][BIGINTEGER]      = BigIntegerComparator.singleInstance;
        comparators[SHORT][BIGDECIMAL]      = BigDecimalComparator.singleInstance;
        // Integer
        comparators[INTEGER][NULL]          = null;
        comparators[INTEGER][BOOLEAN]       = null;
        comparators[INTEGER][CHARACTER]     = IntegerComparator.singleInstance;
        comparators[INTEGER][STRING]        = null;
        comparators[INTEGER][ENUM]          = IntegerComparator.singleInstance;
        comparators[INTEGER][BYTE]          = IntegerComparator.singleInstance;
        comparators[INTEGER][SHORT]         = IntegerComparator.singleInstance;
        comparators[INTEGER][INTEGER]       = IntegerComparator.singleInstance;
        comparators[INTEGER][LONG]          = LongComparator.singleInstance;
        comparators[INTEGER][FLOAT]         = FloatComparator.singleInstance;
        comparators[INTEGER][DOUBLE]        = DoubleComparator.singleInstance;
        comparators[INTEGER][BIGINTEGER]    = BigIntegerComparator.singleInstance;
        comparators[INTEGER][BIGDECIMAL]    = BigDecimalComparator.singleInstance;
        // Long
        comparators[LONG][NULL]             = null;
        comparators[LONG][BOOLEAN]          = null;
        comparators[LONG][CHARACTER]        = LongComparator.singleInstance;
        comparators[LONG][STRING]           = null;
        comparators[LONG][ENUM]             = LongComparator.singleInstance;
        comparators[LONG][BYTE]             = LongComparator.singleInstance;
        comparators[LONG][SHORT]            = LongComparator.singleInstance;
        comparators[LONG][INTEGER]          = LongComparator.singleInstance;
        comparators[LONG][LONG]             = LongComparator.singleInstance;
        comparators[LONG][FLOAT]            = FloatComparator.singleInstance;
        comparators[LONG][DOUBLE]           = DoubleComparator.singleInstance;
        comparators[LONG][BIGINTEGER]       = BigIntegerComparator.singleInstance;
        comparators[LONG][BIGDECIMAL]       = BigDecimalComparator.singleInstance;
        // Float
        comparators[FLOAT][NULL]            = null;
        comparators[FLOAT][BOOLEAN]         = null;
        comparators[FLOAT][CHARACTER]       = FloatComparator.singleInstance;
        comparators[FLOAT][STRING]          = null;
        comparators[FLOAT][ENUM]            = FloatComparator.singleInstance;
        comparators[FLOAT][BYTE]            = FloatComparator.singleInstance;
        comparators[FLOAT][SHORT]           = FloatComparator.singleInstance;
        comparators[FLOAT][INTEGER]         = FloatComparator.singleInstance;
        comparators[FLOAT][LONG]            = FloatComparator.singleInstance;
        comparators[FLOAT][FLOAT]           = FloatComparator.singleInstance;
        comparators[FLOAT][DOUBLE]          = DoubleComparator.singleInstance;
        comparators[FLOAT][BIGINTEGER]      = BigDecimalComparator.singleInstance;
        comparators[FLOAT][BIGDECIMAL]      = BigDecimalComparator.singleInstance;
        // Double
        comparators[DOUBLE][NULL]           = null;
        comparators[DOUBLE][BOOLEAN]        = null;
        comparators[DOUBLE][CHARACTER]      = DoubleComparator.singleInstance;
        comparators[DOUBLE][STRING]         = null;
        comparators[DOUBLE][ENUM]           = DoubleComparator.singleInstance;
        comparators[DOUBLE][BYTE]           = DoubleComparator.singleInstance;
        comparators[DOUBLE][SHORT]          = DoubleComparator.singleInstance;
        comparators[DOUBLE][INTEGER]        = DoubleComparator.singleInstance;
        comparators[DOUBLE][LONG]           = DoubleComparator.singleInstance;
        comparators[DOUBLE][FLOAT]          = DoubleComparator.singleInstance;
        comparators[DOUBLE][DOUBLE]         = DoubleComparator.singleInstance;
        comparators[DOUBLE][BIGINTEGER]     = BigDecimalComparator.singleInstance;
        comparators[DOUBLE][BIGDECIMAL]     = BigDecimalComparator.singleInstance;
        // BigInteger
        comparators[BIGINTEGER][NULL]       = null;
        comparators[BIGINTEGER][BOOLEAN]    = null;
        comparators[BIGINTEGER][CHARACTER]  = BigIntegerComparator.singleInstance;
        comparators[BIGINTEGER][STRING]     = null;
        comparators[BIGINTEGER][ENUM]       = BigIntegerComparator.singleInstance;
        comparators[BIGINTEGER][BYTE]       = BigIntegerComparator.singleInstance;
        comparators[BIGINTEGER][SHORT]      = BigIntegerComparator.singleInstance;
        comparators[BIGINTEGER][INTEGER]    = BigIntegerComparator.singleInstance;
        comparators[BIGINTEGER][LONG]       = BigIntegerComparator.singleInstance;
        comparators[BIGINTEGER][FLOAT]      = BigDecimalComparator.singleInstance;
        comparators[BIGINTEGER][DOUBLE]     = BigDecimalComparator.singleInstance;
        comparators[BIGINTEGER][BIGINTEGER] = BigIntegerComparator.singleInstance;
        comparators[BIGINTEGER][BIGDECIMAL] = BigDecimalComparator.singleInstance;
        // BigDecimal
        comparators[BIGDECIMAL][NULL]       = null;
        comparators[BIGDECIMAL][BOOLEAN]    = null;
        comparators[BIGDECIMAL][CHARACTER]  = BigDecimalComparator.singleInstance;
        comparators[BIGDECIMAL][STRING]     = null;
        comparators[BIGDECIMAL][ENUM]       = BigDecimalComparator.singleInstance;
        comparators[BIGDECIMAL][BYTE]       = BigDecimalComparator.singleInstance;
        comparators[BIGDECIMAL][SHORT]      = BigDecimalComparator.singleInstance;
        comparators[BIGDECIMAL][INTEGER]    = BigDecimalComparator.singleInstance;
        comparators[BIGDECIMAL][LONG]       = BigDecimalComparator.singleInstance;
        comparators[BIGDECIMAL][FLOAT]      = BigDecimalComparator.singleInstance;
        comparators[BIGDECIMAL][DOUBLE]     = BigDecimalComparator.singleInstance;
        comparators[BIGDECIMAL][BIGINTEGER] = BigDecimalComparator.singleInstance;
        comparators[BIGDECIMAL][BIGDECIMAL] = BigDecimalComparator.singleInstance;
    }
    
    public static final Comparator getInstance(Class<?> type) {
        int index = indexOf(type);
        return index < 0 ? null : comparators[index][index];
    }
    
    public static final Comparator getInstance(Class<?> type1, Class<?> type2) {
        if (type1 == type2)
            return getInstance(type1);
        
        int index1 = indexOf(type1);
        if (index1 < 0)
            return null;
        
        int index2 = indexOf(type2);
        if (index2 < 0)
            return null;
        
        return comparators[index1][index2];
    }
    
    private static final Class<?>[][] equalityTypes = new Class<?>[PRIMITIVE_COUNT][PRIMITIVE_COUNT];
    
    static {
        // Null
        equalityTypes[NULL][NULL]             = Object.class;
        equalityTypes[NULL][BOOLEAN]          = Object.class;
        equalityTypes[NULL][CHARACTER]        = Object.class;
        equalityTypes[NULL][STRING]           = Object.class;
        equalityTypes[NULL][ENUM]             = Object.class;
        equalityTypes[NULL][BYTE]             = Object.class;
        equalityTypes[NULL][SHORT]            = Object.class;
        equalityTypes[NULL][INTEGER]          = Object.class;
        equalityTypes[NULL][LONG]             = Object.class;
        equalityTypes[NULL][FLOAT]            = Object.class;
        equalityTypes[NULL][DOUBLE]           = Object.class;
        equalityTypes[NULL][BIGINTEGER]       = Object.class;
        equalityTypes[NULL][BIGDECIMAL]       = Object.class;
        // Boolean
        equalityTypes[BOOLEAN][NULL]          = Object.class;
        equalityTypes[BOOLEAN][BOOLEAN]       = Object.class;
        equalityTypes[BOOLEAN][CHARACTER]     = null;
        equalityTypes[BOOLEAN][STRING]        = null;
        equalityTypes[BOOLEAN][ENUM]          = null;
        equalityTypes[BOOLEAN][BYTE]          = null;
        equalityTypes[BOOLEAN][SHORT]         = null;
        equalityTypes[BOOLEAN][INTEGER]       = null;
        equalityTypes[BOOLEAN][LONG]          = null;
        equalityTypes[BOOLEAN][FLOAT]         = null;
        equalityTypes[BOOLEAN][DOUBLE]        = null;
        equalityTypes[BOOLEAN][BIGINTEGER]    = null;
        equalityTypes[BOOLEAN][BIGDECIMAL]    = null;
        // Character
        equalityTypes[CHARACTER][NULL]        = Object.class;
        equalityTypes[CHARACTER][BOOLEAN]     = null;
        equalityTypes[CHARACTER][CHARACTER]   = Object.class;
        equalityTypes[CHARACTER][STRING]      = String.class;
        equalityTypes[CHARACTER][ENUM]        = Integer.class;
        equalityTypes[CHARACTER][BYTE]        = Integer.class;
        equalityTypes[CHARACTER][SHORT]       = Integer.class;
        equalityTypes[CHARACTER][INTEGER]     = Integer.class;
        equalityTypes[CHARACTER][LONG]        = Long.class;
        equalityTypes[CHARACTER][FLOAT]       = Float.class;
        equalityTypes[CHARACTER][DOUBLE]      = Double.class;
        equalityTypes[CHARACTER][BIGINTEGER]  = BigInteger.class;
        equalityTypes[CHARACTER][BIGDECIMAL]  = BigDecimal.class;
        // String
        equalityTypes[STRING][NULL]           = Object.class;
        equalityTypes[STRING][BOOLEAN]        = null;
        equalityTypes[STRING][CHARACTER]      = String.class;
        equalityTypes[STRING][STRING]         = Object.class;
        equalityTypes[STRING][ENUM]           = null;
        equalityTypes[STRING][BYTE]           = null;
        equalityTypes[STRING][SHORT]          = null;
        equalityTypes[STRING][INTEGER]        = null;
        equalityTypes[STRING][LONG]           = null;
        equalityTypes[STRING][FLOAT]          = null;
        equalityTypes[STRING][DOUBLE]         = null;
        equalityTypes[STRING][BIGINTEGER]     = null;
        equalityTypes[STRING][BIGDECIMAL]     = null;
        // Enum
        equalityTypes[ENUM][NULL]             = Object.class;
        equalityTypes[ENUM][BOOLEAN]          = null;
        equalityTypes[ENUM][CHARACTER]        = Integer.class;
        equalityTypes[ENUM][STRING]           = String.class;
        equalityTypes[ENUM][ENUM]             = null; // XXX Special case
        equalityTypes[ENUM][BYTE]             = Integer.class;
        equalityTypes[ENUM][SHORT]            = Integer.class;
        equalityTypes[ENUM][INTEGER]          = Integer.class;
        equalityTypes[ENUM][LONG]             = Long.class;
        equalityTypes[ENUM][FLOAT]            = Float.class;
        equalityTypes[ENUM][DOUBLE]           = Double.class;
        equalityTypes[ENUM][BIGINTEGER]       = BigInteger.class;
        equalityTypes[ENUM][BIGDECIMAL]       = BigDecimal.class;
        // Byte
        equalityTypes[BYTE][NULL]             = Object.class;
        equalityTypes[BYTE][BOOLEAN]          = null;
        equalityTypes[BYTE][CHARACTER]        = Integer.class;
        equalityTypes[BYTE][STRING]           = null;
        equalityTypes[BYTE][ENUM]             = Integer.class;
        equalityTypes[BYTE][BYTE]             = Object.class;
        equalityTypes[BYTE][SHORT]            = Short.class;
        equalityTypes[BYTE][INTEGER]          = Integer.class;
        equalityTypes[BYTE][LONG]             = Long.class;
        equalityTypes[BYTE][FLOAT]            = Float.class;
        equalityTypes[BYTE][DOUBLE]           = Double.class;
        equalityTypes[BYTE][BIGINTEGER]       = BigInteger.class;
        equalityTypes[BYTE][BIGDECIMAL]       = BigDecimal.class;
        // Short
        equalityTypes[SHORT][NULL]            = Object.class;
        equalityTypes[SHORT][BOOLEAN]         = null;
        equalityTypes[SHORT][CHARACTER]       = Integer.class;
        equalityTypes[SHORT][STRING]          = null;
        equalityTypes[SHORT][ENUM]            = Integer.class;
        equalityTypes[SHORT][BYTE]            = Short.class;
        equalityTypes[SHORT][SHORT]           = Object.class;
        equalityTypes[SHORT][INTEGER]         = Integer.class;
        equalityTypes[SHORT][LONG]            = Long.class;
        equalityTypes[SHORT][FLOAT]           = Float.class;
        equalityTypes[SHORT][DOUBLE]          = Double.class;
        equalityTypes[SHORT][BIGINTEGER]      = BigInteger.class;
        equalityTypes[SHORT][BIGDECIMAL]      = BigDecimal.class;
        // Integer
        equalityTypes[INTEGER][NULL]          = Object.class;
        equalityTypes[INTEGER][BOOLEAN]       = null;
        equalityTypes[INTEGER][CHARACTER]     = Integer.class;
        equalityTypes[INTEGER][STRING]        = null;
        equalityTypes[INTEGER][ENUM]          = Integer.class;
        equalityTypes[INTEGER][BYTE]          = Integer.class;
        equalityTypes[INTEGER][SHORT]         = Integer.class;
        equalityTypes[INTEGER][INTEGER]       = Object.class;
        equalityTypes[INTEGER][LONG]          = Long.class;
        equalityTypes[INTEGER][FLOAT]         = Float.class;
        equalityTypes[INTEGER][DOUBLE]        = Double.class;
        equalityTypes[INTEGER][BIGINTEGER]    = BigInteger.class;
        equalityTypes[INTEGER][BIGDECIMAL]    = BigDecimal.class;
        // Long
        equalityTypes[LONG][NULL]             = Object.class;
        equalityTypes[LONG][BOOLEAN]          = null;
        equalityTypes[LONG][CHARACTER]        = Long.class;
        equalityTypes[LONG][STRING]           = null;
        equalityTypes[LONG][ENUM]             = Long.class;
        equalityTypes[LONG][BYTE]             = Long.class;
        equalityTypes[LONG][SHORT]            = Long.class;
        equalityTypes[LONG][INTEGER]          = Long.class;
        equalityTypes[LONG][LONG]             = Object.class;
        equalityTypes[LONG][FLOAT]            = Float.class;
        equalityTypes[LONG][DOUBLE]           = Double.class;
        equalityTypes[LONG][BIGINTEGER]       = BigInteger.class;
        equalityTypes[LONG][BIGDECIMAL]       = BigDecimal.class;
        // Float
        equalityTypes[FLOAT][NULL]            = Object.class;
        equalityTypes[FLOAT][BOOLEAN]         = null;
        equalityTypes[FLOAT][CHARACTER]       = Float.class;
        equalityTypes[FLOAT][STRING]          = null;
        equalityTypes[FLOAT][ENUM]            = Float.class;
        equalityTypes[FLOAT][BYTE]            = Float.class;
        equalityTypes[FLOAT][SHORT]           = Float.class;
        equalityTypes[FLOAT][INTEGER]         = Float.class;
        equalityTypes[FLOAT][LONG]            = Float.class;
        equalityTypes[FLOAT][FLOAT]           = Object.class;
        equalityTypes[FLOAT][DOUBLE]          = Double.class;
        equalityTypes[FLOAT][BIGINTEGER]      = BigDecimal.class;
        equalityTypes[FLOAT][BIGDECIMAL]      = BigDecimal.class;
        // Double
        equalityTypes[DOUBLE][NULL]           = Object.class;
        equalityTypes[DOUBLE][BOOLEAN]        = null;
        equalityTypes[DOUBLE][CHARACTER]      = Double.class;
        equalityTypes[DOUBLE][STRING]         = null;
        equalityTypes[DOUBLE][ENUM]           = Double.class;
        equalityTypes[DOUBLE][BYTE]           = Double.class;
        equalityTypes[DOUBLE][SHORT]          = Double.class;
        equalityTypes[DOUBLE][INTEGER]        = Double.class;
        equalityTypes[DOUBLE][LONG]           = Double.class;
        equalityTypes[DOUBLE][FLOAT]          = Double.class;
        equalityTypes[DOUBLE][DOUBLE]         = Object.class;
        equalityTypes[DOUBLE][BIGINTEGER]     = BigDecimal.class;
        equalityTypes[DOUBLE][BIGDECIMAL]     = BigDecimal.class;
        // BigInteger
        equalityTypes[BIGINTEGER][NULL]       = Object.class;
        equalityTypes[BIGINTEGER][BOOLEAN]    = null;
        equalityTypes[BIGINTEGER][CHARACTER]  = BigInteger.class;
        equalityTypes[BIGINTEGER][STRING]     = null;
        equalityTypes[BIGINTEGER][ENUM]       = BigInteger.class;
        equalityTypes[BIGINTEGER][BYTE]       = BigInteger.class;
        equalityTypes[BIGINTEGER][SHORT]      = BigInteger.class;
        equalityTypes[BIGINTEGER][INTEGER]    = BigInteger.class;
        equalityTypes[BIGINTEGER][LONG]       = BigInteger.class;
        equalityTypes[BIGINTEGER][FLOAT]      = BigDecimal.class;
        equalityTypes[BIGINTEGER][DOUBLE]     = BigDecimal.class;
        equalityTypes[BIGINTEGER][BIGINTEGER] = Object.class;
        equalityTypes[BIGINTEGER][BIGDECIMAL] = BigDecimal.class;
        // BigDecimal
        equalityTypes[BIGDECIMAL][NULL]       = Object.class;
        equalityTypes[BIGDECIMAL][BOOLEAN]    = null;
        equalityTypes[BIGDECIMAL][CHARACTER]  = BigDecimal.class;
        equalityTypes[BIGDECIMAL][STRING]     = null;
        equalityTypes[BIGDECIMAL][ENUM]       = BigDecimal.class;
        equalityTypes[BIGDECIMAL][BYTE]       = BigDecimal.class;
        equalityTypes[BIGDECIMAL][SHORT]      = BigDecimal.class;
        equalityTypes[BIGDECIMAL][INTEGER]    = BigDecimal.class;
        equalityTypes[BIGDECIMAL][LONG]       = BigDecimal.class;
        equalityTypes[BIGDECIMAL][FLOAT]      = BigDecimal.class;
        equalityTypes[BIGDECIMAL][DOUBLE]     = BigDecimal.class;
        equalityTypes[BIGDECIMAL][BIGINTEGER] = BigDecimal.class;
        equalityTypes[BIGDECIMAL][BIGDECIMAL] = Object.class;
    }
    
    public static final Class<?> getEqualityType(Class<?> type1, Class<?> type2) {
        if (type1 == type2 || type1 == null || type2 == null)
            return Object.class;
        
        if (type1.isAssignableFrom(type2) || type2.isAssignableFrom(type1))
            return Object.class;
        
        int index1 = indexOf(type1);
        if (index1 < 0)
            return null;
        
        int index2 = indexOf(type2);
        if (index2 < 0)
            return null;
        
        return equalityTypes[index1][index2];
    }
    
}

final class CharacterComparator extends Comparator {
    
    static final Comparator singleInstance = new CharacterComparator();
    
    private CharacterComparator() {}
    
    public Class<?> getType() {
        return Character.class;
    }
    
    public int compare(Object v1, Object v2) {
        return safeValueOf((Character) v1).compareTo(safeValueOf((Character) v2));
    }

}

final class StringComparator extends Comparator {
    
    static final Comparator singleInstance = new StringComparator();
    
    private StringComparator() {}
    
    public Class<?> getType() {
        return String.class;
    }
    
    public int compare(Object v1, Object v2) {
        return safeValueOf((String) v1).compareTo(safeValueOf((String) v2));
    }
    
}

final class ByteComparator extends Comparator {
    
    static final Comparator singleInstance = new ByteComparator();
    
    private ByteComparator() {}
    
    public Class<?> getType() {
        return Byte.class;
    }
    
    public int compare(Object v1, Object v2) {
        return safeValueOf((Byte) v1).compareTo(safeValueOf((Byte) v2));
    }
    
}

final class ShortComparator extends Comparator {
    
    static final Comparator singleInstance = new ShortComparator();
    
    private ShortComparator() {}
    
    public Class<?> getType() {
        return Short.class;
    }
    
    public int compare(Object v1, Object v2) {
        return safeValueOf((Short) v1).compareTo(safeValueOf((Short) v2));
    }
    
}

final class IntegerComparator extends Comparator {
    
    static final Comparator singleInstance = new IntegerComparator();
    
    private IntegerComparator() {}
    
    public Class<?> getType() {
        return Integer.class;
    }
    
    public int compare(Object v1, Object v2) {
        return safeValueOf((Integer) v1).compareTo(safeValueOf((Integer) v2));
    }
    
}

final class LongComparator extends Comparator {
    
    static final Comparator singleInstance = new LongComparator();
    
    private LongComparator() {}
    
    public Class<?> getType() {
        return Long.class;
    }
    
    public int compare(Object v1, Object v2) {
        return safeValueOf((Long) v1).compareTo(safeValueOf((Long) v2));
    }
    
}

final class FloatComparator extends Comparator {
    
    static final Comparator singleInstance = new FloatComparator();
    
    private FloatComparator() {}
    
    public Class<?> getType() {
        return Float.class;
    }
    
    public int compare(Object v1, Object v2) {
        return safeValueOf((Float) v1).compareTo(safeValueOf((Float) v2));
    }
    
}

final class DoubleComparator extends Comparator {
    
    static final Comparator singleInstance = new DoubleComparator();
    
    private DoubleComparator() {}
    
    public Class<?> getType() {
        return Double.class;
    }
    
    public int compare(Object v1, Object v2) {
        return safeValueOf((Double) v1).compareTo(safeValueOf((Double) v2));
    }
    
}

final class BigIntegerComparator extends Comparator {
    
    static final Comparator singleInstance = new BigIntegerComparator();
    
    private BigIntegerComparator() {}
    
    public Class<?> getType() {
        return BigInteger.class;
    }
    
    public int compare(Object v1, Object v2) {
        return safeValueOf((BigInteger) v1).compareTo(safeValueOf((BigInteger) v2));
    }
    
}

final class BigDecimalComparator extends Comparator {
    
    static final Comparator singleInstance = new BigDecimalComparator();
    
    private BigDecimalComparator() {}
    
    public Class<?> getType() {
        return BigDecimal.class;
    }
    
    public int compare(Object v1, Object v2) {
        return safeValueOf((BigDecimal) v1).compareTo(safeValueOf((BigDecimal) v2));
    }
    
}
