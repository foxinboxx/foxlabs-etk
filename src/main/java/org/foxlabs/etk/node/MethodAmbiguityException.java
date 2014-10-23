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

package org.foxlabs.etk.node;

import org.foxlabs.etk.resource.ResourceManager;
import org.foxlabs.etk.support.Signature;

public class MethodAmbiguityException extends SemanticException {
    private static final long serialVersionUID = 6207177171468684202L;
    
    private Class<?> type;
    private Signature signature;
    
    public MethodAmbiguityException(Class<?> type, Signature signature) {
        super(ResourceManager.getMessage("semantic.methodAmbiguity",
                signature.toString()));
        
        this.type = type;
        this.signature = signature;
    }
    
    public Class<?> getType() {
        return type;
    }
    
    public Signature getSignature() {
        return signature;
    }
    
}
