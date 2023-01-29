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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Supot Saelao
 * @version 1.0
 */
public class ClassUtils {
	private static final char PACKAGE_SEP		= '.';
	private static final char INNER_CLASS_SEP 	= '$';
	private static final List<Class<?>> NUMBER_CLASS = Arrays.asList(int.class, long.class, double.class, float.class,
			short.class, Integer.class, Long.class, Double.class, Float.class, BigInteger.class, BigDecimal.class, Byte.class);
	private static final Map<Class<?>, Class<?>> WRAPPER_CLASS;
	
	static {
		WRAPPER_CLASS = new HashMap<>(8);
		WRAPPER_CLASS.put(int.class, Integer.class);
		WRAPPER_CLASS.put(long.class, Long.class);
		WRAPPER_CLASS.put(double.class, Double.class);
		WRAPPER_CLASS.put(float.class, Float.class);
		WRAPPER_CLASS.put(short.class, Short.class);
		WRAPPER_CLASS.put(byte.class, Byte.class);
		WRAPPER_CLASS.put(boolean.class, Boolean.class);
		WRAPPER_CLASS.put(char.class, Character.class);
	}
	
	private ClassUtils() {

	}

	public static boolean existsClass(String className) {
		try {
			contextClassLoader().loadClass(className);
			return true;
		} catch (Exception ex) {
			//Ignore error
		}
		
		return false;
	}
	
	public static String getShortName(String className) {
		return getLastPart(getLastPart(className, PACKAGE_SEP), INNER_CLASS_SEP);
	}

	public static <T> T newInstance(Class<T> clazz) {
		if (Validators.isNull(clazz)) {
			throw Exceptions.toRuntimeException("Specified class is null.");
		}
		if (clazz.isInterface()) {
			throw Exceptions.toRuntimeException("Specified class is an interface");
		}

		try {
			return  clazz.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassCastException | NoSuchMethodException |
				 InvocationTargetException ex) {
			String err = String.format("Could not instantiate a class: %s", clazz.getName());
			throw Exceptions.toRuntimeException(err, ex);
		}
	}

	public static Class<?> classForNameFromContext(String className) {
		return classForName(className, contextClassLoader());
	}

	public static Class<?> classForName(String className, ClassLoader loader) {
		try {
			return Class.forName(className, true, loader);
		} catch (ClassNotFoundException ex) {
			throw Exceptions.toRuntimeException(ex);
		}
	}

	public static ClassLoader contextClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	public static ClassLoader staticClassLoader() {
		return ClassUtils.class.getClassLoader();
	}

    public static String getLastPart(String value, char separator) {
		if (Validators.isNull(value)) {
			return null;
		}
		
        int index = value.lastIndexOf(separator);
        return (index < 0 ? value : value.substring(index + 1));
    }
    
    public static Class<?> getParameterType(Method method) {
    	return getParameterType(method, 1);
    }
    
	public static Class<?> getParameterType(Method method, int position) {
		if (Validators.isNull(method)) {
			return null;
		}

		Class<?>[] paramTypes = method.getParameterTypes();
		if (paramTypes.length == 0 || paramTypes.length > position) {
			return null;
		}

		int inx = Math.max(position - 1, 0);
		return paramTypes[inx];
	}
    
    public static List<Class<?>> getNumberClass() {
    	return Collections.unmodifiableList(NUMBER_CLASS);
    }
    
	public static Class<?> getWrapper(Class<?> clazz) {
		if (Validators.isNull(clazz)) {
			return null;
		}
		
		Class<?> wrapClazz = WRAPPER_CLASS.get(clazz);
		if (wrapClazz != null) {
			return wrapClazz;
		}
		return clazz;
	}
    
	public static Class<?> getPrimitive(Class<?> clazz) {
		if (Validators.isNull(clazz)) {
			return null;
		}

		for (Map.Entry<Class<?>, Class<?>> map : WRAPPER_CLASS.entrySet()) {
			if (clazz.equals(map.getValue())) {
				return map.getKey();
			}
		}

		return null;
	}
    
	public static Map<String, Field> getDeclaredFields(Object obj) {
		if (Validators.isNull(obj)) {
			return Collections.emptyMap();
		}
		return getDeclaredFields(obj.getClass());
	}
	
	public static Map<String, Field> getDeclaredFields(Class<?> clazz) {
		Map<String, Field> mapFields = new LinkedHashMap<>();
		while (clazz != null) {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				if (Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
					continue;
				}
				mapFields.put(field.getName(), field);
			}
			clazz = clazz.getSuperclass();
		}
		return mapFields;
	}
	
	public static Object getValue(Object obj, Field field) {
		try {
			if (Validators.isNullOne(obj, field)) {
				return null;
			}
			
			field.setAccessible(true);
			return field.get(obj);
		} catch (IllegalArgumentException | IllegalAccessException ex) {
			//Ignore error
		}

		return null;
	}
	
	public static void setValue(Object target, Field field, Object value) {
		try {
			if (Validators.isNullOne(target, field)) {
				return;
			}
			field.setAccessible(true);
			field.set(target, value);
		} catch (IllegalArgumentException | IllegalAccessException ex) {
			//Ignore error
		}
	}
	
    public static boolean isString(Class<?> clazzType) {
		return (Validators.isNotNull(clazzType) && clazzType.equals(String.class));
	}
    
    public static boolean isNumber(Class<?> clazz) {
		return NUMBER_CLASS.contains(clazz);
	}
}
