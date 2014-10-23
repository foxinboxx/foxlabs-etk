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

package org.foxlabs.etk.node;

import org.foxlabs.etk.Environment;
import org.foxlabs.etk.EvaluationException;
import org.foxlabs.etk.support.Indexer;

public final class IndexNode extends Node implements OperatorNode {
    
    private final Node collection;
    private final Node key;
    private final Indexer indexer;
    
    IndexNode(Indexer indexer, Node collection, Node key) {
        this.collection = collection;
        this.key = key;
        this.indexer = indexer;
    }
    
    public int getId() {
        return INDEX_NODE;
    }
    
    public int getPrecedence() {
        return operatorPrecedences[getId()];
    }
    
    public String getSymbol() {
        return operatorSymbols[getId()];
    }
    
    public Class<?> getType() {
        return indexer.getType();
    }
    
    public Node getCollection() {
        return collection;
    }
    
    public Node getKey() {
        return key;
    }
    
    public Object evaluate(Environment context) throws EvaluationException {
        Object collectionObj = collection.evaluate(context);
        Object keyObj = key.evaluate(context);
        try {
            return indexer.lookup(collectionObj, keyObj);
        } catch (Exception e) {
            throw new EvaluationException(this, e);
        }
    }
    
    public <T extends Throwable> void accept(NodeVisitor<T> visitor) throws T {
        visitor.visit(this);
    }
    
    public boolean isDetermined() {
        return collection.isDetermined() &&
               key.isDetermined();
    }
    
    public int hashCode() {
        int hash = getId();
        hash = 31 * hash + collection.hashCode();
        hash = 31 * hash + key.hashCode();
        return hash;
    }
    
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        
        if (obj instanceof IndexNode) {
            IndexNode other = (IndexNode) obj;
            return getType() == other.getType() &&
                   collection.equals(other.collection) &&
                   key.equals(other.key);
        }
        
        return false;
    }
    
}
