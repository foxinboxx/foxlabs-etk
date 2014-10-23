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

import java.io.Reader;
import java.io.IOException;

import org.foxlabs.etk.Environment;
import org.foxlabs.etk.EtkException;
import org.foxlabs.etk.codec.parsing.ParsingException;
import org.foxlabs.etk.codec.parsing.Token;
import org.foxlabs.etk.codec.parsing.TokenFilter;
import org.foxlabs.etk.codec.parsing.TokenScanner;
import org.foxlabs.etk.codec.parsing.Tokenizer;
import org.foxlabs.etk.node.*;
import org.foxlabs.etk.resource.ResourceManager;

import static org.foxlabs.etk.codec.DefaultConstants.*;

public class DefaultDecoder extends ExpressionDecoder {
    
    protected final TokenScanner[] scanners;
    
    protected DefaultDecoder() {
        this(tokenHandlers);
    }
    
    protected DefaultDecoder(TokenScanner[] scanners) {
        this.scanners = scanners;
    }
    
    public String getGrammar() {
        return DefaultConstants.GRAMMAR;
    }
    
    public Node decode(Reader in, Environment context) throws IOException {
        Scanner scanner = new Scanner(in, context);
        return scanner.scan();
    }
    
    // Scanner
    
    protected class Scanner {
        
        protected final Tokenizer tokenizer;
        protected final Environment context;
        protected final NodeBuilder builder;
        
        protected Scanner(Reader reader, Environment context) {
            TokenFilter filter = new TokenFilter.Exclude(TT_LINECOMMENT, TT_BLOCKCOMMENT);
            this.tokenizer = new Tokenizer(reader, scanners, filter);
            this.context = context;
            this.builder = new NodeBuilder(context);
        }
        
        public Node scan() throws IOException {
            if (tokenizer.next(TT_EOF) != null)
                return null;
            
            if (scanExpr()) {
                if (tokenizer.next(TT_EOF) != null)
                    return builder.popNode();
                
                throw newParsingException("codec.grammar.invalidExpression");
            }
            
            throw newParsingException("codec.grammar.expressionExpected");
        }
        
        public boolean scanExpr() throws IOException {
            try {
                return scanConcat();
            } catch (EtkException e) {
                throw new ParsingException(e, tokenizer.getLine(), tokenizer.getColumn());
            }
        }
        
        public boolean scanConcat() throws IOException {
            if (scanCondition()) {
                Token firstToken = tokenizer.next(TT_CONCAT);
                if (firstToken != null) {
                    builder.mark();
                    int count = 0;
                    for (; tokenizer.next(TT_CONCAT) != null; count++) {
                        if (!scanCondition())
                            throw newParsingException("codec.grammar.expressionExpected");
                    }
                    
                    if (count > 0) {
                        builder.concat();
                        setPosition(firstToken);
                    } else {
                        builder.unmark();
                    }
                }
                return true;
            }
            return false;
        }
        
        public boolean scanCondition() throws IOException {
            if (scanOr()) {
                tokenizer.mark();
                try {
                    Token firstToken = tokenizer.next(TT_QUESTIONMARK);
                    if (firstToken == null)
                        return true;
                    
                    if (!scanExpr())
                        throw newParsingException("codec.grammar.expressionExpected");
                    
                    if (tokenizer.next(TT_COLON) == null)
                        throw newParsingException("codec.grammar.tokenExpected",
                                scanners[TT_COLON].toString());
                    
                    if (!scanExpr())
                        throw newParsingException("codec.grammar.expressionExpected");
                    
                    builder.condition();
                    setPosition(firstToken);
                    tokenizer.consume();
                    return true;
                } finally {
                    tokenizer.reset();
                }
            }
            return false;
        }
        
        public boolean scanOr() throws IOException {
            if (scanAnd()) {
                Token firstToken;
                while ((firstToken = tokenizer.next(TT_OR)) != null) {
                    if (!scanAnd())
                        throw newParsingException("codec.grammar.expressionExpected");
                    
                    builder.or();
                    setPosition(firstToken);
                }
                return true;
            }
            return false;
        }
        
