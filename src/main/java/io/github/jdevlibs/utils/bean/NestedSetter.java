/*  ---------------------------------------------------------------------------
 *  * Copyright 2020-2021 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  ---------------------------------------------------------------------------
 */
package io.github.jdevlibs.utils.bean;

import io.github.jdevlibs.utils.ClassUtils;
import io.github.jdevlibs.utils.ReflectionUtils;
import io.github.jdevlibs.utils.Validators;
import io.github.jdevlibs.utils.JdbcUtils;
import io.github.jdevlibs.utils.exception.PropertyAccessException;
import io.github.jdevlibs.utils.exception.SystemException;

import java.lang.reflect.Method;

/**
 * @author Qussay Najjar
 * @version 1.1
 * @since 2014-04-13
 *
 * @author supot.jdev
 */
public class NestedSetter {
    private static final String ERR_SET = "Setter information: expected type: %s, actual type: %s.";

    private final Method[] getMethods;
    private final Method[] setMethods;
    private final Method method;
    private final String propertyName;

    private NestedSetter(Method[] getMethods, Method[] setMethods, Method method, String propertyName) {
        this.method = method;
        this.propertyName = propertyName;
        this.getMethods = getMethods;
        this.setMethods = setMethods;
    }

    /**
     * Set value to a target object (if a system cannot set value don't throw exception)
     * @param target The target object
     * @param value The value
     */
    public void setValue(Object target, Object value) {
        setValue(target, value, false);
    }

    /**
     * Set value to a target object
     * @param target The target object
     * @param value The value
     * @param throwException throw exception (true throw exception, false don't throw exception when error)
     */
    public void setValue(Object target, Object value, boolean throwException) {
        try {
            invokeSet(target, value);
        } catch (SystemException ex) {
            if (throwException) {
                validatePrimitive(value);
                String message = String.format(ERR_SET, method.getParameterTypes()[0].getName(),
                        value == null ? null : value.getClass().getName());
                throw new PropertyAccessException(message, ex);
            }
        }
    }

    /**
     * Get property class type, When not found return null
     * @return The class type of property
     */
    public Class<?> getPropertyType() {
        if (method == null) {
            return null;
        }

        Class<?>[] paramTypes = method.getParameterTypes();
        if (Validators.isEmpty(paramTypes)) {
            return null;
        }
        return paramTypes[0];
    }

    /**
     * Get property name
     * @return The property name
     */
    public String getName() {
        return propertyName;
    }

    @Override
    public String toString() {
        return "{propertyName:" + propertyName + ", " + method.getName()
                + ", setMethods:" + setMethods.length + ", getMethods:" + getMethods.length + "}";
    }

    /**
     * Create a setter for a nested property.
     * @param clazz The class for create Get/Set method
     * @param propertyName The class property name
     * @return Class of Get/Set method
     */
    public static NestedSetter create(Class<?> clazz, String propertyName) {
        return getSetterOrNull(clazz, propertyName);
    }

    private static NestedSetter getSetterOrNull(Class<?> clazz, String propertyName) {
        if (Validators.isNullOne(clazz, propertyName) || clazz == Object.class) {
            return null;
        }

        String[] propertyParts = ReflectionUtils.getPropertyParts(propertyName);
        int nestedCount = propertyParts.length;

        Method[] getMethods = new Method[nestedCount - 1];
        Method[] setMethods = new Method[nestedCount - 1];
        Class<?> currentClass = clazz;
        for (int i = 0; i < nestedCount - 1; i++) {
            String property = JdbcUtils.toPropertyName(propertyParts[i]);
            Method getter = ReflectionUtils.getClassGetter(currentClass, property);
            if (getter == null) {
                return null;
            }

            getMethods[i] = getter;
            setMethods[i] = ReflectionUtils.getClassSetter(currentClass, property, getter);
            currentClass = getMethods[i].getReturnType();
        }

        String property = JdbcUtils.toPropertyName(propertyParts[nestedCount - 1]);
        Method method = setterMethod(currentClass, property);
        if (method != null) {
            method.setAccessible(true);
            return new NestedSetter(getMethods, setMethods, method, JdbcUtils.toNestedPropertyName(propertyName));
        }

        NestedSetter setter = getSetterOrNull(clazz.getSuperclass(), propertyName);
        if (setter == null) {
            Class<?>[] interfaces = clazz.getInterfaces();
            for (int i = 0; setter == null && i < interfaces.length; i++) {
                setter = getSetterOrNull(interfaces[i], propertyName);
            }
        }

        return setter;
    }

    private void validatePrimitive(Object value) {
        if (value == null && method.getParameterTypes()[0].isPrimitive()) {
            throw new PropertyAccessException("Value is null, but property type is primitive.");
        }
    }

    private void invokeSet(Object target, Object value) throws SystemException {
        try {
            Object tmpTarget = target;
            for (int i = 0; i < getMethods.length; i++) {
                Object tmpTarget2 = getMethods[i].invoke(tmpTarget);
                if (tmpTarget2 == null) {
                    tmpTarget2 = ClassUtils.newInstance(getMethods[i].getReturnType());
                    setMethods[i].invoke(tmpTarget, tmpTarget2);
                }
                tmpTarget = tmpTarget2;
            }

            method.invoke(tmpTarget, value);
        } catch (Exception ex) {
            throw new SystemException(ex);
        }
    }

    private static Method setterMethod(Class<?> clazz, String propertyName) {
        Method getter = ReflectionUtils.getClassGetter(clazz, propertyName);
        return ReflectionUtils.getClassSetter(clazz, propertyName, getter);
    }
}