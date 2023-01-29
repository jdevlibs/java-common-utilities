package io.github.jdevlibs.main;

import io.github.jdevlibs.utils.ReflectionUtils;
import io.github.jdevlibs.utils.bean.NestedSetter;
import io.github.jdevlibs.model.Dog;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author supot.jdev
 * @version 1.0
 */
public class NestedSetterTest {
    public static void main(String[] args) {
        List<Field> fields = ReflectionUtils.getDeclaredFields(Dog.class);
        for (Field field : fields) {
            NestedSetter setter = NestedSetter.create(Dog.class, field.getName());
            System.out.println(setter.toString());
        }

    }
}
