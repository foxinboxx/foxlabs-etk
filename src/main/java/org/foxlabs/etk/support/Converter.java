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

public abstract class Converter extends Operator {
    
    public static final int NARROWING = -1;
    public static final int IDENTITY  =  0;
    public static final int WIDENING  =  1;
    
    Converter() {}
    
    public abstract int getKind();
    
    public abstract Class<?> getType();
    
    public abstract Object convert(Object v);
    
    private static final Converter[][] converters = new Converter[PRIMITIVE_COUNT][PRIMITIVE_COUNT];
    
    static {
        // Null
        converters[NULL][NULL]             = null;
        converters[NULL][BOOLEAN]          = new IdentityConverter(Boolean.class);
        converters[NULL][CHARACTER]        = new IdentityConverter(Character.class);
        converters[NULL][STRING]           = new IdentityConverter(String.class);
        converters[NULL][ENUM]             = null; // XXX Special case
        converters[NULL][BYTE]             = new IdentityConverter(Byte.class);
        converters[NULL][SHORT]            = new IdentityConverter(Short.class);
        converters[NULL][INTEGER]          = new IdentityConverter(Integer.class);
        converters[NULL][LONG]             = new IdentityConverter(Long.class);
        converters[NULL][FLOAT]            = new IdentityConverter(Float.class);
        converters[NULL][DOUBLE]           = new IdentityConverter(Double.class);
        converters[NULL][BIGINTEGER]       = new IdentityConverter(BigInteger.class);
        converters[NULL][BIGDECIMAL]       = new IdentityConverter(BigDecimal.class);
        // Boolean
        converters[BOOLEAN][NULL]          = null;
        converters[BOOLEAN][BOOLEAN]       = new IdentityConverter(Boolean.class);
        converters[BOOLEAN][CHARACTER]     = null;
        converters[BOOLEAN][STRING]        = StringConverter.singleInstance;
        converters[BOOLEAN][ENUM]          = null;
        converters[BOOLEAN][BYTE]          = null;
        converters[BOOLEAN][SHORT]         = null;
        converters[BOOLEAN][INTEGER]       = null;
        converters[BOOLEAN][LONG]          = null;
        converters[BOOLEAN][FLOAT]         = null;
        converters[BOOLEAN][DOUBLE]        = null;
        converters[BOOLEAN][BIGINTEGER]    = null;
        converters[BOOLEAN][BIGDECIMAL]    = null;
        // Character
        converters[CHARACTER][NULL]        = null;
        converters[CHARACTER][BOOLEAN]     = CharacterToBooleanConverter.singleInstance;
        converters[CHARACTER][CHARACTER]   = new IdentityConverter(Character.class);
        converters[CHARACTER][STRING]      = CharacterToStringConverter.singleInstance;
        converters[CHARACTER][ENUM]        = null;
        converters[CHARACTER][BYTE]        = CharacterToByteConverter.singleInstance;
        converters[CHARACTER][SHORT]       = CharacterToShortConverter.singleInstance;
        converters[CHARACTER][INTEGER]     = CharacterToIntegerConverter.singleInstance;
        converters[CHARACTER][LONG]        = CharacterToLongConverter.singleInstance;
        converters[CHARACTER][FLOAT]       = CharacterToFloatConverter.singleInstance;
        converters[CHARACTER][DOUBLE]      = CharacterToDoubleConverter.singleInstance;
        converters[CHARACTER][BIGINTEGER]  = CharacterToBigIntegerConverter.singleInstance;
        converters[CHARACTER][BIGDECIMAL]  = CharacterToBigDecimalConverter.singleInstance;
        // String
        converters[STRING][NULL]           = null;
        converters[STRING][BOOLEAN]        = null;
        converters[STRING][CHARACTER]      = null;
        converters[STRING][STRING]         = new IdentityConverter(String.class);
        converters[STRING][ENUM]           = null;
        converters[STRING][BYTE]           = null;
        converters[STRING][SHORT]          = null;
        converters[STRING][INTEGER]        = null;
        converters[STRING][LONG]           = null;
        converters[STRING][FLOAT]          = null;
        converters[STRING][DOUBLE]         = null;
        converters[STRING][BIGINTEGER]     = null;
        converters[STRING][BIGDECIMAL]     = null;
        // Enum
        converters[ENUM][NULL]             = null;
        converters[ENUM][BOOLEAN]          = null;
        converters[ENUM][CHARACTER]        = null;
        converters[ENUM][STRING]           = StringConverter.singleInstance;
        converters[ENUM][ENUM]             = null; // XXX Special case
        converters[ENUM][BYTE]             = EnumToByteConverter.singleInstance;
        converters[ENUM][SHORT]            = EnumToShortConverter.singleInstance;
        converters[ENUM][INTEGER]          = EnumToIntegerConverter.singleInstance;
        converters[ENUM][LONG]             = EnumToLongConverter.singleInstance;
        converters[ENUM][FLOAT]            = EnumToFloatConverter.singleInstance;
        converters[ENUM][DOUBLE]           = EnumToDoubleConverter.singleInstance;
        converters[ENUM][BIGINTEGER]       = EnumToBigIntegerConverter.singleInstance;
        converters[ENUM][BIGDECIMAL]       = EnumToBigDecimalConverter.singleInstance;
        // Byte
        converters[BYTE][NULL]             = null;
        converters[BYTE][BOOLEAN]          = ByteToBooleanConverter.singleInstance;
        converters[BYTE][CHARACTER]        = ByteToCharacterConverter.singleInstance;
        converters[BYTE][STRING]           = StringConverter.singleInstance;
        converters[BYTE][ENUM]             = null;
        converters[BYTE][BYTE]             = new IdentityConverter(Byte.class);
        converters[BYTE][SHORT]            = ByteToShortConverter.singleInstance;
        converters[BYTE][INTEGER]          = ByteToIntegerConverter.singleInstance;
        converters[BYTE][LONG]             = ByteToLongConverter.singleInstance;
        converters[BYTE][FLOAT]            = ByteToFloatConverter.singleInstance;
        converters[BYTE][DOUBLE]           = ByteToDoubleConverter.singleInstance;
        converters[BYTE][BIGINTEGER]       = ByteToBigIntegerConverter.singleInstance;
        converters[BYTE][BIGDECIMAL]       = ByteToBigDecimalConverter.singleInstance;
        // Short
        converters[SHORT][NULL]            = null;
        converters[SHORT][BOOLEAN]         = ShortToBooleanConverter.singleInstance;
        converters[SHORT][CHARACTER]       = ShortToCharacterConverter.singleInstance;
        converters[SHORT][STRING]          = StringConverter.singleInstance;
        converters[SHORT][ENUM]            = null;
        converters[SHORT][BYTE]            = ShortToByteConverter.singleInstance;
        converters[SHORT][SHORT]           = new IdentityConverter(Short.class);
        converters[SHORT][INTEGER]         = ShortToIntegerConverter.singleInstance;
        converters[SHORT][LONG]            = ShortToLongConverter.singleInstance;
        converters[SHORT][FLOAT]           = ShortToFloatConverter.singleInstance;
        converters[SHORT][DOUBLE]          = ShortToDoubleConverter.singleInstance;
        converters[SHORT][BIGINTEGER]      = ShortToBigIntegerConverter.singleInstance;
        converters[SHORT][BIGDECIMAL]      = ShortToBigDecimalConverter.singleInstance;
        // Integer
        converters[INTEGER][NULL]          = null;
        converters[INTEGER][BOOLEAN]       = IntegerToBooleanConverter.singleInstance;
        converters[INTEGER][CHARACTER]     = IntegerToCharacterConverter.singleInstance;
        converters[INTEGER][STRING]        = StringConverter.singleInstance;
        converters[INTEGER][ENUM]          = null;
        converters[INTEGER][BYTE]          = IntegerToByteConverter.singleInstance;
        converters[INTEGER][SHORT]         = IntegerToShortConverter.singleInstance;
        converters[INTEGER][INTEGER]       = new IdentityConverter(Integer.class);
        converters[INTEGER][LONG]          = IntegerToLongConverter.singleInstance;
        converters[INTEGER][FLOAT]         = IntegerToFloatConverter.singleInstance;
        converters[INTEGER][DOUBLE]        = IntegerToDoubleConverter.singleInstance;
        converters[INTEGER][BIGINTEGER]    = IntegerToBigIntegerConverter.singleInstance;
        converters[INTEGER][BIGDECIMAL]    = IntegerToBigDecimalConverter.singleInstance;
        // Long
        converters[LONG][NULL]             = null;
        converters[LONG][BOOLEAN]          = LongToBooleanConverter.singleInstance;
        converters[LONG][CHARACTER]        = LongToCharacterConverter.singleInstance;
        converters[LONG][STRING]           = StringConverter.singleInstance;
        converters[LONG][ENUM]             = null;
        converters[LONG][BYTE]             = LongToByteConverter.singleInstance;
        converters[LONG][SHORT]            = LongToShortConverter.singleInstance;
        converters[LONG][INTEGER]          = LongToIntegerConverter.singleInstance;
        converters[LONG][LONG]             = new IdentityConverter(Long.class);
        converters[LONG][FLOAT]            = LongToFloatConverter.singleInstance;
        converters[LONG][DOUBLE]           = LongToDoubleConverter.singleInstance;
        converters[LONG][BIGINTEGER]       = LongToBigIntegerConverter.singleInstance;
        converters[LONG][BIGDECIMAL]       = LongToBigDecimalConverter.singleInstance;
        // Float
        converters[FLOAT][NULL]            = null;
        converters[FLOAT][BOOLEAN]         = FloatToBooleanConverter.singleInstance;
        converters[FLOAT][CHARACTER]       = FloatToCharacterConverter.singleInstance;
        converters[FLOAT][STRING]          = StringConverter.singleInstance;
        converters[FLOAT][ENUM]            = null;
        converters[FLOAT][BYTE]            = FloatToByteConverter.singleInstance;
        converters[FLOAT][SHORT]           = FloatToShortConverter.singleInstance;
        converters[FLOAT][INTEGER]         = FloatToIntegerConverter.singleInstance;
        converters[FLOAT][LONG]            = FloatToLongConverter.singleInstance;
        converters[FLOAT][FLOAT]           = new IdentityConverter(Float.class);
        converters[FLOAT][DOUBLE]          = FloatToDoubleConverter.singleInstance;
        converters[FLOAT][BIGINTEGER]      = FloatToBigIntegerConverter.singleInstance;
        converters[FLOAT][BIGDECIMAL]      = FloatToBigDecimalConverter.singleInstance;
        // Double
        converters[DOUBLE][NULL]           = null;
        converters[DOUBLE][BOOLEAN]        = DoubleToBooleanConverter.singleInstance;
        converters[DOUBLE][CHARACTER]      = DoubleToCharacterConverter.singleInstance;
        converters[DOUBLE][STRING]         = StringConverter.singleInstance;
        converters[DOUBLE][ENUM]           = null;
        converters[DOUBLE][BYTE]           = DoubleToByteConverter.singleInstance;
        converters[DOUBLE][SHORT]          = DoubleToShortConverter.singleInstance;
        converters[DOUBLE][INTEGER]        = DoubleToIntegerConverter.singleInstance;
        converters[DOUBLE][LONG]           = DoubleToLongConverter.singleInstance;
        converters[DOUBLE][FLOAT]          = DoubleToFloatConverter.singleInstance;
        converters[DOUBLE][DOUBLE]         = new IdentityConverter(Double.class);
        converters[DOUBLE][BIGINTEGER]     = DoubleToBigIntegerConverter.singleInstance;
        converters[DOUBLE][BIGDECIMAL]     = DoubleToBigDecimalConverter.singleInstance;
        // BigInteger
        converters[BIGINTEGER][NULL]       = null;
        converters[BIGINTEGER][BOOLEAN]    = BigIntegerToBooleanConverter.singleInstance;
        converters[BIGINTEGER][CHARACTER]  = BigIntegerToCharacterConverter.singleInstance;
        converters[BIGINTEGER][STRING]     = StringConverter.singleInstance;
        converters[BIGINTEGER][ENUM]       = null;
        converters[BIGINTEGER][BYTE]       = BigIntegerToByteConverter.singleInstance;
        converters[BIGINTEGER][SHORT]      = BigIntegerToShortConverter.singleInstance;
        converters[BIGINTEGER][INTEGER]    = BigIntegerToIntegerConverter.singleInstance;
        converters[BIGINTEGER][LONG]       = BigIntegerToLongConverter.singleInstance;
        converters[BIGINTEGER][FLOAT]      = BigIntegerToFloatConverter.singleInstance;
        converters[BIGINTEGER][DOUBLE]     = BigIntegerToDoubleConverter.singleInstance;
        converters[BIGINTEGER][BIGINTEGER] = new IdentityConverter(BigInteger.class);
        converters[BIGINTEGER][BIGDECIMAL] = BigIntegerToBigDecimalConverter.singleInstance;
        // BigDecimal
        converters[BIGDECIMAL][NULL]       = null;
        converters[BIGDECIMAL][BOOLEAN]    = BigDecimalToBooleanConverter.singleInstance;
        converters[BIGDECIMAL][CHARACTER]  = BigDecimalToCharacterConverter.singleInstance;
        converters[BIGDECIMAL][STRING]     = StringConverter.singleInstance;
        converters[BIGDECIMAL][ENUM]       = null;
        converters[BIGDECIMAL][BYTE]       = BigDecimalToByteConverter.singleInstance;
        converters[BIGDECIMAL][SHORT]      = BigDecimalToShortConverter.singleInstance;
        converters[BIGDECIMAL][INTEGER]    = BigDecimalToIntegerConverter.singleInstance;
        converters[BIGDECIMAL][LONG]       = BigDecimalToLongConverter.singleInstance;
        converters[BIGDECIMAL][FLOAT]      = BigDecimalToFloatConverter.singleInstance;
        converters[BIGDECIMAL][DOUBLE]     = BigDecimalToDoubleConverter.singleInstance;
        converters[BIGDECIMAL][BIGINTEGER] = BigDecimalToBigIntegerConverter.singleInstance;
        converters[BIGDECIMAL][BIGDECIMAL] = new IdentityConverter(BigDecimal.class);
    }
    
