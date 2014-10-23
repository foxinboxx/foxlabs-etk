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

import java.lang.reflect.Modifier;

import java.util.ArrayList;

import org.foxlabs.etk.annotation.FxNames;
import org.foxlabs.etk.annotation.FxNamespace;
import org.foxlabs.etk.support.Signature;
import org.foxlabs.util.resource.ResourceHelper;

public class DefaultFunctionLoader extends FunctionLoader {
    
    public static final String BASE_FUNCTIONS_PKG =
        ResourceHelper.getPackageName(org.foxlabs.etk.function.base.Abs.class);
    
    public DefaultFunctionLoader(String... pkgs) {
        this(null, false, null, pkgs);
    }
    
    public DefaultFunctionLoader(FunctionLoader parent, String... pkgs) {
        this(parent, false, null, pkgs);
    }
    
    public DefaultFunctionLoader(FunctionLoader parent,
                                 boolean caseSensitive,
                                 ClassLoader cl,
                                 String... pkgs) {
        super(parent, caseSensitive);
        
        ArrayList<Function> functions = new ArrayList<Function>();
        findFunctions(functions, ResourceHelper.getClassLoader(cl), pkgs);
        preloadFunctions(functions.iterator());
    }
    
    private static void findFunctions(ArrayList<Function> functions,
            ClassLoader cl, String[] pkgs) {
        for (String pkg : pkgs) {
            for (Class<?> cls : ResourceHelper.findClasses(pkg, cl, true))
                if (Function.class.isAssignableFrom(cls)) {
                    int mod = cls.getModifiers();
                    if (Modifier.isPublic(mod) && !Modifier.isAbstract(mod)) {
                        try {
                            cls.getConstructor(functionConstructorArgs);
                            String namespace = findFunctionNamespace(cls);
                            String[] names = findFunctionNames(cls);
                            if (names == null) {
                                String name = cls.getSimpleName();
                                functions.add(newFunction(cls, name, namespace));
                            } else {
                                for (String name : names)
                                    functions.add(newFunction(cls, name, namespace));
                            }
                        } catch (NoSuchMethodException e) {
                        }
                    }
                }
        }
    }
    
    private static String findFunctionNamespace(Class<?> cls) {
        if (cls == Object.class)
            return Signature.NULL_NS;
        
        FxNamespace fxNamespace = cls.getAnnotation(FxNamespace.class);
        if (fxNamespace == null)
            return findFunctionNamespace(cls.getSuperclass());
        
        return fxNamespace.value().length() == 0
            ? Signature.NULL_NS
            : fxNamespace.value();
    }
    
    private static String[] findFunctionNames(Class<?> cls) {
        if (cls == Object.class)
            return null;
        
        FxNames fxNames = cls.getAnnotation(FxNames.class);
        if (fxNames == null || fxNames.value().length == 0)
            return findFunctionNames(cls.getSuperclass());
        
        return fxNames.value();
    }
    
}
