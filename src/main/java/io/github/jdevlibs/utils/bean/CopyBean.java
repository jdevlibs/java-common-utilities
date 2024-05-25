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

import io.github.jdevlibs.utils.BeanUtils;
import io.github.jdevlibs.utils.ClassUtils;
import io.github.jdevlibs.utils.ReflectionUtils;
import io.github.jdevlibs.utils.Validators;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author supot.jdev
 * @version 1.0
 */
public class CopyBean<T> {
    private final CopyBeanOptions options;
    private List<Field> targetFields;
    private Map<String, Field> sourceFields;
    private String sourceClass;
    private Class<T> clazz;

    public CopyBean() {
        this.options = CopyBeanOptions.defaultOptions();
    }

    public CopyBean(CopyBeanOptions options) {
        if (options == null) {
            options = CopyBeanOptions.defaultOptions();
        }
        this.options = options;
    }

    public CopyBean(Class<T> targetClass) {
        this.clazz = targetClass;
        this.options = CopyBeanOptions.defaultOptions();
    }

    public CopyBean(Class<T> clazz, CopyBeanOptions options) {
        if (options == null) {
            options = CopyBeanOptions.defaultOptions();
        }
        this.clazz = clazz;
        this.options = options;
    }

    public List<T> copyProperties(List<?> sources, String... ignores) {
        this.initialTargetField();
        if (Validators.isEmptyOne(sources, targetFields)) {
            return null;
        }

        this.initialSourceFields(sources, ignores);
        if (Validators.isEmpty(sourceFields)) {
            return null;
        }

        List<T> items = new ArrayList<>();
        for (Object source : sources) {
            if (source == null) {
                continue;
            }
            T result = copy(source);
            items.add(result);
        }

        return items;
    }

    public T copyProperties(Object source, String... ignores) {
        this.initialTargetField();
        if (Validators.isEmptyOne(source, targetFields)) {
            return null;
        }

        this.initialSourceFields(source, ignores);
        if (Validators.isEmpty(sourceFields)) {
            return null;
        }

        return copy(source);
    }

    private T copy(Object source) {
        T result = ClassUtils.newInstance(clazz);
        copy(source, result);
        return result;
    }

    public void copyProperties(Object source, Object target, String... ignores) {
        this.initialTargetField(target);
        if (Validators.isEmptyOne(source, target, targetFields)) {
            return;
        }

        this.initialSourceFields(source, ignores);
        if (Validators.isEmpty(sourceFields)) {
            return;
        }

        copy(source, target);
    }

    private void copy(Object source, Object target) {
        for (Field field : targetFields) {
            Field sourceField = sourceFields.get(field.getName());
            if (sourceField == null) {
                continue;
            }
            Object value = BeanUtils.getValue(source, sourceField);
            if (value != null) {
                BeanUtils.setValue(target, field, value);
            }
        }
    }

    private void initialTargetField() {
        if (targetFields == null && clazz != null) {
            targetFields = ReflectionUtils.getDeclaredFields(clazz, options.getTargetIgnores());
        }
    }

    private void initialTargetField(Object target) {
        if (targetFields == null && target != null) {
            targetFields = ReflectionUtils.getDeclaredFields(target.getClass(), options.getTargetIgnores());
        }
    }

    private void initialSourceFields(List<?> objects, String... ignores) {
        Object source = objects.stream()
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
        initialSourceFields(source, ignores);
    }

    private void initialSourceFields(Object source, String... ignores) {
        if (source == null || source.getClass().getName().equals(sourceClass)) {
            return;
        }

        if (ignores != null && ignores.length > 0) {
            options.resetSourceIgnores();
            options.sourceIgnores(ignores);
        }

        sourceClass = source.getClass().getName();
        sourceFields = ReflectionUtils.getDeclaredFieldsAsMap(source.getClass(), options.getSourceIgnores());
    }
}
