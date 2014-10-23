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

import org.foxlabs.etk.EtkException;
import org.foxlabs.etk.resource.ResourceManager;

public class PropertyNotFoundException extends EtkException {
    private static final long serialVersionUID = -1863726307154657591L;
    
    private Class<?> type;
    private String name;
    
    public PropertyNotFoundException(Class<?> type, String name) {
        super(ResourceManager.getMessage("environment.propertyNotFound", name));
        this.type = type;
        this.name = name;
    }
    
    public Class<?> getType() {
        return type;
    }
    
    public String getName() {
        return name;
    }
    
}
