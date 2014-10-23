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

package org.foxlabs.etk.codec.parsing.handler;

import java.io.Writer;
import java.io.IOException;

import java.math.BigDecimal;

import org.foxlabs.etk.codec.parsing.LookaheadReader;
import org.foxlabs.etk.codec.parsing.ParsingException;
import org.foxlabs.etk.codec.parsing.Token;
import org.foxlabs.etk.codec.parsing.TokenHandler;

public class FloatHandler extends TokenHandler {
    
    private final int dp;
    private final boolean exp;
    
    public FloatHandler(int tokenId) {
        this(tokenId, '.', true);
    }
    
    public FloatHandler(int tokenId, char dp, boolean exp) {
        super(tokenId);
        this.dp = dp;
        this.exp = exp;
    }
    
    public Token scanToken(LookaheadReader in, int flags) throws IOException {
        int ch = in.read();
        if (ch == LookaheadReader.EOF || !isDigit(ch))
            return null;
        
        while (isDigit(ch = in.read()));
        
        if (ch != dp)
            return null;
        
        while (isDigit(ch = in.read()));
        
        if (exp && isExp(ch)) {
            ch = in.read();
            if (isSign(ch))
                ch = in.read();
            
            if (!isDigit(ch))
                throw new ParsingException("Invalid float literal",
                        in.getLine(), in.getColumn());
            
            while (isDigit(ch = in.read()));
        }
        
        if (ch != LookaheadReader.EOF)
            in.unread();
        
        Number n = null;
        String s = in.getString();
        try {
            n = Float.valueOf(s);
        } catch (NumberFormatException e1) {
            try {
                n = Double.valueOf(s);
            } catch (NumberFormatException e2) {
                try {
                    n = new BigDecimal(s);
                } catch (NumberFormatException e3) {
                    return null;
                }
            }
        }
        
        return in.flushToken(tokenId, n);
    }
    
    public void printToken(Object value, Writer out) throws IOException {
        out.write(((Number) value).toString());
    }
    
    public String toString() {
        return "<FLOAT>";
    }
    
    private static boolean isDigit(int ch) {
        return ch >= '0' && ch <= '9';
    }
    
    private static boolean isExp(int ch) {
        return ch == 'e' || ch == 'E';
    }
    
    private static boolean isSign(int ch) {
        return ch == '-' || ch == '+';
    }
    
}
