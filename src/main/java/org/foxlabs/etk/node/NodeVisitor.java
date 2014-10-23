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

public interface NodeVisitor<T extends Throwable> {
    
    void visit(NullNode node) throws T;
    
    void visit(TrueNode node) throws T;
    
    void visit(FalseNode node) throws T;
    
    void visit(EnumNode node) throws T;
    
    void visit(CharNode node) throws T;
    
    void visit(NumberNode node) throws T;
    
    void visit(StringNode node) throws T;
    
    void visit(VariableNode node) throws T;
    
    void visit(ArrayNode node) throws T;
    
    void visit(FunctionNode node) throws T;
    
    void visit(CastNode node) throws T;
    
    void visit(ConvertNode node) throws T;
    
    void visit(NegateNode node) throws T;
    
    void visit(AddNode node) throws T;
    
    void visit(SubtractNode node) throws T;
    
    void visit(MultiplyNode node) throws T;
    
    void visit(DivideNode node) throws T;
    
    void visit(ModuloNode node) throws T;
    
    void visit(EqualNode node) throws T;
    
    void visit(NotEqualNode node) throws T;
    
    void visit(GreaterThanNode node) throws T;
    
    void visit(GreaterThanEqualNode node) throws T;
    
    void visit(LessThanNode node) throws T;
    
    void visit(LessThanEqualNode node) throws T;
    
    void visit(NotNode node) throws T;
    
    void visit(AndNode node) throws T;
    
    void visit(OrNode node) throws T;
    
    void visit(ConditionNode node) throws T;
    
    void visit(ConcatNode node) throws T;
    
    void visit(IndexNode node) throws T;
    
    void visit(PropertyNode node) throws T;
    
    void visit(MethodNode node) throws T;
    
}
