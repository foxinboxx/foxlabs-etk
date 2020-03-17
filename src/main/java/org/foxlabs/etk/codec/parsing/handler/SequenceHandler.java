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

import org.foxlabs.common.Strings;
import org.foxlabs.etk.codec.parsing.LookaheadReader;
import org.foxlabs.etk.codec.parsing.Token;
import org.foxlabs.etk.codec.parsing.TokenHandler;
import org.foxlabs.etk.codec.parsing.Tokenizer;

public class SequenceHandler extends TokenHandler {
    
    private final char[] sequenceIn;
    private final char[] sequenceUp;
    
    public SequenceHandler(int tokenId, String sequence) {
        super(tokenId);
        this.sequenceIn = sequence.toCharArray();
        this.sequenceUp = sequence.toUpperCase().toCharArray();
    }
    
    public Token scanToken(LookaheadReader in, int flags) throws IOException {
        int ch = in.read();
        
        if ((flags & Tokenizer.CASE_INSENSITIVE) == 0) {
            for (int i = 0; i < sequenceIn.length; ch = in.read(), i++)
                if (ch == LookaheadReader.EOF ||
                    sequenceIn[i] != ch)
                    return null;
        } else {
            for (int i = 0; i < sequenceUp.length; ch = in.read(), i++)
                if (ch == LookaheadReader.EOF ||
                    sequenceUp[i] != Character.toUpperCase(ch))
                    return null;
        }
        
        if (ch != LookaheadReader.EOF)
            in.unread();
        
        return in.flushToken(tokenId);
    }
    
    public void printToken(Object value, Writer out) throws IOException {
        out.write(sequenceIn);
    }
    
    public String toString() {
        return "\"" + Strings.escape(new String(sequenceIn)) + "\"";
    }
    
}
