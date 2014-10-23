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

public class LookaheadReader extends Reader {
    
    public static final int EOF = -1;
    public static final int EOL = '\n';
    
    private static final int CR = '\r';
    private static final int LF = '\n';
    
    private static final int minBufferDeltaSize = 1024;
    private static final int expectedLineLength = 80;
    
    private final Reader in;
    
    private int line0, line;
    private int column0, column;
    
    private char[] buffer = new char[minBufferDeltaSize];
    private int size = 0;
    private int offset = 0;
    private int cursor = 0;
    private int mark = 0;
    private boolean skipCr = false;
    
    public LookaheadReader(Reader in) {
        this(in, 1, 1);
    }
    
    public LookaheadReader(Reader in, int line, int column) {
        this.in = in;
        this.line0 = this.line = line;
        this.column0 = this.column = column;
    }
    
    public int getLine() {
        synchronized (lock) {
            updateCursor();
            return line;
        }
    }
    
    public int getColumn() {
        synchronized (lock) {
            updateCursor();
            return column;
        }
    }
    
    private void updateCursor() {
        if (cursor != offset) {
            line = line0;
            column = column0;
            for (int i = 0; i < offset; i++) {
                if (buffer[i] == EOL) {
                    column = 1;
                    line++;
                } else {
                    column++;
                }
            }
            cursor = offset;
        }
    }
    
    public int read() throws IOException {
        synchronized (lock) {
            int n = fillBuffer(1);
            return n < 0 ? EOF : buffer[offset++];
        }
    }
    
    public int read(char[] dest, int start, int count) throws IOException {
        if (count == 0)
            return 0;
        
        synchronized (lock) {
            int n = fillBuffer(count);
            if (n > 0) {
                System.arraycopy(buffer, offset, dest, start, n);
                offset += n;
            }
            return n;
        }
    }
    
    public String readLine() throws IOException {
        synchronized (lock) {
            int n, start = offset, length = 0;
            
            boolean eol = false;
            while (!((n = fillBuffer(expectedLineLength)) < 0 || eol)) {
                for (int i = 0; i < n; i++) {
                    if (buffer[offset++] == EOL) {
                        length += i;
                        eol = true;
                        break;
                    }
                }
                if (!eol)
                    length += n;
            }
            
            return length == 0 ? null : new String(buffer, start, length);
        }
    }
    
    public int readWhitespace() throws IOException {
        synchronized (lock) {
            int n, length = 0;
            
            boolean ws = true;
            while ((n = fillBuffer(expectedLineLength)) >= 0 && ws) {
                for (int i = 0; i < n; i++) {
                    if (ws = Character.isWhitespace(buffer[offset])) {
                        offset++;
                    } else {
                        length += i;
                        ws = false;
                        break;
                    }
                }
                if (ws)
                    length += n;
            }
            
            return length;
        }
    }
    
    private int fillBuffer(int count) throws IOException {
        int unreadCount = size - offset;
        if (unreadCount >= count)
            return count;
        
        int streamCount = count - unreadCount;
        if (streamCount < minBufferDeltaSize)
            streamCount = minBufferDeltaSize;
        
        char[] delta = new char[streamCount];
        streamCount = in.read(delta);
        if (streamCount <= 0)
            return unreadCount > 0 ? unreadCount : streamCount;
        
        int newSize = size + streamCount;
        if (newSize > buffer.length) {
            int incSize = newSize - buffer.length;            
            char[] dump = new char[buffer.length * 3 / 2 + incSize];
            System.arraycopy(buffer, 0, dump, 0, size);
            buffer = dump;
        }
        
        int n = unreadCount + streamCount;
        for (int i = 0; i < streamCount; i++) {
            char ch = delta[i];
            if (ch == LF) {
                skipCr = true;
                buffer[size++] = EOL;
            } else if (ch == CR) {
                if (skipCr) {
                    skipCr = false;
                    n--;
                } else {
                    buffer[size++] = EOL;
                }
            } else {
                skipCr = false;
                buffer[size++] = ch;
            }
        }
        
        if (skipCr)
            fillBuffer(minBufferDeltaSize);
        
        return n > count ? count : n;
    }
    
    public void unread() {
        unread(1);
    }
    
    public void unread(int count) {
        if (offset < count)
            throw new IllegalStateException();
        offset -= count;
    }
    
    public long skip(long count) throws IOException {
        synchronized (lock) {
            int n = fillBuffer((int) count);
            if (n > 0)
                offset += n;
            return n;
        }
    }
    
    public boolean ready() throws IOException {
        synchronized (lock) {
            return offset < size || in.ready();
        }
    }
    
    public boolean markSupported() {
        return true;
    }
    
    public void mark() {
        synchronized (lock) {
            mark = offset;
        }
    }
    
    public void mark(int unused) {
        mark();
    }
    
    public void reset() {
        synchronized (lock) {
            offset = mark;
        }
    }
    
    public String getString() {
        synchronized (lock) {
            return offset == 0 ? null : new String(buffer, 0, offset);
        }
    }
    
    public void flush() {
        synchronized (lock) {
            if (offset > 0) {
                updateCursor();
                line0 = line;
                column0 = column;
                
                System.arraycopy(buffer, offset, buffer, 0, size -= offset);
                offset = 0;
            }
            mark = cursor = 0;
        }
    }
    
    public Token flushToken(int tokenId) {
        return flushToken(tokenId, null);
    }
    
    public Token flushToken(int tokenId, Object value) {
        synchronized (lock) {
            String source = getString();
            try {
                return new Token(tokenId, line0, column0, source, value);
            } finally {
                flush();
            }
        }
    }
    
    public void close() throws IOException {
        synchronized (lock) {
            in.close();
        }
    }
    
}