    public static final Converter getInstance(Class<?> fromType, Class<?> toType) {
        if (toType == null)
            return null;
        
        if (fromType == toType) {
            if (toType.isEnum())
                return new IdentityConverter(toType);
            
            int index = indexOf(toType);
            if (index < 0)
                return new IdentityConverter(toType);
            
            return converters[index][index];
        }
        
        if (fromType == null) {
            if (toType.isEnum())
                return new IdentityConverter(toType);
            
            int index = indexOf(toType);
            if (index < 0)
                return new IdentityConverter(toType);
            
            return converters[NULL][index];
        }
        
        if (toType.isAssignableFrom(fromType))
            return new IdentityConverter(toType);
        
        if (fromType == Object.class)
            return new ObjectConverter(toType);
        
        if (fromType.isArray()) {
            if (toType.isArray()) {
                Class<?> fromComponentType = fromType.getComponentType();
                Class<?> toComponentType = toType.getComponentType();
                Converter converter = getInstance(fromComponentType, toComponentType);
                return converter == null ? null : new ArrayConverter(toType, converter);
            }
            
            return null;
        }
        
        int fromIndex = indexOf(fromType);
        if (fromIndex < 0)
            return null;
        
        int toIndex = indexOf(toType);
        if (toIndex < 0)
            return null;
        
        return converters[fromIndex][toIndex];
    }
    
