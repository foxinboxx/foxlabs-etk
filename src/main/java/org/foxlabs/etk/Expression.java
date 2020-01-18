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

package org.foxlabs.etk;

import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.foxlabs.etk.io.NodeInputStream;
import org.foxlabs.etk.io.NodeOutputStream;
import org.foxlabs.etk.node.Node;
import org.foxlabs.etk.node.NodeVisitor;

public class Expression implements Serializable {
    private static final long serialVersionUID = 5608323719628721515L;
    
    private transient Node root;
    private transient int hash = 0;
    
    private String source;
    
    public Expression(Node root) {
        this(root, null);
    }
    
    public Expression(Node root, String source) {
        if (root == null)
            throw new NullPointerException();
        this.root = root;
        setSource(source);
    }
    
    public final Node getRootNode() {
        return root;
    }
    
    public final Class<?> getType() {
        return root.getType();
    }
    
    public Object evaluate(Environment context) throws EvaluationException {
        try {
            return root.evaluate(context);
        } catch (EvaluationException e) {
            e.setOwner(this);
            throw e;
        }
    }
    
    public final <T extends Throwable> void accept(NodeVisitor<T> visitor) throws T {
        root.accept(visitor);
    }
    
    public String getSource() {
        return source;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
    
    public int hashCode() {
        if (hash == 0)
            hash = root.hashCode();
        return hash;
    }
    
    public boolean equals(Object obj) {
        return obj instanceof Expression &&
               ((Expression) obj).root.equals(root);
    }
    
    public String toString() {
        return source == null ? root.toString() : source;
    }
    
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        Environment context = DefaultEnvironment.getGlobalContext();
        try (NodeInputStream stream = new NodeInputStream(in)) {
            root = stream.readNode(context);
        }
    }
    
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        NodeOutputStream nout = out instanceof NodeOutputStream
            ? (NodeOutputStream) out
            : new NodeOutputStream(out);
        nout.writeNode(root);
        nout.flush();
    }
    
}
