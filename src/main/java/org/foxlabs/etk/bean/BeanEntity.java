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

package org.foxlabs.etk.bean;

import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Set;
import java.util.TreeSet;

import org.foxlabs.etk.EtkException;
import org.foxlabs.etk.support.Signature;

public final class BeanEntity {
    
    private final BeanManager manager;
    
    private final Class<?> type;
    private BeanEntity parent;
    
    private final Map<String, PropertyAccessor> propertyMap;
    private final Map<Signature, MethodAccessor> methodMap;
    
    protected BeanEntity(BeanManager manager,
                         Class<?> type,
                         PropertyAccessor[] properties,
                         MethodAccessor[] methods) {
        this.manager = manager;
        this.type = type;
        
        propertyMap = new HashMap<String, PropertyAccessor>();
        for (PropertyAccessor accessor : properties)
            propertyMap.put(internalNameOf(accessor.getName()),
                            accessor);
        
        methodMap = new HashMap<Signature, MethodAccessor>();
        for (MethodAccessor accessor : methods)
            methodMap.put(internalSignatureOf(accessor.getSignature()),
                          accessor);
    }
    
    protected BeanEntity(BeanManager manager,
                         Class<?> type,
                         BeanEntity parent,
                         PropertyAccessor[] properties,
                         MethodAccessor[] methods) {
        this(manager, type, properties, methods);
        
        this.parent = parent;
    }
    
    public BeanManager getBeanManager() {
        return manager;
    }
    
    public Class<?> getType() {
        return type;
    }
    
    public BeanEntity getParent() throws EtkException {
        if (parent == null)
            parent = findParent(type.getSuperclass());
        
        return parent == this ? null : parent;
    }
    
    private BeanEntity findParent(Class<?> type) throws EtkException {
        if (type == null)
            return this;
        
        try {
            return manager.getBeanEntity(type, false);
        } catch (BeanNotFoundException e) {
            return findParent(type.getSuperclass());
        }
    }
    
    // Properties
    
    public PropertyAccessor getPropertyAccessor(String name)
            throws PropertyNotFoundException, EtkException {
        PropertyAccessor accessor = propertyMap.get(internalNameOf(name));
        
        if (accessor == null) {
            if (getParent() == null)
                throw new PropertyNotFoundException(type, name);
            
            accessor = getParent().getPropertyAccessor(name);
        }
        
        return accessor;
    }
    
    public Map<String, PropertyAccessor> getPropertyAccessors()
            throws EtkException {
        return getPropertyAccessors(false, false);
    }
    
    public Map<String, PropertyAccessor> getPropertyAccessors(boolean recurse, boolean sort)
            throws EtkException {
        Map<String, PropertyAccessor> accessors = sort
            ? new TreeMap<String, PropertyAccessor>()
            : new HashMap<String, PropertyAccessor>();
        
        if (recurse && getParent() != null)
            getParent().findPropertyAccessors(accessors);
        
        copyPropertyAccessors(accessors);
        
        return accessors;
    }
    
    private void findPropertyAccessors(Map<String, PropertyAccessor> accessors)
            throws EtkException {
        if (getParent() != null)
            getParent().findPropertyAccessors(accessors);
        
        copyPropertyAccessors(accessors);
    }
    
    private void copyPropertyAccessors(Map<String, PropertyAccessor> accessors) {
        if (manager.isCaseSensitive()) {
            accessors.putAll(propertyMap);
        } else {
            for (PropertyAccessor accessor : propertyMap.values())
                accessors.put(accessor.getName(), accessor);
        }
    }
    
    // Methods
    
    public MethodAccessor getMethodAccessor(Signature signature)
            throws MethodNotFoundException, EtkException {
        MethodAccessor accessor = methodMap.get(internalSignatureOf(signature));
        
        if (accessor == null) {
            if (getParent() == null)
                throw new MethodNotFoundException(type, signature);
            
            accessor = getParent().getMethodAccessor(signature);
        }
        
        return accessor;
    }
    
    public Set<Signature> getMethodSignatures(String name)
            throws EtkException {
        return getMethodSignatures(name, true, false);
    }
    
    public Set<Signature> getMethodSignatures(String name, boolean recurse, boolean sort)
            throws EtkException {
        Set<Signature> signatures = sort
            ? new TreeSet<Signature>()
            : new HashSet<Signature>();
        
        findMethodSignatures(name, signatures, recurse);
        
        return signatures;
    }
    
    private void findMethodSignatures(String name, Set<Signature> signatures, boolean recurse)
            throws EtkException {
        if (recurse && getParent() != null)
            getParent().findMethodSignatures(name, signatures, true);
        
        String internalName = internalNameOf(name);
        for (Map.Entry<Signature, MethodAccessor> entry : methodMap.entrySet())
            if (entry.getKey().getLocalName().equals(internalName))
                signatures.add(entry.getValue().getSignature());
    }
    
    public Map<Signature, MethodAccessor> getMethodAccessors()
            throws EtkException {
        return getMethodAccessors(false, false);
    }
    
    public Map<Signature, MethodAccessor> getMethodAccessors(boolean recurse, boolean sort)
            throws EtkException {
        Map<Signature, MethodAccessor> accessors = sort
            ? new TreeMap<Signature, MethodAccessor>()
            : new HashMap<Signature, MethodAccessor>();
        
        if (recurse && getParent() != null)
            getParent().findMethodAccessors(accessors);
        
        copyMethodAccessors(accessors);
        
        return accessors;
    }
    
    private void findMethodAccessors(Map<Signature, MethodAccessor> accessors)
            throws EtkException {
        if (getParent() != null)
            getParent().findMethodAccessors(accessors);
        
        copyMethodAccessors(accessors);
    }
    
    private void copyMethodAccessors(Map<Signature, MethodAccessor> accessors) {
        if (manager.isCaseSensitive()) {
            accessors.putAll(methodMap);
        } else {
            for (MethodAccessor accessor : methodMap.values())
                accessors.put(accessor.getSignature(), accessor);
        }
    }
    
    private String internalNameOf(String name) {
        return manager.isCaseSensitive() ? name : name.toUpperCase();
    }
    
    private Signature internalSignatureOf(Signature signature) {
        return manager.isCaseSensitive() ? signature : signature.toCaseInsensitive();
    }
    
}