    public static final boolean canAssign(Class<?> fromType, Class<?> toType) {
        if (toType == null)
            return false;
        
        if (fromType == toType || fromType == null)
            return true;
        
        if (toType.isAssignableFrom(fromType))
            return true;
        
        if (fromType.isArray()) {
            if (toType.isArray()) {
                Class<?> fromComponentType = fromType.getComponentType();
                Class<?> toComponentType = toType.getComponentType();
                return canAssign(fromComponentType, toComponentType);
            }
            
            return false;
        }
        
        int fromIndex = indexOf(fromType);
        if (fromIndex < 0)
            return false;
        
        int toIndex = indexOf(toType);
        if (toIndex < 0)
            return false;
        
        Converter converter = converters[fromIndex][toIndex];
        return converter != null && converter.getKind() != NARROWING;
    }
    
    public static final boolean canAssign(Class<?>[] fromTypes, Class<?>[] toTypes, int count) {
        for (int i = 0; i < count; i++)
            if (!canAssign(fromTypes[i], toTypes[i]))
                return false;
        
        return true;
    }
    
}

// ============================================================================

final class IdentityConverter extends Converter {
    
    private final Class<?> type;
    
    IdentityConverter(Class<?> type) {
        this.type = type;
    }
    
    public Class<?> getType() {
        return type;
    }
    
