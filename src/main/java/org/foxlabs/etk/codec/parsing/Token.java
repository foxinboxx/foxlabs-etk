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

public final class Token {
    
    private final int id;
    private final int line;
    private final int column;
    private final String source;
    private final Object value;
    
    Token(int id, int line, int column, String source, Object value) {
        this.id = id;
        this.line = line;
        this.column = column;
        this.source = source;
        this.value = value;
    }
    
    public int getId() {
        return id;
    }
    
    public int getLine() {
        return line;
    }
    
    public int getColumn() {
        return column;
    }
    
    public String getSource() {
        return source;
    }
    
    public Object getValue() {
        return value;
    }
    
    public <T> T getValue(Class<T> type) {
        return type.cast(value);
    }
    
    public String getString() {
        return getValue(String.class);
    }
    
    public char getChar() {
        return getValue(Character.class);
    }
    
    public Number getNumber() {
        return getValue(Number.class);
    }
    
    public boolean getBoolean() {
        return getValue(Boolean.class);
    }
    
    public Class<?> getClazz() {
        return getValue(Class.class);
    }
    
    public String toString() {
        return source;
    }
    
}
