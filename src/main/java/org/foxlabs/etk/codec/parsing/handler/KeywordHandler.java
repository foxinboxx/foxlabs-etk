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

public class KeywordHandler extends TokenHandler {

    private final char[] keywordIn;
    private final char[] keywordUp;

    public KeywordHandler(int tokenId, String keyword) {
        super(tokenId);
        this.keywordIn = keyword.toCharArray();
        this.keywordUp = keyword.toUpperCase().toCharArray();
    }

    @Override
    public Token scanToken(LookaheadReader in, int flags) throws IOException {
        int ch = in.read();

        if ((flags & Tokenizer.CASE_INSENSITIVE) == 0) {
            for (int i = 0; i < keywordIn.length; ch = in.read(), i++)
                if (ch == LookaheadReader.EOF ||
                        keywordIn[i] != ch)
                    return null;
        } else {
            for (int i = 0; i < keywordUp.length; ch = in.read(), i++)
                if (ch == LookaheadReader.EOF ||
                    keywordUp[i] != Character.toUpperCase(ch))
                    return null;
        }

        if (IdentifierHandler.isIdentifierPart(ch))
            return null;

        if (ch != LookaheadReader.EOF)
            in.unread();

        return in.flushToken(tokenId);
    }

    @Override
    public void printToken(Object value, Writer out) throws IOException {
        out.write(keywordIn);
    }

    @Override
    public String toString() {
        return "\"" + Strings.escape(new String(keywordIn)) + "\"";
    }

}