    public int getKind() {
        return IDENTITY;
    }
    
    public Object convert(Object v) {
        return v;
    }
    
}

abstract class WideningConverter extends Converter {
    
    public final int getKind() {
        return WIDENING;
    }
    
}

abstract class NarrowingConverter extends Converter {
    
    public final int getKind() {
        return NARROWING;
    }
    
}

final class ObjectConverter extends NarrowingConverter {
    
    private final Class<?> type;
    
    ObjectConverter(Class<?> type) {
        this.type = type;
    }
    
    public Class<?> getType() {
        return type;
    }
    
    public Object convert(Object v) {
        return safeCastOf(v, type);
    }
    
}

final class ArrayConverter extends Converter {
    
    private final Class<?> type;
    private final Converter converter;
    
    ArrayConverter(Class<?> type, Converter converter) {
        this.type = type;
        this.converter = converter;
    }
    
    public Class<?> getType() {
        return type;
    }
    
    public int getKind() {
        return converter.getKind();
    }
    
    public Object convert(Object v) {
        if (v == null)
            return null;
        
        Object[] source = (Object[]) v;
        Object[] target = new Object[source.length];
        
        for (int i = 0; i < source.length; i++)
            target[i] = converter.convert(source[i]);
        
        return target;
    }
    
}

// ===== String ===============================================================

final class StringConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new StringConverter();
    
    private StringConverter() {}
    
    public Class<?> getType() {
        return String.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : v.toString();
    }
    
}

// ===== Character ============================================================

final class CharacterToBooleanConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new CharacterToBooleanConverter();
    
    private CharacterToBooleanConverter() {}
    
    public Class<?> getType() {
        return Boolean.class;
    }
    
    public Object convert(Object v) {
        if (v == null)
            return null;
        return ((Character) v).charValue() == 0 ? Boolean.FALSE : Boolean.TRUE;
    }
    
}

final class CharacterToStringConverter extends WideningConverter {
    
    static final Converter singleInstance = new CharacterToStringConverter();
    
    private CharacterToStringConverter() {}
    
    public Class<?> getType() {
        return String.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : v.toString();
    }
    
}

final class CharacterToByteConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new CharacterToByteConverter();
    
    private CharacterToByteConverter() {}
    
    public Class<?> getType() {
        return Byte.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Byte.valueOf((byte) ((Character) v).charValue());
    }
    
}

final class CharacterToShortConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new CharacterToShortConverter();
    
    private CharacterToShortConverter() {}
    
    public Class<?> getType() {
        return Short.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Short.valueOf((short) ((Character) v).charValue());
    }
    
}

final class CharacterToIntegerConverter extends WideningConverter {
    
    static final Converter singleInstance = new CharacterToIntegerConverter();
    
    private CharacterToIntegerConverter() {}
    
    public Class<?> getType() {
        return Integer.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Integer.valueOf(((Character) v).charValue());
    }
    
}

final class CharacterToLongConverter extends WideningConverter {
    
    static final Converter singleInstance = new CharacterToLongConverter();
    
    private CharacterToLongConverter() {}
    
    public Class<?> getType() {
        return Long.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Long.valueOf(((Character) v).charValue());
    }
    
}

final class CharacterToFloatConverter extends WideningConverter {
    
    static final Converter singleInstance = new CharacterToFloatConverter();
    
    private CharacterToFloatConverter() {}
    
    public Class<?> getType() {
        return Float.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Float.valueOf(((Character) v).charValue());
    }
    
}

final class CharacterToDoubleConverter extends WideningConverter {
    
    static final Converter singleInstance = new CharacterToDoubleConverter();
    
    private CharacterToDoubleConverter() {}
    
    public Class<?> getType() {
        return Double.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Double.valueOf(((Character) v).charValue());
    }
    
}

final class CharacterToBigIntegerConverter extends WideningConverter {
    
    static final Converter singleInstance = new CharacterToBigIntegerConverter();
    
    private CharacterToBigIntegerConverter() {}
    
    public Class<?> getType() {
        return BigInteger.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : BigInteger.valueOf(((Character) v).charValue());
    }
    
}

final class CharacterToBigDecimalConverter extends WideningConverter {
    
    static final Converter singleInstance = new CharacterToBigDecimalConverter();
    
    private CharacterToBigDecimalConverter() {}
    
    public Class<?> getType() {
        return BigDecimal.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : BigDecimal.valueOf(((Character) v).charValue());
    }
    
}

// ===== Enum =================================================================

final class EnumToByteConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new EnumToByteConverter();
    
    private EnumToByteConverter() {}
    
    public Class<?> getType() {
        return Byte.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Byte.valueOf((byte) ((Enum<?>) v).ordinal());
    }
    
}

final class EnumToShortConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new EnumToShortConverter();
    
    private EnumToShortConverter() {}
    
    public Class<?> getType() {
        return Short.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Short.valueOf((short) ((Enum<?>) v).ordinal());
    }
    
}

final class EnumToIntegerConverter extends WideningConverter {
    
    static final Converter singleInstance = new EnumToIntegerConverter();
    
    private EnumToIntegerConverter() {}
    
    public Class<?> getType() {
        return Integer.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Integer.valueOf(((Enum<?>) v).ordinal());
    }
    
}

final class EnumToLongConverter extends WideningConverter {
    
