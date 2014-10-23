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

package org.foxlabs.etk.codec;

import java.io.Writer;
import java.io.IOException;

import java.util.StringTokenizer;

import org.foxlabs.etk.codec.parsing.TokenPrinter;
import org.foxlabs.etk.node.*;

import static org.foxlabs.etk.codec.DefaultConstants.*;

public class DefaultEncoder extends ExpressionEncoder {
    
    protected final TokenPrinter[] printers;
    
    protected DefaultEncoder() {
        this(tokenHandlers);
    }
    
    protected DefaultEncoder(TokenPrinter[] printers) {
        this.printers = printers;
    }
    
    public String getGrammar() {
        return DefaultConstants.GRAMMAR;
    }
    
    public void encode(Node node, Writer out) throws IOException {
        Printer printer = new Printer(out);
        printer.print(node);
        printers[TT_EOF].printToken(null, out);
    }
    
    // Printer
    
    protected class Printer {
        
        protected final Writer out;
        protected final NodeVisitor<IOException> visitor;
        
        public Printer(Writer out) {
            this.out = out;
            this.visitor = new Visitor(this);
        }
        
        public void print(Node node) throws IOException {
            node.accept(visitor);
        }
        
        // Print methods
        
        public void printNull(NullNode node) throws IOException {
            printers[TT_NULL].printToken(null, out);
        }
        
        public void printTrue(TrueNode node) throws IOException {
            printers[TT_TRUE].printToken(null, out);
        }
        
        public void printFalse(FalseNode node) throws IOException {
            printers[TT_FALSE].printToken(null, out);
        }
        
        public void printEnum(EnumNode node) throws IOException {
            Enum<?> value = node.getValue();
            printers[TT_SHARP].printToken(null, out);
            printType(value.getClass(), out);
            printers[TT_DOT].printToken(null, out);
            printers[TT_IDENTIFIER].printToken(value.name(), out);
        }
        
        public void printChar(CharNode node) throws IOException {
            printers[TT_CHAR].printToken(node.getValue(), out);
        }
        
        public void printNumber(NumberNode node) throws IOException {
            TokenPrinter number = printers[node.isFloat() ? TT_FLOAT : TT_INTEGER];
            number.printToken(node.getValue(), out);
        }
        
        public void printString(StringNode node) throws IOException {
            printers[TT_STRING].printToken(node.getValue(), out);
        }
        
        public void printVariable(VariableNode node) throws IOException {
            printers[TT_IDENTIFIER].printToken(node.getVariableName(), out);
        }
        
        public void printArray(ArrayNode node) throws IOException {
            printers[TT_LBRACE].printToken(null, out);
            TokenPrinter comma = printers[TT_COMMA];
            int count = node.getItemCount();
            if (count > 0) {
                node.getItem(0).accept(visitor);
                for (int i = 1; i < count; i++) {
                    comma.printToken(null, out);
                    if (prettyPrint)
                        out.write(' ');
                    node.getItem(i).accept(visitor);
                }
            }
            printers[TT_RBRACE].printToken(null, out);
        }
        
        public void printFunction(FunctionNode node) throws IOException {
            // Namespace
            if (namespaceAware && node.getFunctionNamespace().length() > 0) {
                printers[TT_IDENTIFIER].printToken(node.getFunctionNamespace(), out);
                printers[TT_COLON].printToken(null, out);
            }
            
            // Name
            printers[TT_IDENTIFIER].printToken(node.getFunctionLocalName(), out);
            
            // Arguments
            TokenPrinter comma = printers[TT_COMMA];
            printers[TT_LPAREN].printToken(null, out);
            int count = node.getArgumentCount();
            if (count > 0) {
                node.getArgument(0).accept(visitor);
                for (int i = 1; i < count; i++) {
                    comma.printToken(null, out);
                    if (prettyPrint)
                        out.write(' ');
                    node.getArgument(i).accept(visitor);
                }
            }
            printers[TT_RPAREN].printToken(null, out);
        }
        
        public void printCast(CastNode node) throws IOException {
            Node operand = node.getOperand();
            boolean parenthesize = operand instanceof OperatorNode;
            
            // Type
            printers[TT_LPAREN].printToken(null, out);
            printType(node.getType(), out);
            printers[TT_RPAREN].printToken(null, out);
            
            if (prettyPrint)
                out.write(' ');
            
            // Operand
            if (parenthesize)
                printers[TT_LPAREN].printToken(null, out);
            operand.accept(visitor);
            if (parenthesize)
                printers[TT_RPAREN].printToken(null, out);
        }
        
        public void printConvert(ConvertNode node) throws IOException {
            node.getOperand().accept(visitor);
        }
        
        public void printNegate(NegateNode node) throws IOException {
            printUnary(node, TT_MINUS);
        }
        