        public boolean scanAnd() throws IOException {
            if (scanEquality()) {
                Token firstToken;
                while ((firstToken = tokenizer.next(TT_AND)) != null) {
                    if (!scanEquality())
                        throw newParsingException("codec.grammar.expressionExpected");
                    
                    builder.and();
                    setPosition(firstToken);
                }
                return true;
            }
            return false;
        }
        
        public boolean scanEquality() throws IOException {
            if (scanRelational()) {
                Token firstToken;
                while ((firstToken = tokenizer.next(TT_EQ, TT_NE)) != null) {
                    int opId = tokenizer.last().getId();
                    
                    if (!scanRelational())
                        throw newParsingException("codec.grammar.expressionExpected");
                    
                    switch (opId) {
                        case TT_EQ:
                            builder.eq();
                            break;
                            
                        case TT_NE:
                            builder.ne();
                            break;
                            
                        default:
                            throw new InternalError();
                    }
                    
                    setPosition(firstToken);
                }
                return true;
            }
            return false;
        }
        
        public boolean scanRelational() throws IOException {
            if (scanAdditive()) {
                Token firstToken;
                while ((firstToken = tokenizer.next(TT_LT, TT_GT, TT_LE, TT_GE)) != null) {
                    int opId = tokenizer.last().getId();
                    
                    if (!scanAdditive())
                        throw newParsingException("codec.grammar.expressionExpected");
                    
                    switch (opId) {
                        case TT_LT:
                            builder.lt();
                            break;
                            
                        case TT_GT:
                            builder.gt();
                            break;
                            
                        case TT_LE:
                            builder.le();
                            break;
                            
                        case TT_GE:
                            builder.ge();
                            break;
                            
                        default:
                            throw new InternalError();
                    }
                    
                    setPosition(firstToken);
                }
                return true;
            }
            return false;
        }
        
        public boolean scanAdditive() throws IOException {
            if (scanMultiplicative()) {
                Token firstToken;
                while ((firstToken = tokenizer.next(TT_PLUS, TT_MINUS)) != null) {
                    int opId = tokenizer.last().getId();
                    
                    if (!scanMultiplicative())
                        throw newParsingException("codec.grammar.expressionExpected");
                    
                    switch (opId) {
                        case TT_PLUS:
                            builder.add();
                            break;
                            
                        case TT_MINUS:
                            builder.subtract();
                            break;
                            
                        default:
                            throw new InternalError();
                    }
                    
                    setPosition(firstToken);
                }
                return true;
            }
            return false;
        }
        
        public boolean scanMultiplicative() throws IOException {
            if (scanAccessible()) {
                Token firstToken;
                while ((firstToken = tokenizer.next(TT_MUL, TT_DIV, TT_MOD)) != null) {
                    int opId = tokenizer.last().getId();
                    
                    if (!scanAccessible())
                        throw newParsingException("codec.grammar.expressionExpected");
                    
                    switch (opId) {
                        case TT_MUL:
                            builder.multiply();
                            break;
                            
                        case TT_DIV:
                            builder.divide();
                            break;
                            
                        case TT_MOD:
                            builder.modulo();
                            break;
                            
                        default:
                            throw new InternalError();
                    }
                    
                    setPosition(firstToken);
                }
                return true;
            }
            return false;
        }
        
        public boolean scanAccessible() throws IOException {
            if (scanUnary()) {
                Token firstToken;
                while ((firstToken = tokenizer.next(TT_DOT, TT_LBRACK)) != null) {
                    switch (tokenizer.last().getId()) {
                        case TT_DOT:
                            if (!scanMethod() && !scanProperty())
                                throw newParsingException("codec.grammar.invalidExpression");
                            break;
                            
                        case TT_LBRACK:
                            if (!scanIndex())
                                throw newParsingException("codec.grammar.invalidExpression");
                            break;
                            
                        default:
                            throw new InternalError();
                    }
                    
                    setPosition(firstToken);
                }
                return true;
            }
            return false;
        }
        
