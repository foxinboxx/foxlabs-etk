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
import org.foxlabs.etk.codec.parsing.Tokenizer;

public class BlockCommentHandler extends TokenHandler {
    
    private final char[] prefixIn;
    private final char[] prefixUp;
    private final char[] suffixIn;
    private final char[] suffixUp;
    
    public BlockCommentHandler(int tokenId, String prefix, String suffix) {
        super(tokenId);
        this.prefixIn = prefix.toCharArray();
        this.prefixUp = prefix.toUpperCase().toCharArray();
        this.suffixIn = suffix.toCharArray();
        this.suffixUp = suffix.toUpperCase().toCharArray();
    }
    
    public Token scanToken(LookaheadReader in, int flags) throws IOException {
        int ch = in.read();
        
        if ((flags & Tokenizer.CASE_INSENSITIVE) == 0) {
            for (int i = 0; i < prefixIn.length; ch = in.read(), i++)
                if (ch == LookaheadReader.EOF ||
                    prefixIn[i] != ch)
                    return null;
            
            boolean end;
            do {
                end = true;
                for (int i = 0; i < suffixIn.length; ch = in.read(), i++) {
                    if (ch == LookaheadReader.EOF)
                        throw new ParsingException("Unexpected end of comment",
                                in.getLine(), in.getColumn());
                    
                    if (suffixIn[i] != ch) {
                        end = false;
                        ch = in.read();
                        break;
                    }
                }
            } while (!end);
        } else {
            for (int i = 0; i < prefixUp.length; ch = in.read(), i++)
                if (ch == LookaheadReader.EOF ||
                    prefixUp[i] != Character.toUpperCase(ch))
                    return null;
            
            boolean end;
            do {
                end = true;
                for (int i = 0; i < suffixUp.length; ch = in.read(), i++) {
                    if (ch == LookaheadReader.EOF)
                        throw new ParsingException("Unexpected end of comment",
                                in.getLine(), in.getColumn());
                    
                    if (suffixUp[i] != Character.toUpperCase(ch)) {
                        end = false;
                        ch = in.read();
                        break;
                    }
                }
            } while (!end);
        }
        
        if (ch != LookaheadReader.EOF)
            in.unread();
        
        String s = in.getString();
        int beginIndex = prefixIn.length;
        int endIndex = s.length() - suffixIn.length;
        
        return in.flushToken(tokenId, s.substring(beginIndex, endIndex));
    }
    
    public void printToken(Object value, Writer out) throws IOException {
        out.write(prefixIn);
        if (value != null)
            out.write((String) value);
        out.write(suffixIn);
    }
    
    public String toString() {
        return "<BLOCKCOMMENT>";
    }
    
}
