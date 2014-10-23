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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;

import org.foxlabs.etk.EtkException;
import org.foxlabs.etk.resource.ResourceManager;
import org.foxlabs.etk.support.Signature;

public abstract class FunctionLoader {
    
    static final Class<?>[] functionConstructorArgs = new Class<?>[]{
        String.class, // Name
        String.class  // Namespace
    };
    
    private static FunctionLoader defaultRootLoader;
    
    public static FunctionLoader getDefaultRootLoader() {
        if (defaultRootLoader == null)
            defaultRootLoader = new DefaultFunctionLoader(
                    DefaultFunctionLoader.BASE_FUNCTIONS_PKG);
        return defaultRootLoader;
    }
    
    private FunctionLoader parent;
    private final boolean caseSensitive;
    private Map<String, NamespaceCache> cache = new HashMap<String, NamespaceCache>();
    
    protected FunctionLoader() {
        this(null, false);
    }
    
    protected FunctionLoader(FunctionLoader parent) {
        this(parent, false);
    }
    
    protected FunctionLoader(FunctionLoader parent, boolean caseSensitive) {
        this.parent = parent;
        this.caseSensitive = caseSensitive;
    }
    
    public final boolean isCaseSensitive() {
        return caseSensitive;
    }
    
    public final FunctionLoader getParent() {
        return parent;
    }
    
    public final Function loadFunction(Signature signature)
            throws FunctionNotFoundException, EtkException {
        String namespace = signature.getNamespace();
        String internalNamespace = internalNamespaceOf(namespace);
        
        // First, check if the function has already been loaded.
        NamespaceCache scope;
        synchronized (cache) {
            scope = cache.get(internalNamespace);
        }
        if (scope != null) {
            Function function = scope.get(signature);
            if (function != null)
                return function;
        }
        
        try {
            Function function = findFunction(signature);
            function.loader = this;
            
            if (scope == null) {
                synchronized (cache) {
                    scope = cache.get(internalNamespace);
                    if (scope == null) {
                        scope = new NamespaceCache(namespace);
                        cache.put(internalNamespace, scope);
                    }
                }
            }
            
            return scope.put(function);
        } catch (FunctionNotFoundException e) {
            // Still not found.
            if (parent != null)
                return parent.loadFunction(signature);
        }
        
        // Function could not be found.
        throw new FunctionNotFoundException(signature);
    }
    
    protected synchronized final void preloadFunctions(Iterator<Function> i) {
        while (i.hasNext()) {
            Function function = i.next();
            function.loader = this;
            Signature signature = function.getSignature();
            String namespace = signature.getNamespace();
            String internalNamespace = internalNamespaceOf(namespace);
            
            NamespaceCache scope = cache.get(internalNamespace);
            if (scope == null) {
                scope = new NamespaceCache(namespace);
                cache.put(internalNamespace, scope);
            }
            
            scope.put(function);
        }
    }
    
    protected Function findFunction(Signature signature)
            throws FunctionNotFoundException, EtkException {
        throw new FunctionNotFoundException(signature);
    }
    
    public final Set<String> getNamespaces() throws EtkException {
        return getNamespaces(true, false);
    }
    
    public final Set<String> getNamespaces(boolean recurse, boolean sort)
            throws EtkException {
        Set<String> namespaces = sort
            ? new TreeSet<String>()
            : new HashSet<String>();
        
        findNamespaces(namespaces, recurse);
        
        return namespaces;
    }
    
    private void findNamespaces(Set<String> namespaces, boolean recurse)
            throws EtkException {
        if (recurse && parent != null)
            parent.findNamespaces(namespaces, true);
        
        Iterator<String> i = findNamespaces();
        if (i != null)
            while (i.hasNext())
                namespaces.add(i.next());
        
        synchronized (cache) {
            if (caseSensitive) {
                namespaces.addAll(cache.keySet());
            } else {
                for (NamespaceCache scope : cache.values())
                    namespaces.add(scope.namespace);
            }
        }
    }
    
    protected Iterator<String> findNamespaces() throws EtkException {
        return null;
    }
    
    public final Set<Signature> getSignatures(String namespace, String name)
            throws EtkException {
        return getSignatures(namespace, name, true, false);
    }
    
    public final Set<Signature> getSignatures(String namespace, String name,
            boolean recurse, boolean sort) throws EtkException {
        Set<Signature> signatures = sort
            ? new TreeSet<Signature>()
            : new HashSet<Signature>();
        
        findSignatures(signatures, namespace, name, recurse);
        
        return signatures;
    }
    
