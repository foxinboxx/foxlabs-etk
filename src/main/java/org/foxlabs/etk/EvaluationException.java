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

import java.io.PrintStream;

import org.foxlabs.etk.node.Node;

public class EvaluationException extends RuntimeException {
    private static final long serialVersionUID = -1885076974465179916L;
    
    private static final int MAX_TRACE_LEVEL = 100;
    
    private final Node node;
    
    private Expression owner;
    private String name;
    
    public EvaluationException(Node node, String message) {
        super(message);
        this.node = node;
    }
    
    public EvaluationException(Node node, String message, Throwable cause) {
        super(message, cause);
        this.node = node;
    }
    
    public EvaluationException(Node node, Throwable cause) {
        super(cause);
        this.node = node;
    }
    
    public Node getNode() {
        return node;
    }
    
    public Expression getOwner() {
        return owner;
    }
    
    public void setOwner(Expression owner) {
        this.owner = owner;
    }
    
    public String getOwnerName() {
        return name;
    }
    
    public void setOwnerName(String name) {
        this.name = name;
    }
    
    public void printStackTrace(PrintStream s) {
        synchronized (s) {
            s.println(getMessage());
            
            int level = 0;
            Throwable cause = this;
            while (cause instanceof EvaluationException) {
                EvaluationException e = (EvaluationException) cause;
                if (e.owner != null) {
                    s.print("\tat ");
                    String source = e.owner.getSource();
                    int line = e.node.getSourceLine();
                    int column = e.node.getSourceColumn();
                    s.print("[");
                    if (e.name != null) {
                        s.print(e.name);
                        s.print(':');
                    }
                    if (source == null || line < 0 || column < 0) {
                        s.print("Unknown source");
                    } else {
                        s.print(line);
                        s.print(',');
                        s.print(column);
                    }
                    s.print("]:\t");
                    if (source == null) {
                        s.print(e.owner);
                    } else {
                        s.print(source);
                    }
                    s.println();
                }
                
                if (++level > MAX_TRACE_LEVEL) {
                    s.println("\t...");
                    break;
                }
                
                cause = e.getCause();
            }
        }
    }
    
}
