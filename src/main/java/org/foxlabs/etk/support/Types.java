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

public final class Types {
    
    public static final Class<?>[] VOID = new Class<?>[0];
    
    public static final int PRIMITIVE_COUNT = 13;
    
    public static final int NULL       = 0;
    public static final int BOOLEAN    = 1;
    public static final int CHARACTER  = 2;
    public static final int STRING     = 3;
    public static final int ENUM       = 4;
    public static final int BYTE       = 5;
    public static final int SHORT      = 6;
    public static final int INTEGER    = 7;
    public static final int LONG       = 8;
    public static final int FLOAT      = 9;
    public static final int DOUBLE     = 10;
    public static final int BIGINTEGER = 11;
    public static final int BIGDECIMAL = 12;
    
    private static final String[] primitiveNames = new String[PRIMITIVE_COUNT];
    
    static {
        primitiveNames[NULL]       = "null";
        primitiveNames[BOOLEAN]    = "boolean";
        primitiveNames[CHARACTER]  = "char";
        primitiveNames[STRING]     = "string";
        primitiveNames[ENUM]       = "enum";
        primitiveNames[BYTE]       = "byte";
        primitiveNames[SHORT]      = "short";
        primitiveNames[INTEGER]    = "int";
        primitiveNames[LONG]       = "long";
        primitiveNames[FLOAT]      = "float";
        primitiveNames[DOUBLE]     = "double";
        primitiveNames[BIGINTEGER] = "bigint";
        primitiveNames[BIGDECIMAL] = "bigdec";
    }
    
    private static final Class<?>[] primitiveTypes = new Class<?>[PRIMITIVE_COUNT];
    
    static {
        primitiveTypes[NULL]       = null;
        primitiveTypes[BOOLEAN]    = Boolean.class;
        primitiveTypes[CHARACTER]  = Character.class;
        primitiveTypes[STRING]     = String.class;
        primitiveTypes[ENUM]       = Enum.class;
        primitiveTypes[BYTE]       = Byte.class;
        primitiveTypes[SHORT]      = Short.class;
        primitiveTypes[INTEGER]    = Integer.class;
        primitiveTypes[LONG]       = Long.class;
        primitiveTypes[FLOAT]      = Float.class;
        primitiveTypes[DOUBLE]     = Double.class;
        primitiveTypes[BIGINTEGER] = BigInteger.class;
        primitiveTypes[BIGDECIMAL] = BigDecimal.class;
    }
    
    public static final java.util.Comparator<Class<?>> COMPARATOR = new java.util.Comparator<Class<?>>() {
        
        public int compare(Class<?> type1, Class<?> type2) {
            return Types.compare(type1, type2);
        }
        
    };
    
    private Types() {}
    
    public static Class<?> wrapperOf(Class<?> type) {
        if (type == null)
            return null;
        
        if (type.isPrimitive()) {
            if (type == Boolean.TYPE)
                return Boolean.class;
            if (type == Character.TYPE)
                return Character.class;
            if (type == Byte.TYPE)
                return Byte.class;
            if (type == Short.TYPE)
                return Short.class;
            if (type == Integer.TYPE)
                return Integer.class;
            if (type == Long.TYPE)
                return Long.class;
            if (type == Float.TYPE)
                return Float.class;
            if (type == Double.TYPE)
                return Double.class;
        }
        
        return type;
    }
    
    public static Class<?>[] wrapperOf(Class<?>... types) {
        if (types.length == 0)
            return types;
        Class<?>[] wrappers = new Class<?>[types.length];
        for (int i = 0; i < types.length; i++)
            wrappers[i] = wrapperOf(types[i]);
        return wrappers;
    }
    
    public static int indexOf(Class<?> type) {
        if (type == null)
            return NULL;
        
        if (type.isEnum())
            return ENUM;
        
        for (int i = 0; i < PRIMITIVE_COUNT; i++)
            if (primitiveTypes[i] == type)
                return i;
        
        return -1;
    }
    
    public static int indexOf(String name) {
        for (int i = 0; i < PRIMITIVE_COUNT; i++)
            if (primitiveNames[i].equalsIgnoreCase(name))
                return i;
        
        return -1;
    }
    
