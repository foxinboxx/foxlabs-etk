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

import org.foxlabs.etk.codec.parsing.LookaheadReader;
import org.foxlabs.etk.codec.parsing.Token;
import org.foxlabs.etk.codec.parsing.TokenHandler;

public class IdentifierHandler extends TokenHandler {
    
    public IdentifierHandler(int tokenId) {
        super(tokenId);
    }
    
    public Token scanToken(LookaheadReader in, int flags) throws IOException {
        int ch = in.read();
        if (ch == LookaheadReader.EOF || !isIdentifierStart(ch))
            return null;
        
        for (ch = in.read(); isIdentifierPart(ch); ch = in.read());
        
        if (ch != LookaheadReader.EOF)
            in.unread();
        
        return in.flushToken(tokenId, in.getString());
    }
    
    public void printToken(Object value, Writer out) throws IOException {
        out.write((String) value);
    }
    
    public String toString() {
        return "<IDENTIFIER>";
    }
    
    public static boolean isIdentifierStart(int ch) {
        return Character.isLetter(ch) || ch == '_' || ch == '$';
    }
    
    public static boolean isIdentifierPart(int ch) {
        return Character.isLetter(ch) || Character.isDigit(ch) ||
               ch == '_' || ch == '$';
    }
    
}