    static final Converter singleInstance = new EnumToLongConverter();
    
    private EnumToLongConverter() {}
    
    public Class<?> getType() {
        return Long.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Long.valueOf(((Enum<?>) v).ordinal());
    }
    
}

final class EnumToFloatConverter extends WideningConverter {
    
    static final Converter singleInstance = new EnumToFloatConverter();
    
    private EnumToFloatConverter() {}
    
    public Class<?> getType() {
        return Float.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Float.valueOf(((Enum<?>) v).ordinal());
    }
    
}

final class EnumToDoubleConverter extends WideningConverter {
    
    static final Converter singleInstance = new EnumToDoubleConverter();
    
    private EnumToDoubleConverter() {}
    
    public Class<?> getType() {
        return Double.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Double.valueOf(((Enum<?>) v).ordinal());
    }
    
}

final class EnumToBigIntegerConverter extends WideningConverter {
    
    static final Converter singleInstance = new EnumToBigIntegerConverter();
    
    private EnumToBigIntegerConverter() {}
    
    public Class<?> getType() {
        return BigInteger.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : BigInteger.valueOf(((Enum<?>) v).ordinal());
    }
    
}

final class EnumToBigDecimalConverter extends WideningConverter {
    
    static final Converter singleInstance = new EnumToBigDecimalConverter();
    
    private EnumToBigDecimalConverter() {}
    
    public Class<?> getType() {
        return BigDecimal.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : BigDecimal.valueOf(((Enum<?>) v).ordinal());
    }
    
}

// ===== Byte =================================================================

final class ByteToBooleanConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new ByteToBooleanConverter();
    
    private ByteToBooleanConverter() {}
    
    public Class<?> getType() {
        return Boolean.class;
    }
    
    public Object convert(Object v) {
        if (v == null)
            return null;
        return ((Byte) v).byteValue() == 0 ? Boolean.FALSE : Boolean.TRUE;
    }
    
}

final class ByteToCharacterConverter extends WideningConverter {
    
    static final Converter singleInstance = new ByteToCharacterConverter();
    
    private ByteToCharacterConverter() {}
    
    public Class<?> getType() {
        return Character.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Character.valueOf((char) ((Byte) v).intValue());
    }
    
}

final class ByteToShortConverter extends WideningConverter {
    
    static final Converter singleInstance = new ByteToShortConverter();
    
    private ByteToShortConverter() {}
    
    public Class<?> getType() {
        return Short.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Short.valueOf(((Byte) v).shortValue());
    }
    
}

final class ByteToIntegerConverter extends WideningConverter {
    
    static final Converter singleInstance = new ByteToIntegerConverter();
    
    private ByteToIntegerConverter() {}
    
    public Class<?> getType() {
        return Integer.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Integer.valueOf(((Byte) v).intValue());
    }
    
}

final class ByteToLongConverter extends WideningConverter {
    
    static final Converter singleInstance = new ByteToLongConverter();
    
    private ByteToLongConverter() {}
    
    public Class<?> getType() {
        return Long.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Long.valueOf(((Byte) v).longValue());
    }
    
}

final class ByteToFloatConverter extends WideningConverter {
    
    static final Converter singleInstance = new ByteToFloatConverter();
    
    private ByteToFloatConverter() {}
    
    public Class<?> getType() {
        return Float.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Float.valueOf(((Byte) v).floatValue());
    }
    
}

final class ByteToDoubleConverter extends WideningConverter {
    
    static final Converter singleInstance = new ByteToDoubleConverter();
    
    private ByteToDoubleConverter() {}
    
    public Class<?> getType() {
        return Double.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Double.valueOf(((Byte) v).doubleValue());
    }
    
}

final class ByteToBigIntegerConverter extends WideningConverter {
    
    static final Converter singleInstance = new ByteToBigIntegerConverter();
    
    private ByteToBigIntegerConverter() {}
    
    public Class<?> getType() {
        return BigInteger.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : BigInteger.valueOf(((Byte) v).longValue());
    }
    
}

final class ByteToBigDecimalConverter extends WideningConverter {
    
    static final Converter singleInstance = new ByteToBigDecimalConverter();
    
    private ByteToBigDecimalConverter() {}
    
    public Class<?> getType() {
        return BigDecimal.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : BigDecimal.valueOf(((Byte) v).doubleValue());
    }
    
}

// ===== Short ================================================================

final class ShortToBooleanConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new ShortToBooleanConverter();
    
    private ShortToBooleanConverter() {}
    
    public Class<?> getType() {
        return Boolean.class;
    }
    
    public Object convert(Object v) {
        if (v == null)
            return null;
        return ((Short) v).shortValue() == 0 ? Boolean.FALSE : Boolean.TRUE;
    }
    
}

final class ShortToCharacterConverter extends WideningConverter {
    
    static final Converter singleInstance = new ShortToCharacterConverter();
    
    private ShortToCharacterConverter() {}
    
    public Class<?> getType() {
        return Character.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Character.valueOf((char) ((Short) v).intValue());
    }
    
}

final class ShortToByteConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new ShortToByteConverter();
    
    private ShortToByteConverter() {}
    
    public Class<?> getType() {
        return Byte.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Byte.valueOf(((Short) v).byteValue());
    }
    
}

final class ShortToIntegerConverter extends WideningConverter {
    
    static final Converter singleInstance = new ShortToIntegerConverter();
    
    private ShortToIntegerConverter() {}
    
    public Class<?> getType() {
        return Integer.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Integer.valueOf(((Short) v).intValue());
    }
    
}

final class ShortToLongConverter extends WideningConverter {
    
