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

import java.math.BigInteger;
import java.math.BigDecimal;

import java.util.Iterator;
import java.util.Set;

import org.foxlabs.etk.EtkException;
import org.foxlabs.etk.bean.BeanManager;
import org.foxlabs.etk.bean.MethodAccessor;
import org.foxlabs.etk.bean.MethodNotFoundException;
import org.foxlabs.etk.bean.PropertyAccessor;
import org.foxlabs.etk.function.Function;
import org.foxlabs.etk.function.FunctionLoader;
import org.foxlabs.etk.function.FunctionNotFoundException;
import org.foxlabs.etk.support.*;
import org.foxlabs.etk.variable.VariableTable;

public class NodeFactory {
    
    private NodeFactory() {}
    
    public static NullNode createNullNode() {
        return NullNode.singleInstance;
    }
    
    public static LiteralNode<Boolean> createLiteralNode(boolean value) {
        return value ? TrueNode.singleInstance : FalseNode.singleInstance;
    }
    
    public static LiteralNode<Character> createLiteralNode(char value) {
        return new CharNode(value);
    }
    
    public static LiteralNode<Number> createLiteralNode(byte value) {
        return new NumberNode(value);
    }
    
    public static LiteralNode<Number> createLiteralNode(short value) {
        return new NumberNode(value);
    }
    
    public static LiteralNode<Number> createLiteralNode(int value) {
        return new NumberNode(value);
    }
    
    public static LiteralNode<Number> createLiteralNode(long value) {
        return new NumberNode(value);
    }
    
    public static LiteralNode<Number> createLiteralNode(float value) {
        return new NumberNode(value);
    }
    
    public static LiteralNode<Number> createLiteralNode(double value) {
        return new NumberNode(value);
    }
    
    public static LiteralNode<?> createLiteralNode(Object value)
            throws EtkException {
        if (value == null)
            return createNullNode();
        
        Class<?> type = value.getClass();
        
        if (Number.class.isAssignableFrom(type)) {
            if (type == Byte.class ||
                type == Short.class ||
                type == Integer.class ||
                type == Long.class ||
                type == Float.class ||
                type == Double.class ||
                type == BigInteger.class ||
                type == BigDecimal.class)
                return new NumberNode((Number) value);
        } else if (type == String.class) {
            return new StringNode((String) value);
        } else if (type == Boolean.class) {
            return createLiteralNode(((Boolean) value).booleanValue());
        } else if (type == Character.class) {
            return new CharNode((Character) value);
        } else if (type.isEnum()) {
            return new EnumNode((Enum<?>) value);
        }
        
        throw new LiteralTypeNotSupportedException(type);
    }
    
    public static NegateNode createNegateNode(Node operand)
            throws EtkException {
        Calculator calculator = getCalculator(Node.NEG_NODE,
                                              operand.getType());
        
        return new NegateNode(calculator,
                              convertNodeIfNecessary(operand, calculator.getType()));
    }
    
    public static AddNode createAddNode(Node operand1, Node operand2)
            throws EtkException {
        Calculator calculator = getCalculator(Node.ADD_NODE,
                                              operand1.getType(),
                                              operand2.getType());
        
        return new AddNode(calculator,
                           convertNodeIfNecessary(operand1, calculator.getType()),
                           convertNodeIfNecessary(operand2, calculator.getType()));
    }
    
    public static SubtractNode createSubtractNode(Node operand1, Node operand2)
            throws EtkException {
        Calculator calculator = getCalculator(Node.SUB_NODE,
                                              operand1.getType(),
                                              operand2.getType());
        
        return new SubtractNode(calculator,
                                convertNodeIfNecessary(operand1, calculator.getType()),
                                convertNodeIfNecessary(operand2, calculator.getType()));
    }
    
    public static MultiplyNode createMultiplyNode(Node operand1, Node operand2)
            throws EtkException {
        Calculator calculator = getCalculator(Node.MUL_NODE,
                                              operand1.getType(),
                                              operand2.getType());
        
        return new MultiplyNode(calculator,
                                convertNodeIfNecessary(operand1, calculator.getType()),
                                convertNodeIfNecessary(operand2, calculator.getType()));
    }
    
    public static DivideNode createDivideNode(Node operand1, Node operand2)
            throws EtkException {
        Calculator calculator = getCalculator(Node.DIV_NODE,
                                              operand1.getType(),
                                              operand2.getType());
        
        return new DivideNode(calculator,
                              convertNodeIfNecessary(operand1, calculator.getType()),
                              convertNodeIfNecessary(operand2, calculator.getType()));
    }
    
    public static ModuloNode createModuloNode(Node operand1, Node operand2)
            throws EtkException {
        Calculator calculator = getCalculator(Node.MOD_NODE,
                                              operand1.getType(),
                                              operand2.getType());
        
        return new ModuloNode(calculator,
                              convertNodeIfNecessary(operand1, calculator.getType()),
                              convertNodeIfNecessary(operand2, calculator.getType()));
    }
    