    private void findSignatures(Set<Signature> signatures, String namespace, String name,
            boolean recurse) throws EtkException {
        if (recurse && parent != null)
            parent.findSignatures(signatures, namespace, name, true);
        
        Iterator<Signature> i = findSignatures(namespace, name);
        if (i != null)
            while (i.hasNext())
                signatures.add(i.next());
        
        if (namespace == null) {
            synchronized (cache) {
                for (NamespaceCache scope : cache.values())
                    scope.getSignatures(signatures, name);
            }
        } else {
            NamespaceCache scope;
            synchronized (cache) {
                scope = cache.get(internalNamespaceOf(namespace));
            }
            if (scope != null)
                scope.getSignatures(signatures, name);
        }
    }
    
    protected Iterator<Signature> findSignatures(String namespace, String name)
            throws EtkException {
        return null;
    }
    
    private class NamespaceCache {
        
        String namespace;
        Map<String, CacheEntry> entries = new HashMap<String, CacheEntry>();
        
        NamespaceCache(String namespace) {
            this.namespace = namespace;
        }
        
        synchronized Function get(Signature signature) {
            String internalName = internalNameOf(signature.getLocalName());
            CacheEntry entry = entries.get(internalName);
            return entry == null ? null : entry.get(signature);
        }
        
        synchronized Function put(Function function) {
            String name = function.getSignature().getLocalName();
            String internalName = internalNameOf(name);
            
            CacheEntry entry = entries.get(internalName);
            if (entry == null) {
                entry = new CacheEntry();
                entries.put(internalName, entry);
            }
            
            return entry.put(function);
        }
        
        synchronized void getSignatures(Set<Signature> set, String name) {
            if (name == null) {
                for (CacheEntry entry : entries.values())
                    entry.getSignatures(set);
            } else {
                CacheEntry entry  = entries.get(internalNameOf(name));
                if (entry != null)
                    entry.getSignatures(set);
            }
        }
        
    }
    
    private static class CacheEntry {
        
        Function[] functions = new Function[2];
        int count = 0;
        
        Function get(Signature signature) {
            for (int i = 0; i < count; i++)
                if (signature.isSameArguments(functions[i].getSignature()))
                    return functions[i];
            
            return null;
        }
        
        Function put(Function function) {
            Signature signature = function.getSignature();
            
            for (int i = 0; i < count; i++)
                if (signature.isSameArguments(functions[i].getSignature()))
                    return functions[i];
            
            if (count == functions.length) {
                Function[] temp = new Function[functions.length + 4];
                System.arraycopy(functions, 0, temp, 0, functions.length);
                functions = temp;
            }
            
            return functions[count++] = function;
        }
        
        void getSignatures(Set<Signature> set) {
            for (int i = 0; i < count; i++)
                set.add(functions[i].getSignature());
        }
        
    }
    
    private String internalNameOf(String name) {
        return caseSensitive ? name : name.toUpperCase();
    }
    
    private String internalNamespaceOf(String namespace) {
        return caseSensitive || namespace == Signature.NULL_NS
            ? namespace
            : namespace.toUpperCase();
    }
    
    // Utility
    
    protected static Function newFunction(String className,
            String name, String namespace) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        return newFunction(className, namespace, name, cl);
    }
    
    protected static Function newFunction(String className,
            String name, String namespace, ClassLoader cl) {
        try {
            return newFunction(cl.loadClass(className), namespace, name);
        } catch (ClassNotFoundException e) {
            throw ResourceManager.newEtkException("function.classNotFound",
                    className);
        }
    }
    
    protected static Function newFunction(Class<?> cls,
            String name, String namespace) {
        Class<? extends Function> fcls;
        try {
            fcls = cls.asSubclass(Function.class);
        } catch (ClassCastException e) {
            throw ResourceManager.newEtkException("function.notAFunctionClass",
                    cls.getName());
        }
        
        Constructor<? extends Function> constructor;
        try {
            constructor = fcls.getConstructor(functionConstructorArgs);
        } catch (NoSuchMethodException e) {
            throw ResourceManager.newEtkException("function.invalidFunctionClass",
                    cls.getSimpleName());
        }
        
        try {
            return constructor.newInstance(name, namespace);
        } catch (Exception e) {
            String message = e instanceof InvocationTargetException
                ? e.getCause().getMessage()
                : e.getMessage();
            
            throw ResourceManager.newEtkException("function.cannotInstantiate",
                    cls.getName(), message);
        }
    }
    
}