    static final Converter singleInstance = new ShortToLongConverter();
    
    private ShortToLongConverter() {}
    
    public Class<?> getType() {
        return Long.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Long.valueOf(((Short) v).longValue());
    }
    
}

final class ShortToFloatConverter extends WideningConverter {
    
    static final Converter singleInstance = new ShortToFloatConverter();
    
    private ShortToFloatConverter() {}
    
    public Class<?> getType() {
        return Float.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Float.valueOf(((Short) v).floatValue());
    }
    
}

final class ShortToDoubleConverter extends WideningConverter {
    
    static final Converter singleInstance = new ShortToDoubleConverter();
    
    private ShortToDoubleConverter() {}
    
    public Class<?> getType() {
        return Double.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Double.valueOf(((Short) v).doubleValue());
    }
    
}

final class ShortToBigIntegerConverter extends WideningConverter {
    
    static final Converter singleInstance = new ShortToBigIntegerConverter();
    
    private ShortToBigIntegerConverter() {}
    
    public Class<?> getType() {
        return BigInteger.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : BigInteger.valueOf(((Short) v).longValue());
    }
    
}

final class ShortToBigDecimalConverter extends WideningConverter {
    
    static final Converter singleInstance = new ShortToBigDecimalConverter();
    
    private ShortToBigDecimalConverter() {}
    
    public Class<?> getType() {
        return BigDecimal.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : BigDecimal.valueOf(((Short) v).doubleValue());
    }
    
}

// ===== Integer ==============================================================

final class IntegerToBooleanConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new IntegerToBooleanConverter();
    
    private IntegerToBooleanConverter() {}
    
    public Class<?> getType() {
        return Boolean.class;
    }
    
    public Object convert(Object v) {
        if (v == null)
            return null;
        return ((Integer) v).intValue() == 0 ? Boolean.FALSE : Boolean.TRUE;
    }
    
}

final class IntegerToCharacterConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new IntegerToCharacterConverter();
    
    private IntegerToCharacterConverter() {}
    
    public Class<?> getType() {
        return Character.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Character.valueOf((char) ((Integer) v).intValue());
    }
    
}

final class IntegerToByteConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new IntegerToByteConverter();
    
    private IntegerToByteConverter() {}
    
    public Class<?> getType() {
        return Byte.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Byte.valueOf(((Integer) v).byteValue());
    }
    
}

final class IntegerToShortConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new IntegerToShortConverter();
    
    private IntegerToShortConverter() {}
    
    public Class<?> getType() {
        return Short.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Short.valueOf(((Integer) v).shortValue());
    }
    
}

final class IntegerToLongConverter extends WideningConverter {
    
    static final Converter singleInstance = new IntegerToLongConverter();
    
    private IntegerToLongConverter() {}
    
    public Class<?> getType() {
        return Long.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Long.valueOf(((Integer) v).longValue());
    }
    
}

final class IntegerToFloatConverter extends WideningConverter {
    
    static final Converter singleInstance = new IntegerToFloatConverter();
    
    private IntegerToFloatConverter() {}
    
    public Class<?> getType() {
        return Float.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Float.valueOf(((Integer) v).floatValue());
    }
    
}

final class IntegerToDoubleConverter extends WideningConverter {
    
    static final Converter singleInstance = new IntegerToDoubleConverter();
    
    private IntegerToDoubleConverter() {}
    
    public Class<?> getType() {
        return Double.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Double.valueOf(((Integer) v).doubleValue());
    }
    
}

final class IntegerToBigIntegerConverter extends WideningConverter {
    
    static final Converter singleInstance = new IntegerToBigIntegerConverter();
    
    private IntegerToBigIntegerConverter() {}
    
    public Class<?> getType() {
        return BigInteger.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : BigInteger.valueOf(((Integer) v).longValue());
    }
    
}

final class IntegerToBigDecimalConverter extends WideningConverter {
    
    static final Converter singleInstance = new IntegerToBigDecimalConverter();
    
    private IntegerToBigDecimalConverter() {}
    
    public Class<?> getType() {
        return BigDecimal.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : BigDecimal.valueOf(((Integer) v).doubleValue());
    }
    
}

// ===== Long =================================================================

final class LongToBooleanConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new LongToBooleanConverter();
    
    private LongToBooleanConverter() {}
    
    public Class<?> getType() {
        return Boolean.class;
    }
    
    public Object convert(Object v) {
        if (v == null)
            return null;
        return ((Long) v).longValue() == 0 ? Boolean.FALSE : Boolean.TRUE;
    }
    
}

final class LongToCharacterConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new LongToCharacterConverter();
    
    private LongToCharacterConverter() {}
    
    public Class<?> getType() {
        return Character.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Character.valueOf((char) ((Long) v).longValue());
    }
    
}

final class LongToByteConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new LongToByteConverter();
    
    private LongToByteConverter() {}
    
    public Class<?> getType() {
        return Byte.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Byte.valueOf(((Long) v).byteValue());
    }
    
}

final class LongToShortConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new LongToShortConverter();
    
    private LongToShortConverter() {}
    
    public Class<?> getType() {
        return Short.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Short.valueOf(((Long) v).shortValue());
    }
    
}

final class LongToIntegerConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new LongToIntegerConverter();
    
    private LongToIntegerConverter() {}
    
    public Class<?> getType() {
        return Integer.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Integer.valueOf(((Long) v).intValue());
    }
    
}

final class LongToFloatConverter extends WideningConverter {
    
    static final Converter singleInstance = new LongToFloatConverter();
    
    private LongToFloatConverter() {}
    