        public void printAdd(AddNode node) throws IOException {
            printBinary(node, TT_PLUS);
        }
        
        public void printSubtract(SubtractNode node) throws IOException {
            printBinary(node, TT_MINUS);
        }
        
        public void printMultiply(MultiplyNode node) throws IOException {
            printBinary(node, TT_MUL);
        }
        
        public void printDivide(DivideNode node) throws IOException {
            printBinary(node, TT_DIV);
        }
        
        public void printModulo(ModuloNode node) throws IOException {
            printBinary(node, TT_MOD);
        }
        
        public void printEqual(EqualNode node) throws IOException {
            printBinary(node, TT_EQ);
        }
        
        public void printNotEqual(NotEqualNode node) throws IOException {
            printBinary(node, TT_NE);
        }
        
        public void printGreaterThan(GreaterThanNode node) throws IOException {
            printBinary(node, TT_GT);
        }
        
        public void printGreaterThanEqual(GreaterThanEqualNode node) throws IOException {
            printBinary(node, TT_GE);
        }
        
        public void printLessThan(LessThanNode node) throws IOException {
            printBinary(node, TT_LT);
        }
        
        public void printLessThanEqual(LessThanEqualNode node) throws IOException {
            printBinary(node, TT_LE);
        }
        
        public void printNot(NotNode node) throws IOException {
            printUnary(node, TT_NOT);
        }
        
        public void printAnd(AndNode node) throws IOException {
            printBinary(node, TT_AND);
        }
        
        public void printOr(OrNode node) throws IOException {
            printBinary(node, TT_OR);
        }
        
        public void printCondition(ConditionNode node) throws IOException {
            printTernary(node, TT_QUESTIONMARK, TT_COLON);
        }
        
        public void printConcat(ConcatNode node) throws IOException {
            printNary(node, TT_CONCAT);
        }
        
        public void printIndex(IndexNode node) throws IOException {
            // Collection
            node.getCollection().accept(visitor);
            
            printers[TT_LBRACK].printToken(null, out);
            
            // Key
            node.getKey().accept(visitor);
            
            printers[TT_RBRACK].printToken(null, out);
        }
        
        public void printProperty(PropertyNode node) throws IOException {
            node.getObject().accept(visitor);
            printers[TT_DOT].printToken(null, out);
            printers[TT_IDENTIFIER].printToken(node.getPropertyName(), out);
        }
        
        public void printMethod(MethodNode node) throws IOException {
            node.getObject().accept(visitor);
            printers[TT_DOT].printToken(null, out);
            printers[TT_IDENTIFIER].printToken(node.getMethodName(), out);
            printers[TT_LPAREN].printToken(null, out);
            int count = node.getArgumentCount();
            if (count > 0) {
                TokenPrinter comma = printers[TT_COMMA];
                node.getArgument(0).accept(visitor);
                for (int i = 1; i < count; i++) {
                    comma.printToken(null, out);
                    if (prettyPrint)
                        out.write(' ');
                    node.getArgument(i).accept(visitor);
                }
            }
            printers[TT_RPAREN].printToken(null, out);
        }
        
        // Utility methods
        
        protected void printType(Class<?> type, Writer out) throws IOException {
            StringTokenizer tokenizer = new StringTokenizer(type.getName(), ".");
            while (tokenizer.hasMoreTokens()) {
                out.write(tokenizer.nextToken());
                if (tokenizer.hasMoreTokens())
                    printers[TT_DOT].printToken(null, out);
            }
        }
        
        protected void printUnary(UnaryNode node, int tokenId) throws IOException {
            Node operand = node.getOperand();
            boolean parenthesize = operand instanceof OperatorNode;
            printers[tokenId].printToken(null, out);
            if (parenthesize)
                printers[TT_LPAREN].printToken(null, out);
            node.getOperand().accept(visitor);
            if (parenthesize)
                printers[TT_RPAREN].printToken(null, out);
        }
        
        protected void printBinary(BinaryNode node, int tokenId) throws IOException {
            int precedence = Node.getOperatorPrecedence(node);
            Node operand = node.getOperand1();
            boolean parenthesize = Node.getOperatorPrecedence(operand) < precedence;
            
            if (parenthesize)
                printers[TT_LPAREN].printToken(null, out);
            operand.accept(visitor);
            if (parenthesize)
                printers[TT_RPAREN].printToken(null, out);
            
            if (prettyPrint)
                out.write(' ');
            printers[tokenId].printToken(null, out);
            if (prettyPrint)
                out.write(' ');
            
            operand = node.getOperand2();
            parenthesize = Node.getOperatorPrecedence(operand) < precedence;
            
            if (parenthesize)
                printers[TT_LPAREN].printToken(null, out);
            operand.accept(visitor);
            if (parenthesize)
                printers[TT_RPAREN].printToken(null, out);
        }
        
