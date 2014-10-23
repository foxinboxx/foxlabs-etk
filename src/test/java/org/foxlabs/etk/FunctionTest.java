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

import org.junit.Test;

import org.foxlabs.etk.Expression;
import org.foxlabs.etk.Environment;
import org.foxlabs.etk.DefaultEnvironment;
import org.foxlabs.etk.node.NodeBuilder;

public class FunctionTest {
    
    @Test
    public void testFunction() {
        Environment context = DefaultEnvironment.getGlobalContext();
        NodeBuilder builder = new NodeBuilder(context);
        
        builder.pushLiteral(10)
               .pushLiteral(20)
               .pushLiteral(30)
               .pushLiteral(40)
               .pushLiteral(50)
               .function("AVG");
        
        Expression expr = builder.popExpression();
        
        System.out.println(expr);
        System.out.println(expr.evaluate(context));
    }
    
}
