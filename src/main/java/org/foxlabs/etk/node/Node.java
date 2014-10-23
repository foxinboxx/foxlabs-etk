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

import java.util.Arrays;

import org.foxlabs.etk.Environment;
import org.foxlabs.etk.EvaluationException;
import org.foxlabs.etk.codec.ExpressionEncoder;

public abstract class Node {
    
    public static final int NULL_NODE      = 1;
    public static final int TRUE_NODE      = 2;
    public static final int FALSE_NODE     = 3;
    public static final int ENUM_NODE      = 4;
    public static final int CHAR_NODE      = 5;
    public static final int NUMBER_NODE    = 6;
    public static final int STRING_NODE    = 7;
    public static final int VARIABLE_NODE  = 8;
    public static final int ARRAY_NODE     = 9;
    public static final int FUNCTION_NODE  = 10;
    public static final int CONVERT_NODE   = 11;
    public static final int CAST_NODE      = 12;
    public static final int NEG_NODE       = 13;
    public static final int ADD_NODE       = 14;
    public static final int SUB_NODE       = 15;
    public static final int MUL_NODE       = 16;
    public static final int DIV_NODE       = 17;
    public static final int MOD_NODE       = 18;
    public static final int EQ_NODE        = 19;
    public static final int NE_NODE        = 20;
    public static final int GT_NODE        = 21;
    public static final int GE_NODE        = 22;
    public static final int LT_NODE        = 23;
    public static final int LE_NODE        = 24;
    public static final int NOT_NODE       = 25;
    public static final int AND_NODE       = 26;
    public static final int OR_NODE        = 27;
    public static final int CONDITION_NODE = 28;
    public static final int CONCAT_NODE    = 29;
    public static final int INDEX_NODE     = 30;
    public static final int PROPERTY_NODE  = 31;
    public static final int METHOD_NODE    = 32;
    
    public static final int NODE_COUNT    = 33;
    
    static final int[] operatorPrecedences = new int[NODE_COUNT];
    
    static {
        Arrays.fill(operatorPrecedences, Integer.MAX_VALUE);
        
        operatorPrecedences[NEG_NODE]       = 900;
        operatorPrecedences[ADD_NODE]       = 700;
        operatorPrecedences[SUB_NODE]       = 700;
        operatorPrecedences[MUL_NODE]       = 800;
        operatorPrecedences[DIV_NODE]       = 800;
        operatorPrecedences[MOD_NODE]       = 800;
        operatorPrecedences[EQ_NODE]        = 500;
        operatorPrecedences[NE_NODE]        = 500;
        operatorPrecedences[GT_NODE]        = 600;
        operatorPrecedences[GE_NODE]        = 600;
        operatorPrecedences[LT_NODE]        = 600;
        operatorPrecedences[LE_NODE]        = 600;
        operatorPrecedences[NOT_NODE]       = 900;
        operatorPrecedences[AND_NODE]       = 400;
        operatorPrecedences[OR_NODE]        = 300;
        operatorPrecedences[CONDITION_NODE] = 200;
        operatorPrecedences[CONCAT_NODE]    = 100;
        operatorPrecedences[INDEX_NODE]     = 1000;
        operatorPrecedences[PROPERTY_NODE]  = 1000;
        operatorPrecedences[METHOD_NODE]    = 1000;
    }
    
    public static int getOperatorPrecedence(int nodeId) {
        return operatorPrecedences[nodeId];
    }
    
    public static int getOperatorPrecedence(Node node) {
        return node instanceof ConvertNode
            ? getOperatorPrecedence(((ConvertNode) node).getOperand())
            : operatorPrecedences[node.getId()];
    }
    
    static final String[] operatorSymbols = new String[NODE_COUNT];
    
    static {
        operatorSymbols[NEG_NODE]       = "-";
        operatorSymbols[ADD_NODE]       = "+";
        operatorSymbols[SUB_NODE]       = "-";
        operatorSymbols[MUL_NODE]       = "*";
        operatorSymbols[DIV_NODE]       = "/";
        operatorSymbols[MOD_NODE]       = "%";
        operatorSymbols[EQ_NODE]        = "==";
        operatorSymbols[NE_NODE]        = "!=";
        operatorSymbols[GT_NODE]        = ">";
        operatorSymbols[GE_NODE]        = ">=";
        operatorSymbols[LT_NODE]        = "<";
        operatorSymbols[LE_NODE]        = "<=";
        operatorSymbols[NOT_NODE]       = "!";
        operatorSymbols[AND_NODE]       = "&&";
        operatorSymbols[OR_NODE]        = "||";
        operatorSymbols[CONDITION_NODE] = "?";
        operatorSymbols[CONCAT_NODE]    = "|";
        operatorSymbols[INDEX_NODE]     = "[]";
        operatorSymbols[PROPERTY_NODE]  = ".";
        operatorSymbols[METHOD_NODE]    = ".";
    }
    
    public static String getOperatorSymbol(int nodeId) {
        return operatorSymbols[nodeId];
    }
    
    public static String getOperatorSymbol(Node node) {
        return node instanceof ConvertNode
            ? getOperatorSymbol(((ConvertNode) node).getOperand())
            : operatorSymbols[node.getId()];
    }
    
    private int line = -1, column = -1;
    
    Node() {}
    
    public abstract int getId();
    
    public abstract Class<?> getType();
    
    public abstract Object evaluate(Environment context) throws EvaluationException;
    
    public abstract <T extends Throwable> void accept(NodeVisitor<T> visitor) throws T;
    
    public final int getSourceLine() {
        return line;
    }
    
    public final int getSourceColumn() {
        return column;
    }
    
    public final void setSourcePosition(int line, int column) {
        this.line = line;
        this.column = column;
    }
    
    public boolean isDetermined() {
        return true;
    }
    
    public String toString() {
        return ExpressionEncoder.getDefault().encode(this);
    }
    
}
