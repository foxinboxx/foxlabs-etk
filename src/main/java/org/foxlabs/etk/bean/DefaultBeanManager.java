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

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.MethodDescriptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.foxlabs.etk.Environment;
import org.foxlabs.etk.annotation.Determined;
import org.foxlabs.etk.support.Signature;
import org.foxlabs.etk.support.Types;

public class DefaultBeanManager extends BeanManager {
    
    public DefaultBeanManager() {
        this(null, true);
    }
    
    public DefaultBeanManager(BeanManager parent) {
        this(parent, true);
    }
    
    public DefaultBeanManager(BeanManager parent, boolean caseSensitive) {
        super(parent, caseSensitive);
    }
    
    protected BeanEntity findBeanEntity(Class<?> type)
            throws BeanNotFoundException {
        BeanInfo info;
        try {
            info = Introspector.getBeanInfo(type);
        } catch (IntrospectionException e) {
            throw new BeanNotFoundException(type);
        }
        
        return new BeanEntity(this,
                              type,
                              getPropertyAccessors(info),
                              getMethodAccessors(type, info));
    }
    
    private static PropertyAccessor[] getPropertyAccessors(BeanInfo info) {
        PropertyDescriptor[] properties = info.getPropertyDescriptors();
        if (properties.length == 0)
            return NO_PROPERTIES;
        
        PropertyAccessor[] accessors = new PropertyAccessor[properties.length];
        for (int i = 0, count = properties.length; i < count; i++)
            accessors[i] = new DefaultPropertyAccessor(properties[i]);
        
        return accessors;
    }
    
    private static MethodAccessor[] getMethodAccessors(Class<?> type, BeanInfo info) {
        MethodDescriptor[] methods = info.getMethodDescriptors();
        if (methods.length == 0)
            return NO_METHODS;
        
        MethodAccessor[] accessors = new MethodAccessor[methods.length];
        for (int i = 0, count = methods.length; i < count; i++)
            accessors[i] = new DefaultMethodAccessor(type, methods[i]);
        
        return accessors;
    }
    
}

class DefaultPropertyAccessor implements PropertyAccessor {
    
    private final Class<?> type;
    private final String name;
    private final Method getter;
    
    public DefaultPropertyAccessor(PropertyDescriptor descriptor) {
        this.type = Types.wrapperOf(descriptor.getPropertyType());
        this.name = descriptor.getName();
        this.getter = descriptor.getReadMethod();
    }
    
    public Class<?> getType() {
        return type;
    }
    
    public String getName() {
        return name;
    }
    
    public Object resolve(Environment context, Object target)
            throws Throwable {
        try {
            return getter.invoke(context, target);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }
    
    public boolean isDetermined() {
        return getter.isAnnotationPresent(Determined.class);
    }
    
}

class DefaultMethodAccessor implements MethodAccessor {
    
    private final Class<?> type;
    private final Signature signature;
    private final Method method;
    
    public DefaultMethodAccessor(Class<?> type, MethodDescriptor descriptor) {
        this.method = descriptor.getMethod();
        this.type = Types.wrapperOf(method.getReturnType());
        this.signature = new Signature(descriptor.getName(),
                                       Types.toString(type),
                                       method.getParameterTypes());
    }
    
    public Class<?> getType() {
        return type;
    }
    
    public Signature getSignature() {
        return signature;
    }
    
    public Object invoke(Environment context, Object target, Object... arguments)
            throws Throwable {
        try {
            return method.invoke(context, target, arguments);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }
    
    public boolean isDetermined() {
        return method.isAnnotationPresent(Determined.class);
    }
    
}
