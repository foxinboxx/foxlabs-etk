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

package org.foxlabs.etk.codec;

import java.util.Iterator;

import java.io.Reader;
import java.io.StringReader;
import java.io.IOException;

import org.foxlabs.etk.Environment;
import org.foxlabs.etk.node.Node;
import org.foxlabs.util.resource.Service;

public abstract class ExpressionDecoder {
    
    protected boolean namespaceAware = true;
    
    protected ExpressionDecoder() {}
    
    public abstract String getGrammar();
    
    public static ExpressionDecoder getDefault() {
        return new DefaultDecoder();
    }
    
    public static ExpressionDecoder getInstance(String grammar)
            throws UnsupportedGrammarException {
        return getInstance(grammar, Thread.currentThread().getContextClassLoader());
    }
    
    public static ExpressionDecoder getInstance(String grammar, ClassLoader cl)
            throws UnsupportedGrammarException {
        if (grammar == null || DefaultConstants.GRAMMAR.equals(grammar))
            return getDefault();
        
        Iterator<ExpressionDecoder> i = Service.lookup(ExpressionDecoder.class, cl);
        while (i.hasNext()) {
            ExpressionDecoder encoder = i.next();
            if (grammar.equals(encoder.getGrammar()))
                return encoder;
        }
        
        throw new UnsupportedGrammarException(grammar);
    }
    
    public Node decode(String text, Environment context) {
        try {
            return decode(new StringReader(text), context);
        } catch (IOException e) {
            throw new InternalError();
        }
    }
    
    public abstract Node decode(Reader in, Environment context)
            throws IOException;
    
    public final boolean isNamespaceAware() {
        return namespaceAware;
    }
    
    public void setNamespaceAware(boolean awareness) {
        namespaceAware = awareness;
    }
    
    public Object getProperty(String name) {
        throw new IllegalArgumentException(name);
    }
    
    public void setProperty(String name, Object value) {
        throw new IllegalArgumentException(name);
    }
    
    public boolean supportsProperty(String name) {
        try {
            getProperty(name);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
}