    public static EqualNode createEqualNode(Node operand1, Node operand2)
            throws EtkException {
        Class<?> type = getEqualityType(Node.EQ_NODE,
                                        operand1.getType(),
                                        operand2.getType());
        
        return new EqualNode(convertNodeIfNecessary(operand1, type),
                             convertNodeIfNecessary(operand2, type));
    }
    
    public static NotEqualNode createNotEqualNode(Node operand1, Node operand2)
            throws EtkException {
        Class<?> type = getEqualityType(Node.NE_NODE,
                                        operand1.getType(),
                                        operand2.getType());
        
        return new NotEqualNode(convertNodeIfNecessary(operand1, type),
                                convertNodeIfNecessary(operand2, type));
    }
    
    public static GreaterThanNode createGreaterThanNode(Node operand1, Node operand2)
            throws EtkException {
        Comparator comparator = getComparator(Node.GT_NODE,
                                              operand1.getType(),
                                              operand2.getType());
        
        return new GreaterThanNode(comparator,
                                   convertNodeIfNecessary(operand1, comparator.getType()),
                                   convertNodeIfNecessary(operand2, comparator.getType()));
    }
    
    public static GreaterThanEqualNode createGreaterThanEqualNode(Node operand1, Node operand2)
            throws EtkException {
        Comparator comparator = getComparator(Node.GE_NODE,
                                              operand1.getType(),
                                              operand2.getType());
        
        return new GreaterThanEqualNode(comparator,
                                        convertNodeIfNecessary(operand1, comparator.getType()),
                                        convertNodeIfNecessary(operand2, comparator.getType()));
    }
    
    public static LessThanNode createLessThanNode(Node operand1, Node operand2)
            throws EtkException {
        Comparator comparator = getComparator(Node.LT_NODE,
                                              operand1.getType(),
                                              operand2.getType());
        
        return new LessThanNode(comparator,
                                convertNodeIfNecessary(operand1, comparator.getType()),
                                convertNodeIfNecessary(operand2, comparator.getType()));
    }
    
    public static LessThanEqualNode createLessThanEqualNode(Node operand1, Node operand2)
            throws EtkException {
        Comparator comparator = getComparator(Node.LE_NODE,
                                              operand1.getType(),
                                              operand2.getType());
        
        return new LessThanEqualNode(comparator,
                                     convertNodeIfNecessary(operand1, comparator.getType()),
                                     convertNodeIfNecessary(operand2, comparator.getType()));
    }
    
    public static NotNode createNotNode(Node operand)
            throws EtkException {
        return new NotNode(convertNodeIfNecessary(operand, Boolean.class));
    }
    
    public static AndNode createAndNode(Node operand1, Node operand2)
            throws EtkException {
        return new AndNode(convertNodeIfNecessary(operand1, Boolean.class),
                           convertNodeIfNecessary(operand2, Boolean.class));
    }
    
    public static OrNode createOrNode(Node operand1, Node operand2)
            throws EtkException {
        return new OrNode(convertNodeIfNecessary(operand1, Boolean.class),
                          convertNodeIfNecessary(operand2, Boolean.class));
    }
    
    public static ConditionNode createConditionNode(Node condition, Node case1, Node case2)
            throws EtkException {
        return new ConditionNode(convertNodeIfNecessary(condition, Boolean.class),
                                 case1,
                                 case2);
    }
    
    public static IndexNode createIndexNode(Node collection, Node key)
            throws EtkException {
        Class<?> collectionType = collection.getType();
        Class<?> keyType = Indexer.getKeyType(collectionType);
        
        if (keyType == null)
            throw new IndexedTypeNotSupportedException(collectionType);
        
        return new IndexNode(Indexer.getInstance(collectionType, keyType),
                             collection,
                             convertNodeIfNecessary(key, keyType));
    }
    
    public static CastNode createCastNode(Node operand, Class<?> type)
            throws EtkException {
        Converter converter = getConverter(operand.getType(), type);
        
        return new CastNode(converter, operand);
    }
    
    public static ArrayNode createArrayNode(Node... items)
            throws EtkException {
        return new ArrayNode(items);
    }
    
    public static ConcatNode createConcatNode(Node... operands)
            throws EtkException {
        return new ConcatNode(operands);
    }
    
    public static VariableNode createVariableNode(VariableTable table, String name)
            throws EtkException {
        Class<?> type = Types.wrapperOf(table.getVariableType(name));
        return new VariableNode(type, name);
    }
    
    public static FunctionNode createFunctionNode(FunctionLoader loader,
            String name, String namespace, Node... arguments)
            throws EtkException {
        Class<?>[] argtypes = getNodeTypes(arguments);
        
        Signature signature = new Signature(name, namespace, argtypes);
        Set<Signature> candidates = loader.getSignatures(namespace, name);
        if (candidates.isEmpty())
            throw new FunctionNotFoundException(signature);
        
        Signature choice = retainCandidates(candidates, argtypes);
        
        if (choice == null) {
            if (candidates.isEmpty())
                throw new FunctionNotFoundException(signature);
            
            reduceCandidates(candidates);
            if (candidates.size() > 1)
                throw new FunctionAmbiguityException(signature);
            
            choice = candidates.iterator().next();
        }
        
        Function function = loader.loadFunction(choice);
        return new FunctionNode(function, convertNodesIfNecessary(choice, arguments));
    }
    
