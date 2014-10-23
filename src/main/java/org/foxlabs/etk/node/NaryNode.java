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

public abstract class NaryNode extends Node implements OperatorNode {
    
    final Node[] operands;
    
    private transient int hash = 0;
    
    NaryNode(Node... operands) {
        this.operands = operands;
    }
    
    public int getPrecedence() {
        return operatorPrecedences[getId()];
    }
    
    public String getSymbol() {
        return operatorSymbols[getId()];
    }
    
    public final int getOperandCount() {
        return operands.length;
    }
    
    public final Node getOperand(int index) {
        return operands[index];
    }
    
    public final Node[] getOperands() {
        return operands.length == 0 ? operands : operands.clone();
    }
    
    public boolean isDetermined() {
        for (Node operand : operands)
            if (!operand.isDetermined())
                return false;
        return true;
    }
    
    public int hashCode() {
        if (hash == 0) {
            hash = getId();
            for (Node operand : operands)
                hash = 31 * hash + operand.hashCode();
        }
        return hash;
    }
    
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        
        if (obj instanceof NaryNode) {
            NaryNode other = (NaryNode) obj;
            if (getId() == other.getId()) {
                if (getType() == other.getType()) {
                    int opcount = operands.length;
                    if (opcount == other.operands.length) {
                        for (int i = 0; i < opcount; i++)
                            if (!operands[i].equals(other.operands[i]))
                                return false;
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
}
