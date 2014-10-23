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

import java.io.Writer;
import java.io.StringWriter;
import java.io.IOException;

import org.foxlabs.etk.Expression;
import org.foxlabs.etk.node.Node;
import org.foxlabs.util.resource.Service;

public abstract class ExpressionEncoder {
    
    protected boolean namespaceAware = true;
    protected boolean prettyPrint = true;
    
    protected ExpressionEncoder() {}
    
    public abstract String getGrammar();
    
    public static ExpressionEncoder getDefault() {
        return new DefaultEncoder();
    }
    
    public static ExpressionEncoder getInstance(String grammar)
            throws UnsupportedGrammarException {
        return getInstance(grammar, Thread.currentThread().getContextClassLoader());
    }
    
    public static ExpressionEncoder getInstance(String grammar, ClassLoader cl)
            throws UnsupportedGrammarException {
        if (grammar == null || DefaultConstants.GRAMMAR.equals(grammar))
            return getDefault();
        
        Iterator<ExpressionEncoder> i = Service.lookup(ExpressionEncoder.class, cl);
        while (i.hasNext()) {
            ExpressionEncoder encoder = i.next();
            if (grammar.equals(encoder.getGrammar()))
                return encoder;
        }
        
        throw new UnsupportedGrammarException(grammar);
    }
    
    public String encode(Expression expr) {
        return encode(expr.getRootNode());
    }
    
    public void encode(Expression expr, Writer out) throws IOException {
        encode(expr.getRootNode(), out);
    }
    
    public String encode(Node node) {
        try {
            StringWriter out = new StringWriter(256);
            encode(node, out);
            return out.toString();
        } catch (IOException e) {
            throw new InternalError();
        }
    }
    
    public abstract void encode(Node node, Writer out) throws IOException;
    
    public final boolean isNamespaceAware() {
        return namespaceAware;
    }
    
    public void setNamespaceAware(boolean awareness) {
        namespaceAware = awareness;
    }
    
    public final boolean isPrettyPrint() {
        return prettyPrint;
    }
    
    public final void setPrettyPrint(boolean pretty) {
        prettyPrint = pretty;
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