    public Class<?> getType() {
        return Float.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Float.valueOf(((Long) v).floatValue());
    }
    
}

final class LongToDoubleConverter extends WideningConverter {
    
    static final Converter singleInstance = new LongToDoubleConverter();
    
    private LongToDoubleConverter() {}
    
    public Class<?> getType() {
        return Double.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Double.valueOf(((Long) v).doubleValue());
    }
    
}

final class LongToBigIntegerConverter extends WideningConverter {
    
    static final Converter singleInstance = new LongToBigIntegerConverter();
    
    private LongToBigIntegerConverter() {}
    
    public Class<?> getType() {
        return BigInteger.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : BigInteger.valueOf(((Long) v).longValue());
    }
    
}

final class LongToBigDecimalConverter extends WideningConverter {
    
    static final Converter singleInstance = new LongToBigDecimalConverter();
    
    private LongToBigDecimalConverter() {}
    
    public Class<?> getType() {
        return BigDecimal.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : BigDecimal.valueOf(((Long) v).doubleValue());
    }
    
}

// ===== Float ================================================================

final class FloatToBooleanConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new FloatToBooleanConverter();
    
    private FloatToBooleanConverter() {}
    
    public Class<?> getType() {
        return Boolean.class;
    }
    
    public Object convert(Object v) {
        if (v == null)
            return null;
        return ((Float) v).floatValue() == .0f ? Boolean.FALSE : Boolean.TRUE;
    }
    
}

final class FloatToCharacterConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new FloatToCharacterConverter();
    
    private FloatToCharacterConverter() {}
    
    public Class<?> getType() {
        return Character.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Character.valueOf((char) ((Float) v).floatValue());
    }
    
}

final class FloatToByteConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new FloatToByteConverter();
    
    private FloatToByteConverter() {}
    
    public Class<?> getType() {
        return Byte.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Byte.valueOf(((Float) v).byteValue());
    }
    
}

final class FloatToShortConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new FloatToShortConverter();
    
    private FloatToShortConverter() {}
    
    public Class<?> getType() {
        return Short.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Short.valueOf(((Float) v).shortValue());
    }
    
}

final class FloatToIntegerConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new FloatToIntegerConverter();
    
    private FloatToIntegerConverter() {}
    
    public Class<?> getType() {
        return Integer.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Integer.valueOf(((Float) v).intValue());
    }
    
}

final class FloatToLongConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new FloatToLongConverter();
    
    private FloatToLongConverter() {}
    
    public Class<?> getType() {
        return Long.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Long.valueOf(((Float) v).longValue());
    }
    
}

final class FloatToDoubleConverter extends WideningConverter {
    
    static final Converter singleInstance = new FloatToDoubleConverter();
    
    private FloatToDoubleConverter() {}
    
    public Class<?> getType() {
        return Double.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Double.valueOf(((Float) v).doubleValue());
    }
    
}

final class FloatToBigIntegerConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new FloatToBigIntegerConverter();
    
    private FloatToBigIntegerConverter() {}
    
    public Class<?> getType() {
        return BigInteger.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : BigDecimal.valueOf(((Float) v).doubleValue()).toBigInteger();
    }
    
}

final class FloatToBigDecimalConverter extends WideningConverter {
    
    static final Converter singleInstance = new FloatToBigDecimalConverter();
    
    private FloatToBigDecimalConverter() {}
    
    public Class<?> getType() {
        return BigDecimal.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : BigDecimal.valueOf(((Float) v).doubleValue());
    }
    
}

// ===== Double ===============================================================

final class DoubleToBooleanConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new DoubleToBooleanConverter();
    
    private DoubleToBooleanConverter() {}
    
    public Class<?> getType() {
        return Boolean.class;
    }
    
    public Object convert(Object v) {
        if (v == null)
            return null;
        return ((Double) v).doubleValue() == .0d ? Boolean.FALSE : Boolean.TRUE;
    }
    
}

final class DoubleToCharacterConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new DoubleToCharacterConverter();
    
    private DoubleToCharacterConverter() {}
    
    public Class<?> getType() {
        return Character.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Character.valueOf((char) ((Double) v).doubleValue());
    }
    
}

final class DoubleToByteConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new DoubleToByteConverter();
    
    private DoubleToByteConverter() {}
    
    public Class<?> getType() {
        return Byte.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Byte.valueOf(((Double) v).byteValue());
    }
    
}

final class DoubleToShortConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new DoubleToShortConverter();
    
    private DoubleToShortConverter() {}
    
    public Class<?> getType() {
        return Short.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Short.valueOf(((Double) v).shortValue());
    }
    
}

final class DoubleToIntegerConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new DoubleToIntegerConverter();
    
    private DoubleToIntegerConverter() {}
    
    public Class<?> getType() {
        return Integer.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Integer.valueOf(((Double) v).intValue());
    }
    
}

final class DoubleToLongConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new DoubleToLongConverter();
    
    private DoubleToLongConverter() {}
    
    public Class<?> getType() {
        return Long.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Long.valueOf(((Double) v).longValue());
    }
    
}

final class DoubleToFloatConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new DoubleToFloatConverter();
    
    private DoubleToFloatConverter() {}
    
    public Class<?> getType() {
        return Float.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Float.valueOf(((Double) v).floatValue());
    }
    
}

final class DoubleToBigIntegerConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new DoubleToBigIntegerConverter();
    
    private DoubleToBigIntegerConverter() {}
    
    public Class<?> getType() {
        return BigInteger.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : BigDecimal.valueOf(((Double) v).doubleValue()).toBigInteger();
    }
    
}

