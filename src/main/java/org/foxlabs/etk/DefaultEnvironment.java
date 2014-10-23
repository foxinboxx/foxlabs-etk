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

package org.foxlabs.etk;

import org.foxlabs.etk.bean.BeanEntity;
import org.foxlabs.etk.bean.BeanManager;
import org.foxlabs.etk.function.Function;
import org.foxlabs.etk.function.FunctionLoader;
import org.foxlabs.etk.resource.ResourceManager;
import org.foxlabs.etk.support.Signature;
import org.foxlabs.etk.support.Types;
import org.foxlabs.etk.variable.DefaultVariableTable;
import org.foxlabs.etk.variable.VariableTable;
import org.foxlabs.util.resource.ResourceHelper;

public class DefaultEnvironment implements Environment {
    
    protected ClassLoader classLoader;
    protected VariableTable variableTable;
    protected FunctionLoader functionLoader; 
    protected BeanManager beanManager;
    
    protected DefaultEnvironment() {
        this(null);
    }
    
    protected DefaultEnvironment(ClassLoader loader) {
        this.classLoader = ResourceHelper.getClassLoader(loader);
    }
    
    public final ClassLoader getClassLoader() {
        return classLoader;
    }
    
    public final void setClassLoader(ClassLoader loader) {
        if (loader == null)
            throw new NullPointerException();
        classLoader = loader;
    }
    
    public Class<?> resolveType(String name) throws EtkException {
        try {
            return Types.typeOf(name, classLoader);
        } catch (ClassNotFoundException e) {
            throw ResourceManager.newEtkException("environment.typeNotFound",
                    name);
        }
    }
    
    public Enum<?> resolveEnum(Class<?> type, String name) throws EtkException {
        try {
            return Types.enumvalueOf(type, name);
        } catch (ClassCastException e) {
            throw ResourceManager.newEtkException("environment.notAnEnumType",
                    type.getName());
        } catch (IllegalArgumentException e) {
            throw ResourceManager.newEtkException("environment.invalidEnumLiteral",
                    type.getName(), name);
        }
    }
    
    public VariableTable getVariableTable() {
        return variableTable;
    }
    
    public void setVariableTable(VariableTable table) {
        variableTable = table;
    }
    
    public Object resolveVariable(String name) throws EtkException {
        if (variableTable == null)
            throw new UnsupportedOperationException();
        return variableTable.resolveVariable(name, this);
    }
    
    public FunctionLoader getFunctionLoader() {
        return functionLoader;
    }
    
    public void setFunctionLoader(FunctionLoader loader) {
        functionLoader = loader;
    }
    
    public Function resolveFunction(Signature signature) throws EtkException {
        if (functionLoader == null)
            throw new UnsupportedOperationException();
        return functionLoader.loadFunction(signature);
    }
    
    public BeanManager getBeanManager() {
        return beanManager;
    }
    
    public void setBeanManager(BeanManager manager) {
        beanManager = manager;
    }
    
    public BeanEntity resolveEntity(Class<?> type) throws EtkException {
        if (beanManager == null)
            throw new UnsupportedOperationException();
        return beanManager.getBeanEntity(type);
    }
    
    // Global context
    
    private static Environment globalContext;
    
    public static final Environment getGlobalContext() {
        if (globalContext == null) {
            ClassLoader cl = ResourceHelper.getClassLoader();
            
            globalContext = createEnvironmentEntity(Environment.class, cl);
            if (globalContext == null) {
                DefaultEnvironment context = new DefaultEnvironment(cl);
                
                context.variableTable = createEnvironmentEntity(VariableTable.class, cl);
                if (context.variableTable == null) {
                    context.variableTable = new DefaultVariableTable();
                }
                
                context.functionLoader = createEnvironmentEntity(FunctionLoader.class, cl);
                if (context.functionLoader == null) {
                    context.functionLoader = FunctionLoader.getDefaultRootLoader();
                }
                
                context.beanManager = createEnvironmentEntity(BeanManager.class, cl);
                if (context.beanManager == null) {
                    context.beanManager = BeanManager.getDefaultRootManager();
                }
                
                globalContext = context;
            }
        }
        return globalContext;
    }
    
    public static final void setGlobalContext(Environment context) {
        if (context == null)
            throw new NullPointerException();
        globalContext = context;
    }
    
    private static <T> T createEnvironmentEntity(Class<T> entity, ClassLoader cl) {
        String className = System.getProperty(entity.getName());
        if (className == null)
            return null;
        
        Class<?> theClass;
        try {
            theClass = cl.loadClass(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
        
        Class<? extends T> entityClass;
        try {
            entityClass = theClass.asSubclass(entity);
        } catch (ClassCastException e) {
            return null;
        }
        
        try {
            return entityClass.newInstance();
        } catch (Exception e) {
            return null;
        }
    }
    
}
