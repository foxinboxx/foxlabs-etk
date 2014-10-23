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

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;

import org.foxlabs.etk.EtkException;
import org.foxlabs.etk.support.Signature;
import org.foxlabs.etk.support.Types;

public abstract class BeanManager {
    
    protected static final PropertyAccessor[] NO_PROPERTIES = new PropertyAccessor[0];
    protected static final MethodAccessor[] NO_METHODS = new MethodAccessor[0];
    
    private static BeanManager defaultRootManager;
    
    public static BeanManager getDefaultRootManager() {
        if (defaultRootManager == null)
            defaultRootManager = new DefaultBeanManager();
        return defaultRootManager;
    }
    
    private BeanManager parent;
    private final boolean caseSensitive;
    private Map<Class<?>, BeanEntity> cache = new HashMap<Class<?>, BeanEntity>();
    
    protected BeanManager() {
        this(null, true);
    }
    
    protected BeanManager(BeanManager parent) {
        this(parent, true);
    }
    
    protected BeanManager(BeanManager parent, boolean caseSensitive) {
        this.parent = parent;
        this.caseSensitive = caseSensitive;
    }
    
    public final boolean isCaseSensitive() {
        return caseSensitive;
    }
    
    public final BeanManager getParent() {
        return parent;
    }
    
    public final BeanEntity getBeanEntity(Class<?> type)
            throws BeanNotFoundException, EtkException {
        return getBeanEntity(type, true);
    }
    
    public final BeanEntity getBeanEntity(Class<?> type, boolean recurse)
            throws BeanNotFoundException, EtkException {
        BeanEntity entity;
        
        // First, check if the bean entity has already been loaded.
        synchronized (cache) {
            entity = cache.get(type);
        }
        if (entity != null)
            return entity;
        
        try {
            entity = findBeanEntity(type);
            
            synchronized (cache) {
                cache.put(type, entity);
            }
            
            return entity;
        } catch (BeanNotFoundException e) {
            // Still not found.
            if (recurse) {
                if (parent != null)
                    return parent.getBeanEntity(type, true);
            }
        }
        
        // Bean entity could not be found.
        throw new BeanNotFoundException(type);
    }
    
    protected BeanEntity findBeanEntity(Class<?> type)
            throws BeanNotFoundException, EtkException {
        throw new BeanNotFoundException(type);
    }
    
    public final PropertyAccessor getPropertyAccessor(Class<?> type, String name)
            throws BeanNotFoundException, PropertyNotFoundException, EtkException {
        return getBeanEntity(type).getPropertyAccessor(name);
    }
    
    public final MethodAccessor getMethodAccessor(Class<?> type, Signature signature)
            throws BeanNotFoundException, PropertyNotFoundException, EtkException {
        return getBeanEntity(type).getMethodAccessor(signature);
    }
    
    public final Set<Class<?>> getAvailableTypes() throws EtkException {
        return getAvailableTypes(true, false);
    }
    
    public final Set<Class<?>> getAvailableTypes(boolean recurse, boolean sort)
            throws EtkException {
        Set<Class<?>> types = sort
            ? new TreeSet<Class<?>>(Types.COMPARATOR)
            : new HashSet<Class<?>>();
        
        return findAvailableTypes(types, recurse) ? types : null;
    }
    
    private boolean findAvailableTypes(Set<Class<?>> types, boolean recurse)
            throws EtkException {
        if (recurse && parent != null) {
            if (!parent.findAvailableTypes(types, true))
                return false;
        }
        
        Iterator<Class<?>> i = findAvailableTypes();
        if (i == null)
            return false;
        
        while (i.hasNext())
            types.add(i.next());
        
        synchronized (cache) {
            types.addAll(cache.keySet());
        }
        
        return true;
    }
    
    protected Iterator<Class<?>> findAvailableTypes() throws EtkException {
        return null;
    }
    
    public final void cleanup() {
        cleanup(true);
    }
    
    public synchronized final void cleanup(boolean recurse) {
        cache.clear();
        
        if (recurse && parent != null)
            parent.cleanup(true);
    }
    
}