final class DoubleToBigDecimalConverter extends WideningConverter {
    
    static final Converter singleInstance = new DoubleToBigDecimalConverter();
    
    private DoubleToBigDecimalConverter() {}
    
    public Class<?> getType() {
        return BigDecimal.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : BigDecimal.valueOf(((Double) v).doubleValue());
    }
    
}

// ===== BigInteger ===========================================================

final class BigIntegerToBooleanConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new BigIntegerToBooleanConverter();
    
    private BigIntegerToBooleanConverter() {}
    
    public Class<?> getType() {
        return Boolean.class;
    }
    
    public Object convert(Object v) {
        if (v == null)
            return null;
        return BigInteger.ZERO.equals((BigInteger) v) ? Boolean.FALSE : Boolean.TRUE;
    }
    
}

final class BigIntegerToCharacterConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new BigIntegerToCharacterConverter();
    
    private BigIntegerToCharacterConverter() {}
    
    public Class<?> getType() {
        return Character.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Character.valueOf((char) ((BigInteger) v).intValue());
    }
    
}

final class BigIntegerToByteConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new BigIntegerToByteConverter();
    
    private BigIntegerToByteConverter() {}
    
    public Class<?> getType() {
        return Byte.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Byte.valueOf(((BigInteger) v).byteValue());
    }
    
}

final class BigIntegerToShortConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new BigIntegerToShortConverter();
    
    private BigIntegerToShortConverter() {}
    
    public Class<?> getType() {
        return Short.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Short.valueOf(((BigInteger) v).shortValue());
    }
    
}

final class BigIntegerToIntegerConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new BigIntegerToIntegerConverter();
    
    private BigIntegerToIntegerConverter() {}
    
    public Class<?> getType() {
        return Integer.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Integer.valueOf(((BigInteger) v).intValue());
    }
    
}

final class BigIntegerToLongConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new BigIntegerToLongConverter();
    
    private BigIntegerToLongConverter() {}
    
    public Class<?> getType() {
        return Long.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Long.valueOf(((BigInteger) v).longValue());
    }
    
}

final class BigIntegerToFloatConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new BigIntegerToFloatConverter();
    
    private BigIntegerToFloatConverter() {}
    
    public Class<?> getType() {
        return Float.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Float.valueOf(((BigInteger) v).floatValue());
    }
    
}

final class BigIntegerToDoubleConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new BigIntegerToDoubleConverter();
    
    private BigIntegerToDoubleConverter() {}
    
    public Class<?> getType() {
        return Double.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Double.valueOf(((BigInteger) v).doubleValue());
    }
    
}

final class BigIntegerToBigDecimalConverter extends WideningConverter {
    
    static final Converter singleInstance = new BigIntegerToBigDecimalConverter();
    
    private BigIntegerToBigDecimalConverter() {}
    
    public Class<?> getType() {
        return BigDecimal.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : new BigDecimal((BigInteger) v);
    }
    
}

// ===== BigDecimal ===========================================================

final class BigDecimalToBooleanConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new BigDecimalToBooleanConverter();
    
    private BigDecimalToBooleanConverter() {}
    
    public Class<?> getType() {
        return Boolean.class;
    }
    
    public Object convert(Object v) {
        if (v == null)
            return null;
        return BigDecimal.ZERO.equals((BigDecimal) v) ? Boolean.FALSE : Boolean.TRUE;
    }
    
}

final class BigDecimalToCharacterConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new BigDecimalToCharacterConverter();
    
    private BigDecimalToCharacterConverter() {}
    
    public Class<?> getType() {
        return Character.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Character.valueOf((char) ((BigDecimal) v).intValue());
    }
    
}

final class BigDecimalToByteConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new BigDecimalToByteConverter();
    
    private BigDecimalToByteConverter() {}
    
    public Class<?> getType() {
        return Byte.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Byte.valueOf(((BigDecimal) v).byteValue());
    }
    
}

final class BigDecimalToShortConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new BigDecimalToShortConverter();
    
    private BigDecimalToShortConverter() {}
    
    public Class<?> getType() {
        return Short.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Short.valueOf(((BigDecimal) v).shortValue());
    }
    
}

final class BigDecimalToIntegerConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new BigDecimalToIntegerConverter();
    
    private BigDecimalToIntegerConverter() {}
    
    public Class<?> getType() {
        return Integer.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Integer.valueOf(((BigDecimal) v).intValue());
    }
    
}

final class BigDecimalToLongConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new BigDecimalToLongConverter();
    
    private BigDecimalToLongConverter() {}
    
    public Class<?> getType() {
        return Long.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Long.valueOf(((BigDecimal) v).longValue());
    }
    
}

final class BigDecimalToFloatConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new BigDecimalToFloatConverter();
    
    private BigDecimalToFloatConverter() {}
    
    public Class<?> getType() {
        return Float.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Float.valueOf(((BigDecimal) v).floatValue());
    }
    
}

final class BigDecimalToDoubleConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new BigDecimalToDoubleConverter();
    
    private BigDecimalToDoubleConverter() {}
    
    public Class<?> getType() {
        return Double.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : Double.valueOf(((BigDecimal) v).doubleValue());
    }
    
}

final class BigDecimalToBigIntegerConverter extends NarrowingConverter {
    
    static final Converter singleInstance = new BigDecimalToBigIntegerConverter();
    
    private BigDecimalToBigIntegerConverter() {}
    
    public Class<?> getType() {
        return BigInteger.class;
    }
    
    public Object convert(Object v) {
        return v == null ? null : ((BigDecimal) v).toBigInteger();
    }
    
}
