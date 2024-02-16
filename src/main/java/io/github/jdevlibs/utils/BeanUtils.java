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
package io.github.jdevlibs.utils;

import io.github.jdevlibs.utils.bean.CopyBean;
import io.github.jdevlibs.utils.bean.CopyBeanOptions;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author supot.jdev
 * @version 1.0
 */
public final class BeanUtils {
    private BeanUtils() {

    }

    /**
     * Copy a collection of objects to a new collection object
     * @param sources The List of source objects
     * @param targetClass Target copy class
     * @param ignores Ignore properties for copy
     * @return Collection of copy objects
     * @param <T> Generic target class
     */
    public static  <T> List<T> copyProperties(List<?> sources, Class<T> targetClass, String... ignores) {
        if (Validators.isEmptyOne(sources, targetClass)) {
            return new ArrayList<>(0);
        }

        CopyBeanOptions options = CopyBeanOptions.defaultOptions();
        options.sourceIgnores(ignores);
        return copyProperties(sources, targetClass, options);
    }

    /**
     * Copy a collection of objects to a new collection object
     * @param sources The List of source objects
     * @param targetClass Target copy class
     * @param options copy options
     * @return Collection of copy objects
     * @param <T> Generic target class
     */
    public static  <T> List<T> copyProperties(List<?> sources, Class<T> targetClass, CopyBeanOptions options) {
        if (Validators.isEmptyOne(sources, targetClass)) {
            return new ArrayList<>(0);
        }

        CopyBean<T> copyBean = new CopyBean<>(targetClass, options);
        return copyBean.copyProperties(sources);
    }

    /**
     * Copy source object to new target class
     * @param source The source object
     * @param targetClass Target copy class
     * @param ignores Ignore properties for copy
     * @return The target copy object
     * @param <T> Generic target class
     */
    public static <T> T copyProperties(Object source, Class<T> targetClass, String... ignores) {
        CopyBeanOptions options = CopyBeanOptions.defaultOptions();
        options.sourceIgnores(ignores);
        return copyProperties(source, targetClass, options);
    }

    /**
     * Copy source object to new target class
     * @param source The source object
     * @param targetClass Target copy class
     * @param options copy options
     * @return The target copy object
     * @param <T> Generic target class
     */
    public static <T> T copyProperties(Object source, Class<T> targetClass, CopyBeanOptions options) {
        if (Validators.isNullOne(source, targetClass)) {
            return null;
        }

        CopyBean<T> copyBean = new CopyBean<>(targetClass, options);
        return copyBean.copyProperties(source);
    }

    /**
     * Returns the value of a field on a bean.
     * @param bean The object to lookup value
     * @param field The field name of bean
     * @return The field value if found, otherwise return null
     * @param <T> Generic data type
     */
    @SuppressWarnings("unchecked")
    public static <T> T getValueAsType(Object bean, Field field) {
        try {
            Object value = getValue(bean, field);
            return (T) value;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Returns the value of a (nested) field on a bean.
     * @param bean The object to lookup value
     * @param fieldName The field name of bean, [Simple: (name), Nested: (name1.name2.name3)]
     * @return The field value if found, otherwise return null
     * @param <T> Generic data type
     */
    @SuppressWarnings("unchecked")
    public static <T> T getValueAsType(Object bean, String fieldName) {
        try {
            Object value = getValue(bean, fieldName);
            return (T) value;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Returns the value of a field on a bean.
     * @param bean The object to lookup value
     * @param field The field name of bean
     * @return The field value if found, otherwise return null
     */
    public static Object getValue(Object bean, Field field) {
        if (Validators.isNullOne(bean, field)) {
            return null;
        }

        Object value = null;
        try {
            field.setAccessible(true);
            value = field.get(bean);
            field.setAccessible(false);
        } catch (IllegalAccessException ex) {
            //Skip
        }

        return value;
    }

    /**
     * Returns the value of a (nested) field on a bean.
     * @param bean The object to lookup value
     * @param fieldName The field name of bean, [Simple: (name), Nested: (name1.name2.name3)]
     * @return The field value if found, otherwise return null
     */
    public static Object getValue(Object bean, String fieldName) {
        if (Validators.isEmptyOne(bean, fieldName)) {
            return null;
        }

        try {
            String[] names = fieldName.split("[.]");
            Class<?> clazz = bean.getClass();
            Object value = null;
            for (String name : names) {
                Field field = getField(clazz, name);
                if (field == null) {
                    return null;
                }

                field.setAccessible(true);
                value = field.get(bean);
                if (value != null) {
                    clazz = value.getClass();
                    bean = value;
                }
            }

            return value;
        } catch (IllegalAccessException ex) {
            //Skip
        }

        return null;
    }

    /**
     * Sets the value of a field on a bean.
     * @param bean the object
     * @param field the field name
     * @param value the value to set
     */
    public static void setValue(Object bean, Field field, Object value) {
        if (Validators.isNullOne(bean, field)) {
            return;
        }
        setFieldValue(bean, field, value);
    }

    /**
     * Sets the value of a (nested) field on a bean.
     * @param bean the object
     * @param fieldName the field name, with '.' separating nested properties
     * @param value the value to set
     */
    public static void setValue(Object bean, String fieldName, Object value) {
        if (Validators.isEmptyOne(bean, fieldName)) {
            return;
        }

        try {
            if (fieldName.contains(".")) {
                int index = fieldName.indexOf('.');
                String parentFieldName = fieldName.substring(0, index);
                Field field = getField(bean.getClass(), parentFieldName);
                if (field == null) {
                    return;
                }

                field.setAccessible(true);
                Object fieldValue = field.get(bean);
                if (fieldValue == null) {
                    Class<?> type = field.getType();
                    fieldValue = type.getConstructor().newInstance();
                    field.set(bean, fieldValue);
                }
                field.setAccessible(false);

                String nextFieldName = fieldName.substring(index + 1);
                setValue(fieldValue, nextFieldName, value);
            } else {
                Field field = getField(bean.getClass(), fieldName);
                setFieldValue(bean, field, value);
            }
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException ex) {
            //Skip
        }
    }

    /**
     * Get the specified field on the class/superclass.
     * @param clazz The class definition.
     * @param name The field to lookup
     * @return The field if found, otherwise return null
     */
    public static Field getField(Class<?> clazz, String name) {
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException ex) {
            if (clazz.getSuperclass() != null) {
                return getField(clazz.getSuperclass(), name);
            }
        }
        return null;
    }

    private static void setFieldValue(Object bean, Field field, Object value) {
        try {
            if (value == null && field.getType().isPrimitive()) {
                return;
            }

            if (field != null) {
                field.setAccessible(true);
                field.set(bean, Convertors.convertWithType(field.getType(), value));
                field.setAccessible(false);
            }
        } catch (IllegalAccessException ex) {
            //Skip
        }
    }

}