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

public class CharHandler extends TokenHandler {
    
    private final int quote;
    private final int escape;
    private final boolean specials;
    
    public CharHandler(int tokenId) {
        this(tokenId, '\'', '\\', true);
    }
    
    public CharHandler(int tokenId, char quote, char escape, boolean specials) {
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
        if (!(ch == LookaheadReader.EOF || ch == quote || isSpecial(ch))) {
            boolean valid = true;
            
            if (ch == escape) {
                ch = in.read();
                if (!(ch == quote || ch == escape)) {
                    if (valid = specials) {
                        switch (ch) {
                            case 't':
                                ch = '\t';
                                break;
                                
                            case 'r':
                                ch = '\r';
                                break;
                                
                            case 'n':
                                ch = '\n';
                                break;
                                
                            case 'b':
                                ch = '\b';
                                break;
                                
                            case 'f':
                                ch = '\f';
                                break;
                                
                            default:
                                valid = false;
                                break;
                        }
                    }
                }
            }
            
            if (valid && in.read() == quote)
                return in.flushToken(tokenId, (char) ch);
        }
        
        throw new ParsingException("Invalid character literal",
                in.getLine(), in.getColumn());
    }
    
    public void printToken(Object value, Writer out) throws IOException {
        out.write(quote);
        
        char ch = (Character) value;
        if (ch == quote || ch == escape) {
            out.write(escape);
            out.write(ch);
        } else if (specials) {
            switch (ch) {
                case '\t':
                    out.write(escape);
                    out.write('t');
                    break;
                    
                case '\r':
                    out.write(escape);
                    out.write('r');
                    break;
                    
                case '\n':
                    out.write(escape);
                    out.write('n');
                    break;
                    
                case '\b':
                    out.write(escape);
                    out.write('b');
                    break;
                    
                case '\f':
                    out.write(escape);
                    out.write('f');
                    break;
                    
                default:
                    out.write(ch);
                    break;
            }
        } else {
            out.write(ch);
        }
        
        out.write(quote);
    }
    
    public String toString() {
        return "<CHAR>";
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