    public static Class<?> typeOf(int index) {
        return index < 0 ? Object.class : primitiveTypes[index];
    }
    
    public static Class<?> typeOf(String name, ClassLoader cl) throws ClassNotFoundException {
        int index = name.lastIndexOf("[]");
        if (index > 0) {
            return arraytypeOf(typeOf(name.substring(0, index), cl));
        } else {
            index = indexOf(name);
            return index < 0 ? cl.loadClass(name) : primitiveTypes[index];
        }
    }
    
    public static boolean isInteger(Class<? extends Number> type) {
        return type == Byte.class ||
               type == Short.class ||
               type == Integer.class ||
               type == Long.class ||
               type == BigInteger.class;
    }
    
    public static boolean isFloat(Class<? extends Number> type) {
        return type == Float.class ||
               type == Double.class ||
               type == BigDecimal.class;
    }
    
    public static Class<?> supertypeOf(Class<?> type) {
        return type == null ? Object.class : type;
    }
    
    public static Class<?> supertypeOf(Class<?> type1, Class<?> type2) {
        if (type1 == type2)
            return type1;
        if (type1 == null)
            return type2;
        if (type2 == null)
            return type1;
        
        if (type1.isAssignableFrom(type2))
            return type1;
        
        if (type2.isAssignableFrom(type1))
            return type2;
        
        return Object.class;
    }
    
    public static Class<?> supertypeOf(Class<?>... types) {
        int count = types.length;
        if (count == 0)
            return Object.class;
        
        Class<?> type = types[0];
        if (count == 1)
            return supertypeOf(type);
        
        for (int i = 1; i < count; i++) {
            type = supertypeOf(type, types[i]);
            if (type == Object.class)
                return Object.class;
        }
        
        return type;
    }
    
    public static Class<?> arraytypeOf(Class<?> type) {
        return java.lang.reflect.Array.newInstance(type, 0).getClass();
    }
    
    public static Enum<?> enumvalueOf(Class<?> type, String name) {
        return Enum.valueOf(type.asSubclass(Enum.class), name);
    }
    
    public static int hashCode(Class<?>... types) {
        if (types.length == 0)
            return 0;
        
        int hash = 1;
        for (Class<?> type : types)
            hash = 31 * hash + (type == null ? 0 : type.hashCode());
        
        return hash;
    }
    
    public static boolean equals(Class<?>[] types1, Class<?>[] types2) {
        int count = types1.length;
        if (count != types2.length)
            return false;
        
        for (int i = 0; i < count; i++)
            if (types1[i] != types2[i])
                return false;
        
        return true;
    }
    
    public static int compare(Class<?> type1, Class<?> type2) {
        return toString(type1).compareTo(toString(type2));
    }
    
    public static int compare(Class<?>[] types1, Class<?>[] types2) {
        int count = types1.length;
        if (count < types2.length)
            return -1;
        if (count > types2.length)
            return 1;
        
        for (int i = 0; i < count; i++) {
            int cmp = compare(types1[i], types2[i]);
            if (cmp != 0)
                return cmp;
        }
        
        return 0;
    }
    
    public static String toString(Class<?> type) {
        if (type.isArray()) {
            StringBuilder buf = new StringBuilder();
            toString(buf, type);
            return buf.toString();
        }
        int index = indexOf(type);
        return index < 0 || index == ENUM ? type.getName() : primitiveNames[index];
    }
    
    public static void toString(StringBuilder buf, Class<?> type) {
        if (type.isArray()) {
            toString(buf, type.getComponentType());
            buf.append('[');
            buf.append(']');
        } else {
            int index = indexOf(type);
            if (index < 0 || index == ENUM) {
                buf.append(type.getName());
            } else {
                buf.append(primitiveNames[index]);
            }
        }
    }
    
    public static String toString(Class<?>... types) {
        if (types.length == 0)
            return "";
        StringBuilder buf = new StringBuilder();
        toString(buf, types);
        return buf.toString();
    }
    
    public static void toString(StringBuilder buf, Class<?>... types) {
        int count = types.length;
        if (count > 0) {
            toString(buf, types[0]);
            for (int i = 1; i < count; i++) {
                buf.append(',');
                toString(buf, types[i]);
            }
        }
    }
    
}
