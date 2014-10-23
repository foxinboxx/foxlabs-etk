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

public class EofHandler extends TokenHandler {
    
    public EofHandler(int tokenId) {
        super(tokenId);
    }
    
    public Token scanToken(LookaheadReader in, int flags) throws IOException {
        return in.read() == LookaheadReader.EOF
            ? in.flushToken(tokenId)
            : null;
    }
    
    public void printToken(Object value, Writer out) throws IOException {
        // Nothing to print
    }
    
    public String toString() {
        return "<EOF>";
    }
    
}
