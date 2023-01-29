package io.github.jdevlibs.main;

import io.github.jdevlibs.utils.ReflectionUtils;
import io.github.jdevlibs.model.Dog;
import io.github.jdevlibs.model.Horse;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author supot.jdev
 * @version 1.0
 */
public class ReflectionUtilsTest {
    public static void main(String[] args) {
        System.out.println("+++++++++++++ Only protected/private ++++++++++++++");
        List<Field> fields = ReflectionUtils.getDeclaredFields(Dog.class);
        for (Field field : fields) {
            System.out.println(field.getName() + ": " + field.getType());
        }

        System.out.println("+++++++++++++ ALL ++++++++++++++");
        fields = ReflectionUtils.getAllDeclaredFields(Dog.class);
        for (Field field : fields) {
            System.out.println(field.getName() + ": " + field.getType());
        }

        System.out.println("+++++++++++++ Skip ++++++++++++++");
        fields = ReflectionUtils.getAllDeclaredFields(Horse.class, "speed", "price");
        for (Field field : fields) {
            System.out.println(field.getName() + ": " + field.getType());
        }
    }
}