        public boolean scanMethod() throws IOException {
            tokenizer.mark();
            try {
                Token token = tokenizer.next(TT_IDENTIFIER);
                if (token == null)
                    return false;
                
                if (tokenizer.next(TT_LPAREN) == null)
                    return false;
                
                builder.mark();
                scanArguments();
                
                if (tokenizer.next(TT_RPAREN) == null)
                    throw newParsingException("codec.grammar.tokenExpected",
                            scanners[TT_RPAREN].toString());
                
                builder.method(token.getString());
                tokenizer.consume();
                return true;
            } finally {
                tokenizer.reset();
            }
        }
        
        public boolean scanProperty() throws IOException {
            tokenizer.mark();
            try {
                Token token = tokenizer.next(TT_IDENTIFIER);
                if (token == null)
                    return false;
                
                builder.property(token.getString());
                tokenizer.consume();
                return true;
            } finally {
                tokenizer.reset();
            }
        }
        
        public boolean scanIndex() throws IOException {
            tokenizer.mark();
            try {
                if (!scanExpr())
                    throw newParsingException("codec.grammar.expressionExpected");
                
                if (tokenizer.next(TT_RBRACK) == null)
                    throw newParsingException("codec.grammar.tokenExpected",
                            scanners[TT_RBRACK].toString());
                
                builder.index();
                tokenizer.consume();
                return true;
            } finally {
                tokenizer.reset();
            }
        }
        
        public boolean scanUnary() throws IOException {
            return scanCast()
                || scanPlus()
                || scanMinus()
                || scanNot()
                || scanArray()
                || scanLiteral()
                || scanFunction()
                || scanVariable()
                || scanSubexpr();
        }
        
        public boolean scanCast() throws IOException {
            tokenizer.mark();
            try {
                Token firstToken = tokenizer.next(TT_LPAREN);
                if (firstToken == null)
                    return false;
                
                Token token = tokenizer.next(TT_IDENTIFIER);
                if (token == null)
                    return false;
                
                StringBuilder typeBuffer = new StringBuilder();
                typeBuffer.append(token.getString());
                
                while (tokenizer.next(TT_DOT) != null) {
                    token = tokenizer.next(TT_IDENTIFIER);
                    if (token == null)
                        return false;
                    typeBuffer.append('.')
                              .append(token.getString());
                }
                
                if (tokenizer.next(TT_RPAREN) == null)
                    return false;
                
                if (!scanUnary())
                    return false;
                
                Class<?> type = context.resolveType(typeBuffer.toString());
                
                builder.cast(type);
                setPosition(firstToken);
                tokenizer.consume();
                return true;
            } finally {
                tokenizer.reset();
            }
        }
        
        public boolean scanPlus() throws IOException {
            tokenizer.mark();
            try {
                Token firstToken = tokenizer.next(TT_PLUS);
                if (firstToken == null)
                    return false;
                
                if (tokenizer.next(TT_PLUS, TT_MINUS, TT_NOT) != null)
                    throw newParsingException("codec.grammar.invalidToken",
                            scanners[tokenizer.last().getId()].toString());
                
                if (!scanUnary())
                    throw newParsingException("codec.grammar.expressionExpected");
                
                if (!Number.class.isAssignableFrom(builder.getNode().getType()))
                    throw newParsingException("codec.grammar.invalidToken",
                            scanners[TT_PLUS].toString());
                
                tokenizer.consume();
                setPosition(firstToken);
                return true;
            } finally {
                tokenizer.reset();
            }
        }
        
