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
import org.foxlabs.etk.support.Types;

public class UnaryOperatorNotFoundException extends SemanticException {
    private static final long serialVersionUID = -3651069527017918684L;
    
    private int nodeId;
    private Class<?> type;
    
    public UnaryOperatorNotFoundException(int nodeId, Class<?> type) {
        super(ResourceManager.getMessage("semantic.undefinedUnaryOperator",
                Node.getOperatorSymbol(nodeId),
                Types.toString(type)));
        
        this.nodeId = nodeId;
        this.type = type;
    }
    
    public int getNodeId() {
        return nodeId;
    }
    
    public Class<?> getType() {
        return type;
    }
    
}
