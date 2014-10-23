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

package org.foxlabs.etk.xml;

public interface XmlConstants {
    
    String DEFAULT_NS_URI       = "http://foxlabs.org/p/etk/1.0/schema/NodeTypes1_0.xsd";
    String DEFAULT_NS_PREFIX    = "etk";
    
    String XML_NULL             = "Null";
    String XML_TRUE             = "True";
    String XML_FALSE            = "False";
    String XML_ENUM             = "Enum";
    String XML_ENUM_TYPE        = "type";
    String XML_CHAR             = "Char";
    String XML_NUMBER           = "Number";
    String XML_NUMBER_TYPE      = "type";
    String XML_STRING           = "String";
    String XML_VARIABLE         = "Variable";
    String XML_ARRAY            = "Array";
    String XML_FUNCTION         = "Function";
    String XML_FUNCTION_NAME    = "name";
    String XML_FUNCTION_ARGS    = "Arguments";
    String XML_CAST             = "Cast";
    String XML_CAST_TYPE        = "type";
    String XML_NEG              = "Neg";
    String XML_ADD              = "Add";
    String XML_SUB              = "Sub";
    String XML_MUL              = "Mul";
    String XML_DIV              = "Div";
    String XML_MOD              = "Mod";
    String XML_EQ               = "Eq";
    String XML_NE               = "Ne";
    String XML_GT               = "Gt";
    String XML_GE               = "Ge";
    String XML_LT               = "Lt";
    String XML_LE               = "Le";
    String XML_NOT              = "Not";
    String XML_AND              = "And";
    String XML_OR               = "Or";
    String XML_CONDITION        = "Condition";
    String XML_CONDITION_IF     = "If";
    String XML_CONDITION_THEN   = "Then";
    String XML_CONDITION_ELSE   = "Else";
    String XML_CONCAT           = "Concat";
    String XML_INDEX            = "Index";
    String XML_INDEX_COLLECTION = "Collection";
    String XML_INDEX_KEY        = "Key";
    String XML_PROPERTY         = "Property";
    String XML_PROPERTY_NAME    = "name";
    String XML_PROPERTY_OBJECT  = "Object";
    String XML_METHOD           = "Method";
    String XML_METHOD_NAME      = "name";
    String XML_METHOD_OBJECT    = "Object";
    String XML_METHOD_ARGS      = "Arguments";
    
}
