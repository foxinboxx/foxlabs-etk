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

import org.foxlabs.etk.codec.parsing.TokenHandler;
import org.foxlabs.etk.codec.parsing.handler.*;

public abstract class DefaultConstants {
    
    private DefaultConstants() {}
    
    public static final String GRAMMAR = "default";
    
    // Token IDs
    
    public static final int TT_EOF          = 0;
    
    public static final int TT_LINECOMMENT  = 1;
    public static final int TT_BLOCKCOMMENT = 2;
    
    public static final int TT_STRING       = 3;
    public static final int TT_FLOAT        = 4;
    public static final int TT_INTEGER      = 5;
    public static final int TT_CHAR         = 6;
    
    public static final int TT_COMMA        = 7;
    public static final int TT_COLON        = 8;
    public static final int TT_DOT          = 9;
    public static final int TT_QUESTIONMARK = 10;
    public static final int TT_SHARP        = 11;
    
    public static final int TT_LPAREN       = 12;
    public static final int TT_RPAREN       = 13;
    public static final int TT_LBRACK       = 14;
    public static final int TT_RBRACK       = 15;
    public static final int TT_LBRACE       = 16;
    public static final int TT_RBRACE       = 17;
    
    public static final int TT_PLUS         = 18;
    public static final int TT_MINUS        = 19;
    public static final int TT_MUL          = 20;
    public static final int TT_DIV          = 21;
    public static final int TT_MOD          = 22;
    public static final int TT_EQ           = 23;
    public static final int TT_NE           = 24;
    public static final int TT_GT           = 25;
    public static final int TT_GE           = 26;
    public static final int TT_LT           = 27;
    public static final int TT_LE           = 28;
    public static final int TT_NOT          = 29;
    public static final int TT_AND          = 30;
    public static final int TT_OR           = 31;
    public static final int TT_CONCAT       = 32;
    
    public static final int TT_NULL         = 33;
    public static final int TT_TRUE         = 34;
    public static final int TT_FALSE        = 35;
    
    public static final int TT_IDENTIFIER   = 36;
    
    public static final int TOKEN_COUNT     = 37;
    
    // Token handler table
    
    static final TokenHandler[] tokenHandlers = new TokenHandler[TOKEN_COUNT];
    
    static {
        tokenHandlers[TT_EOF]          = new EofHandler(TT_EOF);
        
        tokenHandlers[TT_LINECOMMENT]  = new LineCommentHandler(TT_LINECOMMENT, "//");
        tokenHandlers[TT_BLOCKCOMMENT] = new BlockCommentHandler(TT_BLOCKCOMMENT, "/*", "*/");
        
        tokenHandlers[TT_STRING]       = new StringHandler(TT_STRING, '"', '\\', true);
        tokenHandlers[TT_FLOAT]        = new FloatHandler(TT_FLOAT, '.', true);
        tokenHandlers[TT_INTEGER]      = new IntHandler(TT_INTEGER);
        tokenHandlers[TT_CHAR]         = new CharHandler(TT_CHAR, '\'', '\\', true);
        
        tokenHandlers[TT_COMMA]        = new SequenceHandler(TT_COMMA, ",");
        tokenHandlers[TT_COLON]        = new SequenceHandler(TT_COLON, ":");
        tokenHandlers[TT_DOT]          = new SequenceHandler(TT_DOT, ".");
        tokenHandlers[TT_QUESTIONMARK] = new SequenceHandler(TT_QUESTIONMARK, "?");
        tokenHandlers[TT_SHARP]        = new SequenceHandler(TT_SHARP, "#");
        
        tokenHandlers[TT_LPAREN]       = new SequenceHandler(TT_LPAREN, "(");
        tokenHandlers[TT_RPAREN]       = new SequenceHandler(TT_RPAREN, ")");
        tokenHandlers[TT_LBRACK]       = new SequenceHandler(TT_LBRACK, "[");
        tokenHandlers[TT_RBRACK]       = new SequenceHandler(TT_RBRACK, "]");
        tokenHandlers[TT_LBRACE]       = new SequenceHandler(TT_LBRACE, "{");
        tokenHandlers[TT_RBRACE]       = new SequenceHandler(TT_RBRACE, "}");
        
        tokenHandlers[TT_PLUS]         = new SequenceHandler(TT_PLUS, "+");
        tokenHandlers[TT_MINUS]        = new SequenceHandler(TT_MINUS, "-");
        tokenHandlers[TT_MUL]          = new SequenceHandler(TT_MUL, "*");
        tokenHandlers[TT_DIV]          = new SequenceHandler(TT_DIV, "/");
        tokenHandlers[TT_MOD]          = new SequenceHandler(TT_MOD, "%");
        tokenHandlers[TT_EQ]           = new SequenceHandler(TT_EQ, "==");
        tokenHandlers[TT_NE]           = new SequenceHandler(TT_NE, "!=");
        tokenHandlers[TT_GT]           = new SequenceHandler(TT_GT, ">");
        tokenHandlers[TT_GE]           = new SequenceHandler(TT_GE, ">=");
        tokenHandlers[TT_LT]           = new SequenceHandler(TT_LT, "<");
        tokenHandlers[TT_LE]           = new SequenceHandler(TT_LE, "<=");
        tokenHandlers[TT_NOT]          = new SequenceHandler(TT_NOT, "!");
        tokenHandlers[TT_AND]          = new SequenceHandler(TT_AND, "&&");
        tokenHandlers[TT_OR]           = new SequenceHandler(TT_OR, "||");
        tokenHandlers[TT_CONCAT]       = new SequenceHandler(TT_CONCAT, "|");
        
        tokenHandlers[TT_NULL]         = new KeywordHandler(TT_NULL, "null");
        tokenHandlers[TT_TRUE]         = new KeywordHandler(TT_TRUE, "true");
        tokenHandlers[TT_FALSE]        = new KeywordHandler(TT_FALSE, "false");
        
        tokenHandlers[TT_IDENTIFIER]   = new IdentifierHandler(TT_IDENTIFIER);
    }
    
    public static TokenHandler[] getTokenHandlerTable() {
        return tokenHandlers.clone();
    }
    
}
