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
import org.foxlabs.etk.support.Signature;
import org.foxlabs.etk.variable.VariableTable;

public interface Environment {
    
    ClassLoader getClassLoader();
    
    Class<?> resolveType(String name) throws EtkException;
    
    Enum<?> resolveEnum(Class<?> type, String name) throws EtkException;
    
    VariableTable getVariableTable();
    
    Object resolveVariable(String name) throws EtkException;
    
    FunctionLoader getFunctionLoader();
    
    Function resolveFunction(Signature signature) throws EtkException;
    
    BeanManager getBeanManager();
    
    BeanEntity resolveEntity(Class<?> type) throws EtkException;
    
}
