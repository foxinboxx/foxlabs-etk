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
import org.foxlabs.etk.codec.parsing.ParsingException;
import org.foxlabs.etk.codec.parsing.Token;
import org.foxlabs.etk.codec.parsing.TokenHandler;

public class StringHandler extends TokenHandler {
    
    private final char quote;
    private final char escape;
    private final boolean specials;
    
    public StringHandler(int tokenId) {
        this(tokenId, '"', '\\', true);
    }
    
    public StringHandler(int tokenId, char quote, char escape, boolean specials) {
        super(tokenId);
        this.quote = quote;
        this.escape = escape;
        this.specials = specials;
    }
    
    public Token scanToken(LookaheadReader in, int flags) throws IOException {
        int ch = in.read();
        if (ch != quote)
            return null;
        
        ch = in.read();
        StringBuilder sb = new StringBuilder();
        for (; !(ch == LookaheadReader.EOF || isSpecial(ch)); ch = in.read()) {
            if (ch == quote) {
                return in.flushToken(tokenId, sb.toString());
            } else if (ch == escape) {
                ch = in.read();
                if (ch == quote || ch == escape) {
                    sb.append((char) ch);
                } else if (specials) {
                    boolean invalid = false;
                    switch (ch) {
                        case 't':
                            sb.append('\t');
                            break;
                            
                        case 'r':
                            sb.append('\r');
                            break;
                            
                        case 'n':
                            sb.append('\n');
                            break;
                            
                        case 'b':
                            sb.append('\b');
                            break;
                            
                        case 'f':
                            sb.append('\f');
                            break;
                            
                        default:
                            invalid = true;
                            break;
                    }
                    if (invalid)
                        break;
                } else {
                    break;
                }
            } else {
                sb.append((char) ch);
            }
        }
        
        throw new ParsingException("Invalid string literal",
                in.getLine(), in.getColumn());
    }
    
    public void printToken(Object value, Writer out) throws IOException {
        out.write(quote);
        
        String s = (String) value;
        int length = s.length();
        if (length > 0) {
            StringBuilder sb = new StringBuilder(length + 10);
            for (int i = 0; i < length; i++) {
                char ch = s.charAt(i);
                if (ch == quote || ch == escape) {
                    sb.append(escape);
                    sb.append(ch);
                } else if (specials) {
                    switch (ch) {
                        case '\t':
                            sb.append(escape);
                            sb.append('t');
                            break;
                            
                        case '\r':
                            sb.append(escape);
                            sb.append('r');
                            break;
                            
                        case '\n':
                            sb.append(escape);
                            sb.append('n');
                            break;
                            
                        case '\b':
                            sb.append(escape);
                            sb.append('b');
                            break;
                            
                        case '\f':
                            sb.append(escape);
                            sb.append('f');
                            break;
                            
                        default:
                            sb.append(ch);
                            break;
                    }
                } else {
                    sb.append(ch);
                }
            }
            out.write(sb.toString());
        }
        
        out.write(quote);
    }
    
    public String toString() {
        return "<STRING>";
    }
    
    private static boolean isSpecial(int ch) {
        switch (ch) {
            case '\t':
            case '\r':
            case '\n':
            case '\b':
            case '\f':
                return true;
                
            default:
                return false;
        }
    }
    
}
