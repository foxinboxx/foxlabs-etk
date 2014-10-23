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
import org.foxlabs.etk.codec.parsing.Tokenizer;

public class LineCommentHandler extends TokenHandler {
    
    private final char[] prefixIn;
    private final char[] prefixUp;
    
    public LineCommentHandler(int tokenId) {
        this(tokenId, "//");
    }
    
    public LineCommentHandler(int tokenId, String prefix) {
        super(tokenId);
        this.prefixIn = prefix.toCharArray();
        this.prefixUp = prefix.toUpperCase().toCharArray();
    }
    
    public Token scanToken(LookaheadReader in, int flags) throws IOException {
        int ch = in.read();
        
        if ((flags & Tokenizer.CASE_INSENSITIVE) == 0) {
            for (int i = 0; i < prefixIn.length; ch = in.read(), i++)
                if (ch == LookaheadReader.EOF ||
                    prefixIn[i] != ch)
                    return null;
        } else {
            for (int i = 0; i < prefixUp.length; ch = in.read(), i++)
                if (ch == LookaheadReader.EOF ||
                    prefixUp[i] != Character.toUpperCase(ch))
                    return null;
        }
        
        if (ch != LookaheadReader.EOF)
            in.unread();
        
        in.readLine();
        
        String s = in.getString();
        int beginIndex = prefixIn.length;
        int endIndex = s.length();
        if (s.charAt(endIndex - 1) == LookaheadReader.EOL)
            endIndex--;
        
        return in.flushToken(tokenId, s.substring(beginIndex, endIndex));
    }
    
    public void printToken(Object value, Writer out) throws IOException {
        out.write(prefixIn);
        if (value != null)
            out.write((String) value);
    }
    
    public String toString() {
        return "<LINECOMMENT>";
    }
    
}
