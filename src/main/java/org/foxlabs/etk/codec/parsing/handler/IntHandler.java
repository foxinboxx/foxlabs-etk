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

import java.math.BigInteger;

import org.foxlabs.etk.codec.parsing.LookaheadReader;
import org.foxlabs.etk.codec.parsing.Token;
import org.foxlabs.etk.codec.parsing.TokenHandler;

public class IntHandler extends TokenHandler {
    
    public IntHandler(int tokenId) {
        super(tokenId);
    }
    
    public Token scanToken(LookaheadReader in, int flags) throws IOException {
        int ch = in.read();
        if (ch == LookaheadReader.EOF || !isDigit(ch))
            return null;
        
        while (isDigit(ch = in.read()));
        
        if (ch != LookaheadReader.EOF)
            in.unread();
        
        Number n = null;
        String s = in.getString();
        try {
            n = Integer.valueOf(s);
        } catch (NumberFormatException e1) {
            try {
                n = Long.valueOf(s);
            } catch (NumberFormatException e2) {
                try {
                    n = new BigInteger(s);
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
        return "<INT>";
    }
    
    private static boolean isDigit(int ch) {
        return ch >= '0' && ch <= '9';
    }
    
}
