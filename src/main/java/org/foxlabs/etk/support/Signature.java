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

import static org.foxlabs.etk.support.Converter.canAssign;

public final class Signature implements Comparable<Signature>, java.io.Serializable {
    private static final long serialVersionUID = 6905780704484948027L;
    
    public static final String NULL_NS = "";
    
    private final String name;
    private final String namespace;
    private final Class<?>[] argtypes;
    
    private transient int hash = 0;
    
    public Signature(String name, Class<?>... argtypes) {
        this(NULL_NS, name, argtypes);
    }
    
    public Signature(String name, String namespace, Class<?>... argtypes) {
        if (name == null)
            throw new NullPointerException();
        
        this.name = name;
        this.namespace = namespace == null ? NULL_NS : namespace;
        this.argtypes = argtypes.length == 0 ? Types.VOID : argtypes;
    }
    
    public String getNamespace() {
        return namespace;
    }
    
    public String getLocalName() {
        return name;
    }
    
    public String getQualifiedName() {
        return namespace == NULL_NS ? name : namespace + ":" + name;
    }
    
    public int getMinArgumentCount() {
        int argcount = argtypes.length;
        return isVarArgs() ? argcount - 1 : argcount;
    }
    
    public int getMaxArgumentCount() {
        return isVarArgs() ? Integer.MAX_VALUE : argtypes.length;
    }
    
    public Class<?> getArgumentType(int index) {
        int argcount = argtypes.length;
        
        if (isVarArgs()) {
            if (index > argcount - 2)
                return argtypes[argcount - 1].getComponentType();
        }
        
        return argtypes[index];
    }
    
    public Class<?>[] getArgumentTypes() {
        return argtypes.length == 0 ? argtypes : argtypes.clone();
    }
    
    public boolean isVarArgs() {
        int argcount = argtypes.length;
        return argcount > 0 && argtypes[argcount - 1].isArray();
    }
    
    public boolean isSameArguments(Signature other) {
        return Types.equals(argtypes, other.argtypes);
    }
    
    public boolean isSameArguments(Class<?>... types) {
        return Types.equals(argtypes, types);
    }
    
    public boolean isAssignableArguments(Signature other) {
        if (other.isVarArgs()) {
            if (isVarArgs()) {
                int argcount1 = other.argtypes.length;
                int argcount2 = argtypes.length;
                
                if (argcount1 < argcount2)
                    return false;
                
                if (canAssign(other.argtypes, argtypes, --argcount2)) {
                    Class<?> vartype1 = other.argtypes[--argcount1].getComponentType();
                    Class<?> vartype2 = argtypes[argcount2].getComponentType();
                    if (!canAssign(vartype1, vartype2))
                        return false;
                    
                    for (int i = argcount2; i < argcount1; i++)
                        if (!canAssign(other.argtypes[i], vartype2))
                            return false;
                    
                    return true;
                }
                
                return false;
            }
            
            return false;
        }
        
        return isAssignableArguments(other.argtypes);
    }
    
    public boolean isAssignableArguments(Class<?>... types) {
        int argcount1 = types.length;
        int argcount2 = argtypes.length;
        
        if (argcount1 < argcount2) {
            if (argcount1 + 1 == argcount2 && isVarArgs()) {
                return canAssign(types, argtypes, argcount1);
            }
            
            return false;
        }
        
        if (argcount1 == argcount2) {
            if (argcount1 == 0)
                return true;
            
            if (isVarArgs()) {
                if (canAssign(types, argtypes, --argcount1)) {
                    Class<?> vartype = argtypes[argcount1].getComponentType();
                    return canAssign(types[argcount1], vartype);
                }
                
                return false;
            }
            
            return canAssign(types, argtypes, argcount1);
        }
        
        if (isVarArgs() && canAssign(types, argtypes, --argcount2)) {
            Class<?> vartype = argtypes[argcount2].getComponentType();
            for (int i = argcount2; i < argcount1; i++)
                if (!canAssign(types[i], vartype))
                    return false;
            
            return true;
        }
        
        return false;
    }
    
    public int compareTo(Signature other) {
        int cmp = namespace.compareTo(other.namespace);
        if (cmp == 0) {
            cmp = name.compareTo(other.name);
            if (cmp == 0) {
                return Types.compare(argtypes, other.argtypes);
            }
        }
        return cmp;
    }
    
    public int hashCode() {
        if (hash == 0)
            hash = (namespace + name).hashCode() ^ Types.hashCode(argtypes);
        
        return hash;
    }
    
    public boolean equals(Object obj) {
        if (obj instanceof Signature) {
            Signature other = (Signature) obj;
            return name.equals(other.name) &&
                   namespace.equals(other.namespace) &&
                   Types.equals(argtypes, other.argtypes);
        }
        
        return false;
    }
    
    public boolean equalsIgnoreCase(Object obj) {
        if (obj instanceof Signature) {
            Signature other = (Signature) obj;
            return name.equalsIgnoreCase(other.name) &&
                   namespace.equalsIgnoreCase(other.namespace) &&
                   Types.equals(argtypes, other.argtypes);
        }
        
        return false;
    }
    
    public Signature toCaseInsensitive() {
        return new Signature(name.toUpperCase(),
                             namespace == NULL_NS
                                 ? namespace
                                 : namespace.toUpperCase(),
                             argtypes);
    }
    
    public String toString() {
        StringBuilder buf = new StringBuilder();
        toString(buf);
        return buf.toString();
    }
    
    public void toString(StringBuilder buf) {
        buf.append(getQualifiedName());
        buf.append('(');
        Types.toString(buf, argtypes);
        buf.append(')');
    }
    
}
