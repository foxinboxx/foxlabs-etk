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

public class BinaryOperatorNotFoundException extends SemanticException {
    private static final long serialVersionUID = 4183967218499023079L;
    
    private int nodeId;
    private Class<?> type1;
    private Class<?> type2;
    
    public BinaryOperatorNotFoundException(int nodeId, Class<?> type1, Class<?> type2) {
        super(ResourceManager.getMessage("semantic.undefinedBinaryOperator",
                Node.getOperatorSymbol(nodeId),
                Types.toString(type1),
                Types.toString(type2)));
        
        this.nodeId = nodeId;
        this.type1 = type1;
        this.type2 = type2;
    }
    
    public int getNodeId() {
        return nodeId;
    }
    
    public Class<?> getType1() {
        return type1;
    }
    
    public Class<?> getType2() {
        return type2;
    }
    
}