        public boolean scanMinus() throws IOException {
            tokenizer.mark();
            try {
                Token firstToken = tokenizer.next(TT_MINUS);
                if (firstToken == null)
                    return false;
                
                if (tokenizer.next(TT_MINUS, TT_PLUS, TT_NOT) != null)
                    throw newParsingException("codec.grammar.invalidToken",
                            scanners[tokenizer.last().getId()].toString());
                
                if (!scanUnary())
                    throw newParsingException("codec.grammar.expressionExpected");
                
                builder.negate();
                if (((NegateNode) builder.getNode()).getOperand() instanceof NumberNode)
                    builder.pushResult();
                setPosition(firstToken);
                tokenizer.consume();
                return true;
            } finally {
                tokenizer.reset();
            }
        }
        
        public boolean scanNot() throws IOException {
            tokenizer.mark();
            try {
                Token firstToken = tokenizer.next(TT_NOT);
                if (firstToken == null)
                    return false;
                
                if (tokenizer.next(TT_NOT, TT_MINUS, TT_PLUS) != null)
                    throw newParsingException("codec.grammar.invalidToken",
                            scanners[tokenizer.last().getId()].toString());
                
                if (!scanUnary())
                    throw newParsingException("codec.grammar.expressionExpected");
                
                builder.not();
                setPosition(firstToken);
                tokenizer.consume();
                return true;
            } finally {
                tokenizer.reset();
            }
        }
        
        public boolean scanArray() throws IOException {
            tokenizer.mark();
            try {
                Token firstToken = tokenizer.next(TT_LBRACE);
                if (firstToken == null)
                    return false;
                
                builder.mark();
                scanArguments();
                
                if (tokenizer.next(TT_RBRACE) == null)
                    throw newParsingException("codec.grammar.tokenExpected",
                            scanners[TT_RBRACE].toString());
                
                builder.array();
                setPosition(firstToken);
                tokenizer.consume();
                return true;
            } finally {
                tokenizer.reset();
            }
        }
        
        public boolean scanFunction() throws IOException {
            tokenizer.mark();
            try {
                Token token = tokenizer.next(TT_IDENTIFIER);
                if (token == null)
                    return false;
                Token firstToken = token;
                
                String namespace = null;
                String name = token.getString();
                
                if (tokenizer.next(TT_COLON) != null) {
                    if ((token = tokenizer.next(TT_IDENTIFIER)) == null)
                        return false;
                    if (namespaceAware)
                        namespace = name;
                    name = token.getString();
                }
                
                if (tokenizer.next(TT_LPAREN) == null)
                    return false;
                
                builder.mark();
                scanArguments();
                
                if (tokenizer.next(TT_RPAREN) == null)
                    throw newParsingException("codec.grammar.tokenExpected",
                            scanners[TT_RPAREN].toString());
                
                builder.function(name, namespace);
                setPosition(firstToken);
                tokenizer.consume();
                return true;
            } finally {
                tokenizer.reset();
            }
        }
        
        public boolean scanVariable() throws IOException {
            tokenizer.mark();
            try {
                Token firstToken = tokenizer.next(TT_IDENTIFIER);
                if (firstToken == null)
                    return false;
                
                builder.pushVariable(firstToken.getString());
                setPosition(firstToken);
                tokenizer.consume();
                return true;
            } finally {
                tokenizer.reset();
            }
        }
        
        public boolean scanSubexpr() throws IOException {
            tokenizer.mark();
            try {
                if (tokenizer.next(TT_LPAREN) == null)
                    return false;
                
                if (!scanExpr())
                    throw newParsingException("codec.grammar.expressionExpected");
                
                if (tokenizer.next(TT_RPAREN) == null)
                    throw newParsingException("codec.grammar.tokenExpected",
                            scanners[TT_RPAREN].toString());
                
                tokenizer.consume();
                return true;
            } finally {
                tokenizer.reset();
            }
        }
        
        public int scanArguments() throws IOException {
            int count = 0;
            if (scanExpr()) {
                for (count = 1; tokenizer.next(TT_COMMA) != null; count++) {
                    if (!scanExpr())
                        throw newParsingException("codec.grammar.expressionExpected");
                }
            }
            return count;
        }
        
