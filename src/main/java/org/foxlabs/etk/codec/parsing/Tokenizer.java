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

package org.foxlabs.etk.codec.parsing;

import java.io.Reader;
import java.io.IOException;

public final class Tokenizer {
    
    public static final int PRESERVE_WHITESPACE = 0x01;
    public static final int CASE_INSENSITIVE    = 0x02;
    
    private final LookaheadReader reader;
    private final TokenScanner[] scanners;
    private final TokenFilter filter;
    private final int flags;
    
    private Token[] tokens = new Token[100];
    private int size = 0;
    private int cursor = 0;
    
    private int[] marks = new int[10];
    private int marker = 0;
    
    public Tokenizer(Reader reader,
                     TokenScanner[] scanners) {
        this(reader, scanners, TokenFilter.ALL, 0);
    }
    
    public Tokenizer(Reader reader,
                     TokenScanner[] scanners,
                     TokenFilter filter) {
        this(reader, scanners, filter, 0);
    }
    
    public Tokenizer(Reader reader,
                     TokenScanner[] scanners,
                     TokenFilter filter,
                     int flags) {
        this.reader = reader instanceof LookaheadReader
            ? (LookaheadReader) reader
            : new LookaheadReader(reader);
        this.scanners = scanners;
        this.filter = filter == null ? TokenFilter.ALL : filter;
        this.flags = flags;
    }
    
    public int getFlags() {
        return flags;
    }
    
    public boolean testFlags(int mask) {
        return (flags & mask) == mask;
    }
    
    public int getLine() {
        return size == 0
            ? reader.getLine()
            : tokens[cursor < size ? cursor : cursor - 1].getLine();
    }
    
    public int getColumn() {
        return size == 0
            ? reader.getColumn()
            : tokens[cursor < size ? cursor : cursor - 1].getColumn();
    }
    
    public Token next() throws IOException {
        return readToken() ? tokens[cursor++] : null;
    }
    
    public Token next(int tokenId) throws IOException {
        return readToken(tokenId) ? tokens[cursor++] : null;
    }
    
    public Token next(int... tokenIds) throws IOException {
        for (int i = 0; i < tokenIds.length; i++) {
            Token token = next(tokenIds[i]);
            if (token != null)
                return token;
        }
        return null;
    }
    
    private boolean readToken() throws IOException {
        if (cursor < size)
            return true;
        
        skipWhitespace();
        
        for (TokenScanner scanner : scanners) {
            Token token = scanner.scanToken(reader, flags);
            if (token == null) {
                reader.reset();
            } else if (filter.accept(token)) {
                appendToken(token);
                return true;
            } else {
                return readToken();
            }
        }
        
        return false;
    }
    
    private boolean readToken(int tokenId) throws IOException {
        if (cursor < size)
            return tokens[cursor].getId() == tokenId;
        
        skipWhitespace();
        
        Token token = scanners[tokenId].scanToken(reader, tokenId);
        if (token == null) {
            reader.reset();
            if (readToken())
                return tokens[cursor].getId() == tokenId;
        } else if (filter.accept(token)) {
            appendToken(token);
            return true;
        }
        
        return false;
    }
    
    private void appendToken(Token token) {
        if (size == tokens.length) {
            Token[] dump = new Token[size * 3 / 2 + 1];
            System.arraycopy(tokens, 0, dump, 0, size);
            tokens = dump;
        }
        tokens[size++] = token;
    }
    
    private void skipWhitespace() throws IOException {
        if ((flags & PRESERVE_WHITESPACE) == 0) {
            reader.readWhitespace();
            reader.flush();
        }
    }
    
    public Token last() {
        if (cursor == 0)
            throw new IllegalStateException();
        return tokens[cursor - 1];
    }
    
    public void mark() {
        if (marker == marks.length) {
            int[] dump = new int[marks.length * 3 / 2 + 1];
            System.arraycopy(marks, 0, dump, 0, marks.length);
            marks = dump;
        }
        marks[marker++] = cursor;
    }
    
    public void reset() {
        if (marker == 0)
            throw new IllegalStateException();
        cursor = marks[--marker];
    }
    
    public void consume() {
        size -= cursor;
        if (size > 0)
            System.arraycopy(tokens, cursor, tokens, 0, size);
        
        cursor = 0;
        for (int i = 0; i < marker; i++)
            marks[i] = 0;
    }
    
}
