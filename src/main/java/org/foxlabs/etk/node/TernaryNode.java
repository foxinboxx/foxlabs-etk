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

public abstract class TernaryNode extends Node implements OperatorNode {
    
    final Node operand1;
    final Node operand2;
    final Node operand3;
    
    TernaryNode(Node operand1, Node operand2, Node operand3) {
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.operand3 = operand3;
    }
    
    public int getPrecedence() {
        return operatorPrecedences[getId()];
    }
    
    public String getSymbol() {
        return operatorSymbols[getId()];
    }
    
    public final Node getOperand1() {
        return operand1;
    }
    
    public final Node getOperand2() {
        return operand2;
    }
    
    public final Node getOperand3() {
        return operand3;
    }
    
    public boolean isDetermined() {
        return operand1.isDetermined() &&
               operand2.isDetermined() &&
               operand3.isDetermined();
    }
    
    public int hashCode() {
        int hash = getId();
        hash = 31 * hash + operand1.hashCode();
        hash = 31 * hash + operand2.hashCode();
        hash = 31 * hash + operand3.hashCode();
        return hash;
    }
    
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        
        if (obj instanceof TernaryNode) {
            TernaryNode other = (TernaryNode) obj;
            return getId() == other.getId() &&
                   getType() == other.getType() &&
                   operand1.equals(other.operand1) &&
                   operand2.equals(other.operand2) &&
                   operand3.equals(other.operand3);
        }
        
        return false;
    }
    
}
