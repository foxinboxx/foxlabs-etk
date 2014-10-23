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

package org.foxlabs.etk.resource;

import java.io.IOException;

import org.foxlabs.etk.EtkException;
import org.foxlabs.etk.xml.XmlException;
import org.foxlabs.etk.xml.wrapper.SimpleXmlReader;
import org.foxlabs.util.resource.MessageBundle;
import org.foxlabs.util.resource.ResourceHelper;

public class ResourceManager {
    
    private static final String RESOURCE_DIRECTORY =
        ResourceHelper.getResourcePath(ResourceManager.class);
    
    private static final MessageBundle messageBundle;
    
    static {
        messageBundle = MessageBundle.getInstance(RESOURCE_DIRECTORY + "/messages");
    }
    
    private ResourceManager() {}
    
    // Messages
    
    public static String getMessage(String key) {
        return messageBundle.get(key);
    }
    
    public static String getMessage(String key, Object... arguments) {
        return messageBundle.format(key, arguments);
    }
    
    // Exceptions
    
    public static NullPointerException newNullPointerException() {
        return new NullPointerException(getMessage("runtime.nullPointer"));
    }
    
    public static NullPointerException newNullPointerArgumentException(int index) {
        return new NullPointerException(getMessage("runtime.nullPointerArgument", index));
    }
    
    public static ArithmeticException newDivisionByZeroException() {
        return new ArithmeticException(getMessage("runtime.divisionByZero"));
    }
    
    public static IndexOutOfBoundsException newIndexOutOfBoundsException(int index, int size) {
        return new IndexOutOfBoundsException(getMessage("runtime.indexOutOfBounds", index, size));
    }
    
    public static ClassCastException newClassCastException(Class<?> type) {
        return new ClassCastException(getMessage("runtime.classCast", type));
    }
    
    public static EtkException newEtkException(String message) {
        return new EtkException(getMessage(message));
    }
    
    public static EtkException newEtkException(String message, Object... arguments) {
        return new EtkException(getMessage(message, arguments));
    }
    
    public static IOException newIOException(String message) {
        return new IOException(getMessage(message));
    }
    
    public static IOException newIOException(String message, Object... arguments) {
        return new IOException(getMessage(message, arguments));
    }
    
    public static XmlException newXmlException(SimpleXmlReader<?> in, Throwable cause) {
        return new XmlException(getMessage("xml.readError") +
                " - " + in.getLocationString(), cause);
    }
    
    public static XmlException newXmlException(SimpleXmlReader<?> in,
            String message, Object... arguments) {
        return new XmlException(getMessage(message, arguments) +
                " - " + in.getLocationString());
    }
    
}
