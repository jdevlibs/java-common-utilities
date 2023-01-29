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

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.github.jdevlibs.utils.exception.SystemException;

/**
 * <p>
 * Utility class that uses {@code java.lang.reflect} standard library. It
 * provides easy access to the standard reflect methods that are needed usually
 * when dealing with generic object types.
 * </p>
 * 
 * @author Qussay Najjar
 * 
 * @version 1.0
 * @since 2013-09-27
 * 
 * @version 1.1
 * @since 2014-04-13
 */
public final class ReflectionUtils {
	
	/**
	 * When {@code Type} initialized with a value of an object, its fully
	 * qualified class name will be prefixed with this.
	 */
	private static final String TYPE_CLASS_NAME_PREFIX = "class ";
	private static final String TYPE_INTERFACE_NAME_PREFIX = "interface ";
	public static final Class<?>[] NO_PARAM_SIGNATURE = new Class<?>[0];

	private ReflectionUtils() {

	}

	/**
	 * Try to find a getter method by a property name. Check parent classes and
	 * interfaces.
	 *
	 * @param rootClass a root class form, which begin to find a getter
	 * @param propertyName a property name
	 * @return getter method or null, if such a getter is not exist
	 */
	public static Method findGetterMethod(Class<?> rootClass, String propertyName) {
		Class<?> checkClass = rootClass;
		Method result = null;

		// check containerClass, and then its super types (if any)
		while (result == null && checkClass != null) {
			if (checkClass.equals(Object.class)) {
				break;
			}

			result = getClassGetter(checkClass, propertyName);
			checkClass = checkClass.getSuperclass();
		}

		// if no getter found yet, check all implemented interfaces
		if (result == null && rootClass != null) {
			for (Class<?> theInterface : rootClass.getInterfaces()) {
				result = getClassGetter(theInterface, propertyName);
				if (result != null) {
					break;
				}
			}
		}

		return result;
	}

	/**
	 * Try to find a class getter method by a property name. Don't check parent
	 * classes or interfaces.
	 *
	 * @param classToCheck a class in which find a getter
	 * @param propertyName a property name
	 * @return the getter method or null, if such a getter is not exist.
	 */
	public static Method getClassGetter(Class<?> classToCheck, String propertyName) {
		PropertyDescriptor[] descriptors = getPropertyDescriptors(classToCheck);
		for (PropertyDescriptor descriptor : descriptors) {
			if (isGetter(descriptor, propertyName)) {
				return descriptor.getReadMethod();
			}
		}

		return null;
	}

	private static boolean isGetter(PropertyDescriptor descriptor, String propertyName) {
		Method method = descriptor.getReadMethod();
		return method != null && method.getParameterTypes().length == 0
				&& descriptor.getName().equalsIgnoreCase(propertyName);
	}

	/**
	 * Try to find a class setter method by a property name. Don't check parent
	 * classes or interfaces.
	 *
	 * @param classToCheck a class in which find a setter
	 * @param propertyName a property name
	 * @param getterMethod a getter method for getting a type of property
	 *
	 * @return the setter method or null, if such a setter is not exist.
	 */
	public static Method getClassSetter(Class<?> classToCheck, String propertyName, Method getterMethod) {
		return getClassSetter(classToCheck, propertyName, getterMethod == null ? null : getterMethod.getReturnType());
	}

	/**
	 * Try to find a class setter method by a property name. Don't check parent
	 * classes or interfaces.
	 *
	 * @param classToCheck a class in which find a setter
	 * @param propertyName a property name
	 * @param propertyType a type of property
	 *
	 * @return the setter method or null, if such a setter is not exist.
	 */
	public static Method getClassSetter(Class<?> classToCheck, String propertyName, Class<?> propertyType) {
		PropertyDescriptor[] descriptors = getPropertyDescriptors(classToCheck);
		for (PropertyDescriptor descriptor : descriptors) {
			if (isSetter(descriptor, propertyName, propertyType)) {
				return descriptor.getWriteMethod();
			}
		}

		return null;
	}

	private static boolean isSetter(PropertyDescriptor descriptor, String propertyName, Class<?> propertyType) {
		Method method = descriptor.getWriteMethod();

		return method != null && method.getParameterTypes().length == 1 && method.getName().startsWith("set")
				&& descriptor.getName().equalsIgnoreCase(propertyName)
				&& (propertyType == null || method.getParameterTypes()[0].equals(propertyType));
	}

	private static PropertyDescriptor[] getPropertyDescriptors(Class<?> beanClass) {
		try {
			return Introspector.getBeanInfo(beanClass).getPropertyDescriptors();
		} catch (IntrospectionException ex) {
			throw Exceptions.toRuntimeException(ex);
		}
	}

	public static Field findField(Class<?> classToCheck, String propertyName) {
		if (classToCheck == null || Object.class.equals(classToCheck)) {
			return null;
		}

		try {
			return classToCheck.getDeclaredField(propertyName);
		} catch (NoSuchFieldException ex) {
			return findField(classToCheck.getSuperclass(), propertyName);
		}
	}