        public boolean scanLiteral() throws IOException {
            return scanString()
                || scanFloat()
                || scanInt()
                || scanChar()
                || scanEnum()
                || scanTrue()
                || scanFalse()
                || scanNull();
        }
        
        public boolean scanNull() throws IOException {
            Token firstToken = tokenizer.next(TT_NULL);
            if (firstToken == null)
                return false;
            
            builder.pushNull();
            setPosition(firstToken);
            tokenizer.consume();
            return true;
        }
        
        public boolean scanTrue() throws IOException {
            Token firstToken = tokenizer.next(TT_TRUE);
            if (firstToken == null)
                return false;
            
            builder.pushLiteral(true);
            setPosition(firstToken);
            tokenizer.consume();
            return true;
        }
        
        public boolean scanFalse() throws IOException {
            Token firstToken = tokenizer.next(TT_FALSE);
            if (firstToken == null)
                return false;
            
            builder.pushLiteral(false);
            setPosition(firstToken);
            tokenizer.consume();
            return true;
        }
        
        public boolean scanEnum() throws IOException {
            tokenizer.mark();
            try {
                Token firstToken = tokenizer.next(TT_SHARP);
                if (firstToken == null)
                    return false;
                
                Token token = tokenizer.next(TT_IDENTIFIER);
                if (token != null) {
                    StringBuilder typeBuffer = new StringBuilder();
                    typeBuffer.append(token.getString());
                    
                    if (tokenizer.next(TT_DOT) != null) {
                        token = tokenizer.next(TT_IDENTIFIER);
                        if (token != null) {
                            boolean valid = true;
                            while (valid && tokenizer.next(TT_DOT) != null) {
                                typeBuffer.append('.')
                                          .append(token.getString());
                                token = tokenizer.next(TT_IDENTIFIER);
                                valid = token != null;
                            }
                            if (valid) {
                                Class<?> type = context.resolveType(typeBuffer.toString());
                                Enum<?> value = context.resolveEnum(type, token.getString());
                                
                                builder.pushLiteral(value);
                                setPosition(firstToken);
                                tokenizer.consume();
                                return true;
                            }
                        }
                    }
                }
                
                throw newParsingException("codec.grammar.tokenExpected");
            } finally {
                tokenizer.reset();
            }
        }
        
        public boolean scanChar() throws IOException {
            Token firstToken = tokenizer.next(TT_CHAR);
            if (firstToken == null)
                return false;
            
            builder.pushLiteral(firstToken.getChar());
            setPosition(firstToken);
            tokenizer.consume();
            return true;
        }
        
        public boolean scanInt() throws IOException {
            Token firstToken = tokenizer.next(TT_INTEGER);
            if (firstToken == null)
                return false;
            
            builder.pushLiteral(firstToken.getNumber());
            setPosition(firstToken);
            tokenizer.consume();
            return true;
        }
        
        public boolean scanFloat() throws IOException {
            Token firstToken = tokenizer.next(TT_FLOAT);
            if (firstToken == null)
                return false;
            
            builder.pushLiteral(firstToken.getNumber());
            setPosition(firstToken);
            tokenizer.consume();
            return true;
        }
        
        public boolean scanString() throws IOException {
            Token firstToken = tokenizer.next(TT_STRING);
            if (firstToken == null)
                return false;
            
            builder.pushLiteral(firstToken.getString());
            setPosition(firstToken);
            tokenizer.consume();
            return true;
        }
        
        private void setPosition(Token token) {
            builder.getNode().setSourcePosition(token.getLine(), token.getColumn());
        }
        
        // Exception
        
        private ParsingException newParsingException(String message) {
            return new ParsingException(ResourceManager.getMessage(message),
                    tokenizer.getLine(), tokenizer.getColumn());
        }
        
        private ParsingException newParsingException(String message, Object... arguments) {
            return new ParsingException(ResourceManager.getMessage(message, arguments),
                    tokenizer.getLine(), tokenizer.getColumn());
        }
        
    }
    
}
