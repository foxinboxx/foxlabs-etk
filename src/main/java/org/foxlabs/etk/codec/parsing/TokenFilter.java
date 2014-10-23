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

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

public interface TokenFilter {
    
    boolean accept(Token token);
    
    TokenFilter ALL = new TokenFilter() {
        public boolean accept(Token token) {
            return true;
        }
    };
    
    class Not implements TokenFilter {
        
        final TokenFilter filter;
        
        public Not(TokenFilter filter) {
            this.filter = filter;
        }
        
        public boolean accept(Token token) {
            return !filter.accept(token);
        }
        
    }
    
    class And implements TokenFilter {
        
        final TokenFilter[] filters;
        
        public And(TokenFilter... filters) {
            this.filters = filters;
        }
        
        public boolean accept(Token token) {
            for (TokenFilter filter : filters)
                if (!filter.accept(token))
                    return false;
            return true;
        }
        
    }
    
    class Or implements TokenFilter {
        
        final TokenFilter[] filters;
        
        public Or(TokenFilter... filters) {
            this.filters = filters;
        }
        
        public boolean accept(Token token) {
            for (TokenFilter filter : filters)
                if (filter.accept(token))
                    return true;
            return false;
        }
        
    }
    
    class Exclude implements TokenFilter {
        
        final Set<Integer> tokenIds;
        
        public Exclude(int... tokenIds) {
            this.tokenIds = new HashSet<Integer>();
            for (int tokenId : tokenIds)
                this.tokenIds.add(tokenId);
        }
        
        public Exclude(Collection<Integer> tokenIds) {
            this.tokenIds = new HashSet<Integer>(tokenIds);
        }
        
        public boolean accept(Token token) {
            return !tokenIds.contains(token.getId());
        }
        
    }
    
}