	/**
	 * Get [private, protected with not static] declared field of class
	 * @param clazz The class
	 * @param ignores ignores properties
	 * @return Collection of declared field of class
	 */
	public static List<Field> getDeclaredFields(Class<?> clazz, String... ignores) {
		if (ignores != null && ignores.length > 0) {
			return getDeclaredFields(clazz, Arrays.asList(ignores));
		}
		return getDeclaredFields(clazz, Collections.emptyList());
	}

	/**
	 * Get [private, protected with not static] declared field of class
	 * @param clazz The class
	 * @param ignores ignores properties
	 * @return Collection of declared field of class
	 */
	public static List<Field> getDeclaredFields(Class<?> clazz, final List<String> ignores) {
		if (clazz == null) {
			return new ArrayList<>();
		}

		List<Field> fields = new ArrayList<>(getDeclaredFields(clazz.getSuperclass(), ignores));
		List<Field> filteredFields = Arrays.stream(clazz.getDeclaredFields())
				.filter(f -> (Modifier.isPrivate(f.getModifiers()) || Modifier.isProtected(f.getModifiers())) && !Modifier.isStatic(f.getModifiers()))
				.filter(f -> (Validators.isEmpty(ignores) || !ignores.contains(f.getName())))
				.collect(Collectors.toList());
		fields.addAll(filteredFields);
		return fields;
	}

	/**
	 * Get [private, protected with not static] declared field of class
	 * @param clazz The class
	 * @param ignores ignores properties
	 * @return Map of declared field of class
	 */
	public static Map<String, Field> getDeclaredFieldsAsMap(Class<?> clazz, String... ignores) {
		if (ignores != null && ignores.length > 0) {
			return getDeclaredFieldsAsMap(clazz, Arrays.asList(ignores));
		}
		return getDeclaredFieldsAsMap(clazz, Collections.emptyList());
	}

	/**
	 * Get [private, protected with not static] declared field of class
	 * @param clazz The class
	 * @param ignores ignores properties
	 * @return Map of declared field of class
	 */
	public static Map<String, Field> getDeclaredFieldsAsMap(Class<?> clazz, final List<String> ignores) {
		List<Field> fields = getDeclaredFields(clazz, ignores);
		if (Validators.isEmpty(fields)) {
			return Collections.emptyMap();
		}

		return fields.stream().collect(Collectors.toMap(Field::getName, Function.identity()));
	}

	/**
	 * Get [private, protected with not static] declared field of class
	 * @param clazz The class
	 * @param ignores ignores properties
	 * @return Collection of declared field of class
	 */
	public static List<Field> getAllDeclaredFields(Class<?> clazz, String... ignores) {
		if (ignores != null && ignores.length > 0) {
			return getAllDeclaredFields(clazz, Arrays.asList(ignores));
		}
		return getAllDeclaredFields(clazz, Collections.emptyList());
	}

	/**
	 * Get [private, protected with not static] declared field of class
	 * @param clazz The class
	 * @param ignores ignores properties
	 * @return Collection of declared field of class
	 */
	public static List<Field> getAllDeclaredFields(Class<?> clazz, final List<String> ignores) {
		if (clazz == null) {
			return new ArrayList<>();
		}

		List<Field> fields = new ArrayList<>(getAllDeclaredFields(clazz.getSuperclass()));
		List<Field> filteredFields = Arrays.stream(clazz.getDeclaredFields())
				.filter(f -> (Validators.isEmpty(ignores) || !ignores.contains(f.getName())))
				.collect(Collectors.toList());
		fields.addAll(filteredFields);
		return fields;
	}

	/**
	 * Get [private, protected with not static] declared field of class
	 * @param clazz The class
	 * @param ignores ignores properties
	 * @return Map of declared field of class
	 */
	public static Map<String, Field> getAllDeclaredFieldsAsMap(Class<?> clazz, String... ignores) {
		if (ignores != null && ignores.length > 0) {
			return getAllDeclaredFieldsAsMap(clazz, Arrays.asList(ignores));
		}
		return getAllDeclaredFieldsAsMap(clazz, Collections.emptyList());
	}

	/**
	 * Get [private, protected with not static] declared field of class
	 * @param clazz The class
	 * @param ignores ignores properties
	 * @return Map of declared field of class
	 */
	public static Map<String, Field> getAllDeclaredFieldsAsMap(Class<?> clazz, final List<String> ignores) {
		List<Field> fields = getAllDeclaredFields(clazz, ignores);
		if (Validators.isEmpty(fields)) {
			return Collections.emptyMap();
		}

		return fields.stream().collect(Collectors.toMap(Field::getName, Function.identity()));
	}

	public static String[] getPropertyParts(String property) {
		return splitByDot(property);
	}

