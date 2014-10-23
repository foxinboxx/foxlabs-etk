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

public class AltHandler extends TokenHandler {
    
    private final TokenHandler[] handlers;
    
    public AltHandler(TokenHandler main, TokenHandler... alts) {
        super(main.getTokenId());
        handlers = new TokenHandler[alts.length + 1];
        handlers[0] = main;
        int tokenId = main.getTokenId();
        for (int i = 0; i < alts.length; i++) {
            if (alts[i].getTokenId() != tokenId)
                throw new IllegalArgumentException(Integer.toString(i));
            handlers[i + 1] = alts[i];
        }
    }
    
    public Token scanToken(LookaheadReader in, int flags) throws IOException {
        for (int i = 0; i < handlers.length; i++) {
            try {
                Token token = handlers[i].scanToken(in, flags);
                if (token != null)
                    return token;
            } finally {
                in.reset();
            }
        }
        return null;
    }
    
    public void printToken(Object value, Writer out) throws IOException {
        handlers[0].printToken(value, out);
    }
    
    public String toString() {
        if (handlers.length == 1)
            return handlers[0].toString();
        
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        sb.append(handlers[0].toString());
        for (int i = 1; i < handlers.length; i++) {
            sb.append(" | ");
            sb.append(handlers[i].toString());
        }
        sb.append(')');
        return sb.toString();
    }
    
}