        protected void printTernary(TernaryNode node, int token1Id, int token2Id) throws IOException {
            node.getOperand1().accept(visitor);
            
            if (prettyPrint)
                out.write(' ');
            printers[token1Id].printToken(null, out);
            if (prettyPrint)
                out.write(' ');
            
            node.getOperand2().accept(visitor);
            
            if (prettyPrint)
                out.write(' ');
            printers[token2Id].printToken(null, out);
            if (prettyPrint)
                out.write(' ');
            
            node.getOperand3().accept(visitor);
        }
        
        protected void printNary(NaryNode node, int tokenId) throws IOException {
            int count = node.getOperandCount();
            if (count > 0) {
                TokenPrinter operator = printers[tokenId];
                int precedence = Node.getOperatorPrecedence(node);
                
                Node operand = node.getOperand(0);
                boolean parenthesize = Node.getOperatorPrecedence(operand) < precedence;
                
                if (parenthesize)
                    printers[TT_LPAREN].printToken(null, out);
                operand.accept(visitor);
                if (parenthesize)
                    printers[TT_RPAREN].printToken(null, out);
                
                for (int i = 1; i < count; i++) {
                    if (prettyPrint)
                        out.write(' ');
                    operator.printToken(null, out);
                    if (prettyPrint)
                        out.write(' ');
                    
                    operand = node.getOperand(i);
                    parenthesize = Node.getOperatorPrecedence(operand) < precedence;
                    
                    if (parenthesize)
                        printers[TT_LPAREN].printToken(null, out);
                    operand.accept(visitor);
                    if (parenthesize)
                        printers[TT_RPAREN].printToken(null, out);
                }
            }
        }
        
    }
    
    // Visitor
    
    private static class Visitor implements NodeVisitor<IOException> {
        
        private final Printer printer;
        
        private Visitor(Printer printer) {
            this.printer = printer;
        }
        
        public void visit(NullNode node) throws IOException {
            printer.printNull(node);
        }
        public void visit(TrueNode node) throws IOException {
            printer.printTrue(node);
        }
        public void visit(FalseNode node) throws IOException {
            printer.printFalse(node);
        }
        public void visit(EnumNode node) throws IOException {
            printer.printEnum(node);
        }
        public void visit(CharNode node) throws IOException {
            printer.printChar(node);
        }
        public void visit(NumberNode node) throws IOException {
            printer.printNumber(node);
        }
        public void visit(StringNode node) throws IOException {
            printer.printString(node);
        }
        public void visit(VariableNode node) throws IOException {
            printer.printVariable(node);
        }
        public void visit(ArrayNode node) throws IOException {
            printer.printArray(node);
        }
        public void visit(FunctionNode node) throws IOException {
            printer.printFunction(node);
        }
        public void visit(CastNode node) throws IOException {
            printer.printCast(node);
        }
        public void visit(ConvertNode node) throws IOException {
            printer.printConvert(node);
        }
        public void visit(NegateNode node) throws IOException {
            printer.printNegate(node);
        }
        public void visit(AddNode node) throws IOException {
            printer.printAdd(node);
        }
        public void visit(SubtractNode node) throws IOException {
            printer.printSubtract(node);
        }
        public void visit(MultiplyNode node) throws IOException {
            printer.printMultiply(node);
        }
        public void visit(DivideNode node) throws IOException {
            printer.printDivide(node);
        }
        public void visit(ModuloNode node) throws IOException {
            printer.printModulo(node);
        }
        public void visit(EqualNode node) throws IOException {
            printer.printEqual(node);
        }
        public void visit(NotEqualNode node) throws IOException {
            printer.printNotEqual(node);
        }
        public void visit(GreaterThanNode node) throws IOException {
            printer.printGreaterThan(node);
        }
        public void visit(GreaterThanEqualNode node) throws IOException {
            printer.printGreaterThanEqual(node);
        }
        public void visit(LessThanNode node) throws IOException {
            printer.printLessThan(node);
        }
        public void visit(LessThanEqualNode node) throws IOException {
            printer.printLessThanEqual(node);
        }
        public void visit(NotNode node) throws IOException {
            printer.printNot(node);
        }
        public void visit(AndNode node) throws IOException {
            printer.printAnd(node);
        }
        public void visit(OrNode node) throws IOException {
            printer.printOr(node);
        }
        public void visit(ConditionNode node) throws IOException {
            printer.printCondition(node);
        }
        public void visit(ConcatNode node) throws IOException {
            printer.printConcat(node);
        }
        public void visit(IndexNode node) throws IOException {
            printer.printIndex(node);
        }
        public void visit(PropertyNode node) throws IOException {
            printer.printProperty(node);
        }
        public void visit(MethodNode node) throws IOException {
            printer.printMethod(node);
        }
        
    }
    
}
