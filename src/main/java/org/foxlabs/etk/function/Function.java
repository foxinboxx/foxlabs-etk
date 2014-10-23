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

package org.foxlabs.etk.function;

import org.foxlabs.etk.Environment;
import org.foxlabs.etk.annotation.Determined;
import org.foxlabs.etk.support.Operator;
import org.foxlabs.etk.support.Signature;
import org.foxlabs.etk.support.Types;

public abstract class Function extends Operator {
    
    private final Signature signature;
    private final Class<?> type;
    
    FunctionLoader loader;
    
    protected Function(String name, String namespace, Class<?> type, Class<?>... argtypes) {
        this(new Signature(name, namespace, Types.wrapperOf(argtypes)), type);
    }
    
    protected Function(Signature signature, Class<?> type) {
        if (signature == null || type == null)
            throw new NullPointerException();
        
        this.signature = signature;
        this.type = Types.wrapperOf(type);
    }
    
    public final FunctionLoader getFunctionLoader() {
        return loader;
    }
    
    public final Class<?> getType() {
        return type;
    }
    
    public final Signature getSignature() {
        return signature;
    }
    
    public final String getNamespace() {
        return signature.getNamespace();
    }
    
    public final String getLocalName() {
        return signature.getLocalName();
    }
    
    public final String getQualifiedName() {
        return signature.getQualifiedName();
    }
    
    public final int getMinArgumentCount() {
        return signature.getMinArgumentCount();
    }
    
    public final int getMaxArgumentCount() {
        return signature.getMaxArgumentCount();
    }
    
    public final Class<?> getArgumentType(int index) {
        return signature.getArgumentType(index);
    }
    
    public final Class<?>[] getArgumentTypes() {
        return signature.getArgumentTypes();
    }
    
    public final boolean isVarArgs() {
        return signature.isVarArgs();
    }
    
    public boolean isDetermined() {
        return getClass().isAnnotationPresent(Determined.class);
    }
    
    public abstract Object evaluate(Environment context, Object... arguments)
            throws Exception;
    
    public int hashCode() {
        int hash = getClass().hashCode();
        hash = 31 * hash + type.hashCode();
        hash = 31 * hash + signature.hashCode();
        return hash;
    }
    
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        
        if (obj instanceof Function) {
            Function other = (Function) obj;
            return getClass() == other.getClass() &&
                   type == other.type &&
                   signature.equals(other.signature);
        }
        
        return false;
    }
    
    public final String toString() {
        StringBuilder buf = new StringBuilder();
        
        signature.toString(buf);
        buf.append(':');
        Types.toString(buf, type);
        
        return buf.toString();
    }
    
}