	public static <T extends Annotation> T getAnnotation(Class<?> classToCheck, String propertyName,
			Class<T> annotationClass) {
		T result = getAnnotation(findGetterMethod(classToCheck, propertyName), annotationClass);
		return result == null ? getAnnotation(findField(classToCheck, propertyName), annotationClass) : result;
	}

	private static <T extends Annotation> T getAnnotation(AccessibleObject accessibleObject, Class<T> annotationClass) {
		return accessibleObject == null ? null : accessibleObject.getAnnotation(annotationClass);
	}

	public static Method extractMethod(Class<?> clazz, String methodName) throws SystemException {
		try {
			return clazz.getMethod(methodName, NO_PARAM_SIGNATURE);
		} catch (NoSuchMethodException | SecurityException ex) {
			throw new SystemException(ex);
		}
	}

	public static Method extractMethod(Class<?> clazz, String methodName, Class<?> paramType) throws SystemException {

		try {
			if (Validators.isNull(paramType)) {
				return clazz.getMethod(methodName);
			}
			return clazz.getMethod(methodName, paramType);
		} catch (NoSuchMethodException | SecurityException ex) {
			throw new SystemException(ex);
		}
	}

	public static <T> T invoke(Object obj, Method method) throws SystemException {
		return invoke(obj, method, null);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T invoke(Object obj, Method method, Object param) throws SystemException {
		try {
			if (Validators.isNull(param)) {
				return (T) method.invoke(obj);
			}
			return (T) method.invoke(obj, param);
		} catch (Exception ex) {
			throw new SystemException(ex);
		}
	}

    public static String[] splitByDot(String value) {
        return split(value, "\\.");
    }

    public static String[] splitBySpace(String value) {
        return split(value, "\\s+");
    }
    
    public static String[] split(String value, String regExp) {
        String result = value == null ? "" : value.trim();
        return result.length() == 0 ? new String[0] : result.split(regExp);
    }

	public static String getClassName(Type type) {
		if (Validators.isNull(type)) {
			return "";
		}
		
		String className = type.toString();
		if (className.startsWith(TYPE_CLASS_NAME_PREFIX)) {
			className = className.substring(TYPE_CLASS_NAME_PREFIX.length());
		} else if (className.startsWith(TYPE_INTERFACE_NAME_PREFIX)) {
			className = className.substring(TYPE_INTERFACE_NAME_PREFIX.length());
		}
		return className;
	}

	public static Class<?> getClass(Type type) throws ClassNotFoundException {
		String className = getClassName(type);
		if (Validators.isEmpty(className)) {
			return null;
		}
		return Class.forName(className);
	}

	/**
	 * Creates a new instance of the class represented by this {@code Type}
	 * object.
	 * @param type
	 *            the {@code Type} object whose its representing {@code Class}
	 *            object will be instantiated.
	 * @return a newly allocated instance of the class
	 */
	public static Object newInstance(Type type) {
		try {
			Class<?> clazz = getClass(type);
			if (clazz == null) {
				return null;
			}
			return  clazz.getDeclaredConstructor().newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException |
				 NoSuchMethodException ex) {
			return null;
		}
	}

	/**
	 * Returns an array of {@code Type} objects representing the actual type
	 * arguments to this object. If the returned value is null, then this object
	 * represents a non-parameterized object.
	 * @param obj
	 *            the {@code object} whose type arguments are needed.
	 * @return an array of {@code Type} objects representing the actual type
	 *         arguments to this object.
	 */
	public static Type[] getParameterizedTypes(Object obj) {
		if (Validators.isNull(obj)) {
			return new Type[] {};
		}

		Type superclassType = obj.getClass().getGenericSuperclass();
		if (!ParameterizedType.class.isAssignableFrom(superclassType.getClass())) {
			return new Type[] {};
		}

		return ((ParameterizedType) superclassType).getActualTypeArguments();
	}

	public static boolean hasDefaultConstructor(Class<?> clazz) {
		if (Validators.isNull(clazz)) {
			return false;
		}
		try {
			Class<?>[] empty = {};
			clazz.getConstructor(empty);
		} catch (NoSuchMethodException e) {
			return false;
		}
		return true;
	}

	public static Class<?> getFieldClass(Class<?> clazz, String name) {
		if (Validators.isNullOne(clazz, name)) {
			return null;
		}
		
		Class<?> propertyClass = null;
		for (Field field : clazz.getDeclaredFields()) {
			field.setAccessible(true);
			if (field.getName().equalsIgnoreCase(name)) {
				propertyClass = field.getType();
				break;
			}
		}

		return propertyClass;
	}

	public static Class<?> getMethodReturnType(Class<?> clazz, String name) {
		if (Validators.isNullOne(clazz, name)) {
			return null;
		}

		name = name.toLowerCase();
		Class<?> returnType = null;
		for (Method method : clazz.getDeclaredMethods()) {
			if (method.getName().equals(name)) {
				returnType = method.getReturnType();
				break;
			}
		}

		return returnType;
	}
}