    public static PropertyNode createPropertyNode(BeanManager manager, String name, Node object)
            throws EtkException {
        PropertyAccessor accessor = manager.getPropertyAccessor(object.getType(), name);
        return new PropertyNode(accessor, object);
    }
    
    public static MethodNode createMethodNode(BeanManager manager,
            String name, Node object, Node... arguments) throws EtkException {
        Class<?> type = object.getType();
        Class<?>[] argtypes = getNodeTypes(arguments);
        
        Signature signature = new Signature(name, Types.toString(type), argtypes);
        Set<Signature> candidates = manager.getBeanEntity(type).getMethodSignatures(name);
        if (candidates.isEmpty())
            throw new MethodNotFoundException(type, signature);
        
        Signature choice = retainCandidates(candidates, argtypes);
        
        if (choice == null) {
            if (candidates.isEmpty())
                throw new MethodNotFoundException(type, signature);
            
            reduceCandidates(candidates);
            if (candidates.size() > 1)
                throw new MethodAmbiguityException(type, signature);
            
            choice = candidates.iterator().next();
        }
        
        MethodAccessor accessor = manager.getMethodAccessor(type, choice);
        return new MethodNode(accessor, object, convertNodesIfNecessary(choice, arguments));
    }
    
    // ===== Utility ==========================================================
    
    private static Class<?>[] getNodeTypes(Node... nodes) {
        Class<?>[] types = new Class<?>[nodes.length];
        for (int i = 0; i < nodes.length; i++)
            types[i] = nodes[i].getType();
        return types;
    }
    
    private static Node convertNodeIfNecessary(Node node, Class<?> toType)
            throws SemanticException {
        Class<?> fromType = node.getType();
        
        Converter converter = Converter.getInstance(fromType, toType);
        if (converter == null || converter.getKind() == Converter.NARROWING)
            throw new CannotConvertException(fromType, toType);
        
        return converter.getKind() == Converter.IDENTITY
                   ? node
                   : new ConvertNode(converter, node);
    }
    
    private static Node[] convertNodesIfNecessary(Signature signature, Node[] nodes)
            throws SemanticException {
        Node[] result = new Node[nodes.length];
        for (int i = 0; i < nodes.length; i++)
            result[i] = convertNodeIfNecessary(nodes[i], signature.getArgumentType(i));
        return result;
    }
    
    private static Converter getConverter(Class<?> fromType, Class<?> toType)
            throws SemanticException {
        Converter converter = Converter.getInstance(fromType, toType);
        
        if (converter == null)
            throw new CannotConvertException(fromType, toType);
        
        return converter;
    }
    
    private static Calculator getCalculator(int id, Class<?> type)
            throws SemanticException {
        Calculator calculator = Calculator.getInstance(type);
        
        if (calculator == null)
            throw new UnaryOperatorNotFoundException(id, type);
        
        return calculator;
    }
    
    private static Calculator getCalculator(int id, Class<?> type1, Class<?> type2)
            throws SemanticException {
        Calculator calculator = Calculator.getInstance(type1, type2);
        
        if (calculator == null)
            throw new BinaryOperatorNotFoundException(id, type1, type2);
        
        return calculator;
    }
    
    private static Comparator getComparator(int id, Class<?> type1, Class<?> type2)
            throws SemanticException {
        Comparator comparator = Comparator.getInstance(type1, type2);
        
        if (comparator == null)
            throw new BinaryOperatorNotFoundException(id, type1, type2);
        
        return comparator;
    }
    
    private static Class<?> getEqualityType(int id, Class<?> type1, Class<?> type2)
            throws SemanticException {
        Class<?> type = Comparator.getEqualityType(type1, type2);
        
        if (type == null)
            throw new BinaryOperatorNotFoundException(id, type1, type2);
        
        return type;
    }
    
    private static Signature retainCandidates(Set<Signature> signatires, Class<?>[] argtypes) {
        Iterator<Signature> i;
        for (i = signatires.iterator(); i.hasNext(); ) {
            Signature signature = i.next();
            if (signature.isSameArguments(argtypes))
                return signature;
            if (!signature.isAssignableArguments(argtypes))
                i.remove();
        }
        return null;
    }
    
    private static void reduceCandidates(Set<Signature> candidates) {
        if (candidates.size() > 1) {
            Iterator<Signature> i;
            for (i = candidates.iterator(); i.hasNext(); ) {
                Signature candidate = i.next();
                for (Signature other : candidates)
                    if (candidate != other && candidate.isAssignableArguments(other)) {
                        i.remove();
                        break;
                    }
            }
        }
    }
    
}
